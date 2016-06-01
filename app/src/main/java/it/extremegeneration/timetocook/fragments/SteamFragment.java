package it.extremegeneration.timetocook.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import it.extremegeneration.timetocook.R;

/**
 * Created by Our PC on 28-May-16.
 */
public class SteamFragment extends Fragment {

    public static final String LOG_TAG = SteamFragment.class.getSimpleName();


    private static final String ARG_SECTION_NUMBER = "section_number";

    public static SteamFragment newInstance (int sectionNumber){
        SteamFragment steamFragment = new SteamFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        steamFragment.setArguments(args);
        return steamFragment;

    }

    public SteamFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_steam, container, false);
        return rootView;
    }
}

