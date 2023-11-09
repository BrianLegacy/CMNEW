
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ke.co.mspace.nonsmppmanager.model;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.DateFormat;
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
import ke.co.mspace.export.LargeFileExport;
import ke.co.mspace.nonsmppmanager.service.SMSOutServiceApi;
import ke.co.mspace.nonsmppmanager.service.SMSOutServiceImpl;
import ke.co.mspace.nonsmppmanager.util.JdbcUtil;
import ke.co.mspace.nonsmppmanager.util.JsfUtil;

/**
 *
 * @author Norrey Osako
 */
@ManagedBean
@ViewScoped
public class SMPPOut implements Serializable {

    private Long id;
    private Long tUSER_id;
    private String seqNo;

    private String sourceAddr;
    private String destinationAddr;
    private String messagePayload;
    private char userMessageReference;
    private String timeSubmitted;
    private String timeProcessed;
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
    private String displayTimeProcessed;
    private List<String> usernames;
    private int totaSMS = 0;
    private String users;
    private String realStatus;
    private List<SMPPOut> smsOutReport;
    private String smsOutReport2;
    private String smsOutReport3;
    private String summaryOrDetail="Summary";
    private static final Logger LOG = Logger.getLogger(SMPPOut.class.getName());
    private boolean renderModal=false;
     private int limit=500;

    public boolean isRenderModal() {
        return renderModal;
    }

    public void setRenderModal(boolean renderModal) {
        System.out.println("calling render modal with"+renderModal);
        this.renderModal = renderModal;
    }
    public List<SMPPOut> getSmsOutReport() {
        return smsOutReport;
    }

    public void setSmsOutReport(List<SMPPOut> smsOutReport) {

        this.smsOutReport = smsOutReport;
    }

    public String getSmsOutReport2() {

        return smsOutReport2;
    }
      public String getSummaryOrDetail() {
        return summaryOrDetail;
    }

