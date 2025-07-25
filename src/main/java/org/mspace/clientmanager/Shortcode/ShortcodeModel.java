/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.mspace.clientmanager.Shortcode;

/**
 *
 * @author brian
 */
import java.util.Date;

public class ShortcodeModel {

    private int id;
    private int userid;
    private String keyword;
    private String username;
    private String shortcode;
    private String callbackUrl;
    private boolean status;
    private String realStatus;
    private Date dueDate;
    private Date disconnectDate;

    public ShortcodeModel() {
    }

    public ShortcodeModel(int userid, String shortcode, String callbackUrl, boolean status, String realStatus, Date dueDate, Date disconnectDate) {
        this.userid = userid;
        this.shortcode = shortcode;
        this.callbackUrl = callbackUrl;
        this.status = status;
        this.realStatus = realStatus;
        this.dueDate = dueDate;
        this.disconnectDate = disconnectDate;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserid() {
        return userid;
    }

    public void setUserid(int userid) {
        this.userid = userid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getShortcode() {
        return shortcode;
    }

    public void setShortcode(String shortcode) {
        this.shortcode = shortcode;
    }

    public String getCallbackUrl() {
        return callbackUrl;
    }

    public void setCallbackUrl(String callbackUrl) {
        this.callbackUrl = callbackUrl;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public String getRealStatus() {
        if (status) {
            realStatus = "Active";
        } else {
            realStatus = "Inactive";
        }
        return realStatus;
    }

    public void setRealStatus(String realStatus) {
        this.realStatus = realStatus;
    }

    public Date getDueDate() {
        return dueDate;
    }

    public void setDueDate(Date dueDate) {
        this.dueDate = dueDate;
    }

    public Date getDisconnectDate() {
        return disconnectDate;
    }

    public void setDisconnectDate(Date disconnectDate) {
        this.disconnectDate = disconnectDate;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }
}
