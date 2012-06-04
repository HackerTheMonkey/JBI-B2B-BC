/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.orange.uklab.b2bbc.persistence;
import java.util.HashMap;

/**
 *
 * @author hasanein
 */
public interface DataPersister
{
    public void createPersistenceCategory(String categoryName) throws PersistenceException;
    public boolean persistObject(Object object, String categoryName, boolean overwrite) throws PersistenceException;
    public boolean persistObjects(HashMap<Object, String> objects, boolean overwrite) throws PersistenceException;
    public boolean unpersistObject(String objectId, String categoryName) throws PersistenceException;
    public boolean unpersistObjects(HashMap<String, String> objects) throws PersistenceException;
    public Object retrieveObject(String objectId, String categoryName) throws PersistenceException;
    public Object[] retrieveObjects(HashMap<String, String> objects) throws PersistenceException;
    public Object[] retrieveAllObjects(String categoryName) throws PersistenceException;
    public boolean purgeCategory(String categoryName);
}