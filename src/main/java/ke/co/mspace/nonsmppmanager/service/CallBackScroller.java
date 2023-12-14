/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ke.co.mspace.nonsmppmanager.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.bean.SessionScoped;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import ke.co.mspace.nonsmppmanager.model.Alpha;
import ke.co.mspace.nonsmppmanager.model.CallBack;
import ke.co.mspace.nonsmppmanager.model.EmailPricingTable;
import ke.co.mspace.nonsmppmanager.model.Facet;
import org.mspace.clientmanager.group.Group;
import ke.co.mspace.nonsmppmanager.model.GroupTemp;
import ke.co.mspace.nonsmppmanager.model.Paybill;
import ke.co.mspace.nonsmppmanager.util.JdbcUtil;
import ke.co.mspace.nonsmppmanager.util.JsfUtil;
//import org.richfaces.model.SortField;
//import org.richfaces.model.SortOrder;

/**
 *
 * @author developer
 */
@ManagedBean
@ViewScoped
public class CallBackScroller {
    
    private JdbcUtil util = new JdbcUtil();
    Connection conn = null;
    private int currentPricingRow;
    private int currentRow;
    private int currentGroupRow;
    private CallBack currentItem;
    public GroupTemp currentGroupItem=new GroupTemp();
    public Group currentGroupItem1=new Group();
    private EmailPricingTable currentPricingItem;
    private List<CallBack> allcallbacks = new ArrayList();
    private final ArrayList<Facet> columns = new ArrayList();
    
    private static final Logger LOG = Logger.getLogger(CallBackScroller.class.getName());

    private int rows = 10;

    public Group getCurrentGroupItem1() {
        return currentGroupItem1;
    }

    public void setCurrentGroupItem1(Group currentGroupItem1) {
        LocalDateTime start=LocalDateTime.now();
        this.currentGroupItem1 = currentGroupItem1;
        JsfUtil.printTimeDiff(start, LocalDateTime.now());
    }

    
    public int getCurrentGroupRow() {
        return currentGroupRow;
    }

    public void setCurrentGroupRow(int currentGroupRow) {
        this.currentGroupRow = currentGroupRow;
    }

    public GroupTemp getCurrentGroupItem() {
        return currentGroupItem;
    }

    public void setCurrentGroupItem(GroupTemp currentGroupItem) {
        System.out.println("sett");
        this.currentGroupItem = currentGroupItem;
    }
      public void oblivon(Group group) {
        System.out.println("sett");
    }

    
    public int getCurrentPricingRow() {
        return currentPricingRow;
    }

    public void setCurrentPricingRow(int currentPricingRow) {
        this.currentPricingRow = currentPricingRow;
    }
    
    public void psetCurrentCallback(CallBack data){
        System.out.println("========="+data);
        
        currentItem=data;
         FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("getcallback", currentItem.getCallback_url());
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("previouscode", currentItem.getUssd_assigned_code());
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("editcallBack", currentItem.getUserid());
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("userid", currentItem.getUserid());

       
    }

    
   
    public EmailPricingTable getCurrentPricingItem() {
        return currentPricingItem;
    }

    public void setCurrentPricingItem(EmailPricingTable currentPricingItem) {
        this.currentPricingItem = currentPricingItem;
    }
    
    

    public int getRows() {
        return this.rows;
    }

    public void setRows(int rows) {
        this.rows = rows;
    }
    
    public boolean valida(){
        System.out.println("Printing validators");
        Iterator<String> c=FacesContext.getCurrentInstance().getApplication().getValidatorIds();
        c.forEachRemaining((t) -> {
            System.out.println(t);
        });
           System.out.println("Printing validators");
        return false;
    }
    
//    private SortOrder order = new SortOrder();
    
    private String delcallBack;
   
    
    @PostConstruct
    public void init() {
       currentItem = new CallBack();
       currentPricingItem=new EmailPricingTable();
    }
    
