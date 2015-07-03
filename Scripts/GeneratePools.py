'''
Created on Jun 27, 2015

@author: andreea
'''
from random import randint
import datetime
import random
import pycurl
import json
import time

class PoolManager(object):
    '''
    classdocs
    '''
    

    def __init__(self):
        '''
        Constructor
        '''
        self.current_pools = 1000
        
        # read lats and longs
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
        
    
    def generate_random_lat(self):
        return random.uniform(-90.0, 90.0)
    
    def generate_random_long(self):
        return random.uniform(-180.0, 180.0)
        
    def generate_random_user(self):
        return self.usersIDs[randint(0, 999)]
        
    def generate_pool_for_match(self):
        source_pos = randint(0, 999)
        destination_pos = randint(0, 999)
        pool = {}
#         date = datetime.datetime.now().strftime('%Y-%m-%dT%H:%M:%S.%f')
        pool["date"] = "2015-06-28T01:12:30.238948"
        
        pool["driver_id"] = self.generate_random_user()
        pool["seats"] = randint(2, 6)
        pool["source_point_lat"] = self.source_latitudes[source_pos]
        pool["source_point_lon"] = self.source_longitudes[source_pos]
        pool["destination_point_lat"] = self.destination_latitudes[destination_pos]
        pool["destination_point_lon"] = self.destination_longitudes[destination_pos]
        self.send_create_pool_request(pool)
        
    def generate_random_pool(self):
        pool = {}
#         date = datetime.datetime.now().strftime('%Y-%m-%dT%H:%M:%S.%f')
        pool["date"] = "2015-06-28T01:12:30.238948"
        pool["driver_id"] = self.generate_random_user()
        pool["seats"] = randint(2, 6)
        pool["source_point_lat"] = self.generate_random_lat()
        pool["source_point_lon"] = self.generate_random_long()
        pool["destination_point_lat"] = self.generate_random_lat()
        pool["destination_point_lon"] = self.generate_random_long()
        self.send_create_pool_request(pool)
        
        
    def send_create_pool_request(self, data):
        self.current_pools += 1
        url = 'https://bustling-bay-88919.appspot.com/_ah/api/symbidrive/v1.1/pool/create_pool'
        c = pycurl.Curl()
        c.setopt(pycurl.URL, url)
        c.setopt(pycurl.HTTPHEADER, ['Content-Type: application/json'])
        c.setopt(pycurl.VERBOSE, 0)
        c.setopt(pycurl.POST, 1)
        c.setopt(pycurl.POSTFIELDS, json.dumps(data))
        ts = time.clock()
        c.perform()
        ms = time.clock() - ts
        
        line = str(self.current_pools) + " " + str(ms) + "\n"
        f = open("create_pool.txt", 'a')
        f.write(line)
    
    def get_number_of_pool(self):
        return self.current_pools
              
# PoolManager().generate_random_pool() 
# # print str(datetime.datetime.now())   
# 
# poolManager = PoolManager()
# for i in range(10):
#     poolManager.generate_pool_for_match()