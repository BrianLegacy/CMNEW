/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ke.co.mspace.nonsmppmanager.service;

import java.io.IOException;
import java.sql.Connection;
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
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.context.FacesContext;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import ke.co.mspace.export.LargeFileExport;
import ke.co.mspace.nonsmppmanager.model.OPTOut;
import ke.co.mspace.nonsmppmanager.model.SMPPOut;
import ke.co.mspace.nonsmppmanager.model.SMSOut;
import ke.co.mspace.nonsmppmanager.util.JsfUtil;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.PrintSetup;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;

/**
 *
 * @author Norrey Osako
 */
public class SMSOutServiceImpl implements SMSOutServiceApi {
    private static final Logger LOGGER = Logger.getLogger("SMSOutServiceImpl.class" );

    public static final String ANSI_BLUE = "\u001B[34m";
     public static final String ANSI_RESET = "\u001B[0m";

    private static final Logger LOG = Logger.getLogger(SMSOutServiceImpl.class.getName());

    private final static DateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");

    private final String user_id = FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("user_id").toString();
    private int limit=500;
    public SMSOutServiceImpl() {

    }
    
    //set sql to session
    public void smsSetSql(String user, String startDate, String endDate,String scheduleStart,String scheduleEnd, Connection conn) throws SQLException {

        DateFormat outFormat = new SimpleDateFormat("yyyy-MM-dd");
     

        String query = " SELECT id from tUSER WHERE username='" + user + "'";
//        System.out.println("Th selected user id report" + user);
        Statement stmt1 = conn.createStatement();
        ResultSet rs1 = stmt1.executeQuery(query);
        String adminid = null;
        while (rs1.next()) {
            adminid = rs1.getString("id");
//            System.out.println("The super ID" + adminid);
        }
        String sql = null;
        String sqlReseller = null;
        if (user == null) {
            //SELECT tSMSOUT.id AS myid,tSMSOUT.source_addr, tSMSOUT.destination_addr, tSMSOUT.message_payload, tSMSOUT.time_submitted, tSMSOUT.time_processed, tSMSOUT.user, tSMSOUT.status, tSMSSTATUS.desctiption  FROM tSMSOUT LEFT JOIN tSMSSTATUS ON tSMSOUT.status=tSMSSTATUS.id WHERE time_submitted >= '" + startDate + "' AND time_submitted <= '" + endDate + "' 
            //UNION ALL SELECT tSMSOUT_COMPLETE.id AS myid,tSMSOUT_COMPLETE.source_addr, tSMSOUT_COMPLETE.destination_addr, tSMSOUT_COMPLETE.message_payload, tSMSOUT_COMPLETE.time_submitted, tSMSOUT_COMPLETE.time_processed, tSMSOUT_COMPLETE.user, tSMSOUT_COMPLETE.status, tSMSSTATUS.desctiption  FROM tSMSOUT_COMPLETE LEFT JOIN tSMSSTATUS ON tSMSOUT_COMPLETE.status=tSMSSTATUS.id WHERE time_submitted >= '" + startDate + "' AND time_submitted <= '" + endDate + "' order by myid desc";

            sql = "SELECT tSMSOUT.id AS myid,tSMSOUT.source_addr, tSMSOUT.destination_addr, tSMSOUT.message_payload, tSMSOUT.time_submitted, tSMSOUT.time_processed, "
                    + "tSMSOUT.user, tSMSOUT.status, tSMSSTATUS.desctiption  FROM tSMSOUT LEFT JOIN tSMSSTATUS ON tSMSOUT.status=tSMSSTATUS.id WHERE time_submitted >= '" + startDate + "' "
                    + "AND time_submitted <= '" + endDate + "' "
                    + "UNION ALL SELECT tSMSOUT_COMPLETE.id AS myid,tSMSOUT_COMPLETE.source_addr, tSMSOUT_COMPLETE.destination_addr, tSMSOUT_COMPLETE.message_payload, "
                    + "tSMSOUT_COMPLETE.time_submitted, tSMSOUT_COMPLETE.time_processed, tSMSOUT_COMPLETE.user, tSMSOUT_COMPLETE.status, tSMSSTATUS.desctiption  FROM tSMSOUT_COMPLETE"
                    + " LEFT JOIN tSMSSTATUS ON tSMSOUT_COMPLETE.status=tSMSSTATUS.id WHERE time_submitted >= '" + startDate + "' AND time_submitted <= '" + endDate + "'   UNION ALL "
                    + "select sc.id as myId , sc.source as source_addr,sc.dest as destination_addr,  sc.message as message_payload, sc.sendTime as time_submitted, "
                    + " sc.sendTime as time_processed ,sc.username as user, 11 as status ,'Scheduled' as  desctiption from tUSERSMSSCHEDULE sc where  sc.sendTime >= '"+scheduleStart+"' AND sc.sendTime  <= '"+scheduleEnd+"'   order by myid desc";

            sqlReseller = "SELECT tSMSOUT.id AS myid,tSMSOUT.source_addr, tSMSOUT.destination_addr, tSMSOUT.message_payload, tSMSOUT.time_submitted, tSMSOUT.time_processed, tSMSOUT.user, tSMSOUT.status, tSMSSTATUS.desctiption  "
                    + "FROM tSMSOUT LEFT JOIN tSMSSTATUS ON tSMSOUT.status=tSMSSTATUS.id inner join tUSER on tSMSOUT.user = tUSER.username "
                    + "WHERE time_submitted >= '" + startDate + "' AND time_submitted <= '" + endDate + "' and tUSER.agent = '"+user_id+"' UNION ALL "
                    + "SELECT tSMSOUT_COMPLETE.id AS myid,tSMSOUT_COMPLETE.source_addr, tSMSOUT_COMPLETE.destination_addr, tSMSOUT_COMPLETE.message_payload,"
                    + " tSMSOUT_COMPLETE.time_submitted, tSMSOUT_COMPLETE.time_processed, tSMSOUT_COMPLETE.user, tSMSOUT_COMPLETE.status, tSMSSTATUS.desctiption  "
                    + "FROM tSMSOUT_COMPLETE LEFT JOIN tSMSSTATUS ON tSMSOUT_COMPLETE.status=tSMSSTATUS.id inner join tUSER on tSMSOUT_COMPLETE.user = tUSER.username"
                    + " WHERE time_submitted >= " + startDate + " AND time_submitted <= '" + endDate + "' and tUSER.agent = '" + user_id + "' order by myid desc";

        } else if (user.isEmpty()) {
            //sql = "SELECT tSMSOUT.id AS myid,tSMSOUT.source_addr, tSMSOUT.destination_addr, tSMSOUT.message_payload, tSMSOUT.time_submitted, tSMSOUT.time_processed, tSMSOUT.user, tSMSOUT.status, tSMSSTATUS.desctiption  FROM tSMSOUT LEFT JOIN tSMSSTATUS ON tSMSOUT.status=tSMSSTATUS.id WHERE time_submitted >= '" + startDate + "' AND time_submitted <= '" + endDate + "'
            //UNION ALL SELECT tSMSOUT_COMPLETE.id AS myid,tSMSOUT_COMPLETE.source_addr, tSMSOUT_COMPLETE.destination_addr, tSMSOUT_COMPLETE.message_payload, tSMSOUT_COMPLETE.time_submitted, tSMSOUT_COMPLETE.time_processed, tSMSOUT_COMPLETE.user, tSMSOUT_COMPLETE.status, tSMSSTATUS.desctiption  FROM tSMSOUT_COMPLETE LEFT JOIN tSMSSTATUS ON tSMSOUT_COMPLETE.status=tSMSSTATUS.id WHERE time_submitted >= '" + startDate + "' AND time_submitted <= '" + endDate + "' order by myid desc";

            sql = "SELECT tSMSOUT.id AS myid,tSMSOUT.source_addr, tSMSOUT.destination_addr, tSMSOUT.message_payload, tSMSOUT.time_submitted, tSMSOUT.time_processed, tSMSOUT.user, tSMSOUT.status, tSMSSTATUS.desctiption  FROM tSMSOUT LEFT JOIN tSMSSTATUS ON tSMSOUT.status=tSMSSTATUS.id WHERE time_submitted >= '" + startDate + "' AND time_submitted <= '" + endDate + "' "
                    + "UNION ALL SELECT tSMSOUT_COMPLETE.id AS myid,tSMSOUT_COMPLETE.source_addr, tSMSOUT_COMPLETE.destination_addr, tSMSOUT_COMPLETE.message_payload, tSMSOUT_COMPLETE.time_submitted, tSMSOUT_COMPLETE.time_processed, tSMSOUT_COMPLETE.user, tSMSOUT_COMPLETE.status, tSMSSTATUS.desctiption  FROM tSMSOUT_COMPLETE LEFT JOIN tSMSSTATUS ON tSMSOUT_COMPLETE.status=tSMSSTATUS.id WHERE time_submitted >= '" + startDate + "' AND time_submitted <= '" + endDate + "' UNION ALL "
                    + "select sc.id as myId , sc.source as source_addr,sc.dest as destination_addr,  sc.message as message_payload, sc.sendTime as time_submitted, "
                    + " sc.sendTime as time_processed ,sc.username as user, 11 as status ,'Scheduled' as  desctiption from tUSERSMSSCHEDULE sc where  sc.sendTime >= '"+scheduleStart+"' AND sc.sendTime  <= '"+scheduleEnd+"'   order by myid desc";

            sqlReseller = "SELECT tSMSOUT.id AS myid,tSMSOUT.source_addr, tSMSOUT.destination_addr, tSMSOUT.message_payload, tSMSOUT.time_submitted, tSMSOUT.time_processed, tSMSOUT.user, tSMSOUT.status, tSMSSTATUS.desctiption  "
                    + "FROM tSMSOUT LEFT JOIN tSMSSTATUS ON tSMSOUT.status=tSMSSTATUS.id inner join tUSER on tSMSOUT.user = tUSER.username "
                    + "WHERE time_submitted >= '"+startDate+"' AND time_submitted <= '"+endDate+"' and tUSER.agent = '"+user_id+"' UNION ALL "
                    + "SELECT tSMSOUT_COMPLETE.id AS myid,tSMSOUT_COMPLETE.source_addr, tSMSOUT_COMPLETE.destination_addr, tSMSOUT_COMPLETE.message_payload, tSMSOUT_COMPLETE.time_submitted, tSMSOUT_COMPLETE.time_processed, tSMSOUT_COMPLETE.user, tSMSOUT_COMPLETE.status, tSMSSTATUS.desctiption  "
                    + "FROM tSMSOUT_COMPLETE LEFT JOIN tSMSSTATUS ON tSMSOUT_COMPLETE.status=tSMSSTATUS.id inner join tUSER on tSMSOUT_COMPLETE.user = tUSER.username WHERE time_submitted >= '"+startDate+"' AND time_submitted <= '"+endDate+"' and tUSER.agent = '" + user_id + "' order by myid desc";
        } else {
            //  sql = "SELECT tSMSOUT.id AS myid,tSMSOUT.source_addr, tSMSOUT.destination_addr, tSMSOUT.message_payload, tSMSOUT.time_submitted, tSMSOUT.time_processed, tSMSOUT.user, tSMSOUT.status, tSMSSTATUS.desctiption  FROM tSMSOUT LEFT JOIN tSMSSTATUS ON tSMSOUT.status=tSMSSTATUS.id LEFT JOIN tUSER ON tUSER.username=tSMSOUT.user  WHERE time_submitted >= '" + startDate + "' AND time_submitted <= '" + endDate + "' AND (user = '" + user + "' OR tUSER.super_account_id= '" + adminid + "' ) 
            //UNION ALL SELECT tSMSOUT_COMPLETE.id AS myid,tSMSOUT_COMPLETE.source_addr, tSMSOUT_COMPLETE.destination_addr, tSMSOUT_COMPLETE.message_payload, tSMSOUT_COMPLETE.time_submitted, tSMSOUT_COMPLETE.time_processed, tSMSOUT_COMPLETE.user, tSMSOUT_COMPLETE.status, tSMSSTATUS.desctiption  FROM tSMSOUT_COMPLETE LEFT JOIN tSMSSTATUS ON tSMSOUT_COMPLETE.status=tSMSSTATUS.id LEFT JOIN tUSER ON tUSER.username=tSMSOUT_COMPLETE.user  WHERE time_submitted >= '" + startDate + "' AND time_submitted <= '" + endDate + "' AND (user = '" + user + "'  OR tUSER.super_account_id= '" + adminid + "' ) order by myid desc";
            sql = "SELECT tSMSOUT.id AS myid,tSMSOUT.source_addr, tSMSOUT.destination_addr, tSMSOUT.message_payload, tSMSOUT.time_submitted, tSMSOUT.time_processed, tSMSOUT.user, tSMSOUT.status, tSMSSTATUS.desctiption  FROM tSMSOUT LEFT JOIN tSMSSTATUS ON tSMSOUT.status=tSMSSTATUS.id  LEFT JOIN tUSER ON tUSER.username=tSMSOUT.user WHERE time_submitted >= '" + startDate + "' AND time_submitted <= '" + endDate + "' AND (user = '" + user + "' OR tUSER.agent= '" + adminid + "' OR tUSER.super_account_id='" + adminid + "' )"
                    + "UNION ALL SELECT tSMSOUT_COMPLETE.id AS myid,tSMSOUT_COMPLETE.source_addr, tSMSOUT_COMPLETE.destination_addr, tSMSOUT_COMPLETE.message_payload, tSMSOUT_COMPLETE.time_submitted, tSMSOUT_COMPLETE.time_processed, tSMSOUT_COMPLETE.user, tSMSOUT_COMPLETE.status, tSMSSTATUS.desctiption  FROM tSMSOUT_COMPLETE LEFT JOIN tSMSSTATUS ON tSMSOUT_COMPLETE.status=tSMSSTATUS.id LEFT JOIN tUSER ON tUSER.username=tSMSOUT_COMPLETE.user WHERE time_submitted >= '" + startDate + "' AND time_submitted <= '" + endDate + "' AND (user = '" + user + "' OR tUSER.agent= '" + adminid + "' OR tUSER.super_account_id='" + adminid + "' ) order by myid desc";
//            System.out.println(sql);
            sqlReseller = sql;
        }

        //System.out.println(sqlReseller);
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("reportSQL", UserServiceImpl.isReseller().equalsIgnoreCase("none") ? sqlReseller : sql);
        
    }

