package com.example.inwon.inwonbook;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;

/**
 * Created by inwon on 2017-02-20.
 */

public class Item {
    Bitmap image;
    String write;
    String nick;

    Bitmap getImage(){
        return this.image;
    }
    String getWrite(){
        return this.write;
    }
    String getNick(){return this.nick;}
    Item(String nick,Bitmap image, String write){
        this.image = image;
        this.write = write;
        this.nick = nick;
    }
}
