/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.orange.uklab.b2bbc.sip.impl;

import com.orange.uklab.b2bbc.sip.B2BDialog;
import com.orange.uklab.b2bbc.sip.B2BSession;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

/**
 *
 * @author hasanein
 */
public class B2BSessionImpl implements B2BSession
{
    private String ownerServiceUnitName = null;
    private HashMap<String, Object> attributes = new HashMap<String, Object>();
    private Object timer = null;
    private HashMap<String, B2BDialog> b2bDialogsDB = new HashMap<String, B2BDialog>();
    private Date creationTime = null;    
    private String b2bSessionID = null;

    /**
     *
     * @return
     */
    @Override
    public String getOwnerServiceUnitName()
    {
        return ownerServiceUnitName;
    }

    /**
     *
     * @param serviceUnitName
     */
    @Override
    public void setOwnerServiceUnitName(String serviceUnitName)
    {
        this.ownerServiceUnitName = serviceUnitName;
    }

    /**
     *
     * @param attributeName
     * @return
     */
    @Override
    public Object getAttribute(String attributeName)
    {
        return attributes.get(attributeName);
    }

    /**
     *
     * @param attributeName
     * @param attribute
     */
    @Override
    public void setAttribute(String attributeName, Object attribute)
    {
        this.attributes.put(attributeName, attribute);
    }

    /**
     *
     * @return
     */
    @Override
    public Object getTimer()
    {
        return timer;
    }

    /**
     *
     * @param timer
     */
    @Override
    public void setTimer(Object timer)
    {
        this.timer = timer;
    }

    /**
     *
     * @param dialogId
     * @return
     */
    @Override
    public B2BDialog getB2BDialog(String dialogId, boolean isFullDialogID, String fromTag)
    {
        if(isFullDialogID)
        {
            return b2bDialogsDB.get(dialogId);
        }
        else
        {
            Iterator iterator = b2bDialogsDB.entrySet().iterator();
            while(iterator.hasNext())
            {
                Map.Entry keyValuePairs = (Entry) iterator.next();
                String _dialogID = (String) keyValuePairs.getKey();
                System.out.println("Comparing " + _dialogID + " with " + dialogId);
                if((_dialogID.contains(dialogId.toLowerCase())) && (_dialogID.contains(fromTag.toLowerCase())))
                {
                    return (B2BDialog) keyValuePairs.getValue();
                }
            }
            return null;
        }
    }
    
    /**
     *
     * @param b2BDialog
     */
    @Override
    public void addB2BDialog(B2BDialog b2BDialog)
    {
        String dialogId;
//        dialogId = b2BDialog.getCallId() + b2BDialog.getLocalTag() + b2BDialog.getRemoteTag();
        dialogId = ((B2BDialogImpl)b2BDialog).getDialogId();
        this.b2bDialogsDB.put(dialogId, b2BDialog);
    }

    /**
     *
     * @param dialogId
     */
    @Override
    public void removeB2BDialog(String dialogId)
    {
        this.b2bDialogsDB.remove(dialogId);
    }

    /**
     *
     * @return
     */
    @Override
    public Date getB2BSessionCreationTime()
    {
        return creationTime;
    }


    /**
     *
     * @param creationTime
     */
    @Override
    public void setB2BSessionCreationTime(Date creationTime)
    {
        this.creationTime = creationTime;
    }

    /**
     *
     * @return
     */
    @Override
    public String getB2BSessionId()
    {
        return b2bSessionID;
    }

    /**
     *
     * @param b2bSessionID
     */
    @Override
    public void setB2BSessionId(String b2bSessionID)
    {
        this.b2bSessionID = b2bSessionID;
    }

}