     public CallBackScroller() {
        initColumnsHeaders();
//        SortField[] fields = {new SortField("make", Boolean.valueOf(true))};
//        this.order.setFields(fields);
    }
    
     public void fetchCurrentRow(ActionEvent event) {
        String vin = (String) FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("username");
        this.setCurrentRow(Integer.parseInt(
                (String) FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("row")));
        for (CallBack item : allcallbacks) {
            if (item.getUssd_assigned_code().equals(vin)) {
                System.out.println("THE REQUESTED NAME: " + item.getUssd_assigned_code());
                this.setCurrentItem(item);
                break;
            }
            System.out.println("THE REQUESTED NAME CHECK FAILED: " + item.getUssd_assigned_code());
        }
    }
    public void  clearCurrentItem(){
         this.currentPricingItem=new EmailPricingTable();
         this.currentPricingRow=0;
         
     }
     public List<CallBack> getAllCallBacks() {
        try {

            allcallbacks = UserServiceImpl.getCallBack();
        } catch (SQLException ex) {
            Logger.getLogger(CallBackScroller.class.getName()).log(Level.SEVERE, null, ex);
        }
        return this.allcallbacks;
    }
          public List<EmailPricingTable> getAllPricings() {
              List<EmailPricingTable> pricings=new  ArrayList<>();
        try {

            pricings = UserServiceImpl.getPricingTable();
        } catch (SQLException ex) {
            Logger.getLogger(CallBackScroller.class.getName()).log(Level.SEVERE, null, ex);
        }
        return pricings;
    }
    
     
      public void initColumnsHeaders() {
        this.columns.clear();
    }

    public int getCurrentRow() {
        return currentRow;
    }

    public void setCurrentRow(int currentRow) {
        this.currentRow = currentRow;
    }

    public CallBack getCurrentItem() {
        return currentItem;
    }

    public void setCurrentItem(CallBack currentItem) {
        this.currentItem = currentItem;
    }
    public void storeCurentid() {
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("getcallback", currentItem.getCallback_url());
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("previouscode", currentItem.getUssd_assigned_code());
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("editcallBack", currentItem.getUserid());
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("userid", currentItem.getUserid());



    }
        public void storeTuserid(int userId) {
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("userid", currentItem.getUserid());
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("editcallBack", currentItem.getUserid());
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("previouscode", currentItem.getUssd_assigned_code());
    //    FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("previouscode", currentItem.getid());

    }
        
          public void storeTGroupid(int id,String groupname, String description) {
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("groupid", id);
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("groupname",groupname);
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("groupdescription", description);
          currentGroupItem.setId(id);
          currentGroupItem.setGroupname(groupname);
          currentGroupItem.setDescription(description);
          
              System.out.println("gsj"+currentGroupItem);

    }
                     
