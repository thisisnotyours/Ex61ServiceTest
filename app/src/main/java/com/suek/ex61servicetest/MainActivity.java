package com.suek.ex61servicetest;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void clickPlay(View view) {
        //뮤직을 백그라운드에서 실행하는 서비스를 시작!!
        Intent intent= new Intent(this, MusicService.class);   //Intent 1
        //startService(intent);
        //포어그라운드 서비스로 실행하도록.. (Manifest.xml 에 퍼미션)
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.O) startForegroundService(intent);
        else startService(intent);


    }

    public void clickStop(View view) {
        //뮤직을 백그라운드에서 실행하는 서비스를 종료!!
        Intent intent= new Intent(this, MusicService.class);  //Intent 2
        stopService(intent);
    }
}
