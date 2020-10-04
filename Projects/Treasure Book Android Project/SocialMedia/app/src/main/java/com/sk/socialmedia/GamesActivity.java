package com.sk.socialmedia;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

public class GamesActivity extends AppCompatActivity {
    LinearLayout game1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_games);
        game1=findViewById(R.id.game1);
        ActionBar actionBar=getSupportActionBar();
        actionBar.setTitle("Game Zone");
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        //actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#FF4081")));

        game1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(GamesActivity.this,TicTacToeGameActivity.class));
            }
        });

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }
}