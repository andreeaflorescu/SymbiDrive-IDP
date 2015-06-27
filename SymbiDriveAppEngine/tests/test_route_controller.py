'''
Created on Jun 27, 2015

@author: andreea
'''
import unittest
from model.user import User, SocialIdentifier
from utils import constants
from google.appengine.ext import ndb, testbed
import datetime
from model.pool import Pool
from controller.route_controller import create_route
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