package com.example.inwon.inwonbook;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by inwon on 2017-02-23.
 */

public class ListViewAdapter extends BaseAdapter{
private ArrayList<Listitem> Listitems = new ArrayList<>();

    public ListViewAdapter(){

    }

    @Override
    public int getCount() {
        return Listitems.size();
    }

    @Override
    public Object getItem(int position) {
        return Listitems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final int pos = position;
        final Context context = parent.getContext();

        if(convertView == null){
            LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.listview_item,parent,false);
        }
        TextView nick = (TextView)convertView.findViewById(R.id.item_nick);
        TextView comment = (TextView)convertView.findViewById(R.id.item_comment);

        return convertView;
    }
    public void addItem(String nick, String comment) {
        Listitem Listitem = new Listitem();
        Listitem.setNick(nick);
        Listitem.setComment(comment);

        Listitems.add(Listitem);
    }
}
