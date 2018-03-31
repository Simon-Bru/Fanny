package edu.bruguerolle.rocher.fanny.model;

import org.json.JSONException;
import org.json.JSONObject;

public class Player {

    private String name;
    private int score       = 0;
    private int beerNb      = 0;
    private int gammelleNb  = 0;
    private int cendarNb    = 0;
    private int pissetteNb  = 0;

    public JSONObject toJson() throws JSONException {
        JSONObject json = new JSONObject();
        json.put("name", name);
        json.put("score", score);
        json.put("beerNb", beerNb);
        json.put("gammelleNb", gammelleNb);
        json.put("cendarNb", cendarNb);
        json.put("pissetteNb", pissetteNb);
        return json;
    }

    public static Player fromJson(JSONObject json) throws JSONException {
        Player player = new Player();
        player.setName(json.getString("name"));
        player.setScore(json.getInt("score"));
        player.setBeerNb(json.getInt("beerNb"));
        player.setGammelleNb(json.getInt("gammelleNb"));
        player.setCendarNb(json.getInt("cendarNb"));
        player.setPissetteNb(json.getInt("pissetteNb"));
        return player;
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

    public int getBeerNb() {
        return beerNb;
    }

    public void setBeerNb(int beerNb) {
        this.beerNb = beerNb;
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

    public int getPissetteNb() {
        return pissetteNb;
    }

    public void setPissetteNb(int pissetteNb) {
        this.pissetteNb = pissetteNb;
    }

    public Player() {}
}
