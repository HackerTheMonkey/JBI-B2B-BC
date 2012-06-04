/*
 * ExtAttribute.java
 */
package b2bbc.model;

import org.netbeans.modules.xml.xam.dom.Attribute;

/**
 * @author chikkala
 */
public enum ExtAttribute implements Attribute, ExtConstants
{
    
    //TODO: define any additional extension elements attributes here
    
    ACTION(ATTR_ACTION),
//    SERVICE_URL(ATTR_SERVICE_URL),
//    USERNAME(ATTR_USERNAME),
//    PASSWORD(ATTR_PASSWORD),
//    MBEAN(ATTR_MBEAN),
    BINDING_PROTOCOL(ATTR_BINDING_PROTOCOL),
    BINDING_ADDRESS(ATTR_BINDING_ADDRESS),
    BINDING_PORT(ATTR_BINDING_PORT);
    
    private String name;
    private Class type;
    private Class subtype;
    
    ExtAttribute(String name)
    {
        this(name, String.class);
    }
    
    ExtAttribute(String name, Class type)
    {
        this(name, type, null);
    }
    
    ExtAttribute(String name, Class type, Class subtype)
    {
        this.name = name;
        this.type = type;
        this.subtype = subtype;
    }
    
    @Override
    public String toString()
    {
        return name;
    }
    
    public Class getType()
    {
        return type;
    }
    
    public String getName()
    {
        return name;
    }
    
    public Class getMemberType()
    {
        return subtype;
    }
}
