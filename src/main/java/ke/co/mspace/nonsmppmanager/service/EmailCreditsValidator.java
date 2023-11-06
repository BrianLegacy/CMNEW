/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ke.co.mspace.nonsmppmanager.service;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.inject.Named;
import javax.enterprise.context.Dependent;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.ValidatorException;
import ke.co.mspace.nonsmppmanager.util.JdbcUtil;
import ke.co.mspace.nonsmppmanager.util.JsfUtil;
import javax.faces.validator.Validator;
/**
 *
 * @author developer
 */
//@Named(value = "emailCreditsValidator")
//@Dependent
@FacesValidator("validateEmailCredits")
public class EmailCreditsValidator implements Validator {

        private static final Logger LOG = Logger.getLogger(ValidateCredits.class.getName());
    final JdbcUtil util = new JdbcUtil();
    Connection conn;

    @Override
    public void validate(FacesContext fc, UIComponent uic, Object value) throws ValidatorException {
        try {
            UserScroller us = new UserScroller();

            conn = util.getConnectionTodbSMS();
            LOG.info("kvalidate email");
           // System.out.println("Credits to Allocate" + inputValue);
            int adminv =Character.getNumericValue((char) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("admin"));
            
            System.out.println("Component ID:" + uic.getId());
            if (uic.getId().equals("smscredits")) {
                int availableCredits = Math.round(us.availableCredits(conn)[2]);
                int inputValue=(Integer) value;
                if ((inputValue > availableCredits) && adminv != 1) {
                    FacesMessage facesMessage = new FacesMessage("Insuficient Credits to allocate this Ammount");
                    facesMessage.setSeverity(FacesMessage.SEVERITY_ERROR);
                    //FacesContext.getCurrentInstance().addMessage("message", new FacesMessage("hola"));
//                    JsfUtil.addErrorMessage("Insuficient Credits to allocate this Ammount");
                    throw new ValidatorException(facesMessage);
                }
            }else if(uic.getId().equals("costpersms")){
                float costPersoms = us.availableCredits(conn)[1];
              if((Float)value <= costPersoms  && adminv != 1){
                   FacesMessage facesMessage = new FacesMessage("User cost per SMS cant be below Agent's cost per sms");
                    facesMessage.setSeverity(FacesMessage.SEVERITY_ERROR);
                    JsfUtil.addErrorMessage("Cost per sms too low");
                    throw new ValidatorException(facesMessage);
              }
            }

            JdbcUtil.closeConnection(conn);

        } catch (SQLException ex) {
            Logger.getLogger(ValidateCredits.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
