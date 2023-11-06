/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.mspace.clientmanager.validators;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;
import org.mspace.clientmanager.group.GroupDAOImpl;
import org.mspace.clientmanager.group.GroupDAO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author developer
 */
@FacesValidator("groupnamevalidator")
public class GroupNameValidator implements Validator {
    Logger logger=LoggerFactory.getLogger(GroupNameValidator.class);
    GroupDAO groupService = new GroupDAOImpl();

    @Override
    public void validate(FacesContext fc, UIComponent uic, Object o) throws ValidatorException {

        String _name = (String) o;
        boolean result = groupService.checkIfGroupNameExists(_name);
       
        if (result) {
             FacesMessage facesMessage
                = new FacesMessage(FacesMessage.SEVERITY_ERROR, "GroupName Error", "Group name already taken");
            logger.warn("validation failed");
            throw new ValidatorException(facesMessage);
        }

    }

}
