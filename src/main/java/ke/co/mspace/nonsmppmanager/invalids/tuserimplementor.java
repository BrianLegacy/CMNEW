/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ke.co.mspace.nonsmppmanager.invalids;

import ke.co.mspace.nonsmppmanager.invalids.Tuser;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.mspace.clientmanager.util.getsession;
import org.mspace.clientmanager.security.Encryption;
import org.mspace.clientmanager.security.Sha256Encryption;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ke.co.mspace.nonsmppmanager.util.JdbcUtil;
import ke.co.mspace.nonsmppmanager.util.PasswordUtil;

/**
 *
 * @author mspace
 */
public class tuserimplementor implements tuserinterface {

    private final JdbcUtil util = new JdbcUtil();
    Logger logger = LoggerFactory.getLogger(tuserimplementor.class);
    Encryption sha256Encryption = new Sha256Encryption();
    Calendar calendar = Calendar.getInstance();
    java.sql.Timestamp ourJavaTimestampObject = new java.sql.Timestamp(calendar.getTime().getTime());

    @Override
    public void update(Tuser user) {

    }

    @Override
    public void remove(Tuser user) {

    }

    @Override
    public List<Tuser> list() {
        List lista = null;


        return lista;
    }

    @Override
    public Tuser getbyid(long id) {
        Tuser user = new Tuser();

        return user;
    }

    @Override
    public void saveuser(Tuser user) {
    }

    @Override
    public Tuser getUser(String username, String password) {
        Tuser u = new Tuser();

        return u;
    }

@Override
public Tuser getUserJDBC(String username, String password) {
    System.out.println("getUserJDBC");
    HttpSession lhttpsession = getsession.getSession();
    Map<String, Tuser> sessionMap = new HashMap<>();
    int lresellerId = (Integer) lhttpsession.getAttribute("resellerId");

    String jdbcSql = "SELECT * FROM tUSER WHERE username=? AND (admin =5 OR admin =1)";
    logger.debug("sql " + jdbcSql);
    Tuser u = new Tuser();
    Connection conn = null;
    int encrypted = encryptionLogin();

    try {
        conn = util.getConnectionTodbSMS();
        PreparedStatement pst = conn.prepareStatement(jdbcSql);
        pst.setString(1, username);
        ResultSet rs = pst.executeQuery();

        if (rs.next()) {
            String dbPassword = rs.getString("password");

            boolean passwordMatches = false;
            if (encrypted == 1) {
                passwordMatches = PasswordUtil.match(dbPassword, password);
            } else {
                passwordMatches = dbPassword.equals(password);
            }

            if (passwordMatches) {
                // Populate Tuser
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
                String takadmin = rs.getString("taskadmin");
                u.setSuperAccountId(rs.getInt("super_account_id"));
                u.setMax_contacts(rs.getInt("max_contacts"));
                u.setTaskadmin(takadmin.charAt(0));

                if (lresellerId == 0 && isResellerClient(u.getAgent())) {
                    System.out.println("Reseller client check failed");
                    FacesMessage message = new FacesMessage("Not Successful", " Either your Username or password is invalid");
                    FacesContext.getCurrentInstance().addMessage(null, message);
                    return null;
                }

                String sql = "UPDATE tUSER SET logged_in=1, logged_in_time='" + ourJavaTimestampObject + "' WHERE username=? AND password=?";
                PreparedStatement pst2 = conn.prepareStatement(sql);
                pst2.setString(1, username);
                pst2.setString(2, dbPassword); // Note: should match the stored password
                pst2.execute();
            } else {
                FacesMessage message = new FacesMessage("Not Successful", " Either your Username or password is invalid");
                FacesContext.getCurrentInstance().addMessage(null, message);
                return null;
            }
        } else {
            FacesMessage message = new FacesMessage("Not Successful", " Either your Username or password is invalid");
            FacesContext.getCurrentInstance().addMessage(null, message);
            return null;
        }

        JdbcUtil.closeConnection(conn);

    } catch (Exception m) {
        FacesMessage message = new FacesMessage("Not Successful", " Either your Username or password is invalid");
        FacesContext.getCurrentInstance().addMessage(null, message);

        m.printStackTrace();
        JdbcUtil.closeConnection(conn);
    }
    System.out.println("Returning user");
    return u;
}

private int encryptionLogin() {
    try (Connection con = util.getConnectionTodbTask(); PreparedStatement ps = con.prepareStatement("SELECT encryptedlogin FROM dbTASK.tClient LIMIT 1")) {
        ResultSet rs = ps.executeQuery();
        int result = 0;
        if (rs.next()) {
            result = rs.getInt("encryptedlogin");
        }
        return result;
    } catch (SQLException ex) {
        logger.debug("sql " + ex);
        return 0;
    }
}

    @Override
//       SecurityUtil.verify(this.username, this.password)
    public Tuser getUserWithSha256(String username, String password) {
        Tuser u = new Tuser();

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
            String sql = "update tUSER   set logged_in=0, logged_in_time='" + ourJavaTimestampObject + "' where username =? and id= ?";

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
        boolean result = false;
        try {
            conn = util.getConnectionTodbTask();
            String sql = "select id from tClient  where id != 0";

            PreparedStatement pst = conn.prepareStatement(sql);
            final ResultSet rs = pst.executeQuery();

            while (rs.next()) {
                int _id = rs.getInt("id");
                String id = String.valueOf(_id);
                if (agentId.equals(id)) {
                    result = true;
                }
            }

            JdbcUtil.closeConnection(conn);

        } catch (SQLException e) {
            e.printStackTrace();
            JdbcUtil.closeConnection(conn);
        }
        return result;

    }

}
