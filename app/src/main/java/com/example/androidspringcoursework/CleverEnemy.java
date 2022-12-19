package com.example.androidspringcoursework;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;

import static com.example.androidspringcoursework.GameView.screenRatioX;
import static com.example.androidspringcoursework.GameView.screenRatioY;

/**
 * Class for Clever enemies found in the medium difficulty level
 */
public class CleverEnemy extends GameObject {

    public boolean wasHit;
    Bitmap CleverEnemy;

    CleverEnemy(Resources res) {

        wasHit = true;
        CleverEnemy = BitmapFactory.decodeResource(res, R.drawable.enemy2);

        x = 0;
        y = -height;

        //width scaled for different screen sizes
        width = CleverEnemy.getWidth();
        width /= 45;
        width *= (int) (width * screenRatioX);

        //height scaled for different screen sizes
        height = CleverEnemy.getHeight();
        height /= 45;
        height *= (int) (height * screenRatioY);

        xSpeed = 5;
        ySpeed = 10;

        CleverEnemy = Bitmap.createScaledBitmap(CleverEnemy, width, height, false);

    }

    Bitmap getEnemy () {
        return CleverEnemy;
    }

    //Creates a rectangle around the object that can be used to detect for collisions
    Rect getCollisionShape () {
        return new Rect(x, y, x + width, y + height);
    }
}
