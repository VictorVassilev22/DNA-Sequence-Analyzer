
package webSrc;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the webSrc package. 
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

    private final static QName _ReadSample_QNAME = new QName("http://FileReader/", "readSample");
    private final static QName _ReadSampleResponse_QNAME = new QName("http://FileReader/", "readSampleResponse");
    private final static QName _IOException_QNAME = new QName("http://FileReader/", "IOException");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: webSrc
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link ReadSample }
     * 
     */
    public ReadSample createReadSample() {
        return new ReadSample();
    }

    /**
     * Create an instance of {@link ReadSampleResponse }
     * 
     */
    public ReadSampleResponse createReadSampleResponse() {
        return new ReadSampleResponse();
    }

    /**
     * Create an instance of {@link IOException }
     * 
     */
    public IOException createIOException() {
        return new IOException();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ReadSample }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://FileReader/", name = "readSample")
    public JAXBElement<ReadSample> createReadSample(ReadSample value) {
        return new JAXBElement<ReadSample>(_ReadSample_QNAME, ReadSample.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ReadSampleResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://FileReader/", name = "readSampleResponse")
    public JAXBElement<ReadSampleResponse> createReadSampleResponse(ReadSampleResponse value) {
        return new JAXBElement<ReadSampleResponse>(_ReadSampleResponse_QNAME, ReadSampleResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link IOException }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://FileReader/", name = "IOException")
    public JAXBElement<IOException> createIOException(IOException value) {
        return new JAXBElement<IOException>(_IOException_QNAME, IOException.class, null, value);
    }

}
