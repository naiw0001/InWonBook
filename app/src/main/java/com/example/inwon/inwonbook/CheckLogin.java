package com.example.inwon.inwonbook;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.CookieManager;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Collection;
import java.util.List;

/**
 * Created by inwon on 2017-02-16.
 */

public class CheckLogin extends AsyncTask<String,Void,String>{

    private String url = "http://1.224.44.55/inwonbook_login.php";
    private String data;
    private String id,pw;
    private StringBuilder json = new StringBuilder();
    private String result;
    public static String nick = "";

    public CheckLogin(String id, String pw){
        this.id = id;
        this.pw = pw;
    }

    @Override
    protected String doInBackground(String... params) {
        try {
            URL url = new URL(this.url);
            HttpURLConnection conn = (HttpURLConnection)url.openConnection();
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);
            conn.setDoInput(true);
            data = URLEncoder.encode("id","UTF-8") +"="+URLEncoder.encode(id,"UTF-8");
            data += "&"+URLEncoder.encode("pw","UTF-8") +"="+URLEncoder.encode(pw,"UTF-8");
            OutputStreamWriter out = new OutputStreamWriter(conn.getOutputStream());
            out.write(data);
            out.flush();
            out.close();
            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(),"UTF-8"));
            while(true){
                String line = br.readLine();
                if(line == null) break;
                json.append(line + "\n");
            }
            br.close();
            result = login(json.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public String login(String json){
        String result=null;
        String _nick = null;
        try{
            JSONArray ja = new JSONArray(json);
            for(int i=0; i<ja.length();i++){
                JSONObject jo = ja.getJSONObject(i);
                result = jo.getString("result");
                _nick = jo.getString("nick");
            }
        }catch (Exception e){}

        if(result.equals("1"))nick = _nick;

        return result;
    }
}
