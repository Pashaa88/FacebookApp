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
import java.util.Calendar;
import java.util.Date;

public class Posts {

    public static void getFacebookPosts(Facebook Facebook, String searchPost, HSSFSheet sheetPost, HSSFSheet sheetComment, HSSFWorkbook workbook) throws FacebookException {

        // Deklarationen
        Cell cellPost, cellComment;
        Row rowPost, rowComment;

        Calendar calendar = Calendar.getInstance();
        java.util.Date now = calendar.getTime();
        java.sql.Timestamp currentTimestamp = new java.sql.Timestamp(now.getTime());

        // Posts zur Suchseite finden
        ResponseList<Post> resultsPost = Facebook.getPosts(searchPost, new Reading().until(currentTimestamp));

        int rowNumPost = 0;
        int rowNumComment = 0;

        // Für jeden Post
        for (Post post : resultsPost) {

            int cellNumPost = 0;

            rowPost = sheetPost.createRow(rowNumPost++);
            rowComment = sheetComment.createRow(rowNumComment++);

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

            // Kommentare auslesen
            ResponseList<Comment> resultsComments = Facebook.getPostComments(post.getId());

            int CommentCounter = 0;

            // Solange bis keine nächste Seite erfolgt
            do {

                // Für alle Kommentare des einzelnen Posttext
                for (Comment comment : resultsComments) {

                    int cellNumComment = 0;

                    // KommentarID
                    cellComment = rowComment.createCell(cellNumComment++);
                    cellComment.setCellValue(comment.getId());

                    // UserID
                    cellComment = rowComment.createCell(cellNumComment++);
                    cellComment.setCellValue(comment.getFrom().getId().toString());

                    // User aufrufen
                    //User user = Facebook.getUser(post.getComments().get(j).getFrom().getId());
                    //ResponseList<User> resultsUser = Facebook.searchUsers(post.getComments().get(j).getFrom().getId());

                    // Name
                    cellComment = rowComment.createCell(cellNumComment++);
                    cellComment.setCellValue(comment.getFrom().getName().toString());

                    // Geschlecht
                    //cellComment = rowComment.createCell(cellNumComment++);
                    //cellComment.setCellValue(user.getGender());

                    // Herkunftsland - NULLPOINTEREXCEPTION!!!
                    //cell = row.createCell(cellNumComment++);
                    //cell.setCellValue(user.getLocale().toString());
                    //System.out.println(user.getLocale());
                    // OR ResponseList<Location> location = Facebook.searchLocations("UserId");

                    // Nachricht
                    cellComment = rowComment.createCell(cellNumComment++);
                    cellComment.setCellValue(comment.getMessage().toString());

                    // Erstellungszeit
                    cellComment = rowComment.createCell(cellNumComment++);
                    cellComment.setCellValue(comment.getCreatedTime().toString());

                    // Anzahl Likes
                    cellComment = rowComment.createCell(cellNumComment++);
                    cellComment.setCellValue(comment.getLikeCount().toString());

                    rowComment = sheetComment.createRow(rowNumComment++);

                }
                if (resultsComments.getPaging().getNext() != null) {
                    // Nächste Seite der Kommentare
                    ResponseList<Comment> resultsNextComments = Facebook.fetchNext(resultsComments.getPaging());
                    resultsComments = resultsNextComments;
                }
                else {
                    break;
                }

            } while (resultsComments.size() > 0 && resultsComments.getPaging() != null);

            // Posts voneinander trennen (Für Datenbankübertragung später rausnehmen)
            rowPost = sheetPost.createRow(rowNumPost++);

        }
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

