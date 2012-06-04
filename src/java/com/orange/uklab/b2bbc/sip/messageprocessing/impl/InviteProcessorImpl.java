/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.orange.uklab.b2bbc.sip.messageprocessing.impl;
import com.orange.uklab.b2bbc.descriptor.ServiceUnit;
import com.orange.uklab.b2bbc.descriptor.impl.ServiceUnitImpl;
import com.orange.uklab.b2bbc.runtime.contexts.RuntimeComponentContext;
import com.orange.uklab.b2bbc.sip.impl.B2BDialogImpl;
import com.orange.uklab.b2bbc.sip.impl.B2BSessionImpl;
import com.orange.uklab.b2bbc.sip.impl.SipMessageDispatcherImpl;
import com.orange.uklab.b2bbc.sip.messageprocessing.InviteProcessor;
import com.orange.uklab.b2bbc.sip.states.B2BDialogStates;
import gov.nist.javax.sip.Utils;
import gov.nist.javax.sip.address.SipUri;
import gov.nist.javax.sip.header.RecordRouteList;
import gov.nist.javax.sip.message.SIPRequest;
import gov.nist.javax.sip.message.SIPResponse;
import java.util.Date;
import java.util.logging.Logger;
import javax.sip.Dialog;
import javax.sip.RequestEvent;
import javax.sip.address.SipURI;

/**
 *
 * @author hasanein
 */
public class InviteProcessorImpl implements InviteProcessor
{
    private Logger logger;

    /**
     * 
     */
    public InviteProcessorImpl()
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
     * 
     * @param requestEvent
     * @return
     */
    @Override
    public boolean processIncomingInvite(RequestEvent requestEvent)
    {
        /**
         * Whether or not to forward the received requestEvent to the
         * intersted ServiceUnit or not. Default value is TRUE.
         */
        boolean forwardRequest = true;
        /**
         * Get the Dialog associated with the received INVITE requestEvent, if there
         * is one. if there isn't any Dialog with which this INVITE is associated, then
         * we need to create a new B2BDialog and B2BSession with an INITIAL status.
         */
        Dialog sipDialog = null;
        logger.finest("Determining the SipDialog associated with the received INVITE...");
        sipDialog = isRequestWithinDialog(requestEvent);
        if(sipDialog == null)
        {
            logger.finest("The received INVITE request is not associated with any SIP Dialog...Creating Session Information...");
            /**
             * The received INVITE requestEvent is not part of an existing
             * Dialog.
             */

            /**
             * Get the ServiceUnit associated with the received requestEvent.
             */
            logger.info("Determining the ServiceUnit associated with the received requestEvent...");
            SipUri sipUri = (SipUri) requestEvent.getRequest().getRequestURI();
            ServiceUnitImpl serviceUnit = (ServiceUnitImpl) RuntimeComponentContext.getInstance().getServiceUnit(sipUri.getPort());  
            /**
             * Create a new B2BSession and a new B2BDialog for the newly received
             * requestEvent.
             */
            logger.finest("Creating Session Information and persisting them into memory...");
            createSessionInformation(requestEvent, serviceUnit);
            /**
             * Send a TRYING response back to the requestor
             */
            logger.info("Sending a TRYING response back to the INVITE sender...");
            SIPResponse tryingResponse = prepareTryingResponse(requestEvent);
            SipMessageDispatcherImpl sipMessageDispatcher = new SipMessageDispatcherImpl();
            sipMessageDispatcher.sendResponse(requestEvent, tryingResponse, serviceUnit, null);
        }
        else
        {
            /**
             * The received INVITE requestEvent is part of an existing
             * Dialog.
             *
             * At the moment we do nothing but simply forward the reINVITE
             * to the associated ServiceUnit
             */
        }
        return forwardRequest;
    }

