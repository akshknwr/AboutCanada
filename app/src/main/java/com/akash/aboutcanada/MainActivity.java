package com.akash.aboutcanada;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    ListView listView;
private final static String json_url="https://dl.dropboxusercontent.com/s/2iodh4vg0eortkl/facts.json";
String title="null";
List<CanadianFact> canadianFactList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listView= findViewById(R.id.rows_list);
        canadianFactList=new ArrayList<>();
        new SyncJson().execute();

    }

    public class SyncJson extends AsyncTask<String, String, String>{
        @Override
        protected String doInBackground(String... strings) {
            try {
                JsonParser jsonParser=new JsonParser();
                //getting JSONObject from url
                JSONObject jsonObject=jsonParser.getJsonObjectFromURL(json_url);
                //retrieving rows in array from json object
                JSONArray rows=jsonObject.getJSONArray("rows");

                //JSONObject titleObject=jsonObject.getJSONObject("title");
                title=jsonObject.getString("title");
                for (int i=0;i<rows.length();i++){
                    JSONObject row= rows.getJSONObject(i);
                    CanadianFact canadianFact=new CanadianFact();
                    canadianFact.setTitle(row.getString("title"));
                    canadianFact.setDescription(row.getString("description"));
                    canadianFact.setImageLink(row.getString("imageHref"));
                    canadianFactList.add(canadianFact);




                }

            } catch (JSONException e){
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            getSupportActionBar().setTitle(title);
            ListAdapter listAdapter=new ListAdapter(canadianFactList,MainActivity.this);
            listView.setAdapter(listAdapter);
        }
    }
}
