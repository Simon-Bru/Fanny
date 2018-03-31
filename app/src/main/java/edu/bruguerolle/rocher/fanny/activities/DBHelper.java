package edu.bruguerolle.rocher.fanny.activities;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

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
    private static final String SCORE1_COL= "SCORE&";
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
    private static final String LOCATION = "LOCATION";
    private static final String FANNY_COL = "FANNY";

    private static final String CREATE_TABLE = "CREATE TABLE "+TABLE_NAME+
            " ("+UID+" INTEGER PRIMARY KEY AUTOINCREMENT, "+
            PLAYER1NAME_COL+" TEXT ,"+
            PLAYER2NAME_COL+" TEXT, "+
            SCORE1_COL+" INTEGER ," +
            SCORE2_COL+" INTEGER ,"+
            GAMELLE1_COL+" INTEGER, "+
            GAMELLE2_COL+" INTEGER, "+
            BEER1_COL+" INTEGER ," +
            BEER2_COL+" INTEGER ," +
            CENDAR1_COL+" INTEGER ," +
            CENDAR2_COL+" INTEGER ," +
            PISSETTE1_COL+" INTEGER ," +
            PISSETTE2_COL+" INTEGER ," +
            IMAGEPATH+" TEXT ," +
            LOCATION+" DOUBLE ," +
            FANNY_COL+" BOOLEAN ,)";
    private static final String DROP_TABLE ="DROP TABLE IF EXISTS "+TABLE_NAME;


    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);


    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE);
    }

    public void insertData(Player player1, Player player2, double location, boolean fanny){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(PLAYER1NAME_COL,player1.getName());
        contentValues.put(PLAYER2NAME_COL,player2.getName());
        contentValues.put(SCORE1_COL,player1.getScore());
        contentValues.put(SCORE2_COL,player2.getScore());
        contentValues.put(GAMELLE1_COL,player1.getGammelleNb());
        contentValues.put(GAMELLE2_COL,player2.getGammelleNb());
        contentValues.put(BEER1_COL,player1.getPlayerBeer());
        contentValues.put(BEER2_COL,player2.getPlayerBeer());
        contentValues.put(CENDAR1_COL,player1.getCendarNb());
        contentValues.put(CENDAR2_COL,player2.getCendarNb());
        contentValues.put(PISSETTE1_COL,player1.getPissetNb());
        contentValues.put(PISSETTE2_COL,player2.getPissetNb());
        contentValues.put(LOCATION,location);
        contentValues.put(FANNY_COL,fanny);

        db.insert(TABLE_NAME,null ,contentValues);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}