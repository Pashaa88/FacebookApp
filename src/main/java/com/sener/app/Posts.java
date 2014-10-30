package com.sener.app;

import facebook4j.*;

import facebook4j.internal.org.json.JSONArray;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;

public class Posts {

    public static void getFacebookPosts(Facebook Facebook, String searchPost, HSSFSheet sheetPost, HSSFSheet sheetComment, HSSFWorkbook workbook) throws FacebookException {

        // Deklarationen
        Cell cellPost, cellComment;
        Row rowPost, rowComment;

        Calendar calendar = Calendar.getInstance();
        java.util.Date now = calendar.getTime();
        java.sql.Timestamp currentTimestamp = new java.sql.Timestamp(now.getTime());

        // Posts zur Suchseite finden
        //ResponseList<Post> resultsPost = Facebook.getPosts(searchPost, new Reading().until(currentTimestamp));
        ResponseList<Post> resultsPost = Facebook.getPosts(searchPost, new Reading().since("last week"));

        int rowNumPost = 0;
        int rowNumComment = 0;

        // Für jeden Post
        for (Post post : resultsPost) {

            if (post.getStory() == null) {

                int cellNumPost = 0;

                rowPost = sheetPost.createRow(rowNumPost++);
                rowComment = sheetComment.createRow(rowNumComment++);

                // PostID
                cellPost = rowPost.createCell(cellNumPost++);
                cellPost.setCellValue(post.getId());

                // Posttext
                cellPost = rowPost.createCell(cellNumPost++);
                if (post.getMessage() != null) {
                    cellPost.setCellValue(post.getMessage());
                }

                // Erstellungszeit
                cellPost = rowPost.createCell(cellNumPost++);
                cellPost.setCellValue(post.getCreatedTime().toString());

                // Anzahl Shares
                cellPost = rowPost.createCell(cellNumPost++);
                if (post.getSharesCount() != null) {
                    cellPost.setCellValue(post.getSharesCount());
                }

                // Kommentare auslesen
                ResponseList<Comment> resultsComments = Facebook.getPostComments(post.getId());

                // Solange bis keine nächste Seite erfolgt
                while (resultsComments.size() > 0 && resultsComments.getPaging().getNext() != null) {

                    // Für alle Kommentare des einzelnen Posttext
                    for (Comment comment : resultsComments) {

                        int cellNumComment = 0;

                        // KommentarID
                        cellComment = rowComment.createCell(cellNumComment++);
                        cellComment.setCellValue(comment.getId());

                        // UserID
                        cellComment = rowComment.createCell(cellNumComment++);
                        cellComment.setCellValue(comment.getFrom().getId());

                        // User aufrufen
                        User user = Facebook.getUser(comment.getFrom().getId());

                        // Name
                        cellComment = rowComment.createCell(cellNumComment++);
                        cellComment.setCellValue(comment.getFrom().getName());

                        // Geschlecht
                        cellComment = rowComment.createCell(cellNumComment++);
                        cellComment.setCellValue(user.getGender());

                        // Herkunftsland
                        cellComment = rowComment.createCell(cellNumComment++);
                        if (user.getLocale() != null) {
                            cellComment.setCellValue(user.getLocale().getDisplayCountry());
                        }

                        // Freunde
                        //String query = "SELECT friend_count FROM user WHERE uid = " + user.getId();
                        //JSONArray jsonArray = Facebook.executeFQL(query);
                        //System.out.println(jsonArray);

                        // Kommentartext
                        cellComment = rowComment.createCell(cellNumComment++);
                        cellComment.setCellValue(comment.getMessage());

                        // Erstellungszeit
                        cellComment = rowComment.createCell(cellNumComment++);
                        cellComment.setCellValue(comment.getCreatedTime().toString());

                        // Anzahl Likes
                        cellComment = rowComment.createCell(cellNumComment++);
                        cellComment.setCellValue(comment.getLikeCount().toString());

                        rowComment = sheetComment.createRow(rowNumComment++);

                    }

                    if (resultsComments.getPaging().getNext() != null || resultsComments.getPaging() != null) {
                        // Nächste Seite der Kommentare
                        resultsComments = Facebook.fetchNext(resultsComments.getPaging());

                    } else {
                        break;
                    }
                }

                // Posts voneinander trennen (Für Datenbankübertragung später rausnehmen)
                rowPost = sheetPost.createRow(rowNumPost++);

            }



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

