package com.example.inwon.inwonbook.Friend_Relationship;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;

import org.json.JSONArray;
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
 * Created by inwon on 2017-03-08.
 */

public class Member_DB{

    public ArrayList bring_member(){
        ArrayList member = new ArrayList();
    class Bring extends AsyncTask<String,Void,ArrayList>{
        @Override
        protected ArrayList doInBackground(String... params) {
            String link = params[0];
            StringBuilder json = new StringBuilder();
            try{
                URL url = new URL(link);
                HttpURLConnection conn = (HttpURLConnection)url.openConnection();
                conn.setDoInput(true);
                BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(),"UTF-8"));
                while(true){
                    String line = br.readLine();
                    if(line == null)break;
                    json.append(line+"\n");
                }
                br.close();
            }catch (Exception e){}
            ArrayList nick = getJson(json.toString());
            return nick;
        }
      }
        try {
            member = new Bring().execute("http://1.224.44.55/inwonbook_member_select.php").get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return member;
    }
    private ArrayList getJson(String json){
        ArrayList<String> nick_arr = new ArrayList<>();
        try {
            JSONArray ja = new JSONArray(json);
            for(int i=0; i<ja.length();i++){
                JSONObject jo = ja.getJSONObject(i);
                String nickname = jo.getString("nickname");
                nick_arr.add(nickname);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return nick_arr;

    }
    public void friendapply(String fm, String io, String tm){
        class Apply extends AsyncTask<String,Void,Void>{
            @Override
            protected Void doInBackground(String... params) {
                String from_member = params[0];
                String isokay = params[1];
                String to_member = params[2];
                String link = "http://1.224.44.55/inwonbook_friend_apply.php";
                String data;
                try{
                    URL url = new URL(link);
                    HttpURLConnection conn = (HttpURLConnection)url.openConnection();
                    conn.setDoOutput(true);
                    conn.setDoInput(true);
                    data = URLEncoder.encode("from_member","UTF-8")+"="+URLEncoder.encode(from_member,"UTF-8");
                    data += "&"+ URLEncoder.encode("isokay","UTF-8")+"="+URLEncoder.encode(isokay,"UTF-8");
                    data += "&"+URLEncoder.encode("to_member","UTF-8")+"="+URLEncoder.encode(to_member,"UTF-8");
                    OutputStreamWriter ow = new OutputStreamWriter(conn.getOutputStream());
                    ow.write(data);
                    ow.flush();
                    ow.close();
                    BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                }catch (Exception e){}
                return null;
            }
        }
        new Apply().execute(fm,io,tm);
    }

    public ArrayList getapply(String my_nick){
        ArrayList apply = new ArrayList();
        class Getapply extends AsyncTask<String,Void,ArrayList>{
            @Override
            protected ArrayList doInBackground(String... params) {
                String my_nick = params[0];
                String link = "http://1.224.44.55/inwonbook_get_friendapply.php";
                String data;
                ArrayList f_apply;
                StringBuilder json = new StringBuilder();
                try{
                    URL url = new URL(link);
                    HttpURLConnection conn = (HttpURLConnection)url.openConnection();
                    conn.setDoOutput(true);
                    conn.setDoInput(true);
                    data = URLEncoder.encode("my_nick","UTF-8")+"="+URLEncoder.encode(my_nick,"UTF-8");
                    OutputStreamWriter ow = new OutputStreamWriter(conn.getOutputStream());
                    ow.write(data);
                    ow.flush();
                    ow.close();
                    BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    while(true){
                        String line = br.readLine();
                        if(line==null)break;
                        json.append(line+"\n");
                    }
                    br.close();
                }catch (Exception e){}

                f_apply = get_apply(json.toString());
                return f_apply;
            }
        }
        try {
            apply = new Getapply().execute(my_nick).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return apply;
    }
    private ArrayList get_apply(String json){
        ArrayList apply = new ArrayList();
        try{
            JSONArray ja = new JSONArray(json);

            for(int i=0; i<ja.length();i++){
                JSONObject jo = ja.getJSONObject(i);
                String fm = jo.getString("from_member");
                String io = jo.getString("isokay");
                String tm = jo.getString("to_member");
                apply.add(fm);
                apply.add(io);
                apply.add(tm);
            }
        }catch (Exception e){}

        return apply;
    }

    public void friend_ok(){
        class Ok extends AsyncTask<String,Void,String>{
            @Override
            protected String doInBackground(String... params) {
                String link = params[0];
                String uri = link+"?ok=1";
                try{
                    URL url = new URL(uri);
                    HttpURLConnection conn = (HttpURLConnection)url.openConnection();
                    conn.setDoInput(true);
                    conn.setDoOutput(true);
                }catch (Exception e){}
                return null;
            }
        }
    }

    public void putfriend(){
       class Friend extends AsyncTask<Void,Void,Void>{

           @Override
           protected Void doInBackground(Void... params) {

               return null;

           }
       }
    }


}
