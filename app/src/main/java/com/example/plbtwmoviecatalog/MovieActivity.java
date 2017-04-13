package com.example.plbtwmoviecatalog;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MovieActivity extends AppCompatActivity {

    //URL Popular Movie
    private String url ="https://api.themoviedb.org/3/movie/popular";

    private static final String TAG_TITLE="original_title";
    private static final String TAG_RELEASE="release_date";
    private static final String TAG_HASIL="results";
    private static final String TAG_SUMMARY="overview";
    private static final String TAG_ID="id";
    private static final String TAG_IMG="poster_path";

    ProgressDialog pDialog;
    Button btnHome1;

    ArrayList<HashMap<String, String>> MovieDataList;
    JSONParser jParser = new JSONParser();
    JSONArray dataMovie = null;
    ListView lv;

    private class AmbilDataMovie extends AsyncTask<String,String,String>
    {
        String dTitle,dRelease;
        int sukses=0;
        public  AmbilDataMovie()
        {
            pDialog = new ProgressDialog(MovieActivity.this);
        }
        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
            pDialog.setMessage("Mengambil Data Film...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }
        @Override
        protected String doInBackground(String... args)
        {
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("api_key", "ecd96601be503f8c89fd3760450c061b"));

            try{
                JSONObject json = jParser.makeHttpRequest(url, "GET", params);

                if (json != null) {

                    MovieDataList = new ArrayList<HashMap<String, String>>();
                    Log.d("Semua Film :", json.toString());
                    dataMovie = json.getJSONArray(TAG_HASIL);

                    for (int i = 0; i < 20; i++)
                    {
                        JSONObject c = dataMovie.getJSONObject(i);

                        String Title=c.getString(TAG_TITLE);
                        String Release=c.getString(TAG_RELEASE);
                        String Summary=c.getString(TAG_SUMMARY);
                        String Id=c.getString(TAG_ID);
                        String Poster=c.getString(TAG_IMG);
                        HashMap<String, String> map = new HashMap<String, String>();
                        map.put(TAG_TITLE,Title);
                        map.put(TAG_RELEASE,Release);
                        map.put(TAG_SUMMARY,Summary);
                        map.put(TAG_ID,Id);
                        map.put(TAG_IMG,Poster);
                        MovieDataList.add(map);
                    }

                }

            } catch (JSONException e)
            {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String file_url)
        {
            pDialog.dismiss();
            ListAdapter adapter = new SimpleAdapter(
                    MovieActivity.this, MovieDataList,
                    R.layout.list_movie, new String[]{
                    TAG_TITLE,TAG_RELEASE},
                    new int[]{R.id.tvtitle, R.id.tvrelease}
            );
            lv.setAdapter(adapter);

            lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Bundle B = new Bundle();
                    B.putString("Summary", MovieDataList.get(position).get(TAG_SUMMARY));
                    B.putString("Title",MovieDataList.get(position).get(TAG_TITLE));
                    B.putString("Id",MovieDataList.get(position).get(TAG_ID));
                    B.putString("Release",MovieDataList.get(position).get(TAG_RELEASE));
                    B.putString("Poster",MovieDataList.get(position).get(TAG_IMG));

                    Intent i = new Intent(getApplicationContext(), MovieDetailActivity.class);
                    i.putExtras(B);
                    startActivity(i);
                    finish();
                }
            });

        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie);
        MovieDataList = new ArrayList<HashMap<String, String>>();
        lv = (ListView) findViewById(R.id.lvdata);
        btnHome1 = (Button) findViewById(R.id.btnhome1);
        new AmbilDataMovie().execute();
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int possition, long id) {
                String dTitle = MovieDataList.get(possition).get(TAG_TITLE);
                String dSummary = MovieDataList.get(possition).get(TAG_RELEASE);
            }
        });
        btnHome1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), MovieMenuActivity.class);
                startActivity(i);
                finish();
            }
        });
    }
}
