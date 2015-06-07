'''
Created on May 15, 2015

@author: andreea
'''

import endpoints

from protorpc import messages
from protorpc import message_types
from protorpc import remote
from controller import user_controller
from controller import pool_controller
from google.appengine.ext import ndb
import datetime


symbidrive_api = endpoints.api(name='symbidrive', version='v1.1')

package = 'Symbidrive'

class RegisterUserRequest(messages.Message):
    deviceID = messages.StringField(1, required=True)
    socialID = messages.StringField(2, required=True)
    profile = messages.StringField(3, required=True)
    username = messages.StringField(4, required=True)
    
class UserInfoRequest(messages.Message):
    socialID = messages.StringField(1, required=True)
    
class Rating(messages.Message):
    average = messages.FloatField(1, required=True)
    number = messages.IntegerField(2, required=True)
    
class UserInfoResponse(messages.Message):
    username = messages.StringField(1, required=True)
    telephone = messages.StringField(2, required=True)
    isSmoker = messages.BooleanField(3, required=True)
    listenToMusic = messages.BooleanField(4, required=True)
    car = messages.StringField(5, required=True)
    rating = messages.MessageField(Rating, 6, required=True)
    feedback = messages.StringField(7, repeated=True)
    
class UpdateUserInfoRequest(messages.Message):
    socialID = messages.StringField(1, required=True)
    username = messages.StringField(2, required=True)
    telephone = messages.StringField(3, required=True)
    isSmoker = messages.BooleanField(4, required=True)
    listenToMusic = messages.BooleanField(5, required=True)
    car = messages.StringField(6, required=True)

class AddRatingRequest(messages.Message):
    socialID = messages.StringField(1, required=True)
    rating = messages.IntegerField(2, required=True)
    
class AddFeedbackRequest(messages.Message):
    socialID = messages.StringField(1, required=True)
    feedback = messages.StringField(2, repeated=True)
    
class UserResponse(messages.Message):
    ret = messages.StringField(1, required=True)

@symbidrive_api.api_class(path='user')
class User_endpoint(remote.Service):
    '''
        Expose user_controller operations
    '''
    @endpoints.method(RegisterUserRequest, UserResponse,
                      path='register_user', http_method='POST')
    def register_user(self, request):
        return UserResponse(ret=user_controller.register_user(request.deviceID,
                                                              request.socialID,
                                                              request.profile,
                                                              request.username))

    @endpoints.method(UserInfoRequest, UserInfoResponse,
                      path='get_user_info', http_method='POST')
    def get_user_info(self, request):
        info = user_controller.get_user_info(request.socialID)
        return UserInfoResponse(username=info['username'],
                                telephone=info['telephone'],
                                isSmoker=info['isSmoker'],
                                listenToMusic=info['listenToMusic'],
                                car=info['car'],
                                rating=Rating(average=info['rating'].average,
                                              number=info['rating'].number),
                                feedback=info['feedback'])

    @endpoints.method(UpdateUserInfoRequest, UserResponse,
                      path='update_user_profile', http_method='POST')
    def update_user_profile(self, request):
        return UserResponse(ret=user_controller.update_user_profile(request.socialID,
                                                                    request.telephone,
                                                                    request.isSmoker,
                                                                    request.listenToMusic,
                                                                    request.car))
    @endpoints.method(AddRatingRequest, UserResponse,
                      path='add_rating', http_method='POST')
    def add_rating(self, request):
        return UserResponse(ret=user_controller.add_rating(request.socialID, 
                                                           request.rating))
        
    @endpoints.method(AddFeedbackRequest, UserResponse,
                      path='add_feedback', http_method='POST')
    def add_feedback(self, request):
        return UserResponse(ret=user_controller.add_feedback(request.socialID,
                                                             request.feedback))
        
        
class CreatePoolRequest(messages.Message):
    driver_id = messages.StringField(1, required=True)
    source_point_lat = messages.FloatField(2, required=True)
    source_point_lon = messages.FloatField(3, required=True)
    destination_point_lat = messages.FloatField(4, required=True)
    destination_point_lon = messages.FloatField(5, required=True)
    date = message_types.DateTimeField(6, required=True)
    seats = messages.IntegerField(7, required=True)
    is_weekly = messages.BooleanField(8, required=False)
    
class DeletePoolRequest(messages.Message):
    pool_id = messages.IntegerField(1, required=True)
    
