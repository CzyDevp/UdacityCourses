/*
 * Copyright (C) 2016 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.android.sunshine;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.android.sunshine.data.SunshinePreferences;
import com.example.android.sunshine.utilities.NetworkUtils;
import com.example.android.sunshine.utilities.OpenWeatherJsonUtils;

import org.json.JSONException;

import java.io.IOException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {
    private TextView mWeatherTextView,ed1;
   // private EditText ed1;
    private ProgressBar pb;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forecast);
        mWeatherTextView = (TextView) findViewById(R.id.tv_weather_data);
        ed1= (TextView)findViewById(R.id.ed1);
        pb= (ProgressBar)findViewById(R.id.pb_load);
        ed1.setText(NetworkUtils.buildUrl(SunshinePreferences.getPreferredWeatherLocation(this)).toString());
     // TODO (9) Call loadWeatherData to perform the network request to get the weather
        loadWeatherData();
    }
    // TODO (8) Create a method that will get the user's preferred location and execute your new AsyncTask and call it loadWeatherData
       void loadWeatherData()
          {
              String location = SunshinePreferences.getPreferredWeatherLocation(this);
               new getWeatherData().execute(location);
          }
    // TODO (5) Create a class that extends AsyncTask to perform network requests
    public class getWeatherData extends AsyncTask<String,Void,String[]>
    {
        @Override
        protected void onPreExecute() {
            pb.setVisibility(View.VISIBLE);
            super.onPreExecute();
        }

        @Override
        protected String[] doInBackground(String... params)
        {
            //result = ;
            if(params.length==0) {
                return null;
            }
            String lctn = params[0];
            URL weatherRequestUrl = NetworkUtils.buildUrl(lctn);
              try
            {
                String jsonWeather = NetworkUtils.getResponseFromHttpUrl(weatherRequestUrl);
                String[] simpledata = new String[jsonWeather.length()];
                try {
                    simpledata = OpenWeatherJsonUtils.getSimpleWeatherStringsFromJson(MainActivity.this,jsonWeather);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return  simpledata;

            }
            catch(IOException e)
            {
                e.printStackTrace();
                return  null;
            }

        }

        @Override
        protected void onPostExecute(String[] s)
        {
            pb.setVisibility(View.INVISIBLE);
            if(s!=null) {
                for (String wthr : s) {
                    mWeatherTextView.append(wthr + "\n\n\n");
                }
            }
            super.onPostExecute(s);

        }
    }

}