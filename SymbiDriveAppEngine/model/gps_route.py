'''
Created on Apr 22, 2015

@author: andreea
'''
from google.appengine.ext import ndb

class GPSRoute(ndb.Model):
    '''
    classdocs
    '''
    name = ndb.StringProperty(required=True)
    driver_socialID = ndb.StringProperty(required=True)
    route_points = ndb.GeoPtProperty(repeated=True)
    has_associated_pool = ndb.BooleanProperty(default=False)
    pool_id = ndb.IntegerProperty(default=-1)