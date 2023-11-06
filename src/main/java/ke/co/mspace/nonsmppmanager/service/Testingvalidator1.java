/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ke.co.mspace.nonsmppmanager.service;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.*;

/**
 *
 * @author Samson
 */

//@FacesValidator(value = "testingvalidator")
public class Testingvalidator1 implements Validator {

    public Testingvalidator1() {
    }

    @Override
    public void validate(FacesContext fc, UIComponent uic, Object o) throws ValidatorException {
        System.out.println("i have been called here");
        throw new UnsupportedOperationException("Not supported yet."); 
    }
    
}
