/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.mspace.clientmanager.group;

import javax.faces.bean.ManagedBean;

/**
 *
 * @author developer
 */
public class Group {

    private int id;
    private String groupname;
    private String description;

    public Group() {

    }

    public Group(int id, String groupname, String description) {
        this.id = id;
        this.groupname = groupname;
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getGroupname() {
        return groupname;
    }

    public void setGroupname(String groupname) {
        this.groupname = groupname;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "Group{" + "id=" + id + ", groupname=" + groupname + ", description=" + description + '}';
    }

}
