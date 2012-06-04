/*
 *  WSDLExtTemplateProvider.java
 */
package b2bbc.template;

import java.io.InputStream;
import org.netbeans.modules.xml.wsdl.bindingsupport.spi.ExtensibilityElementTemplateProvider;
import org.openide.util.NbBundle;
/**
 *  @author chikkala
 */
public class WSDLExtTemplateProvider extends ExtensibilityElementTemplateProvider
{
    
    private final String templateUrl = "template.xml"; // relative path.
    
    public InputStream getTemplateInputStream()
    {
        return this.getClass().getResourceAsStream(templateUrl);
    }
    
    public String getLocalizedMessage(String str, Object[] objects)
    {
        return NbBundle.getMessage(this.getClass(), str, objects);
    }
}
