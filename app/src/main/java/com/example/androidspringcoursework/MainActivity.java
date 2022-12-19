package com.example.androidspringcoursework;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

/**
 * class for handling the main menu
 */
public class MainActivity extends Activity {

    //set up buttons
    Button button_level_one;
    Button button_level_two;
    Button button_level_three;
    Button button_level_custom;
    Button button_highscore;

    //variable for level selection
    public static int levelselected = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        //set up UI buttons with actions to change activity

        button_level_one = (Button)findViewById(R.id.button_level_one);
        button_level_one.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                levelselected = 1;
                startActivity(new Intent(MainActivity.this, GameActivity.class));
            }
        });

        button_level_two = (Button)findViewById(R.id.button_level_two);
        button_level_two.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                levelselected = 2;
                startActivity(new Intent(MainActivity.this, GameActivity.class));
            }
        });

        button_level_three = (Button)findViewById(R.id.button_level_three);
        button_level_three.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                levelselected = 3;
                startActivity(new Intent(MainActivity.this, GameActivity.class));
            }
        });

        button_level_custom = (Button)findViewById(R.id.button_custom);
        button_level_custom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                levelselected = 4;
                startActivity(new Intent(MainActivity.this, CreateLevel.class));
            }
        });

        button_highscore = (Button)findViewById(R.id.Button_view_highscores);
        button_highscore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, HighScore.class));
            }
        });

        //display the previous player's score
        TextView lastScoreText = findViewById(R.id.lastScoreText);

        SharedPreferences preferences = getSharedPreferences("game", MODE_PRIVATE);
        lastScoreText.setText("Last Score: " + preferences.getInt("lastscore", 0));
    }
}