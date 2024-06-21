/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.mspace.clientmanager.group;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author developer
 */
public interface GroupDAO {

    public boolean checkIfGroupNameExists(String groupName);

    public void saveUserGroup(Group group);

    public boolean deleteGroup(int id);

    public List<Group> fetchGroups();

    boolean updateGroup(int id, String groupname, String description);

}
