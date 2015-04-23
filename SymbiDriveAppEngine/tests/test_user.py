'''
Created on Apr 21, 2015

@author: andreea
'''
import unittest
from model.user import User, SocialIdentifier
from model.user import Rating
from google.appengine.ext import testbed
from google.appengine.api import datastore_errors
from utils import constants

class TestUser(unittest.TestCase):
    
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


    def test_put_user_without_username(self):
        try:
            User().put()
        except datastore_errors.BadValueError:
            pass
    
    def test_put_user_with_username(self):
        User(username="Andreea").put()
        self.assertEqual(1, len(User.query().fetch(2)), "Number of users must be one after inserting valid user!")
    
    def test_get_user__with_no_rating(self):
        User(username="Andreea", telephone="0720829025", isSmoker=True).put()
        self.assertEqual(User.query().fetch(3)[0].rating, None, "Rating value should be None")
    
    def test_get_user_with_empty_rating(self):
        User(username="Andreea", telephone="0720829025", isSmoker=True, rating=Rating()).put()
        user = User.query().fetch(1)[0]
        self.assertEqual(user.rating.average, 0, "Rating average should be 0")
        self.assertEqual(user.rating.number, 0, "Rating number should be 0")
    
    def test_put_user_rating_not_integer(self):
        rating_value = "9";
        user = User(username="Andreea", telephone="0720829025", isSmoker=True)
        self.assertRaisesRegexp(ValueError, "Rating must be an integer value", user.add_rating, rating_value)
     
    def test_put_user_rating_not_in_range(self):
        rating_value = 9;
        user = User(username="Andreea", telephone="0720829025", isSmoker=True)

        self.assertRaisesRegexp(ValueError, "Rating must be an integer value between 1 and 5", user.add_rating, rating_value)
        
    def test_put_valid_rating(self):
        rating_value = 5;
        user = User(username="Andreea", telephone="0720829025", isSmoker=True)        
        user.add_rating(rating_value)
        user.put()
        
        self.assertEqual(User.query().fetch(2)[0].rating.average, 5, "Rating should have value equal to 5")
        self.assertEqual(User.query().fetch(2)[0].rating.number, 1, "Number of ratings should be 1")
        
    def test_add_more_rating_values(self):
        rating_values = [1, 3, 4, 5, 2]
        final_rating = 0;
        for rating in rating_values:
            final_rating += rating
        final_rating /= 5
        
        # create initial user with no rating
        User(username="Andreea", telephone="0720829025", isSmoker=True).put()
        
        # get user and update rating
        user = User.query().fetch(1)[0]
        for rating in rating_values:
            user.add_rating(rating)
        user.put(); 
        
        # get user and check if rating is equal to final_rating
        user = User.query().fetch(1)[0]
        self.assertEqual(user.rating.average, 
                         final_rating, 
                         "Rating should have value: " + str(final_rating)) 
        self.assertEqual(user.rating.number, 
                         len(rating_values), 
                         "Number of ratings shoulb be equal to " + str(len(rating_values)))

    def test_put_feedback(self):
        # create initial user with no feedback
        User(username="Andreea", telephone="0720829025", isSmoker=True).put()
        
        # get user and update feedback
        user = User.query().fetch(1)[0]
        feedback_msg = "Feedback message"
        user.add_feedback(feedback_msg)
        user.put()
        
        #check if feedback is saved
        user = User.query().fetch(1)[0]
        self.assertEqual(user.feedback[0], feedback_msg, "Wrong feedback message")
    
    def test_put_many_feedbacks(self):
        # create initial user with no feedback
        User(username="Andreea", telephone="0720829025", isSmoker=True).put()
        
        # get user and update feedback
        user = User.query().fetch(1)[0]
        feedback_msgs = ["Feedback message1", "Feedback message2"]
        for feedback_msg in feedback_msgs:
            user.add_feedback(feedback_msg)
        
        user.put()
        
        #check if feedback is saved
        user = User.query().fetch(1)[0]
        self.assertEqual(len(user.feedback), len(feedback_msgs), "Wrong number of feedback messages")
        for i in range(len(user.feedback)):
            self.assertEqual(user.feedback[i], feedback_msgs[i], "Wrong feedback message")
            
    def create_users(self):
        User(deviceID=["12345"], 
             socialProfile=[SocialIdentifier(socialID="32412", profile=constants.SocialProfile.FACEBOOK)],
             username="Andreea").put()
        User(deviceID=["12325"], 
             socialProfile=[SocialIdentifier(socialID="32143", profile=constants.SocialProfile.GOOGLE)], 
             username="Ion").put()
        User(deviceID=["13245"], 
             socialProfile=[SocialIdentifier(socialID="31412", profile=constants.SocialProfile.FACEBOOK),
                            SocialIdentifier(socialID="11412", profile=constants.SocialProfile.GOOGLE)], 
             username="Maria").put()
            
    def test_find_user_by_deviceID(self):
        self.create_users()
        
        result = User.query(User.deviceID == "12345").fetch(1)
        self.assertEqual(len(result), 1, "Should return one user")
    
    def test_find_user_by_socialID(self):
        self.create_users()
        
        result = User.query(User.socialProfile.socialID == "11412").fetch(1)
        self.assertEqual(len(result), 1, "Should return one user")
    
    def test_find_user_by_deviceID_empty(self):
        self.create_users()
        
        result = User.query(User.deviceID == "99999").fetch(1)
        self.assertEquals(len(result), 0, "Should return no user")
         
if __name__ == "__main__":
#     import sys;sys.argv = ['', 'TestUser.testPutUserWithoutUsername']
#     unittest.main()
    suite = unittest.TestLoader().loadTestsFromTestCase(TestUser)