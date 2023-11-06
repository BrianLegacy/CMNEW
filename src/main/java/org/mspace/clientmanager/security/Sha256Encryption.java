/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.mspace.clientmanager.security;

import java.security.NoSuchAlgorithmException;

/**
 *
 * @author developer
 */

public class Sha256Encryption implements Encryption {

    @Override
    public String encrypt(String plainText) {
//        if(plainText.isEmpty()) return "";
//        String ret = "";
//        try {
//            java.security.MessageDigest digest = java.security.MessageDigest.getInstance("SHA-256");
////            digest.update("salted".getBytes());
//            byte[] hash = digest.digest(plainText.getBytes(java.nio.charset.StandardCharsets.UTF_8));
//            StringBuffer hexString = new StringBuffer();
//
//            for (int i = 0; i < hash.length; i++) {
//                String hex = Integer.toHexString(0xff & hash[i]);
//                if (hex.length() == 1) {
//                    hexString.append('0');
//                }
//                hexString.append(hex);
//            }
//            String sha = hexString.toString();
//            ret = sha;
////            ret = javax.xml.bind.DatatypeConverter.printBase64Binary(sha.getBytes());
////            System.out.println("Encrypted " + text + " to Sha-256: " + sha);// + " EncodeBase64: " + ret);
//        } catch (NoSuchAlgorithmException e) {
//            e.printStackTrace();
//        }
//        return ret;
return plainText;
    }

    @Override
    public boolean compare(String encryptedPassword, String plainPassword) {
        boolean result=false;
        System.out.println("Suggested Password :" + encrypt(plainPassword));
        System.out.println("Password from db :" + encryptedPassword);
        if (encryptedPassword.equals(encrypt(plainPassword))) {

            result=true;
        }
        return result;
    }

}
