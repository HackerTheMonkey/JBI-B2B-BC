/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.orange.uklab.b2bbc.descriptor.impl;
import com.ibm.wsdl.ServiceImpl;
import com.orange.uklab.b2bbc.descriptor.AbstractJbiDescriptor;
import com.orange.uklab.b2bbc.descriptor.AbstractServiceEndpoint;
import com.orange.uklab.b2bbc.descriptor.ParsingException;
import com.orange.uklab.b2bbc.descriptor.ServiceUnit;
import com.orange.uklab.b2bbc.runtime.contexts.RuntimeComponentContext;
import com.orange.uklab.b2bbc.runtime.contexts.impl.ServiceUnitStore;
import java.io.File;
import java.net.BindException;
import java.net.URI;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.logging.Logger;
import javax.wsdl.Definition;
import javax.wsdl.Port;
import javax.wsdl.WSDLException;
import javax.wsdl.extensions.UnknownExtensibilityElement;
import javax.wsdl.factory.WSDLFactory;
import javax.wsdl.xml.WSDLReader;
import javax.xml.namespace.QName;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.*;

/**
 *
 * @author hasanein
 */
public class JbiDescriptor extends AbstractJbiDescriptor
{   
    private Logger logger;
    private Element jbiXmlElement;
    private int bindingPort = 0;
    private String bindingAddress = null;
    private String bindingProtocol = null;
    
    /**
     *
     * @param descriptorRootPath
     * @param descriptorName
     * @param descriptorType
     */
    public JbiDescriptor(String descriptorRootPath, String descriptorName, String descriptorType)
    {
        super(descriptorRootPath, descriptorName, descriptorType);
        RuntimeComponentContext runtimeComponentContext = RuntimeComponentContext.getInstance();
        logger = runtimeComponentContext.getLogger(this.getClass().getName(), null);
    }

    private String resolveNamespacePrefix(String namepacePrefix)
    {
        return jbiXmlElement.getAttributeNode("xmlns:" + namepacePrefix).getValue();
    }

    /**
     *
     * @return
     * @throws ParsingException
     */
    @Override
    public ServiceUnit parseServiceUnitDescriptor() throws ParsingException
    {        
        String jbiDescriptorVersion;
        URI nameSapce;
        ServiceEndpointImpl[] serviceEndpoints;
        ServiceUnitImpl serviceUnit;
        String status = ServiceUnitStore.SERVICE_UNIT_STATUS_SHUTDOWN;
        boolean isBindingCommponent;
        /**
         * Start Parsing the JBI file
         */        
        try
        {
            File jbiXmlFile = new File(descriptorRootPath + "\\META-INF\\jbi.xml");
            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
            Document doc = documentBuilder.parse(jbiXmlFile);
            this.jbiXmlElement = doc.getDocumentElement();
            jbiXmlElement.normalize();
            /**
             * Start parsing the document.
             */
            jbiDescriptorVersion = jbiXmlElement.getAttributeNode("version").getValue();
            String strNamespace = jbiXmlElement.getAttributeNode("xmlns").getValue();
            nameSapce = new URI(strNamespace);
            Node servicesXmlNode = jbiXmlElement.getElementsByTagName("services").item(0);
            isBindingCommponent = (servicesXmlNode.getAttributes().item(0).getNodeValue().equals("true")) ? true : false;
            logger.info("######### 3service info = " + servicesXmlNode.getChildNodes().item(1).getAttributes().item(1).getPrefix());
            /**
             * Parse the provide/consume services and compose the required
             * ServiceEndpoints accordingly.
             */
            NodeList consumeProvideNodeList = servicesXmlNode.getChildNodes();
            int noOfConsumesProvidesServices = consumeProvideNodeList.getLength();
            ArrayList arrayList = new ArrayList();
            for (int i = 0 ; i < noOfConsumesProvidesServices ; i++)
            {
                ServiceEndpointImpl serviceEndpoint = new ServiceEndpointImpl();
                Node service = consumeProvideNodeList.item(i);

                if((service.getNodeName().equals(AbstractServiceEndpoint.ENDPOINT_TYPE_PROVIDE))||(service.getNodeName().equals(AbstractServiceEndpoint.ENDPOINT_TYPE_CONSUME)))
                {
                    serviceEndpoint.setEndpointName(service.getAttributes().getNamedItem("endpoint-name").getNodeValue());
                    serviceEndpoint.setEndpointType((service.getNodeName().equals(AbstractServiceEndpoint.ENDPOINT_TYPE_PROVIDE)) ? AbstractServiceEndpoint.ENDPOINT_TYPE_PROVIDE : AbstractServiceEndpoint.ENDPOINT_TYPE_CONSUME);
                    String interfaceLocalName = (service.getAttributes().getNamedItem("interface-name").getNodeValue().contains(":")) ? service.getAttributes().getNamedItem("interface-name").getNodeValue().split(":")[1] : service.getAttributes().getNamedItem("interface-name").getNodeValue();                   
                    String interfaceNamespacePrefix = (service.getAttributes().getNamedItem("interface-name").getNodeValue().contains(":")) ? service.getAttributes().getNamedItem("interface-name").getNodeValue().split(":")[0] : null;
                    QName interfaceName = new QName(resolveNamespacePrefix(interfaceNamespacePrefix), interfaceLocalName);
                    serviceEndpoint.setInterfaceNames(new QName[] {interfaceName});
                    String serviceLocalName = (service.getAttributes().getNamedItem("service-name").getNodeValue().contains(":")) ? service.getAttributes().getNamedItem("service-name").getNodeValue().split(":")[1] : service.getAttributes().getNamedItem("service-name").getNodeValue();
                    String serviceNamespacePrefix = (service.getAttributes().getNamedItem("service-name").getNodeValue().contains(":")) ? service.getAttributes().getNamedItem("service-name").getNodeValue().split(":")[0] : null;
                    QName serviceName = new QName(resolveNamespacePrefix(serviceNamespacePrefix), serviceLocalName);
                    logger.info("KKKKKKKKKKKKKKKKK: " + interfaceName.getNamespaceURI());
                    serviceEndpoint.setServiceName(serviceName);
                    serviceEndpoint.setServiceUnitName(descriptorName);
                    arrayList.add(serviceEndpoint);
                }                
            }
            serviceEndpoints = new ServiceEndpointImpl[arrayList.size()];
            serviceEndpoints = (ServiceEndpointImpl[])arrayList.toArray(serviceEndpoints);
            serviceUnit = new ServiceUnitImpl(descriptorName, jbiDescriptorVersion, nameSapce, serviceEndpoints, status, isBindingCommponent);         

            /**
             * TESTING CODE - THE ACTUAL CODE SHOULD BE USING A WSDL
             * PARSER TO PARSE THE REQUIRED FILES.
             */
//            if(serviceUnit.getName().equals("MockSU-B2BBC"))
//            {
//                serviceUnit.setBindingAddress("192.168.1.2");
//                serviceUnit.setBindingPort(1234);
//                serviceUnit.setBindingProtocol("UDP");
//            }
//            else if(serviceUnit.getName().equals("MockSU1-B2BBC"))
//            {
//                serviceUnit.setBindingAddress("192.168.1.2");
//                serviceUnit.setBindingPort(1235);
//                serviceUnit.setBindingProtocol("UDP");
//            }
//            else
//            {
//                serviceUnit.setBindingAddress("192.168.1.2");
//                serviceUnit.setBindingPort(1236);
//                serviceUnit.setBindingProtocol("UDP");
//            }
            parseBindingInformation();
            serviceUnit.setBindingAddress(bindingAddress);
            serviceUnit.setBindingPort(bindingPort);
            serviceUnit.setBindingProtocol(bindingProtocol);
            /**
             * DEBUG OUTPUT
             */            
            return serviceUnit;
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
            throw new ParsingException(ex.getMessage());
        }
    }

