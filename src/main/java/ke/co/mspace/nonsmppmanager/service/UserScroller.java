/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ke.co.mspace.nonsmppmanager.service;

import ke.co.mspace.nonsmppmanager.invalids.FacePainter;
import com.sun.faces.context.FacesContextImpl;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
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
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Stream;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.component.UIComponent;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;
import javax.faces.event.AbortProcessingException;
import javax.faces.event.ActionEvent;
import javax.faces.event.AjaxBehaviorEvent;
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
import org.mspace.clientmanager.user.UserController;
import ke.co.mspace.nonsmppmanager.model.UserProfile;
import ke.co.mspace.nonsmppmanager.model.creditRecord;
import ke.co.mspace.nonsmppmanager.util.JdbcUtil;
import ke.co.mspace.nonsmppmanager.util.JsfUtil;
import ke.co.mspace.nonsmppmanager.util.SessionUtil;
import org.apache.commons.lang3.StringUtils;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;

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
@ManagedBean
@ViewScoped
public class UserScroller {
   public static int maxUsernameLength=20;
    private Paybill paybill;
    private UserController user;
    
    private CallBack callback=new CallBack();
    // Paybill paybill= new Paybill();
    private Alpha pCurrentAlpha;
    private UserController currentItem = new UserController();
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
     private String currentAlphanumeric;
    private Long alphid;
    public String userS;
    public String emailUsers;
    public UploadedFile file;
      public void upload() {
        if (file != null) {
            FacesMessage message = new FacesMessage("Successful", file.getFileName() + " is uploaded.");
            FacesContext.getCurrentInstance().addMessage(null, message);
        }
    }

    public UploadedFile getFile() {
        return file;
    }
   public String abbreviate(String str){
      return  StringUtils.abbreviate(str,maxUsernameLength);
    }
    public void setFile(UploadedFile file) {
        this.file = file;
    }
    

    
    private List<UserController> getAllEmailUsers;

    
      @ManagedProperty(value = "#{facePainter}")
    public FacePainter facePainter;

    public FacePainter getFacePainter() {
        return facePainter;
    }

    public void setFacePainter(FacePainter facePainter) {
        this.facePainter = facePainter;
    }
  
    @PostConstruct
    public void init() {
        this.user = new UserController();
        this.paybill = new Paybill();
        this.callback =new CallBack();
        fetchAllUsers();
    }

    public Alpha getpCurrentAlpha() {
        return pCurrentAlpha;
    }

    public void setpCurrentAlpha(Alpha pCurrentAlpha) {
        this.pCurrentAlpha = pCurrentAlpha;
    }
    
    

    public void psetCurrentAlphanumeric(String str){
              
        setCurrentAlphanumeric(str);
    }
    
     public void psetCurrentAlphanumericObject(Alpha alpha){
              
         setpCurrentAlpha(alpha);
    }
    public String getCurrentAlphanumeric() {
        return currentAlphanumeric;
    }

