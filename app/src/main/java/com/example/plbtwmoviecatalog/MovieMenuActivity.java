package com.example.plbtwmoviecatalog;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.sackcentury.shinebuttonlib.ShineButton;
import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.TwitterSession;

public class MovieMenuActivity extends AppCompatActivity {

    ShineButton btnTopMovie;
    ShineButton btnAbout;
    TextView userName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_menu);
        TwitterSession session = Twitter.getSessionManager().getActiveSession();
        btnTopMovie = (ShineButton) findViewById(R.id.btnTopMovies);
        btnAbout = (ShineButton) findViewById(R.id.btnAbout);
        userName = (TextView) findViewById(R.id.txtUsername);
        userName.setText("Selamat datang kepada "+session.getUserName());
        btnTopMovie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), MovieActivity.class);
                startActivity(i);
            }
        });
        btnAbout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), AboutActivity.class);
                startActivity(i);
            }
        });

    }
}