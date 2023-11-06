/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ke.co.mspace.nonsmppmanager.invalids;

import java.io.Serializable;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

/**
 *
 * @author mspace
 */
@ManagedBean(name = "facePainter")
@SessionScoped
public class FacePainter implements Serializable {

    private String mainContent = "clientmanager/manageuserdetails/addsmsuser.xhtml";

    public String getMainContent() {

        return mainContent;
    }

    public void setMainContent(String mainContent) {
        System.out.println("called");
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

}
