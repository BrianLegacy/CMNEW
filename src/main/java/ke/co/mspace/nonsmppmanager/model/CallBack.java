/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ke.co.mspace.nonsmppmanager.model;

import org.mspace.clientmanager.group.Group;
import ke.co.mspace.nonsmppmanager.invalids.FacePainter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;

import ke.co.mspace.nonsmppmanager.service.AlphaServiceImpl;
import ke.co.mspace.nonsmppmanager.service.UserServiceImpl;
import static ke.co.mspace.nonsmppmanager.service.UserServiceImpl.getUserIdByUsername;
import ke.co.mspace.nonsmppmanager.util.JdbcUtil;
import ke.co.mspace.nonsmppmanager.util.JsfUtil;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;

/**
 *
 * @author developer
 */

@ManagedBean(name = "callback")
@SessionScoped
public class CallBack {

    private int userid;
    private String callback_url;
      private String testbednumbers;
      
    private String ussd_assigned_code;
    private boolean status;
        private boolean testbed;
    private String real_status;
    private int temporary_ussd_assigned_code;
public boolean validaTetestbednumbers() {

		System.out.println("inside validate method");

return true;
	}
    public String getTestbednumbers() {
        return testbednumbers;
    }

    public void setTestbednumbers(String testbednumbers) {
        this.testbednumbers = testbednumbers;
    }

    public boolean isTestbed() {
        return testbed;
    }

    public void setTestbed(boolean testbed) {
        this.testbed = testbed;
    }

    
    public int getTemporary_ussd_assigned_code() {
        int x=Integer.parseInt(ussd_assigned_code);
        return x;
    }

    public void setTemporary_ussd_assigned_code(int temporary_ussd_assigned_code) {
        this.ussd_assigned_code=Integer.toString(temporary_ussd_assigned_code);
        this.temporary_ussd_assigned_code = temporary_ussd_assigned_code;
    }
    private Set<Integer> keys = new HashSet();
    Connection conn;

    public String getReal_status() {
        if (status == true) {
            real_status = "Active";
        } else {
            real_status = "Inactive";
        }
        return real_status;
    }

    public void setReal_status(String real_status) {
        this.real_status = real_status;
    }

    public CallBack(int id, int userid, String callback_url, String ussd_assigned_code, boolean status, String real_status) {
        this.userid = userid;
        this.callback_url = callback_url;
        this.ussd_assigned_code = ussd_assigned_code;
        this.status = status;
        this.real_status = real_status;
    }

    public CallBack() {
    }

    private String selectedUser = "";

    public int getUserid() {
//                   System.out.println("User id is sending id "+userid);

        return userid;
    }

    public void setUserid(int userid) {
            
//           System.out.println("User id is set to "+userid);
        this.userid = userid;
    }

 

    public String getCallback_url() {
        return callback_url;
    }

    public void setCallback_url(String callback_url) {
        this.callback_url = callback_url;
    }

    public String getUssd_assigned_code() {
        return ussd_assigned_code;
    }

    public void setUssd_assigned_code(String ussd_assigned_code) {
        this.ussd_assigned_code = ussd_assigned_code;
    }

 

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    final JdbcUtil util = new JdbcUtil();
    @ManagedProperty(value = "#{facePainter}")
    public FacePainter facePainter;

    public FacePainter getFacePainter() {
        return facePainter;
    }

    public void setFacePainter(FacePainter facePainter) {
        this.facePainter = facePainter;
    }
    public void addCallBack() {
        if(! checkDulicity(userid, callback_url)){
      
            this.conn = this.util.getConnectionTodbPAYMENT();
            UserServiceImpl callback = new UserServiceImpl();
            callback.persistCallBack(this, conn,ussd_assigned_code);
            System.out.println("The assigned code is " + ussd_assigned_code);
            clearAll();
            JdbcUtil.closeConnection(this.conn);
//                    facePainter.setMainContent("clientmanager/paybill/managepaybills.xhtml");

       
        
        }else{
               FacesMessage facesMsg = new FacesMessage(FacesMessage.SEVERITY_FATAL, "Duplicity",null );
       // FacesContext.getCurrentInstance().addMessage(null, facesMsg);
        JsfUtil.addErrorMessage("Duplicate UserID and Callback Combination");
       }
    }

