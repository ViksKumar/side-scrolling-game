package com.example.androidspringcoursework;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;

import static com.example.androidspringcoursework.GameView.screenRatioX;
import static com.example.androidspringcoursework.GameView.screenRatioY;

/**
 * Class for the player ship
 */

public class Player extends GameObject {

    public boolean isGoingUp;
    public boolean isGoingDown;
    public int toShoot = 0;
    Bitmap player;
    private GameView gameView;

    Player (GameView gameView, int screenY, Resources res) {

        this.gameView = gameView;
        player = BitmapFactory.decodeResource(res, R.drawable.player);
        isGoingUp = false;

        x = (int) (64 * screenRatioX);
        y = screenY / 2;

        width = player.getWidth();
        width /= 50;
        width *= (int) (width * screenRatioX);

        height = player.getHeight();
        height /= 50;
        height *= (int) (height * screenRatioY);

        xSpeed = 0;
        ySpeed = 30;

        player = Bitmap.createScaledBitmap(player, width, height, false);

    }

    Bitmap getPlayer () {

        //if player is shooting
        if (toShoot != 0) {
            //reset request and shoot laser
            toShoot--;
            gameView.newLaser();
        }
        return player;
    }

    //Creates a rectangle around the object that can be used to detect for collisions
    Rect getCollisionShape () {
        return new Rect(x, y, x + width, y + height);
    }



}

