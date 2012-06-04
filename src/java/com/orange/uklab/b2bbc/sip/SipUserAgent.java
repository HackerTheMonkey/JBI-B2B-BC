/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.orange.uklab.b2bbc.sip;
import com.orange.uklab.b2bbc.descriptor.ServiceUnit;
import javax.sip.Dialog;
import javax.sip.RequestEvent;
import javax.sip.message.Request;
import javax.sip.message.Response;

/**
 * Processing Requests and generating responses.
 * @author hasanein
 */
public interface SipUserAgent
{
    /**
     * This method is used to receive and process the incoming SIP
     * messages received via SIP native interface through the SIP stack.
     * @param originalRequestEvent
     * @param serviceUnit
     */
    public void receiveRequest( RequestEvent originalRequestEvent, ServiceUnit serviceUnit);
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
    public Dialog sendResponse(RequestEvent originalRequestEvent,Response response, ServiceUnit serviceUnit);
    /**
     *
     * @param request
     * @param serviceUnit
     */
    public Dialog sendRequest(Request request, ServiceUnit serviceUnit);
}
