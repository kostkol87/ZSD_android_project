package com.example.kostya.test;

import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;


/**
 * Created by Kostya on 15.05.2015.
 */
public class CabinetLogin {

    private final static String VSG_STR = "<input type=\"hidden\" name=\"__VIEWSTATEGENERATOR\" id=\"__VIEWSTATEGENERATOR\" value=\"";
    private final static String VS_STR = "<input type=\"hidden\" name=\"__VIEWSTATE\" id=\"__VIEWSTATE\" value=\"";
    private final static String EV_STR = "<input type=\"hidden\" name=\"__EVENTVALIDATION\" id=\"__EVENTVALIDATION\" value=\"";

    public static String cabinet_login(String uid, String pwd) throws Exception {
        String responseStr = "";
        URL cookieURL = new URL("https://cabinet.nch-spb.com/");
        HttpURLConnection coocon = (HttpURLConnection) cookieURL.openConnection();

        //save cookies
        String tmpCooka = coocon.getHeaderField("Set-Cookie");
        String cooka = tmpCooka.substring(0, tmpCooka.indexOf(";"));


        //save hidden inputs values
        Map<String, String> hidden_map = new HashMap<>();
        String inputLine;
        String login_page = "";

        InputStream ins = coocon.getInputStream();
        InputStreamReader isr = new InputStreamReader(ins);
        BufferedReader in = new BufferedReader(isr);

        while ((inputLine = in.readLine()) != null) {
            login_page += inputLine + "\n";
            if (inputLine.contains(VSG_STR)) {
                String tmp = inputLine.substring(inputLine.indexOf(VSG_STR) + VSG_STR.length());
                String VSG = tmp.substring(0, tmp.indexOf("\""));
                hidden_map.put("__VIEWSTATEGENERATOR", VSG);
            }
            if (inputLine.contains(VS_STR)) {
                String tmp = inputLine.substring(inputLine.indexOf(VS_STR) + VS_STR.length());
                String VS = tmp.substring(0, tmp.indexOf("\""));
                hidden_map.put("__VIEWSTATE", VS);
            }
            if (inputLine.contains(EV_STR)) {
                String tmp = inputLine.substring(inputLine.indexOf(EV_STR) + EV_STR.length());
                String EV = tmp.substring(0, tmp.indexOf("\""));
                hidden_map.put("__EVENTVALIDATION", EV);
            }
        }

        String getValues = URLEncoder.encode("__EVENTTARGET", "UTF-8") + "=" + URLEncoder.encode("", "UTF-8");
        getValues += "&" + URLEncoder.encode("__EVENTARGUMENT", "UTF-8") + "=" + URLEncoder.encode("", "UTF-8");
        getValues += "&" + URLEncoder.encode("__VIEWSTATE", "UTF-8") + "=" + URLEncoder.encode(hidden_map.get("__VIEWSTATE"), "UTF-8");
        getValues += "&" + URLEncoder.encode("__VIEWSTATEGENERATOR", "UTF-8") + "=" + URLEncoder.encode(hidden_map.get("__VIEWSTATEGENERATOR"), "UTF-8");
        getValues += "&" + URLEncoder.encode("__EVENTVALIDATION", "UTF-8") + "=" + URLEncoder.encode(hidden_map.get("__EVENTVALIDATION"), "UTF-8");
        getValues += "&" + URLEncoder.encode("hideLoginError", "UTF-8") + "=" + URLEncoder.encode("", "UTF-8");
        getValues += "&" + URLEncoder.encode("txtUsername", "UTF-8") + "=" + URLEncoder.encode(uid, "UTF-8");
        getValues += "&" + URLEncoder.encode("txtPassword", "UTF-8") + "=" + URLEncoder.encode(pwd, "UTF-8");
        getValues += "&" + URLEncoder.encode("btnLogin.x", "UTF-8") + "=" + URLEncoder.encode("0", "UTF-8");
        getValues += "&" + URLEncoder.encode("btnLogin.y", "UTF-8") + "=" + URLEncoder.encode("0", "UTF-8");

        String baseURL = "https://cabinet.nch-spb.com";
        String httpsURL = baseURL + "?" + getValues;

        HttpClient client = new DefaultHttpClient();
        HttpGet get = new HttpGet(httpsURL);
        ResponseHandler<String> responseHandler = new BasicResponseHandler();
        String resp = client.execute(get, responseHandler);
        responseStr = resp;

        return responseStr;
    }

}