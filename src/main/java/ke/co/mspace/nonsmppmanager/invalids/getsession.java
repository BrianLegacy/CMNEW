/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ke.co.mspace.nonsmppmanager.invalids;


import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 *
 * @author mspace
 */
public class getsession {
    public static HttpSession getSession(){
        return (HttpSession)FacesContext.getCurrentInstance().getExternalContext().getSession(false);
        
    }
    public static HttpServletRequest getrequest(){
    return (HttpServletRequest)FacesContext.getCurrentInstance().getExternalContext().getRequest();
    }
    
}

