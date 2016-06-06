package it.extremegeneration.timetocook.dataModel;

import android.app.SearchManager;
import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.provider.BaseColumns;
import android.support.annotation.Nullable;
import android.util.Log;

public class CookingProvider extends ContentProvider {

    private static final String LOG = CookingProvider.class.getSimpleName();

    //Context for columns translation
    private static Context myContext;

    private FoodDatabase.CookingDBHelper cookingDBHelper;

    private FoodDatabase foodDatabase;

    private static final UriMatcher sUriMatcher = buildUriMatcher();

    static final int FOOD = 1;
    static final int FOOD_WITH_ID = 2;
    static final int FAVOURITE = 3;
    static final int FAVOURITE_WITH_ID = 4;
    static final int CATEGORY = 5;
    static final int CATEGORY_WITH_ID = 6;

    static final int SEARCH_SUGGEST = 7;

    static final int STEAM = 8;
    static final int STEAM_WITH_ID = 9;
    static final int BOILING = 10;
    static final int BOILING_WITH_ID = 11;
    static final int FIRE = 12;
    static final int FIRE_WITH_ID = 13;
    static final int OVEN = 14;
    static final int OVEN_WITH_ID = 15;


    //Class that helps to create query
    private static final SQLiteQueryBuilder sFoodAndFavouriteQueryBuilder;

    //INNER JOIN
    static {
        sFoodAndFavouriteQueryBuilder = new SQLiteQueryBuilder();

        sFoodAndFavouriteQueryBuilder.setTables(
                CookingContract.FoodEntry.TABLE_NAME + " INNER JOIN " +
                        CookingContract.FavouriteEntry.TABLE_NAME +
                        " ON " + CookingContract.FoodEntry.TABLE_NAME +
                        "." + CookingContract.FoodEntry._ID +
                        " = " + CookingContract.FavouriteEntry.TABLE_NAME +
                        "." + CookingContract.FavouriteEntry.COLUMN_ID_FAVOURITE);

    }

    private Cursor getIdFavourite() {
        return sFoodAndFavouriteQueryBuilder.query(
                cookingDBHelper.getReadableDatabase(),
                null,
                null,
                null,
                null,
                null,
                null
        );
    }


    static UriMatcher buildUriMatcher() {
        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = CookingContract.CONTENT_AUTHORITY;

        matcher.addURI(authority, CookingContract.PATH_FOOD, FOOD);
        matcher.addURI(authority, CookingContract.PATH_FOOD + "/#", FOOD_WITH_ID);
        matcher.addURI(authority, CookingContract.PATH_FAVOURITE, FAVOURITE);
        matcher.addURI(authority, CookingContract.PATH_FAVOURITE + "/#", FAVOURITE_WITH_ID);
        matcher.addURI(authority, CookingContract.PATH_CATEGORY, CATEGORY);
        matcher.addURI(authority, CookingContract.PATH_CATEGORY + "/#", CATEGORY_WITH_ID);

        // to get suggestions...
        matcher.addURI(authority, SearchManager.SUGGEST_URI_PATH_QUERY, SEARCH_SUGGEST);
        matcher.addURI(authority, SearchManager.SUGGEST_URI_PATH_QUERY + "/*", SEARCH_SUGGEST);

        matcher.addURI(authority, CookingContract.PATH_STEAM, STEAM);
        matcher.addURI(authority, CookingContract.PATH_STEAM + "/#", STEAM_WITH_ID);
        matcher.addURI(authority, CookingContract.PATH_FIRE, FIRE);
        matcher.addURI(authority, CookingContract.PATH_FIRE + "/#", FIRE_WITH_ID);
        matcher.addURI(authority, CookingContract.PATH_BOILING, BOILING);
        matcher.addURI(authority, CookingContract.PATH_BOILING + "/#", BOILING_WITH_ID);
        matcher.addURI(authority, CookingContract.PATH_OVEN, OVEN);
        matcher.addURI(authority, CookingContract.PATH_OVEN + "/#", OVEN_WITH_ID);

        return matcher;
    }


    @Override
    public boolean onCreate() {
        Log.v(LOG, "I'm here, onCreate");
        myContext = getContext();

        cookingDBHelper = new FoodDatabase.CookingDBHelper(getContext());
        foodDatabase = new FoodDatabase(getContext());
//        cookingDBHelper.getWritableDatabase();
        return true;
    }

