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
    return constants.ExitCode.ROUTE_ADDED

def add_route_to_pool(pool_id, route_id):
    route = GPSRoute.get_by_id(route_id)
    route.has_associated_pool = True
    route.pool_id = pool_id
    route.put()
    
    return constants.ExitCode.ROUTE_ADDED

def get_user_routes(p_driver_socialID):
    routes = GPSRoute.query(GPSRoute.driver_socialID == p_driver_socialID).fetch(50)
    return routes

import unittest
from model.user import User, SocialIdentifier
from utils import constants
from google.appengine.ext import ndb, testbed
import datetime
from model.pool import Pool
from google.appengine.api.datastore_types import GeoPt

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

    def test_add_route_invalid_user(self):
        res = create_route("p_name", "p_driver_socialID", [])
        self.assertEqual(res, constants.ExitCode.INVALID_USER, "Invalid exit code")
    
    def test_add_route_success(self):
        User(deviceID=["12345"], 
             socialProfile=SocialIdentifier(socialID="32412", profile=constants.SocialProfile.FACEBOOK),
             username="Andreea").put()
        ret = create_route("p_name", "32412", [GeoPt(10.23, 11.23), GeoPt(21.21, 12.21)])
        self.assertEqual(ret, constants.ExitCode.ROUTE_ADDED, "Invalid exit code")
        
if __name__ == "__main__":
    #import sys;sys.argv = ['', 'Test.testName']
    unittest.main()