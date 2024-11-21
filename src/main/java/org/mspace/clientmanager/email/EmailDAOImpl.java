/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.mspace.clientmanager.email;

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
import ke.co.mspace.nonsmppmanager.util.HikariJDBCDataSource;
import ke.co.mspace.nonsmppmanager.util.JdbcUtil;
import ke.co.mspace.nonsmppmanager.util.PasswordUtil;
import org.mspace.clientmanager.user.UserController;

/**
 *
 * @author olal
 */
public class EmailDAOImpl implements EmailDAO {

    HttpSession session = getsession.getSession();
    char admin = (Character) session.getAttribute("taskAdmin");
    Long agent = (Long) session.getAttribute("id");

    private static final Logger LOGGER = Logger.getLogger(EmailDAOImpl.class.getName());
//    private final JdbcUtil jdbcUtil = new JdbcUtil();

    @Override
    public List<SelectItem> getExistingUsers() {
        List<SelectItem> users = new ArrayList<>();
        String queryAdmin = "SELECT username FROM tUSER WHERE emailuser= 'N'";
        String queryRes = "SELECT username FROM tUSER WHERE agent = '" + agent + "' AND emailuser= 'N'";

        try (Connection conn = HikariJDBCDataSource.getConnectionTodbSMS();) {
            PreparedStatement ps = admin == 'Y' ? conn.prepareStatement(queryAdmin) : conn.prepareStatement(queryRes);
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
    public List<SelectItem> emailUsers() {
        List<SelectItem> users = new ArrayList<>();
        String queryAdmin = "select username from tUSER where admin != 5 and emailuser = 'Y' order by username";
        String queryRes = "select username from tUSER where agent = ? and emailuser='Y' and admin != 5 order by username";

        try (Connection conn = HikariJDBCDataSource.getConnectionTodbSMS(); PreparedStatement ps = admin == 'Y' ? conn.prepareStatement(queryAdmin) : conn.prepareStatement(queryRes)) {
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
    public List<UserController> fetchEmailUsers() {

        String adminQuery = "SELECT tUSER.id, tUSER.username, tUSER.password, tUSER.admin, tUSER.max_total, tUSER.max_contacts, "
                + "tUSER.organization, tUSER.contact_number, tUSER.email_address, tUSER.enable_email_alert, "
                + "tUSER.end_date, tUSER.start_date, tUSER.alertThreshold, tUSER.cost_per_sms, tUSER.arrears, tUSER.group "
                + "FROM tUSER "
                + "WHERE tUSER.emailuser = 'Y' AND tUSER.admin != 5 LIMIT 50000";

        String resellerQuery = "SELECT tUSER.id, tUSER.username, tUSER.password, tUSER.admin, tUSER.max_total, tUSER.max_contacts, "
                + "tUSER.organization, tUSER.contact_number, tUSER.email_address, tUSER.enable_email_alert, "
                + "tUSER.end_date, tUSER.start_date, tUSER.alertThreshold, tUSER.cost_per_sms, tUSER.arrears, tUSER.group "
                + "FROM tUSER "
                + "WHERE tUSER.emailuser = 'Y' AND tUSER.agent = ? AND tUSER.admin != 5";

        List<UserController> emailUsers = new ArrayList<>();

        try (Connection connection = HikariJDBCDataSource.getConnectionTodbSMS(); PreparedStatement ps = admin == 'Y' ? connection.prepareStatement(adminQuery) : connection.prepareStatement(resellerQuery)) {
            if (admin != 'Y') {
                ps.setLong(1, agent);
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
                    if (ad.trim() != null) {
                        if (ad.equalsIgnoreCase("4")) {
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
                    emailUsers.add(aUser);
                }

            }
            LOGGER.log(Level.INFO, "Success");
        } catch (Exception e) {

            LOGGER.log(Level.SEVERE, "Error while fetching users", e);
        }
        return emailUsers;
    }

    public String getGroup(int id) {
        String sql = " select groupname from tGROUPS where id= ?";
        String groupName = null;

        try (Connection connection = HikariJDBCDataSource.getConnectionTodbSMS(); PreparedStatement ps = connection.prepareStatement(sql)) {
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
    public boolean addExisting(String username) {
        String sql = "UPDATE tUSER SET emailuser = 'Y' WHERE username= ? ";

        boolean result = false;
        try (Connection conn = HikariJDBCDataSource.getConnectionTodbSMS(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, username);

            result = pstmt.executeUpdate() > 0;
            LOGGER.log(Level.INFO, "Executed SQL: {0}", sql);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "SQL error occurred", e);
        }
        return result;
    }

    @Override
    public boolean changePass(String username, String password, Long id) {
        String sql = "update tUSER set password = ? where username= ? AND id = ?";
        boolean result = false;

        try (Connection conn = HikariJDBCDataSource.getConnectionTodbSMS(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            String hashedPass = PasswordUtil.encrypt(password);
            pstmt.setString(1, hashedPass);

            pstmt.setString(2, username);
            pstmt.setLong(3, id);

            System.out.println("hashedPassword is " + hashedPass);

            // Execute the query
            result = pstmt.executeUpdate() == 1;
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "SQL error occured ", ex);
        }
        return result;
    }

    @Override
    public boolean editEmailUser(UserController user) {
        String sql = "UPDATE tUSER SET username = ?, max_total = ?, organization = ?, "
                + "contact_number = ?, email_address = ?, enable_email_alert = ?, cost_per_sms = ?, arrears = ?, "
                + "alertThreshold = ?, `group` = ?  WHERE id = ?";

        boolean result = false;
        try (Connection conn = HikariJDBCDataSource.getConnectionTodbSMS(); PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, user.getUsername());
            pstmt.setLong(2, user.getSmsCredits());
            pstmt.setString(3, user.getOrganization());
            pstmt.setString(4, user.getUserMobile());
            pstmt.setString(5, user.getUserEmail());
            pstmt.setBoolean(6, user.isEnableEmailAlertWhenCreditOver());
            pstmt.setFloat(7, user.getCost_per_sms());
            pstmt.setInt(8, user.getArrears());
            pstmt.setInt(9, user.getAlertThreshold());
            pstmt.setInt(10, user.getGroupId());
            pstmt.setLong(11, user.getId());

            result = pstmt.executeUpdate() > 0;
            LOGGER.log(Level.INFO, "Executed SQL: {0}", sql);
            return result;
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "SQL error occurred", e);
        }
        return result;
    }

    @Override
    public boolean deleteEmailUser(UserController user
    ) {
        String sql = "DELETE from tUSER where username =?";
        boolean result = false;
        try (Connection conn = HikariJDBCDataSource.getConnectionTodbSMS(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, user.getUsername());

            // Execute the query
            result = pstmt.executeUpdate() > 0;
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "SQL error occured ", ex);
        }
        return result;
    }

    @Override
    public boolean verify(UserController user) {
        String sql = "UPDATE tUSER SET verifiedEmail = 1 WHERE username=? and id=?";

        try (Connection con = HikariJDBCDataSource.getConnectionTodbSMS(); PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, user.getUsername());
            ps.setLong(2, user.getId());
            if (ps.executeUpdate() > 0) {
                System.out.println(user.getUsername() + " is successfully verified");
                return true;
            }

        } catch (SQLException e) {
            System.out.println("An sql exception occured " + e);
        }
        return false;
    }

}
