package it.extremegeneration.timetocook.fragments;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import it.extremegeneration.timetocook.R;
import it.extremegeneration.timetocook.adapters.RecyclerViewAdapter;
import it.extremegeneration.timetocook.dataModel.CookingContract;


public class MainFragment extends Fragment
        implements LoaderManager.LoaderCallbacks<Cursor> {

    //RECYCLER VIEW
    private RecyclerView recyclerView;
    private RecyclerViewAdapter recyclerViewAdapter;

    //ID for the loader
    public static final int LOADER = 1;

    public static final String[] FOOD_COLUMNS = {
            CookingContract.FoodEntry._ID,
            CookingContract.FoodEntry.COLUMN_NAME,
            CookingContract.FoodEntry.COLUMN_DESCRIPTION,
            CookingContract.FoodEntry.COLUMN_CATEGORY_ID
    };


    public MainFragment() {
        // Required empty public constructor
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getLoaderManager().initLoader(LOADER,null,this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
    // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        recyclerViewAdapter = new RecyclerViewAdapter(getActivity(),
                new RecyclerViewAdapter.AdapterOnClickHandler() {

                    @Override
                    public void onClick(Long foodID, RecyclerView.ViewHolder viewHolder) {
                        //MANAGE CLICK ACTIONS
                    }
                });

        recyclerView = (RecyclerView) rootView.findViewById(R.id.fragment_recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(recyclerViewAdapter);

        return rootView;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String sortOrder = CookingContract.FoodEntry.COLUMN_NAME + " ASC";
        Uri uriFood = CookingContract.FoodEntry.CONTENT_URI;

        return new CursorLoader(
                getActivity(),
                uriFood,
                FOOD_COLUMNS,
                null,
                null,
                sortOrder
        );
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

        recyclerViewAdapter.swapCursor(data);

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        recyclerViewAdapter.swapCursor(null);
    }

    public interface Callback {
        //When a food has been selected
        void onItemSelected (Uri foodUri);
    }
}
