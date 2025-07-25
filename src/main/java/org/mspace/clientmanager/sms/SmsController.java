/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.mspace.clientmanager.sms;

import java.io.IOException;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import ke.co.mspace.nonsmppmanager.model.Alpha;
import ke.co.mspace.nonsmppmanager.model.SmsLazyDataModel;
import ke.co.mspace.nonsmppmanager.service.AlphaServiceApi;
import ke.co.mspace.nonsmppmanager.service.AlphaServiceImpl;
import ke.co.mspace.nonsmppmanager.util.JdbcUtil;
import ke.co.mspace.nonsmppmanager.util.JsfUtil;
import org.mspace.clientmanager.group.GroupDAO;
import org.mspace.clientmanager.group.GroupDAOImpl;
import org.mspace.clientmanager.user.UserController;
import org.primefaces.model.LazyDataModel;

/**
 *
 * @author olal
 */
@ManagedBean(name = "smscontroller")
@SessionScoped
public class SmsController implements Serializable {

    private LazyDataModel<UserController> lazyModel;

    private static final long serialVersionUID = 1L;

    private SmsDAO smsDAO;
    private List<UserController> smsUsers;
    private UserController newSmsUser;
    private UserController currentSmsUser;
    private AlphaServiceApi alphaDAO;
    private String alphanumeric;
    private Alpha selectedAlpha;
    private List<Alpha> senderIds;
    private GroupDAO groupDAO;
    private List<SelectItem> listGroups;
    private List<SelectItem> listUsers;
    private String username;
    private String newPassword;
    private String confirmPassword;
    private List<SelectItem> existingUsers;

    private List<SelectItem> listAlphas;
    private final JdbcUtil jdbcUtil = new JdbcUtil();

    @PostConstruct
    public void init() {
        smsDAO = new SmsDAOImpl();
        newSmsUser = new UserController();
        currentSmsUser = new UserController();
        lazyModel = new SmsLazyDataModel((SmsDAOImpl) smsDAO);
    }
   

       public LazyDataModel<UserController> getLazyModel() {
        return lazyModel;
    }

    public List<SelectItem> getExistingUsers() {
        existingUsers = smsDAO.getExistingUsers();
        return existingUsers;
    }

    public void setExistingUsers(List<SelectItem> existingUsers) {
        this.existingUsers = existingUsers;
    }

       
    public List<SelectItem> getListUsers() {
        listUsers = smsDAO.smsUsers();
        return listUsers;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }

    public List<UserController> getSmsUsers() {
        return smsUsers;
    }

    public List<SelectItem> getListAlphas() {
        listAlphas = smsDAO.getAlphas();
        return listAlphas;
    }

    public List<SelectItem> getListGroups() {
        groupDAO = new GroupDAOImpl();
        listGroups = groupDAO.listGroups();
        return listGroups;
    }

    public List<Alpha> getSenderIds() {
        return senderIds;
    }

    public UserController getNewSmsUser() {
        return newSmsUser;
    }

    public void setNewSmsUser(UserController newSmsUser) {
        this.newSmsUser = newSmsUser;
    }

    public UserController getCurrentSmsUser() {
        return currentSmsUser;
    }

    public void setCurrentSmsUser(UserController currentSmsUser) {
        newPassword = "";
        confirmPassword = "";
        this.currentSmsUser = currentSmsUser;
        refreshAlphas();
    }

    public String getAlphanumeric() {
        return alphanumeric;
    }

    public void setAlphanumeric(String alphanumeric) {
        this.alphanumeric = alphanumeric;
    }

    public Alpha getSelectedAlpha() {
        return selectedAlpha;
    }

    public void setSelectedAlpha(Alpha selectedAlpha) {
        this.selectedAlpha = selectedAlpha;
    }

    public void addSmsUser() {
        if (newSmsUser != null) {
            try {
                newSmsUser.saveUser();
            } catch (IOException e) {
                JsfUtil.addErrorMessage("Error occured while trying to create user");
            }
        }
    }

