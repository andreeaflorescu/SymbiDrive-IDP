'''
Created on Apr 28, 2015

@author: andreea
'''
from model.pool import Pool
from google.appengine.ext import ndb
from google.appengine.api import search
from model.user import User
from utils import constants
from controller.route_controller import add_route_to_pool, create_route
from model.gps_route import GPSRoute
from google.appengine.api.datastore_types import GeoPt
from controller.profiles_controller import get_user_info_facebook, match_users,\
    calculate_similatrity
from model.user_data import UserData

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
    pool_key = Pool(driver_socialID=p_driverID,
                     date=p_date,
                     seats=p_seats,
                     route_id=p_route_id).put()
         
    # add the route to pool
    add_route_to_pool(pool_key.id(), p_route_id)

def create_pool_using_start_and_end_location(p_driverID, p_source_point, p_destination_point, p_date, p_seats):
    pool_key = Pool(driver_socialID=p_driverID,
                         source_point=p_source_point,
                         destination_point=p_destination_point,
                         date=p_date,
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
            # check if the route_id is valid
            if (GPSRoute.get_by_id(p_route_id) is None):
                return constants.ExitCode.INVALID_POOL_PARAMETER
            create_pool_using_gps_route(p_driverID, p_route_id, p_date, p_seats)
            
#             store_data_from_facebook(p_driverID)
            
            return constants.ExitCode.POOL_ADDED
               
    else:
        create_pool_using_start_and_end_location(p_driverID, p_source_point, p_destination_point, p_date, p_seats)
        
#         store_data_from_facebook(p_driverID)
        
        return constants.ExitCode.POOL_ADDED

def store_data_from_facebook(p_driverID):
    facebook_data = get_user_info_facebook(p_driverID)
    UserData(p_driverID, facebook_data).put()

def add_passenger_to_pool(pool_id, passenger_socialId):
    
    pool = Pool.get_by_id(pool_id)
    if (pool is None):
        return constants.ExitCode.INVALID_POOL
    
    user = User.query(User.socialProfile.socialID == passenger_socialId).fetch(1);
    if (len(user) == 0):
        return constants.ExitCode.INVALID_USER
    
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
    expr = "distance(s_point, geopoint(%f,%f)) + distance(d_point, geopoint(%f,%f))" % (
                start_point.lat, start_point.lon,
                end_point.lat, end_point.lon)
    sortexpr = search.SortExpression(
                expression=expr,
                direction=search.SortExpression.ASCENDING, default_value=2 * walking_distance + 1)
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
    expr = "distance(point, geopoint(%f,%f)) + distance(point, geopoint(%f,%f))" % (
                start_point.lat, start_point.lon,
                end_point.lat, end_point.lon)
    sortexpr = search.SortExpression(
                expression=expr,
                direction=search.SortExpression.ASCENDING, default_value=2 * walking_distance + 1)
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
    
#     scores = match_results(socialID, results_by_points)
     
#     final_res = {}
#     final_res["scores"] = scores
#     final_res["pools"] = results_by_points
    
    return results_by_points

def get_pools(socialID):
    
    res = {}
    created_pools = Pool.query(Pool.driver_socialID == socialID).fetch(10)
    joined_pools = Pool.query(Pool.passengers == socialID).fetch(10)
    
    res["created"] = created_pools
    res["joined"] = joined_pools
    
    return res
    

def match_results(socialID, results_by_points):
    
    scores = {}
    
    try:
        passenger_set = get_user_info_facebook(socialID)
        for result in results_by_points:
            driver_set = UserData.query(UserData.token == result.driver_socialID).fetch(1)[0]
            scores[result.driver_socialID] = calculate_similatrity(passenger_set, driver_set)
    except:
        scores['error'] = 0.0
        pass  
    
    return scores

'''
Created on Apr 28, 2015

@author: andreea
'''
import unittest
from model.user import User, SocialIdentifier
from utils import constants
from google.appengine.ext import ndb, testbed
import datetime
from model.pool import Pool

class Test(unittest.TestCase):


    def setUp(self):
        # First, create an instance of the Testbed class.
        self.testbed = testbed.Testbed()
        # Then activate the testbed, which prepares the service stubs for use.
        self.testbed.activate()
        # Next, declare which service stubs you want to use.
        self.testbed.init_datastore_v3_stub()
        self.testbed.init_memcache_stub()
        self.testbed.init_search_stub()

    def tearDown(self):
        self.testbed.deactivate()

    def test_add_pool_simple(self):
        User(deviceID=["12345"],
             socialProfile=SocialIdentifier(socialID="32412", profile=constants.SocialProfile.FACEBOOK),
             username="Andreea").put()
        # p_driverID, p_source_point, p_destination_point, p_date, p_seats, p_is_weekly=False
        res = create_pool("32412",
                          ndb.GeoPt(-21, 32), ndb.GeoPt(32, 12),
                          None,
                          datetime.datetime.now(),
                          2)
        
        self.assertEqual(res, constants.ExitCode.POOL_ADDED, "Invalid exit code")
        
        pool = Pool.query(Pool.driver_socialID == "32412").fetch(1)
        
        self.assertEqual(len(pool), 1, "Pool was not saved")
        self.assertEqual(pool[0].source_point, ndb.GeoPt(-21, 32), "Invalid source point")
        self.assertEqual(pool[0].destination_point, ndb.GeoPt(32, 12), "Invalid destination point")
        self.assertEqual(len(pool[0].passengers), 0, "Invalid passengers list")
    
    def test_create_pool_invalid_user(self):
        res = create_pool("32412",
                          ndb.GeoPt(-21, 32), ndb.GeoPt(32, 12),
                          None,
                          datetime.datetime.now(),
                          2)
        self.assertEqual(res, constants.ExitCode.INVALID_USER, "Wrong error message")
        
    def test_create_pool_invalid_params(self):
        User(deviceID=["12345"],
             socialProfile=SocialIdentifier(socialID="32412", profile=constants.SocialProfile.FACEBOOK),
             username="Andreea").put()
        res = create_pool("32412",
                          None, None,
                          None,
                          datetime.datetime.now(),
                          2)
        self.assertEqual(res, constants.ExitCode.INVALID_POOL_PARAMETER, "Wrong error message")
    
    def test_create_pool_invalid_route_id(self):
        User(deviceID=["12345"],
             socialProfile=SocialIdentifier(socialID="32412", profile=constants.SocialProfile.FACEBOOK),
             username="Andreea").put()
        res = create_pool("32412",
                          None, None,
                          1,
                          datetime.datetime.now(),
                          2)
        self.assertEqual(res, constants.ExitCode.INVALID_POOL_PARAMETER, "Wrong error message")
    
    def test_create_pool_using_route(self):
        User(deviceID=["12345"],
             socialProfile=SocialIdentifier(socialID="32412", profile=constants.SocialProfile.FACEBOOK),
             username="Andreea").put()
        GPSRoute(name="route_name", driver_socialID="12345", route_points=[GeoPt(10.23, 11.23), GeoPt(21.21, 12.21)]).put()
        route_id = GPSRoute.query(GPSRoute.name == "route_name").fetch(1)[0].key.id();
        res = create_pool("32412",
                          None, None,
                          route_id,
                          datetime.datetime.now(),
                          2)
        self.assertEqual(res, constants.ExitCode.POOL_ADDED, "Wrong error message")
        
    def test_delete_pool(self):
        User(
             deviceID=["12345"],
             socialProfile=SocialIdentifier(socialID="32412", profile=constants.SocialProfile.FACEBOOK),
             username="Andreea").put()
        # p_driverID, p_source_point, p_destination_point, p_date, p_seats, p_is_weekly=False
        create_pool("32412",
                  ndb.GeoPt(-21, 32), ndb.GeoPt(32, 12),
                  None,
                  datetime.datetime.now(),
                  2)
        
        pool = Pool.query(Pool.driver_socialID == "32412").fetch(1)
        res = delete_pool(pool[0].key.id())
        self.assertEqual(res, constants.ExitCode.POOL_DELETED)
    
    def test_delete_passenger(self):
        User(
             deviceID=["12345"],
             socialProfile=SocialIdentifier(socialID="32412", profile=constants.SocialProfile.FACEBOOK),
             username="Andreea").put()
        
        User(deviceID=["13245"],
             socialProfile=SocialIdentifier(socialID="11412", profile=constants.SocialProfile.FACEBOOK),
             username="Maria").put()
        
        # p_driverID, p_source_point, p_destination_point, p_date, p_seats, p_is_weekly=False
        create_pool("32412",
                  ndb.GeoPt(-21, 32), ndb.GeoPt(32, 12),
                  None,
                  datetime.datetime.now(),
                  2)
        
        pool = Pool.query(Pool.driver_socialID == "32412").fetch(1)[0]
        pool.add_passenger("11412")
        
        res = delete_passenger_from_pool(pool.key.id(), "11412")
        self.assertEqual(res, constants.ExitCode.PASSENGER_DELETED_FROM_POOL, "Invalid exit code")
        
    def test_add_passenger_invalid_pool(self):
        res = add_passenger_to_pool("1234", "1234")
        self.assertEqual(res, constants.ExitCode.INVALID_POOL, "Invalid exit code")
        
    def test_add_passenger_invalid_user(self):
        # test setup
        User(
             deviceID=["12345"],
             socialProfile=SocialIdentifier(socialID="32412", profile=constants.SocialProfile.FACEBOOK),
             username="Andreea").put()
        create_pool("32412",
                  ndb.GeoPt(-21, 32), ndb.GeoPt(32, 12),
                  None,
                  datetime.datetime.now(),
                  2)
        pool_id = Pool.query(Pool.driver_socialID == "32412").fetch(1)[0].key.id()
        # call function for testing
        res = add_passenger_to_pool(pool_id, "1234")
        self.assertEqual(res, constants.ExitCode.INVALID_USER, "Invalid exit code")
    
    def test_add_passenger_correct_arguments(self):
        User(
             deviceID=["12345"],
             socialProfile=SocialIdentifier(socialID="32412", profile=constants.SocialProfile.FACEBOOK),
             username="Andreea").put()

        User(deviceID=["13245"],
             socialProfile=SocialIdentifier(socialID="11412", profile=constants.SocialProfile.FACEBOOK),
             username="Maria").put();
             
        create_pool("32412",
                  ndb.GeoPt(-21, 32), ndb.GeoPt(32, 12),
                  None,
                  datetime.datetime.now(),
                  2)
        pool_id = Pool.query(Pool.driver_socialID == "32412").fetch(1)[0].key.id()
        
        res = add_passenger_to_pool(pool_id, "11412")
        self.assertEqual(res, constants.ExitCode.PASSENGER_ADDED_TO_POOL, "Wrong exit code")
        
        pool = Pool.get_by_id(pool_id)
        self.assertEqual(pool.seats, 1, "The number of seats was not decremented")
        self.assertEqual(pool.passengers, ["11412"], "Passenger was not added")
        
            
        
    def test_find_pool(self):
        User(
             deviceID=["12345"],
             socialProfile=SocialIdentifier(socialID="32412", profile=constants.SocialProfile.FACEBOOK),
             username="Andreea").put()

        User(deviceID=["13245"],
             socialProfile=SocialIdentifier(socialID="11412", profile=constants.SocialProfile.FACEBOOK),
             username="Maria").put()

        date = datetime.datetime.now()
        # p_driverID, p_source_point, p_destination_point, p_date, p_seats, p_is_weekly=False
        create_pool("32412",
                  ndb.GeoPt(-21, 32), ndb.GeoPt(32, 12),
                  None,
                  date,
                  2)
        create_pool("11412",
                  ndb.GeoPt(-21, 32), ndb.GeoPt(31, 12),
                  None,
                  date,
                  2)
        create_pool("32412",
                  ndb.GeoPt(-21, 32), ndb.GeoPt(32, 12),
                  None,
                  date + datetime.timedelta(hours=7),
                  2)

        res = find_pool("32412", ndb.GeoPt(-21, 32), ndb.GeoPt(31, 12), date, datetime.timedelta(hours=6), 1000)
#         print res[0].key.id()
        self.assertEqual(len(res), 1, "Expected 1 returned value, got %d" % (len(res)))
    
    def test_view_pools(self):
        User(
             deviceID=["12345"],
             socialProfile=SocialIdentifier(socialID="32412", profile=constants.SocialProfile.FACEBOOK),
             username="Andreea").put()
             
        date = datetime.datetime.now()
        create_pool("32412",
                  ndb.GeoPt(-21, 32), ndb.GeoPt(32, 12),
                  None,
                  date,
                  2)
    
        res = get_pools("32412")
        print res
        
if __name__ == "__main__":
    # import sys;sys.argv = ['', 'Test.test_add_pool_simple']
    unittest.main()
