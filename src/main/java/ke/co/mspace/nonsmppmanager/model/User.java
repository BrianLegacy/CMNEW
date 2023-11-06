/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ke.co.mspace.nonsmppmanager.model;

import org.mspace.clientmanager.group.Group;
import ke.co.mspace.nonsmppmanager.invalids.FacePainter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.validation.constraints.Pattern;
import ke.co.mspace.nonsmppmanager.service.AlphaScroller;
import ke.co.mspace.nonsmppmanager.service.AlphaServiceApi;
import ke.co.mspace.nonsmppmanager.service.AlphaServiceImpl;
import ke.co.mspace.nonsmppmanager.service.EmailUserServiceImpl;
import ke.co.mspace.nonsmppmanager.service.ManageCreditApi;
import ke.co.mspace.nonsmppmanager.service.ManageCreditImpl;
import ke.co.mspace.nonsmppmanager.service.UserScroller;
import ke.co.mspace.nonsmppmanager.service.UserServiceApi;
import ke.co.mspace.nonsmppmanager.service.UserServiceImpl;
import ke.co.mspace.nonsmppmanager.util.JdbcUtil;
import ke.co.mspace.nonsmppmanager.util.JsfUtil;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Norrey Osako
 */
@ManagedBean
//@ViewScoped
public class User {
    private  org.slf4j.Logger logger=LoggerFactory.getLogger(User.class);

    private Long id;

    @NotEmpty(message = "Username required")
//    @Pattern(regexp = "^[a-zA-Z0-9_-]{3,16}$", message = "Invalid Username")
    private String username;

    @NotEmpty(message = "Password required")
    @Length(min = 6, message = "Six or more characters")
    private String password;

     
    private int smsCredits;
    private int emailCredits;

    private String organization;
     

    @Length(min = 10, message = "Invalid Mobile Number")
    private String userMobile;

    @NotEmpty(message = "Email required")
    @Pattern(regexp = ".+@.+\\..+", message = "Invalid Email")
    private String userEmail;

    private List<String> comboAlphanumerics;

    private Date startDate= new Date();
    private Date endDate;

    private boolean enableEmailAlertWhenCreditOver = false;
    private boolean reseller;
//    private List<User> listUsers;
    private String selectedUsername;
    private String previousUsername;
    private String message;
    private String alphanumeric;
      private String group;
      private Group selectedGroup;
    private String airalphanumeric;
    private Long alphaId;
    private int creditsToManage;
    private int emailCreditsToManage;   
    private char admin;
    private String userType;
    private boolean createAccount;
    private String creditManageType;
    private Map<String, Object> simpleStatistics;
    private int alertThreshold = 100;
    private int arrears;
    private float cost_per_sms = (float) 1.0;
    private int maxContacts;
    private int maxTotal;

   
    private String firstName="";
    private String surName="";
    private int emailPlan=1;
    private Connection conn = null;
    final JdbcUtil util = new JdbcUtil();
    private static final Logger LOG = Logger.getLogger(User.class.getName());

    public User() {
    }

