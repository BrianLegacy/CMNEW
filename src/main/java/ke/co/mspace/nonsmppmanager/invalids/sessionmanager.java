/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ke.co.mspace.nonsmppmanager.invalids;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Iterator;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;
import ke.co.mspace.nonsmppmanager.model.UserProfile;
import ke.co.mspace.nonsmppmanager.service.UserScroller;
import ke.co.mspace.nonsmppmanager.util.JdbcUtil;
import ke.co.mspace.nonsmppmanager.util.PasswordUtil;
import org.primefaces.model.file.UploadedFile;

/**
 *
 * @author mspace
 */
@ManagedBean(name = "sessionmanager")
@SessionScoped
public class sessionmanager implements Serializable {

    HttpSession session = getsession.getSession();
    long remainigsms;
    String company_logopath;
    private String info;
    String fileName = null;
    File file;
    UploadedFile uploadedFile;
    private String path = "";
    private String loggedInuser = (String) session.getAttribute("username");
    Long id = (Long) session.getAttribute("id");
    private String username = (String) session.getAttribute("username");
    private String newPassword;

    public String getInfo() {

        int temporaladmin = Character.getNumericValue((char) session.getAttribute("temporaladmin"));

        String agent = (String) session.getAttribute("agent");

        if (!agent.isEmpty()) {
            return " ";
        }
        if (temporaladmin == 3) {
            return " ";
        }

        Object obj = session.getAttribute("resellerId");

        if (obj != null) {
            info = obj.toString().equals("0") ? "We hope you enjoy your MSpace SMS Experience.<br/><br/>"
                    + "For any comments or queries, please do not hesitate to contact our support desk "
                    + "support@mspace.co.ke or call us on 0722 962 934" : "";
        }
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public long getRemainigsms() {
        System.out.println("Long Range Validate rmaining sms" + remainigsms);
        return remainigsms;
    }

    public void setRemainigsms(long remainigsms) {
        this.remainigsms = remainigsms;
    }

    public String getCompany_logopath() {
        return company_logopath;
    }

    public void setCompany_logopath(String company_logopath) {
        this.company_logopath = company_logopath;
    }

    public sessionmanager() {
    }

    public String getsessionname() {
        String m = "Welcome ";
        remainigsms = (long) session.getAttribute("max_total");
        int emailBalance = (int) session.getAttribute("max_contacts");
        String username = (String) session.getAttribute("username");

        // Check if remainigsms or emailBalance are -1 and adjust the message accordingly
        String smsBalanceMessage = (remainigsms == -1) ? "Unlimited" : String.valueOf(remainigsms);
        String emailBalanceMessage = (emailBalance == -1) ? "Unlimited" : String.valueOf(emailBalance);

        // Formulate the message
        String message = m + username + " - SMS : <b>" + smsBalanceMessage + "</b>, Email : <b>" + emailBalanceMessage + "</b>";
        String reseller = m + "<b>" + username + "</b>";
        return (remainigsms == -1) ? message : reseller;
    }

    public Boolean showChat() {

        String uname = FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("admin").toString();
        String agent = (String) getsession.getSession().getAttribute("agent");
        if (!agent.isEmpty()) {
            return false;
        }
        return !uname.equalsIgnoreCase("3") ? true : false;
    }

    public String getsubaccstatus() {
        String username = (String) session.getAttribute("subaccount");
        return username;
    }

    public String getsessionnameonly() {
        HttpSession session = getsession.getSession();
        String username = (String) session.getAttribute("username");
        return username;
    }

    public String getPayBill() {
        String resPayBill = (String) session.getAttribute("resellerpaybill");
        int resId = (int) session.getAttribute("resellerId");

        return resId > 0 ? resPayBill : "778827";
    }

    client_logo clnt = new client_logo();

    public String getLogoPath() {

        System.out.println("home page settingkserter");
        session.setAttribute("resellerId", 0);
        char adminv;

        if ((org.mspace.clientmanager.util.getsession.getSession().getAttribute("temporaladmin")) != null) {
            System.out.println("temporal admin not null");
            adminv = (char) org.mspace.clientmanager.util.getsession.getSession().getAttribute("temporaladmin");
            if (adminv == '1') {
                return clnt.clnt_logo_mod();
            }
            long loggedInId = (long) org.mspace.clientmanager.util.getsession.getSession().getAttribute("id");
            String loggedInUsername = (String) org.mspace.clientmanager.util.getsession.getSession().getAttribute("username");
            String retriedImagePath = clnt.clnt_logo2(loggedInUsername);
            session.setAttribute("clientlogopath", retriedImagePath);
            System.out.println("home page logged in id  " + loggedInId);
            System.out.println("homepage clientlogopath " + retriedImagePath);
            return retriedImagePath;

        }

        HttpSession session = getsession.getSession();
        company_logopath = (String) session.getAttribute("clientlogopath");

//                company_logopath =client_logo.clnt_logo();
        //String path = new File("").getAbsolutePath();
        //String paath = session.getServletContext().getRealPath("/");
        System.out.println("String real path: " + company_logopath);
        System.out.println("String real path: " + (String) session.getAttribute("logopath2"));
//         return "https://i.pinimg.com/originals/cf/8a/11/cf8a11b44a748c4ce286fb020f920ada.png";
        return company_logopath;

    }

    public void handleFileUpload(UploadedFile uploadedFile1) {
//        UploadedFile item = event.getFile();
//        System.out.println("");

        UploadedFile item = uploadedFile1;

        fileName = "";
        System.out.println("The file+ +++>>>" + item.getFileName());
        if (item.getFileName() != null) {
            //uploadedFile = event.getFile();
            //fileName = uploadedFile.getFileName();

            fileName = linuxFileName(item.getFileName());
            System.out.println("The Linux file+ +++>>>" + fileName);
            try {
                InputStream inputs = uploadedFile1.getInputStream();
                System.out.println("inputs " + inputs);
                String timestamp = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
                String undersc = "-";
                fileName = undersc + fileName
                        .replace(" ", "-");
                fileName = timestamp.concat(fileName);

                System.out.println("Name of Excel uploaded is " + fileName);

                try {
                    System.out.println("Uploaded file:  " + fileName);
                    if (!fileName.isEmpty() || fileName != null) {
                        this.copyFile(fileName, inputs);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            } catch (Exception m) {
                m.printStackTrace();
//                messager = new FacesMessage(" Not Succesful", " Check the columns of your Excel file.");
//                FacesContext.getCurrentInstance().addMessage(null, messager);
            }

        } else {
//            messager = new FacesMessage(" NOT Succesful", item.getFileName() + " is not  uploaded.");
//            FacesContext.getCurrentInstance().addMessage(null, messager);
        }
        System.out.println("Exiting file upload safely");
        path = "../files/config/" + fileName;

    }

    public void copyFile(String fileName, InputStream in) {
        try {
            String catalina = System.getProperty("catalina.home");
            String destination = catalina + "/webapps/files/config/";
            // write the inputStream to a FileOutputStream

            OutputStream out = new FileOutputStream(new java.io.File(destination + fileName));

            int read = 0;
            byte[] bytes = new byte[1024];

            while ((read = in.read(bytes)) != -1) {
                out.write(bytes, 0, read);
            }

            in.close();
            out.flush();
            out.close();

            System.out.println("file copied to !" + destination + fileName);
        } catch (IOException e) {
            System.out.println("check System space remaining ");
            System.out.println(e.getMessage());
        }
    }

    private String linuxFileName(String fileName) {
        char j = '"';
        ArrayList StrChrList = new ArrayList(Arrays.asList("-,:@%$&#+<>?!=/\\*(){}[]~`".concat(String.valueOf(j)).split("")));
        for (Iterator it = StrChrList.iterator(); it.hasNext();) {
            Object obj = it.next();
            fileName = fileName.replace(" ", "_");
            if (fileName.contains(obj.toString())) {
                fileName = fileName.replace(obj.toString(), "");
            }
        }
        return fileName;
    }

    public String getUrl() {
        HttpSession session = getsession.getSession();
        String agent = (String) session.getAttribute("agent");
        int temporaladmin = Character.getNumericValue((char) session.getAttribute("temporaladmin"));
        if (!agent.isEmpty()) {
            return "";
        }
        if (temporaladmin == 3) {
            return "";
        }
        return (String) session.getAttribute("urlLink");
    }

    public String getClientname() {
        String agent = (String) session.getAttribute("agent");
        int temporaladmin = Character.getNumericValue((char) session.getAttribute("temporaladmin"));
        if (!agent.isEmpty()) {
            return "";
        }
        if (temporaladmin == 3) {
            return "";
        }
        HttpSession session = getsession.getSession();
        return (String) session.getAttribute("clientname");
    }

    public UploadedFile getUploadedFile() {
        return uploadedFile;
    }

    public void setUploadedFile(UploadedFile uploadedFile) {
        this.uploadedFile = uploadedFile;
    }

    public void storeData() {
          updateTuser_username();

        if (this.uploadedFile != null) {
            System.out.println("uploadedFile: " + uploadedFile.getFileName());

            handleFileUpload(uploadedFile);
            System.out.println("path: " + path);

        } else {
            System.out.println("No file selected!");
        }

        System.out.println("updated username: " + loggedInuser);
        System.out.println("path: " + path);
        
        String updateSql;
        if(this.uploadedFile != null){
            System.out.println("1 called");
            updateSql = "Update dbTASK.tClient set picPath='" + path + "', clientName='" + loggedInuser + "' where clientName='" + username + "'";
        }else{
            System.out.println("2 called");
           updateSql = "Update dbTASK.tClient set clientName='" + loggedInuser + "' where clientName='" + username + "'";
        }
        
       
        try {
            JdbcUtil jdbcUtil = new JdbcUtil();

            try (Connection conn = jdbcUtil.getConnectionTodbSMS()) {
                System.out.println("update sql: " +updateSql);
                PreparedStatement ps = conn.prepareStatement(updateSql);
                int result = ps.executeUpdate();
                if (result > 0) {
                    System.out.println("Successfully updated client logo");
                    FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Info", "Successfully updated client logo");
                    FacesContext.getCurrentInstance().addMessage(null, message);
                }
            }
        } catch (SQLException e) {
            System.out.println("++++++++++++++++++++++++++");
            System.out.println("An sql exception has occured " + e);

        }

    }

    public void saveNewPassword() {
        if (newPassword != null) {

            String encryptedPassword = PasswordUtil.encrypt(newPassword);

            String sql = "UPDATE dbSMS.tUSER SET password = ? WHERE id = ?";

            JdbcUtil jdbcUtil = new JdbcUtil();
            try (Connection conn = jdbcUtil.getConnectionTodbSMS()) {

                PreparedStatement ps = conn.prepareStatement(sql);

                ps.setString(1, encryptedPassword);
                ps.setLong(2, id);
                
                int result = ps.executeUpdate();
                
                if(result > 0){
                    System.out.println("Successfully updated password");
                    FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "info", "Successfully updated user password");
                    FacesContext.getCurrentInstance().addMessage(null, message);
                }else{
                    System.out.println("Password not updated!");
                }

            } catch (SQLException e) {
                System.out.println("An sql exception has occured " + e);
            }
        }
    }

    public void updateTuser_username() {
        JdbcUtil jdbcUtil = new JdbcUtil();

        String updateSql = "UPDATE dbSMS.tUSER SET username=? WHERE id=?";

        try (Connection conn = jdbcUtil.getConnectionTodbSMS()) {
            PreparedStatement ps = conn.prepareStatement(updateSql);
            ps.setString(1, loggedInuser);
            ps.setLong(2, id);
            int result = ps.executeUpdate();
            if (result > 0) {
                System.out.println("Successfully updated users name");
            }
        } catch (SQLException e) {
            System.out.println("An sql exception has occured " + e);

        }
    }

    public String getLoggedInuser() {
        return loggedInuser;
    }

    public void setLoggedInuser(String loggedInuser) {
        this.loggedInuser = loggedInuser;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

}
