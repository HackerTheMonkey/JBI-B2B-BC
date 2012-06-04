/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.orange.uklab.b2bbc.sip.messageprocessing;
import javax.sip.RequestEvent;

/**
 *
 * @author hasanein
 */
public interface RegisterProcessor
{
    /**
     *
     * @param requestEvent
     * @return
     */
    /**
     *
     * @param requestEvent
     * @return
     */
    public boolean processIncomingRegister(RequestEvent requestEvent);
}
