'''
Created on Apr 23, 2015

@author: andreea
'''
from model.user import User, SocialIdentifier
from model.pool import Pool
from model.gps_route import GPSRoute
from utils import constants
from model.deleted_user import DeletedUser
from google.appengine.ext import ndb

def register_user(p_deviceID, p_socialID, p_profile, p_username):
    
    # check if user was deleted because of negative rating
    user = DeletedUser.query(ndb.OR(DeletedUser.deviceID == p_deviceID,
                             DeletedUser.socialProfile.socialID == p_socialID)).fetch(1)
    
    if (len(user) == 1):
        return constants.ExitCode.DELETED_USER
    
    user = User.query(User.deviceID == p_deviceID).fetch(1)
    # check if user has already register on the current device
    if (len(user) == 1):
        # check if the social profile is the same as the one already saved in db
        if (user[0].socialProfile.profile == p_profile):
            if user[0].socialProfile.socialID != p_socialID:
                #update entities with the old socialID
                oldSocialID = user[0].socialProfile.socialID
                
                pools = Pool.query(Pool.driver_socialID == oldSocialID).fetch()
                for pool in pools:
                    pool.driver_socialID = p_socialID
                    pool.put()
                
                pools = Pool.query().fetch()
                for pool in pools:
                    if oldSocialID in pool.passengers:
                        pool.passengers.remove(oldSocialID)
                        pool.passengers.append(p_socialID)
                        pool.put()
                
                routes = GPSRoute.query(GPSRoute.driver_socialID == oldSocialID).fetch()
                for route in routes:
                    route.driver_socialID = p_socialID
                    route.put()
                
                user[0].socialProfile.socialID = p_socialID
                user[0].put()
                
                return constants.ExitCode.USER_REGISTER_MODIFIED_TOKEN
            else:
                return constants.ExitCode.USER_ALREADY_REGISTERED
        else:
            # save the new social profile
            user[0].socialProfile.socialID = p_socialID
            user[0].socialProfile.profile = p_profile
            user[0].put()
            return constants.ExitCode.USER_REGISTER_WITH_OTHER_PROFILE
    else:
        # check if the user is already register on other device
        user = User.query(User.socialProfile.socialID == p_socialID).fetch(1)
        if (len(user) == 1):
            user[0].deviceID.append(p_deviceID)
            user[0].put()
            return constants.ExitCode.USER_REGISTER_ON_OTHER_DEVICE
        else:
            # create a new user and save it
            User(deviceID= [p_deviceID], username= p_username,
                 socialProfile= SocialIdentifier(socialID= p_socialID, profile= p_profile)
                 ).put()
            return constants.ExitCode.USER_CREATED

def get_user_info(p_socialID):
    user = User.query(User.socialProfile.socialID == p_socialID).fetch(1)
    # check if user exists
    res = {}
    if (len(user) == 1):
        res['username'] = "" if user[0].username is None else user[0].username
        res['telephone'] = "" if user[0].telephone is None else user[0].telephone
        res['isSmoker'] = user[0].isSmoker
        res['listenToMusic'] = user[0].listenToMusic
        res['car'] = "" if user[0].car is None else user[0].car
        res['rating'] = -1.0 if user[0].rating is None else user[0].rating.average
        res['feedback'] = user[0].feedback
    
    return res

def update_user_profile(p_socialID, p_telephone, p_isSmoker, p_listenToMusic, p_car):
    user = User.query(User.socialProfile.socialID == p_socialID).fetch(1)
    
    if (len(user) == 1):
        user[0].telephone = p_telephone
        user[0].isSmoker = p_isSmoker
        user[0].listenToMusic = p_listenToMusic
        user[0].car = p_car
        user[0].put()
        
        return constants.ExitCode.PROFILE_UPDATED
    else:
        return constants.ExitCode.INVALID_USER


def add_rating(p_socialID, p_rating):
    '''
    Keyword arguments:
    p_socialID -- string value for the social_ID of the user you want to rate
    p_rating -- integer value between 1 and 5
    '''
    user = User.query(User.socialProfile.socialID == p_socialID).fetch()
    
    if (len(user) == 1):
        try: 
            user[0].add_rating(p_rating)
            user[0].put()
            return constants.ExitCode.RATING_ADDED
        except ValueError:
            return constants.ExitCode.INVALID_RATING
    else:
        return constants.ExitCode.INVALID_USER

def add_feedback(p_socialID, p_feedback):
    '''
    Keyword arguments:
    p_socialID -- string value for the social_ID of the user you want to rate
    p_feedback -- feedback string
    '''
    
    user = User.query(User.socialProfile.socialID == p_socialID).fetch(1)
    
    if (len(user) == 1):
        user[0].add_feedback(p_feedback)
        user[0].put()
        return constants.ExitCode.FEEDBACK_ADDED
    else:
        return constants.ExitCode.INVALID_USER

    
    
    
