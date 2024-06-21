/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.mspace.clientmanager.sms;

import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.bean.ViewScoped;
import org.mspace.clientmanager.user.UserController;

/**
 *
 * @author olal
 */
@ManagedBean(name = "smscontroller")
@ViewScoped
public class SmsController implements Serializable {

    private static final long serialVersionUID = 1L;

    private SmsDAO smsDAO;
    private List<UserController> smsUsers;
    private UserController newSmsUser;
    private UserController currentSmsUser;

    @PostConstruct
    public void init() {
        smsDAO = new SmsDAOImpl();
        refreshUsers();
        newSmsUser = new UserController();

    }

    public List<UserController> getSmsUsers() {
        return smsUsers;
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
        this.currentSmsUser = currentSmsUser;
    }

    private void refreshUsers() {
        smsUsers = smsDAO.fetchSmsusers();
    }
    
    public void resetSmsUser(){
        newSmsUser= new UserController();
    }
}
