/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.mspace.clientmanager.group;

/**
 *
 * @author developer
 */
public interface GroupDAO {
    public boolean checkIfGroupNameExists(String groupName);
       public void saveUserGroup(String groupname, String description) ;
    
    
}
