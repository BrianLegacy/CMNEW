/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.mspace.clientmanager.email;

import java.util.List;
import javax.faces.model.SelectItem;
import org.mspace.clientmanager.user.UserController;

/**
 *
 * @author olal
 */
public interface EmailDAO {
    
    List<SelectItem> emailUsers();
    
    boolean changePass(String username,String newPassword, Long id);
    
    public List<UserController> fetchEmailUsers();
    
    boolean editEmailUser(UserController user);
    
    boolean deleteEmailUser(UserController user);
    
    List<SelectItem> getExistingUsers();
    
    boolean addExisting (String username);
    
}
