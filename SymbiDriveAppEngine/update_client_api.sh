#!/bin/bash
endpointscfg.py get_client_lib java -bs gradle -o . symbidrive_api.User_endpoint symbidrive_api.Pool_endpoint symbidrive_api.Route_endpoint
unzip symbidrive-v1.1.zip
rm -r ../SymbiDrive/symbidrive
mv symbidrive ../SymbiDrive
