/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ke.co.mspace.nonsmppmanager.invalids;

//import com.mspace1.model.TmanageCredits;
import org.mspace.clientmanager.util.getsession;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import javax.enterprise.context.SessionScoped;
import javax.faces.bean.ManagedBean;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import ke.co.mspace.nonsmppmanager.util.JdbcUtil;
import org.hibernate.HibernateException;
import org.hibernate.Session;

/**
 *
 * @author mspace
 */

@ManagedBean(name = "logo_client")
@SessionScoped
public class logo_client implements Serializable {

    HttpSession session = getsession.getSession();
    client_logo clnt = new client_logo();
    JdbcUtil jdbcUtil=new JdbcUtil();

    private String linkLabel;
    private String link;

    private String logopath;
    private String a;
    private String user_type;

    private String clientName;
    private String clientContact;

    public String getLogopath() {
        return logopath;
    }

    public void setLogopath(String logopath) {
        this.logopath = logopath;
    }

    public String getA() {
        return a;
    }

    public void setA(String a) {
        this.a = a;
    }

    public String getUrlLink() {
        String ret = clnt.getLink();
        session.setAttribute("urlLink", ret);
        session.setAttribute("clientname", clnt.getClientName());
        return ret;
    }

    public String getClientName(){
        int reselId = (int) session.getAttribute("resellerId");
        clientName = reselId > 0 ? getResellerName(reselId) : "MSpace";
        link = reselId != 0 ? "" : "https://www.mspace.co.ke";
        session.setAttribute("urlLink", link);
        session.setAttribute("clientname", clientName);
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public String getClientContact(){
        int reselId = (int) session.getAttribute("resellerId");
        clientContact = reselId > 0 ? getResellerContact(reselId) : "0722 962 934";
        return clientContact;
    }

    public void setClientContact(String clientContact) {
        this.clientContact = clientContact;
    }

    /**
     * Creates a new instance of logo_client
     */
    /**
     * Creates a new instance of logo_client
     *
     * @return
     */
    public String clnt_logo() {

        session.setAttribute("resellerId", 0);

        logopath = clnt.clnt_logo();
        a = logopath;

        if (a == null || a.isEmpty()) {
            a = "../files/config/MSpacelogo.png";
        }

        session.setAttribute("logopath", a);

        return a;
    }

    public String getLinkLabel() {

        return linkLabel;
    }

    public void setLinkLabel(String linkLabel) {
        this.linkLabel = linkLabel;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    private int resellerId;

    public void setResellerId(int resellerId) {
        this.resellerId = resellerId;
    }

    public int getResellerId() {
        return this.resellerId;
    }

    public String clnt_logo2() {
        int id;
        try {
            HttpServletRequest request = getsession.getrequest();
            id = request != null ? Integer.valueOf(request.getParameter("id")) : 0;
            session.setAttribute("resellerId", id);

        } catch (NumberFormatException e) {
            id = (int) session.getAttribute("resellerId");
        }

        resellerId = id;

        session.setAttribute("resellerId", id);
//        clientName = getResellerName(id);

        logopath = clnt.clnt_logo2(id);
        a = logopath;

        if (a == null || a.isEmpty()) {

            a = "../files/config/MSpacelogo1.png";
        }

        session.setAttribute("logopath", a);
         session.setAttribute("logopath2", a);

        return a;
    }

    public String current_yr() {

        int reselId = 0;
        if(session.getAttribute("agent")!=null && !((String) session.getAttribute("agent")).equalsIgnoreCase("") ){
            reselId=Integer.parseInt((String) session.getAttribute("agent"));
        }
        linkLabel = reselId != 0 ? "Licenced to " + getResellerName(reselId) + "" : "MSpace Solutions Ltd. ";

        link = reselId != 0 ? "" : "https://www.mspace.co.ke";
        // session.setAttribute("urlLink", link);
        return new SimpleDateFormat("yyyy").format(new Date());
    }
       public String current_yr2() {

        int reselId = (int) session.getAttribute("resellerId");

        linkLabel = (int) session.getAttribute("resellerId") != 0 ? "Licenced to " + getResellerName(reselId) + "" : "MSpace Solutions Ltd. ";

        link = (int) session.getAttribute("resellerId") != 0 ? "" : "https://www.mspace.co.ke";
      
        return new SimpleDateFormat("yyyy").format(new Date());
    }

    public String getUser_type() {
        user_type = clnt.getUserType();
        return user_type;
    }

    public int loginPos() {
        return getUser_type().equals("none") ? 65 : 230;
    }

    private String getResellerName(int id) {
         Connection conn = null;
        boolean result = false;
        String lclientName="";
        try {
            conn = jdbcUtil.getConnectionTodbTask();
            String sql = "select * from tClient where id =?";

            PreparedStatement pst = conn.prepareStatement(sql);
            pst.setLong(1, id);
            final ResultSet rs = pst.executeQuery();
            if(rs.next()){
                lclientName = rs.getString("clientName");
            }

            JdbcUtil.closeConnection(conn);

        } catch (SQLException e) {
            e.printStackTrace();
            JdbcUtil.closeConnection(conn);
        }
return lclientName;

    }
//
    private String getResellerContact(long id) {
Connection conn = null;
        boolean result = false;
         String lclientContact="";
        try {
            conn = jdbcUtil.getConnectionTodbSMS();
            String sql = "select contact_number from tUSER wherer id =?";

            PreparedStatement pst = conn.prepareStatement(sql);
            pst.setLong(1, id);
            final ResultSet rs = pst.executeQuery();
           if(rs.next()){
               lclientContact=rs.getString("contact_number");
               
           }
           getsession.getSession().setAttribute("clientContact", lclientContact);
         

            JdbcUtil.closeConnection(conn);

        } catch (SQLException e) {
            
            e.printStackTrace();
            JdbcUtil.closeConnection(conn);
            return "";
        }
          return lclientContact;
       

    }

//    private int getPrev_topup(int reselId) {
//
//        if (reselId > 0) {
//            String sql = "select m.* from tManageCredits m inner join tUSER u on m.username = u.username "
//                    + "where now() between actionTime and date_add(actionTime, interval 1 month) and u.id = " + reselId + "";
//
//            try (Session lsession = org.mspace.clientmanager.util.HibernateUtil.getSessionFactory().openSession()) {
//                lsession.beginTransaction();
//                List<TmanageCredits> credList = lsession.createNativeQuery(sql, TmanageCredits.class).list();
//
//                if (credList.size() > 0) {
//                    return credList.get(0).getPreviousBalance();
//                }
//
//            } catch (HibernateException e) {
//                e.printStackTrace();
//                return 0;
//            }
//        }
//        return 0;
//    }
}
