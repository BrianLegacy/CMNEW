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
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import ke.co.mspace.nonsmppmanager.util.JdbcUtil;
import ke.co.mspace.nonsmppmanager.util.JsfUtil;
import org.mspace.clientmanager.group.Group;

/**
 *
 * @author developer
 */
@ManagedBean
@RequestScoped
public class GroupService {

    private static final Logger LOGGER = Logger.getLogger(GroupService.class.getName());

    private final JdbcUtil util = new JdbcUtil();
    private Group currentGroupItem1 = new Group();
    private ArrayList<Group> groups;

    @PostConstruct
    public void init() {
        fetchGroups();
    }

    public void setGroups(ArrayList<Group> groups) {
        this.groups = groups;
    }

    public ArrayList<Group> getGroups() {
        return groups;
    }

    public void fetchGroups() {
        groups = UserServiceImpl.getGroups(util.getConnectionTodbSMS());
    }

    public Group getCurrentGroupItem1() {
        return currentGroupItem1;
    }

    public void setCurrentGroupItem1(Group currentGroupItem1) {
        LocalDateTime start = LocalDateTime.now();
        this.currentGroupItem1 = currentGroupItem1;
        JsfUtil.printTimeDiff(start, LocalDateTime.now());
    }

    public void saveGroup() {
        if (currentGroupItem1.getGroupname().isEmpty()) {
            return;
        }
        String sql = "UPDATE tGROUPS SET groupname = ?, description = ? WHERE id = ?";
        try (Connection conn = util.getConnectionTodbSMS(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, currentGroupItem1.getGroupname());
            ps.setString(2, currentGroupItem1.getDescription());
            ps.setInt(3, currentGroupItem1.getId());
            ps.executeUpdate();
            System.out.println("current item ############" + currentGroupItem1.getGroupname());
            JsfUtil.addSuccessMessage("Group updated successfully");
            LOGGER.log(Level.INFO, sql);
            fetchGroups();
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error updating group", e);
            JdbcUtil.printSQLException(e);
        }
    }

    public void deleteGroup(int id) {
        String sql = "DELETE FROM tGROUPS WHERE id = ?";
        try (Connection conn = util.getConnectionTodbSMS(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
            LOGGER.log(Level.INFO, sql);
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error deleting group", e);
            JdbcUtil.printSQLException(e);
        }
    }
}
