package it.extremegeneration.timetocook.dataModel;

import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

import it.extremegeneration.timetocook.R;


public class CookingContract {


    public static String LOG_TAG = CookingContract.class.getSimpleName();


    public static final String CONTENT_AUTHORITY =
            "it.extremegeneration.timetocook.app";

    public static final Uri BASE_CONTENT_URI =
            Uri.parse("content://" + CONTENT_AUTHORITY);


    public static final String PATH_FOOD = "food";
    public static final String PATH_FAVOURITE = "favourites";
    public static final String PATH_CATEGORY = "category";

    //Cooking methods
    public static final String PATH_STEAM = "steam";
    public static final String PATH_BOILING = "boiling";
    public static final String PATH_FIRE = "fire";
    public static final String PATH_OVEN = "oven";


    public static final class FoodEntry implements BaseColumns {

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendEncodedPath(PATH_FOOD).build();

        public static final String TABLE_NAME = "food";

        public static final String COLUMN_ID = "ID";
        public static final String COLUMN_NAME = CookingProvider.getMyContext().getResources().getString(R.string.food_COLUMN_NAME);
        public static final String COLUMN_DESCRIPTION = CookingProvider.getMyContext().getResources().getString(R.string.food_COLUMN_DESCRIPTION);
        public static final String COLUMN_ICON_FOOD = "icon_food";
        public static final String COLUMN_KCAL = "kcal";
        public static final String COLUMN_IMAGE_FOOD = "image_food";
        public static final String COLUMN_CATEGORY_ID = "category_id";

        public static Uri buildFoodUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }
    }

    public static final class FavouriteEntry implements BaseColumns {
        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendEncodedPath(PATH_FAVOURITE).build();
        public static final String TABLE_NAME = "favourites";

        public static final String COLUMN_ID_FAVOURITE = "id_favourite";

        public static Uri buildFavouriteUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }
    }

    public static final class CategoryEntry implements BaseColumns {
        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendEncodedPath(PATH_CATEGORY).build();
        public static final String TABLE_NAME = "category";

        public static final String COLUMN_CATEGORY_NAME = CookingProvider.getMyContext().getResources().getString(R.string.category_COLUMN_NAME);
        public static final String COLUMN_PIC_CATEGORY = "image_category";

        public static Uri buildCategoryUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }
    }

    public static final class SteamEntry implements BaseColumns {
        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendEncodedPath(PATH_STEAM).build();
        public static final String TABLE_NAME = "steam";

        public static final String COLUMN_ID_FOOD = "ID_food";
        public static final String COLUMN_TIPS = CookingProvider.getMyContext().getResources().getString(R.string.steam_COLUMN_TIPS);
        public static final String COLUMN_TIME_FULL = "time_full";
        public static final String COLUMN_TIME_PIECES = "time_pieces";
        public static final String COLUMN_TIME_FROZEN = "time_frozen";

        public static Uri buildSteamUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }
    }

    public static final class BoilingEntry implements BaseColumns {
        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendEncodedPath(PATH_BOILING).build();
        public static final String TABLE_NAME = "boiling";

        public static final String COLUMN_ID_FOOD = "ID_food";
        public static final String COLUMN_TIPS = CookingProvider.getMyContext().getResources().getString(R.string.boiling_COLUMN_TIPS);
        public static final String COLUMN_TIME_FULL = "time_full";
        public static final String COLUMN_TIME_PIECES = "time_pieces";
        public static final String COLUMN_TIME_FROZEN = "time_frozen";


        public static Uri buildBoilingUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }
    }

    public static final class FireEntry implements BaseColumns {
        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendEncodedPath(PATH_FIRE).build();
        public static final String TABLE_NAME = "fire";

        public static final String COLUMN_ID_FOOD = "ID_food";
        public static final String COLUMN_TIPS = CookingProvider.getMyContext().getResources().getString(R.string.fire_COLUMN_TIPS);
        public static final String COLUMN_TIME_FULL = "time_full";
        public static final String COLUMN_TIME_PIECES = "time_pieces";
        public static final String COLUMN_TIME_FROZEN = "time_frozen";

        public static Uri buildFireUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }
    }

    public static final class OvenEntry implements BaseColumns {
        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendEncodedPath(PATH_OVEN).build();
        public static final String TABLE_NAME = "oven";

        public static final String COLUMN_ID_FOOD = "ID_food";
        public static final String COLUMN_TIPS = CookingProvider.getMyContext().getResources().getString(R.string.oven_COLUMN_TIPS);
        public static final String COLUMN_TIME_FULL = "time_full";
        public static final String COLUMN_TIME_PIECES = "time_pieces";
        public static final String COLUMN_TIME_FROZEN = "time_frozen";

        public static Uri buildOvenUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }
    }


    public static long getIdFromUri(Uri uri) {
        return ContentUris.parseId(uri);
    }

}
