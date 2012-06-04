/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.orange.uklab.b2bbc.runtime.contexts;

import EDU.oswego.cs.dl.util.concurrent.PooledExecutor;
import com.orange.uklab.b2bbc.descriptor.AbstractServiceEndpoint;
import com.orange.uklab.b2bbc.descriptor.ServiceUnit;
import com.orange.uklab.b2bbc.descriptor.impl.ServiceEndpointImpl;
import com.orange.uklab.b2bbc.descriptor.impl.ServiceUnitImpl;
import com.orange.uklab.b2bbc.runtime.contexts.impl.ServiceUnitStore;
import com.orange.uklab.b2bbc.runtime.contexts.impl.StorageException;
import com.orange.uklab.b2bbc.sip.B2BDialog;
import com.orange.uklab.b2bbc.sip.B2BSession;
import com.orange.uklab.b2bbc.sip.impl.B2BDialogImpl;
import com.orange.uklab.b2bbc.sip.impl.B2BSessionImpl;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.MissingResourceException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.jbi.JBIException;
import javax.jbi.component.ComponentContext;
import javax.jbi.messaging.DeliveryChannel;
import javax.jbi.messaging.MessagingException;
import javax.jbi.servicedesc.ServiceEndpoint;
import javax.sip.Dialog;
import javax.sip.PeerUnavailableException;
import javax.sip.SipFactory;
import javax.sip.SipListener;
import javax.sip.SipProvider;
import javax.sip.SipStack;
import javax.sip.address.AddressFactory;
import javax.sip.header.HeaderFactory;
import javax.sip.message.MessageFactory;
import javax.xml.namespace.QName;

/**
 *
 * @author hasanein
 */
public final class RuntimeComponentContext implements ServiceUnitStore
{
    private static RuntimeComponentContext instanceHolder;
    private ComponentContext componentContext = null;
    private HashMap<String, ArrayList> serviceUnitsRepository = new HashMap<String, ArrayList>();
    private HashMap<String, SipProvider> sipProvidersDb = new HashMap<String, SipProvider>();
    private HashMap<String, SipListener> sipListenersDb = new HashMap<String, SipListener>();
    private HashMap<String, PooledExecutor> pooledExecutorsDb = new HashMap<String, PooledExecutor>();
    private SipFactory sipFactory = null;
    private AddressFactory addressFactory = null;
    private HeaderFactory headerFactory = null;
    private MessageFactory messageFactory = null;
    private SipStack sipStack = null;
    private HashMap<String, HashMap<String, B2BSession>> b2bSessionRepository = new HashMap<String, HashMap<String, B2BSession>>();
    /**
     *
     */
    public static String serverTransactionID = null;

    /**
     * Here we are making the constructor private in a way to hide it so that
     * other code can't use the default constructor to create a new instance of
     * the object. New instances should only be obtained via the getInstance()
     * method.
     */
    private RuntimeComponentContext()
    {
    }

    /**
     *
     * @return
     */
    public synchronized SipStack getSipStack()
    {
        if (this.sipStack != null)
        {
            return this.sipStack;
        }
        else
        {
            throw new IllegalStateException("SipStack is not set yet.");
        }
    }

    /**
     *
     * @param sipStack
     */
    public synchronized void setSipStack(SipStack sipStack)
    {
        if (this.sipStack == null)
        {
            this.sipStack = sipStack;
        }
        else
        {
            throw new IllegalStateException("SipStack is already set.");
        }
    }
    
    /**
     *
     * @param serviceUnitName
     * @param sipProvider
     */
    public synchronized void addSipProvider(String serviceUnitName, SipProvider sipProvider)
    {
        sipProvidersDb.put(serviceUnitName, sipProvider);
    }

    /**
     *
     * @param serviceUnitName
     * @return
     */
    public synchronized SipProvider getSipProvider(String serviceUnitName)
    {
        return sipProvidersDb.get(serviceUnitName);
    }

    /**
     *
     * @param serviceUnitName
     */
    public synchronized void removeSipProvider(String serviceUnitName)
    {
            sipProvidersDb.remove(serviceUnitName);
    }

