/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ke.co.mspace.nonsmppmanager.invalids;

import org.mspace.clientmanager.util.getsession;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import ke.co.mspace.nonsmppmanager.util.HikariJDBCDataSource;
import org.hibernate.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author mspace
 */
public class client_logo implements Serializable {

    private Logger logger = LoggerFactory.getLogger(client_logo.class);
    private String logopath;
    private Tclient tclient;
    private String userType = "show";
    private String footer;
//    private final JdbcUtil util = new JdbcUtil();

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
        tclient = new Tclient();
        Connection conn = null;
        boolean result = false;
        try {
            System.out.println("inside getSystemType before establishing connection todbTask");
            conn = HikariJDBCDataSource.getConnectionTodbTask();
            
            System.out.println("client_logo connection: " + conn);
            System.out.println("inside getSystemType after establishing connection todbTask");
            String sql = "select * from dbTASK.tClient";
            System.out.println("testing type");
            PreparedStatement pst = conn.prepareStatement(sql);

            final ResultSet rs = pst.executeQuery();
            if (rs.next()) {

                System.out.println("tClient data found!");
                tclient.setId(rs.getInt("id"));
                tclient.setClientName(rs.getString("clientName"));
                tclient.setPicPath(rs.getString("picPath"));
                tclient.setEmail(rs.getString("email"));
                tclient.setSystemType(rs.getString("systemType"));
                logopath = tclient.getPicPath();
                String lsystem_type = tclient.getSystemType();
                userType = lsystem_type.equalsIgnoreCase("integrated") ? "none" : "show";
            }else{
                System.out.println("client_logo data not found!");
            }
            
            conn.close();

        } catch (SQLException e) {
            logopath = "../files/config/MSpacelogo.png";
            System.out.println("An exception occured while trying to connect to dbTask " + e);
//            e.printStackTrace();
//uncomment create conn in dbtask connection
        }
        return tclient;
    }

    public String getLink() {
        Tclient client = getSystemType();
        System.out.println("system getLink " + (client.getSystemType().equalsIgnoreCase("integrated") ? "#" : "https://www.mspace.co.ke"));
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

        if ((getsession.getSession().getAttribute("temporaladmin")) != null) {
            int adminKind = Character.getNumericValue((char) getsession.getSession().getAttribute("temporaladmin"));
            String agent = (String) getsession.getSession().getAttribute("agent");
            if (!agent.isEmpty()) {
                return (String) getsession.getSession().getAttribute("logopath2");
            }
            if (adminKind == 3) {
                return (String) getsession.getSession().getAttribute("logopath2");
            }
        }
//        if from  is resseler get path from session
        Connection conn = null;
        boolean result = false;
        try {
            conn = HikariJDBCDataSource.getConnectionTodbTask();
            String sql = "select * from tClient";

            PreparedStatement pst = conn.prepareStatement(sql);

            final ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                tclient = new Tclient();
                tclient.setId(rs.getInt("id"));
                tclient.setClientName(rs.getString("clientName"));
                tclient.setPicPath(rs.getString("picPath"));
                tclient.setEmail(rs.getString("email"));
                tclient.setSystemType(rs.getString("systemType"));
                logopath = tclient.getPicPath();
                String lsystem_type = tclient.getSystemType();
                userType = lsystem_type.equalsIgnoreCase("integrated") ? "none" : "show";
            }

            conn.close();
        } catch (SQLException e) {
            logopath = "../files/config/MSpacelogo.png";
//            e.printStackTrace();

        }

        return logopath;
    }

    public String clnt_logo_mod() {
        Session session1 = null;
        Connection conn = null;
        boolean result = false;
        try {
            conn = HikariJDBCDataSource.getConnectionTodbTask();
            String sql = "select * from tClient";

            PreparedStatement pst = conn.prepareStatement(sql);

            final ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                tclient = new Tclient();
                tclient.setId(rs.getInt("id"));
                tclient.setClientName(rs.getString("clientName"));
                tclient.setPicPath(rs.getString("picPath"));
                tclient.setEmail(rs.getString("email"));
                tclient.setSystemType(rs.getString("systemType"));
                logopath = tclient.getPicPath();
                String lsystem_type = tclient.getSystemType();
                userType = lsystem_type.equalsIgnoreCase("integrated") ? "none" : "show";
            }
            conn.close();

        } catch (SQLException e) {
            logopath = "../files/config/MSpacelogo.png";
//            e.printStackTrace();
        }

        return logopath;
    }

    public String clnt_logo2(String username) {

        //String smsserver = "http://dlr.mspace.co.ke:8080";
//        String smsserver = "https://mspace.co.ke";
        ///String smsserver = "http://smsgateway.mspace.co.ke:8080";
        String reseller_logopath = "";
        String sql = "SELECT * FROM dbTASK.tClient WHERE clientName = ?";

        try (Connection conn = HikariJDBCDataSource.getConnectionTodbTask(); PreparedStatement pst = conn.prepareStatement(sql)) {

            pst.setString(1, username);
            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    Tclient tclient = new Tclient();
                    tclient.setId(rs.getInt("id"));
                    tclient.setClientName(rs.getString("clientName"));
                    tclient.setPicPath(rs.getString("picPath"));
                    tclient.setEmail(rs.getString("email"));
                    tclient.setSystemType(rs.getString("systemType"));

                    String str = tclient.getPicPath();
                    reseller_logopath = str; // Adjust this if needed
                } else {
                    reseller_logopath = clnt_logo();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return reseller_logopath;
    }
}
