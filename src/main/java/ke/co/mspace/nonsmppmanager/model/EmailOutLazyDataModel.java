/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ke.co.mspace.nonsmppmanager.model;

import java.util.List;
import java.util.Map;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import ke.co.mspace.nonsmppmanager.service.EmailOutServiceApi;
import org.primefaces.model.FilterMeta;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortMeta;

/**
 *
 * @author amos
 */

@ManagedBean
@SessionScoped
public class EmailOutLazyDataModel extends LazyDataModel<EmailOut>{
    
    private EmailOutServiceApi emailDao;
    private String user;
    private String startDate;
    private String endDate;
    private int rows = 0;
    private boolean loaderActive = true;
    
    public EmailOutLazyDataModel(){
        
    }
    
    public EmailOutLazyDataModel(EmailOutServiceApi emailDao, String user, String startDate, String endDate){
        System.out.println("Inside emailOutLazyModel");
        this.emailDao = emailDao;
        this.user = user;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    @Override
    public List<EmailOut> load(int first, int pageSize, Map<String, SortMeta> sortBy, Map<String, FilterMeta> filterBy) {
         this.loaderActive = true;
        if(emailDao != null){
            System.out.println("loaderActive: " + loaderActive);
            System.out.println("load called");
            System.out.println("user: " + user + " " + startDate + " " + endDate + " limit " + pageSize + " offset " + first  );
            List<EmailOut> data = emailDao.fetchEmailReport(user, startDate, endDate, first, pageSize);
            loaderActive = false;
            return data;
        }
        return null;
    }
    
     public void setFilters(String user, String startDate, String endDate) {
        this.user = user;
        this.startDate = startDate;
        this.endDate = endDate;
    }
     

    @Override
    public int count(Map<String, FilterMeta> filterBy) {
        if(this.emailDao != null){
            
            System.out.println("Inside count of lazyloader");
//             rows = emailDao.fetchRows(user, startDate, endDate);
             return emailDao.fetchRows(user, startDate, endDate);
        }
        return 0;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public int getRows() {
        return rows;
    }

    public void setRows(int rows) {
        this.rows = rows;
    }

    public boolean isLoaderActive() {
        return loaderActive;
    }

    public void setLoaderActive(boolean loaderActive) {
        this.loaderActive = loaderActive;
    }
    
    
    
}




