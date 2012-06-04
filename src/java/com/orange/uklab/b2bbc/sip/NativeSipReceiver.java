/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.orange.uklab.b2bbc.sip;
import javax.sip.DialogTerminatedEvent;
import javax.sip.IOExceptionEvent;
import javax.sip.RequestEvent;
import javax.sip.ResponseEvent;
import javax.sip.TimeoutEvent;
import javax.sip.TransactionTerminatedEvent;

/**
 *
 * @author hasanein
 */
public interface NativeSipReceiver
{
    /**
     *
     * @param dialogTerminatedEvent
     */
    public void receiveEvent(DialogTerminatedEvent dialogTerminatedEvent);
    /**
     *
     * @param iOExceptionEvent
     */
    public void receiveEvent(IOExceptionEvent iOExceptionEvent);
    /**
     *
     * @param requestEvent
     */
    public void receiveEvent(RequestEvent requestEvent);
    /**
     *
     * @param responseEvent
     */
    public void receiveEvent(ResponseEvent responseEvent);
    /**
     *
     * @param timeoutEvent
     */
    public void receiveEvent(TimeoutEvent timeoutEvent);
    /**
     *
     * @param transactionTerminatedEvent
     */
    public void receiveEvent(TransactionTerminatedEvent transactionTerminatedEvent);
}
