
package wise_samples.mtom;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the wise_samples.mtom package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {

    private final static QName _Result_QNAME = new QName("http://wise_samples/MTOM", "result");
    private final static QName _ToWhom_QNAME = new QName("http://wise_samples/MTOM", "toWhom");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: wise_samples.mtom
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link byte[]}{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://wise_samples/MTOM", name = "result")
    public JAXBElement<byte[]> createResult(byte[] value) {
        return new JAXBElement<byte[]>(_Result_QNAME, byte[].class, null, ((byte[]) value));
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://wise_samples/MTOM", name = "toWhom")
    public JAXBElement<String> createToWhom(String value) {
        return new JAXBElement<String>(_ToWhom_QNAME, String.class, null, value);
    }

}
