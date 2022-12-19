package com.example.androidspringcoursework;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;

import static com.example.androidspringcoursework.GameView.screenRatioX;
import static com.example.androidspringcoursework.GameView.screenRatioY;

/**
 * Class for the lasers fired by 'Shooter' enemies
 */
public class EnemyLaser extends GameObject{

    Bitmap EnemyLaser;

    EnemyLaser(Resources res) {

        EnemyLaser = BitmapFactory.decodeResource(res, R.drawable.enemylaser);

        //width scaled for different screen sizes
        width = EnemyLaser.getWidth();
        width /= 4;
        width *= (int) screenRatioX;

        //height scaled for different screen sizes
        height = EnemyLaser.getHeight();
        height /= 4;
        height *= (int) screenRatioY;

        xSpeed = 15;
        ySpeed = 0;

        EnemyLaser = Bitmap.createScaledBitmap(EnemyLaser, width, height, false);
    }

    //Creates a rectangle around the object that can be used to detect for collisions
    Rect getCollisionShape () {
        return new Rect(x, y, x + width, y + height);
    }
}