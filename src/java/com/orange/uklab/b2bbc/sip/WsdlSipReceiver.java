/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.orange.uklab.b2bbc.sip;
import com.orange.uklab.b2bbc.descriptor.ServiceUnit;
import gov.nist.javax.sip.message.SIPRequest;
import gov.nist.javax.sip.message.SIPResponse;

/**
 *
 * @author hasanein
 */
public interface WsdlSipReceiver
{
    /**
     *
     * @param request
     * @param serviceUnit
     */
    public void sendRequest(SIPRequest request, ServiceUnit serviceUnit);
    /**
     *
     * @param response
     * @param serviceUnit
     */
    public void sendResponse(SIPResponse response, ServiceUnit serviceUnit);
}
