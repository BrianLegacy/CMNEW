/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ke.co.mspace.nonsmppmanager.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import ke.co.mspace.nonsmppmanager.invalids.FacePainter;
import ke.co.mspace.nonsmppmanager.invalids.UserInfo;
import ke.co.mspace.nonsmppmanager.model.EmailOut;
import ke.co.mspace.nonsmppmanager.util.JdbcUtil;

/**
 *
 * @author amos
 */
public class EmailOutServiceApiImpl implements EmailOutServiceApi {

    private static final Logger LOGGER = Logger.getLogger("EmailOutServiceImpl.class");
    private final JdbcUtil jdbcUtil = new JdbcUtil();
    private static final Logger LOG = Logger.getLogger(SMSOutServiceImpl.class.getName());
    private FacePainter facePainter = new FacePainter();
    private UserInfo userInfo = new UserInfo();


    @Override
    public List<EmailOut> fetchEmailReport(String user, String startDate, String endDate) {
        
        System.out.println("Inside FetchEmail report");

        String sqlmain = "SELECT tEMAILOUT.id, tEMAILOUT.mobile, tEMAILOUT.name, tEMAILOUT.emailSrc,\n"
                + "                tEMAILOUT.emailTo, tEMAILOUT.emailReplyTo, \n"
                + "                tEMAILOUT.subject, tEMAILOUT.attachment, tEMAILOUT.message_id, tEMAILOUT.`user`,\n"
                + "                tEMAILOUT.submittedby , tEMAILOUT.timesubmitted, tEMAILOUT.emailBody,\n"
                + "                tEMAILOUT.status, tEMAILOUT.emailType\n"
                + "                FROM dbEMAIL.tEMAILOUT\n"
                + "                WHERE dbEMAIL.tEMAILOUT.user = '" + user + "' AND dbEMAIL.tEMAILOUT.timesubmitted BETWEEN '" + startDate + "' AND '" + endDate + "' \n"
                + "                \n"
                + "           UNION\n"
                + "                \n"
                + "SELECT tEMAILOUT_COMPLETE.id, tEMAILOUT_COMPLETE.mobile, tEMAILOUT_COMPLETE.name, tEMAILOUT_COMPLETE.emailSrc,\n"
                + "                tEMAILOUT_COMPLETE.emailTo, tEMAILOUT_COMPLETE.emailReplyTo, \n"
                + "                tEMAILOUT_COMPLETE.subject, tEMAILOUT_COMPLETE.attachment, tEMAILOUT_COMPLETE.message_id, tEMAILOUT_COMPLETE.`user`,\n"
                + "                tEMAILOUT_COMPLETE.submittedby , tEMAILOUT_COMPLETE.timesubmitted, tEMAILOUT_COMPLETE.emailBody,\n"
                + "                tEMAILOUT_COMPLETE.status, tEMAILOUT_COMPLETE.emailType\n"
                + "                FROM dbEMAIL.tEMAILOUT_COMPLETE\n"
                + "                WHERE dbEMAIL.tEMAILOUT_COMPLETE.user = '" + user + "' AND dbEMAIL.tEMAILOUT_COMPLETE.timesubmitted BETWEEN '" + startDate + "' AND '" + endDate + "' \n"
                + "                ORDER BY id DESC";

        List<EmailOut> results = new ArrayList<>();

        try (Connection conn = jdbcUtil
                .getConnectionTodbEMAIL(); PreparedStatement pstmt = conn.prepareStatement(sqlmain)) {
                    System.out.println("Established the connection and successfully prepared statement");
         
                    ResultSet rs = pstmt.executeQuery();

                System.out.println("Connection established! ");
                
                while (rs.next()) {
                                        
                    EmailOut emailOut = new EmailOut();
                    emailOut.setUser(rs.getString("user"));
                    emailOut.setEmailBody(rs.getString("emailBody"));
                    emailOut.setEmailSrc(rs.getString("emailSrc"));
                    emailOut.setEmailReplyTo(rs.getString("emailReplyTo"));
                    emailOut.setEmailTo(rs.getString("emailTo"));
                    emailOut.setStatus(
                            rs.getInt("status") == 0 ? "To Be Sent"
                            : rs.getInt("status") == 1 ? "Sending"
                            : rs.getInt("status") == 2 ? "Successfully Sent"
                            : rs.getInt("status") == 3 ? "Opened"
                            : rs.getInt("status") == 4 ? "Network"
                            : rs.getInt("status") == 5 ? "Bounced"
                            : rs.getInt("status") == 6 ? "Expired"
                            : rs.getInt("status") == 7 ? "Submit failed"
                            : rs.getInt("status") == 8 ? "Error Building Pdf"
                            : rs.getInt("status") == 11 ? "Scheduled"
                            : rs.getInt("status") == 12 ? "Invalid E-Statement Policy No."
                            : rs.getInt("status") == 13 ? "E-Statement not found"
                            : rs.getInt("status") == 14 ? "E-Statement not found or Error Building Pdf"
                            : rs.getInt("status") == 9 ? "Cancelled"
                            : rs.getInt("status") == -3 ? "Retrying Email Verification"
                            : rs.getInt("status") == 15 ? "Invalid Email Address"
                            : rs.getInt("status") == -1 ? "Verifying Email Address"
                            : rs.getInt("status") == -2 ? "Email Address To Be Verified"
                            : rs.getInt("status") == -2 ? "Un Subscribed"
                            : "Unknown");
                    emailOut.setAttachment(rs.getString("attachment"));
                    emailOut.setMessageId(rs.getString("message_id"));
                    emailOut.setMobile(rs.getString("mobile"));
                    emailOut.setSubject(rs.getString("subject"));
                    emailOut.setTimeSubmitted(rs.getDate("timesubmitted"));

                    results.add(emailOut);
                }
                
                System.out.println("Finished data retrieval");
            

        } catch (Exception e) {
            LOG.log(Level.SEVERE, "Error retrieving emailout report ", e);
        }

        return results;
    }

