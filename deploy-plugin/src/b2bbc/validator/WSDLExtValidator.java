/*
 * WSDLExtValidator.java
 */

package b2bbc.validator;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import b2bbc.model.BindingExt;
import b2bbc.model.ExtVisitor;
import b2bbc.model.FaultExt;
import b2bbc.model.InputExt;
import b2bbc.model.OperationExt;
import b2bbc.model.OutputExt;
import b2bbc.model.PortExt;
import org.netbeans.modules.xml.wsdl.model.Binding;
import org.netbeans.modules.xml.wsdl.model.BindingFault;
import org.netbeans.modules.xml.wsdl.model.BindingInput;
import org.netbeans.modules.xml.wsdl.model.BindingOperation;
import org.netbeans.modules.xml.wsdl.model.BindingOutput;
import org.netbeans.modules.xml.wsdl.model.Definitions;
import org.netbeans.modules.xml.wsdl.model.Port;
import org.netbeans.modules.xml.wsdl.model.Service;
import org.netbeans.modules.xml.wsdl.model.WSDLModel;
import org.netbeans.modules.xml.xam.Model;
import org.netbeans.modules.xml.xam.Model.State;
import org.netbeans.modules.xml.xam.spi.Validation;
import org.netbeans.modules.xml.xam.spi.Validation.ValidationType;
import org.netbeans.modules.xml.xam.spi.ValidationResult;
import org.netbeans.modules.xml.xam.spi.Validator;
import org.netbeans.modules.xml.xam.spi.Validator.ResultItem;
import org.openide.util.NbBundle;

/**
 * This class implements the wsdl model validation with respect to the wsdl extension
 * elements supported by the specific wsdl extension namespace.
 * @author ${user}
 */
public class WSDLExtValidator implements Validator, ExtVisitor
{
    
    public static final Set<ResultItem> EMPTY_RESULT_ITEM = Collections.emptySet();
    public static final Set<Model> EMPTY_MODEL = Collections.emptySet();
    public static final ValidationResult EMPTY_RESULT = new ValidationResult(EMPTY_RESULT_ITEM, EMPTY_MODEL);
    
    private Validation mValidation;
    private ValidationType mValidationType;
    private ValidationResult mValidationResult;
    
    public WSDLExtValidator()
    {
    }
    
    /**
     * @return name of the validation service.
     */
    public String getName()
    {
        return getClass().getName();
    }
    
    /**
     * Validates given model.
     * @param model model to validate.
     * @param validation reference to the validation context.
     * @param validationType the type of validation to perform
     * @return ValidationResult.
     */
    public ValidationResult validate(Model model, Validation validation, ValidationType validationType)
    {
        
        mValidation = validation;
        mValidationType = validationType;
        
        HashSet<ResultItem> results = new HashSet<ResultItem>();
        HashSet<Model> models = new HashSet<Model>();
        models.add(model);
        
        mValidationResult = new ValidationResult(results, models);
        
        if (model instanceof WSDLModel)
        {
            WSDLModel wsdlModel = (WSDLModel) model;
            
            if (model.getState() == State.NOT_WELL_FORMED)
            {
                return EMPTY_RESULT;
            }
            
            validateWSDLModel(wsdlModel, results); // validate WSDL Model
        }
        // Clear out our state
        mValidation = null;
        mValidationType = null;
        
        return mValidationResult;
    }
    /**
     *  this method validates the WSDLModel and updates the result set with the
     *  possible ERRORs, ADVICEs or WARNINGS
     *  It calls each wsdl extension element models visitor method to validate each extension element.
     *  Modify this method to add more validations such as scope and limit of the extension elements etc.
     */
    public void validateWSDLModel(WSDLModel wsdlModel, HashSet<ResultItem> results)
    {
        //TODO: add any specific validations such as extension element size etc in addition
        // to the element validation itself.
        Definitions defs = wsdlModel.getDefinitions();
        // for each binding
        for (Binding binding : defs.getBindings())
        {
            // validate binding extensions
            for (BindingExt bindingExt : binding.getExtensibilityElements(BindingExt.class))
            {
                bindingExt.accept(this);
            }
            // for each binding operation
            for (BindingOperation bindingOp : binding.getBindingOperations())
            {
                // validate binding operation extensions
                for (OperationExt operationExt : bindingOp.getExtensibilityElements(OperationExt.class))
                {
                    operationExt.accept(this);
                }
                // validate binidng operation input extensions
                BindingInput bindingInput = bindingOp.getBindingInput();
                if (bindingInput != null)
                {
                    for (InputExt inputExt : bindingInput.getExtensibilityElements(InputExt.class))
                    {
                        inputExt.accept(this);
                    }
                }
                // validate binding operation output extensions
                BindingOutput bindingOutput = bindingOp.getBindingOutput();
                if (bindingOutput != null)
                {
                    for (OutputExt outputExt : bindingOutput.getExtensibilityElements(OutputExt.class))
                    {
                        outputExt.accept(this);
                    }
                }
                // validate binding operation fault extensions
                for (BindingFault bindingFault : bindingOp.getBindingFaults())
                {
                    for (FaultExt faultExt : bindingFault.getExtensibilityElements(FaultExt.class))
                    {
                        faultExt.accept(this);
                    }
                }
            }
        }
        // for each service
        for (Service service : defs.getServices())
        {
            // for each port
            for (Port port : service.getPorts())
            {
                // validate port extension
                for (PortExt portExt : port.getExtensibilityElements(PortExt.class))
                {
                    portExt.accept(this);
                }
            }
        }
    }
    
