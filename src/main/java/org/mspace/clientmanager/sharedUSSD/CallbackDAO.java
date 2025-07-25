/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.mspace.clientmanager.sharedUSSD;

import java.util.List;

/**
 *
 * @author olal
 */
public interface CallbackDAO {

    boolean createCallback(CallbackModel callback);

    List<CallbackModel> fetchCallbacks();

    boolean editCallback(CallbackModel callback);

    boolean deleteCallback(CallbackModel callback);

}
