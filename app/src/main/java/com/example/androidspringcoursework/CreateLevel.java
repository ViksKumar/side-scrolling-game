package com.example.androidspringcoursework;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

/**
 * Activity for user created levels
 */

public class CreateLevel extends Activity {

    //set default values in-case of null input
    public static int easyenemynumber = 1;
    public static int mediumenemynumber = 1;
    public static int hardenemynumber = 1;

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Set View
        setContentView(R.layout.activity_create_level);

        //Set up UI elements
        Button button_play;
        button_play = findViewById(R.id.btnCPlay);

        EditText easyEnemyNum = findViewById(R.id.editTextEasy);
        EditText mediumEnemyNum = findViewById(R.id.editTextMedium);
        EditText hardEnemyNum = findViewById(R.id.editTextHard);

        ImageView backButton = findViewById(R.id.backimagecl);

        //Return to main activity if back 'button'(image) is pressed
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(CreateLevel.this, MainActivity.class));
            }
        });

        //Set number of enemies and load game
        button_play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String easyinput = easyEnemyNum.getText().toString();
                easyenemynumber = Integer.parseInt(easyinput);

                String mediuminput = mediumEnemyNum.getText().toString();
                mediumenemynumber = Integer.parseInt(mediuminput);

                String hardinput = hardEnemyNum.getText().toString();
                hardenemynumber = Integer.parseInt(hardinput);

                startActivity(new Intent(CreateLevel.this, GameActivity.class));
            }
        });
    }
}