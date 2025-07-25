package org.mspace.clientmanager.sharedUSSD;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import ke.co.mspace.nonsmppmanager.util.JdbcUtil;

/**
 *
 * @author olal
 */
public class CallbackDAOimpl implements CallbackDAO {

    private static final Logger LOGGER = Logger.getLogger(CallbackDAOimpl.class.getName());
    private final JdbcUtil jdbcUtil = new JdbcUtil();

    @Override
    public List<CallbackModel> fetchCallbacks() {
        String sql = "SELECT * FROM dbUSSD.tSharedUssdClients LEFT JOIN dbSMS.tUSER ON dbUSSD.tSharedUssdClients.tuser_id = dbSMS.tUSER.id";
        ArrayList<CallbackModel> callbacklist = new ArrayList();

        try (Connection conn = jdbcUtil.getConnectionTodbUSSD(); PreparedStatement ps = conn.prepareStatement(sql)) {
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {     
                    CallbackModel callback = new CallbackModel();
                    callback.setId(rs.getInt("id"));
                    callback.setUserid(rs.getInt("tUSER_id"));
                    callback.setCallback_url(rs.getString("callback_url"));
                    callback.setUssd_assigned_code(rs.getString("ussd_assigned_code"));
                    callback.setStatus(rs.getBoolean("status"));
                    callback.setTestbed(rs.getInt("type") == 0 ? true : false);
                    callback.setTestbednumbers(rs.getString("testbedmobiles"));
                    callback.setDuedate(rs.getDate("due_date"));
                    callback.setDisconnectDate(rs.getDate("disconnect_date"));
                    callback.setUsername(rs.getString("username"));

                    callbacklist.add(callback);
                }
            }catch(SQLException ex){
                System.out.println("An sql exception has occured! " + ex);
            }
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, null, ex);
        }
        return callbacklist;
    }

    @Override
    public boolean editCallback(CallbackModel callback) {
        System.out.println("Inside editCallback");
        String sql = "UPDATE tSharedUssdClients set "
                + "callback_url=?,ussd_assigned_code=?,status=?,type=?,testbedmobiles=?, due_date=?, disconnect_date=? "
                + " where id=? ";
        boolean result = false;

        String testBedNumbers = callback.getTestbednumbers();
        callback.setTestbednumbers(formatNumbers(testBedNumbers));
                
        try (Connection conn = jdbcUtil.getConnectionTodbUSSD(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, callback.getCallback_url());
            ps.setString(2, callback.getUssd_assigned_code());
            ps.setBoolean(3, callback.isStatus());
            ps.setInt(4, callback.isTestbed() ? 0 : 1);
            ps.setString(5, callback.getTestbednumbers());
            ps.setDate(6,  new java.sql.Date(callback.getDuedate().getTime()));
            ps.setDate(7, new java.sql.Date(callback.getDisconnectDate().getTime()));
            ps.setInt(8, callback.getId());

            result = ps.executeUpdate() > 0;

            return result;
        } catch (SQLException ex) {
            System.out.println("An sql exception has occured " + ex);
//            LOGGER.log(Level.SEVERE, null, ex);
        }
        return result;
    }

    @Override
    public boolean deleteCallback(CallbackModel callback) {

        String sql = "DELETE from tSharedUssdClients where id=?";
        boolean result = false;
        try (Connection conn = jdbcUtil.getConnectionTodbUSSD(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, callback.getId());

            // Execute the query
            result = pstmt.executeUpdate() > 0;
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, null, ex);
        }
        return result;
    }

    public String formatNumbers(String nums) {
        String[] numbers = nums.split(",\\s*");

        for (int i = 0; i <= numbers.length - 1; i++) {
            if (numbers[i].startsWith("254") && numbers[i].length() == 12) {
//        okay
            } else if (((numbers[i].startsWith("01") || (numbers[i].startsWith("07"))) && numbers[i].length() == 10)) {
                numbers[i] = "254" + numbers[i].substring(1, numbers[i].length());
            }

        }

        return String.join(",", numbers);
    }

    @Override
    public boolean createCallback(CallbackModel callback) {
        String testBedNumbers = callback.getTestbednumbers();
        callback.setTestbednumbers(formatNumbers(testBedNumbers));

        String sql = "INSERT INTO tSharedUssdClients(tuser_id,callback_url,"
                + "ussd_assigned_code,status,testbedmobiles,type,due_date,disconnect_date)VALUES(?,?,?,?,?,?,?,?)";
        boolean result = false;
        try (Connection conn = jdbcUtil.getConnectionTodbUSSD(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, callback.getUserid());
            ps.setString(2, callback.getCallback_url());
            ps.setString(3, callback.getUssd_assigned_code());
            ps.setBoolean(4, callback.isStatus());
            ps.setString(5, callback.getTestbednumbers());
            ps.setInt(6, callback.isTestbed() ? 0 : 1);
            ps.setDate(7, new java.sql.Date(callback.getDuedate().getTime()));
            ps.setDate(8, new java.sql.Date(callback.getDisconnectDate().getTime()));
            result = ps.executeUpdate() > 0;
            
            return result;
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, null, ex);
        }
        return result;
    }
}


