package oam.oamtest.fragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.VelocityTrackerCompat;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.squareup.picasso.Picasso;

import java.util.List;

import oam.oamtest.R;
import oam.oamtest.models.ListingModel;
import oam.oamtest.models.ListingsResponseModel;
import oam.oamtest.services.ListingsService;
import retrofit2.Callback;
import retrofit2.Response;

public class ListingsFragment extends Fragment {

    private static final int SWIPE_MIN_DISTANCE = 10;
    private static final int SWIPE_MAX_OFF_PATH = 350;
    private static final int SWIPE_THRESHOLD_VELOCITY = 300;

    private GestureDetector gestureDetector;
    Callback<ListingsResponseModel> callback;
    View.OnTouchListener gestureListener;

    RelativeLayout listingLayout;
    float listingUpdatedX, listingOriginalX;
    ListingsService mListingsService;
    List<ListingModel> mListings;
    int currentIndex = 0;
    ImageView mListingImage;
    boolean moveToNext = true;

    public static ListingsFragment newInstance() {
        return new ListingsFragment();
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        callback = new Callback<ListingsResponseModel>() {
            @Override
            public void onResponse(Response<ListingsResponseModel> response) {
                listingLayout.setOnTouchListener(gestureListener);
                mListings = response.body().data;
                showNextListing();
            }

            @Override
            public void onFailure(Throwable t) {

            }
        };
        mListingsService = ListingsService.getInstance();

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_listings, container, false);

        listingLayout = (RelativeLayout) view.findViewById(R.id.listing_layout);
        mListingImage = (ImageView) view.findViewById(R.id.listing_image);

        gestureDetector = new GestureDetector(getActivity(), new MyGestureDetector());
        gestureListener = new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {

                int index = event.getActionIndex();
                int action = event.getActionMasked();
                int pointerId = event.getPointerId(index);


                switch (event.getAction()) {

                    case MotionEvent.ACTION_UP:
                    case MotionEvent.ACTION_CANCEL:

                        listingLayout.animate().x(listingOriginalX).setDuration(10).start();

                        break;


                    case MotionEvent.ACTION_DOWN:
                        listingOriginalX = listingLayout.getX();

                        listingUpdatedX = listingLayout.getX() - event.getRawX();
                        break;

                    case MotionEvent.ACTION_MOVE:

                        if(moveToNext){
                            listingLayout.animate()
                                    .x(event.getRawX() + listingUpdatedX)
                                    .setDuration(0)
                                    .start();

                            if ((listingOriginalX - 300) > listingLayout.getX()) {
                                moveToNext = false;
                                Log.d("Direction", "LIKE");
                                showNextListing();
                                listingLayout.animate().x(listingOriginalX).setDuration(10).start();
                            } else if ((listingOriginalX + 300) < listingLayout.getX()) {
                                moveToNext = false;
                                Log.d("Direction", "DISLIKE");
                                showNextListing();
                                listingLayout.animate().x(listingOriginalX).setDuration(10).start();
                            }
                        }

                        break;
                    default:
                        return false;
                }
                return true;

            }
        };


        return view;
    }


    private void showNextListing() {
        ListingModel currentListingModel = mListings.get(currentIndex);

        Picasso.with(getActivity())
                .load(currentListingModel.default_image)
                .fit().centerCrop()
                .tag(currentListingModel.default_image)
                .into(mListingImage, new com.squareup.picasso.Callback() {
                    @Override
                    public void onSuccess() {
                        moveToNext = true;
                    }

                    @Override
                    public void onError() {
                        moveToNext = true;
                    }
                });

        currentIndex++;

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        try {
            mListingsService.getListings(callback);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onResume() {
        super.onResume();

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
