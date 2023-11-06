/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ke.co.mspace.nonsmppmanager.model;

/**
 *
 * @author Norrey Osako
 */
//import org.richfaces.model.Ordering;


/**
 * TODO Class description goes here.
 * @author "Andrey Markavtsov"
 *
 */
public class Facet {
    private String header;
    private String footer;
    
    /**
     * TODO Description goes here.
     * @param header
     * @param footer
     */
    public Facet(String header, String footer) {
        super();
        this.header = header;
        this.footer = footer;
    }
    /**
     * @return the header
     */
    public String getHeader() {
        return header;
    }
    /**
     * @param header the header to set
     */
    public void setHeader(String header) {
        this.header = header;
    }
    /**
     * @return the footer
     */
    public String getFooter() {
        return footer;
    }
    /**
     * @param footer the footer to set
     */
    public void setFooter(String footer) {
        this.footer = footer;
    }

//    private Ordering sortOrder;
//    
//    public Ordering getSortOrder() {
//        return sortOrder;
//    }
//    
//    public void setSortOrder(Ordering sortOrder) {
//        this.sortOrder = sortOrder;
//    }
}