    /**
     *
     * @param serviceUnitName
     * @param sipListener
     */
    public synchronized void addSipListener(String serviceUnitName, SipListener sipListener)
    {
        sipListenersDb.put(serviceUnitName, sipListener);
    }

    /**
     *
     * @param serviceUnitName
     * @return
     */
    public synchronized SipListener getSipListener(String serviceUnitName)
    {
        return sipListenersDb.get(serviceUnitName);
    }

    /**
     *
     * @param serviceUnitName
     */
    public synchronized void removeSipListener(String serviceUnitName)
    {
            sipListenersDb.remove(serviceUnitName);
    }
    /**
     *
     * @param serviceUnitName
     * @param pooledExecutor
     */
    public synchronized void addPooledExecutor(String serviceUnitName, PooledExecutor pooledExecutor)
    {
        pooledExecutorsDb.put(serviceUnitName, pooledExecutor);
    }

    /**
     *
     * @param serviceUnitName
     * @return
     */
    public synchronized PooledExecutor getPooledExecutor(String serviceUnitName)
    {
        return pooledExecutorsDb.get(serviceUnitName);
    }

    /**
     *
     * @param serviceUnitName
     */
    public synchronized void removePooledExecutor(String serviceUnitName)
    {
            pooledExecutorsDb.remove(serviceUnitName);
    }

    /**
     *
     * @return
     */
    public synchronized String getComponentName()
    {
        if (this.componentContext != null)
        {
            return this.componentContext.getComponentName();
        }
        else
        {
            throw new IllegalStateException("Component Context is not set yet.");
        }
    }

