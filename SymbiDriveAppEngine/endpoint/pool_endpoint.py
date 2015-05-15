'''
Created on May 15, 2015

@author: alex
'''

import endpoints
from protorpc import messages
from protorpc import message_types
from protorpc import remote
from endpoint.constants import symbidrive_api
from controller import pool_controller
from google.appengine.ext import ndb
import datetime

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
    start_point_lat = messages.FloatField(1, required=True)
    start_pointsource_point_lon = messages.FloatField(2, required=True)
    end_point_lat = messages.FloatField(3, required=True)
    end_point_lon = messages.FloatField(4, required=True)
    date = message_types.DateTimeField(5, required=True)
    delta = message_types.DateTimeField(6, required=True)
    walking_distance = messages.FloatField(7, required=False)
   
class SinglePoolResponse(messages.Message):
    driver_id = messages.StringField(1, required=True)
    source_point_lat = messages.FloatField(2, required=True)
    source_point_lon = messages.FloatField(3, required=True)
    destination_point_lat = messages.FloatField(4, required=True)
    destination_point_lon = messages.FloatField(5, required=True)
    date = message_types.DateTimeField(6, required=True)
    seats = messages.IntegerField(7, required=True)

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
        destination_point = ndb.GeoPt(request.destination_point_lat. request.destination_point_lon)
        date = message_types.DateTimeField().value_from_message(request.date)
        if request.is_weekly is None:
            is_weekly = False
        else:
            is_weekly = True

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
        start_point = ndb.GeoPt(request.start_point_lat, request.start_point_lon)
        end_point = ndb.GeoPt(request.end_point_lat. request.end_point_lon)
        date = message_types.DateTimeField().value_from_message(request.date)
        delta_ = message_types.DateTimeField().value_from_message(request.delta)
        delta = datetime.timedelta(hours=delta_.hours, minutes=delta_.minutes)
        if request.walking_distance is None:
            walking_distance = 1000
        else:
            walking_distance = request.walking_distance

        pools_ = pool_controller.find_pool(start_point, end_point, date, delta, walking_distance)
        
        pools = []
        for pool_ in pools_:
            pool = SinglePoolResponse(driver_id=pool_.driver_id,
                                      source_point_lat=pool_.source_point.lat,
                                      source_point_lon=pool_.source_point.lon,
                                      destination_point_lat=pool_.destination_point.lat,
                                      destination_point_lon=pool_.destination_point.lon,
                                      date=message_types.DateTimeField().value_to_message(pool_.date),
                                      seats=pool_.seats)
            pools.append(pool)
        
        return FindPoolResponse(pools=pools)
