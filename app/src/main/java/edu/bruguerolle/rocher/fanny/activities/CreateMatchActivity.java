package edu.bruguerolle.rocher.fanny.activities;

import android.content.Intent;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import edu.bruguerolle.rocher.fanny.R;

import static edu.bruguerolle.rocher.fanny.activities.RecordMatchActivity.PLAYER_1;
import static edu.bruguerolle.rocher.fanny.activities.RecordMatchActivity.PLAYER_2;

public class CreateMatchActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_match);
        Button go = findViewById(R.id.startMatchButton);
        go.setOnClickListener(startMatchHandler);
    }

    View.OnClickListener startMatchHandler = new View.OnClickListener() {
        public void onClick(View v) {
            Intent startRecordActivity = new Intent(getApplicationContext(), RecordMatchActivity.class);
            String player1Name = ((TextInputEditText) findViewById(R.id.player1Name)).getText().toString();
            startRecordActivity.putExtra(PLAYER_1, player1Name);
            String player2Name = ((TextInputEditText) findViewById(R.id.player2Name)).getText().toString();
            startRecordActivity.putExtra(PLAYER_2, player2Name);
            startActivity(startRecordActivity);
        }
    };
}
