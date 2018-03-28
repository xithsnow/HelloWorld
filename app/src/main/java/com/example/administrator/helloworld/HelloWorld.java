package com.example.administrator.helloworld;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import java.util.Timer;
import java.util.TimerTask;


public class HelloWorld extends AppCompatActivity {
private long delay = 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_helloworld);
        Timer timer = new Timer();
        timer.schedule(task,delay);
    }

    public void onClick(View view) {
       // System.out.println("ABC");
        setContentView(R.layout.activity_calculator);
    }
    private TimerTask task = new TimerTask() {
        @Override
        public void run() {
            startActivity(new Intent(HelloWorld.this,Calculator.class));
            finish();
        }
    };



}
