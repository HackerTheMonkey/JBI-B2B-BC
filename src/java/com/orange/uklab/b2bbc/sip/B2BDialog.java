/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.orange.uklab.b2bbc.sip;
import gov.nist.javax.sip.header.RecordRouteList;
import java.util.Date;
import javax.sip.address.SipURI;

/**
 *
 * @author hasanein
 */
public interface B2BDialog
{

    Object getAttribute(String attributeName);

    String getB2BSessionID();

    String getCallId();

    Date getCreationTime();

    long getLocalSeqNumber();

    String getLocalTag();

    SipURI getLocalUri();

    long getRemoteSeqNumber();

    String getRemoteTag();

    SipURI getRemoteTarget();

    SipURI getRemoteUri();

    RecordRouteList getRouteSet();

    String getState();

    boolean isSecure();

    void setAttribute(String attributeName, Object attribute);

    void setB2BSessionID(String b2bSessionId);

    void setCallId(String callId);

    void setCreationTime(Date creationTime);

    void setLocalSeqNumber(long localSeqNumber);

    void setLocalTag(String localTag);

    void setLocalUri(SipURI sipURI);

    void setRemoteSeqNumber(long remoteSeqNumber);

    void setRemoteTag(String remoteTag);

    void setRemoteTarget(SipURI remoteTarget);

    void setRemoteUri(SipURI sipURI);

    void setRouteSet(RecordRouteList routeSet);

    void setSecure(boolean secureFlag);

    void setState(String state);

       
}
