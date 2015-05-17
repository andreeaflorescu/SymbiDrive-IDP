'''
Created on Apr 28, 2015

@author: andreea
'''
from model.user import User
from model.deleted_user import DeletedUser, SocialIdentifier
import datetime
from model.pool import Pool

'''
    The results of this function will be checked by the wrapper module
    A "bad_user" will receive a push notification to inform him/her that the 
    negative rating has led to his/her exclusion from the app users
'''    
def check_users():
    users = User.query().fetch(1000)
    bad_users = []
    for user in users:
        if (user.rating is None):
            pass
        elif (user.rating.number < 5):
            pass
        elif (user.rating.average < 2.5):
            deletedUser = DeletedUser(SocialIdentifier = 
                        SocialIdentifier(socialID = user.socialProfile.socialID,
                                        profile = user.socialProfile.profile),
                        deviceID = user.deviceID)
            deletedUser.put()
            
            user.key.delete()
            bad_users.append(deletedUser)

    
    return bad_users

def delete_old_pools():
    yestarday = datetime.datetime.now() - datetime.timedelta(days=1)
    Pool.query(Pool.date <= yestarday).fetch(1000).delete_all()


    