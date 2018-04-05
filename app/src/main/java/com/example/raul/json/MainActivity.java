package com.example.raul.json;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {


    TextView textView;


    public class JsonDownloadTask extends AsyncTask<String, Void, String>{

        @Override
        protected String doInBackground(String... params) {
            URL url = null;
            try {
                String result = "";
                url = new URL(params[0]);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();

                InputStream inputStream = httpURLConnection.getInputStream();
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);

                int data = inputStreamReader.read(); //Leer primer caracter
                while (data != -1){
                    char character = (char)data;
                    result += character;
                    data = inputStreamReader.read();
                }


                return result;

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }


            return null;
        }


        @Override
        protected void onPostExecute(String resul){
            super.onPostExecute(resul);

            try {

                JSONObject jsonObject = new JSONObject(resul);

                Log.i("jsonObject", jsonObject.toString());

                JSONObject main = new JSONObject(jsonObject.getString("main"));

                Log.i("jsonObject", main.toString());

                String temp = main.getString("temp");

                Log.i("jsonObject", temp);

                Double kelvin = Double.parseDouble(temp);
                Double celsius = kelvin - 273.5;
                String result = String.format("%.0f", celsius);

                textView.setText(result);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView  = (TextView) findViewById(R.id.textView2);

        String url = "api.openweathermap.org/data/2.5/weather?q=Cartago";
        JsonDownloadTask jsonDownloadTask = new JsonDownloadTask();
        jsonDownloadTask.execute(url);

    }
}
