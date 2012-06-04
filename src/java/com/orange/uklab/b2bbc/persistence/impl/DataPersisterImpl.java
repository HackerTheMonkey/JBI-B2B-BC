/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.orange.uklab.b2bbc.persistence.impl;

import com.orange.uklab.b2bbc.descriptor.impl.ServiceUnitImpl;
import com.orange.uklab.b2bbc.persistence.DataPersister;
import com.orange.uklab.b2bbc.persistence.Persistable;
import com.orange.uklab.b2bbc.persistence.PersistenceException;
import com.orange.uklab.b2bbc.runtime.contexts.RuntimeComponentContext;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author hasanein
 */
public class DataPersisterImpl implements DataPersister
{
    private Logger logger;
    private RuntimeComponentContext runtimeComponentContext;

    public DataPersisterImpl()
    {
        runtimeComponentContext = RuntimeComponentContext.getInstance();
        logger = runtimeComponentContext.getLogger(this.getClass().getName(), null);
    }
    /** This method involves the creation of the category file structure for
     *  the persisted objects to be stored in there. The exact place where the
     *  category directoty structure to be stored is inside the component
     *  installation root directory. The component installation root directory
     *  can be obtained from the RuntimeComponentContext.
     * @param categoryName
     * @throws PersistenceException
     */
    @Override
    public void createPersistenceCategory(String categoryName) throws PersistenceException
    {
        /**
         * Get the Component Installation Root Directory from the Runtime
         * ComponentContext. In case that the category already exist, then
         * shall throw an Excetion with an appropriate error message.
         */
        runtimeComponentContext = RuntimeComponentContext.getInstance();
        String componentInstallRoot = runtimeComponentContext.getComponentInstallRoot();
        /**
         * Here we need to make sure that the directory structure for the whole
         * persistance storage is already exist. If not it will be created.
         */
        File installRootDir = new File(componentInstallRoot + "\\" + "persistence");
        boolean isPersistenceDirAlreadyExist = !(installRootDir.mkdir());
        if(isPersistenceDirAlreadyExist)
        {
            logger.info("Persistence Directory is already exist...");
        }
        else
        {
            logger.info("Creating the persistence directory...");
        }
        /**
         * Now we need to create the category directory requested, if it is
         * already exist then an exception is to be thrown with the appropriate
         * error message.
         */
        String categoryDirPath = componentInstallRoot + "\\persistence\\" + categoryName;
        File categoryDir = new File(categoryDirPath);
        boolean isCategoryDirAlreadyExist = !(categoryDir.mkdir());
        if(isCategoryDirAlreadyExist)
        {
            throw new PersistenceException("Category Directory is already exist in the component persistence store: " + categoryName);            
        }
        else
        {
            logger.info("Category directory has been created in the persistence store: " + categoryDirPath);
        }
    }

    /**
     * This method will check whether the given category name exist in the
     * persistence storage as a directory, if it does not exist then an exception
     * is to be thrown.
     * @param categoryName
     */
//    private void checkCategoryExistance(String categoryName) throws PersistenceException
//    {
//
//    }

    public boolean isCategoryExist(String categoryName)
    {
        String installRootDir = runtimeComponentContext.getComponentInstallRoot();
        String categoryDirPath = installRootDir + "\\persistence\\" + categoryName;
        File categoryDir = new File(categoryDirPath);
        return categoryDir.exists();
    }

    private boolean isObjectPersisted(String categoryName, String objectId)
    {
        String installRootDir = runtimeComponentContext.getComponentInstallRoot();
        String categoryDirPath = installRootDir + "\\persistence\\" + categoryName;
        String filename = categoryDirPath + "\\" + objectId;
        File objectFilename = new File(filename);
        return objectFilename.exists();
    }

