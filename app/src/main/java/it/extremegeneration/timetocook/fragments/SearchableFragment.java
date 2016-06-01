package it.extremegeneration.timetocook.fragments;

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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import it.extremegeneration.timetocook.R;
import it.extremegeneration.timetocook.activities.SearchableActivity;
import it.extremegeneration.timetocook.adapters.AdapterSearchable;
import it.extremegeneration.timetocook.dataModel.CookingContract;


public class SearchableFragment extends Fragment
        implements LoaderManager.LoaderCallbacks<Cursor> {

    public static final String LOG_TAG = SearchableFragment.class.getSimpleName();



    public String query;

    //RECYCLER VIEW
    private RecyclerView recyclerView;
    private AdapterSearchable adapterSearchable;
    private int myPostion;

    //ID for the loader
    public static final int LOADER_SEARCHABLE = 100;

    public static final String[] FOOD_COLUMNS = {
            CookingContract.FoodEntry._ID,
            CookingContract.FoodEntry.COLUMN_ID,
            CookingContract.FoodEntry.COLUMN_NAME,
            CookingContract.FoodEntry.COLUMN_CATEGORY_ID
    };

    //todo : add the columns of categories and pics


    public SearchableFragment() {
        // Required empty public constructor
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        Log.v("Fragment", "onActivityCreated");

        super.onActivityCreated(savedInstanceState);

        getLoaderManager().initLoader(LOADER_SEARCHABLE, null, this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        Log.v("Fragment", "onCreateView");

        //Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_searchable, container, false);
        Bundle arguments = getArguments();
        if (arguments != null) {
            query = getArguments().getString(SearchableActivity.QUERY_FOR_BUNDLE);
        }

        adapterSearchable = new AdapterSearchable(getActivity(),
                new AdapterSearchable.AdapterOnClickHandler() {

                    @Override
                    public void onClick(Long foodID, AdapterSearchable.ViewHolderSearchable viewHolder) {
                        //MANAGE CLICK ACTIONS
                    }
                });

        recyclerView = (RecyclerView) rootView.findViewById(R.id.fragment_searchable);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapterSearchable);

        return rootView;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Log.v("Fragment", "onCreatedLoader");

        String sortOrder = CookingContract.FoodEntry.COLUMN_NAME + " ASC";
        Uri uriFood = CookingContract.FoodEntry.CONTENT_URI;

        Log.v(" onCreateLoader", "QUERY: " + query);

        String selectionArgs = "%" + query + "%";

        return new CursorLoader(
                getContext(),
                uriFood,
                FOOD_COLUMNS,
                "_id IN (SELECT _id FROM food WHERE name LIKE ?)",
                new String[] {selectionArgs},
                sortOrder

        );

//        Cursor idFoodCursor = getContext().getContentResolver().query(
//                uriFood,
//                null,
//                CookingContract.FoodEntry.COLUMN_NAME + " LIKE ?",
//                new String[]{selectionArgs},
//                null
//        );
//
//        if (idFoodCursor.moveToFirst()) {
//
//            List<String> ids = new ArrayList<String>();
//            while (!idFoodCursor.isAfterLast()) {
//                ids.add(idFoodCursor.getString(idFoodCursor.getColumnIndex(CookingContract.FoodEntry._ID)));
//                idFoodCursor.moveToNext();
//            }
//            idFoodCursor.close();
//
//            //Convert the ArrayList in String[]
//            String[] idSelectionArg = new String[ids.size()];
//            ids.toArray(idSelectionArg);
//
//            return new CursorLoader(
//                    getContext(),
//                    uriFood,
//                    FOOD_COLUMNS,
//                    CookingContract.FoodEntry._ID + " = ?",
//                    idSelectionArg,
//                    sortOrder
//            );
//
//        } else {
//            Toast.makeText(getContext(), "NO CORRESPONDECE IN YOUR RESEARCH", Toast.LENGTH_SHORT).show();
//            return null;
//        }
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        adapterSearchable.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        adapterSearchable.swapCursor(null);
    }

    public interface Callback {
        void onItemSelected(Uri foodUri);
    }
}
