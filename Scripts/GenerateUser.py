'''
Created on Jun 27, 2015

@author: andreea
'''
import random
import string
from random import randint
import pycurl
import json
import time

class UserManager(object):

    def __init__(self):
        '''
        Constructor
        '''
        self.current_number_of_users = 0;
    
    def generate_random_string(self, length):
        return ''.join(random.choice(string.ascii_uppercase + string.digits) for _ in range(length))
    
    def generate_random_profile(self):
        return ["Facebook", "Google+"][randint(0,1)]
    
    def generate_random_user(self):
        user = {}
        user["deviceID"] = self.generate_random_string(100)
        user["socialID"] = self.generate_random_string(100)
        user["profile"] = self.generate_random_profile()
        user["username"] = self.generate_random_string(20)
        return user
    
    def send_user_request(self):
        data = self.generate_random_user()
        self.current_number_of_users += 1
        
        c = pycurl.Curl()
        c.setopt(pycurl.URL, 'https://bustling-bay-88919.appspot.com/_ah/api/symbidrive/v1.1/user/register_user')
        c.setopt(pycurl.HTTPHEADER, ['Content-Type: application/json'])
        c.setopt(pycurl.VERBOSE, 0)
        c.setopt(pycurl.POST, 1)
        c.setopt(pycurl.POSTFIELDS, json.dumps(data))
        ts = time.clock()
        c.perform()
        ms = time.clock() - ts
        
        line = str(self.current_number_of_users) + " " + str(ms) + "\n"
        f = open("register_user.txt", 'a')
        g = open("profile_ids.txt", 'a')
        
        f.write(line)
        g.write(data["socialID"] + " ")
        f.close()
        g.close()