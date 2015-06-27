'''
Created on May 15, 2015

@author: andreea
'''

import endpoints

from protorpc import messages
from protorpc import message_types
from protorpc import remote

from google.appengine.ext import ndb
import datetime
from controller.route_controller import create_route, get_user_routes
from controller import user_controller, pool_controller


symbidrive_api = endpoints.api(name='symbidrive', version='v1.1')

package = 'Symbidrive'

class RegisterUserRequest(messages.Message):
    deviceID = messages.StringField(1, required=True)
    socialID = messages.StringField(2, required=True)
    profile = messages.StringField(3, required=True)
    username = messages.StringField(4, required=True)
    
class UserInfoRequest(messages.Message):
    socialID = messages.StringField(1, required=True)
    
class UserInfoResponse(messages.Message):
    username = messages.StringField(1, required=True)
    telephone = messages.StringField(2, required=True)
    isSmoker = messages.BooleanField(3, required=True)
    listenToMusic = messages.BooleanField(4, required=True)
    car = messages.StringField(5, required=True)
    rating = messages.FloatField(6, required=True)
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
                                rating=info['rating'],
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
    source_point_lat = messages.FloatField(2, required=False)
    source_point_lon = messages.FloatField(3, required=False)
    route_id = messages.IntegerField(4, required=False)
    destination_point_lat = messages.FloatField(5, required=True)
    destination_point_lon = messages.FloatField(6, required=True)
    date = message_types.DateTimeField(7, required=True)
    seats = messages.IntegerField(8, required=True)
    is_weekly = messages.BooleanField(9, required=False)
    
class DeletePoolRequest(messages.Message):
    pool_id = messages.IntegerField(1, required=True)
    
class ManagePassangerRequest(messages.Message):
    pool_id = messages.IntegerField(1, required=True)
    passenger_id = messages.StringField(2, required=True)
    
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
    destination_point_lat = messages.FloatField(4, required=False)
    destination_point_lon = messages.FloatField(5, required=False)
    route_id = messages.IntegerField(6, required = False)
    date = message_types.DateTimeField(7, required=True)
    seats = messages.IntegerField(8, required=True)
    pool_id = messages.IntegerField(9, required=True)
    score = messages.IntegerField(10, required=True)

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
        if request.is_weekly is None:
            is_weekly = False
        else:
            is_weekly = request.is_weekly
        date = request.date.replace(tzinfo=None)
            
        if request.source_point_lat is None or \
           request.source_point_lon is None or \
           request.destination_point_lat is None or \
           request.destination_point_lon is None:
            return PoolResponse(ret=pool_controller.create_pool(request.driver_id,
                                                            None,
                                                            None,
                                                            request.route_id,
                                                            date,
                                                            request.seats,
                                                            is_weekly))
        else:
            source_point = ndb.GeoPt(request.source_point_lat, request.source_point_lon)
            destination_point = ndb.GeoPt(request.destination_point_lat, request.destination_point_lon)
            return PoolResponse(ret=pool_controller.create_pool(request.driver_id,
                                                            source_point,
                                                            destination_point,
                                                            None,
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

        find_pool_result = pool_controller.find_pool(socialID, start_point, end_point, date, delta, walking_distance)
        
        pools = []
        
        if find_pool_result["pools"] is None:
            return FindPoolResponse(pools=[])
        
        for i in range(len(find_pool_result["pools"])):
            res = find_pool_result["pools"][i]
            score = find_pool_result["scores"][i]
            pool = SinglePoolResponse(driver_id=res.driver_socialID,
                                      source_point_lat=res.source_point.lat,
                                      source_point_lon=res.source_point.lon,
                                      destination_point_lat=res.destination_point.lat,
                                      destination_point_lon=res.destination_point.lon,
                                      route_id= res.route_id,
                                      date=res.date,
                                      seats=res.seats,
                                      pool_id=res.key.id(),
                                      score=score
                                      )
            pools.append(pool)
        
        return FindPoolResponse(pools=pools)

class CreateRouteRequest(messages.Message):
#     p_name, p_driver_socialID, p_route_points
    name = messages.StringField(1, required=True)
    driver_socialID = messages.StringField(2, required=True)
    route_points_lat = messages.FloatField(3, repeated=True)
    route_points_long = messages.FloatField(4, repeated=True)
    
class CreateRouteResponse(messages.Message):
    ret = messages.StringField(1, required=True)
    

class GetRoutesRequest(messages.Message):
    driver_socialID = messages.StringField(1, required=True)

class SingleGetRoutesResponse(messages.Message):
    name = messages.StringField(1, required=True)
    driver_socialID = messages.StringField(2, required=True)
    route_points_lat = messages.FloatField(3, repeated=True)
    route_points_long = messages.FloatField(4, repeated=True)


class GetRoutesResponse(messages.Message):
    routes = messages.MessageField(SingleGetRoutesResponse, 1, repeated=True)

@symbidrive_api.api_class(path='route')
class Route_endpoint(remote.Service):
    '''
        Expose route_controller operations
    '''
    @endpoints.method(CreateRouteRequest, CreateRouteResponse,
                      path='create_route', http_method='POST')
    def create_route(self, request):
        # create route points array
        route_points = []
        for i in range(len(request.route_points_lat)):
            route_points.append(ndb.GeoPt(request.route_points_lat[i], request.route_points_long[i]))
        return CreateRouteResponse(ret = create_route(request.name, 
                                                      request.driver_socialID, 
                                                      route_points))
    @endpoints.method(GetRoutesRequest, GetRoutesResponse,
                      path='get_routes', http_method='POST')    
    def get_routes(self, request):
        routes = get_user_routes(request.driver_socialID)
        if routes is None:
            return GetRoutesResponse(routes=[])
        routes_response = []
        for route in routes:
            route_points_lat = []
            route_points_long = []
            for point in route.route_points:
                route_points_lat.append(point.lat)
                route_points_long.append(point.lon)
            routes_response.append(SingleGetRoutesResponse(name=route.name, 
                                                           driver_socialID=route.driver_socialID, 
                                                           route_points_lat=route_points_lat,
                                                           route_points_long=route_points_long))
        return GetRoutesResponse(routes=routes_response)
            
APPLICATION = endpoints.api_server([symbidrive_api])
