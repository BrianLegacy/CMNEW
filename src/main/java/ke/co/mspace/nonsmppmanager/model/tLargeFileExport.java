/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ke.co.mspace.nonsmppmanager.model;

/**
 *
 * @author Samson
 */
import java.io.Serializable;
import java.math.RoundingMode;
import java.sql.Date;
import java.text.DecimalFormat;
public class tLargeFileExport implements Serializable{
    
    private int id;
    private int user_id;
    private String sql_querry;
    private Date date_inserted;
    private String excel_location;
    private     String report_name;
    private String file_size;
    private Date date_finished;
    private int status;
    private String status_desc;
    private String color;

    public tLargeFileExport() {
    }

    public tLargeFileExport(int id, int user_id, String sql_querry, Date date_inserted, String excel_location, String report_name, String file_size, Date date_finished, int status, String status_desc, String color) {
        this.id = id;
        this.user_id = user_id;
        this.sql_querry = sql_querry;
        this.date_inserted = date_inserted;
        this.excel_location = excel_location;
        this.report_name = report_name;
        this.file_size = file_size;
        this.date_finished = date_finished;
        this.status = status;
        this.status_desc = status_desc;
        this.color = color;
    }

    public String getStatus_desc() {
        return status_desc;
    }

    public void setStatus_desc(String status_desc) {
       
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
       
    }

 

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getSql_querry() {
        return sql_querry;
    }

    public void setSql_querry(String sql_querry) {
        this.sql_querry = sql_querry;
    }

    public Date getDate_inserted() {
        return date_inserted;
    }

    public void setDate_inserted(Date date_inserted) {
        this.date_inserted = date_inserted;
    }

    public String getExcel_location() {
        return excel_location;
    }

    public void setExcel_location(String excel_location) {
        this.excel_location = excel_location;
    }

    public String getReport_name() {
        return report_name;
    }

    public void setReport_name(String report_name) {
        this.report_name = report_name;
    }

    public String getFile_size() {
       Double double_fileSize =Double.parseDouble(file_size);
       double_fileSize=double_fileSize/1000000;
        DecimalFormat decimalFormat=new DecimalFormat("#.####");
        decimalFormat.setRoundingMode(RoundingMode.CEILING);
        return decimalFormat.format(double_fileSize);
        
        
    }

    public void setFile_size(String file_size) {
        this.file_size = file_size;
    }

    public Date getDate_finished() {
        return date_finished;
    }

    public void setDate_finished(Date date_finished) {
        this.date_finished = date_finished;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        if (status==0){
            this.status_desc="processing";
            this.color="red";}
            else{
            this.status_desc="ready";
             this.color="green";
                    }
        
        this.status = status;
    }

    @Override
    public String toString() {
        return "tLargeFileExport{" + "id=" + id + ", user_id=" + user_id + ", sql_querry=" + sql_querry + ", date_inserted=" + date_inserted + ", excel_location=" + excel_location + ", report_name=" + report_name + ", file_size=" + file_size + ", date_finished=" + date_finished + ", status=" + status + '}';
    }
    
    
    
    
}
