package ke.co.mspace.nonsmppmanager.model;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import ke.co.mspace.nonsmppmanager.service.PaybillServiceApi;
import ke.co.mspace.nonsmppmanager.service.PaybillServiceImpl;
import ke.co.mspace.nonsmppmanager.service.UserServiceImpl;
import ke.co.mspace.nonsmppmanager.util.JdbcUtil;
import ke.co.mspace.nonsmppmanager.util.JsfUtil;
import org.hibernate.validator.constraints.NotEmpty;
@ManagedBean(name = "ppaybill")
public class Paybill {

    /*  38 */ int k = 0;
    
    private int paybillz;
    private int currentrow;
    private int userid;
    private String message;
    private String default_message;
    @NotEmpty(message = "Email required")
//    @Pattern(regexp = ".+@.+\\..+", message = "Input Email invalid")
    private String email;
    private String transID;
    private String accNo;
    private int ammount;
    private String mobile;
    private String name;
    private String accBalance;
    private String time_received;
    private List<Paybill> listPaybill;
    private String sender_id;
    private Set<Integer> keys = new HashSet();
    Connection conn;

    public Paybill(int paybillz, int userid, String message, String email, String transID, String accNo, int ammount, String mobile, String name, String accBalance, String time_received, Connection conn) {
        this.paybillz = paybillz;
        this.userid = userid;
        this.message = message;
        this.email = email;
        this.accNo = accNo;
        this.ammount = ammount;
        this.mobile = mobile;
        this.name = name;
        this.accBalance = accBalance;
        this.time_received = time_received;
        this.conn = conn;
    }

    private String selectedUser = "";

    public String getSelectedUser() {
        return selectedUser;
    }

    public void setSelectedUser(String selectedUser) {
        this.selectedUser = selectedUser;
    }

    public int getK() {
        return this.k;
    }

    public void setK(int k) {
        this.k = k;
    }

    public int getPaybillz() {
        return this.paybillz;
    }

    public void setPaybillz(int paybillz) {
        this.paybillz = paybillz;
    }

    public int getUserid() {
        return this.userid;
    }

    public void setUserid(int userid) {
        this.userid = userid;
    }

