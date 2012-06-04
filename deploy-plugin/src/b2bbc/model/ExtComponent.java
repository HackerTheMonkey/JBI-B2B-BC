/*
 * ExtComponent.java
 */

package b2bbc.model;

import org.netbeans.modules.xml.wsdl.model.ExtensibilityElement;
import org.netbeans.modules.xml.wsdl.model.WSDLModel;
import org.netbeans.modules.xml.wsdl.model.spi.GenericExtensibilityElement;
import org.w3c.dom.Element;

/**
 * This is the base class for the extension model
 * @author chikkala
 */
public interface ExtComponent extends ExtConstants, ExtensibilityElement
{
    
    void accept(ExtVisitor visitor);
    /**
     *  this class is the base class for the extension model objects
     */
    public static abstract class ExtModelImpl
            extends GenericExtensibilityElement implements ExtComponent
    {
        
        public ExtModelImpl(WSDLModel model, Element e)
        {
            super(model, e);
        }
        
        @Override
        protected String getNamespaceURI()
        {
            return NS_URI;
        }
    }
}
