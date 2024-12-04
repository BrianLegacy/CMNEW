/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ke.co.mspace.nonsmppmanager.service;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.model.SelectItem;
import ke.co.mspace.nonsmppmanager.model.Alpha;
import ke.co.mspace.nonsmppmanager.model.Facet;
import org.mspace.clientmanager.user.UserController;
import ke.co.mspace.nonsmppmanager.util.JdbcUtil;
import ke.co.mspace.nonsmppmanager.util.JsfUtil;

//import org.richfaces.component.UIScrollableDataTable;
//import org.richfaces.model.SortField;
//import org.richfaces.model.SortOrder;
//import org.richfaces.model.selection.SimpleSelection;

/**
 *
 * @author Norrey Osako
 */
@ManagedBean
@ViewScoped
public class AlphaScroller {

    private Alpha currentItem = new Alpha();
    private JdbcUtil util = new JdbcUtil();
    Connection conn = null;
    String user;
    private static final Logger LOG = Logger.getLogger(AlphaScroller.class.getName());

    private int rows = 10;

    public int getRows() {
        return rows;
    }

    public void setRows(int rows) {
        this.rows = rows;
    }

    public void fetchCurrentRow(ActionEvent event) {
        String vin = (FacesContext.getCurrentInstance().
                getExternalContext().getRequestParameterMap().get("username"));
        currentRow = Integer.parseInt(FacesContext.getCurrentInstance().
                getExternalContext().getRequestParameterMap().get("row"));
        for (Alpha item : allUsers) {
            if (item.getUsername().equals(vin)) {
                System.out.println("W GETTING THIS FAR");
                currentItem = item;
                break;
            }
        }
    }

    private Set<Integer> keys = new HashSet<>();

    private int currentRow;

//    private SimpleSelection selection = new SimpleSelection();
//
//    private UIScrollableDataTable table;
//
//    private SortOrder order = new SortOrder();

    private int scrollerPage = 1;

    private ArrayList<Alpha[]> model = null;

    private ArrayList<Alpha> selectedCars = new ArrayList<>();
    private final ArrayList<Facet> columns = new ArrayList<>();
    private static final int DECIMALS = 1;
    private static final int ROUNDING_MODE = BigDecimal.ROUND_HALF_UP;

    private List<Alpha> allUsers = null;

    public AlphaScroller() {
        initColumnsHeaders();
//        SortField[] fields = {new SortField("make", true)};
//        order.setFields(fields);
    }
    @PostConstruct
    public void init(){
        fetchAlphas();
    }
    public void fetchAlphas(){
        System.out.println("fetching alphas");
           synchronized (this) {
            if (allUsers == null || allUsers.isEmpty()) {
                try {
                    conn = util.getConnectionTodbSMS();
                  
                    allUsers = new ArrayList<>();
                    AlphaServiceApi service = new AlphaServiceImpl();
                    allUsers = service.getAllAlphanumerics(conn);

                    JdbcUtil.closeConnection(conn);
                } catch (SQLException e) {
                    JdbcUtil.printSQLException(e);
                }
            }
        }
    }

    public List<Alpha> getAllAlphas() {
     

        return allUsers;
    }
 public void psetCurrentAlpha(Alpha alpha){
     System.out.println("settingsx");
        currentItem=alpha;
        
    }
    //==========================================================================
    public List<Alpha> getAgentAlphas() {

        synchronized (this) {
            if (allUsers == null || allUsers.isEmpty()) {
              
                    //String username=currentItem.getUsername();
                    conn = util.getConnectionTodbSMS();
                    LOG.info("getAllAlphas");
                    allUsers = new ArrayList<>();
                    AlphaServiceApi service = new AlphaServiceImpl();
                    allUsers = service.getAgentAlphas(conn, user);

                    JdbcUtil.closeConnection(conn);
                
            }
        }

        return allUsers;
    }

    public List<SelectItem> getPagesToScroll() {

        List<SelectItem> list = new ArrayList<SelectItem>();
        for (int i = 1; i <= allUsers.size() / getRows() + 1; i++) {
            if (Math.abs(i - scrollerPage) < 5) {
                SelectItem item = new SelectItem(i);
                list.add(item);
            }
        }
        return list;
    }

    public List<Alpha> getTenRandomUsers() {

        List<Alpha> result = new ArrayList<>();
        int size = getAllAlphas().size() - 1;
        for (int i = 0; i < 10; i++) {
            result.add(getAllAlphas().get(rand(1, size)));
        }
        return result;
    }

    public int genRand() {

        return rand(1, 10000);
    }

    public List<UserController> createUser(String make, String model,
            int count) {

        ArrayList<UserController> iiList = null;

        try {
            int arrayCount = count;

            UserController[] demoInventoryItemArrays = new UserController[arrayCount];

            for (int j = 0; j < demoInventoryItemArrays.length; j++) {
                UserController ii = new UserController();

                demoInventoryItemArrays[j] = ii;

            }

            iiList = new ArrayList<UserController>(Arrays
                    .asList(demoInventoryItemArrays));

        } catch (Exception e) {
            System.out.println("!!!!!!createCategory Error: " + e.getMessage());
            e.printStackTrace();
        }
        return iiList;
    }

    public static int rand(int lo, int hi) {

        Random rn2 = new Random();
        int n = hi - lo + 1;
        int i = rn2.nextInt() % n;
        if (i < 0) {
            i = -i;
        }
        return lo + i;
    }

