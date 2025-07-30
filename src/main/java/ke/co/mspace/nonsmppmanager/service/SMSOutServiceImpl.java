/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ke.co.mspace.nonsmppmanager.service;

import java.sql.Connection;
import javax.servlet.http.HttpSession;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.context.FacesContext;
import ke.co.mspace.nonsmppmanager.model.OPTOut;
import ke.co.mspace.nonsmppmanager.model.SMPPOut;
import ke.co.mspace.nonsmppmanager.model.SMSOut;
import ke.co.mspace.nonsmppmanager.util.JdbcUtil;
import ke.co.mspace.nonsmppmanager.invalids.getsession;

/**
 *
 * @author Norrey Osako
 */
public class SMSOutServiceImpl implements SMSOutServiceApi {

    private static final Logger LOGGER = Logger.getLogger("SMSOutServiceImpl.class");

    public static final String ANSI_BLUE = "\u001B[34m";
    public static final String ANSI_RESET = "\u001B[0m";

    private static final Logger LOG = Logger.getLogger(SMSOutServiceImpl.class.getName());

    private final static DateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");

    private final String user_id = FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("user_id").toString();
    private int limit = 500;
    private final JdbcUtil jdbcUtil = new JdbcUtil();
    HttpSession session = getsession.getSession();

    char admin = (Character) session.getAttribute("taskAdmin");
    Long userId = (Long) session.getAttribute("id");

    public SMSOutServiceImpl() {

    }

