/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ke.co.mspace.nonsmppmanager.service;

/**
 *
 * @author Norrey Osako
 */
import java.sql.Connection;
import java.sql.SQLException;
import java.util.logging.Logger;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;
import ke.co.mspace.nonsmppmanager.model.User;
import ke.co.mspace.nonsmppmanager.util.JdbcUtil;

/**
 * source : www.javabeat.net
 */
@FacesValidator("customUsernameValidator")
public class CustomUsernameValidator implements Validator {
    private static final Logger LOG = Logger.getLogger(CustomUsernameValidator.class.getName());
    

    @Override
    public void validate(FacesContext facesContext, UIComponent arg1, Object value) throws ValidatorException {
        String inputValue = (String) value;
        try{
            JdbcUtil util = new JdbcUtil();
            Connection conn = util.getConnectionTodbSMS();
            LOG.info("validate");
         
        UserServiceApi service = new UserServiceImpl();
        User user = service.loadCustomerByUsername(inputValue, conn);
        boolean isValid = (user.getUsername()==null);
           
            if (!isValid) {
                FacesMessage facesMessage = new FacesMessage("Username already in use", "Username already in use");
                
                facesMessage.setSeverity(FacesMessage.SEVERITY_ERROR);
                
                

			throw new ValidatorException(facesMessage);
            }
            JdbcUtil.closeConnection(conn);
        }catch(SQLException e){
            JdbcUtil.printSQLException(e);
        }
//        @Override
//        public void validate(FacesContext facesContext, UIComponent arg1, Object value) throws ValidatorException {
//        String inputValue = (String) value;
//        try{
//            JdbcUtil util = new JdbcUtil();
//            Connection conn = util.getConnectionTodbTASKS();
//            LOG.info("validate");
//        UserServiceApi service = new UserServiceImpl();
//        User user = service.(inputValue, conn);
//        boolean isValid = (user.getUsername()==null);
//        
//            if (!isValid) {
//                FacesMessage facesMessage = new FacesMessage("Code already in use", "Code already in use");
//                
//                facesMessage.setSeverity(FacesMessage.SEVERITY_ERROR);
//			throw new ValidatorException(facesMessage);
//            }
//            JdbcUtil.closeConnection(conn);
//        }catch(SQLException e){
//            JdbcUtil.printSQLException(e);
//        }
        
    }
}