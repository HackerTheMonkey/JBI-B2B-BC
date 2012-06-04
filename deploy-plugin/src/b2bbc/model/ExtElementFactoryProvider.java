/*
 * ExtElementFactoryProvider
 */

package b2bbc.model;

import java.util.Collections;
import java.util.Set;
import javax.xml.namespace.QName;
import org.netbeans.modules.xml.wsdl.model.WSDLComponent;
import org.netbeans.modules.xml.wsdl.model.spi.ElementFactory;
import org.w3c.dom.Element;
/**
 *  Factory class that has wsdl extension elements model factories
 * @author chikkala
 */
public class ExtElementFactoryProvider
{
    
    public static class BindingExtFactory extends ElementFactory
    {
        public Set<QName> getElementQNames()
        {
            return Collections.singleton(ExtConstants.QN_BINDING_EXT);
        }
        public WSDLComponent create(WSDLComponent context, Element element)
        {
            return new BindingExt.BindingExtImpl(context.getModel(), element);
        }
    }
    public static class OperationExtFactory extends ElementFactory
    {
        public Set<QName> getElementQNames()
        {
            return Collections.singleton(ExtConstants.QN_OPERATION_EXT);
        }
        public WSDLComponent create(WSDLComponent context, Element element)
        {
            return new OperationExt.OperationExtImpl(context.getModel(), element);
        }
    }
    public static class InputExtFactory extends ElementFactory
    {
        public Set<QName> getElementQNames()
        {
            return Collections.singleton(ExtConstants.QN_INPUT_EXT);
        }
        public WSDLComponent create(WSDLComponent context, Element element)
        {
            return new InputExt.InputExtImpl(context.getModel(), element);
        }
    }
    public static class OutputExtFactory extends ElementFactory
    {
        public Set<QName> getElementQNames()
        {
            return Collections.singleton(ExtConstants.QN_OUTPUT_EXT);
        }
        public WSDLComponent create(WSDLComponent context, Element element)
        {
            return new OutputExt.OutputExtImpl(context.getModel(), element);
        }
    }
    public static class FaultExtFactory extends ElementFactory
    {
        public Set<QName> getElementQNames()
        {
            return Collections.singleton(ExtConstants.QN_FAULT_EXT);
        }
        public WSDLComponent create(WSDLComponent context, Element element)
        {
            return new FaultExt.FaultExtImpl(context.getModel(), element);
        }
    }
    public static class PortExtFactory extends ElementFactory
    {
        public Set<QName> getElementQNames()
        {
            return Collections.singleton(ExtConstants.QN_PORT_EXT);
        }
        public WSDLComponent create(WSDLComponent context, Element element)
        {
            return new PortExt.PortExtImpl(context.getModel(), element);
        }
    }
    
    
}
