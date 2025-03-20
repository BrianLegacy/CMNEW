/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ke.co.mspace.nonsmppmanager.model;

import java.util.Date;

/**
 *
 * @author amos
 */
public class ManageCredit {
    
    private int id;
    private String username;
    private Date actionTime;
    private int previousBal;
    private int newBalance;
    private int numCredits;
    private String topupMode;
    private String systemType;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Date getActionTime() {
        return actionTime;
    }

    public void setActionTime(Date actionTime) {
        this.actionTime = actionTime;
    }

    public int getPreviousBal() {
        return previousBal;
    }

    public void setPreviousBal(int previousBal) {
        this.previousBal = previousBal;
    }

    public int getNewBalance() {
        return newBalance;
    }

    public void setNewBalance(int newBalance) {
        this.newBalance = newBalance;
    }

    public int getNumCredits() {
        return numCredits;
    }

    public void setNumCredits(int numCredits) {
        this.numCredits = numCredits;
    }

    public String getTopupMode() {
        return topupMode;
    }

    public void setTopupMode(String topupMode) {
        this.topupMode = topupMode;
    }

    public String getSystemType() {
        return systemType;
    }

    public void setSystemType(String systemType) {
        this.systemType = systemType;
    }
    
    
    
}
