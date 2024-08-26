/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.mspace.clientmanager.reports;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import ke.co.mspace.nonsmppmanager.model.SMPPOut;
import ke.co.mspace.nonsmppmanager.service.SMSOutServiceApi;
import ke.co.mspace.nonsmppmanager.service.SMSOutServiceImpl;
import ke.co.mspace.nonsmppmanager.util.JsfUtil;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 *
 * @author olal
 */
@ManagedBean(name = "smppreport")
@ViewScoped
public class SmppController {

    private List<SMPPOut> records;
    private String username;
    private Date reportStartDate = new Date();
    private Date reportEndDate = new Date();
    private SMSOutServiceApi smsDAO;
    private int rows;

    @PostConstruct
    public void init() {
        smsDAO = new SMSOutServiceImpl();
    }

    public int getRows() {
        return rows;
    }

    public void setRows(int rows) {
        this.rows = rows;
    }

    public List<SMPPOut> getRecords() {
        return records;
    }

    public void setRecords(List<SMPPOut> records) {
        this.records = records;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Date getReportStartDate() {
        return reportStartDate;
    }

    public void setReportStartDate(Date reportStartDate) {
        this.reportStartDate = reportStartDate;
    }

    public Date getReportEndDate() {
        return reportEndDate;
    }

    public void setReportEndDate(Date reportEndDate) {
        this.reportEndDate = reportEndDate;
    }

    public void generateSMPP() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");

        String startDate = simpleDateFormat.format(reportStartDate);
        startDate = startDate.substring(0, 8) + "000001";

        String endDate = simpleDateFormat.format(reportEndDate);
        endDate = endDate.substring(0, 8) + "235959";

        if (!username.isEmpty()) {
            records = smsDAO.fetchSMPPReport(username, startDate, endDate);
            rows = smsDAO.getTotalSmppCount(startDate, endDate, username);
            if (!records.isEmpty()) {
                JsfUtil.addSuccessMessage(rows + " SMS sent.");
            } else {
                JsfUtil.addSuccessMessage("No records found match search.");
            }
        } else {
            JsfUtil.addErrorMessage("No User selected.");
        }
    }

    public void postProcessXLSX(Object document) {
        XSSFWorkbook workbook = (XSSFWorkbook) document;
        XSSFSheet sheet = workbook.getSheetAt(0);
        Row header = sheet.getRow(0);

        CellStyle cellStyle = workbook.createCellStyle();
        Font font = workbook.createFont();
        font.setBold(true);
        cellStyle.setFont(font);

        for (int i = 0; i < header.getPhysicalNumberOfCells(); i++) {
            Cell cell = header.getCell(i);
            cell.setCellStyle(cellStyle);
        }
        int lastRowNum = sheet.getLastRowNum();
        Row newRow = sheet.createRow(lastRowNum + 1);
        Cell newCell = newRow.createCell(8);

        String message = "SMS sent during this period is " + rows;
        newCell.setCellValue(message);
    }
}