    public int fetchRows(String user, String startDate, String endDate) {

        int rows = 0;

        String sqlcountmain = "SELECT COUNT(*) FROM dbEMAIL.tEMAILOUT_COMPLETE WHERE dbEMAIL.tEMAILOUT_COMPLETE.user='" + user + "'\n"
                + "AND dbEMAIL.tEMAILOUT_COMPLETE.timesubmitted BETWEEN '" + startDate + "'\n"
                + "AND '" + endDate + "'\n"
                + "\n"
                + "UNION \n"
                + "\n"
                + "SELECT COUNT(*) FROM dbEMAIL.tEMAILOUT WHERE dbEMAIL.tEMAILOUT.user='" + user + "'\n"
                + "AND dbEMAIL.tEMAILOUT.timesubmitted BETWEEN '" + startDate + "'\n"
                + "AND '" + endDate + "'";

        try (Connection conn = jdbcUtil.getConnectionTodbEMAIL(); PreparedStatement pstmt = conn.prepareStatement(sqlcountmain)) {
            try (ResultSet rs = pstmt.executeQuery()) {

                while (rs.next()) {
                    rows += rs.getInt(1);
                }

            } catch (Exception ex) {
                System.out.println("An exception has occured " + ex);
            }
        } catch (Exception ex) {
            System.out.println("An exception has occured! " + ex);
        }

        return rows;
    }

//    
//    @Override
//    public String insertIntotLARGEREPORTEXPORT(String user, String startDate, String endDate) {
//        
//        userInfo.bulkReports();
//        
//        fetchBulkReports(user);
//
//        LocalDateTime currentDate = LocalDateTime.now();
//        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
//
//        String todayDate = dtf.format(currentDate);
//        String page = "emailoutreports";
//
//        String sqlStatement = String.format(
//                "SELECT tEMAILOUT.mobile, tEMAILOUT.name, tEMAILOUT.emailSrc, "
//                + "tEMAILOUT.emailTo, tEMAILOUT.emailReplyTo, "
//                + "tEMAILOUT.subject, tEMAILOUT.attachment, tEMAILOUT.message_id, tEMAILOUT.`user`, "
//                + "tEMAILOUT.submittedby, tEMAILOUT.timesubmitted, tEMAILOUT.emailBody, "
//                + "tEMAILOUT.status, tEMAILOUT.emailType "
//                + "FROM dbEMAIL.tEMAILOUT "
//                + "WHERE dbEMAIL.tEMAILOUT.user = '%s' AND dbEMAIL.tEMAILOUT.timesubmitted BETWEEN '%s' AND '%s' "
//                + "UNION "
//                + "SELECT tEMAILOUT_COMPLETE.mobile, tEMAILOUT_COMPLETE.name, tEMAILOUT_COMPLETE.emailSrc, "
//                + "tEMAILOUT_COMPLETE.emailTo, tEMAILOUT_COMPLETE.emailReplyTo, "
//                + "tEMAILOUT_COMPLETE.subject, tEMAILOUT_COMPLETE.attachment, tEMAILOUT_COMPLETE.message_id, tEMAILOUT_COMPLETE.`user`, "
//                + "tEMAILOUT_COMPLETE.submittedby, tEMAILOUT_COMPLETE.timesubmitted, tEMAILOUT_COMPLETE.emailBody, "
//                + "tEMAILOUT_COMPLETE.status, tEMAILOUT_COMPLETE.emailType "
//                + "FROM dbEMAIL.tEMAILOUT_COMPLETE "
//                + "WHERE dbEMAIL.tEMAILOUT_COMPLETE.user = '%s' AND dbEMAIL.tEMAILOUT_COMPLETE.timesubmitted BETWEEN '%s' AND '%s'",
//                user, startDate, endDate, user, startDate, endDate
//        );
//
////        get UserId
//        int userId = fetchUserId(user);
//
//        System.out.println("userId: " + userId);
//
////     create and store file
//
//        File fileDir = new File(System.getProperty("user.home") + "/Files/export/");
//        System.out.println("file " + fileDir);
//        boolean existsFileLocation = fileDir.exists();
//        System.out.println("file " + existsFileLocation);
//
//        String reportName = "Sent Email Reports";
//
//        if (!fileDir.exists()) {
//            fileDir.mkdirs();
//            File tempFile = new File(fileDir.getAbsolutePath() + "/" + reportName + ".xlsx");
//            try {
//                tempFile.createNewFile();
//            } catch (IOException ex) {
//                Logger.getLogger(EmailOutController.class.getName()).log(Level.SEVERE, null, ex);
//            }
//            System.out.println("temporaryFile " + tempFile);
//        }
//
//        String sql = "INSERT INTO dbSMS.tLARGEREPORTEXPORT (user_id, sql_querry, date_inserted, excel_file_location, report_name, file_size, status, `system`)\n"
//                + "VALUES(?, ?, ?, ?, 'Sent Email Reports', 0, 0, 'client manager');";
//
//        try (Connection conn = jdbcUtil.getConnectionTodbSMS(); PreparedStatement pstmt = conn.prepareStatement(sql);) {
//            pstmt.setInt(1, userId);
//            pstmt.setString(2, sqlStatement);
//            pstmt.setString(3, todayDate);
//            pstmt.setString(4, fileDir.getAbsolutePath() + "/" + reportName + ".xlsx");
//
//            try {
//                pstmt.execute();
//               
//                
//                System.out.println("inserted querry to db ");
//
//                FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Success", "Successfully executed");
//                FacesContext.getCurrentInstance().addMessage(null, message);
// 
//                facePainter.setMainContent("clientmanager/reports/bulkreports.xhtml");
//                userInfo.bulkReports();
//
//            } catch (SQLException ex) {
//                System.out.println("An sqlException has occured -> " + ex);
//
//                FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Something went wrong");
//                FacesContext.getCurrentInstance().addMessage(null, message);
//            }
//
//        } catch (Exception e) {
//            System.out.println("An sql exception has ocurred: -- " + e);
//
//            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Something went wrong");
//            FacesContext.getCurrentInstance().addMessage(null, message);
//        }
//        return null;
//    }
//
//    public int fetchUserId(String user) {
//        String userId_sql = "SELECT id FROM dbSMS.tUSER WHERE username ='" + user + "';";
//
//        try (Connection conn = jdbcUtil.getConnectionTodbSMS(); PreparedStatement pstmt = conn.prepareStatement(userId_sql)) {
//            ResultSet rs = pstmt.executeQuery();
//            if (rs.next()) {
//                return rs.getInt("id");
//            }
//        } catch (Exception e) {
//            System.out.println("An sqlexception has occured " + e);
//        }
//        return 0;
//    }
//
//    @Override
//    public List<LargeReport> fetchBulkReports(String username) {
//        
//        List<LargeReport> allReports = new ArrayList<>();
//        
//        int id = fetchUserId(username);
//        
//        String sql = "SELECT tLARGEREPORTEXPORT.user_id, tLARGEREPORTEXPORT.report_name, tLARGEREPORTEXPORT.file_size, \n" +
//"                tLARGEREPORTEXPORT.date_inserted, tLARGEREPORTEXPORT.status FROM dbSMS.tLARGEREPORTEXPORT\n" +
//"                WHERE user_id = ? \n" +
//"                ORDER BY tLARGEREPORTEXPORT.date_finished;";
//        
//        try (Connection conn = jdbcUtil.getConnectionTodbEMAIL(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
//           
//            pstmt.setInt(1, id);
//            
//            try (ResultSet rs = pstmt.executeQuery()) {
//                while(rs.next()){
//                    LargeReport report = new LargeReport();
//                    report.setUserId(rs.getLong("user_id"));
//                    report.setReportDescription(rs.getString("report_name"));
//                    report.setDateCreated(rs.getString("date_inserted"));
//                    report.setStatus(rs.getInt("status") == 0 ? "Inactive" : "Active");
//                    report.setFileSize(rs.getInt("file_size"));
//                    report.setUsername(username);
//                    
//                    allReports.add(report);
//                }
//                
//            }catch(Exception ex){
//                System.out.println("An exception has occured " + ex);
//            }
//    }catch(SQLException ex){
//            System.out.println("An sql exception has occured " + ex);
//    }
//    
//    return allReports;
//}

}