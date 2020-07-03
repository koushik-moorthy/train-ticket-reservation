package com.example.trainbooking;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private static int SPLASH_TIME_OUT = 2000;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ActivityCompat.requestPermissions(MainActivity.this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},123);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run()
            {
                Intent homeIntent = new Intent(MainActivity.this, UserLogin.class);
                startActivity(homeIntent);
                finish();
            }
        },SPLASH_TIME_OUT);
        checkConnection();
    }

    public void checkConnection(){
        ConnectivityManager manager=(ConnectivityManager)getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork=manager.getActiveNetworkInfo();
        if(null==activeNetwork){
            Toast.makeText(this,"No Internet Connection",Toast.LENGTH_SHORT).show();
        }

    }
}
