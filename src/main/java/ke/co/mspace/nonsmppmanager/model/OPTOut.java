
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ke.co.mspace.nonsmppmanager.model;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import ke.co.mspace.nonsmppmanager.service.SMSOutServiceApi;
import ke.co.mspace.nonsmppmanager.service.SMSOutServiceImpl;
import ke.co.mspace.nonsmppmanager.util.HikariJDBCDataSource;
import ke.co.mspace.nonsmppmanager.util.JdbcUtil;


/**
 *
 * @author Norrey Osako
 */
@ManagedBean
@ViewScoped
public class OPTOut implements Serializable {

    private Long id;
    private Long tUSER_id;
    private String seqNo;

    private String sourceAddr;
    private String destinationAddr;
    private String messagePayload;
    private String reply;
    private char userMessageReference;
    private String timeSubmitted;
    private String emailed;
    private char status;
    private String errorinfo;
    private String messageId;
    private String sentby;

    private String rule;
    private String user;
    private String submittedby;

    private Date reportStartDate = new Date();
    private Date reportEndDate = new Date();
    private int smsCount;
    private String displayTimeSubmitted; 
    private List<String> usernames;
    private int totaSMS = 0;
    private String users; 
    private List<OPTOut> smsOutReport;
    private String smsOutReport2;
    private String smsOutReport3;
    private static final Logger LOG = Logger.getLogger(OPTOut.class.getName());

    public List<OPTOut> getSmsOutReport() {
        return smsOutReport;
    }

    public void setSmsOutReport(List<OPTOut> smsOutReport) {
        
        this.smsOutReport = smsOutReport;
    }

    public String getSmsOutReport2() {

        return smsOutReport2;
    }

    public void setSmsOutReport2(String smsOutReport2) {
        this.smsOutReport2 = smsOutReport2;
    }

    public String getSmsOutReport3() {
        return smsOutReport3;
    }

