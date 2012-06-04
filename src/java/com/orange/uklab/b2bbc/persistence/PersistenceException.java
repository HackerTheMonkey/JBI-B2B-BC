/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.orange.uklab.b2bbc.persistence;

/**
 *
 * @author hasanein
 */
public class PersistenceException extends Exception
{
    /**
     * 
     * @param errorMessage
     */
    public PersistenceException(String errorMessage)
    {
        super(errorMessage);
    }

    /**
     * 
     * @param errorMessage
     * @param cause
     */
    public PersistenceException(String errorMessage, Throwable cause)
    {
        super(errorMessage, cause);
    }

    /**
     * 
     * @param cause
     */
    public PersistenceException(Throwable cause)
    {
        super(cause);
    }
}
