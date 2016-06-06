package it.extremegeneration.timetocook;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import java.util.concurrent.TimeUnit;

import it.extremegeneration.timetocook.activities.MainActivity;

/**
 * Created by Our PC on 01-Jun-16.
 */
public class NotificationService extends Service {

    private static final String LOG_TAG = NotificationService.class.getSimpleName();
    public static final String TIME_INFO = "time_info";
    public static final String PAUSE_RESUME_INFO = "pause_resume_info";

    CountDownTimer timer;
    NotificationCompat.Builder builder;

    Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
    String normalTime;
    long timeLeft;


    public long counter;

    public static boolean IS_RUNNING = true;

    public static final String ID_FOOD = "id_food";
    public static final String TIME_OF_COOKING = "time_of_cooking";
    public static final String NAME_OF_FOOD = "name_of_food";

    public int id_food;
    public String nameOfFood;
    public long enteredTimeFormatted;


    private Looper mServiceLooper;
    private ServiceHandler mServiceHandler;


    private final class ServiceHandler extends Handler {


        public ServiceHandler(Looper looper) {
            super(looper);

        }

        @Override
        public void handleMessage(Message msg) {
            // Normally we would do some work here, like download a file.
            // For our sample, we just sleep for 5 seconds.
            try {
                showCountDownTimer(enteredTimeFormatted);
                showNotification(nameOfFood, id_food);
                timer.start();
            } catch (Exception e) {
                // Restore interrupt status.
                Thread.currentThread().interrupt();
            }
            // Stop the service using the startId, so that we don't stop
            // the service in the middle of handling another job
            stopSelf(msg.arg1);
        }
    }

    @Override
    public void onCreate() {
        // Start up the thread running the service.  Note that we create a
        // separate thread because the service normally runs in the process's
        // main thread, which we don't want to block.  We also make it
        // background priority so CPU-intensive work will not disrupt our UI.


//        HandlerThread thread = new HandlerThread("ServiceStartArguments",
//                android.os.Process.THREAD_PRIORITY_BACKGROUND);
//        thread.start();
//
//        // Get the HandlerThread's Looper and use it for our Handler
//        mServiceLooper = thread.getLooper();
//        mServiceHandler = new ServiceHandler(mServiceLooper);
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        // For each start request, send a message to start a job and deliver the


        if (intent.getAction().equals(Constants.ACTION.START_FOREGROUND_ACTION)) {
            IS_RUNNING = true;
            Log.i(LOG_TAG, "Received Start Foreground Intent");

            // id of the food = id of a notification
            id_food = intent.getIntExtra(ID_FOOD, 0);
            // getting time of cooking pressed
            int timeOfCooking = intent.getIntExtra(TIME_OF_COOKING, 0);
            // getting name of the food for the title of the notification
            nameOfFood = intent.getStringExtra(NAME_OF_FOOD);
            enteredTimeFormatted = timeOfCooking * 60000;

            Log.i(LOG_TAG, "id of food passed= " + id_food + "/" + nameOfFood);
            Log.i(LOG_TAG, "time of cooking passed= " + timeOfCooking);

            HandlerThread thread = new HandlerThread("ServiceStartArguments",
                    android.os.Process.THREAD_PRIORITY_BACKGROUND);
            thread.start();

            // Get the HandlerThread's Looper and use it for our Handler
            mServiceLooper = thread.getLooper();
            mServiceHandler = new ServiceHandler(mServiceLooper);


            // start ID so we know which request we're stopping when we finish the job
            Message msg = mServiceHandler.obtainMessage();
            msg.arg1 = startId;
            mServiceHandler.sendMessage(msg);

//            showNotification(nameOfFood, id_food);
//            showCountDownTimer(enteredTimeFormatted);
//            timer.start();
        } else if (intent.getAction().equals(Constants.ACTION.PAUSE_ACTION)) { // Btn pause
            IS_RUNNING = false;
            Log.i(LOG_TAG, "Pressed Pause");
            timer.cancel();
            String timeLeftString = String.format(
                    "%02d:%02d:%02d",
                    TimeUnit.MILLISECONDS.toHours(timeLeft),
                    TimeUnit.MILLISECONDS.toMinutes(timeLeft) -
                            TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(timeLeft)),
                    TimeUnit.MILLISECONDS.toSeconds(timeLeft) -
                            TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(timeLeft)));

