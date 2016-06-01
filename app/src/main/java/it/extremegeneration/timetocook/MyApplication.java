package it.extremegeneration.timetocook;

import android.app.Application;
import android.content.Context;
import android.util.Log;


public class MyApplication extends Application {

    private static Context myContext;

    private final String LOG = MyApplication.class.getSimpleName();

    @Override
    public void onCreate() {
        super.onCreate();
        myContext = this;
        Log.v(LOG, "I'm in onCreate");
    }

    public static Context getMyContext() {
        return myContext;
    }
}
