package oam.oamtest.fragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import oam.oamtest.MainActivity;
import oam.oamtest.R;

public class ListingsFragment extends Fragment {

    private static final int SWIPE_MIN_DISTANCE = 10;
    private static final int SWIPE_MAX_OFF_PATH = 350;
    private static final int SWIPE_THRESHOLD_VELOCITY = 300;

    private GestureDetector gestureDetector;
    View.OnTouchListener gestureListener;

    RelativeLayout listingLayout;
    float listingUpdatedX, listingOriginalX;

    public static ListingsFragment newInstance() {
        return new ListingsFragment();
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_listings, container, false);

        listingLayout = (RelativeLayout) view.findViewById(R.id.listing_layout);

        gestureDetector = new GestureDetector(getActivity(), new MyGestureDetector());
        gestureListener = new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {

                switch (event.getAction()) {

                    case MotionEvent.ACTION_UP:

                        listingLayout.animate().x(listingOriginalX).setDuration(10).start();

                        break;


                    case MotionEvent.ACTION_DOWN:

                        listingOriginalX = listingLayout.getX();

                        listingUpdatedX = listingLayout.getX() - event.getRawX();
                        break;

                    case MotionEvent.ACTION_MOVE:

                        listingLayout.animate()
                                .x(event.getRawX() + listingUpdatedX)
                                .setDuration(0)
                                .start();
                        break;
                    default:
                        return false;
                }
                return true;




               //return gestureDetector.onTouchEvent(event);
            }
        };
        listingLayout.setOnTouchListener(gestureListener);


//        mRecyclerView = (RecyclerView) view.findViewById(R.id.fragment_search_results_results_container);
//        mResultCountTextView = (TextView) view.findViewById(R.id.fragment_search_results_result_count);
//        mResultCountTextView.setText(String.format("%1s %2s", 0, getString(R.string.results_found)));


        return view;
    }


    class MyGestureDetector extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            try {
                if (Math.abs(e1.getY() - e2.getY()) > SWIPE_MAX_OFF_PATH)
                    return false;
                if (e1.getX() - e2.getX() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
                 //   Toast.makeText(getActivity(), "Left Swipe", Toast.LENGTH_SHORT).show();
                  //  listingLayout.animate().x(e1.getX());
                } else if (e2.getX() - e1.getX() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
                 //   listingLayout.setTranslationX(e2.getX());
                  //  Toast.makeText(getActivity(), "Right Swipe", Toast.LENGTH_SHORT).show();
                }
            } catch (Exception e) {
                // nothing
            }
            return false;
        }




        @Override
        public boolean onDown(MotionEvent e) {
            return true;
        }

    }



}
