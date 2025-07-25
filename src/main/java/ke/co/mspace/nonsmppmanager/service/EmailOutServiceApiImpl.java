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
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import ke.co.mspace.nonsmppmanager.invalids.FacePainter;
import ke.co.mspace.nonsmppmanager.invalids.UserInfo;
import ke.co.mspace.nonsmppmanager.model.EmailOut;
import ke.co.mspace.nonsmppmanager.util.JdbcUtil;
import ke.co.mspace.nonsmppmanager.util.JsfUtil;

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
    private boolean loader = false;

    @Override
    public List<EmailOut> fetchEmailReport(String user, String startDate, String endDate, int first, int pageSize) {

        System.out.println("Inside FetchEmail report");

//        String sqlmain = "SELECT * FROM (SELECT tEMAILOUT.id, tEMAILOUT.mobile, tEMAILOUT.name, tEMAILOUT.emailSrc,\n"
//                + "                tEMAILOUT.emailTo, tEMAILOUT.emailReplyTo, \n"
//                + "                tEMAILOUT.subject, tEMAILOUT.attachment, tEMAILOUT.message_id, tEMAILOUT.`user`,\n"
//                + "                tEMAILOUT.submittedby , tEMAILOUT.timesubmitted, tEMAILOUT.emailBody,\n"
//                + "                tEMAILOUT.status, tEMAILOUT.emailType\n"
//                + "                FROM dbEMAIL.tEMAILOUT\n"
//                + "                WHERE dbEMAIL.tEMAILOUT.user = '" + user + "' AND dbEMAIL.tEMAILOUT.timesubmitted BETWEEN '" + startDate + "' AND '" + endDate + "' \n"
//                + "           UNION ALL\n"
//                + "                \n"
//                + "SELECT tEMAILOUT_COMPLETE.id, tEMAILOUT_COMPLETE.mobile, tEMAILOUT_COMPLETE.name, tEMAILOUT_COMPLETE.emailSrc,\n"
//                + "                tEMAILOUT_COMPLETE.emailTo, tEMAILOUT_COMPLETE.emailReplyTo, \n"
//                + "                tEMAILOUT_COMPLETE.subject, tEMAILOUT_COMPLETE.attachment, tEMAILOUT_COMPLETE.message_id, tEMAILOUT_COMPLETE.`user`,\n"
//                + "                tEMAILOUT_COMPLETE.submittedby , tEMAILOUT_COMPLETE.timesubmitted, tEMAILOUT_COMPLETE.emailBody,\n"
//                + "                tEMAILOUT_COMPLETE.status, tEMAILOUT_COMPLETE.emailType\n"
//                + "                FROM dbEMAIL.tEMAILOUT_COMPLETE\n"
//                + "                WHERE dbEMAIL.tEMAILOUT_COMPLETE.user = '" + user + "' AND dbEMAIL.tEMAILOUT_COMPLETE.timesubmitted BETWEEN '" + startDate + "' AND '" + endDate + "' \n"
//                + "                ) AS combined_results \n"
//                + "                ORDER BY timesubmitted DESC LIMIT ? OFFSET ?";

                   String sqlmain = "SELECT tEMAILOUT.id, tEMAILOUT.mobile, tEMAILOUT.name, tEMAILOUT.emailSrc,\n"
                + "                tEMAILOUT.emailTo, tEMAILOUT.emailReplyTo, \n"
                + "                tEMAILOUT.subject, tEMAILOUT.attachment, tEMAILOUT.message_id, tEMAILOUT.`user`,\n"
                + "                tEMAILOUT.submittedby , tEMAILOUT.timesubmitted, tEMAILOUT.emailBody,\n"
                + "                tEMAILOUT.status, tEMAILOUT.emailType\n"
                + "                FROM dbEMAIL.tEMAILOUT\n"
                + "                WHERE dbEMAIL.tEMAILOUT.user = '" + user + "' AND dbEMAIL.tEMAILOUT.timesubmitted BETWEEN '" + startDate + "' AND '" + endDate + "' \n"
                + "                ORDER BY timesubmitted DESC LIMIT ? OFFSET ?";

        List<EmailOut> results = new ArrayList<>();

        try (Connection conn = jdbcUtil
                .getConnectionTodbEMAIL(); PreparedStatement pstmt = conn.prepareStatement(sqlmain)) {
            System.out.println("Established the connection and successfully prepared statement");
            pstmt.setInt(1, pageSize);
            pstmt.setInt(2, first);
//            pstmt.setInt(3, pageSize);
//            pstmt.setInt(4, first);

            ResultSet rs = pstmt.executeQuery();

            System.out.println("Connection established! ");
            int count = 0;

//            if (rs.next()) {
//                System.out.println("Data Present!");
//            } else {
//                System.out.println("No Data found!");
//            }

            while (rs.next()) {
//                System.out.println("data " + count++);
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

            int rows = fetchRows(user, startDate, endDate);
            if (rows > 0) {
                System.out.println("rows has data");
                FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_INFO, "Success", "Successfully retrieved " + rows + "  records "));
                
                JsfUtil.addSuccessMessage("Successfully retrieved " + rows + "  records ");

            } else {
                System.out.println("Rows has no data");
                FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_INFO, "", "No data found!"));
                
                JsfUtil.addSuccessMessage("No records found!");

            }

            System.out.println("Finished data retrieval");

        } catch (Exception e) {
            LOG.log(Level.SEVERE, "Error retrieving emailout report ", e);
        }

        return results;
    }

    public int fetchRows(String user, String startDate, String endDate) {

        int rows = 0;

//           String sqlcountmain = "SELECT COUNT(*) FROM dbEMAIL.tEMAILOUT WHERE dbEMAIL.tEMAILOUT.user='" + user + "'\n"
//                + "AND dbEMAIL.tEMAILOUT.timesubmitted BETWEEN '" + startDate + "'\n"
//                + "AND '" + endDate + "'";

        String sqlcountmain = "SELECT SUM(count)\n"
                + "FROM (\n"
                + "    SELECT COUNT(*) AS count\n"
                + "    FROM dbEMAIL.tEMAILOUT \n"
                + "    WHERE dbEMAIL.tEMAILOUT.user = '"+ user + "'\n"
                + "      AND dbEMAIL.tEMAILOUT.timesubmitted BETWEEN '" + startDate + "' AND '" + endDate + "'\n"
                + "\n"
                + "    UNION ALL\n"
                + "\n"
                + "    SELECT COUNT(*) AS count\n"
                + "    FROM dbEMAIL.tEMAILOUT_COMPLETE \n"
                + "    WHERE dbEMAIL.tEMAILOUT_COMPLETE.user = '" + user + "'\n"
                + "      AND dbEMAIL.tEMAILOUT_COMPLETE.timesubmitted BETWEEN '" + startDate + "' AND '" + endDate + "'\n"
                + ") AS combined_counts";

        try (Connection conn = jdbcUtil.getConnectionTodbEMAIL(); PreparedStatement pstmt = conn.prepareStatement(sqlcountmain)) {
            try (ResultSet rs = pstmt.executeQuery()) {

                while (rs.next()) {
                    rows += rs.getInt(1);
                }
               
                System.out.println("all rows: " + rows);
                FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_INFO, "Success", "Successfully retrieved " + rows + "  records "));
                
            } catch (Exception ex) {
                System.out.println("An exception has occured " + ex);
            }
        } catch (Exception ex) {
            System.out.println("An exception has occured! " + ex);
        }


        return rows;
    }

}
