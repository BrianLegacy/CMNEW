/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.mspace.clientmanager.sms;

import java.util.List;
import javax.faces.model.SelectItem;
import org.mspace.clientmanager.user.UserController;

/**
 *
 * @author olal
 */
public interface SmsDAO {

    List<SelectItem> smsUsers();
    
    List<SelectItem> getAlphas();
    
    void createSmsUser(UserController user);

    List<UserController> fetchSmsusers();

    boolean deleteSmsUser(UserController user);

    boolean editSmsUser(UserController user);
    
    boolean changePass(String username, String password);


}
