
/*
correct one
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ke.co.mspace.nonsmppmanager.service;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
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
@FacesValidator(value = "testbedvalidatorv1")
public class TestbedValidator implements Validator {
String count="red";
    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        
        this.count = count;
    }

  


    /**
     * Creates a new instance of TestBedValidatar
     */
    public TestbedValidator () {
    }

    @Override
       public void validate(FacesContext fc, UIComponent uic, Object o) throws ValidatorException {
           
        String testpattern="^\\s*((0?[17]\\d{8})|(\\+?25[46][17]\\d{8}))\\s*$";
        Pattern pattern=Pattern.compile(testpattern);
       
        String value= (String)o;
      String [] numbers=value.split(",\\s*");
        List<String> numberList=Arrays.asList(numbers);
        
        numberList.forEach((num)->{
       
            Matcher matcher=pattern.matcher(num);
        if(!matcher.matches()){
        throwError();
        }
        });
       
       
       
       
       
       
       
       
       
       }

    private void throwError(){
       System.out.println(getCount());
         FacesMessage message=new FacesMessage();
            message.setDetail("Please only  valid numbers");
            message.setSummary("Several numbers are invalid");
            message.setSeverity(FacesMessage.SEVERITY_FATAL);
            throw new ValidatorException(message); 
            
           
    }
    
    
    
}
