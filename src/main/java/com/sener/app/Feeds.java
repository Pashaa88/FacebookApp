package com.sener.app;

import facebook4j.Facebook;
import facebook4j.FacebookException;
import facebook4j.Post;
import facebook4j.ResponseList;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;

public class Feeds {

    public static void getFacebookFeed(Facebook Facebook, String searchPost, HSSFSheet sheetComment, HSSFWorkbook workbook) throws FacebookException {

        // Deklarationen
        Cell cellFeed;
        Row rowFeed;
        int rowNumFeed;
        int cellNumFeed;

        //searchPost = "";
        ResponseList<Post> results = Facebook.getFeed(searchPost);
        for (int i = 0; i < results.size(); i++) {

            System.out.println(results.get(i).getFrom().getName());
            System.out.println(results.get(i).getMessage());
            System.out.println(results.get(i).getCreatedTime());
            System.out.println(results.get(i).getLikes().getPaging());
            System.out.println("---------------------------");
        }
    }
}
