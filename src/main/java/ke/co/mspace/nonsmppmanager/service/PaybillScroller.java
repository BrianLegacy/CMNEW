package ke.co.mspace.nonsmppmanager.service;

import java.io.PrintStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.model.SelectItem;
import ke.co.mspace.nonsmppmanager.model.Facet;
import ke.co.mspace.nonsmppmanager.model.Paybill;
import org.mspace.clientmanager.user.UserController;
import ke.co.mspace.nonsmppmanager.util.JdbcUtil;
import ke.co.mspace.nonsmppmanager.util.JsfUtil;
//import org.richfaces.component.UIScrollableDataTable;
//import org.richfaces.model.SortField;
//import org.richfaces.model.SortOrder;
//import org.richfaces.model.selection.SimpleSelection;
@ManagedBean(name = "ppaybilscroller")
public class PaybillScroller {

    private Paybill currentItem;// = new Paybill();
    private JdbcUtil util = new JdbcUtil();
    Connection conn = null;

    private static final Logger LOG = Logger.getLogger(PaybillScroller.class.getName());

    private int rows = 10;

    public int getRows() {
        return this.rows;
    }

    public void setRows(int rows) {
        this.rows = rows;
    }

    public void fetchCurrentRow(ActionEvent event) {
        String vin = (String) FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("username");
        this.currentRow = Integer.parseInt(
                (String) FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("row"));
        for (Paybill item : allPaybills) {
            if (item.getName().equals(vin)) {
                System.out.println("THE REQUESTED NAME: " + item.getName());
                this.currentItem = item;
                break;
            }
            System.out.println("THE REQUESTED NAME CHECK FAILED: " + item.getName());
        }
    }

    @PostConstruct
    public void init() {
        currentItem = new Paybill();
    }

    private Set<Integer> keys = new HashSet();

    private int currentRow;

//    private SimpleSelection selection = new SimpleSelection();
//
//    private UIScrollableDataTable table;
//
//    private SortOrder order = new SortOrder();

    private int scrollerPage = 1;

    private ArrayList<Paybill[]> model = null;

    private ArrayList<Paybill> selectedCars = new ArrayList();
    private final ArrayList<Facet> columns = new ArrayList();

    private static final int DECIMALS = 1;
    private static final int ROUNDING_MODE = 4;
    private List<Paybill> allPaybills = new ArrayList();
    private String filteredUser;

    public PaybillScroller() {
        initColumnsHeaders();
//        SortField[] fields = {new SortField("make", Boolean.valueOf(true))};
//        this.order.setFields(fields);
    }
public void psetCurrentItem(Paybill data){
        System.out.println("========="+data);
        
        currentItem=data;
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("delbill", currentItem.getPaybillz());
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("delid", currentItem.getUserid());
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("prevPayBill", currentItem.getPaybillz());

    }
public void psetCurrentItemForEdit(Paybill data){
  
        currentItem=data;
          FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("editmybill", currentItem.getUserid());
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("prevPayBill", currentItem.getPaybillz());

       
    }

    public List<Paybill> getAllPaybill() {
        try {

            allPaybills = UserServiceImpl.getPayBill();
        } catch (SQLException ex) {
            Logger.getLogger(PaybillScroller.class.getName()).log(Level.SEVERE, null, ex);
        }
        return this.allPaybills;
    }

    public List<SelectItem> getPagesToScroll() {
        List<SelectItem> list = new ArrayList();
        for (int i = 1; i <= this.allPaybills.size() / getRows() + 1; i++) {
            if (Math.abs(i - this.scrollerPage) < 5) {
                SelectItem item = new SelectItem(Integer.valueOf(i));
                list.add(item);
            }
        }
        return list;
    }

    public List<Paybill> getTenRandomUsers() {
        List<Paybill> result = new ArrayList();
        int size = getAllPaybill().size() - 1;
        for (int i = 0; i < 10; i++) {
            result.add(getAllPaybill().get(rand(1, size)));
        }
        return result;
    }

    public int genRand() {
        return rand(1, 10000);
    }

