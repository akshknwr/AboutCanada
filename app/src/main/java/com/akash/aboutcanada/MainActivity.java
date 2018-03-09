package com.akash.aboutcanada;


import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Build;
import android.preference.PreferenceManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import android.view.View;
import android.widget.AbsListView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {
    ListView listView;
    int previousFirstItem;
    int previousLastItem=0;
    //initial offset starting with 0 so later on can be updated
    private  int offset=2;
    LinearLayout pulllayout; //a layout to show pull guide to refresh
private final static String json_url="https://dl.dropboxusercontent.com/s/2iodh4vg0eortkl/facts.json";
String title="null";
List<CanadianFact> canadianFactList;
ArrayList<CanadianFact> subList;
SwipeRefreshLayout swipeRefreshLayout;
boolean isJsonLoaded;

public enum Appstart{
    First_Time, Normal, First_Version;
}
public Appstart checkAppStart(){
    SharedPreferences preferences= PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
    Appstart appstart=Appstart.Normal;
    try {
        PackageInfo packageInfo=getPackageManager().getPackageInfo(getPackageName(),0);
        int lastVersion=preferences.getInt("previous_version",0);
        int currentVersion=packageInfo.versionCode;
        appstart=checkAppStart(currentVersion,lastVersion);
        preferences.edit().putInt("previous_version",currentVersion).commit();


    } catch (PackageManager.NameNotFoundException e){
        e.printStackTrace();
    }
    return appstart;
}

public Appstart checkAppStart(int currentVersion, int lastVersion){
    if (lastVersion==-1){
        return Appstart.First_Time;

    } else if (lastVersion<currentVersion){
        return Appstart.First_Time;

    }else if (lastVersion>currentVersion){
        return Appstart.Normal;

    } else
    {
        return Appstart.Normal;

    }

}

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        pulllayout=findViewById(R.id.pull_layout);
        switch (checkAppStart()){
            case Normal:break;
            case First_Time: pulllayout.setVisibility(View.VISIBLE);
            break;
            case First_Version: pulllayout.setVisibility(View.VISIBLE);
            break;
            default:break;

        }

        listView= findViewById(R.id.rows_list);
        isJsonLoaded=false; //json wouldnt be loaded at the start of app;
        swipeRefreshLayout=findViewById(R.id.refresh_layout);
        swipeRefreshLayout.setOnRefreshListener(this);


        swipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                swipeRefreshLayout.setRefreshing(true);
                if (isJsonLoaded){

                    fetchData(); //fetches images only if json has loaded so third party library glide knows image link
                } else new SyncJson().execute();   // retreives json if it hasn't loaded yet


            }
        });

        canadianFactList=new ArrayList<>();
        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView absListView, int state) {
                switch (state){
                    case SCROLL_STATE_IDLE: pulllayout.setVisibility(View.GONE); //scrolling stopped
                        Log.d("FLing","idle");
                        break;
                    case  SCROLL_STATE_TOUCH_SCROLL: pulllayout.setVisibility(View.VISIBLE);
                        Log.d("FLing","scrolling");
                    break;
                    case SCROLL_STATE_FLING: Log.d("FLing","fling");
                    break;
                }
            }

            @Override
            public void onScroll(AbsListView absListView, int i, int i1, int i2) {

            }
        });


    }
    @Override
    public void onRefresh(){
        swipeRefreshLayout.setRefreshing(true);
        pulllayout.setVisibility(View.VISIBLE);

        if (isJsonLoaded){

            fetchData(); //fetches images only if json has loaded so third party library glide knows image link
        } else new SyncJson().execute();   // retreives json if it hasn't loaded yet
    }

//
    public  class SyncJson extends AsyncTask<String, String, String>{



        @Override
        protected String doInBackground(String... strings) {


            JsonParser jsonParser=new JsonParser();
            //getting JSONObject from url
            JSONObject jsonObject=jsonParser.getJsonObjectFromURL(json_url);
            // String url=json_url;
            try {


                JSONArray rows = jsonObject.getJSONArray("rows");

                //JSONObject titleObject=jsonObject.getJSONObject("title");
                title = jsonObject.getString("title");

                for (int i = 0; i < rows.length(); i++) {
                    JSONObject row = rows.getJSONObject(i);
                    CanadianFact canadianFact = new CanadianFact();
                    canadianFact.setTitle(row.getString("title"));
                    canadianFact.setDescription(row.getString("description"));
                    canadianFact.setImageLink(row.getString("imageHref"));
                    canadianFact.setLoaded(false); //Because Image has not been downloaded yet so we can download later
                    if (canadianFact.getTitle().equalsIgnoreCase("null") &&
                            canadianFact.getDescription().equalsIgnoreCase("null") &&
                            canadianFact.getImageLink().equalsIgnoreCase("null")){
                        //do not add empty row on list
                    } else canadianFactList.add(canadianFact);



                }
            } catch (JSONException e){
                e.printStackTrace();
            }


            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            swipeRefreshLayout.setRefreshing(false);
            getSupportActionBar().setTitle(title);


            subList=new ArrayList<CanadianFact>(canadianFactList.subList(0,offset));
            previousFirstItem=0;
            isJsonLoaded=true;
            previousLastItem=offset;
            final ListAdapter listAdapter=new ListAdapter(subList,MainActivity.this);
            listView.setAdapter(listAdapter);



        }
    }

    private void fetchData(){
        if (previousLastItem < canadianFactList.size()) {
            List<CanadianFact> subListOfCanada;
            if ((previousLastItem +offset) < canadianFactList.size()){
                subListOfCanada=new ArrayList<CanadianFact>(canadianFactList.subList(previousLastItem,previousLastItem+offset));
               // subList.addAll(canadianFactList.subList(previousLastItem + 1, previousLastItem + offset));

            } else subListOfCanada=new ArrayList<>(canadianFactList.subList(previousLastItem, canadianFactList.size()));
                //subList.addAll(canadianFactList.subList(previousLastItem+1,canadianFactList.size() - 1));


            // subList.clear();
            //subList.addAll(canadianFactList);

            Log.d("fetchdata", "fetching");

//        for (int i=0;i<3;i++){
//
//            subList.add(canadianFactList.get(previousLastItem+1));
//        }
            previousFirstItem = previousLastItem + 1;
            previousLastItem = previousLastItem + offset;
            ((ListAdapter) listView.getAdapter()).addItems(subListOfCanada);

            listView.post(new Runnable() {
                @Override
                public void run() {
                    listView.smoothScrollToPosition(0);
                }
            });



           // listView.setSelection(0);
            if (Build.VERSION.SDK_INT >= 19) {
                // listView.smoothScrollByOffset(listView.getCount() -1);
                //listView.scrollListBy(100);
            }
        } else Toast.makeText(getApplicationContext(),"All informations are already retreived",Toast.LENGTH_SHORT).show();

        swipeRefreshLayout.setRefreshing(false);

        pulllayout.setVisibility(View.GONE);




    }

}
