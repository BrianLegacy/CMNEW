/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ke.co.mspace.nonsmppmanager.invalids;

import ke.co.mspace.nonsmppmanager.invalids.Tuser;
import ke.co.mspace.nonsmppmanager.invalids.Tclient;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.mspace.clientmanager.util.getsession;
import org.mspace.clientmanager.security.Encryption;
import org.mspace.clientmanager.security.Sha256Encryption;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.servlet.HttpMethodConstraintElement;
import javax.servlet.http.HttpSession;
import ke.co.mspace.nonsmppmanager.util.JdbcUtil;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ke.co.mspace.nonsmppmanager.util.JdbcUtil;

/**
 *
 * @author mspace
 */
public class tuserimplementor implements tuserinterface {
    private final JdbcUtil util=new JdbcUtil();
    Logger logger=LoggerFactory.getLogger(tuserimplementor.class);
 Encryption sha256Encryption=new Sha256Encryption();
    Calendar calendar = Calendar.getInstance();
    java.sql.Timestamp ourJavaTimestampObject = new java.sql.Timestamp(calendar.getTime().getTime());
        

    @Override
    public void update(Tuser user) {
//        Session session = getSessionFactory().openSession();
//        try {
//
//            session.beginTransaction();
//            session.update(user);
//            session.getTransaction().commit();
//        } catch (HibernateException k) {
//            k.printStackTrace();
//        } finally {
//            session.close();
//        }
    }

    @Override
    public void remove(Tuser user) {
//        Session session = getSessionFactory().openSession();
//        try {
//            session.beginTransaction();
//            session.delete(user);
//            session.getTransaction().commit();
//        } catch (HibernateException k) {
//        } finally {
//            session.close();
//        }
    }

    @Override
    public List<Tuser> list() {
        List lista = null;
//        Session session = getSessionFactory().openSession();
//        HttpSession sessionm = getsession.getSession();
//        long id = (long) sessionm.getAttribute("id");
//        String user = (String) sessionm.getAttribute("username");
//        try {
//            session.beginTransaction();
//            lista = session.createQuery("from Tuser u where u.username=:username and u.id=:id")
//                    .setParameter("username", user)
//                    .setParameter("id", id)
//                    .list();
//         
//            session.getTransaction().commit();
//        } catch (HibernateException k) {
//        } finally {
//            session.close();
//        }

        return lista;
    }

    @Override
    public Tuser getbyid(long id) { 
        Tuser user=new Tuser();
//        try (Session session = getSessionFactory().openSession()) {
//            session.beginTransaction();
//            return (Tuser) session.load(Tuser.class, id);
//        } catch (Exception e) {
//            e.printStackTrace();
//            return null;
//        }
return user;
    }

    @Override
    public void saveuser(Tuser user) {
//        Session session = getSessionFactory().openSession();
//        try {
//
//            session.beginTransaction();
//            session.save(user);
//            session.getTransaction().commit();
//        } catch (HibernateException k) {
//        } finally {
//            session.close();
//        }
    }

