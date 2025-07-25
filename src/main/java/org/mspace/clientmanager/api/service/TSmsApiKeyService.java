/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.mspace.clientmanager.api.service;

import java.util.List;
import javax.faces.bean.RequestScoped;
import javax.faces.model.SelectItem;
import org.mspace.clientmanager.api.ApiException;
import org.mspace.clientmanager.api.dao.TSmsApiKeyDAO;
import org.mspace.clientmanager.api.model.TSmsApiKey;

/**
 *
 * @author olal
 */
@RequestScoped
public class TSmsApiKeyService {

    private TSmsApiKeyDAO smsApiDAO = new TSmsApiKeyDAO();

    public TSmsApiKeyService() {
    }

    public List<SelectItem> getUsers() {
        return smsApiDAO.getUsers();
    }

    public List<TSmsApiKey> getAllSmsApiKeys() {
        return smsApiDAO.selectAll();
    }

    public String createSmsApiKey(TSmsApiKey key) throws ApiException {
        return smsApiDAO.insert(key);
    }

    public void updateSmsApiKey(int id) {
        smsApiDAO.update(id);
    }

    public void updateApiKeyStatus(int id, int newStatus) {
        smsApiDAO.updateStatus(id, newStatus);
    }

    public void deleteSmsApiKey(int id) {
        smsApiDAO.delete(id);
    }

    public TSmsApiKey getSmsApiKeyById(int id) {
      return smsApiDAO.getSmsApiKeyById(id);
    }
    
    public void updateSMSApiKeyName(TSmsApiKey manageSmsApiKey){
        smsApiDAO.updateSmsApiKeyName(manageSmsApiKey);
    }
}
