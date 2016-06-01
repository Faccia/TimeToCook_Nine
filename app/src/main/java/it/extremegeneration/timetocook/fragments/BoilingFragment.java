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
public class BoilingFragment extends Fragment {

    public static final String LOG_TAG = BoilingFragment.class.getSimpleName();


    private static final String ARG_SECTION_NUMBER = "section_number";

    public static BoilingFragment newInstance (int sectionNumber){
        BoilingFragment boilingFragment = new BoilingFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        boilingFragment.setArguments(args);
        return boilingFragment;

    }

    public BoilingFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_boiling, container, false);
        return rootView;
    }
}
