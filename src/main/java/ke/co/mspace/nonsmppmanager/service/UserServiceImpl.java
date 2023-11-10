package ke.co.mspace.nonsmppmanager.service;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import org.mspace.clientmanager.user.UserController;

import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import ke.co.mspace.nonsmppmanager.model.Alpha;
import ke.co.mspace.nonsmppmanager.model.Alpnumeric;
import ke.co.mspace.nonsmppmanager.model.AuthenticationBean;
import ke.co.mspace.nonsmppmanager.model.CallBack;
import ke.co.mspace.nonsmppmanager.model.EmailPricingTable;
import ke.co.mspace.nonsmppmanager.model.EmailUser;
import org.mspace.clientmanager.group.Group;
import ke.co.mspace.nonsmppmanager.model.UserProfile;
import ke.co.mspace.nonsmppmanager.model.creditRecord;
import ke.co.mspace.nonsmppmanager.util.JdbcUtil;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
//import org.richfaces.event.UploadEvent;
//import org.richfaces.model.UploadItem;
import ke.co.mspace.nonsmppmanager.model.Paybill;
import ke.co.mspace.nonsmppmanager.util.JsfUtil;
import ke.co.mspace.nonsmppmanager.util.SessionUtil;

/**
 *
 * @author Norrey Osako
 */
public class UserServiceImpl implements UserServiceApi {

    private static final Logger LOG = Logger.getLogger(UserServiceImpl.class.getName());

    private String selectedUser;

    public String getSelectedUser() {
        return selectedUser;
    }

    public void setSelectedUser(String selectedUser) {
        this.selectedUser = selectedUser;
    }

    public UserServiceImpl() {

    }
    public String names = "";
    private static String reseller = "none", myAccount;
    private static UserProfile loggedInUser;

    @Override
    public List<UserController> getAllUsers(Connection conn, String name) throws SQLException {
        final Map<Integer, String> groupMap = getGroupMap(conn);
       
        names = name;
        UserScroller us = new UserScroller();
        String lname = us.lastInsert();

        String sql = "";

        sql = "SELECT tUSER.id, tUSER.username, tUSER.password, tUSER.admin, tUSER.max_total,tUSER.max_contacts,"
                + "tUSER.organization, tUSER.contact_number, tUSER.email_address, tUSER.enable_email_alert,"
                + "tUSER.end_date, tUSER.start_date, tUSER.alertThreshold,tUSER.cost_per_sms,tUSER.arrears,tUSER.group"
                + "  FROM tUSER where tUSER.username='" + name + "'";
        
      

        List<UserController> result = new ArrayList<>();

        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(sql);

        while (rs.next()) {
            UserController aUser = new UserController();
            aUser.setId(rs.getLong("id"));
            aUser.setUsername(rs.getString("username"));
            aUser.setPassword(rs.getString("password"));
            aUser.setAdmin(rs.getString("admin").charAt(0));

            String ad = "" + aUser.getAdmin();
            if (ad.trim() != null) {
                if (ad.equalsIgnoreCase("4")) {
                    //System.err.println("AD" + ad + ": username :" + aUser.getUsername() + "can create sub accounts");
                    aUser.setUserType("Yes");
                    aUser.setCreateAccount(true);
                } else {
                    //System.err.println("AD" + ad + ": username :" + aUser.getUsername() + "cannot create sub accounts");
                    aUser.setUserType("No");
                    aUser.setCreateAccount(false);
                }
            }

            aUser.setSmsCredits(rs.getInt("max_total"));
            aUser.setOrganization(rs.getString("organization"));
            aUser.setUserMobile(rs.getString("contact_number"));
            aUser.setUserEmail(rs.getString("email_address"));
            aUser.setEnableEmailAlertWhenCreditOver(rs.getBoolean("enable_email_alert"));
            aUser.setEndDate(rs.getDate("end_date"));
            aUser.setStartDate(rs.getDate("start_date"));
            aUser.setAlertThreshold(rs.getInt("alertThreshold"));
            aUser.setCost_per_sms(rs.getFloat("cost_per_sms"));
            aUser.setArrears(rs.getInt("arrears"));
            //aUser.setAlphanumeric(rs.getString("alphanumeric"));
            //aUser.setAlphaId(rs.getLong("alphaId"));
            aUser.setMaxContacts(rs.getInt("max_contacts"));
            
            aUser.setGroup(groupMap.get(rs.getInt("group")));
            result.add(aUser);
        }

        return result;
    }
     public Map<Integer, String> getGroupMap(Connection conn) throws SQLException {

        String sql = "select id ,groupname from dbSMS.tGROUPS";

                 Map<Integer, String> map=new HashMap();
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(sql);

        while (rs.next()) {
 
           map.put(rs.getInt("id"),rs.getString("groupname"));
         
    }  return map;
     }
    

    @Override
    public List<UserController> getLastCreated(Connection conn, String name) throws SQLException {
        UserScroller us = new UserScroller();
        name = us.lastInsert();

        names = name;
        int last = this.getAutoId(conn);

        String sql = "SELECT tUSER.id, tUSER.username, tUSER.password, tUSER.admin, tUSER.max_total,"
                + "tUSER.organization, tUSER.contact_number, tUSER.email_address, tUSER.enable_email_alert,"
                + "tUSER.end_date, tUSER.start_date, tUSER.alertThreshold,tUSER.cost_per_sms,tUSER.arrears,tAllowedAlphanumerics.alphanumeric, tAllowedAlphanumerics.id as alphaId FROM tUSER"
                + " LEFT JOIN tAllowedAlphanumerics on tUSER.username=tAllowedAlphanumerics.username where tUSER.admin != 1 and tUSER.username='" + name + "' and tUSER.id=" + last + "";

        // System.out.println("Name is:::+=======>" + name);
        List<UserController> result = new ArrayList<>();

        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(sql);

        while (rs.next()) {
            UserController aUser = new UserController();
            aUser.setId(rs.getLong("id"));
            aUser.setUsername(rs.getString("username"));
            aUser.setPassword(rs.getString("password"));
            aUser.setAdmin(rs.getString("admin").charAt(0));

            String ad = "" + aUser.getAdmin();
            if (ad.trim() != null) {
                if (ad.equalsIgnoreCase("4")) {
                    //System.err.println("AD" + ad + ": username :" + aUser.getUsername() + "can create sub accounts");
                    aUser.setUserType("Yes");
                    aUser.setCreateAccount(true);
                } else {
                    //System.err.println("AD" + ad + ": username :" + aUser.getUsername() + "cannot create sub accounts");
                    aUser.setUserType("No");
                    aUser.setCreateAccount(false);
                }
            }
            aUser.setSmsCredits(rs.getInt("max_total"));
            aUser.setOrganization(rs.getString("organization"));
            aUser.setUserMobile(rs.getString("contact_number"));
            aUser.setUserEmail(rs.getString("email_address"));
            aUser.setEnableEmailAlertWhenCreditOver(rs.getBoolean("enable_email_alert"));
            aUser.setEndDate(rs.getDate("end_date"));
            aUser.setStartDate(rs.getDate("start_date"));
            aUser.setAlertThreshold(rs.getInt("alertThreshold"));
            aUser.setCost_per_sms(rs.getFloat("cost_per_sms"));
            aUser.setArrears(rs.getInt("arrears"));
            aUser.setAlphanumeric(rs.getString("alphanumeric"));
            aUser.setAlphaId(rs.getLong("alphaId"));
            result.add(aUser);
        }

        return result;

    }

