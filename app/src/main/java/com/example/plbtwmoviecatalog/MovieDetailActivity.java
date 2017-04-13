package com.example.plbtwmoviecatalog;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.net.wifi.WifiConfiguration;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.share.Sharer;
import com.facebook.share.model.ShareHashtag;
import com.facebook.FacebookSdk;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareButton;
import com.facebook.share.widget.ShareDialog;
import com.varunest.sparkbutton.SparkButton;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MovieDetailActivity extends AppCompatActivity {

    TextView MovieName,Summary,StatusMovie,Budged;
    ArrayList<HashMap<String, String>> MovieDataList;
    Button btnBack;
    SparkButton btnShare;
    private Bitmap bitmap;
    ImageView img;
    ProgressDialog pDialog;
    CallbackManager callbackManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        facebookSDKInitialize();
        setContentView(R.layout.activity_movie_detail);
        final Bundle B = getIntent().getExtras();
        final String poster=B.getString("Poster");
        final String idmovie = B.getString("Id");
        final String Title = B.getString("Title");
        final String IMAGE_URL="http://image.tmdb.org/t/p/w500"+poster;

        MovieDataList= new ArrayList<HashMap<String, String>>();
        btnBack = (Button) findViewById(R.id.btnBack);
        btnShare= (SparkButton) findViewById(R.id.btnShare);
        MovieName= (TextView) findViewById(R.id.tvMovieName);
        Summary = (TextView) findViewById(R.id.tvSummary);
        img = (ImageView)findViewById(R.id.imgMovie);
        ShareButton shareButton = (ShareButton)findViewById(R.id.fb_share_button);
        final ShareLinkContent linkContent = new ShareLinkContent.Builder()
                .setContentTitle(Title)
                .setContentDescription(Title + " is one of my favorite movie")
                .setContentUrl(Uri.parse("https://www.themoviedb.org/movie/"+idmovie))
                .setShareHashtag(new ShareHashtag.Builder()
                        .setHashtag("#PLBTWMovieKatalog")
                        .build())
                .build();
        shareButton.setShareContent(linkContent);

        MovieName.setText(B.getString("Title"));
        Summary.setText(B.getString("Summary") + "\n" + B.getString("Release"));
        new LoadImage().execute(IMAGE_URL);


        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), MovieActivity.class);
                startActivity(i);
                finish();
            }
        });
        btnShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), TweetActivity.class);
                Bundle A = new Bundle();
                A.putString("Id",idmovie);
                A.putString("Title",Title);
                i.putExtras(A);
                startActivity(i);
                finish();
            }
        });

    }
    @Override
    protected void onResume()
    {
        super.onResume();
        AppEventsLogger.activateApp(this);
    }

    @Override
    protected void onPause()
    {
        super.onPause();
        AppEventsLogger.deactivateApp(this);
    }

    @Override
    protected void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
            super.onActivityResult(requestCode, resultCode, data);
            callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    protected void facebookSDKInitialize()
    {
        FacebookSdk.sdkInitialize(getApplicationContext());
        callbackManager = CallbackManager.Factory.create();
    }

    private class LoadImage extends AsyncTask<String, String, Bitmap> {

        public LoadImage()
        {
            pDialog = new ProgressDialog(MovieDetailActivity.this);
        }
        @Override

        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(MovieDetailActivity.this);
            pDialog.setMessage("Memproses gambar...");
            pDialog.show();

        }
        protected Bitmap doInBackground(String... args) {
            try {
                bitmap = BitmapFactory.decodeStream((InputStream)new URL(args[0]).getContent());

            } catch (Exception e) {
                e.printStackTrace();
            }
            return bitmap;
        }

        protected void onPostExecute(Bitmap image) {

            if(image != null){
                img.setImageBitmap(image);
                pDialog.dismiss();

            }else{

                pDialog.dismiss();
                Toast.makeText(MovieDetailActivity.this, "Gambar Tidak Ditemukan atau Jaringan Tidak Terhubung", Toast.LENGTH_SHORT).show();

            }
        }
    }
}