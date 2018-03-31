package edu.bruguerolle.rocher.fanny.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

import edu.bruguerolle.rocher.fanny.model.Match;
import edu.bruguerolle.rocher.fanny.model.Player;

/**
 * Created by cleme on 31/03/2018.
 */

public class DBHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "Match.db";
    public static final String TABLE_NAME = "myMatch";
    private static final String UID="_id";
    private static final String PLAYER1NAME_COL= "PLAYER1NAME";
    private static final String PLAYER2NAME_COL= "PLAYER2NAME";
    private static final String SCORE1_COL= "SCORE1";
    private static final String SCORE2_COL= "SCORE2";
    private static final String GAMELLE1_COL= "GAMELLE1";
    private static final String GAMELLE2_COL= "GAMELLE2";
    private static final String BEER1_COL= "BEER1";
    private static final String BEER2_COL= "BEER2";
    private static final String CENDAR1_COL= "CENDAR1";
    private static final String CENDAR2_COL = "CENDAR2";
    private static final String PISSETTE1_COL = "PISSETTE1";
    private static final String PISSETTE2_COL = "PISSETTE2";
    private static final String IMAGEPATH = "IMAGEPATH";
    private static final String LONGITUDE = "LONGITUDE";
    private static final String LATITUDE = "LATITUDE";
    private static final String FANNY_COL = "FANNY";

    private static final String CREATE_TABLE = "CREATE TABLE "+TABLE_NAME+
            " ("+UID+" INTEGER PRIMARY KEY AUTOINCREMENT, "+
            PLAYER1NAME_COL+" VARCHAR, "+
            PLAYER2NAME_COL+" VARCHAR, "+
            SCORE1_COL+" INTEGER, "+
            SCORE2_COL+" INTEGER ,"+
            GAMELLE1_COL+" INTEGER, "+
            GAMELLE2_COL+" INTEGER, "+
            BEER1_COL+" INTEGER ," +
            BEER2_COL+" INTEGER ," +
            CENDAR1_COL+" INTEGER ," +
            CENDAR2_COL+" INTEGER ," +
            PISSETTE1_COL+" INTEGER ," +
            PISSETTE2_COL+" INTEGER ," +
            IMAGEPATH+" VARCHAR ," +
            LONGITUDE +" DOUBLE ," +
            LATITUDE +" DOUBLE ," +
            FANNY_COL+" BOOLEAN)";
    private static final String DROP_TABLE ="DROP TABLE IF EXISTS "+TABLE_NAME;


    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);


    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE);
    }

    public long insertMatch(Match match){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(PLAYER1NAME_COL,match.getPlayer1().getName());
        contentValues.put(PLAYER2NAME_COL,match.getPlayer2().getName());
        contentValues.put(SCORE1_COL,match.getPlayer1().getScore());
        contentValues.put(SCORE2_COL,match.getPlayer2().getScore());
        contentValues.put(GAMELLE1_COL,match.getPlayer1().getGammelleNb());
        contentValues.put(GAMELLE2_COL,match.getPlayer2().getGammelleNb());
        contentValues.put(BEER1_COL,match.getPlayer1().getBeerNb());
        contentValues.put(BEER2_COL,match.getPlayer2().getBeerNb());
        contentValues.put(CENDAR1_COL,match.getPlayer1().getCendarNb());
        contentValues.put(CENDAR2_COL,match.getPlayer2().getCendarNb());
        contentValues.put(PISSETTE1_COL,match.getPlayer1().getPissetteNb());
        contentValues.put(PISSETTE2_COL,match.getPlayer2().getPissetteNb());
        contentValues.put(LONGITUDE,match.getLongitude());
        contentValues.put(LATITUDE,match.getLatitude());
        contentValues.put(FANNY_COL,match.isFanny());

        return db.insert(TABLE_NAME,null ,contentValues);
    }

    public void updateMatchImgPath(long matchId, String imgPath) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(IMAGEPATH, imgPath);

        db.update(TABLE_NAME, values, UID+"="+matchId, null);
    }

    public List<Match> getPreviousMatches() {
        List<Match> matches = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_NAME, null, null,null,null,null,null,"5");
        Match match;
        Player player1, player2;
        while(cursor.moveToNext()) {
            match = new Match();
            player1 = new Player();
            player2 = new Player();
            player1.setName(cursor.getString(cursor.getColumnIndex(PLAYER1NAME_COL)));
            player2.setName(cursor.getString(cursor.getColumnIndex(PLAYER2NAME_COL)));
            player1.setScore(cursor.getInt(cursor.getColumnIndex(SCORE1_COL)));
            player2.setScore(cursor.getInt(cursor.getColumnIndex(SCORE2_COL)));
            player1.setCendarNb(cursor.getInt(cursor.getColumnIndex(CENDAR1_COL)));
            player2.setCendarNb(cursor.getInt(cursor.getColumnIndex(CENDAR2_COL)));
            player1.setGammelleNb(cursor.getInt(cursor.getColumnIndex(GAMELLE1_COL)));
            player2.setGammelleNb(cursor.getInt(cursor.getColumnIndex(GAMELLE2_COL)));
            player1.setBeerNb(cursor.getInt(cursor.getColumnIndex(BEER1_COL)));
            player2.setBeerNb(cursor.getInt(cursor.getColumnIndex(BEER2_COL)));
            player1.setPissetteNb(cursor.getInt(cursor.getColumnIndex(PISSETTE1_COL)));
            player2.setPissetteNb(cursor.getInt(cursor.getColumnIndex(PISSETTE2_COL)));

            match.setPlayer1(player1);
            match.setPlayer2(player2);
            match.setFanny(cursor.getInt(cursor.getColumnIndex(FANNY_COL)) > 0);
            match.setId(cursor.getLong(cursor.getColumnIndex(UID)));
            match.setLongitude(cursor.getDouble(cursor.getColumnIndex(LONGITUDE)));
            match.setLatitude(cursor.getDouble(cursor.getColumnIndex(LATITUDE)));
            match.setImgPath(cursor.getString(cursor.getColumnIndex(IMAGEPATH)));
            matches.add(match);
        }

        return matches;
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