    private void parseBindingInformation() throws ParsingException, WSDLException, BindException
    {
        logger.info("Parsing the binding information for ServiceUnit: " + descriptorName);
        String bindingWsdlFilePath = descriptorRootPath + "\\" + descriptorName + ".wsdl";
        WSDLFactory wSDLFactory = WSDLFactory.newInstance();
        WSDLReader wSDLReader = wSDLFactory.newWSDLReader();
        Definition definition = wSDLReader.readWSDL(bindingWsdlFilePath);
        Map bindingsMap = definition.getServices();
        Iterator bindingsMapIterator = bindingsMap.entrySet().iterator();
        /**
         * DEBUG OUTPUT
         */
        logger.info("DEBUGGING: bindingsMap Size: " + bindingsMap.size());
        /**
         * Only one service wsdl element is allowed per a single service unit.
         */
        if(bindingsMap.size() != 1)
        {
            throw new BindException("Only one service wsdl element is allowed per a ServiceUnit");
        }
        /**
         * Get the port(endpoint) contained within the binding
         */
        Map.Entry bindingsMapEntry = (Entry) bindingsMapIterator.next();
        ServiceImpl service = (ServiceImpl) bindingsMapEntry.getValue();
        Map endpointsMap = service.getPorts();

        /**
         * DEBUG OUTPUT
         */
        logger.info("DEBUGGING: endpointsMap size: " + endpointsMap.size());
        /**
         * Only one port (endpoint) is allowed per a single service wsdl element.
         */
        if(endpointsMap.size() != 1)
        {
            throw new BindException("Only one port is allowed per a single ServiceUnit");
        }

        Map.Entry entry = (Entry) endpointsMap.entrySet().iterator().next();
        Port endpoint = (Port) entry.getValue();

        UnknownExtensibilityElement unknownElement = (UnknownExtensibilityElement) endpoint.getExtensibilityElements().get(0);
        Element addressExtElement = unknownElement.getElement();
        /**
         * Obtain the binding information and populate the corresponding
         * fields in the respective ServiceUnit.
         */
        bindingAddress = addressExtElement.getAttribute("bindingAddress");
        bindingPort = Integer.parseInt(addressExtElement.getAttribute("bindingPort"));
        bindingProtocol = addressExtElement.getAttribute("bindingProtocol");
    }
    
}