    public void setSummaryOrDetail(String summaryOrDetail) {
        this.summaryOrDetail = summaryOrDetail;
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

    public String getTimeProcessed() {
        return timeProcessed;
    }

    public void setTimeProcessed(String timeProcessed) {
        this.timeProcessed = timeProcessed;
    }

    public char getStatus() {
        return status;
    }

    public void setStatus(char status) {

        this.status = status;
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

    public String getRealStatus() {

        return realStatus;
    }

    public void setRealStatus(String realStatus) {
        this.realStatus = realStatus;
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
            Logger.getLogger(SMPPOut.class.getName()).log(Level.SEVERE, null, ex);
        }
        displayFormat.applyPattern("yyyy-MM-dd HH:mm:ss");
        displayTimeSubmitted = displayFormat.format(d);

        return displayTimeSubmitted;
    }

    public void setDisplayTimeSubmitted(String displayTimeSubmitted) {
        this.displayTimeSubmitted = displayTimeSubmitted;
    }

    public String getDisplayTimeProcessed() {
        SimpleDateFormat displayFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        Date d = null;
        try {
            d = displayFormat.parse(timeProcessed);
        } catch (ParseException ex) {
            Logger.getLogger(SMPPOut.class.getName()).log(Level.SEVERE, null, ex);
        }
        displayFormat.applyPattern("yyyy-MM-dd HH:mm:ss");
        displayTimeProcessed = displayFormat.format(d);

        return displayTimeProcessed;
    }

    public void setDisplayTimeProcessed(String displayTimeProcessed) {
        this.displayTimeProcessed = displayTimeProcessed;
    }
   private int numOfSMS;

    public int getNumOfSMS() {
        return numOfSMS;
    }

    public void setNumOfSMS(int numOfSMS) {
        this.numOfSMS = numOfSMS;
    }
    public List<SMPPOut> smppOutReport() {
        List<SMPPOut> report = null;
        try {
            final JdbcUtil util = new JdbcUtil();
            Connection conn = util.getConnectionTodbSMS();
            LOG.info("smppOutReport");
            SMSOutServiceApi service = new SMSOutServiceImpl();
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
            String startDate = simpleDateFormat.format(reportStartDate);
            String endDate = simpleDateFormat.format(reportEndDate);
             //added by horace
                DateFormat outFormat = new SimpleDateFormat("yyyy-MM-dd");
            System.out.println("Start Date" + reportStartDate + "  End Date" + reportEndDate);
            String schsdate = (outFormat.format(reportStartDate));
            String schedate = (outFormat.format(reportEndDate));

            schsdate = schsdate + " 00:00:01";
            schedate = schedate + " 23:59:59";
            int countOfSMS=service.smppcheckIfFileIsLarge(user, startDate, endDate,schsdate,schedate, conn);
                        System.out.println("Returned count "+countOfSMS);

           setNumOfSMS(countOfSMS);
            if(numOfSMS>limit){
              renderModal = true;
              
               service.smppSetSql(user, startDate, endDate, conn);
               
           
          //end
//            Map<String, Object> results = service.smppOutReport(user, startDate, endDate, conn,limit);
//            report = (List) results.get("result");
//            LOG.log(Level.INFO, "THE STATUS VALUE IS  : {0}", this.status);
//            String realSMSStatus = service.getRealSMSStatus(String.valueOf(status), conn);
//            int noSMS = (Integer) results.get("noSMS");
//            this.setTotaSMS(noSMS);
//            this.setRealStatus(String.valueOf(status));
            //horace
               JsfUtil.addSuccessMessage("Can only display/export reports less than 15000 rows. To get full report use the bulk functionality");
           //
            JdbcUtil.closeConnection(conn);
          }
           else{
               System.out.println("failed");
           
          //end
            Map<String, Object> results = service.smppOutReport(user, startDate, endDate, conn,0);
                
            report = (List) results.get("result");
            LOG.log(Level.INFO, "THE STATUS VALUE IS  : {0}", this.status);
            String realSMSStatus = service.getRealSMSStatus(String.valueOf(status), conn);
            int noSMS = (Integer) results.get("noSMS");
            this.setTotaSMS(noSMS);
            this.setRealStatus(String.valueOf(status));
            JdbcUtil.closeConnection(conn);
           }
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

            conn = util.getConnectionTodbSMS();
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
            conn = util.getConnectionTodbSMS();
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

    public void smppTabularReport() {

        //if (user == null || user.equals("")) {
        //   JsfUtil.addErrorMessage("User not selected.");
        // } else {
        setSmsOutReport(smppOutReport());
        // }
    }

    public void testVisualReport() {
        setSmsOutReport2(smsOutReport2());
    }

    public void testVisualAllReport() {
        setSmsOutReport3(smsOutReport3());
    }

    public void generateXLSX() {
        if (user == null || user.equals("")) {
            JsfUtil.addErrorMessage("User not selected.");
        } else {
            try {
                Connection conn = null;
                JdbcUtil util = new JdbcUtil();
                conn = util.getConnectionTodbSMS();

                LOG.info("generateXLSX");

                SMSOutServiceApi service = new SMSOutServiceImpl();
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
                String startDate = simpleDateFormat.format(reportStartDate);
                String endDate = simpleDateFormat.format(reportEndDate);
                service.generateSMPPXSL(user, startDate, endDate, conn);
                JdbcUtil.closeConnection(conn);
            } catch (SQLException e) {
                JdbcUtil.printSQLException(e);
            }
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
                conn = util.getConnectionTodbSMS();
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
        public void executeReport() {
        System.out.println("Execute Report....generate");
       
            final JdbcUtil util = new JdbcUtil();
            Connection conn = util.getConnectionTodbSMS();
            //int rows = (Integer) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("rowSize");
            LargeFileExport export = new LargeFileExport();
            export.checkSmsList(conn, "SMS OUT REPORT");
      
    }
    boolean summary=false;

    public boolean isSummary() {
        return summary;
        
    }

    public void setSummary(boolean summary) {
        this.summary = summary;
          if(summary)
            {
            setSummaryOrDetail("Detail");
        }
       
        else {setSummaryOrDetail("summary");}
//        int count=0;
//        List<SMSOut> summaryList = null;
//        smsOutReport.forEach((v)->{});
//        for(SMSOut list:smsOutReport){
//           String source= list.getSourceAddr();
//            for(SMSOut l:smsOutReport){
////               
//            }
//        }
    }
    
public  int smppSummarySMS(){
        List<SMPPOut> report = null;
        int summarycount=0;
        try {
            final JdbcUtil util = new JdbcUtil();
            Connection conn = util.getConnectionTodbSMS();
            LOG.info("smsOutReport");
            SMSOutServiceApi service = new SMSOutServiceImpl();
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
            String startDate = simpleDateFormat.format(reportStartDate);
            String endDate = simpleDateFormat.format(reportEndDate);
             //added by horace
                DateFormat outFormat = new SimpleDateFormat("yyyy-MM-dd");
            System.out.println("Start Date" + reportStartDate + "  End Date" + reportEndDate);
            String schsdate = (outFormat.format(reportStartDate));
            String schedate = (outFormat.format(reportEndDate));

            schsdate = schsdate + " 00:00:01";
            schedate = schedate + " 23:59:59";
        
           
               System.out.println("failed");
           
          //end
            Map<String, Object> results = service.smppgetSummarySms(user, startDate, endDate, conn,0);
            
            summarycount = (Integer) results.get("noSMS");
          
            JdbcUtil.closeConnection(conn);
           
        } catch (SQLException e) {
            JdbcUtil.printSQLException(e);
        }
        return summarycount;
    }
}
