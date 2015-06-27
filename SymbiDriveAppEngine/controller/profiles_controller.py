'''
Created on Jun 23, 2015

@author: zombie
'''

import appengine_config
import utils
import urllib2
from keyring.py25compat import json
from google.appengine.ext import deferred
import facebook


def get_user_info(userToken, profile):
    
    if(profile == utils.constants.SocialProfile.FACEBOOK):
        get_user_info_facebook(userToken)
        

def get_user_info_facebook(userToken):
    
    result = []
    
    graph = facebook.GraphAPI(access_token=userToken)
    likes = graph.get_connections(id='me', connection_name='likes')
    places = graph.get_connections(id='me', connection_name='tagged_places')
    
    get_facebook_likes(likes, result)
    get_facebook_places(places, result)
    
    return result 

def get_facebook_likes(likes_json, result):
    
    for like in likes_json['data']:
        result.append(like['name'])
        
    if 'next' in likes_json['paging'] :
        next_page_url = urllib2.urlopen(likes_json['paging']['next']).read()
        json_response = json.loads(next_page_url)
        get_facebook_likes(json_response, result)   
        
def get_facebook_places(places_json, result):
    
    for place in places_json['data']:
        if place['place']['name'] not in result:
            result.append(place['place']['name'])
            
    if 'next' in places_json['paging']:
        next_page_url = urllib2.urlopen(places_json['paging']['next']).read()
        json_response = json.loads(next_page_url)
        get_facebook_places(json_response, result)        
    
    

def calculate_similatrity(set1, set2):
    
    score = 0.0;
    
    for string1 in set1:
        for string2 in set2:
            if(string1 == string2):
                score += 1.0
            else:
                score += compute_dice_coefficient(string1, string2)
                
    return score        

def split_string(p_string):
    
    p_string = "".join(p_string.lower().split())
    
    return [p_string[i:i+2] for i in xrange(len(p_string) - 1)]


def compute_dice_coefficient(x, y): 
    
    x_set = split_string(x)
    y_set = split_string(y)
    
    summ = len(x_set) + len(y_set)
    intersect = len(frozenset(x_set).intersection(y_set))
    
    lenghtRaport = 1
    
    if len(x) > len(y):
        if intersect < len(y_set) / 2:
            return 0.0
        lenghtRaport = len(y) * 1.0 / len(x)
    else:
        if intersect < len(x_set) / 2:
            return 0.0;
        lenghtRaport = len(x) * 1.0 / len(y)
    
    return (2.0 * intersect / summ * lenghtRaport)

def match_users(set1, set2):
    result = deferred.defer(calculate_similatrity, set1, set2)
    return result
    