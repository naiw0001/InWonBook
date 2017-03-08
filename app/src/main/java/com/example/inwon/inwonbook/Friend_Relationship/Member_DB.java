package com.example.inwon.inwonbook.Friend_Relationship;

import android.os.AsyncTask;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by inwon on 2017-03-08.
 */

public class Member_DB{

    public void bring_member(){
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
      new Bring().execute("http://1.224.44.55/inwonbook_member_select.php");
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

}
