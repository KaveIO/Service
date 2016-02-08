# Integration Tests

This folder contains the integration tests for the Service. These are written are written as a python module which can be run via nosetests 

 - https://nose.readthedocs.org/en/latest/

The principle of these tests is that they run against different sets of environments. This makes these tests quite difficult to set up. The \_\_init\_\_.py takes care of a big part of the common fixture loading in mongo. Currently the responsibilities of separate integration tests aren't completely separated. 

## Requirements
These tests all require a different environment to test its integration against.

### Base 
 - A completely deployed Service on a JBoss on 127.0.0.1. 
 - A MongoDB on 127.0.0.1 with
   - Authentication enabled 
   - An user named 'admin' with password 'admin' with roles: readWriteAnyDatabase, dbAdminAnyDatabase, userAdminAnyDatabase

### test_oauth.py
 -  A completely deployed ServiceAuthKave on a JBoss on 127.0.0.1.

### test_v1_data.py
 -  Provided by base (fixture should be decoupled)
 
### test_v1_proxy.py
 -  Provided by base (fixture should be decoupled)
