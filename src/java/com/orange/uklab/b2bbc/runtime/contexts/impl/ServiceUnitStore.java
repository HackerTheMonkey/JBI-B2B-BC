/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.orange.uklab.b2bbc.runtime.contexts.impl;
import com.orange.uklab.b2bbc.descriptor.ServiceUnit;
import javax.jbi.servicedesc.ServiceEndpoint;

/**
 *
 * @author hasanein
 */
public interface ServiceUnitStore
{
    /**
     *
     */
    /**
     *
     */
    public final String SERVICE_UNIT_STATUS_STARTED = "started";
    /**
     *
     */
    /**
     *
     */
    public final String SERVICE_UNIT_STATUS_SHUTDOWN = "shutdown";
    /**
     *
     */
    /**
     *
     */
    public final String SERVICE_UNIT_STATUS_STOPPED = "stopped";
    
    /**
     *
     * @param serviceUnit
     * @return
     * @throws StorageException
     */
    /**
     *
     * @param serviceUnit
     * @return
     * @throws StorageException
     */
    public boolean storeServiceUnit(ServiceUnit serviceUnit) throws StorageException;
    /**
     *
     * @param serviceUnits
     * @throws StorageException
     */
    /**
     *
     * @param serviceUnits
     * @throws StorageException
     */
    public void storeServiceUnits(ServiceUnit[] serviceUnits) throws StorageException;
    /**
     *
     * @param serviceUnitName
     * @return
     * @throws StorageException
     */
    /**
     *
     * @param serviceUnitName
     * @return
     * @throws StorageException
     */
    public boolean removeServiceUnit(String serviceUnitName) throws StorageException;
    /**
     *
     * @param serviceUnitName
     * @return
     */
    /**
     *
     * @param serviceUnitName
     * @return
     */
    public ServiceUnit getServiceUnit(String serviceUnitName);
    /**
     *
     * @param serviceUnitName
     * @return
     */
    /**
     *
     * @param serviceUnitName
     * @return
     */
    public String getServiceUnitStatus(String serviceUnitName);
    /**
     *
     * @param serviceUnitName
     * @param status
     * @throws IllegalArgumentException
     * @throws StorageException
     */
    /**
     *
     * @param serviceUnitName
     * @param status
     * @throws IllegalArgumentException
     * @throws StorageException
     */
    public void setServiceUnitStatus(String serviceUnitName, String status) throws IllegalArgumentException, StorageException;
    /**
     *
     * @return
     */
    /**
     *
     * @return
     */
    public ServiceEndpoint[] getAllDefinedEndpoints();
    /**
     *
     * @param serviceUnitName
     * @return
     */
    /**
     *
     * @param serviceUnitName
     * @return
     */
    public ServiceEndpoint[] getServiceUnitEndpoints(String serviceUnitName);
    /**
     *
     * @param endpointName
     * @return
     */
    /**
     *
     * @param endpointName
     * @return
     */
    public ServiceEndpoint getServiceEndpoint(String endpointName);
    /**
     *
     * @return
     */
    /**
     *
     * @return
     */
    public ServiceUnit[] getAllServiceUnits();
    /**
     *
     */
    /**
     *
     */
    public void clear();
}
