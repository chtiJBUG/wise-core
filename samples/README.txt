Wise Invoke Service Easily samples

Here you find a set of samples demonstrating how to use wise in a standalone application
Any directory, except lib and ant ones, contains a single example. Directory name would suggest
which kind of test you will find in. Only in case we think example needs a further explanation you 
will find a local README.txt inside its directory.

lib directory contains library referred by examples. ant directory contains build.xml imported from
all examples' build.xml.

How to run examples?

1. Enter in specific example directory ;)
2. Edit build.properties changing "JBossHome" and "ServerConfig" property to point to your JBossAS instance
3. Start your JBossAS instance (of course it have to provide JBossWS)
4. type "ant deployTestWS" to deploy server side content (aka the ws against example will run)
5. type "ant runTest" to run the client side example
6. type "ant undeployTestWS" to undeploy server side content
7. Have a look to the code.

If something changes for a specific example you will find instructions on local README.txt

have fun.
 