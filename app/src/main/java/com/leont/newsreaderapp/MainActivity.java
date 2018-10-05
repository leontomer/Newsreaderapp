package com.leont.newsreaderapp;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.ExecutionException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
public class MainActivity extends AppCompatActivity {


   public static ListView l;
   public static ArrayList<String> titles=new ArrayList<String>();
    public static ArrayList<String> articles=new ArrayList<String>();;
     ArrayAdapter arrayAdapter;


    public class DownloadTask extends AsyncTask<String, Void, String> {


        @Override
        protected String doInBackground(String... urls) {

            String result = "";
            URL url;
            HttpURLConnection urlConnection = null;

            try {

                url = new URL(urls[0]);

                urlConnection = (HttpURLConnection) url.openConnection();

                InputStream in = urlConnection.getInputStream();

                InputStreamReader reader = new InputStreamReader(in);

                int data = reader.read();

                while (data != -1) {

                    char current = (char) data;

                    result += current;

                    data = reader.read();
                }
                //Log.i("res", result);
                return result;

            } catch (Exception e) {

                e.printStackTrace();

            }

            return null;
        }


    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);

        try {

            JSONObject jsonObject = new JSONObject(result);

            String weatherInfo = jsonObject.getString("weather");

            Log.i("Weather content", weatherInfo);

            JSONArray arr = new JSONArray(weatherInfo);

            for (int i = 0; i < arr.length(); i++) {

                JSONObject jsonPart = arr.getJSONObject(i);

                Log.i("main", jsonPart.getString("main"));
                Log.i("description", jsonPart.getString("description"));

            }


        } catch (JSONException e) {
            e.printStackTrace();
        }



    }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        l=findViewById(R.id.listV);
        titles=new ArrayList<String>();
        articles=new ArrayList<String>();
        arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, titles);

        l.setAdapter(arrayAdapter);


        arrayAdapter.notifyDataSetChanged();
        DownloadTask task=new DownloadTask();
        String ids=null;
        String full=null;
        try {
            ids = task.execute("https://hacker-news.firebaseio.com/v0/topstories.json?print=pretty").get();
           int i=2;
    Log.i("res",ids);
            ArrayList<String> arids=new ArrayList<String>();
            while(i+8<ids.length()){
                arids.add(ids.substring(i,i+8));
                i+=10;
            }
           // Log.i("f",arids.get(0).toString());
           // Log.i("s",arids.get(1).toString());
            int j=0;

            while(j<arids.size()){
                DownloadTask task2=new DownloadTask();


                full=task2.execute("https://hacker-news.firebaseio.com/v0/item/"+arids.get(j)+".json?print=pretty").get();
             //   Log.i("ful:",full);
                try {

                    JSONObject jsonObject = new JSONObject(full);

                    String title = jsonObject.getString("title");
                    JSONObject jsonObject2 = new JSONObject(full);
                 String articleURL=jsonObject2.getString("url");
                    titles.add(title);
                    arrayAdapter.notifyDataSetChanged();
                    articles.add(articleURL);
                    j++;

                } catch (JSONException e) {

                    j++;
                }

            }
        } catch (ExecutionException e) {
        } catch (InterruptedException e) {
        }


        /*
        try {



            String[] splitResult = result.split("6726582748");
            Pattern p = Pattern.compile("'home-title'>(.*?)</h2>");
            Matcher m = p.matcher(splitResult[1]);

            while (m.find()) {

                arr.add(m.group(1));

                arrayAdapter.notifyDataSetChanged();

            }



                //     }


            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

*/

       l.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                            @Override
                                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                                              Intent  intent = new Intent(getApplicationContext(), WV.class);
                                                intent.putExtra("val",articles.get(position));

                                                startActivity(intent);


                                            }
                                        }


        );}



}


