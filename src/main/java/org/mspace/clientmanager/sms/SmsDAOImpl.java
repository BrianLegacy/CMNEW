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
import javax.servlet.http.HttpSession;
import ke.co.mspace.nonsmppmanager.invalids.getsession;
import org.mspace.clientmanager.user.UserController;
import ke.co.mspace.nonsmppmanager.util.JdbcUtil;

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
    public List<UserController> fetchSmsusers() {
        
        String adminQuery = "SELECT dbSMS.tUSER.id, dbSMS.tUSER.username, dbSMS.tUSER.password, dbSMS.tUSER.admin, "
                + "dbSMS.tUSER.max_total, dbSMS.tUSER.max_contacts, dbSMS.tUSER.organization, dbSMS.tUSER.contact_number, "
                + "dbSMS.tUSER.email_address, dbSMS.tUSER.enable_email_alert, dbSMS.tUSER.end_date, dbSMS.tUSER.start_date, "
                + "dbSMS.tUSER.alertThreshold, dbSMS.tUSER.cost_per_sms, dbSMS.tUSER.arrears, dbSMS.tUSER.group "
                + "FROM dbSMS.tUSER "
                + "WHERE dbSMS.tUSER.admin != '5' AND dbSMS.tUSER.agent != 'email'";

        // Reseller query string
        String resellerQuery = "SELECT dbSMS.tUSER.id, dbSMS.tUSER.username, dbSMS.tUSER.password, dbSMS.tUSER.admin, "
                + "dbSMS.tUSER.max_total, dbSMS.tUSER.max_contacts, dbSMS.tUSER.organization, dbSMS.tUSER.contact_number, "
                + "dbSMS.tUSER.email_address, dbSMS.tUSER.enable_email_alert, dbSMS.tUSER.end_date, dbSMS.tUSER.start_date, "
                + "dbSMS.tUSER.alertThreshold, dbSMS.tUSER.cost_per_sms, dbSMS.tUSER.arrears, dbSMS.tUSER.group "
                + "FROM dbSMS.tUSER "
                + "WHERE dbSMS.tUSER.agent = ? AND dbSMS.tUSER.admin = '3'";
        List<UserController> smsUsers = new ArrayList<>();

        try (Connection connection = jdbcUtil.getConnectionTodbSMS(); PreparedStatement ps = admin == 'Y' ? connection.prepareStatement(adminQuery) : connection.prepareStatement(resellerQuery)) {

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

        }

        return smsUsers;
    }
    
    public String getGroup(int id){
        String sql =" select groupname from tGROUPS where id= ?";
          String groupName = null;

        try (Connection connection = jdbcUtil.getConnectionTodbSMS();
             PreparedStatement ps = connection.prepareStatement(sql)) {
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
    public boolean deleteUser() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public boolean editSmsUser() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public boolean manageCredit() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

}
