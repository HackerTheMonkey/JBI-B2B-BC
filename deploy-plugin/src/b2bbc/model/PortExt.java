/*
 * PortExt.java
 */

package b2bbc.model;

import org.netbeans.modules.xml.wsdl.model.Port;
import org.netbeans.modules.xml.wsdl.model.WSDLModel;
import org.netbeans.modules.xml.xam.Component;
import org.w3c.dom.Element;
import org.w3c.dom.Element;

/**
 * This interface and its implementation represents the java model for service port
 * extension element.
 *
 * @author chikkala
 */
public interface PortExt extends ExtComponent
{
    /**
     * 
     * @return
     */
    public String getBindingAddress();

    /**
     * 
     * @param bindingAddress
     */
    public void setBindingAddress(String bindingAddress);

    /**
     *
     * @return
     */
    public String getBindingProtocol();

    /**
     * 
     * @param bindingProtocol
     */
    public void setBindingProtocol(String bindingProtocol);

    /**
     *
     * @return
     */
    public String getBindingPort();

    /**
     * 
     * @param bindingPort
     */
    public void setBindingPort(String bindingPort);

    
    /**
     * This class is an implementation of PortExt interface that provides java model
     * for service port extensibility element.
     */
    public static class PortExtImpl extends ExtModelImpl implements PortExt
    {
        
        public PortExtImpl(WSDLModel model, Element e)
        {
            super(model, e);
        }
        
        public PortExtImpl(WSDLModel model)
        {
            this(model, createPrefixedElement(QN_PORT_EXT, model));
        }
        
        public void accept(ExtVisitor visitor)
        {
            visitor.visit(this);
        }
        
        @Override
        public boolean canBeAddedTo(Component target)
        {
            if (target instanceof Port)
            {
                return true;
            }
            return false;
        }

        public String getBindingAddress()
        {
            return getAttribute(ExtAttribute.BINDING_ADDRESS);
        }

        public void setBindingAddress(String bindingAddress)
        {
            setAttribute(ATTR_BINDING_ADDRESS, ExtAttribute.BINDING_ADDRESS, bindingAddress);
        }

        public String getBindingProtocol()
        {
            return getAttribute(ExtAttribute.BINDING_PROTOCOL);
        }

        public void setBindingProtocol(String bindingProtocol)
        {
            setAttribute(ATTR_BINDING_PROTOCOL, ExtAttribute.BINDING_PROTOCOL, bindingProtocol);
        }

        public String getBindingPort()
        {
            return getAttribute(ExtAttribute.BINDING_PORT);
        }

        public void setBindingPort(String bindingPort)
        {
            setAttribute(ATTR_BINDING_PORT, ExtAttribute.BINDING_PORT, bindingPort);
        }
        
    }
}
