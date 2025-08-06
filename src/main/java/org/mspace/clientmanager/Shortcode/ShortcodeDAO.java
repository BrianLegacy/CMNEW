/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.mspace.clientmanager.Shortcode;

/**
 *
 * @author brian
 */

import java.util.List;

public interface ShortcodeDAO {

    boolean createShortcode(ShortcodeModel shortcode);

    List<ShortcodeModel> fetchShortcodes();
    List<ShortcodeModel> fetchShortcodesWithNullKeyword();


    boolean editShortcode(ShortcodeModel shortcode);

    boolean deleteShortcode(ShortcodeModel shortcode);

}
