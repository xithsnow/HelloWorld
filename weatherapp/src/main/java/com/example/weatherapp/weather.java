package com.example.weatherapp;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DecimalFormat;
import java.text.NumberFormat;

public class weather extends AppCompatActivity {

    private TextView tvWindSpeed,tvHumidity,tvCloud,tvLocation,tvTemp;
    private ImageView ivIcon;
    public static final String WEATHER_SOUCE = "http://api.openweathermap.org/data/2.5/weather?APPID=82445b6c96b99bc3ffb78a4c0e17fca5&mode=json&id=1735161";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);

    tvWindSpeed = (TextView)findViewById(R.id.weather_speed);
    tvHumidity = (TextView)findViewById(R.id.weather_humidity);
    tvCloud = (TextView)findViewById(R.id.weather_cloud);
    tvLocation = (TextView)findViewById(R.id.location);
    tvTemp = (TextView)findViewById(R.id.temperature);

    ivIcon = (ImageView)findViewById(R.id.weather_icon);

    // new WeatherRetrieval().execute();
        volleyRequest();
    }



    public void refresh(View view) {
        //new WeatherRetrieval().execute();
        //Log.d(weather.class.getSimpleName(), "refresh: Refresh button is hit");
        volleyRequest();
    }

    private void volleyRequest() {
        //Log.d(weather.class.getSimpleName(), "volleyRequest: Invoked");
        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest strReq = new StringRequest(Request.Method.GET, WEATHER_SOUCE, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //Log.d(weather.class.getSimpleName(), "onResponse: Response ");
                try {
                    final JSONObject weatherJSON = new JSONObject(response);

                    tvLocation.setText(weatherJSON.getString("name") + "," + weatherJSON.getJSONObject("sys").getString("country"));
                    tvWindSpeed.setText(String.valueOf(weatherJSON.getJSONObject("wind").getDouble("speed")) + " mps");
                    tvCloud.setText(String.valueOf(weatherJSON.getJSONObject("clouds").getInt("all")) + "%");

                    final JSONObject mainJSON = weatherJSON.getJSONObject("main");
                    double temperatueK = mainJSON.getDouble("temp");
                    double temp = temperatueK - 273.15;
                    NumberFormat formatter = new DecimalFormat("#.0");
                    tvTemp.setText(formatter.format(temp));
                    tvHumidity.setText(String.valueOf(mainJSON.getInt("humidity")) + "%");

                    final JSONArray weatherJSONARR = weatherJSON.getJSONArray("weather");
                    if (weatherJSONARR.length() > 0) {
                        int code = weatherJSONARR.getJSONObject(0).getInt("id");
                        ivIcon.setImageResource(getIcon(code));
                    }


                } catch (Exception e) {
                    Log.e(weather.class.getSimpleName(), "onPostExecute: " + e.getMessage());
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError err) {
                Log.e(weather.class.getSimpleName(), "onErrorResponse: ERROR" + err.getMessage() );
            }
        });
        queue.add(strReq);
    }
        public int getIcon(int num1) {
            //Log.d(weather.class.getSimpleName(), "inBetween: "+num1);
            if(num1 >= 200 && num1 <=232){
                return R.drawable.ic_thunderstorm_large;
            }else if(num1 >= 300 && num1 <= 321){
                return R.drawable.ic_drizzle_large;
            }else if(num1>= 500 && num1 < 531){
                return R.drawable.ic_rain_large;
            }else if(num1>= 600 && num1 < 622){
                return R.drawable.ic_snow_large;
            }else if(num1 == 800){
                return R.drawable.ic_day_few_clouds_large;
            }else if(num1 == 802){
                return R.drawable.ic_scattered_clouds_large;
            }else if(num1>= 803 && num1 < 804){
                return R.drawable.ic_broken_clouds_large;
            }else if(num1>= 701 && num1 < 762){
                return R.drawable.ic_fog_large;
            }else if(num1 == 781 || num1 == 900){
                return R.drawable.ic_tornado_large;
            }else if(num1 == 905){
                return R.drawable.ic_windy_large;
            }else if(num1 == 906){
                return R.drawable.ic_hail_large;
            }
            else{
                return 802;
            }

        }
