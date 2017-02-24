package com.example.inwon.inwonbook;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.concurrent.ExecutionException;

/**
 * Created by inwon on 2017-02-16.
 */

public class LoginActivity extends AppCompatActivity{
    private EditText id_edit,pw_edit;
    private String id,pw;
    private CheckLogin checkLogin;
    private AlertDialog.Builder joindialog;
    private String check;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        id_edit = (EditText)findViewById(R.id.edit_id);
        pw_edit = (EditText)findViewById(R.id.edit_pw);

        if(ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED){

            if(ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.WRITE_EXTERNAL_STORAGE)){

            }else {
                ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},1);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case 1: {
                if(grantResults.length>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED){

                }else {}
            }
            return;
        }
    }

    public void btnClick(View v){

        switch (v.getId()) {
            case R.id.login_btn:
                id = id_edit.getText().toString();
                pw = pw_edit.getText().toString();
                checkLogin = new CheckLogin(id,pw);
                try {
                    check = checkLogin.execute().get();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }

                if(check.equals("1")){// login success
                    Intent intent = new Intent(LoginActivity.this,ViewActivity.class);
                    startActivity(intent);
                    finish();
                }else if(check.equals("0")){// login fail
                    Toast.makeText(getApplicationContext(),"아이디 또는 비밀번호가 일치하지않습니다.",Toast.LENGTH_SHORT).show();
                }

                break;
            case R.id.join_btn:
                joindialog = new AlertDialog.Builder(this);
                joindialog.setTitle("회원가입");
                LayoutInflater inflater = (LayoutInflater)getSystemService(LAYOUT_INFLATER_SERVICE);
                final View view = inflater.inflate(R.layout.joindialog,null);
                joindialog.setView(view);
                joindialog.setNegativeButton("취소",null);
                joindialog.setPositiveButton("가입", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        EditText join_id = (EditText)view.findViewById(R.id.join_id);
                        EditText join_pw = (EditText)view.findViewById(R.id.join_pw);
                        EditText join_name = (EditText)view.findViewById(R.id.join_name);
                        EditText join_nick = (EditText)view.findViewById(R.id.join_nick);
                        String id = join_id.getText().toString();
                        String pw = join_pw.getText().toString();
                        String name = join_name.getText().toString();
                        String nick = join_nick.getText().toString();
                        join(id,pw,name,nick);
                        Toast.makeText(getApplicationContext(),"회원가입이 완료되었습니다.",Toast.LENGTH_SHORT).show();
                    }
                });
                joindialog.show();
                break;


        }
    }


    public void join(String id, String pw, String name, String nick){
        String url = "http://1.224.44.55/inwonbook_member.php";
        Log.d("url",url);
        class JoinTask extends AsyncTask<String,String,String>{

            @Override
            protected String doInBackground(String... params) {
                String url = params[0];
                String id = params[1];
                String pw = params[2];
                String name = params[3];
                String nick = params[4];
                String data;

                try{
                    URL join_url = new URL(url);
                    HttpURLConnection conn = (HttpURLConnection)join_url.openConnection();
                    conn.setRequestMethod("POST");
                    conn.setDoOutput(true);
                    data = URLEncoder.encode("id","UTF-8") +"="+ URLEncoder.encode(id,"UTF-8");
                    data += "&"+URLEncoder.encode("pw","UTF-8") +"="+ URLEncoder.encode(pw,"UTF-8");
                    data += "&"+URLEncoder.encode("name","UTF-8") +"="+ URLEncoder.encode(name,"UTF-8");
                    data += "&"+URLEncoder.encode("nick","UTF-8") +"="+ URLEncoder.encode(nick,"UTF-8");
                    OutputStreamWriter out = new OutputStreamWriter(conn.getOutputStream());
                    out.write(data);
                    out.flush();
                    out.close();

                    BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    String re = br.readLine();
                    return re;

                }catch (Exception e){}


                return null;
            }

        }
        new JoinTask().execute(url,id,pw,name,nick);
    }

}
