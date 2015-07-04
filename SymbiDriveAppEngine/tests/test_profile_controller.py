'''
Created on Jun 24, 2015

@author: zombie
'''
import unittest
from google.appengine.ext import testbed
from controller import profiles_controller
import time
from controller.profiles_controller import calculate_similatrity


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

#     @unittest.skip('skip')
    def test_get_user_info_facebook(self):
        
        p_token = "CAACEdEose0cBALTXaDqUNcAdMKEYNbWWZCCxLdcOkYY8JIcZCFVEJxh0Gj84hS5reMUJIL4YRXjeU7CYy05UHnxUzVwBULqLFpKyFLdNS3htXZAOnDfWSgjWbZBwysgsXW4QZCZAPwLfRd3ZClU4kER5mOWiJesZBdiLlH6qR9jXg7NWX6ZCosi3bNTQiwh4S2kOMWp3RFnZAKIaaiii7GZC4O0"
        res = profiles_controller.get_user_info_facebook(p_token)
        
        self.assertGreater(len(res), 0, "Result is empty")
        pass
    
    @unittest.skip('skip')
    def test_similatiry(self):
        
        p_token1 = "CAACEdEose0cBAOZCZBPaQOCTO9wbYReR2lBxUbT558HTaTkaEdVrDhUnnmZC7gQisPqd4ohU6UZA3o7g2bOyO7u32J2SO0m8OUnIpYR06hPFdVrBuaF6BxPk7IL77LZBkqR9AWTc64V0pwomDZAkQewPTgxJ0p6gNc2MY24R8o6RdolJrNDiZAWmMeZAzKnCZCkzwlV2OuOYJPEQosFr9CO9z"
        p_token2 = "CAACEdEose0cBAJZBc1tM3fjGMcBgYRyPZBKIQhO7AXe4gmufrJyZAcvasokau5MZBsadD5aNxFwZCZC4ema868YoY7Tt4JhZCLF5GhGZBcafKdaR5mfduPKkcjzduU3Jo1nscRd2epOxbiFnLeGl6KY0CVL4CyzvybkEqciLEHm0XOvEL6yZBR4ECHMNz4R5IfHzJcjVrfzsoT42jeBo7aK7aICDrsESvgXcZD"
        p_token3 = "CAACEdEose0cBAOasqegBtx9NnZBGiefADra211LDYnLZCLbUZBZBwFwjbkC7mGLZCraQBlnVBNgfhQFCGPAFKGj5kU01udniQMP01l6mxVIJir0qXf1uiRDDhlf8eUFevvXRZByoY2SJraU6uhZC8sytq5acZANxzewc6FlgLhoZBl5IqrBqs464l3JPFGPeonpN7n3vuo0T6j9YPCOyQrOZCsxcbYdXKchC0ZD"
        p_token4 = "CAACEdEose0cBAJZBc1tM3fjGMcBgYRyPZBKIQhO7AXe4gmufrJyZAcvasokau5MZBsadD5aNxFwZCZC4ema868YoY7Tt4JhZCLF5GhGZBcafKdaR5mfduPKkcjzduU3Jo1nscRd2epOxbiFnLeGl6KY0CVL4CyzvybkEqciLEHm0XOvEL6yZBR4ECHMNz4R5IfHzJcjVrfzsoT42jeBo7aK7aICDrsESvgXcZD"
        p_token5 = "CAACEdEose0cBAAssgRlgBjOKDsjXgiZBZCBDtCM4PZCxfdFEtrOf0a5KUeh6f7vcZCcC56E3RBlwFNO2zdyfGHZBFZCRB6ANC9rKpcuCmPEcJJTlgvBEmUsYxGwSVZBd0sZCvD5yj3cgc6humauguWOap0VHKHBcg2qDVUJljDcGEc8lX4ZAEYifziup6Dr5sjkkfy8yZCKZA3EzukZAtYiiw2nkxXWl9kc7VXMZD"
        p_token6 = "CAACEdEose0cBAPbK4EXAkHwlWZBJShMX02m2SgCkKgUMsD2QW6P9Ldj7alHHzase9zcejkfpirjZBeNhBq9N9LSvDAe2buQ6oD7Of4AjR9BBKJSCZCbsAEDrAeg8124TyeJCeZB6Vo0bSZB9krVHu1RNaBw6KFwoxdfwralqLG7RVzjZA7n8BH7XDmg6zzX5DzcZBL0ylo3CmZChhAbtsgsZBY4fbN96zgzEZD"
        
        f=open("text", 'w+')
        
        time1 = time.time()
        set1 = profiles_controller.get_user_info_facebook(p_token1)
        time2 = time.time()
        f.write("get user info = " + str((time2 - time1)) + "\n")
        sets = []
        sets.append(profiles_controller.get_user_info_facebook(p_token2))
        sets.append(profiles_controller.get_user_info_facebook(p_token3))
        sets.append(profiles_controller.get_user_info_facebook(p_token4))
        sets.append(profiles_controller.get_user_info_facebook(p_token5))
        sets.append(profiles_controller.get_user_info_facebook(p_token6))
        
        time3 = time.time()
#         res = profiles_controller.calculate_similatrity(set1, set2)
        for i in range(len(sets)):
            calculate_similatrity(set1, sets[i])
        time4 = time.time()
        f.write("get similatiry = " + str((time4 - time3)) + "\n")
#         print res
        
#         self.assertGreater(res, -1.0, "Similarity is not corerect")
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
    @unittest.skip('skip')
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