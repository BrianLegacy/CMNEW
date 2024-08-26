/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ke.co.mspace.nonsmppmanager.service;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import ke.co.mspace.nonsmppmanager.model.OPTOut;
import ke.co.mspace.nonsmppmanager.model.SMPPOut;
import ke.co.mspace.nonsmppmanager.model.SMSOut;

/**
 *
 * @author Norrey Osako
 */
public interface SMSOutServiceApi {

    List<OPTOut> fetchOptOutReport(String startDate, String endDate);
    
    int getTotalSmsCount(String startDate, String endDate, String user);
    
    int getTotalSmppCount(String startDate, String endDate, String user);
    
    List<SMSOut> fetchSMSReport(String user, String startDate, String endDate);

    List<SMPPOut> fetchSMPPReport(String user, String startDate, String endDate);

    Map<String, Object> userSMSOutReport(String user, String startDate, String endDate, String scheduleStart, String scheduleEnd, Connection conn, int limit) throws SQLException;

    //added by horace
    public Map<String, Object> getSummarySms(String user, String startDate, String endDate, String scheduleStart, String scheduleEnd, Connection conn, int limit) throws SQLException;

    public Map<String, Object> smppgetSummarySms(String user, String startDate, String endDate, Connection conn, int limit) throws SQLException;

//added by horace
    public int checkIfFileIsLarge(String user, String startDate, String endDate, String scheduleStart, String scheduleEnd, Connection conn) throws SQLException;

    public void generateXSL(String user, String startDate, String endDate, Connection conn) throws SQLException;

    public void generateSMPPXSL(String user, String startDate, String endDate, Connection conn) throws SQLException;

    public void generateOPTXSL(String startDate, String endDate, Connection conn) throws SQLException;

    public List<String> getUsernames(Connection conn) throws SQLException;

    public Map<String, Object> smsOutGroupBy(String user, String startDate, String endDate, Connection conn) throws SQLException;

    public Map<String, String> smsOutGroupByUser(String startDate, String endDate, Connection conn) throws SQLException;

    public String getRealSMSStatus(String smsID, Connection conn) throws SQLException;

    public List<String> getSMPPUsernames(Connection conn) throws SQLException;

    public Map<String, Object> smppOutReport(String user, String startDate, String endDate, Connection conn, int limit) throws SQLException;

    public Map<String, Object> optOutReport(String startDate, String endDate, Connection conn) throws SQLException;

    public ResultSet getResultSet(String user, String startDate, String endDate, Connection conn);

    public void smppSetSql(String user, String startDate, String endDate, Connection conn) throws SQLException;

    public void smsSetSql(String user, String startDate, String endDate, String scheduleStart, String scheduleEnd, Connection conn) throws SQLException;

    public int smppcheckIfFileIsLarge(String user, String startDate, String endDate, String scheduleStart, String scheduleEnd, Connection conn) throws SQLException;
}
