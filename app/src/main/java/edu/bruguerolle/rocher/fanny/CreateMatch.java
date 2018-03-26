package edu.bruguerolle.rocher.fanny;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class CreateMatch extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_match);
        Button go = findViewById(R.id.startMatchButton);
        go.setOnClickListener(startMatchHandler);
    }

    View.OnClickListener startMatchHandler = new View.OnClickListener() {
        public void onClick(View v) {
            Intent startRecordActivity = new Intent(CreateMatch.this,RecordMatch.class);
            startActivity(startRecordActivity);
        }
    };
}