    public void setSmsOutReport3(String smsOutReport3) {
        this.smsOutReport3 = smsOutReport3;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSeqNo() {
        return seqNo;
    }

    public void setSeqNo(String seqNo) {
        this.seqNo = seqNo;
    }

    public String getSourceAddr() {
        return sourceAddr;
    }

    public void setSourceAddr(String sourceAddr) {
        this.sourceAddr = sourceAddr;
    }

    public String getDestinationAddr() {
        return destinationAddr;
    }

    public void setDestinationAddr(String destinationAddr) {
        this.destinationAddr = destinationAddr;
    }

    public String getMessagePayload() {
        return messagePayload;
    }

    public void setMessagePayload(String messagePayload) {
        this.messagePayload = messagePayload;
    }

    public String getReply() {
        return reply;
    }

    public void setReply(String reply) {
        this.reply = reply;
    }
    
    public char getUserMessageReference() {
        return userMessageReference;
    }

    public void setUserMessageReference(char userMessageReference) {
        this.userMessageReference = userMessageReference;
    }

    public String getTimeSubmitted() {
        return timeSubmitted;
    }

    public void setTimeSubmitted(String timeSubmitted) {
        this.timeSubmitted = timeSubmitted;
    }

    public String getEmailed() {
        return emailed;
    }

    public void setEmailed(String emailed) {
        this.emailed = emailed;
    }
 
    public String getErrorinfo() {
        return errorinfo;
    }

    public void setErrorinfo(String errorinfo) {
        this.errorinfo = errorinfo;
    }

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public String getSentby() {
        return sentby;
    }

    public void setSentby(String sentby) {
        this.sentby = sentby;
    }

    public String getRule() {
        return rule;
    }

    public void setRule(String rule) {
        this.rule = rule;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getSubmittedby() {
        return submittedby;
    }

    public void setSubmittedby(String submittedby) {
        this.submittedby = submittedby;
    }

    public Date getReportStartDate() {
        return reportStartDate;
    }

    public void setReportStartDate(Date reportStartDate) {
        this.reportStartDate = reportStartDate;
    }

    public Date getReportEndDate() {
        return reportEndDate;
    }

    public void setReportEndDate(Date reportEndDate) {

        Calendar date = Calendar.getInstance();
        date.setTime(reportEndDate);

        date.set(Calendar.HOUR_OF_DAY, 23);
        date.set(Calendar.MINUTE, 59);
        date.set(Calendar.SECOND, 59);
        date.set(Calendar.MILLISECOND, 999);

        Date newEndDate = date.getTime();
        this.reportEndDate = newEndDate;
    }

    public int getSmsCount() {

        return getSmsCount(messagePayload);

    }

    public void setSmsCount(int smsCount) {
        this.smsCount = smsCount;
    }

    public String getUsers() {
        return users;
    }

    public void setUsers(String users) {
        this.users = users;
    }
 
    public int getTotaSMS() {

        return totaSMS;
    }

    public void setTotaSMS(int totaSMS) {
        this.totaSMS = totaSMS;
    }

    public String getDisplayTimeSubmitted() {

        SimpleDateFormat displayFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        Date d = null;
        try {
            d = displayFormat.parse(timeSubmitted);
        } catch (ParseException ex) {
            Logger.getLogger(OPTOut.class.getName()).log(Level.SEVERE, null, ex);
        }
        displayFormat.applyPattern("yyyy-MM-dd HH:mm:ss");
        displayTimeSubmitted = displayFormat.format(d);

        return displayTimeSubmitted;
    }

    public void setDisplayTimeSubmitted(String displayTimeSubmitted) {
        this.displayTimeSubmitted = displayTimeSubmitted;
    }
 
    public List<OPTOut> optOutReport() {
        List<OPTOut> report = null;
        try {
            final JdbcUtil util = new JdbcUtil();
            Connection conn = HikariJDBCDataSource.getConnectionTodbSMS();
            LOG.info("smsOutReport");
            SMSOutServiceApi service = new SMSOutServiceImpl();
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
            String startDate = simpleDateFormat.format(reportStartDate);
            String endDate = simpleDateFormat.format(reportEndDate);

            Map<String, Object> results = service.optOutReport(startDate, endDate, conn);
            report = (List) results.get("result");
            //LOG.log(Level.INFO, "THE STATUS VALUE IS  : {0}", this.status);
            
            //String realSMSStatus = service.getRealSMSStatus(String.valueOf(status), conn);
            int noSMS = (Integer) results.get("noSMS");
            
            this.setTotaSMS(noSMS);
            //LOG.log(Level.INFO, "THE Total SMS VALUE IS  : {0}", this.totaSMS);
            //this.setRealStatus(String.valueOf(status));
            JdbcUtil.closeConnection(conn);
        } catch (SQLException e) {
            JdbcUtil.printSQLException(e);
        }
        return report;
    }

    public String smsOutReport2() {
        String pieChartData = null;
        final JdbcUtil util = new JdbcUtil();
        Connection conn = null;
        try {

            conn = HikariJDBCDataSource.getConnectionTodbSMS();
            LOG.info("smsOutReport2");
            SMSOutServiceApi service = new SMSOutServiceImpl();
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
            String startDate = simpleDateFormat.format(reportStartDate);
            String endDate = simpleDateFormat.format(reportEndDate);

            List<Object[]> report = (List) service.smsOutGroupBy(user, startDate, endDate, conn).get("result");

            StringBuilder stringBuilder = new StringBuilder();

            for (Object[] pieData : report) {
                stringBuilder.append("[");
                stringBuilder.append('"');
                stringBuilder.append(pieData[0]);
                stringBuilder.append(" ");
                stringBuilder.append(pieData[1]);
                stringBuilder.append('"');
                stringBuilder.append(",");
                stringBuilder.append(pieData[2]);
                stringBuilder.append("]");
                stringBuilder.append(",");
            }

            if (stringBuilder.toString().equals("")) {

                return null;
            }

            pieChartData = stringBuilder.toString().substring(0,
                    stringBuilder.toString().length() - 1);

            JdbcUtil.closeConnection(conn);
        } catch (SQLException e) {
            JdbcUtil.printSQLException(e);
        }
        return pieChartData;
    }

    public String smsOutReport3() {
        String theData = null;

        try {
            Connection conn = null;
            JdbcUtil util = new JdbcUtil();
            conn = HikariJDBCDataSource.getConnectionTodbSMS();
            LOG.info("smsOutReoport3");
            SMSOutServiceApi service = new SMSOutServiceImpl();
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
            String startDate = simpleDateFormat.format(reportStartDate);
            String endDate = simpleDateFormat.format(reportEndDate);

            Map<String, String> output = service.smsOutGroupByUser(startDate, endDate, conn);

            if (output.equals("")) {

                return null;
            }

            setUsers(output.get("users"));
            theData = output.get("date");
            JdbcUtil.closeConnection(conn);
        } catch (SQLException e) {
            JdbcUtil.printSQLException(e);
        }

        return theData;
    }

    /**
     *
     */
    public void optoutTabularReport() {
        
       setSmsOutReport(optOutReport());
    }

    public void testVisualReport() {
        setSmsOutReport2(smsOutReport2());
    }

    public void testVisualAllReport() {
        setSmsOutReport3(smsOutReport3());
    }

    public void generateXLSX() {
       
            try {
                Connection conn = null;
                JdbcUtil util = new JdbcUtil();
                conn = HikariJDBCDataSource.getConnectionTodbSMS();

                LOG.info("generateXLSX");

                SMSOutServiceApi service = new SMSOutServiceImpl();
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
                String startDate = simpleDateFormat.format(reportStartDate);
                String endDate = simpleDateFormat.format(reportEndDate);
                service.generateOPTXSL(startDate, endDate, conn);
                JdbcUtil.closeConnection(conn);
            } catch (SQLException e) {
                JdbcUtil.printSQLException(e);
            }
       

    }

    public List<String> getUsernames() {
        boolean containsNames = FacesContext.getCurrentInstance().getExternalContext().getSessionMap().containsKey("smppUsernames");

        if (containsNames) {
            return (List<String>) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("smppUsernames");
        } else {
            List<String> output = null;

            try {
                Connection conn = null;
                final JdbcUtil util = new JdbcUtil();
                conn = HikariJDBCDataSource.getConnectionTodbSMS();
                LOG.info("getSMPPUsernames");
                SMSOutServiceApi service = new SMSOutServiceImpl();
                output = service.getSMPPUsernames(conn);
                JdbcUtil.closeConnection(conn);
            } catch (SQLException e) {
                JdbcUtil.printSQLException(e);
            }
            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("smppUsernames", output);
            return output;
        }
    }

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

}
