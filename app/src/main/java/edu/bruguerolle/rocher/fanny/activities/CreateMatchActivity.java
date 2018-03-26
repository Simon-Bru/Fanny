package edu.bruguerolle.rocher.fanny.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import edu.bruguerolle.rocher.fanny.R;

public class CreateMatchActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_match);
        Button go = findViewById(R.id.startMatchButton);
        go.setOnClickListener(startMatchHandler);
    }

    View.OnClickListener startMatchHandler = new View.OnClickListener() {
        public void onClick(View v) {
            Intent startRecordActivity = new Intent(CreateMatchActivity.this,RecordMatchActivity.class);
            startActivity(startRecordActivity);
        }
    };
}
