package com.example.plbtwmoviecatalog;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterCore;
import com.twitter.sdk.android.tweetcomposer.TweetComposer;

import java.io.File;

import io.fabric.sdk.android.Fabric;

public class TweetActivity extends AppCompatActivity{
    @Override
    public void onCreate(Bundle savedInstaceState) {
        super.onCreate(savedInstaceState);
        final Bundle B = getIntent().getExtras();
        final String idmovie=B.getString("Id");
        final String Title=B.getString("Title");

        TwitterAuthConfig authConfig =  new TwitterAuthConfig("consumerKey", "consumerSecret");
        Fabric.with(this, new TwitterCore(authConfig), new TweetComposer());

        TweetComposer.Builder builder = new TweetComposer.Builder(this)
                .text("Film "+Title+" ini menarik untuk ditonton - "+"https://www.themoviedb.org/movie/"+idmovie + "#PLBTWMovieKatalog");
        builder.show();

        Intent i = new Intent(getApplicationContext(), MovieMenuActivity.class);
        startActivity(i);

    }

}