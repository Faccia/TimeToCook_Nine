package it.extremegeneration.timetocook.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import it.extremegeneration.timetocook.R;
import it.extremegeneration.timetocook.dataModel.CookingContract;


public class FavFragment extends Fragment {
    private static final String ARG_SECTION_NUMBER = "section_number";

    public static FavFragment newInstance (int sectionNumber){
        FavFragment favFragment = new FavFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        favFragment.setArguments(args);
        return favFragment;

    }

    public FavFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_fav, container, false);
        return view;
    }



}
