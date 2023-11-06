/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.mspace.clientmanager.util;


import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author developer
 */
public class SecurityUtil {
    
    
   
    public static boolean compare(String encryptedPassword, String plainPassword){
        System.out.println("encrypt :"+encrypt(plainPassword));
         System.out.println("db :"+encryptedPassword);
       if(encryptedPassword.equals(encrypt(plainPassword))){
         
           return true;
       }
       return false;
    }
  
    
       public static String encrypt(String text) {
        String ret = "";
        try {
            java.security.MessageDigest digest = java.security.MessageDigest.getInstance("SHA-256");
//            digest.update("salted".getBytes());
            byte[] hash = digest.digest(text.getBytes(java.nio.charset.StandardCharsets.UTF_8));
            StringBuffer hexString = new StringBuffer();

            for (int i = 0; i < hash.length; i++) {
                String hex = Integer.toHexString(0xff & hash[i]);
                if (hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }
            String sha = hexString.toString(); 
            ret = sha;
//            ret = javax.xml.bind.DatatypeConverter.printBase64Binary(sha.getBytes());
//            System.out.println("Encrypted " + text + " to Sha-256: " + sha);// + " EncodeBase64: " + ret);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return ret;
    }
       
       public static Connection getConnection(){
        try {
            Class.forName(
                    "com.mysql.jdbc.Driver");
            return DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/dbSMS", "mysql", "mysql123");
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(SecurityUtil.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(SecurityUtil.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
           
       }
    
}
