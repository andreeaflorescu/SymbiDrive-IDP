'''
Created on Jun 24, 2015

@author: zombie
'''
import unittest
from google.appengine.ext import testbed
from controller import profiles_controller
from google.appengine.api import taskqueue


class TestGetUserProfileInfo(unittest.TestCase):


    def setUp(self):
         # First, create an instance of the Testbed class.
        self.testbed = testbed.Testbed()
        # Then activate the testbed, which prepares the service stubs for use.
        self.testbed.activate()
        # Next, declare which service stubs you want to use.
        self.testbed.init_datastore_v3_stub()
        self.testbed.init_memcache_stub()
        pass

   
    def tearDown(self):
        self.testbed.deactivate()
        pass

    @unittest.skip('skip')
    def test_get_user_info_facebook(self):
        
        p_token = "CAACEdEose0cBAOqQkqv6SCliXxtsaWEuT6zOgyzPqE6KYkZBZAXGy3mqfbQsI4XcJdf4oZCFvNpgLcPhVW2sjlC2Q3WZAAfl8cP9fIteHlO0ZCyLtM0WnpCTq8qdKzdDTnNOBFiWPWghTWYu7ZAZBhZCPZC7NupgOQ2Y66ZAz6KEwQqUqcAMG5rW5HwrZAPtFRK7yRQDW3z0DbdAW4ZAxqa3JJrx"
        res = profiles_controller.get_user_info_facebook(p_token)
        
        self.assertGreater(len(res), 0, "Result is empty")
        pass
    
    @unittest.skip('skip')
    def test_similatiry(self):
        
        p_token1 = "CAACEdEose0cBAOqQkqv6SCliXxtsaWEuT6zOgyzPqE6KYkZBZAXGy3mqfbQsI4XcJdf4oZCFvNpgLcPhVW2sjlC2Q3WZAAfl8cP9fIteHlO0ZCyLtM0WnpCTq8qdKzdDTnNOBFiWPWghTWYu7ZAZBhZCPZC7NupgOQ2Y66ZAz6KEwQqUqcAMG5rW5HwrZAPtFRK7yRQDW3z0DbdAW4ZAxqa3JJrx"
        p_token2 = "CAACEdEose0cBAO3wq2ZCJgmwgZCde9Vozlu3FZCZAb7dFvK6mMts5DRGXZCH4ZAyzq7EFiLVnS1N7XJ7r6zIIrwUWNVxt5hbBNY1HoKAO2MZCziJVpFUXA2mZA6Hj3a9KhCDCR8M2py9mmMGgflIE0v76weIOgdcYFjnTSFDKwhTmulVKa592deiC07ok1qnfgItJlr62L9xnZBPbRTrkSPYg"
        
        set1 = profiles_controller.get_user_info_facebook(p_token1)
        set2 = profiles_controller.get_user_info_facebook(p_token2)
        
        res = profiles_controller.calculate_similatrity(set1, set2)
        print res
        
        self.assertGreater(res, -1.0, "Similarity is not corerect")
        pass
    
    @unittest.skip('skip')   
    def test_spilt_string(self):
        
        p_string = "mana sAdf dE"
        res = profiles_controller.split_string(p_string)

        self.assertEqual(len(res), len(p_string) - 3, "Strings are not equal")
        pass
    
    @unittest.skip('skip')
    def test_dice_coefficient(self):
        
        p_x = "french"
        p_y = "france"
        res = profiles_controller.compute_dice_coefficient(p_x, p_y)
        
        self.assertEqual(res, 0.4, "GRESIT")
        pass
    
    def test_match_users(self):
        p_token1 = "CAACEdEose0cBAGWZBQ8MsKMk95i7gnZA4N17rJtI8pjOy7GoP5Xvev1LSgAr5CRgLGDoOmmxjGIZAgZAv9prEKalc7Y0GgQgmWQZCZAnXdEGNltwY8rr1GZBcDJqXfr4sysaVEIUfKVDgmOwZAi3F8XUIWTIecYoQYwZB0GoFSYCHpnMYQhb1Sm968AXlAZCsXFR5Whkk1mHZAHAB4C0PGXBHtg"
        p_token2 = "CAACEdEose0cBAA7xIIcGgcHZA4z0C0r1P0iZBJHFTzlaZCFIQsyZB1pWcMhmXCZBpTzZAXhjaoxQaz2wVpFB5mN48zRRofZCBVNVOTblmUJQXLZCo7Q9uyndKpTHtijF3BXD8ZBJ4Wkha7nm9iOmbEjDF8LpBfBY9B3hi6abXM2yPeVAdrKoiL2rIpJPZAgc8f2LZAFdptEVNrj4sc2zaSATWUG"
        
        set1 = profiles_controller.get_user_info_facebook(p_token1)
        
        for i in range(20):
            set2 = profiles_controller.get_user_info_facebook(p_token2)
            print profiles_controller.match_users(set1, set2)
            
        self.assertEqual(0, 0, "Bla bla bla")
        
if __name__ == "__main__":
    #import sys;sys.argv = ['', 'Test.testName']
    unittest.main()