/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.mspace.clientmanager.api.dao;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.model.SelectItem;
import javax.servlet.http.HttpSession;
import ke.co.mspace.nonsmppmanager.invalids.getsession;

import org.mspace.clientmanager.api.model.TSmsApiKey;
import ke.co.mspace.nonsmppmanager.util.JdbcUtil;
import org.mspace.clientmanager.api.ApiException;
import org.mspace.clientmanager.api.Constants;

/**
 *
 * @author olal
 */
public class TSmsApiKeyDAO {

    HttpSession session = getsession.getSession();
    String user = (String) session.getAttribute("username");
    Long agent = (Long) session.getAttribute("id");
    char admin = (Character) session.getAttribute("taskAdmin");

    private static final Logger LOGGER = Logger.getLogger(TSmsApiKeyDAO.class.getName());

    private static final String INSERT = "INSERT INTO tApiKeys (tUserId, name, apiKey, dateCreated, expiryDate, status) VALUES (?, ?, ?, ?, ?, ?)";
    private JdbcUtil jdbcUtil = new JdbcUtil();

    public List<SelectItem> getUsers() {
        List<SelectItem> users = new ArrayList<>();
        String queryAdmin = "SELECT id, username FROM tUSER WHERE organization = '" + user + "'";
        String queryRes = "SELECT id, username FROM tUSER WHERE agent = '" + agent + "'";

        try (Connection conn = jdbcUtil.getConnectionTodbSMS(); PreparedStatement ps = admin == 'Y' ? conn.prepareStatement(queryAdmin) :conn.prepareStatement(queryRes)) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                users.add(new SelectItem(rs.getInt("id"), rs.getString("username")));
            }
        } catch (SQLException e) {
            // Handle exceptions
            LOGGER.log(Level.SEVERE, "Error fetching user types", e);
        }
        return users;
    }

    public List<TSmsApiKey> selectAll() {
        String queryRes = "SELECT a.id ,u.username, a.name, a.apiKey, a.expiryDate, a.status FROM tApiKeys a LEFT JOIN tUSER u ON a.tUserId = u.id WHERE u.agent = ?;";
        String queryAdmin = "SELECT a.id ,u.username, a.name, a.apiKey, a.expiryDate, a.status FROM tApiKeys a LEFT JOIN tUSER u ON a.tUserId = u.id WHERE u.agent IS NOT NULL;";
        List<TSmsApiKey> keys = new ArrayList<>();

        try (Connection connection = jdbcUtil.getConnectionTodbSMS(); PreparedStatement ps = admin == 'Y' ? connection.prepareStatement(queryAdmin) : connection.prepareStatement(queryRes)) {

            if ('Y' != admin) {
                ps.setLong(1, agent);
            }

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                TSmsApiKey key = new TSmsApiKey();
                key.setId(rs.getInt("id"));
                key.setUsername(rs.getString("username"));
                key.setName(rs.getString("name"));
                key.setApiKey(rs.getString("apiKey"));
                key.setExpiryDate(rs.getTimestamp("expiryDate"));
                key.setStatus(rs.getInt("status"));
                keys.add(key);
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error loading users", e);
        }
        return keys;
    }

    public String insert(TSmsApiKey key) throws ApiException {
        try (Connection connection = jdbcUtil.getConnectionTodbSMS(); PreparedStatement ps = connection.prepareStatement(INSERT)) {
            Date now = new Date();
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(now);
            calendar.add(Calendar.MONTH, 3);
            Date expiryDate = calendar.getTime();

            String apiKey = generateKey(String.valueOf(key.getUserId()), key.getName());

            ps.setInt(1, key.getUserId());
            ps.setString(2, key.getName());
            ps.setString(3, apiKey);
            ps.setTimestamp(4, new java.sql.Timestamp(now.getTime()));
            ps.setTimestamp(5, new java.sql.Timestamp(expiryDate.getTime()));
            ps.setInt(6, 1); // Status set to active
            ps.executeUpdate();
            return apiKey;
        } catch (SQLException ex) {
            if (Constants.DUPLICATE_SQL_STATE.equals(ex.getSQLState()) && Constants.DUPLICATE_ERROR_CODE.equals(String.valueOf(ex.getErrorCode()))) {
                throw new ApiException("A key with the same name already exists.");
            }
            return null;
        }

    }

    public void update(int id) {
        try (Connection connection = jdbcUtil.getConnectionTodbSMS()) {
            // Fetch the existing key details
            String selectQuery = "SELECT * FROM tApiKeys WHERE id = ?";
            try (PreparedStatement selectStatement = connection.prepareStatement(selectQuery)) {
                selectStatement.setInt(1, id);
                ResultSet rs = selectStatement.executeQuery();
                if (rs.next()) {
                    // Get the existing values
                    int userId = rs.getInt("tUserId");
                    String name = rs.getString("name");
                    Date dateCreated = rs.getTimestamp("dateCreated");
                    Date expiryDate = rs.getTimestamp("expiryDate");
                    int status = rs.getInt("status");

                    // Generate new API key
                    String apiKey = generateKey(String.valueOf(userId), name);

                    // Update the key with the new API key
                    String updateQuery = "UPDATE tApiKeys SET apiKey = ? WHERE id = ?";
                    try (PreparedStatement updateStatement = connection.prepareStatement(updateQuery)) {
                        updateStatement.setString(1, apiKey);
                        updateStatement.setInt(2, id);
                        updateStatement.executeUpdate();
                    }
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error updating Key", e);
        }
    }

    public TSmsApiKey getSmsApiKeyById(int id) {
        TSmsApiKey apiKey = null;
        String query = "SELECT id, tUserId, name, apiKey FROM tApiKeys WHERE id = ?";
        try (Connection connection = jdbcUtil.getConnectionTodbSMS(); PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1, id);
            ResultSet resultSet = ps.executeQuery();
            if (resultSet.next()) {
                apiKey = new TSmsApiKey();
                apiKey.setId(resultSet.getInt("id"));
                apiKey.setUserId(resultSet.getInt("tUserId"));
                apiKey.setName(resultSet.getString("name"));
                apiKey.setApiKey(resultSet.getString("apiKey"));
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error loading Key by Id ", e);
        }
        return apiKey;
    }

    public void updateStatus(int id, int newStatus) {
        String UPDATE_STATUS = "UPDATE tApiKeys SET status = ? WHERE id = ?";
        try (Connection connection = jdbcUtil.getConnectionTodbSMS(); PreparedStatement ps = connection.prepareStatement(UPDATE_STATUS)) {
            ps.setInt(1, newStatus);
            ps.setInt(2, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            printSQLException(e);
            LOGGER.log(Level.SEVERE, "Error updating status", e);
        }
    }

    public void delete(int id) {
        String DELETE = "DELETE FROM tApiKeys WHERE id = ?";
        try (Connection connection = jdbcUtil.getConnectionTodbSMS(); PreparedStatement ps = connection.prepareStatement(DELETE)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error Deleting", e);
        }
    }

    private byte[] sha512Hash(String input) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-512");
            return md.digest(input.getBytes(StandardCharsets.UTF_8));
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    public String generateKey(String selectedUser, String username) {
        Date date = new Date();
        String combined = selectedUser + username + date;

        // Hash the combined string using SHA-512
        byte[] hashBytes = sha512Hash(combined);
        StringBuilder stringBuilder = new StringBuilder();

        for (int i = 0; i < hashBytes.length; i++) {
            String hex = Integer.toHexString(0xff & hashBytes[i]);
            if (hex.length() == 1) {
                stringBuilder.append('0');
            }
            stringBuilder.append(hex);

        }
        String sha512 = stringBuilder.toString();

        return sha512;
    }

    private void printSQLException(SQLException ex) {
        for (Throwable e : ex) {
            if (e instanceof SQLException) {
                e.printStackTrace(System.err);
                System.err.println("SQLState: " + ((SQLException) e).getSQLState());
                System.err.println("Error Code: " + ((SQLException) e).getErrorCode());
                System.err.println("Message: " + e.getMessage());
                Throwable t = ex.getCause();
                while (t != null) {
                    System.out.println("Cause: " + t);
                    t = t.getCause();
                }
            }
        }
    }

}