class ManagePassangerRequest(messages.Message):
    pool_id = messages.IntegerField(1, required=True)
    passanger_id = messages.StringField(2, required=True)
    
class PoolResponse(messages.Message):
    ret = messages.StringField(1, required=True)
    
class FindPoolRequest(messages.Message):
    socialID = messages.StringField(1, required=True)
    start_point_lat = messages.FloatField(2, required=True)
    start_point_lon = messages.FloatField(3, required=True)
    end_point_lat = messages.FloatField(4, required=True)
    end_point_lon = messages.FloatField(5, required=True)
    date = message_types.DateTimeField(6, required=True)
    delta = message_types.DateTimeField(7, required=True)
    walking_distance = messages.FloatField(8, required=False)
   
class SinglePoolResponse(messages.Message):
    driver_id = messages.StringField(1, required=True)
    source_point_lat = messages.FloatField(2, required=True)
    source_point_lon = messages.FloatField(3, required=True)
    destination_point_lat = messages.FloatField(4, required=True)
    destination_point_lon = messages.FloatField(5, required=True)
    date = message_types.DateTimeField(6, required=True)
    seats = messages.IntegerField(7, required=True)
    pool_id = messages.IntegerField(8, required=True)

class FindPoolResponse(messages.Message):
    pools = messages.MessageField(SinglePoolResponse, 1, repeated=True)

@symbidrive_api.api_class(path='pool')
class Pool_endpoint(remote.Service):
    '''
        Expose pool_controller operations
    '''
    @endpoints.method(CreatePoolRequest, PoolResponse,
                      path='create_pool', http_method='POST')
    def create_pool(self, request):
        source_point = ndb.GeoPt(request.source_point_lat, request.source_point_lon)
        destination_point = ndb.GeoPt(request.destination_point_lat, request.destination_point_lon)
        date = request.date.replace(tzinfo=None)
        if request.is_weekly is None:
            is_weekly = False
        else:
            is_weekly = request.is_weekly

        return PoolResponse(ret=pool_controller.create_pool(request.driver_id,
                                                            source_point,
                                                            destination_point,
                                                            date,
                                                            request.seats,
                                                            is_weekly))
        
    @endpoints.method(DeletePoolRequest, PoolResponse,
                      path='delete_pool', http_method='POST')
    def delete_pool(self, request):
        return PoolResponse(ret=pool_controller.delete_pool(request.pool_id))

    @endpoints.method(ManagePassangerRequest, PoolResponse,
                      path='add_passenger_to_pool', http_method='POST')
    def add_passenger_to_pool(self, request):
        return PoolResponse(ret=pool_controller.add_passenger_to_pool(request.pool_id, 
                                                                      request.passenger_id))
        
    @endpoints.method(ManagePassangerRequest, PoolResponse,
                      path='delete_passenger_from_pool', http_method='POST')
    def delete_passenger_from_pool(self, request):
        return PoolResponse(ret=pool_controller.delete_passenger_from_pool(request.pool_id, 
                                                                           request.passenger_id))
        
    @endpoints.method(FindPoolRequest, FindPoolResponse,
                      path='find_pool', http_method='POST')
    def find_pool(self, request):
        socialID = request.socialID
        start_point = ndb.GeoPt(request.start_point_lat, request.start_point_lon)
        end_point = ndb.GeoPt(request.end_point_lat, request.end_point_lon)
        date = request.date.replace(tzinfo=None)
        delta_ = request.delta
        delta = datetime.timedelta(hours=delta_.hour, minutes=delta_.minute)
        if request.walking_distance is None:
            walking_distance = 1000
        else:
            walking_distance = request.walking_distance

        pools_ = pool_controller.find_pool(socialID, start_point, end_point, date, delta, walking_distance)
        
        pools = []
        for pool_ in pools_:
            pool = SinglePoolResponse(driver_id=pool_.driver_socialID,
                                      source_point_lat=pool_.source_point.lat,
                                      source_point_lon=pool_.source_point.lon,
                                      destination_point_lat=pool_.destination_point.lat,
                                      destination_point_lon=pool_.destination_point.lon,
                                      date=pool_.date,
                                      seats=pool_.seats,
                                      pool_id=pool_.key.id())
            pools.append(pool)
        
        return FindPoolResponse(pools=pools)


APPLICATION = endpoints.api_server([symbidrive_api])