            Intent timerInfoIntent = new Intent(TIME_INFO);
            timerInfoIntent.putExtra("VALUE", timeLeftString);
            LocalBroadcastManager.getInstance(NotificationService.this).sendBroadcast(timerInfoIntent);

            Intent btnInfoIntent = new Intent(PAUSE_RESUME_INFO);
            btnInfoIntent.putExtra("BTN_TEXT", "Resume");
            LocalBroadcastManager.getInstance(NotificationService.this).sendBroadcast(btnInfoIntent);

            updateNotification(nameOfFood, id_food);
        } else if (intent.getAction().equals(Constants.ACTION.RESUME_ACTION)) {
            IS_RUNNING = true;
            Log.i(LOG_TAG, "Pressed Resume");


            Intent btnInfoIntent = new Intent(PAUSE_RESUME_INFO);
            btnInfoIntent.putExtra("BTN_TEXT", "Pause");
            LocalBroadcastManager.getInstance(NotificationService.this).sendBroadcast(btnInfoIntent);

            showNotification(nameOfFood, id_food);
            showCountDownTimer(timeLeft);
            timer.start();
            String timeLeftString = String.format(
                    "%02d:%02d:%02d",
                    TimeUnit.MILLISECONDS.toHours(timeLeft),
                    TimeUnit.MILLISECONDS.toMinutes(timeLeft) -
                            TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(timeLeft)),
                    TimeUnit.MILLISECONDS.toSeconds(timeLeft) -
                            TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(timeLeft)));

            Intent timerInfoIntent = new Intent(TIME_INFO);
            timerInfoIntent.putExtra("VALUE", timeLeftString);
            LocalBroadcastManager.getInstance(NotificationService.this).sendBroadcast(timerInfoIntent);

        } else if (intent.getAction().equals(Constants.ACTION.ADD_MIN_ACTION)) {
            Log.i(LOG_TAG, "Pressed Add Minute");
            timer.cancel();
            showCountDownTimer(timeLeft + 60000);
            timer.start();
            updateNotification(nameOfFood, id_food);
        } else if (intent.getAction().equals(Constants.ACTION.STOP_FOREGROUND_ACTION)) {
            Log.i(LOG_TAG, "Received Stop Foreground Intent");
            stopForeground(true);
            Intent timerInfoIntent = new Intent(TIME_INFO);
            timerInfoIntent.putExtra("VALUE", "Timer is deleted");
            LocalBroadcastManager.getInstance(NotificationService.this).sendBroadcast(timerInfoIntent);
            timer.cancel();
            cancelNotification(id_food);
            Log.d(LOG_TAG, "id of notification, cancel notification = " + id_food);
        }

        return START_STICKY;
    }

    private void showNotification(String nameOfFood, int id_food) {
        //startForeground(id_food, getMyActivityNotification(nameOfFood).build());
        final NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(id_food, getMyActivityNotification(nameOfFood).build());
        Log.d(LOG_TAG, "id of notification, show notification = " + id_food);
    }


    @Override
    public void onDestroy() {
        Log.i(LOG_TAG, "In onDestroy");
        super.onDestroy();
    }


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private NotificationCompat.Builder getMyActivityNotification(String nameOfFood) {

        Intent notificationIntent = new Intent(this, MainActivity.class);
        notificationIntent.setAction(Constants.ACTION.MAIN_ACTION);
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);

        Intent pauseIntent = new Intent(this, NotificationService.class);
        pauseIntent.setAction(Constants.ACTION.PAUSE_ACTION);
        PendingIntent pausePendingIntent = PendingIntent.getService(this, 0, pauseIntent, 0);

        Intent resumeIntent = new Intent(this, NotificationService.class);
        resumeIntent.setAction(Constants.ACTION.RESUME_ACTION);
        PendingIntent resumePendingIntent = PendingIntent.getService(this, 0, resumeIntent, 0);

        Intent addMinIntent = new Intent(this, NotificationService.class);
        addMinIntent.setAction(Constants.ACTION.ADD_MIN_ACTION);
        PendingIntent addMinPendingIntent = PendingIntent.getService(this, 0, addMinIntent, 0);

        builder = new NotificationCompat.Builder(this);

        if (IS_RUNNING) {
            builder.addAction(R.drawable.ic_pause_black_24dp, "PAUSE", pausePendingIntent);
        } else {
            builder.addAction(R.drawable.ic_play_arrow_black_24dp, "RESUME", resumePendingIntent);
        }


        return builder.setContentTitle(nameOfFood)
                .setContentText("Time left " + normalTime)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentIntent(pendingIntent)
                .setOngoing(true)
                .setAutoCancel(true)
                .addAction(R.drawable.ic_add_black_24dp, "ADD 1 MIN", addMinPendingIntent)
                .setVisibility(Notification.VISIBILITY_PUBLIC)
                .setPriority(Notification.PRIORITY_HIGH)
                .setCategory(Notification.CATEGORY_ALARM)
                .setOnlyAlertOnce(false);
    }


    public void updateNotification(String nameOfFood, int id_food) {
        final NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(id_food, getMyActivityNotification(nameOfFood).build());
        Log.d(LOG_TAG, "id of notification, update notification = " + id_food);


    }


    private void showCountDownTimer(long number) {
        timer = new CountDownTimer(number, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                timeLeft = millisUntilFinished;
                normalTime = String.format(
                        "%02d:%02d:%02d",
                        TimeUnit.MILLISECONDS.toHours(timeLeft),
                        TimeUnit.MILLISECONDS.toMinutes(timeLeft) -
                                TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(timeLeft)),
                        TimeUnit.MILLISECONDS.toSeconds(timeLeft) -
                                TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(timeLeft)));
                Log.i(LOG_TAG, "time left = " + normalTime);
                counter = millisUntilFinished / 1000;
                updateNotification(nameOfFood, id_food);

                Intent timerInfoIntent = new Intent(TIME_INFO);
                timerInfoIntent.putExtra("VALUE", normalTime);
                LocalBroadcastManager.getInstance(NotificationService.this).sendBroadcast(timerInfoIntent);

            }

            @Override
            public void onFinish() {
                counter = 0;
                readyNotification(nameOfFood, id_food);

                Intent timerInfoIntent = new Intent(TIME_INFO);
                timerInfoIntent.putExtra("VALUE", "Dish is ready!");
                LocalBroadcastManager.getInstance(NotificationService.this).sendBroadcast(timerInfoIntent);
            }
        };
    }


    public void cancelNotification(int notifyId) {
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancel(notifyId);
    }


    public void readyNotification(String nameOfFood, int id_food) {

        Intent notificationIntent = new Intent(this, MainActivity.class);
        notificationIntent.setAction(Constants.ACTION.MAIN_ACTION);
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);

        Intent stopIntent = new Intent(this, NotificationService.class);
        stopIntent.setAction(Constants.ACTION.STOP_FOREGROUND_ACTION);
        PendingIntent stopPendingIntent = PendingIntent.getService(this, 0, stopIntent, 0);

        Intent addMinIntent = new Intent(this, NotificationService.class);
        addMinIntent.setAction(Constants.ACTION.ADD_MIN_ACTION);
        PendingIntent addMinPendingIntent = PendingIntent.getService(this, 0, addMinIntent, 0);

        builder = new NotificationCompat.Builder(this);


        builder.setContentTitle(nameOfFood)
                .setContentText("Your dish is ready")
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentIntent(pendingIntent)
                .setOngoing(true)
                .setAutoCancel(true)
                .addAction(R.drawable.ic_stop_black_24dp, "Stop", stopPendingIntent)
                .setSound(alarmSound)
                .addAction(R.drawable.ic_add_black_24dp, "Add 1 minute", addMinPendingIntent)
                .setVisibility(Notification.VISIBILITY_PUBLIC)
                .setPriority(Notification.PRIORITY_HIGH)
                .setCategory(Notification.CATEGORY_ALARM)
                .setOnlyAlertOnce(false);

        Notification notification = builder.build();
        notification.flags = Notification.FLAG_INSISTENT;

        final NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(id_food, notification);


    }


}


