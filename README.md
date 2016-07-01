Service
=======

(REST) Service based on JBoss RESTeasy, which offers an interface to the data contained in MongoDB.

NOTE: use JDK 1.7 for JBoss server (like all packages)!!!
Eclipse plug-in: Window, Preferences, Server, Runtime Environment, Edit, JRE

Contains two versions of our data interface. 

### v0 
 - /Service/{applicationId}/layer/{layerName}
 - /Service/{applicationId}/nodes/{nodesName}
 - /Service/{applicationId}/edges/{edgesName}
 - /Service/{applicationId}/input/{inputName}

### v1 
 - /Service/v1/filter/{applicationId}/{collection} GET and POST
 - /Service/v1/data/{applicationId}/{collection} only POST

The v1 interface is still under heavy development however perfectly usable.


Setup
=====
+ Ubuntu 14.04
+ JDK 7 or JDK 8
+ MongoDB version 3.0.12

## To install from source
* Go to the root folder of the project
* `maven install`
* target/Service-bin.zip contains the Data Service application

## To run the Data Service
Download the package Service-bin.zip. Extract the content and run the script run.sh.
Alternatively:
* Open a terminal windows and execute `cd path/to/service`
* `java -jar Service.jar`

Configuration Files
==================
There are two files to configure:
+ application.properties:
    * ds.server.name={name of the application, default is "DataService"}
    * ds.server.host={host of the server, default is "localhost"}
    * ds.server.port={port, default is "9000"}

+ mongo.properties: configuration of the mongo security database.
    * mongodb.url
    * mongodb.port
    * mongodb.username
    * mongodb.password
    * mongodb.database
