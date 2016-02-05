# Integration Tests

Testing of the Service proved to be a bit difficult. Internal Unit-test works fine however 
running integration tests is quite tricky. Our inablity to run an nice embeded webserver 
with multiple services (the Service and the ServiceAuth) make this tricky.

In this folder there are some very crude integration tests in python. You will need:

 - A fully configured JBoss
 - A fully configured Mongo 
 - Both of the Services deployed
 - The data of the fixture loaded & some other magic data which I don't don describe here. 

All in all, this is quite unusable still. Or scratch that and call it 'work in progress'.

The main point here now is these things are at least stored somewhere.... 

