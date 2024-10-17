/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.mspace.clientmanager.reports;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import ke.co.mspace.nonsmppmanager.model.creditRecord;
import ke.co.mspace.nonsmppmanager.service.UserServiceApi;
import ke.co.mspace.nonsmppmanager.service.UserServiceImpl;
import ke.co.mspace.nonsmppmanager.util.JdbcUtil;
import ke.co.mspace.nonsmppmanager.util.JsfUtil;

/**
 *
 * @author olal
 */
@ManagedBean(name = "credit")
@ViewScoped
public class creditController {

    private final JdbcUtil jdbcUtil = new JdbcUtil();
    private String username;
    private List<creditRecord> records;
    private UserServiceApi creditDAO;

    @PostConstruct
    public void init() {
        creditDAO = new UserServiceImpl();
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public List<creditRecord> getRecords() {
        return records;
    }

    public void setRecords(List<creditRecord> records) {
        this.records = records;
    }

    public void generateCreditHistory() {
        Connection conn = jdbcUtil.getConnectionTodbSMS();
        try {
            records = creditDAO.getAllUsersCred(conn, username);

            if (!records.isEmpty()) {
                JsfUtil.addSuccessMessage(records.size() + " Records retrieved.");
            } else {
                JsfUtil.addSuccessMessage("No records found match search.");
            }
        } catch (SQLException ex) {
            Logger.getLogger(creditController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
