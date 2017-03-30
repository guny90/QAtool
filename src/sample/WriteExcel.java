package sample;

import jxl.CellView;
import jxl.Workbook;
import jxl.WorkbookSettings;
import jxl.format.UnderlineStyle;
import jxl.write.*;
import jxl.write.biff.RowsExceededException;

import java.io.File;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Locale;

/**
 * Created by gsamadova on 2/13/2017.
 */
public class WriteExcel {

    private WritableCellFormat timesBoldUnderLine;
    private WritableCellFormat times;
    private String inputFile;

    public void setOutputFile(String inputFile) {
        this.inputFile = inputFile;
    }

    public void write(ResultSet prodSet, ResultSet testSet) throws IOException, WriteException  {
        File file = new File(inputFile);
        WorkbookSettings workbookSettings = new WorkbookSettings();
        workbookSettings.setLocale(new Locale("en","EN"));
        WritableWorkbook workbook = Workbook.createWorkbook(file, workbookSettings);
        workbook.createSheet("Production",0);
        workbook.createSheet("Testbed", 1);
        WritableSheet prodSheet = workbook.getSheet(0);
        WritableSheet testSheet = workbook.getSheet(1);
        createLabel(prodSheet);
        createContent(prodSheet, prodSet);
        createLabel(testSheet);
        createContent(testSheet, testSet);
        workbook.write();
        workbook.close();
    }

    private void createLabel(WritableSheet sheet) throws WriteException {
        WritableFont times10pt = new WritableFont(WritableFont.TIMES, 10);
        times = new WritableCellFormat(times10pt);
        times.setWrap(true);

        WritableFont times10ptBoldUnterLine = new WritableFont(WritableFont.TIMES, 10, WritableFont.BOLD, false, UnderlineStyle.SINGLE);
        timesBoldUnderLine = new WritableCellFormat(times10ptBoldUnterLine);
        timesBoldUnderLine.setWrap(true);

        CellView cv = new CellView();
        cv.setFormat(times);
        cv.setFormat(timesBoldUnderLine);
        cv.setAutosize(true);

        // Write a few headers
        addCaption(sheet, 0, 0, "MSISDN");
        addCaption(sheet, 1, 0, "ICCID");
        addCaption(sheet, 2, 0, "Status");
        addCaption(sheet, 3, 0, "Used by");
    }

    private void createContent(WritableSheet sheet, ResultSet set) throws WriteException, RowsExceededException    {

        int i =0;
        try {
            while(set.next())   {
                addLabel(sheet, 0, i, set.getString("msisdn"));
                addLabel(sheet, 1, i, set.getString("iccid"));
                addLabel(sheet, 2, i, set.getString("status"));
                addLabel(sheet, 3, i, set.getString("name") + " " +
                        set.getString("surname") + " " + set.getString("comment"));
                i++;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void addCaption(WritableSheet sheet, int column, int row, String s) throws WriteException {
        Label label;
        label = new Label(column, row, s, timesBoldUnderLine);
        sheet.addCell(label);
    }

    private void addLabel(WritableSheet sheet, int column, int row, String s) throws WriteException{
        Label label;
        label = new Label(column, row, s, times);
        sheet.addCell(label);
    }
}
