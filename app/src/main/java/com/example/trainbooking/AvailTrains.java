    package com.example.trainbooking;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

    public class AvailTrains extends AppCompatActivity {
        LinearLayout mparent;
        TextView mine;

    LayoutInflater layoutInflater;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_avail_trains);
        mine=(TextView) findViewById(R.id.mytrain);
//        layoutInflater=(LayoutInflater)getSystemService(LAYOUT_INFLATER_SERVICE);

        for(int i=0;i<9;i++){
            mine.setText("i"+i);
        }
    }
}
