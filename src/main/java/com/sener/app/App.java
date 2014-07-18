package com.sener.app;

import facebook4j.Facebook;
import facebook4j.FacebookException;
import facebook4j.FacebookFactory;

import facebook4j.conf.Configuration;
import facebook4j.conf.ConfigurationBuilder;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

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

        // Excel-Workbook mit Tabellenblätter vorbereiten
        HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFSheet sheetPost = workbook.createSheet("Posts");
        HSSFSheet sheetComment = workbook.createSheet("Comments");
        HSSFSheet sheetFeed = workbook.createSheet("Feeds");

        // Suchstring
        String searchPage = "BMW";

        // Methodenaufruf um Posts zu erhalten
        Posts.getFacebookPosts(Facebook, searchPage, sheetPost, sheetComment, workbook);

        // Methodenaufruf um Feeds zu erhalten
        Feeds.getFacebookFeed(Facebook, searchPage);

        System.out.println("Completed Successful!");

    }
}