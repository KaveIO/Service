#!/bin/bash
# This script populate data in a docker container with a mongo database. The server currently relies on 
# mongo and this can help fulfilling this requirement in demo scenarios. This is NOT MEANT 
# FOR PRODUCTION PURPOSES. This is for easy testing and demo purposes only. 

DOCKER_NAME="service-mongo"

docker exec -t $DOCKER_NAME mongo -eval 'db.createUser({user: "sec", pwd: "sec",roles: [ { role: "read", db: "sec" } ]})' localhost/sec
docker exec -t $DOCKER_NAME mongo -eval 'db.createUser({user: "test", pwd: "test",roles: [ { role: "read", db: "test" }]})' localhost/test

docker exec -t $DOCKER_NAME mongo -eval 'db.applications.insert({"name" : "test", "database" : { "username" : "test", "host" : "localhost", "password" : "test", "port" : 27017, "database" : "test" }})' localhost/sec
docker exec -t $DOCKER_NAME mongo -eval 'db.users.insert({"username" : "test", "password" : "test", "roles" : ["test.user"]})' localhost/sec

docker exec -t $DOCKER_NAME mongo -eval 'db.roles.insert({ "name": "user", "deny": [ { "service": "data", "resource": "trilaterationFitterLayer", "rights": "*" } ], "allow": [ { "service": "data", "resource": "*", "rights": "crd"}]})' localhost/test
docker exec -t $DOCKER_NAME mongo -eval 'db.roles.insert({ "name": "admin", "deny": [ ], "allow": [ { "service": "data", "resource": "*", "rights": "*" }, { "service": "proxy", "resource": "*", "rights": "*" } ] })' localhost/test

