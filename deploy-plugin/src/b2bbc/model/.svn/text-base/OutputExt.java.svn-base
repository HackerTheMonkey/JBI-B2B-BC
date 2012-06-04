/*
 * OutputExt.java
 */

package b2bbc.model;

import org.netbeans.modules.xml.wsdl.model.BindingOutput;
import org.netbeans.modules.xml.wsdl.model.WSDLModel;
import org.netbeans.modules.xml.xam.Component;
import org.w3c.dom.Element;
import org.w3c.dom.Element;

/**
 * This interface and its implementation represents the java model for binding operation
 * output extension element.
 *
 * @author chikkala
 */
public interface OutputExt extends ExtComponent
{
    
    //TODO: define get/set methods for properties for OutputExt if the extension element has attributes.
    /**
     * This class is an implementation of OutputExt interface that provides java model
     * for binding operation output extension element.
     */
    public static class OutputExtImpl extends ExtModelImpl implements OutputExt
    {
        
        public OutputExtImpl(WSDLModel model, Element e)
        {
            super(model, e);
        }
        
        public OutputExtImpl(WSDLModel model)
        {
            this(model, createPrefixedElement(QN_OUTPUT_EXT, model));
        }
        
        public void accept(ExtVisitor visitor)
        {
            visitor.visit(this);
        }
        @Override
        public boolean canBeAddedTo(Component target)
        {
            if (target instanceof BindingOutput)
            {
                return true;
            }
            return false;
        }
    }
}
