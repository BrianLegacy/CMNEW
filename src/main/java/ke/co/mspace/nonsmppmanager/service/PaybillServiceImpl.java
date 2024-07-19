/*     */ package ke.co.mspace.nonsmppmanager.service;

/*     */
 /*     */ import java.sql.Connection;
/*     */ import java.sql.PreparedStatement;
/*     */ import java.sql.ResultSet;
/*     */ import java.sql.SQLException;
/*     */ import java.sql.Statement;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import java.util.logging.Level;
/*     */ import java.util.logging.Logger;
/*     */ import ke.co.mspace.nonsmppmanager.model.Paybill;
/*     */ import ke.co.mspace.nonsmppmanager.util.JdbcUtil;
/*     */ import ke.co.mspace.nonsmppmanager.util.JsfUtil;

public class PaybillServiceImpl implements PaybillServiceApi {

    private static final Logger LOG = Logger.getLogger(PaybillServiceImpl.class.getName());
    Paybill paybill = new Paybill();
    List paybills;

    public List<String> getPaybill(Connection conn) /*     */ {
        String sql = "SELECT DISTINT FROM ";
        ResultSet rs = null;
        PreparedStatement stmt = null;
        paybills = new ArrayList();
        try {
            stmt = conn.prepareStatement(sql);
            rs = stmt.executeQuery();
            while (rs.next()) {
                String a = rs.getString("short_code");
                paybills.add(a);
            }

            return paybills;
        } catch (SQLException e) {
            Logger.getLogger(PaybillServiceImpl.class.getName()).log(Level.SEVERE, null, e.getMessage());
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }

                if (stmt != null) {
                    stmt.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(PaybillServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return paybills;
    }

    @Override
    public List<Paybill> getAllPaybill(Connection conn) {

        System.out.println("EXECUTING THE LIST");
        String sql = "SELECT * FROM tUSERPAYBILL";
        List<Paybill> result = new ArrayList();
        try {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);

            while (rs.next()) {
                Paybill paybill = new Paybill();
                paybill.setUserid(rs.getInt("tUSER_id"));
                paybill.setPaybillz(rs.getInt("paybill"));
                paybill.setMessage(rs.getString("reply"));
                paybill.setEmail(rs.getString("email"));

                result.add(paybill);
            }
            JdbcUtil.closeConnection(conn);
        } catch (SQLException ex) {
            JdbcUtil.printSQLException(ex);
        }

        return result;
    }


    /*     */ public void persistPaybill(int id, int paybill, Connection conn)
            /*     */ throws SQLException /*     */ {
        /* 114 */ String sql = "INSERT INTO tUSERPAYBILL(tUSER_id,paybill,reply,email) VALUES (?,?,? ?)";
        /*     */
 /*     */
 /*     */
 /*     */
 /* 119 */ PreparedStatement pstmt = conn.prepareStatement(sql);
        /*     */
 /*     */
 /*     */
 /*     */
 /* 124 */ pstmt.setInt(1, id);
        /* 125 */ pstmt.setInt(2, paybill);
        /*     */
 /*     */
 /*     */
 /*     */
 /* 130 */ int count = pstmt.executeUpdate();
        /* 131 */ System.out.println("Persist Paybill : " + count);
        /*     */    }

    /*     */
 /*     */
 /*     */
 /*     */ public void updatePaybill(int id, int paybill, Connection conn)
            /*     */ throws SQLException /*     */ {
        /* 139 */ String sql = "update tUSERPAYBILL set paybill= ? WHERE id in (select username from tUSER where id = ? or super_account_id in(select id from tUSER where id = ?))";
        /*     */
 /*     */
 /*     */
 /*     */
 /*     */
 /*     */
 /*     */
 /* 147 */ PreparedStatement pstmt = conn.prepareStatement(sql);
        /*     */
 /* 149 */ pstmt.setInt(1, paybill);
        /* 150 */ pstmt.setInt(2, id);
        /* 151 */ pstmt.setInt(3, id);
        /*     */
 /*     */
 /* 154 */ int count = pstmt.executeUpdate();
        /* 155 */ System.out.println("Count status" + count);
        /*     */    }

    /*     */
 /*     */ /*     */
 /*     */
 /*     */ public Paybill loadPaybillByUsername(String selectedUsername, Connection conn)
            /*     */ throws SQLException /*     */ {
        /* 162 */ String sql = "SELECT * FROM tUSERPAYBILL WHERE username='" + selectedUsername + "'";
        /* 163 */ Paybill paybill = new Paybill();
        /*     */
 /* 165 */ Statement stmt = conn.createStatement();
        /* 166 */ ResultSet rs = stmt.executeQuery(sql);
        /*     */
 /* 168 */ while (rs.next()) /*     */ {
            /* 170 */ paybill.setUserid(rs.getInt("tUSER_id"));
            /* 171 */ paybill.setName(rs.getString("username"));
            /*     */
 /* 173 */ paybill.setUserid(rs.getInt("paybill"));
            /*     */        }
        /*     */
 /*     */
 /* 177 */ return paybill;
        /*     */    }

    /*     */
 /*     */
    @Override
    public void updatePaybillByUsername(String previousUsername, String username, Connection conn)
            /*     */ throws SQLException /*     */ {
        /* 183 */ String sql = "UPDATE tAllowedAlphanumerics SET username=? WHERE username=?";
        /*     */
 /*     */
 /*     */
 /*     */
 /* 188 */ PreparedStatement pstmt = conn.prepareStatement(sql);
        /*     */
 /* 190 */ pstmt.setString(1, previousUsername);
        /* 191 */ pstmt.setString(2, username);
        /*     */

 /* 194 */ int count = pstmt.executeUpdate();
        /*     */    }

    /*     */ /*     */
 /*     */ public void deletePaybillfromtRULES(Paybill paybill, Connection conn) throws SQLException /*     */ {
        /* 199 */ String sql2 = "DELETE FROM tRULES where dest=?";
        /* 200 */ PreparedStatement pstmt = conn.prepareStatement(sql2);
        /*     */
 /* 202 */ System.out.println("Deleted  from database.." + paybill.getPaybillz());
        /* 203 */ pstmt.setInt(1, paybill.getPaybillz());
        /*     */
 /* 205 */ pstmt.executeUpdate();
        /*     */    }

    public void deletePaybill(Paybill paybill, Connection conn) {
        try {
            String sql = "DELETE FROM tUSERPAYBILL WHERE tUSER_id=?";
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                System.out.println("Deleted  from database..>>>>>>>>>>>");
                pstmt.setInt(1, paybill.getUserid());
                System.out.println("DELETING OF USER ID: " + paybill.getUserid());
                int count = pstmt.executeUpdate();
            }
        } catch (SQLException ex) {
            System.out.println("Error occured: " + ex);
            JsfUtil.addSuccessMessage("Error occured while deleting the paybill.Send below to your administrator for a solution" + ex);
        }
    }

//    @Override
// public void generateXSL() {
//         try {
//            /* 253 */ HSSFWorkbook wb = new HSSFWorkbook();
//            /* 254 */ Map<String, CellStyle> styles = createStyles(wb);
//            /* 255 */ HSSFSheet sheet = wb.createSheet("User_Alphanumerics_sheet_1");
//            /*     */
// /* 257 */ PrintSetup printSetup = sheet.getPrintSetup();
//            /* 258 */ printSetup.setLandscape(true);
//            /* 259 */ sheet.setFitToPage(true);
//            /* 260 */ sheet.setHorizontallyCenter(true);
//            /*     */
// /*     */
// /* 263 */ Row titleRow = sheet.createRow(0);
//            /* 264 */ titleRow.setHeightInPoints(45.0F);
//            /* 265 */ Cell titleCell = titleRow.createCell(0);
//            /* 266 */ titleCell.setCellValue("USER PAYBILL/TILL NUMBER LIST");
//            /* 267 */ titleCell.setCellStyle((CellStyle) styles.get("title"));
//            /* 268 */ sheet.addMergedRegion(CellRangeAddress.valueOf("$A$1:$B$1"));
//            /*     */
// /* 270 */ String[] titles = {"USERNAME", "PAYBILL/TILL NUMBER"};
//            /* 271 */ HSSFRow row = sheet.createRow(1);
//            /* 272 */ row.setHeightInPoints(40.0F);
//            /*     */
// /*     */
// /* 275 */ for (int i = 0; i < titles.length; i++) {
//                /* 276 */ Cell headerCell = row.createCell(i);
//                /* 277 */ headerCell.setCellValue(titles[i]);
//                /* 278 */ headerCell.setCellStyle((CellStyle) styles.get("header"));
//                /*     */            }
//            /* 280 */ List<Paybill> PaybillList = null;
//            /*     */ Connection conn;
//            /* 282 */ 
//                JdbcUtil util = new JdbcUtil();
//                /* 283 */ conn = util.getConnectionTodbPAYMENT();
//                /*     */
// /* 285 */ PaybillList = getAllPaybill(conn);
//                /* 286 */ JdbcUtil.closeConnection(conn);
//                /*     */            
//            /* 290 */ int rowNum = 2;
//            /*     */ /*     */
// /* 292 */ for (Paybill paybill : PaybillList) {
//                /* 293 */ row = sheet.createRow(rowNum);
//                /* 294 */ row.createCell(0).setCellValue(paybill.getPaybillz());
//                /* 295 */ row.createCell(1).setCellValue(paybill.getName());
//                /*     */
// /* 297 */ rowNum++;
//                /*     */            }
//            /*     */
// /* 300 */ sheet.setColumnWidth(0, 5120);
//            /* 301 */ sheet.setColumnWidth(1, 5120);
//            /*     */
// /* 303 */ FacesContext context = FacesContext.getCurrentInstance();
//            /* 304 */ HttpServletResponse res = (HttpServletResponse) context.getExternalContext().getResponse();
//            /* 305 */ res.setContentType("application/vnd.ms-excel");
//            /* 306 */ res.setHeader("Content-disposition", "attachment;filename=mydata.xlsx");
//            /*     */
// /* 308 */ ServletOutputStream out = res.getOutputStream();
//            /* 309 */ wb.write(out);
//            /* 310 */ out.flush();
//            /* 311 */ out.close();
//            /* 312 */ FacesContext.getCurrentInstance().responseComplete();
//            /*     */        } catch (Exception e) {
//            /* 314 */ e.printStackTrace();
//            /*     */        }
//        /*     */    }

