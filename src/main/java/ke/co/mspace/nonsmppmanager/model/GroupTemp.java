/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ke.co.mspace.nonsmppmanager.model;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.faces.context.FacesContext;
import ke.co.mspace.nonsmppmanager.service.AlphaScroller;
import ke.co.mspace.nonsmppmanager.service.AlphaServiceImpl;
import ke.co.mspace.nonsmppmanager.service.EmailUserServiceImpl;
import ke.co.mspace.nonsmppmanager.service.ManageCreditApi;
import ke.co.mspace.nonsmppmanager.service.ManageCreditImpl;
import ke.co.mspace.nonsmppmanager.service.UserScroller;
import ke.co.mspace.nonsmppmanager.service.UserServiceApi;
import ke.co.mspace.nonsmppmanager.util.JdbcUtil;
import ke.co.mspace.nonsmppmanager.util.JsfUtil;

/**
 *
 * @author developer
 */
public class GroupTemp {
private int id;
private String groupname;
private String description;

    public GroupTemp() {
    }

    public GroupTemp(int id, String groupname, String description) {
        this.id = id;
        this.groupname = groupname;
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getGroupname() {
        return groupname;
    }

    public void setGroupname(String groupname) {
        this.groupname = groupname;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
    
     private Connection conn = null;
    final JdbcUtil util = new JdbcUtil();
    
      public void saveUserGroup() throws IOException {
         
        try {
            conn = util.getConnectionTodbSMS();
             String sql = "insert into tGROUPS (groupname,description) values ('"+this.groupname+"','"+this.description+"')";
              System.out.println(sql);
        PreparedStatement pst = conn.prepareStatement(sql);
         pst.execute();
            
             JsfUtil.addSuccessMessage("Group  created successfully.");
            JdbcUtil.closeConnection(conn);
        } catch (SQLException e) {
            JsfUtil.addErrorMessage("Some problem occured.");
            JdbcUtil.closeConnection(conn);
        }
        
    

    }
    
    
    
    
    
    
    
    

    @Override
    public String toString() {
        return "Group{" + "id=" + id + ", groupname=" + groupname + ", description=" + description + '}';
    }
    
    
    
}
