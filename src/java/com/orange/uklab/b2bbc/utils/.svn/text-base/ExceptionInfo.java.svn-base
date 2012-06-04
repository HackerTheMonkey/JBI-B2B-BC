/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.orange.uklab.b2bbc.utils;

/**
 *
 * @author lhhm2777
 */
public class ExceptionInfo
{

    private int nestingLevel = 0;
    private TaskStatusMessage taskStatusMessage = null;
    private String stackTrace = null;

    /**
     * 
     * @param nestingLevel
     * @param taskStatusMessage
     * @param stackTrace
     */
    public ExceptionInfo(int nestingLevel, TaskStatusMessage taskStatusMessage, String stackTrace)
    {
        this.nestingLevel = nestingLevel;
        this.stackTrace = stackTrace;
        this.taskStatusMessage = taskStatusMessage;
    }

    /**
     * 
     * @return
     */
    public int getNestingLevel()
    {
        return nestingLevel;
    }

    /**
     * 
     * @param nestingLevel
     */
    public void setNestingLevel(int nestingLevel)
    {
        this.nestingLevel = nestingLevel;
    }

    /**
     * 
     * @return
     */
    public String getStackTrace()
    {
        return stackTrace;
    }

    /**
     * 
     * @param stackTrace
     */
    public void setStackTrace(String stackTrace)
    {
        this.stackTrace = stackTrace;
    }

    /**
     * 
     * @return
     */
    public TaskStatusMessage getTaskStatusMessage()
    {
        return taskStatusMessage;
    }

    /**
     * 
     * @param taskStatusMessage
     */
    public void setTaskStatusMessage(TaskStatusMessage taskStatusMessage)
    {
        this.taskStatusMessage = taskStatusMessage;
    }
}
