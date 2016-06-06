package it.extremegeneration.timetocook.dataModel;

import android.app.SearchManager;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;

import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

import java.util.HashMap;


public class FoodDatabase {

    private static final String DATABASE_NAME = "cookingDB.db";
    private static final int DATABASE_VERSION = 4;

    public final CookingDBHelper cookingDBHelper;


    public static final HashMap<String, String> mColumnMap = buildColumnMap();

    //Build a map for all columns that may be requested, which will be given to SQLiteQueryBuilder.
    //This allow ContentProvider to request columns without the need to know real column names and
    //create the alias itself
    private static HashMap<String, String> buildColumnMap() {
        HashMap<String, String> map = new HashMap<String, String>();

        map.put(CookingContract.FoodEntry._ID,
                CookingContract.FoodEntry._ID + " as " + BaseColumns._ID);
        map.put(CookingContract.FoodEntry.COLUMN_NAME,
                CookingContract.FoodEntry.COLUMN_NAME + " as " + SearchManager.SUGGEST_COLUMN_TEXT_1);
        map.put(CookingContract.FoodEntry.COLUMN_DESCRIPTION,
                CookingContract.FoodEntry.COLUMN_DESCRIPTION + " as " + SearchManager.SUGGEST_COLUMN_TEXT_2);
        map.put(CookingContract.FoodEntry.COLUMN_ID,
                CookingContract.FoodEntry.COLUMN_ID + " as " + SearchManager.SUGGEST_COLUMN_INTENT_DATA_ID);

        map.put(CookingContract.FoodEntry.COLUMN_ICON_FOOD,
                CookingContract.FoodEntry.COLUMN_ICON_FOOD + " as " + SearchManager.SUGGEST_COLUMN_ICON_1);

        return map;
    }

    public FoodDatabase(Context context) {
        cookingDBHelper = new CookingDBHelper(context);
    }


    public static class CookingDBHelper extends SQLiteAssetHelper {


        public CookingDBHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
            setForcedUpgrade();
        }


        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            super.onUpgrade(db, oldVersion, newVersion);

//        db.execSQL("DROP TABLE IF EXISTS " + CookingContract.FoodEntry.TABLE_NAME);
//        onCreate(db);
        }

    }


}



