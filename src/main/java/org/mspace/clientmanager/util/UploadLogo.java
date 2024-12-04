/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.mspace.clientmanager.util;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.file.UploadedFile;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpSession;
import ke.co.mspace.nonsmppmanager.util.JdbcUtil;

/**
 *
 * @author olal
 */
public class UploadLogo {

    private final JdbcUtil util = new JdbcUtil();
    private static final Logger LOGGER = Logger.getLogger(UploadLogo.class.getName());
    private static final String FILE_DIRECTORY = "/path/to/save/directory/"; 
    private static final String BASE_URL = "https://www.mspace.co.ke/files/config/";
    
    HttpSession session = ke.co.mspace.nonsmppmanager.invalids.getsession.getSession();
    Long user_id = (Long) session.getAttribute("id");

    public void handleFileUpload(FileUploadEvent event) {
        UploadedFile uploadedFile = event.getFile();
        String fileName = uploadedFile.getFileName();
        String destination = BASE_URL + fileName;

        // Save the file to the server
        try {
            saveFile(uploadedFile.getInputStream(), fileName);
            // Update the database with the file path
            setImagePath(user_id, destination); // Example ID, replace with the actual ID
            FacesMessage message = new FacesMessage("Successful", fileName + " is uploaded.");
            FacesContext.getCurrentInstance().addMessage(null, message);
        } catch (IOException | SQLException e) {
            FacesMessage message = new FacesMessage("Error", "File upload failed.");
            FacesContext.getCurrentInstance().addMessage(null, message);
            LOGGER.log(Level.SEVERE, "File upload failed", e);
        }
    }

    private void saveFile(InputStream inputStream, String fileName) throws IOException {
        File targetFile = new File(BASE_URL + fileName);
        Files.copy(inputStream, targetFile.toPath());
    }

    public void setImagePath(long id, String picPath) throws SQLException {
        String sqlUpdate = "UPDATE dbTASK.tClient SET picPath= ? WHERE id= ?";
        try (Connection conn = util.getConnectionTodbTask(); PreparedStatement pstm = conn.prepareStatement(sqlUpdate)) {
            pstm.setString(1, picPath);
            pstm.setLong(2, id);
            pstm.executeUpdate();
        }catch(SQLException e){
            System.out.println(""+ e);
        }
       
    }

}
