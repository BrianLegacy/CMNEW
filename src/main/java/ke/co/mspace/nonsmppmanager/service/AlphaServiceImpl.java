package ke.co.mspace.nonsmppmanager.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import ke.co.mspace.nonsmppmanager.model.Alpha;
import ke.co.mspace.nonsmppmanager.model.Alpnumeric;
import static ke.co.mspace.nonsmppmanager.model.AuthenticationBean.AUTH_KEY;
import org.mspace.clientmanager.group.Group;
import ke.co.mspace.nonsmppmanager.util.JdbcUtil;
import ke.co.mspace.nonsmppmanager.util.SessionUtil;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.PrintSetup;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;

/**
 *
 * @author Norrey Osako
 */
public class AlphaServiceImpl implements AlphaServiceApi {

    private static final Logger LOG = Logger.getLogger(AlphaServiceImpl.class.getName());
    private final String user_id = FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("id").toString();

//    private final String user_id = FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("id").toString();
//    private final String user = FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get(AUTH_KEY).toString();
    
private final String user =SessionUtil.getAUTH_KEY();
    public AlphaServiceImpl() {

    }

    public String getUser_id() {
        return user_id;
    }

    public String getUser() {
        return user;
    }

    @Override
    public Map<String,String> getAlphanumericsNames(Connection conn) {

        //String sql = "select distinct short_code from tSDP where short_code_type = 2";
        String sql = "select distinct short_code,sid_type from tSDPNew where short_code_type = 2 order by short_code asc";
        String sql2 = "select distinct short_code from tSDP where short_code_type = 2";

//        String sqlReseller = "SELECT DISTINCT t.short_code  FROM tSDP t inner join tUSER u on "
//                + "t.agent_id = u.id where t.short_code_type = 2 and t.agent_id = '"+user_id+"'";
        String sqlReseller = "SELECT DISTINCT t.short_code,t.sid_type  FROM tSDPNew t inner join tUSER u on "
                + "t.agent_id = u.id where t.short_code_type = 2 and t.agent_id = '" + user_id + "'";

        String sqlReseller2 = "SELECT DISTINCT t.short_code  FROM tSDP t inner join tUSER u on "
                + "t.agent_id = u.id where t.short_code_type = 2 and t.agent_id = '" + user_id + "' order by t.short_code asc";

        ResultSet rs = null;
        PreparedStatement stmt = null;
        Map<String,String> alphanumerics = new HashMap<>();
        List<String> airtelalphas = new ArrayList<>();

        try {
            stmt = conn.prepareStatement(SessionUtil.getReseller().equalsIgnoreCase("none") ? sqlReseller : sql);
            rs = stmt.executeQuery();
            while (rs.next()) {
                String a = rs.getString("short_code");
                
                alphanumerics.put(a,a);
            }
        } catch (SQLException e) {
            Logger.getLogger(AlphaServiceImpl.class.getName()).log(Level.SEVERE, null, e.getMessage());
        } finally {

            try {
                if (rs != null) {

                    rs.close();
                }

                if (stmt != null) {
                    stmt.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(AlphaServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
            }

        }
        return  alphanumerics;
    }
    
    
       public List<SelectItem> getGroups(Connection conn) {

        //String sql = "select distinct short_code from tSDP where short_code_type = 2";
        String sql = "select * from dbSMS.tGROUPS ";
      

        ResultSet rs = null;
        PreparedStatement stmt = null;
        List<SelectItem> groups = new ArrayList<>();

        try {
            stmt = conn.prepareStatement(sql);
            rs = stmt.executeQuery();
            while (rs.next()) {
                Group group=new Group();
                group.setId(rs.getInt("id"));
                group.setGroupname(rs.getString("groupname"));
                group.setDescription(rs.getString("description"));
                groups.add(new SelectItem(group.getGroupname()));
                
            }
        } catch (SQLException e) {
            Logger.getLogger(AlphaServiceImpl.class.getName()).log(Level.SEVERE, null, e.getMessage());
        } finally {

            try {
                if (rs != null) {

                    rs.close();
                }

                if (stmt != null) {
                    stmt.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(AlphaServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
            }

        }
        return groups;
    }
       
              public Group getGroup(String groupname,Connection conn) {

        //String sql = "select distinct short_code from tSDP where short_code_type = 2";
        String sql = "select * from dbSMS.tGROUPS where groupname=? ";
      

        ResultSet rs = null;
        PreparedStatement stmt = null;
        Group group=new Group();
        try {
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, groupname);
            rs = stmt.executeQuery();
            while (rs.next()) {
                
                group.setId(rs.getInt("id"));
                group.setGroupname(rs.getString("groupname"));
                group.setDescription(rs.getString("description"));
                
            }
        } catch (SQLException e) {
            Logger.getLogger(AlphaServiceImpl.class.getName()).log(Level.SEVERE, null, e.getMessage());
        } finally {

            try {
                if (rs != null) {

                    rs.close();
                }

                if (stmt != null) {
                    stmt.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(AlphaServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
            }

        }
        return group;
    }


    @Override
    public List<String> getAirTelAlphas(Connection conn) {

        // String sql2 = "select distinct airtel from tSDP where short_code_type = 2";
        String sql2 = "select distinct airtel from tSDPNew where short_code_type = 2";

//         String sqlReseller2 = "SELECT DISTINCT airtel  FROM tSDP t inner join tUSER u on "
//                + "t.agent_id = u.id where t.short_code_type = 2 and t.agent_id = '"+user_id+"'";
        String sqlReseller2 = "SELECT DISTINCT airtel  FROM tSDPNew t inner join tUSER u on "
                + "t.agent_id = u.id where t.short_code_type = 2 and t.agent_id = '" + user_id + "'";

        ResultSet rs = null;
        PreparedStatement stmt = null;

        List<String> airtelalphas = new ArrayList<>();

        try {
            stmt = conn.prepareStatement(UserServiceImpl.isReseller().equalsIgnoreCase("none") ? sqlReseller2 : sql2);
            rs = stmt.executeQuery();
            while (rs.next()) {
                String a = rs.getString("airtel");
                airtelalphas.add(a);
            }
        } catch (SQLException e) {
            Logger.getLogger(AlphaServiceImpl.class.getName()).log(Level.SEVERE, null, e.getMessage());
        } finally {

            try {
                if (rs != null) {

                    rs.close();
                }

                if (stmt != null) {
                    stmt.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(AlphaServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
            }

        }
        return airtelalphas;
    }

    @Override
    public List<String> getUnusagnedphanumericsNames(Connection conn) {

        //String sql = "select distinct short_code from tSDP where short_code_type = 2";
        String sql = "select distinct short_code from tSDPNew where short_code_type = 2";

//        String sqlReseller = "SELECT DISTINCT t.short_code  FROM tSDP t inner join tUSER u on "
//                + "t.agent_id = u.id where t.short_code_type = 2 and t.agent_id = '"+user_id+"'";
        String sqlReseller = "SELECT DISTINCT t.short_code  FROM tSDPNew t inner join tUSER u on "
                + "t.agent_id = u.id where t.short_code_type = 2 and t.agent_id = '" + user_id + "'";

        ResultSet rs = null;
        PreparedStatement stmt = null;
        List<String> alphanumerics = new ArrayList<>();

        try {
            stmt = conn.prepareStatement(UserServiceImpl.isReseller().equalsIgnoreCase("none") ? sqlReseller : sql);
            rs = stmt.executeQuery();
            while (rs.next()) {
                String a = rs.getString("short_code");
                alphanumerics.add(a);
            }
        } catch (SQLException e) {
            Logger.getLogger(AlphaServiceImpl.class.getName()).log(Level.SEVERE, null, e.getMessage());
        } finally {

            try {
                if (rs != null) {

                    rs.close();
                }

                if (stmt != null) {
                    stmt.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(AlphaServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
            }

        }
        return alphanumerics;
    }

    @Override
    public List<Alpha> getAllAlphanumerics(Connection conn) {
        
        String sql = "SELECT * FROM tAllowedAlphanumerics";
        String sqlReseller = "SELECT t.* FROM tAllowedAlphanumerics t inner join tUSER u on t.username = u.username where u.agent = '" + user_id + "'";
        List<Alpha> result = new ArrayList<>();
        try {

            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(SessionUtil.isReseller() ? sqlReseller : sql);
            System.out.println("ksql "+(SessionUtil.isReseller() ? sqlReseller : sql));
            while (rs.next()) {
                Alpha alpha = new Alpha();
                alpha.setId(rs.getLong("id"));
                alpha.setUsername(rs.getString("username"));
                alpha.setName(rs.getString("alphanumeric"));

                result.add(alpha);
            }
            JdbcUtil.closeConnection(conn);
        } catch (SQLException ex) {
            JdbcUtil.printSQLException(ex);
        }

        return result;

    }
    //============================================================================================

    @Override
    public List<Alpha> getAgentAlphas(Connection conn, String user) {

        UserScroller us = new UserScroller();
        String selected_id = us.userSelectedUSerID();
        System.out.println();
        String sqlReseller = "SELECT t.* FROM tAllowedAlphanumerics t inner join tUSER u on "
                + "t.username = u.username where u.agent = '926'";
        System.out.println("*******************" + sqlReseller);
        List<Alpha> result = new ArrayList<>();
        try {

            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sqlReseller);

            while (rs.next()) {
                Alpha alpha = new Alpha();
                alpha.setId(rs.getLong("id"));
                alpha.setUsername(rs.getString("username"));
                alpha.setName(rs.getString("alphanumeric"));

                result.add(alpha);
            }
            JdbcUtil.closeConnection(conn);
        } catch (SQLException ex) {
            JdbcUtil.printSQLException(ex);
        }

        return result;

    }

    @Override
    public int persistAlpha(String username, String alpha, String alpaType, Connection conn) throws SQLException {

        String sql = "INSERT INTO tAllowedAlphanumerics(username, alphanumeric,sid_type) VALUES (?, ?,?)";

//         String sql = "update tAllowedAlphanumerics set "
//                + "alphanumeric= ?,sid_type=? WHERE username in "
//                + "(select username from tUSER where username = ? or super_account_id in"
//                + "(select id from tUSER where username = ?))";
        PreparedStatement pstmt = conn.prepareStatement(sql);
        // Bind values to the parameters
        pstmt.setString(1, username);
        pstmt.setString(2, alpha);
        pstmt.setString(3, alpaType);
        int count = pstmt.executeUpdate();
        return count;
    }

    @Override
    public int updateAlpha(String username, String alphanumeric, String sid_type, Connection conn) throws SQLException {

//sql = "UPDATE tAllowedAlphanumerics SET  alphanumeric=? WHERE username=?";
        String sql = "update tAllowedAlphanumerics set"
                + " alphanumeric= ? , sid_type=? WHERE username in "
                + "(select username from tUSER where username = ? or super_account_id in"
                + "(select id from tUSER where username = ?))";

        ////////////////////////////////////////////////////////
        //Inserting values
        ////////////////////////////////////////////////////////
        PreparedStatement pstmt = conn.prepareStatement(sql);
        // Bind values to the parameters
        pstmt.setString(1, alphanumeric);
        pstmt.setString(2, sid_type);
        pstmt.setString(3, username);
        pstmt.setString(4, username);
        // Execute the query
        int count = pstmt.executeUpdate();

        System.out.println("Count status" + count);
        return count;
    }

    //++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
    @Override
    public void updateAgentAlphas(String username, String alphanumeric, Connection conn) throws SQLException {
        System.out.println("Enter Method");
//sql = "UPDATE tAllowedAlphanumerics SET  alphanumeric=? WHERE username=?";
        //String sql = "Update tSDP set agent_id =? where short_code=?";
        String sql = "Update tSDPNew set agent_id =? where short_code=?";
        System.out.println("The querry" + sql);
        ////////////////////////////////////////////////////////
        //Inserting values
        ////////////////////////////////////////////////////////
        PreparedStatement pstmt = conn.prepareStatement(sql);
        // Bind values to the parameters
        pstmt.setString(1, username);
        pstmt.setString(2, alphanumeric);
        System.out.println(pstmt);

        // Execute the query
        int count = pstmt.executeUpdate();
        System.out.println("Count status" + count);

    }

    //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
    @Override
    public Alpha loadAlphanumericByUsername(String selectedUsername, Connection conn) throws SQLException {

        String sql = "SELECT * FROM tAllowedAlphanumerics WHERE username='" + selectedUsername + "'";
        Alpha alpha = new Alpha();

        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(sql);

        while (rs.next()) {

            alpha.setId(rs.getLong("id"));
            alpha.setUsername(rs.getString("username"));

            alpha.setName(rs.getString("alphanumeric"));

        }

        return alpha;
    }

    @Override
    public void updateAlphaByUsername(String previousUsername, String username, Connection conn) throws SQLException {

        String sql = "UPDATE tAllowedAlphanumerics SET username=? WHERE username=?";

        ////////////////////////////////////////////////////////
        //Inserting values
        ////////////////////////////////////////////////////////
        PreparedStatement pstmt = conn.prepareStatement(sql);
        // Bind values to the parameters
        pstmt.setString(1, previousUsername);
        pstmt.setString(2, username);

        // Execute the query
        int count = pstmt.executeUpdate();

    }

    public void deleteAlphanumeric(Alpha aThis, Connection conn) throws SQLException {

        String sql = "DELETE FROM tAllowedAlphanumerics WHERE username=?";

        ////////////////////////////////////////////////////////
        //Deleting values
        ////////////////////////////////////////////////////////
        PreparedStatement pstmt = conn.prepareStatement(sql);
        // Bind values to the parameters
        pstmt.setString(1, aThis.getUsername());

        // Execute the query
        int count = pstmt.executeUpdate();

    }

    @Override
    public void removeAgentAlpha(Alpha selected, Connection conn) {

        try {
            String sql = "Update tSDPNew set agent_id ='' where short_code=?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, selected.getName());
            int count = pstmt.executeUpdate();
            System.out.println("Count status" + count);
        } catch (SQLException ex) {
            Logger.getLogger(AlphaServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    @Override
    public void generateXSL() {

        try {

            HSSFWorkbook wb = new HSSFWorkbook();
            Map<String, CellStyle> styles = createStyles(wb);
            HSSFSheet sheet = wb.createSheet("User_Alphanumerics_sheet_1");

            PrintSetup printSetup = sheet.getPrintSetup();
            printSetup.setLandscape(true);
            sheet.setFitToPage(true);
            sheet.setHorizontallyCenter(true);

            //title row
            Row titleRow = sheet.createRow(0);
            titleRow.setHeightInPoints(45);
            Cell titleCell = titleRow.createCell(0);
            titleCell.setCellValue("USER ALPHANUMERICS LIST");
            titleCell.setCellStyle(styles.get("title"));
            sheet.addMergedRegion(CellRangeAddress.valueOf("$A$1:$B$1"));

            String[] titles = {"USERNAME", "ALPHANUMERIC"};
            HSSFRow row = sheet.createRow(1);
            row.setHeightInPoints(40);

            Cell headerCell;
            for (int i = 0; i < titles.length; i++) {
                headerCell = row.createCell(i);
                headerCell.setCellValue(titles[i]);
                headerCell.setCellStyle(styles.get("header"));
            }
            List<Alpha> alphaList = null;
            
                JdbcUtil util = new JdbcUtil();
                Connection conn = util.getConnectionTodbSMS();

                alphaList = getAllAlphanumerics(conn);
                JdbcUtil.closeConnection(conn);
            
            int rowNum = 2;

            for (Alpha alpha : alphaList) {
                row = sheet.createRow(rowNum);
                row.createCell(0).setCellValue(alpha.getUsername());
                row.createCell(1).setCellValue(alpha.getName());

                rowNum++;
            }

            sheet.setColumnWidth(0, 20 * 256); //30 characters wide
            sheet.setColumnWidth(1, 20 * 256);

            FacesContext context = FacesContext.getCurrentInstance();
            HttpServletResponse res = (HttpServletResponse) context.getExternalContext().getResponse();
            res.setContentType("application/vnd.ms-excel");
            res.setHeader("Content-disposition", "attachment;filename=mydata.xlsx");

            ServletOutputStream out = res.getOutputStream();
            wb.write(out);
            out.flush();
            out.close();
            FacesContext.getCurrentInstance().responseComplete();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static Map<String, CellStyle> createStyles(Workbook wb) {
        Map<String, CellStyle> styles = new HashMap<>();
        CellStyle style;
        Font titleFont = wb.createFont();
        titleFont.setFontHeightInPoints((short) 18);
        titleFont.setBoldweight(Font.BOLDWEIGHT_BOLD);
        style = wb.createCellStyle();
        style.setAlignment(CellStyle.ALIGN_CENTER);
        style.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
        style.setFont(titleFont);
        styles.put("title", style);

        Font monthFont = wb.createFont();
        monthFont.setFontHeightInPoints((short) 11);
        monthFont.setColor(IndexedColors.WHITE.getIndex());
        style = wb.createCellStyle();
        style.setAlignment(CellStyle.ALIGN_CENTER);
        style.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
        style.setFillForegroundColor(IndexedColors.GREY_50_PERCENT.getIndex());
        style.setFillPattern(CellStyle.SOLID_FOREGROUND);
        style.setFont(monthFont);
        style.setWrapText(true);
        styles.put("header", style);

        style = wb.createCellStyle();
        style.setAlignment(CellStyle.ALIGN_CENTER);
        style.setWrapText(true);
        style.setBorderRight(CellStyle.BORDER_THIN);
        style.setRightBorderColor(IndexedColors.BLACK.getIndex());
        style.setBorderLeft(CellStyle.BORDER_THIN);
        style.setLeftBorderColor(IndexedColors.BLACK.getIndex());
        style.setBorderTop(CellStyle.BORDER_THIN);
        style.setTopBorderColor(IndexedColors.BLACK.getIndex());
        style.setBorderBottom(CellStyle.BORDER_THIN);
        style.setBottomBorderColor(IndexedColors.BLACK.getIndex());
        styles.put("cell", style);

        style = wb.createCellStyle();
        style.setAlignment(CellStyle.ALIGN_CENTER);
        style.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
        style.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
        style.setFillPattern(CellStyle.SOLID_FOREGROUND);
        style.setDataFormat(wb.createDataFormat().getFormat("0.00"));
        styles.put("formula", style);

        style = wb.createCellStyle();
        style.setAlignment(CellStyle.ALIGN_CENTER);
        style.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
        style.setFillForegroundColor(IndexedColors.GREY_40_PERCENT.getIndex());
        style.setFillPattern(CellStyle.SOLID_FOREGROUND);
        style.setDataFormat(wb.createDataFormat().getFormat("0.00"));
        styles.put("formula_2", style);

        return styles;
    }

//    @Override
//    public String  getAgentAlphanumerics(Connection conn,String user) {
//      // String sql = "select distinct short_code from tSDP where short_code_type = 2";
//        UserScroller us=new UserScroller();
//        String sql = "SELECT DISTINCT t.short_code  FROM tSDP t inner join tUSER u on "
//                + "t.agent_id = u.id where t.short_code_type = 2 and t.agent_id = '"+user+'"';
//         System.out.println("\n"+sql);
//        ResultSet rs = null;
//        PreparedStatement stmt = null;
//        List<String> alphanumerics = new ArrayList<>();
//        
//        try {
//            stmt = conn.prepareStatement( sql);
//            rs = stmt.executeQuery();
//            while (rs.next()) {
//                String a = rs.getString("short_code");
//                alphanumerics.add(a);
//            }
//        } catch (SQLException e) {
//            Logger.getLogger(AlphaServiceImpl.class.getName()).log(Level.SEVERE, null, e.getMessage());
//        }
//        return alphanumerics.toString();
//   }
    @Override
    public String getAlphanumericType(String alpha, Connection conn) {
        String alphaType = "";
        String sql = "SELECT sid_type from tSDPNew where short_code='" + alpha + "'";
      //  System.out.println(sql);
        ResultSet rs = null;
        PreparedStatement stmt = null;
        try {

            stmt = conn.prepareStatement(sql);

            rs = stmt.executeQuery();
            while (rs.next()) {
                alphaType = rs.getString("sid_type");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            Logger.getLogger(AlphaServiceImpl.class.getName()).log(Level.SEVERE, null, e.getMessage());
        }
        System.err.println("sid type " + alphaType);
        return alphaType;
    }

    @Override
    public List<Alpnumeric> getAlphanumerics(Connection conn) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean findAlphanumericByUsername(String selectedUsername, String alphanumeric, Connection conn) throws SQLException {
        boolean exist=false;
        String sql = "SELECT username, alphanumeric FROM tAllowedAlphanumerics WHERE username='" + selectedUsername + "' and alphanumeric='" + alphanumeric + "'";
        Alpha alpha = null;
        //System.out.println(sql);
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(sql);
        if (rs.next()) {
           return true;
        }

        return exist;

    }

    @Override
    public int removeUseAlpha(String alphanumeric, String username, Connection conn) throws SQLException {
        String sql = "Delete from tAllowedAlphanumerics  where username=? and alphanumeric =?";
        PreparedStatement pst = conn.prepareStatement(sql);
        pst.setString(2, alphanumeric);
        pst.setString(1, username);
        System.out.println(pst);

        int deleted = pst.executeUpdate();

        return deleted;
    }
}
