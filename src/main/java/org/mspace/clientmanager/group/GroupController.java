/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.mspace.clientmanager.group;

import ke.co.mspace.nonsmppmanager.invalids.FacePainter;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import org.mspace.clientmanager.logger.MyLogger;

/**
 *
 * @author developer
 */
@ManagedBean(name="groupcontroller")
@ViewScoped
public class GroupController {

    GroupDAO groupDAO;
    MyLogger logger;
      @ManagedProperty(value = "#{group}")
    public Group group;
      @ManagedProperty(value = "#{facePainter}")
    public FacePainter facePainter;
    public GroupController() {
        groupDAO=new GroupDAOImpl();
        logger=new MyLogger(GroupController.class);
    }
    
    public boolean checkIfGroupNameExists(String groupName){
        
        return groupDAO.checkIfGroupNameExists(groupName);
    }
    
    public void saveUserGroup() {
        groupDAO.saveUserGroup(group.getGroupname(), group.getDescription());
        group.setDescription("");
        group.setGroupname("");
        facePainter.setMainContent("clientmanager/groups/viewgroups.xhtml");
    }
    

    public Group getGroup() {
        return group;
    }

    public void setGroup(Group group) {
        this.group = group;
    }

    public FacePainter getFacePainter() {
        return facePainter;
    }

    public void setFacePainter(FacePainter facePainter) {
        this.facePainter = facePainter;
    }
    
    
    
}
