package com.example.androidspringcoursework;

/**
 * Class for the score being sent to the database
 */

public class Score {

    String name, score;

    public Score(String name, String score) {
        this.name = name;
        this.score = score;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }
}
