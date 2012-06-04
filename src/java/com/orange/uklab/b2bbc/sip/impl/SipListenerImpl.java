/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.orange.uklab.b2bbc.sip.impl;

import com.orange.uklab.b2bbc.runtime.contexts.RuntimeComponentContext;
import gov.nist.javax.sip.message.SIPRequest;
import java.util.logging.Logger;
import javax.sip.DialogTerminatedEvent;
import javax.sip.IOExceptionEvent;
import javax.sip.RequestEvent;
import javax.sip.ResponseEvent;
import javax.sip.SipListener;
import javax.sip.TimeoutEvent;
import javax.sip.TransactionTerminatedEvent;

/**
 *
 * @author hasanein
 */
public class SipListenerImpl implements SipListener
{
    private SipDirectorImpl sipDirector = null;
    private Logger logger = null;
    
    /**
     *
     */
    /**
     *
     */
    public SipListenerImpl()
    {
        initLogger();
        this.sipDirector = new SipDirectorImpl();
    }

    private void initLogger()
    {
        RuntimeComponentContext runtimeComponentContext = RuntimeComponentContext.getInstance();
        logger = runtimeComponentContext.getLogger(this.getClass().getName(), null);
    }
    
    @Override
    public void processRequest(RequestEvent requestEvent)
    {        
        logger.info("processRequest: " + "Message Received: " + requestEvent.getRequest().getMethod());
        this.sipDirector.receiveEvent(requestEvent);
    }

    @Override
    public void processResponse(ResponseEvent responseEvent)
    {
        logger.info("processResponse: " + "Message Received: " + responseEvent.getResponse().getReasonPhrase());
        this.sipDirector.receiveEvent(responseEvent);
    }

    @Override
    public void processTimeout(TimeoutEvent timeoutEvent)
    {
        logger.info("processTimeout: " + "TimeOut Received: " + timeoutEvent.toString());
        this.sipDirector.receiveEvent(timeoutEvent);
    }

    @Override
    public void processIOException(IOExceptionEvent exceptionEvent)
    {
        logger.info("processIOException: " + "IOExceptionEvent Received: " + exceptionEvent.toString());
        this.sipDirector.receiveEvent(exceptionEvent);
    }

    @Override
    public void processTransactionTerminated(TransactionTerminatedEvent transactionTerminatedEvent)
    {
        logger.info("processTransactionTerminated: " + "TransactionTerminatedEvent Received: " + transactionTerminatedEvent.toString());
        this.sipDirector.receiveEvent(transactionTerminatedEvent);
    }

    @Override
    public void processDialogTerminated(DialogTerminatedEvent dialogTerminatedEvent)
    {
        logger.info("processDialogTerminated: " + "DialogTerminatedEvent Received: " + dialogTerminatedEvent.toString());        
        this.sipDirector.receiveEvent(dialogTerminatedEvent);
    }

}
