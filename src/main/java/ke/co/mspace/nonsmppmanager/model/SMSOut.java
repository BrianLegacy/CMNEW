/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ke.co.mspace.nonsmppmanager.model;

import ke.co.mspace.nonsmppmanager.invalids.FacePainter;
import java.io.IOException;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import ke.co.mspace.export.LargeFileExport;
import ke.co.mspace.nonsmppmanager.service.SMSOutServiceApi;
import ke.co.mspace.nonsmppmanager.service.SMSOutServiceImpl;
import ke.co.mspace.nonsmppmanager.service.UserScroller;
import ke.co.mspace.nonsmppmanager.service.UserServiceImpl;
import ke.co.mspace.nonsmppmanager.util.JdbcUtil;
import ke.co.mspace.nonsmppmanager.util.JsfUtil;
import ke.co.mspace.nonsmppmanager.util.SessionUtil;
import org.primefaces.context.RequestContext;

/**
 *
 * @author Norrey Osako
 */
@ManagedBean
@ViewScoped
//@SessionScoped
public class SMSOut implements Serializable {
 public static final String ANSI_BLUE = "\u001B[34m";
 public static final String ANSI_RESET = "\u001B[0m";

    private Long id;
    private Long rid;
    private String seqNo;
    private String serviceType;
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
    private int esmClass;
        
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
    private List<SMSOut> smsOutReport;
    private String smsOutReport2;
    private String smsOutReport3;
    private int reportSize;
    private String summaryOrDetail="Summary";
    private boolean renderModal = false;
    private static final Logger LOG = Logger.getLogger(SMSOut.class.getName());
    private int limit=500;
    

