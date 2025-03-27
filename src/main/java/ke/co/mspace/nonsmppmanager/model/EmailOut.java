/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ke.co.mspace.nonsmppmanager.model;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import ke.co.mspace.nonsmppmanager.util.JdbcUtil;
import ke.co.mspace.nonsmppmanager.util.SessionUtil;


/**
 *
 * @author amos
 */
@ManagedBean(name = "emailout")
@ViewScoped
public class EmailOut implements Serializable{
    
    public static final String ANSI_BLUE = "\u001B[34m";
    public static final String ANSI_RESET = "\u001B[0m";

    private Long id;
    private String user;
    private String mobile;
    private String emailSrc;
    private String emailTo;
    private String emailBody;
    private String subject;
    private String messageId;
    private String submittedBy;
    private String status;
    private String emailReplyTo;
    private Date timeSubmitted;
    private String attachment;
    
        private final int adnminval = Character.getNumericValue(SessionUtil.getAdmin());
        List<SelectItem> dataTT;
        private String user_id = FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("id").toString();

    
        public List<SelectItem> getData() {
        dataTT = new ArrayList<>();
        String fetch = "SELECT username FROM dbSMS.tUSER WHERE  emailuser = 'Y' ORDER BY username";
        String fetchForReseller = "SELECT username FROM dbSMS.tUSER WHERE agent = ? | emailuser='Y' ORDER BY username";

        try (
                Connection con = new JdbcUtil().getConnectionTodbSMS(); PreparedStatement stmt = adnminval == 5 ? con.prepareStatement(fetchForReseller) : con.prepareStatement(fetch)) {
            if (adnminval == 5) {
                stmt.setString(1, user_id);
            } 

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    String originalUsername = rs.getString(1);
                    dataTT.add(new SelectItem(originalUsername));
                }
            }
       }catch (SQLException err) {
            System.err.println("SQL error: " + err.getMessage());
        }
        return dataTT;

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getEmailSrc() {
        return emailSrc;
    }

    public void setEmailSrc(String emailSrc) {
        this.emailSrc = emailSrc;
    }

    public String getEmailTo() {
        return emailTo;
    }

    public void setEmailTo(String emailTo) {
        this.emailTo = emailTo;
    }

    public String getEmailBody() {
        return emailBody;
    }

    public void setEmailBody(String emailBody) {
        this.emailBody = emailBody;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public String getSubmittedBy() {
        return submittedBy;
    }

    public void setSubmittedBy(String submittedBy) {
        this.submittedBy = submittedBy;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getEmailReplyTo() {
        return emailReplyTo;
    }

    public void setEmailReplyTo(String emailReplyTo) {
        this.emailReplyTo = emailReplyTo;
    }

    public Date getTimeSubmitted() {
        return timeSubmitted;
    }

    public void setTimeSubmitted(Date timeSubmitted) {
        this.timeSubmitted = timeSubmitted;
    }

    public String getAttachment() {
        return attachment;
    }

    public void setAttachment(String attachment) {
        this.attachment = attachment;
    }
    
      
}