    /**
     * 
     * @param requestEvent
     * @param serviceUnit
     */
    private void createSessionInformation(RequestEvent requestEvent, ServiceUnit serviceUnit)
    {        
         /**
         * Getting the SipRequest from the RequestEvent. and extracting
         * the SipUri version of the RequestUri
         */
        SIPRequest sipRequest = (SIPRequest)requestEvent.getRequest();
        SipUri requestSipUri  = (SipUri)sipRequest.getRequestURI();
        /**
         * Create the B2BSession
         */
        logger.info("Creating a B2BSession...");
        B2BSessionImpl b2BSession = new B2BSessionImpl();
        b2BSession.setB2BSessionCreationTime(new Date());
        b2BSession.setOwnerServiceUnitName(serviceUnit.getName());
        String b2bSessionID = serviceUnit.getName() + "-" + sipRequest.getCallId().getCallId() + "-" + sipRequest.getFromTag() + "-" + b2BSession.getB2BSessionCreationTime().getTime();
        logger.finest("B2BSession ID: " + b2bSessionID);
        b2BSession.setB2BSessionId(b2bSessionID);
        /**
         * Create the B2BDialog
         * At this stage, we will not set the parameters related to the local
         * end of the B2BDialog such as localCSeq as we haven't
         * responded back to the requestor yet with a Dialog creation response
         * (apart from the TRYING response which does not form any dialog)
         * later on these parameters will be set accordingly.
         *
         * At the SIP stack level, the dialog has not been created yet, but
         * what we are trying to do is to create an INITIAL B2BSession and
         * B2BDialog so that the the received WSDL service requests can be
         * matched to an existing session & dialog.
         */
        logger.info("Creating the B2BDialog...");
        B2BDialogImpl b2BDialog = new B2BDialogImpl();
        /**
         * Setting the B2BSessionID
         */
        b2BDialog.setB2BSessionID(b2bSessionID);
        /**
         * Setting the Call-ID of the dialog.
         */
        b2BDialog.setCallId(sipRequest.getCallId().getCallId());
        logger.info("Setting the Dialog Call-ID to: " + sipRequest.getCallId().getCallId());
        /**
         * Setting the dialog creation time.
         */
        b2BDialog.setCreationTime(new Date());
        /**
         * Setting the local Uri of the dialog.
         * The local URI is the URI in the To Header Field.
         */
        SipURI localUri = (SipURI) sipRequest.getTo().getAddress().getURI();
        logger.finest("Setting the dialog local URI to: " + localUri);
        b2BDialog.setLocalUri(localUri);
        /**
         * Setting the remote sequence number (CSeq) which is used
         * to associate request/response pairs to their respective
         * transactions.
         */
        long remoteSequenceNumber = sipRequest.getCSeq().getSeqNumber();
        logger.finest("Setting the remote sequence number to: " + remoteSequenceNumber);
        b2BDialog.setRemoteSeqNumber(remoteSequenceNumber);
        /**
         * Setting the remote tag of the dialog, the remote tag will be set to the value
         * of the From tag.
         */
        String remoteTag = sipRequest.getFromTag();
        b2BDialog.setRemoteTag(remoteTag);
        logger.finest("Setting the remote tag to: " + remoteTag);
        /**
         * Setting the local tag of the dialog, the local tag will be set to the value
         * of the to tag that will be generated using the UTILS helper classes.
         *
         * Note: The assigned To Tag need to be the same one that will be used
         * to send responses back to the SIP client who initially sent this
         * request.
         */
        String toTag = Utils.getInstance().generateTag();
        b2BDialog.setLocalTag(toTag);
        logger.finest("Setting the local tag to: " + toTag);
        /**
         * Setting the remote target URI of the dialog to the value
         * of the URI passed in the Contacts header field.
         */
        SipUri remoteTargetUri = (SipUri) sipRequest.getContactHeader().getAddress().getURI();
        logger.finest("Setting the remote target URI to: " + remoteTargetUri.toString());
        b2BDialog.setRemoteTarget(remoteTargetUri);
        /**
         * Setting the remote URI of the dialog to the value of the
         * From header field.
         */
        SipUri remoteUri = (SipUri) sipRequest.getFromHeader().getAddress().getURI();
        logger.finest("Setting the remote URI to: " + remoteTargetUri.toString());
        b2BDialog.setRemoteUri(remoteUri);
        /**
         * Setting the route-set parameter of the Dialog to the value of
         * the Route header field.
         */
        RecordRouteList recordRouteSet = sipRequest.getRecordRouteHeaders();
        if(recordRouteSet == null)
        {
            logger.finest("The received SIP Message does not have any RecordRouteSet...");
            logger.finest("The dialog routeSet property is null...");
        }
        else
        {
            b2BDialog.setRouteSet(recordRouteSet);
            logger.finest("Setting the dialog routeSet property, " + recordRouteSet.size() + " record routes found.");
        }
        /**
         * Setting the secure flag if the dialog
         */
        String transportParameter = ((SipURI)sipRequest.getRequestURI()).getTransportParam();
        /**
         * If the transport parameter is not set, then the request is considered to be
         * insecure. Similarly, the request is considered to be insecure if the transport
         * parameter is set to anything other than "TLS".
         * Another condition must be met for the secure flag to be set which is that the
         * SipURI must be of the right secure scheme which is "SIPS".
         */
        boolean isSecureTransport = false;
        boolean isSecureUri = false;

        if(transportParameter == null)
        {
            isSecureTransport = false;
        }
        else
        {
            if(!transportParameter.equals("TLS"))
            {
                isSecureTransport = false;
            }
            else
            {
                isSecureTransport = true;
            }
        }
        /**
         * Checking for the SipUri Scheme
         */
        if(requestSipUri.getScheme().equals("SIPS"))
        {
            isSecureUri = true;
        }
        /**
         * Setting the secure flag
         */
        if((isSecureTransport == true) && (isSecureUri == true))
        {
            b2BDialog.setSecure(true);
            logger.finest("Setting the Dialog secure flag to: true");
        }
        else
        {
            b2BDialog.setSecure(false);
            logger.finest("Setting the Dialog secure flag to: false");
        }
        /**
         * Setting the Dialog state, the state of this dialog should be
         * set to INITIAL at this stage of the B2BDialog lifecycle.
         */
        b2BDialog.setState(B2BDialogStates.INITIAL.toString());
        logger.finest("The state of the B2BDialog has been set to: " + b2BDialog.getState());
        /**
         * Setting the DialogID
         */
        String dialogId = sipRequest.getDialogId(true, toTag);
        b2BDialog.setDialogId(dialogId);
        logger.finest("Dialog ID has been set to " + dialogId);
        /**
         * Add the newly created Dialog to the B2BSession
         */
        b2BSession.addB2BDialog(b2BDialog);
        logger.info("The newly created B2BDialog has been added to the B2BSession with ID: " + b2BSession.getB2BSessionId());
        /**
         * Add the originalRequestEvent as an attribute inside the created
         * B2BDialog.
         */
        b2BDialog.setAttribute("originalRequestEvent", requestEvent);
        logger.info("Stroing the originlRequestEvent inside the B2BDialog...");
        /**
         * Add the newly created B2BSession to the RuntimeComponentContext
         */
        RuntimeComponentContext runtimeComponentContext = RuntimeComponentContext.getInstance();
        runtimeComponentContext.addB2BSession(b2BSession);
        logger.info("The newly created B2BSession has been added to the RuntimeComponentContext");
    }

    private SIPResponse prepareTryingResponse(RequestEvent requestEvent)
    {       
        logger.fine("Preparing the TRYING response...");
        SIPRequest sipRequest = (SIPRequest) requestEvent.getRequest();
        SIPResponse sipResponse = sipRequest.createResponse(SIPResponse.TRYING, "TRYING");
        return sipResponse;
    }

    private Dialog isRequestWithinDialog(RequestEvent requestEvent)
    {
        logger.finest("Trying to retrieve the SipDialog from the received requestEvent...");
        Dialog sipDialog = requestEvent.getDialog();
        if(sipDialog != null)
        {
            logger.info("SipDialog retrieved from the received RequestEvent...");
            return sipDialog;
        }
        else
        {
            logger.info("Searching the RuntimeComponentContext for a possible Dialog with which this requestEvent is associated...");
            SIPRequest sipRequest = (SIPRequest) requestEvent.getRequest();
            String dialogId = sipRequest.getDialogId(true);
            sipDialog = (Dialog) RuntimeComponentContext.getInstance().getSipDialog(dialogId);
            if(sipDialog == null)
            {
                logger.info("The received INVITE requestEvent is not associated with any Dialog...");
            }
            return sipDialog;
        }
    }

}
