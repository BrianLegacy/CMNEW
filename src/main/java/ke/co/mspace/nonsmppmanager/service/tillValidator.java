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
import javax.faces.bean.ManagedBean;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.*;

/**
 *
 * @author Samson
 */

@FacesValidator("tillValidator")
public class tillValidator implements Validator {

    public tillValidator() {
    }

   private static final String NUM="^[0-9]*$";
    @Override
    public void validate(FacesContext fc, UIComponent uic, Object o) throws ValidatorException {
      //  throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        System.out.println("called");
        Pattern pattern=Pattern.compile(this.NUM);
       
        
        String value= ((Integer)o).toString();;
        Matcher matcher=pattern.matcher(value);
        if(!matcher.matches()){
            FacesMessage message=new FacesMessage();
            message.setDetail("Please only enter digits");
            message.setSummary("You cant enter nondigit");
            message.setSeverity(FacesMessage.SEVERITY_ERROR);
            throw new ValidatorException(message);
        }
        
    }
    
    public void validate2(FacesContext fc, UIComponent uic, Object o) throws ValidatorException {
      //  throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
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
             throwError("Error","Several numbers are invalid");
           
         }
        });
        
        
      
        
    }
    
    private void throwError(String title,String msg){
           FacesMessage message=new FacesMessage();
            message.setDetail(title);
            message.setSummary(msg);
            message.setSeverity(FacesMessage.SEVERITY_ERROR);
            throw new ValidatorException(message);
    }
}
