/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.orange.uklab.b2bbc.nmr.impl;
import com.orange.uklab.b2bbc.descriptor.ServiceUnit;
import com.orange.uklab.b2bbc.nmr.NmrListener;
import com.orange.uklab.b2bbc.runtime.contexts.RuntimeComponentContext;
import com.orange.uklab.b2bbc.sip.impl.SipDirectorImpl;
import com.orange.uklab.b2bbc.wsdlparsing.impl.MessageTransformer;
import gov.nist.javax.sip.message.SIPMessage;
import gov.nist.javax.sip.message.SIPRequest;
import gov.nist.javax.sip.message.SIPResponse;
import java.util.logging.Logger;
import javax.jbi.messaging.DeliveryChannel;
import javax.jbi.messaging.InOnly;
import javax.jbi.messaging.MessageExchange;
import javax.jbi.messaging.NormalizedMessage;
import javax.xml.namespace.QName;
import javax.xml.transform.dom.DOMSource;

/**
 *
 * @author hasanein
 */
public class NmrListenerImpl implements NmrListener
{    
    private Logger logger = null;
    private DeliveryChannel deliveryChannel = null;
    private String threadSignature = null;
    private boolean isInterrupted = false;

    /**
     *
     */
    /**
     *
     */
    public NmrListenerImpl()
    {
        logger = RuntimeComponentContext.getInstance().getLogger(this.getClass().getName(), null);
    }

    @Override
    public void run()
    {
        while(!isInterrupted)
        {
            init();
            /**
             * Wait for a new Normalized Message to be received from
             * the NMR
             */
            logger.info(threadSignature + ": Listening for MessageExchanged from the NMR...");
            try
            {
                /**
                 * Waiting for Normalized Messages to be received from the NMR
                 */
                MessageExchange messageExchange = deliveryChannel.accept();
                /**
                 * Extract the NormalizedMessage Content Source from the
                 * received MessageExchange.
                 */
                logger.info("MessageExchange has been received from the NMR...");
                InOnly inOnlyMessageExchange = (InOnly) messageExchange;
                NormalizedMessage normalizedMessage = inOnlyMessageExchange.getInMessage();
                DOMSource normalizedMessageContent = (DOMSource) normalizedMessage.getContent();
                logger.info("The NormalizedMessage content has been extracted from received MessageExchange");
                /**
                 * Determine the ServiceUnit that sent this MessageExchange
                 */
                QName serviceName = inOnlyMessageExchange.getService();
                String serviceEndpointName = inOnlyMessageExchange.getEndpoint().getEndpointName();
                ServiceUnit serviceUnit = RuntimeComponentContext.getInstance().getServiceUnit(serviceName, serviceEndpointName);
                if(serviceUnit != null)
                {
                    logger.info("ServiceUnit for the received MessageExchange has been determined: " + serviceUnit.getName());
                    /**
                     * The received WSDL SIP Normalized Messages need to be
                     * denormalized back into a Native SIP Message using the Message
                     * Transformer class before sending it to the SipDirector for processing.
                    */
                    logger.info("Denormalizing the NormalizedMessage Content...");
                    MessageTransformer messageTransformer = new MessageTransformer();
                    SIPMessage sipMessage = messageTransformer.deNormalizeSipMessage(normalizedMessageContent);
                    logger.info("The WSDL SipMessage has been transformed into a native SipMessage");
                    logger.info("The received message is: " + sipMessage.getFirstLine());
                    /**
                     * The transformed message need to be passed to the SipDirector
                     * for further processing.
                     */
                    logger.info("Sending the DeNormalized Message to the SipDirector for further processing...");
                    if(sipMessage.getClass().getName().equals("gov.nist.javax.sip.message.SIPResponse"))
                    {
                        logger.info("Sending a SipResponse to the SipDirector...");
                        SIPResponse sipResponse = (SIPResponse)sipMessage;
                        SipDirectorImpl sipDirector = new SipDirectorImpl();
                        sipDirector.sendResponse(sipResponse, serviceUnit);
                    }
                    else if(sipMessage.getClass().getName().equals("gov.nist.javax.sip.message.SIPRequest"))
                    {
                        logger.info("Sending a SipRequest to the SipDirector...");
                        SIPRequest sipRequest = (SIPRequest)sipMessage;
                        SipDirectorImpl sipDirector = new SipDirectorImpl();
                        sipDirector.sendRequest(sipRequest, serviceUnit);
                    }
                    else
                    {
                        logger.severe("Discarding an unrecognized SIP Message...." +
                                ": This message is not dispatched to the SIP Director");
                        continue;
                    }
                    logger.info("Native SipMessage has been dispatched to the SipDirector");
                }
                else
                {
                    logger.severe("Unable to determine the ServiceUnit for the received MessageExchage, MessageExchange has been discareded");
                    continue;
                }
            }
            catch (Exception ex)
            {
                if(ex.getMessage().trim().equals("java.lang.InterruptedException"))
                {
                    logger.info(threadSignature + "Stop listening for messages from the NMR...");
                    isInterrupted = true;
                    break;
                }
                else
                {
                    logger.severe(ex.getMessage());
                    ex.printStackTrace();
                }
            }
        }
        logger.info(threadSignature + ": Exiting...");
    }

    private void init()
    {
        deliveryChannel = RuntimeComponentContext.getInstance().getDeliveryChannel();
        threadSignature = "NAME= " + Thread.currentThread().getName() + "-ID= " + Thread.currentThread().getId();
    }
}
