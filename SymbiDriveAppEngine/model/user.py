'''
Created on Apr 21, 2015

@author: andreea
'''
from google.appengine.ext import ndb

class Rating(ndb.Model):
    average = ndb.FloatProperty(default=0)
    number = ndb.IntegerProperty(default=0)
    
    def add_rating(self, value):  
        self.average = (self.average * self.number + value)/(self.number + 1)
        self.number += 1
        
class SocialIdentifier(ndb.Model):
    socialID = ndb.StringProperty(required=True)
    profile = ndb.StringProperty(required=True)

class User(ndb.Model):
    '''
    Models an individual User entry with username, telephone,
        isSmoker, listenToMusic, car, rating, feedback
    '''
    
    username = ndb.StringProperty(required=True)
    telephone = ndb.StringProperty()
    isSmoker = ndb.BooleanProperty(default=False)
    listenToMusic = ndb.BooleanProperty(default=False)
    car = ndb.StringProperty()
    rating = ndb.StructuredProperty(Rating)
    feedback = ndb.StringProperty(repeated=True)
    
    # these will be use to identify users
    socialProfile = ndb.StructuredProperty(SocialIdentifier)
    deviceID = ndb.StringProperty(repeated=True)
    
    def add_rating(self, value):
        if (isinstance(value, int)):
            if not (value >= 1 and value <=5):
                raise ValueError("Rating must be an integer value between 1 and 5")
        else:
            raise ValueError("Rating must be an integer value")
        
        if (self.rating is None):
            self.rating = Rating()
        self.rating.add_rating(value)
    
    def add_feedback(self, feedback_msg):
        if (self.feedback is None):
            self.feedback = []
        self.feedback.append(feedback_msg)