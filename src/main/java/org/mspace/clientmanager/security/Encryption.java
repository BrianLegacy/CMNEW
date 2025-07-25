/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.mspace.clientmanager.security;

/**
 *
 * @author developer
 */
public interface Encryption {
    public  String encrypt(String plainText);
    public boolean compare(String encryptedPassword, String plainPassword);
    
}
