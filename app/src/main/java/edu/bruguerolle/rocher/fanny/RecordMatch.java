package edu.bruguerolle.rocher.fanny;

import android.app.Fragment;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class RecordMatch extends AppCompatActivity implements DeroulementMatch.InterfaceButtonsListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.record_match);
        FragmentTransaction transac = getSupportFragmentManager().beginTransaction();
        transac.replace(R.id.RecordFragment, DeroulementMatch.newInstance()).commit();

        FragmentTransaction transac2 = getSupportFragmentManager().beginTransaction();
        transac2.replace(R.id.RecordFragment2, DeroulementMatch.newInstance()).commit();

        Button stopMatch = findViewById(R.id.StopMatchButton);
        stopMatch.setOnClickListener(stopMatchHandler);

    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    View.OnClickListener stopMatchHandler = new View.OnClickListener() {
        public void onClick(View v) {
            Intent stopRecordActivity = new Intent(RecordMatch.this,PhotoActivity.class);
            startActivity(stopRecordActivity);
        }
    };
}
