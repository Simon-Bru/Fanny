package edu.bruguerolle.rocher.fanny.model;


import com.google.android.gms.maps.model.LatLng;

public class Match {

    private int id;
    private Player player1;
    private Player player2;
    private boolean fanny;
    private String imgPath;
    private LatLng location;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public LatLng getLocation() {
        return location;
    }

    public void setLocation(LatLng location) {
        this.location = location;
    }

    public boolean isFanny() {
        return fanny;
    }

    public void setFanny(boolean fanny) {
        this.fanny = fanny;
    }
}
