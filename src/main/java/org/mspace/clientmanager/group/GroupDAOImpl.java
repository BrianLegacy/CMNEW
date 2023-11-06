/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.mspace.clientmanager.group;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import ke.co.mspace.nonsmppmanager.util.JdbcUtil;
import ke.co.mspace.nonsmppmanager.util.JsfUtil;

/**
 *
 * @author developer
 */
public class GroupDAOImpl implements GroupDAO {

    private final JdbcUtil util;

    public GroupDAOImpl() {
        this.util = new JdbcUtil();
    }
    
    

    @Override
    public boolean checkIfGroupNameExists(String groupName) {
        Connection conn = null;
        boolean result = false;
        try {
            conn = util.getConnectionTodbSMS();
            String sql = "select COUNT(*)  from dbSMS.tGROUPS tg where tg.groupname = ?";

            PreparedStatement pst = conn.prepareStatement(sql);
            pst.setString(1, groupName);
            final ResultSet rs = pst.executeQuery();
            rs.next();
            int count = rs.getInt(1);

            result = count > 0;

            JdbcUtil.closeConnection(conn);

        } catch (SQLException e) {
            e.printStackTrace();
            JdbcUtil.closeConnection(conn);
        }
        return result;
    }

    @Override
    public void saveUserGroup(String groupname, String description) {
        Connection conn = null;
        try {
            conn = util.getConnectionTodbSMS();
            String sql = "insert into tGROUPS (groupname,description) values ('" + groupname + "','" + description + "')";
          
            PreparedStatement pst = conn.prepareStatement(sql);
            pst.execute();

            JsfUtil.addSuccessMessage("Group  created successfully.");
            JdbcUtil.closeConnection(conn);
        } catch (SQLException e) {
            e.printStackTrace();
            JsfUtil.addErrorMessage("Some problem occured.");
            JdbcUtil.closeConnection(conn);
        }

    }

}