    public void clearAll() {
        this.callback_url = "";
        this.ussd_assigned_code = "";
        this.status = true;
        this.userid = 0;
    }

    public String getSelectedUser() {
        return selectedUser;
    }

    public void setSelectedUser(String selectedUser) {
        this.selectedUser = selectedUser;
    }
    
    @PostConstruct
            public void init(){

           fetchcallback();
            }
    
ArrayList<CallBack> callbacks;

    public ArrayList<CallBack> getCallbacks()  throws SQLException{
        return callbacks;
//return fetchcallback();
    }

    public void setCallbacks(ArrayList<CallBack> callbacks) {
        this.callbacks = callbacks;
    }

    public void fetchcallback()  {
        System.out.println("get kallbak kalled");
        UserServiceImpl callback = new UserServiceImpl();
//        System.out.println("Selected User: " + selectedUser);
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("selectedUserCombo", selectedUser);
      
        try {
            callbacks= UserServiceImpl.getCallBack();
        } catch (SQLException ex) {
            Logger.getLogger(CallBack.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    ArrayList<Group> groups;

    public void setGroups(ArrayList<Group> groups) {
        this.groups = groups;
    }
    
    
    
       public ArrayList<Group> getGroups() throws SQLException {
     
        return  groups;
    }
       
       public void fetchGroups(){
           groups=UserServiceImpl.getGroups(util.getConnectionTodbSMS());
       }
      ArrayList<EmailPricingTable> pricingtable;

    public ArrayList<EmailPricingTable> getPricingtable() {
        return pricingtable;
    }

    public void setPricingtable(ArrayList<EmailPricingTable> pricingtable) {
        this.pricingtable = pricingtable;
    }
      
      public void fetchPricing()  {
           ArrayList<EmailPricingTable> _pricingtable=null;
        try {
          pricingtable=  UserServiceImpl.getPricingTable();
        } catch (SQLException ex) {
            Logger.getLogger(CallBack.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public Set<Integer> getKeys() {
        return keys;
    }

    public void setKeys(Set<Integer> keys) {
        this.keys = keys;
    }
    
      public boolean checkDulicity(int userID, String callback) {
           
        String sql = "SELECT * from tSharedUssdClients WHERE tuser_id=? and callback_url=?";
        System.out.println("Seearching for existing records to prevent duplicity for "+userID+"and" +callback+"sql:" +sql);
        try {
         this.conn = this.util.getConnectionTodbUSSD();
         PreparedStatement ps=this.conn.prepareStatement(sql);
         //ps.setInt(1, userid);
         ps.setInt(1, userID);
         ps.setString(2, callback);
         ResultSet rs=ps.executeQuery();
         while(rs.next()){
             System.out.println("Duplicate UserID and Callback Combination");
            //throw new SQLException("Duplicate UserID and Callback Combination");
            return true;
         }
                    JdbcUtil.closeConnection(this.conn);

        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return false;
        
    }
      
        public  ArrayList<EmailPricingTable> getPricingTable() throws SQLException{
        String username = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("selectedUserCombo");
        JdbcUtil util = new JdbcUtil();
        Connection conn = util.getConnectionTodbEMAIL();

        String sql = "select * from tEMAILPRICING te";
            System.out.println("sql "+sql);
        PreparedStatement ps = conn.prepareStatement(sql);
        ArrayList<EmailPricingTable> pricingtable = new ArrayList();
        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
           EmailPricingTable pricing = new EmailPricingTable();
            pricing.setId(rs.getInt("id"));
           pricing.setPrice(rs.getInt("price"));
            pricing.setEmails_purchased(rs.getString("emails_purchased"));
             pricing.setExpiry(rs.getString("expiry"));
             pricingtable.add(pricing);
             System.out.println("called "+pricing);
        }
            System.out.println("method ended");
        return pricingtable;
    } 

}