    @Override
    public Tuser getUser(String username, String password) {
         Tuser u = new Tuser();
//        HttpSession lhttpsession = getsession.getSession();
//        Map<String, Tuser> sessionMap = new HashMap<>();
//        int lresellerId = (Integer) lhttpsession.getAttribute("resellerId");
//        //{ checks whether user is reseller client
//        String hql = "from Tuser s where s.username = :username and s.password = :password and (s.admin = 5 or s.admin=1)";
//        //}
//
//       
//        try (Session lsession = getSessionFactory().openSession()) {
//            lsession.getTransaction().begin();
//
//            //String hql = "from Tuser s where s.+username = :username and s.password = :password";
//            List<Tuser> luser = lsession.createQuery(hql)
//                    .setParameter("username", username)
//                    .setParameter("password", password)
//                    .list();
//            if (luser != null && luser.size() > 0) {
//                lhttpsession.setAttribute("user", luser);
//                u = luser.get(0);
//                if (u != null) {
//                   
//                    if (lresellerId == 0 && isResellerClient(u.getAgent())) {
//                        FacesMessage message = new FacesMessage("Not Succesful", " Either your Username or password is invalid");
//                        FacesContext.getCurrentInstance().addMessage(null, message);
//                        return null;
//                    }
//                    if (lhttpsession.getAttribute("user").equals(luser)) {
//                        lhttpsession.removeAttribute("user");
//
//                    }
//                    String sql = "update Tuser m  set m.loggedIn=1, m.loggedInTime='" + ourJavaTimestampObject + "' where m.username = :username and m.password = :password";
////                ;
//                    Query qry = lsession.createQuery(sql);
//                    qry.setParameter("username", username);
//                    qry.setParameter("password", password);
//
//                    qry.executeUpdate();
//                    lsession.getTransaction().commit();
//                }
//            } else {
//                FacesMessage message = new FacesMessage("Not Succesful", " Either your Username or password is invalid");
//                FacesContext.getCurrentInstance().addMessage(null, message);
//            }
//
//        } catch (Exception m) {
//
//            FacesMessage message = new FacesMessage("Not Succesful", " Either your Username or password is invalid");
//            FacesContext.getCurrentInstance().addMessage(null, message);
//           
//            m.printStackTrace();
//
//        }
        return u;
    }
    @Override
    public Tuser getUserJDBC(String username, String password) {
        System.out.println("getUserJDBC");
        HttpSession lhttpsession = getsession.getSession();
        Map<String, Tuser> sessionMap = new HashMap<>();
        int lresellerId = (Integer) lhttpsession.getAttribute("resellerId");
      
        String jdbcSql="select * from tUSER where username=? and password =? and (admin =5 or admin =1)";
 logger.debug("sql "+ jdbcSql);
        Tuser u = new Tuser();
           Connection conn = null;

            try {
            conn = util.getConnectionTodbSMS();
            PreparedStatement pst = conn.prepareStatement(jdbcSql);
            pst.setString(1, username);
             pst.setString(2, password);
            final ResultSet rs = pst.executeQuery();
            if(rs.next()){
            
          //        populate Tuser
          u.setId(rs.getLong("id"));
          u.setUsername(rs.getString("username"));
          u.setPassword(rs.getString("password"));
          u.setAgent(rs.getString("agent"));
          u.setMaxDaily(rs.getInt("max_daily"));
          u.setMaxWeekly(rs.getInt("max_weekly"));
          u.setMaxMonthly(rs.getInt("max_monthly"));
          u.setMaxTotal(rs.getInt("max_total"));
          u.setMax_total_amt(rs.getInt("max_total_amt"));
          u.setAdmin(rs.getString("admin").charAt(0));
          u.setContractNum(rs.getString("contract_num"));
          u.setContactNumber(rs.getString("contact_number"));
          u.setShortCodes(rs.getString("short_codes"));
          u.setResend_failed_sms(rs.getInt("resend_failed_sms"));
          u.setOrganization(rs.getString("organization"));
          String takadmin=rs.getString("taskadmin");
          u.setSuperAccountId(rs.getInt("super_account_id"));
          
                
          u.setTaskadmin(takadmin.charAt(0));
          
//                          lhttpsession.setAttribute("user", u);
                               if (lresellerId == 0 && isResellerClient(u.getAgent())) {
                        FacesMessage message = new FacesMessage("Not Succesful", " Either your Username or password is invalid");
                        FacesContext.getCurrentInstance().addMessage(null, message);
                        return null;
                    }
//                    if (lhttpsession.getAttribute("user").equals(luser)) {
//                        lhttpsession.removeAttribute("user");
//
//                    }
                    String sql = "update tUSER  set loggedIn=1, loggedInTime='" + ourJavaTimestampObject + "' where username =? and password = ?";
//                ;

  PreparedStatement pst2 = conn.prepareStatement(sql);
            pst.setString(1, username);
             pst.setString(2, password);
             pst.execute();
            } else {
                FacesMessage message = new FacesMessage("Not Succesful", " Either your Username or password is invalid");
                FacesContext.getCurrentInstance().addMessage(null, message);
            }
            
            
//          populate Tuser
            JdbcUtil.closeConnection(conn);

        }  catch (Exception m) {

            FacesMessage message = new FacesMessage("Not Succesful", " Either your Username or password is invalid");
            FacesContext.getCurrentInstance().addMessage(null, message);
           
            m.printStackTrace();
        JdbcUtil.closeConnection(conn);


        }
        return u;
        
    }
       @Override
//       SecurityUtil.verify(this.username, this.password)
    public Tuser getUserWithSha256(String username, String password) {
         Tuser u = new Tuser();
//        HttpSession lhttpsession = getsession.getSession();
//        Map<String, Tuser> sessionMap = new HashMap<>();
//        int lresellerId = (Integer) lhttpsession.getAttribute("resellerId");
//        //{ checks whether user is reseller client
//        String hql = "from Tuser s where s.username = :username and s.password = :password and (s.admin = 5 or s.admin=1)";
//        
//        
//        
//        //}
//
//       
//        try (Session lsession = getSessionFactory().openSession()) {
//            lsession.getTransaction().begin();
//
//            //String hql = "from Tuser s where s.+username = :username and s.password = :password";
//            List<Tuser> luser = lsession.createQuery(hql)
//                    .setParameter("username", username)
////                    .setParameter("password", password)
//                    .list();
//           
//             boolean isAuthenticated=sha256Encryption.compare(luser.get(0).getPassword(),password);
//             if(!isAuthenticated){
//                 
//                
//                        FacesMessage message = new FacesMessage("Not Succesful", " Either your Username or password is invalid");
//                        FacesContext.getCurrentInstance().addMessage(null, message);
//                      
//                        return null;
//             }
//             
//             
//             
//            if (luser != null && luser.size() > 0) {
//                lhttpsession.setAttribute("user", luser);
//                u = luser.get(0);
//                if (u != null) {
//                   
//                    if (lresellerId == 0 && isResellerClient(u.getAgent())) {
//                        FacesMessage message = new FacesMessage("Not Succesful", " Either your Username or password is invalid");
//                        FacesContext.getCurrentInstance().addMessage(null, message);
//                        return null;
//                    }
//                    if (lhttpsession.getAttribute("user").equals(luser)) {
//                        lhttpsession.removeAttribute("user");
//
//                    }
//                    String sql = "update Tuser m  set m.loggedIn=1, m.loggedInTime='" + ourJavaTimestampObject + "' where m.username = :username and m.password = :password";
//
//                    Query qry = lsession.createQuery(sql);
//                    qry.setParameter("username", username);
//                    qry.setParameter("password", password);
//
//                    qry.executeUpdate();
//                    lsession.getTransaction().commit();
//                }
//            } else {
//                FacesMessage message = new FacesMessage("Not Succesful", " Either your Username or password is invalid");
//                FacesContext.getCurrentInstance().addMessage(null, message);
//            }
//
//        } catch (Exception m) {
//
//            FacesMessage message = new FacesMessage("Not Succesful", " Either your Username or password is invalid");
//            FacesContext.getCurrentInstance().addMessage(null, message);
//          
//            m.printStackTrace();
//
//        }
        return u;
    }

