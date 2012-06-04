/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.orange.uklab.b2bbc.wsdlparsing.impl;

import com.ibm.wsdl.DefinitionImpl;
import com.ibm.wsdl.MessageImpl;
import com.ibm.wsdl.PartImpl;
import com.ibm.wsdl.factory.WSDLFactoryImpl;
import com.ibm.wsdl.xml.WSDLReaderImpl;
import com.orange.uklab.b2bbc.runtime.contexts.RuntimeComponentContext;
import com.orange.uklab.b2bbc.wsdlparsing.MessageDeNormalizer;
import com.orange.uklab.b2bbc.wsdlparsing.MessageNormalizer;
import gov.nist.javax.sip.header.RequestLine;
import gov.nist.javax.sip.header.SIPHeader;
import gov.nist.javax.sip.header.StatusLine;
import gov.nist.javax.sip.message.SIPMessage;
import gov.nist.javax.sip.message.SIPRequest;
import gov.nist.javax.sip.message.SIPResponse;
import gov.nist.javax.sip.parser.HeaderParser;
import gov.nist.javax.sip.parser.RequestLineParser;
import gov.nist.javax.sip.parser.StatusLineParser;
import java.util.Iterator;
import java.util.ListIterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.namespace.QName;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

/**
 *
 * @author hasanein
 */
public class MessageTransformer implements MessageDeNormalizer, MessageNormalizer
{
    private Logger logger = null;
    private String defaultJbiMessageNamespace = "http://java.sun.com/xml/ns/jbi/wsdl-11-wrapper";    

    /**
     *
     */
    public MessageTransformer()
    {
        initLogger();
    }

    private void initLogger()
    {
        logger = RuntimeComponentContext.getInstance().getLogger(this.getClass().getName(), null);
    }    
    
    /**
     *
     * @param sipMessage
     * @return
     */
    @Override
    public Source normalizeSipMessage(SIPMessage sipMessage)
    {                
        DOMSource dOMSource = new DOMSource();
        /**
         * To determine the type of the SipMessage that we are receiving (request
         * or response), we need to use java reflection API.
         */
        String className = sipMessage.getClass().getName();
        if(className.equals("gov.nist.javax.sip.message.SIPRequest"))
        {
            /**
             * Request
             */
            dOMSource = (DOMSource) normalizeSipRequest(sipMessage);
        }
        else
        {
            /**
             * Response
             */
            dOMSource = (DOMSource) normalizeSipResponse(sipMessage);
        }
        return dOMSource;
    }


    private Source normalizeSipRequest(SIPMessage sipMessage)
    {
        DOMSource dOMSource = new DOMSource();
        try
        {
            /**
             * Get a SipRequest from the passed in sipMessage
             */
            SIPRequest sipRequest = (SIPRequest)sipMessage;
            /**
             * Create a Document object to be the main Node in the returned
             * DOMSource
             */
            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
            Document mainDocument = documentBuilder.newDocument();
            /**
             * Create the jbi message wrapper and append it as the immediate
             * child of the mainDocument
             */
            createMessageWrapper(mainDocument, "receiveInviteMessage", "tns:receiveInviteMessage");
            /**
             * Append a Wrapper Part Element for the Sip Request Line
             */
             Element partElement = createAndAppendPart(mainDocument);
            /**
             * Get the name of the part content element from the service description
             * WSDL document and then append that content element with the appropriate
             * content retrieved from the SipRequest
             */
             String requestLineElementName = getElementNameFromWSDL("receiveInviteMessage", "Request-Line");
             Element requestLineElement = mainDocument.createElement(requestLineElementName);
             RequestLine requestLine = sipRequest.getRequestLine();
             requestLineElement.setTextContent( sipRequest.getRequestLine().toString().substring(0, requestLine.toString().length() - 2));
             partElement.appendChild(requestLineElement);
            /**
             * For each one of the header fields contained in the passed in SipMessage, we need
             * to append a part element encapsulating an element with a name that corresponds to
             * the given header fields, that name is to be retrieved from the WSDL service
             * description of the service to be consumed.
             */
            ListIterator headerNamesListIterator =  sipMessage.getHeaderNames();
            while(headerNamesListIterator.hasNext())
            {
                /**
                 * Create the part
                 */
                Element headerPartElement = createAndAppendPart(mainDocument);
                /**
                 * Create the header element
                 */
                String headerName = (String) headerNamesListIterator.next();
                String headerElementName = getElementNameFromWSDL("receiveInviteMessage", headerName);
                Element headerFieldElement = mainDocument.createElement(headerElementName);
                String headerContent =  sipMessage.getHeader(headerName).toString();
                headerFieldElement.setTextContent(headerContent.substring(0, headerContent.length() - 2));
                /**
                 * Append the newly created header field to the part associated with it.
                 */
                headerPartElement.appendChild(headerFieldElement);
            }
            /**
             * DEBUG OUTPUT - Print the Document XML to standard output
             */
            printXmlToScreen(mainDocument);
            /**
             * Set the main node of the DOMsource to the mainDocument
             */
             dOMSource.setNode(mainDocument);
             return dOMSource;
        }
        catch(Exception ex)
        {
            logger.severe(ex.getMessage());
            ex.printStackTrace();
        }
        return dOMSource;
    }

