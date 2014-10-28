package com.sener.app;

import facebook4j.*;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import java.util.Calendar;

/**
 * Created by karatee on 28.10.14.
 */
public class Content {

    public static void getFacebookContents(Facebook Facebook, String searchContent, HSSFSheet sheetPost, HSSFSheet sheetComment, HSSFWorkbook workbook) throws FacebookException {

        Calendar calendar = Calendar.getInstance();
        java.util.Date now = calendar.getTime();
        java.sql.Timestamp currentTimestamp = new java.sql.Timestamp(now.getTime());

        // Posts zur Suchseite finden
        ResponseList<Post> resultsContent = Facebook.searchPosts(searchContent, new Reading().until(currentTimestamp));

        for (Post content : resultsContent) {

            System.out.println("Action: " + content.getActions());
            System.out.println("Application: " + content.getApplication());
            System.out.println("Caption: " + content.getCaption());
            System.out.println("Comments: " + content.getComments());
            System.out.println("CreatTime: " + content.getCreatedTime());
            System.out.println("Description: " + content.getDescription());
            System.out.println("From: " + content.getFrom());
            System.out.println("FullPicture: " + content.getFullPicture());
            System.out.println("Icon: " + content.getIcon());
            System.out.println("Id: " + content.getId());
            System.out.println("Likes: " + content.getLikes());
            System.out.println("Message: " + content.getMessage());
            System.out.println("MessageTags: " + content.getMessageTags());
            System.out.println("Name: " + content.getName());
            System.out.println("Place: " + content.getPlace());
            System.out.println("SharesCount: " + content.getSharesCount());
            System.out.println("Type: " + content.getType());
            System.out.println("-------------------");

        }

    }

}
