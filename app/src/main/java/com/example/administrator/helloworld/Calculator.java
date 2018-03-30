package com.example.administrator.helloworld;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.text.NumberFormat;

/**
 * Created by Administrator on 3/27/2018.
 */

public class Calculator extends AppCompatActivity {
private EditText etLoanAmount, etDownPayment, etTerm, etAnnualInterestRate;
private TextView tvMonthlyRepayment, tvTotalRepayment, tvTotalInterest, tvAverageMonthlyInterest;

public static final String PREF_CALC = "LoanCalc";
public static final String HAS_RECORD = "hasRecord";
public static final String MONTH_REPAY = "monthly repayment";
public static final String TOT_REPAY = "total repayment";
public static final String TOT_INTER = "total interest";
public static final String MONTH_INST = "monthly installment";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calculator);
        final NumberFormat dp2format = new DecimalFormat("#,###");

        etLoanAmount = (EditText)findViewById(R.id.loan_amount);
        etDownPayment = (EditText)findViewById(R.id.down_payment);
        etTerm = (EditText)findViewById(R.id.term);
        etAnnualInterestRate = (EditText)findViewById(R.id.annual_interest_rate);

        tvMonthlyRepayment = (TextView)findViewById(R.id.monthly_repayment);
        tvTotalRepayment = (TextView)findViewById(R.id.total_repayment);
        tvTotalInterest = (TextView)findViewById(R.id.total_interest);
        tvAverageMonthlyInterest = (TextView)findViewById(R.id.average_monthly_interest);

        SharedPreferences sp = getSharedPreferences(PREF_CALC,Context.MODE_PRIVATE);
        if(sp.getBoolean(HAS_RECORD,false)){
            tvMonthlyRepayment.setText(sp.getString(MONTH_REPAY,"0.00"));
            tvTotalRepayment.setText(sp.getString(TOT_REPAY,"0.00"));
            tvTotalInterest.setText(sp.getString(TOT_INTER,"0.00"));
            tvAverageMonthlyInterest.setText(sp.getString(MONTH_INST,"0.00"));
        }
    }


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
        Log.d("Check","Button Clicked!");
        count();
    }

    public void clear(View view) {
        Log.d("Check","Button 2 Clicked!");
        reset();
    }

    public void count(){
        String amt = etLoanAmount.getText().toString();
        String downPy = etDownPayment.getText().toString();
        String intR = etAnnualInterestRate.getText().toString();
        String term = etTerm.getText().toString();

        if(amt.isEmpty() || downPy.isEmpty() || intR.isEmpty() || term.isEmpty())
        {
            return;
        }
       //Log.d(Calculator.class.getSimpleName(),amt);
        //Log.d(Calculator.class.getSimpleName(),downPy);
        //Log.d(Calculator.class.getSimpleName(),intR);
        //Log.d(Calculator.class.getSimpleName(),term);

        double loanAmt = Double.parseDouble(amt) - Double.parseDouble(downPy);
        double intrest = Double.parseDouble(intR) / 12 / 100;
        double noOfMonth = (Integer.parseInt(term)* 12);

        //Log.d(Calculator.class.getSimpleName(),""+loanAmt);
       // Log.d(Calculator.class.getSimpleName(),""+intrest);
       // Log.d(Calculator.class.getSimpleName(),""+noOfMonth);





        if(noOfMonth > 0 ){
            double monthlyRepay = loanAmt * (intrest + (intrest/(Math.pow((1+intrest),noOfMonth)-1)));
            double totalRepay = monthlyRepay * noOfMonth;
            double totalInter = totalRepay - loanAmt;
            double monthlyInt = totalInter / noOfMonth;

            NumberFormat dp2format = new DecimalFormat("#,###.00");
            tvMonthlyRepayment.setText(dp2format.format(monthlyRepay));
            tvTotalRepayment.setText(dp2format.format(totalRepay));
            tvTotalInterest.setText(dp2format.format(totalInter));
            tvAverageMonthlyInterest.setText(dp2format.format(monthlyInt));
            /*
            public static final String PREF_CALC = "LoanCalc";
            public static final String HAS_RECORD = "hasRecord";
            public static final String MONTH_REPAY = "monthly repayment";
            public static final String TOT_REPAY = "total repayment";
            public static final String TOT_INTER = "total interest";
            public static final String MONTH_INST = "monthly installment";

             */
            SharedPreferences sp = getSharedPreferences(PREF_CALC, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sp.edit();
            editor.putBoolean(HAS_RECORD,true);
            editor.putString(MONTH_REPAY,dp2format.format(monthlyRepay));
            editor.putString(TOT_REPAY,dp2format.format(totalRepay));
            editor.putString(TOT_INTER,dp2format.format(totalInter));
            editor.putString(MONTH_INST,dp2format.format(monthlyInt));
            editor.apply();

        }

    }
    public void reset(){
        etLoanAmount.setText("");
        etDownPayment.setText("");
        etTerm.setText("");
        etAnnualInterestRate.setText("");

        tvMonthlyRepayment.setText(R.string.default_result);
        tvTotalRepayment.setText(R.string.default_result);
        tvTotalInterest.setText(R.string.default_result);
        tvAverageMonthlyInterest.setText(R.string.default_result);

    }

    public void hide(View view) {
        view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
}