    @Override
    public void logindetailupdate() {
        HttpSession sessionm = getsession.getSession();
        long id = (long) sessionm.getAttribute("id");
        String user = (String) sessionm.getAttribute("username");
//        Session session = getSessionFactory().openSession();
                Connection conn = null;
         try {
            conn = util.getConnectionTodbSMS();
            String sql = "update tUSER   set loggedIn=0, loggedInTime='" + ourJavaTimestampObject + "' where username =? and id= ?";

            PreparedStatement pst = conn.prepareStatement(sql);
            pst.setString(1, user);
            pst.setLong(2, id);
            pst.execute();
            JdbcUtil.closeConnection(conn);

        } catch (SQLException e) {
            e.printStackTrace();
            JdbcUtil.closeConnection(conn);
        }
        
    }

    private boolean isResellerClient(String agentId) {

         Connection conn = null;
          boolean result=false;
        try {
            conn = util.getConnectionTodbSMS();
            String sql = "select id from tClient  where id != 0";

            PreparedStatement pst = conn.prepareStatement(sql);
            final ResultSet rs = pst.executeQuery();
           
            while(rs.next()){
                int _id=rs.getInt("id");
                if(((_id==Integer.valueOf(agentId)))) result=true;
            }
            

            JdbcUtil.closeConnection(conn);
            

        } catch (SQLException e) {
            e.printStackTrace();
            JdbcUtil.closeConnection(conn);
        }
        return result;
        
    }

}
