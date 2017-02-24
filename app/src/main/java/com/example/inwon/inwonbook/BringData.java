package com.example.inwon.inwonbook;

import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ArrayAdapter;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by inwon on 2017-02-20.
 */

public class BringData extends AsyncTask<String, Void, ArrayList> {

            @Override
            protected ArrayList<String> doInBackground(String... params) {
                String link = params[0];
                ArrayList<String> array = new ArrayList<>();
                StringBuilder json = new StringBuilder();
                try {
                    URL url = new URL(link);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setDoOutput(true);
                    conn.setDoInput(true);

                    BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
                    while (true) {
                        String line = br.readLine();
                        if (line == null) break;
                        json.append(line + "\n");
                    }
                    br.close();
                    array = bringdata(json.toString());

                } catch (Exception e) {
                    e.printStackTrace();
                }
                return array;
            }

            public ArrayList<String> bringdata(String json) {
                ArrayList list = new ArrayList();
                String nick,write,img;
                String idx;
                try {
                    JSONArray ja = new JSONArray(json);
                    for (int i = 0; i < ja.length(); i++) {
                        JSONObject jo = ja.getJSONObject(i);
                        idx = jo.getString("idx");
                        nick = jo.getString("nick");
                        write = jo.getString("write");
                        img = jo.getString("img");
                        list.add(idx);
                        list.add(nick);
                        list.add(write);
                        list.add(img);
                    }
                } catch (Exception e) {
                }
                return list;
            }

        }


