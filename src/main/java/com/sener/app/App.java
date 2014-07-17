package com.sener.app;

import facebook4j.Facebook;
import facebook4j.FacebookException;
import facebook4j.FacebookFactory;
import facebook4j.Post;
import facebook4j.ResponseList;
import facebook4j.User;
import facebook4j.Paging;

import facebook4j.conf.Configuration;
import facebook4j.conf.ConfigurationBuilder;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;

import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;

public class App {
	
	public static void main(String[] args) throws FacebookException {
		// Create conf builder and set authorization and access keys
		ConfigurationBuilder configurationBuilder = new ConfigurationBuilder();
		configurationBuilder.setDebugEnabled(true);
		configurationBuilder.setOAuthAppId("480341025430254");
		configurationBuilder.setOAuthAppSecret("045dda836dedc6485d59744a4c07f324");
		configurationBuilder.setOAuthAccessToken("CAACEdEose0cBAFpJBs2tl1PuuQhIxtvwAORAGAHUzWNHBmw0XkZCZAwfiWDXboJZAtVaMOvA6o3EEtl7SYmhfpgQOTRckdpvn6juZAhA8H5qZA5RG9RKjfjn0sdR9N5Up8Kyzm4DvZC5srgbnL5g3wVhJCFfAikI9ckwWO4ZB3GRBH6DjZC3JDhT7ZBoW7rdtwqKOAcbZARQk5d2s7bCQzwkEP");
		configurationBuilder.setOAuthPermissions("email, publish_stream, id, name, first_name, last_name, read_stream , generic, gender");
		configurationBuilder.setUseSSL(true);
		configurationBuilder.setJSONStoreEnabled(true);

		// Create configuration and get Facebook instance
		Configuration configuration = configurationBuilder.build();
		FacebookFactory ff = new FacebookFactory(configuration);
		Facebook Facebook = ff.getInstance();

		try {
			// Set search string and get results
			String searchPost = "BMW";
			Date date = new Date();
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy.MM.dd-hh_mm");
			String fileName = "/Users/karatee/Documents/Hochschule Reutlingen/Master/3. Semester/Jahresprojekt/Facebook4J/Ergebnis/" + searchPost + "_" + simpleDateFormat.format(date) + ".xls";
			String results = getFacebookPosts(Facebook, searchPost);
			File file = new File(fileName);
			if (!file.exists()) {
				file.createNewFile();
				FileWriter fw = new FileWriter(file.getAbsoluteFile());
				BufferedWriter bw = new BufferedWriter(fw);
				bw.write(results);
				bw.close();
				System.out.println("Completed");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// This method is used to get Facebook posts based on the search string set
	// above
	public static String getFacebookPosts(Facebook Facebook, String searchPost) throws FacebookException {

        Cell cell, cell2;
        Row row, row2;
        int rownum, rownum2;
        int cellnum, cellnum2;

        HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFSheet sheet = workbook.createSheet("Comments");
        HSSFSheet sheet2 = workbook.createSheet("Posts");

        String searchResult = "Item : " + searchPost + "\n";
        StringBuffer searchMessage = new StringBuffer();
        ResponseList<Post> results = Facebook.getPosts(searchPost);

        //Pagination http://facebook4j.org/en/code-examples.html
        //System.out.println();

        rownum = 0;
        rownum2 = 0;

        for (Post post : results) {

            row = sheet.createRow(rownum++);
            row2 = sheet2.createRow(rownum2++);

            cellnum = 0;
            cellnum2 = 0;

            //System.out.println(post.getMessage());

            cell2 = row2.createCell(cellnum2++);
            cell2.setCellValue(post.getId().toString());

            cell2 = row2.createCell(cellnum2++);
            cell2.setCellValue(post.getMessage().toString());

            cell2 = row2.createCell(cellnum2++);
            cell2.setCellValue(post.getCreatedTime().toString());



            cell2 = row2.createCell(cellnum2++);
            cell2.setCellValue(post.getSharesCount());

            row2 = sheet2.createRow(rownum2++);

            searchMessage.append(post.getMessage() + "\n");
            for (int j = 0; j < post.getComments().size(); j++) {

                row = sheet.createRow(rownum++);
                for (int k = 0; k < 1; k++) {

                    cellnum = 0;

                    cell = row.createCell(cellnum++);
                    cell.setCellValue(post.getComments().get(j).getId());

                    cell = row.createCell(cellnum++);
                    cell.setCellValue(post.getComments().get(j).getFrom().getId().toString());

                    User user = Facebook.getUser(post.getComments().get(j).getFrom().getId());

                    cell = row.createCell(cellnum++);
                    cell.setCellValue(post.getComments().get(j).getFrom().getName().toString());

                    cell = row.createCell(cellnum++);
                    cell.setCellValue(user.getGender());

                    cell = row.createCell(cellnum++);
                    cell.setCellValue(user.getLocale().toString());
                    System.out.println(user.getLocale());

                    String birthday = user.getBirthday();
                    System.out.println(birthday);

                    cell = row.createCell(cellnum++);
                    cell.setCellValue(post.getComments().get(j).getMessage().toString());

                    cell = row.createCell(cellnum++);
                    cell.setCellValue(post.getComments().get(j).getCreatedTime().toString());

                    cell = row.createCell(cellnum++);
                    cell.setCellValue(post.getComments().get(j).getLikeCount().toString());

                    searchMessage.append(post.getComments().get(j).getFrom().getId() + ", ");
                    searchMessage.append(post.getComments().get(j).getFrom().getName() + ", ");

                    searchMessage.append(post.getComments().get(j).getMessage() + ", ");
                    searchMessage.append(post.getComments().get(j).getCreatedTime() + ", ");
                    searchMessage.append(post.getComments().get(j).getLikeCount() + "\n");

                }

            }

            FileOutputStream out = null;
            try {
                out = new FileOutputStream(new File("/Users/karatee/Desktop/test.xls"));
                workbook.write(out);
                out.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }


        }

        String feedString = getFacebookFeed(Facebook, searchPost);
        searchResult = searchResult + searchMessage.toString();
        searchResult = searchResult + feedString;



        return searchResult;
    }

	// This method is used to get Facebook feeds based on the search string set
	// above
	public static String getFacebookFeed(Facebook Facebook, String searchPost)
			throws FacebookException {
		String searchResult = "";
		StringBuffer searchMessage = new StringBuffer();
		ResponseList<Post> results = Facebook.getFeed(searchPost);
		for (Post post : results) {
			//System.out.println(post.getMessage());
			searchMessage.append(post.getFrom().getName() + ", ");
			searchMessage.append(post.getMessage() + ", ");
			searchMessage.append(post.getCreatedTime() + "\n");
		}
		searchResult = searchResult + searchMessage.toString();
		return searchResult;
	}

	// This method is used to create JSON object from data string
	public static String stringToJson(String data) {
		JsonConfig cfg = new JsonConfig();
		try {
			JSONObject jsonObject = JSONObject.fromObject(data, cfg);
			//System.out.println("JSON = " + jsonObject.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "JSON Created";
	}
}