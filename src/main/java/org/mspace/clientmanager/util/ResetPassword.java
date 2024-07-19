/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.mspace.clientmanager.util;

import java.net.URLEncoder;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Random;
import java.io.IOException;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.function.Supplier;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.bean.ViewScoped;
import ke.co.mspace.nonsmppmanager.util.JdbcUtil;
import ke.co.mspace.nonsmppmanager.util.JsfUtil;
import ke.co.mspace.nonsmppmanager.util.PasswordUtil;

/**
 *
 * @author olal
 */
@ManagedBean(name = "passwordreset")
@ViewScoped
public class ResetPassword implements Serializable {

    private static final long serialVersionUID = 1L;
    private final JdbcUtil util = new JdbcUtil();
    private static final Logger LOGGER = Logger.getLogger(ResetPassword.class.getName());

    private final String APIURL = "http://api.mspace.co.ke/mspaceservice/wr/sms/sendtext/username=MSpaceOtp/password=Mspace@2022/senderid=MSpace";

    private String userEmail;
    private String userContact;
    private String username;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getUserContact() {
        return userContact;
    }

    public void setUserContact(String userContact) {
        this.userContact = userContact;
    }

    public String generateRandomNumbers() {
        StringBuilder sb = new StringBuilder();
        Random random = new Random();

        for (int i = 0; i < 6; i++) {
            int number = random.nextInt(100);
            sb.append(number);
            if (i < 5) {
                sb.append(" ");
            }
        }
        return sb.toString();
    }

    public void resetPassword() {
        String sql = "Select email_address, contact_number from tUSER where username = '" + username + "'";

        try (Connection conn = util.getConnectionTodbSMS(); PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            if (!rs.next()) {
                JsfUtil.addErrorMessage("User does not exist");
                return;
            } else {
                userEmail = rs.getString("email_address");
                userContact = rs.getString("contact_number");
            }

            String updatePass = "Update tUSER set password = ? where username = '" + username + "'";
            String tempPass = generateRandomNumbers();
            String hashed = PasswordUtil.encrypt(tempPass);

            try (PreparedStatement pst = conn.prepareStatement(updatePass)) {
                pst.setString(1, hashed);
                pst.executeUpdate();
            }

            int eresult = sendEmail(userEmail, tempPass);
            int sresult = sendSMS(userContact, tempPass);

            if (eresult != 0 && sresult != 0) {
                if (eresult == 1 && sresult == 200) {
                    JsfUtil.addSuccessMessage("Password reset successfully. Email and SMS sent.");
                } else {
                    JsfUtil.addSuccessMessage("Message not sent try again. ");
                }
            } else {
                JsfUtil.addSuccessMessage("Message not sent try again. ");
            }

        } catch (SQLException ex) {
            System.out.println("" + ex);
        }
    }

    private int sendEmail(String userEmail, String msg) {
        String emailSource = "accounts@mspace.co.ke";
        String emailReplyTo = emailSource;
        String subject = "Reset Password";
        String emailBody = "The password is " + msg;
        String user = "MSpaceOtp";

        String sql = "insert into tEMAILOUT (emailSrc, emailTo, emailReplyTo, emailBody, subject, user) "
                + " values(?,?,?,?,?,?)";
        int result = 0;
        try (Connection conn = util.getConnectionTodbEMAIL(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, emailSource);
            ps.setString(2, userEmail);
            ps.setString(3, emailReplyTo);
            ps.setString(4, emailBody);
            ps.setString(5, subject);
            ps.setString(6, user);

            result = ps.executeUpdate();

        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage());
        }
        return result;
    }

    private int sendSMS(String contact, String msg) {
        try {
            if (userContact == null || userContact.isEmpty()) {
                LOGGER.log(Level.SEVERE, "Invalid contact number");
                return 0;
            }

            msg = URLEncoder.encode(msg, "UTF-8").replaceAll("\\+", "%20");
            URL url = new URL(APIURL + "/recipient=" + userContact + "/message=" + msg);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");

            int responseCode = con.getResponseCode();
            LOGGER.log(Level.INFO, "Response Code: " + responseCode);
            return responseCode;
        } catch (UnsupportedEncodingException e) {
            LOGGER.log(Level.SEVERE, "Encoding error", e);
        } catch (MalformedURLException e) {
            LOGGER.log(Level.SEVERE, "Malformed URL", e);
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "IO Exception", e);
        }
        return 0;
    }

}
