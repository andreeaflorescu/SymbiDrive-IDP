'''
Created on Jun 27, 2015

@author: andreea
'''
from GenerateUser import UserManager
from GeneratePools import PoolManager
from FindPools import FindPools



def generate_users():
    userManager = UserManager()
    for i in range(1000):
        userManager.send_user_request()

def generate_and_join_pool():
    createPoolManager = PoolManager()
    findPoolManager = FindPools()
    steps = [1000, 1500, 2000, 3000, 4000, 5000]
    
    for i in range(4000):
        if i in steps:
            createPoolManager.generate_pool_for_match()
            createPoolManager.generate_pool_for_match()
            findPoolManager.find_pools_small_response(createPoolManager.get_number_of_pool())
            findPoolManager.find_pools_large_response(createPoolManager.get_number_of_pool())
        createPoolManager.generate_random_pool()

# generate_and_join_pool()
findPoolManager = FindPools()
findPoolManager.find_pools_small_response(2000)
findPoolManager.find_pools_large_response(2000)