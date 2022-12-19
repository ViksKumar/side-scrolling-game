package com.example.androidspringcoursework;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * class for handling highscores
 */
public class HighScore extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.high_score);

        String TAG = "HighScore";

        Toast.makeText(HighScore.this, "Freebase Connection OK!", Toast.LENGTH_LONG).show();

        //set up UI elements
        TextView statusDisplay = findViewById(R.id.textShow);
        ImageView backButton = findViewById(R.id.backimagehs);
        EditText playerName = findViewById(R.id.txtName);
        Button button_write, button_read;
        button_write = findViewById(R.id.button_store);
        button_read = findViewById(R.id.button_read);

        // Write a message to the database
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("Android Score");

        //set up action when back 'button'(image) is pressed
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HighScore.this, MainActivity.class));
            }
        });

        /**
         * button activity for submitting score
         */
        button_write.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String usrID, name, score;
                int lastscore;

                //get the last score from system preferences to submit to the scoreboard
                SharedPreferences preferences = getSharedPreferences("game", MODE_PRIVATE);
                lastscore =  preferences.getInt("lastscore", 0);

                //set values for database
                //ensures values are stored in order of score
                usrID = lastscore + playerName.getText().toString();
                name = playerName.getText().toString() + ":";
                score = String.valueOf(lastscore);

                //set the score and name to an object
                Score obj = new Score(name,score);

                //check that player and entered a name
                if (usrID.equalsIgnoreCase("")) {
                    statusDisplay.setText("Status : Enter Name");
                } else {
                    //write values to database
                    myRef.child(usrID).setValue(obj);
                    statusDisplay.setText("Status : Data Stored");
                    Toast.makeText(HighScore.this, "Data Stored OK!", Toast.LENGTH_LONG).show();
                }
            }
        });

        /**
         * button activity for updating scoreboard
         */
        button_read.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //read from database
                myRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        //method called once with initial values and again when data is updated
                        ArrayList value = new ArrayList();

                        for (DataSnapshot ds : dataSnapshot.getChildren()) {
                            String  data = "";
                            for (DataSnapshot dsChildren : ds.getChildren()) {
                                data = data + dsChildren.getValue();
                            }
                            //stores the data into an array list
                            value.add(data);
                        }
                        int i = 0;
                        Iterator iter = value.iterator(); // iterator for array list
                        String txtPrint = ""; // get 5 strings on 5 lines
                        while (iter.hasNext() && i < 5) { // get the top 5 scores
                            txtPrint = txtPrint + iter.next() + "\n";
                            i++;
                        }

                        //display highscores on screen
                        statusDisplay.setText("High Scores" + "\n\n" + txtPrint);

                        //reset player name
                        playerName.setText("");
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        //if there is an error reading values
                        Log.w(TAG, "Failed to read value.", error.toException());

                    }
                });
            }
        });
    }
}