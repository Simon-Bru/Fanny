package edu.bruguerolle.rocher.fanny.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.util.MissingFormatArgumentException;

import edu.bruguerolle.rocher.fanny.R;
import edu.bruguerolle.rocher.fanny.db.DBHelper;
import edu.bruguerolle.rocher.fanny.services.WebService;

public class PhotoActivity extends ImagePickerBaseActivity {
    public static final String ARG_SCORE = "arg_score";
    public static final String ARG_LOSER = "arg_loser";
    public static final String ARG_WINNER = "arg_winner";
    public static final String ARG_FANNY = "arg_fanny";
    public static final String ARG_INSERTED_ID = "arg_inserted_id";

    private long matchId;
    private String loser;
    private String winner;
    private String score;
    private boolean isFanny;

    private Uri imageUri;

    private DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo);

        dbHelper = new DBHelper(this);

        Bundle bundle = getIntent().getExtras();
        if(bundle == null) {
            throw new MissingFormatArgumentException("You must provide winner loser and score to launch this activity");
        }
        loser = bundle.getString(ARG_LOSER);
        winner = bundle.getString(ARG_WINNER);
        score = bundle.getString(ARG_SCORE);
        isFanny = bundle.getBoolean(ARG_FANNY);
        matchId = bundle.getLong(ARG_INSERTED_ID);

        if(isFanny) {
            findViewById(R.id.fannyText).setVisibility(View.VISIBLE);
        }

        ((TextView)findViewById(R.id.winnerText)).setText(winner+" wins !");
        ((TextView)findViewById(R.id.loserText)).setText(loser);
        ((TextView)findViewById(R.id.scoreText)).setText(score);

        findViewById(R.id.pickBtn).setOnClickListener(pickPhoto);
        findViewById(R.id.nextBtn).setOnClickListener(next);
    }

    View.OnClickListener pickPhoto = new View.OnClickListener(){
        @Override
        public void onClick(View v) {
            startCameraOrGalleryIntent(R.id.matchPicture, "fanny_"+matchId);
        }
    };

    View.OnClickListener next = new View.OnClickListener(){
        @Override
        public void onClick(View v) {
            /*
             We update the data we inserted with the image path
              */
            // REMOTE API
            SharedPreferences sharedPreferences = getSharedPreferences(getString(R.string.app_name), MODE_PRIVATE);
            String deviceId = sharedPreferences.getString(getString(R.string.device_id), "");
            WebService.startActionPut(getApplicationContext(), deviceId, matchId, imageUri.getPath());

            // LOCAL SQLITE
            new Thread(new Runnable() {
                @Override
                public void run() {
                    dbHelper.updateMatchImgPath(matchId, imageUri.getPath());
                }
            }).start();

            Intent clearIntent = new Intent(getApplicationContext(), MainActivity.class);
            clearIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(clearIntent);
        }
    };

    @Override
    public void onImagePicked(Uri uri) {
        imageUri = uri;
    }
}
