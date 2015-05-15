'''
Created on May 15, 2015

@author: alex
'''

import endpoints
from protorpc import messages
from protorpc import message_types
from protorpc import remote
from endpoint.constants import symbidrive_api
from controller import user_controller

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
    telephone = messages.StringField(2, required=True)
    isSmoker = messages.BooleanField(3, required=True)
    listenToMusic = messages.BooleanField(4, required=True)
    car = messages.StringField(5, required=True)

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
