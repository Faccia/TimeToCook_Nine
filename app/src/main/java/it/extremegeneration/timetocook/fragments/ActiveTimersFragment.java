package it.extremegeneration.timetocook.fragments;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import it.extremegeneration.timetocook.Constants;
import it.extremegeneration.timetocook.NotificationService;
import it.extremegeneration.timetocook.R;


public class ActiveTimersFragment extends Fragment implements View.OnClickListener {

    // TODO: add recycler view


    public static final String LOG_TAG = ActiveTimersFragment.class.getSimpleName();
    private static final String ARG_SECTION_NUMBER = "section_number";

    // TESTING
    private TextView nameTextView;
    private TextView timerTextView;
    private Button pauseBtn;
    private Button addMinBtn;
    private Button deleteBtn;
    private TimerStatusReceiver mTimerStatusReceiver;
    private SharedPreferences mPrefs;




    public ActiveTimersFragment() {
        // Required empty public constructor
    }


    public static ActiveTimersFragment newInstance(int sectionNumber) {
        ActiveTimersFragment activeTimersFragment = new ActiveTimersFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        activeTimersFragment.setArguments(args);
        return activeTimersFragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_active_timers, container, false);
        nameTextView = (TextView) view.findViewById(R.id.active_timer_name);
        timerTextView = (TextView) view.findViewById(R.id.active_timer_timeLeft);
        pauseBtn = (Button) view.findViewById(R.id.active_timer_btn_pause);
        pauseBtn.setText(NotificationService.IS_RUNNING ? "Pause" : "Resume");
        pauseBtn.setCompoundDrawablesWithIntrinsicBounds
                (NotificationService.IS_RUNNING ? R.drawable.ic_pause_black_24dp : R.drawable.ic_play_arrow_black_24dp,
                        0, 0, 0);
        pauseBtn.setOnClickListener(this);
        addMinBtn = (Button) view.findViewById(R.id.active_timer_btn_addMin);
        addMinBtn.setOnClickListener(this);
        deleteBtn = (Button) view.findViewById(R.id.active_timer_btn_delete);
        deleteBtn.setOnClickListener(this);

        mTimerStatusReceiver = new TimerStatusReceiver();
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(
                mTimerStatusReceiver, new IntentFilter(NotificationService.TIME_INFO));

        mPrefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String savedTime = mPrefs.getString("view_mode", "");
        timerTextView.setText(savedTime);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(
                mTimerStatusReceiver, new IntentFilter(NotificationService.TIME_INFO));
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(
                mTimerStatusReceiver, new IntentFilter(NotificationService.PAUSE_RESUME_INFO));

    }

    @Override
    public void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(mTimerStatusReceiver);

    }

    @Override
    public void onClick(View v) {
        Intent service = new Intent(getActivity(), NotificationService.class);
        switch (v.getId()){
            case R.id.active_timer_btn_pause:
                if (NotificationService.IS_RUNNING) {// Btn Pause
                    service.setAction(Constants.ACTION.PAUSE_ACTION);
                    getActivity().startService(service);
                    break;
                } else if (!NotificationService.IS_RUNNING) { //Btn Resume
                    service.setAction(Constants.ACTION.RESUME_ACTION);
                    getActivity().startService(service);
                    break;
                }
                break;

            case R.id.active_timer_btn_addMin:
                break;

            case R.id.active_timer_btn_delete:
                pauseBtn.setText("Pause");
                pauseBtn.setCompoundDrawablesWithIntrinsicBounds
                        (R.drawable.ic_pause_black_24dp,
                                0, 0, 0);
                service.setAction(Constants.ACTION.STOP_FOREGROUND_ACTION);
                getActivity().startService(service);
                NotificationService.IS_RUNNING = false;
                SharedPreferences.Editor ed = mPrefs.edit();
                ed.putString("view_mode", "");
                ed.commit();
                break;
        }

    }

    private class TimerStatusReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent != null && intent.getAction().equals(NotificationService.TIME_INFO)) {
                if (intent.hasExtra("VALUE")) {
                    timerTextView.setText(intent.getStringExtra("VALUE"));
                    SharedPreferences.Editor ed = mPrefs.edit();
                    ed.putString("view_mode", intent.getStringExtra("VALUE"));
                    ed.commit();
                }
            }
            if (intent != null && intent.getAction().equals(NotificationService.PAUSE_RESUME_INFO)) {
                if (intent.hasExtra("BTN_TEXT")) {
                    pauseBtn.setText(intent.getStringExtra("BTN_TEXT"));
                    if (intent.getStringExtra("BTN_TEXT").equals("Pause")) {
                        pauseBtn.setCompoundDrawablesWithIntrinsicBounds
                                (R.drawable.ic_pause_black_24dp,
                                        0, 0, 0);
                    } else {
                        pauseBtn.setCompoundDrawablesWithIntrinsicBounds
                                (R.drawable.ic_play_arrow_black_24dp,
                                        0, 0, 0);
                    }
                }
            }
        }
    }
}
