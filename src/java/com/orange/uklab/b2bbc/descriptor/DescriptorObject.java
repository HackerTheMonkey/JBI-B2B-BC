/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.orange.uklab.b2bbc.descriptor;
import java.net.URI;

/**
 *
 * @author hasanein
 */
public interface DescriptorObject
{
    /**
     * Implementation of this method shall return the name of the ServiceUnit
     * modelled by this class. The name of the ServiceUnit is initially passed
     * to the Component Framework from the JBI framework alongside with the
     * path to where the ServiceUnit ZIP is exploded.
     * @return
     */
    public String getName();
    /**
     * Implementation of this method shall return the version
     * of the JBI descriptor from which this object was populated.
     * @return
     */
    public String getJbiDescriptorVersion();
    /**
     * Implementation of this method shall return back the default namespace
     * used in the JBI descriptor.
     * @return
     */
    public URI getNamepace();
}
