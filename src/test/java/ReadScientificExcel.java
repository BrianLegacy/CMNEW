
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Samson
 */
public class ReadScientificExcel {

    public static Workbook wb = null;
    public static String sheetname = null;
    static int cols, rows;

    public static Sheet getSheet() {
        Sheet sheet = null;
        File excel = new File("/home/Samson/Documents/documents/jasper.xls");
        try {
            InputStream istream = new FileInputStream(excel);
            wb = new HSSFWorkbook(istream);
            sheet = wb.getSheet("Sheet1");

        } catch (IOException ex) {
            Logger.getLogger(ReadScientificExcel.class.getName()).log(Level.SEVERE, null, ex);
        }

        return sheet;
    }

    public static void readSheetData(Sheet sheet) {

    }

    public static void main(String[] args) {
        getSheetCols();
        String[][] ret;
        Sheet sheet = getSheet();
        if (cols != 0) {
            System.out.println("The sheet has " + cols + " columns and " + rows + " rows");
            ret = new String[rows][cols];
            for (int d = 0; d < rows; d++) {
                Row row = sheet.getRow(d);
                String[] kevol = new String[cols];
                for (int j = 0; j < cols; j++) {

                    if (rows == 1 && cols == 1) {
                        kevol[j] = " ";
                        ret[d][j] = " ";
                        break;
                    }
                    try {
                        Cell cell2 = row.getCell(j);
                        //test code
//                        cell2.setCellType(1);
                        switch (cell2.getCellType()) {
                            case Cell.CELL_TYPE_STRING:
                                if (cell2.equals("")) {
                                    ret[d][j] = " ";
                                    kevol[j] = " ";
                                } else {
                                    System.out.println("Reading excel value " + cell2.getRichStringCellValue().getString());
                                    kevol[j] = cell2.getRichStringCellValue().getString();
                                    ret[d][j] = cell2.getRichStringCellValue().getString();
                                    System.out.println("Numeric in string value is " + cell2.getNumericCellValue() + " Converted to String: " + new BigDecimal(ret[d][j].toString()).toPlainString());

                                }

                                break;
                            case Cell.CELL_TYPE_NUMERIC:

                                if (DateUtil.isCellDateFormatted(cell2)) {
                                    DataFormatter dataFormatter = new DataFormatter();
                                    try {
                                        kevol[j] = (dataFormatter.formatCellValue(cell2));
                                        ret[d][j] = (dataFormatter.formatCellValue(cell2));
                                    } catch (Exception k) {
                                        kevol[j] = String.valueOf(cell2.getDateCellValue());
                                        ret[d][j] = String.valueOf(cell2.getDateCellValue());
                                    }
                                } else {
                                    String numericString = String.valueOf(cell2.getNumericCellValue());
                                    try {
                                        kevol[j] = new BigDecimal(numericString).toPlainString();
                                        ret[d][j] = new BigDecimal(numericString).toPlainString();
//                                        System.out.println("Numeric in string value is " + ret[d][j] + " Converted to String: "
//                                                + ret[d][j].toString());
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                        kevol[j] = numericString;
                                        ret[d][j] = numericString;
                                    }
                                }
                                break;
                            case Cell.CELL_TYPE_BOOLEAN:
                                kevol[j] = String.valueOf(cell2.getBooleanCellValue());
                                ret[d][j] = String.valueOf(cell2.getBooleanCellValue());

                                break;
                            case Cell.CELL_TYPE_FORMULA:
                                kevol[j] = (cell2.getCellFormula());
                                ret[d][j] = (cell2.getCellFormula());

                                break;
                            case Cell.CELL_TYPE_BLANK:
                                kevol[j] = "";
                                ret[d][j] = "";

                                break;
                            default:
                                System.out.println();
                                ret[d][j] = " ";
                                kevol[j] = " ";
                        }

                    } catch (Exception ex) {
                        kevol[j] = "";
                        ret[d][j] = "";
                    }

                }

            }
        } else {
            System.out.println("Cant get columns ");
        }
    }

    private static void getSheetCols() {
        sheetname = getSheet().getSheetName();
        if (wb != null) {
            Sheet sheet;
            int aa = 0;
            int bb = 0, bc;
            try {
                if ((sheetname != null)) {
                    sheet = wb.getSheet(sheetname);
//            rows = sheet.getPhysicalNumberOfRows();

                    Iterator rowIter = sheet.rowIterator();
                    while (rowIter.hasNext()) {
                        Row myRow = (Row) rowIter.next();
                        Iterator cellIter = myRow.cellIterator();
                        while (cellIter.hasNext()) {
                            Cell cell = (Cell) cellIter.next();
                            bc = cell.getColumnIndex();
                            if (bc > bb) {
                                bb = cell.getColumnIndex();
                            }
                            aa = cell.getRowIndex();
                        }

                        rows = aa + 1;
                        cols = bb + 1;

                    }
                }
            } catch (Exception ex) {
                System.out.println("null Exception for workbook and sheet name");
            }
        }
    }

}
