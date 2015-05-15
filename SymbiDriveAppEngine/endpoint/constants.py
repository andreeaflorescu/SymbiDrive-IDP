'''
Created on May 15, 2015

@author: alex
'''

import endpoints

WEB_CLIENT_ID = 'replace this with your web client application ID'
ANDROID_CLIENT_ID = 'replace this with your Android client ID'
ANDROID_AUDIENCE = WEB_CLIENT_ID

symbidrive_api = endpoints.api(name='symbidrive', version='v1'
                               allowed_client_ids=[WEB_CLIENT_ID, ANDROID_CLIENT_ID,
                                                   endpoints.API_EXPLORER_CLIENT_ID],
                               audiences=[ANDROID_AUDIENCE],
                               scopes=[endpoints.EMAIL_SCOPE])

APPLICATION = endpoints.api_server([symbidrive_api])
