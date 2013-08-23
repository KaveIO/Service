Service
=======

(REST) Service based on JBoss RESTeasy, which offers an 
interface to the data contained in MongoDB.

At the moment contains only the "/layer/{n}" sevice, which also serves as an example.

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