    private Source normalizeSipResponse(SIPMessage sipMessage)
    {
        DOMSource dOMSource = new DOMSource();
        try
        {
            /**
             * Get a SipRequest from the passed in sipMessage
             */
            SIPResponse sipResponse = (SIPResponse)sipMessage;
            /**
             * Create a Document object to be the main Node in the returned
             * DOMSource
             */
            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
            Document mainDocument = documentBuilder.newDocument();
            /**
             * Create the jbi message wrapper and append it as the immediate
             * child of the mainDocument
             */
            createMessageWrapper(mainDocument, "sendResponseMessage", "tns:sendResponseMessage");
            /**
             * Append a Wrapper Part Element for the Sip Status Line
             */
             Element partElement = createAndAppendPart(mainDocument);
            /**
             * Get the name of the part content element from the service description
             * WSDL document and then append that content element with the appropriate
             * content retrieved from the SipRequest
             */
             String statusLineElementName = getElementNameFromWSDL("sendResponseMessage", "Status-Line");
             Element statusLineElement = mainDocument.createElement(statusLineElementName);
             StatusLine statusLine = sipResponse.getStatusLine();
             statusLineElement.setTextContent(sipResponse.getStatusLine().toString().substring(0, statusLine.toString().length() - 2));
             partElement.appendChild(statusLineElement);
            /**
             * For each one of the header fields contained in the passed in SipMessage, we need
             * to append a part element encapsulating an element with a name that corresponds to
             * the given header fields, that name is to be retrieved from the WSDL service
             * description of the service to be consumed.
             */
            ListIterator headerNamesListIterator =  sipMessage.getHeaderNames();
            while(headerNamesListIterator.hasNext())
            {
                /**
                 * Create the part
                 */
                Element headerPartElement = createAndAppendPart(mainDocument);
                /**
                 * Create the header element
                 */
                String headerName = (String) headerNamesListIterator.next();
                String headerElementName = getElementNameFromWSDL("sendResponseMessage", headerName);
                Element headerFieldElement = mainDocument.createElement(headerElementName);
                String headerContent =  sipMessage.getHeader(headerName).toString();
                headerFieldElement.setTextContent(headerContent.substring(0, headerContent.length() - 2));
                /**
                 * Append the newly created header field to the part associated with it.
                 */
                headerPartElement.appendChild(headerFieldElement);
            }
            /**
             * DEBUG OUTPUT - Print the Document XML to standard output
             */
            printXmlToScreen(mainDocument);
            /**
             * Set the main node of the DOMsource to the mainDocument
             */
             dOMSource.setNode(mainDocument);
             return dOMSource;
        }
        catch(Exception ex)
        {
            logger.severe(ex.getMessage());
            ex.printStackTrace();
        }
        return dOMSource;
    }

    /**
     * 
     */
    private void createMessageWrapper(Document document, String messageName, String messageType)
    {
        logger.finest("Message Wrapper creation started...");
        try
        {
            /**
             * Create the new Element representing a message, set its attributes
             * and add it as the immediate child of the root Document.
             */
            Element rootMessageElement = document.createElementNS(defaultJbiMessageNamespace, "jbi:message");
            logger.finest("The rootMessageElement has been created with a namespace of: " + defaultJbiMessageNamespace);
            logger.finest("Setting the message wrapper attribute values...");

            rootMessageElement.setAttribute("version", "1.0");
            rootMessageElement.setAttribute("type", messageType);
            rootMessageElement.setAttribute("name", messageName);
            rootMessageElement.setAttribute("xmlns:tns", RuntimeComponentContext.getProperty("com.orange.uklab.b2bbc.WSDL_CONSUME_TARGET_NAMESPACE"));
            
            document.appendChild(rootMessageElement);
            logger.finest("Message Wrapper appended as the immediate child of the root document: " + document.getDocumentElement().getLocalName());
            /**
             * Print the whole Document XML Content to the standard output
             */
            if(logger.getLevel() == Level.FINEST)
            {
                //printXmlToScreen(document);
            }
        }
        catch(Exception ex)
        {
            logger.severe(ex.getMessage());
            ex.printStackTrace();
        }        
    }

