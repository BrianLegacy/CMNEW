/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ke.co.mspace.nonsmppmanager.model;

/**
 *
 * @author sofwaredeveloper
 */
public class tclientModel {
    public int id;
    public String clientName;
    public String email;
    public String picPath;
    public String systType;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPicPath() {
        return picPath;
    }

    public void setPicPath(String picPath) {
        this.picPath = picPath;
    }

    public String getSystType() {
        return systType;
    }

    public void setSystType(String systType) {
        this.systType = systType;
    }
    

    public tclientModel() {
    }
    
}
