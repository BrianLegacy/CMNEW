/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ke.co.mspace.nonsmppmanager.model;

/**
 *
 * @author Samson
 */
public class Alpnumeric {
    
    private String alphanumeric;
    private  String sid_type;
    private String userName;
    private Long id;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    
    

    public String getAlphanumeric() {
        return alphanumeric;
    }

    public void setAlphanumeric(String alphanumeric) {
        this.alphanumeric = alphanumeric;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
    

   

    public String getSid_type() {
        return sid_type;
    }

    public void setSid_type(String sid_type) {
        this.sid_type = sid_type;
    }
    
    
    
    
}
