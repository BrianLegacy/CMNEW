/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.mspace.clientmanager.sharedUSSD;

/**
 *
 * @author olal
 */
public class CallbackModel {

    private int id;
    private int userid;
    private String callback_url;
    private String testbednumbers;

    private String ussd_assigned_code;
    private boolean status;
    private boolean testbed;
    private String real_status;

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

    public String getCallback_url() {
        return callback_url;
    }

    public void setCallback_url(String callback_url) {
        this.callback_url = callback_url;
    }

    public String getTestbednumbers() {
        return testbednumbers;
    }

    public void setTestbednumbers(String testbednumbers) {
        this.testbednumbers = testbednumbers;
    }

    public String getUssd_assigned_code() {
        return ussd_assigned_code;
    }

    public void setUssd_assigned_code(String ussd_assigned_code) {
        this.ussd_assigned_code = ussd_assigned_code;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public boolean isTestbed() {
        return testbed;
    }

    public void setTestbed(boolean testbed) {
        this.testbed = testbed;
    }

    public String getReal_status() {
        if (status == true) {
            real_status = "Active";
        } else {
            real_status = "Inactive";
        }
        return real_status;
    }

    public void setReal_status(String real_status) {
        this.real_status = real_status;
    }

    public CallbackModel(int userid, String callback_url,  String ussd_assigned_code, boolean status, String real_status) {
        this.userid = userid;
        this.callback_url = callback_url;
        this.ussd_assigned_code = ussd_assigned_code;
        this.status = status;
        this.real_status = real_status;
    }

    public CallbackModel() {
    }
    
    
}
