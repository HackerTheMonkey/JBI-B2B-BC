/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.orange.uklab.b2bbc.nmr;
import javax.jbi.JBIException;
import javax.jbi.servicedesc.ServiceEndpoint;

/**
 *
 * @author hasanein
 */
public interface NmrAgent
{
    /**
     *
     * @param serviceEndpoint
     * @throws JBIException
     */
    public void activateServiceEndpoint(ServiceEndpoint serviceEndpoint) throws JBIException;
    /**
     *
     * @param serviceEndpoint
     * @throws JBIException
     */
    public void deActivateServiceEndpoint(ServiceEndpoint serviceEndpoint) throws JBIException;
    /**
     *
     * @param normalizedMessageContent
     * @param serviceEndpoint
     */
    public void sendNormalizedMessage(javax.xml.transform.Source normalizedMessageContent, ServiceEndpoint serviceEndpoint, Object messageContent);
}
