package edu.bruguerolle.rocher.fanny.activities;

import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ArrayAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import edu.bruguerolle.rocher.fanny.R;
import edu.bruguerolle.rocher.fanny.adapter.MatchesAdapter;
import edu.bruguerolle.rocher.fanny.db.DBHelper;
import edu.bruguerolle.rocher.fanny.model.Match;
import edu.bruguerolle.rocher.fanny.services.WebService;

public class PreviousMatches extends AppCompatActivity {

    private List<Match> matches;

    private RecyclerView mRecyclerView;
    private MatchesAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_previous_matches);
        mRecyclerView = findViewById(R.id.matchRecycler);

        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        findViewById(R.id.more).setOnClickListener(loadMore);

        new GetMatchesTask(this).execute();
    }

    private class GetMatchesTask extends AsyncTask<Void, Void, List<Match>> {

        PreviousMatches activity;

        GetMatchesTask(PreviousMatches activity) {
            this.activity = activity;
        }

        @Override
        protected List<Match> doInBackground(Void... voids) {
            DBHelper db = new DBHelper(activity);
            return db.getPreviousMatches();
        }

        @Override
        protected void onPostExecute(List<Match> result) {
            activity.matches = result;
            activity.mAdapter = new MatchesAdapter(activity.matches, getApplicationContext());
            activity.mRecyclerView.setAdapter(activity.mAdapter);
        }
    }

    private View.OnClickListener loadMore = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            SharedPreferences sharedPreferences = getSharedPreferences(getString(R.string.app_name), MODE_PRIVATE);
            WebService.startActionGet(getApplicationContext(),
                    sharedPreferences.getString(getString(R.string.device_id), ""), callback);
            findViewById(R.id.more).setVisibility(View.GONE);
        }
    };

    private ResultReceiver callback = new ResultReceiver(new Handler()){
        @Override
        protected void onReceiveResult(int resultCode, Bundle resultData) {
            if(resultCode == WebService.CALLBACK_CODE) {
                String jsonString = resultData.getString(WebService.EXTRA_CODE);
                try {
                    JSONArray jsonArray = new JSONArray(jsonString);
                    for(int i = 0; i < jsonArray.length(); i++) {
                        matches.add(Match.fromJson(jsonArray.getJSONObject(i)));
                    }

                    mAdapter.notifyItemInserted(matches.size() - 1);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    };
}