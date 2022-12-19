package com.example.androidspringcoursework;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;

import static com.example.androidspringcoursework.GameView.screenRatioX;
import static com.example.androidspringcoursework.GameView.screenRatioY;

/**
 * Class for the shooter enemy found in the hard level
 */

public class ShooterEnemy extends GameObject {

    public boolean wasHit;
    Bitmap ShooterEnemy;

    ShooterEnemy(Resources res) {

        wasHit = true;
        ShooterEnemy = BitmapFactory.decodeResource(res, R.drawable.enemy3);

        x = 0;
        y = -height;

        width = ShooterEnemy.getWidth();
        width /= 7;
        width *= (int) screenRatioX;

        height = ShooterEnemy.getHeight();
        height /= 7;
        height *= (int) screenRatioY;

        xSpeed = 5;
        ySpeed = 10;

        ShooterEnemy = Bitmap.createScaledBitmap(ShooterEnemy, width, height, false);

    }

    Bitmap getEnemy () {
        return ShooterEnemy;
    }

    //Creates a rectangle around the object that can be used to detect for collisions
    Rect getCollisionShape () {
        return new Rect(x, y, x + width, y + height);
    }
}
