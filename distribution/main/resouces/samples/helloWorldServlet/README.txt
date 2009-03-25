This example is little different from others since it demonstrate the use of wise in a web application.

This sample needs a jboss-4.x application server with wise-core.sar deployed. 
To do this just copy wise-core.sar directory in JBoss deployment directory.

To run this sample you have simply to call ant deployTestWS which will deploy a war containing both the "server side" webservice and
the "client" servlet. To call the servlet point your browser to 

http://localhost:8080/HelloWorld/HelloWorldServlet?NAME=superman 

Then change the name of you preferred super hero ;)
Have fun.
