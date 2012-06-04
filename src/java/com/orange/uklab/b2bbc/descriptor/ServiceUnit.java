/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.orange.uklab.b2bbc.descriptor;
import javax.jbi.servicedesc.ServiceEndpoint;

/**
 *
 * @author hasanein
 */
public interface ServiceUnit extends DescriptorObject
{            
    /**
     * Implementation of this method shall return back a list of ServiceEndpoints
     * that are to be consumed/provided by the respective component that they
     * are being deployed to.
     * @return
     */
    public ServiceEndpoint[] getServiceEndpoints();
    /**
     * Implementation of this method shall return a boolean value indicating
     * whether the service artifact is targted to a Binding Component or to
     * a Service Engine.
     * @return
     */
    public boolean isBindingComponent();
    /**
     * Implementation of this method shall return all the extensions defined
     * in the services element. If there are no extensions, this method shall
     * return null.
     * @return
     */
    public Object[] getExtensions();

    /**
     *
     * @return
     */
    public String getStatus();

    /**
     *
     * @param port
     */
    public void setBindingPort(int port);
    /**
     *
     * @return
     */
    public int getBindingPort();

    /**
     *
     * @param address
     */
    public void setBindingAddress(String address);
    /**
     *
     * @return
     */
    public String getBindingAddress();

    /**
     *
     * @param protocol
     */
    public void setBindingProtocol(String protocol);
    /**
     *
     * @return
     */
    public String getBindingProtocol();
}
