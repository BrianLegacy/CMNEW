package org.mspace.clientmanager.api.model;

import java.util.Date;
/**
 *
 * @author olal
 */
public class TSmsApiKey {
    private int id;
    private int userId;
    private String username;
    private String name;
    private String apiKey;
    private Date dateCreated;
    private Date expiryDate;
    private int status;


    public TSmsApiKey() {
    }

    public TSmsApiKey(int id, int userId, String username, String name, String apiKey, Date dateCreated, Date expiryDate, int status) {
        this.id = id;
        this.userId = userId;
        this.username = username;
        this.name = name;
        this.apiKey = apiKey;
        this.dateCreated = dateCreated;
        this.expiryDate = expiryDate;
        this.status = status;
    }
    
    
    
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
    
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getApiKey() {
        return apiKey;
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

    public Date getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(Date dateCreated) {
        this.dateCreated = dateCreated;
    }

    public Date getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(Date expiryDate) {
        this.expiryDate = expiryDate;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TSmsApiKey that = (TSmsApiKey) o;

        return id == that.id;
    }

    @Override
    public int hashCode() {
        return id;
    }
}