    @Override
    public boolean persistObject(Object object, String categoryName, boolean overwrite) throws PersistenceException
    {
        String installRootDir = runtimeComponentContext.getComponentInstallRoot();
        String categoryDirPath = installRootDir + "\\persistence\\" + categoryName;
        String filename = categoryDirPath + "\\" + ((Persistable)object).getObjectId();
        /**
         * First thing to do is to make sure that the category name exist, if
         * the category does not exist then an Exception must be thrown to with
         * an appropriate error message.
         */
        if(isCategoryExist(categoryName))
        {
            FileOutputStream fileOutputStream;
            ObjectOutputStream objectOutputStream;
            
            if(overwrite)
            {
                try
                {
                    /**
                     * Overwrite the already persisted object
                     */
                    fileOutputStream = new FileOutputStream(filename);
                    objectOutputStream = new ObjectOutputStream(fileOutputStream);
                    
                    System.out.println(object.getClass().getName());
                    for(Class c : object.getClass().getInterfaces())
                    {
                        System.out.println(c.getName());
                    }

                    ServiceUnitImpl serviceUnitImpl = (ServiceUnitImpl)object;
//                    logger.severe(serviceUnitImpl.getName());
//                    logger.severe(serviceUnitImpl.getObjectId());
//                    logger.severe(serviceUnitImpl.getStatus());
//                    logger.severe(serviceUnitImpl.getNamepace() + "");
//                    logger.severe(serviceUnitImpl.isBindingComponent() + "");
//                    logger.severe(serviceUnitImpl.getServiceEndpoints().toString());
//                    logger.severe(serviceUnitImpl.getServiceEndpoints().length + ": LENGTH");
//                    logger.severe("endpoint name" + serviceUnitImpl.getServiceEndpoints()[0].getEndpointName());                    

                    objectOutputStream.writeObject(object);
                    objectOutputStream.close();
                    return true;

                }
                catch (Exception ex)
                {
                    Logger.getLogger(DataPersisterImpl.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            else
            {
                if(isObjectPersisted(categoryName, ((Persistable)object).getObjectId()))
                {
                    return false;
                }
                else
                {
                    /**
                     * As the passed-in object hasn't been presisted before,
                     * then we can persist it normally.
                     */
                    try
                    {
                        fileOutputStream = new FileOutputStream(filename);
                        objectOutputStream = new ObjectOutputStream(fileOutputStream);
                        objectOutputStream.writeObject(object);
                        objectOutputStream.close();
                    }
                    catch(Exception ex)
                    {
                        logger.severe(ex.getMessage());
                        ex.printStackTrace();
                    }
                    return true;
                }
            }            
        }
        else
        {
            throw new PersistenceException("Category specificed does not exist, can't persist the given object: Category=" + categoryName + ", PersistableObject_ID=" + ((Persistable)object).getObjectId());
        }
        return false;
    }

    @Override
    public boolean persistObjects(HashMap<Object, String> objects, boolean overwrite) throws PersistenceException
    {
        Iterator iterator = objects.entrySet().iterator();
        boolean retValue = false;
        while(iterator.hasNext())
        {
            Map.Entry hashMapEntry = (Map.Entry)iterator.next();
            boolean result = persistObject(hashMapEntry.getKey(), (String)hashMapEntry.getValue(), overwrite);
            retValue = (result == true) ? true : false;
        }
        return retValue;
    }

    @Override
    public boolean unpersistObject(String objectId, String categoryName) throws PersistenceException
    {
        String installRootDir = runtimeComponentContext.getComponentInstallRoot();
        String categoryDirPath = installRootDir + "\\persistence\\" + categoryName;
        String filename = categoryDirPath + "\\" + objectId;

        if(isCategoryExist(categoryName))
        {            
            /**
             * If the object is persisted then unpresist it and return true, otherwise
             * return false
             */
             File objectFilename = new File(filename);
             return objectFilename.delete();
        }
        else
        {
            throw new PersistenceException("Category and Object specificed do not exist, can't unpersist the given object: Category=" + categoryName + ", PersistableObject_ID=" + objectId);
        }
    }

    @Override
    public boolean unpersistObjects(HashMap<String, String> objects) throws PersistenceException
    {
        Iterator iterator = objects.entrySet().iterator();
        boolean retValue = false;
        while(iterator.hasNext())
        {
            Map.Entry hashMapEntry = (Map.Entry)iterator.next();            
            boolean result = unpersistObject((String)hashMapEntry.getKey(), (String)hashMapEntry.getValue());
            retValue = (result == true) ? true : false;
        }
        return retValue;
    }

    @Override
    public Object retrieveObject(String objectId, String categoryName) throws PersistenceException
    {
        String installRootDir = runtimeComponentContext.getComponentInstallRoot();
        String categoryDirPath = installRootDir + "\\persistence\\" + categoryName;
        String filename = categoryDirPath + "\\" + objectId;
        /**
         * First thing to do is to make sure that the category name exist, if
         * the category does not exist then an Exception must be thrown with
         * an appropriate error message.
         */
        if(isCategoryExist(categoryName))
        {
            try
            {
                FileInputStream fileInputStream = new FileInputStream(filename);
                ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
                Object object = objectInputStream.readObject();

                ServiceUnitImpl serviceUnitImpl = (ServiceUnitImpl) object;
//                logger.severe(serviceUnitImpl.getName());
//                logger.severe(serviceUnitImpl.getObjectId());
//                logger.severe(serviceUnitImpl.getStatus());
//                logger.severe(serviceUnitImpl.getNamepace() + "");
//                logger.severe(serviceUnitImpl.isBindingComponent() + "");
//                logger.severe(serviceUnitImpl.getServiceEndpoints().toString());
//                logger.severe(serviceUnitImpl.getServiceEndpoints().length + ": LENGTH");
//                logger.severe("endpoint name" + serviceUnitImpl.getServiceEndpoints()[0].getEndpointName());
//                logger.severe("endpoint name" + serviceUnitImpl.getServiceEndpoints()[0].getClass().getName());
//                logger.severe("Persisted Object Class: " + object.getClass().getName());

                return object;
            }
            catch (Exception ex)
            {
                return null;
            }
        }
        else
        {
            throw new PersistenceException("Category specificed does not exist, can't retrieve the given object: Category=" + categoryName + ", objectID=" + objectId);
        }        
    }

    @Override
    public Object[] retrieveObjects(HashMap<String, String> objects) throws PersistenceException
    {        
        ArrayList arrayList = new ArrayList();
        Iterator iterator = objects.entrySet().iterator();
        while(iterator.hasNext())
        {
            Map.Entry hashMapEntry = (Map.Entry)iterator.next();
            arrayList.add(retrieveObject((String)hashMapEntry.getKey(), (String)hashMapEntry.getValue()));
        }
        Object[] retrievedObjects = new Object[arrayList.size()];
        retrievedObjects = arrayList.toArray(retrievedObjects);
        return retrievedObjects;
    }

    @Override
    public boolean  purgeCategory(String categoryName)
    {
        String installRootDir = runtimeComponentContext.getComponentInstallRoot();
        String categoryDirPath = installRootDir + "\\persistence\\" + categoryName;
        File categoryDir = new File(categoryDirPath);
        for(File dirContent : categoryDir.listFiles())
        {
            dirContent.delete();
        }
        return categoryDir.delete();
    }

    @Override
    public Object[] retrieveAllObjects(String categoryName) throws PersistenceException
    {
        String installRootDir = runtimeComponentContext.getComponentInstallRoot();
        String categoryDirPath = installRootDir + "\\persistence\\" + categoryName;
        HashMap<String, String> hashMap = new HashMap<String, String>();
        logger.info(categoryDirPath);
        File categoryDir = new File(categoryDirPath);
        if(categoryDir.exists())
        {
            for(File persistedObject : categoryDir.listFiles())
            {
               hashMap.put(persistedObject.getName(), categoryName);
            }
            if(hashMap.size() > 0)
            {
                return retrieveObjects(hashMap);
            }
        }
        return null;
    }

}
