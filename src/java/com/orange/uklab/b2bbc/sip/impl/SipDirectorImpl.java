/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.orange.uklab.b2bbc.sip.impl;
import com.orange.uklab.b2bbc.descriptor.ServiceUnit;
import com.orange.uklab.b2bbc.runtime.contexts.RuntimeComponentContext;
import com.orange.uklab.b2bbc.sip.SipDirector;
import com.orange.uklab.b2bbc.sip.NativeSipReceiver;
import com.orange.uklab.b2bbc.sip.WsdlSipReceiver;
import com.orange.uklab.b2bbc.sip.messageprocessing.SipRequestsEnum;
import com.orange.uklab.b2bbc.sip.messageprocessing.impl.InviteProcessorImpl;
import com.orange.uklab.b2bbc.sip.messageprocessing.impl.RegisterProcessorImpl;
import gov.nist.javax.sip.address.SipUri;
import gov.nist.javax.sip.message.SIPRequest;
import gov.nist.javax.sip.message.SIPResponse;
import java.util.logging.Logger;
import javax.sip.DialogTerminatedEvent;
import javax.sip.IOExceptionEvent;
import javax.sip.RequestEvent;
import javax.sip.ResponseEvent;
import javax.sip.TimeoutEvent;
import javax.sip.TransactionTerminatedEvent;
import javax.sip.message.Request;

/**
 *
 * @author hasanein
 */
public class SipDirectorImpl implements SipDirector, NativeSipReceiver, WsdlSipReceiver
{
    private SipMessageDispatcherImpl sipMessageDispatcher = null;    
    private Logger logger = null;    

    /**
     *
     */
    /**
     *
     */
    public SipDirectorImpl()
    {
        initLogger();
        this.sipMessageDispatcher = new SipMessageDispatcherImpl();        
    }

    private void initLogger()
    {
        logger = RuntimeComponentContext.getInstance().getLogger(this.getClass().getName(), null);
    }

    /**
     *
     * @param dialogTerminatedEvent
     */
    /**
     *
     * @param dialogTerminatedEvent
     */
    @Override
    public void receiveEvent(DialogTerminatedEvent dialogTerminatedEvent)
    {
        logger.info("receiveEvent: receiving event,  DialogTerminatedEvent: " + dialogTerminatedEvent.toString());
    }

    /**
     *
     * @param iOExceptionEvent
     */
    /**
     *
     * @param iOExceptionEvent
     */
    @Override
    public void receiveEvent(IOExceptionEvent iOExceptionEvent)
    {
        logger.info("receiveEvent: receiving event, IOExceptionEvent: " + iOExceptionEvent.toString());
    }

    /**
     *
     * @param requestEvent
     */
    /**
     *
     * @param requestEvent
     */
    @Override
    public void receiveEvent(RequestEvent requestEvent)
    {
        if(initiateGenericProcessing(requestEvent))
        {
            if(initiateMethodSpecifcProcessing(requestEvent))
            {
                /**
                 * Forwarding the received EventObject to the intersted
                 * ServiceEngine as per the deployed ServiceUnit configurations.
                 */
                Request request = requestEvent.getRequest();
                logger.info("Sending the received request to the SipMessageDispatcher");
                SipUri sipRequestUri = (SipUri) request.getRequestURI();
                ServiceUnit serviceUnit = RuntimeComponentContext.getInstance().getServiceUnit(sipRequestUri.getPort());
                sipMessageDispatcher.receiveRequest(requestEvent,serviceUnit);
            }
        }                
    }

    /**
     * 
     * @param requestEvent
     * @return 
     */
    public boolean initiateGenericProcessing(RequestEvent requestEvent)
    {
        logger.info("receiveEvent: receiving event, RequestEvent: Method:  " + requestEvent.getRequest().getMethod());
        boolean requestValidationOutcome = validateIncomingRequest(requestEvent);
        if(requestValidationOutcome)
        {
            logger.info("Method validation Completed Sucessfully");
        }
        else
        {
            logger.info("Method validation Completed with errors");
        }
        /**
         * This method will return TRUE if the processing outcome did not
         * resulted in a response to be sent back to the requestor, otherwise
         * it returns FALSE;
         */
        return true;
    }

    /**
     *
     * @param requestEvent
     * @return
     */
    public boolean initiateMethodSpecifcProcessing(RequestEvent requestEvent)
    {
        String requestMethod = requestEvent.getRequest().getMethod();
        boolean returnValue = false;
        switch(SipRequestsEnum.valueOf(requestMethod))
        {
            case INVITE:
                InviteProcessorImpl inviteProcessor = new InviteProcessorImpl();
                return inviteProcessor.processIncomingInvite(requestEvent);                
            case REGISTER:
                logger.info("REGISTER request received from " + requestEvent.getRequest().getRequestURI());
                RegisterProcessorImpl registerProcessor = new RegisterProcessorImpl();                
                return registerProcessor.processIncomingRegister(requestEvent);
            case INFO:
                logger.info("INFO RECEIVED...");
                break;
            case OPTIONS:
                logger.info("OPTIONS RECEIVED...");
                break;
            case BYE:
                logger.info("BYE RECEIVED...");
                break;
            case CANCEL:
                logger.info("CANCEL RECEIVED..." + requestEvent.getRequest().getMethod());
                break;
            case ACK:
                logger.info("ACK RECEIVED..." + requestEvent.getRequest().getMethod());
                break;
            default:
                logger.info("Method not supported...");
        }
        return returnValue;
    }


