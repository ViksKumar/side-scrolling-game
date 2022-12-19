package com.example.androidspringcoursework;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceView;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Class which contains the game information
 */
public class GameView extends SurfaceView implements Runnable{

    private Thread thread;
    private boolean isPlaying, isGameOver = false;
    private Background background1, background2;
    private int screenX, screenY, score = 0;
    private Paint paint;
    public static float screenRatioX, screenRatioY;
    private Player player;
    private List<PlayerLaser> playerLasers;
    private List<EnemyLaser> enemyLasers;
    private PassiveEnemy[] passiveEnemies;
    private CleverEnemy[] cleverEnemies;
    private ShooterEnemy[] shooterEnemies;
    private Random random;
    private SharedPreferences preferences;
    private GameActivity activity;
    private int rechargeCounter;
    String TAG = "GameView";

    public GameView(GameActivity activity, int screenX, int screenY) {
        super(activity);

        this.activity = activity;

        //gets preferences to store game information
        preferences = activity.getSharedPreferences("game", Context.MODE_PRIVATE);

        //ensures game can play on different screen sizes
        this.screenX = screenX;
        this.screenY = screenY;
        screenRatioX = 1920f / screenX;
        screenRatioY = 1080f / screenY;

        //two background images used for scrolling animation
        background1 = new Background(screenX, screenY, getResources());
        background2 = new Background(screenX, screenY, getResources());

        //player character
        player = new Player(this, screenY, getResources());

        //storage of game objects
        playerLasers = new ArrayList<>();
        enemyLasers = new ArrayList<>();
        passiveEnemies = new PassiveEnemy[0];
        cleverEnemies = new CleverEnemy[0];
        shooterEnemies = new ShooterEnemy[0];

        //set up enemies depending on game level

        //easy level only has passive enemies
        if(MainActivity.levelselected == 1) {
            passiveEnemies = new PassiveEnemy[4];

            for (int i = 0; i < 4; i++ ) {
                PassiveEnemy passiveEnemy = new PassiveEnemy(getResources());
                passiveEnemies[i] = passiveEnemy;
            }
        }

        //medium level has clever enemies
        if(MainActivity.levelselected == 2) {
            cleverEnemies = new CleverEnemy[4];

            for (int i = 0; i < 4; i++ ) {
                CleverEnemy cleverEnemy = new CleverEnemy(getResources());
                cleverEnemies[i] = cleverEnemy;
            }
        }

        //hard level has shooter enemies
        if(MainActivity.levelselected == 3) {
            shooterEnemies = new ShooterEnemy[4];

            for (int i = 0; i < 4; i++ ) {
                ShooterEnemy shooterEnemy = new ShooterEnemy(getResources());
                shooterEnemies[i] = shooterEnemy;
            }
        }

        //custom levels can have any type of enemy, depending on user input
        if(MainActivity.levelselected == 4) {
            passiveEnemies = new PassiveEnemy[CreateLevel.easyenemynumber];
            cleverEnemies = new CleverEnemy[CreateLevel.mediumenemynumber];
            shooterEnemies = new ShooterEnemy[CreateLevel.hardenemynumber];

            for (int i = 0; i < CreateLevel.easyenemynumber; i++ ) {
                PassiveEnemy passiveEnemy = new PassiveEnemy(getResources());
                passiveEnemies[i] = passiveEnemy;
            }

            for (int i = 0; i < CreateLevel.mediumenemynumber; i++ ) {
                CleverEnemy cleverEnemy = new CleverEnemy(getResources());
                cleverEnemies[i] = cleverEnemy;
            }

            for (int i = 0; i < CreateLevel.hardenemynumber; i++ ) {
                ShooterEnemy shooterEnemy = new ShooterEnemy(getResources());
                shooterEnemies[i] = shooterEnemy;
            }
        }

        random = new Random();

        background2.x = screenX;

        paint = new Paint();
        paint.setTextSize(128);
        paint.setColor(Color.WHITE);

        rechargeCounter = 0;

    }

