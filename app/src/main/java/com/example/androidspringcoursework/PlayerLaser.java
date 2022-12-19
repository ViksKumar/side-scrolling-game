package com.example.androidspringcoursework;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;

import static com.example.androidspringcoursework.GameView.screenRatioX;
import static com.example.androidspringcoursework.GameView.screenRatioY;

/**
 * Class for the player laser
 */
public class PlayerLaser extends GameObject{

    Bitmap PlayerLaser;

    PlayerLaser(Resources res) {

        PlayerLaser = BitmapFactory.decodeResource(res, R.drawable.playerlaser);

        width = PlayerLaser.getWidth();
        width /= 4;
        width *= (int) screenRatioX;

        height = PlayerLaser.getHeight();
        height /= 4;
        height *= (int) screenRatioY;

        xSpeed = 50;
        ySpeed = 0;

        PlayerLaser = Bitmap.createScaledBitmap(PlayerLaser, width, height, false);
    }

    //Creates a rectangle around the object that can be used to detect for collisions
    Rect getCollisionShape () {
        return new Rect(x, y, x + width, y + height);
    }
}
