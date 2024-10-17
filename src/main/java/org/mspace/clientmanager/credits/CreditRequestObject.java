/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.mspace.clientmanager.credits;

import java.sql.Connection;

/**
 *
 * @author developer
 */
public class CreditRequestObject {
    int smsCredits;
    int creditsToManage;
    int current;
    int adminv;
    Connection connection;
    String username;
    String agent;

    private CreditRequestObject(Builder builder) {
        this.adminv=builder.adminv;
        this.current=builder.current;
        this.agent=builder.agent;
        this.connection=builder.connection;
        this.creditsToManage=builder.creditsToManage;
        this.username=builder.username;
        this.smsCredits=builder.smsCredits;
        
        
    }
    

    public int getSmsCredits() {
        return smsCredits;
    }

    public void setSmsCredits(int smsCredits) {
        this.smsCredits = smsCredits;
    }

    public int getCreditsToManage() {
        return creditsToManage;
    }

    public void setCreditsToManage(int creditsToManage) {
        this.creditsToManage = creditsToManage;
    }

    public int getCurrent() {
        return current;
    }

    public void setCurrent(int current) {
        this.current = current;
    }

    public int getAdminv() {
        return adminv;
    }

    public void setAdminv(int adminv) {
        this.adminv = adminv;
    }

    public Connection getConnection() {
        return connection;
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getAgent() {
        return agent;
    }

    public void setAgent(String agent) {
        this.agent = agent;
    }
    
    public static class Builder{
    int smsCredits;
    int creditsToManage;
    int current;
    int adminv;
    Connection connection;
    String username;
    String agent;
    
    public Builder setSMSCredits(int smsCredits){
        this.smsCredits=smsCredits;
        return this;
    }
      public Builder setCreditsToManage(int creditsToManage){
        this.creditsToManage=creditsToManage;
        return this;
    }
        public Builder setCurrent(int current){
        this.current=current;
        return this;
    }
          public Builder setAdminv(int adminv){
        this.adminv=adminv;
        return this;
    }
      public Builder setConnection(Connection connection){
        this.connection=connection;
        return this;
    }
        public Builder setsername(String username){
        this.username=username;
        return this;
    }
          public Builder setAgent(String agent){
        this.agent=agent;
        return this;
    }
            public CreditRequestObject build(){
       
        return new CreditRequestObject(this);
    }
        
    }
    
    
    
    
    
}
