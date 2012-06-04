/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.orange.uklab.b2bbc.sip.impl;

import com.orange.uklab.b2bbc.descriptor.ServiceUnit;
import com.orange.uklab.b2bbc.descriptor.impl.ServiceEndpointImpl;
import com.orange.uklab.b2bbc.nmr.NmrAgent;
import com.orange.uklab.b2bbc.nmr.impl.NmrAgentImpl;
import com.orange.uklab.b2bbc.runtime.contexts.RuntimeComponentContext;
import com.orange.uklab.b2bbc.sip.SipUserAgent;
import com.orange.uklab.b2bbc.wsdlparsing.impl.MessageTransformer;
import gov.nist.javax.sip.SipProviderImpl;
import gov.nist.javax.sip.SipStackImpl;
import gov.nist.javax.sip.message.SIPMessage;
import gov.nist.javax.sip.message.SIPRequest;
import gov.nist.javax.sip.message.SIPResponse;
import gov.nist.javax.sip.stack.SIPTransaction;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.jbi.servicedesc.ServiceEndpoint;
import javax.sip.Dialog;
import javax.sip.DialogState;
import javax.sip.InvalidArgumentException;
import javax.sip.RequestEvent;
import javax.sip.ServerTransaction;
import javax.sip.SipException;
import javax.sip.message.Request;
import javax.sip.message.Response;
import javax.xml.transform.Source;

/**
 *
 * @author hasanein
 */
public class SipUserAgentImpl implements SipUserAgent
{
    private NmrAgent nmrAgent = null;
    private Logger logger = null;
    private MessageTransformer messageTransformer = null;

    /**
     *
     */
    /**
     *
     */
    public SipUserAgentImpl()
    {
        initLogger();
        this.nmrAgent = new NmrAgentImpl();
        this.messageTransformer = new MessageTransformer();
    }    

    private void initLogger()
    {
        this.logger = RuntimeComponentContext.getInstance().getLogger(this.getClass().getName(), null);
    }
    
