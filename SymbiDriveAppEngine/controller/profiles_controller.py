'''
Created on Jun 23, 2015

@author: zombie
'''

import appengine_config
import utils
import urllib2
from google.appengine.ext import deferred
import facebook
import json
import io
from _codecs import encode


def get_user_info(userToken, profile):
    
    if(profile == utils.constants.SocialProfile.FACEBOOK):
        get_user_info_facebook(userToken)
        

def get_user_info_facebook(userToken):
    
    app_id = ""
    app_secret = ""
    
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
            
    if 'paging' in places_json and 'next' in places_json['paging']:
        next_page_url = urllib2.urlopen(places_json['paging']['next']).read()
        json_response = json.loads(next_page_url)
        get_facebook_places(json_response, result)        
    
    

def calculate_similatrity(set1, set2):
    
    score = 0.0
#     f = open("text", 'w+')
    
    for string1 in set1:
        for string2 in set2:
            if(string1 == string2):
                score += 1.0
            else:
#                 c = compute_dice_coefficient(string1, string2)
#                 if c > 0.0: 
#                     f.write(string1.encode('utf-8') + " ~ " + string2.encode('utf-8') + "\t")
#                     f.write(str(c))
#                     f.write('\n')
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
        if intersect < len(x) / 2:
            return 0.0
        lenghtRaport = len(y) * 1.0 / len(x)
    else:
        if intersect < len(y) / 2:
            return 0.0
        lenghtRaport = len(x) * 1.0 / len(y)
    
    return (2.0 * intersect / summ * lenghtRaport)

def match_users(set1, set2):
    result = deferred.defer(calculate_similatrity, set1, set2)
    return result
    