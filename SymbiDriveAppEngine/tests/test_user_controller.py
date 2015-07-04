'''
Created on Apr 21, 2015

@author: andreea
'''

import unittest
from google.appengine.ext import testbed
from controller.user_controller import register_user, get_user_info, add_rating,\
    update_user_profile, add_feedback
from utils import constants
from model.user import User, SocialIdentifier

class TestRegisterUser(unittest.TestCase):
    
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
        
    def test_register_new_user(self):
        # p_deviceID, p_socialID, p_profile, p_username
        p_deviceID = "124453"
        p_profileID = "2131314"
        p_profile = constants.SocialProfile.FACEBOOK
        p_username = "Andreea Florescu"

        res = register_user(p_deviceID, p_profileID, p_profile, p_username)
        self.assertEqual(res, constants.ExitCode.USER_CREATED, "Invalid exit code")
        
        user = User.query(User.socialProfile.socialID == p_profileID).fetch(1)
        self.assertEqual(len(user), 1, "Register user failed")
        self.assertEqual(len(user[0].deviceID), 1, "Only one device must be registered")
        self.assertEqual(user[0].deviceID[0], p_deviceID, "Device ids don't match")
        self.assertEqual(user[0].socialProfile.socialID, p_profileID, "Social IDs don't match")
        self.assertEqual(user[0].socialProfile.profile, p_profile, "Profiles type don't match")
        self.assertEqual(user[0].username, p_username, "Usernames don't match")
    
    def test_register_user_existing_deviceID(self):
        # add user for test
        register_user("12345", "123456", constants.SocialProfile.GOOGLE, "Andreea")
        register_user("12346", "123457", constants.SocialProfile.FACEBOOK, "Mia")
        register_user("12347", "12345", constants.SocialProfile.GOOGLE, "Ion")
        
        # try to register user with existing deviceID, but other profile ID
        res = register_user("12345", "214353", constants.SocialProfile.FACEBOOK, "Andreea")
        self.assertEqual(res, constants.ExitCode.USER_REGISTER_WITH_OTHER_PROFILE, "Invalid exit code")
        
        # check if profile has been updated
        user = User.query(User.deviceID == "12345").fetch(1)
        self.assertEqual(len(user), 1, "User is not in the database")
        self.assertEqual(user[0].socialProfile.socialID, "214353", "Social ID is not updated corectly")
        self.assertEqual(user[0].socialProfile.profile, constants.SocialProfile.FACEBOOK, "Profile type is not updated")
    
    def test_register_new_device(self):
        # add user for test
        register_user("12345", "123456", constants.SocialProfile.GOOGLE, "Andreea")
        register_user("12346", "123457", constants.SocialProfile.FACEBOOK, "Mia")
        register_user("12347", "12345", constants.SocialProfile.GOOGLE, "Ion")
        
        # try to register user with existing profile ID, but other deviceID
        res = register_user("22222", "123456", constants.SocialProfile.GOOGLE, "Andreea")
        self.assertEqual(res, constants.ExitCode.USER_REGISTER_ON_OTHER_DEVICE, "Invalid exit code")
        
        # check if profile has been updated
        user = User.query(User.socialProfile.socialID == "123456").fetch(1)
        self.assertEqual(len(user[0].deviceID), 2, "User should be registered with 2 devices")
        self.assertTrue("22222" in user[0].deviceID, "DeviceID list should contain the new added ID")
        self.assertTrue("12345" in user[0].deviceID, "DeviceID list should contain the old ID")

    
    def test_register_existing_user(self):
        # add user for test
        register_user("12345", "123456", constants.SocialProfile.GOOGLE, "Andreea")
        register_user("12346", "123457", constants.SocialProfile.FACEBOOK, "Mia")
        register_user("12347", "12345", constants.SocialProfile.GOOGLE, "Ion")
        
        # try to register user with existing profile ID, but other deviceID
        res = register_user("12346", "123457", constants.SocialProfile.FACEBOOK, "Mia")
        self.assertEqual(res, constants.ExitCode.USER_ALREADY_REGISTERED, "Invalid exit code")
        
        # check if no other user was added
        self.assertEqual(len(User.query().fetch(10)), 3 , "Wrong number of users")
    

