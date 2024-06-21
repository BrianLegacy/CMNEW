/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.mspace.clientmanager.sms;

import java.util.List;
import org.mspace.clientmanager.user.UserController;

/**
 *
 * @author olal
 */
public interface SmsDAO {

    void createSmsUser(UserController user);

    List<UserController> fetchSmsusers();

    boolean deleteUser();

    boolean editSmsUser();

    boolean manageCredit();

}
