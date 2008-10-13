import org.jboss.wise.core.client.InvocationResult;
import org.jboss.wise.core.client.WSDynamicClient;
import org.jboss.wise.core.client.WSMethod;
import org.jboss.wise.core.client.factories.WSDynamicClientFactory;
import org.jboss.wise.core.exception.InvocationException;
import org.jboss.wise.core.exception.MCKernelUnavailableException;
import org.jboss.wise.core.exception.MappingException;
import org.jboss.wise.core.exception.WiseConnectionException;
import org.jboss.wise.core.exception.WiseRuntimeException;


WSDynamicClient client = WSDynamicClientFactory.getInstance().getClient("http://127.0.0.1:8080/HelloWorldGroovy/HelloWorldWS?wsdl");
WSMethod method = client.getWSMethod("HelloWorldWSService", "HelloWorldPort", "sayHello");
HashMap<String, Object> requestMap = new HashMap<String, Object>();
requestMap.put("toWhom", "SpiderMan");
InvocationResult result = method.invoke(requestMap, null);
println(result.getMappedResult(null, null));
