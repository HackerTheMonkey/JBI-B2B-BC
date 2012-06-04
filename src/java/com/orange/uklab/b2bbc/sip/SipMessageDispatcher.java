/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.orange.uklab.b2bbc.sip;
import com.orange.uklab.b2bbc.descriptor.ServiceUnit;
import javax.sip.RequestEvent;
import javax.sip.message.Request;
import javax.sip.message.Response;

/**
 *
 * @author hasanein
 */
public interface SipMessageDispatcher
{
    /**
     *
     * @param originalRequestEvent
     * @param serviceUnit
     */
    public void receiveRequest(RequestEvent originalRequestEvent, ServiceUnit serviceUnit);
    /**
     *
     * @param response
     * @param serviceUnit
     */
    public void receiveResponse(Response response, ServiceUnit serviceUnit);
    /**
     *
     * @param originalRequestEvent
     * @param response
     * @param serviceUnit
     */
    public void sendResponse(RequestEvent originalRequestEvent,Response response, ServiceUnit serviceUnit, String toTag);
    /**
     *
     * @param request
     * @param serviceUnit
     */
    public void sendRequest(Request request, ServiceUnit serviceUnit);
}
