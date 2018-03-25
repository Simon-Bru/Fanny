package edu.bruguerolle.rocher.fanny.fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.ConstraintSet;
import android.support.v4.app.Fragment;
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
    public static ScoreFragment newInstance(boolean isOpponent, ScoreChangeListener listener) {
        ScoreFragment fragment = new ScoreFragment();
        Bundle args = new Bundle();

        args.putBoolean(ARG_IS_OPPONENT, isOpponent);
        fragment.setArguments(args);
        fragment.mListener = listener;
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
        void onPointMarked(boolean isPositive);
        void onMatchOver();
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
            if(Math.abs(velocityX) > Math.abs(velocityY) &&
                    remainingPoints < pointsImg.length && remainingPoints >= 0) {

                ConstraintLayout container = root.findViewById(R.id.pointsContainer);
                TransitionManager.beginDelayedTransition(container);

                ImageView point;
                ConstraintSet constraintSet = new ConstraintSet();
                constraintSet.clone(container);

                if(velocityX > 0 && remainingPoints >= 0) {
                    point = getActivity().findViewById(pointsImg[remainingPoints]);
                    constraintSet = pullRight(constraintSet, point);

                    if(remainingPoints == 0) {
                        mListener.onMatchOver();
                    } else {
                        mListener.onPointMarked(true);
                        remainingPoints =  remainingPoints - 1;
                    }
                } else if(remainingPoints < pointsImg.length-1){
                    remainingPoints++;
                    mListener.onPointMarked(false);

                    point = getActivity().findViewById(pointsImg[remainingPoints]);
                    constraintSet = pullLeft(constraintSet, point);
                }

                constraintSet.applyTo(container);
                return true;
            } else {
                return false;
            }
        }

        private ConstraintSet pullRight(ConstraintSet constraintSet, ImageView point) {
            if(remainingPoints < pointsImg.length - 1) {
                constraintSet.connect(point.getId(), ConstraintSet.END,
                        pointsImg[remainingPoints+1], ConstraintSet.END,45);
            } else {
                constraintSet.connect(point.getId(), ConstraintSet.END, ConstraintSet.PARENT_ID, ConstraintSet.END,0);
            }
            constraintSet.clear(point.getId(), ConstraintSet.START);

            return constraintSet;
        }

        private ConstraintSet pullLeft(ConstraintSet constraintSet, ImageView point) {
            constraintSet.connect(point.getId(), ConstraintSet.START,
                    (remainingPoints > 0 ?
                            pointsImg[remainingPoints - 1] :
                            ConstraintLayout.LayoutParams.PARENT_ID),
                    ConstraintSet.START,  remainingPoints > 0 ? 45 : 0);
            constraintSet.clear(point.getId(), ConstraintSet.END);

            return constraintSet;
        }
    }
}
