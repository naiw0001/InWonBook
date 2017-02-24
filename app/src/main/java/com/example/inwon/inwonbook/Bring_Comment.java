package com.example.inwon.inwonbook;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * Bring Comment
 */

public class Bring_Comment extends AsyncTask<String,Void,ArrayList>{

    @Override
    protected ArrayList doInBackground(String... params) {
        String link = params[0];
        String pos = params[1];
        StringBuilder json = new StringBuilder();
        URL url;
        try {
            url = new URL(link+"?idx="+pos);
            HttpURLConnection conn = (HttpURLConnection)url.openConnection();
            conn.setDoOutput(true);
            conn.setDoInput(true);
            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            while(true){
                String line = br.readLine();
                if(line == null)break;
                json.append(line+"\n");
            }
            br.close();
        }catch (Exception e){}
        ArrayList result = comment_b(json.toString());
        return result;
    }

    private ArrayList comment_b(String json){
        ArrayList list = new ArrayList();
        String idx,write_idx,nick,comment;
        try {
            JSONArray ja = new JSONArray(json);
            for (int i=0; i<ja.length();i++){
                JSONObject jo = ja.getJSONObject(i);
                idx = jo.getString("idx");
                write_idx = jo.getString("write_idx");
                nick = jo.getString("nick");
                comment = jo.getString("comment");
                list.add(idx);
                list.add(write_idx);
                list.add(nick);
                list.add(comment);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;

    }
}
