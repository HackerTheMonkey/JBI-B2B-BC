/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.orange.uklab.b2bbc.sip.messageprocessing.impl;
import com.orange.uklab.b2bbc.descriptor.impl.ServiceUnitImpl;
import com.orange.uklab.b2bbc.runtime.contexts.RuntimeComponentContext;
import com.orange.uklab.b2bbc.sip.impl.B2BDialogImpl;
import com.orange.uklab.b2bbc.sip.impl.SipMessageDispatcherImpl;
import com.orange.uklab.b2bbc.sip.messageprocessing.RegisterProcessor;
import gov.nist.javax.sip.address.SipUri;
import gov.nist.javax.sip.message.SIPRequest;
import gov.nist.javax.sip.message.SIPResponse;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sip.PeerUnavailableException;
import javax.sip.RequestEvent;
import javax.sip.header.ContactHeader;

/**
 *
 * @author hasanein
 */
public class RegisterProcessorImpl implements RegisterProcessor
{
    private Logger logger;

    /**
     * 
     */
    public RegisterProcessorImpl()
    {
        initLogger();
    }

    /**
     *
     */
    private void initLogger()
    {
        RuntimeComponentContext runtimeComponentContext = RuntimeComponentContext.getInstance();
        logger = runtimeComponentContext.getLogger(this.getClass().getName(), null);
    }

    /**
     * This method will just cause the sending of a 200 OK response back to
     * the SIP client who sends the REGISTER request. This is used just to
     * imitate a SIP registrar for testing purposes.
     * @param origibalRequestEvent
     * @return
     */
    @Override
    public boolean processIncomingRegister(RequestEvent origibalRequestEvent)
    {
        /**
         * The return value of false is to indicate to the
         * SIP director that no need to forward the request
         * to the associated service unit via the NMR
         */
        boolean returnValue = true;
//        /**
//         * Get the 200 OK response for the received register request
//         */
//        logger.info("Preparing an OK response for the received REGISTER request...");
//        SIPResponse okResponse = prepareOkResponse(origibalRequestEvent);
//        /**
//         * Get the service unit that this request is associated with
//         */
//        logger.info("Getting the ServiceUnit associated with the received REGISTER request...");
//        SipUri sipUri = (SipUri) origibalRequestEvent.getRequest().getRequestURI();
//        ServiceUnitImpl serviceUnit = (ServiceUnitImpl) RuntimeComponentContext.getInstance().getServiceUnit(sipUri.getPort());
//        /**
//         * send the response along with the associated service unit
//         * to the SipMessageDispatcher to be sent back to the SIP
//         * client.
//         */
//        SipMessageDispatcherImpl sipMessageDispatcher = new SipMessageDispatcherImpl();
//        logger.info("Sending a 200 OK response back to the SIP client...");
//        sipMessageDispatcher.sendResponse(origibalRequestEvent,okResponse, serviceUnit, null);
//        logger.info("The 200 OK response has been sent...");
        return returnValue;
    }

    private SIPResponse prepareOkResponse(RequestEvent requestEvent)
    {

        try
        {
            SIPRequest sipRequest = (SIPRequest) requestEvent.getRequest();
            SIPResponse sipResponse = sipRequest.createResponse(200);
            logger.info(sipResponse.toString() + ":::::::::::::::::::::::::::::::");
            ContactHeader contact = RuntimeComponentContext.getInstance().getSipFactory().createHeaderFactory().createContactHeader(sipRequest.getToHeader().getAddress());
            contact.setExpires(3600);
            sipResponse.addHeader(contact);
            sipResponse.setReasonPhrase("OK");
            return sipResponse;
        }
        catch (Exception ex)
        {
            Logger.getLogger(RegisterProcessorImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return null;
    }
    
}
