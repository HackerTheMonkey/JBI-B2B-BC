/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.orange.uklab.b2bbc.persistence;
import com.orange.uklab.b2bbc.descriptor.DescriptorObject;
import java.io.Serializable;

/**
 *
 * @author hasanein
 */
public interface Persistable<E extends DescriptorObject> extends Serializable
{
    public String getObjectId();
}
