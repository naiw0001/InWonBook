package com.example.inwon.inwonbook;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

/**
 * Created by inwon on 2017-02-23.
 */

public class Good_Count{
    private String idx;

    public Good_Count(String idx){
        this.idx = idx;
    }

    public void update_good_count(){

        class update_gc extends AsyncTask<String,Void,ArrayList>{
            @Override
            protected ArrayList doInBackground(String... params) {
                String idx = params[0];
                String link = "http://1.224.44.55/inwonbook_good_count.php?idx="+idx;
                try {
                    URL url = new URL(link);
                    HttpURLConnection conn = (HttpURLConnection)url.openConnection();
                    conn.setDoInput(true);
                    conn.setDoOutput(true);
                    BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                }catch (Exception e){}
                return null;
            }

        }
        new update_gc().execute(idx);
    }

    public String getgood_count(){
        String good_count=null;
        class getgood extends AsyncTask<String,Void,String>{
            @Override
            protected String doInBackground(String... params) {
                String idx = params[0];
                String link = "http://1.224.44.55/inwonbook_select_good.php?idx="+idx;
                Log.i("link",link);
                Log.i("idx",idx);
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

                String result=null;
                result = getjson(json.toString());

                return result;
            }

            private String getjson(String json){
                String gc=null;
                try {
                    JSONArray ja = new JSONArray(json);
                    for(int i=0;i<ja.length();i++){
                        JSONObject jo = ja.getJSONObject(i);
                        gc = jo.getString("good_count");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return gc;
            }
        }
        try {
            good_count = new getgood().execute(idx).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return good_count;
    }
    public void insert_goot_count(){
        class Insert_gc extends AsyncTask<String,Void,String>{

            @Override
            protected String doInBackground(String... params) {
                String link = "http://1.224.44.55/inwonbook_good_insert.php";
                String data;
                URL url;
                try{
                    url = new URL(link);
                    HttpURLConnection conn = (HttpURLConnection)url.openConnection();
                    conn.setDoOutput(true);
                    data = URLEncoder.encode("write_idx","UTF-8")+"="+URLEncoder.encode(idx,"UTF-8");
                    OutputStreamWriter ow = new OutputStreamWriter(conn.getOutputStream());
                    ow.write(data);
                    ow.flush();
                    ow.close();
                    BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    String line = br.readLine();
                    return line;
                }catch (Exception e){}
                return null;
            }
        }
        new Insert_gc().execute();
    }
}