    public void editSmsUser() {
        if (currentSmsUser != null) {
            if (smsDAO.editSmsUser(currentSmsUser)) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Success", "User updated successfully."));
            } else {
                JsfUtil.addErrorMessage("Error while editing user");
            }
        } else {
            JsfUtil.addErrorMessage("No User selected for update.");
        }
    }

    public void changePass() {
        System.out.println("newPassword " + newPassword + " confirmedPass " + confirmPassword);
        if (!newPassword.isEmpty()) {
            if (!newPassword.equals(confirmPassword)) {
                FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Passwords do not match!"));
                        newPassword = "";
                        confirmPassword="";
                return;
            }

            if (smsDAO.changePass(currentSmsUser.getUsername(), newPassword, currentSmsUser.getId())) {
                JsfUtil.addSuccessMessage(currentSmsUser.getUsername() + "'s password changed successfully");
                System.out.println("username: " + currentSmsUser.getUsername() + " \n newPassword: " + newPassword + "\n Id: " + currentSmsUser.getId());

                newPassword = "";
                confirmPassword = "";
            } else {
                JsfUtil.addErrorMessage("Failed to change password try again");
                newPassword = "";
                confirmPassword = "";
            }

        } else {
            JsfUtil.addErrorMessage("Enter username and Password");
        }
    }

    public void manageSmsCredit() {
        if (currentSmsUser != null) {
            if (currentSmsUser.getCreditsToManage() < 0) {
                JsfUtil.addErrorMessage("Credit has to be positive value.");
            } else {
                try {
                    currentSmsUser.manageCredit();
                } catch (IOException e) {
                    JsfUtil.addErrorMessage("Error reading the form try again");
                }
            }
        }
    }

    public void deleteUser() {
        if (currentSmsUser != null) {
            if (smsDAO.deleteSmsUser(currentSmsUser)) {
                JsfUtil.addSuccessMessage("User was deleted successfully. ");
            } else {
                JsfUtil.addSuccessMessage("Try again Something went wrong. ");
            }
        } else {
            JsfUtil.addErrorMessage("Select User and try again. ");
        }

    }

    public void assignAlpha() {
        alphaDAO = new AlphaServiceImpl();
        try (Connection conn = jdbcUtil.getConnectionTodbSMS()) {
            if (alphaDAO.findAlphanumericByUsername(currentSmsUser.getUsername(), alphanumeric, conn)) {
                JsfUtil.addErrorMessage("Sender Id already exists for  " + currentSmsUser.getUsername());
            } else {
                String alphaType = alphaDAO.getAlphanumericType(alphanumeric, conn);
                if (alphaDAO.persistAlpha(currentSmsUser.getUsername(), alphanumeric, alphaType, conn) > 0) {
                    refreshAlphas();
                    JsfUtil.addSuccessMessage(" Sender Id successfully assigned ");
                } else {
                    JsfUtil.addErrorMessage("Something went wrong, try again");
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(SmsController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void deleteAlpha() {
        alphaDAO = new AlphaServiceImpl();
        if (selectedAlpha != null) {
            try (Connection conn = jdbcUtil.getConnectionTodbSMS()) {
                if (alphaDAO.removeUseAlpha(selectedAlpha.getName(), selectedAlpha.getUsername(), conn) > 0) {
                    refreshAlphas();
                    JsfUtil.addSuccessMessage("Successfully deleted Sender ID");
                } else {
                    JsfUtil.addErrorMessage("Something went wrong, Try again");
                }
            } catch (SQLException ex) {
                Logger.getLogger(SmsController.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            JsfUtil.addErrorMessage("Select sender Id and try again. ");
        }
    }

    private void refreshAlphas() {
        alphaDAO = new AlphaServiceImpl();
        try (Connection conn = jdbcUtil.getConnectionTodbSMS()) {
            senderIds = alphaDAO.loadAlphanumerics(currentSmsUser.getUsername(), conn);
        } catch (SQLException ex) {
            Logger.getLogger(SmsController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void resetSmsUser() {
        newSmsUser = new UserController();
    }
    
   public void addExistingUser(){
       if(username != null){
           if(smsDAO.addExisting(username)){
                  JsfUtil.addSuccessMessage("Success, User updated successfully.");
            } else {
                JsfUtil.addErrorMessage("Error while Adding user");
            }
           init();
        } else {
            JsfUtil.addErrorMessage("No User selected.");
        }
       
     }
   
}




