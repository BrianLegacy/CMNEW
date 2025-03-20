/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ke.co.mspace.nonsmppmanager.service;

import java.util.List;
import ke.co.mspace.nonsmppmanager.model.EmailOut;

/**
 *
 * @author amos
 */
public interface EmailOutServiceApi {
    
    List<EmailOut> fetchEmailReport(String user, String startDate, String endDate, int first, int pageSize);
    
    public int fetchRows(String user, String startDate, String endDate);
    
//    public String insertIntotLARGEREPORTEXPORT(String user, String startDate, String endDate);
    
//    List<LargeReport> fetchBulkReports(String username);
    
}





