/*
 * WSDLExtValidatorSchemaFactory.java
 */
package b2bbc.validator;

import java.io.InputStream;
import java.io.InputStream;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import org.netbeans.modules.xml.wsdl.validator.spi.ValidatorSchemaFactory;

/**
 * This class implements ValidatorSchemaFactory interface.
 *
 * @author chikkala
 */
public class WSDLExtValidatorSchemaFactory extends ValidatorSchemaFactory
{
    
    private final String NS_URL =
            "http://java.sun.com/jbi/wsdl-extensions/sample/jmx-bc/";
    private final String wsdlExtXSDResourcePath =
            "/b2bbc/B2BBCWsdlExt.xsd";;
            
            public String getNamespaceURI()
            {
                return NS_URL;
            }
            
            public InputStream getSchemaInputStream()
            {
                return this.getClass().getResourceAsStream(wsdlExtXSDResourcePath);
            }
            /**
             * Returns the Inputstream related to this schema
             */
            public Source getSchemaSource()
            {
                InputStream in = this.getClass().getResourceAsStream(wsdlExtXSDResourcePath);
                Source s = new StreamSource(in);
                s.setSystemId(this.getClass().getResource(wsdlExtXSDResourcePath).toString());
                return s;
            }
            
}
