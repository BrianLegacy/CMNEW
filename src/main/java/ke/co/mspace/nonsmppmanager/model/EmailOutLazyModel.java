///*
// * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
// * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
// */
//package ke.co.mspace.nonsmppmanager.model;
//
//import java.util.List;
//import java.util.Map;
//import org.mspace.clientmanager.email.EmailDAO;
//import org.mspace.clientmanager.email.EmailDAOImpl;
//import org.primefaces.model.FilterMeta;
//import org.primefaces.model.LazyDataModel;
//import org.primefaces.model.SortMeta;
//
///**
// *
// * @author amos
// */
//public class EmailOutLazyModel extends LazyDataModel<EmailOut>{
//
//    private final EmailDAO emailDAO;
//    
//    public EmailOutLazyModel(){
//        emailDAO = new EmailDAOImpl();
//    }
//    
//    @Override
//    public int count(Map<String, FilterMeta> filterBy) {
//        return null;
//    }
//
//    @Override
//    public List<EmailOut> load(int first, int pageSize, Map<String, SortMeta> sortBy, Map<String, FilterMeta> filterBy) {
//        
//        String usernameFilter = null;
//        
//        if(!filterBy.isEmpty() && filterBy.containsKey("username")){
//                FilterMeta filterMeta = filterBy.get("username");
//                if(filterMeta != null && filterMeta.getFilterValue() != null){
//                    usernameFilter = filterMeta.getFilterValue().toString();
//                    
//                }
//    }
//        
//        int totalRecords = emailDAO.countSmsUsers(); 
//        System.out.println("Total records for pagination are: " + totalRecords);
//        this.setRowCount(totalRecords); 
//
//        // Fetch the data for the current page
//        return emailDAO.fetchEmailUsers(pageSize, first, usernameFilter);
//    }
//    
//}
