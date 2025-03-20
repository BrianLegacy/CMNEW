/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.mspace.clientmanager.sms;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.model.SelectItem;
import javax.servlet.http.HttpSession;
import ke.co.mspace.nonsmppmanager.invalids.getsession;
import org.mspace.clientmanager.user.UserController;
import ke.co.mspace.nonsmppmanager.util.JdbcUtil;
import ke.co.mspace.nonsmppmanager.util.PasswordUtil;

/**
 *
 * @author olal
 */
public class SmsDAOImpl implements SmsDAO {

    HttpSession session = getsession.getSession();
    char admin = (Character) session.getAttribute("taskAdmin");
    Long agent = (Long) session.getAttribute("id");

    private static final Logger LOGGER = Logger.getLogger(SmsDAOImpl.class.getName());
    private JdbcUtil jdbcUtil = new JdbcUtil();

    @Override
    public void createSmsUser(UserController user) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public List<SelectItem> smsUsers() {
        List<SelectItem> users = new ArrayList<>();
        String queryAdmin = "select username from tUSER where admin != 5 and smsuser = 'Y' order by username";
        String queryRes = "select username from tUSER where agent = ? and smsuser='Y' and admin != 5 order by username";

        try (Connection conn = jdbcUtil.getConnectionTodbSMS(); PreparedStatement ps = admin == 'Y' ? conn.prepareStatement(queryAdmin) : conn.prepareStatement(queryRes)) {
            if (admin != 'Y') {
                ps.setLong(1, agent);
            }
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                users.add(new SelectItem(rs.getString("username")));
            }
        } catch (SQLException e) {
            // Handle exceptions
            LOGGER.log(Level.SEVERE, "Error fetching usernames", e);
        }
        return users;
    }

    @Override
    public List<UserController> fetchSmsusers(int limit, int offset, String usernameFilter) {

        String adminQuery = "SELECT dbSMS.tUSER.id, dbSMS.tUSER.username, dbSMS.tUSER.password, dbSMS.tUSER.admin, "
                + "dbSMS.tUSER.max_total, dbSMS.tUSER.max_contacts, dbSMS.tUSER.organization, dbSMS.tUSER.contact_number, "
                + "dbSMS.tUSER.email_address, dbSMS.tUSER.enable_email_alert, dbSMS.tUSER.end_date, dbSMS.tUSER.start_date, "
                + "dbSMS.tUSER.alertThreshold, dbSMS.tUSER.cost_per_sms, dbSMS.tUSER.arrears, dbSMS.tUSER.group "
                + "FROM dbSMS.tUSER "
                + "WHERE dbSMS.tUSER.admin != '5' AND dbSMS.tUSER.smsuser = 'Y' AND dbSMS.tUSER.username LIKE ? LIMIT ? OFFSET ?";

        // Reseller query string
        String resellerQuery = "SELECT dbSMS.tUSER.id, dbSMS.tUSER.username, dbSMS.tUSER.password, dbSMS.tUSER.admin, "
                + "dbSMS.tUSER.max_total, dbSMS.tUSER.max_contacts, dbSMS.tUSER.organization, dbSMS.tUSER.contact_number, "
                + "dbSMS.tUSER.email_address, dbSMS.tUSER.enable_email_alert, dbSMS.tUSER.end_date, dbSMS.tUSER.start_date, "
                + "dbSMS.tUSER.alertThreshold, dbSMS.tUSER.cost_per_sms, dbSMS.tUSER.arrears, dbSMS.tUSER.group "
                + "FROM dbSMS.tUSER "
                + "WHERE dbSMS.tUSER.agent = ? AND dbSMS.tUSER.admin = '3' AND dbSMS.tUSER.smsuser = 'Y' LIMIT ? OFFSET ?";
        List<UserController> smsUsers = new ArrayList<>();

        String filterValue = (usernameFilter != null && !usernameFilter.trim().isEmpty() ? "%" + usernameFilter + "%" : "%");

        Connection connection = jdbcUtil.getConnectionTodbSMS();

        try (PreparedStatement ps = admin == 'Y' ? connection.prepareStatement(adminQuery) : connection.prepareStatement(resellerQuery)) {

            if (admin != 'Y') {
                ps.setLong(1, agent);
                ps.setInt(2, limit);
                ps.setInt(3, offset);
            } else {
                ps.setString(1, filterValue);
                ps.setInt(2, limit);
                ps.setInt(3, offset);
            }
            LOGGER.log(Level.INFO, "Executing SQL query");

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    UserController aUser = new UserController();
                    aUser.setId(rs.getLong("id"));
                    aUser.setUsername(rs.getString("username"));
                    aUser.setPassword(rs.getString("password"));
                    aUser.setAdmin(rs.getString("admin").charAt(0));

                    String ad = "" + aUser.getAdmin();
                    if (ad != null && !ad.trim().isEmpty()) {
                        if (ad.equals("4")) {
                            aUser.setUserType("Yes");
                            aUser.setCreateAccount(true);
                        } else {
                            aUser.setUserType("No");
                            aUser.setCreateAccount(false);
                        }
                    }

                    aUser.setSmsCredits(rs.getInt("max_total"));
                    aUser.setOrganization(rs.getString("organization"));
                    aUser.setUserMobile(rs.getString("contact_number"));
                    aUser.setUserEmail(rs.getString("email_address"));
                    aUser.setEnableEmailAlertWhenCreditOver(rs.getBoolean("enable_email_alert"));
                    aUser.setEndDate(rs.getDate("end_date"));
                    aUser.setStartDate(rs.getDate("start_date"));
                    aUser.setAlertThreshold(rs.getInt("alertThreshold"));
                    aUser.setCost_per_sms(rs.getFloat("cost_per_sms"));
                    aUser.setArrears(rs.getInt("arrears"));
                    aUser.setMaxContacts(rs.getInt("max_contacts"));
                    aUser.setGroup(getGroup(rs.getInt("group")));

                    smsUsers.add(aUser);
                }
            }

        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Sql exception while retreiving users");
        }

        return smsUsers;
    }

    public String getGroup(int id) {
        String sql = " select groupname from tGROUPS where id= ?";
        String groupName = null;

        try (Connection connection = jdbcUtil.getConnectionTodbSMS(); PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    groupName = rs.getString("groupname");
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error collecting groups");
        }

        return groupName;
    }

    @Override
    public boolean editSmsUser(UserController user) {
        String sql = "UPDATE tUSER SET "
                + "username= ?, organization=?, contact_number = ?, email_address=?"
                + ", enable_email_alert=?,cost_per_sms=?,arrears=?,alertThreshold=? ,`group` =?  WHERE id=?";
        boolean result = false;
        try (Connection conn = jdbcUtil.getConnectionTodbSMS(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, user.getUsername());
            pstmt.setString(2, user.getOrganization());
            pstmt.setString(3, user.getUserMobile());
            pstmt.setString(4, user.getUserEmail());
            pstmt.setBoolean(5, user.isEnableEmailAlertWhenCreditOver());
            pstmt.setFloat(6, user.getCost_per_sms());
            pstmt.setInt(7, user.getArrears());

            pstmt.setInt(8, user.getAlertThreshold());
            pstmt.setInt(9, user.getGroupId());
            pstmt.setLong(10, user.getId());

            result = pstmt.executeUpdate() > 0;
            LOGGER.log(Level.INFO, "Executed SQL: {0}", sql);
            return result;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "SQL error occurred", e);
        }
        return result;

    }

    @Override
    public List<SelectItem> getAlphas() {
        List<SelectItem> users = new ArrayList<>();
        String queryAdmin = "SELECT short_code, sid_type FROM tSDPNew WHERE short_code_type = 2 ORDER BY short_code ";
        String queryRes = "SELECT t.short_code, t.sid_type FROM tSDPNew t INNER JOIN tUSER u ON t.agent_id = u.id "
                + "WHERE t.short_code_type = 2 AND t.agent_id = ? ORDER BY short_code  ";

        try (Connection conn = jdbcUtil.getConnectionTodbSMS(); PreparedStatement ps = admin == 'Y' ? conn.prepareStatement(queryAdmin) : conn.prepareStatement(queryRes)) {
            if (admin != 'Y') {
                ps.setLong(1, agent);
            }
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                users.add(new SelectItem(rs.getString("short_code")));
            }
        } catch (SQLException e) {
            // Handle exceptions
            LOGGER.log(Level.SEVERE, "Error fetching alphas types", e);
        }
        return users;
    }

    @Override
    public boolean deleteSmsUser(UserController user
    ) {
        String sql = "DELETE from tUSER where username =?";
        boolean result = false;
        try (Connection conn = jdbcUtil.getConnectionTodbSMS(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, user.getUsername());

            // Execute the query
            result = pstmt.executeUpdate() > 0;
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "SQL error occured ", ex);
        }
        return result;
    }

    @Override
    public boolean changePass(String username, String password, Long id) {
        String sql = "update tUSER set password = ? where username= ? AND id = ?";
        boolean result = false;

        try (Connection conn = jdbcUtil.getConnectionTodbSMS(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            String hashedPass = PasswordUtil.encrypt(password);
            pstmt.setString(1, hashedPass);

            pstmt.setString(2, username);
            pstmt.setLong(3, id);

            // Execute the query
            result = pstmt.executeUpdate() == 1;
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "SQL error occured ", ex);
        }
        return result;
    }

    public int countSmsUsers() {
        String query;
        if (admin == 'Y') {
            query = "SELECT COUNT(*) FROM dbSMS.tUSER WHERE dbSMS.tUSER.admin != '5' AND dbSMS.tUSER.smsuser = 'Y'";
        } else {
            query = "SELECT COUNT(*) FROM dbSMS.tUSER WHERE dbSMS.tUSER.agent = '" + agent + "'  AND dbSMS.tUSER.admin = '3' AND dbSMS.tUSER.smsuser = 'Y'";
        }

        System.out.println(query);
        try (Connection connection = jdbcUtil.getConnectionTodbSMS(); PreparedStatement ps = connection.prepareStatement(query); ResultSet rs = ps.executeQuery()) {

            if (rs.next()) {
                return rs.getInt(1);  // Return total record count
            }
        } catch (SQLException e) {
            System.out.println("an error occured in count " + e);
        }

        return 0;
    }
    
    public int countFilteredSmsUsers(String usernameFilter) {
    String sql = "SELECT COUNT(*) FROM dbSMS.tUSER WHERE admin != '5' AND smsuser = 'Y'";

    // Add the filter if the username filter is provided
    if (usernameFilter != null && !usernameFilter.trim().isEmpty()) {
        sql += " AND username LIKE ?";
    }

    int count = 0;  // To store the total count of records

    try (Connection connection = jdbcUtil.getConnectionTodbSMS();
         PreparedStatement ps = connection.prepareStatement(sql)) {

        // If there's a username filter, set it as a parameter in the query
        if (usernameFilter != null && !usernameFilter.trim().isEmpty()) {
            ps.setString(1, "%" + usernameFilter + "%");
        }

        // Execute the query and retrieve the count from the ResultSet
        try (ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                count = rs.getInt(1);  // Get the count from the first column
            }
        }

    } catch (SQLException e) {
        // Handle any SQL exceptions
        LOGGER.log(Level.SEVERE, "Error executing count query", e);
    }

    return count;  // Return the total number of filtered users
}

    @Override
    public List<SelectItem> getExistingUsers(){
        List<SelectItem> users = new ArrayList<>();
        String queryAdmin = "SELECT username FROM tUSER WHERE smsuser= 'N'";
        String queryRes = "SELECT username FROM tUSER WHERE agent = '" + agent + "' AND emailuser= 'N'";
        
        try (Connection conn = jdbcUtil.getConnectionTodbSMS(); PreparedStatement ps = admin == 'Y' ? conn.prepareStatement(queryAdmin) : conn.prepareStatement(queryRes)) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                users.add(new SelectItem(rs.getString("username")));
            }
        } catch (SQLException e) {
            // Handle exceptions
            LOGGER.log(Level.SEVERE, "Error fetching user types", e);
        }
        return users;
    }
    
    @Override
    public boolean addExisting(String username){
        String sql = "UPDATE tUSER SET smsuser = 'Y' WHERE username= ?";
        
         boolean result = false;
        try (Connection conn = jdbcUtil.getConnectionTodbSMS(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, username);

            result = pstmt.executeUpdate() > 0;
            LOGGER.log(Level.INFO, "Executed SQL: {0}", sql);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "SQL error occurred", e);
        }
        return result;
    }

}
