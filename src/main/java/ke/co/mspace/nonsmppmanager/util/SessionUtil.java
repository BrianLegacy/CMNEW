/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ke.co.mspace.nonsmppmanager.util;

import javax.faces.context.FacesContext;
import static ke.co.mspace.nonsmppmanager.model.AuthenticationBean.AUTH_KEY;

/**
 *
 * @author developer
 */
public class SessionUtil {

    public static String getAUTH_KEY(){
       return (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("non.smpp.manager");
    }
    
    public static String getLoggedInUser(){
       return (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("loggedInUser");
    }
       public static Long getLoggedInUserId(){
       return (Long) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("user_id");
    }
    
     public static String getAgent(){
       String agent="";
        if (isReseller()) {
        agent=getLoggedInUserId().toString();
        }
         
        return agent;
    }
    
    public static boolean isReseller() {
        return getAdmin() == '5';
    }
     public static boolean isIAdmin() {
        return getAdmin() == '1';
    }

    public static String getReseller() {
        if (isReseller()) {
            return "none";
        } else {
            return "show";
        }
    }

    public static String getMyAccount() {
        if (isReseller()){
            return "show";
        } else {
            return "none";
        }
    }

    public static String getManageReseller() {
        if (isReseller()) {
            return "none";
        } else {
            return "show";
        }
    }

    public static String getshowCreditHistoryReprt() {
        if (isReseller()) {
            return "show";
        } else {
            return "none";
        }
    }

    public static String getvisual() {
        if (isReseller()) {
            return "resources/js/visualization.js";
        } else {
            return "resources/js/emptyvisual.js";
        }
    }

    public static String getPicLocation() {
        if (isReseller()) {
            return "(String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get(\"myloc\");";
        } else {
            return "resources/images/logo.gif";
        }
    }

    public static char getAdmin() {
       char admin = (char) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("admin");
       
        return admin;
    }

}
