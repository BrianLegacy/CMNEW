/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ke.co.mspace.nonsmppmanager.model;

import java.util.List;
import java.util.Map;
import org.mspace.clientmanager.sms.SmsDAO;
import org.mspace.clientmanager.sms.SmsDAOImpl;
import org.mspace.clientmanager.user.UserController;
import org.primefaces.model.FilterMeta;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortMeta;

/**
 *
 * @author amos
 */

public class SmsLazyDataModel extends LazyDataModel<UserController>{

    private final SmsDAO smsDAO;

    // Constructor to inject DAO dependency
    public SmsLazyDataModel(SmsDAOImpl smsDAOImpl) {
        this.smsDAO = smsDAOImpl;
    }

    // Override the load method to handle pagination
  
    
    @Override
    public List<UserController> load(int first, int pageSize, Map<String, SortMeta> sortBy, Map<String, FilterMeta> filterBy) {
        
        String usernameFilter = null;
        
        if(!filterBy.isEmpty() && filterBy.containsKey("username")){
                FilterMeta filterMeta = filterBy.get("username");
                if(filterMeta != null && filterMeta.getFilterValue() != null){
                    usernameFilter = filterMeta.getFilterValue().toString();
                    
                }
    }
        
        int totalRecords = smsDAO.countSmsUsers(); 
        System.out.println("Total records for pagination are: " + totalRecords);
        this.setRowCount(totalRecords); 

        // Fetch the data for the current page
        return smsDAO.fetchSmsusers(pageSize, first, usernameFilter);
    }
    
     @Override
    public int count(Map<String, FilterMeta> filterBy) {
         String usernameFilter = null;

    if (!filterBy.isEmpty() && filterBy.containsKey("username")) {
        FilterMeta filterMeta = filterBy.get("username");
        if (filterMeta != null && filterMeta.getFilterValue() != null) {
            usernameFilter = filterMeta.getFilterValue().toString();
            System.out.println("username filter: " + usernameFilter);
        }
    }

    // Delegate the counting logic to the DAO
    return smsDAO.countFilteredSmsUsers(usernameFilter);
    }
}





