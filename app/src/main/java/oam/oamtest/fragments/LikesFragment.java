package oam.oamtest.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.Dao;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import oam.oamtest.R;
import oam.oamtest.adapters.LikesAdapter;
import oam.oamtest.models.ListingModel;
import oam.oamtest.repository.DatabaseHelper;

public class LikesFragment extends Fragment {

    private DatabaseHelper databaseHelper = null;
    RecyclerView recyclerView;

    public static LikesFragment newInstance() {
        return new LikesFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_likes, container, false);

        recyclerView = (RecyclerView) view.findViewById(R.id.fragment_likes_recyclerview);

        return view;
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(llm);
        recyclerView.setAdapter(new LikesAdapter(getListings(), getActivity()));
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


    public List<ListingModel> getListings() {
        try {
            Dao<ListingModel, Integer> favouritesDao = getHelper().getListingsDao();
            return favouritesDao.queryForAll();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return new ArrayList<>();
    }


}
