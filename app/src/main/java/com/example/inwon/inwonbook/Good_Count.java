package com.example.inwon.inwonbook;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

/**
 * Created by inwon on 2017-02-23.
 */

public class Good_Count{
    String idx;

    public Good_Count(String idx){
        this.idx = idx;
    }

    public void update_good_count(){

        class update_gc extends AsyncTask<String,Void,ArrayList>{
            @Override
            protected ArrayList doInBackground(String... params) {
                String idx = params[0];
                String link = "http://1.224.44.55/inwonbook_update_good.php?idx="+idx;
                try {
                    URL url = new URL(link);
                    HttpURLConnection conn = (HttpURLConnection)url.openConnection();
                    conn.setDoInput(true);
                    conn.setDoOutput(true);
                    BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    String re = br.readLine();
                }catch (Exception e){}
                return null;
            }

        }
        new update_gc().execute(idx);
    }

    public ArrayList getgood_count(){
        ArrayList good_list = new ArrayList();
        class getgood extends AsyncTask<String,Void,ArrayList>{
            @Override
            protected ArrayList doInBackground(String... params) {
                String idx = params[0];
                String link = "http://1.224.44.55/inwonbook_select_good.php?idx="+idx;
                StringBuilder json = new StringBuilder();
                try {
                    URL url = new URL(link);
                    HttpURLConnection conn = (HttpURLConnection)url.openConnection();
                    conn.setDoInput(true);
                    BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    while(true){
                        String line = br.readLine();
                        if(line == null)break;
                        json.append(line+"\n");
                    }
                    br.close();
                }catch (Exception c){}

                ArrayList result;
                result = getjson(json.toString());
                return result;
            }

            private ArrayList getjson(String json){
                String write_idx,gc;
                ArrayList list = new ArrayList();
                try {
                    JSONArray ja = new JSONArray(json);
                    for(int i=0;i<ja.length();i++){
                        JSONObject jo = ja.getJSONObject(i);
                        write_idx = jo.getString("write_idx");
                        gc = jo.getString("good_count");
                        list.add(write_idx);
                        list.add(gc);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return list;
            }
        }
        try {
            good_list = new getgood().execute(idx).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return good_list;
    }
}
