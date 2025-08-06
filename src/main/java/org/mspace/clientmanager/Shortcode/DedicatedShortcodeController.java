/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author Brian
 */

package org.mspace.clientmanager.Shortcode;

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

@ManagedBean(name = "dedicatedShortcodeController")
@ViewScoped
public class DedicatedShortcodeController implements Serializable {

    private static final long serialVersionUID = 1L;
    private final JdbcUtil util = new JdbcUtil();

    private ShortcodeDAO shortcodeDAO;
    private List<ShortcodeModel> shortcodes;
    private ShortcodeModel currentShortcode;
    private ShortcodeModel newShortcode;
    private String username;

    @PostConstruct
    public void init() {
        shortcodeDAO = new ShortcodeDAOImpl();
        refreshShortcodes();
        newShortcode = new ShortcodeModel();
        currentShortcode = new ShortcodeModel();
    }

    public List<ShortcodeModel> getShortcodes() {
        return shortcodes;
    }

    public void setShortcodes(List<ShortcodeModel> shortcodes) {
        this.shortcodes = shortcodes;
    }

    public ShortcodeModel getCurrentShortcode() {
        return currentShortcode;
    }

    public void setCurrentShortcode(ShortcodeModel currentShortcode) {
        this.currentShortcode = currentShortcode;
    }

    public ShortcodeModel getNewShortcode() {
        return newShortcode;
    }

    public void setNewShortcode(ShortcodeModel newShortcode) {
        this.newShortcode = newShortcode;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void prepareNewShortcode() {
        newShortcode = new ShortcodeModel();
        username = null;
    }

    public void createShortcode() {
        int userID = findUserId(username);
        newShortcode.setUserid(userID);
        newShortcode.setUsername(username);

        if (shortcodeDAO.createShortcode(newShortcode)) {
            refreshShortcodes();
            newShortcode = new ShortcodeModel();
            username = null;
            JsfUtil.addSuccessMessage("Success, Shortcode created successfully.");
        } else {
            JsfUtil.addErrorMessage("Error while creating shortcode.");
        }
    }

    public void editShortcode() {
        if (currentShortcode != null) {
            if (shortcodeDAO.editShortcode(currentShortcode)) {
                refreshShortcodes();
                JsfUtil.addSuccessMessage("Success, Shortcode updated successfully.");
            } else {
                JsfUtil.addErrorMessage("Error while editing shortcode.");
            }
        } else {
            JsfUtil.addErrorMessage("No Shortcode selected for update.");
        }
    }

    public void deleteShortcode() {
        if (currentShortcode != null) {
            if (shortcodeDAO.deleteShortcode(currentShortcode)) {
                refreshShortcodes();
                JsfUtil.addSuccessMessage("Success, Shortcode deleted successfully.");
            } else {
                JsfUtil.addErrorMessage("Error while deleting shortcode.");
            }
        } else {
            JsfUtil.addErrorMessage("No Shortcode selected for deletion.");
        }
    }

    private void refreshShortcodes() {
        shortcodes = shortcodeDAO.fetchShortcodesWithNullKeyword();
    }

    private int findUserId(String username) {
        int id = 0;
        String sql = "SELECT id FROM tUSER WHERE username=?";
        try (Connection conn = util.getConnectionTodbSMS(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, username);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    id = rs.getInt("id");
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return id;
    }
}

