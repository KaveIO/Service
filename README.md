Service
=======

(REST) Service based on JBoss RESTeasy, which offers an
interface to the data contained in MongoDB.

At the moment contains only the "/layer/{n}" sevice, which also serves as an example.


To Play
=======
To test simple queries this does the trick:
+ install READ Console Chrome app in Chrome (just Google it)
+ setup a tunnel from your local machine at port 8080 to the remote Service at BitBrains:

    	ssh -N -L 8080:tom-001:8080 bitbrains

+ In the REST Console app set the following fields:
   + Target:
      + Request URI: localhost:8080/Service/layer/twitter
      + Request Method: POST
   + Body: 
      + Content Type: application/json
      + RAW Body: see syntax below

Setup
=====
+ Ubuntu 13.04
+ JDK 1.7u25
+ Eclipse Kepler + JBoss Dev Tools for Juno
+ JBoss AS 7.1.1 Community Edition (Can be installed and run from within Eclipse)
+ Tunnel to mdb-001: ssh -t -L 27017:localhost:27017 user@94.143.211.214 'ssh -L 27017:localhost:27017 user@mdb-001'

To Test
=======
+ Create tunnel
+ CO source
+ mvn clean package
+ copy war to jboss-as-7.1.1.Final/standalone/deployments/.
+ jboss-as-7.1.1.Final #> sh bin/standalone.sh
+ or just deploy it to a server from inside Eclipse (easier)
+ goto http://localhost:8080/Services/rest/layer/twitter.json

To Deploy On BB
===============
(Note this has not been tested)
+ Comment line 21 and uncomment line 22 in AbstractDao.java. I (Jan Amoraal) need to "fix" the code to use properties.

        POST /Service//layer/LAYER_NAME
        {
        
        		"sort": int          # The result set is always sorted on the timestamp. Value 1 for ascending, value -1 for Descending. default 1
        		"limit": int         # The maximum amount of records the result set can have. 0 is interpreted as infinite. default 0
        		"filter" {
        			"timestamp": {
        				"pastwindow": int           # Only returns items which have a timestamp less than 'age' ago. default not filtered
        				"after": int         #
        				"before": int        #
        			}
        
        			"location": {
        				"near": {
        					"geometry" : GeoJSON Point
        					"distance": Double
        				},
        				"within": {
        					"geometry" : GeoJSON Polygon
        				}
        			}
        
        			"relation": {
        				"type": "node" | "edge"
        				"ids": [ObjectId]
        			}
        		}
        	}
        }
