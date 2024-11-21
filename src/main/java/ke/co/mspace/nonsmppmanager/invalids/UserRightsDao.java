/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ke.co.mspace.nonsmppmanager.invalids;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import ke.co.mspace.nonsmppmanager.invalids.TuserRights;
import org.mspace.clientmanager.util.getsession;
import java.util.List;
import javax.servlet.http.HttpSession;
import ke.co.mspace.nonsmppmanager.util.HikariJDBCDataSource;
import ke.co.mspace.nonsmppmanager.util.JdbcUtil;
import org.hibernate.Session;

/**
 *
 * @author George Kibira
 */
public class UserRightsDao {
    public UserRightsDao() {

    }

    public TuserRights getUser() {
        HttpSession sessionm = getsession.getSession();
         
        long id = (long) sessionm.getAttribute("id");
        
        TuserRights u = new TuserRights();
       System.out.println("Executing tUSERIGHTS: where id = "+(int)id);
        Connection conn = null;
        boolean result = false;
        try {
            conn = HikariJDBCDataSource.getConnectionTodbSMS();
            String sql = "select * from tUSERRIGHTS where id =?";

            PreparedStatement pst = conn.prepareStatement(sql);
            pst.setLong(1,id);
            final ResultSet rs = pst.executeQuery();
             if (rs.next()) {
               u.setId(rs.getInt("id"));
               u.setNewdept(rs.getString("dept").charAt(0));
               u.setDept(rs.getString("dept").charAt(0));
               u.setNewmsgcat(rs.getString("newmsgcat").charAt(0));
               u.setMsgcats(rs.getString("msgcats").charAt(0));
               
            }else{
            }

            JdbcUtil.closeConnection(conn);

        } catch (SQLException e) {
            e.printStackTrace();
            JdbcUtil.closeConnection(conn);
        }
        return u;
       
    }

}
