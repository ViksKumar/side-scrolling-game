package com.example.androidspringcoursework;

import android.graphics.Bitmap;
import android.graphics.Rect;
/**
 * abstract class for all of the objects in the game
 */
public abstract class GameObject {
    protected int x;
    protected int y;
    protected int width;
    protected int height;
    protected int xSpeed;
    protected int ySpeed;

    public GameObject() {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.xSpeed = xSpeed;
        this.ySpeed = ySpeed;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public int getxSpeed() {
        return xSpeed;
    }

    public int getySpeed() {
        return ySpeed;
    }

    abstract Rect getCollisionShape();
}