    public User(Long id, String username, String password, int smsCredits, int emailCredits,String organization,
            String userMobile, String userEmail, Date startDate, Date endDate, String alphanumeric,
            int creditsToManage, char admin, String creditManageType, int alertThreshold, int arrears, float cost_per_sms, int MaxContacts, int MaxTotal) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.smsCredits = smsCredits;
        this.emailCredits = emailCredits;
        this.organization = organization;
        this.userMobile = userMobile;
        this.userEmail = userEmail;
        this.startDate = startDate;
        this.endDate = endDate;
        this.alphanumeric = alphanumeric;
        this.creditsToManage = creditsToManage;
        this.admin = admin;
        this.creditManageType = creditManageType;
        this.alertThreshold = alertThreshold;
        this.comboAlphanumerics = new ArrayList<>();
        this.cost_per_sms = cost_per_sms;
        this.arrears = arrears;
//        this.maxContacts=MaxContacts;
        this.maxTotal=MaxTotal;

    }
    
     @ManagedProperty(value = "#{facePainter}")
    public FacePainter facePainter;

    public FacePainter getFacePainter() {
        return facePainter;
    }

    public void setFacePainter(FacePainter facePainter) {
        this.facePainter = facePainter;
    }
     
    
    List<SelectItem> selectItems;

    public Map<String,String>  getComboAlphanumerics() {
  
      Map<String,String> myAlphanumerics = new HashMap<>();
     
   
        AlphaServiceImpl asi = new AlphaServiceImpl();
        selectItems= new ArrayList();
     
       
        try {
            conn = util.getConnectionTodbSMS();
            myAlphanumerics = asi.getAlphanumericsNames(conn);
            JdbcUtil.closeConnection(conn);
        } catch (SQLException e) {
            JdbcUtil.closeConnection(conn);
        }
        logger.debug("returned "+myAlphanumerics.size()+" alphanumerics");
        return myAlphanumerics;
    }
        public List<SelectItem> getComboGroups() {
        AlphaServiceImpl asi = new AlphaServiceImpl();
        selectItems= new ArrayList();
        List<SelectItem> mygroups = new ArrayList<>();
        try {
            conn = util.getConnectionTodbSMS();
            mygroups = asi.getGroups(conn);
            JdbcUtil.closeConnection(conn);
        } catch (SQLException e) {
            JdbcUtil.closeConnection(conn);
        }
        return mygroups;
    }
             public List<SelectItem> getTypeCombo() {
        List<SelectItem> types = new ArrayList<>();
        types.add(new SelectItem("Expiry"));
         types.add(new SelectItem("No Expiry"));
        return types;
    }

    public List<String> getAirComboAlphanumerics() {
        AlphaServiceImpl asi = new AlphaServiceImpl();
        List<String> myAlphanumerics = new ArrayList<>();
        try {
            conn = util.getConnectionTodbSMS();
            myAlphanumerics = asi.getAirTelAlphas(conn);
            JdbcUtil.closeConnection(conn);
        } catch (SQLException e) {
            JdbcUtil.closeConnection(conn);
        }
        return myAlphanumerics;
    }

    //++++++++++++++++++++++++++++++++++++++++++++++++++++++
    public List<String> getUnusignedAlphanumerics() {
        AlphaServiceImpl asi = new AlphaServiceImpl();
        List<String> myAlphanumerics = new ArrayList<>();
        try {
            conn = util.getConnectionTodbSMS();
            myAlphanumerics = asi.getUnusagnedphanumericsNames(conn);
            JdbcUtil.closeConnection(conn);
        } catch (SQLException e) {
            JdbcUtil.closeConnection(conn);
        }
        return myAlphanumerics;
    }
       public void addResellerALphanumerics() {
        AlphaServiceImpl asi = new AlphaServiceImpl();
        
        List<String> myAlphanumerics = new ArrayList<>();
        try {
            conn = util.getConnectionTodbSMS();
//            asi.updateAgentAlphas(username, alphanumeric, conn);
            JdbcUtil.closeConnection(conn);
        } catch (SQLException e) {
            JdbcUtil.closeConnection(conn);
        }
//        return myAlphanumerics;
    }

    public void setComboAlphanumerics(List<String> comboAlphanumerics) {
        this.comboAlphanumerics = comboAlphanumerics;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getSurName() {
        return surName;
    }

    public void setSurName(String surName) {
        this.surName = surName;
    }

    public Group getSelectedGroup() {
        return selectedGroup;
    }

    public void setSelectedGroup(Group selectedGroup) {
        this.selectedGroup = selectedGroup;
    }

 

    public Long getId() {

        return id;
    }

    public void setId(Long id) {

        this.id = id;
    }

    public Long getAlphaId() {

        return alphaId;
    }

    public void setAlphaId(Long alphaId) {
        this.alphaId = alphaId;
    }

    public int getArrears() {
        return arrears;
    }

    public void setArrears(int arrears) {
        this.arrears = arrears;
    }

    public float getCost_per_sms() {
        //System.out.println("client per sms: " + cost_per_sms);
        return cost_per_sms;
    }

    public void setCost_per_sms(float cost_per_sms) {
        this.cost_per_sms = cost_per_sms;
    }

    public String getUsername() {

        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {

        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getSmsCredits() {

        return smsCredits;
    }

    public void setSmsCredits(int smsCredits) {
        this.smsCredits = smsCredits;
    }
    
    public int getEmailCredits() {

        return emailCredits;
    }

    public void setEmailCredits(int emailCredits) {
        this.emailCredits = emailCredits;
    }

    public String getOrganization() {

        return organization;
    }

    public void setOrganization(String organization) {
        this.organization = organization;
    }

    public String getUserMobile() {

        return userMobile;
    }

    public void setUserMobile(String userMobile) {
        this.userMobile = userMobile;
    }

    public String getUserEmail() {

        return userEmail;
    }

    public void setUserEmail(String userEmail) {

        this.userEmail = userEmail;
    }
    
    
    
    public String getUssdCode() {

        return userEmail;
    }

    public void setUssdCode(String userEmail) {

        this.userEmail = userEmail;
    }
    
    

    public int getMaxContacts() {
        return maxContacts;
    }

    public void setMaxContacts(int MaxContacts) {
        this.maxContacts = MaxContacts;
    }
    
     public int getMaxTotal() {
        return maxTotal;
    }

    public void setMaxTotal(int maxTotal) {
        this.maxTotal = maxTotal;
    }
  
    public Date getStartDate() {
        
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public int getEmailPlan() {
        return emailPlan;
    }

    public void setEmailPlan(int emailPlan) {
        this.emailPlan = emailPlan;
    }

    
    
    public Date getEndDate() {
       //System.out.println(this.emailPlan);
//        Calendar cal = Calendar.getInstance();
//        cal.add(Calendar.DATE,this.emailPlan);
//        endDate=cal.getTime();
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public boolean isEnableEmailAlertWhenCreditOver() {
        return enableEmailAlertWhenCreditOver;
    }

    public void setEnableEmailAlertWhenCreditOver(boolean enableEmailAlertWhenCreditOver) {
        this.enableEmailAlertWhenCreditOver = enableEmailAlertWhenCreditOver;
    }

    public boolean getReseller() {
        return reseller;
    }

    public boolean notReseller() {
        Object obj = FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("reseller");
        if (obj != null && !obj.equals("null")) {
            return !(Boolean) obj;
        }
        return true;
    }

    public void setReseller(boolean reseller) {
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("reseller", reseller);
        this.reseller = reseller;
    }

    public int getAlertThreshold() {
        return alertThreshold;
    }

    public void setAlertThreshold(int alertThreshold) {
        this.alertThreshold = alertThreshold;
    }

    public String getSelectedUsername() {
        return selectedUsername;
    }

    public void setSelectedUsername(String selectedUsername) {
        this.selectedUsername = selectedUsername;
    }

    public String getAiralphanumeric() {
        return airalphanumeric;
    }

    public void setAiralphanumeric(String airalphanumeric) {
        this.airalphanumeric = airalphanumeric;
    }

    public String getAlphanumeric() {

        return alphanumeric;
    }

    public void setAlphanumeric(String alphanumeric) {
        this.alphanumeric = alphanumeric;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getCreditsToManage() {
        return creditsToManage;
    }

    public void setCreditsToManage(int creditsToManage) {
        this.creditsToManage = creditsToManage;
    }
    
    public int getEmailCreditsToManage() {
        return emailCreditsToManage;
    }

    public void setEmailCreditsToManage(int emailCreditsToManage) {
        this.emailCreditsToManage = emailCreditsToManage;
    }
    

    public String getPreviousUsername() {
        return username;
    }

    public void setPreviousUsername(String previousUsername) {
        this.previousUsername = previousUsername;
    }

    public boolean isCreateAccount() {
        return createAccount;
    }

    public void setCreateAccount(boolean createAccount) {
        this.createAccount = createAccount;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    @Override
    public String toString() {
        return "User{" + "id=" + id + ", username=" + username + ", password=" + password + ", smsCredits=" + smsCredits + ", emailCredits=" + emailCredits + ", organization=" + organization + ", userMobile=" + userMobile + ", userEmail=" + userEmail + ", comboAlphanumerics=" + comboAlphanumerics + ", startDate=" + startDate + ", endDate=" + endDate + ", enableEmailAlertWhenCreditOver=" + enableEmailAlertWhenCreditOver + ", reseller=" + reseller + ", selectedUsername=" + selectedUsername + ", previousUsername=" + previousUsername + ", message=" + message + ", alphanumeric=" + alphanumeric + ", group=" + group + ", selectedGroup=" + selectedGroup + ", airalphanumeric=" + airalphanumeric + ", alphaId=" + alphaId + ", creditsToManage=" + creditsToManage + ", emailCreditsToManage=" + emailCreditsToManage + ", admin=" + admin + ", userType=" + userType + ", createAccount=" + createAccount + ", creditManageType=" + creditManageType + ", simpleStatistics=" + simpleStatistics + ", alertThreshold=" + alertThreshold + ", arrears=" + arrears + ", cost_per_sms=" + cost_per_sms + ", maxContacts=" + maxContacts + ", maxTotal=" + maxTotal + ", firstName=" + firstName + ", surName=" + surName + ", emailPlan=" + emailPlan + ", conn=" + conn + ", util=" + util + ", facePainter=" + facePainter + ", selectItems=" + selectItems + ", impl=" + impl + ", us=" + us + ", ab=" + ab + '}';
    }

  
     
    UserServiceImpl impl = new UserServiceImpl();
    
    public void riderectTo() throws IOException {
        FacesContext.getCurrentInstance().getExternalContext().redirect("ControlPage.jsf");
    }

    public void manageCredit() throws IOException {
       
        ManageCreditImpl mcr = new ManageCreditImpl();
        char adminv = mcr.admiValue();

        AlphaScroller ac = new AlphaScroller();
        UserScroller us = new UserScroller();
        // int credit=mci.getAgentAvailableCredits();
        // System.out.println("Available Credits::::" + ac.currentUSer() + ":::");

        try {
            String agent = ac.currentUSer();
            conn = util.getConnectionTodbSMS();
            //========================>
            //System.out.println("Getting here or not"+us.availableCredits(conn));
            int current = Math.round(us.availableCredits(conn)[0]);
            LOG.info("manageCredit");
            SMSCredits smsCredit = new SMSCredits();
            ManageCreditApi creditManager = new ManageCreditImpl();
            UserServiceApi userService = new UserServiceImpl();

            smsCredit.setUsername(username);
            smsCredit.setActionTime(new Date());

            switch (creditManageType) {

                case "add":
                    FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("credit_type", "add");
                    final int previous_balance = smsCredits;
                    this.smsCredits = smsCredits + creditsToManage;
                    final int new_balance = smsCredits;
                    int newBalace;
                    //if((curent>0) && (creditsToManage<=current) && (currennt!=-1))
                    if (current < creditsToManage && current != -1) {
                        
                        JsfUtil.addErrorMessage("You have insufficient SMS balance.Your balance is: " + current + " SMS");
                       // System.out.println(" The Operation is invalid ");
                    } else {
                        String updateUser = ("User Update::: " + new Date() + " User: " + this.username + " Previous Balance: " + previous_balance + " Credit Allocated: " + creditsToManage + " New Balance: " + new_balance);
                        //System.out.println(updateUser);
                        //System.out.println("Sms credits to manage :" + creditsToManage + "New balance: " + new_balance + "prevoius Balance: " + previous_balance);
                        smsCredit.setActionType(adminv == '1' ? '1' : '3');
                        smsCredit.setNumCredits(creditsToManage);
                        smsCredit.setNew_balance(new_balance);
                        smsCredit.setPrevious_balance(previous_balance);
                        //creditManager.persistUpdate(smsCredit, conn);
                        creditManager.persistUpdate2(smsCredit, conn, creditsToManage, current, current - creditsToManage);
                        newBalace = adminv == '1' ? -1 : current - creditsToManage;
                        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("new_balace", newBalace);
                        userService.updateCredits(username, smsCredits, conn);
                        userService.updateAgentCredits(agent, current, creditsToManage, current - creditsToManage, conn);
                        //=======================

                        JsfUtil.addSuccessMessage("You have successfully added " + creditsToManage + " SMS Credits to user " + username);
                    }
                    break;

                case "update":
                    FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("credit_type", "update");
                    final int previous_balance2 = smsCredits;
                    if (creditsToManage > previous_balance2 && previous_balance2 != -1) {
                        JsfUtil.addErrorMessage("Balance is lower than SMS to deduct");
                        //System.out.println("Balance is lower than sms todeduct");
                    } else {
                        int afterBalance = previous_balance2 - creditsToManage;
                        //System.out.println("CURRENT SMS CREDITS/PREVIOUS BALANCE: " + previous_balance2 + "\n" + "CREDITS TO MANAGE: " + creditsToManage + "\n" + "NEW BALANCE: " + afterBalance);
                        this.smsCredits = creditsToManage;
                        String updateUser = ("User Update::: " + new Date() + " User: " + this.username + " Previous Balance: " + previous_balance2 + " Credit Allocated: " + creditsToManage + " New Balance: " + afterBalance);
                        //System.out.println(updateUser);
                        final int new_balance2 = afterBalance;
                        smsCredit.setActionType(adminv == '1' ? '2' : '3');
                        smsCredit.setNumCredits(creditsToManage);
                        smsCredit.setNew_balance(new_balance2);
                        smsCredit.setPrevious_balance(previous_balance2);
                        //
                        //creditManager.persistUpdate(smsCredit, conn,);
                        creditManager.persistUpdate2(smsCredit, conn, creditsToManage, previous_balance2, previous_balance2 - creditsToManage);
                        userService.updateCredits(username, previous_balance2 - smsCredits, conn);
                        newBalace = adminv == '1' ? -1 : previous_balance2 - smsCredits;
                        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("new_balace", previous_balance2 - creditsToManage);
                        userService.updateAgentCredits(agent, current, creditsToManage, previous_balance2 - creditsToManage, conn);
                        userService.alterAgentCredits(agent, current, previous_balance2, creditsToManage, conn);
                       // System.out.println("Credits to manage ==" + creditsToManage + "\n" + "Previous balance is ==" + previous_balance2 + "And Curent:" + current);
                        //System.out.println("SMS after alter is :" + smsCredits);
                        //FacesContext.getCurrentInstance().getExternalContext().redirect(toRedirect);

                        JsfUtil.addSuccessMessage("You have successfully deducted " + creditsToManage + " SMS  " + "from  " + username);
                    }

                    break;
            }
            this.updateAdminBal();
          
            JdbcUtil.closeConnection(conn);
        } catch (SQLException e) {
            JdbcUtil.closeConnection(conn);
        }
        // FacesContext.getCurrentInstance().getExternalContext().redirect(toRedirect);
    }
    
    
    
   //Function to manage Email Credits implemented by Aaron
    public void manageEmailCredit() throws IOException {
        ManageCreditImpl mcr = new ManageCreditImpl();
        char adminv = mcr.admiValue();

        AlphaScroller ac = new AlphaScroller();
        UserScroller us = new UserScroller();
        // int credit=mci.getAgentAvailableCredits();
        // System.out.println("Available Credits::::" + ac.currentUSer() + ":::");

        try {
            String agent = ac.currentUSer();
            conn = util.getConnectionTodbSMS();
            //========================>
            //System.out.println("Getting here or not"+us.availableCredits(conn));
            int current = Math.round(us.availableCredits(conn)[0]);
            LOG.info("manageCredit");
            SMSCredits smsCredit = new SMSCredits();
            ManageCreditApi creditManager = new ManageCreditImpl();
            UserServiceApi userService = new UserServiceImpl();

            smsCredit.setUsername(username);
            smsCredit.setActionTime(new Date());
         

            switch (creditManageType) {

                case "add":
                    FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("credit_type", "add");
                    final int previous_balance = maxContacts;
                    this.maxContacts = maxContacts + creditsToManage;
                    final int new_balance = maxContacts;
                    int newBalace;
                    //if((curent>0) && (creditsToManage<=current) && (currennt!=-1))
                    if (current < creditsToManage && current != -1) {
                        JsfUtil.addErrorMessage("You have insufficient Email balance.Your balance is: " + current + " Email");
                       // System.out.println(" The Operation is invalid ");
                    } else {
                        String updateUser = ("User Update::: " + new Date() + " User: " + this.username + " Previous Balance: " + previous_balance + " Credit Allocated: " + creditsToManage + " New Balance: " + new_balance);
                        //System.out.println(updateUser);
                        //System.out.println("Sms credits to manage :" + creditsToManage + "New balance: " + new_balance + "prevoius Balance: " + previous_balance);
                        smsCredit.setActionType(adminv == '1' ? '1' : '3');
                        smsCredit.setNumCredits(creditsToManage);
                        smsCredit.setNew_balance(new_balance);
                        smsCredit.setPrevious_balance(previous_balance);
                        //creditManager.persistUpdate(smsCredit, conn);
                        creditManager.persistUpdate2(smsCredit, conn, creditsToManage, current, current - creditsToManage);
                        newBalace = adminv == '1' ? -1 : current - creditsToManage;
                        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("new_balace", newBalace);
                        userService.updateEmailCredits(username, maxContacts, conn);
                        userService.updateAgentCredits(agent, current, creditsToManage, current - creditsToManage, conn);
                        //=======================

                        JsfUtil.addSuccessMessage("You have successfully Added " + creditsToManage + " Email Credits to " + username + " On "+ new Date());
                    }
                    break;

                 case "update":
                    FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("credit_type", "update");
                    final int previous_balance2 = maxContacts;
                    if (creditsToManage > previous_balance2 && previous_balance2 != -1) {
                        JsfUtil.addErrorMessage("Balance is lower than Emails to deduct");
                        //System.out.println("Balance is lower than sms todeduct");
                    } else {
                        int afterBalance = previous_balance2 - creditsToManage;
                        //System.out.println("CURRENT SMS CREDITS/PREVIOUS BALANCE: " + previous_balance2 + "\n" + "CREDITS TO MANAGE: " + creditsToManage + "\n" + "NEW BALANCE: " + afterBalance);
                        this.maxContacts = creditsToManage;
                        String updateUser = ("User Update::: " + new Date() + " User: " + this.username + " Previous Balance: " + previous_balance2 + " Credit Allocated: " + creditsToManage + " New Balance: " + afterBalance);
                        //System.out.println(updateUser);
                        final int new_balance2 = afterBalance;
                        smsCredit.setActionType(adminv == '1' ? '2' : '3');
                        smsCredit.setNumCredits(creditsToManage);
                        smsCredit.setNew_balance(new_balance2);
                        smsCredit.setPrevious_balance(previous_balance2);
                        //
                        //creditManager.persistUpdate(smsCredit, conn,);
                        creditManager.persistUpdate2(smsCredit, conn, creditsToManage, previous_balance2, previous_balance2 - creditsToManage);
                        userService.updateEmailCredits(username, previous_balance2 - maxContacts, conn);
                        newBalace = adminv == '1' ? -1 : previous_balance2 - maxContacts;
                        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("new_balace", previous_balance2 - creditsToManage);
                        userService.updateAgentCredits(agent, current, creditsToManage, previous_balance2 - creditsToManage, conn);
                        userService.alterAgentCredits(agent, current, previous_balance2, creditsToManage, conn);
                       // System.out.println("Credits to manage ==" + creditsToManage + "\n" + "Previous balance is ==" + previous_balance2 + "And Curent:" + current);
                        //System.out.println("SMS after alter is :" + smsCredits);
                        //FacesContext.getCurrentInstance().getExternalContext().redirect(toRedirect);

                        JsfUtil.addSuccessMessage("You have successfully deducted " + creditsToManage + " Emails  " + "from  " + username);
                    }

                    break;
            }
            this.updateAdminBal();
            JdbcUtil.closeConnection(conn);
        } catch (SQLException e) {
            JdbcUtil.closeConnection(conn);
        }
        // FacesContext.getCurrentInstance().getExternalContext().redirect(toRedirect);
    }
    
    
    public void updateAdminBal() {
        AlphaScroller ac = new AlphaScroller();
        String sql = "UPDATE tUSER set max_total = '-1' where admin=1";
        JdbcUtil util = new JdbcUtil();
        try {
            Connection con = util.getConnectionTodbSMS();
            Statement st = con.createStatement();
            //System.out.println(sql);
            st.executeUpdate(sql);
            //System.out.println("Upating the ");
        } catch (SQLException ex) {
            Logger.getLogger(User.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public String getCreditManageType() {

        return creditManageType;
    }

    public void setCreditManageType(String creditManageType) {
        this.creditManageType = creditManageType;
    }

    public char getAdmin() {
        return admin;
    }

    public void setAdmin(char admin) {
        this.admin = admin;
    }

    public String loadViewUsers() {
        //updates the initial username (this.username) value into session map
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("lastuser", this.username);
        //return "/manager/showLast.jsp";
        return "/manager/showUsers.jsp";
    }
     public String loadViewGroups() {
        //updates the initial username (this.username) value into session map
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("lastuser", this.username);
        //return "/manager/showLast.jsp";
        return "/manager/showGroups.jsp";
    }
    
    
//    added by Aaron
     public String loadViewEmailUsers() {
        //updates the initial username (this.username) value into session map
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("lastuser", this.username);
        //return "/manager/showLast.jsp";
        return "/manager/showEmailUsers.jsp";
    }
    
    public String loadViewRes() {

        //return "/manager/viewNewReseller.jsp";
        return "/manager/viewUserperAgent.jsp";
    }

    public void validateCredits(FacesContext context, UIComponent component, Object value) {
        char adminv = '0';
        int resellerBalance=0;
        AlphaScroller ac = new AlphaScroller();
        UserScroller us = new UserScroller();
        String agent = ac.currentUSer();
        int current = Math.round(us.availableCredits(conn)[0]);
        String getRes = "SELECT admin,max_total from tUSER where username='" + agent + "'";
        try {
            conn = util.getConnectionTodbSMS();
            Statement t = conn.createStatement();
            ResultSet rs = t.executeQuery(getRes);

            while (rs.next()) {
                //Gets the value of the admin forthe current user
                adminv = rs.getString("admin").charAt(0);
                resellerBalance = rs.getInt("max_total");
                //System.out.println("THE ADMIN VALUE IS  :" + adminv);

            }
        } catch (Exception e) {
        }
        //if 
        if (Integer.valueOf(value.toString()) > resellerBalance && adminv!='1') {
            
            JsfUtil.addErrorMessage("Your ");

        }

    }

    public void saveEmailUser() throws IOException {
        
           AlphaServiceImpl asi = new AlphaServiceImpl();
       
     
       
        try {
            conn = util.getConnectionTodbSMS();
            this.setSelectedGroup(asi.getGroup(group, conn));
            JdbcUtil.closeConnection(conn);
        } catch (SQLException e) {
            JdbcUtil.closeConnection(conn);
        }
        char adminv = '0';
        int resellerBalance=0;
        AlphaScroller ac = new AlphaScroller();
        UserScroller us = new UserScroller();
        UserServiceApi userService = new UserServiceImpl();
        String agent = ac.currentUSer();
        int current =Math.round(us.availableCredits(conn)[0]);
        String getRes = "SELECT admin,max_contacts from tUSER where username='" + agent + "'";
//        String getRes = "SELECT admin,max_contacts from tUSER where username=' and agent ='email'";
        try {
            conn = util.getConnectionTodbSMS();
            Statement t = conn.createStatement();
            ResultSet rs = t.executeQuery(getRes);

            while (rs.next()) {
                //Gets the value of the admin forthe current user
                adminv = rs.getString("admin").charAt(0);
                resellerBalance = rs.getInt("max_contacts");
                //System.out.println("THE ADMIN VALUE IS  :" + adminv);
            }

            LOG.info("saveEmailUser");
            EmailUserServiceImpl service = new EmailUserServiceImpl();
            ManageCreditApi creditManager = new ManageCreditImpl();
            this.setEndDate(service.setEndDate());
            this.setStartDate(new Date());

            if (adminv != '1') {
                this.setAdmin('3');
            } else {
                this.setAdmin('2');
            }
//            System.out.println("Cost Per SMS "+this.cost_per_sms);
            
            

            service.persistUser(this, conn);
            
            //
            SMSCredits credits = new SMSCredits();
            credits.setActionTime(new Date());
            credits.setActionType(adminv=='1'?'1':'3');
            credits.setNumCredits(resellerBalance<emailCredits && adminv!='1'? 0:emailCredits);
            credits.setUsername(username);
            credits.setPrevious_balance(0);
            credits.setNew_balance(emailCredits);
//            credits.setAgent(agent);
            credits.setAgent_prevbal(resellerBalance);
            
            credits.setAgent_newbal(adminv=='1'? -1:resellerBalance-emailCredits);
            
            creditManager.persistUpdate(credits, conn);//credits.getNumCredits(),0,credits.getNumCredits());

            User newUser = service.loadCustomerByUsername(username, conn);
            if (FacesContext.getCurrentInstance().getExternalContext().getSessionMap().containsKey("userScroller")) {
                ((UserScroller) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("userScroller")).addUserToList(newUser);
            }
            
            
    //            AlphaServiceApi aService = new AlphaServiceImpl();
//            if (!alphanumeric.isEmpty()) {
//                //get the type of alpa selected and persist it in
//                String alphaType=aService.getAlphanumericType(alphanumeric, conn);
//                aService.persistAlpha(username, alphanumeric,alphaType,conn);
//            }
//            Alpha alpha = aService.loadAlphanumericByUsername(username, conn);
//            if (FacesContext.getCurrentInstance().getExternalContext().getSessionMap().containsKey("alphaScroller")) {
//                ((AlphaScroller) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("alphaScroller")).addAlphaToList(alpha);
//            }
            JsfUtil.addSuccessMessage("Email User Saved successfully.");
            //Added to manage ccredits 
            userService.updateCredits(username, credits.getNumCredits(), conn);
            this.updateAdminBal();
            clearAll();
            JdbcUtil.closeConnection(conn);
        } catch (SQLException e) {
            JdbcUtil.printSQLException(e);
        }
facePainter.setMainContent("clientmanager/manageuserdetails/manageemailusers.xhtml");
    }
//    
////    
 public void saveUser(User user) throws IOException {
  
      AlphaServiceImpl asi = new AlphaServiceImpl();
       
       
        try {
            conn = util.getConnectionTodbSMS();
            user.setSelectedGroup(asi.getGroup(group, conn));
            JdbcUtil.closeConnection(conn);
        } catch (SQLException e) {
            JdbcUtil.closeConnection(conn);
        }
         
        char adminv = '0';
        int resellerBalance=0;
        AlphaScroller ac = new AlphaScroller();
        UserScroller us = new UserScroller();
        UserServiceApi userService = new UserServiceImpl();
        String agent = ac.currentUSer();
        int current =Math.round(us.availableCredits(conn)[0]);
        String getRes = "SELECT admin,max_total from tUSER where username='" + agent + "'";
        try {
            conn = util.getConnectionTodbSMS();
            Statement t = conn.createStatement();
            ResultSet rs = t.executeQuery(getRes);

            while (rs.next()) {
                //Gets the value of the admin forthe current user
                adminv = rs.getString("admin").charAt(0);
                resellerBalance = rs.getInt("max_total");
             
            }
            
            UserServiceApi service = new UserServiceImpl();
            ManageCreditApi creditManager = new ManageCreditImpl();
            user.setEndDate(service.setEndDate());
            user.setStartDate(new Date());

            if (adminv != '1') {
                
                user.setAdmin('3');
            } else {
               user.setAdmin('2');
            }
            
            

            service.persistUser(user, conn); //saving to tUser table
            SMSCredits credits = new SMSCredits();
            credits.setActionTime(new Date());
            credits.setActionType(adminv=='1'?'1':'3');
            credits.setNumCredits(resellerBalance<smsCredits && adminv!='1'? 0:smsCredits);
            credits.setUsername(username);
            credits.setPrevious_balance(0);
            credits.setNew_balance(smsCredits);
//            credits.setAgent(agent);
            credits.setAgent_prevbal(resellerBalance);
            
            credits.setAgent_newbal(adminv=='1'? -1:resellerBalance-smsCredits);
            
            creditManager.persistUpdate(credits, conn);//credits.getNumCredits(),0,credits.getNumCredits()); //saving to tManageCredit table

            User newUser = service.loadCustomerByUsername(username, conn);
            if (FacesContext.getCurrentInstance().getExternalContext().getSessionMap().containsKey("userScroller")) {
                ((UserScroller) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("userScroller")).addUserToList(newUser);
            }
            AlphaServiceApi aService = new AlphaServiceImpl();
            if (!alphanumeric.isEmpty()) {
                //get the type of alpa selected and persist it in
                String alphaType=aService.getAlphanumericType(alphanumeric, conn);
                aService.persistAlpha(username, alphanumeric,alphaType,conn);
            }
            Alpha alpha = aService.loadAlphanumericByUsername(username, conn);
            if (FacesContext.getCurrentInstance().getExternalContext().getSessionMap().containsKey("alphaScroller")) {
                ((AlphaScroller) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("alphaScroller")).addAlphaToList(alpha);
            }
//
            JsfUtil.addSuccessMessage("User saved successfully.");
            //Added to manage ccredits 
            userService.updateCredits(username, credits.getNumCredits(), conn);
            user.updateAdminBal();
            clearAll();
            JdbcUtil.closeConnection(conn);
        } catch (SQLException e) {
            JdbcUtil.printSQLException(e);
        }
facePainter.setMainContent("clientmanager/manageuserdetails/showsmsusers.xhtml");
    }
//    end added code

    public void redirect() throws IOException {
        FacesContext.getCurrentInstance().getExternalContext().redirect("showLast.jsf");
    }

    //=======================================================================================
    public void saveReseller() throws IOException {
        try {
            conn = util.getConnectionTodbSMS();
            LOG.info("saveUser");
            UserServiceApi service = new UserServiceImpl();
            ManageCreditApi creditManager = new ManageCreditImpl();
            this.setEndDate(service.setEndDate());
            this.setStartDate(new Date());

            this.setAdmin('5');
            this.setReseller(true);

            service.persistUserAgent(this, conn);
           // System.out.println("new created reseller user: " + this.username);
            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("newReseller", this.username);

            SMSCredits credits = new SMSCredits();
            credits.setActionTime(new Date());
            credits.setActionType('1');
            credits.setNumCredits(smsCredits);
            credits.setUsername(username);
            credits.setPrevious_balance(0);
            credits.setNew_balance(smsCredits);
            creditManager.persistUpdate(credits, conn);
            User newUser = service.loadCustomerByUsername(username, conn);
            if (FacesContext.getCurrentInstance().getExternalContext().getSessionMap().containsKey("userScroller")) {
                ((UserScroller) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("userScroller")).addUserToList(newUser);
            }
            AlphaServiceApi aService = new AlphaServiceImpl();
            String alpatype= aService.getAlphanumericType(message, conn);
            aService.persistAlpha(username, alphanumeric,alpatype, conn);
            Alpha alpha = aService.loadAlphanumericByUsername(username, conn);
//            if (FacesContext.getCurrentInstance().getExternalContext().getSessionMap().containsKey("alphaScroller")) {
//                ((AlphaScroller) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("alphaScroller")).addAlphaToList(alpha);
//            }
            JsfUtil.addSuccessMessage("Reseller saved successfully.");
            // FacesContext.getCurrentInstance().getExternalContext().redirect("viewUserperAgent.jsf");

            clearAll();
//           FacesContext facesContext = FacesContext.getCurrentInstance();
//            facesContext.getApplication().getNavigationHandler().handleNavigation(facesContext,null, "viewUserperAgent.jsf");
            //FacesContext.getCurrentInstance().getExternalContext().redirect("viewNewReseller.jsf");
            JdbcUtil.closeConnection(conn);
        } catch (SQLException e) {
            JdbcUtil.printSQLException(e);
        }
facePainter.setMainContent("clientmanager/managereseller/managereseller.xhtml");
    }
//=======================================================================================

    public void updateUser() {

        try {
            conn = util.getConnectionTodbSMS();
            LOG.info("updateUser");
            UserServiceApi service = new UserServiceImpl();
            AlphaServiceApi alphaService = new AlphaServiceImpl();
            this.setEndDate(service.setEndDate());
            this.setStartDate(new Date());
            this.setAdmin('2');

            alphaService.updateAlphaByUsername(previousUsername, username, conn);
            service.updateUser(this, conn);
            JsfUtil.addSuccessMessage("User info updated succssfully.");
            JdbcUtil.closeConnection(conn);
        } catch (SQLException e) {
            JdbcUtil.printSQLException(e);
        }
    }

    public void fullProfile() {
        try {
            conn = util.getConnectionTodbSMS();
            UserServiceApi userService = new UserServiceImpl();
            User aUser = userService.loadCustomerByUsername(username, conn);
            this.id = aUser.getId();
            this.username = aUser.getUsername();
            this.userEmail = aUser.getUserEmail();
            this.userMobile = aUser.getUserMobile();
            this.password = aUser.getPassword();
            this.smsCredits = aUser.getSmsCredits();
            this.organization = aUser.getOrganization();
            JdbcUtil.closeConnection(conn);
        } catch (SQLException e) {
            JdbcUtil.printSQLException(e);
        }

    }

    public void generateXSL() {
        try {
            conn = util.getConnectionTodbSMS();
            LOG.info("generateXSL");
            UserServiceApi service = new UserServiceImpl();
            service.generateXSL(conn);
            JdbcUtil.closeConnection(conn);
        } catch (SQLException e) {
            JdbcUtil.printSQLException(e);
        }
    }

    public void clearAll() {

        this.username = "";
        this.userMobile = "";
        this.userEmail = "";
        this.smsCredits = 0;
        this.password = "";
        this.organization = "";
        this.alphanumeric = "";
        this.enableEmailAlertWhenCreditOver = false;
        this.alertThreshold = 100;

    }
    UserServiceImpl us = new UserServiceImpl();
    AuthenticationBean ab = new AuthenticationBean();

    public String driftName() throws SQLException {
        String drift = us.getDrift();
        //System.out.println(drift);
        return drift;
    }

    public String logo() {
        String logolocation = us.getPicLocation();
        //System.out.println(logolocation);
        return logolocation;
    }

    public String visual() {
        String visual = us.getVisual();
        //System.out.println(visual);
        return visual;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }
       
    
}