                public void storeGroupid() {
            
//             FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("groupid", currentGroupItem.getId());
//        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("groupname", currentGroupItem.getGroupname());
//        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("groupdescription", currentGroupItem.getDescription());
          System.out.println("values "+currentGroupItem);
        


    }
    
        
              public void storeTPricingid(int userId) {
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("storedpricingid", currentPricingItem.getId());
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("storedemailspurchased", currentPricingItem.getEmails_purchased());
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("storedexpiry", currentPricingItem.getExpiry());
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("storedprice", currentPricingItem.getPrice());
    //    FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("previouscode", currentItem.getid());

    }
          public void psetCurrentPricingItemForEdit(EmailPricingTable data){
              
  currentPricingItem=data;
                FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("storedpricingid", currentPricingItem.getId());
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("storedemailspurchased", currentPricingItem.getEmails_purchased());
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("storedexpiry", currentPricingItem.getExpiry());
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("storedprice", currentPricingItem.getPrice());
//          FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("editmybill", currentItem.getUserid());
//        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("prevPayBill", currentItem.getPaybillz());

       
    }
          public void pstorePricingid(EmailPricingTable data){
              
  currentPricingItem=data;
  FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("pricingemailspurchased", currentPricingItem.getEmails_purchased());
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("pricingexpiry", currentPricingItem.getExpiry());
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("pricingprice", currentPricingItem.getPrice());
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("pricingid", currentPricingItem.getId());
            System.out.println("values "+currentPricingItem);
       
    }
        public void storePricingid() {
            
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("pricingemailspurchased", currentPricingItem.getEmails_purchased());
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("pricingexpiry", currentPricingItem.getExpiry());
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("pricingprice", currentPricingItem.getPrice());
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("pricingid", currentPricingItem.getId());
            System.out.println("values "+currentPricingItem);
        


    }
     


    
    public void deleteCallBack() {
//        String code="";
        String code="";
        Map smap = FacesContext.getCurrentInstance().getExternalContext().getSessionMap();
        if (smap.containsKey("previouscode")) {
               code=(String) smap.get("previouscode");
              // ussdid=(String) smap.get("ussdid");
               
            System.out.println("Code: " +code);
        }
        try {
             conn = util.getConnectionTodbUSSD();
//            String sql = "DELETE from tSharedUssdClients where ussd_assigned_code =?";
             String sql = "DELETE from tSharedUssdClients where id=?";
            PreparedStatement psmt = conn.prepareStatement(sql);
            psmt.setInt(1, getUssdId(code));
          //  psmt.setString(2, userid);
            psmt.executeUpdate();
            psmt.close();
            conn.close();
            JsfUtil.addSuccessMessage("CallBack deleted successfully.");
            System.out.println("Deleted "+code+" for user id "+getUssdId(code)+" successfully");
            JdbcUtil.closeConnection(this.conn);
        } catch (SQLException ex) {
            ex.printStackTrace();
            JdbcUtil.printSQLException(ex);
        }
        smap = null;
    }
    
    public void deletePricingTable() {
// int currentPricingId = 0;
        Map smap = FacesContext.getCurrentInstance().getExternalContext().getSessionMap();
//        if (smap.containsKey("pricingid")) {
           int    currentPricingId=(int) smap.get("pricingid");
            
           
//        }
         
        System.out.println("current pricing id"+ currentPricingId);
        
        try {
             conn = util.getConnectionTodbEMAIL();;
             String sql = "DELETE from tEMAILPRICING where id=?";
            PreparedStatement psmt = conn.prepareStatement(sql);
            psmt.setInt(1, currentPricingId);
          //  psmt.setString(2, userid);
            psmt.executeUpdate();
            psmt.close();
            conn.close();
            JsfUtil.addSuccessMessage("Pricing deleted successfully.");
            System.out.println("Deleted "+currentPricingId+" successfully");
            JdbcUtil.closeConnection(this.conn);
        } catch (SQLException ex) {
            ex.printStackTrace();
            JdbcUtil.printSQLException(ex);
        }
        smap = null;
    }
    
     public void deleteGroup(int id) {

        try {
             conn = util.getConnectionTodbSMS();;
             String sql = "DELETE from tGROUPS where id=?";
            PreparedStatement psmt = conn.prepareStatement(sql);
            psmt.setInt(1, id);
          //  psmt.setString(2, userid);
            psmt.executeUpdate();
            psmt.close();
            conn.close();
            JsfUtil.addSuccessMessage("Group deleted successfully.");
            System.out.println("Deleted "+currentGroupItem1.getGroupname()+" successfully");
            JdbcUtil.closeConnection(this.conn);
        } catch (SQLException ex) {
            ex.printStackTrace();
            JdbcUtil.printSQLException(ex);
        }
    }

    /**
     * @return the delcallBack
     */
    public String getDelcallBack() {
        return delcallBack;
    }