    //Normal execution should check the size and execute different sql
    @Override
    public Map<String, Object> userSMSOutReport(String user, String startDate, String endDate,String scheduleStart,String scheduleEnd, Connection conn, int limit) throws SQLException {

        DateFormat outFormat = new SimpleDateFormat("yyyy-MM-dd");


        String query = " SELECT id from tUSER WHERE username='" + user + "'";

        Statement stmt1 = conn.createStatement();
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
                    + " sc.sendTime as time_processed ,sc.username as user, 11 as status ,'Scheduled' as  desctiption from tUSERSMSSCHEDULE sc where  sc.sendTime >= '"+scheduleStart+"' AND sc.sendTime  <= '"+scheduleEnd+"'   order by myid desc";

            sqlReseller = "SELECT tSMSOUT.id AS myid,tSMSOUT.source_addr, tSMSOUT.destination_addr, tSMSOUT.message_payload, tSMSOUT.time_submitted, tSMSOUT.time_processed, tSMSOUT.user, tSMSOUT.status, tSMSSTATUS.desctiption  "
                    + "FROM tSMSOUT LEFT JOIN tSMSSTATUS ON tSMSOUT.status=tSMSSTATUS.id inner join tUSER on tSMSOUT.user = tUSER.username "
                    + "WHERE time_submitted >= '" + startDate + "' AND time_submitted <= '" + endDate + "' and tUSER.agent = '"+user_id+"' UNION ALL "
                    + "SELECT tSMSOUT_COMPLETE.id AS myid,tSMSOUT_COMPLETE.source_addr, tSMSOUT_COMPLETE.destination_addr, tSMSOUT_COMPLETE.message_payload,"
                    + " tSMSOUT_COMPLETE.time_submitted, tSMSOUT_COMPLETE.time_processed, tSMSOUT_COMPLETE.user, tSMSOUT_COMPLETE.status, tSMSSTATUS.desctiption  "
                    + "FROM tSMSOUT_COMPLETE LEFT JOIN tSMSSTATUS ON tSMSOUT_COMPLETE.status=tSMSSTATUS.id inner join tUSER on tSMSOUT_COMPLETE.user = tUSER.username"
                    + " WHERE time_submitted >= " + startDate + " AND time_submitted <= '" + endDate + "' and tUSER.agent = '" + user_id + "' order by myid desc";

        } else if (user.isEmpty()) {
            //sql = "SELECT tSMSOUT.id AS myid,tSMSOUT.source_addr, tSMSOUT.destination_addr, tSMSOUT.message_payload, tSMSOUT.time_submitted, tSMSOUT.time_processed, tSMSOUT.user, tSMSOUT.status, tSMSSTATUS.desctiption  FROM tSMSOUT LEFT JOIN tSMSSTATUS ON tSMSOUT.status=tSMSSTATUS.id WHERE time_submitted >= '" + startDate + "' AND time_submitted <= '" + endDate + "'
            //UNION ALL SELECT tSMSOUT_COMPLETE.id AS myid,tSMSOUT_COMPLETE.source_addr, tSMSOUT_COMPLETE.destination_addr, tSMSOUT_COMPLETE.message_payload, tSMSOUT_COMPLETE.time_submitted, tSMSOUT_COMPLETE.time_processed, tSMSOUT_COMPLETE.user, tSMSOUT_COMPLETE.status, tSMSSTATUS.desctiption  FROM tSMSOUT_COMPLETE LEFT JOIN tSMSSTATUS ON tSMSOUT_COMPLETE.status=tSMSSTATUS.id WHERE time_submitted >= '" + startDate + "' AND time_submitted <= '" + endDate + "' order by myid desc";

            sql = "SELECT tSMSOUT.id AS myid,tSMSOUT.source_addr, tSMSOUT.destination_addr, tSMSOUT.message_payload, tSMSOUT.time_submitted, tSMSOUT.time_processed, tSMSOUT.user, tSMSOUT.status, tSMSSTATUS.desctiption  FROM tSMSOUT LEFT JOIN tSMSSTATUS ON tSMSOUT.status=tSMSSTATUS.id WHERE time_submitted >= '" + startDate + "' AND time_submitted <= '" + endDate + "' "
                    + "UNION ALL SELECT tSMSOUT_COMPLETE.id AS myid,tSMSOUT_COMPLETE.source_addr, tSMSOUT_COMPLETE.destination_addr, tSMSOUT_COMPLETE.message_payload, tSMSOUT_COMPLETE.time_submitted, tSMSOUT_COMPLETE.time_processed, tSMSOUT_COMPLETE.user, tSMSOUT_COMPLETE.status, tSMSSTATUS.desctiption  FROM tSMSOUT_COMPLETE LEFT JOIN tSMSSTATUS ON tSMSOUT_COMPLETE.status=tSMSSTATUS.id WHERE time_submitted >= '" + startDate + "' AND time_submitted <= '" + endDate + "' UNION ALL "
                    + "select sc.id as myId , sc.source as source_addr,sc.dest as destination_addr,  sc.message as message_payload, sc.sendTime as time_submitted, "
                    + " sc.sendTime as time_processed ,sc.username as user, 11 as status ,'Scheduled' as  desctiption from tUSERSMSSCHEDULE sc where  sc.sendTime >= '"+scheduleStart+"' AND sc.sendTime  <= '"+scheduleEnd+"'   order by myid desc";

            sqlReseller = "SELECT tSMSOUT.id AS myid,tSMSOUT.source_addr, tSMSOUT.destination_addr, tSMSOUT.message_payload, tSMSOUT.time_submitted, tSMSOUT.time_processed, tSMSOUT.user, tSMSOUT.status, tSMSSTATUS.desctiption  "
                    + "FROM tSMSOUT LEFT JOIN tSMSSTATUS ON tSMSOUT.status=tSMSSTATUS.id inner join tUSER on tSMSOUT.user = tUSER.username "
                    + "WHERE time_submitted >= '"+startDate+"' AND time_submitted <= '"+endDate+"' and tUSER.agent = '"+user_id+"' UNION ALL "
                    + "SELECT tSMSOUT_COMPLETE.id AS myid,tSMSOUT_COMPLETE.source_addr, tSMSOUT_COMPLETE.destination_addr, tSMSOUT_COMPLETE.message_payload, tSMSOUT_COMPLETE.time_submitted, tSMSOUT_COMPLETE.time_processed, tSMSOUT_COMPLETE.user, tSMSOUT_COMPLETE.status, tSMSSTATUS.desctiption  "
                    + "FROM tSMSOUT_COMPLETE LEFT JOIN tSMSSTATUS ON tSMSOUT_COMPLETE.status=tSMSSTATUS.id inner join tUSER on tSMSOUT_COMPLETE.user = tUSER.username WHERE time_submitted >= '"+startDate+"' AND time_submitted <= '"+endDate+"' and tUSER.agent = '" + user_id + "' order by myid desc";
        } else {
             sql = "SELECT tSMSOUT.id AS myid,tSMSOUT.source_addr, tSMSOUT.destination_addr, tSMSOUT.message_payload, tSMSOUT.time_submitted, tSMSOUT.time_processed, tSMSOUT.user, tSMSOUT.status, tSMSSTATUS.desctiption  FROM tSMSOUT LEFT JOIN tSMSSTATUS ON tSMSOUT.status=tSMSSTATUS.id  LEFT JOIN tUSER ON tUSER.username=tSMSOUT.user WHERE time_submitted >= '" + startDate + "' AND time_submitted <= '" + endDate + "' AND (user = '" + user + "' OR tUSER.agent= '" + adminid + "' OR tUSER.super_account_id='" + adminid + "' )"
                    + "UNION ALL SELECT tSMSOUT_COMPLETE.id AS myid,tSMSOUT_COMPLETE.source_addr, tSMSOUT_COMPLETE.destination_addr, tSMSOUT_COMPLETE.message_payload, tSMSOUT_COMPLETE.time_submitted, tSMSOUT_COMPLETE.time_processed, tSMSOUT_COMPLETE.user, tSMSOUT_COMPLETE.status, tSMSSTATUS.desctiption  FROM tSMSOUT_COMPLETE LEFT JOIN tSMSSTATUS ON tSMSOUT_COMPLETE.status=tSMSSTATUS.id LEFT JOIN tUSER ON tUSER.username=tSMSOUT_COMPLETE.user WHERE time_submitted >= '" + startDate + "' AND time_submitted <= '" + endDate + "' AND (user = '" + user + "' OR tUSER.agent= '" + adminid + "' OR tUSER.super_account_id='" + adminid + "' ) order by myid desc";
//            System.out.println(sql);
            sqlReseller = sql;
        }
        
        String sqlLimit="";
        if(limit==0){
           sqlLimit="";
        }
        else {
            sqlLimit="limit "+limit+1;

        }
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("reportSQL", UserServiceImpl.isReseller().equalsIgnoreCase("none") ? sqlReseller : sql);
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(UserServiceImpl.isReseller().equalsIgnoreCase("none") ? sqlReseller+" "+sqlLimit : sql+" "+sqlLimit);

        SMSOut smsout = new SMSOut();
        List<SMSOut> result = new ArrayList<>();

        int count=0;
        while (rs.next()) {

            count++;
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
        int noSMS = 0;
        for (SMSOut sms : result) {
            noSMS += sms.getSmsCount();
        }

        Map<String, Object> mapResult = new HashMap<>();
        mapResult.put("result", result);
        mapResult.put("noSMS", noSMS);
        //} end of if else
//System.out.println("Total Rows:"+count);
        return mapResult;
    }

