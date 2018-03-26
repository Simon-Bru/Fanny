package edu.bruguerolle.rocher.fanny.fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.ConstraintSet;
import android.support.v4.app.Fragment;
import android.text.TextWatcher;
import android.transition.TransitionManager;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import edu.bruguerolle.rocher.fanny.R;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ScoreChangeListener} interface
 * to handle interaction events.
 * Use the {@link ScoreFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ScoreFragment extends Fragment {

    private final int[] pointsImg = {
            R.id.point1,
            R.id.point2,
            R.id.point3,
            R.id.point4,
            R.id.point5,
            R.id.point6,
            R.id.point7,
            R.id.point8,
            R.id.point9,
            R.id.point10
    };

    private int remainingPoints = 9;

    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_IS_OPPONENT = "is_opponent";
    private static final String ARG_REMAINING_PTS = "remaining_points";

    private boolean isRed;

    private View root;

    private ScoreChangeListener mListener;

    private GestureDetector mGestureListener;

    public ScoreFragment() {}

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param isOpponent Parameter 1.
     * @return A new instance of fragment ScoreFragment.
     */
    public static ScoreFragment newInstance(boolean isOpponent) {
        ScoreFragment fragment = new ScoreFragment();
        Bundle args = new Bundle();

        args.putBoolean(ARG_IS_OPPONENT, isOpponent);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            isRed = getArguments().getBoolean(ARG_IS_OPPONENT);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_score, container, false);

        if(!isRed) {
            for (int point : pointsImg) {
                ImageView pointImg = view.findViewById(point);
                pointImg.setImageDrawable(getActivity().getDrawable(R.drawable.blue_point));
            }
        }

        mGestureListener = new GestureDetector(getContext(), new PointGestureListener());

        view.setOnTouchListener(new View.OnTouchListener() {
            @SuppressLint("ClickableViewAccessibility")
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return mGestureListener.onTouchEvent(event);
            }
        });

        root = view;
        return root;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState != null) {
            isRed = savedInstanceState.getBoolean(ARG_IS_OPPONENT);
            remainingPoints = pointsImg.length - 1;

            while(remainingPoints > savedInstanceState.getInt(ARG_REMAINING_PTS)) {
                movePoint(true);
            }
        }
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onSaveInstanceState(Bundle state) {
        super.onSaveInstanceState(state);
        state.putBoolean(ARG_IS_OPPONENT, isRed);
        state.putInt(ARG_REMAINING_PTS, remainingPoints);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof ScoreChangeListener) {
            mListener = (ScoreChangeListener)context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement ScoreChangeListener");
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
    public interface ScoreChangeListener {
        void onPointMarked(boolean isRed, boolean isPositive);
        void onMatchOver(boolean isRed);
    }

    private void movePoint(boolean isPosivitive) {
        ConstraintLayout container = root.findViewById(R.id.pointsContainer);
        TransitionManager.beginDelayedTransition(container);

        ImageView point;
        ConstraintSet constraintSet = new ConstraintSet();
        constraintSet.clone(container);

        if(isPosivitive && remainingPoints >= 0) {
            point = root.findViewById(pointsImg[remainingPoints]);
            constraintSet = pullRight(constraintSet, point.getId());

            if(remainingPoints == 0) {
                mListener.onMatchOver(isRed);
            } else {
                remainingPoints =  remainingPoints - 1;
            }
        } else if(remainingPoints < pointsImg.length-1){
            remainingPoints++;

            point = root.findViewById(pointsImg[remainingPoints]);
            constraintSet = pullLeft(constraintSet, point.getId());
        }

        constraintSet.applyTo(container);
    }

    private ConstraintSet pullRight(ConstraintSet constraintSet, int pointId) {
        if(remainingPoints < pointsImg.length - 1) {
            constraintSet.connect(pointId, ConstraintSet.END,
                    pointsImg[remainingPoints+1], ConstraintSet.END,35);
        } else {
            constraintSet.connect(pointId, ConstraintSet.END, ConstraintSet.PARENT_ID, ConstraintSet.END,0);
        }
        constraintSet.clear(pointId, ConstraintSet.START);

        return constraintSet;
    }

    private ConstraintSet pullLeft(ConstraintSet constraintSet, int pointId) {
        constraintSet.connect(pointId, ConstraintSet.START,
                (remainingPoints > 0 ?
                        pointsImg[remainingPoints - 1] :
                        ConstraintLayout.LayoutParams.PARENT_ID),
                ConstraintSet.START,  remainingPoints > 0 ? 30 : 0);
        constraintSet.clear(pointId, ConstraintSet.END);

        return constraintSet;
    }

    /**
     * Class for slide listening on the points bar
     * Handles animation and activity call when point is marked/removed
     */
    private class PointGestureListener extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onDown(MotionEvent event) {
//            Required for other events
            return true;
        }

        @Override
        public boolean onFling(MotionEvent event1, MotionEvent event2,
                               float velocityX, float velocityY) {
//            IF HORIZONTAL SCROLL
            if(Math.abs(velocityX) > Math.abs(velocityY) &&
                    remainingPoints < pointsImg.length && remainingPoints >= 0) {
                boolean isPosivitive = velocityX > 0;
                mListener.onPointMarked(isRed, isPosivitive);
                movePoint(isPosivitive);
                return true;
            } else {
                return false;
            }
        }
    }
}
