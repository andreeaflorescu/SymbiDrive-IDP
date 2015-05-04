'''
Created on Apr 28, 2015

@author: andreea
'''
from model.pool import Pool
from google.appengine.ext import ndb
from model.user import User
from utils import constants

'''
    driver_socialID = ndb.StringProperty(required=True)
    passengers = ndb.IntegerProperty(repeated=True)
    source_point = ndb.GeoPtProperty(required=True)
    destination_point = ndb.GeoPtProperty(required=True)
    date = ndb.DateTimeProperty(required=True)
    seats = ndb.IntegerProperty(required=True)
    is_weekly = ndb.BooleanProperty(default=False)
'''

'''
p_source_point and p_destination_point should have the type ndb.GeoPt
p_date should have type datetime.datetime
'''
def create_pool(p_driverID, p_source_point, p_destination_point, p_date, p_seats, p_is_weekly=False):
    
    # check if user exists
    user = User.query(User.socialProfile.socialID == p_driverID).fetch(1)
    if (len(user) == 0):
        return constants.ExitCode.INVALID_USER
    
    
    Pool(driver_socialID = p_driverID, source_point = p_source_point,
                    destination_point = p_destination_point,
                    date = p_date,
                    seats=p_seats).put()
    
    return constants.ExitCode.POOL_ADDED

def add_passenger_to_pool(pool_id, passenger_id):
    
    pool = Pool.get_by_id(pool_id)
    if (pool is None):
        return constants.ExitCode.INVALID_POOL
    
    pool.add_passenger(passenger_id)
    pool.put()

    return constants.ExitCode.PASSENGER_ADDED_TO_POOL

def delete_pool(pool_id):
    pool = Pool.get_by_id(pool_id)
    
    if (pool is None):
        return constants.ExitCode.INVALID_POOL
    
    pool.key.delete()
    return constants.ExitCode.POOL_DELETED
    
def delete_passenger_from_pool(pool_id, passenger_id):
    pool = Pool.get_by_id(pool_id)
    
    if (pool is None):
        return constants.ExitCode.INVALID_POOL
    try:
        pool.passengers.remove(passenger_id)
    except ValueError:
        return constants.ExitCode.INVALID_USER
    
    return constants.ExitCode.PASSENGER_DELETED_FROM_POOL

'''
    start_point and end_point are GeoPt
    date is datetime.datetime
'''
def find_pool(start_point, end_point, date, meters=1000):
    Pool.query(ndb.AND())

    