class TestGetUserInfo(unittest.TestCase):
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
    
    def test_get_info_for_invalid_user(self):
        register_user("12345", "123456", constants.SocialProfile.GOOGLE, "Andreea")
        res = {}
        res = get_user_info("123454")
        
        
        self.assertEqual(res, {}, "Response is not empty")
    
    def test_get_info_for_existing_user(self):
        register_user("12345", "123456", constants.SocialProfile.GOOGLE, "Andreea")
        
        actual = get_user_info("123456")
        print actual
        
        print actual['username']
        
        expected = {'username': u'Andreea', 'rating': -1.0, 'feedback': [], 'car': '', 'isSmoker': False, 'telephone': '', 'listenToMusic': False}
        self.assertEqual(actual, expected, "Wrong info about user")
        
class TestUpdateProfile(unittest.TestCase):
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
    
    def test_invalid_user(self):
        res = update_user_profile("1234555", None, False, False, "Logan")
        self.assertEqual(res, constants.ExitCode.INVALID_USER, "Invalid exit code")
    
    def test_update_all_info(self):
        # create user
        register_user("123456", "1234121", constants.SocialProfile.FACEBOOK, "Mihai")
        
        # update user profile
        res = update_user_profile("1234121", "0720829025", True, True, "BMW");
        
        # check result
        self.assertEqual(res, constants.ExitCode.PROFILE_UPDATED, "Invalid exit code");
        
        user = User.query(User.socialProfile.socialID == "1234121").fetch(1)[0]
        self.assertEqual(user.telephone, "0720829025", "Invalid telephone number")
        self.assertEqual(user.isSmoker, True, "Invalid property isSmoker")
        self.assertEqual(user.listenToMusic, True, "Invalid property listenToMusic")
        self.assertEqual(user.car, "BMW", "Invalid car type")
        
    
class TestAddRating(unittest.TestCase):
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
    
    def test_add_rating_invalid_user(self):
        res = add_rating("224121", 5)
        self.assertEqual(res, constants.ExitCode.INVALID_USER, "Wrong exit error")
    
    def test_add_rating_invalid_rating_value(self):
        register_user("123456", "1234532", constants.SocialProfile.FACEBOOK, "Andrei")
        res = add_rating("1234532", "fds")
        self.assertEqual(res, constants.ExitCode.INVALID_RATING, "Wrong exit code")
    
    def test_add_rating_valid_user_and_rating(self):
        register_user("123456", "1234532", constants.SocialProfile.FACEBOOK, "Mi5hu")
        
        res = add_rating("1234532", 3)
        self.assertEqual(res, constants.ExitCode.RATING_ADDED, "Wrong exit code")
        
        user = User.query(User.socialProfile.socialID == "1234532").fetch(1)[0]
        self.assertEqual(user.rating.average, 3, "Wrong rating value")
        
class TestAddFeedback(unittest.TestCase):
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
        
    def test_add_feedback_invalid_user(self):
        
        res = add_feedback("1231213", "p_feedback")
        self.assertEqual(res, constants.ExitCode.INVALID_USER, "Invalid exit code")
    
    def test_add_first_feedback(self):
        register_user("12345", "123456", constants.SocialProfile.GOOGLE, "Andreea")
        
        user = User.query(User.socialProfile.socialID == "123456").fetch(1)[0]
        res = add_feedback("123456", "feedback")
        self.assertEqual(res, constants.ExitCode.FEEDBACK_ADDED, "Invalid exit code")
        
        expected = ["feedback"]
        actual = user.feedback
        self.assertEqual(expected, actual, "Invalid user feedback")
        
    def test_add_additional_feedback(self):
        User(deviceID=["12345"], 
             socialProfile=SocialIdentifier(socialID="32412", profile=constants.SocialProfile.FACEBOOK),
             feedback=["feed"],
             username="Andreea").put()
        
        res = add_feedback("32412", "fsadadsa dsadasd fsdadawdsz");
        self.assertEqual(res, constants.ExitCode.FEEDBACK_ADDED, "Invalid exit code")
        
        user = User.query(User.socialProfile.socialID == "32412").fetch(1)[0]
        actual = user.feedback
        expected = ["feed", "fsadadsa dsadasd fsdadawdsz"]
        self.assertEqual(actual, expected, "Invalid feedback")
             
if __name__ == "__main__":
    suite_register_user = unittest.TestLoader().loadTestsFromTestCase(TestRegisterUser)
    suite_get_info = unittest.TestLoader().loadTestsFromTestCase(TestGetUserInfo)