package edu.bruguerolle.rocher.fanny.activities;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;

import java.util.List;

import edu.bruguerolle.rocher.fanny.R;
import edu.bruguerolle.rocher.fanny.db.DBHelper;
import edu.bruguerolle.rocher.fanny.fragments.MatchControlsFragment;
import edu.bruguerolle.rocher.fanny.fragments.ScoreFragment;
import edu.bruguerolle.rocher.fanny.model.Match;
import edu.bruguerolle.rocher.fanny.model.Player;
import edu.bruguerolle.rocher.fanny.services.WebService;

import static edu.bruguerolle.rocher.fanny.activities.PhotoActivity.ARG_FANNY;
import static edu.bruguerolle.rocher.fanny.activities.PhotoActivity.ARG_LOSER;
import static edu.bruguerolle.rocher.fanny.activities.PhotoActivity.ARG_SCORE;
import static edu.bruguerolle.rocher.fanny.activities.PhotoActivity.ARG_WINNER;

public class RecordMatchActivity extends AppCompatActivity
        implements MatchControlsFragment.MatchControlsButtonsListener,
        ScoreFragment.ScoreChangeListener {

    public static final String PLAYER_1 = "player_1";
    public static final String PLAYER_2 = "player_2";

    private static final String PLAYER1_SCORE_FRAG      = "fragmentScorePlayer1";
    private static final String PLAYER1_CONTROLS_FRAG   = "fragmentControlsPlayer1";
    private static final String PLAYER2_SCORE_FRAG      = "fragmentScorePlayer2";
    private static final String PLAYER2_CONTROLS_FRAG   = "fragmentControlsPlayer2";

    private static final int LOC_CODE = 6;

    private Match match;
    DBHelper myDB;

    private MatchControlsFragment controlsFragmentPlayer1;
    private ScoreFragment scoreFragmentPlayer1;
    private MatchControlsFragment controlsFragmentPlayer2;
    private ScoreFragment scoreFragmentPlayer2;

    private LocationManager locationManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record_match);

        myDB = new DBHelper(this);

        this.match = new Match(getApplicationContext());
        Bundle bundle = getIntent().getExtras();
        Player player1 = new Player();
        Player player2 = new Player();
        if(bundle != null) {
            player1.setName(!TextUtils.isEmpty(bundle.getString(PLAYER_1)) ?
                    bundle.getString(PLAYER_1) :
                    "Player 1");
            player2.setName(!TextUtils.isEmpty(bundle.getString(PLAYER_2)) ?
                    bundle.getString(PLAYER_2) :
                    "Player 2");
        }
        match.setPlayer1(player1);
        match.setPlayer2(player2);

        FragmentManager supportFragmentManager = getSupportFragmentManager();
        if (savedInstanceState != null) {
            //Restore the fragment's instance
            scoreFragmentPlayer1 = (ScoreFragment)supportFragmentManager
                    .getFragment(savedInstanceState, PLAYER1_SCORE_FRAG);
            controlsFragmentPlayer1 = (MatchControlsFragment) supportFragmentManager
                    .getFragment(savedInstanceState, PLAYER1_CONTROLS_FRAG);
            scoreFragmentPlayer2 = (ScoreFragment)supportFragmentManager
                    .getFragment(savedInstanceState, PLAYER2_SCORE_FRAG);
            controlsFragmentPlayer2 = (MatchControlsFragment) supportFragmentManager
                    .getFragment(savedInstanceState, PLAYER2_CONTROLS_FRAG);
        } else {
            scoreFragmentPlayer1 = ScoreFragment.newInstance(false);
            controlsFragmentPlayer1 = MatchControlsFragment.newInstance(PLAYER_1, player1.getName());
            controlsFragmentPlayer2 = MatchControlsFragment.newInstance(PLAYER_2, player2.getName());
            scoreFragmentPlayer2 = ScoreFragment.newInstance(true);
            FragmentTransaction transac = supportFragmentManager.beginTransaction();
            transac.replace(R.id.scoreContainer1, scoreFragmentPlayer1);
            transac.replace(R.id.controlsContainer1, controlsFragmentPlayer1);
            transac.replace(R.id.controlsContainer2, controlsFragmentPlayer2);
            transac.replace(R.id.scoreContainer2, scoreFragmentPlayer2);
            transac.commit();
        }

        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                LOC_CODE);
        this.locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        //Save the fragment's instance
        getSupportFragmentManager().putFragment(outState, PLAYER1_CONTROLS_FRAG, controlsFragmentPlayer1);
        getSupportFragmentManager().putFragment(outState, PLAYER1_SCORE_FRAG, scoreFragmentPlayer1);
        getSupportFragmentManager().putFragment(outState, PLAYER2_CONTROLS_FRAG, controlsFragmentPlayer2);
        getSupportFragmentManager().putFragment(outState, PLAYER2_SCORE_FRAG, scoreFragmentPlayer2);
    }

    @Override
    public void onPointMarked(boolean isOpponent, boolean isPositive) {
        Player player;
        if(isOpponent) {
            player = match.getPlayer1();
        } else {
            player = match.getPlayer2();
        }
        player.setScore(isPositive ? player.getScore()+1 : player.getScore()-1);
    }

    @Override
    public void onMatchOver(boolean isOpponent) {

        Location curLoc = getLastKnownLocation();
        assert curLoc != null;
        match.setLatitude(curLoc.getLatitude());
        match.setLongitude(curLoc.getLongitude());

        // Insert a new record
        myDB.insertData(match);

        WebService.startActionPost(getApplicationContext(), match);

        Intent photoActivityIntent = new Intent(RecordMatchActivity.this, PhotoActivity.class);
        photoActivityIntent.putExtra(ARG_WINNER, isOpponent ?
                        match.getPlayer1().getName() :
                        match.getPlayer2().getName());
        Player loser = isOpponent ?
                match.getPlayer2() :
                match.getPlayer1();
        photoActivityIntent.putExtra(ARG_FANNY, loser.getScore() == 0);
        photoActivityIntent.putExtra(ARG_LOSER, loser.getName());
        photoActivityIntent.putExtra(ARG_SCORE, match.getPlayer1().getScore()+" - "+match.getPlayer2().getScore());
        photoActivityIntent.putExtra(ARG_SCORE, match.getPlayer1().getScore()+" - "+match.getPlayer2().getScore());
        startActivity(photoActivityIntent);
    }

    @Override
    public void onBeerClicked(String playerId) {
        Player playerById = getPlayer(playerId);
        if(playerById != null) {
            playerById.setBeerNb(playerById.getBeerNb()+1);
        }
    }

    @Override
    public void onGamelleClicked(String playerId) {
        Player playerById = getPlayer(playerId);
        if(playerById != null) {
            playerById.setGammelleNb(playerById.getGammelleNb()+1);
        }
    }

    @Override
    public void onCendarClicked(String playerId) {
        Player playerById = getPlayer(playerId);
        if(playerById != null) {
            playerById.setCendarNb(playerById.getCendarNb()+1);
        }
    }

    @Override
    public void onPissetteClicked(String playerId) {
        Player playerById = getPlayer(playerId);
        if(playerById != null) {
            playerById.setPissetteNb(playerById.getPissetteNb()+1);
        }
    }

    private Player getPlayer(String playerIdentifier) {
        return playerIdentifier.equals(PLAYER_1) ? match.getPlayer1() : match.getPlayer2();
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if(requestCode == LOC_CODE) {
            getLastKnownLocation();
        }
    }


    private Location getLastKnownLocation() {
        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                    LOC_CODE);
            return null;
        }

        List<String> providers = locationManager.getProviders(true);
        Location bestLocation = null;
        for (String provider : providers) {
            Location l = locationManager.getLastKnownLocation(provider);
            if (l == null) {
                continue;
            }
            if (bestLocation == null || l.getAccuracy() < bestLocation.getAccuracy()) {
                // Found best last known location: %s", l);
                bestLocation = l;
            }
        }
        return bestLocation;
    }
}
