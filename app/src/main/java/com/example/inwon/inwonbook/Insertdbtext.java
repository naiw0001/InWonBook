package com.example.inwon.inwonbook;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

/**
 * Created by inwon on 2017-02-20.
 */

public class Insertdbtext extends AsyncTask<String,Void,String>{

    @Override
    protected String doInBackground(String... params) {
        String link = params[0];
        String nick = params[1];
        String write = params[2];
        String img = params[3]; // img 저장 경로 + 이름 /testup/
        String img_path = "/testup/"+img;
        String data;
        try {
            URL url = new URL(link);
            data = URLEncoder.encode("nick","UTF-8")+"="+URLEncoder.encode(nick,"UTF-8");
            data += "&"+URLEncoder.encode("write","UTF-8")+"="+URLEncoder.encode(write,"UTF-8");
            data += "&"+URLEncoder.encode("img","UTF-8")+"="+URLEncoder.encode(img_path,"UTF-8");
            HttpURLConnection conn = (HttpURLConnection)url.openConnection();
            conn.setRequestMethod("POST");
            conn.setDoInput(true);
            conn.setDoOutput(true);
            OutputStreamWriter ow = new OutputStreamWriter(conn.getOutputStream());
            ow.write(data);
            ow.flush();
            ow.close();
            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String re = br.readLine();
            return re;

        } catch (Exception e) {
            e.printStackTrace();
        }


        return null;
    }


}

