package com.example.androidspringcoursework;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;

import static com.example.androidspringcoursework.GameView.screenRatioX;
import static com.example.androidspringcoursework.GameView.screenRatioY;

/**
 * Class for Passive enemies found in the easy difficulty level
 */

public class PassiveEnemy extends GameObject {

    public boolean wasHit;
    Bitmap PassiveEnemy;

    PassiveEnemy(Resources res) {

        wasHit = true;
        PassiveEnemy = BitmapFactory.decodeResource(res, R.drawable.enemy1);

        x = 0;
        y = -height;

        width = PassiveEnemy.getWidth();
        width /= 70;
        width *= (int) (width * screenRatioX);

        height = PassiveEnemy.getHeight();
        height /= 70;
        height *= (int) (height * screenRatioY);

        xSpeed = 5;
        ySpeed = 10;

        PassiveEnemy = Bitmap.createScaledBitmap(PassiveEnemy, width, height, false);

    }

    Bitmap getEnemy () {
        return PassiveEnemy;
    }

    //Creates a rectangle around the object that can be used to detect for collisions
    Rect getCollisionShape () {
        return new Rect(x, y, x + width, y + height);
    }
}
