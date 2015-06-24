'''
Created on Apr 28, 2015

@author: andreea
'''
from model.pool import Pool
from google.appengine.ext import ndb
from google.appengine.api import search
from model.user import User
from utils import constants
from controller.route_controller import add_route_to_pool

'''
    driver_socialID = ndb.StringProperty(required=True)
    passengers = ndb.IntegerProperty(repeated=True)
    source_point = ndb.GeoPtProperty(required=True)
    destination_point = ndb.GeoPtProperty(required=True)
    date = ndb.DateTimeProperty(required=True)
    seats = ndb.IntegerProperty(required=True)
    is_weekly = ndb.BooleanProperty(default=False)
'''

def create_pool_using_gps_route(p_driverID, p_route_id, p_date, p_seats):
    # create pool instance with route id added
    pool_key = Pool(driver_socialID = p_driverID, 
                     date = p_date,
                     seats=p_seats,
                     route_id=p_route_id).put()
         
    # add the route to pool
    add_route_to_pool(pool_key.id(), p_route_id)

def create_pool_using_start_and_end_location(p_driverID, p_source_point, p_destination_point,  p_date, p_seats):
    pool_key = Pool(driver_socialID = p_driverID, 
                         source_point = p_source_point,
                         destination_point = p_destination_point,
                         date = p_date,
                         seats=p_seats).put()
    source_point = search.GeoPoint(p_source_point.lat, p_source_point.lon)
    destination_point = search.GeoPoint(p_destination_point.lat, p_destination_point.lon)
    index = search.Index(constants.IndexName.LOCATION_INDEX)
    doc = search.Document(fields=[search.TextField(name='key', value=pool_key.urlsafe()),
                                  search.GeoField(name='s_point', value=source_point),
                                  search.GeoField(name='d_point', value=destination_point)])
    index.put(doc)
'''
p_source_point and p_destination_point should have the type ndb.GeoPt
p_date should have type datetime.datetime
'''
def create_pool(p_driverID, p_source_point, p_destination_point, p_route_id, p_date, p_seats, p_is_weekly=False):
    
    # check if user exists
    user = User.query(User.socialProfile.socialID == p_driverID).fetch(1)
    if (len(user) == 0):
        return constants.ExitCode.INVALID_USER
    
    if p_source_point is None or p_destination_point is None:
        if p_route_id is None:
            return constants.ExitCode.INVALID_POOL_PARAMETER
        else:
            create_pool_using_gps_route(p_driverID, p_route_id, p_date, p_seats)
            return constants.ExitCode.POOL_ADDED
               
    else:
        create_pool_using_start_and_end_location(p_driverID, p_source_point, p_destination_point, p_date, p_seats)
        return constants.ExitCode.POOL_ADDED

def add_passenger_to_pool(pool_id, passenger_socialId):
    
    pool = Pool.get_by_id(pool_id)
    if (pool is None):
        return constants.ExitCode.INVALID_POOL
    
    pool.add_passenger(passenger_socialId)
    pool.put()

    return constants.ExitCode.PASSENGER_ADDED_TO_POOL

def delete_pool(pool_id):
    pool = Pool.get_by_id(pool_id)
    
    if (pool is None):
        return constants.ExitCode.INVALID_POOL
    
    pool.key.delete()
    return constants.ExitCode.POOL_DELETED
    
def delete_passenger_from_pool(pool_id, passenger_id):
    pool = Pool.get_by_id(pool_id)
    
    if (pool is None):
        return constants.ExitCode.INVALID_POOL
    try:
        pool.passengers.remove(passenger_id)
    except ValueError:
        return constants.ExitCode.INVALID_USER
    
    return constants.ExitCode.PASSENGER_DELETED_FROM_POOL

def find_pool_using_start_and_end_points(socialID, start_point, end_point, date, delta, walking_distance=1000):
    index = search.Index(constants.IndexName.LOCATION_INDEX)
    query = "distance(s_point, geopoint(%f,%f)) < %f AND distance(d_point, geopoint(%f,%f)) < %f" % (
                start_point.lat, start_point.lon, walking_distance,
                end_point.lat, end_point.lon, walking_distance)
    expr = "distance(s_point, geopoint(%f,%f)) + distance(d_point, geopoint(%f,%f))" %(
                start_point.lat, start_point.lon,
                end_point.lat, end_point.lon)
    sortexpr = search.SortExpression(
                expression=expr,
                direction=search.SortExpression.ASCENDING, default_value=2*walking_distance+1)
    search_query = search.Query(
                query_string=query,
                options=search.QueryOptions(
                    sort_options=search.SortOptions(expressions=[sortexpr])))

    results = index.search(search_query)
    keys = [ndb.Key(urlsafe=r.field('key').value) for r in results]
    entities = ndb.get_multi(keys)
    ret = []
    for entity in entities:
        # TODO: check if the driverID != socialID -> driver cannot join his/her own pool
        if entity.driver_socialID != socialID and entity.date < date + delta and entity.date > date - delta:
            ret.append(entity)
    return ret

def find_pool_using_gps_routes(socialID, start_point, end_point, date, delta, walking_distance=1000):
    index = search.Index(constants.IndexName.ROUTE_INDEX)
    # TODO replace point with something relevant
    query = "distance(point, geopoint(%f,%f)) < %f AND distance(point, geopoint(%f,%f)) < %f" % (
                start_point.lat, start_point.lon, walking_distance,
                end_point.lat, end_point.lon, walking_distance)
    expr = "distance(point, geopoint(%f,%f)) + distance(point, geopoint(%f,%f))" %(
                start_point.lat, start_point.lon,
                end_point.lat, end_point.lon)
    sortexpr = search.SortExpression(
                expression=expr,
                direction=search.SortExpression.ASCENDING, default_value=2*walking_distance+1)
    search_query = search.Query(
                query_string=query,
                options=search.QueryOptions(
                    sort_options=search.SortOptions(expressions=[sortexpr])))

    results = index.search(search_query)
    
    # for each gps route returned find the corresponding route
    keys = [ndb.Key(urlsafe=r.field('key').value) for r in results]
    entities = ndb.get_multi(keys)
    ret = []
    for entity in entities:
        # get pool that corresponds to the route
        pool = Pool.get_by_id(entity.pool_id)
        if not pool is None:
            if pool.driver_socialID != socialID and pool.date < date + delta and pool.date > date - delta:
                ret.append(pool)
    return ret
'''
    start_point and end_point are GeoPt
    date is datetime.datetime
    delta is time interval around date in which to search
'''
def find_pool(socialID, start_point, end_point, date, delta, walking_distance=1000):
    
    # TODO: Cristina do some turkish delight with the socialID
    # !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
#     location_query =   Pool.query(ndb.AND(Pool.source_point == start_point, 
#                                Pool.destination_point == end_point))
#     
#     time_query = location_query.filter()
#     return time_query.fetch()
    results_by_points = find_pool_using_start_and_end_points(socialID, start_point, end_point, date, delta, walking_distance)
#     results_by_routes = []
#     results_by_routes = find_pool_using_gps_routes(socialID, start_point, end_point, date, delta, walking_distance)
#     return results_by_points.extend(results_by_routes)

    return results_by_points