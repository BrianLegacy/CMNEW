package org.mspace.clientmanager.credits.services;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.context.FacesContext;
import static ke.co.mspace.nonsmppmanager.model.AuthenticationBean.AUTH_KEY;
import ke.co.mspace.nonsmppmanager.model.EmailCredits;

import org.mspace.clientmanager.credits.model.SMSCredits;
import ke.co.mspace.nonsmppmanager.service.AlphaScroller;
import ke.co.mspace.nonsmppmanager.util.JdbcUtil;

/**
 *
 * @author Norrey Osako
 */
public class ManageCreditImpl implements ManageCreditApi {

    JdbcUtil util = new JdbcUtil();
    Connection conn = null;
    private static final Logger LOG = Logger.getLogger(ManageCreditImpl.class.getName());

    public ManageCreditImpl() {

    }
    //======================================================================================================================

    public char admiValue() {
        char adminv = '0';
        try {
            AlphaScroller ac = new AlphaScroller();
            String agent = ac.currentUSer();
            String getRes = "SELECT admin from tUSER where username='" + agent + "'";

            conn = util.getConnectionTodbSMS();
            Statement t = conn.createStatement();
            ResultSet rs = t.executeQuery(getRes);
            while (rs.next()) {
                adminv = rs.getString("admin").charAt(0);
                System.out.println("THE ADMIN VALUE IS  :" + adminv);
            }
        } catch (SQLException ex) {
            Logger.getLogger(ManageCreditImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        return adminv;
    }

    @Override
    public void persistUpdate2(SMSCredits smsCredits, Connection conn, int creditsToManage, int prevBal, int newBal) {
        System.out.println("Credits to Manage: " + creditsToManage + "  Previous Balance: " + prevBal + "  New Balance:" + newBal);
        String credit_type = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("credit_type", "add");
        String user = FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get(AUTH_KEY).toString();
        char adminv = this.admiValue();
        try {

            //--------------------------------
            String sql2 = "INSERT INTO tManageCredits(username, actionType, actionTime"
                    + ", numCredits, previous_balance, new_balance,user,topupMode)"
                    + " VALUES (?, ?, now(), ?, ?, ?,?,?)";
            System.out.println("Executing " + sql2);
            LOG.info("persistUpdate");
            System.out.println("Email user updated");

            PreparedStatement pstmt2 = conn.prepareStatement(sql2);
            // Bind values to the parameters
            pstmt2.setString(1, user);
            System.out.println("user "+user);
            pstmt2.setString(2, String.valueOf(smsCredits.isActionType()));
            System.out.println("actionType "+ String.valueOf(smsCredits.isActionType()));
            
            
            pstmt2.setInt(3, credit_type.equals("add") 
                    ? -creditsToManage : creditsToManage);
            
            System.out.println("numcredits "+(credit_type.equals("add") 
                    ? -creditsToManage : creditsToManage));
            
            
            
            
            
            
            
            
            pstmt2.setInt(4, prevBal);
            System.out.println("prevbal "+prevBal);
            pstmt2.setInt(5, adminv == '1' ? -1 : newBal);
            System.out.println("adminv "+(adminv == '1' 
                    ? -1 : newBal));
            //pstmt.setString(6,adminv==1?user:user);
            pstmt2.setString(6, user);
             System.out.println("user "+user);
            pstmt2.setString(7, "ClientManager");

            // Execute the query
            pstmt2.execute();
            System.out.println("Testing update of SMS credits");
            //-------------------------------------------
            String sql = "INSERT INTO tManageCredits(username, actionType, actionTime, numCredits, previous_balance, new_balance,user,topupMode) VALUES (?, ?, now(), ?, ?, ?,?,?)";
            String reseller = "INSERT INTO tManageCredits(username, actionType, actionTime,"
                    + " numCredits, previous_balance, new_balance,user ) VALUES (?, ?, now(), ?, ?, ?," + user + ")";
            System.out.println("Executing " + sql);
            LOG.info("persistUpdate");
            ////////////////////////////////////////////////////////
            //Inserting values
            ////////////////////////////////////////////////////////
            PreparedStatement pstmt = conn.prepareStatement(sql);
            
            System.out.println("Second Persistin first persist");
            // Bind values to the parameters
            pstmt.setString(1, smsCredits.getUsername());
            System.out.println("username "+smsCredits.getUsername());
            pstmt.setString(2, String.valueOf(smsCredits.isActionType()));
              System.out.println("actiontype "+String.valueOf(smsCredits.isActionType()));
            pstmt.setInt(3, credit_type.equals("add") ? smsCredits.getNumCredits() : -smsCredits.getNumCredits());
            System.out.println("numcredits "+(credit_type.equals("add") ? smsCredits.getNumCredits() : -smsCredits.getNumCredits()));
            pstmt.setInt(4, smsCredits.getPrevious_balance());
            System.out.println("prevbal "+smsCredits.getPrevious_balance());
            pstmt.setInt(5, smsCredits.getNew_balance());
            System.out.println("new balance "+smsCredits.getNew_balance());
            //pstmt.setString(6,adminv==1?user:user);
            pstmt.setString(6, user);
            System.out.println("user "+user);
            pstmt.setString(7, "ClientManager");

            // Execute the query
            boolean count = pstmt.execute();
 System.out.println("//Second Persistin first persist");
  System.out.println("Third Persistin first persist");
            //--------------------------
            String updateAlerted = "update tUSER SET alerted='0' WHERE username=?";

            PreparedStatement pstmt3 = conn.prepareStatement(updateAlerted);

            pstmt3.setString(1, smsCredits.getUsername());
            System.out.println(pstmt3);

            //int count2 =
            pstmt3.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(ManageCreditImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void persistUpdate(SMSCredits smsCredits, Connection conn) {
        // System.out.println("Credits to Mannage: "+creditsToManage+"  Previous Balance: "+prevBal+"  New Balance:"+newBal);
        String credit_type = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("credit_type", "add");

        char adminv = this.admiValue();
        try {
            
            String user = FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get(AUTH_KEY).toString();
            String sql = "INSERT INTO tManageCredits(username, actionType, actionTime, numCredits, previous_balance, new_balance,user,topupMode) VALUES (?, ?, now(), ?, ?, ?,?,?)";
            String reseller = "INSERT INTO tManageCredits(username, actionType, actionTime, numCredits, previous_balance, new_balance,user ) VALUES (?, ?, now(), ?, ?, ?," + user + ")";
            System.out.println("Executing " + sql);
            LOG.info("persistUpdate");
            ////////////////////////////////////////////////////////
            //Inserting values
            ////////////////////////////////////////////////////////
            //-----------------------------------------------
            
            PreparedStatement pstmt1 = conn.prepareStatement(sql);
            // Bind values to the parameters
            pstmt1.setString(1, smsCredits.getAgent());
            pstmt1.setString(2, String.valueOf(smsCredits.isActionType()));
            pstmt1.setInt(3, -smsCredits.getNumCredits());
            pstmt1.setInt(4, smsCredits.getAgent_prevbal());
            pstmt1.setInt(5, smsCredits.getAgent_newbal());
            //pstmt.setString(6,adminv==1?user:user);
            pstmt1.setString(6, user);
            pstmt1.setString(7,  "ClientManager");
            boolean count1 = pstmt1.execute();
            PreparedStatement pstmt = conn.prepareStatement(sql);
            // Bind values to the parameters
            pstmt.setString(1, smsCredits.getUsername());
            pstmt.setString(2, String.valueOf(smsCredits.isActionType()));
            pstmt.setInt(3, smsCredits.getNumCredits());
            pstmt.setInt(4, smsCredits.getPrevious_balance());
            pstmt.setInt(5, smsCredits.getNew_balance());
            //pstmt.setString(6,adminv==1?user:user);
            pstmt.setString(6, user);
            pstmt.setString(7, "ClientManager" );

            // Execute the query
            boolean count = pstmt.execute();

            String updateAlerted = "update tUSER SET alerted='0' WHERE username=?";

            PreparedStatement pstmt3 = conn.prepareStatement(updateAlerted);

            pstmt3.setString(1, smsCredits.getUsername());

            //int count2 =
            pstmt3.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(ManageCreditImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void persistUpdate(EmailCredits credits, Connection conn) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void persistUpdate(int emailCredits, Connection conn, int creditsToManage, int previous_balance2, int i) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