    /**
     * @param delcallBack the delcallBack to set
     */
    public void setDelcallBack(String delcallBack) {
        this.delcallBack = delcallBack;
    }
    
    
    public void save() {
        delcallBack= (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("previouscode");
        int tUser_id=0;
        Map smap = FacesContext.getCurrentInstance().getExternalContext().getSessionMap();
        System.out.println(smap);
        if (smap.containsKey("editcallBack")) {
            tUser_id= (Integer) smap.get("editcallBack");
            System.out.println("Id: "+tUser_id);
        }
        try {
            this.conn = this.util.getConnectionTodbUSSD();
            String sql = "UPDATE tSharedUssdClients set tUSER_id=?,"
                    + "callback_url=?,ussd_assigned_code=?,status=?,type=?,testbedmobiles=?"
                    + " where tUser_id=? and ussd_assigned_code=?";
            PreparedStatement ps = this.conn.prepareStatement(sql);
            ps.setInt(1, tUser_id);
            ps.setString(2, currentItem.getCallback_url());
            ps.setString(3, currentItem.getUssd_assigned_code());
            ps.setBoolean(4, currentItem.isStatus());
            ps.setInt(7, tUser_id);
            ps.setString(8, delcallBack);
            ps.setInt(5, currentItem.isTestbed()?1:0);
            ps.setString(6, currentItem.getTestbednumbers());
            ps.executeUpdate();
            ps.close();
            System.out.println("tUserId: "+tUser_id);
            System.out.println("Call Back url is "+currentItem.getCallback_url());
                        System.out.println("Ussd code is  "+currentItem.getUssd_assigned_code());

            conn.close();
            JsfUtil.addSuccessMessage("CallBack updated successfully");
                this.allcallbacks= null;
                getAllCallBacks();
            JdbcUtil.closeConnection(this.conn);
        } catch (SQLException e) {
            JdbcUtil.printSQLException(e);
        }
        smap = null;
        
        
    }
    
    
    public boolean save3(){
        System.out.println("saved");
        return false;
    }
    
     public void save2() {
    
        delcallBack= (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("previouscode");
        int tUser_id=0;
        Map smap = FacesContext.getCurrentInstance().getExternalContext().getSessionMap();
        System.out.println(smap);
        if (smap.containsKey("editcallBack")) {
            tUser_id= (Integer) smap.get("editcallBack");
            System.out.println("Id: "+tUser_id);
        }
        try {
            this.conn = this.util.getConnectionTodbUSSD();
            String sql = "UPDATE tSharedUssdClients set tUSER_id=?,"
                    + "callback_url=?,ussd_assigned_code=?,status=?,type=?,testbedmobiles=?"
                    + " where tUser_id=? and ussd_assigned_code=?";
            PreparedStatement ps = this.conn.prepareStatement(sql);
            ps.setInt(1, tUser_id);
            ps.setString(2, currentItem.getCallback_url());
            ps.setString(3, currentItem.getUssd_assigned_code());
            ps.setBoolean(4, currentItem.isStatus());
            ps.setInt(7, tUser_id);
            ps.setString(8, delcallBack);
            ps.setInt(5, currentItem.isTestbed()?0:1);
            ps.setString(6, currentItem.getTestbednumbers());
            ps.executeUpdate();
            ps.close();
            System.out.println("tUserId: "+tUser_id);
            System.out.println("Call Back url is "+currentItem.getCallback_url());
                        System.out.println("Ussd code is  "+currentItem.getUssd_assigned_code());

            conn.close();
            JsfUtil.addSuccessMessage("CallBack updated successfully");
                this.allcallbacks= null;
                getAllCallBacks();
            JdbcUtil.closeConnection(this.conn);
        } catch (SQLException e) {
            JdbcUtil.printSQLException(e);
        }
        smap = null;
    }
     
          public void saveGroup() {
              System.out.println("joseph testing"+currentGroupItem1);
              if(currentGroupItem1.getGroupname().equalsIgnoreCase("")){
                  System.out.println("am empty");
                  return;
              }
        try {
            this.conn = this.util.getConnectionTodbSMS();
            String sql = "update tGROUPS set groupname = ?,description=? where id =?";
            PreparedStatement ps = this.conn.prepareStatement(sql);
            ps.setInt(3, currentGroupItem1.getId());
            ps.setString(1, currentGroupItem1.getGroupname());
            ps.setString(2, currentGroupItem1.getDescription());
            ps.executeUpdate();
            ps.close();
            conn.close();
            JsfUtil.addSuccessMessage("Group updated successfully");
//                this.allcallbacks= null;
//                getAllCallBacks();
            JdbcUtil.closeConnection(this.conn);
        } catch (SQLException e) {
            JdbcUtil.printSQLException(e);
        }
//        currentGroupItem1.setGroupname("");
    }
          public void updatePricing() {
              System.out.println("kdsfol");
               System.out.println(currentGroupItem);
               System.out.println("kdsfol");
 String str=currentPricingItem.getEmails_purchased_start() +"-"+currentPricingItem.getEmails_purchased_end();
                  currentPricingItem.setEmails_purchased(str);
                     System.out.println(currentPricingItem);
                     System.out.println("ids" +(int)FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("storedpricingid"));
                     int updateId=(int)FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("storedpricingid");
        try {
            this.conn = this.util.getConnectionTodbEMAIL();
            String sql = "UPDATE tEMAILPRICING  set emails_purchased=?, price=?,expiry=? where id=?";
            PreparedStatement ps = this.conn.prepareStatement(sql);
            ps.setString(1,  currentPricingItem.getEmails_purchased());
            ps.setFloat(2,  currentPricingItem.getPrice());
            ps.setString(3,  currentPricingItem.getExpiry());
            ps.setInt(4,  updateId);
            System.out.println(sql.toString());
            ps.executeUpdate();
            ps.close();
            conn.close();
            JsfUtil.addSuccessMessage("Pricing updated successfully");
//                this.= null;
                getAllPricings();
            JdbcUtil.closeConnection(this.conn);
        } catch (SQLException e) {
            JdbcUtil.printSQLException(e);
        }
    }
              public void savePricing() {
                  String str=currentPricingItem.getEmails_purchased_start() +"-"+currentPricingItem.getEmails_purchased_end();
                  currentPricingItem.setEmails_purchased(str);
                     System.out.println(currentPricingItem);
//                     System.out.println("ids" +(int)FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("storedpricingid"));
//                     int updateId=(int)FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("storedpricingid");
        try {
            this.conn = this.util.getConnectionTodbEMAIL();
            String sql = "insert into tEMAILPRICING(emails_purchased,price, expiry) values (?,?,?)";
            PreparedStatement ps = this.conn.prepareStatement(sql);
            ps.setString(1,  currentPricingItem.getEmails_purchased());
            ps.setFloat(2,  currentPricingItem.getPrice());
            ps.setString(3,  currentPricingItem.getExpiry());
            System.out.println(sql.toString());
            ps.executeUpdate();
            ps.close();
            conn.close();
            JsfUtil.addSuccessMessage("Pricing added successfully");
//                this.= null;
                getAllPricings();
            JdbcUtil.closeConnection(this.conn);
        } catch (SQLException e) {
            JdbcUtil.printSQLException(e);
        }
    }
    
    public int getUssdId (String code) {
        String sql = "SELECT id from tSharedUssdClients WHERE ussd_assigned_code=?";
        int ussdid=0;
        System.out.println("Session mapped code is "+code);
        try {
         this.conn = this.util.getConnectionTodbUSSD();
         PreparedStatement ps=this.conn.prepareStatement(sql);
         //ps.setInt(1, userid);
         ps.setString(1, code);
         ResultSet rs=ps.executeQuery();
         while(rs.next()){
             ussdid=rs.getInt("id");
         }
                    JdbcUtil.closeConnection(this.conn);

        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        
        
        return ussdid;
    }
    
}
