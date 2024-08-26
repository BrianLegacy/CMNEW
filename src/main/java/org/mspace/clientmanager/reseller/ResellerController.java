/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.mspace.clientmanager.reseller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import ke.co.mspace.nonsmppmanager.model.Alpha;
import ke.co.mspace.nonsmppmanager.service.AlphaServiceApi;
import ke.co.mspace.nonsmppmanager.service.AlphaServiceImpl;
import ke.co.mspace.nonsmppmanager.util.JdbcUtil;
import ke.co.mspace.nonsmppmanager.util.JsfUtil;
import org.mspace.clientmanager.group.GroupDAO;
import org.mspace.clientmanager.group.GroupDAOImpl;
import org.mspace.clientmanager.user.UserController;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.file.UploadedFile;

/**
 *
 * @author olal
 */
@ManagedBean(name = "resellercontroller")
@ViewScoped
public class ResellerController implements Serializable {

    private static final long serialVersionUID = 1L;

    private ResellerDAO resellerDAO;
    private List<UserController> resellers;
    private UserController newReseller;
    private UserController currentReseller;
    private AlphaServiceApi alphaDAO;
    private String alphanumeric;
    private Alpha selectedAlpha;
    private List<Alpha> senderIds;
    private List<SelectItem> listGroups;
    private GroupDAO groupDAO;
    private String username;
    private String newPassword;
    private String confirmPassword;
    private List<SelectItem> users;

    private final JdbcUtil jdbcUtil = new JdbcUtil();

    private String logoLocation = "";

    @PostConstruct
    public void init() {
        resellerDAO = new ResellerDAOImpl();
        alphaDAO = new AlphaServiceImpl();
        groupDAO = new GroupDAOImpl();
        refreshUsers();
        newReseller = new UserController();
    }

    public List<SelectItem> getUsers() {
        users = resellerDAO.users();
        return users;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }
    
    
    public List<UserController> getResellers() {
        return resellers;
    }

    public List<Alpha> getSenderIds() {
        return senderIds;
    }

    public List<SelectItem> getListGroups() {
        return listGroups;
    }

    public UserController getNewReseller() {
        return newReseller;
    }

    public void setNewReseller(UserController newReseller) {
        this.newReseller = newReseller;
    }

    public UserController getCurrentReseller() {
        return currentReseller;
    }

    public void setCurrentReseller(UserController currentReseller) {
        this.currentReseller = currentReseller;
        refreshAlphas();
    }

    public String getAlphanumeric() {
        return alphanumeric;
    }

    public void setAlphanumeric(String alphanumeric) {
        this.alphanumeric = alphanumeric;
    }

    public Alpha getSelectedAlpha() {
        return selectedAlpha;
    }

    public void setSelectedAlpha(Alpha selectedAlpha) {
        this.selectedAlpha = selectedAlpha;
    }

    public void addReseller() {
        if (newReseller != null) {
            try {
                newReseller.saveReseller();
                refreshUsers();
            } catch (IOException e) {
                JsfUtil.addErrorMessage("Error occured while trying to create user");
            }
        }
    }

    public void changePass() {
        if (!username.isEmpty() && !newPassword.isEmpty()) {
            if (!newPassword.equals(confirmPassword)) {
                FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Passwords do not match!"));
                return;
            }
            if (resellerDAO.changePass(username, newPassword.trim())) {
                JsfUtil.addSuccessMessage(username + "'s password changed successfully");
            } else {
                JsfUtil.addErrorMessage("Failed to change password try again");
            }

        } else {
            JsfUtil.addErrorMessage("Enter username and Password");
        }
    }

    public void editReseller() {
        if (currentReseller != null) {

            if (resellerDAO.editReseller(currentReseller)) {
                JsfUtil.addSuccessMessage("Success, User updated successfully ");
            } else {
                JsfUtil.addErrorMessage("Error while editing user");
            }
            refreshUsers();
        } else {
            JsfUtil.addErrorMessage("No User selected for update.");
        }
    }

    public void manageCredit() {
        if (currentReseller != null) {
            try {
                currentReseller.manageCredit();
                refreshUsers();
            } catch (IOException e) {
                JsfUtil.addErrorMessage("Error reading the form try again");
            }
        }
    }

    public void manageEmailCredit() {
        if (currentReseller != null) {
            try {
                currentReseller.manageEmailCredit();
                refreshUsers();
            } catch (IOException e) {

                JsfUtil.addErrorMessage("Error while editing credits try again. ");
            }
        }
    }

    public void uploadResellerLogo(FileUploadEvent event) {
        if (currentReseller != null) {
            uploadResImage(event, currentReseller.getUsername());
        } else {
            JsfUtil.addErrorMessage("No current reseller selected.");
        }
    }

