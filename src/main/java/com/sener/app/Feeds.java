package com.sener.app;

import facebook4j.*;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class Feeds {

    public static void getFacebookFeed(Facebook Facebook, String searchPost, HSSFSheet sheetFeed, HSSFWorkbook workbook) throws FacebookException {

        // Deklarationen
        Cell cellFeed;
        Row rowFeed;

        int rowNumFeed = 0;

        //searchPost = "";
        ResponseList<Post> results = Facebook.getFeed(searchPost);
        for (Post feed : results) {

            rowFeed = sheetFeed.createRow(rowNumFeed++);

            int cellNumFeed = 0;

            // FeedID
            cellFeed = rowFeed.createCell(cellNumFeed++);
            cellFeed.setCellValue(feed.getId().toString());

            // UserID
            cellFeed = rowFeed.createCell(cellNumFeed++);
            cellFeed.setCellValue(feed.getFrom().getId().toString());

            // User aufrufen
            User user = Facebook.getUser(feed.getFrom().getId().toString());

            // UserID
            cellFeed = rowFeed.createCell(cellNumFeed++);
            cellFeed.setCellValue(feed.getFrom().getName().toString());

            // Geschlecht
            cellFeed = rowFeed.createCell(cellNumFeed++);
            cellFeed.setCellValue(user.getGender());

            // Herkunftsland - NULLPOINTEREXCEPTION!!!
            //cellFeed = rowFeed.createCell(cellNumFeed++);
            //cellFeed.setCellValue(user.getLocale().toString());
            //System.out.println(user.getLocale());

            // Feedtext
            cellFeed = rowFeed.createCell(cellNumFeed++);
            cellFeed.setCellValue(feed.getMessage());

            // Erstellungszeit
            cellFeed = rowFeed.createCell(cellNumFeed++);
            cellFeed.setCellValue(feed.getCreatedTime().toString());

            // Likes

            System.out.println(feed.getFrom().getName());
            System.out.println(feed.getMessage());
            System.out.println(feed.getCreatedTime());
            System.out.println(feed.getLikes().getPaging());
            System.out.println(user.getLocale());
            System.out.println("---------------------------");

            FileOutputStream out = null;
            try {
                // Ergebnisse in Excel-File übertragen
                out = new FileOutputStream(new File("/Users/karatee/Desktop/test.xls"));
                workbook.write(out);
                out.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            // Posts voneinander trennen (Für Datenbankübertragung später rausnehmen)
            rowFeed = sheetFeed.createRow(rowNumFeed++);
        }
    }
}
