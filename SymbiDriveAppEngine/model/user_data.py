'''
Created on Jun 27, 2015

@author: zombie
'''

from google.appengine.ext import ndb

class UserData(ndb.Model):
    token = ndb.StringProperty(required=True)
    data_set = ndb.StringProperty(repeated=True)
    
    def add_data(self, value):
        if (self.data_set is None):
            self.data_set = []
        self.data_set.append(value)
        