/*
 * ExtConstants.java
 *
 */

package b2bbc.model;

import javax.xml.namespace.QName;

/**
 * This interface defines the constants for the namespace, elements, attributes and qnames
 * corresponding to the wsdl extensions that this component processes. These constants will be
 * used in the wsdl extension model used to process a particular wsdl extensions.
 *
 * @author chikkala
 */
public interface ExtConstants
{
    public static final String NS_URI_WSDL = "http://schemas.xmlsoap.org/wsdl/";
    public static final String NS_URI_XMLNS = "http://www.w3.org/2000/xmlns/";
    /** wsdl extension namespace processed by this wsdl extension model */
    public static final String NS_URI = "http://java.sun.com/jbi/wsdl-extensions/sample/jmx-bc/";
    public static final String NS_DEF_PREFIX = "jmxbc";
    // wsdl extension element names.
    public static final String EL_BINDING_EXT = "binding";
    public static final String EL_OPERATION_EXT = "operation";
    public static final String EL_INPUT_EXT = "input";
    public static final String EL_OUTPUT_EXT = "output";
    public static final String EL_FAULT_EXT = "fault";
    public static final String EL_PORT_EXT = "address";
    
    // Qualified wsdl extension element names.
    public static final QName QN_BINDING_EXT = new QName(NS_URI, EL_BINDING_EXT);
    public static final QName QN_OPERATION_EXT = new QName(NS_URI, EL_OPERATION_EXT);
    public static final QName QN_INPUT_EXT = new QName(NS_URI, EL_INPUT_EXT);
    public static final QName QN_OUTPUT_EXT = new QName(NS_URI, EL_OUTPUT_EXT);
    public static final QName QN_FAULT_EXT = new QName(NS_URI, EL_FAULT_EXT);
    public static final QName QN_PORT_EXT = new QName(NS_URI, EL_PORT_EXT);
    
    //TODO: define any additional extension element attribute names here.
    // wsdl extension elements attribute names.
    public static final String ATTR_ACTION = "action";
//    public static final String ATTR_SERVICE_URL = "serviceURL";
//    public static final String ATTR_USERNAME = "username";
//    public static final String ATTR_PASSWORD = "password";
//    public static final String ATTR_MBEAN = "mbean";
    /**
     * Following is the B2B WSDL Extension Elements attributes definition
     * added for the Binding Extention Element.
     */
    public static final String ATTR_BINDING_PROTOCOL = "bindingProtocol";
    public static final String ATTR_BINDING_PORT = "bindingPort";
    public static final String ATTR_BINDING_ADDRESS = "bindingAddress";

    
}
