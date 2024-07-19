/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.mspace.clientmanager.reseller;

import java.util.List;
import org.mspace.clientmanager.user.UserController;

/**
 *
 * @author olal
 */
public interface ResellerDAO {
    public List<UserController> fetchResellers();
    boolean editReseller(UserController user);
    boolean delReseller(UserController user);
    boolean setImagePath(String username, String picPath);
}
