package org.mspace.clientmanager.api;

import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import org.mspace.clientmanager.api.model.TSmsApiKey;
import org.mspace.clientmanager.api.model.TSmsApiKeyDataModel;
import org.mspace.clientmanager.api.service.TSmsApiKeyService;
import org.primefaces.PrimeFaces;

@ManagedBean
@ViewScoped
public class SmsApiBean implements Serializable {

    private TSmsApiKeyDataModel keys;
    private TSmsApiKey newKey;
    private List<SelectItem> users;
    private TSmsApiKey manageUserKey;

    private String selectedApiKey;
    private int selectedKeyId;

    private TSmsApiKeyService smsApiService = new TSmsApiKeyService();

    @PostConstruct
    public void init() {
        List<TSmsApiKey> keyList = smsApiService.getAllSmsApiKeys();
        keys = new TSmsApiKeyDataModel(keyList);
        newKey = new TSmsApiKey();
        users = smsApiService.getUsers();
    }

    public TSmsApiKey getManageUserKey() {
        return manageUserKey;
    }

    public void setManageUserKey(TSmsApiKey manageUserKey) {
        this.manageUserKey = manageUserKey;
    }
    
    
    public int getSelectedKeyId() {
        return selectedKeyId;
    }

    public void setSelectedKeyId(int selectedKeyId) {
        this.selectedKeyId = selectedKeyId;
    }

    public TSmsApiKeyDataModel getKeys() {
        return keys;
    }

    public TSmsApiKey getNewKey() {
        return newKey;
    }

    public void setNewKey(TSmsApiKey newKey) {
        this.newKey = newKey;
    }

    public List<SelectItem> getUsers() {
        return users;
    }

    public String getSelectedApiKey() {
        return selectedApiKey;
    }

    public void setSelectedApiKey(String selectedApiKey) {
        this.selectedApiKey = selectedApiKey;
    }

    public void createKey() {
        try {
            String key = smsApiService.createSmsApiKey(newKey);
            refreshKeys();
            selectedApiKey = key;
            System.out.println("###########" + selectedApiKey);
            newKey = new TSmsApiKey(); // Reset the new key

            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Success", "Key created successfully."));
            PrimeFaces.current().executeScript("PF('apiKeyDialog').show();");
        } catch (ApiException ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", ex.getMessage()));
        }

    }

    public void updateKey(int id) {
        smsApiService.updateSmsApiKey(id);
        TSmsApiKey updatedKey = smsApiService.getSmsApiKeyById(id);
        selectedApiKey = updatedKey.getApiKey();
        refreshKeys();
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Success", "Key updated successfully."));
        PrimeFaces.current().executeScript("PF('apiKeyDialog').show();");
    }

    public void deleteKey(int id) {
        smsApiService.deleteSmsApiKey(id);
        refreshKeys();
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Success", "Key deleted successfully."));
    }

    public void deactivateKey(int id) {
        smsApiService.updateApiKeyStatus(id, 0);
        refreshKeys();
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Success", "Key deactivated successfully."));
    }

    public void activateKey(int id) {
        smsApiService.updateApiKeyStatus(id, 1);
        refreshKeys();
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Success", "Key activated successfully."));
    }

    private void refreshKeys() {
        List<TSmsApiKey> keyList = smsApiService.getAllSmsApiKeys();
        keys = new TSmsApiKeyDataModel(keyList);
    }

    public void prepareToShowApiKey(int id) {
        TSmsApiKey key = smsApiService.getSmsApiKeyById(id);
        this.selectedApiKey = key.getApiKey();
    }

    public String maskApiKey(String apiKey) {
        if (apiKey == null || apiKey.isEmpty()) {
            return "";
        }

        int visibleChars = 5;
        int totalLength = 70;
        int maskedLength = Math.max(0, totalLength - visibleChars);

        StringBuilder maskedPart = new StringBuilder();
        for (int i = 0; i < maskedLength; i++) {
            maskedPart.append("*");
        }

        String visiblePart;
        if (apiKey.length() <= visibleChars) {
            visiblePart = apiKey;
        } else {
            visiblePart = apiKey.substring(apiKey.length() - visibleChars);
        }

        return maskedPart.toString() + visiblePart;
    }

//    Dialog boxes
    //update
    public void showUpdateDialog(int id) {
        this.setSelectedKeyId(id);
        PrimeFaces.current().executeScript("PF('confirmRegenerate').show();");
    }

    public void confirmUpdate() {
        updateKey(selectedKeyId);

        PrimeFaces.current().executeScript("PF('confirmRegenerate').hide();");
    }

    //Activate
    public void showActivateDialog(int id) {
        this.setSelectedKeyId(id);
        PrimeFaces.current().executeScript("PF('confirmActivate').show();");
    }

    public void confirmActivate() {
        activateKey(selectedKeyId);
        PrimeFaces.current().executeScript("PF('confirmActivate').hide();");
    }

    //Deactivate
    public void showDeactivateDialog(int id) {
        this.setSelectedKeyId(id);
        PrimeFaces.current().executeScript("PF('confirmDeactivate').show();");
    }

    public void confirmDeactivate() {
        deactivateKey(selectedKeyId);
        PrimeFaces.current().executeScript("PF('confirmDeactivate').hide();");
    }

    //Delete
    public void showDeleteDialog(int id) {
        this.setSelectedKeyId(id);
        PrimeFaces.current().executeScript("PF('cd').show();");

    }

    public void confirmDelete() {
        deleteKey(selectedKeyId);
        PrimeFaces.current().executeScript("PF('cd').hide();");
    }
    
    
    public void showUpdate(TSmsApiKey key){
        this.setManageUserKey(key);
        System.out.println("selected key name for update: " + manageUserKey.getName() + "\n username: " + 
                manageUserKey.getUsername() + " \n Id: " + manageUserKey.getId() + "\n userId: " + manageUserKey.getUserId());
    }
    
    public void updateUserApiKeyName(){
        smsApiService.updateSMSApiKeyName(manageUserKey);
    }

}
