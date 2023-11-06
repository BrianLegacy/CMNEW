/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ke.co.mspace.nonsmppmanager.service;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.*;

/**
 *
 * @author Samson
 */
@FacesValidator(value = "urlValidator")
public class URLValidator implements Validator{
    
            
   private static final String URL1="^(https?|ftp|file)://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]";
  // private static final String URL2="^(https?):\\/\\";
   
    /**
     * Creates a new instance of urlValidator
     */
    public URLValidator() {
    }

    @Override
    public void validate(FacesContext fc, UIComponent uic, Object o) throws ValidatorException {
        System.out.println("i have been called");
          Pattern pattern=Pattern.compile(this.URL1,Pattern.CASE_INSENSITIVE);
        //  Pattern pattern1=Pattern.compile(this.URL2);
        String value=(String)o;
        Matcher matcher=pattern.matcher(value);
        //Matcher matcher1=pattern1.matcher(value);
        if(!matcher.matches()){
            FacesMessage message=new FacesMessage();
            message.setDetail("Please only a valid URL");
            message.setSummary("The URL is invalid");
            message.setSeverity(FacesMessage.SEVERITY_ERROR);
            throw new ValidatorException(message);
    }
    
}
}