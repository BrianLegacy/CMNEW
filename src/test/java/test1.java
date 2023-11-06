//public void getsheetvalue() {
//        tableData = new ArrayList();
//        try {
//
//            //Get the nth sheet from the workbook
//            Sheet sheet = workbook.getSheet(sheetname);
//            System.out.println("Excel sheet selected : " + sheetname);
//            getSheetCols();
//            System.out.println("The Excel sheet has " + cols + " column " + " and " + rows + " rows");
//            for (int d = 0; d < rows; d++) {
//                Row row = sheet.getRow(d);
//                String[] kevol = new String[cols];
//                for (int j = 0; j < cols; j++) {             
//                    
//                    if (rows == 1 && cols == 1) {
//                        kevol[j] = " ";
//                        break;
//                    }
//                    try {
//                        Cell cell2 = row.getCell(j);
//                        //test code
////                        cell2.setCellType(1);
//                        switch (cell2.getCellType()) {
//                            case Cell.CELL_TYPE_STRING:
//                                if (cell2.equals("")) {
//
//                                    kevol[j] = " ";
//                                } else {
//                                    kevol[j] = cell2.getRichStringCellValue().getString();
//                                }
//
//                                break;
//                            case Cell.CELL_TYPE_NUMERIC:
//                                if (HSSFDateUtil.isCellDateFormatted(cell2)) {
//                                    kevol[j] = String.valueOf(cell2.getDateCellValue());
//                                } else {
//                                    kevol[j] = String.valueOf(cell2.getNumericCellValue());
//                                }
//                                break;
//                            case Cell.CELL_TYPE_BOOLEAN:
//                                kevol[j] = String.valueOf(cell2.getBooleanCellValue());
//                                break;
//                            case Cell.CELL_TYPE_FORMULA:
//                                kevol[j] = (cell2.getCellFormula());
//                                break;
//                            case Cell.CELL_TYPE_BLANK:
//                                kevol[j] = "";
//                            default:
//                                         System.out.println();
//                        }
//
//                    } catch (Exception ex) {
//                        kevol[j] = "";
//
//                    }
// 
//                }
//              
// tableData.add(Arrays.asList(kevol));
//            }
//
//            //end of sheets for loop
//            //close file input stream
//            columnHeaders = new ArrayList();
//            for (int r = 0; r < cols; r++) {
//                String hold = "col";
//                String colnum = String.valueOf(r);
//                String fullcol = hold.concat(colnum);
//                System.out.println(fullcol);
//                columnHeaders.add(fullcol);
//
////                columns.add(new ColumnModel(fullcol.toUpperCase(), fullcol));
//            }
//
//        } catch (Exception e) {
//            messager = new FacesMessage(" Not Succesful", " Check the columns/ FORMAT of your Excel file.");
//            FacesContext.getCurrentInstance().addMessage(null, messager);
//        }
//
//    }
//
//import java.util.ArrayList;
//import java.util.Arrays;
//import javax.faces.application.FacesMessage;
//import javax.faces.context.FacesContext;
//import org.apache.poi.hssf.usermodel.HSSFDateUtil;
//import org.apache.poi.ss.usermodel.Cell;
//import org.apache.poi.ss.usermodel.Row;
//import org.apache.poi.ss.usermodel.Sheet;
//
