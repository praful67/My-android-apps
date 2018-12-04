package com.example.parsaniahardik.pdffromimagetext;

import android.content.Intent;
import android.net.Uri;
import android.os.CountDownTimer;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Locale;

import jxl.Workbook;
import jxl.WorkbookSettings;
import jxl.format.UnderlineStyle;
import jxl.write.Label;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;

public class ExcelActivity extends AppCompatActivity {

    private WritableCellFormat times;
    private WritableCellFormat bold;
    WritableWorkbook workbook;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_excel);

        String Fnamexls = "logfile" + ".xls";
        File sdCard = Environment.getExternalStorageDirectory();
        File directory = new File(sdCard.getAbsolutePath() + "/newfolder");
        directory.mkdirs();
        File file = new File(directory, Fnamexls);
        WorkbookSettings wbSettings = new WorkbookSettings();

        wbSettings.setLocale(new Locale("en", "EN"));
        WritableFont times10pt = new WritableFont(WritableFont.TIMES, 10);
        // Define the cell format
        times = new WritableCellFormat(times10pt);
        // Lets automatically wrap the cells
        try {
            times.setWrap(true);
        } catch (WriteException e) {
            e.printStackTrace();
        }

        WritableFont boldstyle = new WritableFont(
                WritableFont.TIMES, 10, WritableFont.BOLD, true,
                UnderlineStyle.NO_UNDERLINE);
       /* try {
            boldstyle.setColour(Colour.BLUE);
        } catch (WriteException e) {
            e.printStackTrace();
        }*/
        bold = new WritableCellFormat(boldstyle);
        // Lets automatically wrap the cells
        try {
            bold.setWrap(true);
        } catch (WriteException e) {
            e.printStackTrace();
        }

        try {
            workbook = Workbook.createWorkbook(file, wbSettings);
        } catch (IOException e) {
            e.printStackTrace();
        }
        final WritableSheet sheet = workbook.createSheet("LOG SHEET", 0);


        sheet.setColumnView(0, 15);
        sheet.setColumnView(1, 15);
        sheet.setColumnView(2, 15);
        sheet.setColumnView(3, 20);
        sheet.setColumnView(4, 15);
        sheet.setColumnView(5, 15);

        int i = 0;
        while (i < 250) {

            Label labelroster = new Label(0, i, "Roster Name : ");
            labelroster.setCellFormat(bold);
            Label date = new Label(2, i, "Dates");
            date.setCellFormat(bold);
            Label datevalue = new Label(3, i, "Nov 7 to Nov 15 2018");
            Label today = new Label(4, 0, "Today : ");
            today.setCellFormat(bold);
            Label todayvaule = new Label(5, 0, "Nov 11 Fri 2018");

            Label labelrostername = new Label(1, i, "Morning Car (eg)");
            Label labeldriverlogin = new Label(0, i + 1, "Login Driver : ");
            labeldriverlogin.setCellFormat(bold);

            Label labeldriverloginname = new Label(1, i + 1, "Ramu");
            Label loginridestart = new Label(2, i + 1, "Ride Start : ");
            loginridestart.setCellFormat(bold);
            Label loginridstarttime = new Label(3, i + 1, "09:00 am");
            Label loginrideend = new Label(4, i + 1, "Ride End : ");
            loginrideend.setCellFormat(bold);
            Label loginridendtime = new Label(5, i + 1, "01:00 pm");
            Label labeldriverlogout = new Label(0, i + 2, "Logout Driver : ");
            labeldriverlogout.setCellFormat(bold);
            Label labeldriverlogoutname = new Label(1, i + 2, "Ramu");
            Label logoutridestart = new Label(2, i + 2, "Ride Start : ");
            logoutridestart.setCellFormat(bold);
            Label logoutridstarttime = new Label(3, i + 2, "09:00 am");
            Label logoutrideend = new Label(4, i + 2, "Ride End : ");
            logoutrideend.setCellFormat(bold);
            Label logoutridendtime = new Label(5, i + 2, "01:00 pm");
            Label label0 = new Label(0, i + 5, "S.No");
            label0.setCellFormat(bold);
            Label label1 = new Label(1, i + 5, "Emp. Name");
            label1.setCellFormat(bold);
            Label label2 = new Label(2, i + 5, "checkin time");
            label2.setCellFormat(bold);
            Label label3 = new Label(3, i + 5, "Checkout time");
            label3.setCellFormat(bold);
            Label label4 = new Label(0, i + 20, "Escorts : ");
            label4.setCellFormat(bold);
            i = i + 25;

            try {
                sheet.addCell(date);
                sheet.addCell(datevalue);

                sheet.addCell(label1);
                sheet.addCell(label0);
                sheet.addCell(label2);
                sheet.addCell(label3);
                sheet.addCell(today);
                sheet.addCell(todayvaule);

                sheet.addCell(label4);


                sheet.addCell(labeldriverlogin);
                sheet.addCell(labeldriverloginname);
                sheet.addCell(labeldriverlogout);
                sheet.addCell(labeldriverlogoutname);
                sheet.addCell(labelroster);
                sheet.addCell(labelrostername);
                sheet.addCell(loginrideend);
                sheet.addCell(logoutridestart);
                sheet.addCell(logoutrideend);
                sheet.addCell(loginridendtime);
                sheet.addCell(loginridestart);
                sheet.addCell(loginridstarttime);
                sheet.addCell(logoutridendtime);
                sheet.addCell(logoutridstarttime);
            } catch (WriteException e) {
                e.printStackTrace();
            }

        }

        try {
            workbook.write();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            workbook.close();
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setDataAndType(Uri.fromFile(file),"application/vnd.ms-excel");
            startActivity(intent);
        } catch (WriteException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }
}
 /* sheet.addCell(date);
          sheet.addCell(datevalue);

          sheet.addCell(label1);
          sheet.addCell(label0);
          sheet.addCell(label2);
          sheet.addCell(label3);
          sheet.addCell(today);
          sheet.addCell(todayvaule);

          sheet.addCell(label4);


          sheet.addCell(labeldriverlogin);
          sheet.addCell(labeldriverloginname);
          sheet.addCell(labeldriverlogout);
          sheet.addCell(labeldriverlogoutname);
          sheet.addCell(labelroster);
          sheet.addCell(labelrostername);
          sheet.addCell(loginrideend);
          sheet.addCell(logoutridestart);
          sheet.addCell(logoutrideend);
          sheet.addCell(loginridendtime);
          sheet.addCell(loginridestart);
          sheet.addCell(loginridstarttime);
          sheet.addCell(logoutridendtime);
          sheet.addCell(logoutridstarttime);*/