    public List<UserController> createUser(String make, String model, int count) {
        ArrayList<UserController> iiList = null;
        try {
            int arrayCount = count;

            UserController[] demoInventoryItemArrays = new UserController[arrayCount];

            for (int j = 0; j < demoInventoryItemArrays.length; j++) {
                UserController ii = new UserController();

                demoInventoryItemArrays[j] = ii;
            }

            iiList = new ArrayList(Arrays.asList(demoInventoryItemArrays));
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
        byte[] b = new byte[n];
        for (int i = 0; i < n; i++) {
            b[i] = ((byte) rand(65, 90));
        }
        return new String(b);
    }

//    public SimpleSelection getSelection() {
//        return this.selection;
//    }
//
//    public void setSelection(SimpleSelection selection) {
//        this.selection = selection;
//    }

//    public String takeSelection() {
//        getSelectedCars().clear();
//        if (getSelection().isSelectAll()) {
//            getSelectedCars().addAll(this.allPaybills);
//        } else {
//            Iterator<Object> iterator = getSelection().getKeys();
//            while (iterator.hasNext()) {
//                Object key = iterator.next();
//                this.table.setRowKey(key);
//                if (this.table.isRowAvailable()) {
//                    getSelectedCars().add(
//                            (Paybill) this.table.getRowData());
//                }
//            }
//        }
//        return null;
//    }

    public List<Paybill> getAllPaybills() {
        return this.allPaybills;
    }

    public void setAllPaybills(List<Paybill> allPaybills) {
        this.allPaybills = allPaybills;
    }

    public ArrayList<Paybill> getSelectedCars() {
        return this.selectedCars;
    }

    public void setSelectedCars(ArrayList<Paybill> selectedCars) {
        this.selectedCars = selectedCars;
    }

//    public UIScrollableDataTable getTable() {
//        return this.table;
//    }
//
//    public void setTable(UIScrollableDataTable table) {
//        this.table = table;
//    }

    public void initColumnsHeaders() {
        this.columns.clear();
    }

    public ArrayList<Paybill[]> getModel() {
        if (this.model == null) {
            this.model = new ArrayList();
            for (int i = 0; i < 9; i++) {
                Paybill[] items = new Paybill[6];

                this.model.add(items);
            }
        }
        return this.model;
    }

    public ArrayList<Facet> getColumns() {
        return this.columns;
    }

    public int getScrollerPage() {
        return this.scrollerPage;
    }

    public void setScrollerPage(int scrollerPage) {
        this.scrollerPage = scrollerPage;
    }

//    public SortOrder getOrder() {
//        return this.order;
//    }
//
//    public void setOrder(SortOrder order) {
//        this.order = order;
//    }

    public Paybill getCurrentItem() {
        return this.currentItem;
    }

    public void setCurrentItem(Paybill currentItem) {
        this.currentItem = currentItem;
    }

    public int getCurrentRow() {
        return this.currentRow;
    }

    public void setCurrentRow(int currentRow) {
        this.currentRow = currentRow;
    }

    public void store() {
    }

    public void saveOrPaybill() {
//        Paybill paybil= new Paybill();
        prevpaybill = (Integer) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("prevPayBill");

        int id = 0;
        Map klpd2 = FacesContext.getCurrentInstance().getExternalContext().getSessionMap();
        if (klpd2.containsKey("editmybill")) {
            id = (Integer) klpd2.get("editmybill");
        }
        System.out.println("save or "+currentItem);
        
        try {
            this.conn = this.util.getConnectionTodbPAYMENT();
            String sql = "UPDATE tUSERPAYBILL set tUSER_id=?,paybill=?,default_reply=?,email=? where tUSER_id=? and paybill=?";
            PreparedStatement ps = this.conn.prepareStatement(sql);
            ps.setInt(1, id);
            ps.setInt(2, currentItem.getPaybillz());
            ps.setString(3, currentItem.getDefault_message());
            ps.setString(4, currentItem.getEmail());
            ps.setInt(5, id);
            ps.setInt(6, prevpaybill);
            int count = ps.executeUpdate();
            ps.close();
            conn.close();
            Connection conn2 = this.util.getConnectionTodbSMS();
            String fetchSql = "Select username from tUSER where id=" + id;
            Statement st = conn2.createStatement();
            ResultSet rs = st.executeQuery(fetchSql);
            String username = "";
            while (rs.next()) {
                username = rs.getString("username");
            }
            //TODO: update this query
            String sql2 = "UPDATE tRULES SET dest=?, RuleName=? where RuleName=? and dest=?";
            PreparedStatement pps = conn2.prepareStatement(sql2);
            pps.setInt(1, currentItem.getPaybillz());
            pps.setString(2, username + "_".concat(String.valueOf(currentItem.getPaybillz())));
            pps.setString(3, username + "_" + prevpaybill);
            pps.setInt(4, prevpaybill);
            pps.executeUpdate();
            pps.close();
            conn2.close();
            if (id == 0) {
                JsfUtil.addSuccessMessage("User paybill saved successfully");
            } else {
                JsfUtil.addSuccessMessage("User paybill updated successfully");
                this.allPaybills = null;
                getAllPaybill();
            }

            JdbcUtil.closeConnection(this.conn);
        } catch (SQLException e) {
            JdbcUtil.printSQLException(e);
        }
        klpd2 = null;
    }

    public void delMe() {
        int id = 0;
        Map klpd = FacesContext.getCurrentInstance().getExternalContext().getSessionMap();
        if (klpd.containsKey("editmybill")) {
            id = (Integer) klpd.get("editmybill");
        }
        System.out.println("THE ID TO DkkkkkELETE: " + id);
//  System.out.println("THE ID TO DELETE: "+id);
        try {
            // conn = util.getConnectionTodbPAYMENT();
            Connection conn2 = this.util.getConnectionTodbPAYMENT();
            String sql = "DELETE from tUSERPAYBILL where paybill =?";

            PreparedStatement psmt = conn2.prepareStatement(sql);
            psmt.setInt(1, id);
            int n = psmt.executeUpdate();

            JsfUtil.addSuccessMessage(" Paybill deleted successfully.");
            //System.out.println("Paybill " + paybillz + " was deleted successfully.");

            JdbcUtil.closeConnection(this.conn);
        } catch (SQLException ex) {
            ex.printStackTrace();
            JdbcUtil.printSQLException(ex);
        }
    }

    public void deletePaybill() {
       
        prevpaybill = (Integer) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("prevPayBill");
 
        int id = 0;
        Map klpd = FacesContext.getCurrentInstance().getExternalContext().getSessionMap();
        if (klpd.containsKey("delbill")) {
            id = (Integer) klpd.get("delbill");
        }
        System.out.println("deleting paybill "+id);
        try {
            // conn = util.getConnectionTodbPAYMENT();
            Connection conn2 = this.util.getConnectionTodbPAYMENT();
            String sql = "DELETE from tUSERPAYBILL where paybill =?";

            PreparedStatement psmt = conn2.prepareStatement(sql);
            psmt.setInt(1, id);
            int n = psmt.executeUpdate();
            psmt.close();
            conn2.close();

            String sql2 = "DELETE from tRULES where RuleName=? and dest=?";
            Connection conn = this.util.getConnectionTodbSMS();
            int uid = (Integer) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("delid");

            String fetchSql = "Select username from tUSER where id=" + uid;
            Statement st = conn.createStatement();

            ResultSet rs = st.executeQuery(fetchSql);
            String username = "";
            while (rs.next()) {
                username = rs.getString("username");
            }
            st.close();
            rs.close();
            PreparedStatement psmt1 = conn.prepareStatement(sql2);
            psmt1.setString(1, username + "_" + prevpaybill);
            psmt1.setInt(2, id);
            psmt1.executeUpdate();
            System.out.println("Deleting trule with username: " + username + "_" + prevpaybill + " and paybill: " + id);

            psmt1.close();
            conn.close();

            JsfUtil.addSuccessMessage("Paybill deleted successfully.");
            //System.out.println("Paybill " + paybillz + " was deleted successfully.");

            JdbcUtil.closeConnection(this.conn);
        } catch (SQLException ex) {
            ex.printStackTrace();
            JdbcUtil.printSQLException(ex);
        }
        klpd = null;
    }

    public void delete() {
        this.allPaybills.remove(this.currentRow);
    }

    public Set<Integer> getKeys() {
        return this.keys;
    }

    public void setKeys(Set<Integer> keys) {
        this.keys = keys;
    }

    public void addPaybillToList(Paybill paybill) {
        this.allPaybills.add(paybill);
    }
    private int prevpaybill;

    public int getPrevpaybill() {
        return prevpaybill;
    }

    public void setPrevpaybill(int prevpaybill) {
        this.prevpaybill = prevpaybill;
    }

    public String getFilteredUser() {
        return filteredUser;
    }

    public void setFilteredUser(String filteredUser) {
        this.filteredUser = filteredUser;
    }

    public void storeCurentid() {
        System.out.println("kurrent item "+currentItem);
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("delbill", currentItem.getPaybillz());
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("delid", currentItem.getUserid());
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("prevPayBill", currentItem.getPaybillz());

    }

    public void storeTuserid(int userid) {
        
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("editmybill", currentItem.getUserid());
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("prevPayBill", currentItem.getPaybillz());

    }
}


/* Location:              /home/admin/Downloads/navicat100_lite_en/clientmanager.war!/WEB-INF/classes/ke/co/mspace/nonsmppmanager/service/PaybillScroller.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */
