package com.sener.app;

import facebook4j.Facebook;
import facebook4j.FacebookException;
import facebook4j.Post;
import facebook4j.ResponseList;

public class Feeds {

    public static void getFacebookFeed(Facebook Facebook, String searchPost) throws FacebookException {

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
    }
}
