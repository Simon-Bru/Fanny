package edu.bruguerolle.rocher.fanny;

import android.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class RecordMatch extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.record_match);
        FragmentTransaction transac = getSupportFragmentManager().beginTransaction();
        //Fragment fragment =  transac.replace(R.id.RecordFragment, );

    }

}
