/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ke.co.mspace.nonsmppmanager.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import ke.co.mspace.nonsmppmanager.model.CallBack;
import ke.co.mspace.nonsmppmanager.model.EmailPricingTable;
import ke.co.mspace.nonsmppmanager.util.JdbcUtil;
import ke.co.mspace.nonsmppmanager.util.JsfUtil;

/**
 *
 * @author developer
 */
@ManagedBean
@ViewScoped
public class PricingService {

    private JdbcUtil util = new JdbcUtil();
    Connection conn = null;
    ArrayList<EmailPricingTable> pricingtable;

    @PostConstruct
    public void init() {
        fetchPricing();
    }

    public ArrayList<EmailPricingTable> getPricingtable() {
        return pricingtable;
    }

    public void setPricingtable(ArrayList<EmailPricingTable> pricingtable) {
        this.pricingtable = pricingtable;
    }
    private EmailPricingTable currentPricingItem;

    public EmailPricingTable getCurrentPricingItem() {
        return currentPricingItem;
    }

    public void setCurrentPricingItem(EmailPricingTable currentPricingItem) {
        this.currentPricingItem = currentPricingItem;
    }

    private int currentPricingRow;

    public void clearCurrentItem() {
        this.currentPricingItem = new EmailPricingTable();
        this.currentPricingRow = 0;

    }

    public void psetCurrentPricingItemForEdit(EmailPricingTable data) {

        currentPricingItem = data;
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("storedpricingid", currentPricingItem.getId());
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("storedemailspurchased", currentPricingItem.getEmails_purchased());
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("storedexpiry", currentPricingItem.getExpiry());
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("storedprice", currentPricingItem.getPrice());
//          FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("editmybill", currentItem.getUserid());
//        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("prevPayBill", currentItem.getPaybillz());

    }

    public void pstorePricingid(EmailPricingTable data) {

        currentPricingItem = data;
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("pricingemailspurchased", currentPricingItem.getEmails_purchased());
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("pricingexpiry", currentPricingItem.getExpiry());
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("pricingprice", currentPricingItem.getPrice());
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("pricingid", currentPricingItem.getId());
        System.out.println("values " + currentPricingItem);

    }

    public void updatePricing() {
        String str = currentPricingItem.getEmails_purchased_start() + "-" + currentPricingItem.getEmails_purchased_end();
        currentPricingItem.setEmails_purchased(str);
        System.out.println(currentPricingItem);
        System.out.println("ids" + (int) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("storedpricingid"));
        int updateId = (int) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("storedpricingid");
        try {
            this.conn = this.util.getConnectionTodbEMAIL();
            String sql = "UPDATE tEMAILPRICING  set emails_purchased=?, price=?,expiry=? where id=?";
            PreparedStatement ps = this.conn.prepareStatement(sql);
            ps.setString(1, currentPricingItem.getEmails_purchased());
            ps.setFloat(2, currentPricingItem.getPrice());
            ps.setString(3, currentPricingItem.getExpiry());
            ps.setInt(4, updateId);
            System.out.println(sql.toString());
            ps.executeUpdate();
            ps.close();
            conn.close();

            JsfUtil.addSuccessMessage("Pricing updated successfully");
            JdbcUtil.closeConnection(this.conn);
        } catch (SQLException e) {
            JdbcUtil.printSQLException(e);
        }
    }

    public void fetchPricing() {
        ArrayList<EmailPricingTable> _pricingtable = null;
        try {
            pricingtable = UserServiceImpl.getPricingTable();
        } catch (SQLException ex) {
            Logger.getLogger(CallBack.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void deletePricingTable() {
// int currentPricingId = 0;
        Map smap = FacesContext.getCurrentInstance().getExternalContext().getSessionMap();
//        if (smap.containsKey("pricingid")) {
        int currentPricingId = (int) smap.get("pricingid");

//        }
        System.out.println("current pricing id" + currentPricingId);

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
            JdbcUtil.closeConnection(this.conn);
        } catch (SQLException ex) {
            ex.printStackTrace();
            JdbcUtil.printSQLException(ex);
        }
        smap = null;
    }

    public List<SelectItem> getTypeCombo() {
        List<SelectItem> types = new ArrayList<>();
        types.add(new SelectItem("Expiry"));
        types.add(new SelectItem("No Expiry"));
        return types;
    }

    public void savePricing() {
        String str = currentPricingItem.getEmails_purchased_start() + "-" + currentPricingItem.getEmails_purchased_end();
        currentPricingItem.setEmails_purchased(str);
        System.out.println(currentPricingItem);
//                     System.out.println("ids" +(int)FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("storedpricingid"));
//                     int updateId=(int)FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("storedpricingid");
        try {
            this.conn = this.util.getConnectionTodbEMAIL();
            String sql = "insert into tEMAILPRICING(emails_purchased,price, expiry) values (?,?,?)";
            PreparedStatement ps = this.conn.prepareStatement(sql);
            ps.setString(1, currentPricingItem.getEmails_purchased());
            ps.setFloat(2, currentPricingItem.getPrice());
            ps.setString(3, currentPricingItem.getExpiry());
            System.out.println(sql.toString());
            ps.executeUpdate();
            ps.close();
            conn.close();
            JsfUtil.addSuccessMessage("Pricing added successfully.");
            getAllPricings();
            JdbcUtil.closeConnection(this.conn);
        } catch (SQLException e) {
            JdbcUtil.printSQLException(e);
        }
    }

    public List<EmailPricingTable> getAllPricings() {
        List<EmailPricingTable> pricings = new ArrayList<>();
        try {

            pricings = UserServiceImpl.getPricingTable();
        } catch (SQLException ex) {
            Logger.getLogger(CallBackScroller.class.getName()).log(Level.SEVERE, null, ex);
        }
        return pricings;
    }
}
