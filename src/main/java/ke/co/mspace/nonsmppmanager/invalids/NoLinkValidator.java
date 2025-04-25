/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ke.co.mspace.nonsmppmanager.invalids;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;

/**
 *
 * @author amos
 */
@FacesValidator(value="noLinkValidator")
public class NoLinkValidator implements Validator {

    @Override
    public void validate(FacesContext context, UIComponent component, Object value) throws ValidatorException {
         if (value != null && value.equals("(?i).*(http|www\\.|\\.com|\\.net|\\.org|\\.co|\\.io|://).*")) {
            throw new ValidatorException(new FacesMessage("Username cannot contain links or URLs"));
        }
    }
    
}
