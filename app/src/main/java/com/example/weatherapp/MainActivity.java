package com.example.weatherapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    public void submit(View view) {
        EditText text = (EditText) findViewById(R.id.city);
        String city = text.getText().toString();

        DownloadTask task = new DownloadTask();

        String url = "https://api.openweathermap.org/data/2.5/weather?q=" + city + ",IN&appid=2ebfb967deb4d3d03e912c39a09d4382";

        String result = null;
        try {
            result = task.execute(url).get();
        } catch (Exception e) {
//            Log.i("Exception", e.toString());
            Toast.makeText(this, "Could not Find Weather", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }

        // To remove keyboard once search is don
        InputMethodManager mgr = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        mgr.hideSoftInputFromWindow(text.getWindowToken(), 0);

        Log.i("Result", result);
    }

    public class DownloadTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... strings) {
            String result = "";
            URL url;
            HttpURLConnection urlConnection = null; //Kind of a browser, to get the text from URL

            try {
                url = new URL(strings[0]);
                urlConnection = (HttpURLConnection) url.openConnection();
                // To gather the data coming through we create an input stream
                InputStream in = urlConnection.getInputStream();
                InputStreamReader reader = new InputStreamReader(in);
                int data = reader.read();

                while (data != -1) {
                    char current = (char) data;
                    result += current;
                    // For increment
                    data = reader.read();
                }
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(MainActivity.this, "Could not Find Weather", Toast.LENGTH_SHORT).show();
                return "Failed";
            }
            return result;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            try {
//                JSONObject jsonObject = new JSONObject(s);
//                String weatherInfo = jsonObject.getString("weather");
//                String tempInfo = jsonObject.getString("main");
//                JSONArray arr1 = new JSONArray(weatherInfo);
//                JSONArray arr2 = new JSONArray(tempInfo);
//
//                Log.i("Test", "Reached here !!");
//
//                TextView temp = (TextView) findViewById(R.id.temperature);
//                TextView weather = (TextView) findViewById(R.id.weather);
//
//                for (int i = 0; i < arr1.length(); i++) {
//                    JSONObject jsonPart = arr1.getJSONObject(i);
//                    Log.i("Description", jsonPart.getString("description"));
//                    weather.setText(jsonPart.getString("description"));
//                }
//
//                for (int i = 0; i < arr2.length(); i++) {
//                    JSONObject jsonPart = arr2.getJSONObject(i);
//                    Log.i("Main", jsonPart.getString("temp"));
//                    temp.setText(jsonPart.getString("temp"));
//                }
                JSONObject jsonObject = new JSONObject(s);

                String weatherInfo = jsonObject.getString("weather");
                String tempInfo = jsonObject.getString("main");
                JSONObject jsonObject1 = new JSONObject(tempInfo);
                String temp = jsonObject1.getString("temp");
                Log.i("test", temp);

                Double temperature = Double.parseDouble(temp);
                temperature = temperature - 273;


                JSONArray arr1 = new JSONArray(weatherInfo);


                TextView weather = (TextView) findViewById(R.id.weather);
                TextView t = (TextView) findViewById(R.id.temperature);

                t.setText(String.format("%.2f", temperature));

//                JSONArray arr=new JSONArray(weatherInfo);

                for (int i = 0; i < arr1.length(); i++) {
                    JSONObject jsonPart = arr1.getJSONObject(i);
                    Log.i("Description", jsonPart.getString("description"));
                    weather.setText(jsonPart.getString("description"));
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
}