    public void setCurrentAlphanumeric(String currentAlphanumeric) {
  
        this.currentAlphanumeric = currentAlphanumeric;
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
    
    public String getEmailUsers() {
        return emailUsers;
    }

    public void setEmailUsers(String emailUsers) {
        this.emailUsers = emailUsers;
    }
    
    
    
    
    public void fetchCurrentRow(ActionEvent event) {
        String username = (FacesContext.getCurrentInstance().
                getExternalContext().getRequestParameterMap().get("username"));
        currentRow = Integer.parseInt(FacesContext.getCurrentInstance().
                getExternalContext().getRequestParameterMap().get("row"));
        for (UserController item : allUsers) {
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
    
    private ArrayList<UserController[]> model = null;
    
    private ArrayList<UserController> selectedCars = new ArrayList<>();
    private final ArrayList<Facet> columns = new ArrayList<>();
    private static final int DECIMALS = 1;
    private static final int ROUNDING_MODE = BigDecimal.ROUND_HALF_UP;
    
    private List<UserController> allUsers = new ArrayList<>();
    private List<Alpha> agentAlphas = new ArrayList<>();
    private List<Alpnumeric> userAlphas = new ArrayList<>();
    
    private List<creditRecord> allUsersC = null;
    
    public UserScroller() {
        initColumnsHeaders();
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
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("resellerId", UserID());
        String lastusername = FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("username").toString();
        
    }
    public void addAgentAlphanumeric(ValueChangeEvent event){
          AlphaServiceImpl asi = new AlphaServiceImpl();
        
        List<String> myAlphanumerics = new ArrayList<>();
        try {
            conn = util.getConnectionTodbSMS();
            asi.updateAgentAlphas(userS, event.getNewValue().toString(), conn);
            JdbcUtil.closeConnection(conn);
        } catch (SQLException e) {
            JdbcUtil.closeConnection(conn);
        }
        
    }
     public void usersValueChangeListener(ValueChangeEvent event) {
            userS= event.getNewValue().toString();
                     System.out.println("new change "+userS);
                    

            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("resellerId", Integer.parseInt(UserID()));
        String lastusername = FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("username").toString();
          
             fetchAllUsers();
             fetchUserAlphas();
             fetchAgentAlphas();
            
        
    }
        public void usersValueChangeListenerForCreditHistory(ValueChangeEvent event) {
      
            userS= event.getNewValue().toString();
            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("resellerId", Integer.parseInt(UserID()));
        String lastusername = FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("username").toString();
          
            
            
        
    }

    //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
    //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
    public void getSelectedAlpha(ValueChangeEvent e) {
        selectedAlpha = e.getNewValue().toString();

        //System.out.println("The slected alpha is %%%%%%%" + selectedAlpha + "And the selected user is" + user + "  With id  " + this.UserID());
        try {
            
            boolean agentAvailable = agentLookUp(users);
           
            
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
        public void modifySelectedAlpha(ValueChangeEvent e) {
        selectedAlpha = e.getNewValue().toString();

        //System.out.println("The slected alpha is %%%%%%%" + selectedAlpha + "And the selected user is" + user + "  With id  " + this.UserID());
        try {
            
            boolean agentAvailable = agentLookUp(users);
           
            
            if (selectedAlpha == "" || users == "") {
                
                JsfUtil.addErrorMessage("Agent or Alpha not Selected ");
                //System.out.println("Please select analpha");
            } else {
                conn = util.getConnectionTodbSMS();
                AlphaServiceImpl service = new AlphaServiceImpl();
                service.updateAgentAlphas(this.UserID(), selectedAlpha, conn);
                // System.out.println("We are getting this far my friend");
                JsfUtil.addSuccessMessage("Agent alphanumeric saved successfully");
            }
            
        } catch (SQLException ex) {
            Logger.getLogger(UserScroller.class.getName()).log(Level.SEVERE, null, ex);
        }
//        fetchUserAlphas();
        fetchAgentAlphas();
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
    
    public List<UserController> getAllUsers() throws SQLException {
//        this
        
//        fetchAllUsers();
        return allUsers;
    }
    
    public void fetchAllUsers(){
        synchronized (this) {
            if (allUsers == null || allUsers.isEmpty()) {
                try {
                    conn = util.getConnectionTodbSMS();
                    LOG.info("getAllUsers");
                    allUsers = new ArrayList<>();
                    UserServiceApi userService = new UserServiceImpl();
                    allUsers = userService.getAllUsers(conn, userS);
//                    LOG.info("ALL USERS EMPTY | NULL");
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
//                    LOG.info("ALL USERS EMPTY | NULL");
                    JdbcUtil.closeConnection(conn);
                } catch (SQLException e) {
                    JdbcUtil.printSQLException(e);
                }
            }
        }
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
        if (currentAlphanumeric != null) {
            try {
                conn = util.getConnectionTodbSMS();
                userAlphas = new ArrayList<>();
                AlphaServiceApi userService = new AlphaServiceImpl();
                
                if (userService.removeUseAlpha(currentAlphanumeric, userS, conn) > 0) {
                    JsfUtil.addSuccessMessage("Sender ID " + currentAlphanumeric + " removed successfully");
                }
               // LOG.info("ALL ALPHAS EMPTY | NULL");
                JdbcUtil.closeConnection(conn);
            } catch (SQLException e) {
                JdbcUtil.printSQLException(e);
                e.printStackTrace();
            }
        }
        fetchUserAlphas();
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
            UserController si = new UserController();
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
    
    public List<UserController> getLastUSer() {
        
        synchronized (this) {
            if (allUsers == null || allUsers.isEmpty()) {
                try {
                    conn = util.getConnectionTodbSMS();
//                    LOG.info("getAllUsers");
                    allUsers = new ArrayList<>();
                    UserServiceApi userService = new UserServiceImpl();
                    allUsers = userService.getLastCreated(conn, userS);
//                    LOG.info("ALL USERS EMPTY | NULL");
                    JdbcUtil.closeConnection(conn);
                } catch (SQLException e) {
                    JdbcUtil.printSQLException(e);
                }
            } else {
                allUsers = null;
                try {
                    conn = util.getConnectionTodbSMS();
//                    LOG.info("getAllUsers");
                    allUsers = new ArrayList<>();
                    UserServiceApi userService = new UserServiceImpl();
                    allUsers = userService.getLastCreated(conn, userS);
//                    LOG.info("ALL USERS EMPTY | NULL");
                    JdbcUtil.closeConnection(conn);
                } catch (SQLException e) {
                    JdbcUtil.printSQLException(e);
                }
            }
        }
        return allUsers;
    }
    
    public List<UserController> getLasRes() {
        
        synchronized (this) {
            if (allUsers == null || allUsers.isEmpty()) {
                try {
                    conn = util.getConnectionTodbSMS();
//                    LOG.info("getAllUsers");
                    allUsers = new ArrayList<>();
                    UserServiceApi userService = new UserServiceImpl();
                    allUsers = userService.getLastCreated(conn, userS);
//                    LOG.info("ALL USERS EMPTY | NULL");
                    JdbcUtil.closeConnection(conn);
                } catch (SQLException e) {
                    JdbcUtil.printSQLException(e);
                }
            } else {
                allUsers = null;
                try {
                    conn = util.getConnectionTodbSMS();
//                    LOG.info("getAllUsers");
                    allUsers = new ArrayList<>();
                    UserServiceApi userService = new UserServiceImpl();
                    allUsers = userService.getLastCreated(conn, userS);
//                    LOG.info("ALL USERS EMPTY | NULL");
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
                    allUsersC = new ArrayList<>();
                    UserServiceApi userService = new UserServiceImpl();
                    allUsersC = userService.getAllUsersCred(conn, userS);
                   
                    JdbcUtil.closeConnection(conn);
                } catch (SQLException e) {
                    JdbcUtil.printSQLException(e);
                }
            } else {
                allUsersC = null;
                try {
                    conn = util.getConnectionTodbSMS();
                    allUsersC = new ArrayList<>();
                    UserServiceApi userService = new UserServiceImpl();
                    allUsersC = userService.getAllUsersCred(conn, userS);
//                    LOG.info("ALL USERS EMPTY | NULL");
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
    
    public List<UserController> getTenRandomUsers() throws SQLException {
        
        List<UserController> result = new ArrayList<UserController>();
        int size = getAllUsers().size() - 1;
        for (int i = 0; i < 10; i++) {
            result.add(getAllUsers().get(rand(1, size)));
        }
        return result;
    }
    
    public int genRand() {
        
        return rand(1, 10000);
    }
    
    public List<UserController> createUser(String make, String model,
            int count) {
        
        ArrayList<UserController> iiList = null;
        
        try {
            int arrayCount = count;
            
            UserController[] demoInventoryItemArrays = new UserController[arrayCount];
            
            for (int j = 0; j < demoInventoryItemArrays.length; j++) {
                UserController ii = new UserController();
                
                demoInventoryItemArrays[j] = ii;
                
            }
            
            iiList = new ArrayList<UserController>(Arrays
                    .asList(demoInventoryItemArrays));
            
        } catch (Exception e) {
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
//                            (UserController) table.getRowData());
//                }
//            }
//        }
//        return null;
//    }
    
    public ArrayList<UserController> getSelectedCars() {
        
        return selectedCars;
    }
    
    public void setSelectedCars(ArrayList<UserController> selectedCars) {
        this.selectedCars = selectedCars;
    }
    
//    public UIScrollableDataTable getTable() {
//        
//        return table;
//    }
//    
//    public void setTable(UIScrollableDataTable table) {
//        this.table = table;
//    }
//    
    public void initColumnsHeaders() {
        columns.clear();
        
    }
    
    public ArrayList<UserController[]> getModel() {
        
        if (model == null) {
            model = new ArrayList<UserController[]>();
            for (int i = 0; i < 9; i++) {
                UserController[] items = new UserController[6];
                
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
//    
//    public SortOrder getOrder() {
//        
//        return order;
//    }
//    
//    public void setOrder(SortOrder order) {
//        this.order = order;
//    }
    
    public void pSetCurrentItem(UserController user){
    this.currentItem=user;   
        
    }
      public void processAction(ActionEvent ae) throws AbortProcessingException {
    }
    
    public UserController getCurrentItem() {
        return currentItem;
    }
    
    public void setCurrentItem(UserController currentItem) {
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
            this.userS = "";
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
        
                   AlphaServiceImpl asi = new AlphaServiceImpl();

        
            conn = util.getConnectionTodbSMS();
            currentItem.setSelectedGroup(asi.getGroup(currentItem.getGroup(), conn));
            JdbcUtil.closeConnection(conn);
       
        
        try {
            conn = util.getConnectionTodbSMS();
//            LOG.info("storeUsers - UserScroller");
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

                //currentItem.setMessage("UserController alpha saved successfully.");
//                } else {
//                    service.updateAlpha(username, alphanumeric, alphaType, conn);
//                    JsfUtil.addSuccessMessage("UserController alphanumeric updated successfully");
//                    //currentItem.setMessage("UserController alpha updated successfully.");
//                }
                JdbcUtil.closeConnection(conn);
            } catch (SQLException e) {
                JdbcUtil.printSQLException(e);
            }
        } else {
            JsfUtil.addErrorMessage("Select user to assign Sender ID");
        }
//        fetchAgentAlphas();
fetchUserAlphas();
        
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
    
    public void addUserToList(UserController user) {
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
        @Size(min = 5, message = "Password too short. Min length is {min} characters.")
    private String confirm = "";
    
    @AssertTrue(message = "Passwords entered do not match")
    public boolean isPasswordsEquals() {
        return password.equals(confirm);
    }
    
    public void storeNewPassword() {
//         if (!password.equals(confirm)) {
//             System.out.println("klog not equal");
//             FacesContext context = FacesContext.getCurrentInstance();
//            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Password and Confirm Password must match.", null);
//            context.addMessage("passwordmsgs", message);
//            return;
//        }
        
        try {
            UserServiceApi usv = new UserServiceImpl();
            conn = util.getConnectionTodbSMS();
            usv.updatePassword(conn, password);
            JdbcUtil.closeConnection(conn);
        } catch (SQLException ex) {
            Logger.getLogger(UserScroller.class.getName()).log(Level.SEVERE, null, ex);
        }
        this.confirm="";
        this.password="";
//        JsfUtil.addSuccessMessage("password updated succesfully");
        
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
            service.updateUserWithoutGroup(currentItem, conn);
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
        
    }

   
    private String logoLocation = "";
    
    public String getLogoLocation() {
        return logoLocation;
    }
    
    public void setLogoLocation(String logoLocation) {
        this.logoLocation = logoLocation;
    }
    
    public void uploadResImage(FileUploadEvent event) throws SQLException, IOException {
        System.out.println("method called");
        String selectedUser = this.current_user;
        
        String sql = "SELECT id from tUSER where username='" + selectedUser + "'";
        
        conn = util.getConnectionTodbSMS();
        Statement st = conn.createStatement();
        ResultSet rs = st.executeQuery(sql);
        
        while (rs.next()) {
            user_id = rs.getInt(1);
        }
        util.closeConnection(conn);
             UploadedFile uploadedFile = event.getFile();
 System.out.println("method called id "+user_id);
        String directory = System.getProperty("catalina.home") + "/webapps/files/config"; // Replace with your actual directory path

   System.out.println("method called dir "+directory);
  
  
        try {
            File fileDir = new File(directory);
            if (!fileDir.exists()) {
                fileDir.mkdirs();
            }

            String fileName = uploadedFile.getFileName();
            String uniqueFileName = fileName; // You can modify this to generate a unique name

            String absoluteFilePath = directory + File.separator +  selectedUser.concat("Logo").replace(" ", "")
                    
                    .concat(getFileExtention(uploadedFile.getFileName()));
                    ;
            InputStream input = uploadedFile.getInputstream();
            FileOutputStream output = new FileOutputStream(absoluteFilePath);

            int read;
            byte[] bytes = new byte[1024];
            while ((read = input.read(bytes)) != -1) {
                output.write(bytes, 0, read);
            }

            input.close();
            output.close();

            // Now, absoluteFilePath contains the absolute path to the uploaded file
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        String filedir = "/files/config/" + selectedUser.concat("Logo").replace(" ", "")
                    
                    .concat(getFileExtention(uploadedFile.getFileName()));
        //Call the method that persistsheimage pathto db
        setImagePath(user_id, filedir);
         System.out.println("method called file dir"+filedir);
        JsfUtil.addSuccessMessage("User logo updated Sucessfully");
        
    }
    
    public void showSuccessMessage() {
        // System.out.println("The Message::::::");
        JsfUtil.addSuccessMessage("User logo updated Sucessfully");
    }

    /*This method will update the tClient picPath 
    column with the path from the temp folder*/
    public void setImagePath(int id, String picPath) throws SQLException {
        String sqlUpdate = "UPDATE dbTASK.tClient  set picPath= ? where id='" + id + "' ";
        conn = util.getConnectionTodbTask();
        PreparedStatement pstm = conn.prepareStatement(sqlUpdate);
        //System.out.println("The update querry ====>" + sqlUpdate);
        //System.out.println("image to be inserted is ====>" + picPath);
        pstm.setString(1, picPath);
        int ex = pstm.executeUpdate();
        
        if (ex == 1) {
            
            ulploadms = "Logo updated Successfuy";
//            JsfUtil.addSuccessMessage("UserController logo updated Sucessfully");
            //System.out.println("Success::::: imageuploaded   " + ex);
        } else {
            //System.out.println("Error Uploading image!    " + ex);
//            JsfUtil.addSuccessMessage("UserController logo updated Sucessfully");
            
        }
//        JsfUtil.addSuccessMessage(ulploadms);
    }

    /*This function  renames the uploaded file on the tomcat
    temp folder  to the original filename*/
    public void renameUploadedFile(File original_file, File newfile) throws IOException {
        // this.original=original_file;

        if (original_file.exists()) {
            
        } else {
        }
        boolean success = original_file.renameTo(newfile);
        
    }

    //=======================================================================================================
    public List<UserController> getAllUserByAgent() {
        
        synchronized (this) {
            if (allUsers == null || allUsers.isEmpty()) {
                try {
                    conn = util.getConnectionTodbSMS();
                    allUsers = new ArrayList<>();
                    UserServiceApi userService = new UserServiceImpl();
                    allUsers = userService.getAllUserPerAgent(conn, userS);
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
        float available_credits []= new float[3];
        try {
            conn = util.getConnectionTodbSMS();
            AlphaScroller ac = new AlphaScroller();
            String user = ac.currentUSer();
            String sql = "Select max_total,cost_per_sms,max_contacts from tUSER where username='" + user + "' ";
            
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(sql);
            
            while (rs.next()) {
                available_credits[0] = rs.getInt(1);
                available_credits[1]=rs.getFloat(2);
                 available_credits[2]=rs.getInt(3);
                 System.out.println("available kredot"+available_credits[2]);
            }
        } catch (SQLException ex) {
            Logger.getLogger(UserServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
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

        
        return agentAlphas;
    }
    
    public void fetchAgentAlphas(){
                
        synchronized (this) {
            if (allUsers != null && !allUsers.isEmpty()) {
               
                    conn = util.getConnectionTodbSMS();
                    LOG.info("getAllAlphas");
                    UserServiceApi service = new UserServiceImpl();
                    agentAlphas = service.getAgentAlphas(conn, this.UserID());
                    JdbcUtil.closeConnection(conn);
              
            }
        }
    }
    
      public void pgetAgentAlphas(AjaxBehaviorEvent abe) throws AbortProcessingException {
         getAgentAlphas();
      }
      
          public void usersValueChangeListenerInCreditHisotry(ValueChangeEvent event) {
      
            userS= event.getNewValue().toString();
            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("resellerId", Integer.parseInt(UserID()));
        String lastusername = FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("username").toString();
          
            
            
        
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
       
            
            conn = util.getConnectionTodbSMS();
            
            LOG.info("deleteAlphanumeric");
            UserServiceImpl service = new UserServiceImpl();
            service.deleteUser(currentItem, conn);
            
            JsfUtil.addSuccessMessage("User " + currentItem.getUsername() + "  has been removed successfully.");

            //allUsers.remove(currentItem);
            JdbcUtil.closeConnection(conn);
      
        
    }
    
    
    public void deleteEmailUser() {

            
            conn = util.getConnectionTodbSMS();
            
            LOG.info("deleteAlphanumeric");
            UserServiceImpl service = new UserServiceImpl();
            service.deleteUser(currentItem, conn);
            
            JsfUtil.addSuccessMessage("User " + currentItem.getUsername() + "  has been removed successfully.");

            //allUsers.remove(currentItem);
            JdbcUtil.closeConnection(conn);
       
        
    }
    
    
    public void updateUserEmails() {
       
            
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
        
        
    }
    
    public void deleteReseller() {
        
            
            conn = util.getConnectionTodbSMS();
            
            LOG.info("deleteAlphanumeric");
            UserServiceImpl service = new UserServiceImpl();
            service.deleteUser(currentItem, conn);
            
            JsfUtil.addSuccessMessage("Reseller " + currentItem.getUsername() + "  has been removed successfully.");

            //allUsers.remove(currentItem);
            JdbcUtil.closeConnection(conn);
        
        
    }
    
    public void addPaybill() {
        String user = this.userS;
        
            this.conn = this.util.getConnectionTodbPAYMENT();
            this.conn2 = this.util.getConnectionTodbSMS();
            UserServiceImpl paybill2 = new UserServiceImpl();
            if (this.paybill == null) {
            }
            paybill2.persistPaybill(this.paybill, this.conn, this.conn2);
            JsfUtil.addSuccessMessage("Paybill added successfully!");
            this.paybill = new Paybill();
            this.userS = "";
            JdbcUtil.closeConnection(this.conn);
            JdbcUtil.closeConnection(this.conn2);
        
        facePainter.setMainContent("clientmanager/paybill/managepaybills.xhtml");

    }
    
    public void deletePaybill() {
        
            this.conn = this.util.getConnectionTodbPAYMENT();
            UserServiceImpl paybill2 = new UserServiceImpl();
            //System.out.println("CURRENT ITEM"+this.currentItem);
            paybill2.deletePaybill(this.currentItem1, this.conn);
            JsfUtil.addSuccessMessage("Paybill deleted successfully!");
            
            JdbcUtil.closeConnection(this.conn);
       
    }
    
    
    public void updatePaybill() {
        
        try {
            this.conn = this.util.getConnectionTodbPAYMENT();
            UserServiceImpl paybill2 = new UserServiceImpl();
            paybill2.updatePaybill(this.paybill, this.conn);
            JsfUtil.addSuccessMessage("Paybill updated successfully!");
            
            JdbcUtil.closeConnection(this.conn);
        } catch (SQLException ex) {
            Logger.getLogger(UserScroller.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
    public void finduserId(ValueChangeEvent event) {
        String uservalue = event.getNewValue().toString();
        
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
        }
    }
    public List<Alpnumeric> getUserAlphas() throws SQLException {
      
        
        return userAlphas;
    }
    
    public void fetchUserAlphas(){
        System.out.println("fetching alphas");
          synchronized (this) {
            if (userAlphas == null || userAlphas.isEmpty()) {
                
                    conn = util.getConnectionTodbSMS();
                   // LOG.info("getAllUsers");
                    userAlphas = new ArrayList<>();
                    UserServiceApi userService = new UserServiceImpl();
                    userAlphas = userService.getUserAlphas(conn, userS);
                    //.info("ALL ALPHAS EMPTY | NULL");
                    JdbcUtil.closeConnection(conn);
               
            } else {
                userAlphas = null;
               
                    conn = util.getConnectionTodbSMS();
                    //LOG.info("Get " + userS + "'s Alphas");
                    userAlphas = new ArrayList<>();
                    UserServiceApi userService = new UserServiceImpl();
                    userAlphas = userService.getUserAlphas(conn, userS);
                    JdbcUtil.closeConnection(conn);
               
            }
        }
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
      public  boolean amIResellersUser(){
        return  !SessionUtil.isReseller();
        
     }
       public  boolean amIAdmin(){
        return  !SessionUtil.isIAdmin();
        
     }
          public boolean amIReseller(){
         return SessionUtil.isReseller();
     }
            public String show(){
         return SessionUtil.isReseller()?"none":"show";
     }
}