    public void deleteReseller() {
        if (currentReseller != null) {
            if (resellerDAO.delReseller(currentReseller)) {
                refreshUsers();
                JsfUtil.addSuccessMessage("Reseller was deleted successfully. ");
            } else {
                JsfUtil.addSuccessMessage("Try again Something went wrong. ");
            }
        } else {
            JsfUtil.addErrorMessage("Select Reseller and try again. ");
        }
    }

    public void assignAlpha() {
        try (Connection conn = jdbcUtil.getConnectionTodbSMS()) {
            if (alphaDAO.updateAgentAlphas(currentReseller.getId().toString(), alphanumeric, conn)) {
                refreshAlphas();
                JsfUtil.addSuccessMessage("Sender ID assigned successfully. ");
            } else {
                JsfUtil.addErrorMessage("The ID was not assigned. Try again.");
            }

        } catch (SQLException ex) {
            Logger.getLogger(ResellerController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void delAlpha() {
        if (selectedAlpha != null) {

            try (Connection conn = jdbcUtil.getConnectionTodbSMS()) {
                if (alphaDAO.removeAgentAlpha(selectedAlpha, conn)) {
                    refreshAlphas();
                    JsfUtil.addSuccessMessage("Successfully deleted Sender ID");
                } else {
                    JsfUtil.addErrorMessage("Something went wrong, Try again");
                }

            } catch (SQLException ex) {
                Logger.getLogger(ResellerController.class.getName()).log(Level.SEVERE, null, ex);

            }
        } else {
            JsfUtil.addErrorMessage("Select alpha to delete");
        }

    }

    public void refreshAlphas() {

        try (Connection conn = jdbcUtil.getConnectionTodbSMS()) {
            senderIds = alphaDAO.getAgentAlphas(conn, currentReseller.getId().toString());
        } catch (SQLException ex) {
            Logger.getLogger(ResellerController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void refreshUsers() {
        resellers = resellerDAO.fetchResellers();
    }

    public void uploadResImage(FileUploadEvent event, String username) {
        try {
            System.out.println("The tomcat directory is:" + System.getProperty("catalina.base"));
            System.out.println("Here we are: " + username);

            UploadedFile uploadedFile = event.getFile();
            String fileName = uploadedFile.getFileName();

            // Save the uploaded file to a temporary location
            File tempFile = new File(System.getProperty("java.io.tmpdir"), fileName);
            try (InputStream input = uploadedFile.getInputStream(); OutputStream output = new FileOutputStream(tempFile)) {
                byte[] buffer = new byte[1024];
                int bytesRead;
                while ((bytesRead = input.read(buffer)) != -1) {
                    output.write(buffer, 0, bytesRead);
                }
            }

            System.out.println("This is the original file name: " + fileName + " | content-type: " + uploadedFile.getContentType());
            System.out.println("File saved to temporary location: " + tempFile.getAbsolutePath());

            File inConfig = new File(System.getProperty("catalina.home") + "/webapps/files/config/" + username.concat("Logo")
                    .replace(" ", "")
                    .concat(getFileExtension(fileName)));

            boolean renamed = tempFile.renameTo(inConfig);
            if (renamed) {
                System.out.println("File successfully copied to: " + inConfig.getName());
            } else {
                System.out.println("Failed to move file to: " + inConfig.getAbsolutePath());
            }

            System.out.println("This will be uploaded to db: " + inConfig.getAbsolutePath());
            String filedir = "../files/config/" + inConfig.getName();
            System.out.println("Path to database is: " + filedir);
            logoLocation = System.getProperty("catalina.home") + "/files/config/" + username.concat("Logo")
                    .replace(" ", "")
                    .concat(getFileExtension(fileName));

            if (resellerDAO.setImagePath(username, filedir)) {
                JsfUtil.addSuccessMessage("User logo updated successfully");
            } else {
                JsfUtil.addErrorMessage("User logo not updated!");
            }

        } catch (IOException ex) {
            Logger.getLogger(ResellerController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void renameUploadedFile(File originalFile, File newFile) throws IOException {
        if (originalFile.exists()) {
            System.out.println("File found");
        } else {
            System.out.println("File not found");
        }
        boolean success = originalFile.renameTo(newFile);
        System.out.println(success);
    }

    public String getLogoLocation() {
        return logoLocation;
    }

    public void setLogoLocation(String logoLocation) {
        this.logoLocation = logoLocation;
    }

    // Helper method to get the file extension
    public String getFileExtension(String fileName) {
        int lastIndex = fileName.lastIndexOf('.');
        if (lastIndex == -1) {
            return "";
        }
        return fileName.substring(lastIndex);
    }

    public void resetUser() {
        newReseller = new UserController();
    }
}