    /**
     * calls methods to update the game
     */
    @Override
    public void run() {
        while (isPlaying) {

            updateBackground ();
            updatePlayer();
            updatePlayerLasers();
            updatePassiveEnemies();
            updateCleverEnemies();
            updateShooterEnemies();
            updateEnemyLasers();
            rechargeCounter++;
            draw ();
            sleep ();

        }
    }

    /**
     * puts the thread to sleep
     */
    private void sleep() {
        try {
            Thread.sleep(17);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * animates the background to scroll
     */
    private void updateBackground() {

        background1.x -= 10 * screenRatioX;
        background2.x -= 10 * screenRatioY;

        if (background1.x + background1.background.getWidth() < 0) {
            background1.x = screenX;
        }

        if (background2.x + background2.background.getWidth() < 0) {
            background2.x = screenX;
        }
    }

    /**
     * method for moving the player
     */
    private void updatePlayer() {

        if (player.isGoingUp) {
            player.y += player.ySpeed * screenRatioY;
        }

        if (player.isGoingDown) {
            player.y -= player.ySpeed * screenRatioY;
        }

        if (player.y < 0) {
            player.y = 0;
        }

        if (player.y > screenY - player.height) {
            player.y = screenY - player.height;
        }
    }


    /**
     * method for moving player lasers
     */
    private void updatePlayerLasers() {

        List<PlayerLaser> missedPlayerLaser = new ArrayList<>();

        for (PlayerLaser playerLaser : playerLasers) {

            //if a laser goes off the right side of the screen, add it to the missed list
            if (playerLaser.x > screenX) {
                missedPlayerLaser.add(playerLaser);
            }

            //move the laser at an appropriate speed based on screen size
            playerLaser.x += playerLaser.xSpeed * screenRatioX;

            //check collisions with passive enemies;
            for (PassiveEnemy passiveEnemy : passiveEnemies) {

                //check if the rectangles around the passive enemy and player laser overlap
                if (Rect.intersects(passiveEnemy.getCollisionShape(), playerLaser.getCollisionShape())) {

                    //increase score and clear objects
                    score++;
                    passiveEnemy.x = -500;
                    playerLaser.x = screenX + 500;
                    passiveEnemy.wasHit = true;
                }
            }

            //check collisions with clever enemies;
            for (CleverEnemy cleverEnemy : cleverEnemies) {

                //check if the rectangles around the clever enemy and player laser overlap
                if (Rect.intersects(cleverEnemy.getCollisionShape(), playerLaser.getCollisionShape())) {

                    //increase score and clear objects
                    score++;
                    cleverEnemy.x = -500;
                    playerLaser.x = screenX + 500;
                    cleverEnemy.wasHit = true;
                }
            }

            //check collisions with shooter enemies;
            for (ShooterEnemy shooterEnemy : shooterEnemies) {

                //check if the rectangles around the clever enemy and player laser overlap
                if (Rect.intersects(shooterEnemy.getCollisionShape(), playerLaser.getCollisionShape())) {

                    //increase score and clear objects
                    score++;
                    shooterEnemy.x = -500;
                    playerLaser.x = screenX + 500;
                    shooterEnemy.wasHit = true;
                }
            }
        }

        //remove lasers that missed from the game
        for (PlayerLaser playerLaser : missedPlayerLaser) {
            playerLasers.remove(playerLaser);
        }
    }

    /**
     * method for updating shooter enemy lasers
     */
    private void updateEnemyLasers() {

        List<EnemyLaser> missedEnemyLaser = new ArrayList<>();

        //check if any enemy lasers have passed the player (missed)
        for (EnemyLaser enemyLaser : enemyLasers) {
            if (enemyLaser.x < (player.x-enemyLaser.width)) {
                missedEnemyLaser.add(enemyLaser);
            }

            //update position based on screen size
            enemyLaser.x -= enemyLaser.xSpeed * screenRatioX;

            //check for collision with player
            if (Rect.intersects(enemyLaser.getCollisionShape(), player.getCollisionShape())) {

                //end game and stop updates if player is hit
                isGameOver = true;
                return;
            }
        }

        //remove missed lasers
        for (EnemyLaser enemyLaser : missedEnemyLaser) {
            enemyLasers.remove(enemyLaser);
        }
    }

    /**
     * method for updating passive enemies
     */
    private void updatePassiveEnemies() {

        for (PassiveEnemy passiveEnemy : passiveEnemies) {

            passiveEnemy.x -= passiveEnemy.xSpeed;

            if (passiveEnemy.x + passiveEnemy.width < 0) {

                //if an enemy makes it past the player
                if (!passiveEnemy.wasHit) {

                    //end the game
                    isGameOver = true;
                    return;
                }

                //passive enemies can have different speeds up within a limit
                int bound = (int) (5 * screenRatioX);
                passiveEnemy.xSpeed = random.nextInt(bound);

                if (passiveEnemy.xSpeed < 1 * screenRatioX) {
                    passiveEnemy.xSpeed = (int) (5 * screenRatioX);
                }

                //spawn passive enemies at the right of the screen at a random height.
                passiveEnemy.x = screenX;
                passiveEnemy.y = random.nextInt(screenY - passiveEnemy.height);

                passiveEnemy.wasHit = false;
            }

            //check for collisions with a player
            if (Rect.intersects(passiveEnemy.getCollisionShape(), player.getCollisionShape())) {

                //end the game
                isGameOver = true;
                return; 
            }
        }
    }

    /**
     * method for updating clever enemies
     */
    private void updateCleverEnemies() {

        for (CleverEnemy cleverEnemy : cleverEnemies) {

            cleverEnemy.x -= cleverEnemy.xSpeed;

            //adjust the enemy y value to follow the players movements
            if(cleverEnemy.y > player.y) {
                cleverEnemy.y -= cleverEnemy.ySpeed;
            } else if(cleverEnemy.y < player.y) {
                cleverEnemy.y +=cleverEnemy.ySpeed;
            }

            //if enemy makes it past player
            if (cleverEnemy.x +cleverEnemy.width < 0) {

                if (!cleverEnemy.wasHit) {
                    //end the game
                    isGameOver = true;
                    return;
                }

                //clever enemies can have different speeds up within a limit
                int bound = (int) (10 * screenRatioX);
                cleverEnemy.xSpeed = random.nextInt(bound);

                if (cleverEnemy.xSpeed < 5 * screenRatioX) {
                    cleverEnemy.xSpeed = (int) (10 * screenRatioX);
                }

                //spawn clever enemies at the right of the screen at a random height.
                cleverEnemy.x = screenX;
                cleverEnemy.y = random.nextInt(screenY - cleverEnemy.height);

               cleverEnemy.wasHit = false;
            }

            //check for collisions with player
            if (Rect.intersects(cleverEnemy.getCollisionShape(), player.getCollisionShape())) {

                isGameOver = true;
                return;
            }
        }
    }

    /**
     * method for updating shooter enemies
     */
    private void updateShooterEnemies() {

        for (ShooterEnemy shooterEnemy : shooterEnemies) {

            shooterEnemy.x -= shooterEnemy.xSpeed;

            /*
            a recharge counter is increased every time the game updates
            enemies fire when this counter is divided with no remainder
            this enables enemies to automatically fire periodically
             */
            if (rechargeCounter % 50 == 0) {

                //create new enemy laser at shooter's position
                EnemyLaser enemyLaser = new EnemyLaser(getResources());
                enemyLaser.x = shooterEnemy.x - shooterEnemy.width;
                enemyLaser.y = shooterEnemy.y + (shooterEnemy.height / 2);
                enemyLasers.add(enemyLaser);
            }

            //if shooter enemy passes player
            if (shooterEnemy.x + shooterEnemy.width < 0) {

                if (!shooterEnemy.wasHit) {

                    //end game
                    isGameOver = true;
                    return;
                }

                //random shooter enemy speed witin bounds
                int bound = (int) (10 * screenRatioX);
                shooterEnemy.xSpeed = random.nextInt(bound);

                if (shooterEnemy.xSpeed < 5 * screenRatioX) {
                    shooterEnemy.xSpeed = (int) (10 * screenRatioX);
                }

                //shooter enemies spawn on the right of the screen at random heights
                shooterEnemy.x = screenX;
                shooterEnemy.y = random.nextInt(screenY - shooterEnemy.height);

                shooterEnemy.wasHit = false;
            }

            //check for collision with player
            if (Rect.intersects(shooterEnemy.getCollisionShape(), player.getCollisionShape())) {

                isGameOver = true;
                return;
            }
        }
    }

    /**
     * method for drawing objects to the game screen
     */
    private void draw() {

        if (getHolder().getSurface().isValid()) {
            Canvas canvas = getHolder().lockCanvas();
            canvas.drawBitmap(background1.background, background1.x, background1.y, paint);
            canvas.drawBitmap(background2.background, background2.x, background2.y, paint);

            for (PassiveEnemy passiveEnemy : passiveEnemies) {
                canvas.drawBitmap(passiveEnemy.getEnemy(), passiveEnemy.x, passiveEnemy.y, paint);
            }

            for (CleverEnemy cleverEnemy : cleverEnemies) {
                canvas.drawBitmap(cleverEnemy.getEnemy(), cleverEnemy.x, cleverEnemy.y, paint);
            }

            for (ShooterEnemy shooterEnemy : shooterEnemies) {
                canvas.drawBitmap(shooterEnemy.getEnemy(), shooterEnemy.x, shooterEnemy.y, paint);
            }

            for (PlayerLaser playerLaser : playerLasers) {
                canvas.drawBitmap(playerLaser.PlayerLaser, playerLaser.x, playerLaser.y, paint);
            }

            for (EnemyLaser enemyLaser : enemyLasers) {
                canvas.drawBitmap(enemyLaser.EnemyLaser, enemyLaser.x, enemyLaser.y, paint);
            }

            canvas.drawBitmap(player.getPlayer(), player.x, player.y, paint);

            canvas.drawText(score + "", screenX/ 2f, 164, paint);

            //check if the game is over and the game does not need to be redrawn
            if (isGameOver) {
                isPlaying = false;
                getHolder().unlockCanvasAndPost(canvas);
                saveLastScore();
                waitBeforeExiting (); // pause for player clarity
                return;
            }
            getHolder().unlockCanvasAndPost(canvas);
        }
    }

    /**
     * method called when exiting the game
     * ensures user knows they have lost
     */
    private void waitBeforeExiting() {

        try {
            Thread.sleep(3000);
            activity.startActivity(new Intent(activity, MainActivity.class));
            activity.finish();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * method for saving most recent score to system preferences
     * allows user to submit score to highscore system
     */
    private void saveLastScore() {

            SharedPreferences.Editor editor = preferences.edit();
            editor.putInt("lastscore", score);
            editor.apply();

    }

    /**
     * method for resuming the game when back in focus
     */
    public void resume() {

        isPlaying = true;
        thread = new Thread(this);
        thread.start();
    }

    /**
     * method for pausing the game if it is out of focus
     */
    public void pause () {
        try {
            isPlaying = false;
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * method for handling touch events
     * moves player and shoots lasers
     * @param event
     * @return
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (event.getX() < screenX / 2 && event.getY() > screenY / 2) {
                    player.isGoingUp = true;
                }
                if (event.getX() < screenX / 2 && event.getY() < screenY / 2) {
                    player.isGoingDown = true;
                }
                break;
            case MotionEvent.ACTION_UP:
                player.isGoingUp = false;
                player.isGoingDown = false;
                if (event.getX() > screenX / 2)
                    player.toShoot++;
                break;
        }

        return true;
    }

    /**
     * method called to create a new player laser
     */
    public void newLaser() {

        //laser created at player position
        PlayerLaser playerLaser = new PlayerLaser(getResources());
        playerLaser.x = player.x + player.width;
        playerLaser.y = player.y + (player.height / 2);
        playerLasers.add(playerLaser);
    }
}
