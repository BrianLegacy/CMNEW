/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ke.co.mspace.nonsmppmanager.model;

import java.io.File;
import java.io.IOException;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
//import javax.xml.parsers.DocumentBuilder;
//import javax.xml.parsers.DocumentBuilderFactory;
//import org.w3c.dom.Document;
//import org.w3c.dom.Element;


/**
 *
 * @author Norrey Osako
 */
public class PanelMenuBean {
    
    
    private String current;
    private String currentPage="/manager/newUser.jsp";
    File file=new File("/manager/newUser.jsp");
    
    private String viewagents="manager/viewUserperAgent.jsp";

    public String getViewagents() {
        return viewagents;
    }

    public void setViewagents(String viewagents) {
        this.viewagents = viewagents;
    }
     public void reload() throws IOException {
//        ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
//    ec.redirect(((HttpServletRequest) ec.getRequest()).getRequestURI());
    }

    public String getCurrentPage() {
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("lastuser");
        System.out.println("attempting set");
        return currentPage;
    }

    public void setCurrentPage(String currentPage) {
        this.currentPage = currentPage;
    }
    public String getEmailUserCurrentPage() {
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("lastuser");
        return currentPage;
    }

    public void setEmailUserCurrentPage(String currentPage) {
        this.currentPage = currentPage;
    }
    

    public String getCurrent() {
        return current;
    }

    public void setCurrent(String current) {
        this.current = current;
    }
    
    
    
    public String putcurrent(){
    
        System.out.println(current);
        
        return "addUser";
    }

    
}
