'''
Created on Jun 24, 2015

@author: andreea
'''
from model.gps_route import GPSRoute
from model.user import User
from utils import constants
from google.appengine.api import search

def create_route(p_name, p_driver_socialID, p_route_points):
    user = User.query(User.socialProfile.socialID == p_driver_socialID).fetch(1)
    if (len(user) == 0):
        return constants.ExitCode.INVALID_USER
    
    route_key = GPSRoute(name=p_name, driver_socialID=p_driver_socialID, route_points=p_route_points).put()
    
    # add route to document index
    fields_arr=[search.TextField(name='key', value=route_key.urlsafe())]
    for route_point in p_route_points:
        # add point as document field
        search_point = search.GeoPoint(route_point.lat, route_point.lon)
        fields_arr.append(search.GeoField(name='point', value=search_point))
        
    index = search.Index(constants.IndexName.ROUTE_INDEX)
    doc = search.Document(fields=fields_arr)
    index.put(doc)

def add_route_to_pool(pool_id, route_id):
    route = GPSRoute.get_by_id(route_id)
    route.has_associated_pool = True
    route.pool_id = pool_id
    route.put()
    
    return constants.ExitCode.ROUTE_ADDED

def get_route_id(p_name):
    route = GPSRoute.query(GPSRoute.name == p_name)
    return route.key.id()