    public static String randomstring(int lo, int hi) {

        int n = rand(lo, hi);
        byte b[] = new byte[n];
        for (int i = 0; i < n; i++) {
            b[i] = (byte) rand('A', 'Z');
        }
        return new String(b);
    }

//    public SimpleSelection getSelection() {
//
//        return selection;
//    }

//    public void setSelection(SimpleSelection selection) {
//        this.selection = selection;
//    }

//    public String takeSelection() {
//        getSelectedCars().clear();
//        if (getSelection().isSelectAll()) {
//            getSelectedCars().addAll(allUsers);
//        } else {
//            Iterator<Object> iterator = getSelection().getKeys();
//            while (iterator.hasNext()) {
//                Object key = iterator.next();
//                table.setRowKey(key);
//                if (table.isRowAvailable()) {
//                    getSelectedCars().add(
//                            (Alpha) table.getRowData());
//                }
//            }
//        }
//        return null;
//    }

    public ArrayList<Alpha> getSelectedCars() {

        return selectedCars;
    }

    public void setSelectedCars(ArrayList<Alpha> selectedCars) {
        this.selectedCars = selectedCars;
    }

//    public UIScrollableDataTable getTable() {
//
//        return table;
//    }
//
//    public void setTable(UIScrollableDataTable table) {
//        this.table = table;
//    }

    public void initColumnsHeaders() {
        columns.clear();

    }

    public ArrayList<Alpha[]> getModel() {

        if (model == null) {
            model = new ArrayList<Alpha[]>();
            for (int i = 0; i < 9; i++) {
                Alpha[] items = new Alpha[6];

                model.add(items);
            }
        }
        return model;
    }

    public ArrayList<Facet> getColumns() {

        return columns;
    }

    public int getScrollerPage() {

        return scrollerPage;
    }

    public void setScrollerPage(int scrollerPage) {
        this.scrollerPage = scrollerPage;
    }

//    public SortOrder getOrder() {
//
//        return order;
//    }
//
//    public void setOrder(SortOrder order) {
//        this.order = order;
//    }

    public Alpha getCurrentItem() {

        return currentItem;
    }
 public void psetCurrentAlphanumericObject(Alpha alpha){
              setCurrentItem(alpha);
//         setpCurrentAlpha(alpha);
        System.out.println("about to setthe "+alpha);
    }
    public void setCurrentItem(Alpha currentItem) {
        this.currentItem = currentItem;
        System.out.println("volunteer "+currentItem);
    }
     public void psetCurrentItem(Alpha currentItem) {
        this.currentItem = currentItem;
        System.out.println("volunteer "+currentItem);
    }

    public int getCurrentRow() {

        return currentRow;
    }

    public void setCurrentRow(int currentRow) {
        this.currentRow = currentRow;
    }

    public void store() {

    }

    public void saveOrUpdateAlpha() {
        System.out.println("INside alpha "+currentItem);
        try {

            conn = util.getConnectionTodbSMS();
//            LOG.info("saveOrUpdateAlpha");
            AlphaServiceImpl service = new AlphaServiceImpl();
            String username = currentItem.getUsername();
            String alphanumeric = currentItem.getName();
            Long id = currentItem.getId();

            System.out.println("Username : " + username);
            System.out.println("Alpha : " + alphanumeric);
            System.out.println("Id : " + id);
            AlphaServiceImpl aService = new AlphaServiceImpl();
            String alphaType = aService.getAlphanumericType(alphanumeric, conn);
            if (id == 0) {

                service.persistAlpha(username, alphanumeric, alphaType, conn);
                JsfUtil.addSuccessMessage("User alphanumeric saved successfully");
                //currentItem.setMessage("UserController alpha saved successfully.");
            } else {

                service.updateAlpha(username, alphanumeric, alphaType, conn);
                JsfUtil.addSuccessMessage("User alphanumeric updated successfully");
                allUsers = null;
                getAllAlphas();
                //currentItem.setMessage("UserController alpha updated successfully.");

            }
            JdbcUtil.closeConnection(conn);
        } catch (SQLException e) {
            JdbcUtil.printSQLException(e);
        }

    }

    public void deleteAlphanumeric() {
        try {

            conn = util.getConnectionTodbSMS();

            LOG.info("deleteAlphanumeric"+currentItem);
            AlphaServiceImpl service = new AlphaServiceImpl();
            service.deleteAlphanumeric(currentItem, conn);

            JsfUtil.addSuccessMessage(" Alphanumeric deleted successfully.");
            allUsers.remove(currentItem);
            JdbcUtil.closeConnection(conn);
        } catch (SQLException ex) {
            JdbcUtil.printSQLException(ex);

        }

    }

    public void deleteAgentAlphanumeric() {
       

            conn = util.getConnectionTodbSMS();

            LOG.info("deleteAlphanumeric");
            AlphaServiceImpl service = new AlphaServiceImpl();
            System.out.println("======================="+currentItem);
            service.removeAgentAlpha(currentItem, conn);

            JsfUtil.addSuccessMessage(" Alphanumeric removed successfully.");
            //allUsers.remove(currentItem);
            JdbcUtil.closeConnection(conn);
//     fetchAlphas();
    

    }

    public void delete() {
        allUsers.remove(currentRow);
    }

    public Set<Integer> getKeys() {
        return keys;
    }

    public void setKeys(Set<Integer> keys) {
        this.keys = keys;
    }

    public void addAlphaToList(Alpha alpha) {
        allUsers.add(alpha);
    }
    AlphaServiceImpl al = new AlphaServiceImpl();

    public String currentUSer() {
        return al.getUser();
    }

    public String getSelectedUSer() {
        UserScroller us = new UserScroller();
        return String.valueOf(currentItem.getId());

    }
}
