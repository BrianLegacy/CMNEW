/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ke.co.mspace.export;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.FileSystemException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import ke.co.mspace.nonsmppmanager.model.tLargeFileExport;
import ke.co.mspace.nonsmppmanager.util.JdbcUtil;
import ke.co.mspace.nonsmppmanager.util.JsfUtil;

/**
 *
 * @author developer
 */
public class LargeFileExport implements Serializable{

    public void checkSmsList(Connection con, String reportName) {
        //smsListSize = list.size();
        String reportSQL = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("reportSQL");
        int loggedInUser = Integer.valueOf(FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("id").toString());
        //System.out.println("The list is " + list.size());
       
//        System.out.println("Large Export indetified");
        loadLargereportExport(reportSQL, loggedInUser, con, reportName);
//        try {
//            System.out.println("Attmpting Redirection....");
//            FacesContext.getCurrentInstance().getExternalContext().redirect("http://localhost:8080/ReportStatus/report.xhtml?uid=" + loggedInUser);
//        } catch (IOException ex) {
//            ex.printStackTrace();
//        }
    }

    public void loadLargereportExport(String sql, int user_id, Connection con, String reportName) {
        if(sql.contains("SMPP")){
            reportName="SMPP OUT REPORT";
        }
        //Iserts data to table tLARGEREPORTEXPORT when a certain limit is exceeded 
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
       
        String loggedUser = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("loggedInUser");
        String insert = "INSERT INTO tLARGEREPORTEXPORT (user_id,sql_querry,date_inserted,excel_file_location,report_name,status,system) "
                + "VALUES(?,?,NOW(),?,?,?,?)";
        //Database obj = new Database();
        //Connection con = obj.getConnectiondbSMS();
        
        PreparedStatement st = null;
        ResultSet rs = null;
        try {
            st = con.prepareStatement(insert);
            st.setInt(1, user_id);
            st.setString(2, sql);
//            File fileDir = new File(System.getProperty("user.home") + "/apache-tomcat-8.5.66/webapps/files/SMSBulkReports/");
File fileDir = new File("/files/SMSBulkReports/");
            // System.out.println("Home location " + System.getProperty("user.home"));
//                        boolean success = new File(System.getProperty("catalina.home") + "/apache-tomcat-8.5.66/webapps/files/SMSBulkReports/").mkdirs();

            boolean success = new File("/files/SMSBulkReports/").mkdirs();
            reportName = reportName.replaceAll(" ", "_");
            String formated_date=sdf.format(new Date()).toString();
            formated_date=formated_date.replaceAll("-", "").replaceAll(" ","").replace(":", "");
            
            reportName = loggedUser.toUpperCase() + "_" + reportName +"_"+formated_date;

            //horace
             

            
            //
            File tempFile = new File(fileDir.getAbsolutePath() + "/" + reportName + ".xlsx");
            if(tempFile.isDirectory()){
                throw new FileSystemException(tempFile.getAbsolutePath()+" is a directory");
            }
            //tempFile.createNewFile();
            if (tempFile.createNewFile()) {
            } else {
               
                throw new FileAlreadyExistsException(tempFile.getAbsolutePath()+" file already exists");
            }
            if (!fileDir.exists()) {
                fileDir.mkdirs();
                // File tempFile = new File(fileDir.getAbsolutePath() + reportName + ".xlsx");
                tempFile = new File(fileDir.getAbsolutePath() + reportName + ".xlsx");
                if (!tempFile.exists()) {
                    File file;
                    file = new File(fileDir.getAbsolutePath() + "/" + reportName + ".xlsx");
                }
                //new File(System.getProperty("user.home")+"/Files/export/").mkdirs();
                //st.setString(3, System.getProperty("user.home") + "/Files/export/" + reportDesc + ".xlsx");
            } else {
            }

            st.setString(3, fileDir.getAbsolutePath() + "/" + reportName + ".xlsx");
            //st.setString(3, "/Files/export/" + reportDesc + ".xlsx");
            st.setString(4, reportName);
            st.setInt(5, 0);
            st.setString(6, "Client-Manager-System");
            int updated = st.executeUpdate();
            if (updated > 0) {
                //executeScript();
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            Logger.getLogger(LargeFileExport.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void executeScript() {
        Process p;
        try {
            List<String> cmdList = new ArrayList<String>();
            // adding command and args to the list
            cmdList.add("sh");
            cmdList.add("/files/export/reportscript.sh");
            //cmdList.add("/home/developer/shellscripts/reportscript.sh");
            ProcessBuilder pb = new ProcessBuilder(cmdList);
            p = pb.start();
            p.waitFor();
            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    p.getInputStream()));
            BufferedReader errorReader = new BufferedReader(new InputStreamReader(p.getErrorStream()));
            String line;
            while ((line = reader.readLine()) != null) {
            }
            line = "";
            while ((line = errorReader.readLine()) != null) {
              
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    
    public List<tLargeFileExport> getExports(){
        JdbcUtil jdbc=new JdbcUtil();
       
        List<tLargeFileExport> list=new ArrayList<>();
        
        try {
           Connection con= jdbc.getConnectionTodbSMS();
          
           Statement st=con.createStatement();
                   int loggedInUser = (Integer.valueOf(FacesContext.getCurrentInstance()
                           .getExternalContext().getSessionMap().get("id").hashCode())) ;
           
            String selectNormalUser="select * from tLARGEREPORTEXPORT where user_id="+loggedInUser+" order by id desc";
            String selectResseller="select * from tLARGEREPORTEXPORT as tl inner join tUSER as tu on tl.user_id = tu.id where (tl.user_id="+loggedInUser+" or tu.agent ="+loggedInUser+") order by tl.id desc";
             String selectADMIN="select * from tLARGEREPORTEXPORT order by id desc";
             String LoggedInIserName=getLoggedInUserName(loggedInUser);
           
            String selectSQL= LoggedInIserName.equals("admin")?selectADMIN:selectResseller;
     
          ResultSet rs= st.executeQuery(selectSQL);
          while(rs.next()){
               tLargeFileExport export=new tLargeFileExport();
               export.setId(rs.getInt("id"));
               export.setUser_id(rs.getInt("user_id"));
               export.setSql_querry(rs.getString("sql_querry"));
               export.setDate_inserted(rs.getDate("date_inserted"));
               export.setExcel_location(rs.getString("excel_file_location"));
               export.setReport_name(rs.getString("report_name"));
               export.setFile_size(rs.getString("file_size"));
               export.setDate_finished(rs.getDate("date_finished"));
               export.setStatus(rs.getInt("status"));
               if(rs.getInt("status")==0){
                NotAllProcessed();
               }
               list.add(export);
          }
           
            
        } catch (SQLException ex) {
            Logger.getLogger(LargeFileExport.class.getName()).log(Level.SEVERE, null, ex);
        }
        int status=0;
       for(tLargeFileExport l:list){
         if (l.getStatus()==0){
           status=1;
       }
       }
       if(status==0){
       onAllProcessed();
       }
        return list;
    }
    int called=0;
    private void NotAllProcessed() {
        if(called==0){
           FacesMessage facesMsg = new FacesMessage(FacesMessage.SEVERITY_INFO, "still processing some messages", "Not done");
        FacesContext.getCurrentInstance().addMessage(null, facesMsg);
        called=1;
    }}

    private void onAllProcessed() {
         FacesMessage facesMsg = new FacesMessage(FacesMessage.SEVERITY_INFO, "All processing done", "Done");
        FacesContext.getCurrentInstance().addMessage(null, facesMsg);
    }
    public String getLoggedInUserName(int loggedID){
        String username="";
       JdbcUtil jdbcUtil=new JdbcUtil();
        try {
            Connection con=jdbcUtil.getConnectionTodbSMS();
            ResultSet rs=con.prepareStatement("select username from tUSER where id="+loggedID).executeQuery();
            rs.next();
          username= rs.getString("username");
           return username;
        } catch (SQLException ex) {
            Logger.getLogger(LargeFileExport.class.getName()).log(Level.SEVERE, null, ex);
        }
        return username;
    }

}
