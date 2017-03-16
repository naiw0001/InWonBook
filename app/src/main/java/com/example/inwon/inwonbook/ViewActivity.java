package com.example.inwon.inwonbook;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.inwon.inwonbook.Friend_Relationship.Member_DB;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import static android.view.View.GONE;

/**
 * Created by inwon on 2017-02-20.
 */

public class ViewActivity extends AppCompatActivity {
    private TextView write;
    private Bitmap img_bitmap;
    private ArrayList<String> textarray, fm_arr, io_arr, tm_arr;
    private static ArrayList<String> nick_arr, write_arr, img_arr, idx_arr;
    static private RelativeLayout comment_layout;
    static Animation anim_up, anim_down, anim_right, anim_left;
    private EditText comment;
    private Button send_comment;
    static int val;
    private static ListView list;
    private static ListViewAdapter adapter;
    private static int isslide = 0; // 댓글창 유무
    private int isfriend_layout = 0, isfriend_apply=0;
    private DrawerLayout drawer;
    private ListView member_list, friend_list, friend_apply_list;
    private ArrayAdapter member_adapter, friend_adapter, friend_apply_adapter;
    private ArrayList member;
    private AlertDialog.Builder dialog;
    private ArrayList friendapply;
    private RelativeLayout friend_layout, friend_apply_layout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view);
        write = (TextView) findViewById(R.id.write);
        write.setOnClickListener(writeclick);
        comment_layout = (RelativeLayout) findViewById(R.id.comment_layout);
        textarray = new ArrayList<>();
        idx_arr = new ArrayList<>();
        nick_arr = new ArrayList<>();
        write_arr = new ArrayList<>();
        img_arr = new ArrayList<>();
        anim_up = AnimationUtils.loadAnimation(ViewActivity.this, R.anim.slid_up);
        anim_down = AnimationUtils.loadAnimation(ViewActivity.this, R.anim.slid_down);
        anim_right = AnimationUtils.loadAnimation(ViewActivity.this, R.anim.slid_right);
        anim_left = AnimationUtils.loadAnimation(ViewActivity.this, R.anim.slid_left);

        friend_layout = (RelativeLayout) findViewById(R.id.friend_list_layout);
        list = (ListView) findViewById(R.id.list);
        friend_list = (ListView) findViewById(R.id.friend_list);
        friend_adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1);
        friend_list.setAdapter(friend_adapter);
        friend_apply_list = (ListView)findViewById(R.id.friend_apply_list);
        friend_apply_layout = (RelativeLayout)findViewById(R.id.friend_apply_layout);
        friend_apply_adapter = new ArrayAdapter(this,android.R.layout.simple_list_item_1);
        friend_apply_list.setAdapter(friend_apply_adapter);

        adapter = new ListViewAdapter();
        list.setAdapter(adapter);
        dialog = new AlertDialog.Builder(this);
        comment = (EditText) findViewById(R.id.comment_edit);
        send_comment = (Button) findViewById(R.id.comment_btn);
        drawer = (DrawerLayout) findViewById(R.id.drawer);
        Member_DB md = new Member_DB();
        member = md.bring_member();
        friendapply = md.getapply(CheckLogin.nick);
        fm_arr = new ArrayList<>();
        io_arr = new ArrayList<>();
        tm_arr = new ArrayList<>();

        friendapply_m();

        member_adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, member);
        member_adapter.add("친구 목록");
        member_adapter.add("친구 요청");
        member_list = (ListView) findViewById(R.id.drawer_list);
        member_list.setAdapter(member_adapter);
        member_list.setOnItemClickListener(list_cilck);
        setitem();
        my_friend();
    }

    AdapterView.OnItemClickListener list_cilck = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            final String nickname = member_adapter.getItem(position).toString();
            if (nickname.equals("친구 목록")) {
                my_friend();
                drawer.closeDrawer(Gravity.RIGHT);
                friend_layout.startAnimation(anim_right);
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        friend_layout.setVisibility(View.VISIBLE);
                    }
                }, 200);
                isfriend_layout = 1;
            } else if (nickname.equals("친구 요청")) {
                drawer.closeDrawer(Gravity.RIGHT);
                friend_apply_layout.setVisibility(View.VISIBLE);
                friendapply_m();
                isfriend_apply = 1;
            } else {
                dialog.setTitle(nickname + "님을 추가하시겠습니까?");
                dialog.setPositiveButton("예", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Member_DB md = new Member_DB();
                        md.friendapply(CheckLogin.nick, "요청", nickname);
                        Toast.makeText(ViewActivity.this, "친구 요청을 보냈습니다.", Toast.LENGTH_SHORT).show();
                    }
                });
                dialog.setNegativeButton("아니오", null);
                dialog.show();
            }

        }

    };
        View.OnClickListener writeclick = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ViewActivity.this, WriteActivity.class));
            }
        };

        public void my_friend() {
            for (int i = 0; i < fm_arr.size(); i++) {
                if (fm_arr.get(i).toString().equals(CheckLogin.nick)) {
                    if (io_arr.get(i).toString().equals("수락")) {
                        friend_adapter.add(tm_arr.get(i).toString());
                    }
                } else if (tm_arr.get(i).toString().equals(CheckLogin.nick)) {
                    if (io_arr.get(i).toString().equals("수락")) {
                        friend_adapter.add(fm_arr.get(i).toString());
                    }
                }
            }
            friend_adapter.notifyDataSetChanged();
        }

        public void friendapply_m() {

            for (int i = 0; i < friendapply.size(); ) {
                fm_arr.add(friendapply.get(i).toString());
                io_arr.add(friendapply.get(i + 1).toString());
                tm_arr.add(friendapply.get(i + 2).toString());
                i += 3;
            }

            //친구 요청후 수락/거절
            for (int i = 0; i < tm_arr.size(); i++) {

                if (CheckLogin.nick.equals(tm_arr.get(i).toString())) {
                    if (io_arr.get(i).toString().equals("요청")) {
                        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
                        dialog.setTitle("추가하시겠습니까?");
                        dialog.setPositiveButton("수락", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Member_DB db = new Member_DB();
                                db.friend_ok();
                                Toast.makeText(ViewActivity.this, "수락되었습니다.", Toast.LENGTH_SHORT).show();
                            }
                        });
                        dialog.setNegativeButton("거절", null);
                        dialog.show();
                    }
                }

            }

        }

        // onClick
        public void btn_send_comment(View v) {
            String temp = comment.getText().toString(); // comment
            String link = "http://1.224.44.55/inwonbook_comment_insert.php";
            String nick = CheckLogin.nick;
            String comment_idx = idx_arr.get(val).toString();
            Toast.makeText(getApplicationContext(), "send: " + temp + ", nick: " + nick + " comment_idx: " + comment_idx, Toast.LENGTH_SHORT).show();
            new InsertComment().execute(link, comment_idx, nick, temp);
            comment.setText("");
            slidlayout(val, 0);
            adapter.notifyDataSetChanged();
        }

        //댓글창 down
        public void slidlayout_down() {
            comment_layout.startAnimation(anim_down);
            comment_layout.setVisibility(GONE);
            comment_list.clear();
            isslide = 0;
        }

        private static ArrayList comment_list;

        //댓글창 up
        public static void slidlayout(int position, int anim) {
            isslide = 1;
            comment_list = new ArrayList();
            val = position;
            String temp = idx_arr.get(val).toString();
            String link = "http://1.224.44.55/inwonbook_comment_select.php";
            if (anim == 1) {
                comment_layout.startAnimation(anim_up);
            }
            try {
                comment_list = new Bring_Comment().execute(link, temp).get();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
            viewcomment();
            comment_layout.setVisibility(View.VISIBLE);
        }

        private static void viewcomment() { // commnet
            ArrayList nick, comment;
            adapter.clear();
            nick = new ArrayList();
            comment = new ArrayList();
            for (int i = 0; i < comment_list.size(); ) {
                nick.add(comment_list.get(i + 2).toString());
                comment.add(comment_list.get(i + 3).toString());
                if ((i + 4) == comment_list.size()) {
                    break;
                }
                i += 4;
            }
            for (int i = 0; i < nick.size(); i++) {
                adapter.addItem(nick.get(i).toString(), comment.get(i).toString());
            }
            adapter.notifyDataSetChanged();
        }

        private long backKeyPressedTime = 0;

        @Override
        public void onBackPressed() {
            if (System.currentTimeMillis() > backKeyPressedTime + 2000) {
                backKeyPressedTime = System.currentTimeMillis();
                if (isslide == 1) {
                    slidlayout_down();
                    backKeyPressedTime = 0;
                } else if (isfriend_layout == 1) {
                    friend_layout.startAnimation(anim_left);
                    android.os.Handler handler = new android.os.Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            friend_layout.setVisibility(GONE);
                        }
                    }, 200);
                } else if(isfriend_apply==1) {
                friend_apply_layout.setVisibility(GONE);
               }
                    Toast.makeText(getApplicationContext(), "한번 더 누르면 종료됩니다.", Toast.LENGTH_SHORT).show();
                return;
            }
            if (System.currentTimeMillis() <= backKeyPressedTime + 2000) {
                finish();
            }
        }

        private class Getimg extends AsyncTask<String, Void, Bitmap> {

            @Override
            protected Bitmap doInBackground(String... params) {
                String img = params[0];
                String link = "http://1.224.44.55";
                Bitmap bitmap = null;
                try {
                    URL url = new URL(link + img);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setDoInput(true);
                    conn.setDoOutput(true);
                    conn.connect();
                    InputStream is = conn.getInputStream();
                    bitmap = BitmapFactory.decodeStream(is);

                } catch (Exception e) {
                    e.printStackTrace();
                }
                return bitmap;
            }

        }

        @Override
        public boolean onCreateOptionsMenu(Menu menu) {
            getMenuInflater().inflate(R.menu.menu_main, menu);
            return true;
        }

        @Override
        public boolean onOptionsItemSelected(MenuItem item) {
            switch (item.getItemId()) {
                case R.id.friend:
                    drawer.openDrawer(Gravity.RIGHT);
                    if (drawer.isDrawerOpen(Gravity.RIGHT)) {
                        drawer.closeDrawer(Gravity.RIGHT);
                    }
                    break;
            }
            return false;
        }

        private void setitem() {
            BringData data = new BringData();
            try {
                textarray = data.execute("http://1.224.44.55/inwonbook_select.php").get();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }

            RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerview);
            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
            recyclerView.setHasFixedSize(true);
            recyclerView.setLayoutManager(layoutManager);
            List<Item> items = new ArrayList<>();
            for (int i = 0; i < textarray.size(); ) {
                idx_arr.add(textarray.get(i).toString());
                nick_arr.add(textarray.get(i + 1).toString());
                write_arr.add(textarray.get(i + 2).toString());
                img_arr.add(textarray.get(i + 3).toString());

                if ((i + 4) == textarray.size()) {
                    break;
                }
                i += 4;
            }
            Item[] item = new Item[nick_arr.size()];
            for (int i = 0; i < nick_arr.size(); i++) {
                if (img_arr.get(i).toString().equals("no")) {
                    item[i] = new Item(nick_arr.get(i).toString(), null, write_arr.get(i).toString());
                } else {
                    try {
                        Getimg getimg = new Getimg();
                        img_bitmap = getimg.execute(img_arr.get(i).toString()).get();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    }
                }
                item[i] = new Item(nick_arr.get(i).toString(), img_bitmap, write_arr.get(i).toString());
                Good_Count gc = new Good_Count(String.valueOf(i));
                String good_count = gc.getgood_count();

                item[i].good_count(good_count);

                items.add(item[i]);
            }

            RecyclerAdapter adapter = new RecyclerAdapter(getApplicationContext(), items, R.layout.activity_view);
            recyclerView.setAdapter(adapter);
        }

    }

