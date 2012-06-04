/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.orange.uklab.b2bbc.main;

/**
 *
 * @author lhhm2777
 */
public class B2BException extends Exception
{
    /**
     * 
     * @param errorMessage
     */
    public B2BException(String errorMessage)
    {
        super(errorMessage);
    }

    /**
     *
     * @param errorMessage
     * @param cause
     */
    public B2BException(String errorMessage, Throwable cause)
    {
        super(errorMessage, cause);
    }

    /**
     * 
     * @param cause
     */
    public B2BException(Throwable cause)
    {
        super(cause);
    }
}