    /**
     *
     * @param responseEvent
     */
    /**
     *
     * @param responseEvent
     */
    @Override
    public void receiveEvent(ResponseEvent responseEvent)
    {
        logger.info("receiveEvent: receiving event, ResponseEvent: Response Phrase:  " + responseEvent.getResponse().getReasonPhrase());
    }

    /**
     *
     * @param timeoutEvent
     */
    /**
     *
     * @param timeoutEvent
     */
    @Override
    public void receiveEvent(TimeoutEvent timeoutEvent)
    {
        logger.info("receiveEvent: receiving event, TimeoutEvent: " + timeoutEvent.toString());
    }

    /**
     *
     * @param transactionTerminatedEvent
     */
    /**
     *
     * @param transactionTerminatedEvent
     */
    @Override
    public void receiveEvent(TransactionTerminatedEvent transactionTerminatedEvent)
    {
        logger.info("receiveEvent: receiving event, TimeoTransactionTerminatedEventtEvent: " + transactionTerminatedEvent.toString());
    }

    /**
     * This is an atomic method that will be used by the Director to validate
     * the incoming messages it receives via the Receiver interface. If the
     * message is validated positively, then state changes and further processing
     * would occur, otherwise the message should be rejected appropriately and
     * no state changes to be performed.
     * This check is to be conducted whether or not the received request is
     * inside or outside a dialog and regardless of the request method.
     * @param requestEvent
     * @return the outcome of the validation operation
     */  
    public boolean validateIncomingRequest(RequestEvent requestEvent)
    {
        boolean requestAuthenticationOutcome = authenticateIncomingRequest(requestEvent);
        boolean methodInspectionOutcome = inspectIncomingMethod(requestEvent);
        boolean headerFieldsInspectionOutcome = inspectIncomingRequestHeaderFields(requestEvent);
        boolean contentInspectionOutcome = inspectIncomingRequestContent(requestEvent);

        /**
         * Applying Extension Processing goes here.
         */

        /**
         * If all of the above generic checks have been passed, then the
         * processing that follows is method specific.
         */
        return (requestAuthenticationOutcome & methodInspectionOutcome & headerFieldsInspectionOutcome & contentInspectionOutcome);
    }

    private boolean authenticateIncomingRequest(RequestEvent requestEvent)
    {
        return true;
    }

    private boolean inspectIncomingMethod(RequestEvent requestEvent)
    {
        return true;
    }

    private boolean inspectIncomingRequestHeaderFields(RequestEvent requestEvent)
    {
        return true;
    }

    private boolean inspectIncomingRequestContent(RequestEvent requestEvent)
    {
        return true;
    }

    /**
     *
     * @param request
     * @param serviceUnit
     */
    /**
     *
     * @param request
     * @param serviceUnit
     */
    @Override
    public void sendRequest(SIPRequest request, ServiceUnit serviceUnit)
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     *
     * @param response
     * @param serviceUnit
     */
    /**
     *
     * @param response
     * @param serviceUnit
     */
    @Override
    public void sendResponse(SIPResponse response, ServiceUnit serviceUnit)
    {
        logger.info("SIP Response has been received from the NMR side.....");
        /**
         * We need to find the originalRequestEvent associated with
         * the request that this response is targeted to.
         */
        RuntimeComponentContext runtimeComponentContext = RuntimeComponentContext.getInstance();
        String callId = response.getCallId().getCallId();
        String fromTag = response.getFromTag();
        B2BSessionImpl b2BSession = (B2BSessionImpl) runtimeComponentContext.getB2BSession(serviceUnit, callId, fromTag);
        if(b2BSession != null)
        {
            logger.info("The B2BSession associated with the response being sent has been obtained: " + b2BSession.getB2BSessionId());
            B2BDialogImpl b2BDialog = (B2BDialogImpl) b2BSession.getB2BDialog(callId, false, fromTag);
            String toTag = b2BDialog.getLocalTag();
            RequestEvent originalRequestEvent = (RequestEvent) b2BDialog.getAttribute("originalRequestEvent");
            if(originalRequestEvent != null)
            {
                logger.info("Original Request Event has been obtained..." + originalRequestEvent.toString());
                logger.info("Sending the response to the SipMessageDispatcher...");
                logger.info("Setting the To Tag to the Local Tag which is: " + toTag);
                sipMessageDispatcher.sendResponse(originalRequestEvent, response, serviceUnit, toTag);
            }
        }
        else
        {
            logger.info("There is no B2BSession associated with the received response...");
            logger.info("The sendResponse request is discardded...");
        }
    }
}
