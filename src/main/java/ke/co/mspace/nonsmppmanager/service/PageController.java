/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ke.co.mspace.nonsmppmanager.service;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import ke.co.mspace.nonsmppmanager.util.HikariJDBCDataSource;
import ke.co.mspace.nonsmppmanager.util.JdbcUtil;

/**
 *
 * @author Norrey Osako
 */
public class PageController {

    /**
     * Creates a new instance of PageController
     */
    public PageController() {
    }

    public String alreadyLoggedIn() {
        return "loggedin";
    }
    
    public String loadScript(String username,String password){
         JdbcUtil util = new JdbcUtil();

        String sql = "SELECT * FROM tUSER WHERE username='" + username + "' AND password='" + password + "' AND (admin=1 or admin=5)";
        UserScroller us= new UserScroller();
        Connection conn=null;
        try {
             Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            conn = HikariJDBCDataSource.getConnectionTodbSMS();
        } catch (SQLException ex) {
            Logger.getLogger(PageController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return "";

           
       
    }
}
