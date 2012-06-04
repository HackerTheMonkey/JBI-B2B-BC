/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.orange.uklab.b2bbc.sip.impl;
import com.orange.uklab.b2bbc.descriptor.ServiceUnit;
import com.orange.uklab.b2bbc.runtime.contexts.RuntimeComponentContext;
import com.orange.uklab.b2bbc.sip.B2BSession;
import com.orange.uklab.b2bbc.sip.SipMessageDispatcher;
import gov.nist.javax.sip.message.SIPResponse;
import java.util.logging.Logger;
import javax.sip.Dialog;
import javax.sip.RequestEvent;
import javax.sip.message.Request;
import javax.sip.message.Response;

/**
 *
 * @author hasanein
 */
public class SipMessageDispatcherImpl implements SipMessageDispatcher
{
    //private SipUserAgentClientImpl userAgentClient = null;
    private SipUserAgentImpl userAgent = null;
    private Logger logger = null;

    /**
     *
     */
    public SipMessageDispatcherImpl()
    {
        initLogger();
        //this.userAgentClient = new SipUserAgentClientImpl();
        this.userAgent = new SipUserAgentImpl();
    }

    private void initLogger()
    {
        this.logger = RuntimeComponentContext.getInstance().getLogger(this.getClass().getName(), null);
    }

    /**
     *
     * @param originalRequestEvent
     * @param serviceUnit
     */
    /**
     *
     * @param originalRequestEvent
     * @param serviceUnit
     */
    @Override
    public void receiveRequest(RequestEvent originalRequestEvent, ServiceUnit serviceUnit)
    {
        logger.info("Request Message received: " + originalRequestEvent.getRequest().getMethod());
        logger.info("Forwarding request to the ClientAgentServer for processing...");
        userAgent.receiveRequest(originalRequestEvent, serviceUnit);
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
    public void receiveResponse(Response response, ServiceUnit serviceUnit)
    {
        logger.info("Response Message received: " + response.getReasonPhrase());
        logger.info("Forwarding response to the ClientAgentServer for processing...");
        userAgent.receiveResponse(response, serviceUnit);
    }

    /**
     *
     * @param originalRequestEvent
     * @param response
     * @param serviceUnit
     */
    /**
     *
     * @param originalRequestEvent
     * @param response
     * @param serviceUnit
     */
    @Override
    public void sendResponse(RequestEvent originalRequestEvent,Response response, ServiceUnit serviceUnit, String toTag)
    {        
        /**
         * Here we need to do some specific handling specific to the type
         * of responses being sent.
         */
        
        /**
         * If it is a 180 RINGING response, then we need to set the To tag to
         * the same to tag generated when creating the associated B2BSession
         * and B2BDialog.
         */
        if(isDialogCreating(response.getStatusCode()))
        {
            logger.fine("Sending a ringing response...");
            /**
             * Copy the Local Tag from the B2BDialog to the
             * SipResponse
             */
            logger.info("Setting the To Dialog of the response to be sent to : " + toTag);
            SIPResponse sipResponse = (SIPResponse) response;
            sipResponse.setToTag(toTag);
        }
        logger.info("Sending Response for the request associated with ServiceUnit: " + serviceUnit.getName());
        /**
         * Conveying the response sending to the UserAgent
         */
        logger.info("Sending Response: " + response.getReasonPhrase());
        Dialog sipDialog = userAgent.sendResponse(originalRequestEvent, response, serviceUnit);
        if(sipDialog == null)
        {
            logger.info("The sent response " + response.getReasonPhrase() + " did not resulted in any dialog...");
        }
        else
        {
            logger.info("The sent response " + response.getReasonPhrase() + " has created a new Dialog with ID: " + sipDialog.getDialogId());
        }
        /**
         * Set the underlying SIP Dialog of the B2BDialog for
         * Dialog creating responses as well as the local seq number and the
         * current state of the dialog.
         */
        if(isDialogCreating(response.getStatusCode()))
        {
            B2BSession b2BSessions[] = RuntimeComponentContext.getInstance().getB2BSessions(serviceUnit.getName());
            for(B2BSession b2BSession : b2BSessions)
            {
                B2BDialogImpl b2BDialogImpl = null;
                if(sipDialog != null)
                {
                    b2BDialogImpl = (B2BDialogImpl) b2BSession.getB2BDialog(sipDialog.getDialogId(), true, null);
                }
                if(b2BDialogImpl != null)
                {
                    logger.info("Setting the underlying SIP Dialog property of the B2BDialog: " + b2BDialogImpl.getDialogId());
                    b2BDialogImpl.setUnderlyingSipDialog(sipDialog);
                    b2BDialogImpl.setLocalSeqNumber(sipDialog.getLocalSeqNumber());
                    b2BDialogImpl.setState(sipDialog.getState().toString());
                    logger.info("Setting the Dialog status with ID: " + b2BDialogImpl.getDialogId() + " to " + sipDialog.getState().toString());
                    break;
                }
                else
                {
                    logger.info("Unable to locate a B2BDialog with ID " + sipDialog.getDialogId());
                }
            }
        }

    }


    private boolean isDialogCreating(int statusCode)
    {
        if(statusCode == 180 || statusCode == 200)
        {
            return true;
        }
        else
        {
            return false;
        }
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
    public void sendRequest(Request request, ServiceUnit serviceUnit)
    {
        logger.info("Sending Request: " + request.getMethod());
        userAgent.sendRequest(request, serviceUnit);
        logger.info("Sending Request that is associated with ServiceUnit: " + serviceUnit.getName());
    }

}
