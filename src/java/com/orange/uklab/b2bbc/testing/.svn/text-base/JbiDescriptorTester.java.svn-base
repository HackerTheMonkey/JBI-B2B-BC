/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.orange.uklab.b2bbc.testing;
import com.orange.uklab.b2bbc.descriptor.ParsingException;
import com.orange.uklab.b2bbc.descriptor.impl.JbiDescriptor;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author hasanein
 */
public class JbiDescriptorTester
{
    public void test1()
    {
        JbiDescriptor jbiDescriptor = new JbiDescriptor(null, null, null);
        try
        {
            jbiDescriptor.parseServiceUnitDescriptor();
        }
        catch (ParsingException ex)
        {
            Logger.getLogger(JbiDescriptorTester.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
