/*
 * InputExt.java
 */

package b2bbc.model;

import org.netbeans.modules.xml.wsdl.model.BindingInput;
import org.netbeans.modules.xml.wsdl.model.WSDLModel;
import org.netbeans.modules.xml.xam.Component;
import org.w3c.dom.Element;
import org.w3c.dom.Element;

/**
 * This interface and its implementation represents the java model for binding
 * operation input extension element.
 *
 * @author chikkala
 */
public interface InputExt extends ExtComponent
{
    
    //TODO: define get/set methods for properties for InputExt if the extension element has attributes.
    /**
     * This class is an implementation of InputExt interface that provides java model
     * for binding operation input extensibility element.
     */
    public static class InputExtImpl extends ExtModelImpl implements InputExt
    {
        
        public InputExtImpl(WSDLModel model, Element e)
        {
            super(model, e);
        }
        
        public InputExtImpl(WSDLModel model)
        {
            this(model, createPrefixedElement(QN_INPUT_EXT, model));
        }
        
        public void accept(ExtVisitor visitor)
        {
            visitor.visit(this);
        }
        
        @Override
        public boolean canBeAddedTo(Component target)
        {
            if (target instanceof BindingInput)
            {
                return true;
            }
            return false;
        }
    }
}
