package org.mspace.clientmanager.group;

import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import ke.co.mspace.nonsmppmanager.invalids.FacePainter;
import ke.co.mspace.nonsmppmanager.util.JsfUtil;

/**
 * GroupController class for managing groups
 */
@ManagedBean(name = "groupcontroller")
@ViewScoped
public class GroupController implements Serializable {

    private static final long serialVersionUID = 1L;

    private GroupDAO groupDAO;
    private List<Group> groups;
    private Group group;
    private Group currentGroupItem;
    private List<SelectItem> listgroups;

    @ManagedProperty(value = "#{facePainter}")
    private FacePainter facePainter;

    @PostConstruct
    public void init() {
        groupDAO = new GroupDAOImpl();
        refreshGroups();
        group = new Group();
        listgroups = groupDAO.listGroups();
    }

    public List<Group> getGroups() {
        return groups;
    }

    public List<SelectItem> getListgroups() {
        return listgroups;
    }

    public void setListgroups(List<SelectItem> listgroups) {
        this.listgroups = listgroups;
    }

    public Group getCurrentGroupItem() {
        return currentGroupItem;
    }

    public void setCurrentGroupItem(Group currentGroupItem) {
        this.currentGroupItem = currentGroupItem;
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

    public boolean checkIfGroupNameExists(String groupName) {
        return groupDAO.checkIfGroupNameExists(groupName);
    }

    public void saveUserGroup() {
        groupDAO.saveUserGroup(group);
        refreshGroups();

        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Success", "Group created successfully."));
        group = new Group();
        facePainter.setMainContent("clientmanager/groups/viewgroups.xhtml");

    }

    public void updateUserGroup() {
        if (currentGroupItem != null) {
            groupDAO.updateGroup(currentGroupItem.getId(), currentGroupItem.getGroupname(), currentGroupItem.getDescription());
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Success", "Group updated successfully."));
            refreshGroups();
        } else {
            JsfUtil.addErrorMessage("No group selected for update.");
        }
    }

    public void deleteUserGroup(int id) {
        if (groupDAO.deleteGroup(id)) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Success", "Group deleted successfully."));
            refreshGroups();
        } else {
            JsfUtil.addErrorMessage("Error deleting group.");
        }
    }

    private void refreshGroups() {
        groups = groupDAO.fetchGroups();
    }

    public void resetGroup() {
        group = new Group(); // Reset the group instance
    }
}