/*

    private class WeatherRetrieval extends AsyncTask<Void, Void, String>
    {
        @Override
        protected void onPreExecute(){
            super.onPreExecute();
        }
        @Override
        protected String doInBackground(Void... voids) {
            if(checkNetwork()){
                try {
                    URL url = new URL(WEATHER_SOUCE);
                    HttpURLConnection conn = (HttpURLConnection)url.openConnection();
                    conn.setRequestMethod("GET");
                    conn.setConnectTimeout(15000);
                    conn.setReadTimeout(15000);
                    conn.connect();

                    int responseCode = conn.getResponseCode();
                    if(responseCode == HttpURLConnection.HTTP_OK){
                        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                        if(bufferedReader != null){
                            String readline;
                            StringBuffer strBuffer = new StringBuffer();
                            while((readline=bufferedReader.readLine()) != null){
                                strBuffer.append((readline));
                            }
                            return strBuffer.toString();
                        }
                    }
                } catch (Exception e) {
                    Log.e(weather.class.getSimpleName(), "doInBackground: "+e.getMessage() );
                    //e.printStackTrace();
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result){
            super.onPostExecute(result);
            if(result != null){
                try {
                    final JSONObject weatherJSON = new JSONObject(result);

                    tvLocation.setText(weatherJSON.getString("name") + "," + weatherJSON.getJSONObject("sys").getString("country"));
                    tvWindSpeed.setText(String.valueOf(weatherJSON.getJSONObject("wind").getDouble("speed")) + " mps");
                    tvCloud.setText(String.valueOf(weatherJSON.getJSONObject("clouds").getInt("all")) + "%");

                    final JSONObject mainJSON = weatherJSON.getJSONObject("main");
                    double temperatueK = mainJSON.getDouble("temp");
                    double temp = temperatueK - 273.15;
                    NumberFormat formatter = new DecimalFormat("#.0");
                    tvTemp.setText(formatter.format(temp));
                    tvHumidity.setText(String.valueOf(mainJSON.getInt("humidity"))+"%");

                    final JSONArray weatherJSONARR = weatherJSON.getJSONArray("weather");
                    if(weatherJSONARR.length()>0){
                        int code = weatherJSONARR.getJSONObject(0).getInt("id");
                        ivIcon.setImageResource(getIcon(code));
                    }


                }catch (Exception e){
                    Log.e(weather.class.getSimpleName(), "onPostExecute: "+e.getMessage() );
                }
            }



        }

        public Boolean checkNetwork() {

            NetworkInfo networkInfo = ((ConnectivityManager) weather.this.getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo();
            if (networkInfo != null && networkInfo.isConnected()){
                //TODO network CONNECTED
                return true;

            }else{
                //TODO no connection
                Log.d(weather.class.getSimpleName(), "checkNetwork: No Network");
                return false;
            }
        }

        public int getIcon(int code){
            return inBetween(code);
        }

        public int inBetween(int num1){
            Log.d(weather.class.getSimpleName(), "inBetween: "+num1);
            if(num1 >= 200 && num1 <=232){
                return R.drawable.ic_thunderstorm_large;
            }else if(num1 >= 300 && num1 <= 321){
                return R.drawable.ic_drizzle_large;
            }else if(num1>= 500 && num1 < 531){
                return R.drawable.ic_rain_large;
            }else if(num1>= 600 && num1 < 622){
                return R.drawable.ic_snow_large;
            }else if(num1 == 800){
                return R.drawable.ic_day_few_clouds_large;
            }else if(num1 == 802){
                return R.drawable.ic_scattered_clouds_large;
            }else if(num1>= 803 && num1 < 804){
                return R.drawable.ic_broken_clouds_large;
            }else if(num1>= 701 && num1 < 762){
                return R.drawable.ic_fog_large;
            }else if(num1 == 781 || num1 == 900){
                return R.drawable.ic_tornado_large;
            }else if(num1 == 905){
                return R.drawable.ic_windy_large;
            }else if(num1 == 906){
                return R.drawable.ic_hail_large;
            }
            else{
                return 802;
            }
        }

    }
    */


}


