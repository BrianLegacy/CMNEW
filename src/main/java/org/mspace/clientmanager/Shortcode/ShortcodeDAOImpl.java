package org.mspace.clientmanager.Shortcode;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import ke.co.mspace.nonsmppmanager.util.JdbcUtil;

public class ShortcodeDAOImpl implements ShortcodeDAO {

    private static final Logger LOGGER = Logger.getLogger(ShortcodeDAOImpl.class.getName());
    private final JdbcUtil jdbcUtil = new JdbcUtil();

    @Override
    public List<ShortcodeModel> fetchShortcodes() {
//        String sql = "SELECT * FROM dbSMS.shared_shortcode "
//                + "LEFT JOIN dbSMS.tUSER ON dbSMS.shared_shortcode.userid = dbSMS.tUSER.id";
        String sql = "SELECT * FROM dbSMS.shared_shortcode "
                + "LEFT JOIN dbSMS.tUSER ON dbSMS.shared_shortcode.userid = dbSMS.tUSER.id "
                + "WHERE keyword IS NOT NULL AND keyword <> ''";

        List<ShortcodeModel> shortcodeList = new ArrayList<>();

        try (Connection conn = jdbcUtil.getConnectionTodbSMS(); PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                ShortcodeModel shortcode = new ShortcodeModel();
                shortcode.setId(rs.getInt("id"));
                shortcode.setUserid(rs.getInt("userid"));
                shortcode.setUsername(rs.getString("username"));
                shortcode.setShortcode(rs.getString("shortcode"));
                shortcode.setKeyword(rs.getString("keyword"));
                shortcode.setCallbackUrl(rs.getString("callback_url"));
                shortcode.setStatus(rs.getBoolean("status"));
                shortcode.setDueDate(rs.getDate("due_date"));
                shortcode.setDisconnectDate(rs.getDate("disconnect_date"));

                shortcodeList.add(shortcode);
            }

        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, null, ex);
        }

        return shortcodeList;
    }

//    @Override
//    public boolean createShortcode(ShortcodeModel shortcode) {
//        String sql = "INSERT INTO dbSMS.shared_shortcode(userid, username, shortcode, callback_url, status, due_date, disconnect_date, keyword) "
//                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
//        try (Connection conn = jdbcUtil.getConnectionTodbSMS(); PreparedStatement ps = conn.prepareStatement(sql)) {
//
//            ps.setInt(1, shortcode.getUserid());
//            ps.setString(2, shortcode.getUsername());
//            ps.setString(3, shortcode.getShortcode());
//            ps.setString(4, shortcode.getCallbackUrl());
//            ps.setBoolean(5, shortcode.isStatus());
//            ps.setDate(6, new java.sql.Date(shortcode.getDueDate().getTime()));
//            ps.setDate(7, new java.sql.Date(shortcode.getDisconnectDate().getTime()));
//            ps.setString(8, shortcode.getKeyword());
//
//            return ps.executeUpdate() > 0;
//
//        } catch (SQLException ex) {
//            LOGGER.log(Level.SEVERE, null, ex);
//        }
//
//        return false;
//    }
    @Override
    public boolean createShortcode(ShortcodeModel shortcode) {
        String insertSQL = "INSERT INTO dbSMS.shared_shortcode(userid, username, shortcode, callback_url, status, due_date, disconnect_date, keyword) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        String updateUserSQL = "UPDATE dbSMS.tUSER SET short_codes = "
                + "CONCAT_WS(',', IFNULL(NULLIF(short_codes, ''), NULL), ?) "
                + "WHERE id = ? AND FIND_IN_SET(?, short_codes) = 0";

        try (Connection conn = jdbcUtil.getConnectionTodbSMS()) {
            conn.setAutoCommit(false); // Begin transaction

            try (PreparedStatement ps = conn.prepareStatement(insertSQL)) {
                ps.setInt(1, shortcode.getUserid());
                ps.setString(2, shortcode.getUsername());
                ps.setString(3, shortcode.getShortcode());
                ps.setString(4, shortcode.getCallbackUrl());
                ps.setBoolean(5, shortcode.isStatus());
                ps.setDate(6, new java.sql.Date(shortcode.getDueDate().getTime()));
                ps.setDate(7, new java.sql.Date(shortcode.getDisconnectDate().getTime()));
                ps.setString(8, shortcode.getKeyword());
                ps.executeUpdate();
            }

            try (PreparedStatement ps2 = conn.prepareStatement(updateUserSQL)) {
                ps2.setString(1, shortcode.getShortcode());
                ps2.setInt(2, shortcode.getUserid());
                ps2.setString(3, shortcode.getShortcode());
                ps2.executeUpdate();
            }

            conn.commit();
            return true;
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, null, ex);
        }

        return false;
    }

    @Override
    public boolean editShortcode(ShortcodeModel shortcode) {
        String sql = "UPDATE dbSMS.shared_shortcode SET shortcode=?, callback_url=?, status=?, due_date=?, disconnect_date=?, keyword=? WHERE id=?";
        try (Connection conn = jdbcUtil.getConnectionTodbSMS(); PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, shortcode.getShortcode());
            ps.setString(2, shortcode.getCallbackUrl());
            ps.setBoolean(3, shortcode.isStatus());
            ps.setDate(4, new java.sql.Date(shortcode.getDueDate().getTime()));
            ps.setDate(5, new java.sql.Date(shortcode.getDisconnectDate().getTime()));
            ps.setString(6, shortcode.getKeyword());
            ps.setInt(7, shortcode.getId());

            return ps.executeUpdate() > 0;

        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, null, ex);
        }

        return false;
    }

