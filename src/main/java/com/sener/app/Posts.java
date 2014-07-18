package com.sener.app;

import facebook4j.Facebook;
import facebook4j.FacebookException;
import facebook4j.Post;
import facebook4j.ResponseList;
import facebook4j.User;
import facebook4j.Paging;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class Posts {

    public static void getFacebookPosts(Facebook Facebook, String searchPost, HSSFSheet sheetPost, HSSFSheet sheetComment, HSSFWorkbook workbook) throws FacebookException {

        // Deklarationen
        Cell cellPost, cellComment;
        Row rowPost, rowComment;
        int rowNumPost, rowNumComment;
        int cellNumPost, cellNumComment;

        // Posts zur Suchseite finden
        ResponseList<Post> results = Facebook.getPosts(searchPost);

        //Pagination http://facebook4j.org/en/code-examples.html

        rowNumPost = 0;
        rowNumComment = 0;

        // Für jeden Post
        for (Post post : results) {

            rowPost = sheetPost.createRow(rowNumPost++);
            rowComment = sheetComment.createRow(rowNumComment++);

            cellNumPost = 0;

            // PostID
            cellPost = rowPost.createCell(cellNumPost++);
            cellPost.setCellValue(post.getId().toString());

            // Posttext
            cellPost = rowPost.createCell(cellNumPost++);
            cellPost.setCellValue(post.getMessage().toString());

            // Erstellungszeit
            cellPost = rowPost.createCell(cellNumPost++);
            cellPost.setCellValue(post.getCreatedTime().toString());

            // Anzahl Shares
            cellPost = rowPost.createCell(cellNumPost++);
            cellPost.setCellValue(post.getSharesCount());

            // Für alle Kommentare des einzelnen Posttext
            for (int j = 0; j < post.getComments().size(); j++) {

                // Für jedes einzelne Kommentar
                for (int k = 0; k < 1; k++) {

                    cellNumComment = 0;

                    // KommentarID
                    cellComment = rowComment.createCell(cellNumComment++);
                    cellComment.setCellValue(post.getComments().get(j).getId());

                    // UserID
                    cellComment = rowComment.createCell(cellNumComment++);
                    cellComment.setCellValue(post.getComments().get(j).getFrom().getId().toString());

                    // User aufrufen
                    //User user = Facebook.getUser(post.getComments().get(j).getFrom().getId());

                    // Name
                    cellComment = rowComment.createCell(cellNumComment++);
                    cellComment.setCellValue(post.getComments().get(j).getFrom().getName().toString());

                    // Geschlecht
                    //cell = row.createCell(cellNumComment++);
                    //cell.setCellValue(user.getGender());

                    // Herkunftsland
                    //cell = row.createCell(cellNumComment++);
                    //cell.setCellValue(user.getLocale().toString());
                    //System.out.println(user.getLocale());

                    // Nachricht
                    cellComment = rowComment.createCell(cellNumComment++);
                    cellComment.setCellValue(post.getComments().get(j).getMessage().toString());

                    // Erstellungszeit
                    cellComment = rowComment.createCell(cellNumComment++);
                    cellComment.setCellValue(post.getComments().get(j).getCreatedTime().toString());

                    // Anzahl Likes
                    cellComment = rowComment.createCell(cellNumComment++);
                    cellComment.setCellValue(post.getComments().get(j).getLikeCount().toString());

                }
                // Kommentare zu jedem Post voneinander trennen (Für Datenbankübertragung später rausnehmen)
                rowComment = sheetComment.createRow(rowNumComment++);

            }
            // Posts voneinander trennen (Für Datenbankübertragung später rausnehmen)
            rowPost = sheetPost.createRow(rowNumPost++);

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

        }
    }
}
