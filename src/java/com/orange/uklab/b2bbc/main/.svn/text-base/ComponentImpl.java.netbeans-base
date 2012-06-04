/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.orange.uklab.b2bbc.main;

import com.orange.uklab.b2bbc.lifecycle.ComponentLifeCycleImpl;
import com.orange.uklab.b2bbc.lifecycle.ServiceUnitManagerImpl;
import javax.jbi.component.Component;
import javax.jbi.component.ComponentLifeCycle;
import javax.jbi.component.ServiceUnitManager;
import javax.jbi.messaging.MessageExchange;
import javax.jbi.servicedesc.ServiceEndpoint;
import org.w3c.dom.Document;
import org.w3c.dom.DocumentFragment;

/**
 *
 * @author hasaneinali
 */
public class ComponentImpl implements Component
{

    /**
     * 
     * @return
     */
    @Override
    public ComponentLifeCycle getLifeCycle()
    {
        ComponentLifeCycleImpl componentLifeCycleImpl = new ComponentLifeCycleImpl();
        System.out.println("getLifeCycle() has been called");
        return componentLifeCycleImpl;
    }

    /**
     * 
     * @return
     */
    @Override
    public ServiceUnitManager getServiceUnitManager()
    {
        /**
         * At this place the Component implementation should return back an
         * instance of an implementation of the ServiceUnitManager interface
         * that will be used for the life cycle management of the about to be
         * deployed service unit artifacts.
         */        
        ServiceUnitManager serviceUnitManager = new ServiceUnitManagerImpl();
        System.out.println("ServiceUnitManager has been called...");
        return serviceUnitManager;
    }

    /**
     * 
     * @param serviceEndpoint
     * @return
     */
    @Override
    public Document getServiceDescription(ServiceEndpoint serviceEndpoint)
    {
        /**
         * When people will query the JBI environment regarding the
         * service units that are provided by this component, then
         * the JBI environment will query this method for the WSDL service
         * description of the given ServiceEndpoint. At the moment, we don't need
         * to provide anything, as we don't have any services provided.
         */
        return null;
    }

    /**
     * 
     * @param serviceEndpoint
     * @param messageExchange
     * @return
     */
    @Override
    public boolean isExchangeWithConsumerOkay(ServiceEndpoint serviceEndpoint, MessageExchange messageExchange)
    {
        return true;
    }

    /**
     * 
     * @param serviceEndpoint
     * @param messageExchange
     * @return
     */
    @Override
    public boolean isExchangeWithProviderOkay(ServiceEndpoint serviceEndpoint, MessageExchange messageExchange)
    {
        return true;
    }

    /**
     * 
     * @param epr
     * @return
     */
    @Override
    public ServiceEndpoint resolveEndpointReference(DocumentFragment epr)
    {
        return null;
    }
}