    /**
     *
     * @return
     */
    public synchronized DeliveryChannel getDeliveryChannel()
    {
        if (this.componentContext != null)
        {
            try
            {
                return this.componentContext.getDeliveryChannel();
            }
            catch (MessagingException ex)
            {
                Logger.getLogger(RuntimeComponentContext.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        else
        {
            throw new IllegalStateException("Component Context is not set yet.");
        }
        return null;
    }

    /**
     *
     * @return
     */
    public synchronized ComponentContext getComponentContext()
    {
        if(this.componentContext != null)
        {
            return this.componentContext;
        }
        else
        {
            throw new IllegalStateException("Component Context is not set yet.");
        }
    }

    /**
     *
     * @param componentContext
     */
    public synchronized void setComponentContext(ComponentContext componentContext)
    {
        if (this.componentContext == null)
        {
            this.componentContext = componentContext;
        }
        else
        {
            throw new IllegalStateException("Component Context is already set.");
        }
    }

    /**
     *
     * @param suffix
     * @param resourceBundleName
     * @return
     */
    public synchronized Logger getLogger(String suffix, String resourceBundleName)
    {
        if (this.componentContext != null)
        {
            try
            {
                Logger retLogger = this.componentContext.getLogger(suffix, resourceBundleName);
                
                if(getProperty("com.orange.uklab.b2bbc.LOG_LEVEL") != null)
                {
                    if(getProperty("com.orange.uklab.b2bbc.LOG_LEVEL").equals("FINEST"))
                    {
                        retLogger.setLevel(Level.FINEST);
                    }
                    else if(getProperty("com.orange.uklab.b2bbc.LOG_LEVEL").equals("FINER"))
                    {
                        retLogger.setLevel(Level.FINE);
                    }
                    else if(getProperty("com.orange.uklab.b2bbc.LOG_LEVEL").equals("FINE"))
                    {
                        retLogger.setLevel(Level.FINE);
                    }
                    else
                    {
                        retLogger.setLevel(Level.INFO);
                    }
                }

                return retLogger;
            }
            catch (MissingResourceException ex)
            {
                Logger.getLogger(RuntimeComponentContext.class.getName()).log(Level.SEVERE, null, ex);
            }
            catch (JBIException ex)
            {
                Logger.getLogger(RuntimeComponentContext.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        else
        {
            throw new IllegalStateException("Component Context is not set yet.");
        }
        return null;//This is not reachable
    }

    /**
     *
     * @return
     */
    public synchronized String getComponentInstallRoot()
    {
        return this.componentContext.getInstallRoot();
    }

    /**
     *
     * @return
     */
    public synchronized static RuntimeComponentContext getInstance()
    {
        if (instanceHolder == null)
        {
            synchronized (RuntimeComponentContext.class)
            {
                instanceHolder = new RuntimeComponentContext();
                return instanceHolder;
            }
        }
        else
        {
            return instanceHolder;
        }
    }

    /**
     *
     * @param serviceUnit
     * @return
     * @throws StorageException
     */
    @Override
    public synchronized boolean storeServiceUnit(ServiceUnit serviceUnit) throws StorageException
    {
        if(serviceUnit != null)
        {
            if (!this.serviceUnitsRepository.containsKey(serviceUnit.getName()))
            {
                ArrayList serviceUnitArrayList = new ArrayList();
                serviceUnitArrayList.add(serviceUnit);
                serviceUnitArrayList.add(ServiceUnitStore.SERVICE_UNIT_STATUS_SHUTDOWN);
                serviceUnitsRepository.put(serviceUnit.getName(), serviceUnitArrayList);
                return true;
            }
            else
            {
                throw new StorageException("Service Unit already exist: " + serviceUnit);
            }
        }
        else
        {
            return false;
        }
    }
    
    

    /**
     *
     * @param serviceUnitName
     * @return
     * @throws StorageException
     */
    @Override
    public synchronized boolean removeServiceUnit(String serviceUnitName) throws StorageException
    {        
        if(serviceUnitsRepository.containsKey(serviceUnitName))
        {
            serviceUnitsRepository.remove(serviceUnitName);
            return true;
        }
        else
        {
            throw new StorageException("Service Unit does not exist: " + serviceUnitName);
        }
    }

    /**
     *
     * @param serviceUnitName
     * @return
     */
    @Override
    public synchronized ServiceUnit getServiceUnit(String serviceUnitName)
    {
        if (serviceUnitsRepository.containsKey(serviceUnitName))
        {
            return (ServiceUnit)serviceUnitsRepository.get(serviceUnitName).get(0);
        }
        else
        {
            return null;
        }
    }

    /**
     *
     * @param serviceUnitName
     * @return
     */
    @Override
    public synchronized String getServiceUnitStatus(String serviceUnitName)
    {
        if (serviceUnitsRepository.containsKey(serviceUnitName))
        {
            return (String)serviceUnitsRepository.get(serviceUnitName).get(1);
        }
        else
        {
            return null;
        }
    }

    /**
     *
     * @param serviceUnitName
     * @param status
     * @throws IllegalArgumentException
     * @throws StorageException
     */
    @Override
    public synchronized void setServiceUnitStatus(String serviceUnitName, String status) throws IllegalArgumentException, StorageException
    {
        if (status.equals(ServiceUnitStore.SERVICE_UNIT_STATUS_SHUTDOWN) || status.equals(ServiceUnitStore.SERVICE_UNIT_STATUS_STARTED) || status.equals(ServiceUnitStore.SERVICE_UNIT_STATUS_STOPPED))
        {
            if (serviceUnitsRepository.containsKey(serviceUnitName))
            {
                serviceUnitsRepository.get(serviceUnitName).set(1, status);
            }
            else
            {
                throw new StorageException("Service Unit does not exist: " + serviceUnitName);
            }
        }
        else
        {
            throw  new IllegalArgumentException("Unrecognized Service Unit Status: " + status);
        }
    }

    /**
     *
     * @param endpointName
     * @return
     */
    @Override
    public synchronized ServiceEndpoint getServiceEndpoint(String endpointName)
    {
        ServiceEndpoint[] serviceEndpoints = getAllDefinedEndpoints();
        ServiceEndpoint serviceEndpoint = null;
        for(ServiceEndpoint s : serviceEndpoints)
        {
            if (s.getEndpointName().equals(endpointName))
            {
                serviceEndpoint = s;
            }
        }
        return serviceEndpoint;
    }
    
    /**
     *
     * @return
     */
    @Override
    public synchronized ServiceEndpoint[] getAllDefinedEndpoints()
    {
        ArrayList arrayOfServiceEndpointsArrays = new ArrayList();
        ArrayList arrayOfServiceEndpoint = new ArrayList();
        Iterator iterator = serviceUnitsRepository.entrySet().iterator();
        while(iterator.hasNext())
        {
            Map.Entry hashMapEntry = (Map.Entry)iterator.next();
            ArrayList al = (ArrayList)hashMapEntry.getValue();
            ServiceUnit serviceUnit = (ServiceUnit)al.get(0);
            arrayOfServiceEndpointsArrays.add(serviceUnit.getServiceEndpoints());
        }
        for (int i = 0 ; i < arrayOfServiceEndpointsArrays.size() ; i++)
        {
            ServiceEndpoint[] s = (ServiceEndpoint[])arrayOfServiceEndpointsArrays.get(i);
            for(ServiceEndpoint s1 : s)
            {
                arrayOfServiceEndpoint.add(s1);
            }
        }
        ServiceEndpoint[] serviceEndpoints = new ServiceEndpointImpl[arrayOfServiceEndpoint.size()];
        serviceEndpoints = (ServiceEndpoint[])arrayOfServiceEndpoint.toArray(serviceEndpoints);
        return serviceEndpoints;
    }

    /**
     *
     * @param serviceUnitName
     * @return
     */
    @Override
    public synchronized ServiceEndpoint[] getServiceUnitEndpoints(String serviceUnitName)
    {
        if (serviceUnitsRepository.containsKey(serviceUnitName))
        {
            ServiceUnitImpl serviceUnit = (ServiceUnitImpl)serviceUnitsRepository.get(serviceUnitName).get(0);
            return serviceUnit.getServiceEndpoints();
        }
        else
        {
            return null;
        }        
    }

    /**
     *
     * @return
     */
    @Override
    public synchronized ServiceUnit[] getAllServiceUnits()
    {
        Iterator iterator = serviceUnitsRepository.entrySet().iterator();
        ArrayList arrayList = new ArrayList();
        while(iterator.hasNext())
        {
            Map.Entry hashMapEntry = (Map.Entry)iterator.next();
            ArrayList al = (ArrayList)hashMapEntry.getValue();
            ServiceUnit serviceUnit = (ServiceUnit)al.get(0);
            arrayList.add(serviceUnit);
        }
        ServiceUnitImpl serviceUnit[] = new ServiceUnitImpl[arrayList.size()];
        serviceUnit = (ServiceUnitImpl[])arrayList.toArray(serviceUnit);
        return serviceUnit;
    }

    /**
     *
     * @param serviceUnits
     * @throws StorageException
     */
    @Override
    public void storeServiceUnits(ServiceUnit[] serviceUnits) throws StorageException
    {        
        if(serviceUnits != null)
        {
            for(ServiceUnit serviceUnit : serviceUnits)
            {
                storeServiceUnit(serviceUnit);
            }
        }
    }

    /**
     *
     */
    @Override
    public synchronized void clear()
    {
        instanceHolder = null;
    }

    /**
     *
     * @return
     */
    public synchronized SipFactory getSipFactory()
    {
        if (this.sipFactory != null)
        {
            return this.sipFactory;
        }
        else
        {
            throw new IllegalStateException("SipFactory is not set yet.");
        }
    }

    /**
     *
     * @param sipFactory
     */
    public synchronized void setSipFactory(SipFactory sipFactory)
    {
        if (this.sipFactory == null)
        {
            this.sipFactory = sipFactory;
        }
        else
        {
            throw new IllegalStateException("SipFactory is already set.");
        }
    }

    /**
     *
     * @param b2BSession
     */
    public synchronized void addB2BSession(B2BSession b2BSession)
    {
        HashMap<String, B2BSession> hashMap = new HashMap<String, B2BSession>();
        hashMap.put(b2BSession.getB2BSessionId(), b2BSession);
        b2bSessionRepository.put(b2BSession.getOwnerServiceUnitName(), hashMap);
    }

    /**
     *
     * @param b2bSessionId
     * @return
     */
    public synchronized boolean removeB2bSession(String b2bSessionId)
    {
        Iterator iterator = b2bSessionRepository.entrySet().iterator();
        while(iterator.hasNext())
        {
            Map.Entry entryPair = (Map.Entry)iterator.next();
            /**
             * HashMap for each service unit
             */
            HashMap hashMap = (HashMap)entryPair.getValue();
            
            Iterator iterator2 = hashMap.entrySet().iterator();
            while(iterator2.hasNext())
            {
                Map.Entry pair = (Map.Entry) iterator2.next();
                String id = (String)pair.getKey();
                if(id.equals(b2bSessionId))
                {
                    hashMap.remove(id);
                    break;
                }
                else
                {
                    continue;
                }
            }
        }
        return false;
    }

    /**
     *
     * @param port
     * @return
     */
    public synchronized ServiceUnit getServiceUnit(int port)
    {
        ServiceUnitImpl[] serviceUnits = (ServiceUnitImpl[]) getAllServiceUnits();
        for(ServiceUnitImpl serviceUnit : serviceUnits)
        {
            if(serviceUnit.getBindingPort() == port)
            {
                return serviceUnit;
            }
        }
        return null;
    }

    /**
     *
     * @param serviceUnitName
     * @return
     */
    public synchronized B2BSession[] getB2BSessions(String serviceUnitName)
    {
        ArrayList b2bSessionsArrayList = new ArrayList();
        Iterator iterator01 = b2bSessionRepository.entrySet().iterator();
        System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ looking for " + serviceUnitName);
        while(iterator01.hasNext())
        {
            Map.Entry keyValuePair = (Entry) iterator01.next();
            String suName = (String) keyValuePair.getKey();
            System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ comparing with " + suName);
            if(suName.equals(serviceUnitName))
            {
                HashMap serviceUnitB2BSessionsHashMap = (HashMap) keyValuePair.getValue();
                Iterator iterator02 = serviceUnitB2BSessionsHashMap.entrySet().iterator();
                while(iterator02.hasNext())
                {
                    Map.Entry b2bSessionMapEntry = (Entry) iterator02.next();
                    b2bSessionsArrayList.add(b2bSessionMapEntry.getValue());
                }
                break;
            }
        }
        B2BSessionImpl[] b2BSessions = new B2BSessionImpl[b2bSessionsArrayList.size()];        
        b2BSessions = (B2BSessionImpl[]) b2bSessionsArrayList.toArray(b2BSessions);        
        return b2BSessions;
    }

    public synchronized Dialog getSipDialog(String dialogId)
    {
        /**
         * This method will rectrieve a SIP Dialog from the existing
         * SIP sessions basing on the provided dialogId argument, if no SIP
         * Dialog is found then a null is returned instead. note that this is
         * the underlying SIP dialog and not the B2B dialog, whereby the B2BDialog
         * is an extension of the native SIP Dialog.
         */
         Dialog sipDialog = null;
         B2BDialogImpl b2BDialog = null;
         B2BSession[] b2BSessions = getAllB2BSessions();
         for(B2BSession b2BSession : b2BSessions)
         {
            b2BDialog = (B2BDialogImpl) b2BSession.getB2BDialog(dialogId, true, null);
            if(b2BDialog != null)
            {
                sipDialog = b2BDialog.getUnderlyingSipDialog();
                return sipDialog;
            }
         }
        
         return sipDialog;
    }

    public synchronized B2BSession[] getAllB2BSessions()
    {
        ArrayList b2bSessionsArrayList = new ArrayList();
        Iterator iterator01 = b2bSessionRepository.entrySet().iterator();
        while(iterator01.hasNext())
        {
            Map.Entry keyValuePair = (Entry) iterator01.next();
            HashMap serviceUnitB2BSessionsHashMap = (HashMap) keyValuePair.getValue();
            Iterator iterator02 = serviceUnitB2BSessionsHashMap.entrySet().iterator();
            while(iterator02.hasNext())
            {
                Map.Entry b2bSessionMapEntry = (Entry) iterator02.next();
                b2bSessionsArrayList.add(b2bSessionMapEntry.getValue());
            }
        }
        B2BSessionImpl[] b2BSessions = new B2BSessionImpl[b2bSessionsArrayList.size()];
        b2BSessions = (B2BSessionImpl[]) b2bSessionsArrayList.toArray(b2BSessions);
        return b2BSessions;
    }

    /**
     *
     * @param propertyName
     * @return
     */
    public static String getProperty(String propertyName)
    {
        try
        {
            String configFilepath = RuntimeComponentContext.getInstance().getComponentInstallRoot() + "\\META-INF\\config.properties";
            Properties configProperties = new Properties();
            configProperties.load(new FileInputStream(configFilepath));
            return configProperties.getProperty(propertyName);
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        return null;
    }

    /**
     *
     * @return
     */
    public AddressFactory getAddressFactory()
    {
        if (this.sipFactory != null)
        {
            if(addressFactory != null)
            {
                return addressFactory;
            }
            else
            {
                initAddressFactory();
                return addressFactory;
            }
        }
        else
        {
            throw new IllegalStateException("SipFactory is not set yet, can't initialize the AddressFactory");
        }        
    }

    private AddressFactory initAddressFactory()
    {
        try
        {
            addressFactory = sipFactory.createAddressFactory();
            return addressFactory;
        }
        catch (PeerUnavailableException ex)
        {
            getLogger(this.getClass().getName(), null).severe(ex.getMessage());
            ex.printStackTrace();
        }
        return addressFactory;
    }

    /**
     *
     * @return
     */
    public MessageFactory getMessageFactory()
    {
        if (this.sipFactory != null)
        {
            if(addressFactory != null)
            {
                return messageFactory;
            }
            else
            {
                initMessageFactory();
                return messageFactory;
            }
        }
        else
        {
            throw new IllegalStateException("SipFactory is not set yet, can't initialize the MessageFactory");
        }
    }

    private MessageFactory initMessageFactory()
    {
        try
        {
            messageFactory = sipFactory.createMessageFactory();
            return messageFactory;
        }
        catch (PeerUnavailableException ex)
        {
            getLogger(this.getClass().getName(), null).severe(ex.getMessage());
            ex.printStackTrace();
        }
        return messageFactory;
    }

    /**
     *
     * @return
     */
    public HeaderFactory getHeaderFactory()
    {
        if (this.sipFactory != null)
        {
            if(headerFactory != null)
            {
                return headerFactory;
            }
            else
            {
                initHeaderFactory();
                return headerFactory;
            }
        }
        else
        {
            throw new IllegalStateException("SipFactory is not set yet, can't initialize the HeaderFactory");
        }
    }

    private HeaderFactory initHeaderFactory()
    {
        try
        {
            headerFactory = sipFactory.createHeaderFactory();
            return headerFactory;
        }
        catch (PeerUnavailableException ex)
        {
            getLogger(this.getClass().getName(), null).severe(ex.getMessage());
            ex.printStackTrace();
        }
        return headerFactory;
    }

    /**
     *
     * @param serviceName
     * @param endpointName
     * @return
     */
    public synchronized ServiceUnit getServiceUnit(QName serviceName, String endpointName)
    {
        ServiceUnit serviceUnit = null;
        ServiceEndpoint[] serviceEndpoints = getAllDefinedEndpoints();
        for(ServiceEndpoint s : serviceEndpoints)
        {
            ServiceEndpointImpl serviceEndpoint = (ServiceEndpointImpl) s;
            if((serviceEndpoint.getEndpointType().equals(AbstractServiceEndpoint.ENDPOINT_TYPE_PROVIDE)) && (serviceEndpoint.getEndpointName().equals(endpointName)) && (serviceEndpoint.getServiceName().getLocalPart().equals(serviceName.getLocalPart())) && (serviceEndpoint.getServiceName().getNamespaceURI().equals(serviceName.getNamespaceURI())))
            {
                serviceUnit = getServiceUnit(serviceEndpoint.getServiceUnitName());
                return serviceUnit;
            }
        }
        return serviceUnit;
    }

    public B2BSession getB2BSession(ServiceUnit ownerServiceUnit, String partialDialogID, String fromTag)
    {
        B2BSession[] b2BSessions = getAllB2BSessions();
        for(B2BSession b2BSession : b2BSessions)
        {
            B2BSessionImpl b2BSessionImpl = (B2BSessionImpl) b2BSession;
            if(b2BSessionImpl.getOwnerServiceUnitName().equals(ownerServiceUnit.getName()))
            {
                /**
                 * If the B2BSession belongs to the given ServiceUnit then
                 * it need to be searched for a Dialog with the given
                 * partial Dialog ID.
                 */
                if((b2BSessionImpl.getB2BDialog(partialDialogID, false, fromTag)) != null)
                {
                    return b2BSessionImpl;
                }
            }
        }
        return null;
    }
}