///*
// * To change this template, choose Tools | Templates
// * and open the template in the editor.
// */
//
//package com.orange.uklab.b2bbc.testing;
//
//import com.orange.uklab.b2bbc.descriptor.ServiceUnit;
//import com.orange.uklab.b2bbc.descriptor.impl.ServiceUnitImpl;
//import com.orange.uklab.b2bbc.persistence.DataPersister;
//import com.orange.uklab.b2bbc.persistence.Persistable;
//import com.orange.uklab.b2bbc.persistence.PersistenceException;
//import com.orange.uklab.b2bbc.persistence.impl.DataPersisterImpl;
//import com.orange.uklab.b2bbc.runtime.contexts.RuntimeComponentContext;
//import java.util.HashMap;
//import java.util.logging.Logger;
//
///**
// *
// * @author lhhm2777
// */
//public class DataPersistenceTester
//{
//    public void persistObjectTest() throws PersistenceException
//    {
//        RuntimeComponentContext runtimeComponentContext = RuntimeComponentContext.getInstance();
//        Logger logger = runtimeComponentContext.getLogger(this.getClass().getName(), null);
//        logger.info("**************************************************************************");
//        DataPersister dataPersister = new DataPersisterImpl();
//        dataPersister.createPersistenceCategory("Test_Persistence_Cat");
//        logger.info("Persistence Category created");
//        ServiceUnit serviceUnit = new ServiceUnitImpl(null, null, null, null, null, true);
//        logger.info("Service Unit Created with name: " + serviceUnit.getName());
//        dataPersister.persistObject((Persistable)serviceUnit, "Test_Persistence_Cat", true);
//        logger.info("**************************************************************************");
//    }
//
//    public void unpersistObjectTest() throws Exception
//    {
//        RuntimeComponentContext runtimeComponentContext = RuntimeComponentContext.getInstance();
//        Logger logger = runtimeComponentContext.getLogger(this.getClass().getName(), null);
//        logger.info("**************************************************************************");
//        DataPersister dataPersister = new DataPersisterImpl();
//        boolean result = dataPersister.unpersistObject("MockServiceUnit", "Test_Persistence_Cat");
//        if(result)
//        {
//            logger.info("Object Unpersisted");
//        }
//        else
//        {
//            logger.warning("Can't Unpersist Object");
//        }
//        logger.info("**************************************************************************");
//    }
//
//    public void persistObjectsTest() throws Exception
//    {
//        RuntimeComponentContext runtimeComponentContext = RuntimeComponentContext.getInstance();
//        Logger logger = runtimeComponentContext.getLogger(this.getClass().getName(), null);
//        logger.info("**************************************************************************");
//        DataPersister dataPersister = new DataPersisterImpl();
//        ServiceUnit serviceUnit1 = new ServiceUnitImpl(null, null, null, null, null, true);
//        ServiceUnit serviceUnit2 = new ServiceUnitImpl(null, null, null, null, null, true);
//        HashMap<Object, String> hashMap = new HashMap<Object, String>();
//        dataPersister.createPersistenceCategory("ServiceUnit1");
//        dataPersister.createPersistenceCategory("ServiceUnit2");
//        hashMap.put(serviceUnit1, "ServiceUnit1");
//        hashMap.put(serviceUnit2, "ServiceUnit2");
//        boolean result = dataPersister.persistObjects(hashMap, true);
//        if(result)
//        {
//            logger.info("Objects Persisted");
//        }
//        else
//        {
//            logger.warning("Can't Persist Objects");
//        }
//        logger.info("**************************************************************************");
//    }
//
//    public void unpersistObjectsTest() throws Exception
//    {
//        RuntimeComponentContext runtimeComponentContext = RuntimeComponentContext.getInstance();
//        Logger logger = runtimeComponentContext.getLogger(this.getClass().getName(), null);
//        logger.info("**************************************************************************");
//        DataPersister dataPersister = new DataPersisterImpl();
//        HashMap<String, String> hashMap = new HashMap<String, String>();
//        hashMap.put("MockServiceUnit", "ServiceUnit1");
//        hashMap.put("MockServiceUnit2", "ServiceUnit2");
//        boolean result = dataPersister.unpersistObjects(hashMap);
//        if(result)
//        {
//            logger.info("Objects UnPersisted");
//        }
//        else
//        {
//            logger.warning("Can't UnPersist Objects");
//        }
//        logger.info("**************************************************************************");
//    }
//
//    public void retrieveObjectTest() throws Exception
//    {
//        RuntimeComponentContext runtimeComponentContext = RuntimeComponentContext.getInstance();
//        Logger logger = runtimeComponentContext.getLogger(this.getClass().getName(), null);
//        logger.info("**************************************************************************");
//        DataPersister dataPersister = new DataPersisterImpl();
//        Persistable<ServiceUnit> serviceUnit =  dataPersister.retrieveObject("MockServiceUnit", "ServiceUnit1");
//        ServiceUnit s = (ServiceUnit) serviceUnit;
//        logger.info("ServiceUnit retrieved: " + s.getName());
//    }
//
//    public void retrieveObjectsTest() throws Exception
//    {
//        RuntimeComponentContext runtimeComponentContext = RuntimeComponentContext.getInstance();
//        Logger logger = runtimeComponentContext.getLogger(this.getClass().getName(), null);
//        logger.info("**************************************************************************");
//        DataPersister dataPersister = new DataPersisterImpl();
//        HashMap<String, String> hashMap = new HashMap<String, String>();
//        hashMap.put("MockServiceUnit", "ServiceUnit1");
//        hashMap.put("MockServiceUnit2", "ServiceUnit2");
//        Persistable<ServiceUnit> serviceUnit[] =  dataPersister.retrieveObjects(hashMap);
//        for(int i = 0 ; i < serviceUnit.length ; i++)
//        {
//            logger.info("Retrieved ServiceUnit: " + serviceUnit[i].getObjectId());
//        }
//        logger.info("**************************************************************************");
//    }
//
//    public void purgeCategoryTest()
//    {
//        RuntimeComponentContext runtimeComponentContext = RuntimeComponentContext.getInstance();
//        Logger logger = runtimeComponentContext.getLogger(this.getClass().getName(), null);
//        logger.info("**************************************************************************");
//        DataPersister dataPersister = new DataPersisterImpl();
//        if(dataPersister.purgeCategory("Test_Persistence_Cat"))
//        {
//            logger.info("Category Purged...");
//        }
//        else
//        {
//            logger.info("Unable to purge category");
//        }
//        logger.info("**************************************************************************");
//    }
//
//    public void retrieveAllObjectsTest() throws Exception
//    {
//        persistObjectTest();
//    }
//}
