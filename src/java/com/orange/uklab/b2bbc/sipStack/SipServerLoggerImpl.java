/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.orange.uklab.b2bbc.sipStack;
import com.orange.uklab.b2bbc.runtime.contexts.RuntimeComponentContext;
import gov.nist.core.ServerLogger;
import gov.nist.javax.sip.message.SIPMessage;
import java.util.Properties;
import java.util.logging.Logger;
import javax.sip.SipStack;

/**
 *
 * @author hasanein
 */
public class SipServerLoggerImpl implements ServerLogger
{
    private Logger logger = null;

    /**
     *
     */
    public SipServerLoggerImpl()
    {
        initLogger();
    }

    private void initLogger()
    {
        this.logger = RuntimeComponentContext.getInstance().getLogger(this.getClass().getName(), null);
    }
    
    /**
     *
     */
    @Override
    public void closeLogFile()
    {
        logger.finer("closeLogFile Called");
    }

    /**
     *
     * @param arg0
     * @param arg1
     * @param arg2
     * @param arg3
     * @param arg4
     */
    @Override
    public void logMessage(SIPMessage arg0, String arg1, String arg2, boolean arg3, long arg4)
    {
        logger.finer(arg0 + " " + arg1 + " " + arg2);
    }

    /**
     *
     * @param arg0
     * @param arg1
     * @param arg2
     * @param arg3
     * @param arg4
     * @param arg5
     */
    @Override
    public void logMessage(SIPMessage arg0, String arg1, String arg2, String arg3, boolean arg4, long arg5)
    {
        logger.finer(arg0 + " " + arg1 + " " + arg2 + " " + arg3);
    }

    /**
     *
     * @param arg0
     * @param arg1
     * @param arg2
     * @param arg3
     * @param arg4
     */
    @Override
    public void logMessage(SIPMessage arg0, String arg1, String arg2, String arg3, boolean arg4)
    {
        logger.finer(arg0 + " " + arg1 + " " + arg2 + " " + arg3);
    }

    /**
     *
     * @param arg0
     */
    @Override
    public void logException(Exception arg0)
    {
        arg0.printStackTrace();
        logger.finer("Exception: " + arg0.getMessage());
    }

    /**
     *
     * @param arg0
     */
    @Override
    public void setStackProperties(Properties arg0)
    {
        logger.finer("setStackProperties Called");
    }

    /**
     *
     * @param arg0
     */
    @Override
    public void setSipStack(SipStack arg0)
    {
        logger.finer("setSipStack Called");
    }

}