    /*     */
// /*     */ private static Map<String, CellStyle> createStyles(Workbook wb) {
//        /* 319 */ Map<String, CellStyle> styles = new HashMap();
//        /*     */
// /* 321 */ Font titleFont = wb.createFont();
//        /* 322 */ titleFont.setFontHeightInPoints((short) 18);
//        /* 323 */ titleFont.setBoldweight((short) 700);
//        /* 324 */ CellStyle style = wb.createCellStyle();
//        /* 325 */ style.setAlignment((short) 2);
//        /* 326 */ style.setVerticalAlignment((short) 1);
//        /* 327 */ style.setFont(titleFont);
//        /* 328 */ styles.put("title", style);
//        /*     */
// /* 330 */ Font monthFont = wb.createFont();
//        /* 331 */ monthFont.setFontHeightInPoints((short) 11);
//        /* 332 */ monthFont.setColor(IndexedColors.WHITE.getIndex());
//        /* 333 */ style = wb.createCellStyle();
//        /* 334 */ style.setAlignment((short) 2);
//        /* 335 */ style.setVerticalAlignment((short) 1);
//        /* 336 */ style.setFillForegroundColor(IndexedColors.GREY_50_PERCENT.getIndex());
//        /* 337 */ style.setFillPattern((short) 1);
//        /* 338 */ style.setFont(monthFont);
//        /* 339 */ style.setWrapText(true);
//        /* 340 */ styles.put("header", style);
//        /*     */
// /* 342 */ style = wb.createCellStyle();
//        /* 343 */ style.setAlignment((short) 2);
//        /* 344 */ style.setWrapText(true);
//        /* 345 */ style.setBorderRight((short) 1);
//        /* 346 */ style.setRightBorderColor(IndexedColors.BLACK.getIndex());
//        /* 347 */ style.setBorderLeft((short) 1);
//        /* 348 */ style.setLeftBorderColor(IndexedColors.BLACK.getIndex());
//        /* 349 */ style.setBorderTop((short) 1);
//        /* 350 */ style.setTopBorderColor(IndexedColors.BLACK.getIndex());
//        /* 351 */ style.setBorderBottom((short) 1);
//        /* 352 */ style.setBottomBorderColor(IndexedColors.BLACK.getIndex());
//        /* 353 */ styles.put("cell", style);
//        /*     */
// /* 355 */ style = wb.createCellStyle();
//        /* 356 */ style.setAlignment((short) 2);
//        /* 357 */ style.setVerticalAlignment((short) 1);
//        /* 358 */ style.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
//        /* 359 */ style.setFillPattern((short) 1);
//        /* 360 */ style.setDataFormat(wb.createDataFormat().getFormat("0.00"));
//        /* 361 */ styles.put("formula", style);
//        /*     */
// /* 363 */ style = wb.createCellStyle();
//        /* 364 */ style.setAlignment((short) 2);
//        /* 365 */ style.setVerticalAlignment((short) 1);
//        /* 366 */ style.setFillForegroundColor(IndexedColors.GREY_40_PERCENT.getIndex());
//        /* 367 */ style.setFillPattern((short) 1);
//        /* 368 */ style.setDataFormat(wb.createDataFormat().getFormat("0.00"));
//        /* 369 */ styles.put("formula_2", style);
//        /*     */
// return styles;
//      }
    public void updatePaybill(String username, String paybill, Connection conn)
            /*     */ throws SQLException /*     */ {

        //String sql="SELECT * from "
/* 386 */ throw new UnsupportedOperationException("Not supported yet.");
        /*     */    }

    /*     */
 /*     */ public void persistPaybill(String username, String paybill, Connection conn) throws SQLException /*     */ {
        /* 391 */ throw new UnsupportedOperationException("Not supported yet.");
        /*     */    }

    /*     */ /*     */
 /*     */ public Paybill loadAlphanumericByUsername(String selectedUsername, Connection conn) throws SQLException /*     */ {
        /* 396 */ throw new UnsupportedOperationException("Not supported yet.");
        /*     */    }
    /*     */

    @Override
    public void generateXSL() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
 }


/* Location:              /home/admin/Downloads/navicat100_lite_en/clientmanager.war!/WEB-INF/classes/ke/co/mspace/nonsmppmanager/service/PaybillServiceImpl.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */
