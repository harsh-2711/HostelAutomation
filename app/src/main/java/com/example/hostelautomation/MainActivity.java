package com.example.hostelautomation;

import android.content.Intent;
import android.os.AsyncTask;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.hostelautomation.Adapter.RoomsAdapter;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    ArrayList<Pair<String, Integer>> list;
    RecyclerView.LayoutManager RecyclerViewLayoutManager;
    RoomsAdapter RecyclerViewHorizontalAdapter;
    LinearLayoutManager HorizontalLayout ;
    View ChildView ;
    int RecyclerViewItemPosition ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = (RecyclerView) findViewById(R.id.roomsRecycler);
        RecyclerViewLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(RecyclerViewLayoutManager);

        // Adding items to recycler view
        list = new ArrayList<>();
        AddItemsToRecyclerView addItemsToRecyclerView = new AddItemsToRecyclerView();
        addItemsToRecyclerView.execute();

        RecyclerViewHorizontalAdapter = new RoomsAdapter(list);

        HorizontalLayout = new LinearLayoutManager(MainActivity.this, LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(HorizontalLayout);

        recyclerView.setAdapter(RecyclerViewHorizontalAdapter);

        recyclerView.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {

            GestureDetector gestureDetector = new GestureDetector(MainActivity.this, new GestureDetector.SimpleOnGestureListener() {

                @Override public boolean onSingleTapUp(MotionEvent motionEvent) {

                    return true;
                }

            });

            @Override
            public boolean onInterceptTouchEvent(@NonNull RecyclerView recyclerView, @NonNull MotionEvent motionEvent) {

                ChildView = recyclerView.findChildViewUnder(motionEvent.getX(), motionEvent.getY());

                if(ChildView != null && gestureDetector.onTouchEvent(motionEvent)) {

                    //Getting clicked value.
                    RecyclerViewItemPosition = recyclerView.getChildAdapterPosition(ChildView);

                    Intent intent = new Intent(MainActivity.this, RoomActivity.class);
                    intent.putExtra("room_name", list.get(RecyclerViewItemPosition).first);
                    startActivity(intent);
                }

                return false;
            }

            @Override
            public void onTouchEvent(@NonNull RecyclerView recyclerView, @NonNull MotionEvent motionEvent) {

            }

            @Override
            public void onRequestDisallowInterceptTouchEvent(boolean b) {

            }
        });

    }

    private class AddItemsToRecyclerView extends AsyncTask<Void,Void,Void> {
        @Override
        protected Void doInBackground(Void... voids) {
            list.add(new Pair<String, Integer>("Living Room", R.drawable.living_room));
            list.add(new Pair<String, Integer>("Master Bedroom", R.drawable.master_bedroom));
            // list.add(new Pair<String, Integer>("Bedroom", R.drawable.bedroom));
            list.add(new Pair<String, Integer>("Kitchen", R.drawable.kitchen));
            return null;
        }
    }
}
