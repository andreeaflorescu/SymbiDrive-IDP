'''
Created on Jun 28, 2015

@author: andreea
'''
import pycurl
import json
import time
import random
from random import randint

class FindPools(object):
    '''
    classdocs
    '''


    def __init__(self):
        '''
        Constructor
        '''
        f = open("source_lat", "r")
        g = open("source_long", "r")
        h = open("profile_ids.txt", "r")
        self.source_latitudes = [line.rstrip('\n') for line in f]
        f.close()
        
        self.source_longitudes = [line.rstrip('\n') for line in g]
        g.close()
        
        self.usersIDs = h.readline().split(' ')
        h.close()
        
        f = open("destination_lat", "r")
        self.destination_latitudes = [line.rstrip('\n') for line in f]
        f.close()
        
        g = open("destination_long", "r")
        self.destination_longitudes = [line.rstrip('\n') for line in g]
        g.close()
    
    def generate_random_user(self):
        return self.usersIDs[randint(0, 999)]
    
    def find_pools_large_response(self, number_of_pools):
        # read lats and longs
        data = {}
        source_pos = randint(0, 999)
        destination_pos = randint(0, 999)
        data["date"] = "2015-06-28T01:12:30.238948"
        data["delta"] = "2015-06-29T01:12:30.238948"
        data["socialID"] = self.generate_random_user()
        data["start_point_lat"] = self.source_latitudes[source_pos]
        data["start_point_lon"] = self.source_longitudes[source_pos]
        data["end_point_lat"] = self.destination_latitudes[destination_pos]
        data["end_point_lon"] = self.destination_longitudes[destination_pos]
        
        self.send_find_pool_request(data, number_of_pools)
    
        
    def generate_random_lat(self):
        return random.uniform(-90.0, 90.0)
    
    def generate_random_long(self):
        return random.uniform(-180.0, 180.0)
    
    def find_pools_small_response(self, number_of_pools):
        data = {}
        data["date"] = "2015-06-28T01:12:30.238948"
        data["delta"] = "2015-06-29T01:12:30.238948"
        data["socialID"] = self.generate_random_user()
        data["start_point_lat"] = self.generate_random_lat()
        data["start_point_lon"] = self.generate_random_long()
        data["end_point_lat"] = self.generate_random_lat()
        data["end_point_lon"] = self.generate_random_long()
        
        self.send_find_pool_request(data, number_of_pools)
    
    def send_find_pool_request(self, data, number_of_pools):
        url = 'https://bustling-bay-88919.appspot.com/_ah/api/symbidrive/v1.1/pool/find_pool'
        c = pycurl.Curl()
        c.setopt(pycurl.URL, url)
        c.setopt(pycurl.HTTPHEADER, ['Content-Type: application/json'])
        c.setopt(pycurl.VERBOSE, 0)
        c.setopt(pycurl.POST, 1)
        c.setopt(pycurl.POSTFIELDS, json.dumps(data))
        ts = time.clock()
        c.perform()
        ms = time.clock() - ts
        
        line = str(number_of_pools) + " " + str(ms) + "\n"
        f = open("find_pool.txt", 'a')
        f.write(line)
        

# FindPools().find_pools_large_response(10)