'''
Created on Jun 27, 2015

@author: andreea
'''

from google.appengine.ext import ndb

class FeedbackMessage(ndb.Model):
    string_message = ndb.StringProperty(required=True)
    passenger_socialID = ndb.StringProperty(required=True)
    
class Feedback(ndb.Model):
    driver_socialID = ndb.StringProperty(required=True)
    feedback = ndb.StructuredProperty(FeedbackMessage, repeated=True)
    
    