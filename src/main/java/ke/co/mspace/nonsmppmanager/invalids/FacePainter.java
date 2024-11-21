/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ke.co.mspace.nonsmppmanager.invalids;

import java.io.Serializable;
import javax.annotation.PostConstruct;
import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author mspace
 */

@ManagedBean(name = "facePainter")
@ApplicationScoped
public class FacePainter implements Serializable {

    private String mainContent = "clientmanager/manageuserdetails/showsmsusers.xhtml";
    
    public String getMainContent() {

        return mainContent;
    }

    public void setMainContent(String mainContent) {
//        updater k = new updater();
        try {
//            k.getSmsBalance();
        } catch (Exception ex) {
            System.out.println("getting balance error");
        }
        this.mainContent = mainContent;
    }

    public void setMainContent2(String mainContent) {
        try {

            this.mainContent = mainContent;
        } catch (Exception ex) {
        }
    }
    
    public void resetMainContent() {
        this.mainContent = "clientmanager/manageuserdetails/showsmsusers.xhtml";
    }

}


