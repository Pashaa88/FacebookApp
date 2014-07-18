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

		// Zugang zu Facebook mit Token erstellen
        // Platzhalter für Token setzen vor dem Hochladen zur eigenen Sicherheit: ("*************")
		ConfigurationBuilder configurationBuilder = new ConfigurationBuilder();
		configurationBuilder.setDebugEnabled(true);
        configurationBuilder.setOAuthAppId("*************");
        configurationBuilder.setOAuthAppSecret("*************");
        configurationBuilder.setOAuthAccessToken("*************");
		configurationBuilder.setOAuthPermissions("email, publish_stream, id, name, first_name, last_name, read_stream , generic, gender");
		configurationBuilder.setUseSSL(true);
		configurationBuilder.setJSONStoreEnabled(true);

		// Konfiguration erstellen und neue Instanz zu Facebook setzen
		Configuration configuration = configurationBuilder.build();
		FacebookFactory ff = new FacebookFactory(configuration);
		Facebook Facebook = ff.getInstance();

		try {
			// Suchstring
			String searchPost = "BMW";

            // Ergebnisdatei generieren
            Date date = new Date();
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy.MM.dd-hh_mm");
			String fileName = "/Users/karatee/Documents/Hochschule Reutlingen/Master/3. Semester/Jahresprojekt/Facebook4J/Ergebnis/" + searchPost + "_" + simpleDateFormat.format(date) + ".xls";

            // Methodenaufruf um Posts zu suchen
            String results = getFacebookPosts(Facebook, searchPost);

            // Ergebnisse in Datei schreiben
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

	// Methode um Posts zu suchen (später in eine eigene Java-Klasse übergeben)
	public static String getFacebookPosts(Facebook Facebook, String searchPost) throws FacebookException {

        // Deklarationen
        Cell cellPost, cellComment;
        Row rowPost, rowComment;
        int rowNumPost, rowNumComment;
        int cellNumPost, cellNumComment;

        // Excel-Workbook mit Tabellenblätter vorbereiten
        HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFSheet sheetPost = workbook.createSheet("Posts");
        HSSFSheet sheetComment = workbook.createSheet("Comments");

        // Posts zur Suchseite finden
        String searchResult = "Item : " + searchPost + "\n";
        StringBuffer searchMessage = new StringBuffer();
        ResponseList<Post> results = Facebook.getPosts(searchPost);

        //Pagination http://facebook4j.org/en/code-examples.html

        rowNumPost = 0;
        rowNumComment = 0;

        // Für jeden Post
        for (Post post : results) {

            rowPost = sheetPost.createRow(rowNumPost++);
            rowComment = sheetComment.createRow(rowNumComment++);

            cellNumPost = 0;
            cellNumComment = 0;

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

            // Dummy
            searchMessage.append(post.getMessage() + "\n");

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

                    // Dummy
                    searchMessage.append(post.getComments().get(j).getFrom().getId() + ", ");
                    searchMessage.append(post.getComments().get(j).getFrom().getName() + ", ");
                    searchMessage.append(post.getComments().get(j).getMessage() + ", ");
                    searchMessage.append(post.getComments().get(j).getCreatedTime() + ", ");
                    searchMessage.append(post.getComments().get(j).getLikeCount() + "\n");

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

        // Dummy
        String feedString = getFacebookFeed(Facebook, searchPost);
        searchResult = searchResult + searchMessage.toString();
        searchResult = searchResult + feedString;

        return searchResult;
    }

	// Methode um Facebookfeeds zum Suchstring zu erhalten
	public static String getFacebookFeed(Facebook Facebook, String searchPost)
			throws FacebookException {
		String searchResult = "";
		StringBuffer searchMessage = new StringBuffer();
		ResponseList<Post> results = Facebook.getFeed(searchPost);
		for (Post post : results) {
			//System.out.println(post.getMessage());
			// Dummy
            searchMessage.append(post.getFrom().getName() + ", ");
			searchMessage.append(post.getMessage() + ", ");
			searchMessage.append(post.getCreatedTime() + "\n");
		}
        // Dummy
		searchResult = searchResult + searchMessage.toString();
		return searchResult;
	}

	// Methode um Stringwerte in Json-Objekte umzuwandeln
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