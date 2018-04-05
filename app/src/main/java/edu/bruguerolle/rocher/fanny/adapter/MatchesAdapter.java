package edu.bruguerolle.rocher.fanny.adapter;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import edu.bruguerolle.rocher.fanny.R;
import edu.bruguerolle.rocher.fanny.model.Match;
import edu.bruguerolle.rocher.fanny.model.Player;

public class MatchesAdapter extends RecyclerView.Adapter<MatchesAdapter.MatchViewHolder> {

    private List<Match> matches;

    private Context context;

    private Geocoder geocoder;

    public MatchesAdapter(List<Match> data, Context context) {
        matches = data;
        this.context = context;
        this.geocoder = new Geocoder(context, Locale.getDefault());
    }

    private String getAddressFromGeo(double longitude, double latitude) {
        try {
            List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);

            if (addresses != null) {
                Address returnedAddress = addresses.get(0);
                StringBuilder strReturnedAddress = new StringBuilder();
                return returnedAddress.getThoroughfare();
            }
            else {
                return "";
            }
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
    }

    @NonNull
    @Override
    public MatchViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_match, parent, false);
        return new MatchViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MatchViewHolder holder, int position) {
        Match curMatch = matches.get(position);

        holder.fanny.setVisibility(curMatch.isFanny() ? View.VISIBLE : View.GONE);
        String addressFromGeo = getAddressFromGeo(curMatch.getLongitude(), curMatch.getLatitude());
        holder.location.setText(addressFromGeo);
        if(!TextUtils.isEmpty(curMatch.getImgPath())) {
            holder.picture.setImageURI(Uri.parse(curMatch.getImgPath()));
            holder.picture.setVisibility(View.VISIBLE);
        }
        Player player1 = curMatch.getPlayer1();
        holder.player1Name.setText(player1.getName());
        holder.player1Gamelle.setText(Integer.toString(player1.getGammelleNb()));
        holder.player1Pissette.setText(Integer.toString(player1.getPissetteNb()));
        holder.player1Score.setText(Integer.toString(player1.getScore()));
        holder.player1Cendar.setText(Integer.toString(player1.getCendarNb()));
        holder.player1Beer.setText(Integer.toString(player1.getBeerNb()));

        Player player2 = curMatch.getPlayer2();
        holder.player2Name.setText(player2.getName());
        holder.player2Gamelle.setText(Integer.toString(player2.getGammelleNb()));
        holder.player2Pissette.setText(Integer.toString(player2.getPissetteNb()));
        holder.player2Score.setText(Integer.toString(player2.getScore()));
        holder.player2Cendar.setText(Integer.toString(player2.getCendarNb()));
        holder.player2Beer.setText(Integer.toString(player2.getBeerNb()));
    }

    @Override
    public int getItemCount() {
        return matches.size();
    }

    public static class MatchViewHolder extends RecyclerView.ViewHolder {
        public View view;
        public TextView location;
        public TextView fanny;
        public ImageView picture;

        public TextView player1Name;
        public TextView player1Score;
        public TextView player1Beer;
        public TextView player1Cendar;
        public TextView player1Pissette;
        public TextView player1Gamelle;

        public TextView player2Name;
        public TextView player2Score;
        public TextView player2Beer;
        public TextView player2Cendar;
        public TextView player2Pissette;
        public TextView player2Gamelle;

        public MatchViewHolder(View itemView) {
            super(itemView);
            view = itemView;
            location = view.findViewById(R.id.location);
            fanny = view.findViewById(R.id.fannyText);
            picture = view.findViewById(R.id.matchPicture);

            player1Name = view.findViewById(R.id.player1Name);
            player1Beer = view.findViewById(R.id.player1Beer);
            player1Cendar = view.findViewById(R.id.player1Cendar);
            player1Score = view.findViewById(R.id.player1Score);
            player1Pissette = view.findViewById(R.id.player1Pissettes);
            player1Gamelle = view.findViewById(R.id.player1Gammelle);

            player2Name = view.findViewById(R.id.player2Name);
            player2Beer = view.findViewById(R.id.player2Beer);
            player2Cendar = view.findViewById(R.id.player2Cendar);
            player2Score = view.findViewById(R.id.player2Score);
            player2Pissette = view.findViewById(R.id.player2Pissettes);
            player2Gamelle = view.findViewById(R.id.player2Gammelle);
        }
    }
}
