package edu.bruguerolle.rocher.fanny.model;

public class Player {

    private int id;
    private String name;
    private int score       = 0;
    private int playerBeer  = 0;
    private int gammelleNb  = 0;
    private int cendarNb    = 0;
    private int pissetNb    = 0;
    private boolean fanny   = false;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getPlayerBeer() {
        return playerBeer;
    }

    public void setPlayerBeer(int playerBeer) {
        this.playerBeer = playerBeer;
    }

    public int getGammelleNb() {
        return gammelleNb;
    }

    public void setGammelleNb(int gammelleNb) {
        this.gammelleNb = gammelleNb;
    }

    public int getCendarNb() {
        return cendarNb;
    }

    public void setCendarNb(int cendarNb) {
        this.cendarNb = cendarNb;
    }

    public int getPissetNb() {
        return pissetNb;
    }

    public void setPissetNb(int pissetNb) {
        this.pissetNb = pissetNb;
    }

    public boolean isFanny() {
        return fanny;
    }

    public void setFanny(boolean fanny) {
        this.fanny = fanny;
    }

    public Player() {
    }

}
