/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.orange.uklab.b2bbc.wsdlparsing;
import gov.nist.javax.sip.message.SIPMessage;
import javax.xml.transform.Source;

/**
 *
 * @author hasanein
 */
public interface MessageNormalizer
{
    public Source normalizeSipMessage(SIPMessage sipMessage);
}
