package it.extremegeneration.timetocook.fragments;

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import it.extremegeneration.timetocook.R;
import it.extremegeneration.timetocook.dataModel.CookingContract;

/**
 * Created by Our PC on 28-May-16.
 */
public class FireFragment extends Fragment {

    public static final String LOG_TAG = FireFragment.class.getSimpleName();

    private static final String ARG_SECTION_NUMBER = "section_number";

    public static FireFragment newInstance(int sectionNumber) {
        FireFragment fireFragment = new FireFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fireFragment.setArguments(args);
        return fireFragment;

    }

    public FireFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_fire, container, false);

        Bundle arguments = getArguments();
        long id_food = 0;

        if (arguments != null) {
            int int_id_food = arguments.getInt("ID");//Take the id of the food clicked
            id_food = Long.valueOf(int_id_food);
            Log.d(LOG_TAG, "id food = " + id_food);

        } else {
            Log.v(LOG_TAG, "ARGUMENT NULL");
        }
        TextView tipsTextView = (TextView) rootView.findViewById(R.id.detail_tips);

        Uri uriFireId = CookingContract.FireEntry.buildFireUri(id_food);
        Cursor fireCursor = getContext().getContentResolver().query(
                uriFireId, null, null, null, null);
        if (fireCursor.moveToFirst()){
            int tipsColumn = fireCursor.getColumnIndex(CookingContract.FireEntry.COLUMN_TIPS);
            String tipsString = fireCursor.getString(tipsColumn);
            tipsTextView.setText(tipsString);
        }
        return rootView;
    }
}
