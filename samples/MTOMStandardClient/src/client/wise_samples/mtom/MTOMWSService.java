
package wise_samples.mtom;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.Logger;
import javax.xml.namespace.QName;
import javax.xml.ws.Service;
import javax.xml.ws.WebEndpoint;
import javax.xml.ws.WebServiceClient;


/**
 * This class was generated by the JAX-WS RI.
 * JAX-WS RI 2.1.3-b02-
 * Generated source version: 2.0
 * 
 */
@WebServiceClient(name = "MTOMWSService", targetNamespace = "http://wise_samples/MTOM", wsdlLocation = "file:/home/oracle/temp/Wise0.xml")
public class MTOMWSService
    extends Service
{

    private final static URL MTOMWSSERVICE_WSDL_LOCATION;
    private final static Logger logger = Logger.getLogger(wise_samples.mtom.MTOMWSService.class.getName());

    static {
        URL url = null;
        try {
            URL baseUrl;
            baseUrl = wise_samples.mtom.MTOMWSService.class.getResource(".");
            url = new URL(baseUrl, "file:/home/oracle/temp/Wise0.xml");
        } catch (MalformedURLException e) {
            logger.warning("Failed to create URL for the wsdl Location: 'file:/home/oracle/temp/Wise0.xml', retrying as a local file");
            logger.warning(e.getMessage());
        }
        MTOMWSSERVICE_WSDL_LOCATION = url;
    }

    public MTOMWSService(URL wsdlLocation, QName serviceName) {
        super(wsdlLocation, serviceName);
    }

    public MTOMWSService() {
        super(MTOMWSSERVICE_WSDL_LOCATION, new QName("http://wise_samples/MTOM", "MTOMWSService"));
    }

    /**
     * 
     * @return
     *     returns MTOM
     */
    @WebEndpoint(name = "MTOMPort")
    public MTOM getMTOMPort() {
        return super.getPort(new QName("http://wise_samples/MTOM", "MTOMPort"), MTOM.class);
    }

}