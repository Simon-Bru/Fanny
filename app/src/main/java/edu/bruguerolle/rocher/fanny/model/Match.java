package edu.bruguerolle.rocher.fanny.model;


import android.content.Context;
import android.content.SharedPreferences;

import org.json.JSONException;
import org.json.JSONObject;

import edu.bruguerolle.rocher.fanny.R;

public class Match {

    private int id;
    private String deviceId;
    private Player player1;
    private Player player2;
    private boolean fanny;
    private String imgPath;
    private double longitude;
    private double latitude;

    public Match(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(context.getString(R.string.app_name), Context.MODE_PRIVATE);
        deviceId = sharedPreferences.getString(context.getString(R.string.device_id), "");
    }

    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        try {
            json.put("id", id);
            json.put("deviceId", deviceId);
            json.put("player1", player1.toJson());
            json.put("player2", player2.toJson());
            json.put("fanny", fanny);
            json.put("imgPath", imgPath);
            json.put("longitude", longitude);
            json.put("latitude", latitude);
            return json;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public Player getPlayer1() {
        return player1;
    }

    public void setPlayer1(Player player1) {
        this.player1 = player1;
    }

    public Player getPlayer2() {
        return player2;
    }

    public void setPlayer2(Player player2) {
        this.player2 = player2;
    }

    public String getImgPath() {
        return imgPath;
    }

    public void setImgPath(String imgPath) {
        this.imgPath = imgPath;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public boolean isFanny() {
        return fanny;
    }

    public void setFanny(boolean fanny) {
        this.fanny = fanny;
    }
}