    private String getElementNameFromWSDL(String messageName, String partName)
    {
        String elementPartname = null;
        try
        {
            /**
             * Get the WSDL definitions
             */
            WSDLFactoryImpl wsdlFactory = (WSDLFactoryImpl) WSDLFactoryImpl.newInstance();
            WSDLReaderImpl wsdlReader = (WSDLReaderImpl) wsdlFactory.newWSDLReader();
            DefinitionImpl definition = (DefinitionImpl) wsdlReader.readWSDL(RuntimeComponentContext.getProperty("com.orange.uklab.b2bbc.WSDL_CONSUME_FILENAME"));
            /**
             * Get the wanted message.
             */
            Map messagesMap = definition.getMessages();
            Iterator messagesIterator = messagesMap.entrySet().iterator();
            MessageImpl message = null;
            while(messagesIterator.hasNext())
            {
                Map.Entry mapEntry = (Entry) messagesIterator.next();
                QName messageQname = (QName) mapEntry.getKey();                
                if(messageQname.getLocalPart().equals(messageName))
                {
                    message =  (MessageImpl) mapEntry.getValue();
                    break;
                }
            }
            /**
             * Get the wanted part
             */
            Map partsMap = message.getParts();
            Iterator partsMapIterator = partsMap.entrySet().iterator();
            PartImpl part = null;
            while(partsMapIterator.hasNext())
            {
                Map.Entry partsMapEntry = (Entry) partsMapIterator.next();
                String partWsdlName = (String) partsMapEntry.getKey();
                if(partWsdlName.equals(partName))
                {
                    part =  (PartImpl) partsMapEntry.getValue();
                    break;
                }
            }
            /**
             * Find the wanted Element name correspond to that part
             */
            elementPartname = part.getElementName().getLocalPart();
        }
        catch(Exception ex)
        {
            logger.severe(ex.getMessage());
            ex.printStackTrace();
        }
        return elementPartname;
    }

    private Element createAndAppendPart(Document document)
    {
        Element rootElement = document.getDocumentElement();
        Element partWrapperElement = document.createElementNS(defaultJbiMessageNamespace, "jbi:part");
        Element partElement = (Element) rootElement.appendChild(partWrapperElement);
        return partElement;
    }

