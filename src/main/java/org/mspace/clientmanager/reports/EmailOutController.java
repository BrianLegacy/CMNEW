/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.mspace.clientmanager.reports;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import ke.co.mspace.nonsmppmanager.model.EmailOut;
import ke.co.mspace.nonsmppmanager.service.EmailOutServiceApi;
import ke.co.mspace.nonsmppmanager.service.EmailOutServiceApiImpl;
import ke.co.mspace.nonsmppmanager.util.JsfUtil;

/**
 *
 * @author amos
 */
@ManagedBean(name = "emailoutcontroller")
@ViewScoped
public class EmailOutController implements Serializable {

    private List<EmailOut> emailUsers;
    private String username;
    private Date reportStartDate = new Date();
    private Date reportEndDate = new Date();

    private EmailOutServiceApi emailDao;
    private int rows;

    @PostConstruct
    public void init() {
        emailDao = new EmailOutServiceApiImpl();
    }

    public void generateEmailOut() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");

        String startDate = simpleDateFormat.format(reportStartDate);
        startDate = startDate.substring(0, 8) + "000001";

        String endDate = simpleDateFormat.format(reportEndDate);
        endDate = endDate.substring(0, 8) + "235959";

        if (!username.isEmpty()) {
            System.out.println("Selected user is: " + username);
            System.out.println("startdate " + startDate);
            System.out.println("endDate " + endDate);
            emailUsers = emailDao.fetchEmailReport(username, startDate, endDate);

            if (!emailUsers.isEmpty()) {
                JsfUtil.addSuccessMessage("Fetched All Email Reports.");
                System.out.println("rows: " + getRows());
            } else {
                JsfUtil.addSuccessMessage("No records found match search.");
            }
        } else {

            System.out.println("Username is null");
            JsfUtil.addErrorMessage("No User selected.");
        }
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Date getReportStartDate() {
        return reportStartDate;
    }

    public void setReportStartDate(Date reportStartDate) {
        this.reportStartDate = reportStartDate;
    }

    public Date getReportEndDate() {
        return reportEndDate;
    }

    public void setReportEndDate(Date reportEndDate) {
        this.reportEndDate = reportEndDate;
    }

    public int getRows() {

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");

        String startDate = simpleDateFormat.format(reportStartDate);
        startDate = startDate.substring(0, 8) + "000001";

        String endDate = simpleDateFormat.format(reportEndDate);
        endDate = endDate.substring(0, 8) + "235959";

        rows = emailDao.fetchRows(username, startDate, endDate);
        return rows;
    }

    public void setRows(int rows) {
        this.rows = rows;
    }

    public List<EmailOut> getEmailUsers() {
        return emailUsers;
    }

    public void setEmailUsers(List<EmailOut> emailUsers) {
        this.emailUsers = emailUsers;
    }

    public EmailOutServiceApi getEmailDao() {
        return emailDao;
    }

}
