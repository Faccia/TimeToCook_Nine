package it.extremegeneration.timetocook.fragments;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import it.extremegeneration.timetocook.Constants;
import it.extremegeneration.timetocook.NotificationService;
import it.extremegeneration.timetocook.R;
import it.extremegeneration.timetocook.dataModel.CookingContract;

/**
 * Created by Our PC on 28-May-16.
 */
public class BoilingFragment extends Fragment implements View.OnClickListener {

    public static final String LOG_TAG = BoilingFragment.class.getSimpleName();

    private ImageView imageViewStartTimerFull;
    private ImageView imageViewStartTimerPieces;
    private ImageView imageViewStartTimerFrozen;

    private TextView textViewTimeFull;
    private TextView textViewTimePieces;
    private TextView textViewTimeFrozen;
    private TextView textViewName;


    private int id_food;

    private int timeFull;
    private int timePieces;
    private int timeFrozen;

    private String nameOfFood;

    public static final String[] BOIL_COLUMNS = {
            CookingContract.BoilingEntry.COLUMN_TIME_FULL,
            CookingContract.BoilingEntry.COLUMN_TIME_FROZEN,
            CookingContract.BoilingEntry.COLUMN_TIME_PIECES,


    };

    public static final String[] FOOD_COLUMNS = {
            CookingContract.FoodEntry.COLUMN_NAME
    };


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

        Bundle arguments = getArguments();

        if (arguments != null) {
            id_food = arguments.getInt("ID");//Take the id of the food clicked
            Log.d(LOG_TAG, "id food = " + id_food);

        } else {
            Log.v(LOG_TAG, "ARGUMENT NULL");
        }

        Uri uriBoilId = CookingContract.BoilingEntry.buildBoilingUri(id_food);
        Cursor boilCursor = getContext().getContentResolver().query(
                uriBoilId, BOIL_COLUMNS, null, null, null);
        if (boilCursor.moveToFirst()){
            int timeFullColumn = boilCursor.getColumnIndex(CookingContract.BoilingEntry.COLUMN_TIME_FULL);
            timeFull = boilCursor.getInt(timeFullColumn);
            int timePiecesColumn = boilCursor.getColumnIndex(CookingContract.BoilingEntry.COLUMN_TIME_PIECES);
            timePieces = boilCursor.getInt(timePiecesColumn);
            int timeFrozenColumn = boilCursor.getColumnIndex(CookingContract.BoilingEntry.COLUMN_TIME_FROZEN);
            timeFrozen = boilCursor.getInt(timeFrozenColumn);

        }

        Uri uriFoodId = CookingContract.FoodEntry.buildFoodUri(id_food);
        Cursor foodCursor = getContext().getContentResolver().query(
                uriFoodId, FOOD_COLUMNS, null, null, null);
        if (foodCursor.moveToFirst()){
            int nameColumn = foodCursor.getColumnIndex(CookingContract.FoodEntry.COLUMN_NAME);
            nameOfFood = foodCursor.getString(nameColumn);

        }


        imageViewStartTimerFull = (ImageView) rootView.findViewById(R.id.bnt_timerStart_full);
        imageViewStartTimerFull.setOnClickListener(this);
        imageViewStartTimerPieces = (ImageView) rootView.findViewById(R.id.bnt_timerStart_pieces);
        imageViewStartTimerPieces.setOnClickListener(this);
        imageViewStartTimerFrozen = (ImageView) rootView.findViewById(R.id.bnt_timerStart_frozen);
        imageViewStartTimerFrozen.setOnClickListener(this);

        textViewTimeFull = (TextView) rootView.findViewById(R.id.detail_time_full_textView);

        Log.d (LOG_TAG, "time full = " + timeFull);
        textViewTimeFull.setText(Integer.toString(timeFull));
        textViewTimePieces = (TextView) rootView.findViewById(R.id.detail_time_pieces_textView);
        textViewTimePieces.setText(Integer.toString(timePieces));
        textViewTimeFrozen =(TextView) rootView.findViewById(R.id.detail_time_frozen_textView);
        textViewTimeFrozen.setText(Integer.toString(timeFrozen));

        textViewName = (TextView) rootView.findViewById(R.id.detail_name_food);
        textViewName.setText(nameOfFood);
        return rootView;
    }

    @Override
    public void onClick(View v) {
        Intent serviceIntent = new Intent(getActivity(), NotificationService.class);
        serviceIntent.setAction(Constants.ACTION.START_FOREGROUND_ACTION);
        serviceIntent.putExtra(NotificationService.ID_FOOD, id_food);
        serviceIntent.putExtra(NotificationService.NAME_OF_FOOD, nameOfFood);
        NotificationService.IS_RUNNING = true;
        switch (v.getId()){
            case R.id.bnt_timerStart_full:
                serviceIntent.putExtra(NotificationService.TIME_OF_COOKING, timeFull);
                getActivity().startService(serviceIntent);

                
                break;
            case R.id.bnt_timerStart_pieces:
                serviceIntent.putExtra(NotificationService.TIME_OF_COOKING, timePieces);
                getActivity().startService(serviceIntent);
                break;
            case R.id.bnt_timerStart_frozen:
                serviceIntent.putExtra(NotificationService.TIME_OF_COOKING, timeFrozen);
                getActivity().startService(serviceIntent);
                break;
        }

    }



}
