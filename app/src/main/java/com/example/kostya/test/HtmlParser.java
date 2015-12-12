package com.example.kostya.test;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * Created by Kostya on 20.05.2015.
 */
public class HtmlParser {
    public static String b_titles;

    public static boolean checkString(String string) {
        if (string == null) return false;
        return string.matches("^-?\\d+$");
    }

    public static String parse(String login, String password) throws Exception {
        String html_response = "";

        html_response = CabinetLogin.cabinet_login(login, password);
        Document doc = Jsoup.parse(html_response);

        Element body = doc.body();
        Elements title = body.getElementsByClass("title");
        try{
            b_titles = title.get(7).text();
        }catch(Exception e){
            b_titles = "Войти не удалось";
        }

        if(checkString(b_titles.substring(0, b_titles.length()-5))){
            b_titles = b_titles.substring(0, b_titles.length()-5);
        }
        return b_titles;
    }
}
