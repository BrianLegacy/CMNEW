package org.mspace.clientmanager.group;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.model.SelectItem;
import ke.co.mspace.nonsmppmanager.util.JdbcUtil;
import ke.co.mspace.nonsmppmanager.util.JsfUtil;

/**
 * GroupDAOImpl class for managing group database operations.
 */
public class GroupDAOImpl implements GroupDAO {

    private static final Logger LOGGER = Logger.getLogger(GroupDAOImpl.class.getName());
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
            String sql = "SELECT COUNT(*) FROM dbSMS.tGROUPS tg WHERE tg.groupname = ?";
            PreparedStatement pst = conn.prepareStatement(sql);
            pst.setString(1, groupName);
            ResultSet rs = pst.executeQuery();
            rs.next();
            int count = rs.getInt(1);
            result = count > 0;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "SQL error occurred", e);
        } finally {
            JdbcUtil.closeConnection(conn);
        }
        return result;
    }

    @Override
    public void saveUserGroup(Group group) {
        String sql = "INSERT INTO tGROUPS (groupname, description) VALUES (?, ?)";
        try (Connection conn = util.getConnectionTodbSMS(); PreparedStatement pst = conn.prepareStatement(sql)) {
            pst.setString(1, group.getGroupname());
            pst.setString(2, group.getDescription());
            pst.executeUpdate();
            LOGGER.log(Level.INFO, "Executed SQL: {0}", sql);
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "SQL error occurred", e);
            JsfUtil.addErrorMessage("Some problem occurred.");
        }
    }

    @Override
    public boolean deleteGroup(int id) {
        String sql = "DELETE FROM tGROUPS WHERE id = ?";
        boolean result = false;
        try (Connection conn = util.getConnectionTodbSMS(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            result = ps.executeUpdate() > 0;
            LOGGER.log(Level.INFO, sql);
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "SQL error occurred", e);
        }
        return result;
    }

    @Override
    public List<Group> fetchGroups() {
        String sql = "SELECT * FROM dbSMS.tGROUPS";
        List<Group> groups = new ArrayList<>();
        try (Connection conn = util.getConnectionTodbSMS(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Group group = new Group();
                group.setId(rs.getInt("id"));
                group.setGroupname(rs.getString("groupname"));
                group.setDescription(rs.getString("description"));
                groups.add(group);
            }
            LOGGER.log(Level.INFO, sql);
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "SQL error occurred", e);
        }
        return groups;
    }

    @Override
    public boolean updateGroup(int id, String groupname, String description) {
        String sql = "UPDATE tGROUPS SET groupname = ?, description = ? WHERE id = ?";
        boolean result = false;
        try (Connection conn = util.getConnectionTodbSMS(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, groupname);
            ps.setString(2, description);
            ps.setInt(3, id);
            result = ps.executeUpdate() > 0;
            LOGGER.log(Level.INFO, sql);
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "SQL error occurred", e);
        }
        return result;
    }

    @Override
    public List<SelectItem> listGroups() {
        String sql = "select id , groupname from tGROUPS order by groupname";
        
        List<SelectItem> results = new ArrayList<>();
        
        try (Connection conn = util.getConnectionTodbSMS(); PreparedStatement ps = conn.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()){
            while(rs.next()){
                results.add(new SelectItem(rs.getInt("id"), rs.getString("groupname")));
            }
        }catch(SQLException ex){
            LOGGER.log(Level.SEVERE, sql);
        }
        return results;
    }
}
