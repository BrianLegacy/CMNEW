/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.mspace.clientmanager.email;

import java.io.IOException;
import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.model.SelectItem;
import ke.co.mspace.nonsmppmanager.util.JsfUtil;
import org.mspace.clientmanager.group.GroupDAO;
import org.mspace.clientmanager.group.GroupDAOImpl;
import org.mspace.clientmanager.user.UserController;

/**
 *
 * @author olal
 */
@ManagedBean(name = "emailcontroller")
@ViewScoped
public class EmailController implements Serializable {

    private static final long serialVersionUID = 1L;

    private EmailDAO emailDAO;
    private List<UserController> emailUsers;
    private UserController newEmailUser;
    private UserController currentEmailUser;
    private List<SelectItem> existingUsers;
    private String username;
    private GroupDAO groupDAO;
    private List<SelectItem> listGroups;

    @PostConstruct
    public void init() {
        emailDAO = new EmailDAOImpl();
        groupDAO = new GroupDAOImpl();
        refreshUsers();
        newEmailUser = new UserController();
        currentEmailUser = new UserController();

    }

    public List<UserController> getEmailUsers() {
        return emailUsers;
    }

    public UserController getNewEmailUser() {
        return newEmailUser;
    }

    public List<SelectItem> getListGroups() {
        listGroups = groupDAO.listGroups();
        return listGroups;
    }

    public void setNewEmailUser(UserController newEmailUser) {
        this.newEmailUser = newEmailUser;
    }

    public UserController getCurrentEmailUser() {
        return currentEmailUser;
    }

    public void setCurrentEmailUser(UserController currentEmailUser) {
        this.currentEmailUser = currentEmailUser;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public List<SelectItem> getExistingUsers() {

        existingUsers = emailDAO.getExistingUsers();

        return existingUsers;
    }

    public void setExistingUsers(List<SelectItem> existingUsers) {
        this.existingUsers = existingUsers;
    }

    public void addEmailUser() {
        if (newEmailUser != null) {
            try {
                newEmailUser.saveEmailUser();
                refreshUsers();
            } catch (IOException e) {
                JsfUtil.addErrorMessage("Error occured while trying to create user");
            }
        }
    }

    public void addExistingUser() {
        if (username != null) {
            if (emailDAO.addExisting(username)) {
                JsfUtil.addSuccessMessage("Success, User updated successfully.");
            } else {
                JsfUtil.addErrorMessage("Error while Adding user");
            }
            refreshUsers();
        } else {
            JsfUtil.addErrorMessage("No User selected.");
        }
    }

    public void editEmailUser() {
        if (currentEmailUser != null) {
            if (emailDAO.editEmailUser(currentEmailUser)) {
                JsfUtil.addSuccessMessage("Success, User updated successfully.");
            } else {
                JsfUtil.addErrorMessage("Error while editing user");
            }
            refreshUsers();
        } else {
            JsfUtil.addErrorMessage("No User selected for update.");

        }
    }

    public void manageCredit() {
        if (currentEmailUser != null) {
            try {
                currentEmailUser.manageEmailCredit();
                refreshUsers();
            } catch (IOException e) {

                JsfUtil.addErrorMessage("Error while editing credits try again. ");
            }
        }
    }

    public void deleteUser() {
        if (currentEmailUser != null) {
            if (emailDAO.deleteEmailUser(currentEmailUser)) {
                refreshUsers();
                JsfUtil.addSuccessMessage("Email user was deleted successfully. ");
            } else {
                JsfUtil.addSuccessMessage("Try again Something went wrong. ");
            }
        } else {
            JsfUtil.addErrorMessage("Select user and try again. ");
        }

    }

    private void refreshUsers() {
        emailUsers = emailDAO.fetchEmailUsers();
    }
}