    @Override
    public void receiveRequest(RequestEvent originalRequestEvent, ServiceUnit serviceUnit)
    {
        logger.fine("Request Received: " + originalRequestEvent.getRequest().getMethod());
        logger.finest("Normalizing the Request Message...");        

        Source normalizedMessageContent = messageTransformer.normalizeSipMessage((SIPRequest) originalRequestEvent.getRequest());
        logger.info("Sending the received normalized request to the NMR agent...");
        ServiceEndpoint[] serviceEndpoints = serviceUnit.getServiceEndpoints();
        for(ServiceEndpoint s : serviceEndpoints)
        {
            ServiceEndpointImpl sep = (ServiceEndpointImpl) s;
            if(sep.getEndpointType().equals(ServiceEndpointImpl.ENDPOINT_TYPE_CONSUME))
            {
                Object sipMessageContent = originalRequestEvent.getRequest().getContent();
                nmrAgent.sendNormalizedMessage(normalizedMessageContent, sep, sipMessageContent);
                break;
            }
        }
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
        logger.fine("Response Received: " + response.getReasonPhrase());
        logger.finest("Normalizing the Response Message...");
        Source normalizedMessage = messageTransformer.normalizeSipMessage((SIPResponse) response);
        logger.info("Sending the received normalized response to the NMR agent...");
        ServiceEndpoint[] serviceEndpoints = serviceUnit.getServiceEndpoints();
        for(ServiceEndpoint s : serviceEndpoints)
        {
            ServiceEndpointImpl sep = (ServiceEndpointImpl) s;
            if(sep.getEndpointType().equals(ServiceEndpointImpl.ENDPOINT_TYPE_CONSUME))
            {
                Object sipMessageContent = response.getContent();
                nmrAgent.sendNormalizedMessage(normalizedMessage, sep, sipMessageContent);
                break;
            }
        }
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
    public Dialog sendResponse(RequestEvent originalRequestEvent,Response response, ServiceUnit serviceUnit)
    {
        /**
         * Get the provider registered for the ServiceUnit
         */
        logger.finest("Determining the SipProvider registered for ServiceUnit: " + serviceUnit.getName() + " for sending the received SipResponse");
        SipProviderImpl sipProvider = (SipProviderImpl) RuntimeComponentContext.getInstance().getSipProvider(serviceUnit.getName());
        logger.fine("Sending a SipResponse associated with the ServiceUnit: " + serviceUnit.getName());
        /**
         * Create/Obtain the ServerTransaction associated with the Request.
         */

        /**
         * Get the ServerTransaction from the received RequestEvent
         */
        ServerTransaction serverTransaction = null;
        serverTransaction = originalRequestEvent.getServerTransaction();

        /**
         * If the ServerTransaction is null, then that means the RequestEvent does not
         * contain one, we need to query the SipStack if it knows about the transaction related to
         * this request, if it does not, then we need to create a new one which will result in storing the
         * newly created ServerTransaction inside the SipStack so that later queries could be made possible when
         * needded.
         */
         try
         {
            if(serverTransaction == null)
            {
                /**
                 * Query the SipStack if it knows about the ServerTransaction associated
                 * with the received SipRequest.
                 */
                SipStackImpl sipStack = (SipStackImpl) RuntimeComponentContext.getInstance().getSipStack();
                serverTransaction = (ServerTransaction) sipStack.findTransaction((SIPMessage)originalRequestEvent.getRequest(), true);
                /**
                 * If the SipStack does not know about the ServerTransaction, then
                 * we need to create a new one
                 */
                if(serverTransaction == null)
                {
                    /**
                     * Instantiate a new ServerTransaction
                     */
                    serverTransaction = sipProvider.getNewServerTransaction(originalRequestEvent.getRequest());
                    logger.finest("Unable to obtain the ServerTransaction associated with the RequestEvent, a new ServerTransaction has been obtained");
                }
            }

            /**
             * Send the response back to the client via the SIP stack
             */
            logger.info("Sending a response: " + response.getReasonPhrase() + " to the " + originalRequestEvent.getRequest().getMethod() + " received request...");
            serverTransaction.sendResponse(response);
            logger.info("Number of current dialogs are: " + ((SipStackImpl)RuntimeComponentContext.getInstance().getSipStack()).getDialogs().size());
         }
         catch(Exception ex)
         {
             logger.severe(ex.getMessage());
             ex.printStackTrace();
         }

        /**
         * Check if the ServerTransaction is associated with a Dialog or not, if
         * yes then set the Dialog to be automatically terminated basing on the
         * reception of a BYE request.
         */
        Dialog resultedDialog = null;
        resultedDialog = serverTransaction.getDialog();
        if(resultedDialog != null)
        {
            try
            {
                /**
                 * Configuring the SipStack to automatically terminate the
                 * Dialog once BYE request received.
                 */
                resultedDialog.terminateOnBye(true);
            }
            catch (SipException ex)
            {
                 logger.severe(ex.getMessage());
                 ex.printStackTrace();
            }
        }
        logger.info("Number of current dialogs are: " + ((SipStackImpl)RuntimeComponentContext.getInstance().getSipStack()).getDialogs(DialogState.EARLY).size());        
        return resultedDialog;
    }

    /**
     *
     * @param request
     * @param serviceUnit
     */
    @Override
    public Dialog sendRequest(Request request, ServiceUnit serviceUnit)
    {
        /**
         * Get the provider registered for the ServiceUnit
         */
        logger.finest("Determining the SipProvider registered for ServiceUnit: " + serviceUnit.getName() + " for sending the received SipRequest");
        SipProviderImpl sipProvider = (SipProviderImpl) RuntimeComponentContext.getInstance().getSipProvider(serviceUnit.getName());
        try
        {
            logger.fine("Sending a SipRequest associated with the ServiceUnit: " + serviceUnit.getName());
            sipProvider.sendRequest(request);
        }
        catch(Exception ex)
        {
            logger.severe(ex.getMessage());
            ex.printStackTrace();
        }
        return null;
    }
    


}
