package com.example.androidspringcoursework;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.graphics.Point;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

/**
 * class for the game activity
 */
public class GameActivity extends Activity {

    private GameView gameView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //sets plain view for game
        setContentView(R.layout.activity_game);

        //sets position on screen for game
        Point point = new Point();
        getWindowManager().getDefaultDisplay().getSize(point);
        gameView = new GameView(this, point.x, point.y);

        //starts game view
        setContentView(gameView);
    }

    //pauses game when out of focus
    @Override
    protected void onPause() {
        super.onPause();
        gameView.pause();
    }

    //resumes game when it is focused again
    @Override
    protected void onResume() {
        super.onResume();
        gameView.resume();
    }
}