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


public class CustomCodeValidator implements Validator{
private static final String NUM="^[0-9]*$";
    @Override
    public void validate(FacesContext fc, UIComponent uic, Object o) throws ValidatorException {
      //  throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        Pattern pattern=Pattern.compile(this.NUM);
        String value=(String)o;
        Matcher matcher=pattern.matcher(value);
        if(!matcher.matches()){
            FacesMessage message=new FacesMessage();
            message.setDetail("Please only enter digits");
            message.setSummary("You cant enter nondigit");
            message.setSeverity(FacesMessage.SEVERITY_ERROR);
            throw new ValidatorException(message);
        }
        
    }
    
}
