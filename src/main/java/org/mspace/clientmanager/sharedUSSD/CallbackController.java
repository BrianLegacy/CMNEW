/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.mspace.clientmanager.sharedUSSD;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import ke.co.mspace.nonsmppmanager.util.JdbcUtil;
import ke.co.mspace.nonsmppmanager.util.JsfUtil;

/**
 *
 * @author olal
 */
@ManagedBean(name = "callbackController")
@ViewScoped
public class CallbackController implements Serializable {

    private final JdbcUtil util = new JdbcUtil();
    private static final long serialVersionUID = 1L;
    private CallbackDAO callbackDAO;
    private List<CallbackModel> callbacks;
    private CallbackModel currentUrl;
    private CallbackModel newUrl;
    private String username;

    @PostConstruct
    public void init() {
        callbackDAO = new CallbackDAOimpl();
        refreshurls();
        newUrl = new CallbackModel();
        currentUrl = new CallbackModel();
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public List<CallbackModel> getCallbacks() {
        return callbacks;
    }

    public void setCallbacks(List<CallbackModel> callbacks) {
        this.callbacks = callbacks;
    }

    public CallbackModel getCurrentUrl() {
        return currentUrl;
    }

    public void setCurrentUrl(CallbackModel currentUrl) {
        this.currentUrl = currentUrl;
    }

    public CallbackModel getNewUrl() {
        return newUrl;
    }

    public void setNewUrl(CallbackModel newUrl) {
        this.newUrl = newUrl;
    }

    public void createCallback() {
        int userID = finduserId(username);
        newUrl.setUserid(userID);
        if (callbackDAO.createCallback(newUrl)) {
            System.out.println("############## "+ newUrl.isStatus() + "$$$$$$$$$$$$$ " + newUrl.isTestbed());
            refreshurls();
            newUrl = new CallbackModel();
            JsfUtil.addSuccessMessage("Success, Callback created successfully.");
        } else {
            JsfUtil.addErrorMessage("Error while creating callback");
        }
    }

    public void editCallback() {
        if (currentUrl != null) {
            if (callbackDAO.editCallback(currentUrl)) {
                refreshurls();
                JsfUtil.addSuccessMessage("Success, Callback updated successfully.");
            } else {
                JsfUtil.addErrorMessage("Error while editing callback");
            }
        } else {
            JsfUtil.addErrorMessage("No Callback selected for update.");
        }
    }

    public void deleteCallback() {
        if (currentUrl != null) {
            if (callbackDAO.deleteCallback(currentUrl)) {
                refreshurls();
                JsfUtil.addSuccessMessage("Success, Callback deleted successfully.");
            } else {
                JsfUtil.addErrorMessage("Error while deleting callback");
            }
        } else {
            JsfUtil.addErrorMessage("No Callback selected for update.");
        }
    }

    private void refreshurls() {
        callbacks = callbackDAO.fetchCallbacks();
    }

    private int finduserId(String username) {
        int id = 0 ;
        String sql = "SELECT * FROM tUSER WHERE username=?";
        try (Connection conn = util.getConnectionTodbSMS(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, username);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    id = rs.getInt("id");
                    return id;
                }
            }
        } catch (SQLException ex) {

        }
        return id;
    }
}
