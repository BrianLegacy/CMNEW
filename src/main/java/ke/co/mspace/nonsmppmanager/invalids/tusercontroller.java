/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ke.co.mspace.nonsmppmanager.invalids;

//import com.mspace1.model2.Tadpost;
//import com.mspace1.model2.Tupdatenotifications;

import org.mspace.clientmanager.util.getsession;
import java.io.IOException;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import ke.co.mspace.nonsmppmanager.util.JdbcUtil;
import org.slf4j.LoggerFactory;

/**
 *
 * @author mspace
 */
@ManagedBean(name = "tusercontroller")
@RequestScoped
public class tusercontroller implements Serializable {

    HttpSession session1 = getsession.getSession();
    private Tuser user;
    private String goTo;
    private String accTopUp;
    private String diftChat;
    logo_client km = new logo_client();
    private boolean termsAgreed;
    private String show_survey;
    private String client_name;

    private String username;
    private String password;
    private String passwrdReset;
    org.slf4j.Logger logger =LoggerFactory.getLogger(tusercontroller.class);
      private final JdbcUtil util=new JdbcUtil();

    /**
     * Creates a;; new instance of tusercontroller
     */
    @PostConstruct
    public void init() {
        user = new Tuser();
//        this.getAdPost();
    }

    public String getClient_name() {
        return client_name;
    }

    public void setClient_name(String client_name) {
        this.client_name = client_name;
    }

    public Tuser getUser() {
        return user;
    }

    public void setUser(Tuser user) {
        this.user = user;
    }

    public String getGoTo() {

        Object obj = session1.getAttribute("goto");
        goTo = obj != null ? obj.toString() : "";

        return goTo;
    }

    public String getAccTopUp() {

        Object obj = session1.getAttribute("acctopup");
        accTopUp = obj != null ? obj.toString() : "";

        return accTopUp;
    }

    public void setAccTopUp(String accTopUp) {
        this.accTopUp = accTopUp;
    }

    public String getDiftChat() {

        Object obj = session1.getAttribute("resellerId");

        if (obj != null) {
            diftChat = obj.toString().equals("0") ? "drift.js" : "none.js";
        }

        return "s";
    }

    public void setDiftChat(String diftChat) {
//        this.diftChat = diftChat;
    }

//    public String getShow_survey() {
//        show_survey = survey_opt();
//        return show_survey;
//    }

