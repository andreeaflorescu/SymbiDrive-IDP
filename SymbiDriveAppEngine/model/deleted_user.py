'''
Created on Apr 28, 2015

@author: andreea
'''
from google.appengine.ext import ndb

class SocialIdentifier(ndb.Model):
    socialID = ndb.StringProperty(required=True)
    profile = ndb.StringProperty(required=True)

class DeletedUser(ndb.Model):
    # these will be use to identify users
    socialProfile = ndb.StructuredProperty(SocialIdentifier)
    deviceID = ndb.StringProperty(repeated=True)