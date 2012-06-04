/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.orange.uklab.b2bbc.sipStack;
import com.orange.uklab.b2bbc.runtime.contexts.RuntimeComponentContext;
import gov.nist.core.StackLogger;
import java.util.Properties;
import java.util.logging.Logger;

/**
 *
 * @author hasanein
 */
public class SipStackLoggerImpl implements StackLogger
{
    private Logger logger = null;

    /**
     *
     */
    public SipStackLoggerImpl()
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
    public void logStackTrace()
    {
        logger.finer("logStackTrace() method called");
    }

    /**
     *
     * @param arg0
     */
    @Override
    public void logStackTrace(int arg0)
    {
        
        logger.finer("logStackTrace(int arg0) method called " + arg0);
    }

    /**
     *
     * @return
     */
    @Override
    public int getLineCount()
    {
        /**
         * A mock line count
         */
        return 10;
    }

    /**
     *
     * @param arg0
     */
    @Override
    public void logException(Throwable arg0)
    {
        arg0.printStackTrace();
        logger.finer("Exception: " + arg0.getMessage());
    }

    /**
     *
     * @param arg0
     */
    @Override
    public void logDebug(String arg0)
    {
        logger.finer("DEBUG: " + arg0);
    }

    /**
     *
     * @param arg0
     */
    @Override
    public void logTrace(String arg0)
    {
        logger.finer("TRACE: " + arg0);
    }

    /**
     *
     * @param arg0
     */
    @Override
    public void logFatalError(String arg0)
    {
        logger.finer("FATAL ERROR: " + arg0);
    }

    /**
     *
     * @param arg0
     */
    @Override
    public void logError(String arg0)
    {
        logger.finer("ERROR: " + arg0);
    }

    /**
     *
     * @return
     */
    @Override
    public boolean isLoggingEnabled()
    {
       return true;
    }

    /**
     *
     * @param arg0
     * @return
     */
    @Override
    public boolean isLoggingEnabled(int arg0)
    {
        return true;
    }

    /**
     *
     * @param arg0
     * @param arg1
     */
    @Override
    public void logError(String arg0, Exception arg1)
    {
        logger.finer("ERROR WITH EXCEPTION: " + arg0 + " : " + arg1.getMessage());
        arg1.printStackTrace();
    }

    /**
     *
     * @param arg0
     */
    @Override
    public void logWarning(String arg0)
    {
        logger.finer("WARNING: " + arg0);
    }

    /**
     *
     * @param arg0
     */
    @Override
    public void logInfo(String arg0)
    {
        logger.finer("INFO: " + arg0);
    }

    /**
     *
     */
    @Override
    public void disableLogging()
    {
        logger.finer("disableLogging has been called");
    }

    /**
     *
     */
    @Override
    public void enableLogging()
    {
        logger.finer("enableLogging has been called");
    }

    /**
     *
     * @param arg0
     */
    @Override
    public void setBuildTimeStamp(String arg0)
    {
        logger.finer("setBuildTimeStamp: " + arg0);
    }

    /**
     *
     * @param arg0
     */
    @Override
    public void setStackProperties(Properties arg0)
    {
        logger.finer("setStackProperties() has been called");
    }

    /**
     *
     * @return
     */
    @Override
    public String getLoggerName()
    {
        return "SipB2BBC_Logger";
    }

}
