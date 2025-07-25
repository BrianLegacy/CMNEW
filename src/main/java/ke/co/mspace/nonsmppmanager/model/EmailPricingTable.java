/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ke.co.mspace.nonsmppmanager.model;

import javax.faces.model.SelectItem;

/**
 *
 * @author developer
 */
public class EmailPricingTable {
    private Integer id;
    private String emails_purchased;
    private String emails_purchased_start;
    private String emails_purchased_end;
    
    private float price; 
    private String expiry;
    private SelectItem selectType;

    public SelectItem getSelectType() {
        return selectType;
    }

    public void setSelectType(SelectItem selectType) {
        this.selectType = selectType;
    }
    
    

    public EmailPricingTable() {
    }

    public EmailPricingTable(Integer id, String emails_purchased, String emails_purchased_start, String emails_purchased_end, float price, String expiry) {
        this.id = id;
        this.emails_purchased = emails_purchased;
        this.emails_purchased_start = emails_purchased_start;
        this.emails_purchased_end = emails_purchased_end;
        this.price = price;
        this.expiry = expiry;
    }

    public String getEmails_purchased_start() {
        return emails_purchased_start;
    }

    public void setEmails_purchased_start(String emails_purchased_start) {
        this.emails_purchased_start = emails_purchased_start;
    }

    public String getEmails_purchased_end() {
        return emails_purchased_end;
    }

    public void setEmails_purchased_end(String emails_purchased_end) {
        this.emails_purchased_end = emails_purchased_end;
    }


    
    
    
    
    
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getEmails_purchased() {
        return emails_purchased;
    }

    public void setEmails_purchased(String emails_purchased) {
        this.emails_purchased = emails_purchased;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public String getExpiry() {
        return expiry;
    }

    public void setExpiry(String expiry) {
        this.expiry = expiry;
    }

    @Override
    public String toString() {
        return "EmailPricingTable{" + "id=" + id + ", emails_purchased=" + emails_purchased + ", emails_purchased_start=" + emails_purchased_start + ", emails_purchased_end=" + emails_purchased_end + ", price=" + price + ", expiry=" + expiry + '}';
    }

    
    
}