    public void visit(BindingExt bindingExt)
    {
        Collection<ResultItem> results = mValidationResult.getValidationResult();
        //TODO: validate attributes and other required things and add appropriate results.
        //
        //results.add(
        //    new Validator.ResultItem(this, // validator
        //    Validator.ResultType.ERROR,  // .ADVICE | .ERROR | .WARNING
        //    bindingExt,  // Validated object
        //    NbBundle.getMessage(this.getClass(), "I18NKEY") // I18Ned message
        //    ));
        //
    }
    
    public void visit(OperationExt operationExt)
    {
        Collection<ResultItem> results = mValidationResult.getValidationResult();
        //TODO: validate attributes and other required things and add appropriate results.
        //
        //results.add(
        //    new Validator.ResultItem(this, // validator
        //    Validator.ResultType.ERROR,  // .ADVICE | .ERROR | .WARNING
        //    operationExt,  // Validated object
        //    NbBundle.getMessage(this.getClass(), "I18NKEY") // I18Ned message
        //    ));
        //
    }
    
    public void visit(InputExt inputExt)
    {
        Collection<ResultItem> results = mValidationResult.getValidationResult();
        //TODO: validate attributes and other required things and add appropriate results.
        //
        //results.add(
        //    new Validator.ResultItem(this, // validator
        //    Validator.ResultType.ERROR,  // .ADVICE | .ERROR | .WARNING
        //    inputExt,  // Validated object
        //    NbBundle.getMessage(this.getClass(), "I18NKEY") // I18Ned message
        //    ));
        //
    }
    
    public void visit(OutputExt outputExt)
    {
        Collection<ResultItem> results = mValidationResult.getValidationResult();
        //TODO: validate attributes and other required things and add appropriate results.
        //
        //results.add(
        //    new Validator.ResultItem(this, // validator
        //    Validator.ResultType.ERROR,  // .ADVICE | .ERROR | .WARNING
        //    outputExt,  // Validated object
        //    NbBundle.getMessage(this.getClass(), "I18NKEY") // I18Ned message
        //    ));
        //
    }
    
    public void visit(FaultExt faultExt)
    {
        Collection<ResultItem> results = mValidationResult.getValidationResult();
        //TODO: validate attributes and other required things and add appropriate results.
        //
        //results.add(
        //    new Validator.ResultItem(this, // validator
        //    Validator.ResultType.ERROR,  // .ADVICE | .ERROR | .WARNING
        //    faultExt,  // Validated object
        //    NbBundle.getMessage(this.getClass(), "I18NKEY") // I18Ned message
        //    ));
        //
    }
    
    public void visit(PortExt portExt)
    {
        Collection<ResultItem> results = mValidationResult.getValidationResult();
        //TODO: validate attributes and other required things and add appropriate results.
        //
        //results.add(
        //    new Validator.ResultItem(this, // validator
        //    Validator.ResultType.ERROR,  // .ADVICE | .ERROR | .WARNING
        //    portExt,  // Validated object
        //    NbBundle.getMessage(this.getClass(), "I18NKEY") // I18Ned message
        //    ));
        //
    }
}
