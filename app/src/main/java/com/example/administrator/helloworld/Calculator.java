package com.example.administrator.helloworld;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

/**
 * Created by Administrator on 3/27/2018.
 */

public class Calculator extends AppCompatActivity {
/*
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calculator);

    }
*/
    public int add(int num1, int num2){

        return num1+num2;
    }
    public int countdown(int num1){
        if(num1 == 0){
            return num1;
        }
        else {
            return countdown(num1 - 1);
        }
    }

    public double discAmt(int amount,int percentage){
        double num1 = amount * (amount / percentage);
        return (Math.round(num1)/100);
    }

    public void calculate(View view) {
       //TODO:Calculate the amounts
    }

    public void clear(View view) {
        //TODO:Reset the Amounts

    }
}
