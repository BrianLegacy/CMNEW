/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.mspace.clientmanager.util;

import java.io.BufferedReader;
import java.net.URLEncoder;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Random;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import ke.co.mspace.nonsmppmanager.util.HikariJDBCDataSource;
import ke.co.mspace.nonsmppmanager.util.JdbcUtil;
import ke.co.mspace.nonsmppmanager.util.JsfUtil;
import ke.co.mspace.nonsmppmanager.util.PasswordUtil;
import org.apache.commons.io.input.BufferedFileChannelInputStream;

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
            int number = random.nextInt(9);
            sb.append(number);
        }
        return sb.toString();
    }

    public void resetPassword() {
        String sql = "Select email_address, contact_number from tUSER where username = '" + username + "'";

        try (Connection conn = HikariJDBCDataSource.getConnectionTodbSMS(); PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
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
            System.out.println("Temporary  password: " + tempPass + " For: " + username);
            int eresult = sendEmail(userEmail, tempPass);
            int sresult = sendSMS(userContact, tempPass);

            checkBalanceAndNotify();

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
        String emailBody = "This is your one time password " + msg;
        String user = "MSpaceOtp";

        String sql = "insert into tEMAILOUT (emailSrc, emailTo, emailReplyTo, emailBody, subject, user) "
                + " values(?,?,?,?,?,?)";
        int result = 0;
        try (Connection conn = HikariJDBCDataSource.getConnectionTodbEMAIL(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, emailSource);
            ps.setString(2, userEmail);
            ps.setString(3, emailReplyTo);
            ps.setString(4, emailBody);
            ps.setString(5, subject);
            ps.setString(6, user);

            result = ps.executeUpdate();
            LOGGER.log(Level.INFO, "success table tEMAILOUT apdated SQl" + ps.toString());

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

    public void checkBalanceAndNotify() {
        try {
            // Define the URL for checking the balance
            URL url = new URL("http://api.mspace.co.ke/mspaceservice/wr/sms/balance/username=MSpaceOtp/password=Mspace@2022");
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");

            // Get the response code and read the balance from the response
            int responseCode = con.getResponseCode();
            if (responseCode == 200) {
                try (BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()))) {
                    String inputLine;
                    StringBuilder content = new StringBuilder();
                    while ((inputLine = in.readLine()) != null) {
                        content.append(inputLine);
                    }
                    // Parse the balance from the response (assuming it's a plain number)
                    double balance = Double.parseDouble(content.toString().trim());
                    System.out.println("Balance " + balance);
                    // Check if the balance is less than 5
                    if (balance < 5) {
                        // Send an email to the accountant
                        sendBalanceLowEmail(balance);
                    }
                }
            } else {
                LOGGER.log(Level.SEVERE, "Failed to check balance. Response Code: " + responseCode);
            }
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "IO Exception", e);
        }
    }

    private void sendBalanceLowEmail(double balance) {
        String emailSource = "accounts@mspace.co.ke";
        String emailReplyTo = emailSource;
        String subject = "URGENT: Low SMS Balance for MSpaceOtp";
        String emailBody = "Dear Ian,\n\nThe current SMS balance for the sender ID 'MSpaceOtp' is " + balance + ". Please recharge the account as soon as possible.\n\nThank you.";
        String user = "MSpaceOtp";
        String accountantEmail = emailSource;

        String sql = "insert into tEMAILOUT (emailSrc, emailTo, emailReplyTo, emailBody, subject, user) "
                + " values(?,?,?,?,?,?)";

        try (Connection conn = HikariJDBCDataSource.getConnectionTodbEMAIL(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, emailSource);
            ps.setString(2, accountantEmail);
            ps.setString(3, emailReplyTo);
            ps.setString(4, emailBody);
            ps.setString(5, subject);
            ps.setString(6, user);

            ps.executeUpdate();
            LOGGER.log(Level.INFO, "Balance low email sent to accountant.");

        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Failed to send balance low email", ex);
        }
    }
}
