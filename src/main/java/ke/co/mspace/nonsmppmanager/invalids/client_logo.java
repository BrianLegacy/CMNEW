/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ke.co.mspace.nonsmppmanager.invalids;

import ke.co.mspace.nonsmppmanager.invalids.Tclient;
//import static org.mspace.clientmanager.util.HibernateUtil2.getSessionFactory;
import org.mspace.clientmanager.util.getsession;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import ke.co.mspace.nonsmppmanager.util.JdbcUtil;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author mspace
 */
public class client_logo implements Serializable {
    private Logger logger=LoggerFactory.getLogger(client_logo.class);
    private String logopath;
    private Tclient tclient;
    private String userType = "show";
    private String footer;
      private final JdbcUtil util=new JdbcUtil();


    public void init() {
        Tclient k = new Tclient();
    }

    public Tclient getTclient() {
        return tclient;
    }

    public void setTclient(Tclient tclient) {
        this.tclient = tclient;
    }

    public String getLogopath() {
        return logopath;
    }

    public void setLogopath(String logopath) {
        this.logopath = logopath;
    }

    public String getUserType() {
        return userType;
    }

    public String getFooter() {
        return footer;
    }

    public void setFooter(String footer) {
        this.footer = footer;
    }

    public Tclient getSystemType() {
        tclient=new Tclient();
        Connection conn = null;
        boolean result = false;
        try {
            conn = util.getConnectionTodbTask();
            String sql = "select * from tClient";
            System.out.println("testing type");
            PreparedStatement pst = conn.prepareStatement(sql);
          
            final ResultSet rs = pst.executeQuery();
            if(rs.next()){
                
            tclient.setId(rs.getInt("id"));
             tclient.setClientName(rs.getString("clientName"));
               tclient.setPicPath(rs.getString("picPath"));
               tclient.setEmail(rs.getString("email"));
               tclient.setSystemType(rs.getString("systemType"));
              logopath = tclient.getPicPath();
            String lsystem_type = tclient.getSystemType();
            userType = lsystem_type.equalsIgnoreCase("integrated") ? "none" : "show";
            }
            JdbcUtil.closeConnection(conn);

        } catch (SQLException e) {
              logopath = "../files/config/MSpacelogo.png";
//            e.printStackTrace();
            JdbcUtil.closeConnection(conn);
        }
        return tclient;
    }

    public String getLink() {
        Tclient client = getSystemType();
        System.out.println("system getLink "+(client.getSystemType().equalsIgnoreCase("integrated") ? "#" : "https://www.mspace.co.ke"));
        return client.getSystemType().equalsIgnoreCase("integrated") ? "#" : "https://www.mspace.co.ke";
    }

    public String getClientName() {
        Tclient client = getSystemType();
        return client.getClientName();
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
        Session session1 = null;
        
        
         if((getsession.getSession().getAttribute("temporaladmin"))!=null ){
       int adminKind= Character.getNumericValue((char) getsession.getSession().getAttribute("temporaladmin") );
             String agent= (String) getsession.getSession().getAttribute("agent") ;
             if(!agent.isEmpty()){
                  return (String)  getsession.getSession().getAttribute("logopath2");
             }
       if(adminKind==3){
           return (String)  getsession.getSession().getAttribute("logopath2");
       }
         }
//        if from  is resseler get path from session
         Connection conn = null;
        boolean result = false;
        try {
            conn = util.getConnectionTodbTask();
            String sql = "select * from tClient";

            PreparedStatement pst = conn.prepareStatement(sql);
          
            final ResultSet rs = pst.executeQuery();
            if(rs.next()){
                tclient=new Tclient();
            tclient.setId(rs.getInt("id"));
             tclient.setClientName(rs.getString("clientName"));
               tclient.setPicPath(rs.getString("picPath"));
               tclient.setEmail(rs.getString("email"));
               tclient.setSystemType(rs.getString("systemType"));
              logopath = tclient.getPicPath();
            String lsystem_type = tclient.getSystemType();
            userType = lsystem_type.equalsIgnoreCase("integrated") ? "none" : "show";
            }
            JdbcUtil.closeConnection(conn);

        } catch (SQLException e) {
              logopath = "../files/config/MSpacelogo.png";
//            e.printStackTrace();
            JdbcUtil.closeConnection(conn);
        }
      
        return logopath;
    }
    public String clnt_logo_mod() {
        Session session1 = null;
        
//        
//         if((getsession.getSession().getAttribute("temporaladmin"))!=null ){
//       int adminKind= Character.getNumericValue((char) getsession.getSession().getAttribute("temporaladmin") );
//             String agent= (String) getsession.getSession().getAttribute("agent") ;
//             if(!agent.isEmpty()){
//                  return (String)  getsession.getSession().getAttribute("logopath2");
//             }
//       if(adminKind==3){
//           return (String)  getsession.getSession().getAttribute("logopath2");
//       }
//         }
//        if from  is resseler get path from session
         Connection conn = null;
        boolean result = false;
        try {
            conn = util.getConnectionTodbTask();
            String sql = "select * from tClient";

            PreparedStatement pst = conn.prepareStatement(sql);
          
            final ResultSet rs = pst.executeQuery();
            if(rs.next()){
                tclient=new Tclient();
            tclient.setId(rs.getInt("id"));
             tclient.setClientName(rs.getString("clientName"));
               tclient.setPicPath(rs.getString("picPath"));
               tclient.setEmail(rs.getString("email"));
               tclient.setSystemType(rs.getString("systemType"));
              logopath = tclient.getPicPath();
            String lsystem_type = tclient.getSystemType();
            userType = lsystem_type.equalsIgnoreCase("integrated") ? "none" : "show";
            }
            JdbcUtil.closeConnection(conn);

        } catch (SQLException e) {
              logopath = "../files/config/MSpacelogo.png";
//            e.printStackTrace();
            JdbcUtil.closeConnection(conn);
        }
      
        return logopath;
    }

    public String clnt_logo2(int id) {
        
       

        //String smsserver = "http://dlr.mspace.co.ke:8080";
//        String smsserver = "https://mspace.co.ke";
        ///String smsserver = "http://smsgateway.mspace.co.ke:8080";
        Session session1 = null;
        String reseller_logopath = "";
          Connection conn = null;
        boolean result = false;
        try {
            conn = util.getConnectionTodbTask();
            String sql = "select * from tClient where id ="+id;

            PreparedStatement pst = conn.prepareStatement(sql);
          
            final ResultSet rs = pst.executeQuery();
//            pst.setLong(1, id);
            if(rs.next()){
                tclient=new Tclient();
            tclient.setId(rs.getInt("id"));
             tclient.setClientName(rs.getString("clientName"));
               tclient.setPicPath(rs.getString("picPath"));
               tclient.setEmail(rs.getString("email"));
               tclient.setSystemType(rs.getString("systemType"));
              logopath = tclient.getPicPath();
     String str = tclient.getPicPath();
                String newstr = str.substring(2);

//                reseller_logopath = smsserver.concat(newstr);
                                reseller_logopath = str;

            } else {
                reseller_logopath = clnt_logo();
            }

            JdbcUtil.closeConnection(conn);

        } catch (SQLException e) {
            e.printStackTrace();
            JdbcUtil.closeConnection(conn);
        }
        
        return reseller_logopath;

    }
}