    @Override
    public List<OPTOut> fetchOptOutReport(String startDate, String endDate) {
        String sqlAdmin = "SELECT dbSMPPGateway.tOPTEDOUTCUSTOMERS.id AS myid, dbSMPPGateway.tOPTEDOUTCUSTOMERS.senderID, dbSMPPGateway.tOPTEDOUTCUSTOMERS.mobile, dbSMPPGateway.tOPTEDOUTCUSTOMERS.optout_message, dbSMPPGateway.tOPTEDOUTCUSTOMERS.reply, dbSMPPGateway.tOPTEDOUTCUSTOMERS.time, dbSMPPGateway.tOPTEDOUTCUSTOMERS.emailed "
                + "FROM dbSMPPGateway.tOPTEDOUTCUSTOMERS "
                + "WHERE dbSMPPGateway.tOPTEDOUTCUSTOMERS.time >= ? AND dbSMPPGateway.tOPTEDOUTCUSTOMERS.time <= ? "
                + "ORDER BY myid DESC";

        String sqlReseller = "SELECT o.id AS myid, o.senderID, o.mobile, o.optout_message, o.reply, o.time, o.emailed "
                + "FROM dbSMPPGateway.tOPTEDOUTCUSTOMERS o "
                + "INNER JOIN dbSMS.tSDPNew t ON o.senderID = t.short_code "
                + "INNER JOIN dbSMS.tUSER u ON t.agent_id = u.id "
                + "WHERE o.time >= ? AND o.time <= ? AND u.id = ? "
                + "ORDER BY myid DESC";

        List<OPTOut> result = new ArrayList<>();

        try (Connection conn = jdbcUtil.getConnectionTodbSMS(); PreparedStatement pstmt = admin == 'Y' ? conn.prepareStatement(sqlAdmin) : conn.prepareStatement(sqlReseller)) {

            pstmt.setString(1, startDate);
            pstmt.setString(2, endDate);

            if ('Y' != admin) {
                pstmt.setLong(3, userId);
            }

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    OPTOut optOut = new OPTOut();
                    optOut.setSourceAddr(rs.getString("senderID"));
                    optOut.setDestinationAddr(rs.getString("mobile"));
                    optOut.setMessagePayload(rs.getString("optout_message"));
                    optOut.setReply(rs.getString("reply"));
                    optOut.setTimeSubmitted(rs.getString("time"));
                    optOut.setEmailed(rs.getString("emailed"));

                    result.add(optOut);
                }
            }
            String sql = admin == 'Y' ? sqlAdmin : sqlReseller;
            System.out.println("Error retrieving opt-out report: " + sql);
        } catch (SQLException e) {
            LOG.log(Level.SEVERE, "Error retrieving opt-out report", e);
        }

        return result;
    }

    @Override
    public int getTotalSmsCount(String startDate, String endDate, String user) {

        
        System.out.println("Start Date" + startDate + "  End Date" + endDate);
        try {
            String query = " SELECT id from tUSER WHERE username='" + user + "'";
            Statement stmt1 = jdbcUtil.getConnectionTodbSMS().createStatement();
            ResultSet rs1 = stmt1.executeQuery(query);
            String adminid = null;
            while (rs1.next()) {
                adminid = rs1.getString("id");
            }

            String sql = null;
            String sqlReseller = null;
            if (user == null) {

                sql = "SELECT tSMSOUT.id AS myid,tSMSOUT.source_addr, tSMSOUT.destination_addr, tSMSOUT.message_payload, tSMSOUT.time_submitted, tSMSOUT.time_processed, "
                        + "tSMSOUT.user, tSMSOUT.status, tSMSSTATUS.desctiption  FROM tSMSOUT LEFT JOIN tSMSSTATUS ON tSMSOUT.status=tSMSSTATUS.id WHERE time_submitted >= '" + startDate + "' "
                        + "AND time_submitted <= '" + endDate + "' "
                        + "UNION ALL SELECT tSMSOUT_COMPLETE.id AS myid,tSMSOUT_COMPLETE.source_addr, tSMSOUT_COMPLETE.destination_addr, tSMSOUT_COMPLETE.message_payload, "
                        + "tSMSOUT_COMPLETE.time_submitted, tSMSOUT_COMPLETE.time_processed, tSMSOUT_COMPLETE.user, tSMSOUT_COMPLETE.status, tSMSSTATUS.desctiption  FROM tSMSOUT_COMPLETE"
                        + " LEFT JOIN tSMSSTATUS ON tSMSOUT_COMPLETE.status=tSMSSTATUS.id WHERE time_submitted >= '" + startDate + "' AND time_submitted <= '" + endDate + "'   UNION ALL "
                        + "select sc.id as myId , sc.source as source_addr,sc.dest as destination_addr,  sc.message as message_payload, sc.sendTime as time_submitted, "
                        + " sc.sendTime as time_processed ,sc.username as user, 11 as status ,'Scheduled' as  desctiption from tUSERSMSSCHEDULE sc  order by myid desc";

                sqlReseller = "SELECT tSMSOUT.id AS myid,tSMSOUT.source_addr, tSMSOUT.destination_addr, tSMSOUT.message_payload, tSMSOUT.time_submitted, tSMSOUT.time_processed, tSMSOUT.user, tSMSOUT.status, tSMSSTATUS.desctiption  "
                        + "FROM tSMSOUT LEFT JOIN tSMSSTATUS ON tSMSOUT.status=tSMSSTATUS.id inner join tUSER on tSMSOUT.user = tUSER.username "
                        + "WHERE time_submitted >= '" + startDate + "' AND time_submitted <= '" + endDate + "' and tUSER.agent = '" + user_id + "' UNION ALL "
                        + "SELECT tSMSOUT_COMPLETE.id AS myid,tSMSOUT_COMPLETE.source_addr, tSMSOUT_COMPLETE.destination_addr, tSMSOUT_COMPLETE.message_payload,"
                        + " tSMSOUT_COMPLETE.time_submitted, tSMSOUT_COMPLETE.time_processed, tSMSOUT_COMPLETE.user, tSMSOUT_COMPLETE.status, tSMSSTATUS.desctiption  "
                        + "FROM tSMSOUT_COMPLETE LEFT JOIN tSMSSTATUS ON tSMSOUT_COMPLETE.status=tSMSSTATUS.id inner join tUSER on tSMSOUT_COMPLETE.user = tUSER.username"
                        + " WHERE time_submitted >= " + startDate + " AND time_submitted <= '" + endDate + "' and tUSER.agent = '" + user_id + "' order by myid desc";

            } else if (user.isEmpty()) {

                sql = "SELECT tSMSOUT.id AS myid,tSMSOUT.source_addr, tSMSOUT.destination_addr, tSMSOUT.message_payload, tSMSOUT.time_submitted, tSMSOUT.time_processed, tSMSOUT.user, tSMSOUT.status, tSMSSTATUS.desctiption  FROM tSMSOUT LEFT JOIN tSMSSTATUS ON tSMSOUT.status=tSMSSTATUS.id WHERE time_submitted >= '" + startDate + "' AND time_submitted <= '" + endDate + "' "
                        + "UNION ALL SELECT tSMSOUT_COMPLETE.id AS myid,tSMSOUT_COMPLETE.source_addr, tSMSOUT_COMPLETE.destination_addr, tSMSOUT_COMPLETE.message_payload, tSMSOUT_COMPLETE.time_submitted, tSMSOUT_COMPLETE.time_processed, tSMSOUT_COMPLETE.user, tSMSOUT_COMPLETE.status, tSMSSTATUS.desctiption  FROM tSMSOUT_COMPLETE LEFT JOIN tSMSSTATUS ON tSMSOUT_COMPLETE.status=tSMSSTATUS.id WHERE time_submitted >= '" + startDate + "' AND time_submitted <= '" + endDate + "' UNION ALL "
                        + "select sc.id as myId , sc.source as source_addr,sc.dest as destination_addr,  sc.message as message_payload, sc.sendTime as time_submitted, "
                        + " sc.sendTime as time_processed ,sc.username as user, 11 as status ,'Scheduled' as  desctiption from tUSERSMSSCHEDULE sc order by myid desc";

                sqlReseller = "SELECT tSMSOUT.id AS myid,tSMSOUT.source_addr, tSMSOUT.destination_addr, tSMSOUT.message_payload, tSMSOUT.time_submitted, tSMSOUT.time_processed, tSMSOUT.user, tSMSOUT.status, tSMSSTATUS.desctiption  "
                        + "FROM tSMSOUT LEFT JOIN tSMSSTATUS ON tSMSOUT.status=tSMSSTATUS.id inner join tUSER on tSMSOUT.user = tUSER.username "
                        + "WHERE time_submitted >= '" + startDate + "' AND time_submitted <= '" + endDate + "' and tUSER.agent = '" + user_id + "' UNION ALL "
                        + "SELECT tSMSOUT_COMPLETE.id AS myid,tSMSOUT_COMPLETE.source_addr, tSMSOUT_COMPLETE.destination_addr, tSMSOUT_COMPLETE.message_payload, tSMSOUT_COMPLETE.time_submitted, tSMSOUT_COMPLETE.time_processed, tSMSOUT_COMPLETE.user, tSMSOUT_COMPLETE.status, tSMSSTATUS.desctiption  "
                        + "FROM tSMSOUT_COMPLETE LEFT JOIN tSMSSTATUS ON tSMSOUT_COMPLETE.status=tSMSSTATUS.id inner join tUSER on tSMSOUT_COMPLETE.user = tUSER.username WHERE time_submitted >= '" + startDate + "' AND time_submitted <= '" + endDate + "' and tUSER.agent = '" + user_id + "' order by myid desc";
            } else {
                sql = "SELECT tSMSOUT.id AS myid,tSMSOUT.source_addr, tSMSOUT.destination_addr, tSMSOUT.message_payload, tSMSOUT.time_submitted, tSMSOUT.time_processed, tSMSOUT.user, tSMSOUT.status, tSMSSTATUS.desctiption  FROM tSMSOUT LEFT JOIN tSMSSTATUS ON tSMSOUT.status=tSMSSTATUS.id  LEFT JOIN tUSER ON tUSER.username=tSMSOUT.user WHERE time_submitted >= '" + startDate + "' AND time_submitted <= '" + endDate + "' AND (user = '" + user + "' OR tUSER.agent= '" + adminid + "' OR tUSER.super_account_id='" + adminid + "' )"
                        + "UNION ALL SELECT tSMSOUT_COMPLETE.id AS myid,tSMSOUT_COMPLETE.source_addr, tSMSOUT_COMPLETE.destination_addr, tSMSOUT_COMPLETE.message_payload, tSMSOUT_COMPLETE.time_submitted, tSMSOUT_COMPLETE.time_processed, tSMSOUT_COMPLETE.user, tSMSOUT_COMPLETE.status, tSMSSTATUS.desctiption  FROM tSMSOUT_COMPLETE LEFT JOIN tSMSSTATUS ON tSMSOUT_COMPLETE.status=tSMSSTATUS.id LEFT JOIN tUSER ON tUSER.username=tSMSOUT_COMPLETE.user WHERE time_submitted >= '" + startDate + "' AND time_submitted <= '" + endDate + "' AND (user = '" + user + "' OR tUSER.agent= '" + adminid + "' OR tUSER.super_account_id='" + adminid + "' ) order by myid desc";
                sqlReseller = sql;
            }

            int totalSmsCount = 0;
            
            try (Connection conn = jdbcUtil.getConnectionTodbSMS(); PreparedStatement statement = conn.prepareStatement(UserServiceImpl.isReseller().equalsIgnoreCase("none") ? sqlReseller : sql)) {
 
                try (ResultSet rs = statement.executeQuery()) {
                    while (rs.next()) {
                        String messagePayload = rs.getString("message_payload");
//                        System.out.println("message_payload present " + messagePayload);
                        totalSmsCount += getSmsCount(messagePayload);
                    }
                }
            } catch (SQLException ex) {
                Logger.getLogger(SMSOutServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
            }
            System.out.println("found " + totalSmsCount +  " total records");

            return totalSmsCount;
        } catch (SQLException e) {
            System.out.println("An exception has occured! " + e);
        }
        return 0;
    }

    @Override
    public List<SMSOut> fetchSMSReport(String user, String startDate, String endDate) {
        try {
            
            System.out.println("Start Date " + startDate + "  End Date " + endDate);

            String query = " SELECT id from tUSER WHERE username='" + user + "'";
            System.out.println("The selected user id report is: " + user);
            Statement stmt1 = jdbcUtil.getConnectionTodbSMS().createStatement();
            ResultSet rs1 = stmt1.executeQuery(query);
            String adminid = null;
            while (rs1.next()) {
                adminid = rs1.getString("id");
                System.out.println("The super ID" + adminid);
            }

            String sql = null;
            String sqlReseller = null;
            if (user == null) {

                sql = "SELECT tSMSOUT.id AS myid,tSMSOUT.source_addr, tSMSOUT.destination_addr, tSMSOUT.message_payload, tSMSOUT.time_submitted, tSMSOUT.time_processed, "
                        + "tSMSOUT.user, tSMSOUT.status, tSMSSTATUS.desctiption  FROM tSMSOUT LEFT JOIN tSMSSTATUS ON tSMSOUT.status=tSMSSTATUS.id WHERE time_submitted >= '" + startDate + "' "
                        + "AND time_submitted <= '" + endDate + "' "
                        + "UNION ALL SELECT tSMSOUT_COMPLETE.id AS myid,tSMSOUT_COMPLETE.source_addr, tSMSOUT_COMPLETE.destination_addr, tSMSOUT_COMPLETE.message_payload, "
                        + "tSMSOUT_COMPLETE.time_submitted, tSMSOUT_COMPLETE.time_processed, tSMSOUT_COMPLETE.user, tSMSOUT_COMPLETE.status, tSMSSTATUS.desctiption  FROM tSMSOUT_COMPLETE"
                        + " LEFT JOIN tSMSSTATUS ON tSMSOUT_COMPLETE.status=tSMSSTATUS.id WHERE time_submitted >= '" + startDate + "' AND time_submitted <= '" + endDate + "'   UNION ALL "
                        + "select sc.id as myId , sc.source as source_addr,sc.dest as destination_addr,  sc.message as message_payload, sc.sendTime as time_submitted, "
                        + " sc.sendTime as time_processed ,sc.username as user, 11 as status ,'Scheduled' as  desctiption from tUSERSMSSCHEDULE sc  order by myid desc";

                sqlReseller = "SELECT tSMSOUT.id AS myid,tSMSOUT.source_addr, tSMSOUT.destination_addr, tSMSOUT.message_payload, tSMSOUT.time_submitted, tSMSOUT.time_processed, tSMSOUT.user, tSMSOUT.status, tSMSSTATUS.desctiption  "
                        + "FROM tSMSOUT LEFT JOIN tSMSSTATUS ON tSMSOUT.status=tSMSSTATUS.id inner join tUSER on tSMSOUT.user = tUSER.username "
                        + "WHERE time_submitted >= '" + startDate + "' AND time_submitted <= '" + endDate + "' and tUSER.agent = '" + user_id + "' UNION ALL "
                        + "SELECT tSMSOUT_COMPLETE.id AS myid,tSMSOUT_COMPLETE.source_addr, tSMSOUT_COMPLETE.destination_addr, tSMSOUT_COMPLETE.message_payload,"
                        + " tSMSOUT_COMPLETE.time_submitted, tSMSOUT_COMPLETE.time_processed, tSMSOUT_COMPLETE.user, tSMSOUT_COMPLETE.status, tSMSSTATUS.desctiption  "
                        + "FROM tSMSOUT_COMPLETE LEFT JOIN tSMSSTATUS ON tSMSOUT_COMPLETE.status=tSMSSTATUS.id inner join tUSER on tSMSOUT_COMPLETE.user = tUSER.username"
                        + " WHERE time_submitted >= " + startDate + " AND time_submitted <= '" + endDate + "' and tUSER.agent = '" + user_id + "' order by myid desc";

            } else if (user.isEmpty()) {

                sql = "SELECT tSMSOUT.id AS myid,tSMSOUT.source_addr, tSMSOUT.destination_addr, tSMSOUT.message_payload, tSMSOUT.time_submitted, tSMSOUT.time_processed, tSMSOUT.user, tSMSOUT.status, tSMSSTATUS.desctiption  FROM tSMSOUT LEFT JOIN tSMSSTATUS ON tSMSOUT.status=tSMSSTATUS.id WHERE time_submitted >= '" + startDate + "' AND time_submitted <= '" + endDate + "' "
                        + "UNION ALL SELECT tSMSOUT_COMPLETE.id AS myid,tSMSOUT_COMPLETE.source_addr, tSMSOUT_COMPLETE.destination_addr, tSMSOUT_COMPLETE.message_payload, tSMSOUT_COMPLETE.time_submitted, tSMSOUT_COMPLETE.time_processed, tSMSOUT_COMPLETE.user, tSMSOUT_COMPLETE.status, tSMSSTATUS.desctiption  FROM tSMSOUT_COMPLETE LEFT JOIN tSMSSTATUS ON tSMSOUT_COMPLETE.status=tSMSSTATUS.id WHERE time_submitted >= '" + startDate + "' AND time_submitted <= '" + endDate + "' UNION ALL "
                        + "select sc.id as myId , sc.source as source_addr,sc.dest as destination_addr,  sc.message as message_payload, sc.sendTime as time_submitted, "
                        + " sc.sendTime as time_processed ,sc.username as user, 11 as status ,'Scheduled' as  desctiption from tUSERSMSSCHEDULE sc order by myid desc";

                sqlReseller = "SELECT tSMSOUT.id AS myid,tSMSOUT.source_addr, tSMSOUT.destination_addr, tSMSOUT.message_payload, tSMSOUT.time_submitted, tSMSOUT.time_processed, tSMSOUT.user, tSMSOUT.status, tSMSSTATUS.desctiption  "
                        + "FROM tSMSOUT LEFT JOIN tSMSSTATUS ON tSMSOUT.status=tSMSSTATUS.id inner join tUSER on tSMSOUT.user = tUSER.username "
                        + "WHERE time_submitted >= '" + startDate + "' AND time_submitted <= '" + endDate + "' and tUSER.agent = '" + user_id + "' UNION ALL "
                        + "SELECT tSMSOUT_COMPLETE.id AS myid,tSMSOUT_COMPLETE.source_addr, tSMSOUT_COMPLETE.destination_addr, tSMSOUT_COMPLETE.message_payload, tSMSOUT_COMPLETE.time_submitted, tSMSOUT_COMPLETE.time_processed, tSMSOUT_COMPLETE.user, tSMSOUT_COMPLETE.status, tSMSSTATUS.desctiption  "
                        + "FROM tSMSOUT_COMPLETE LEFT JOIN tSMSSTATUS ON tSMSOUT_COMPLETE.status=tSMSSTATUS.id inner join tUSER on tSMSOUT_COMPLETE.user = tUSER.username WHERE time_submitted >= '" + startDate + "' AND time_submitted <= '" + endDate + "' and tUSER.agent = '" + user_id + "' order by myid desc";
            } else {
                sql = "SELECT tSMSOUT.id AS myid,tSMSOUT.source_addr, tSMSOUT.destination_addr, tSMSOUT.message_payload, tSMSOUT.time_submitted, tSMSOUT.time_processed, tSMSOUT.user, tSMSOUT.status, tSMSSTATUS.desctiption  FROM tSMSOUT LEFT JOIN tSMSSTATUS ON tSMSOUT.status=tSMSSTATUS.id  LEFT JOIN tUSER ON tUSER.username=tSMSOUT.user WHERE time_submitted >= '" + startDate + "' AND time_submitted <= '" + endDate + "' AND (user = '" + user + "' OR tUSER.agent= '" + adminid + "' OR tUSER.super_account_id='" + adminid + "' )"
                        + "UNION ALL SELECT tSMSOUT_COMPLETE.id AS myid,tSMSOUT_COMPLETE.source_addr, tSMSOUT_COMPLETE.destination_addr, tSMSOUT_COMPLETE.message_payload, tSMSOUT_COMPLETE.time_submitted, tSMSOUT_COMPLETE.time_processed, tSMSOUT_COMPLETE.user, tSMSOUT_COMPLETE.status, tSMSSTATUS.desctiption  FROM tSMSOUT_COMPLETE LEFT JOIN tSMSSTATUS ON tSMSOUT_COMPLETE.status=tSMSSTATUS.id LEFT JOIN tUSER ON tUSER.username=tSMSOUT_COMPLETE.user WHERE time_submitted >= '" + startDate + "' AND time_submitted <= '" + endDate + "' AND (user = '" + user + "' OR tUSER.agent= '" + adminid + "' OR tUSER.super_account_id='" + adminid + "' ) order by myid desc";
                sqlReseller = sql;
            }
            
//            System.out.println("sql: " + sql);

            List<SMSOut> result = new ArrayList<>();

            try (Connection conn = jdbcUtil.getConnectionTodbSMS(); PreparedStatement pstmt = conn.prepareStatement(UserServiceImpl.isReseller().equalsIgnoreCase("none") ? sqlReseller : sql)) {
                try (ResultSet rs = pstmt.executeQuery()) {
//                    if(rs.next()){
//                        System.out.println("Data present");
//                    }else{
//                        System.out.println("No data present");
//                    }
                    while (rs.next()) {
                        SMSOut smsOut = new SMSOut();
                        smsOut.setSourceAddr(rs.getString("source_addr"));
                        smsOut.setDestinationAddr(rs.getString("destination_addr"));
                        smsOut.setMessagePayload(rs.getString("message_payload"));
                        smsOut.setTimeSubmitted(rs.getString("time_submitted"));
                        smsOut.setTimeProcessed(rs.getString("time_processed"));
                        smsOut.setStatus(rs.getString("status").charAt(0));
                        smsOut.setUser(rs.getString("user"));
                        smsOut.setRealStatus(rs.getString("desctiption"));
                        result.add(smsOut);
                    }
                    
                }
//                LOG.log(Level.INFO, "smsout report sql ", sql);

            } catch (Exception e) {
                LOG.log(Level.SEVERE, "Error retrieving smsout report ", e);
            }

            return result;
        } catch (SQLException ex) {
            Logger.getLogger(SMSOutServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @Override
    public List<SMPPOut> fetchSMPPReport(String user, String startDate, String endDate) {
        String sql = "SELECT dbSMPPGateway.tSMSOUT.id AS myid, dbSMPPGateway.tSMSOUT.status, dbSMPPGateway.tSMSOUT.source_addr, dbSMPPGateway.tSMSOUT.destination_addr, "
                + "dbSMPPGateway.tSMSOUT.short_message AS message_payload, dbSMPPGateway.tSMSOUT.time_submitted, dbSMPPGateway.tSMSOUT.time_processed, dbSMPPGateway.tSMSOUT.registered_delivery, "
                + "dbSMPPGateway.tUSER.username AS user, dbSMPPGateway.tSMSSTATUS.desctiption "
                + "FROM dbSMPPGateway.tSMSOUT "
                + "LEFT JOIN dbSMPPGateway.tUSER ON dbSMPPGateway.tSMSOUT.tUSER_id = dbSMPPGateway.tUSER.id "
                + "LEFT JOIN dbSMPPGateway.tSMSSTATUS ON dbSMPPGateway.tSMSOUT.status = dbSMPPGateway.tSMSSTATUS.id "
                + "WHERE dbSMPPGateway.tSMSOUT.time_submitted >= ? AND dbSMPPGateway.tSMSOUT.time_submitted <= ? AND dbSMPPGateway.tUSER.username = ? "
                + "UNION ALL "
                + "SELECT dbSMPPGateway.tSMSOUT_COMPLETE.id AS myid, dbSMPPGateway.tSMSOUT_COMPLETE.status, dbSMPPGateway.tSMSOUT_COMPLETE.source_addr, dbSMPPGateway.tSMSOUT_COMPLETE.destination_addr, "
                + "dbSMPPGateway.tSMSOUT_COMPLETE.short_message, dbSMPPGateway.tSMSOUT_COMPLETE.time_submitted, dbSMPPGateway.tSMSOUT_COMPLETE.time_processed, dbSMPPGateway.tSMSOUT_COMPLETE.registered_delivery, "
                + "dbSMPPGateway.tUSER.username, dbSMPPGateway.tSMSSTATUS.desctiption "
                + "FROM dbSMPPGateway.tSMSOUT_COMPLETE "
                + "LEFT JOIN dbSMPPGateway.tUSER ON dbSMPPGateway.tSMSOUT_COMPLETE.tUSER_id = dbSMPPGateway.tUSER.id "
                + "LEFT JOIN dbSMPPGateway.tSMSSTATUS ON dbSMPPGateway.tSMSOUT_COMPLETE.status = dbSMPPGateway.tSMSSTATUS.id "
                + "WHERE dbSMPPGateway.tSMSOUT_COMPLETE.time_submitted >= ? AND dbSMPPGateway.tSMSOUT_COMPLETE.time_submitted <= ? AND dbSMPPGateway.tUSER.username = ? "
                + "ORDER BY myid DESC LIMIT 50000";

        List<SMPPOut> result = new ArrayList<>();

        try (Connection conn = jdbcUtil.getConnectionTodbSMS(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, startDate);
            pstmt.setString(2, endDate);
            pstmt.setString(3, user);
            pstmt.setString(4, startDate);
            pstmt.setString(5, endDate);
            pstmt.setString(6, user);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    SMPPOut smsOut = new SMPPOut();
                    smsOut.setSourceAddr(rs.getString("source_addr"));
                    smsOut.setDestinationAddr(rs.getString("destination_addr"));
                    smsOut.setMessagePayload(rs.getString("message_payload"));
                    smsOut.setTimeSubmitted(rs.getString("time_submitted"));
                    smsOut.setTimeProcessed(rs.getString("time_processed"));
                    smsOut.setStatus(rs.getString("status").charAt(0));
                    smsOut.setRealStatus(rs.getString("desctiption"));
                    smsOut.setUser(rs.getString("user"));
                    result.add(smsOut);
                }
                LOG.log(Level.INFO, "smpp report sql " + sql);
            }
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "Error retrieving smsout report ", e);
        }

        return result;
    }

    @Override
    public int getTotalSmppCount(String startDate, String endDate, String username) {
        // SQL query to select message_payload (short_message) from both tables
        String sql = "SELECT short_message "
                + "FROM ( "
                + "    SELECT dbSMPPGateway.tSMSOUT.short_message "
                + "    FROM dbSMPPGateway.tSMSOUT "
                + "    LEFT JOIN dbSMPPGateway.tUSER ON dbSMPPGateway.tSMSOUT.tUSER_id = dbSMPPGateway.tUSER.id "
                + "    LEFT JOIN dbSMPPGateway.tSMSSTATUS ON dbSMPPGateway.tSMSOUT.status = dbSMPPGateway.tSMSSTATUS.id "
                + "    WHERE dbSMPPGateway.tSMSOUT.time_submitted >= ? "
                + "      AND dbSMPPGateway.tSMSOUT.time_submitted <= ? "
                + "      AND dbSMPPGateway.tUSER.username = ? "
                + "    UNION ALL "
                + "    SELECT dbSMPPGateway.tSMSOUT_COMPLETE.short_message "
                + "    FROM dbSMPPGateway.tSMSOUT_COMPLETE "
                + "    LEFT JOIN dbSMPPGateway.tUSER ON dbSMPPGateway.tSMSOUT_COMPLETE.tUSER_id = dbSMPPGateway.tUSER.id "
                + "    LEFT JOIN dbSMPPGateway.tSMSSTATUS ON dbSMPPGateway.tSMSOUT_COMPLETE.status = dbSMPPGateway.tSMSSTATUS.id "
                + "    WHERE dbSMPPGateway.tSMSOUT_COMPLETE.time_submitted >= ? "
                + "      AND dbSMPPGateway.tSMSOUT_COMPLETE.time_submitted <= ? "
                + "      AND dbSMPPGateway.tUSER.username = ? "
                + ") AS combined_results "
                + "ORDER BY short_message;";

        int totalSmsCount = 0;

        try (Connection conn = jdbcUtil.getConnectionTodbSMS(); PreparedStatement statement = conn.prepareStatement(sql)) {

            // Set parameters for the prepared statement
            statement.setString(1, startDate);
            statement.setString(2, endDate);
            statement.setString(3, username);
            statement.setString(4, startDate);
            statement.setString(5, endDate);
            statement.setString(6, username);

            // Execute query and process results
            try (ResultSet rs = statement.executeQuery()) {
                while (rs.next()) {
                    System.out.println("data present");
                    String messagePayload = rs.getString("short_message");
                    totalSmsCount += getSmsCount(messagePayload);
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(SMSOutServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
        }

        System.out.println("totalSms count " + totalSmsCount);
        return totalSmsCount;
    }
    //RETURNS TOTAL MESSAGE (PAGES) IN SMS OUT REPORT
    private int getSmsCount(String msg) {
        int ret = 0;

        if (msg == null || msg.length() <= 160) {
            return 1;
        }
        ret = msg.length() / 134;
        if (msg.length() % 134 > 0) {
            ret++;
        }
        return ret;
    }

    public boolean checkForReseller(String username, String password) {
        JdbcUtil util = new JdbcUtil();
        boolean isReseller = false;

        String sql = "SELECT * FROM tUSER WHERE username=? AND password=? AND (admin=1 or admin=5)";
        try {

            Connection conn = util.getConnectionTodbSMS();

            PreparedStatement st = conn.prepareStatement(sql);
            st.setString(1, username);
            st.setString(2, password);
            ResultSet rs = st.executeQuery();
            if (rs != null && rs.next()) {

                FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("admin", rs.getInt("admin"));
                FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("CurretUserID", userId);

                FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("max_total", rs.getInt("max_total"));
//peg
                if (rs.getInt("admin") == 5) {
                    isReseller = true;
                    System.out.println("Reseller is true");
                } else {
                    isReseller = false;
                    System.out.println("reseller is false");
                }

            }
            JdbcUtil.closeConnection(conn);
        } catch (SQLException ex) {
            JdbcUtil.printSQLException(ex);
        }

        return isReseller;
    }

    //set sql to session
    @Override
    public void smsSetSql(String user, String startDate, String endDate, String scheduleStart, String scheduleEnd, Connection conn) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody

    }

    //Normal execution should check the size and execute different sql
    @Override
    public Map<String, Object> userSMSOutReport(String user, String startDate, String endDate, String scheduleStart, String scheduleEnd, Connection conn, int limit) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    /*This is the method that runs on the action listener of the generate report button
     Also get the size of the report 
    
     */
    @Override
    public ResultSet getResultSet(String user, String startDate, String endDate, Connection conn) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody

    }

    ///set sql to session
    //horace changed short_message to short_message and username
    public void smppSetSql(String user, String startDate, String endDate, Connection conn) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody

    }

    //this should proceed if the user clicks proceed
    @Override
    public Map<String, Object> smppOutReport(String user, String startDate, String endDate, Connection conn, int limit) throws SQLException {

        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public Map<String, Object> optOutReport(String startDate, String endDate, Connection conn) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody

    }

    @Override
    public List<String> getUsernames(Connection conn) throws SQLException {

        String sql = "SELECT username FROM tUSER";
        String sqlReseller = "SELECT username FROM tUSER where agent = '" + user_id + "'";
        List<String> result = new ArrayList<>();

        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(UserServiceImpl.isReseller().equalsIgnoreCase("none") ? sqlReseller : sql);
        while (rs.next()) {
            String username = rs.getString("username");
            result.add(username);
        }

        return result;
    }

    @Override
    public List<String> getSMPPUsernames(Connection conn) throws SQLException {

        String sql = "SELECT username FROM dbSMPPGateway.tUSER";
        List<String> result = new ArrayList<>();

        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(sql);

        while (rs.next()) {
            String username = rs.getString("username");
            result.add(username);
        }

        return result;
    }

    @Override
    public Map<String, Object> smsOutGroupBy(String user, String startDate, String endDate, Connection conn) throws SQLException {

        String sql = "SELECT  user, YEAR(time_submitted) year, MONTHNAME(STR_TO_DATE(MONTH(time_submitted), '%m')) month, COUNT(MONTHNAME(STR_TO_DATE(MONTH(time_submitted), '%m'))) frequency from tSMSOUT WHERE time_submitted >= '" + startDate + "' AND time_submitted <= '" + endDate + "' AND user = '" + user + "'  GROUP BY month, year ";

        List<Object[]> results = new ArrayList<>();
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(sql);

        while (rs.next()) {
            Object[] anObject = new Object[3];
            anObject[0] = rs.getInt("year");
            anObject[1] = rs.getString("month");
            anObject[2] = rs.getInt("frequency");
            results.add(anObject);
        }

        Map<String, Object> mapResult = new HashMap<>();
        mapResult.put("result", results);
        mapResult.put("noSMS", 20);

        return mapResult;
    }

    @Override
    public Map<String, String> smsOutGroupByUser(String startDate, String endDate, Connection conn) throws SQLException {

        Calendar startAnotherDate = null;
        Calendar endAnotherDate = null;
        try {
            startAnotherDate = stringToCalendar(startDate);
            endAnotherDate = stringToCalendar(endDate);
        } catch (ParseException ex) {
            Logger.getLogger(SMSOutServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
        }

        // original  String sql = "SELECT time_submitted, user, YEAR(time_submitted) year, MONTHNAME(STR_TO_DATE(MONTH(time_submitted), '%m')) month, COUNT(MONTHNAME(STR_TO_DATE(MONTH(time_submitted), '%m'))) frequency from tSMSOUT WHERE time_submitted >= '" + startDate + "' AND time_submitted <= '" + endDate + "'  GROUP BY user, month, year ORDER BY time_submitted";
        String sql = "SELECT  user, YEAR(time_submitted) year, MONTHNAME(STR_TO_DATE(MONTH(time_submitted), '%m')) month, COUNT(MONTHNAME(STR_TO_DATE(MONTH(time_submitted), '%m'))) frequency from tSMSOUT WHERE time_submitted >= '" + startDate + "' AND time_submitted <= '" + endDate + "'  GROUP BY user, month, year ";

        String sqlReseller = "SELECT t.user, YEAR(t.time_submitted) year, "
                + "MONTHNAME(STR_TO_DATE(MONTH(t.time_submitted), '%m')) month, "
                + "COUNT(MONTHNAME(STR_TO_DATE(MONTH(t.time_submitted), '%m'))) frequency from tSMSOUT t inner join tUSER u "
                + "on t.user = u.username WHERE u.agent = '" + user_id + "' and "
                + "t.time_submitted >= '" + startDate + "' AND t.time_submitted <= '" + endDate + "' "
                + "GROUP BY t.user, month, year ";

        List<Object[]> results = new ArrayList<>();

        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(UserServiceImpl.isReseller().equalsIgnoreCase("none") ? sqlReseller : sql);

        while (rs.next()) {
            Object[] anObject = new Object[4];
            anObject[0] = rs.getInt("year");
            anObject[1] = rs.getString("month");
            anObject[2] = rs.getString("user");
            anObject[3] = rs.getString("frequency");
            results.add(anObject);
        }

        Map<String, Integer> json = new LinkedHashMap<>();
        Set<String> months = new LinkedHashSet<>();
        Set<String> users = new LinkedHashSet<>();

        while (startAnotherDate.before(endAnotherDate)) {
            String month = startAnotherDate.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.getDefault());
            int year = startAnotherDate.get(Calendar.YEAR);
            for (Object[] aResult : results) {
                json.put(year + "-" + month + "-" + aResult[2], 0);
                months.add(aResult[0] + "-" + aResult[1]);
                users.add(String.valueOf(aResult[2]));
            }

            startAnotherDate.add(Calendar.MONTH, 1);
        }

        String madeUp = null;
        for (String aString : json.keySet()) {
            for (Object[] aResult : results) {
                madeUp = aResult[0] + "-" + aResult[1] + "-" + aResult[2];
                if (aString.equals(madeUp)) {
                    json.put(aString, Integer.parseInt(String.valueOf(aResult[3])));
                }
            }

        }
        StringBuilder builder = new StringBuilder();

        for (String aMonth : months) {
            builder.append("[");
            builder.append('"');
            builder.append(aMonth.substring(0, 8));
            builder.append('"');
            builder.append(',');
            for (String aString : json.keySet()) {
                if (aString.contains(aMonth)) {

                    builder.append(json.get(aString));
                    builder.append(",");
                }

            }
            builder.append("]");
            if (builder.length() > 0) {

                if (builder.charAt(builder.lastIndexOf("]") - 1) == ',') {
                    builder.deleteCharAt(builder.lastIndexOf("]") - 1);
                }
            }
            builder.append(",");
        }
        if (builder.length() > 0) {

            builder.deleteCharAt(builder.lastIndexOf(","));

        }
        StringBuilder userBuilder = new StringBuilder();
        userBuilder.append("[");
        for (String aUser : users) {

            userBuilder.append('"');
            userBuilder.append(aUser);
            userBuilder.append('"');
            userBuilder.append(',');
        }
        userBuilder.append(']');
        if (userBuilder.length() > 0) {

            if (userBuilder.charAt(userBuilder.lastIndexOf("]") - 1) == ',') {
                userBuilder.deleteCharAt(userBuilder.lastIndexOf("]") - 1);
            }
        }

        Map<String, String> mapResult = new HashMap<>();
        mapResult.put("data", builder.toString());
        mapResult.put("users", userBuilder.toString());
        return mapResult;
    }

    private Calendar stringToCalendar(String string) throws ParseException {
        Date date = formatter.parse(string);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar;
    }

    @Override
    public String getRealSMSStatus(final String smsID, final Connection conn) throws SQLException {

        String toReturn = null;

        String sql = "SELECT desctiption FROM tSMSSTATUS WHERE id='" + smsID + "'";

        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(sql);

        while (rs.next()) {
            toReturn = rs.getString("desctiption");
        }

        return toReturn;

    }

    @Override
    public int checkIfFileIsLarge(String user, String startDate, String endDate, String scheduleStart, String scheduleEnd, Connection conn) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody

    }
    //enf

    @Override

    public int smppcheckIfFileIsLarge(String user, String startDate, String endDate, String scheduleStart, String scheduleEnd, Connection conn) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody

    }

    @Override
    public Map<String, Object> getSummarySms(String user, String startDate, String endDate, String scheduleStart, String scheduleEnd, Connection conn, int limit) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody

    }

    @Override
    public Map<String, Object> smppgetSummarySms(String user, String startDate, String endDate, Connection conn, int limit) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody

    }

    @Override
    public void generateXSL(String user, String startDate, String endDate, Connection conn) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public void generateSMPPXSL(String user, String startDate, String endDate, Connection conn) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public void generateOPTXSL(String startDate, String endDate, Connection conn) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

}
