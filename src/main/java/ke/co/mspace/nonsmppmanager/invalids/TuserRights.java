/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ke.co.mspace.nonsmppmanager.invalids;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author developer
 */
@Entity
@Table(name = "tUSERRIGHTS")
public class TuserRights implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "id")
    private Integer id;
    @Column(name = "newdept")
    private Character newdept;
    @Column(name = "dept")
    private Character dept;
    @Column(name = "newmsgcat")
    private Character newmsgcat;
    @Column(name = "msgcats")
    private Character msgcats;
    @Column(name = "pendingSmsTasks")
    private Character pendingSmsTasks;
    @Column(name = "pendingEmailTasks")
    private Character pendingEmailTasks;
    @Column(name = "tasksinprogress")
    private Character tasksinprogress;
    @Column(name = "discardedtasks")
    private Character discardedtasks;
    @Column(name = "reassignedtasks")
    private Character reassignedtasks;
    @Column(name = "completedtasks")
    private Character completedtasks;
    @Column(name = "myopentasks")
    private Character myopentasks;
    @Column(name = "myclosedtasks")
    private Character myclosedtasks;
    @Column(name = "sendsmstoclients")
    private Character sendsmstoclients;
    @Column(name = "sendsmstoagents")
    private Character sendsmstoagents;
    @Column(name = "sentsmsdept")
    private Character sentsmsdept;
    @Column(name = "smsturnaround")
    private Character smsturnaround;
    @Column(name = "tasksgroupedbystatus")
    private Character tasksgroupedbystatus;
    @Column(name = "dateUpdated")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateUpdated;
    @Column(name = "newuser")
    private Character newuser;
    @Column(name = "viewusers")
    private Character viewusers;
    @Column(name = "receivedsmscategory")
    private Character receivedsmscategory;
    @Column(name = "smsDept")
    private Character smsDept;
    @Column(name = "smsUser")
    private Character smsUser;
    @Column(name = "smsout")
    private Character smsout;
    @Column(name = "receivedsms")
    private Character receivedsms;
    @Column(name = "receivedsmstotals")
    private Character receivedsmstotals;
    @Basic(optional = false)
    @NotNull
    @Column(name = "receivedsmsSummary")
    private Character receivedsmsSummary;
    @Basic(optional = false)
    @NotNull
    @Column(name = "sentSmsUser")
    private Character sentSmsUser;
    @Basic(optional = false)
    @NotNull
    @Column(name = "Billing")
    private Character billing;
    @Column(name = "sendEmail")
    private Character sendEmail;
    @Column(name = "signaturemgt")
    private Character signaturemgt;
    @Column(name = "emailsubscription")
    private Character emailsubscription;
    @Column(name = "sentemail")
    private Character sentemail;
    @Column(name = "sentemailsummary")
    private Character sentemailsummary;
    @Basic(optional = false)
    @NotNull
    @Column(name = "emailsurvey")
    private Character emailsurvey;
    @Column(name = "clientDetails")
    private Character clientDetails;
    @Column(name = "emailAccSetup")
    private Character emailAccSetup;
    @Column(name = "binds")
    private Character binds;
    @Column(name = "dbConnection")
    private Character dbConnection;
    @Column(name = "campaignMgr")
    private Character campaignMgr;
    @Column(name = "scheduleMgr")
    private Character scheduleMgr;
    @Column(name = "smsout_summary")
    private Character smsoutSummary;
    @Column(name = "ussd_report")
    private Character ussdReport;

    public TuserRights() {
    }

    public TuserRights(Integer id) {
        this.id = id;
    }

    public TuserRights(Integer id, Character receivedsmsSummary, Character sentSmsUser, Character billing, Character emailsurvey) {
        this.id = id;
        this.receivedsmsSummary = receivedsmsSummary;
        this.sentSmsUser = sentSmsUser;
        this.billing = billing;
        this.emailsurvey = emailsurvey;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Character getNewdept() {
        return newdept;
    }

    public void setNewdept(Character newdept) {
        this.newdept = newdept;
    }

    public Character getDept() {
        return dept;
    }

    public void setDept(Character dept) {
        this.dept = dept;
    }

    public Character getNewmsgcat() {
        return newmsgcat;
    }

    public void setNewmsgcat(Character newmsgcat) {
        this.newmsgcat = newmsgcat;
    }

    public Character getMsgcats() {
        return msgcats;
    }

    public void setMsgcats(Character msgcats) {
        this.msgcats = msgcats;
    }

    public Character getPendingSmsTasks() {
        return pendingSmsTasks;
    }

    public void setPendingSmsTasks(Character pendingSmsTasks) {
        this.pendingSmsTasks = pendingSmsTasks;
    }

    public Character getPendingEmailTasks() {
        return pendingEmailTasks;
    }

    public void setPendingEmailTasks(Character pendingEmailTasks) {
        this.pendingEmailTasks = pendingEmailTasks;
    }

    public Character getTasksinprogress() {
        return tasksinprogress;
    }

    public void setTasksinprogress(Character tasksinprogress) {
        this.tasksinprogress = tasksinprogress;
    }

    public Character getDiscardedtasks() {
        return discardedtasks;
    }

    public void setDiscardedtasks(Character discardedtasks) {
        this.discardedtasks = discardedtasks;
    }

    public Character getReassignedtasks() {
        return reassignedtasks;
    }

    public void setReassignedtasks(Character reassignedtasks) {
        this.reassignedtasks = reassignedtasks;
    }

    public Character getCompletedtasks() {
        return completedtasks;
    }

    public void setCompletedtasks(Character completedtasks) {
        this.completedtasks = completedtasks;
    }

    public Character getMyopentasks() {
        return myopentasks;
    }

    public void setMyopentasks(Character myopentasks) {
        this.myopentasks = myopentasks;
    }

    public Character getMyclosedtasks() {
        return myclosedtasks;
    }

    public void setMyclosedtasks(Character myclosedtasks) {
        this.myclosedtasks = myclosedtasks;
    }

    public Character getSendsmstoclients() {
        return sendsmstoclients;
    }

    public void setSendsmstoclients(Character sendsmstoclients) {
        this.sendsmstoclients = sendsmstoclients;
    }

    public Character getSendsmstoagents() {
        return sendsmstoagents;
    }

    public void setSendsmstoagents(Character sendsmstoagents) {
        this.sendsmstoagents = sendsmstoagents;
    }

    public Character getSentsmsdept() {
        return sentsmsdept;
    }

    public void setSentsmsdept(Character sentsmsdept) {
        this.sentsmsdept = sentsmsdept;
    }

    public Character getSmsturnaround() {
        return smsturnaround;
    }

    public void setSmsturnaround(Character smsturnaround) {
        this.smsturnaround = smsturnaround;
    }

    public Character getTasksgroupedbystatus() {
        return tasksgroupedbystatus;
    }

    public void setTasksgroupedbystatus(Character tasksgroupedbystatus) {
        this.tasksgroupedbystatus = tasksgroupedbystatus;
    }

    public Date getDateUpdated() {
        return dateUpdated;
    }

    public void setDateUpdated(Date dateUpdated) {
        this.dateUpdated = dateUpdated;
    }

    public Character getNewuser() {
        return newuser;
    }

    public void setNewuser(Character newuser) {
        this.newuser = newuser;
    }

    public Character getViewusers() {
        return viewusers;
    }

    public void setViewusers(Character viewusers) {
        this.viewusers = viewusers;
    }

    public Character getReceivedsmscategory() {
        return receivedsmscategory;
    }

    public void setReceivedsmscategory(Character receivedsmscategory) {
        this.receivedsmscategory = receivedsmscategory;
    }

    public Character getSmsDept() {
        return smsDept;
    }

    public void setSmsDept(Character smsDept) {
        this.smsDept = smsDept;
    }

    public Character getSmsUser() {
        return smsUser;
    }

    public void setSmsUser(Character smsUser) {
        this.smsUser = smsUser;
    }

    public Character getSmsout() {
        return smsout;
    }

    public void setSmsout(Character smsout) {
        this.smsout = smsout;
    }

    public Character getReceivedsms() {
        return receivedsms;
    }

    public void setReceivedsms(Character receivedsms) {
        this.receivedsms = receivedsms;
    }

    public Character getReceivedsmstotals() {
        return receivedsmstotals;
    }

    public void setReceivedsmstotals(Character receivedsmstotals) {
        this.receivedsmstotals = receivedsmstotals;
    }

    public Character getReceivedsmsSummary() {
        return receivedsmsSummary;
    }

    public void setReceivedsmsSummary(Character receivedsmsSummary) {
        this.receivedsmsSummary = receivedsmsSummary;
    }

    public Character getSentSmsUser() {
        return sentSmsUser;
    }

    public void setSentSmsUser(Character sentSmsUser) {
        this.sentSmsUser = sentSmsUser;
    }

    public Character getBilling() {
        return billing;
    }

    public void setBilling(Character billing) {
        this.billing = billing;
    }

    public Character getSendEmail() {
        return sendEmail;
    }

    public void setSendEmail(Character sendEmail) {
        this.sendEmail = sendEmail;
    }

    public Character getSignaturemgt() {
        return signaturemgt;
    }

    public void setSignaturemgt(Character signaturemgt) {
        this.signaturemgt = signaturemgt;
    }

    public Character getEmailsubscription() {
        return emailsubscription;
    }

    public void setEmailsubscription(Character emailsubscription) {
        this.emailsubscription = emailsubscription;
    }

    public Character getSentemail() {
        return sentemail;
    }

    public void setSentemail(Character sentemail) {
        this.sentemail = sentemail;
    }

    public Character getSentemailsummary() {
        return sentemailsummary;
    }

    public void setSentemailsummary(Character sentemailsummary) {
        this.sentemailsummary = sentemailsummary;
    }

    public Character getEmailsurvey() {
        return emailsurvey;
    }

    public void setEmailsurvey(Character emailsurvey) {
        this.emailsurvey = emailsurvey;
    }

    public Character getClientDetails() {
        return clientDetails;
    }

    public void setClientDetails(Character clientDetails) {
        this.clientDetails = clientDetails;
    }

    public Character getEmailAccSetup() {
        return emailAccSetup;
    }

    public void setEmailAccSetup(Character emailAccSetup) {
        this.emailAccSetup = emailAccSetup;
    }

    public Character getBinds() {
        return binds;
    }

    public void setBinds(Character binds) {
        this.binds = binds;
    }

    public Character getDbConnection() {
        return dbConnection;
    }

    public void setDbConnection(Character dbConnection) {
        this.dbConnection = dbConnection;
    }

    public Character getCampaignMgr() {
        return campaignMgr;
    }

    public void setCampaignMgr(Character campaignMgr) {
        this.campaignMgr = campaignMgr;
    }

    public Character getScheduleMgr() {
        return scheduleMgr;
    }

    public void setScheduleMgr(Character scheduleMgr) {
        this.scheduleMgr = scheduleMgr;
    }

    public Character getSmsoutSummary() {
        return smsoutSummary;
    }

    public void setSmsoutSummary(Character smsoutSummary) {
        this.smsoutSummary = smsoutSummary;
    }

    public Character getUssdReport() {
        return ussdReport;
    }

    public void setUssdReport(Character ussdReport) {
        this.ussdReport = ussdReport;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof TuserRights)) {
            return false;
        }
        TuserRights other = (TuserRights) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.mspace1.model2.TuserRights[ id=" + id + " ]";
    }
    
}
