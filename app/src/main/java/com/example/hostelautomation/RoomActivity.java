package com.example.hostelautomation;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Pair;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.example.hostelautomation.Adapter.ApplianceAdapter;
import com.example.hostelautomation.Dialog.CustomDialog;

import java.util.ArrayList;

public class RoomActivity extends AppCompatActivity {

    private static final String PREFS_NAME = "AppliancesStates";
    private static final String SEPERATOR = "/";

    private static final Integer TV = 1;
    private static final Integer FAN = 2;
    private static final Integer LIGHT = 3;
    private static final Integer CHANDELIER = 4;
    private static final Integer SOCKET = 5;

    private float mDownX;
    private float mDownY;
    private final float SCROLL_THRESHOLD = 10;
    private boolean isOnClick;

    RecyclerView recyclerView;
    ArrayList<Pair<Pair<Pair<String, Integer>, String>, Integer>> list;
    RecyclerView.LayoutManager RecyclerViewLayoutManager;
    ApplianceAdapter RecyclerViewHorizontalAdapter;
    LinearLayoutManager HorizontalLayout ;
    View ChildView ;
    int RecyclerViewItemPosition ;

    ImageView back;
    RelativeLayout roomRelativeLayout;
    TextView roomName;

    WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room);

        recyclerView = (RecyclerView) findViewById(R.id.roomAppliancesRecycler);
        back = (ImageView) findViewById(R.id.back_arrow);
        roomRelativeLayout = (RelativeLayout) findViewById(R.id.roomRelativeLayout);
        roomName = (TextView) findViewById(R.id.room);
        webView = (WebView) findViewById(R.id.webView);

        Intent intent = getIntent();
        String room = intent.getStringExtra("room_name");
        roomName.setText(room);

        final String ip = "255.255.255.0";
        setBackground(room);

        RecyclerViewLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(RecyclerViewLayoutManager);

        // Adding items to recycler view
        list = new ArrayList<>();
        LoadAppliances loadAppliances = new LoadAppliances();
        loadAppliances.execute(room);

        RecyclerViewHorizontalAdapter = new ApplianceAdapter(list);

        HorizontalLayout = new LinearLayoutManager(RoomActivity.this, LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(HorizontalLayout);

        recyclerView.setAdapter(RecyclerViewHorizontalAdapter);

        recyclerView.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {

            GestureDetector gestureDetector = new GestureDetector(RoomActivity.this, new GestureDetector.SimpleOnGestureListener() {

                @Override public boolean onSingleTapUp(MotionEvent motionEvent) {
                    return true;
                }

                @Override  public void onLongPress(MotionEvent e) {
                    return;
                }

            });

            @Override
            public boolean onInterceptTouchEvent(@NonNull RecyclerView recyclerView, @NonNull MotionEvent motionEvent) {

                ChildView = recyclerView.findChildViewUnder(motionEvent.getX(), motionEvent.getY());

                if(ChildView != null && gestureDetector.onTouchEvent(motionEvent)) {

                    //Getting clicked value.
                    RecyclerViewItemPosition = recyclerView.getChildAdapterPosition(ChildView);
                    String code = list.get(RecyclerViewItemPosition).first.second;

                    SharedPreferences preferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);

                    if(preferences.getBoolean(code, false)) {
                        // Appliance is ON
                        SharedPreferences.Editor editor = getSharedPreferences(PREFS_NAME, MODE_PRIVATE).edit();
                        editor.putBoolean(code, false);
                        editor.apply();
                        webView.loadUrl("http://" + ip + SEPERATOR + code + "off");

                        String appl_name = list.get(RecyclerViewItemPosition).first.first.first;
                        String appl_code = list.get(RecyclerViewItemPosition).first.second;

                        switch (list.get(RecyclerViewItemPosition).second) {
                            case 1:
                                list.add(RecyclerViewItemPosition, new Pair<Pair<Pair<String, Integer>, String>, Integer>(new Pair<Pair<String, Integer>, String>(new Pair<String, Integer>(appl_name, R.drawable.tv_off), appl_code), TV));
                                break;
                            case 2:
                                list.add(RecyclerViewItemPosition, new Pair<Pair<Pair<String, Integer>, String>, Integer>(new Pair<Pair<String, Integer>, String>(new Pair<String, Integer>(appl_name, R.drawable.fan_off), appl_code), FAN));
                                break;
                            case 3:
                                list.add(RecyclerViewItemPosition, new Pair<Pair<Pair<String, Integer>, String>, Integer>(new Pair<Pair<String, Integer>, String>(new Pair<String, Integer>(appl_name, R.drawable.light_bulb_off), appl_code), LIGHT));
                                break;
                            case 4:
                                list.add(RecyclerViewItemPosition, new Pair<Pair<Pair<String, Integer>, String>, Integer>(new Pair<Pair<String, Integer>, String>(new Pair<String, Integer>(appl_name, R.drawable.chandelier_off), appl_code), CHANDELIER));
                                break;
                            case 5:
                                list.add(RecyclerViewItemPosition, new Pair<Pair<Pair<String, Integer>, String>, Integer>(new Pair<Pair<String, Integer>, String>(new Pair<String, Integer>(appl_name, R.drawable.socket_off), appl_code), SOCKET));
                                break;
                            default:
                                break;
                        }

                        list.remove(RecyclerViewItemPosition+1);
                        RecyclerViewHorizontalAdapter.notifyDataSetChanged();
                    }
                    else {
                        // Appliance is OFF
                        SharedPreferences.Editor editor = getSharedPreferences(PREFS_NAME, MODE_PRIVATE).edit();
                        editor.putBoolean(code, true);
                        editor.apply();
                        webView.loadUrl("http://" + ip + SEPERATOR + code + "on");

                        String appl_name = list.get(RecyclerViewItemPosition).first.first.first;
                        String appl_code = list.get(RecyclerViewItemPosition).first.second;

                        switch (list.get(RecyclerViewItemPosition).second) {
                            case 1:
                                list.add(RecyclerViewItemPosition, new Pair<Pair<Pair<String, Integer>, String>, Integer>(new Pair<Pair<String, Integer>, String>(new Pair<String, Integer>(appl_name, R.drawable.tv_on), appl_code), TV));
                                break;
                            case 2:
                                list.add(RecyclerViewItemPosition, new Pair<Pair<Pair<String, Integer>, String>, Integer>(new Pair<Pair<String, Integer>, String>(new Pair<String, Integer>(appl_name, R.drawable.fan_on), appl_code), FAN));
                                break;
                            case 3:
                                list.add(RecyclerViewItemPosition, new Pair<Pair<Pair<String, Integer>, String>, Integer>(new Pair<Pair<String, Integer>, String>(new Pair<String, Integer>(appl_name, R.drawable.light_bulb_on), appl_code), LIGHT));
                                break;
                            case 4:
                                list.add(RecyclerViewItemPosition, new Pair<Pair<Pair<String, Integer>, String>, Integer>(new Pair<Pair<String, Integer>, String>(new Pair<String, Integer>(appl_name, R.drawable.chandelier_on), appl_code), CHANDELIER));
                                break;
                            case 5:
                                list.add(RecyclerViewItemPosition, new Pair<Pair<Pair<String, Integer>, String>, Integer>(new Pair<Pair<String, Integer>, String>(new Pair<String, Integer>(appl_name, R.drawable.socket_on), appl_code), SOCKET));
                                break;
                            default:
                                break;
                        }

                        list.remove(RecyclerViewItemPosition+1);
                        RecyclerViewHorizontalAdapter.notifyDataSetChanged();
                    }
                }
                else {
                    switch (motionEvent.getAction() & MotionEvent.ACTION_MASK) {
                        case MotionEvent.ACTION_DOWN:
                            mDownX = motionEvent.getX();
                            mDownY = motionEvent.getY();
                            isOnClick = true;
                            break;
                        case MotionEvent.ACTION_CANCEL:
                        case MotionEvent.ACTION_UP:
                            if (isOnClick) {
                                RecyclerViewItemPosition = recyclerView.getChildAdapterPosition(ChildView);
                                Integer type = list.get(RecyclerViewItemPosition).second;
                                if(type == 2) {
                                    CustomDialog customDialog = new CustomDialog(RoomActivity.this);
                                    customDialog.show();
                                    customDialog.setCanceledOnTouchOutside(false);
                                }
                            }
                            break;
                        case MotionEvent.ACTION_MOVE:
                            if (isOnClick && (Math.abs(mDownX - motionEvent.getX()) > SCROLL_THRESHOLD || Math.abs(mDownY - motionEvent.getY()) > SCROLL_THRESHOLD)) {
                                isOnClick = false;
                            }
                            break;
                        default:
                            break;
                    }
                }

                return false;
            }

            @Override
            public void onTouchEvent(@NonNull RecyclerView recyclerView, @NonNull MotionEvent event) {

            }

            @Override
            public void onRequestDisallowInterceptTouchEvent(boolean b) {

            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private class LoadAppliances extends AsyncTask<String,Void,Void> {
        @Override
        protected Void doInBackground(String... strings) {
            switch (strings[0]) {
                case "Living Room":
                    addTV("TV", "htv");
                    addFan("Fan", "hf1");
                    addLight("Light", "hl1");
                    addChandelier("Chandelier", "hc");
                    addSocket("Socket", "hc1");
                    break;
                default:
                    addTV("TV", "htv");
                    addFan("Fan", "hf1");
                    addLight("Light", "hl1");
                    addSocket("Socket", "hc1");
                    break;
            }

            return null;
        }
    }

    public void addTV(String name, String code) {
        SharedPreferences preferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        if(preferences.getBoolean(code, false))
            list.add(new Pair<Pair<Pair<String, Integer>, String>, Integer>(new Pair<Pair<String, Integer>, String>(new Pair<String, Integer>(name, R.drawable.tv_on), code),TV));
        else
            list.add(new Pair<Pair<Pair<String, Integer>, String>, Integer>(new Pair<Pair<String, Integer>, String>(new Pair<String, Integer>(name, R.drawable.tv_off), code), TV));
    }

    public void addFan(String name, String code) {
        SharedPreferences preferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        if(preferences.getBoolean(code, false))
            list.add(new Pair<Pair<Pair<String, Integer>, String>, Integer>(new Pair<Pair<String, Integer>, String>(new Pair<String, Integer>(name, R.drawable.fan_on), code), FAN));
        else
            list.add(new Pair<Pair<Pair<String, Integer>, String>, Integer>(new Pair<Pair<String, Integer>, String>(new Pair<String, Integer>(name, R.drawable.fan_off), code), FAN));
    }

    public void addLight(String name, String code) {
        SharedPreferences preferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        if(preferences.getBoolean(code, false))
            list.add(new Pair<Pair<Pair<String, Integer>, String>, Integer>(new Pair<Pair<String, Integer>, String>(new Pair<String, Integer>(name, R.drawable.light_bulb_on), code), LIGHT));
        else
            list.add(new Pair<Pair<Pair<String, Integer>, String>, Integer>(new Pair<Pair<String, Integer>, String>(new Pair<String, Integer>(name, R.drawable.light_bulb_off), code), LIGHT));
    }

    public void addChandelier(String name, String code) {
        SharedPreferences preferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        if(preferences.getBoolean(code, false))
            list.add(new Pair<Pair<Pair<String, Integer>, String>, Integer>(new Pair<Pair<String, Integer>, String>(new Pair<String, Integer>(name, R.drawable.chandelier_on), code), CHANDELIER));
        else
            list.add(new Pair<Pair<Pair<String, Integer>, String>, Integer>(new Pair<Pair<String, Integer>, String>(new Pair<String, Integer>(name, R.drawable.chandelier_off), code), CHANDELIER));
    }

    public void addSocket(String name, String code) {
        SharedPreferences preferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        if(preferences.getBoolean(code, false))
            list.add(new Pair<Pair<Pair<String, Integer>, String>, Integer>(new Pair<Pair<String, Integer>, String>(new Pair<String, Integer>(name, R.drawable.socket_on), code), SOCKET));
        else
            list.add(new Pair<Pair<Pair<String, Integer>, String>, Integer>(new Pair<Pair<String, Integer>, String>(new Pair<String, Integer>(name, R.drawable.socket_off), code), SOCKET));
    }

    private void setBackground(String roomname) {
        switch(roomname) {
            case "Living Room":
                Glide.with(this).load(R.drawable.living_room_portrait).into(new SimpleTarget<Drawable>() {
                    @Override
                    public void onResourceReady(Drawable resource, Transition<? super Drawable> transition) {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                            roomRelativeLayout.setBackground(resource);
                        }
                    }
                });
                break;
            case "Master Bedroom":
                Glide.with(this).load(R.drawable.bedroom_portrait).into(new SimpleTarget<Drawable>() {
                    @Override
                    public void onResourceReady(Drawable resource, Transition<? super Drawable> transition) {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                            roomRelativeLayout.setBackground(resource);
                        }
                    }
                });
                break;
            case "Kitchen":
                Glide.with(this).load(R.drawable.kitchen_portrait).into(new SimpleTarget<Drawable>() {
                    @Override
                    public void onResourceReady(Drawable resource, Transition<? super Drawable> transition) {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                            roomRelativeLayout.setBackground(resource);
                        }
                    }
                });
                break;
            default:
                break;
        }
    }
}
