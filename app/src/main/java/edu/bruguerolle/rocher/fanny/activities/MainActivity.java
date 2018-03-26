package edu.bruguerolle.rocher.fanny.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import edu.bruguerolle.rocher.fanny.R;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Button newMatch = findViewById(R.id.NewMatchButton);
        Button previousMatch = findViewById(R.id.PreviousMatchesButton);
        newMatch.setOnClickListener(newMatchHandler);
        previousMatch.setOnClickListener(previousMatchHandler);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_play) {
            // todo start create match activity
        } else if (id == R.id.nav_history) {
//            todo start history
        }  else if (id == R.id.nav_chat) {
//        todo start chat
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    View.OnClickListener newMatchHandler = new View.OnClickListener() {
        public void onClick(View v) {
            Intent startCreateActivity = new Intent(MainActivity.this,CreateMatchActivity.class);
            startActivity(startCreateActivity);
        }
    };
    View.OnClickListener previousMatchHandler = new View.OnClickListener() {
        public void onClick(View v) {
            Intent startPreviousActivity = new Intent(MainActivity.this,PreviousMatches.class);
            startActivity(startPreviousActivity);
        }
    };
}
