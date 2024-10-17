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
@FacesValidator(value = "tetsBedValidator")
public class TestBedValidatar implements Validator {
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
    public TestBedValidatar() {
    }

    @Override
       public void validate(FacesContext fc, UIComponent uic, Object o) throws ValidatorException {
           
         
       
//         System.out.println("scalled");
//^((0?[17]\d{8})|(\+?25[46][17]\d{8}))$
        String testpattern="^[0-9]*$";
        Pattern pattern=Pattern.compile(testpattern);
       
        String value= (String)o;
        
//        System.out.println("Our string "+value);
        String [] numbers=value.split(",\\s*");
        List<String> numberList=Arrays.asList(numbers);
//        System.out.println("Our string[] "+numberList);
        
        numberList.forEach((num)->{
//            System.out.println("Our string[] length"+num.length());
         if(num.length()==10 && (num.startsWith("01") ||(num.startsWith("07")))){
            Matcher matcher=pattern.matcher(num);
        if(!matcher.matches()){
//           System.out.println("setting red");
            setCount("red");
        throwError();
        }
//          System.out.println("setting blue");
            setCount("blue");
         }
         else if(num.length()==12 && num.startsWith("254")){
           Matcher matcher=pattern.matcher(num);
        if(!matcher.matches()){
//             System.out.println("setting red");
             setCount("red");
        throwError();}
           
         }else{
//             System.out.println("setting red");
                  setCount("red");
             throwError();
         }
//           System.out.println("setting blue");
             setCount("blue");
        });
//        num.length()!=12 || !num.startsWith("254")
        
        System.out.println(getCount());
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
