/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ke.co.mspace.nonsmppmanager.service;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.event.ValueChangeEvent;
import javax.faces.model.SelectItem;
import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.Size;
import ke.co.mspace.nonsmppmanager.model.Alpha;
import ke.co.mspace.nonsmppmanager.model.Alpnumeric;
import static ke.co.mspace.nonsmppmanager.model.AuthenticationBean.AUTH_KEY;
import ke.co.mspace.nonsmppmanager.model.CallBack;
import ke.co.mspace.nonsmppmanager.model.Facet;
import ke.co.mspace.nonsmppmanager.model.Paybill;
import ke.co.mspace.nonsmppmanager.model.User;
import ke.co.mspace.nonsmppmanager.model.UserProfile;
import ke.co.mspace.nonsmppmanager.model.creditRecord;
import ke.co.mspace.nonsmppmanager.util.JdbcUtil;
import ke.co.mspace.nonsmppmanager.util.JsfUtil;

//import org.richfaces.component.UIScrollableDataTable;
//import org.richfaces.context.RequestContext;
//import org.richfaces.event.UploadEvent;
//import org.richfaces.model.SortField;
//import org.richfaces.model.SortOrder;
//import org.richfaces.model.UploadItem;
//import org.richfaces.model.selection.SimpleSelection;

/**
 *
 * @author Norrey Osako
 */
public class EmailUserScroller {
    
    private Paybill paybill;
    private User user;
    private CallBack callback=new CallBack();
    // Paybill paybill= new Paybill();
    private User currentItem = new User();
    private Paybill currentItem1 = new Paybill();
    private CallBack currentItem2=new CallBack();
    private final JdbcUtil util = new JdbcUtil();
    private final UserProfile userProfile = UserServiceImpl.getUserProfile();
    Connection conn = null;
    Connection conn2 = null;
    private static final Logger LOG = Logger.getLogger(UserScroller.class.getName());
    String current_user = FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get(AUTH_KEY).toString();
    private int rows = 10;
    
    private int userPlan;
    
    private String alphanumeric;
    
    private Long alphid;
    public String userS;
    private List<User> getAllEmailUsers;

    
    
    @PostConstruct
    public void init() {
        this.user = new User();
        this.paybill = new Paybill();
        this.callback =new CallBack();
    }
    
    public int getRows() {
        return rows;
    }
    
    public void setRows(int rows) {
        this.rows = rows;
    }
    
    public Connection getConn() {
        return conn;
    }
    
    public void setConn(Connection conn) {
        this.conn = conn;
    }
    
    public String getCurrent_user() {
        return current_user;
    }
    
    public void setCurrent_user(String current_user) {
        this.current_user = current_user;
    }
    
    public String getUserid() {
        return userid;
    }
    
    public void setUserid(String userid) {
        this.userid = userid;
    }

    public CallBack getCurrentItem2() {
        return currentItem2;
    }

    public void setCurrentItem2(CallBack currentItem2) {
        this.currentItem2 = currentItem2;
    }
    
    public List<creditRecord> getAllUsersC() {
        return allUsersC;
    }
    
    public void setAllUsersC(List<creditRecord> allUsersC) {
        this.allUsersC = allUsersC;
    }
    
    public String getUser() {
        return users;
    }
    
    public void setUser(String user) {
        this.users = user;
    }
    
    public String getId() {
        return id;
    }
    
    public void setId(String id) {
        this.id = id;
    }
    
    public String getSelectedAlpha() {
        return selectedAlpha;
    }
    
    public void setSelectedAlpha(String selectedAlpha) {
        this.selectedAlpha = selectedAlpha;
    }
    
    public String getAgent() {
        return agent;
    }
    
    public void setAgent(String agent) {
        this.agent = agent;
    }
    
    public String getPicPath() {
        return picPath;
    }
    
    public void setPicPath(String picPath) {
        this.picPath = picPath;
    }
    
    public String getAlphanumeric() {
        return alphanumeric;
    }
    
    public void setAlphanumeric(String alphanumeric) {
        this.alphanumeric = alphanumeric;
    }

    public Long getAlphid() {
        return alphid;
    }

    public void setAlphid(Long alphid) {
        this.alphid = alphid;
    }
    
    
    
    
    public void fetchCurrentRow(ActionEvent event) {
        String username = (FacesContext.getCurrentInstance().
                getExternalContext().getRequestParameterMap().get("username"));
        currentRow = Integer.parseInt(FacesContext.getCurrentInstance().
                getExternalContext().getRequestParameterMap().get("row"));
        for (User item : allUsers) {
            if (item.getUsername().equals(username)) {
                //System.out.println("THE REQUESTED NAME: "+item.getUsername());
                currentItem = item;
                break;
            }
        }
    }
    
    private Set<Integer> keys = new HashSet<>();
    
    private int currentRow;
    
//    private SimpleSelection selection = new SimpleSelection();
//    
//    private UIScrollableDataTable table;
//    
//    private SortOrder order = new SortOrder();
    
    private int scrollerPage = 1;
    
    public String userid;
    
    private ArrayList<User[]> model = null;
    
    private ArrayList<User> selectedCars = new ArrayList<>();
    private final ArrayList<Facet> columns = new ArrayList<>();
    private static final int DECIMALS = 1;
    private static final int ROUNDING_MODE = BigDecimal.ROUND_HALF_UP;
    
    private List<User> allUsers = new ArrayList<>();
    private List<Alpha> agentAlphas = new ArrayList<>();
    private List<Alpnumeric> userAlphas = new ArrayList<>();
    
    private List<creditRecord> allUsersC = null;
    
    public EmailUserScroller() {
//        initColumnsHeaders();
//        SortField[] fields = {new SortField("username", true)};
//        order.setFields(fields);
    }
    
    String users;
    
    String id;
    String selectedAlpha;
    String last;
    
    public void getUserr(ValueChangeEvent event) {
        users = event.getNewValue().toString();
        userS = users;
       // System.out.println("userrrrrrrr: " + user + ":::::" + this.UserID() + ":::");
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("resellerId", UserID());
        String lastusername = FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("username").toString();
        
    }

