'''
Created on Apr 28, 2015

@author: andreea
'''
import unittest
from model.user import User, SocialIdentifier
from utils import constants
from controller.pool_controller import create_pool, delete_pool,\
    delete_passenger_from_pool
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


    def tearDown(self):
        self.testbed.deactivate()

    def test_add_pool_simple(self):
        User(deviceID=["12345"], 
             socialProfile=SocialIdentifier(socialID="32412", profile=constants.SocialProfile.FACEBOOK),
             username="Andreea").put()
        # p_driverID, p_source_point, p_destination_point, p_date, p_seats, p_is_weekly=False
        res = create_pool("32412", 
                          ndb.GeoPt(-21, 32), ndb.GeoPt(32, 12), 
                          datetime.datetime.now(), 
                          2)
        
        self.assertEqual(res, constants.ExitCode.POOL_ADDED, "Invalid exit code")
        
        pool = Pool.query(Pool.driver_socialID == "32412").fetch(1)
        
        self.assertEqual(len(pool), 1, "Pool was not saved")
        self.assertEqual(pool[0].source_point, ndb.GeoPt(-21, 32), "Invalid source point")
        self.assertEqual(pool[0].destination_point, ndb.GeoPt(32, 12), "Invalid destination point")
        self.assertEqual(len(pool[0].passengers), 0, "Invalid passengers list")
        
    def test_delete_pool(self):
        User(
             deviceID=["12345"], 
             socialProfile=SocialIdentifier(socialID="32412", profile=constants.SocialProfile.FACEBOOK),
             username="Andreea").put()
        # p_driverID, p_source_point, p_destination_point, p_date, p_seats, p_is_weekly=False
        create_pool("32412", 
                  ndb.GeoPt(-21, 32), ndb.GeoPt(32, 12), 
                  datetime.datetime.now(), 
                  2)
        
        pool = Pool.query(Pool.driver_socialID=="32412").fetch(1)
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
                  datetime.datetime.now(), 
                  2)
        
        pool = Pool.query(Pool.driver_socialID=="32412").fetch(1)[0]
        pool.add_passenger("11412")
        
        res = delete_passenger_from_pool(pool.key.id(), "11412")
        self.assertEqual(res, constants.ExitCode.PASSENGER_DELETED_FROM_POOL, "Invalid exit code")
        
       

if __name__ == "__main__":
    #import sys;sys.argv = ['', 'Test.test_add_pool_simple']
    unittest.main()