    private String user_id = FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("id").toString();

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }
    private final int adnminval =Character.getNumericValue(SessionUtil.getAdmin());

    public List<SMSOut> getSmsOutReport() {
        return smsOutReport;
    }

    public void setSmsOutReport(List<SMSOut> smsOutReport) {
        this.smsOutReport = smsOutReport;
    }

    public SMSOut() {

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

    public Long getRid() {
        return rid;
    }

    public void setRid(Long rid) {
        this.rid = rid;
    }

    public String getSummaryOrDetail() {
        return summaryOrDetail;
    }

    public void setSummaryOrDetail(String summaryOrDetail) {
        this.summaryOrDetail = summaryOrDetail;
    }
    

    public String getSeqNo() {
        return seqNo;
    }

    public void setSeqNo(String seqNo) {
        this.seqNo = seqNo;
    }

    public String getServiceType() {
        return serviceType;
    }

    public void setServiceType(String serviceType) {
        this.serviceType = serviceType;
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

    public int getEsmClass() {
        return esmClass;
    }

    public void setEsmClass(int esmClass) {
        this.esmClass = esmClass;
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

    public int getReportSize() {
        return reportSize;
    }

    public void setReportSize(int reportSize) {
        this.reportSize = reportSize;
    }

    public boolean isRenderModal() {
        return renderModal;
    }

    public void setRenderModal(boolean renderModal) {
       
        this.renderModal = renderModal;
    }

    public int getTotaSMS() {
//        System.out.println("The total sms sent =  " + totaSMS);
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
            Logger.getLogger(SMSOut.class.getName()).log(Level.SEVERE, null, ex);
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

        if (timeProcessed.length() <= 0) {
            return "";
        } else {
            try {
                d = displayFormat.parse(timeProcessed);
            } catch (ParseException ex) {
                Logger.getLogger(SMSOut.class.getName()).log(Level.SEVERE, null, ex);

            }
        }

        displayFormat.applyPattern("yyyy-MM-dd HH:mm:ss");
        displayTimeProcessed = displayFormat.format(d);

        return displayTimeProcessed;
    }

    public void setDisplayTimeProcessed(String displayTimeProcessed) {
        this.displayTimeProcessed = displayTimeProcessed;
    }
    boolean summary=false;

    public boolean isSummary() {
        return summary;
        
    }
private int summarysmscount;

//    public int getSummarysmscount() {
//        List<SMSOut> list = this.summarySMS();
//        list.g
//        return (this.summarySMS().get("noSMS"));
//    }

    public void setSummarysmscount(int summarysmscount) {
        this.summarysmscount = summarysmscount;
    }
    
    public void setSummary(boolean summary) {
//        System.out.println("automatic"+summary);
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
    private int numOfSMS;

    public int getNumOfSMS() {
        return numOfSMS;
    }

    public void setNumOfSMS(int numOfSMS) {
        this.numOfSMS = numOfSMS;
    }
    public List<SMSOut> smsOutReport() {
        List<SMSOut> report = null;
        try {
//            System.out.println("processing began");
            final JdbcUtil util = new JdbcUtil();
            Connection conn = util.getConnectionTodbSMS();
            SMSOutServiceApi service = new SMSOutServiceImpl();
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
            String startDate = simpleDateFormat.format(reportStartDate);
            DateFormat outFormat = new SimpleDateFormat("yyyy-MM-dd");
//            System.out.println("Start Date" + reportStartDate + "  End Date" + reportEndDate);
            String schsdate = (outFormat.format(reportStartDate));
            String schedate = (outFormat.format(reportEndDate));

            schsdate = schsdate + " 00:00:01";
            schedate = schedate + " 23:59:59";
            String endDate = simpleDateFormat.format(reportEndDate);
         //   System.out.println("Start Date :" + startDate + " End Date : " + endDate);
          //added by horace
            System.out.println("calling checker");
          int countOfSMS=service.checkIfFileIsLarge(user, startDate, endDate,schsdate,schedate, conn);
            System.out.println("Returned count "+countOfSMS);
            setNumOfSMS(countOfSMS);
//            System.out.println("The size of the large report is"+countOfSMS);
           if(numOfSMS>limit){
//                           System.out.println(ANSI_BLUE+"CHECKER RETURNED"+(new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime())).toString()+ANSI_BLUE);

              renderModal = true;
               setRenderModal(true);
               
               RequestContext context = RequestContext.getCurrentInstance();
context.execute("PF('modal').show();");
//                                          System.out.println(ANSI_BLUE+"SET RENDER MODAL TRUE"+(new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime())).toString()+ANSI_BLUE);

              service.smsSetSql(user, startDate, endDate, schedate, schedate, conn);
//              //horacek
//                                         System.out.println(ANSI_BLUE+"REPORT GENERATOR RUNNING"+(new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime())).toString()+ANSI_BLUE);
//
//                final Map<String, Object> results = service.userSMSOutReport(user, startDate, endDate,schsdate,schedate, conn,limit);
//                
//            report = (List) results.get("result");
//            reportSize = report.size();
//            System.out.println("Report size" + reportSize);
//         
//                                         System.out.println(ANSI_BLUE+"REPORT GENERATOR DONE"+(new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime())).toString()+ANSI_BLUE);
//
////            if (reportSize > 2) {
////                
////                List<SMSOut> smallerlist = new ArrayList();
////                smallerlist.addAll(report.subList(0, 2));
////                report = smallerlist;
////                setTotaSMS(report.size());
////            }else{
////                 int noSMS = (Integer) results.get("noSMS");
////            setTotaSMS(noSMS);
////            }
//            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("smsoutReport", report);
////            System.out.println("Report Size is " + report.size());
//            //smsOutReport=(List) results.get("result");
//            String realSMSStatus = service.getRealSMSStatus(String.valueOf(status), conn);
//           int noSMS = (Integer) results.get("noSMS");
//            setTotaSMS(noSMS);
//            setRealStatus(realSMSStatus);
//           //horace
//               JsfUtil.addSuccessMessage("Can only display/export reports less than 15000 rows. To get full report use the bulk functionality");
//           //
//            JdbcUtil.closeConnection(conn);
//              //horace
          }
           else{
          //end  
            final Map<String, Object> results = service.userSMSOutReport(user, startDate, endDate,schsdate,schedate, conn,0);
            report = (List) results.get("result");
            reportSize = report.size();
            System.out.println("Report size" + reportSize);
         

//            if (reportSize > 2) {
//                
//                List<SMSOut> smallerlist = new ArrayList();
//                smallerlist.addAll(report.subList(0, 2));
//                report = smallerlist;
//                setTotaSMS(report.size());
//            }else{
//                 int noSMS = (Integer) results.get("noSMS");
//            setTotaSMS(noSMS);
//            }
            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("smsoutReport", report);
//            System.out.println("Report Size is " + report.size());
            //smsOutReport=(List) results.get("result");
            String realSMSStatus = service.getRealSMSStatus(String.valueOf(status), conn);
           int noSMS = (Integer) results.get("noSMS");
            setTotaSMS(noSMS);
            setRealStatus(realSMSStatus);
           
            JdbcUtil.closeConnection(conn);
           }//end of else
        } catch (SQLException e) {
            JdbcUtil.printSQLException(e);
        }
        return report;
    }
    
    public  int summarySMS(){
    System.out.println("summary called");
         List<SMSOut> report = null;
         int summarycount=0;
        try {
            final JdbcUtil util = new JdbcUtil();
            Connection conn = util.getConnectionTodbSMS();
            LOG.info("smsOutReport");
            SMSOutServiceApi service = new SMSOutServiceImpl();
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
            String startDate = simpleDateFormat.format(reportStartDate);
            DateFormat outFormat = new SimpleDateFormat("yyyy-MM-dd");
//            System.out.println("Start Date" + reportStartDate + "  End Date" + reportEndDate);
            String schsdate = (outFormat.format(reportStartDate));
            String schedate = (outFormat.format(reportEndDate));

            schsdate = schsdate + " 00:00:01";
            schedate = schedate + " 23:59:59";
            String endDate = simpleDateFormat.format(reportEndDate);
//            System.out.println("Start Date :" + startDate + " End Date : " + endDate);
         
         
            final Map<String, Object> results = service.getSummarySms(user, startDate, endDate,schsdate,schedate, conn,0);
//            report = (List) results.get("result");
//            reportSize = report.size();
//            System.out.println("Report size" + reportSize);
//         
//            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("smsoutReport", report);
//            System.out.println("Report Size is " + report.size());
//            //smsOutReport=(List) results.get("result");
//            String realSMSStatus = service.getRealSMSStatus(String.valueOf(status), conn);
summarycount=(Integer) results.get("noSMS");
//           int noSMS = (Integer) results.get("noSMS");
//            setTotaSMS(noSMS);
//            setRealStatus(realSMSStatus);
           
            JdbcUtil.closeConnection(conn);
           //end of else
        } catch (SQLException e) {
            JdbcUtil.printSQLException(e);
        }
        return summarycount;

    }
public void closeTheModal(){
      
    setRenderModal(false);
  
}








































  @ManagedProperty(value = "#{facePainter}")
    public FacePainter facePainter;

    public FacePainter getFacePainter() {
        return facePainter;
    }

    public void setFacePainter(FacePainter facePainter) {
        this.facePainter = facePainter;
    }
    public void executeReport() {
        System.out.println("a hit ");
        setRenderModal(false);
//        System.out.println("Execute Report....generate");
        
            final JdbcUtil util = new JdbcUtil();
            Connection conn = util.getConnectionTodbSMS();
            //int rows = (Integer) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("rowSize");
            LargeFileExport export = new LargeFileExport();
            export.checkSmsList(conn, "SMS OUT REPORT");
            
     
         facePainter.setMainContent("clientmanager/reports/bulkreports.xhtml");
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

    public void testTabularReport() {
//        System.out.println(ANSI_BLUE +"At origin "+(new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime())).toString()+ ANSI_RESET);
       // boolean checkFileSizeResult=checkIfFileIsLarge();
        setSmsOutReport(smsOutReport());
//        System.out.println(ANSI_BLUE +"total end "+(new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime())).toString()+ ANSI_RESET);
    }

    public void testVisualReport() {
        setSmsOutReport2(smsOutReport2());
    }

    public void testVisualAllReport() {
        setSmsOutReport3(smsOutReport3());
    }
    public void doNothing(){
        
    }

    public void generateXLSX() {
        try {
            Connection conn = null;
            JdbcUtil util = new JdbcUtil();
            conn = util.getConnectionTodbSMS();

            LOG.info("generateXLSX");

            SMSOutServiceApi service = new SMSOutServiceImpl();

            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
            String startDate = simpleDateFormat.format(reportStartDate);
            String endDate = simpleDateFormat.format(reportEndDate);

            service.generateXSL(user, startDate, endDate, conn);
            JdbcUtil.closeConnection(conn);
        } catch (SQLException e) {
            JdbcUtil.printSQLException(e);
        }

    }

    public ResultSet getResultSet() {
        ResultSet rs = null;
       
            Connection conn = null;
            JdbcUtil util = new JdbcUtil();
            conn = util.getConnectionTodbSMS();

            LOG.info("generateXLSX");

            SMSOutServiceApi service = new SMSOutServiceImpl();

            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
            String startDate = simpleDateFormat.format(reportStartDate);
            String endDate = simpleDateFormat.format(reportEndDate);
//            System.out.println("Start Date get result set : " + startDate + " End Date: " + endDate);

            rs = service.getResultSet(user, startDate, endDate, conn);
            //JdbcUtil.closeConnection(conn);
    
        return rs;
    }

    List<SelectItem> dataTT;

    public List<SelectItem> getData() {
        dataTT = new ArrayList<>();
        Connection con = null;
        ResultSet rs = null;
        Statement stmt = null;
        try {
            final JdbcUtil util = new JdbcUtil();
            con = util.getConnectionTodbSMS();
            stmt = con.createStatement();
            String fetch = "SELECT username from dbSMS.tUSER where admin !='5' and agent !='email' order by username asc";
            //String fetch = "SELECT username from dbSMS.tUSER where admin =2 order by username asc";
             System.out.println("users sql for admin"+fetch);
            String fetchForReseller = "SELECT username from dbSMS.tUSER where agent = '" + user_id + "' and admin ='3'";
            System.out.println("users sql for reselle"+fetchForReseller);
            //rs = UserServiceImpl.isReseller().equalsIgnoreCase("none") ? stmt.executeQuery(fetchForReseller) : stmt.executeQuery(fetch);
            rs = adnminval == 5 ? stmt.executeQuery(fetchForReseller) : stmt.executeQuery(fetch);
           // System.out.println("DATA" + fetchForReseller);
            while (rs.next()) {
                dataTT.add(new SelectItem(rs.getString(1)));
            }
        } catch (SQLException err) {
            err.getMessage();  }
        return dataTT;    }
public List<SelectItem> getEmailUsers() {
        dataTT = new ArrayList<>();
        Connection con = null;
        ResultSet rs = null;
        Statement stmt = null;
        try {
            final JdbcUtil util = new JdbcUtil();    con = util.getConnectionTodbSMS();  stmt = con.createStatement();
            String fetch = "SELECT username from dbSMS.tUSER where admin !='5' and agent ='email' order by username asc";
            //String fetch = "SELECT username from dbSMS.tUSER where admin =2 order by username asc";
            System.out.println("fetch email sql "+fetch);
//            String fetchForReseller = "SELECT username from dbSMS.tUSER where agent = '" + user_id + "' and admin ='3' and agent ='email' ";
            String fetchForReseller = "SELECT username from dbSMS.tUSER where  admin ='3' and agent ='email' ";

             System.out.println("fetch email sql "+fetchForReseller);
            //rs = UserServiceImpl.isReseller().equalsIgnoreCase("none") ? stmt.executeQuery(fetchForReseller) : stmt.executeQuery(fetch);
            rs = adnminval == 5 ? stmt.executeQuery(fetchForReseller) : stmt.executeQuery(fetch);
            System.out.println("Admin value" + adnminval);
            System.out.println("user id "+user_id);
            while (rs.next()) {
                dataTT.add(new SelectItem(rs.getString(1)));
            }
        } catch (SQLException err) {
            err.getMessage();
        }
        return dataTT;
    }
    
    List<SelectItem> dataCC;

    public List<SelectItem> getDataC() {
        dataCC = new ArrayList<SelectItem>();
        Connection con = null;
        ResultSet rs = null;
        Statement stmt = null;
        try {
            final JdbcUtil util = new JdbcUtil();
            con = util.getConnectionTodbSMS();
            stmt = con.createStatement();
            String sql = "SELECT distinct username from dbSMS.tManageCredits where username  is not null  group by username";
            String sqlReseller = "select distinct t.username from tManageCredits t inner join tUSER u on "
                    + "t.username = u.username where u.agent = '" + user_id + "'";
            //rs = stmt.executeQuery(UserServiceImpl.isReseller().equalsIgnoreCase("none") ? sqlReseller : sql);
//            System.out.println("ADMIN VALUE" + adnminval);
            rs = stmt.executeQuery(adnminval == 5 ? sqlReseller : sql);
//            System.out.println("DATA::" + sqlReseller + "  " + stmt);
            while (rs.next()) {
                dataCC.add(new SelectItem(rs.getString(1)));
            }
        } catch (SQLException err) {
            err.printStackTrace();
            err.getMessage();
        }
        return dataCC;
    }

    public List<String> getUsernames() {
        boolean containsNames = FacesContext.getCurrentInstance().getExternalContext().getSessionMap().containsKey("reportusernames");

        if (containsNames) {
            return (List<String>) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("reportusernames");
        } else {
            List<String> output = null;

            try {
                Connection conn = null;
                final JdbcUtil util = new JdbcUtil();
                conn = util.getConnectionTodbSMS();
                LOG.info("getUsernames");
                SMSOutServiceApi service = new SMSOutServiceImpl();
                output = service.getUsernames(conn);
                JdbcUtil.closeConnection(conn);
            } catch (SQLException e) {
                JdbcUtil.printSQLException(e);
            }
            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("reportusernames", output);
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

    //========================================================================================================
    public List<SelectItem> getDataR() throws Exception {
        dataTT = new ArrayList<>();

        UserScroller us = new UserScroller();
        Connection con = null;
        ResultSet rs = null;
        Statement stmt = null;
        try {
            final JdbcUtil util = new JdbcUtil();
            con = util.getConnectionTodbSMS();
            stmt = con.createStatement();
            String fetch = "SELECT username from dbSMS.tUSER where admin='5'";
            String fetchForReseller = "SELECT username from dbSMS.tUSER where agent = '" + user_id + "' and admin !=3 ";

            //rs = UserServiceImpl.isReseller().equalsIgnoreCase("none") ? stmt.executeQuery(fetchForReseller) : stmt.executeQuery(fetch);
            rs = adnminval == 5 ? stmt.executeQuery(fetchForReseller) : stmt.executeQuery(fetch);
            while (rs.next()) {
                dataTT.add(new SelectItem(rs.getString(1)));
            }
        } catch (SQLException err) {
            err.getMessage();
        }
        return dataTT;
    }



    

}