    public static String isReseller() {
        return reseller;
    }

    public static String showMyAccount() {
        return myAccount;
    }

    public static UserProfile getUserProfile() {
        return loggedInUser;
    }

    @Override
    public void updatePassword(Connection conn, String password) throws SQLException {

        String updateSQL = "update tUSER set password = ? where username = ? and admin = 5";

        PreparedStatement pst = conn.prepareStatement(updateSQL);
        pst.setString(1, password);
        pst.setString(2, loggedInUser.getUsername());
        int r = pst.executeUpdate();
        if (r > 0) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO, "Successfully changed!", "Successfully changed!"));
        }

    }

    @Override
    public List<creditRecord> getAllUsersCred(Connection conn, String name) throws SQLException {
        //System.out.println("nammme: " + name);
        String sql = "";

        sql = "SELECT tManageCredits.id, tManageCredits.username, tManageCredits.actionType, tManageCredits.actionTime,"
                + "tManageCredits.numCredits,tManageCredits.previous_balance,tManageCredits.new_balance,tManageCredits.user from dbSMS.tManageCredits where tManageCredits.username='" + name + "' order by tManageCredits.id desc";

        List<creditRecord> result = new ArrayList<>();

        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(sql);

        while (rs.next()) {
            creditRecord aUser = new creditRecord();
            String actioType = rs.getString("actionType");
            if (actioType.equals("1")) {
                actioType = "Top-Up";
            } else if (actioType.equals("2")) {
                actioType = "Reduction";
            }
            aUser.setUsernC(rs.getString("username"));
            aUser.setActionType(actioType);
            aUser.setActionTime(rs.getString("actionTime"));
            aUser.setNumCredits(rs.getString("numCredits"));
            aUser.setPrevious_balance(rs.getString("previous_balance"));
            aUser.setNew_balance(rs.getString("new_balance"));
            aUser.setCreditedBy(rs.getString("user"));
            result.add(aUser);
        }
        
        return result;
    }

    @Override
    public void persistUser(UserController user, Connection conn) throws SQLException {
        UserScroller us = new UserScroller();
        UserController userl = new UserController();
        AlphaScroller ac = new AlphaScroller();
        UserServiceApi userService = new UserServiceImpl();
        String agent = ac.currentUSer();
        int lResellerId = getNewResellerId(conn);

        String sql = "INSERT INTO tUSER("
                + "username, password, max_total, organization, contact_number, email_address, start_date, "
                + "end_date, enable_email_alert, admin, alertThreshold,super_account_id,arrears,cost_per_sms,agent,firstname,surname,`group`) "
                + "VALUES (?, ?, ?, ?, ?, ?, now(), '2099-12-31', ?, ?, ?,?,?,?,?,?,?,?)" ;
       
        ////////////////////////////////////////////////////////
        //Inserting values
        ////////////////////////////////////////////////////////
        PreparedStatement pstmt = conn.prepareStatement(sql);
        // Bind values to the parameters
        pstmt.setString(1, user.getUsername());
        pstmt.setString(2, user.getPassword());
        //pstmt.setLong(3, user.getSmsCredits());
        pstmt.setLong(3, user.getEmailCredits());
        pstmt.setString(4, user.getOrganization());
        pstmt.setString(5, user.getUserMobile());
        pstmt.setString(6, user.getUserEmail());

        pstmt.setBoolean(7, user.isEnableEmailAlertWhenCreditOver());

        pstmt.setString(8, String.valueOf(user.getAdmin()));
        //pstmt.setString(8, lIsReseller ? "5" : String.valueOf(user.getAdmin()));
        pstmt.setInt(9, user.getAlertThreshold());
        pstmt.setInt(10, 0);
        pstmt.setInt(11, user.getArrears());
        pstmt.setFloat(12, user.getCost_per_sms());
        pstmt.setString(13, getAgent());
//        pstmt.setString(13, "email");
        pstmt.setString(14, user.getFirstName());
        pstmt.setString(15, user.getSurName());
        pstmt.setInt(16, user.getSelectedGroup().getId());
        // Execute the query
        int count = pstmt.executeUpdate();
        //pesistUserInfo(conn, user);
        userService.updateAgentCredits(agent, Math.round(us.availableCredits(conn)[0]), user.getSmsCredits(), user.getSmsCredits(), conn);
        //System.out.println(new Date()+"INSERTING USER INSERTING  NEW USER: " + " " + user.getUsername() + "" + count);
        userl.updateAdminBal();
        
    }

    
    private String getAgent() {
        String agent = "";
        int lAdmin = Character.getNumericValue((char) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("admin"));
        if (lAdmin == 5) {
            agent = FacesContext.getCurrentInstance().getExternalContext()
                    .getSessionMap().get("user_id").toString();
        }
        return agent;
    }

    //on creating new resellers user
    private boolean insertIntoTclient(UserController user, Connection conn, int resellerId) {

        boolean resellerStatus = (Boolean) FacesContext
                .getCurrentInstance()
                .getExternalContext()
                .getSessionMap()
                .get("reseller");

        if (resellerStatus) {

            if (resellerId != 0) {

                String insertSQL = "insert into dbTASK.tClient(id,clientName,email,systemType) "
                        + "value(?,?,?,?)";
                try (PreparedStatement lpst = conn.prepareStatement(insertSQL)) {

                    lpst.setLong(1, resellerId);
                    lpst.setString(2, user.getOrganization());
                    lpst.setString(3, user.getUserEmail());
                    lpst.setString(4, "reseller");

                    lpst.execute();

                    return true;
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }

        //System.out.println(user.getUsername() + " reseller status: " + resellerStatus);
        return false;
    }

    private int getNewResellerId(Connection conn) {
        String selectSQL = "select auto_increment from information_schema.TABLES where TABLE_NAME ='tUSER' and TABLE_SCHEMA='dbSMS'";
        try (PreparedStatement lpst = conn.prepareStatement(selectSQL)) {
            ResultSet lrs = lpst.executeQuery();
            if (lrs.next()) {
                //System.out.println("the next is id is ===============>  " + lrs.getInt("auto_increment"));
                return lrs.getInt("auto_increment");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return 0;
    }

    @Override
    public Date setEndDate() {

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        String endDateInString = "2099-12-31 00:00:00";
        Date endDate = null;
        try {
            endDate = simpleDateFormat.parse(endDateInString);
        } catch (ParseException ex) {
            Logger.getLogger(UserServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        return endDate;
    }

    private int userId;

    public static String drift;

    public String getDrift() {
        return drift;
    }

    public void setDrift(String drift) {
        this.drift = drift;
    }

    @Override
    public int getUserId() {
        return userId;
    }
    private String visual;

    public String getVisual() {
        return visual;
    }

    public void setVisual(String visual) {
        this.visual = visual;
    }
    //public String picLocation = "../files/config/clarineLogo.jpeg";
    public static String picLocation;
    public static String picFromdb;

    public static String getPicFromdb() {
        return picFromdb;
    }

    public static void setPicFromdb(String picFromdb) {
        UserServiceImpl.picFromdb = picFromdb;
    }

    public String getPicLocation() {
        //System.out.println("Location " + picLocation);
        return picLocation;
    }

    public void setPicLocation(String picLocation) {
        this.picLocation = picLocation;
    }
    tClientManager mn = new tClientManager();

    public String dbPic() {
        return mn.loadTclient(this.getLoggedinID());

    }
    public String filelocsure;

    public String retrivePhotosure(String user) {
        JdbcUtil jdb = new JdbcUtil();
        Connection con;
        //FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get(con).toString();
        String sql = "select u.id, t.picPath from dbSMS.tUSER u inner join  dbTASK.tClient t on u.id = t.id where u.username='" + user + "'";
        try {
            con = jdb.getConnectionTodbTask();
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(sql);

            while (rs.next()) {
                filelocsure = rs.getString("picPath");
            }
            // System.out.println(sql);
        } catch (SQLException ex) {
            Logger.getLogger(UserServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        //System.out.println(sql);
        //System.out.println("File locaton is +++++" + filelocsure);
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("myloc", filelocsure);
        return filelocsure;

    }

    public int loggedID;

    public int getLoggedinID() {
        AlphaScroller as = new AlphaScroller();
        JdbcUtil dbcon = new JdbcUtil();
        Connection con;
        // String loggedInus=FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get(AUTH_KEY).toString();
        String loggedInus = as.currentUSer();
        String sql = "SELECT id from tUSER where username ='" + loggedInus + "'";

        //System.out.println("Loggedin user from tclient " + loggedInus);
        try {
            con = dbcon.getConnectionTodbSMS();
            Statement stm = con.createStatement();

            ResultSet rs = stm.executeQuery(sql);

            while (rs.next()) {
                loggedID = rs.getInt("id");
            }

        } catch (SQLException ex) {
            Logger.getLogger(tClientManager.class.getName()).log(Level.SEVERE, null, ex);
        }

        return loggedID;

    }
    private static boolean isReseller;

    public boolean isIsReseller() {
        return isReseller;
    }

    public void setIsReseller(boolean isReseller) {
        this.isReseller = isReseller;
    }

    @Override

    public boolean authenticateUser(String username, String password) {
        JdbcUtil util = new JdbcUtil();
        String id = "SELECT id from tUSER where username='" + username + "'";

        boolean authenticated = false;
        tClientManager tc = new tClientManager();
        AuthenticationBean au = new AuthenticationBean();

        String sql = "SELECT * FROM tUSER WHERE username=? AND password=? AND (admin=1 or admin=5)";
        this.retrivePhotosure(username);
        try {

            Connection conn = util.getConnectionTodbSMS();

            PreparedStatement st = conn.prepareStatement(sql);
            st.setString(1, username);
            st.setString(2, password);
            ResultSet rs = st.executeQuery();
            if (rs != null && rs.next()) {
                authenticated = true;
                userId = rs.getInt("id");

                FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("admin", rs.getInt("admin"));
                FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("CurretUserID", userId);

                FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("max_total", rs.getInt("max_total"));
//peg
                if (rs.getInt("admin") == 5) {
                    showMpesaMenu = "none";
                    isReseller = true;
                    reseller = "none";
                    myAccount = "show";
                    manageReseller = "none";
                    showCreditHistoryReprt = "show";
                    drift = "resources/js/drift.js";
                    visual = "resources/js/visualization.js";
                    picLocation = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("myloc");

                } else {
                    showMpesaMenu = "show";
                    drift = "resources/js/drifts.js";
                    visual = "resources/js/emptyvisual.js";
                    reseller = "show";
                    myAccount = "none";
                    manageReseller = "show";
                    showCreditHistoryReprt = "none";
                    picLocation = "resources/images/logo.gif";
                    isReseller = false;

                }

            }
            JdbcUtil.closeConnection(conn);
        } catch (SQLException ex) {
            JdbcUtil.printSQLException(ex);
        }

        return authenticated;
    }

    // +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
    private BigInteger dailyTotal;
    private BigInteger weeklyTotal;
    private BigInteger monthlyTotal;

    public BigInteger getDailyTotal() {
        return dailyTotal;
    }

    public void setDailyTotal(BigInteger dailyTotal) {
        this.dailyTotal = dailyTotal;
    }

    public BigInteger getWeeklyTotal() {
        return weeklyTotal;
    }

    public void setWeeklyTotal(BigInteger weeklyTotal) {
        this.weeklyTotal = weeklyTotal;
    }

    public BigInteger getMonthlyTotal() {
        return monthlyTotal;
    }

    public void setMonthlyTotal(BigInteger monthlyTotal) {
        this.monthlyTotal = monthlyTotal;
    }

    private BigInteger allSentSms;

    public BigInteger getAllSentSms() {
        return allSentSms;
    }

    public void setAllSentSms(BigInteger allSentSms) {
        this.allSentSms = allSentSms;
    }

    @Override
    public BigInteger smsSumary(String agent) {

        UserScroller us = new UserScroller();
        String current = us.current_user;

        JdbcUtil util = new JdbcUtil();

        String sql = "select sum(sms_count_today) as dailyt, sum(sms_count_week) as weeklyt, sum(sms_count_month) as montht,sum(sms_count_total) as grandTotal from tUSER where agent='" + agent + "'and id!='" + us.agentID() + "'";

        try {
            //System.err.println(sql);

            Connection conn = util.getConnectionTodbSMS();

            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);

            while (rs.next()) {
                dailyTotal = BigInteger.valueOf(rs.getLong("dailyt"));
                weeklyTotal = BigInteger.valueOf(rs.getLong("weeklyt"));
                monthlyTotal = BigInteger.valueOf(rs.getLong("montht"));
                monthlyTotal = BigInteger.valueOf(rs.getLong("montht"));
                allSentSms = BigInteger.valueOf(rs.getLong("grandTotal"));
                //System.out.println("Daily totals :" + dailyTotal + "\t" + "Weekly d totals :" + weeklyTotal + "\t" + "Monthly totals :" + monthlyTotal);

            }

        } catch (SQLException ex) {
            Logger.getLogger(UserServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        return dailyTotal;
    }

    @Override

    public UserProfile refreshProfile(String user) {
        JdbcUtil util = new JdbcUtil();

        String sql = "SELECT * FROM tUSER WHERE username='" + user + "'";

        try {
            //System.err.println(sql);

            Connection conn = util.getConnectionTodbSMS();

            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);

            while (rs.next()) {
                loggedInUser = new UserProfile(rs.getLong("id"), rs.getString("username"), rs.getString("password"),
                        rs.getLong("max_daily"), rs.getLong("max_weekly"), rs.getLong("max_monthly"), rs.getLong("max_total"),
                        rs.getBoolean("logged_in"),
                        rs.getDate("logged_in_time"), rs.getDate("start_date"), rs.getDate("end_date"),
                        BigInteger.valueOf(rs.getLong("sms_count_today")), BigInteger.valueOf(rs.getLong("sms_count_week")),
                        BigInteger.valueOf(rs.getLong("sms_count_month")), BigInteger.valueOf(rs.getLong("sms_count_total")),
                        rs.getString("contact_number"), rs.getString("email_address"), rs.getBoolean("enable_email_alert"), rs.getInt("alertThreshold"));
               
            }

        } catch (SQLException ex) {
            Logger.getLogger(UserServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        return loggedInUser;
    }

    @Override
    public void updateUser(UserController user, Connection conn) throws SQLException {

        String sql = "UPDATE tUSER SET "
                + "username=?, password=?, max_total=?, organization=?, contact_number = ?, email_address=?"
                + ", enable_email_alert=?,cost_per_sms=?,arrears=?,alertThreshold=? ,admin = ? , `group` =? WHERE id=?";

        ////////////////////////////////////////////////////////
        //Inserting values
        ////////////////////////////////////////////////////////
        PreparedStatement pstmt = conn.prepareStatement(sql);
        // Bind values to the parameters
        pstmt.setString(1, user.getUsername());
        pstmt.setString(2, user.getPassword());
        pstmt.setLong(3, user.getSmsCredits());
        pstmt.setString(4, user.getOrganization());
        pstmt.setString(5, user.getUserMobile());
        pstmt.setString(6, user.getUserEmail());
        pstmt.setBoolean(7, user.isEnableEmailAlertWhenCreditOver());
        pstmt.setFloat(8, user.getCost_per_sms());
        pstmt.setInt(9, user.getArrears());

        pstmt.setInt(10, user.getAlertThreshold());

        pstmt.setString(11, String.valueOf(user.getAdmin()));
         pstmt.setInt(12, user.getSelectedGroup().getId());
        pstmt.setLong(13, user.getId());

        // Execute the query
        int count = pstmt.executeUpdate();

    }
    @Override
     public void updateUserWithoutGroup(UserController user, Connection conn) throws SQLException {

        String sql = "UPDATE tUSER SET "
                + "username=?, password=?, max_total=?, organization=?, contact_number = ?, email_address=?"
                + ", enable_email_alert=?,cost_per_sms=?,arrears=?,alertThreshold=? ,admin = ? WHERE id=?";

        ////////////////////////////////////////////////////////
        //Inserting values
        ////////////////////////////////////////////////////////
        PreparedStatement pstmt = conn.prepareStatement(sql);
        // Bind values to the parameters
        pstmt.setString(1, user.getUsername());
        pstmt.setString(2, user.getPassword());
        pstmt.setLong(3, user.getSmsCredits());
        pstmt.setString(4, user.getOrganization());
        pstmt.setString(5, user.getUserMobile());
        pstmt.setString(6, user.getUserEmail());
        pstmt.setBoolean(7, user.isEnableEmailAlertWhenCreditOver());
        pstmt.setFloat(8, user.getCost_per_sms());
        pstmt.setInt(9, user.getArrears());

        pstmt.setInt(10, user.getAlertThreshold());

        pstmt.setString(11, String.valueOf(user.getAdmin()));
        pstmt.setLong(12, user.getId());

        // Execute the query
        int count = pstmt.executeUpdate();

    }

    @Override
    public UserController loadCustomerByUsername(String selectedUsername, Connection conn) throws SQLException {

        String sql = "SELECT * FROM tUSER WHERE username='" + selectedUsername + "'";
        UserController aUser = new UserController();

        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(sql);

        while (rs.next()) {

            aUser.setId(rs.getLong("id"));
            aUser.setUsername(rs.getString("username"));
            aUser.setPassword(rs.getString("password"));
            aUser.setAdmin(rs.getString("admin").charAt(0));
            aUser.setSmsCredits(rs.getInt("max_total"));
            aUser.setOrganization(rs.getString("organization"));
            aUser.setUserMobile(rs.getString("contact_number"));
            aUser.setUserEmail(rs.getString("email_address"));
            aUser.setEnableEmailAlertWhenCreditOver(rs.getBoolean("enable_email_alert"));
            aUser.setCost_per_sms(rs.getFloat("cost_per_sms"));
            aUser.setArrears(rs.getInt("arrears"));
            aUser.setEndDate(rs.getDate("end_date"));
            aUser.setStartDate(rs.getDate("start_date"));
            aUser.setAlertThreshold(rs.getInt("alertThreshold"));
        }
        return aUser;
    }

    @Override
    public void generateXSL(Connection conn) throws SQLException {
        try {
            HSSFWorkbook wb = new HSSFWorkbook();
            HSSFSheet sheet = wb.createSheet("Users_Sheet1");
            HSSFRow row = sheet.createRow(0);
            row.createCell(0).setCellValue("USERNAME");
            row.createCell(1).setCellValue("SMS CREDITS");
            row.createCell(2).setCellValue("ORGANIZATION");
            row.createCell(3).setCellValue("MOBILE");
            row.createCell(4).setCellValue("EMAIL");
            List<UserController> exportUsers = getAllUsers(conn, names);
            int rowNum = 1;
            for (UserController aUser : exportUsers) {
                row = sheet.createRow(rowNum);
                row.createCell(0).setCellValue(aUser.getUsername());
                row.createCell(1).setCellValue(aUser.getSmsCredits());
                row.createCell(2).setCellValue(aUser.getOrganization());
                row.createCell(3).setCellValue(aUser.getUserMobile());
                row.createCell(4).setCellValue(aUser.getUserEmail());
                rowNum++;
            }

            FacesContext context = FacesContext.getCurrentInstance();
            HttpServletResponse res = (HttpServletResponse) context.getExternalContext().getResponse();
            res.setContentType("application/vnd.ms-excel");
            res.setHeader("Content-disposition", "attachment;filename=mydata.xlsx");
            ServletOutputStream out = res.getOutputStream();
            wb.write(out);
            out.flush();
            out.close();
            FacesContext.getCurrentInstance().responseComplete();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void updateCredits(String username, int smsCredits, Connection conn) throws SQLException {
        String sql = "UPDATE tUSER SET max_total = ? WHERE username = ?";
        String sqlReseller = "UPDATE tUSER SET max_total = ? WHERE username = ?";
        PreparedStatement pstmt = conn.prepareStatement(sql);
        // Bind values to the parameters
        pstmt.setInt(1, smsCredits);
        pstmt.setString(2, username);
        int count = pstmt.executeUpdate();
    }

    @Override
    public void updateAgentCredits(String agent, int current, int toDeduct, int newBalace, Connection conn) {
        try {
            Date dt = new Date();
            AlphaScroller as = new AlphaScroller();
            agent = as.currentUSer();
            String sql = "UPDATE tUSER SET max_total = ? WHERE username = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, current - toDeduct);
            pstmt.setString(2, agent);
            int count = pstmt.executeUpdate();

        } catch (SQLException ex) {
            Logger.getLogger(UserServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void alterAgentCredits(String agent, int currentagentcredits, int currrentusercreds, int alter, Connection conn) {
        try {
            AlphaScroller as = new AlphaScroller();
            agent = as.currentUSer();
            int altered = currrentusercreds - alter;
            String sql = "UPDATE tUSER SET max_total = ? WHERE username = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, currentagentcredits + alter);
            pstmt.setString(2, agent);

            int count = pstmt.executeUpdate();
            //System.out.println(agent + "to:::" + altered + "::::");
            //System.out.println("ALTER AGENT CREDITS : " + count + "::::" + alter);
        } catch (SQLException ex) {
            Logger.getLogger(UserServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void updateAlpha(String usernameOld, String usernameNew, Connection conn) throws SQLException {

        String sql = "UPDATE tAllowedAlphanumerics SET username = ? WHERE username = ?";

        PreparedStatement pstmt = conn.prepareStatement(sql);

        pstmt.setString(1, usernameNew);
        pstmt.setString(2, usernameOld);

        boolean count = pstmt.execute();

    }


    @Override
    public Map<String, Object> simpleStatistics() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    //================================================================================================
    private int auto_id;
    private int nextId;

    public int getNextId() {
        return nextId;
    }

    public void setNextId(int nextId) {
        this.nextId = nextId;
    }

    public int getAutoId(Connection conn) throws SQLException {

        String get_id = "SELECT auto_increment from information_schema.TABLES where table_schema = 'dbSMS' and table_name = 'tUSER'";
        Statement st = conn.createStatement();
        ResultSet rs = st.executeQuery(get_id);

        while (rs.next()) {
            auto_id = rs.getInt(1) - 1;

        }       

        return auto_id;
    }

    //Fuction to  insert reseller data into the dbTASK tClient table 
    public void pesistUserInfo(Connection conn, UserController user) throws SQLException {

        String sqlimg = "INSERT INTO dbTASK.tClient (id,clientName,email,systemType,picPath) values(?,?,?,?,?)";

        JdbcUtil util = new JdbcUtil();
        Connection conn2 = util.getConnectionTodbTask();
        PreparedStatement ps2 = conn2.prepareStatement(sqlimg);
        ps2.setInt(1, this.getAutoId(conn));
        ps2.setString(2, user.getOrganization());
        ps2.setString(3, user.getUserEmail());
        ps2.setString(4, "web");
        ps2.setString(5, "url");
        ps2.executeUpdate();
        //System.out.println("(save user info to task client )Success.... with ID" + getAutoId(conn));
    }

    public void persistUserAgent(UserController user, Connection conn) {
       
        String autogen = "";

        String sql = "INSERT INTO tUSER("
                + "username, password, max_total, organization, contact_number, email_address, start_date, "
                + "end_date, enable_email_alert, admin, alertThreshold,super_account_id,arrears,cost_per_sms,agent) "
                + "VALUES (?, ?, ?, ?, ?, ?, now(), '2099-12-31', ?, ?, ?,?,?,?,?)";

        //System.out.println("Insert Reseller Querry" + sql);
        try {
            PreparedStatement pstmt = conn.prepareStatement(sql);
            // Bind values to the parameters
            pstmt.setString(1, user.getUsername());
            pstmt.setString(2, user.getPassword());
            pstmt.setLong(3, user.getSmsCredits());
            pstmt.setString(4, user.getOrganization());
            pstmt.setString(5, user.getUserMobile());
            pstmt.setString(6, user.getUserEmail());
            pstmt.setBoolean(7, user.isEnableEmailAlertWhenCreditOver());
            pstmt.setString(8, user.getReseller() ? "5" : String.valueOf(user.getAdmin()));
            pstmt.setInt(9, user.getAlertThreshold());
            pstmt.setInt(10, 0);
            pstmt.setInt(11, user.getArrears());
            pstmt.setFloat(12, user.getCost_per_sms());

            pstmt.setString(13, this.getAgent());
            // Execute the query
            int count = pstmt.executeUpdate();
            pesistUserInfo(conn, user);
            //System.out.println("INSERTING USER INSERTING USER: " + count);
            //System.out.println("(Save agent )The next auto increment is :" + getAutoId(conn));
        } catch (SQLException ex) {
            Logger.getLogger(UserServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    //================================================================================================
    public List<UserController> getAllUserPerAgent(Connection conn, String name) throws SQLException {

        names = name;
        String id = "";

        String fetchid = "SELECT id from tUSER where username='" + name + "' ";
        Statement stmt1 = conn.createStatement();
        ResultSet rs1 = stmt1.executeQuery(fetchid);
        while (rs1.next()) {
            id = String.valueOf(rs1.getLong("id"));
        }
        // System.out.println("The id is :" + id);

        String sql = " SELECT tUSER.id, tUSER.username, tUSER.password, tUSER.admin, tUSER.max_total, tUSER.organization, tUSER.contact_number, tUSER.email_address,"
                + " tUSER.enable_email_alert,tUSER.end_date, tUSER.start_date, tUSER.alertThreshold,tUSER.cost_per_sms,tUSER.arrears,tAllowedAlphanumerics.alphanumeric, "
                + "tAllowedAlphanumerics.id as alphaId FROM tUSER LEFT JOIN tAllowedAlphanumerics on tUSER.username=tAllowedAlphanumerics.username where tUSER.admin !='1'"
                + "and tUSER.agent='" + id + "'";

        List<UserController> result = new ArrayList<>();
        Statement stmt = conn.createStatement();

        ResultSet rs = stmt.executeQuery(sql);
        //System.out.println(name);
        while (rs.next()) {
            UserController aUser = new UserController();
            aUser.setId(rs.getLong("id"));
            aUser.setUsername(rs.getString("username"));
            aUser.setPassword(rs.getString("password"));
            aUser.setAdmin(rs.getString("admin").charAt(0));

            String ad = "" + aUser.getAdmin();
            if (ad.trim() != null) {
                if (ad.equalsIgnoreCase("4")) {
                    // System.err.println("AD" + ad + ": username :" + aUser.getUsername() + "can create sub accounts");
                    aUser.setUserType("Yes");
                    aUser.setCreateAccount(true);
                } else {
                    // System.err.println("AD" + ad + ": username :" + aUser.getUsername() + "cannot create sub accounts");
                    aUser.setUserType("No");
                    aUser.setCreateAccount(false);
                }
            }
            aUser.setSmsCredits(rs.getInt("max_total"));
            aUser.setOrganization(rs.getString("organization"));
            aUser.setUserMobile(rs.getString("contact_number"));
            aUser.setUserEmail(rs.getString("email_address"));
            aUser.setEnableEmailAlertWhenCreditOver(rs.getBoolean("enable_email_alert"));
            aUser.setEndDate(rs.getDate("end_date"));
            aUser.setStartDate(rs.getDate("start_date"));
            aUser.setAlertThreshold(rs.getInt("alertThreshold"));
            aUser.setCost_per_sms(rs.getFloat("cost_per_sms"));
            aUser.setArrears(rs.getInt("arrears"));
            aUser.setAlphanumeric(rs.getString("alphanumeric"));
            aUser.setAlphaId(rs.getLong("alphaId"));
            result.add(aUser);
        }

        return result;
    }

    //=======================================================================================
    private static String showCreditHistoryReprt;

    public static String showCrediHistoryReport() {
        return showCreditHistoryReprt;
    }
    private static String manageReseller;

    public static String showManageReseller() {
        return manageReseller;
    }

    public static String showMpesaMenu;

    public static String getShowMpesaMenu() {
        return showMpesaMenu;
    }
    //=============================================================================================
    private int resCredits = 0;

    //AlphaServiceImpl al= new AlphaServiceImpl();
    public int getResCredits() {
        return resCredits;
    }

    public void setResCredits(int resCredits) {
        this.resCredits = resCredits;
    }
    //++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++ 

    @Override
    public List<Alpha> getAgentAlphas(Connection conn, String user) {

        UserScroller us = new UserScroller();
        JdbcUtil util = new JdbcUtil();

        //String sqlReseller = "Select short_code, contactEmail from tSDP where agent_id= '" + user + "'";
        String sqlReseller = "Select short_code, contactEmail from tSDPNew where agent_id= '" + user + "'";
        //System.out.println("*******************" + sqlReseller);
        List<Alpha> result = new ArrayList<>();
        try {
            conn = util.getConnectionTodbSMS();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sqlReseller);

            while (rs.next()) {
                Alpha alpha = new Alpha();
                //alpha.setId(rs.getLong("id"));
                alpha.setUsername(rs.getString("contactEmail"));
                alpha.setName(rs.getString("short_code"));

                result.add(alpha);
            }
            JdbcUtil.closeConnection(conn);
        } catch (SQLException ex) {
            JdbcUtil.printSQLException(ex);
        }

        return result;

    }

    @Override
    public String loadScript(String username, String password) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void deleteUser(UserController selected, Connection conn) {
        try {
//          String sql = "UPDATE tUSER set admin= 4 where username =?";
            String sql = "DELETE from tUSER where username =?";
           
            PreparedStatement pstmt = conn.prepareStatement(sql);
            // Bind values to the parameters
            pstmt.setString(1, selected.getUsername());
            //pstmt.setString(2,alphanumeric);
            //System.out.println("Deleting UserController  " + selected);

            // Execute the query
            int count = pstmt.executeUpdate();
            //System.out.println("Count status" + count);
        } catch (SQLException ex) {
            Logger.getLogger(AlphaServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    @Override
    public void deleteReseller(UserController selected, Connection conn) {
        try {
            String sql = "DELETE from tUSER where username =?";
            //System.out.println("The querry" + sql);

            PreparedStatement pstmt = conn.prepareStatement(sql);
            // Bind values to the parameters
            pstmt.setString(1, selected.getUsername());
            //pstmt.setString(2,alphanumeric);
            //System.out.println("Deleting UserController  " + selected);

            // Execute the query
            int count = pstmt.executeUpdate();
            //System.out.println("Count status" + count);
        } catch (SQLException ex) {
            Logger.getLogger(AlphaServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public void persistPaybill(Paybill paybill, Connection conn, Connection conn2) {
        try {
            JdbcUtil util = new JdbcUtil();

            conn = util.getConnectionTodbPAYMENT();
            String sql = "INSERT INTO tUSERPAYBILL(tUSER_id,paybill,default_reply,email,username,sender_id)VALUES(?,?,?,?,?,?)";

            PreparedStatement ps = conn.prepareStatement(sql);
            // System.out.println("UserController ID : "+paybill.getUserid()+"Username:"+paybill.getName());
            ps.setInt(1, paybill.getUserid());
            ps.setInt(2, paybill.getPaybillz());
            ps.setString(3, paybill.getMessage());
            ps.setString(4, paybill.getEmail());
            ps.setString(5, paybill.getName());
            int listSize = getUserAlphas(conn, paybill.getName()).size();
            ps.setString(6, listSize < 1 ? "" : getUserAlphas(conn, paybill.getName()).get(0).getAlphanumeric());
            int count = ps.executeUpdate();
            ps.close();

            conn.close();

            conn2 = util.getConnectionTodbSMS();
            //System.out.print("wwwwwwwwwwwwwwwwwwwww");
            String sql2 = "INSERT INTO  tRULES(RuleName,RuleType,Filename,dest,ex_order)VALUES(?,?,?,?,7)";
            int ruletype = 1;
            String filename = "java -cp /shared/smsfiles/mpesaClient.jar mspace.PBCommon <source> <dest> <msg>";
            PreparedStatement ps2 = conn2.prepareStatement(sql2);

            ps2.setString(1, paybill.getName() + "_" + paybill.getPaybillz());

            ps2.setInt(2, ruletype);
            ps2.setString(3, filename);
            ps2.setInt(4, paybill.getPaybillz());
            ps2.executeUpdate();
            ps2.close();
            conn2.close();
        } catch (SQLException ex) {
            Logger.getLogger(UserServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void deletePaybill(Paybill pabilx, Connection conn) {
        try {
            int id = pabilx.getUserid();

            String sql = "DELETE  FROM  tUSERPAYBILL where tUSER_id =?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, id);
            ps.executeUpdate();

        } catch (Exception k) {

            k.printStackTrace();
        }
    }

    public void updatePaybill(Paybill paybill, Connection conn) throws SQLException {
        JdbcUtil util = new JdbcUtil();
        UserController user = null;
        conn = util.getConnectionTodbPAYMENT();
        String sql = "UPDATE tUSERPAYBILL set paybill=?, reply=?, email=? where id=?";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setInt(4, paybill.getUserid());
        ps.setInt(1, paybill.getPaybillz());
        ps.setString(2, paybill.getMessage());
        ps.setString(3, paybill.getEmail());
        ps.executeUpdate();
        //System.out.println("Successfully updated paybill.....");
    }

    public int getUserId(Paybill paybill, Connection conn) throws SQLException {
        JdbcUtil util = new JdbcUtil();
        conn = util.getConnectionTodbSMS();
        String sql = "SELECT id from tUSER where username=?";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setString(1, paybill.getName());

        ResultSet rs = ps.executeQuery();
        int id = 0;
        id = rs.getInt(id);
        return id;
    }

    public static int getUserIdByUsername(String username, Connection conn) throws SQLException {
        JdbcUtil util = new JdbcUtil();
        conn = util.getConnectionTodbSMS();
        String sql = "SELECT id from tUSER where username=?";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setString(1, username);

        ResultSet rs = ps.executeQuery();
        int id = 0;
        while (rs.next()) {
            id = rs.getInt("id");
        }

        return id;
    }

    public void editPaybill(Paybill paybill, Connection conn)
            throws SQLException {
        JdbcUtil util = new JdbcUtil();
        conn = util.getConnectionTodbPAYMENT();
        String sql = "";
    }

    public static ArrayList<Paybill> getPayBill() throws SQLException {
        String username = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("selectedUserCombo");
        JdbcUtil util = new JdbcUtil();
        Connection conn = util.getConnectionTodbPAYMENT();

        String sql = "SELECT * FROM tUSERPAYBILL";
        if (!username.isEmpty()) {
            int id = getUserIdByUsername(username, conn);
            sql = "SELECT * FROM tUSERPAYBILL where tUSER_id =" + id;
        }
        PreparedStatement ps = conn.prepareStatement(sql);
        ArrayList<Paybill> paybilllist = new ArrayList();
        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            Paybill bill = new Paybill();
            bill.setUserid(rs.getInt("tUSER_id"));
            bill.setPaybillz(rs.getInt("paybill"));
            bill.setMessage(rs.getString("reply"));
            bill.setDefault_message(rs.getString("default_reply"));
            bill.setEmail(rs.getString("email"));
            bill.setName(rs.getString("username"));
            bill.setSender_id(rs.getString("sender_id"));
            paybilllist.add(bill);

        }
        return paybilllist;
    }

    public static ArrayList<Paybill> getPaybillUserId() throws SQLException {
        JdbcUtil util = new JdbcUtil();
        Connection conn = util.getConnectionTodbSMS();
        String sql = "SELECT id FROM tUSER";
        PreparedStatement ps = conn.prepareStatement(sql);
        ArrayList<Paybill> paybillIdList = new ArrayList();
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            Paybill paybill = new Paybill();
            paybill.setUserid(rs.getInt("id"));
            paybillIdList.add(paybill);
        }
        return paybillIdList;
    }

    @Override
    public int UpdateUserMaxContacts(UserController currentItem, int MaxContacts, Date StartDate, Date EndDate, Connection conn) {
        int count = 0;
        PreparedStatement pstmt = null;
        try {
            String sql = "Update tUSER set max_contacts=?,start_date=?,end_date=? where username=?";
            JdbcUtil util = new JdbcUtil();
            //System.out.println("The querry" + sql);
            conn = util.getConnectionTodbSMS();
            pstmt = conn.prepareStatement(sql);
            // Bind values to the parameters
            pstmt.setInt(1, MaxContacts);
            pstmt.setDate(2, new java.sql.Date(StartDate.getTime()));
            pstmt.setDate(3, new java.sql.Date(EndDate.getTime()));
            pstmt.setString(4, currentItem.getUsername());
            //System.out.println("Count status" + pstmt);
            count = pstmt.executeUpdate();

            //update or insert into tCLIENTSBSCRITION
            //get the user_id from above table
            //if user_id not present,insert data else update
            String getIDsql = "Select tuser_id from dbEMAIL.tCLIENTESUBSCRIPTION where tuser_id=?";
            pstmt = conn.prepareStatement(getIDsql);
            pstmt.setInt(1, (int) currentItem.getId().longValue());
            if (!pstmt.executeQuery().next()) {
                //System.out.println("Resource not found for id" + currentItem.getId());
                //insert data 
                String insertsql = "insert into dbEMAIL.tCLIENTESUBSCRIPTION(tuser_id,max_contacts,start_date,expiry_date,subscription_type) "
                        + "values (?,?,?,?,?)";
                pstmt = conn.prepareStatement(insertsql);
                pstmt.setInt(1, (int) currentItem.getId().longValue());
                pstmt.setInt(2, MaxContacts);
                pstmt.setDate(3, new java.sql.Date(StartDate.getTime()));
                pstmt.setDate(4, new java.sql.Date(EndDate.getTime()));

                //pstmt.setString(4, currentItem.getUsername());
                pstmt.setInt(5, 1);
                pstmt.executeUpdate();

            } else {
                //update 

                String updatesql = "update dbEMAIL.tCLIENTESUBSCRIPTION set max_contacts=?,start_date=?,expiry_date=?,subscription_type=? where tuser_id=?";
                pstmt = conn.prepareStatement(updatesql);
                pstmt.setInt(1, MaxContacts);
                pstmt.setDate(2, new java.sql.Date(StartDate.getTime()));
                pstmt.setDate(3, new java.sql.Date(EndDate.getTime()));

                //pstmt.setString(4, currentItem.getUsername());
                pstmt.setInt(4, 1);
                pstmt.setInt(5, (int) currentItem.getId().longValue());
                pstmt.executeUpdate();
            }

        } catch (SQLException ex) {
            Logger.getLogger(AlphaServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                conn.close();
                pstmt.close();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return count;

    }

    @Override
    public List<Alpnumeric> getUserAlphas(Connection conn, String name) {
        JdbcUtil util = new JdbcUtil();
//        String sqlReseller = "Select short_code, contactEmail from tSDP where agent_id= '" + user + "'";
        String sql = "Select id,username,alphanumeric, sid_type from tAllowedAlphanumerics  where username= '" + name + "'";
        // System.out.println("*******************" + sql);
        List<Alpnumeric> result = new ArrayList<>();
        try {
            conn = util.getConnectionTodbSMS();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);

            while (rs.next()) {
                Alpnumeric alpha = new Alpnumeric();
                alpha.setId(rs.getLong("id"));
                alpha.setUserName(rs.getString("username"));
                alpha.setAlphanumeric(rs.getString("alphanumeric"));
                alpha.setSid_type(rs.getString("sid_type"));

                result.add(alpha);
            }
            JdbcUtil.closeConnection(conn);
        } catch (SQLException ex) {
            JdbcUtil.printSQLException(ex);
        }

        return result;
    }
public String formatNumbers(String nums){
  String []numbers=nums.split(",\\s*");
  
  for(int i=0;i<=numbers.length-1;i++){
      if(numbers[i].startsWith("254") && numbers[i].length()==12 ){
//        okay
      }else if(((numbers[i].startsWith("01") ||(numbers[i].startsWith("07")))&& numbers[i].length()==10 )){
          numbers[i]="254"+numbers[i].substring(1, numbers[i].length());
      }
      
    
  }
    
      return String.join(",", numbers);
}
    public void persistCallBack(CallBack callback, Connection conn,String assigned_code) {
        try {
            String testBedNumbers=callback.getTestbednumbers();
            callback.setTestbednumbers( formatNumbers(testBedNumbers));
           
            
            JdbcUtil util = new JdbcUtil();
            conn = util.getConnectionTodbUSSD();
            String sql = "INSERT INTO tSharedUssdClients(tuser_id,callback_url,"
                    + "ussd_assigned_code, status,testbedmobiles,type)VALUES(?,?,?,?,?,?)";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, callback.getUserid());
            ps.setString(2, callback.getCallback_url());
            ps.setString(3, callback.getUssd_assigned_code());
            ps.setBoolean(4, true);
            ps.setString(5, callback.getTestbednumbers());
             ps.setInt(6, callback.isTestbed()?0:1);
            ps.executeUpdate();
            ps.close();
            JsfUtil.addSuccessMessage("CallBack added successfully!");
            conn.close();
        } catch (SQLException ex) {
            JsfUtil.addErrorMessage("CallBack add failed!");
            Logger.getLogger(UserServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
      public static ArrayList<CallBack> getCallBack() throws SQLException {
        String username = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("selectedUserCombo");
        JdbcUtil util = new JdbcUtil();
        Connection conn = util.getConnectionTodbUSSD();

        String sql = "SELECT * FROM tSharedUssdClients";
        if (!username.isEmpty()) {
            int id = getUserIdByUsername(username, conn);
            sql = "SELECT * FROM tSharedUssdClients where tUSER_id =" + id;
        }
        PreparedStatement ps = conn.prepareStatement(sql);
        ArrayList<CallBack> callbacklist = new ArrayList();
        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            CallBack callback = new CallBack();
            callback.setUserid(rs.getInt("tUSER_id"));
            callback.setCallback_url(rs.getString("callback_url"));
            callback.setUssd_assigned_code(rs.getString("ussd_assigned_code"));
            callback.setStatus(rs.getBoolean("status"));
              callback.setTestbed(rs.getInt("type")==0?true:false);
            callback.setTestbednumbers(rs.getString("testbedmobiles"));
            
            callbacklist.add(callback);

        }
        return callbacklist;
    }
            
        public static  ArrayList<EmailPricingTable> getPricingTable() throws SQLException{
        String username = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("selectedUserCombo");
        JdbcUtil util = new JdbcUtil();
        Connection conn = util.getConnectionTodbEMAIL();

        String sql = "select * from tEMAILPRICING te";
        PreparedStatement ps = conn.prepareStatement(sql);
        ArrayList<EmailPricingTable> pricingtable = new ArrayList();
        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
           EmailPricingTable pricing = new EmailPricingTable();
            pricing.setId(rs.getInt("id"));
           pricing.setPrice(rs.getFloat("price"));
            pricing.setEmails_purchased(rs.getString("emails_purchased"));
            String str=rs.getString("emails_purchased");
            String [] strarr=str.split("-");
            pricing.setEmails_purchased_start(strarr[0]);
           pricing.setEmails_purchased_end(strarr[1]);
             pricing.setExpiry(rs.getString("expiry"));
             pricing.setSelectType(new SelectItem(rs.getString("expiry")));
             
             pricingtable.add(pricing);
        }
        return pricingtable;
    } 

  

//    @Override
//    public void updateEmailUser(EmailUser aThis, Connection conn) {
//        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
//    }

//    @Override
//    public void persistEmailUserAgent(EmailUser aThis, Connection conn) {
//        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
//    }

    public void updateEmailUser(UserController aThis, Connection conn) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void persistEmailUserAgent(UserController aThis, Connection conn) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean authenticateEmailUser(String username, String password) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
   //Added to update email credits
            @Override
    public void updateEmailCredits(String username, int smsCredits, Connection conn) throws SQLException {
        String sql = "UPDATE tUSER SET max_contacts = ? WHERE username = ?";
        String sqlReseller = "UPDATE tUSER SET max_contacts = ? WHERE username = ?";
        PreparedStatement pstmt = conn.prepareStatement(sql);
        // Bind values to the parameters
        pstmt.setInt(1, smsCredits);
        pstmt.setString(2, username);
        int count = pstmt.executeUpdate();        
    }
    
    @Override
    public void persistEmailUserAgent(EmailUser user, Connection conn) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void updateEmailUser(EmailUser aThis, Connection conn) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    public boolean isUssdCodeAvailable(String code){
        boolean exist=false;
        
        
        
        return exist;
    }

    @Override
    public void persistUssdCode(UserController aThis, Connection conn) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<UserController> getAllEmailUsers(Connection conn, String userS) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    @Override
    public void persistEmailUser(EmailUser user, Connection conn) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
   public static ArrayList<Group> getGroups(Connection conn) {

        String sql = "select * from dbSMS.tGROUPS ";
        ResultSet rs = null;
        PreparedStatement stmt = null;
        List<Group> groups = new ArrayList<>();

        try {
            stmt = conn.prepareStatement(sql);
            rs = stmt.executeQuery();
            while (rs.next()) {
                Group group=new Group();
                group.setId(rs.getInt("id"));
                group.setGroupname(rs.getString("groupname"));
                group.setDescription(rs.getString("description"));
                groups.add(group);
                
            }
        } catch (SQLException e) {
            Logger.getLogger(CallBack.class.getName()).log(Level.SEVERE, null, e.getMessage());
        } finally {

            try {
                if (rs != null) {

                    rs.close();
                }

                if (stmt != null) {
                    stmt.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(CallBack.class.getName()).log(Level.SEVERE, null, ex);
            }

        }
        return (ArrayList<Group>) groups;
    }
   

    
}