    public String getMessage() {
        return this.message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getEmail() {
        return this.email;
    }

    public Set<Integer> getKeys() {
        return this.keys;
    }

    public void setKeys(Set<Integer> keys) {
        this.keys = keys;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTransID() {
        return this.transID;
    }

    public void setTransID(String transID) {
        this.transID = transID;
    }

    public String getAccNo() {
        return this.accNo;
    }

    public void setAccNo(String accNo) {
        this.accNo = accNo;
    }

    public int getAmmount() {
        return this.ammount;
    }

    public void setAmmount(int ammount) {
        this.ammount = ammount;
    }

    public String getMobile() {
        return this.mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAccBalance() {
        return this.accBalance;
    }

    public void setAccBalance(String accBalance) {
        this.accBalance = accBalance;
    }

    public String getTime_received() {
        return this.time_received;
    }

    public void setTime_received(String time_received) {
        this.time_received = time_received;
    }

    public Connection getConn() {
        return this.conn;
    }

    public void setConn(Connection conn) {
        this.conn = conn;
    }

    public String getDefault_message() {
        return default_message;
    }

    public void setDefault_message(String default_message) {
        this.default_message = default_message;
    }

    public List<Paybill> getListPaybill() {
        return this.listPaybill;

    }

    public void setListPaybill(List<Paybill> listPaybill) {
        this.listPaybill = listPaybill;
    }

    public String getSender_id() {
        return sender_id;
    }

    public void setSender_id(String sender_id) {
        this.sender_id = sender_id;
    }

    public List<Paybill> listPaybill() {
        List<Paybill> paybills = null;
        
            this.conn = this.util.getConnectionTodbPAYMENT();

            PaybillServiceApi paybillService = new PaybillServiceImpl();
            paybills = paybillService.getAllPaybill(this.conn);
            JdbcUtil.closeConnection(this.conn);
     

        return paybills;
    }

    Connection conn2;

    final JdbcUtil util = new JdbcUtil();

    public Paybill() {
    }

    public void addPaybill() {
       
            this.conn = this.util.getConnectionTodbPAYMENT();
            UserServiceImpl paybill = new UserServiceImpl();
            paybill.persistPaybill(this, this.conn, this.conn2);
            JsfUtil.addSuccessMessage("Paybill added successfully!");
            clearAll();
            JdbcUtil.closeConnection(this.conn);
            JdbcUtil.closeConnection(this.conn2);
    
    }

    public void addTillNumber() {
       
            this.conn = this.util.getConnectionTodbPAYMENT();
            UserServiceImpl paybill = new UserServiceImpl();
            paybill.persistPaybill(this, this.conn, this.conn2);
            JsfUtil.addSuccessMessage("Paybill/Till Number added successfully!");
            clearAll();
            JdbcUtil.closeConnection(this.conn);
            JdbcUtil.closeConnection(this.conn2);
        
    }

    public void clearAll() {
        this.message = "";
        this.paybillz = 0;
        this.email = "";
        this.userid = 0;
    }

    public String showPaybill(String paybill) {
        return paybill;
    }

    public void deletePaybill() {
        try {
            this.conn = this.util.getConnectionTodbPAYMENT();
            UserServiceImpl paybill2 = new UserServiceImpl();
            paybill2.deletePaybill(this, conn);
            List<Paybill> list = UserServiceImpl.getPayBill();
            for (Paybill bill : list) {
                System.out.println(bill.getPaybillz());
            }
            JsfUtil.addSuccessMessage("Paybill deleted successfully!");

            JdbcUtil.closeConnection(this.conn);
        } catch (SQLException e) {
            JdbcUtil.printSQLException(e);
        }
    }

    public void finduserid() {
        try {
            this.conn = this.util.getConnectionTodbPAYMENT();
            UserServiceImpl id = new UserServiceImpl();
            id.getAllUsers(this.conn, this.name);

        } catch (SQLException ex) {
            System.out.println("Error " + ex + "occurred");
        }
    }
    
ArrayList<Paybill> paybill;

    public void setPaybill(ArrayList<Paybill> paybill) {
        this.paybill = paybill;
    }

    public ArrayList<Paybill> getpaybill()  {
        return paybill;

    }
    @PostConstruct
    public void init(){
        fetchPaybill();
    }
   public void fetchPaybill(){
        try {
            UserServiceImpl paybill = new UserServiceImpl();
            System.out.println("Selected User: " + selectedUser);
            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("selectedUserCombo", selectedUser);
//        if (selectedUser.isEmpty()) {
//            return new ArrayList<>();
//        } else {
this.paybill= UserServiceImpl.getPayBill();
//}
        } catch (SQLException ex) {
            Logger.getLogger(Paybill.class.getName()).log(Level.SEVERE, null, ex);
        }
   }

    public void fetchCurrentRow(ActionEvent event) {
        String username = (String) FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("username");
        this.currentrow = Integer.parseInt(
                (String) FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("row"));
    }

    @Override
    public String toString() {
        return "Paybill{" + "k=" + k + ", paybillz=" + paybillz + ", currentrow=" + currentrow + ", userid=" + userid + ", message=" + message + ", default_message=" + default_message + ", email=" + email + ", transID=" + transID + ", accNo=" + accNo + ", ammount=" + ammount + ", mobile=" + mobile + ", name=" + name + ", accBalance=" + accBalance + ", time_received=" + time_received + ", listPaybill=" + listPaybill + ", sender_id=" + sender_id + ", keys=" + keys + ", conn=" + conn + ", selectedUser=" + selectedUser + ", conn2=" + conn2 + ", util=" + util + '}';
    }
    
    
}
