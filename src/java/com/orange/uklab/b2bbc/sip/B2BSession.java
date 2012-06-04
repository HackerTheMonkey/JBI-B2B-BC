/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.orange.uklab.b2bbc.sip;
import java.util.Date;

/**
 *
 * @author hasanein
 */
public interface B2BSession
{
    /**
     *
     * @return
     */
    public String getOwnerServiceUnitName();
    /**
     *
     * @param serviceUnitName
     */
    public void setOwnerServiceUnitName(String serviceUnitName);

    /**
     *
     * @param attributeName
     * @return
     */
    public Object getAttribute(String attributeName);
    /**
     *
     * @param attributeName
     * @param attribute
     */
    public void setAttribute(String attributeName, Object attribute);

    /**
     *
     * @return
     */
    public Object getTimer();
    /**
     *
     * @param timer
     */
    public void setTimer(Object timer);

    /**
     *
     * @param dialogId
     * @return
     */
    public B2BDialog getB2BDialog(String dialogId, boolean isFullDialogID, String fromTag);
    /**
     * 
     * @param b2BDialog
     */
    public void addB2BDialog(B2BDialog b2BDialog);
    /**
     *
     * @param dialogId
     */
    public void removeB2BDialog(String dialogId);

    /**
     *
     * @return
     */
    public Date getB2BSessionCreationTime();
    /**
     *
     * @param creationTime
     */
    public void setB2BSessionCreationTime(Date creationTime);

    /**
     *
     * @return
     */
    public String getB2BSessionId();
    /**
     *
     * @param b2bSessionID
     */
    public void setB2BSessionId(String b2bSessionID);
}
