require 'java'
import org.jboss.wise.core.client.InvocationResult;
import org.jboss.wise.core.client.WSDynamicClient;
import org.jboss.wise.core.client.WSMethod;
import org.jboss.wise.core.client.factories.WSDynamicClientFactory;
import org.jboss.wise.core.exception.InvocationException;
import org.jboss.wise.core.exception.MCKernelUnavailableException;
import org.jboss.wise.core.exception.MappingException;
import org.jboss.wise.core.exception.WiseConnectionException;
import org.jboss.wise.core.exception.WiseRuntimeException;
import java.util.HashMap;


client = WSDynamicClientFactory.getInstance().getJAXWSClient("http://127.0.0.1:8080/HelloWorldGroovy/HelloWorldWS?wsdl");
method = client.getWSMethod("HelloWorldWSService", "HelloWorldPort", "sayHello");
requestMap = HashMap.new;
requestMap.put "toWhom", "SpiderMan";
result = method.invoke requestMap, NIL;
stringResult = result.getMappedResult NIL, NIL;
printf "%s\n", stringResult;
