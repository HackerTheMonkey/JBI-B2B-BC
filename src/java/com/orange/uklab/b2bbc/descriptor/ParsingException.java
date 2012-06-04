/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.orange.uklab.b2bbc.descriptor;

/**
 *
 * @author hasanein
 */
public class ParsingException extends Exception
{
    /**
     *
     * @param cause
     */
    public ParsingException(Throwable cause)
    {
        super(cause);
    }

    /**
     *
     * @param message
     * @param cause
     */
    public ParsingException(String message, Throwable cause)
    {
        super(message, cause);
    }

    /**
     *
     * @param message
     */
    public ParsingException(String message)
    {
        super(message);
    }

    /**
     *
     */
    public ParsingException()
    {
    }

}
