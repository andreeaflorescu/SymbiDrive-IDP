'''
Created on Apr 22, 2015

@author: andreea
'''
import unittest
from google.appengine.ext import testbed
from model.pool import Pool
from google.appengine.ext import ndb
import datetime

class TestPool(unittest.TestCase):


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


    def test_create_pool(self):
        Pool(driver_socialID="1", source_point=ndb.GeoPt(-34, -54),
                    destination_point=ndb.GeoPt(-32.0, 43.9),
                    date=datetime.datetime.now(), 
                    seats=2).put()
        print Pool.query().fetch(1)[0]
        pass


if __name__ == "__main__":
    #import sys;sys.argv = ['', 'Test.testName']
    unittest.main()