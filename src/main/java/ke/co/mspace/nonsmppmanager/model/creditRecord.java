/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ke.co.mspace.nonsmppmanager.model;

/**
 *
 * @author support
 */
public class creditRecord {
    private String usernC;
   
    
    private  String actionType;
    
    private String actionTime;
    
    private String numCredits;
    
    private String previous_balance;
    
    private String new_balance;
    
    private String creditedBy;
    
    

    public creditRecord(){
        
    }
    
    
    public creditRecord(String usernC, String actionType, String actionTime, String numCredits, String previous_balance, String new_balance) {
        this.usernC = usernC;
        this.actionType = actionType;
        this.actionTime = actionTime;
        this.numCredits = numCredits;
        this.previous_balance = previous_balance;
        this.new_balance = new_balance;
    }

    public String getCreditedBy() {
        return creditedBy;
    }

    public void setCreditedBy(String creditedBy) {
        this.creditedBy = creditedBy;
    }

    
    
    
    
    public String getUsernC() {
        return usernC;
    }

    public void setUsernC(String usernC) {
        this.usernC = usernC;
    }

    public String getActionType() {
        return actionType;
    }

    public void setActionType(String actionType) {
        this.actionType = actionType;
    }

    public String getActionTime() {
        return actionTime;
    }

    public void setActionTime(String actionTime) {
        this.actionTime = actionTime;
    }

    public String getNumCredits() {
        return numCredits;
    }

    public void setNumCredits(String numCredits) {
        this.numCredits = numCredits;
    }

    public String getPrevious_balance() {
        return previous_balance;
    }

    public void setPrevious_balance(String previous_balance) {
        this.previous_balance = previous_balance;
    }

    public String getNew_balance() {
        return new_balance;
    }

    public void setNew_balance(String new_balance) {
        this.new_balance = new_balance;
    }
    
    
    
}
