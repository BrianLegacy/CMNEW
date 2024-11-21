/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.mspace.clientmanager.reseller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.model.SelectItem;
import ke.co.mspace.nonsmppmanager.util.HikariJDBCDataSource;
import ke.co.mspace.nonsmppmanager.util.JdbcUtil;
import ke.co.mspace.nonsmppmanager.util.PasswordUtil;
import org.mspace.clientmanager.user.UserController;

/**
 *
 * @author olal
 */
public class ResellerDAOImpl implements ResellerDAO {

    private static final Logger LOGGER = Logger.getLogger(ResellerDAOImpl.class.getName());
//    private JdbcUtil jdbcUtil = new JdbcUtil();

    @Override
    public List<UserController> fetchResellers() {
        String sql = "SELECT tUSER.id, tUSER.username, tUSER.password, tUSER.admin, "
                + "tUSER.max_total, tUSER.max_contacts, tUSER.organization, tUSER.contact_number, "
                + "tUSER.email_address, tUSER.enable_email_alert, tUSER.end_date, tUSER.start_date, "
                + "tUSER.alertThreshold, tUSER.cost_per_sms, tUSER.arrears "
                + "FROM tUSER "
                + "WHERE tUSER.admin = '5'";

        List<UserController> resellers = new ArrayList<>();
        try (Connection connection = HikariJDBCDataSource.getConnectionTodbSMS(); PreparedStatement ps = connection.prepareStatement(sql)) {
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
                    aUser.setEmailCredits(rs.getInt("max_contacts"));
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

                    resellers.add(aUser);
                }
            }

        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Sql Exception {0} ", e);
        }

        return resellers;
    }

    @Override
    public boolean editReseller(UserController user) {
        String sql = "UPDATE tUSER SET "
                + "username=?, organization=?, contact_number = ?, email_address=?"
                + ", enable_email_alert=?,cost_per_sms=?,arrears=?,alertThreshold=?  WHERE id=?";
        boolean result = false;
        try (Connection conn = HikariJDBCDataSource.getConnectionTodbSMS(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, user.getUsername());
            pstmt.setString(2, user.getOrganization());
            pstmt.setString(3, user.getUserMobile());
            pstmt.setString(4, user.getUserEmail());
            pstmt.setBoolean(5, user.isEnableEmailAlertWhenCreditOver());
            pstmt.setFloat(6, user.getCost_per_sms());
            pstmt.setInt(7, user.getArrears());
            pstmt.setInt(8, user.getAlertThreshold());
            pstmt.setLong(9, user.getId());

            result = pstmt.executeUpdate() > 0;
            LOGGER.log(Level.INFO, "Executed SQL: {0}", sql);
            return result;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "SQL error occurred", e);
        }
        return result;
    }
    
    @Override
    public List<SelectItem> users() {
        List<SelectItem> users = new ArrayList<>();
        String sql = "select username from tUSER where admin = 5 order by username";
        
        try (Connection conn = HikariJDBCDataSource.getConnectionTodbSMS(); PreparedStatement ps = conn.prepareStatement(sql)) {
            
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
    public boolean changePass(String username, String password) {
        String sql = "update tUSER set password = ? where username= ?";
        boolean result = false;

        try (Connection conn = HikariJDBCDataSource.getConnectionTodbSMS(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            String hashedPass = PasswordUtil.encrypt(password);
            pstmt.setString(1, hashedPass);

            pstmt.setString(2, username);

            // Execute the query
            result = pstmt.executeUpdate() == 1;
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "SQL error occured ", ex);
        }
        return result;
    }

    @Override
    public boolean delReseller(UserController user) {

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
    public boolean setImagePath(String username, String picPath) {
        String sqlUpdate = "UPDATE dbTASK.tClient SET picPath = ? WHERE clientName=? ";
        boolean result = false;
        try (Connection conn = HikariJDBCDataSource.getConnectionTodbTask(); PreparedStatement pstm = conn.prepareStatement(sqlUpdate)) {
            pstm.setString(1, picPath);
            pstm.setString(2, username);
            result = pstm.executeUpdate() > 0;
            return result;
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "SQL error setting picpath", ex);
        }
        return result;
    }

}
