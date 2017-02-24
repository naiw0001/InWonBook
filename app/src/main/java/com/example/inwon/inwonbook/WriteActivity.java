package com.example.inwon.inwonbook;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

/**
 * Created by inwon on 2017-02-20.
 */

public class WriteActivity extends AppCompatActivity{
    private EditText write;
    private Button addimg,write_ok;
    private String text;
    private String path,name;
    private int isaddimg=0;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write);
        write = (EditText)findViewById(R.id.edit_wirte);

    }
    //onClick
    public void write_btn(View v){
        switch (v.getId()) {
            case R.id.addimg:
                //Gallery intent
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
                intent.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent,1);
                break;
            case R.id.write_ok:
                Intent intent1 = new Intent(WriteActivity.this,UploadActivity.class);
                if(isaddimg == 1){
                    intent1.putExtra("path",path);
                    intent1.putExtra("name",name);
                }else if(isaddimg == 0){
                    intent1.putExtra("path","no");
                    intent1.putExtra("name","no");
                }
                text = write.getText().toString();
                intent1.putExtra("text",text);
                startActivity(intent1);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == RESULT_OK){
            if(requestCode == 1){
                Uri uri = data.getData();
                 path = getPath(uri);
                 name = getName(uri);
                if(!path.equals(null)){
                    isaddimg = 1;
                }
            }
        }
    }

    /**
     *
     * @param uri
     * @return
     * 이미지 파일 경로 불러오기
     */
    public String getPath(Uri uri) {
        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = getContentResolver().query(uri, projection, null, null, null);
        int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(columnIndex);
    }

    /**
     *
     * @param uri
     * @return
     * 이미지 파일명 가져오기
     */
    private String getName(Uri uri)
    {
        String[] projection = { MediaStore.Images.ImageColumns.DISPLAY_NAME };
        Cursor cursor = getContentResolver().query(uri, projection, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.ImageColumns.DISPLAY_NAME);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }


}

