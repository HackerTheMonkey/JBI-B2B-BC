/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.orange.uklab.b2bbc.utils;

/**
 *
 * @author lhhm2777
 */
public class TaskStatusMessage
{

    private String locToken = null;
    private String locMessage = null;
    private String[] locParameters = null;

    /**
     * 
     * @param locToken
     * @param locMessage
     * @param locParameters
     */
    public TaskStatusMessage(String locToken, String locMessage, String[] locParameters)
    {
        this.locMessage = locMessage;
        this.locToken = locToken;
        this.locParameters = locParameters;
    }

    /**
     * 
     * @return
     */
    public String getLocMessage()
    {
        return locMessage;
    }

    /**
     * 
     * @param locMessage
     */
    public void setLocMessage(String locMessage)
    {
        this.locMessage = locMessage;
    }

    /**
     * 
     * @return
     */
    public String[] getLocParameters()
    {
        return locParameters;
    }

    /**
     * 
     * @param locParameters
     */
    public void setLocParameters(String[] locParameters)
    {
        this.locParameters = locParameters;
    }

    /**
     * 
     * @return
     */
    public String getLocToken()
    {
        return locToken;
    }

    /**
     * 
     * @param locToken
     */
    public void setLocToken(String locToken)
    {
        this.locToken = locToken;
    }
}
