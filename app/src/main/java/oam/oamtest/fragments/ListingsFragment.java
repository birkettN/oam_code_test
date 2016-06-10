package oam.oamtest.fragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.VelocityTrackerCompat;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.table.TableUtils;
import com.squareup.picasso.Picasso;

import java.sql.SQLException;
import java.util.List;

import oam.oamtest.R;
import oam.oamtest.models.ListingModel;
import oam.oamtest.models.ListingsResponseModel;
import oam.oamtest.repository.DatabaseHelper;
import oam.oamtest.services.ListingsService;
import retrofit2.Callback;
import retrofit2.Response;

public class ListingsFragment extends Fragment {

    private static final int DISTANCE_TO_ACTION = 300;
    private Callback<ListingsResponseModel> callback;
    private View.OnTouchListener gestureListener;
    private RelativeLayout listingLayout;
    private RelativeLayout listingOverlay;
    private float listingUpdatedX, listingOriginalX;
    private ListingsService mListingsService;
    private List<ListingModel> mListings;
    private ImageView mListingImage;
    private boolean moveToNext = true;
    private DatabaseHelper databaseHelper = null;
    private ProgressBar mProgressBar;

    private TextView listingTitle;
    private TextView listingYear;
    private TextView listingColor;
    private TextView listingLocation;
    private TextView listingCondition;
    private TextView listingTransmission;
    private TextView listingDoorCount;


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
        mProgressBar = (ProgressBar) view.findViewById(R.id.listing_progress);
        listingOverlay = (RelativeLayout) view.findViewById(R.id.listing_overlay);

        listingTitle = (TextView) view.findViewById(R.id.listing_title);
        listingYear = (TextView) view.findViewById(R.id.listing_year);
        listingColor = (TextView) view.findViewById(R.id.listing_color);
        listingLocation = (TextView) view.findViewById(R.id.listing_location);
        listingCondition = (TextView) view.findViewById(R.id.listing_condition);
        listingTransmission = (TextView) view.findViewById(R.id.listing_transmission);
        listingDoorCount = (TextView) view.findViewById(R.id.listing_door_count);


        gestureListener = new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {

                switch (event.getAction()) {

                    case MotionEvent.ACTION_UP:
                    case MotionEvent.ACTION_CANCEL:

                        listingLayout.animate().x(listingOriginalX).setDuration(10).start();
                        listingOverlay.setBackgroundColor(ContextCompat.getColor(getActivity(), android.R.color.transparent));
                        break;


                    case MotionEvent.ACTION_DOWN:
                        listingOriginalX = listingLayout.getX();

                        listingUpdatedX = listingLayout.getX() - event.getRawX();
                        break;

                    case MotionEvent.ACTION_MOVE:

                        if (moveToNext) {
                            listingLayout.animate()
                                    .x(event.getRawX() + listingUpdatedX)
                                    .setDuration(0)
                                    .start();

                            if ((listingOriginalX - DISTANCE_TO_ACTION) > listingLayout.getX()) {
                                removeListing(true);
                                Log.d("Direction", "LIKE");
                                showNextListing();
                                listingLayout.animate().x(listingOriginalX).setDuration(10).start();
                            } else if ((listingOriginalX + DISTANCE_TO_ACTION) < listingLayout.getX()) {
                                removeListing(false);
                                Log.d("Direction", "DISLIKE");
                                showNextListing();
                                listingLayout.animate().x(listingOriginalX).setDuration(10).start();
                            } else if ((listingOriginalX - 50) > listingLayout.getX()) {
                                listingOverlay.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.like_overlay));
                            } else if ((listingOriginalX + 50) < listingLayout.getX()) {
                                listingOverlay.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.dislike_overlay));
                            } else if ((listingOriginalX - 1) > listingLayout.getX()) {
                                listingOverlay.setBackgroundColor(ContextCompat.getColor(getActivity(), android.R.color.transparent));
                            } else if ((listingOriginalX + 1) < listingLayout.getX()) {
                                listingOverlay.setBackgroundColor(ContextCompat.getColor(getActivity(), android.R.color.transparent));
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

    private void removeListing(boolean liked) {
        if (liked) {
            saveListing(mListings.get(0));
        }

        moveToNext = false;
        mListings.remove(0);
    }


    private void showNextListing() {
        if (mListings.size() > 0) {
            mProgressBar.setVisibility(View.VISIBLE);
            ListingModel currentListingModel = mListings.get(0);

            listingTitle.setText(currentListingModel.title);
            listingYear.setText(currentListingModel.year);
            listingColor.setText(currentListingModel.colour.title);
            listingLocation.setText(currentListingModel.location.title);
            listingCondition.setText(currentListingModel.condition.title);
            listingTransmission.setText(currentListingModel.transmission.title);
            listingDoorCount.setText(String.format("%s doors", currentListingModel.door_count.title));

            Picasso.with(getActivity())
                    .load(currentListingModel.default_image)
                    .fit().centerCrop()
                    .tag(currentListingModel.default_image)
                    .into(mListingImage, new com.squareup.picasso.Callback() {
                        @Override
                        public void onSuccess() {
                            moveToNext = true;
                            mProgressBar.setVisibility(View.INVISIBLE);
                        }

                        @Override
                        public void onError() {
                            moveToNext = true;
                            mProgressBar.setVisibility(View.INVISIBLE);
                        }
                    });
        }
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


    @Override
    public void onDestroy() {
        super.onDestroy();
        if (databaseHelper != null) {
            OpenHelperManager.releaseHelper();
            databaseHelper = null;
        }
    }

    private DatabaseHelper getHelper() {
        if (databaseHelper == null) {
            databaseHelper =
                    OpenHelperManager.getHelper(getActivity(), DatabaseHelper.class);
        }
        return databaseHelper;
    }

    public void saveListing(ListingModel listingModel) {
        try {
            Dao<ListingModel, Integer> categoriesDao = getHelper().getListingsDao();
            categoriesDao.createOrUpdate(listingModel);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


}
