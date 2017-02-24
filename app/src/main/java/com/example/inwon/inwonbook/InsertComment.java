package com.example.inwon.inwonbook;

import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

/**
 * 댓글 Insert
 */

public class InsertComment extends AsyncTask<String,Void,Void>{

    @Override
    protected Void doInBackground(String... params) {
        String link = params[0];
        String comment_idx = params[1];
        String nick = params[2];
        String comment = params[3];
        String data;
        URL url;
        try{
            url = new URL(link);
            data = URLEncoder.encode("comment_idx","UTF-8")+"="+URLEncoder.encode(comment_idx,"UTF-8");
            data += "&"+URLEncoder.encode("nick","UTF-8")+"="+URLEncoder.encode(nick,"UTF-8");
            data += "&"+URLEncoder.encode("comment","UTF-8")+"="+URLEncoder.encode(comment,"UTF-8");
            HttpURLConnection conn = (HttpURLConnection)url.openConnection();
            OutputStreamWriter ow = new OutputStreamWriter(conn.getOutputStream());
            ow.write(data);
            ow.flush();
            ow.close();
            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String re = br.readLine();
        }catch (Exception e){}


        return null;
    }
}