    /*This is the method that runs on the action listener of the generate report button
     Also get the size of the report 
    
     */
    @Override
    public ResultSet getResultSet(String user, String startDate, String endDate, Connection conn) {
        ResultSet rs = null;
        try {
            String query = " SELECT id from tUSER WHERE username='" + user + "'";
            //System.out.println("Th selected user id report" + user);
            Statement stmt1 = conn.createStatement();
            ResultSet rs1 = stmt1.executeQuery(query);
            String adminid = null;
            while (rs1.next()) {
                adminid = rs1.getString("id");
               // System.out.println("The super ID" + adminid);
            }
            String sql = null;
            String sqlReseller = null;
            if (user == null) {
                //SELECT tSMSOUT.id AS myid,tSMSOUT.source_addr, tSMSOUT.destination_addr, tSMSOUT.message_payload, tSMSOUT.time_submitted, tSMSOUT.time_processed, tSMSOUT.user, tSMSOUT.status, tSMSSTATUS.desctiption  FROM tSMSOUT LEFT JOIN tSMSSTATUS ON tSMSOUT.status=tSMSSTATUS.id WHERE time_submitted >= '" + startDate + "' AND time_submitted <= '" + endDate + "'
                //UNION ALL SELECT tSMSOUT_COMPLETE.id AS myid,tSMSOUT_COMPLETE.source_addr, tSMSOUT_COMPLETE.destination_addr, tSMSOUT_COMPLETE.message_payload, tSMSOUT_COMPLETE.time_submitted, tSMSOUT_COMPLETE.time_processed, tSMSOUT_COMPLETE.user, tSMSOUT_COMPLETE.status, tSMSSTATUS.desctiption  FROM tSMSOUT_COMPLETE LEFT JOIN tSMSSTATUS ON tSMSOUT_COMPLETE.status=tSMSSTATUS.id WHERE time_submitted >= '" + startDate + "' AND time_submitted <= '" + endDate + "' order by myid desc";

                sql = "SELECT tSMSOUT.id AS myid,tSMSOUT.source_addr, tSMSOUT.destination_addr, tSMSOUT.message_payload, tSMSOUT.time_submitted, tSMSOUT.time_processed, "
                        + "tSMSOUT.user, tSMSOUT.status, tSMSSTATUS.desctiption  FROM tSMSOUT LEFT JOIN tSMSSTATUS ON tSMSOUT.status=tSMSSTATUS.id WHERE time_submitted >= '" + startDate + "' "
                        + "AND time_submitted <= '" + endDate + "' "
                        + "UNION ALL SELECT tSMSOUT_COMPLETE.id AS myid,tSMSOUT_COMPLETE.source_addr, tSMSOUT_COMPLETE.destination_addr, tSMSOUT_COMPLETE.message_payload, "
                        + "tSMSOUT_COMPLETE.time_submitted, tSMSOUT_COMPLETE.time_processed, tSMSOUT_COMPLETE.user, tSMSOUT_COMPLETE.status, tSMSSTATUS.desctiption  FROM tSMSOUT_COMPLETE"
                        + " LEFT JOIN tSMSSTATUS ON tSMSOUT_COMPLETE.status=tSMSSTATUS.id WHERE time_submitted >= '" + startDate + "' AND time_submitted <= '" + endDate + "' order by myid desc";

                sqlReseller = "SELECT tSMSOUT.id AS myid,tSMSOUT.source_addr, tSMSOUT.destination_addr, tSMSOUT.message_payload, tSMSOUT.time_submitted, tSMSOUT.time_processed, tSMSOUT.user, tSMSOUT.status, tSMSSTATUS.desctiption  "
                        + "FROM tSMSOUT LEFT JOIN tSMSSTATUS ON tSMSOUT.status=tSMSSTATUS.id inner join tUSER on tSMSOUT.user = tUSER.username "
                        + "WHERE time_submitted >= '20180829000000' AND time_submitted <= '20180829235959' and tUSER.agent = '97' UNION ALL "
                        + "SELECT tSMSOUT_COMPLETE.id AS myid,tSMSOUT_COMPLETE.source_addr, tSMSOUT_COMPLETE.destination_addr, tSMSOUT_COMPLETE.message_payload,"
                        + " tSMSOUT_COMPLETE.time_submitted, tSMSOUT_COMPLETE.time_processed, tSMSOUT_COMPLETE.user, tSMSOUT_COMPLETE.status, tSMSSTATUS.desctiption  "
                        + "FROM tSMSOUT_COMPLETE LEFT JOIN tSMSSTATUS ON tSMSOUT_COMPLETE.status=tSMSSTATUS.id inner join tUSER on tSMSOUT_COMPLETE.user = tUSER.username"
                        + " WHERE time_submitted >= '20180829000000' AND time_submitted <= '20180829235959' and tUSER.agent = '" + user_id + "' order by myid desc";

            } else if (user.isEmpty()) {
                //sql = "SELECT tSMSOUT.id AS myid,tSMSOUT.source_addr, tSMSOUT.destination_addr, tSMSOUT.message_payload, tSMSOUT.time_submitted, tSMSOUT.time_processed, tSMSOUT.user, tSMSOUT.status, tSMSSTATUS.desctiption  FROM tSMSOUT LEFT JOIN tSMSSTATUS ON tSMSOUT.status=tSMSSTATUS.id WHERE time_submitted >= '" + startDate + "' AND time_submitted <= '" + endDate + "'
                //UNION ALL SELECT tSMSOUT_COMPLETE.id AS myid,tSMSOUT_COMPLETE.source_addr, tSMSOUT_COMPLETE.destination_addr, tSMSOUT_COMPLETE.message_payload, tSMSOUT_COMPLETE.time_submitted, tSMSOUT_COMPLETE.time_processed, tSMSOUT_COMPLETE.user, tSMSOUT_COMPLETE.status, tSMSSTATUS.desctiption  FROM tSMSOUT_COMPLETE LEFT JOIN tSMSSTATUS ON tSMSOUT_COMPLETE.status=tSMSSTATUS.id WHERE time_submitted >= '" + startDate + "' AND time_submitted <= '" + endDate + "' order by myid desc";

                sql = "SELECT tSMSOUT.id AS myid,tSMSOUT.source_addr, tSMSOUT.destination_addr, tSMSOUT.message_payload, tSMSOUT.time_submitted, tSMSOUT.time_processed, tSMSOUT.user, tSMSOUT.status, tSMSSTATUS.desctiption  FROM tSMSOUT LEFT JOIN tSMSSTATUS ON tSMSOUT.status=tSMSSTATUS.id WHERE time_submitted >= '" + startDate + "' AND time_submitted <= '" + endDate + "' "
                        + "UNION ALL SELECT tSMSOUT_COMPLETE.id AS myid,tSMSOUT_COMPLETE.source_addr, tSMSOUT_COMPLETE.destination_addr, tSMSOUT_COMPLETE.message_payload, tSMSOUT_COMPLETE.time_submitted, tSMSOUT_COMPLETE.time_processed, tSMSOUT_COMPLETE.user, tSMSOUT_COMPLETE.status, tSMSSTATUS.desctiption  FROM tSMSOUT_COMPLETE LEFT JOIN tSMSSTATUS ON tSMSOUT_COMPLETE.status=tSMSSTATUS.id WHERE time_submitted >= '" + startDate + "' AND time_submitted <= '" + endDate + "' order by myid desc";

                sqlReseller = "SELECT tSMSOUT.id AS myid,tSMSOUT.source_addr, tSMSOUT.destination_addr, tSMSOUT.message_payload, tSMSOUT.time_submitted, tSMSOUT.time_processed, tSMSOUT.user, tSMSOUT.status, tSMSSTATUS.desctiption  "
                        + "FROM tSMSOUT LEFT JOIN tSMSSTATUS ON tSMSOUT.status=tSMSSTATUS.id inner join tUSER on tSMSOUT.user = tUSER.username "
                        + "WHERE time_submitted >= '20180829000000' AND time_submitted <= '20180829235959' and tUSER.agent = '97' UNION ALL "
                        + "SELECT tSMSOUT_COMPLETE.id AS myid,tSMSOUT_COMPLETE.source_addr, tSMSOUT_COMPLETE.destination_addr, tSMSOUT_COMPLETE.message_payload, tSMSOUT_COMPLETE.time_submitted, tSMSOUT_COMPLETE.time_processed, tSMSOUT_COMPLETE.user, tSMSOUT_COMPLETE.status, tSMSSTATUS.desctiption  "
                        + "FROM tSMSOUT_COMPLETE LEFT JOIN tSMSSTATUS ON tSMSOUT_COMPLETE.status=tSMSSTATUS.id inner join tUSER on tSMSOUT_COMPLETE.user = tUSER.username WHERE time_submitted >= '20180829000000' AND time_submitted <= '20180829235959' and tUSER.agent = '" + user_id + "' order by myid desc";

            } else {
                //  sql = "SELECT tSMSOUT.id AS myid,tSMSOUT.source_addr, tSMSOUT.destination_addr, tSMSOUT.message_payload, tSMSOUT.time_submitted, tSMSOUT.time_processed, tSMSOUT.user, tSMSOUT.status, tSMSSTATUS.desctiption  FROM tSMSOUT LEFT JOIN tSMSSTATUS ON tSMSOUT.status=tSMSSTATUS.id LEFT JOIN tUSER ON tUSER.username=tSMSOUT.user  WHERE time_submitted >= '" + startDate + "' AND time_submitted <= '" + endDate + "' AND (user = '" + user + "' OR tUSER.super_account_id= '" + adminid + "' )
                //UNION ALL SELECT tSMSOUT_COMPLETE.id AS myid,tSMSOUT_COMPLETE.source_addr, tSMSOUT_COMPLETE.destination_addr, tSMSOUT_COMPLETE.message_payload, tSMSOUT_COMPLETE.time_submitted, tSMSOUT_COMPLETE.time_processed, tSMSOUT_COMPLETE.user, tSMSOUT_COMPLETE.status, tSMSSTATUS.desctiption  FROM tSMSOUT_COMPLETE LEFT JOIN tSMSSTATUS ON tSMSOUT_COMPLETE.status=tSMSSTATUS.id LEFT JOIN tUSER ON tUSER.username=tSMSOUT_COMPLETE.user  WHERE time_submitted >= '" + startDate + "' AND time_submitted <= '" + endDate + "' AND (user = '" + user + "'  OR tUSER.super_account_id= '" + adminid + "' ) order by myid desc";

                sql = "SELECT tSMSOUT.id AS myid,tSMSOUT.source_addr, tSMSOUT.destination_addr, tSMSOUT.message_payload, tSMSOUT.time_submitted, tSMSOUT.time_processed, tSMSOUT.user, tSMSOUT.status, tSMSSTATUS.desctiption  FROM tSMSOUT LEFT JOIN tSMSSTATUS ON tSMSOUT.status=tSMSSTATUS.id  LEFT JOIN tUSER ON tUSER.username=tSMSOUT.user WHERE time_submitted >= '" + startDate + "' AND time_submitted <= '" + endDate + "' AND (user = '" + user + "' OR tUSER.agent= '" + adminid + "' OR tUSER.super_account_id='" + adminid + "' )"
                        + "UNION ALL SELECT tSMSOUT_COMPLETE.id AS myid,tSMSOUT_COMPLETE.source_addr, tSMSOUT_COMPLETE.destination_addr, tSMSOUT_COMPLETE.message_payload, tSMSOUT_COMPLETE.time_submitted, tSMSOUT_COMPLETE.time_processed, tSMSOUT_COMPLETE.user, tSMSOUT_COMPLETE.status, tSMSSTATUS.desctiption  FROM tSMSOUT_COMPLETE LEFT JOIN tSMSSTATUS ON tSMSOUT_COMPLETE.status=tSMSSTATUS.id LEFT JOIN tUSER ON tUSER.username=tSMSOUT_COMPLETE.user WHERE time_submitted >= '" + startDate + "' AND time_submitted <= '" + endDate + "' AND (user = '" + user + "' OR tUSER.agent= '" + adminid + "' OR tUSER.super_account_id='" + adminid + "' ) order by myid desc";

                sqlReseller = sql;
            }

            //System.out.println(sql);
            Statement stmt = conn.createStatement();
            //System.out.println("Putting slq to session " + sql);
            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("reportSQL", UserServiceImpl.isReseller().equalsIgnoreCase("none") ? sqlReseller : sql);
            rs = stmt.executeQuery(UserServiceImpl.isReseller().equalsIgnoreCase("none") ? sqlReseller : sql);
            //check should come here ..if user selects proceed and the rows are
            //more than 100,run the code else just continue executing but limit the number of rows
            //System.out.println("The last row in the table::::<<<<<>>>>>:::: " + rs.getRow());
            //Set the property size here 
            rs.last();
            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("ListSize", rs.getRow());

            rs.first();

        } catch (SQLException ex) {
            Logger.getLogger(SMSOutServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        return rs;
    }
    
    ///set sql to session
     //horace changed short_message to short_message and username
      public void smppSetSql(String user, String startDate, String endDate, Connection conn) throws SQLException {
          
        String sql = null;
        if (user == null) {
            //SELECT dbSMPPGateway.tSMSOUT.source_addr, dbSMPPGateway.tSMSOUT.destination_addr, dbSMPPGateway.tSMSOUT.short_message, dbSMPPGateway.tSMSOUT.time_submitted, dbSMPPGateway.tSMSOUT.time_processed, dbSMPPGateway.tSMSOUT.registered_delivery, dbSMPPGateway.tUSER.username FROM dbSMPPGateway.tSMSOUT LEFT JOIN dbSMPPGateway.tUSER ON dbSMPPGateway.tSMSOUT.tUSER_id=dbSMPPGateway.tUSER.id WHERE time_submitted >= '201509120000' AND time_submitted <= '201509120000';
            sql = "SELECT dbSMPPGateway.tSMSOUT.id AS myid,dbSMPPGateway.tSMSOUT.status, "
                    + "dbSMPPGateway.tSMSOUT.source_addr, dbSMPPGateway.tSMSOUT.destination_addr, "
                    + "dbSMPPGateway.tSMSOUT.short_message AS message_payload, dbSMPPGateway.tSMSOUT.time_submitted, "
                    + "dbSMPPGateway.tSMSOUT.time_processed, dbSMPPGateway.tSMSOUT.registered_delivery, "
                    + "dbSMPPGateway.tUSER.username AS user,dbSMPPGateway.tSMSSTATUS.desctiption FROM dbSMPPGateway.tSMSOUT"
                    + " LEFT JOIN dbSMPPGateway.tUSER ON dbSMPPGateway.tSMSOUT.tUSER_id=dbSMPPGateway.tUSER.id LEFT "
                    + "JOIN dbSMPPGateway.tSMSSTATUS ON dbSMPPGateway.tSMSOUT.status=dbSMPPGateway.tSMSSTATUS.id WHERE"
                    + " time_submitted >= '" + startDate + "' AND time_submitted <= '" + endDate + "' "
                    + "UNION ALL SELECT dbSMPPGateway.tSMSOUT_COMPLETE.id AS myid,dbSMPPGateway.tSMSOUT_COMPLETE.status, "
                    + "dbSMPPGateway.tSMSOUT_COMPLETE.source_addr, dbSMPPGateway.tSMSOUT_COMPLETE.destination_addr, "
                    + "dbSMPPGateway.tSMSOUT_COMPLETE.short_message, dbSMPPGateway.tSMSOUT_COMPLETE.time_submitted, "
                    + "dbSMPPGateway.tSMSOUT_COMPLETE.time_processed, dbSMPPGateway.tSMSOUT_COMPLETE.registered_delivery,"
                    + " dbSMPPGateway.tUSER.username,dbSMPPGateway.tSMSSTATUS.desctiption FROM dbSMPPGateway.tSMSOUT_COMPLETE LEFT"
                    + " JOIN dbSMPPGateway.tUSER ON dbSMPPGateway.tSMSOUT_COMPLETE.tUSER_id=dbSMPPGateway.tUSER.id LEFT JOIN"
                    + " dbSMPPGateway.tSMSSTATUS ON dbSMPPGateway.tSMSOUT_COMPLETE.status=dbSMPPGateway.tSMSSTATUS.id WHERE "
                    + "time_submitted >= '" + startDate + "' AND time_submitted <= '" + endDate + "' order by myid desc";
        } else if (user.isEmpty()) {

            sql = "SELECT dbSMPPGateway.tSMSOUT.id AS myid,dbSMPPGateway.tSMSOUT.status, dbSMPPGateway.tSMSOUT.source_addr, dbSMPPGateway.tSMSOUT.destination_addr, dbSMPPGateway.tSMSOUT.short_message AS message_payload, dbSMPPGateway.tSMSOUT.time_submitted, dbSMPPGateway.tSMSOUT.time_processed, dbSMPPGateway.tSMSOUT.registered_delivery, dbSMPPGateway.tUSER.username AS user,dbSMPPGateway.tSMSSTATUS.desctiption FROM dbSMPPGateway.tSMSOUT LEFT JOIN dbSMPPGateway.tUSER ON dbSMPPGateway.tSMSOUT.tUSER_id=dbSMPPGateway.tUSER.id LEFT JOIN dbSMPPGateway.tSMSSTATUS ON dbSMPPGateway.tSMSOUT.status=dbSMPPGateway.tSMSSTATUS.id WHERE time_submitted >= '" + startDate + "' AND time_submitted <= '" + endDate + "' "
                    + "UNION ALL SELECT dbSMPPGateway.tSMSOUT_COMPLETE.id AS myid,dbSMPPGateway.tSMSOUT_COMPLETE.status, dbSMPPGateway.tSMSOUT_COMPLETE.source_addr, dbSMPPGateway.tSMSOUT_COMPLETE.destination_addr, dbSMPPGateway.tSMSOUT_COMPLETE.short_message, dbSMPPGateway.tSMSOUT_COMPLETE.time_submitted, dbSMPPGateway.tSMSOUT_COMPLETE.time_processed, dbSMPPGateway.tSMSOUT_COMPLETE.registered_delivery, dbSMPPGateway.tUSER.username,dbSMPPGateway.tSMSSTATUS.desctiption FROM dbSMPPGateway.tSMSOUT_COMPLETE LEFT JOIN dbSMPPGateway.tUSER ON dbSMPPGateway.tSMSOUT_COMPLETE.tUSER_id=dbSMPPGateway.tUSER.id LEFT JOIN dbSMPPGateway.tSMSSTATUS ON dbSMPPGateway.tSMSOUT_COMPLETE.status=dbSMPPGateway.tSMSSTATUS.id WHERE time_submitted >= '" + startDate + "' AND time_submitted <= '" + endDate + "' order by myid desc";
        } else {

            sql = "SELECT dbSMPPGateway.tSMSOUT.id AS myid,dbSMPPGateway.tSMSOUT.status, dbSMPPGateway.tSMSOUT.source_addr, dbSMPPGateway.tSMSOUT.destination_addr, dbSMPPGateway.tSMSOUT.short_message AS message_payload, dbSMPPGateway.tSMSOUT.time_submitted, dbSMPPGateway.tSMSOUT.time_processed, dbSMPPGateway.tSMSOUT.registered_delivery, dbSMPPGateway.tUSER.username AS user,dbSMPPGateway.tSMSSTATUS.desctiption FROM dbSMPPGateway.tSMSOUT LEFT JOIN dbSMPPGateway.tUSER ON dbSMPPGateway.tSMSOUT.tUSER_id=dbSMPPGateway.tUSER.id LEFT JOIN dbSMPPGateway.tSMSSTATUS ON dbSMPPGateway.tSMSOUT.status=dbSMPPGateway.tSMSSTATUS.id WHERE time_submitted >= '" + startDate + "' AND time_submitted <= '" + endDate + "' AND username = '" + user + "' "
                    + "UNION ALL SELECT dbSMPPGateway.tSMSOUT_COMPLETE.id AS myid,dbSMPPGateway.tSMSOUT_COMPLETE.status, dbSMPPGateway.tSMSOUT_COMPLETE.source_addr, dbSMPPGateway.tSMSOUT_COMPLETE.destination_addr, dbSMPPGateway.tSMSOUT_COMPLETE.short_message, dbSMPPGateway.tSMSOUT_COMPLETE.time_submitted, dbSMPPGateway.tSMSOUT_COMPLETE.time_processed, dbSMPPGateway.tSMSOUT_COMPLETE.registered_delivery, dbSMPPGateway.tUSER.username,dbSMPPGateway.tSMSSTATUS.desctiption FROM dbSMPPGateway.tSMSOUT_COMPLETE LEFT JOIN dbSMPPGateway.tUSER ON dbSMPPGateway.tSMSOUT_COMPLETE.tUSER_id=dbSMPPGateway.tUSER.id LEFT JOIN dbSMPPGateway.tSMSSTATUS ON dbSMPPGateway.tSMSOUT_COMPLETE.status=dbSMPPGateway.tSMSSTATUS.id WHERE time_submitted >= '" + startDate + "' AND time_submitted <= '" + endDate + "' AND username = '" + user + "' order by myid desc";
        }
        List<SMPPOut> result = new ArrayList<>();

        
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("reportSQL", sql);
   
    }
    

    //this should proceed if the user clicks proceed
    @Override
    public Map<String, Object> smppOutReport(String user, String startDate, String endDate, Connection conn, int limit) throws SQLException {
           
        String sql = null;
        if (user == null) {
            //horace changed short_message to short_message and username
            //SELECT dbSMPPGateway.tSMSOUT.source_addr, dbSMPPGateway.tSMSOUT.destination_addr, dbSMPPGateway.tSMSOUT.short_message, dbSMPPGateway.tSMSOUT.time_submitted, dbSMPPGateway.tSMSOUT.time_processed, dbSMPPGateway.tSMSOUT.registered_delivery, dbSMPPGateway.tUSER.username FROM dbSMPPGateway.tSMSOUT LEFT JOIN dbSMPPGateway.tUSER ON dbSMPPGateway.tSMSOUT.tUSER_id=dbSMPPGateway.tUSER.id WHERE time_submitted >= '201509120000' AND time_submitted <= '201509120000';
            sql = "SELECT dbSMPPGateway.tSMSOUT.id AS myid,dbSMPPGateway.tSMSOUT.status, "
                    + "dbSMPPGateway.tSMSOUT.source_addr, dbSMPPGateway.tSMSOUT.destination_addr, "
                    + "dbSMPPGateway.tSMSOUT.short_message AS message_payload, dbSMPPGateway.tSMSOUT.time_submitted, "
                    + "dbSMPPGateway.tSMSOUT.time_processed, dbSMPPGateway.tSMSOUT.registered_delivery, "
                    + "dbSMPPGateway.tUSER.username AS user,dbSMPPGateway.tSMSSTATUS.desctiption FROM dbSMPPGateway.tSMSOUT"
                    + " LEFT JOIN dbSMPPGateway.tUSER ON dbSMPPGateway.tSMSOUT.tUSER_id=dbSMPPGateway.tUSER.id LEFT "
                    + "JOIN dbSMPPGateway.tSMSSTATUS ON dbSMPPGateway.tSMSOUT.status=dbSMPPGateway.tSMSSTATUS.id WHERE"
                    + " time_submitted >= '" + startDate + "' AND time_submitted <= '" + endDate + "' "
                    + "UNION ALL SELECT dbSMPPGateway.tSMSOUT_COMPLETE.id AS myid,dbSMPPGateway.tSMSOUT_COMPLETE.status, "
                    + "dbSMPPGateway.tSMSOUT_COMPLETE.source_addr, dbSMPPGateway.tSMSOUT_COMPLETE.destination_addr, "
                    + "dbSMPPGateway.tSMSOUT_COMPLETE.short_message, dbSMPPGateway.tSMSOUT_COMPLETE.time_submitted, "
                    + "dbSMPPGateway.tSMSOUT_COMPLETE.time_processed, dbSMPPGateway.tSMSOUT_COMPLETE.registered_delivery,"
                    + " dbSMPPGateway.tUSER.username,dbSMPPGateway.tSMSSTATUS.desctiption FROM dbSMPPGateway.tSMSOUT_COMPLETE LEFT"
                    + " JOIN dbSMPPGateway.tUSER ON dbSMPPGateway.tSMSOUT_COMPLETE.tUSER_id=dbSMPPGateway.tUSER.id LEFT JOIN"
                    + " dbSMPPGateway.tSMSSTATUS ON dbSMPPGateway.tSMSOUT_COMPLETE.status=dbSMPPGateway.tSMSSTATUS.id WHERE "
                    + "time_submitted >= '" + startDate + "' AND time_submitted <= '" + endDate + "' order by myid desc";
        } else if (user.isEmpty()) {

            sql = "SELECT dbSMPPGateway.tSMSOUT.id AS myid,dbSMPPGateway.tSMSOUT.status, dbSMPPGateway.tSMSOUT.source_addr, dbSMPPGateway.tSMSOUT.destination_addr, dbSMPPGateway.tSMSOUT.short_message AS message_payload, dbSMPPGateway.tSMSOUT.time_submitted, dbSMPPGateway.tSMSOUT.time_processed, dbSMPPGateway.tSMSOUT.registered_delivery, dbSMPPGateway.tUSER.username AS user,dbSMPPGateway.tSMSSTATUS.desctiption FROM dbSMPPGateway.tSMSOUT LEFT JOIN dbSMPPGateway.tUSER ON dbSMPPGateway.tSMSOUT.tUSER_id=dbSMPPGateway.tUSER.id LEFT JOIN dbSMPPGateway.tSMSSTATUS ON dbSMPPGateway.tSMSOUT.status=dbSMPPGateway.tSMSSTATUS.id WHERE time_submitted >= '" + startDate + "' AND time_submitted <= '" + endDate + "' "
                    + "UNION ALL SELECT dbSMPPGateway.tSMSOUT_COMPLETE.id AS myid,dbSMPPGateway.tSMSOUT_COMPLETE.status, dbSMPPGateway.tSMSOUT_COMPLETE.source_addr, dbSMPPGateway.tSMSOUT_COMPLETE.destination_addr, dbSMPPGateway.tSMSOUT_COMPLETE.short_message, dbSMPPGateway.tSMSOUT_COMPLETE.time_submitted, dbSMPPGateway.tSMSOUT_COMPLETE.time_processed, dbSMPPGateway.tSMSOUT_COMPLETE.registered_delivery, dbSMPPGateway.tUSER.username,dbSMPPGateway.tSMSSTATUS.desctiption FROM dbSMPPGateway.tSMSOUT_COMPLETE LEFT JOIN dbSMPPGateway.tUSER ON dbSMPPGateway.tSMSOUT_COMPLETE.tUSER_id=dbSMPPGateway.tUSER.id LEFT JOIN dbSMPPGateway.tSMSSTATUS ON dbSMPPGateway.tSMSOUT_COMPLETE.status=dbSMPPGateway.tSMSSTATUS.id WHERE time_submitted >= '" + startDate + "' AND time_submitted <= '" + endDate + "' order by myid desc";
        } else {

            sql = "SELECT dbSMPPGateway.tSMSOUT.id AS myid,dbSMPPGateway.tSMSOUT.status, dbSMPPGateway.tSMSOUT.source_addr, dbSMPPGateway.tSMSOUT.destination_addr, dbSMPPGateway.tSMSOUT.short_message AS message_payload, dbSMPPGateway.tSMSOUT.time_submitted, dbSMPPGateway.tSMSOUT.time_processed, dbSMPPGateway.tSMSOUT.registered_delivery, dbSMPPGateway.tUSER.username AS user,dbSMPPGateway.tSMSSTATUS.desctiption FROM dbSMPPGateway.tSMSOUT LEFT JOIN dbSMPPGateway.tUSER ON dbSMPPGateway.tSMSOUT.tUSER_id=dbSMPPGateway.tUSER.id LEFT JOIN dbSMPPGateway.tSMSSTATUS ON dbSMPPGateway.tSMSOUT.status=dbSMPPGateway.tSMSSTATUS.id WHERE time_submitted >= '" + startDate + "' AND time_submitted <= '" + endDate + "' AND username = '" + user + "' "
                    + "UNION ALL SELECT dbSMPPGateway.tSMSOUT_COMPLETE.id AS myid,dbSMPPGateway.tSMSOUT_COMPLETE.status, dbSMPPGateway.tSMSOUT_COMPLETE.source_addr, dbSMPPGateway.tSMSOUT_COMPLETE.destination_addr, dbSMPPGateway.tSMSOUT_COMPLETE.short_message, dbSMPPGateway.tSMSOUT_COMPLETE.time_submitted, dbSMPPGateway.tSMSOUT_COMPLETE.time_processed, dbSMPPGateway.tSMSOUT_COMPLETE.registered_delivery, dbSMPPGateway.tUSER.username,dbSMPPGateway.tSMSSTATUS.desctiption FROM dbSMPPGateway.tSMSOUT_COMPLETE LEFT JOIN dbSMPPGateway.tUSER ON dbSMPPGateway.tSMSOUT_COMPLETE.tUSER_id=dbSMPPGateway.tUSER.id LEFT JOIN dbSMPPGateway.tSMSSTATUS ON dbSMPPGateway.tSMSOUT_COMPLETE.status=dbSMPPGateway.tSMSSTATUS.id WHERE time_submitted >= '" + startDate + "' AND time_submitted <= '" + endDate + "' AND username = '" + user + "' order by myid desc";
        }
        List<SMPPOut> result = new ArrayList<>();

        
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("reportSQL", sql);
      
        String sqlLimit="";
        if(limit==0){
           sqlLimit="";
           
        }
        else {
            sqlLimit="limit "+limit;
          
            
        }
        
        Statement stmt = conn.createStatement();     
        ResultSet rs = stmt.executeQuery(sql+" "+sqlLimit);
        
        while (rs.next()) {
            SMPPOut smsOut = new SMPPOut();
            smsOut.setSourceAddr(rs.getString("source_addr"));
            smsOut.setDestinationAddr(rs.getString("destination_addr"));
            smsOut.setMessagePayload(rs.getString("message_payload"));

            smsOut.setTimeSubmitted(rs.getString("time_submitted"));
            smsOut.setTimeProcessed(rs.getString("time_processed"));
            smsOut.setStatus(String.valueOf(rs.getInt("status")).charAt(0));
            smsOut.setRealStatus(rs.getString("desctiption"));

            smsOut.setUser(rs.getString("user"));
            result.add(smsOut);
        }

        int noSMS = 0;
        for (SMPPOut sms : result) {

            noSMS += sms.getSmsCount();
        }
        LOG.log(Level.INFO, "The message: {0}", noSMS);

        Map<String, Object> mapResult = new HashMap<>();
        mapResult.put("result", result);
        mapResult.put("noSMS", noSMS);
        return mapResult;
    }

    @Override
    public Map<String, Object> optOutReport(String startDate, String endDate, Connection conn) throws SQLException {

        String sql = null;

        sql = "SELECT dbSMPPGateway.tOPTEDOUTCUSTOMERS.id AS myid,dbSMPPGateway.tOPTEDOUTCUSTOMERS.senderID, dbSMPPGateway.tOPTEDOUTCUSTOMERS.mobile, dbSMPPGateway.tOPTEDOUTCUSTOMERS.optout_message, dbSMPPGateway.tOPTEDOUTCUSTOMERS.reply, dbSMPPGateway.tOPTEDOUTCUSTOMERS.time, dbSMPPGateway.tOPTEDOUTCUSTOMERS.emailed FROM dbSMPPGateway.tOPTEDOUTCUSTOMERS WHERE dbSMPPGateway.tOPTEDOUTCUSTOMERS.time >= '" + startDate + "' AND dbSMPPGateway.tOPTEDOUTCUSTOMERS.time <= '" + endDate + "' order by myid desc";

//        String sqlReseller = "SELECT o.id AS myid,o.senderID, o.mobile, o.optout_message, o.reply, o.time, o.emailed "
//                + "FROM dbSMPPGateway.tOPTEDOUTCUSTOMERS o inner join dbSMS.tSDP t on o.senderID = t.short_code "
//                + "inner join dbSMS.tUSER u on t.agent_id = u.id WHERE o.time >= '" + startDate + "' AND o.time <= '" + endDate + "' and u.id = " + user_id + " order by myid desc";
        String sqlReseller = "SELECT o.id AS myid,o.senderID, o.mobile, o.optout_message, o.reply, o.time, o.emailed "
                + "FROM dbSMPPGateway.tOPTEDOUTCUSTOMERS o inner join dbSMS.tSDPNew t on o.senderID = t.short_code "
                + "inner join dbSMS.tUSER u on t.agent_id = u.id WHERE o.time >= '" + startDate + "' AND o.time <= '" + endDate + "' and u.id = " + user_id + " order by myid desc";

        List<OPTOut> result = new ArrayList<>();

        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(UserServiceImpl.isReseller().equalsIgnoreCase("none") ? sqlReseller : sql);

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

        int noSMS = 0;
        for (OPTOut sms : result) {

            noSMS += sms.getSmsCount();
        }
        LOG.log(Level.INFO, "The message: {0}", noSMS);

        Map<String, Object> mapResult = new HashMap<>();
        mapResult.put("result", result);
        mapResult.put("noSMS", noSMS);

        return mapResult;
    }

    private static HSSFSheet allocategenerateXSLSheet(HSSFWorkbook wb, String sheetName) {

        HSSFSheet sheet = wb.createSheet(sheetName);
        Map<String, CellStyle> styles = createStyles(wb);//style

        PrintSetup printSetup = sheet.getPrintSetup();
        printSetup.setLandscape(true);
        sheet.setFitToPage(true);
        sheet.setHorizontallyCenter(true);

        //title row
        Row titleRow = sheet.createRow(0);
        titleRow.setHeightInPoints(45);
        Cell titleCell = titleRow.createCell(0);

        titleCell.setCellValue("SMS OUT REPORT");
        titleCell.setCellStyle(styles.get("title"));
        sheet.addMergedRegion(CellRangeAddress.valueOf("$A$1:$H$1"));

        String[] titles = {"Mobile", "Source Address", "Message", "Time Spent", "Last Update", "User", "Status", "No. of SMS"};

        HSSFRow row = sheet.createRow(1);
        row.setHeightInPoints(40);

        Cell headerCell;
        for (int i = 0; i < titles.length; i++) {
            headerCell = row.createCell(i);
            headerCell.setCellValue(titles[i]);
            headerCell.setCellStyle(styles.get("header"));
        }

        return sheet;
    }

    @Override
    public void generateXSL(String user, String startDate, String endDate, Connection conn) throws SQLException {
        // SMSOut sout= new SMSOut();

        try {
            HSSFWorkbook wb = new HSSFWorkbook();
            HSSFSheet sheet = allocategenerateXSLSheet(wb, "Users_Sheet_1");
            //FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("smsoutReport");

            // List<SMSOut> exportSMSOutReport = (List) userSMSOutReport(user, startDate, endDate, conn).get("result");
            List<SMSOut> exportSMSOutReport = null;
            Map kpld = FacesContext.getCurrentInstance().getExternalContext().getSessionMap();
            if (kpld.containsKey("smsoutReport")) {
                exportSMSOutReport = (List) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("smsoutReport");
            } else {
                exportSMSOutReport = new ArrayList();
            }
            int rowNum = 2;
            int sheetCounter = 1;
            int totalSent = 0;
            
            //horace
            if (exportSMSOutReport.size()>1){
                JsfUtil.addSuccessMessage("Will only generate excell containing the first 15000 rows");
            }
            //
            for (SMSOut anSMS : exportSMSOutReport) {
                if (rowNum % 65535 == 0) {//Max row reached, build new sheet
                    sheetCounter++;
                    String new_sheetName = "Users_Sheet_" + sheetCounter;
                    // System.out.println("Attempting to create sheet: " + new_sheetName);
                    sheet = allocategenerateXSLSheet(wb, new_sheetName);
                    rowNum = 2;
                }
                HSSFRow row = sheet.createRow(rowNum);
                row.createCell(0).setCellValue(anSMS.getDestinationAddr());
                row.createCell(1).setCellValue(anSMS.getSourceAddr());
                row.createCell(2).setCellValue(anSMS.getMessagePayload());
                row.createCell(3).setCellValue(anSMS.getTimeSubmitted());
                row.createCell(4).setCellValue(anSMS.getTimeProcessed());

                row.createCell(5).setCellValue(anSMS.getUser());
                row.createCell(6).setCellValue(anSMS.getRealStatus());
                row.createCell(7).setCellValue(anSMS.getSmsCount());

                rowNum++;
                totalSent += anSMS.getSmsCount();
                HSSFRow row2 = sheet.createRow(rowNum);
                Font font = wb.createFont();
                font.setBoldweight(Font.BOLDWEIGHT_BOLD);
                CellStyle style = wb.createCellStyle();
                style.setFont(font);

                //for(int i=0;i<row2.getLastCellNum();i++){
                //}
                row2.createCell(6).setCellValue("Total sms sent");
                row2.createCell(7).setCellValue(totalSent);

                row2.getCell(7).setCellStyle(style);
                row2.getCell(6).setCellStyle(style);
                // row2.getRowStyle().setFont(font);

            }

            sheet.setColumnWidth(0, 20 * 256); //30 characters wide
            sheet.setColumnWidth(1, 15 * 256);
            for (int i = 2; i < 5; i++) {
                sheet.setColumnWidth(i, 20 * 256);  //6 characters wide
            }
            sheet.setColumnWidth(5, 10 * 256);

            sheet.setColumnWidth(6, 20 * 256);
            sheet.setColumnWidth(7, 10 * 256); //10 characters wide

            FacesContext context = FacesContext.getCurrentInstance();
            HttpServletResponse res = (HttpServletResponse) context.getExternalContext().getResponse();
            res.setContentType("application/vnd.ms-excel");
            res.setHeader("Content-disposition", "attachment;filename=mydata.xls");

            ServletOutputStream out = res.getOutputStream();
            wb.write(out);
            out.flush();
            out.close();
            FacesContext.getCurrentInstance().responseComplete();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private static HSSFSheet allocategenerateSMPPXSLSheet(HSSFWorkbook wb, String sheetName) {
        HSSFSheet sheet = wb.createSheet(sheetName);
        /*You can add style here too or write header here*/
        Map<String, CellStyle> styles = createStyles(wb);

        PrintSetup printSetup = sheet.getPrintSetup();
        printSetup.setLandscape(true);
        sheet.setFitToPage(true);
        sheet.setHorizontallyCenter(true);

        //title row
        Row titleRow = sheet.createRow(0);
        titleRow.setHeightInPoints(45);
        Cell titleCell = titleRow.createCell(0);
        titleCell.setCellValue("SMPP SMS OUT REPORT");
        titleCell.setCellStyle(styles.get("title"));
        sheet.addMergedRegion(CellRangeAddress.valueOf("$A$1:$H$1"));

        String[] titles = {"Mobile", "Source Address", "Message", "Time Sent", "Last Update", "User", "Status", "No. of SMS"};

        HSSFRow row = sheet.createRow(1);
        row.setHeightInPoints(40);

        Cell headerCell;
        for (int i = 0; i < titles.length; i++) {
            headerCell = row.createCell(i);
            headerCell.setCellValue(titles[i]);
            headerCell.setCellStyle(styles.get("header"));
        }

        return sheet;
    }

    @Override
    public void generateSMPPXSL(String user, String startDate, String endDate, Connection conn) throws SQLException {
        if (user == null || user.equals("")) {
            return;
        }
        int totalSent = 0;
        try {

            HSSFWorkbook wb = new HSSFWorkbook();
            HSSFSheet sheet = allocategenerateSMPPXSLSheet(wb, "SMPPOUT_Sheet_1");
            List<SMPPOut> exportSMSOutReport = (List) smppOutReport(user, startDate, endDate, conn,0).get("result");
            int rowNum = 2;
            int sheetCounter = 1;
            Font font = wb.createFont();
            font.setBoldweight(Font.BOLDWEIGHT_BOLD);

            for (SMPPOut anSMS : exportSMSOutReport) {

                if (rowNum % 65535 == 0) {
                    sheetCounter++;
                    String new_sheetName = "SMPPOUT_Sheet_" + sheetCounter;
                    sheet = allocategenerateSMPPXSLSheet(wb, new_sheetName);
                    rowNum = 2;
                }
                HSSFRow row = sheet.createRow(rowNum);
                row.createCell(0).setCellValue(anSMS.getDestinationAddr());
                row.createCell(1).setCellValue(anSMS.getSourceAddr());
                row.createCell(2).setCellValue(anSMS.getMessagePayload());
                row.createCell(3).setCellValue(anSMS.getTimeSubmitted());
                row.createCell(4).setCellValue(anSMS.getTimeProcessed());

                row.createCell(5).setCellValue(anSMS.getUser());
                row.createCell(6).setCellValue(anSMS.getRealStatus());
                row.createCell(7).setCellValue(anSMS.getSmsCount());
                rowNum++;
                //code here 
                totalSent += anSMS.getSmsCount();
                HSSFRow row2 = sheet.createRow(rowNum);
//                Font font=wb.createFont();
//                font.setBoldweight(Font.BOLDWEIGHT_BOLD);
                CellStyle style = wb.createCellStyle();
                style.setFont(font);
                row2.createCell(6).setCellValue("Total sms sent");
                row2.createCell(7).setCellValue(totalSent);

                row2.getCell(7).setCellStyle(style);
                row2.getCell(6).setCellStyle(style);
            }

            sheet.setColumnWidth(0, 20 * 256); //30 characters wide
            sheet.setColumnWidth(1, 15 * 256);
            for (int i = 2; i < 5; i++) {
                sheet.setColumnWidth(i, 20 * 256);  //6 characters wide
            }
            sheet.setColumnWidth(5, 10 * 256);

            sheet.setColumnWidth(6, 20 * 256);
            sheet.setColumnWidth(7, 10 * 256); //10 characters wide

            FacesContext context = FacesContext.getCurrentInstance();
            HttpServletResponse res = (HttpServletResponse) context.getExternalContext().getResponse();
            res.setContentType("application/vnd.ms-excel");
            res.setHeader("Content-disposition", "attachment;filename=mydata.xls");

            ServletOutputStream out = res.getOutputStream();
            wb.write(out);
            out.flush();
            out.close();
            FacesContext.getCurrentInstance().responseComplete();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private static HSSFSheet allocategenerateOPTXSLSheet(HSSFWorkbook wb, String sheetName) {
        HSSFSheet sheet = wb.createSheet(sheetName);
        /*You can add style here too or write header here*/
        Map<String, CellStyle> styles = createStyles(wb);

        PrintSetup printSetup = sheet.getPrintSetup();
        printSetup.setLandscape(true);
        sheet.setFitToPage(true);
        sheet.setHorizontallyCenter(true);

        //title row
        Row titleRow = sheet.createRow(0);
        titleRow.setHeightInPoints(45);
        Cell titleCell = titleRow.createCell(0);
        titleCell.setCellValue("OPT SMS OUT REPORT");
        titleCell.setCellStyle(styles.get("title"));
        sheet.addMergedRegion(CellRangeAddress.valueOf("$A$1:$H$1"));

        String[] titles = {"Mobile", "Source Address", "Message", "Reply", "Time Sent", "Emailed", "No. of SMS"};

        HSSFRow row = sheet.createRow(1);
        row.setHeightInPoints(40);

        Cell headerCell;
        for (int i = 0; i < titles.length; i++) {
            headerCell = row.createCell(i);
            headerCell.setCellValue(titles[i]);
            headerCell.setCellStyle(styles.get("header"));
        }

        return sheet;
    }

    @Override
    public void generateOPTXSL(String startDate, String endDate, Connection conn) throws SQLException {
        int totalSent = 0;
        try {
            HSSFWorkbook wb = new HSSFWorkbook();
            HSSFSheet sheet = allocategenerateOPTXSLSheet(wb, "OPTOUT_Sheet_1");
            List<OPTOut> exportSMSOutReport = (List) optOutReport(startDate, endDate, conn).get("result");
            int rowNum = 2;
            int sheetCounter = 1;
            for (OPTOut anSMS : exportSMSOutReport) {

                if (rowNum % 65535 == 0) {
                    sheetCounter++;
                    String new_sheetName = "OPTOUT_Sheet_" + sheetCounter;
                    sheet = allocategenerateOPTXSLSheet(wb, new_sheetName);
                    rowNum = 2;
                }
                HSSFRow row = sheet.createRow(rowNum);
                row.createCell(0).setCellValue(anSMS.getDestinationAddr());
                row.createCell(1).setCellValue(anSMS.getSourceAddr());
                row.createCell(2).setCellValue(anSMS.getMessagePayload());
                row.createCell(3).setCellValue(anSMS.getReply());
                row.createCell(4).setCellValue(anSMS.getTimeSubmitted());
                row.createCell(5).setCellValue(anSMS.getEmailed());

                row.createCell(6).setCellValue(anSMS.getSmsCount());
                rowNum++;

                totalSent += anSMS.getSmsCount();
                HSSFRow row2 = sheet.createRow(rowNum);
                Font font = wb.createFont();
                font.setBoldweight(Font.BOLDWEIGHT_BOLD);
                CellStyle style = wb.createCellStyle();
                style.setFont(font);

                //for(int i=0;i<row2.getLastCellNum();i++){
                //}
                row2.createCell(5).setCellValue("Total sms sent");
                row2.createCell(6).setCellValue(totalSent);

                row2.getCell(5).setCellStyle(style);
                row2.getCell(6).setCellStyle(style);
            }

            sheet.setColumnWidth(0, 20 * 256); //30 characters wide
            sheet.setColumnWidth(1, 15 * 256);
            for (int i = 2; i < 5; i++) {
                sheet.setColumnWidth(i, 20 * 256);  //6 characters wide
            }
            sheet.setColumnWidth(5, 10 * 256);

            sheet.setColumnWidth(6, 20 * 256);
            sheet.setColumnWidth(7, 10 * 256); //10 characters wide

            FacesContext context = FacesContext.getCurrentInstance();
            HttpServletResponse res = (HttpServletResponse) context.getExternalContext().getResponse();
            res.setContentType("application/vnd.ms-excel");
            res.setHeader("Content-disposition", "attachment;filename=mydata.xls");

            ServletOutputStream out = res.getOutputStream();
            wb.write(out);
            out.flush();
            out.close();
            FacesContext.getCurrentInstance().responseComplete();
        } catch (Exception e) {
            e.printStackTrace();
        }

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

    private static Map<String, CellStyle> createStyles(Workbook wb) {

        Map<String, CellStyle> styles = new HashMap<>();
        CellStyle style;
        Font titleFont = wb.createFont();
        titleFont.setFontHeightInPoints((short) 18);
        titleFont.setBoldweight(Font.BOLDWEIGHT_BOLD);
        style = wb.createCellStyle();
        style.setAlignment(CellStyle.ALIGN_CENTER);
        style.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
        style.setFont(titleFont);
        styles.put("title", style);

        Font monthFont = wb.createFont();
        monthFont.setFontHeightInPoints((short) 11);
        monthFont.setColor(IndexedColors.WHITE.getIndex());
        style = wb.createCellStyle();
        style.setAlignment(CellStyle.ALIGN_CENTER);
        style.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
        style.setFillForegroundColor(IndexedColors.GREY_50_PERCENT.getIndex());
        style.setFillPattern(CellStyle.SOLID_FOREGROUND);
        style.setFont(monthFont);
        style.setWrapText(true);
        styles.put("header", style);

        style = wb.createCellStyle();
        style.setAlignment(CellStyle.ALIGN_CENTER);
        style.setWrapText(true);
        style.setBorderRight(CellStyle.BORDER_THIN);
        style.setRightBorderColor(IndexedColors.BLACK.getIndex());
        style.setBorderLeft(CellStyle.BORDER_THIN);
        style.setLeftBorderColor(IndexedColors.BLACK.getIndex());
        style.setBorderTop(CellStyle.BORDER_THIN);
        style.setTopBorderColor(IndexedColors.BLACK.getIndex());
        style.setBorderBottom(CellStyle.BORDER_THIN);
        style.setBottomBorderColor(IndexedColors.BLACK.getIndex());
        styles.put("cell", style);

        style = wb.createCellStyle();
        style.setAlignment(CellStyle.ALIGN_CENTER);
        style.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
        style.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
        style.setFillPattern(CellStyle.SOLID_FOREGROUND);
        style.setDataFormat(wb.createDataFormat().getFormat("0.00"));
        styles.put("formula", style);

        style = wb.createCellStyle();
        style.setAlignment(CellStyle.ALIGN_CENTER);
        style.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
        style.setFillForegroundColor(IndexedColors.GREY_40_PERCENT.getIndex());
        style.setFillPattern(CellStyle.SOLID_FOREGROUND);
        style.setDataFormat(wb.createDataFormat().getFormat("0.00"));
        styles.put("formula_2", style);

        return styles;
    }

    @Override
    public Map<String, Object> smsOutGroupBy(String user, String startDate, String endDate, Connection conn) throws SQLException {

        //original String sql = "SELECT time_submitted, user, YEAR(time_submitted) year, MONTHNAME(STR_TO_DATE(MONTH(time_submitted), '%m')) month, COUNT(MONTHNAME(STR_TO_DATE(MONTH(time_submitted), '%m'))) frequency from tSMSOUT WHERE time_submitted >= '" + startDate + "' AND time_submitted <= '" + endDate + "' AND user = '" + user + "'  GROUP BY month, year ORDER BY time_submitted";
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
    
     public void generateXSLToFileSystem(String user, String startDate, String endDate, Connection conn) throws SQLException {
        // SMSOut sout= new SMSOut();

        try {
            HSSFWorkbook wb = new HSSFWorkbook();
            HSSFSheet sheet = allocategenerateXSLSheet(wb, "Users_Sheet_1");
            //FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("smsoutReport");

            // List<SMSOut> exportSMSOutReport = (List) userSMSOutReport(user, startDate, endDate, conn).get("result");
            List<SMSOut> exportSMSOutReport = null;
            Map kpld = FacesContext.getCurrentInstance().getExternalContext().getSessionMap();
            if (kpld.containsKey("smsoutReport")) {
                exportSMSOutReport = (List) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("smsoutReport");
            } else {
                exportSMSOutReport = new ArrayList();
            }
            int rowNum = 2;
            int sheetCounter = 1;
            int totalSent = 0;
            for (SMSOut anSMS : exportSMSOutReport) {
                if (rowNum % 65535 == 0) {//Max row reached, build new sheet
                    sheetCounter++;
                    String new_sheetName = "Users_Sheet_" + sheetCounter;
                    sheet = allocategenerateXSLSheet(wb, new_sheetName);
                    rowNum = 2;
                }
                HSSFRow row = sheet.createRow(rowNum);
                row.createCell(0).setCellValue(anSMS.getDestinationAddr());
                row.createCell(1).setCellValue(anSMS.getSourceAddr());
                row.createCell(2).setCellValue(anSMS.getMessagePayload());
                row.createCell(3).setCellValue(anSMS.getTimeSubmitted());
                row.createCell(4).setCellValue(anSMS.getTimeProcessed());

                row.createCell(5).setCellValue(anSMS.getUser());
                row.createCell(6).setCellValue(anSMS.getRealStatus());
                row.createCell(7).setCellValue(anSMS.getSmsCount());

                rowNum++;
                totalSent += anSMS.getSmsCount();
                HSSFRow row2 = sheet.createRow(rowNum);
                Font font = wb.createFont();
                font.setBoldweight(Font.BOLDWEIGHT_BOLD);
                CellStyle style = wb.createCellStyle();
                style.setFont(font);

                //for(int i=0;i<row2.getLastCellNum();i++){
                //}
                row2.createCell(6).setCellValue("Total sms sent");
                row2.createCell(7).setCellValue(totalSent);

                row2.getCell(7).setCellStyle(style);
                row2.getCell(6).setCellStyle(style);
                // row2.getRowStyle().setFont(font);

            }

            sheet.setColumnWidth(0, 20 * 256); //30 characters wide
            sheet.setColumnWidth(1, 15 * 256);
            for (int i = 2; i < 5; i++) {
                sheet.setColumnWidth(i, 20 * 256);  //6 characters wide
            }
            sheet.setColumnWidth(5, 10 * 256);

            sheet.setColumnWidth(6, 20 * 256);
            sheet.setColumnWidth(7, 10 * 256); //10 characters wide

            FacesContext context = FacesContext.getCurrentInstance();
            HttpServletResponse res = (HttpServletResponse) context.getExternalContext().getResponse();
            res.setContentType("application/vnd.ms-excel");
            res.setHeader("Content-disposition", "attachment;filename=mydata.xls");

            ServletOutputStream out = res.getOutputStream();
            wb.write(out);
            out.flush();
            out.close();
            FacesContext.getCurrentInstance().responseComplete();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

     @Override
     ///begining 
        public int checkIfFileIsLarge(String user, String startDate, String endDate,String scheduleStart,String scheduleEnd, Connection conn) throws SQLException {
            boolean res=false;

//            System.out.println("Inside checker "+(new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime())).toString());
        DateFormat outFormat = new SimpleDateFormat("yyyy-MM-dd");
//         System.out.println("Start Date"+startDate+"  End Date"+endDate);
//        String schsdate = (outFormat.format(startDate));
//        String schedate = (outFormat.format(endDate));
//
//        schsdate = schsdate + " 00:00:01";
//        schedate = schedate + " 23:59:59";

        String query = " SELECT id from tUSER WHERE username='" + user + "'";
//        System.out.println("Th selected user id report" + user);
//         System.out.println("preparing statement"+(new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime())).toString());
        Statement stmt1 = conn.createStatement();
//          System.out.println(" statement ready"+(new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime())).toString());
        ResultSet rs1 = stmt1.executeQuery(query);
//         System.out.println(" execute query called"+(new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime())).toString());
        String adminid = null;
//         System.out.println(" iterating result set"+(new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime())).toString());
        while (rs1.next()) {
            adminid = rs1.getString("id");
//            System.out.println("The super ID" + adminid);
        }
//         System.out.println(" finished iteration admin work done"+(new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime())).toString());
        String sql = null;
        String sqlReseller = null;
        if (user == null) {
            //SELECT tSMSOUT.id AS myid,tSMSOUT.source_addr, tSMSOUT.destination_addr, tSMSOUT.message_payload, tSMSOUT.time_submitted, tSMSOUT.time_processed, tSMSOUT.user, tSMSOUT.status, tSMSSTATUS.desctiption  FROM tSMSOUT LEFT JOIN tSMSSTATUS ON tSMSOUT.status=tSMSSTATUS.id WHERE time_submitted >= '" + startDate + "' AND time_submitted <= '" + endDate + "' 
            //UNION ALL SELECT tSMSOUT_COMPLETE.id AS myid,tSMSOUT_COMPLETE.source_addr, tSMSOUT_COMPLETE.destination_addr, tSMSOUT_COMPLETE.message_payload, tSMSOUT_COMPLETE.time_submitted, tSMSOUT_COMPLETE.time_processed, tSMSOUT_COMPLETE.user, tSMSOUT_COMPLETE.status, tSMSSTATUS.desctiption  FROM tSMSOUT_COMPLETE LEFT JOIN tSMSSTATUS ON tSMSOUT_COMPLETE.status=tSMSSTATUS.id WHERE time_submitted >= '" + startDate + "' AND time_submitted <= '" + endDate + "' order by myid desc";

            sql = "SELECT tSMSOUT.id AS myid,tSMSOUT.source_addr, tSMSOUT.destination_addr, tSMSOUT.message_payload, tSMSOUT.time_submitted, tSMSOUT.time_processed, "
                    + "tSMSOUT.user, tSMSOUT.status, tSMSSTATUS.desctiption  FROM tSMSOUT LEFT JOIN tSMSSTATUS ON tSMSOUT.status=tSMSSTATUS.id WHERE time_submitted >= '" + startDate + "' "
                    + "AND time_submitted <= '" + endDate + "' "
                    + "UNION ALL SELECT tSMSOUT_COMPLETE.id AS myid,tSMSOUT_COMPLETE.source_addr, tSMSOUT_COMPLETE.destination_addr, tSMSOUT_COMPLETE.message_payload, "
                    + "tSMSOUT_COMPLETE.time_submitted, tSMSOUT_COMPLETE.time_processed, tSMSOUT_COMPLETE.user, tSMSOUT_COMPLETE.status, tSMSSTATUS.desctiption  FROM tSMSOUT_COMPLETE"
                    + " LEFT JOIN tSMSSTATUS ON tSMSOUT_COMPLETE.status=tSMSSTATUS.id WHERE time_submitted >= '" + startDate + "' AND time_submitted <= '" + endDate + "'   UNION ALL "
                    + "select sc.id as myId , sc.source as source_addr,sc.dest as destination_addr,  sc.message as message_payload, sc.sendTime as time_submitted, "
                    + " sc.sendTime as time_processed ,sc.username as user, 11 as status ,'Scheduled' as  desctiption from tUSERSMSSCHEDULE sc where  sc.sendTime >= '"+scheduleStart+"' AND sc.sendTime  <= '"+scheduleEnd+"'   order by myid desc";

            sqlReseller = "SELECT tSMSOUT.id AS myid,tSMSOUT.source_addr, tSMSOUT.destination_addr, tSMSOUT.message_payload, tSMSOUT.time_submitted, tSMSOUT.time_processed, tSMSOUT.user, tSMSOUT.status, tSMSSTATUS.desctiption  "
                    + "FROM tSMSOUT LEFT JOIN tSMSSTATUS ON tSMSOUT.status=tSMSSTATUS.id inner join tUSER on tSMSOUT.user = tUSER.username "
                    + "WHERE time_submitted >= '" + startDate + "' AND time_submitted <= '" + endDate + "' and tUSER.agent = '"+user_id+"' UNION ALL "
                    + "SELECT tSMSOUT_COMPLETE.id AS myid,tSMSOUT_COMPLETE.source_addr, tSMSOUT_COMPLETE.destination_addr, tSMSOUT_COMPLETE.message_payload,"
                    + " tSMSOUT_COMPLETE.time_submitted, tSMSOUT_COMPLETE.time_processed, tSMSOUT_COMPLETE.user, tSMSOUT_COMPLETE.status, tSMSSTATUS.desctiption  "
                    + "FROM tSMSOUT_COMPLETE LEFT JOIN tSMSSTATUS ON tSMSOUT_COMPLETE.status=tSMSSTATUS.id inner join tUSER on tSMSOUT_COMPLETE.user = tUSER.username"
                    + " WHERE time_submitted >= " + startDate + " AND time_submitted <= '" + endDate + "' and tUSER.agent = '" + user_id + "' order by myid desc";

        } else if (user.isEmpty()) {
            sql = "SELECT tSMSOUT.id AS myid,tSMSOUT.source_addr, tSMSOUT.destination_addr, tSMSOUT.message_payload, tSMSOUT.time_submitted, tSMSOUT.time_processed, tSMSOUT.user, tSMSOUT.status, tSMSSTATUS.desctiption  FROM tSMSOUT LEFT JOIN tSMSSTATUS ON tSMSOUT.status=tSMSSTATUS.id WHERE time_submitted >= '" + startDate + "' AND time_submitted <= '" + endDate + "' "
                    + "UNION ALL SELECT tSMSOUT_COMPLETE.id AS myid,tSMSOUT_COMPLETE.source_addr, tSMSOUT_COMPLETE.destination_addr, tSMSOUT_COMPLETE.message_payload, tSMSOUT_COMPLETE.time_submitted, tSMSOUT_COMPLETE.time_processed, tSMSOUT_COMPLETE.user, tSMSOUT_COMPLETE.status, tSMSSTATUS.desctiption  FROM tSMSOUT_COMPLETE LEFT JOIN tSMSSTATUS ON tSMSOUT_COMPLETE.status=tSMSSTATUS.id WHERE time_submitted >= '" + startDate + "' AND time_submitted <= '" + endDate + "' UNION ALL "
                    + "select sc.id as myId , sc.source as source_addr,sc.dest as destination_addr,  sc.message as message_payload, sc.sendTime as time_submitted, "
                    + " sc.sendTime as time_processed ,sc.username as user, 11 as status ,'Scheduled' as  desctiption from tUSERSMSSCHEDULE sc where  sc.sendTime >= '"+scheduleStart+"' AND sc.sendTime  <= '"+scheduleEnd+"'   order by myid desc";

            sqlReseller = "SELECT tSMSOUT.id AS myid,tSMSOUT.source_addr, tSMSOUT.destination_addr, tSMSOUT.message_payload, tSMSOUT.time_submitted, tSMSOUT.time_processed, tSMSOUT.user, tSMSOUT.status, tSMSSTATUS.desctiption  "
                    + "FROM tSMSOUT LEFT JOIN tSMSSTATUS ON tSMSOUT.status=tSMSSTATUS.id inner join tUSER on tSMSOUT.user = tUSER.username "
                    + "WHERE time_submitted >= '"+startDate+"' AND time_submitted <= '"+endDate+"' and tUSER.agent = '"+user_id+"' UNION ALL "
                    + "SELECT tSMSOUT_COMPLETE.id AS myid,tSMSOUT_COMPLETE.source_addr, tSMSOUT_COMPLETE.destination_addr, tSMSOUT_COMPLETE.message_payload, tSMSOUT_COMPLETE.time_submitted, tSMSOUT_COMPLETE.time_processed, tSMSOUT_COMPLETE.user, tSMSOUT_COMPLETE.status, tSMSSTATUS.desctiption  "
                    + "FROM tSMSOUT_COMPLETE LEFT JOIN tSMSSTATUS ON tSMSOUT_COMPLETE.status=tSMSSTATUS.id inner join tUSER on tSMSOUT_COMPLETE.user = tUSER.username WHERE time_submitted >= '"+startDate+"' AND time_submitted <= '"+endDate+"' and tUSER.agent = '" + user_id + "' order by myid desc";
        } else {
            //  sql = "SELECT tSMSOUT.id AS myid,tSMSOUT.source_addr, tSMSOUT.destination_addr, tSMSOUT.message_payload, tSMSOUT.time_submitted, tSMSOUT.time_processed, tSMSOUT.user, tSMSOUT.status, tSMSSTATUS.desctiption  FROM tSMSOUT LEFT JOIN tSMSSTATUS ON tSMSOUT.status=tSMSSTATUS.id LEFT JOIN tUSER ON tUSER.username=tSMSOUT.user  WHERE time_submitted >= '" + startDate + "' AND time_submitted <= '" + endDate + "' AND (user = '" + user + "' OR tUSER.super_account_id= '" + adminid + "' ) 
            //UNION ALL SELECT tSMSOUT_COMPLETE.id AS myid,tSMSOUT_COMPLETE.source_addr, tSMSOUT_COMPLETE.destination_addr, tSMSOUT_COMPLETE.message_payload, tSMSOUT_COMPLETE.time_submitted, tSMSOUT_COMPLETE.time_processed, tSMSOUT_COMPLETE.user, tSMSOUT_COMPLETE.status, tSMSSTATUS.desctiption  FROM tSMSOUT_COMPLETE LEFT JOIN tSMSSTATUS ON tSMSOUT_COMPLETE.status=tSMSSTATUS.id LEFT JOIN tUSER ON tUSER.username=tSMSOUT_COMPLETE.user  WHERE time_submitted >= '" + startDate + "' AND time_submitted <= '" + endDate + "' AND (user = '" + user + "'  OR tUSER.super_account_id= '" + adminid + "' ) order by myid desc";
            sql = "SELECT tSMSOUT.id AS myid,tSMSOUT.source_addr, tSMSOUT.destination_addr, tSMSOUT.message_payload, tSMSOUT.time_submitted, tSMSOUT.time_processed, tSMSOUT.user, tSMSOUT.status, tSMSSTATUS.desctiption  FROM tSMSOUT LEFT JOIN tSMSSTATUS ON tSMSOUT.status=tSMSSTATUS.id  LEFT JOIN tUSER ON tUSER.username=tSMSOUT.user WHERE time_submitted >= '" + startDate + "' AND time_submitted <= '" + endDate + "' AND (user = '" + user + "' OR tUSER.agent= '" + adminid + "' OR tUSER.super_account_id='" + adminid + "' )"
                    + "UNION ALL SELECT tSMSOUT_COMPLETE.id AS myid,tSMSOUT_COMPLETE.source_addr, tSMSOUT_COMPLETE.destination_addr, tSMSOUT_COMPLETE.message_payload, tSMSOUT_COMPLETE.time_submitted, tSMSOUT_COMPLETE.time_processed, tSMSOUT_COMPLETE.user, tSMSOUT_COMPLETE.status, tSMSSTATUS.desctiption  FROM tSMSOUT_COMPLETE LEFT JOIN tSMSSTATUS ON tSMSOUT_COMPLETE.status=tSMSSTATUS.id LEFT JOIN tUSER ON tUSER.username=tSMSOUT_COMPLETE.user WHERE time_submitted >= '" + startDate + "' AND time_submitted <= '" + endDate + "' AND (user = '" + user + "' OR tUSER.agent= '" + adminid + "' OR tUSER.super_account_id='" + adminid + "' ) order by myid desc";
//            System.out.println(sql);
            sqlReseller = sql;
        }
              

               sql = UserServiceImpl.isReseller().equalsIgnoreCase("none") ? sqlReseller : sql;

        
        sql="SELECT COUNT(myid) FROM ("+sql+") as t";
//         System.out.println(ANSI_BLUE+"large sql ="+sql+ANSI_RESET);
//         System.out.println("check if file is large preparing"+(new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime())).toString());
          PreparedStatement ps=conn.prepareStatement(sql);
       long t1= System.currentTimeMillis();
          
//          System.out.println(ANSI_BLUE+"check if file is large calling"+(new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime())).toString()+ANSI_RESET);
          ResultSet rs=ps.executeQuery();
                  long t2= System.currentTimeMillis();
                  long minutes=TimeUnit.MILLISECONDS.toSeconds(t2-t1);
                  double min=(double)minutes/60;
                  
//                           System.out.println("check if file is large ended"+(new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime())).toString());
            LOGGER.log( Level.SEVERE,(ANSI_BLUE+"check if file is large executed with "+min+"minutes"+ANSI_RESET));

          int count=0;
rs.next();
         count=rs.getInt("COUNT(myid)"); 
     return count;
    }
     //enf
        @Override
        
         public int smppcheckIfFileIsLarge(String user, String startDate, String endDate,String scheduleStart,String scheduleEnd, Connection conn) throws SQLException {
            boolean res=false;
        DateFormat outFormat = new SimpleDateFormat("yyyy-MM-dd");
//         System.out.println("Start Date"+startDate+"  End Date"+endDate);
//        String schsdate = (outFormat.format(startDate));
//        String schedate = (outFormat.format(endDate));
//
//        schsdate = schsdate + " 00:00:01";
//        schedate = schedate + " 23:59:59";

        String query = " SELECT id from tUSER WHERE username='" + user + "'";
        Statement stmt1 = conn.createStatement();
        ResultSet rs1 = stmt1.executeQuery(query);
        String adminid = null;
        
        while (rs1.next()) {
            adminid = rs1.getString("id");
//            System.out.println("The super ID" + adminid);
        }
          String sql = null;
        if (user == null) {
            //SELECT dbSMPPGateway.tSMSOUT.source_addr, dbSMPPGateway.tSMSOUT.destination_addr, dbSMPPGateway.tSMSOUT.short_message, dbSMPPGateway.tSMSOUT.time_submitted, dbSMPPGateway.tSMSOUT.time_processed, dbSMPPGateway.tSMSOUT.registered_delivery, dbSMPPGateway.tUSER.username FROM dbSMPPGateway.tSMSOUT LEFT JOIN dbSMPPGateway.tUSER ON dbSMPPGateway.tSMSOUT.tUSER_id=dbSMPPGateway.tUSER.id WHERE time_submitted >= '201509120000' AND time_submitted <= '201509120000';
            sql = "SELECT dbSMPPGateway.tSMSOUT.id AS myid,dbSMPPGateway.tSMSOUT.status, "
                    + "dbSMPPGateway.tSMSOUT.source_addr, dbSMPPGateway.tSMSOUT.destination_addr, "
                    + "dbSMPPGateway.tSMSOUT.short_message, dbSMPPGateway.tSMSOUT.time_submitted, "
                    + "dbSMPPGateway.tSMSOUT.time_processed, dbSMPPGateway.tSMSOUT.registered_delivery, "
                    + "dbSMPPGateway.tUSER.username,dbSMPPGateway.tSMSSTATUS.desctiption FROM dbSMPPGateway.tSMSOUT"
                    + " LEFT JOIN dbSMPPGateway.tUSER ON dbSMPPGateway.tSMSOUT.tUSER_id=dbSMPPGateway.tUSER.id LEFT "
                    + "JOIN dbSMPPGateway.tSMSSTATUS ON dbSMPPGateway.tSMSOUT.status=dbSMPPGateway.tSMSSTATUS.id WHERE"
                    + " time_submitted >= '" + startDate + "' AND time_submitted <= '" + endDate + "' "
                    + "UNION ALL SELECT dbSMPPGateway.tSMSOUT_COMPLETE.id AS myid,dbSMPPGateway.tSMSOUT_COMPLETE.status, "
                    + "dbSMPPGateway.tSMSOUT_COMPLETE.source_addr, dbSMPPGateway.tSMSOUT_COMPLETE.destination_addr, "
                    + "dbSMPPGateway.tSMSOUT_COMPLETE.short_message, dbSMPPGateway.tSMSOUT_COMPLETE.time_submitted, "
                    + "dbSMPPGateway.tSMSOUT_COMPLETE.time_processed, dbSMPPGateway.tSMSOUT_COMPLETE.registered_delivery,"
                    + " dbSMPPGateway.tUSER.username,dbSMPPGateway.tSMSSTATUS.desctiption FROM dbSMPPGateway.tSMSOUT_COMPLETE LEFT"
                    + " JOIN dbSMPPGateway.tUSER ON dbSMPPGateway.tSMSOUT_COMPLETE.tUSER_id=dbSMPPGateway.tUSER.id LEFT JOIN"
                    + " dbSMPPGateway.tSMSSTATUS ON dbSMPPGateway.tSMSOUT_COMPLETE.status=dbSMPPGateway.tSMSSTATUS.id WHERE "
                    + "time_submitted >= '" + startDate + "' AND time_submitted <= '" + endDate + "' order by myid desc";
        } else if (user.isEmpty()) {

            sql = "SELECT dbSMPPGateway.tSMSOUT.id AS myid,dbSMPPGateway.tSMSOUT.status, dbSMPPGateway.tSMSOUT.source_addr, dbSMPPGateway.tSMSOUT.destination_addr, dbSMPPGateway.tSMSOUT.short_message, dbSMPPGateway.tSMSOUT.time_submitted, dbSMPPGateway.tSMSOUT.time_processed, dbSMPPGateway.tSMSOUT.registered_delivery, dbSMPPGateway.tUSER.username,dbSMPPGateway.tSMSSTATUS.desctiption FROM dbSMPPGateway.tSMSOUT LEFT JOIN dbSMPPGateway.tUSER ON dbSMPPGateway.tSMSOUT.tUSER_id=dbSMPPGateway.tUSER.id LEFT JOIN dbSMPPGateway.tSMSSTATUS ON dbSMPPGateway.tSMSOUT.status=dbSMPPGateway.tSMSSTATUS.id WHERE time_submitted >= '" + startDate + "' AND time_submitted <= '" + endDate + "' "
                    + "UNION ALL SELECT dbSMPPGateway.tSMSOUT_COMPLETE.id AS myid,dbSMPPGateway.tSMSOUT_COMPLETE.status, dbSMPPGateway.tSMSOUT_COMPLETE.source_addr, dbSMPPGateway.tSMSOUT_COMPLETE.destination_addr, dbSMPPGateway.tSMSOUT_COMPLETE.short_message, dbSMPPGateway.tSMSOUT_COMPLETE.time_submitted, dbSMPPGateway.tSMSOUT_COMPLETE.time_processed, dbSMPPGateway.tSMSOUT_COMPLETE.registered_delivery, dbSMPPGateway.tUSER.username,dbSMPPGateway.tSMSSTATUS.desctiption FROM dbSMPPGateway.tSMSOUT_COMPLETE LEFT JOIN dbSMPPGateway.tUSER ON dbSMPPGateway.tSMSOUT_COMPLETE.tUSER_id=dbSMPPGateway.tUSER.id LEFT JOIN dbSMPPGateway.tSMSSTATUS ON dbSMPPGateway.tSMSOUT_COMPLETE.status=dbSMPPGateway.tSMSSTATUS.id WHERE time_submitted >= '" + startDate + "' AND time_submitted <= '" + endDate + "' order by myid desc";
        } else {

            sql = "SELECT dbSMPPGateway.tSMSOUT.id AS myid,dbSMPPGateway.tSMSOUT.status, dbSMPPGateway.tSMSOUT.source_addr, dbSMPPGateway.tSMSOUT.destination_addr, dbSMPPGateway.tSMSOUT.short_message, dbSMPPGateway.tSMSOUT.time_submitted, dbSMPPGateway.tSMSOUT.time_processed, dbSMPPGateway.tSMSOUT.registered_delivery, dbSMPPGateway.tUSER.username,dbSMPPGateway.tSMSSTATUS.desctiption FROM dbSMPPGateway.tSMSOUT LEFT JOIN dbSMPPGateway.tUSER ON dbSMPPGateway.tSMSOUT.tUSER_id=dbSMPPGateway.tUSER.id LEFT JOIN dbSMPPGateway.tSMSSTATUS ON dbSMPPGateway.tSMSOUT.status=dbSMPPGateway.tSMSSTATUS.id WHERE time_submitted >= '" + startDate + "' AND time_submitted <= '" + endDate + "' AND username = '" + user + "' "
                    + "UNION ALL SELECT dbSMPPGateway.tSMSOUT_COMPLETE.id AS myid,dbSMPPGateway.tSMSOUT_COMPLETE.status, dbSMPPGateway.tSMSOUT_COMPLETE.source_addr, dbSMPPGateway.tSMSOUT_COMPLETE.destination_addr, dbSMPPGateway.tSMSOUT_COMPLETE.short_message, dbSMPPGateway.tSMSOUT_COMPLETE.time_submitted, dbSMPPGateway.tSMSOUT_COMPLETE.time_processed, dbSMPPGateway.tSMSOUT_COMPLETE.registered_delivery, dbSMPPGateway.tUSER.username,dbSMPPGateway.tSMSSTATUS.desctiption FROM dbSMPPGateway.tSMSOUT_COMPLETE LEFT JOIN dbSMPPGateway.tUSER ON dbSMPPGateway.tSMSOUT_COMPLETE.tUSER_id=dbSMPPGateway.tUSER.id LEFT JOIN dbSMPPGateway.tSMSSTATUS ON dbSMPPGateway.tSMSOUT_COMPLETE.status=dbSMPPGateway.tSMSSTATUS.id WHERE time_submitted >= '" + startDate + "' AND time_submitted <= '" + endDate + "' AND username = '" + user + "' order by myid desc";
        }
        
        sql="SELECT COUNT(*) FROM ("+sql+") as t";
          PreparedStatement ps=conn.prepareStatement(sql);
          ResultSet rs=ps.executeQuery();
          int count=0;
//          while(rs.next()){
              rs.next();
         count=rs.getInt("COUNT(*)");
//          }
//         if(count>=20)
//             res=true;
            
     return count;
    }
         
         @Override
             public Map<String, Object> getSummarySms(String user, String startDate, String endDate,String scheduleStart,String scheduleEnd, Connection conn, int limit) throws SQLException {

        DateFormat outFormat = new SimpleDateFormat("yyyy-MM-dd");
//        String schsdate = (outFormat.format(startDate));
//        String schedate = (outFormat.format(endDate));
//
//        schsdate = schsdate + " 00:00:01";
//        schedate = schedate + " 23:59:59";

        String query = " SELECT id from tUSER WHERE username='" + user + "'";
        Statement stmt1 = conn.createStatement();
        ResultSet rs1 = stmt1.executeQuery(query);
        String adminid = null;
        while (rs1.next()) {
            adminid = rs1.getString("id");
//            System.out.println("The super ID" + adminid);
        }
        String sql = null;
        String sqlReseller = null;
        if (user == null) {
            //SELECT tSMSOUT.id AS myid,tSMSOUT.source_addr, tSMSOUT.destination_addr, tSMSOUT.message_payload, tSMSOUT.time_submitted, tSMSOUT.time_processed, tSMSOUT.user, tSMSOUT.status, tSMSSTATUS.desctiption  FROM tSMSOUT LEFT JOIN tSMSSTATUS ON tSMSOUT.status=tSMSSTATUS.id WHERE time_submitted >= '" + startDate + "' AND time_submitted <= '" + endDate + "' 
            //UNION ALL SELECT tSMSOUT_COMPLETE.id AS myid,tSMSOUT_COMPLETE.source_addr, tSMSOUT_COMPLETE.destination_addr, tSMSOUT_COMPLETE.message_payload, tSMSOUT_COMPLETE.time_submitted, tSMSOUT_COMPLETE.time_processed, tSMSOUT_COMPLETE.user, tSMSOUT_COMPLETE.status, tSMSSTATUS.desctiption  FROM tSMSOUT_COMPLETE LEFT JOIN tSMSSTATUS ON tSMSOUT_COMPLETE.status=tSMSSTATUS.id WHERE time_submitted >= '" + startDate + "' AND time_submitted <= '" + endDate + "' order by myid desc";

            sql = "SELECT tSMSOUT.id as myid, tSMSOUT.message_payload "
                    + " FROM tSMSOUT LEFT JOIN tSMSSTATUS ON tSMSOUT.status=tSMSSTATUS.id WHERE time_submitted >= '" + startDate + "' "
                    + "AND time_submitted <= '" + endDate + "' "
                    + "UNION ALL SELECT   tSMSOUT_COMPLETE.id as myid,tSMSOUT_COMPLETE.message_payload "
                    + "   FROM tSMSOUT_COMPLETE"
                    + " LEFT JOIN tSMSSTATUS ON tSMSOUT_COMPLETE.status=tSMSSTATUS.id WHERE time_submitted >= '" + startDate + "' AND time_submitted <= '" + endDate + "'   UNION ALL "
                    + "select  sc.id as myid, sc.message as message_payload "
                    + " from tUSERSMSSCHEDULE sc where  sc.sendTime >= '"+scheduleStart+"' AND sc.sendTime  <= '"+scheduleEnd+"'   order by myid desc";

            sqlReseller = "SELECT tSMSOUT.id as myid, tSMSOUT.message_payload, tSMSOUT.time_submitted, tSMSOUT.time_processed, tSMSOUT.user, tSMSOUT.status, tSMSSTATUS.desctiption  "
                    + "FROM tSMSOUT LEFT JOIN tSMSSTATUS ON tSMSOUT.status=tSMSSTATUS.id inner join tUSER on tSMSOUT.user = tUSER.username "
                    + "WHERE time_submitted >= '" + startDate + "' AND time_submitted <= '" + endDate + "' and tUSER.agent = '"+user_id+"' UNION ALL "
                    + "SELECT  tSMSOUT_COMPLETE.id as myid, tSMSOUT_COMPLETE.message_payload,tSMSOUT_COMPLETE.time_submitted , tSMSOUT_COMPLETE.time_processed, tSMSOUT_COMPLETE.user,tSMSOUT_COMPLETE.status ,tSMSSTATUS.desctiption "
                    
                    + "FROM tSMSOUT_COMPLETE LEFT JOIN tSMSSTATUS ON tSMSOUT_COMPLETE.status=tSMSSTATUS.id inner join tUSER on tSMSOUT_COMPLETE.user = tUSER.username"
                    + " WHERE time_submitted >= " + startDate + " AND time_submitted <= '" + endDate + "' and tUSER.agent = '" + user_id + "' order by myid desc";

        } else if (user.isEmpty()) {
            //sql = "SELECT tSMSOUT.id AS myid,tSMSOUT.source_addr, tSMSOUT.destination_addr, tSMSOUT.message_payload, tSMSOUT.time_submitted, tSMSOUT.time_processed, tSMSOUT.user, tSMSOUT.status, tSMSSTATUS.desctiption  FROM tSMSOUT LEFT JOIN tSMSSTATUS ON tSMSOUT.status=tSMSSTATUS.id WHERE time_submitted >= '" + startDate + "' AND time_submitted <= '" + endDate + "'
            //UNION ALL SELECT tSMSOUT_COMPLETE.id AS myid,tSMSOUT_COMPLETE.source_addr, tSMSOUT_COMPLETE.destination_addr, tSMSOUT_COMPLETE.message_payload, tSMSOUT_COMPLETE.time_submitted, tSMSOUT_COMPLETE.time_processed, tSMSOUT_COMPLETE.user, tSMSOUT_COMPLETE.status, tSMSSTATUS.desctiption  FROM tSMSOUT_COMPLETE LEFT JOIN tSMSSTATUS ON tSMSOUT_COMPLETE.status=tSMSSTATUS.id WHERE time_submitted >= '" + startDate + "' AND time_submitted <= '" + endDate + "' order by myid desc";
                
            sql = "SELECT tSMSOUT.id as myid,tSMSOUT.message_payload FROM tSMSOUT LEFT JOIN tSMSSTATUS ON tSMSOUT.status=tSMSSTATUS.id WHERE time_submitted >= '" + startDate + "' AND time_submitted <= '" + endDate + "' "
                    + "UNION ALL SELECT  tSMSOUT_COMPLETE.id as myid, tSMSOUT_COMPLETE.message_payload FROM tSMSOUT_COMPLETE LEFT JOIN tSMSSTATUS ON tSMSOUT_COMPLETE.status=tSMSSTATUS.id WHERE time_submitted >= '" + startDate + "' AND time_submitted <= '" + endDate + "' UNION ALL "
                    + "select sc.id as myid, sc.message as message_payload "
                    + " from tUSERSMSSCHEDULE sc where  sc.sendTime >= '"+scheduleStart+"' AND sc.sendTime  <= '"+scheduleEnd+"'   order by myid desc";

            sqlReseller = "SELECT  tSMSOUT.id as myid, tSMSOUT.message_payload "
                    + "FROM tSMSOUT LEFT JOIN tSMSSTATUS ON tSMSOUT.status=tSMSSTATUS.id inner join tUSER on tSMSOUT.user = tUSER.username "
                    + "WHERE time_submitted >= '"+startDate+"' AND time_submitted <= '"+endDate+"' and tUSER.agent = '"+user_id+"' UNION ALL "
                    + "SELECT  tSMSOUT_COMPLETE.id as myid, tSMSOUT_COMPLETE.message_payload"
                    + " FROM tSMSOUT_COMPLETE LEFT JOIN tSMSSTATUS ON tSMSOUT_COMPLETE.status=tSMSSTATUS.id inner join tUSER on tSMSOUT_COMPLETE.user = tUSER.username WHERE time_submitted >= '"+startDate+"' AND time_submitted <= '"+endDate+"' and tUSER.agent = '" + user_id + "' order by myid desc";
        } else {
            //  sql = "SELECT tSMSOUT.id AS myid,tSMSOUT.source_addr, tSMSOUT.destination_addr, tSMSOUT.message_payload, tSMSOUT.time_submitted, tSMSOUT.time_processed, tSMSOUT.user, tSMSOUT.status, tSMSSTATUS.desctiption  FROM tSMSOUT LEFT JOIN tSMSSTATUS ON tSMSOUT.status=tSMSSTATUS.id LEFT JOIN tUSER ON tUSER.username=tSMSOUT.user  WHERE time_submitted >= '" + startDate + "' AND time_submitted <= '" + endDate + "' AND (user = '" + user + "' OR tUSER.super_account_id= '" + adminid + "' ) 
            //UNION ALL SELECT tSMSOUT_COMPLETE.id AS myid,tSMSOUT_COMPLETE.source_addr, tSMSOUT_COMPLETE.destination_addr, tSMSOUT_COMPLETE.message_payload, tSMSOUT_COMPLETE.time_submitted, tSMSOUT_COMPLETE.time_processed, tSMSOUT_COMPLETE.user, tSMSOUT_COMPLETE.status, tSMSSTATUS.desctiption  FROM tSMSOUT_COMPLETE LEFT JOIN tSMSSTATUS ON tSMSOUT_COMPLETE.status=tSMSSTATUS.id LEFT JOIN tUSER ON tUSER.username=tSMSOUT_COMPLETE.user  WHERE time_submitted >= '" + startDate + "' AND time_submitted <= '" + endDate + "' AND (user = '" + user + "'  OR tUSER.super_account_id= '" + adminid + "' ) order by myid desc";
            sql = "SELECT  tSMSOUT.id as myid, message_payload  FROM tSMSOUT LEFT JOIN tSMSSTATUS ON tSMSOUT.status=tSMSSTATUS.id  LEFT JOIN tUSER ON tUSER.username=tSMSOUT.user WHERE time_submitted >= '" + startDate + "' AND time_submitted <= '" + endDate + "' AND (user = '" + user + "' OR tUSER.agent= '" + adminid + "' OR tUSER.super_account_id='" + adminid + "' )"
                    + "UNION ALL SELECT  tSMSOUT_COMPLETE.id as myid,tSMSOUT_COMPLETE.message_payload FROM tSMSOUT_COMPLETE LEFT JOIN tSMSSTATUS ON tSMSOUT_COMPLETE.status=tSMSSTATUS.id LEFT JOIN tUSER ON tUSER.username=tSMSOUT_COMPLETE.user WHERE time_submitted >= '" + startDate + "' AND time_submitted <= '" + endDate + "' AND (user = '" + user + "' OR tUSER.agent= '" + adminid + "' OR tUSER.super_account_id='" + adminid + "' ) order by myid desc";
            
            sqlReseller = sql;
        }
        
        String sqlLimit="";
        if(limit==0){
           sqlLimit="";
        }
        else {
            sqlLimit="limit "+limit;

        }

        //System.out.println(sqlReseller);
//        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("reportSQL", UserServiceImpl.isReseller().equalsIgnoreCase("none") ? sqlReseller : sql);
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(UserServiceImpl.isReseller().equalsIgnoreCase("none") ? sqlReseller+" "+sqlLimit : sql+" "+sqlLimit);
//        rs.last();
        //check should come here ..if user selects proceed and the rows are 
        //more than 100,run the code else just continue executing but limit the number of rows

        //System.out.println("The last row in the table::::<<<<<>>>>>:::: " + rs.getRow());
//        rs.first();
        //check the list size here 
        //if( list is less than the number set run the below code){

        SMSOut smsout = new SMSOut();
        //ResultSet rs= smsout.getResultSet();
        //int rows =(Integer)FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("ListSize");

        //System.out.println("List has "+rows+" Rows ");
        List<SMSOut> result = new ArrayList<>();
        int count=0;
        while (rs.next()) {

            count++;
            SMSOut smsOut = new SMSOut();
            smsOut.setMessagePayload(rs.getString("message_payload"));
            result.add(smsOut);
        }

        int noSMS = 0;
        for (SMSOut sms : result) {
            noSMS += sms.getSmsCount();
        }

        Map<String, Object> mapResult = new HashMap<>();
        mapResult.put("result", result);
        mapResult.put("noSMS", noSMS);
        //} end of if else
//System.out.println("Total Rows:"+count);
        return mapResult;
    }
             
             
              @Override
    public Map<String, Object> smppgetSummarySms(String user, String startDate, String endDate, Connection conn, int limit) throws SQLException {
           
        String sql = null;
        if (user == null) {
            //horace changed short_message to short_message and username
            //SELECT dbSMPPGateway.tSMSOUT.source_addr, dbSMPPGateway.tSMSOUT.destination_addr, dbSMPPGateway.tSMSOUT.short_message, dbSMPPGateway.tSMSOUT.time_submitted, dbSMPPGateway.tSMSOUT.time_processed, dbSMPPGateway.tSMSOUT.registered_delivery, dbSMPPGateway.tUSER.username FROM dbSMPPGateway.tSMSOUT LEFT JOIN dbSMPPGateway.tUSER ON dbSMPPGateway.tSMSOUT.tUSER_id=dbSMPPGateway.tUSER.id WHERE time_submitted >= '201509120000' AND time_submitted <= '201509120000';
            sql = "SELECT dbSMPPGateway.tSMSOUT.id AS myid, "
                    
                    + "dbSMPPGateway.tSMSOUT.short_message AS message_payload"
                    + " FROM dbSMPPGateway.tSMSOUT"
                    + " LEFT JOIN dbSMPPGateway.tUSER ON dbSMPPGateway.tSMSOUT.tUSER_id=dbSMPPGateway.tUSER.id LEFT "
                    + "JOIN dbSMPPGateway.tSMSSTATUS ON dbSMPPGateway.tSMSOUT.status=dbSMPPGateway.tSMSSTATUS.id WHERE"
                    + " time_submitted >= '" + startDate + "' AND time_submitted <= '" + endDate + "' "
                    + "UNION ALL SELECT dbSMPPGateway.tSMSOUT_COMPLETE.id AS myid, "
                    
                    + "dbSMPPGateway.tSMSOUT_COMPLETE.short_message"
                    
                    + "  FROM dbSMPPGateway.tSMSOUT_COMPLETE LEFT"
                    + " JOIN dbSMPPGateway.tUSER ON dbSMPPGateway.tSMSOUT_COMPLETE.tUSER_id=dbSMPPGateway.tUSER.id LEFT JOIN"
                    + " dbSMPPGateway.tSMSSTATUS ON dbSMPPGateway.tSMSOUT_COMPLETE.status=dbSMPPGateway.tSMSSTATUS.id WHERE "
                    + "time_submitted >= '" + startDate + "' AND time_submitted <= '" + endDate + "' order by myid desc";
        } else if (user.isEmpty()) {

            sql = "SELECT dbSMPPGateway.tSMSOUT.id AS myid, tSMSOUT.short_message AS message_payload FROM dbSMPPGateway.tSMSOUT LEFT JOIN dbSMPPGateway.tUSER ON dbSMPPGateway.tSMSOUT.tUSER_id=dbSMPPGateway.tUSER.id LEFT JOIN dbSMPPGateway.tSMSSTATUS ON dbSMPPGateway.tSMSOUT.status=dbSMPPGateway.tSMSSTATUS.id WHERE time_submitted >= '" + startDate + "' AND time_submitted <= '" + endDate + "' "
                    + "UNION ALL SELECT dbSMPPGateway.tSMSOUT_COMPLETE.id AS myid, dbSMPPGateway.tSMSOUT_COMPLETE.short_message FROM dbSMPPGateway.tSMSOUT_COMPLETE LEFT JOIN dbSMPPGateway.tUSER ON dbSMPPGateway.tSMSOUT_COMPLETE.tUSER_id=dbSMPPGateway.tUSER.id LEFT JOIN dbSMPPGateway.tSMSSTATUS ON dbSMPPGateway.tSMSOUT_COMPLETE.status=dbSMPPGateway.tSMSSTATUS.id WHERE time_submitted >= '" + startDate + "' AND time_submitted <= '" + endDate + "' order by myid desc";
        } else {

            sql = "SELECT dbSMPPGateway.tSMSOUT.id AS myid, dbSMPPGateway.tSMSOUT.short_message AS message_payload FROM dbSMPPGateway.tSMSOUT LEFT JOIN dbSMPPGateway.tUSER ON dbSMPPGateway.tSMSOUT.tUSER_id=dbSMPPGateway.tUSER.id LEFT JOIN dbSMPPGateway.tSMSSTATUS ON dbSMPPGateway.tSMSOUT.status=dbSMPPGateway.tSMSSTATUS.id WHERE time_submitted >= '" + startDate + "' AND time_submitted <= '" + endDate + "' AND username = '" + user + "' "
                    + "UNION ALL SELECT dbSMPPGateway.tSMSOUT_COMPLETE.id AS myid, dbSMPPGateway.tSMSOUT_COMPLETE.short_message FROM dbSMPPGateway.tSMSOUT_COMPLETE LEFT JOIN dbSMPPGateway.tUSER ON dbSMPPGateway.tSMSOUT_COMPLETE.tUSER_id=dbSMPPGateway.tUSER.id LEFT JOIN dbSMPPGateway.tSMSSTATUS ON dbSMPPGateway.tSMSOUT_COMPLETE.status=dbSMPPGateway.tSMSSTATUS.id WHERE time_submitted >= '" + startDate + "' AND time_submitted <= '" + endDate + "' AND username = '" + user + "' order by myid desc";
        }
        List<SMPPOut> result = new ArrayList<>();

        
//        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("reportSQL", sql);
       
        String sqlLimit="";
        if(limit==0){
           sqlLimit="";
        }
        else {
            sqlLimit="limit "+limit;
           
            
        }
        
        Statement stmt = conn.createStatement();     
        ResultSet rs = stmt.executeQuery(sql+" "+sqlLimit);
        
        while (rs.next()) {
            SMPPOut smsOut = new SMPPOut();
            
            smsOut.setMessagePayload(rs.getString("message_payload"));

            result.add(smsOut);
        }

        int noSMS = 0;
        for (SMPPOut sms : result) {

            noSMS += sms.getSmsCount();
        }
        LOG.log(Level.INFO, "The message: {0}", noSMS);

        Map<String, Object> mapResult = new HashMap<>();
        mapResult.put("result", result);
        mapResult.put("noSMS", noSMS);

        return mapResult;
    }

}
