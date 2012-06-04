/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.orange.uklab.b2bbc.descriptor.impl;
import com.orange.uklab.b2bbc.descriptor.ServiceUnit;
import com.orange.uklab.b2bbc.persistence.Persistable;
import java.net.URI;
import javax.jbi.servicedesc.ServiceEndpoint;

/**
 *
 * @author hasanein
 */
public class ServiceUnitImpl implements ServiceUnit, Persistable
{

    private String name;
    private String jbiDescriptorVersion;
    private URI nameSapce;
    private ServiceEndpointImpl[] serviceEndpoints;
    private String status;
    private boolean isBindingCommponent;
    private int bindingPort;
    private String bindingAddress;
    private String bindingPtotocol;

    /**
     *
     * @param name
     * @param jbiDescriptionVersion
     * @param namespace
     * @param serviceEndpoints
     * @param status
     * @param isBindingComponent
     */
    public ServiceUnitImpl(String name, String jbiDescriptionVersion, URI namespace, ServiceEndpointImpl[] serviceEndpoints, String status, boolean isBindingComponent)
    {
        this.name = name;
        this.jbiDescriptorVersion = jbiDescriptionVersion;
        this.nameSapce = namespace;
        this.serviceEndpoints = serviceEndpoints;
        this.status = status;
        this.isBindingCommponent = isBindingComponent;
    }
    
    @Override
    public String getName()
    {
        return name;
    }

    @Override
    public String getJbiDescriptorVersion()
    {
        return jbiDescriptorVersion;
    }

    @Override
    public URI getNamepace()
    {
        return nameSapce;
    }

    @Override
    public ServiceEndpoint[] getServiceEndpoints()
    {
        return serviceEndpoints;
    }

    @Override
    public boolean isBindingComponent()
    {
        return isBindingCommponent;
    }

    @Override
    public Object[] getExtensions()
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     *
     * @return
     */
    @Override
    public String getStatus()
    {
        return status;
    }

    /**
     *
     * @return
     */
    @Override
    public String getObjectId()
    {
        return getName();
    }

    /**
     *
     * @param port
     */
    @Override
    public void setBindingPort(int port)
    {
        bindingPort = port;
    }

    /**
     *
     * @return
     */
    @Override
    public int getBindingPort()
    {
        return bindingPort;
    }

    /**
     *
     * @param address
     */
    @Override
    public void setBindingAddress(String address)
    {
        this.bindingAddress = address;
    }

    /**
     *
     * @return
     */
    @Override
    public String getBindingAddress()
    {
        return bindingAddress;
    }

    /**
     *
     * @param protocol
     */
    @Override
    public void setBindingProtocol(String protocol)
    {
        bindingPtotocol = protocol;
    }

    /**
     *
     * @return
     */
    @Override
    public String getBindingProtocol()
    {
        return bindingPtotocol;
    }

}
