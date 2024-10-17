/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.mspace.clientmanager.credits.model;

import java.util.Date;

/**
 *
 * @author mspace
 */
public class SMSCredits {
    private Long id;
    private String username;
    private char actionType;
    private Date actionTime;
    private int numCredits;
    private int previous_balance;
    private int new_balance;
    private String agent;
    private int agent_prevbal;
    private int agent_newbal;
    private String system_type;
    
    public SMSCredits(Long id, String username, char actionType, Date actionTime, int numCredits, int previous_balance, int new_balance, String system_type) {
        this.id = id;
        this.username = username;
        this.actionType = actionType;
        this.actionTime = actionTime;
        this.numCredits = numCredits;
        this.previous_balance = previous_balance;
        this.new_balance = new_balance;
        this.system_type = system_type;
    }

    public SMSCredits() {
    }

    public String getSystem_type() {
        return system_type;
    }

    public void setSystem_type(String system_type) {
        this.system_type = system_type;
    }

    
    public int getAgent_prevbal() {
        return agent_prevbal;
    }

    public void setAgent_prevbal(int agent_prevbal) {
        this.agent_prevbal = agent_prevbal;
    }

    public int getAgent_newbal() {
        return agent_newbal;
    }

    public void setAgent_newbal(int agent_newbal) {
        this.agent_newbal = agent_newbal;
    }
    
    public String getAgent() {
        return agent;
    }

    public void setAgent(String agent) {
        this.agent = agent;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public char isActionType() {
        return actionType;
    }

    public void setActionType(char actionType) {
        this.actionType = actionType;
    }

    public Date getActionTime() {
        return actionTime;
    }

    public void setActionTime(Date actionTime) {
        this.actionTime = actionTime;
    }

    public int getNumCredits() {
        return numCredits;
    }

    public void setNumCredits(int numCredits) {
        this.numCredits = numCredits;
    }
    

    public int getPrevious_balance() {
        return previous_balance;
    }

    public void setPrevious_balance(int previous_balance) {
        this.previous_balance = previous_balance;
    }

    public int getNew_balance() {
        return new_balance;
    }

    public void setNew_balance(int new_balance) {
        this.new_balance = new_balance;
    }
    
}
