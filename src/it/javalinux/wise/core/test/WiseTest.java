package it.javalinux.wise.core.test;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import javax.management.ObjectName;
import org.apache.log4j.Logger;
import org.jboss.wsf.test.JBossWSTestHelper;

/**
 * Wise test base class. Subclass can use the methods in this class to 
 * deploy and undeploy a web service war in JBossAS  
 * @author ema@redhat.com
 *
 */
public class WiseTest {
    private static final Logger logger = Logger.getLogger(WiseTest.class);
    private static final String MAIN_DEPLOYER = "jboss.system:service=MainDeployer";
    private static final String WS_SERVER_CONFIG = "jboss.ws:service=ServerConfig";
    
    /**
     * Deploy the webservice war in JBoss server
     * @param url url for webservice war 
     * @throws Exception if the deployment is failed
     */
    public void deployWS(URL url) throws Exception {      
        JBossWSTestHelper.getServer().invoke(new ObjectName(MAIN_DEPLOYER), "deploy", new Object[] { url }, new String[] { "java.net.URL" });       
    }
    
    /**Undeploy a webservice
     * @param url url of webservice war 
     * @throws Exception if undeployment is failed
     */
    public void undeployWS(URL url) throws Exception {
        JBossWSTestHelper.getServer().invoke(new ObjectName(MAIN_DEPLOYER), "undeploy", new Object[] { url }, new String[] { "java.net.URL" });       
    }
    
    /**Get the URL path for a given webservice war. It will find this war file under ${baseDir}/build/webservice-wars
     * @param warName the webservice war name
     * @return webservice war url
     */
    public URL getWarUrl( String warName ) {
         URL warUrl = null;
         URL dirURL = this.getClass().getClassLoader().getResource(".");
         File file = new File(dirURL.getFile(), "../webservice-wars/" + warName);
         if (file.exists()) {
             try {
                 warUrl = file.getAbsoluteFile().toURL();
             } catch (MalformedURLException e) {
                 return null;
             }
         } else {
             warUrl = null;
         }
         return warUrl;

     }
    
    /**Get the jboss webservice server side hostname and port 
     * @return http://server-hostname:port
     */
    public String getServerHostAndPort() {
        return "http://" + JBossWSTestHelper.getServerHost() + ":" + getServerPort();
    }
    
    /**Get the web service server port
     * @return webservice server configured port
     */
    public String getServerPort() {
        
        try {
             return JBossWSTestHelper.getServer().getAttribute(new ObjectName(WS_SERVER_CONFIG), "WebServicePort").toString();
         } catch (Exception e) {
             logger.warn("WARNING: Failed to get server port and use default 8080");
             return "8080";
         } 
    }


 }
