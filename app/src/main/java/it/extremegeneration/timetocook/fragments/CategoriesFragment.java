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
import it.extremegeneration.timetocook.adapters.AdapterCategories;
import it.extremegeneration.timetocook.dataModel.CookingContract;

public class CategoriesFragment extends Fragment
        implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final String ARG_SECTION_NUMBER = "section_number";

    //RECYCLER VIEW
    private RecyclerView recyclerView;
    private AdapterCategories adapterCategories;

    //ID for loader
    public static final int LOADER_CATEGORIES = 2;

    public static final String[] CATEGORY_COLUMNS = {
            CookingContract.CategoryEntry._ID,
            CookingContract.CategoryEntry.COLUMN_CATEGORY_NAME,
            CookingContract.CategoryEntry.COLUMN_PIC_CATEGORY
    };


    public static CategoriesFragment newInstance(int sectionNumber) {
        CategoriesFragment categoriesFragment = new CategoriesFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        categoriesFragment.setArguments(args);
        return categoriesFragment;

    }

    public CategoriesFragment() {
        // Required empty public constructor
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getLoaderManager().initLoader(LOADER_CATEGORIES,null,this);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_categories, container, false);

        adapterCategories = new AdapterCategories(getActivity(),
                new AdapterCategories.CategoriesOnClickHandler() {
            @Override
            public void onClick(Long categoryID, RecyclerView.ViewHolder viewHolder) {
                //MANAGE CLICK ACTIONS
            }
        });

        recyclerView = (RecyclerView) view.findViewById(R.id.fragment_categories);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapterCategories);

        return view;
    }


    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String sortOrder = CookingContract.CategoryEntry.COLUMN_CATEGORY_NAME + " ASC";
        Uri uriCategory = CookingContract.CategoryEntry.CONTENT_URI;

        return new CursorLoader(
                getActivity(),
                uriCategory,
                CATEGORY_COLUMNS,
                null,
                null,
                sortOrder
        );
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        adapterCategories.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        adapterCategories.swapCursor(null);
    }

    public interface Callback {
        void onItemSelected (Uri categoryUri);
    }
}
