package it.extremegeneration.timetocook.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import it.extremegeneration.timetocook.R;


public class SearchFragment extends Fragment {

    public static final String LOG_TAG = SearchFragment.class.getSimpleName();


    private static final String ARG_SECTION_NUMBER = "section_number";

    public static SearchFragment newInstance (int sectionNumber){
        SearchFragment searchFragment = new SearchFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        searchFragment.setArguments(args);
        return searchFragment;

    }
    public SearchFragment() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_search, container, false);
        return view;
    }


}