    private void printXmlToScreen(Document document)
    {
        try
        {
            logger.warning("Printing out the XML content");
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource dOMSource = new DOMSource(document);
            StreamResult streamResult = new StreamResult(System.out);
            transformer.transform(dOMSource, streamResult);
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }

    /**
     *
     * @param normalizedMessageContent
     * @return
     */
    @Override
    public SIPMessage deNormalizeSipMessage(Source normalizedMessageContent)
    {
        SIPMessage sipMessage = null;
        /**
         * Get the root Document of the NormalizedMessageContent and inspect
         * the type of the message being normalized, either a request or
         * a response and follow the right path accordingly.
         */
        DOMSource domSource = (DOMSource) normalizedMessageContent;
        Document rootDocument = (Document) domSource.getNode();
        Element rootElement = rootDocument.getDocumentElement();

        Element sipStatusLineElement = (Element) rootElement.getElementsByTagName("SipStatusLineElement").item(0);
        if(sipStatusLineElement != null)
        {
            /**
             * Means the message represents a response
             */
            try
            {
                sipMessage = deNormalizeSipResponse(normalizedMessageContent);
            }
            catch(Exception ex)
            {
                logger.severe(ex.getMessage());
                ex.printStackTrace();
            }
        }
        else
        {
            try
            {
                /**
                 * The message represents a request
                 */
                sipMessage = deNormalizeSipRequest(normalizedMessageContent);
            }
            catch (Exception ex)
            {
                logger.severe(ex.getMessage());
                ex.printStackTrace();
            }
        }
        return sipMessage;
    }

    private SIPRequest deNormalizeSipRequest(Source normalizedMessageContent) throws Exception
    {
        /**
         * Create the SipRequest that we are going to return.
         */
        SIPRequest sipRequest = new SIPRequest();
        /**
         * Get the root Document & Element of the Normalized
         * Message Content.
         */
        DOMSource domSource = (DOMSource) normalizedMessageContent;
        Document rootDocument = (Document) domSource.getNode();
        Element rootElement = rootDocument.getDocumentElement();
        /**
         * Get the RequestLine and set its contents appropriately into
         * the sipRequest
         */
        String requestLineContent = rootElement.getElementsByTagName("SipRequestLineElement").item(0).getTextContent();
        RequestLine requestLine = (new RequestLineParser(requestLineContent)).parse();
        sipRequest.setRequestLine(requestLine);
        /**
         * Iterate over the Header Field elements contained in the normalized
         * message, parse them to native SipHeaders and finally populate
         * the SipRequest with those header fields.
         */
         NodeList partsNodeList = rootElement.getChildNodes();
         /**
          * Iterate over the message parts
          */
         for(int i = 0 ; i < partsNodeList.getLength() ; i ++)
         {
            Element partElement = (Element) partsNodeList.item(i);
            NodeList partHeaderElementsNodeList = partElement.getChildNodes();
            /**
             * Iterate over the part's element contents
             */
            for(int j = 0 ; j < partHeaderElementsNodeList.getLength() ; j++)
            {
                String headerElementName = partHeaderElementsNodeList.item(j).getNodeName();
                /**
                 * Execlude the Request/Status line from the mapping operation.
                 */
                if(headerElementName.equals("SipRequestLineElement") || headerElementName.equals("SipStatusLineElement"))
                {
                    logger.finest("Skipping SIP Request/Status line...");
                    continue;
                }
                String sipHeaderFieldName = getHeaderFieldNameFromWSDL(headerElementName, "sendResponseMessage");
                if(sipHeaderFieldName == null)
                {
                    logger.severe("No SIP Header Field mapping for HeaderElementName: " + headerElementName + " inside the WSDL service description. The Headee" +
                            "will not be included in the SIP Response");
                    continue;
                }                
                SipMessageParserMapperImpl sipMessageParserMapper = new SipMessageParserMapperImpl();
                String headerFieldTextContent = partHeaderElementsNodeList.item(j).getTextContent() + "\n";
                /**
                 * If the headerField text content is null, then skip including that header field
                 * in the SipRequest
                 */
                if(headerFieldTextContent == null)
                {
                    logger.severe("Header Field : " + sipHeaderFieldName + " contains no content, thus skipping" +
                            " adding it to the generated SIPRequest");
                    continue;
                }
                HeaderParser headerParser = sipMessageParserMapper.mapHeaderFieldName(sipHeaderFieldName, headerFieldTextContent);
                /**
                 * If there is no parser for the given header field name, then that header field should be skipped
                 */
                if(headerParser == null)
                {
                    logger.severe("No header parser found for header field name: " + sipHeaderFieldName + ", skipping the inclusion of" +
                            " this header field in the SIPRequest");
                    continue;
                }
                else
                {                    
                    logger.info("SIP Header parser found for the header field: " + sipHeaderFieldName);
                    logger.finest("Obtained SIP Header parser is " + headerParser.getClass().getName());
                    SIPHeader sipHeader = headerParser.parse();
                    if(sipHeader == null)
                    {
                        logger.severe("No SIP Header generated by the parser for " + sipHeaderFieldName + " skipping the " +
                                "inclusion of this SIPHeader in the SIPRequest");
                        continue;
                    }
                    logger.info("SIP Header generated by the parser for " + sipHeaderFieldName);
                    sipRequest.addHeader(sipHeader);
                }
            }            
         }
         return sipRequest;
    }

    private SIPResponse deNormalizeSipResponse(Source normalizedMessageContent) throws Exception
    {
        /**
         * Create the SipResponse that we are going to return.
         */
        SIPResponse sipResponse = new SIPResponse();
        /**
         * Get the root Document & Element of the Normalized
         * Message Content.
         */
        DOMSource domSource = (DOMSource) normalizedMessageContent;
        Document rootDocument = (Document) domSource.getNode();
        Element rootElement = rootDocument.getDocumentElement();        
        /**
         * Get the StatusLine and set its contents appropriately into
         * the sipResponse
         */
        String statusLineContent = rootElement.getElementsByTagName("SipStatusLineElement").item(0).getTextContent();
        StatusLine statusLine = (new StatusLineParser(statusLineContent)).parse();
        sipResponse.setStatusLine(statusLine);   
        /**
         * Iterate over the Header Field elements contained in the normalized
         * message, parse them to native SipHeaders and finally populate
         * the SipResponse with those header fields.
         */
         NodeList partsNodeList = rootElement.getChildNodes();
         /**
          * Iterate over the message parts
          */
         for(int i = 0 ; i < partsNodeList.getLength() ; i ++)
         {
            Element partElement = (Element) partsNodeList.item(i);
            NodeList partHeaderElementsNodeList = partElement.getChildNodes();
            /**
             * Iterate over the part's element contents
             */
            for(int j = 0 ; j < partHeaderElementsNodeList.getLength() ; j++)
            {
                String headerElementName = partHeaderElementsNodeList.item(j).getNodeName();
                /**
                 * Execlude the Request/Status line from the mapping operation.
                 */
                if(headerElementName.equals("SipRequestLineElement") || headerElementName.equals("SipStatusLineElement"))
                {
                    logger.finest("Skipping SIP Request/Status line...");
                    continue;
                }
                String sipHeaderFieldName = getHeaderFieldNameFromWSDL(headerElementName, "sendResponseMessage");
                if(sipHeaderFieldName == null)
                {
                    logger.severe("No SIP Header Field mapping for HeaderElementName: " + headerElementName + " inside the WSDL service description. The Headee" +
                            "will not be included in the SIP Response");
                    continue;
                }                
                SipMessageParserMapperImpl sipMessageParserMapper = new SipMessageParserMapperImpl();
                String headerFieldTextContent = partHeaderElementsNodeList.item(j).getTextContent() + "\n";
                /**
                 * If the headerField text content is null, then skip including that header field
                 * in the Sip Response
                 */
                if(headerFieldTextContent == null)
                {
                    logger.severe("Header Field : " + sipHeaderFieldName + " contains no content, thus skipping" +
                            " adding it to the generated SIP Response");
                    continue;
                }
                HeaderParser headerParser = sipMessageParserMapper.mapHeaderFieldName(sipHeaderFieldName, headerFieldTextContent);
                /**
                 * If there is no parser for the given header field name, then that header field should be skipped
                 */
                if(headerParser == null)
                {
                    logger.severe("No header parser found for header field name: " + sipHeaderFieldName + ", skipping the inclusion of" +
                            " this header field in the SIP response");
                    continue;
                }
                else
                {                    
                    logger.info("SIP Header parser found for the header field: " + sipHeaderFieldName);
                    logger.finest("Obtained SIP Header parser is " + headerParser.getClass().getName());
                    SIPHeader sipHeader = headerParser.parse();
                    if(sipHeader == null)
                    {
                        logger.severe("No SIP Header generated by the parser for " + sipHeaderFieldName + " skipping the " +
                                "inclusion of this SIPHeader in the SIP response");
                        continue;
                    }
                    logger.info("SIP Header generated by the parser for " + sipHeaderFieldName);
                    sipResponse.addHeader(sipHeader);
                }
            }

         }
        return sipResponse;
    }

    private String getHeaderFieldNameFromWSDL(String elementname, String messageName)
    {
        String headerFieldName = null;
        try
        {
            /**
             * Get the WSDL definitions
             */
            WSDLFactoryImpl wsdlFactory = (WSDLFactoryImpl) WSDLFactoryImpl.newInstance();
            WSDLReaderImpl wsdlReader = (WSDLReaderImpl) wsdlFactory.newWSDLReader();
            DefinitionImpl definition = (DefinitionImpl) wsdlReader.readWSDL(RuntimeComponentContext.getProperty("com.orange.uklab.b2bbc.WSDL_CONSUME_FILENAME"));
            /**
             * Get the wanted message
             */
            Map messagesMap = definition.getMessages();
            Iterator messagesIterator = messagesMap.entrySet().iterator();
            MessageImpl message = null;
            while(messagesIterator.hasNext())
            {
                Map.Entry mapEntry = (Entry) messagesIterator.next();
                QName messageQname = (QName) mapEntry.getKey();                
                if(messageQname.getLocalPart().equals(messageName))
                {
                    message =  (MessageImpl) mapEntry.getValue();
                    break;
                }
            }
            /**
             * Get the wanted part
             */
            Map partsMap = message.getParts();
            Iterator partsMapIterator = partsMap.entrySet().iterator();
            PartImpl part = null;
            while(partsMapIterator.hasNext())
            {
                Map.Entry partsMapEntry = (Entry) partsMapIterator.next();
                //String partWsdlName = (String) partsMapEntry.getKey();
                part =  (PartImpl) partsMapEntry.getValue();
                String wsdlElementName = part.getElementName().getLocalPart();
                if(wsdlElementName.equals(elementname))
                {
                    /**
                     * The wanted part has been located...
                     */
                    headerFieldName = part.getName();
                    return headerFieldName;
                }
                
            }
        }
        catch(Exception ex)
        {
            logger.severe(ex.getMessage());
            ex.printStackTrace();
        }
        logger.warning("Unable to locate a wsdl part containing an element with a name of: " + elementname);
        return headerFieldName;
    }
}
