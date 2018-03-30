package edu.bruguerolle.rocher.fanny.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import edu.bruguerolle.rocher.fanny.R;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link MatchControlsButtonsListener} interface
 * to handle interaction events.
 * Use the {@link MatchControlsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MatchControlsFragment extends Fragment {
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM_PLAYER        = "param_player";
    private static final String ARG_PARAM_PLAYER_NAME   = "param_player_name";

    private String mPlayer;
    private String mPlayerName;

    private MatchControlsButtonsListener mListener;

    public MatchControlsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *

     * @return A new instance of fragment MatchControlsFragment.
     */
    public static MatchControlsFragment newInstance(String player, String playerName) {
        MatchControlsFragment fragment = new MatchControlsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM_PLAYER, player);
        args.putString(ARG_PARAM_PLAYER_NAME, playerName);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mPlayer = getArguments().getString(ARG_PARAM_PLAYER);
            mPlayerName = getArguments().getString(ARG_PARAM_PLAYER_NAME);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_match_controls, container, false);
        ((TextView)view.findViewById(R.id.PlayerName)).setText(this.mPlayerName);

        /*
        Clicks
         */
        view.findViewById(R.id.gamelleBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onGamelleClicked(mPlayer);
            }
        });
        view.findViewById(R.id.cendarBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onCendarClicked(mPlayer);
            }
        });
        view.findViewById(R.id.pissetteBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onPissetteClicked(mPlayer);
            }
        });
        view.findViewById(R.id.beerBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onBeerClicked(mPlayer);
            }
        });
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState != null) {
            mPlayerName = savedInstanceState.getString(ARG_PARAM_PLAYER_NAME);
            mPlayer = savedInstanceState.getString(ARG_PARAM_PLAYER);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle state) {
        super.onSaveInstanceState(state);
        state.putString(ARG_PARAM_PLAYER, mPlayer);
        state.putString(ARG_PARAM_PLAYER_NAME, mPlayerName);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof MatchControlsButtonsListener) {
            mListener = (MatchControlsButtonsListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface MatchControlsButtonsListener {
        void onBeerClicked(String playerId);
        void onGamelleClicked(String playerId);
        void onCendarClicked(String playerId);
        void onPissetteClicked(String playerId);
    }
}