    public static Context getMyContext (){
        return myContext;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        Cursor retCursor;
        switch (sUriMatcher.match(uri)) {
            case FOOD:
                retCursor = cookingDBHelper.getWritableDatabase().query(
                        CookingContract.FoodEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;

            case FOOD_WITH_ID:
                long food_id = CookingContract.getIdFromUri(uri);

                retCursor = cookingDBHelper.getWritableDatabase().query(
                        CookingContract.FoodEntry.TABLE_NAME,
                        projection,
                        CookingContract.FoodEntry.COLUMN_ID + " =?",
                        new String[]{Long.toString(food_id)},
                        null,
                        null,
                        null
                );
                return retCursor;

            case FAVOURITE:
                retCursor = cookingDBHelper.getWritableDatabase().query(
                        CookingContract.FavouriteEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
//                retCursor = getIdFavourite();
                break;

            case FAVOURITE_WITH_ID:
                long favourite_id = CookingContract.getIdFromUri(uri);

                retCursor = cookingDBHelper.getWritableDatabase().query(
                        CookingContract.FavouriteEntry.TABLE_NAME,
                        projection,
                        CookingContract.FavouriteEntry.COLUMN_ID_FAVOURITE + " =?",
                        new String[]{Long.toString(favourite_id)},
                        null,
                        null,
                        sortOrder
                );
                break;

            case CATEGORY:
                retCursor = cookingDBHelper.getWritableDatabase().query(
                        CookingContract.CategoryEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;

            case BOILING:
                retCursor = cookingDBHelper.getWritableDatabase().query(
                        CookingContract.BoilingEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;

            case BOILING_WITH_ID:
                long boiling_id = CookingContract.getIdFromUri(uri);

                retCursor = cookingDBHelper.getWritableDatabase().query(
                        CookingContract.BoilingEntry.TABLE_NAME,
                        projection,
                        CookingContract.BoilingEntry.COLUMN_ID_FOOD + " =?",
                        new String[]{Long.toString(boiling_id)},
                        null,
                        null,
                        null
                );
                return retCursor;

            case FIRE:
                retCursor = cookingDBHelper.getWritableDatabase().query(
                        CookingContract.FireEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;

            case FIRE_WITH_ID:

                long fire_id = CookingContract.getIdFromUri(uri);

                retCursor = cookingDBHelper.getWritableDatabase().query(
                        CookingContract.FireEntry.TABLE_NAME,
                        projection,
                        CookingContract.FireEntry.COLUMN_ID_FOOD + " =?",
                        new String[]{Long.toString(fire_id)},
                        null,
                        null,
                        null
                );
                return retCursor;

            case STEAM:
                retCursor = cookingDBHelper.getWritableDatabase().query(
                        CookingContract.SteamEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            case STEAM_WITH_ID:
                long steam_id = CookingContract.getIdFromUri(uri);

                retCursor = cookingDBHelper.getWritableDatabase().query(
                        CookingContract.SteamEntry.TABLE_NAME,
                        projection,
                        CookingContract.SteamEntry.COLUMN_ID_FOOD + " =?",
                        new String[]{Long.toString(steam_id)},
                        null,
                        null,
                        null
                );
                return retCursor;

            case OVEN:
                retCursor = cookingDBHelper.getWritableDatabase().query(
                        CookingContract.OvenEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;

            case OVEN_WITH_ID:
                long oven_id = CookingContract.getIdFromUri(uri);

                retCursor = cookingDBHelper.getWritableDatabase().query(
                        CookingContract.OvenEntry.TABLE_NAME,
                        projection,
                        CookingContract.OvenEntry.COLUMN_ID_FOOD +" =?",
                        new String[]{Long.toString(oven_id)},
                        null,
                        null,
                        null
                );
                break;

            case SEARCH_SUGGEST:
                Log.v("uri", "searchsuggest" + uri);

                if (selectionArgs == null) {
                    throw new IllegalArgumentException(
                            "selectionArgs must be provided for the Uri: " + uri);
                }
                return getSuggestion(selectionArgs[0]);

            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        retCursor.setNotificationUri(getContext().getContentResolver(), uri);
        return retCursor;
    }


    //To get suggestion when the User types something in the SearchView
    private Cursor getSuggestion(String query) {
        query = query.toLowerCase();

        String[] columns = new String[]{
                BaseColumns._ID,
                CookingContract.FoodEntry.COLUMN_ID,
                CookingContract.FoodEntry.COLUMN_NAME,
                CookingContract.FoodEntry.COLUMN_ICON_FOOD
//                CookingContract.FoodEntry.COLUMN_DESCRIPTION,
        };


        String selection = CookingContract.FoodEntry.COLUMN_NAME + " LIKE ?";
        String[] selectionArgs = new String[]{"%" + query + "%"};

        SQLiteQueryBuilder builder = new SQLiteQueryBuilder();
        builder.setTables(CookingContract.FoodEntry.TABLE_NAME);

        //The projectionMap will rename the columns to match SearchManager values
        builder.setProjectionMap(FoodDatabase.mColumnMap);


        Cursor cursor = builder.query(
                cookingDBHelper.getWritableDatabase(),
                columns,
                selection,
                selectionArgs,
                null, null, null);

        if (cursor == null) {
            return null;
        } else if (!cursor.moveToFirst()) {
            cursor.close();
            return null;
        }
        return cursor;

    }


    @Nullable
    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        final SQLiteDatabase db = cookingDBHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        Uri returnUri;

        switch (match) {
            case FAVOURITE:
                long _id = db.insert(CookingContract.FavouriteEntry.TABLE_NAME,
                        null,
                        values);
                if (_id > 0) {
                    returnUri = CookingContract.FavouriteEntry.buildFavouriteUri(_id);
                } else {
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                }
                break;

            default:
                throw new android.database.SQLException("Failed to insert row into " + uri);
        }

        getContext().getContentResolver().notifyChange(uri, null);
        return returnUri;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        SQLiteDatabase db = cookingDBHelper.getWritableDatabase();
        int match = sUriMatcher.match(uri);
        int rowsDeleted;

        switch (match) {
            case FAVOURITE:
                rowsDeleted = db.delete(
                        CookingContract.FavouriteEntry.TABLE_NAME,
                        selection,
                        selectionArgs
                );
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        if (rowsDeleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return rowsDeleted;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        return 0;
    }
}