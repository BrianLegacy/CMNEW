/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ke.co.mspace.nonsmppmanager.service;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;

/**
 *
 * @author developer
 */
@FacesValidator(value = "tetsBedValidator")
public class TestBedValidator implements Validator{

    /**
     * Creates a new instance of ThunkValidator
     */
    public TestBedValidator() {
    }

    @Override
    public void validate(FacesContext fc, UIComponent uic, Object o) throws ValidatorException {
       
         System.out.println("called");
        String NUM2="^[0-9]*$";
        Pattern pattern=Pattern.compile(NUM2);
       
        
        String value= (String)o;
        System.out.println("Our string "+value);
        String [] numbers=value.split(",\\s*");
        List<String> numberList=Arrays.asList(numbers);
        System.out.println("Our string[] "+numberList);
        
        numberList.forEach((num)->{
            System.out.println("Our string[] length"+num.length());
         if(num.length()!=12 || !num.startsWith("254")){
             
              FacesMessage message=new FacesMessage();
            message.setDetail("Please only  valid numbers");
            message.setSummary("Several numbers are invalid");
            message.setSeverity(FacesMessage.SEVERITY_ERROR);
            throw new ValidatorException(message); 
           
         }
        });
        
        
 
    }
    
}
