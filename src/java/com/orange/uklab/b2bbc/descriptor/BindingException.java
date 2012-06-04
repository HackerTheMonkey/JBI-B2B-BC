/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.orange.uklab.b2bbc.descriptor;

/**
 *
 * @author hasanein
 */
public class BindingException extends Exception
{

    /**
     *
     * @param cause
     */
    public BindingException(Throwable cause)
    {
        super(cause);
    }

    /**
     *
     * @param message
     * @param cause
     */
    public BindingException(String message, Throwable cause)
    {
        super(message, cause);
    }

    /**
     *
     * @param message
     */
    public BindingException(String message)
    {
        super(message);
    }

    /**
     *
     */
    public BindingException()
    {
    }

}
