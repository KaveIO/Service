Service
=======
(REST) Service based on Grizzly and Jersey which offers an interface to data 
contained in MongoDB.

This project offers two Interfaces: 
### Data 
 - /v1/data/{applicationId}/{collectionName}
 
### Proxy 
 - /v1/proxy/{applicationId}/{proxyName}
 

Setup
=====
+ JDK 8
+ MongoDB version 3.0.12

## To install from source
* Go to the root folder of the project
* `maven package`
* target/Service-1.0.3-SNAPSHOT-bin contains the Data Service application

## To run the Data Service
Configure you database. Download the package Service-bin.zip. Extract the content 
and run the script. 


```
   bin/service start
```

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
