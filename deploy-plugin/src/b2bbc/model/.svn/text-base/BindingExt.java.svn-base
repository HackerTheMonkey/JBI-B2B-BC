/*
 * BindingExt.java
 */

package b2bbc.model;

import org.netbeans.modules.xml.wsdl.model.Binding;
import org.netbeans.modules.xml.wsdl.model.WSDLModel;
import org.netbeans.modules.xml.xam.Component;
import org.w3c.dom.Element;
import org.w3c.dom.Element;

/**
 * This interface and its implementation represents the java model for binding extension element.
 *
 * @author chikkala
 */
public interface BindingExt extends ExtComponent
{
    
    //TODO: define get/set methods for properties for BindingExt if the extension element has attributes.
    /**
     * This class is an implementation of BindingExt interface that provides java model
     * for binding extensibility element.
     */
    public static class BindingExtImpl extends ExtModelImpl implements BindingExt
    {
        
        public BindingExtImpl(WSDLModel model, Element e)
        {
            super(model, e);
        }
        
        public BindingExtImpl(WSDLModel model)
        {
            this(model, createPrefixedElement(QN_BINDING_EXT, model));
        }
        
        public void accept(ExtVisitor visitor)
        {
            visitor.visit(this);
        }
        
        @Override
        public boolean canBeAddedTo(Component target)
        {
            if (target instanceof Binding)
            {
                return true;
            }
            return false;
        }
    }
}
