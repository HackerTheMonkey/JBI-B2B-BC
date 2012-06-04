/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.orange.uklab.b2bbc.descriptor;

/**
 *
 * @author hasanein
 */
public abstract class AbstractJbiDescriptor
{
    /**
     *
     */
    public final static String JBI_DESCRIPTOR_TYPE_SERVICE_UNIT = "service_unit";
    /**
     *
     */
    public final static String JBI_DESCRIPTOR_TYPE_COMPONENT = "component";
    /**
     *
     */
    public final static String JBI_DESCRIPTOR_TYPE_SHARED_LIBRARY = "shared_library";
    /**
     *
     */
    public final static String JBI_DESCRIPTOR_TYPE_SERVICE_ASSEMBLY = "service_assembly";
    /**
     *
     */
    protected String descriptorRootPath;
    /**
     *
     */
    protected String descriptorName;
    /**
     *
     */
    protected String descriptorType;

    /**
     *
     * @param descriptorRootPath
     * @param descriptorName
     * @param descriptorType
     */
    public AbstractJbiDescriptor(String descriptorRootPath, String descriptorName, String descriptorType)
    {
        this.descriptorName = descriptorName;
        this.descriptorRootPath = descriptorRootPath;
        this.descriptorType = descriptorType;
    }
    
    /**
     *
     * @return
     * @throws ParsingException
     */
    public Component parseComponentDescriptor() throws ParsingException
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     *
     * @return
     * @throws ParsingException
     */
    public SharedLibrary parseSharedLibraryDescriptor() throws ParsingException
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     *
     * @return
     * @throws ParsingException
     */
    public ServiceUnit parseServiceUnitDescriptor() throws ParsingException
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     *
     * @return
     * @throws ParsingException
     */
    public ServiceAssembly parseServiceAssemblyDescriptor() throws ParsingException
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