//    @Override
//    public boolean deleteShortcode(ShortcodeModel shortcode) {
//        String sql = "DELETE FROM dbSMS.shared_shortcode WHERE id=?";
//        try (Connection conn = jdbcUtil.getConnectionTodbSMS(); PreparedStatement ps = conn.prepareStatement(sql)) {
//
//            ps.setInt(1, shortcode.getId());
//            return ps.executeUpdate() > 0;
//
//        } catch (SQLException ex) {
//            LOGGER.log(Level.SEVERE, null, ex);
//        }
//
//        return false;
//    }
    
     
    @Override
    public boolean deleteShortcode(ShortcodeModel shortcode) {
        String getShortcodeSQL = "SELECT shortcode, userid FROM dbSMS.shared_shortcode WHERE id = ?";
        String deleteSQL = "DELETE FROM dbSMS.shared_shortcode WHERE id=?";
        String updateUserSQL = "UPDATE dbSMS.tUSER SET short_codes = TRIM(BOTH ',' FROM REPLACE(CONCAT(',', short_codes, ','), ?, ',')) WHERE id = ?";

        try (Connection conn = jdbcUtil.getConnectionTodbSMS()) {
            conn.setAutoCommit(false);

            String shortcodeStr = null;
            int userId = -1;

            try (PreparedStatement ps1 = conn.prepareStatement(getShortcodeSQL)) {
                ps1.setInt(1, shortcode.getId());
                try (ResultSet rs = ps1.executeQuery()) {
                    if (rs.next()) {
                        shortcodeStr = rs.getString("shortcode");
                        userId = rs.getInt("userid");
                    }
                }
            }

            if (shortcodeStr == null || userId == -1) {
                return false;
            }

            try (PreparedStatement ps2 = conn.prepareStatement(deleteSQL)) {
                ps2.setInt(1, shortcode.getId());
                ps2.executeUpdate();
            }

            try (PreparedStatement ps3 = conn.prepareStatement(updateUserSQL)) {
                ps3.setString(1, "," + shortcodeStr + ",");
                ps3.setInt(2, userId);
                ps3.executeUpdate();
            }

            conn.commit();
            return true;
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, null, ex);
        }

        return false;
    }

    //DEDICATED SHORTCODE
    @Override
    public List<ShortcodeModel> fetchShortcodesWithNullKeyword() {
        String sql = "SELECT * FROM dbSMS.shared_shortcode "
                + "LEFT JOIN dbSMS.tUSER ON dbSMS.shared_shortcode.userid = dbSMS.tUSER.id "
                + "WHERE keyword IS NULL OR keyword = '' ";
        List<ShortcodeModel> shortcodeList = new ArrayList<>();

        try (Connection conn = jdbcUtil.getConnectionTodbSMS(); PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                ShortcodeModel shortcode = new ShortcodeModel();
                shortcode.setId(rs.getInt("id"));
                shortcode.setUserid(rs.getInt("userid"));
                shortcode.setUsername(rs.getString("username"));
                shortcode.setShortcode(rs.getString("shortcode"));
                shortcode.setKeyword(rs.getString("keyword"));
                shortcode.setCallbackUrl(rs.getString("callback_url"));
                shortcode.setStatus(rs.getBoolean("status"));
                shortcode.setDueDate(rs.getDate("due_date"));
                shortcode.setDisconnectDate(rs.getDate("disconnect_date"));

                shortcodeList.add(shortcode);
            }

        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, null, ex);
        }

        return shortcodeList;
    }

}
