/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ke.co.mspace.nonsmppmanager.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import ke.co.mspace.nonsmppmanager.util.JdbcUtil;
import ke.co.mspace.nonsmppmanager.util.JsfUtil;
import org.mspace.clientmanager.group.Group;

/**
 *
 * @author developer
 */
@ManagedBean
@ViewScoped
public class GroupService {
     final JdbcUtil util = new JdbcUtil();
         public Group currentGroupItem1=new Group();
        ArrayList<Group> groups;
         Connection conn = null;
        
       
        
         @PostConstruct
            public void init(){
           fetchGroups();
            }

    public void setGroups(ArrayList<Group> groups) {
        this.groups = groups;
    }
    
    
    
       public ArrayList<Group> getGroups() throws SQLException {
     
        return  groups;
    }
       
       public void fetchGroups(){
           groups=UserServiceImpl.getGroups(util.getConnectionTodbSMS());
       }
           public Group getCurrentGroupItem1() {
        return currentGroupItem1;
    }

    public void setCurrentGroupItem1(Group currentGroupItem1) {
        LocalDateTime start=LocalDateTime.now();
        this.currentGroupItem1 = currentGroupItem1;
        System.out.println(currentGroupItem1);
        JsfUtil.printTimeDiff(start, LocalDateTime.now());
    }
    
      public void saveGroup() {
              System.out.println("joseph testing"+currentGroupItem1);
              if(currentGroupItem1.getGroupname().equalsIgnoreCase("")){
                  System.out.println("am empty");
                  return;
              }
        try {
            this.conn = this.util.getConnectionTodbSMS();
            String sql = "update tGROUPS set groupname = ?,description=? where id =?";
            PreparedStatement ps = this.conn.prepareStatement(sql);
            ps.setInt(3, currentGroupItem1.getId());
            ps.setString(1, currentGroupItem1.getGroupname());
            ps.setString(2, currentGroupItem1.getDescription());
            ps.executeUpdate();
            ps.close();
            conn.close();
            JsfUtil.addSuccessMessage("Group updated successfully");
//                this.allcallbacks= null;
//                getAllCallBacks();
            JdbcUtil.closeConnection(this.conn);
        } catch (SQLException e) {
            JdbcUtil.printSQLException(e);
        }
//        currentGroupItem1.setGroupname("");
    }
      
           public void deleteGroup(int id) {

        try {
             conn = util.getConnectionTodbSMS();;
             String sql = "DELETE from tGROUPS where id=?";
            PreparedStatement psmt = conn.prepareStatement(sql);
            psmt.setInt(1, id);
          //  psmt.setString(2, userid);
            psmt.executeUpdate();
            psmt.close();
            conn.close();
            JsfUtil.addSuccessMessage("Group deleted successfully.");
            System.out.println("Deleted "+currentGroupItem1.getGroupname()+" successfully");
            JdbcUtil.closeConnection(this.conn);
        } catch (SQLException ex) {
            ex.printStackTrace();
            JdbcUtil.printSQLException(ex);
        }
    }

       
}
