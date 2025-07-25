/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ke.co.mspace.nonsmppmanager.model;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.Date;

/**
 *
 * @author mspace-dev
 */
public class UserProfile implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;
    private String username;
    private String password;
    private long maxDaily;
    private long maxWeekly;
    private long maxMonthly;
    private long maxTotal;
    private long maxContacts;
    private boolean loggedIn;
    private Date loggedInTime;
    private Date startDate;
    private Date endDate;
    private BigInteger smsCountToday;
    private BigInteger smsCountWeek;
    private BigInteger smsCountMonth;
    private BigInteger smsCountTotal;
    private String contactNumber;
    private String emailAddress;
    private Boolean enableEmailAlert;
    private Integer alertThreshold;

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public UserProfile() {
    }

    public UserProfile(Long id) {
        this.id = id;
    }

    public UserProfile(Long id, String username, String password,
            long maxDaily, long maxWeekly, long maxMonthly, long maxTotal, boolean loggedIn, Date loggedInTime) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.maxDaily = maxDaily;
        this.maxWeekly = maxWeekly;
        this.maxMonthly = maxMonthly;
        this.maxTotal = maxTotal;
        this.loggedIn = loggedIn;
        this.loggedInTime = loggedInTime;
    }

    public UserProfile(Long id, String username, String password, long maxDaily, long maxWeekly, long maxMonthly, long maxTotal, 
            boolean loggedIn, Date loggedInTime, Date startDate, Date endDate, BigInteger smsCountToday, BigInteger smsCountWeek, 
            BigInteger smsCountMonth, BigInteger smsCountTotal, String contactNumber, String emailAddress, 
            Boolean enableEmailAlert, Integer alertThreshold) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.maxDaily = maxDaily;
        this.maxWeekly = maxWeekly;
        this.maxMonthly = maxMonthly;
        this.maxTotal = maxTotal;
        this.loggedIn = loggedIn;
        this.loggedInTime = loggedInTime;
        this.startDate = startDate;
        this.endDate = endDate;
        this.smsCountToday = smsCountToday;
        this.smsCountWeek = smsCountWeek;
        this.smsCountMonth = smsCountMonth;
        this.smsCountTotal = smsCountTotal;
        this.contactNumber = contactNumber;
        this.emailAddress = emailAddress;
        this.enableEmailAlert = enableEmailAlert;
        this.alertThreshold = alertThreshold;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public long getMaxDaily() {
        return maxDaily;
    }

    public void setMaxDaily(long maxDaily) {
        this.maxDaily = maxDaily;
    }

    public long getMaxWeekly() {
        return maxWeekly;
    }

    public void setMaxWeekly(long maxWeekly) {
        this.maxWeekly = maxWeekly;
    }

    public long getMaxMonthly() {
        return maxMonthly;
    }

    public void setMaxMonthly(long maxMonthly) {
        this.maxMonthly = maxMonthly;
    }

    public long getMaxTotal() {
        return maxTotal;
    }

    public void setMaxTotal(long maxTotal) {
        this.maxTotal = maxTotal;
    }
    public long getMaxContacts() {
        return maxContacts;
    }

    public void setMaxContacts(long maxContacts) {
        this.maxContacts = maxContacts;
    }

    public boolean getLoggedIn() {
        return loggedIn;
    }

    public void setLoggedIn(boolean loggedIn) {
        this.loggedIn = loggedIn;
    }

    public Date getLoggedInTime() {
        return loggedInTime;
    }

    public void setLoggedInTime(Date loggedInTime) {
        this.loggedInTime = loggedInTime;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public BigInteger getSmsCountToday() {
        return smsCountToday;
    }

    public void setSmsCountToday(BigInteger smsCountToday) {
        this.smsCountToday = smsCountToday;
    }

    public BigInteger getSmsCountWeek() {
        return smsCountWeek;
    }

    public void setSmsCountWeek(BigInteger smsCountWeek) {
        this.smsCountWeek = smsCountWeek;
    }

    public BigInteger getSmsCountMonth() {
        return smsCountMonth;
    }

    public BigInteger getSmsCountTotal() {
        return smsCountTotal;
    }

    public void setSmsCountTotal(BigInteger smsCountTotal) {
        this.smsCountTotal = smsCountTotal;
    }

    public void setSmsCountMonth(BigInteger smsCountMonth) {
        this.smsCountMonth = smsCountMonth;
    }
    
    public String getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }

    public Boolean getEnableEmailAlert() {
        return enableEmailAlert;
    }

    public void setEnableEmailAlert(Boolean enableEmailAlert) {
        this.enableEmailAlert = enableEmailAlert;
    }

    public Integer getAlertThreshold() {
        return alertThreshold;
    }

    public void setAlertThreshold(Integer alertThreshold) {
        this.alertThreshold = alertThreshold;
    }
}
