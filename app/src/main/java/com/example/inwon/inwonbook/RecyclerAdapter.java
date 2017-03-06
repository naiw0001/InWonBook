package com.example.inwon.inwonbook;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by inwon on 2017-02-20.
 */

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder>{
    Context context;
    List<Item> items;
    int item_layout;

    public RecyclerAdapter(Context context, List<Item> items,int item_layout){
        this.context = context;
        this.items = items;
        this.item_layout = item_layout;
    }

    @Override
    public RecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_cardview, null);
        return new ViewHolder(v);
    }


    @Override
    public void onBindViewHolder(final RecyclerAdapter.ViewHolder holder, final int position) {
        final Item item = items.get(position);
        Bitmap bitmap = item.getImage();
        holder.text_nick.setText(item.getNick());
        holder.imageView.setImageBitmap(bitmap);
        if(bitmap == null){
            holder.imageView.setVisibility(View.GONE);
        }else{
            holder.imageView.setVisibility(View.VISIBLE);
        }
        holder.text_write.setText(item.getWrite());
        if(new Good_Count(String.valueOf(position)).getgood_count().equals(null)){
            holder.good.setText("좋아요: 0개");
        }else {
            holder.good.setText("좋아요: " + item.getGc() + "개");
        }
        holder.comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("adpater_position", String.valueOf(position));
                ViewActivity.slidlayout(position,1);
            }
        });
        holder.good.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Good_Count good_count = new Good_Count(String.valueOf(position));
                if(good_count.getgood_count().equals(null)){
                    good_count.insert_goot_count();
                }else {
                    good_count.update_good_count();
                    String gc = item.getGc();
                    int igc = Integer.parseInt(gc);
                    holder.good.setText("좋아요: "+(igc+1)+"개");
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return this.items.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        ImageView imageView;
        TextView text_write,text_nick;
        Button good,comment;

        public ViewHolder(View itemView) {
            super(itemView);
            imageView = (ImageView)itemView.findViewById(R.id.image);
            text_write = (TextView) itemView.findViewById(R.id.text_write);
            text_nick = (TextView)itemView.findViewById(R.id.text_nick);
            good = (Button)itemView.findViewById(R.id.good);
            comment = (Button)itemView.findViewById(R.id.comment);
        }
    }

}