    //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
    //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
    public void getSelectedAlpha(ValueChangeEvent e) {
        selectedAlpha = e.getNewValue().toString();

        //System.out.println("The slected alpha is %%%%%%%" + selectedAlpha + "And the selected user is" + user + "  With id  " + this.UserID());
        try {
            
            boolean agentAvailable = agentLookUp(users);
            System.out.println(agent);
            
            if (selectedAlpha == "" || users == "") {
                
                JsfUtil.addErrorMessage("Agent or Alpha not Selected ");
                //System.out.println("Please select analpha");
            } else {
                conn = util.getConnectionTodbSMS();
                LOG.info("saveOrUpdateAlpha - UserScroller");
                AlphaServiceImpl service = new AlphaServiceImpl();
                service.updateAgentAlphas(this.UserID(), selectedAlpha, conn);
                // System.out.println("We are getting this far my friend");
                JsfUtil.addSuccessMessage("Agent alphanumeric saved successfully");
            }
            
        } catch (SQLException ex) {
            Logger.getLogger(UserScroller.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    //++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
    String agent;
    
    public boolean agentLookUp(String agent) {
        
        try {
            String sql = "SELECT username from tAllowedAlphanumerics where username='" + agent + "'";
            
            conn = util.getConnectionTodbSMS();
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(sql);
            
            while (rs.next()) {
                agent = rs.getString(1);
                //System.out.println("selected user id is::::" + user_id);
            }
            
        } catch (SQLException ex) {
            Logger.getLogger(UserScroller.class.getName()).log(Level.SEVERE, null, ex);
        }
        if (agent == null) {
            return false;
        } else {
            return true;
        }
        
    }
    
    public Paybill getPaybill() {
        return paybill;
    }
    
    public void setPaybill(Paybill paybill) {
        this.paybill = paybill;
    }
    
    public String getPassword() {
        return password;
    }
    
    public void setPassword(String password) {
        this.password = password;
    }
    
    public int savclicked = 0;
    
    public int getSavclicked() {
        return savclicked;
    }
    
    public void setSavclicked(int savclicked) {
        this.savclicked = savclicked;
    }
    
    public int getUserPlan() {
        return userPlan;
    }
    
    public void setUserPlan(int userPlan) {
        this.userPlan = userPlan;
    }
    
    public List<User> getAllUsers() throws SQLException {
        synchronized (this) {
            if (allUsers == null || allUsers.isEmpty()) {
                try {
                    conn = util.getConnectionTodbSMS();
                    LOG.info("getAllUsers");
                    allUsers = new ArrayList<>();
                    UserServiceApi userService = new UserServiceImpl();
                    allUsers = userService.getAllUsers(conn, userS);
                    LOG.info("ALL USERS EMPTY | NULL");
                    JdbcUtil.closeConnection(conn);
                } catch (SQLException e) {
                    JdbcUtil.printSQLException(e);
                }
            } else {
                allUsers = null;
                try {
                    conn = util.getConnectionTodbSMS();
                    LOG.info("getAllUsers");
                    allUsers = new ArrayList<>();
                    UserServiceApi userService = new UserServiceImpl();
                    allUsers = userService.getAllUsers(conn, userS);
                    LOG.info("ALL USERS EMPTY | NULL");
                    JdbcUtil.closeConnection(conn);
                } catch (SQLException e) {
                    JdbcUtil.printSQLException(e);
                }
            }
        }
        
        return allUsers;
    }
    
    
    
//    
//    
//    public List<User> getAllEmailUsers() throws SQLException {
//        synchronized (this) {
//            if (allUsers == null || allUsers.isEmpty()) {
//                try {
//                    conn = util.getConnectionTodbSMS();
//                    LOG.info("getAllEmailUsers");
//                    allUsers = new ArrayList<>();
//                    UserServiceApi userService = new UserServiceImpl();
//                    allUsers = userService.getAllEmailUsers(conn, userS);
//                    LOG.info("ALL USERS EMPTY | NULL");
//                    JdbcUtil.closeConnection(conn);
//                } catch (SQLException e) {
//                    JdbcUtil.printSQLException(e);
//                }
//            } else {
//                allUsers = null;
//                try {
//                    conn = util.getConnectionTodbSMS();
//                    LOG.info("getAllEmailUsers");
//                    allUsers = new ArrayList<>();
//                    UserServiceApi userService = new UserServiceImpl();
//                    allUsers = userService.getAllEmailUsers(conn, userS);
//                    LOG.info("ALL USERS EMPTY | NULL");
//                    JdbcUtil.closeConnection(conn);
//                } catch (SQLException e) {
//                    JdbcUtil.printSQLException(e);
//                }
//            }
//        }
//        
//        return getAllEmailUsers;
//    }
    
    
    
    
    
    public void removeUserAlpha() throws SQLException {
        
        if (alphanumeric != null) {
            System.out.println("Alpah to delete: " + alphanumeric);
            try {
                conn = util.getConnectionTodbSMS();
                userAlphas = new ArrayList<>();
                AlphaServiceApi userService = new AlphaServiceImpl();
                
                if (userService.removeUseAlpha(alphanumeric, userS, conn) > 0) {
                    JsfUtil.addSuccessMessage("Sender ID " + alphanumeric + " removed successfully");
                }
               // LOG.info("ALL ALPHAS EMPTY | NULL");
                JdbcUtil.closeConnection(conn);
            } catch (SQLException e) {
                JdbcUtil.printSQLException(e);
            }
        }
        
    }
    
    public String lastInsert() throws SQLException {
        String lname = "";
        UserServiceImpl us = new UserServiceImpl();
        JdbcUtil obj = new JdbcUtil();
        Connection con = obj.getConnectionTodbSMS();
        int id = us.getAutoId(con);
        int next = id + 1;
        
        String sql = "SELECT tUSER.id, tUSER.username, tUSER.password, tUSER.admin, tUSER.max_total,"
                + "tUSER.organization, tUSER.contact_number, tUSER.email_address, tUSER.enable_email_alert,"
                + "tUSER.end_date, tUSER.start_date, tUSER.alertThreshold,tUSER.cost_per_sms,tUSER.arrears,tAllowedAlphanumerics.alphanumeric, tAllowedAlphanumerics.id as alphaId FROM tUSER"
                + " LEFT JOIN tAllowedAlphanumerics on tUSER.username=tAllowedAlphanumerics.username where tUSER.admin != 1 and tUSER.id=" + id + "";
        
        PreparedStatement st = con.prepareStatement(sql);
        ResultSet rs = st.executeQuery(sql);
        //System.out.println(sql);
        //System.out.println("888888=" + id);
        if (rs.next()) {
            User si = new User();
            si.setId(rs.getLong("id"));
            si.setUsername(rs.getString("username"));
            si.setUserMobile("contact_number");
            //si.setCost_per_sms("cost_per_sms");
            //System.out.println("Last inserted " + rs.getString("username"));
            lname = rs.getString("username");
            allUsers.add(si);
        }
        return lname;
    }
    
    public List<User> getLastUSer() {
        
        synchronized (this) {
            if (allUsers == null || allUsers.isEmpty()) {
                try {
                    conn = util.getConnectionTodbSMS();
                    LOG.info("getAllUsers");
                    allUsers = new ArrayList<>();
                    UserServiceApi userService = new UserServiceImpl();
                    allUsers = userService.getLastCreated(conn, userS);
                    LOG.info("ALL USERS EMPTY | NULL");
                    JdbcUtil.closeConnection(conn);
                } catch (SQLException e) {
                    JdbcUtil.printSQLException(e);
                }
            } else {
                allUsers = null;
                try {
                    conn = util.getConnectionTodbSMS();
                    LOG.info("getAllUsers");
                    allUsers = new ArrayList<>();
                    UserServiceApi userService = new UserServiceImpl();
                    allUsers = userService.getLastCreated(conn, userS);
                    LOG.info("ALL USERS EMPTY | NULL");
                    JdbcUtil.closeConnection(conn);
                } catch (SQLException e) {
                    JdbcUtil.printSQLException(e);
                }
            }
        }
        return allUsers;
    }
    
    public List<User> getLasRes() {
        
        synchronized (this) {
            if (allUsers == null || allUsers.isEmpty()) {
                try {
                    conn = util.getConnectionTodbSMS();
                    LOG.info("getAllUsers");
                    allUsers = new ArrayList<>();
                    UserServiceApi userService = new UserServiceImpl();
                    allUsers = userService.getLastCreated(conn, userS);
                    LOG.info("ALL USERS EMPTY | NULL");
                    JdbcUtil.closeConnection(conn);
                } catch (SQLException e) {
                    JdbcUtil.printSQLException(e);
                }
            } else {
                allUsers = null;
                try {
                    conn = util.getConnectionTodbSMS();
                    LOG.info("getAllUsers");
                    allUsers = new ArrayList<>();
                    UserServiceApi userService = new UserServiceImpl();
                    allUsers = userService.getLastCreated(conn, userS);
                    LOG.info("ALL USERS EMPTY | NULL");
                    JdbcUtil.closeConnection(conn);
                } catch (SQLException e) {
                    JdbcUtil.printSQLException(e);
                }
            }
        }
        return allUsers;
    }
    
    public List<creditRecord> getAllUsersCred() {
        synchronized (this) {
            if (allUsersC == null || allUsersC.isEmpty()) {
                try {
                    conn = util.getConnectionTodbSMS();
                    LOG.info("getAllUsers");
                    allUsersC = new ArrayList<>();
                    UserServiceApi userService = new UserServiceImpl();
                    allUsersC = userService.getAllUsersCred(conn, userS);
                    System.out.println("test1: " + allUsersC);
                    LOG.info("ALL USERS EMPTY | NULL");
                    JdbcUtil.closeConnection(conn);
                } catch (SQLException e) {
                    JdbcUtil.printSQLException(e);
                }
            } else {
                allUsersC = null;
                try {
                    conn = util.getConnectionTodbSMS();
                    LOG.info("getAllUsers");
                    allUsersC = new ArrayList<>();
                    UserServiceApi userService = new UserServiceImpl();
                    allUsersC = userService.getAllUsersCred(conn, userS);
                    LOG.info("ALL USERS EMPTY | NULL");
                    JdbcUtil.closeConnection(conn);
                } catch (SQLException e) {
                    JdbcUtil.printSQLException(e);
                }
            }
        }
        
        return allUsersC;
    }
    
    public List<SelectItem> getPagesToScroll() {
        
        List<SelectItem> list = new ArrayList<SelectItem>();
        for (int i = 1; i <= allUsers.size() / getRows() + 1; i++) {
            if (Math.abs(i - scrollerPage) < 5) {
                SelectItem item = new SelectItem(i);
                list.add(item);
            }
        }
        return list;
    }
    
    public List<User> getTenRandomUsers() throws SQLException {
        
        List<User> result = new ArrayList<User>();
        int size = getAllUsers().size() - 1;
        for (int i = 0; i < 10; i++) {
            result.add(getAllUsers().get(rand(1, size)));
        }
        return result;
    }
    
    public int genRand() {
        
        return rand(1, 10000);
    }
    
    public List<User> createUser(String make, String model,
            int count) {
        
        ArrayList<User> iiList = null;
        
        try {
            int arrayCount = count;
            
            User[] demoInventoryItemArrays = new User[arrayCount];
            
            for (int j = 0; j < demoInventoryItemArrays.length; j++) {
                User ii = new User();
                
                demoInventoryItemArrays[j] = ii;
                
            }
            
            iiList = new ArrayList<User>(Arrays
                    .asList(demoInventoryItemArrays));
            
        } catch (Exception e) {
            System.out.println("!!!!!!createCategory Error: " + e.getMessage());
            e.printStackTrace();
        }
        return iiList;
    }
    
    public static int rand(int lo, int hi) {
        
        Random rn2 = new Random();
        int n = hi - lo + 1;
        int i = rn2.nextInt() % n;
        if (i < 0) {
            i = -i;
        }
        return lo + i;
    }
    
    public static String randomstring(int lo, int hi) {
        
        int n = rand(lo, hi);
        byte b[] = new byte[n];
        for (int i = 0; i < n; i++) {
            b[i] = (byte) rand('A', 'Z');
        }
        return new String(b);
    }
    
//    public SimpleSelection getSelection() {
//        
//        return selection;
//    }
//    
//    public void setSelection(SimpleSelection selection) {
//        this.selection = selection;
//    }
    
//    public String takeSelection() {
//        getSelectedCars().clear();
//        if (getSelection().isSelectAll()) {
//            getSelectedCars().addAll(allUsers);
//        } else {
//            Iterator<Object> iterator = getSelection().getKeys();
//            while (iterator.hasNext()) {
//                Object key = iterator.next();
//                table.setRowKey(key);
//                if (table.isRowAvailable()) {
//                    getSelectedCars().add(
//                            (User) table.getRowData());
//                }
//            }
//        }
//        return null;
//    }
//    
    public ArrayList<User> getSelectedCars() {
        
        return selectedCars;
    }
    
    public void setSelectedCars(ArrayList<User> selectedCars) {
        this.selectedCars = selectedCars;
    }
//    
//    public UIScrollableDataTable getTable() {
//        
//        return table;
//    }
//    
//    public void setTable(UIScrollableDataTable table) {
//        this.table = table;
//    }
//    
//    public void initColumnsHeaders() {
//        columns.clear();
//        
//    }
    
    public ArrayList<User[]> getModel() {
        
        if (model == null) {
            model = new ArrayList<User[]>();
            for (int i = 0; i < 9; i++) {
                User[] items = new User[6];
                
                model.add(items);
            }
        }
        return model;
    }
    
    public ArrayList<Facet> getColumns() {
        
        return columns;
    }
    
    public int getScrollerPage() {
        
        return scrollerPage;
    }
    
    public void setScrollerPage(int scrollerPage) {
        this.scrollerPage = scrollerPage;
    }
    
//    public SortOrder getOrder() {
//        
//        return order;
//    }
//    
//    public void setOrder(SortOrder order) {
//        this.order = order;
//    }
    
    public User getCurrentItem() {
        
        return currentItem;
    }
    
    public void setCurrentItem(User currentItem) {
        this.currentItem = currentItem;
    }
    
    public int getCurrentRow() {
        
        return currentRow;
    }
    
    public String getUserS() {
        //System.out.println("The selected user is : "+userS);

        String theuser = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("lastuser");
        if (theuser == null) {
            this.userS = userS;
        } else {
            this.userS = theuser;
        }
       
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("lastuser");
        
        return userS;
    }
    
    public void setUserS(String userS) {
        this.userS = userS;
    }
    
    public void setCurrentRow(int currentRow) {
        this.currentRow = currentRow;
    }
    
    public void store() {
        
        try {
            conn = util.getConnectionTodbSMS();
            LOG.info("storeUsers - UserScroller");
            allUsers.set(currentRow, currentItem);
            UserServiceApi service = new UserServiceImpl();
            AlphaServiceApi alphaService = new AlphaServiceImpl();
            currentItem.setEndDate(service.setEndDate());
            currentItem.setStartDate(new Date());
            currentItem.setAdmin(currentItem.getAdmin());
            
            alphaService.updateAlphaByUsername(currentItem.getPreviousUsername(), currentItem.getUsername(), conn);
            service.updateUser(currentItem, conn);
            JsfUtil.addSuccessMessage("User info updated successfully");
            
            keys.clear();
            keys.add(currentRow);
            JdbcUtil.closeConnection(conn);
            
        } catch (SQLException e) {
            JdbcUtil.printSQLException(e);
        }
    }
    
    public void saveOrUpdateAlpha(ValueChangeEvent event) {
        
        boolean exists;
        
        if (!userS.isEmpty() || userS != null) {
            try {
                conn = util.getConnectionTodbSMS();
                LOG.info("saveOrUpdateAlpha - UserScroller");
                AlphaServiceImpl service = new AlphaServiceImpl();
                String username = userS;
                String alphanumeric = event.getNewValue().toString();
                Long id = currentItem.getAlphaId();
                AlphaServiceImpl aService = new AlphaServiceImpl();
                String alphaType = aService.getAlphanumericType(alphanumeric, conn);
                
                Boolean checkExist = aService.findAlphanumericByUsername(username, alphanumeric, conn);
//                System.out.println("Check Exist:"+checkExist);
                if (!checkExist) {
                    if (service.persistAlpha(username, alphanumeric, alphaType, conn) > 0) {
                        JsfUtil.addSuccessMessage("User alphanumeric saved successfully");
                    } else {
                        JsfUtil.addSuccessMessage("User alphanumeric not saved ");
                    }
                } else {
                    JsfUtil.addSuccessMessage("The sender id is alredy assigned  to " + userS);
                }
                
                Alpha alpha = service.loadAlphanumericByUsername(username, conn);
                if (FacesContext.getCurrentInstance().getExternalContext().getSessionMap().containsKey("alphaScroller")) {
                    ((AlphaScroller) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("alphaScroller")).addAlphaToList(alpha);
                }

                //currentItem.setMessage("User alpha saved successfully.");
//                } else {
//                    service.updateAlpha(username, alphanumeric, alphaType, conn);
//                    JsfUtil.addSuccessMessage("User alphanumeric updated successfully");
//                    //currentItem.setMessage("User alpha updated successfully.");
//                }
                JdbcUtil.closeConnection(conn);
            } catch (SQLException e) {
                JdbcUtil.printSQLException(e);
            }
        } else {
            JsfUtil.addErrorMessage("Select user to assign Sender ID");
        }
        
    }
    
    public void delete() {
        allUsers.remove(currentRow);
    }
    
    public Set<Integer> getKeys() {
        return keys;
    }
    
    public void setKeys(Set<Integer> keys) {
        this.keys = keys;
    }
    
    public void addUserToList(User user) {
        allUsers.add(user);
    }
    
    public String showReseller() {
        return UserServiceImpl.isReseller();
    }
    
    public String showMyAccount() {
        //System.out.println(UserServiceImpl.showMyAccount());
        return UserServiceImpl.showMyAccount();
    }
    
    public String sendSMS() {
        String resellerSMS = "https://smsgateway.mspace.co.ke/newSMS/reseller.jsf?id=" + this.agentID();

        
        return resellerSMS;
    }
    
    public String agentID() {
        try {
            String sql = "SELECT id from tUSER where username='" + current_user + "'";
            
            conn = util.getConnectionTodbSMS();
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(sql);
            
            while (rs.next()) {
                user_id = rs.getInt(1);
                // System.out.println("selected user id is::::" + user_id);
            }
            
        } catch (SQLException ex) {
            Logger.getLogger(UserScroller.class.getName()).log(Level.SEVERE, null, ex);
        }
        return String.valueOf(user_id);
    }
    UserServiceImpl us = new UserServiceImpl();
    
    public BigInteger dailyTotal() {
        
        System.out.println("The current user agent id is  " + this.agentID());
        return us.smsSumary(this.agentID());
    }
    
    public BigInteger weeklyTotal() {
        return us.getWeeklyTotal();
    }
    
    public BigInteger grandTotal() {
        return us.getAllSentSms();
    }
    
    public BigInteger monthlyTotal() {
        return us.getMonthlyTotal();
    }
    
    public UserProfile getUserProfile() {
        UserServiceApi ap = new UserServiceImpl();
        //System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@");
        return ap.refreshProfile(current_user);
    }
    
    @Size(min = 5, message = "Password too short. Min length is {min} characters.")
    private String password = "";
    private String confirm = "";
    
    @AssertTrue(message = "Passwords entered do not match")
    public boolean isPasswordsEquals() {
        return password.equals(confirm);
    }
    
    public void storeNewPassword() {
        
        try {
            UserServiceApi usv = new UserServiceImpl();
            conn = util.getConnectionTodbSMS();
            usv.updatePassword(conn, password);
            JdbcUtil.closeConnection(conn);
        } catch (SQLException ex) {
            Logger.getLogger(UserScroller.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
    public void setConfirm(String confirm) {
        this.confirm = confirm;
    }
    
    public String getConfirm() {
        return confirm;
    }

    //=========================================================================================
    private int user_id;
    private String picPath;
    
    public int getUser_id() {
        System.out.println("(getteer)We getting here ");
        return user_id;
    }
    
    public void setUser_id(int user_id) throws SQLException {
        this.user_id = user_id;
        
    }
    
    public void storeResller() {
        
        try {
            conn = util.getConnectionTodbSMS();
            LOG.info("storeResller- Reseller");
            
            allUsers.set(currentRow, currentItem);
            UserServiceApi service = new UserServiceImpl();
            AlphaServiceApi alphaService = new AlphaServiceImpl();
            currentItem.setEndDate(service.setEndDate());
            currentItem.setStartDate(new Date());
            
            alphaService.updateAlphaByUsername(currentItem.getPreviousUsername(), currentItem.getUsername(), conn);
            service.updateUser(currentItem, conn);
            JsfUtil.addSuccessMessage("Reseller  info updated successfully");
            
            keys.clear();
            keys.add(currentRow);
            JdbcUtil.closeConnection(conn);
            
        } catch (SQLException e) {
            JdbcUtil.printSQLException(e);
        }
    }
    public String ulploadms = "";
    
    public String getUlploadms() {
        return ulploadms;
    }
    
    public void setUlploadms(String ulploadms) {
        this.ulploadms = ulploadms;
    }
    
    public String success;
    
    public String getSuccess() {
        return success;
    }
    
    public void setSuccess(String success) {
        this.success = success;
    }
    
    public void UploadComplete() {
        this.getUlploadms();
        
        JsfUtil.addSuccessMessage(this.getUlploadms());
        System.out.println("---------------------" + this.getUlploadms());
        
    }

    /*This function uploads the  the selected image to
    the temp foder in the tomcat diectory */
//    public void uploadImage(UploadEvent evt) throws SQLException, IOException {
//        System.out.println("The tomcat directory is:" + System.getProperty("catalina.base"));
//        //This method will be updating the tClient : adding an image path
//        System.out.println("Here we are :" + userS);
//        String selectedUser = userS;
//        
//        String sql = "SELECT id from tUSER where username='" + selectedUser + "'";
//        
//        conn = util.getConnectionTodbSMS();
//        Statement st = conn.createStatement();
//        ResultSet rs = st.executeQuery(sql);
//        
//        while (rs.next()) {
//            user_id = rs.getInt(1);
//        }
//        
//        picPath = evt.getUploadItem().getFile().getAbsolutePath();
//        
//        File original = new File(picPath);
//        UploadItem item = evt.getUploadItem();
//        System.out.println("This is the orginal file name :   " + item.getFileName() + " | content-type: " + item.getContentType());
//        File toRenameTo = new File(evt.getUploadItem().getFile().getParent() + "/" + item.getFileName());
//        
//        if (toRenameTo.exists()) {
//            System.out.println("File already exists " + toRenameTo.getAbsolutePath());
//        } else {
//            System.out.println("Creating file " + item.getFileName());
//            
//        }
//        
//        System.out.println(toRenameTo.getParent());
//        
//        System.out.println(evt.getUploadItem().getFile().getParentFile().getParentFile() + "/webapps/files/config");//username+logo
//        renameUploadedFile(original, toRenameTo);
//        
//        File inConfig = new File(evt.getUploadItem()
//                .getFile()
//                .getParentFile()
//                .getParentFile() + "/webapps/files/config/" + userS.concat("Logo")
//                        .replace(" ", "")
//                        .concat(getFileExtention(item.getFileName())));
//        toRenameTo.renameTo(inConfig);
//        if (inConfig.exists()) {
//            System.out.println("File sucessfuly  copied to  :" + inConfig.getName());
//        }
//        System.out.println("This will be uploaded to db : " + toRenameTo.getAbsolutePath());
//        String filedir = "../files/config/" + inConfig.getName();
//        System.out.println("Path do database is :  " + filedir);
//        System.out.println("Logo lacated in" + System.getProperty("catalina.home") + "/files/config/" + inConfig.getName());
//        String url = "";
//        logoLocation = System.getProperty("catalina.home") + "/files/config/" + selectedUser.concat("Logo")
//                .replace(" ", "")
//                .concat(getFileExtention(item.getFileName()));
//        
//        setImagePath(user_id, filedir);
//        JsfUtil.addSuccessMessage("User logo updated Sucessfully");
//        
//    }
    private String logoLocation = "";
    
    public String getLogoLocation() {
        return logoLocation;
    }
    
    public void setLogoLocation(String logoLocation) {
        this.logoLocation = logoLocation;
    }
    
//    public void uploadResImage(UploadEvent evt) throws SQLException, IOException {
//        //This method will be updating the tClient : adding an image path
//        System.out.println("Inside reseller set logo");
//        String selectedUser = this.current_user;
//        
//        String sql = "SELECT id from tUSER where username='" + selectedUser + "'";
//        
//        conn = util.getConnectionTodbSMS();
//        Statement st = conn.createStatement();
//        ResultSet rs = st.executeQuery(sql);
//        
//        while (rs.next()) {
//            user_id = rs.getInt(1);
//        }
//        util.closeConnection(conn);
//        //System.out.println("The current usr id is   :"+user_id);
//        picPath = evt.getUploadItem().getFile().getAbsolutePath();
//        // renameUploadedFile(picPath,new File(e.));printUser
//        File original = new File(picPath);
//        UploadItem item = evt.getUploadItem();
//        // System.out.println("This is the orginal file name :   " + item.getFileName() + " | content-type: " + item.getContentType());
//        File toRenameTo = new File(evt.getUploadItem().getFile().getParent() + "/" + item.getFileName());
//        
//        if (toRenameTo.exists()) {
//            //System.out.println("File already exists " + toRenameTo.getAbsolutePath());
//        } else {
//            //System.out.println("Creating file " + item.getFileName());
//
//        }
//        
//        System.out.println(toRenameTo.getParent());
//        //Rename the uploaded file 
//        System.out.println(evt.getUploadItem().getFile().getParentFile().getParentFile() + "/webapps/files/config");//username+logo
//        //logoLocation=evt.getUploadItem().getFile().getParentFile().getParentFile() + "/webapps/files/config/"+current_user+;
//        renameUploadedFile(original, toRenameTo);
//        //File inConfig= new File(evt.getUploadItem().getFile().getParentFile().getParentFile() + "/webapps/files/config/"+item.getFileName());
//        File inConfig = new File(evt.getUploadItem()
//                .getFile()
//                .getParentFile()
//                .getParentFile() + "/webapps/files/config/" + selectedUser.concat("Logo")
//                        .replace(" ", "")
//                        .concat(getFileExtention(item.getFileName())));
//        toRenameTo.renameTo(inConfig);
//        if (inConfig.exists()) {
//            // System.out.println("File sucessfuly  copied to  :" + inConfig.getName());
//        }
//        // System.out.println("This will be uploaded to db : " + toRenameTo.getAbsolutePath());
//        logoLocation = System.getProperty("catalina.home") + "/files/config/" + selectedUser.concat("Logo")
//                .replace(" ", "")
//                .concat(getFileExtention(item.getFileName()));
//        //System.out.println("The File is located in:  " + logoLocation);
//        String filedir = "../files/config/" + inConfig.getName();
//        //System.out.println("Path do database is :  " + filedir);
//        //Call the method that persistsheimage pathto db
//        setImagePath(user_id, filedir);
//        
//        JsfUtil.addSuccessMessage("User logo updated Sucessfully");
//        
//    }
    
    public void showSuccessMessage() {
        // System.out.println("The Message::::::");
        JsfUtil.addSuccessMessage("User logo updated Sucessfully");
    }

    /*This method will update the tClient picPath 
    column with the path from the temp folder*/
    public void setImagePath(int id, String picPath) throws SQLException {
        String sqlUpdate = "UPDATE dbTASK.tClient  set picPath= ? where id='" + id + "' ";
        conn = util.getConnectionTodbPAYMENT();
        PreparedStatement pstm = conn.prepareStatement(sqlUpdate);
        //System.out.println("The update querry ====>" + sqlUpdate);
        //System.out.println("image to be inserted is ====>" + picPath);
        pstm.setString(1, picPath);
        int ex = pstm.executeUpdate();
        
        if (ex == 1) {
            
            ulploadms = "Logo updated Successfuy";
            JsfUtil.addSuccessMessage("User logo updated Sucessfully");
            //System.out.println("Success::::: imageuploaded   " + ex);
        } else {
            //System.out.println("Error Uploading image!    " + ex);
            JsfUtil.addSuccessMessage("User logo updated Sucessfully");
            
        }
        JsfUtil.addSuccessMessage(ulploadms);
    }

    /*This function  renames the uploaded file on the tomcat
    temp folder  to the original filename*/
    public void renameUploadedFile(File original_file, File newfile) throws IOException {
        // this.original=original_file;

        if (original_file.exists()) {
            
            System.out.println("File found");
        } else {
            System.out.println("File not found");
        }
        boolean success = original_file.renameTo(newfile);
        
        System.out.println(success);
    }

    //=======================================================================================================
    public List<User> getAllUserByAgent() {
        
        synchronized (this) {
            if (allUsers == null || allUsers.isEmpty()) {
                try {
                    conn = util.getConnectionTodbSMS();
                    LOG.info("getAllUsers");
                    allUsers = new ArrayList<>();
                    UserServiceApi userService = new UserServiceImpl();
                    allUsers = userService.getAllUserPerAgent(conn, userS);
                    System.out.println("My name is:" + userS);
                    LOG.info("ALL USERS EMPTY | NULL");
                    JdbcUtil.closeConnection(conn);
                } catch (SQLException e) {
                    JdbcUtil.printSQLException(e);
                }
            } else {
                allUsers = null;
                try {
                    conn = util.getConnectionTodbSMS();
                    LOG.info("getAllUsers");
                    allUsers = new ArrayList<>();
                    UserServiceApi userService = new UserServiceImpl();
                    allUsers = userService.getAllUserPerAgent(conn, userS);
                    LOG.info("ALL USERS EMPTY | NULL");
                    JdbcUtil.closeConnection(conn);
                } catch (SQLException e) {
                    JdbcUtil.printSQLException(e);
                }
            }
        }
        
        return allUsers;
    }

    //=====================================================================================
    public String showCreditReport() {
        return UserServiceImpl.showCrediHistoryReport();
    }
    
    public String showMpesaMenu() {
        return UserServiceImpl.showMpesaMenu;
    }
    
    public String showResellerMenu() {
//        String user=FacesContext.getCurrentInstance().getExternalContext().getUserPrincipal().getName();
//        System.out.println(user);
        return UserServiceImpl.showManageReseller();
        
    }
    
    private String getFileExtention(String filename) {
        return filename.contains(".") ? filename.substring(filename.indexOf("."), filename.length()) : filename;
    }

    // ============================================================================================
    public void getAlphas() throws SQLException {
        String sql = "";
        PreparedStatement pstmt = conn.prepareStatement(sql);
        
    }
    
    public String UserID() {
        try {
            String sql = "SELECT id from tUSER where username='" + userS + "'";
            
            conn = util.getConnectionTodbSMS();
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(sql);
            
            while (rs.next()) {
                user_id = rs.getInt(1);
                // System.out.println("selected user id is::::" + user_id);
            }
            
        } catch (SQLException ex) {
            Logger.getLogger(UserScroller.class.getName()).log(Level.SEVERE, null, ex);
        }
        return String.valueOf(user_id);
    }
    //=================================================================================== 

    public float [] availableCredits(Connection conn) {
        float available_credits []= new float[2];
        try {
            conn = util.getConnectionTodbSMS();
            AlphaScroller ac = new AlphaScroller();
            String user = ac.currentUSer();
            String sql = "Select max_total,cost_per_sms from tUSER where username='" + user + "' ";
            //System.out.println(sql);
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(sql);
            
            while (rs.next()) {
                available_credits[0] = rs.getInt(1);
                available_credits[1]=rs.getFloat(2);
            }
        } catch (SQLException ex) {
            Logger.getLogger(UserServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        System.out.println("Original Cost Per SMS:"+available_credits[1]+" Available Credits:"+available_credits[0]);
        return available_credits;
    }
        
        
//    public float [] availableEmailCredits(Connection conn) {
//        float available_credits []= new float[2];
//        try {
//            conn = util.getConnectionTodbSMS();
//            AlphaScroller ac = new AlphaScroller();
//            String user = ac.currentUSer();
//            String sql = "Select max_contacts,cost_per_sms from tUSER where username='" + user + "' ";
//            //System.out.println(sql);
//            Statement st = conn.createStatement();
//            ResultSet rs = st.executeQuery(sql);
//            
//            while (rs.next()) {
//                available_credits[0] = rs.getInt(1);
//                available_credits[1]=rs.getFloat(2);
//            }
//        } catch (SQLException ex) {
//            Logger.getLogger(UserServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
//        }
//        System.out.println("Original Cost Per SMS:"+available_credits[1]+" Available Credits:"+available_credits[0]);
//        return available_credits;
//    }    
        
        
        
        
    
    public String userSelectedUSerID() {
        return this.UserID();
    }

    ///++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
    public List<Alpha> getAgentAlphas() {
        
        synchronized (this) {
            if (allUsers != null && !allUsers.isEmpty()) {
                try {
                    //String username=currentItem.getUsername();
                    conn = util.getConnectionTodbSMS();
                    LOG.info("getAllAlphas");
                    UserServiceApi service = new UserServiceImpl();
                    agentAlphas = service.getAgentAlphas(conn, this.UserID());

                    // System.out.println("this.UserID(): " + this.UserID());
                    //agentAlphas = service.getAgentAlphas(conn,id);
//
//                    Object reselObj = FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("resellerId");
//
//                    
//                    int lResellerId = 0;
//                    
//                    if (reselObj != null) {
//                        lResellerId = (Integer) reselObj;
//                    }
//
                    // System.out.println("Am being executed....yes I was made to be executed");
                    JdbcUtil.closeConnection(conn);
                } catch (SQLException e) {
                    JdbcUtil.printSQLException(e);
                }
            }
        }
        
        return agentAlphas;
    }
    //++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++ 

    public void updateAgentAlpha() {
        try {
            
            conn = util.getConnectionTodbSMS();
            LOG.info("saveOrUpdateAlpha - UserScroller");
            AlphaServiceImpl service = new AlphaServiceImpl();
            String username = currentItem.getUsername();
            String alphanumeric = currentItem.getAlphanumeric();
            Long id = currentItem.getAlphaId();
            AlphaServiceImpl aService = new AlphaServiceImpl();
            
            String alphaType = aService.getAlphanumericType(alphanumeric, conn);
            service.persistAlpha(username, alphanumeric, alphaType, conn);
        } catch (SQLException ex) {
            Logger.getLogger(UserScroller.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
    public void deleteUser() {
        try {
            
            conn = util.getConnectionTodbSMS();
            
            LOG.info("deleteAlphanumeric");
            UserServiceImpl service = new UserServiceImpl();
            service.deleteUser(currentItem, conn);
            
            JsfUtil.addSuccessMessage("User " + currentItem.getUsername() + "  has been removed successfully.");

            //allUsers.remove(currentItem);
            JdbcUtil.closeConnection(conn);
        } catch (SQLException ex) {
            JdbcUtil.printSQLException(ex);
            
        }
        
    }
    
    
    public void deleteEmailUser() {
        try {
            
            conn = util.getConnectionTodbSMS();
            
            LOG.info("deleteAlphanumeric");
            UserServiceImpl service = new UserServiceImpl();
            service.deleteUser(currentItem, conn);
            
            JsfUtil.addSuccessMessage("User " + currentItem.getUsername() + "  has been removed successfully.");

            //allUsers.remove(currentItem);
            JdbcUtil.closeConnection(conn);
        } catch (SQLException ex) {
            JdbcUtil.printSQLException(ex);
            
        }
        
    }
    
    
    public void updateUserEmails() {
        try {
            
            conn = util.getConnectionTodbSMS();
            
            LOG.info("Update user Emails");
            UserServiceImpl service = new UserServiceImpl();
            Calendar cal = Calendar.getInstance();
            cal.add(Calendar.DATE, this.currentItem.getEmailPlan() * 30);
            cal.getTime();
            
            int success = service.UpdateUserMaxContacts(currentItem, currentItem.getMaxContacts(), currentItem.getStartDate(), cal.getTime(), conn);
            
            if (success > 0) {
                JsfUtil.addSuccessMessage("User " + currentItem.getUsername() + " Bulk Emails subscription renewed successfully ");
            }

            //allUsers.remove(currentItem);
            JdbcUtil.closeConnection(conn);
        } catch (SQLException ex) {
            JdbcUtil.printSQLException(ex);
            
        }
        
    }
    
    public void deleteReseller() {
        try {
            
            conn = util.getConnectionTodbSMS();
            
            LOG.info("deleteAlphanumeric");
            UserServiceImpl service = new UserServiceImpl();
            service.deleteUser(currentItem, conn);
            
            JsfUtil.addSuccessMessage("Reseller " + currentItem.getUsername() + "  has been removed successfully.");

            //allUsers.remove(currentItem);
            JdbcUtil.closeConnection(conn);
        } catch (SQLException ex) {
            JdbcUtil.printSQLException(ex);
            
        }
        
    }
    
    public void addPaybill() {
        String user = this.userS;
        try {
            this.conn = this.util.getConnectionTodbPAYMENT();
            this.conn2 = this.util.getConnectionTodbSMS();
            UserServiceImpl paybill2 = new UserServiceImpl();
            if (this.paybill == null) {
                System.out.println("THE PAY BILL IS NULL");
            }
            paybill2.persistPaybill(this.paybill, this.conn, this.conn2);
            JsfUtil.addSuccessMessage("Paybill added successfully!");
            this.paybill = new Paybill();
            this.userS = "";
            JdbcUtil.closeConnection(this.conn);
            JdbcUtil.closeConnection(this.conn2);
        } catch (SQLException e) {
            JdbcUtil.printSQLException(e);
        }
    }
    
    public void deletePaybill() {
        try {
            this.conn = this.util.getConnectionTodbPAYMENT();
            UserServiceImpl paybill2 = new UserServiceImpl();
            //System.out.println("CURRENT ITEM"+this.currentItem);
            paybill2.deletePaybill(this.currentItem1, this.conn);
            JsfUtil.addSuccessMessage("Paybill deleted successfully!");
            
            JdbcUtil.closeConnection(this.conn);
        } catch (SQLException e) {
            JdbcUtil.printSQLException(e);
        }
    }
    
    
    public void updatePaybill() {
        try {
            this.conn = this.util.getConnectionTodbPAYMENT();
            UserServiceImpl paybill2 = new UserServiceImpl();
            paybill2.updatePaybill(this.paybill, this.conn);
            JsfUtil.addSuccessMessage("Paybill updated successfully!");
            
            JdbcUtil.closeConnection(this.conn);
        } catch (SQLException e) {
            JdbcUtil.printSQLException(e);
        }
    }
    
    public void finduserId(ValueChangeEvent event) {
        String uservalue = event.getNewValue().toString();
        
        System.out.println("user: " + uservalue);
        int id = 0;
        String username = "";
        try {
            Connection con2 = this.util.getConnectionTodbSMS();
            String sql = "SELECT * FROM tUSER WHERE username=?";
            PreparedStatement ps = con2.prepareStatement(sql);
            ps.setString(1, uservalue);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                id = rs.getInt("id");
                username = rs.getString("username");
                // System.out.println(id);
                this.callback.setUserid(id);
                this.paybill.setUserid(id);
                this.paybill.setName(username);
            }

            //System.out.println("THE ID"+this.paybill.getUserid()+"THE USERNAME: "+this.paybill.getName());
        } catch (SQLException ex) {
            System.out.println("Error getting user id due to ::" + ex);
        }
    }
    public List<Alpnumeric> getUserAlphas() throws SQLException {
        synchronized (this) {
            if (userAlphas == null || userAlphas.isEmpty()) {
                try {
                    conn = util.getConnectionTodbSMS();
                   // LOG.info("getAllUsers");
                    userAlphas = new ArrayList<>();
                    UserServiceApi userService = new UserServiceImpl();
                    userAlphas = userService.getUserAlphas(conn, userS);
                    //.info("ALL ALPHAS EMPTY | NULL");
                    JdbcUtil.closeConnection(conn);
                } catch (SQLException e) {
                    JdbcUtil.printSQLException(e);
                }
            } else {
                userAlphas = null;
                try {
                    conn = util.getConnectionTodbSMS();
                    //LOG.info("Get " + userS + "'s Alphas");
                    userAlphas = new ArrayList<>();
                    UserServiceApi userService = new UserServiceImpl();
                    userAlphas = userService.getUserAlphas(conn, userS);
                    JdbcUtil.closeConnection(conn);
                } catch (SQLException e) {
                    JdbcUtil.printSQLException(e);
                }
            }
        }
        
        return userAlphas;
    }
    
    public String loadViewPaybills() {
        return "/manager/viewPaybill.jsp";
    }

   
    public String loadViewCallBacks() {
        return "/manager/viewCallBack.jsp";
    }

    /**
     * @return the callback
     */
    public CallBack getCallback() {
        return callback;
    }

    /**
     * @param callback the callback to set
     */
    public void setCallback(CallBack callback) {
        this.callback = callback;
    }
}