    public void setShow_survey(String show_survey) {
        this.show_survey = show_survey;
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

    public String getPasswrdReset() {
        return passwrdReset;
    }

    public void setPasswrdReset(String passwrdReset) {
        this.passwrdReset = passwrdReset;
    }
    

    public String login() {
        HttpSession httpsession = getsession.getSession();
        if (termsAgreed) {
            tuserinterface dao;
            Tuser result;
            TuserRights trights;
            UserRightsDao urDao = new UserRightsDao();
            String user1;
            dao = new tuserimplementor();
            try {
                HttpSession session = getsession.getSession();
                result = dao.getUserJDBC(this.username, this.password);
              
                if (result != null) {
                    if (result.getContractNum() == null || result.getContractNum().equalsIgnoreCase("activated")) {

                        if (result.getUsername() != null) {
                                    
                            user1 = result.getUsername();
                            session.setAttribute("temporaladmin", result.getAdmin());
                            session.setAttribute("username", user1);
                            session.setAttribute("passwd", result.getPassword());
                            session.setAttribute("accountype", result.getAgent());
                            session.setAttribute("admin", result.getAdmin());
                            session.setAttribute("taskAdmin", result.getTaskadmin());
                            session.setAttribute("id", result.getId());
                            session.setAttribute("user_id", result.getId());
                            System.out.println("setting agent as "+result.getAgent());
                            session.setAttribute("agent", result.getAgent());
                            session.setAttribute("max_total", result.getMaxTotal());
                            session.setAttribute("max_daily", result.getMaxDaily());
                            session.setAttribute("max_weekly", result.getMaxWeekly());
                            session.setAttribute("max_monthly", result.getMaxMonthly());
                            session.setAttribute("short_codes", result.getShortCodes());
                            session.setAttribute("sysUser", user_type());
                            session.setAttribute("ClientName", client_name);
                            session.setAttribute("contact_number", result.getContactNumber());
                             session.setAttribute("resend_failed_sms", result.getResend_failed_sms());
                          
                            session.setAttribute("non.smpp.manager",user1 );
                            session.setAttribute("loggedInUser", user1);


                            
                            
                            
                            
                            
                            
                            
                            
                            
                            
                            
                            try {
                                long counttoday = result.getSmsCountToday();
                                long countweek = result.getSmsCountWeek();
                                long countmonth = result.getSmsCountMonth();
                                long counttotoal = result.getSmsCountTotal();

                                session.setAttribute("counttoday", counttoday);
                                session.setAttribute("countweek", countweek);
                                session.setAttribute("countmonth", countmonth);
                                session.setAttribute("counttotoal", counttotoal);
                            } catch (Exception h) {
                                session.setAttribute("counttoday", 0);
                                session.setAttribute("countweek", 0);
                                session.setAttribute("countmonth", 0);
                                session.setAttribute("counttotoal", 0);

                            }
                            session.setAttribute("organization", result.getOrganization());

                            if (result.getSuperAccountId() == 0 && !result.getAgent().equals("shortcode") && !user_type().equals("integrated")) {

                                session.setAttribute("subaccount", "show");
                                //session.setAttribute("UssdMenu", "none");
                            } else {

                                session.setAttribute("subaccount", "none");
                            }

//                            char adminval = (char) httpsession.getAttribute("admin");
//                            char taskadmin = (char) httpsession.getAttribute("taskAdmin");
                            String systype = (String) httpsession.getAttribute("sysUser");
                            String sysname = (String) httpsession.getAttribute("ClientName");
                            if ((systype.equalsIgnoreCase("intergrated") || systype.equalsIgnoreCase("integrated")) && sysname.equalsIgnoreCase("Sanlam")) {
                                //if value of ussd_report for this user is Y, show the ussed report menu item
                                trights = urDao.getUser();
                                if (trights.getUssdReport() != 'Y') {
                                    session.setAttribute("UssdMenu", "none");
                                }
                                session.setAttribute("salamdatatable", "block");
                                session.setAttribute("webdatatable", "none");
                            } else {
                                session.setAttribute("UssdMenu", "block");
                                session.setAttribute("webdatatable", "block");
                                session.setAttribute("salamdatatable", "none");
                            }

                            String resellerId = (session.getAttribute("resellerId").toString());

                            String agentId = result.getAgent() != null && !result.getAgent().isEmpty()
                                    ? result.getAgent() : "0";

                            if (resellerId.equals("0")) {
                                if (user_type().equalsIgnoreCase("integrated")) {

                                    session.setAttribute("acctopup", "none");
                                    session.setAttribute("goto", "show");
                                } else {
                                    session.setAttribute("acctopup", "show");
                                    session.setAttribute("goto", "none");
                                }

                            } else {
                                setUserPaybill(resellerId);
                                if (session.getAttribute("resellerpaybill").toString().equals("none")) {
                                    session.setAttribute("acctopup", "none");
                                } else {
                                    session.setAttribute("acctopup", "show");
                                }
                                session.setAttribute("goto", "none");
                            }

                        } else {
                            termsAgreed = false;
                            //return "home.jsf";
                            return getHomePage();
                        }
                    } else {

                        FacesMessage lmessage = new FacesMessage("Your account is not activated", "Dear customer, your account is not yet activated. We shall get back to you within 48 hrs. You can also call us on 0722962934 or email us on info@mspace.co.ke");
                        FacesContext.getCurrentInstance().addMessage(null, lmessage);
                        termsAgreed = false;

                        //return activate.jsf;
                        //return "home.jsf";
                        return getHomePage();

                    }
                } else {

                    //return "home.jsf";
                    return getHomePage();
                }

            } catch (Exception nullk) {
            
                return getHomePage();
            }

            
            return "clientmanager.jsf";
        } else {
            FacesMessage msg = new FacesMessage("Check that you have read terms and conditions and agree ", "");
            FacesContext.getCurrentInstance().addMessage(null, msg);
            //return "home.jsf";
            return getHomePage();
        }
    }

    public String login2(String username, String password) {
        km.clnt_logo();
        tuserinterface dao;
        Tuser result;
        String user1;
        dao = new tuserimplementor();
        try {
            HttpSession session = getsession.getSession();
            result = dao.getUser(username, password);
            if (result.getUsername() != null) {
                user1 = result.getUsername();
                session.setAttribute("username", user1);
                session.setAttribute("passwd", result.getPassword());
                session.setAttribute("accountype", result.getAgent());
                session.setAttribute("admin", result.getAdmin());
                session.setAttribute("id", result.getId());
                session.setAttribute("agent", result.getAgent());
                session.setAttribute("max_total", result.getMaxTotal());
                session.setAttribute("max_daily", result.getMaxDaily());
                session.setAttribute("max_weekly", result.getMaxWeekly());
                session.setAttribute("max_monthly", result.getMaxMonthly());
                session.setAttribute("counttoday", result.getSmsCountToday());
                session.setAttribute("countweek", result.getSmsCountWeek());
                session.setAttribute("countmonth", result.getSmsCountMonth());
                session.setAttribute("counttotoal", result.getSmsCountTotal());
                session.setAttribute("alertThreshold", result.getAlertThreshold());//new
                session.setAttribute("alertStatus", result.getEnableEmailAlert());//new
                session.setAttribute("mobile", result.getContactNumber());//new
                session.setAttribute("email_Address", result.getEmailAddress());//new
                session.setAttribute("sysUser", user_type());

               

                session.setAttribute("organization", result.getOrganization());
                if (result.getSuperAccountId() == 0 && !result.getAgent().equals("shortcode") && !user_type().equals("integrated")) {
                    session.setAttribute("subaccount", "show");

                } else {
                    session.setAttribute("subaccount", "none");
                }

                //login redirection 
             

                FacesContext context = FacesContext.getCurrentInstance();
                HttpServletResponse response = (HttpServletResponse) context.getExternalContext().getResponse();
                try {
                    response.sendRedirect("./sms.jsf");
                    return "sms.jsf";
                } catch (IOException ex) {
                }

            } else {
                try {
                    ExternalContext context1 = FacesContext.getCurrentInstance().getExternalContext();
                    context1.redirect("http://smsgateway.mspace.co.ke/sms/");

                } catch (IOException ex) {
                    Logger.getLogger(tusercontroller.class.getName()).log(Level.SEVERE, null, ex);
                }
                return "home.jsf";
            }

        } catch (Exception nullk) {
          nullk.printStackTrace(); 

        }

        return "home.jsf";
    }

    public boolean isTermsAgreed() {
        return termsAgreed;
    }

    public void setTermsAgreed(boolean termsAgreed) {
        this.termsAgreed = termsAgreed;
    }

    public void logout() {

        try {
 HttpSession session = getsession.getSession();
            ExternalContext context1 = FacesContext.getCurrentInstance().getExternalContext();

       int temporaladmin =Character.getNumericValue( (char) session.getAttribute("temporaladmin"));
       String agent =(String) session.getAttribute("agent");
            int id = (int) session.getAttribute("resellerId");
            tuserinterface dao = new tuserimplementor();
            dao.logindetailupdate();

 if (temporaladmin == 3) {
                context1.redirect("reseller.jsf?id=" + agent);
            } else {
                context1.redirect("home.jsf");
            }
            session.invalidate();
        } catch (IOException ex) {
            ex.printStackTrace();
            Logger.getLogger(tusercontroller.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public String user_type() {
        Tclient tclient=new Tclient();
          Connection conn = null;
        boolean result = false;
        String userType="";
        try {
            conn = util.getConnectionTodbTask();
            String sql = "select * from tClient";

            PreparedStatement pst = conn.prepareStatement(sql);
          
            final ResultSet rs = pst.executeQuery();
            if(rs.next()){
               
            tclient.setId(rs.getInt("id"));
             tclient.setClientName(rs.getString("clientName"));
               tclient.setPicPath(rs.getString("picPath"));
               tclient.setEmail(rs.getString("email"));
               tclient.setSystemType(rs.getString("systemType"));
              String sysUser = tclient.getSystemType();
            client_name = tclient.getClientName();
            return sysUser;
            }
            JdbcUtil.closeConnection(conn);

        } catch (SQLException e) {
            e.printStackTrace();
            JdbcUtil.closeConnection(conn);
            return null;
        }
        return "";
    
    }

    private String getHomePage() {
        HttpSession session = getsession.getSession();
        int id = (int) session.getAttribute("resellerId");

        return id != 0 ? "home.jsf" : "home.jsf";
    }

    private void setUserPaybill(String id) {
        Connection conn = null;
        boolean result = false;
        try {
            conn = util.getConnectionTodbPAYMENT();
            String sql = "select * from tUSERPAYBILL  WHERE tUSER_id = " + id + "";

            PreparedStatement pst = conn.prepareStatement(sql);
          
            final ResultSet rs = pst.executeQuery();
             HttpSession httpsession = getsession.getSession();
            if(rs.next()){
                httpsession.setAttribute("resellerpaybill", String.valueOf(rs.getInt("paybill")));
            } else{
                  httpsession.setAttribute("resellerpaybill", "none");
            }
            JdbcUtil.closeConnection(conn);

        } catch (SQLException e) {
            e.printStackTrace();
            JdbcUtil.closeConnection(conn);
        }
    }



    public boolean getResellerRender() {
        boolean ret = true;
        HttpSession session = getsession.getSession();
        int id = (int) session.getAttribute("resellerId");
        if (id != 0) {
            ret = false;
        } else {
            return true;
        }
        return ret;
    }

}
