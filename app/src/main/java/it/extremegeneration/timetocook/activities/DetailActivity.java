package it.extremegeneration.timetocook.activities;

import android.app.SearchManager;
import android.content.ComponentName;
import android.content.Context;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;

import java.util.Locale;

import it.extremegeneration.timetocook.R;
import it.extremegeneration.timetocook.dataModel.CookingContract;
import it.extremegeneration.timetocook.fragments.BoilingFragment;
import it.extremegeneration.timetocook.fragments.FireFragment;
import it.extremegeneration.timetocook.fragments.OvenFragment;
import it.extremegeneration.timetocook.fragments.SteamFragment;

public class DetailActivity extends AppCompatActivity {

    public static final String LOG_TAG = DetailActivity.class.getSimpleName();

    private boolean booleanSteam;
    private boolean booleanBoiling;
    private boolean booleanFire;
    private boolean booleanOven;

    public static int id_food;

    private TabLayout mTabLayout;
    private SectionPagerAdapter mSectionPagerAdapter;
    private ViewPager mViewPager;

    private Toolbar mToolbar;


    //POSSIBLE CASES (always including boiling):
    private static final int BOILING = 0;
    private static final int STEAM = 1;
    private static final int FIRE = 2;
    private static final int OVEN = 3;
    private static final int FIRE_STEAM = 4;
    private static final int FIRE_OVEN = 5;
    private static final int STEAM_OVEN = 6;
    private static final int FIRE_STEAM_OVEN = 7;


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.option_menu, menu);

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.search_item).getActionView();

        searchView.setSearchableInfo(searchManager.getSearchableInfo
                (new ComponentName(this, SearchableActivity.class)));

        searchView.setIconifiedByDefault(false);  // Do not iconify the widget; expand it by default

        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        // Toolbar stuff
        mToolbar = (Toolbar) findViewById(R.id.activity_detail_toolbar);
        setSupportActionBar(mToolbar);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mToolbar.setElevation(0);
        }
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        ///GET THE FOOD ID FROM THE BUNDLE
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            id_food = extras.getInt("ID");
        }

        //Check if the given food's ID has STEAM, FIRE, OVEN table correspondence
        checkTablesDatabase();

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionPagerAdapter = new SectionPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.activity_detail_viewPager);
        mViewPager.setAdapter(mSectionPagerAdapter);
        //mViewPager.setOffscreenPageLimit(4);
        mTabLayout = (TabLayout) findViewById(R.id.activity_detail_tabLayout);
        mTabLayout.setupWithViewPager(mViewPager);
        mTabLayout.setTabMode(TabLayout.MODE_FIXED);

        if (savedInstanceState == null) {
            //*****PASS THE URI or the ID TO THE FRAGMENT
            //***OR DO SOMETHING WITH THE URI
        }


    }


    private void checkTablesDatabase() {
        //Let's check which cooking methods are available for the given food

        //Is there a Steam table for this food?
        Cursor cursorSteam = getApplicationContext().getContentResolver().query(
                CookingContract.SteamEntry.CONTENT_URI,
                null,
                CookingContract.SteamEntry.COLUMN_ID_FOOD + " =?",
                new String[]{String.valueOf(id_food)},
                null
        );

        if (cursorSteam.moveToFirst()) booleanSteam = true;
        else booleanSteam = false;
        //steamTextView.setText("steam " + booleanSteam);

        //Is there a Boiling table for this food?
        //NB. Boiling is supposed to be always available
        Cursor cursorBoiling = getApplicationContext().getContentResolver().query(
                CookingContract.BoilingEntry.CONTENT_URI,
                null,
                CookingContract.BoilingEntry.COLUMN_ID_FOOD + " =?",
                new String[]{String.valueOf(id_food)},
                null
        );

        if (cursorBoiling.moveToFirst()) booleanBoiling = true;
        else booleanBoiling = false;
        //boilingTextView.setText("boiling " + booleanBoiling);

        //Is there a Fire table for this food?
        Cursor cursorFire = getApplicationContext().getContentResolver().query(
                CookingContract.FireEntry.CONTENT_URI,
                null,
                CookingContract.FireEntry.COLUMN_ID_FOOD + " =?",
                new String[]{String.valueOf(id_food)},
                null
        );

        if (cursorFire.moveToFirst()) booleanFire = true;
        else booleanFire = false;
        //fireTextView.setText("fire " + booleanFire);

        //Is there a Oven table for this food?
        Cursor cursorOven = getApplication().getContentResolver().query(
                CookingContract.OvenEntry.CONTENT_URI,
                null,
                CookingContract.OvenEntry.COLUMN_ID_FOOD + " =?",
                new String[]{String.valueOf(id_food)},
                null
        );
        if (cursorOven.moveToFirst()) booleanOven = true;
        else booleanOven = false;
    }


    private int checkTheCase() {
        if (booleanFire && booleanOven && booleanSteam) {
            return FIRE_STEAM_OVEN;
        }
        if (booleanSteam && booleanOven) {
            return STEAM_OVEN;
        }
        if (booleanFire && booleanOven) {
            return FIRE_OVEN;
        }
        if (booleanFire && booleanSteam) {
            return FIRE_STEAM;
        }
        if (booleanFire) {
            return FIRE;
        }
        if (booleanSteam) {
            return STEAM;
        }
        if (booleanOven) {
            return OVEN;
        }
        return BOILING;
    }

    public class SectionPagerAdapter extends FragmentPagerAdapter {

        Locale locale = Locale.getDefault();
        Bundle extras = getIntent().getExtras();


        public SectionPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        public Fragment case_Boiling(int position) {

            switch (position) {
                case 0:
                    getIntent().getData();
                    BoilingFragment boilingFragment = BoilingFragment.newInstance(position + 1);
                    extras.putInt("ID", id_food);
                    Log.d(LOG_TAG, "id food = " + id_food);
                    boilingFragment.setArguments(extras);
                    return boilingFragment;

            }
            return null;
        }

        public CharSequence title_Boiling(int position) {
            switch (position) {
                case 0:
                    return getString(R.string.tab_boil).toUpperCase(locale);
            }
            return null;
        }


        public Fragment case_Boiling_Fire(int position) {
            switch (position) {
                case 0:
                    getIntent().getData();
                    BoilingFragment boilingFragment = BoilingFragment.newInstance(position + 1);
                    extras.putInt("ID", id_food);
                    Log.d(LOG_TAG, "id food = " + id_food);
                    boilingFragment.setArguments(extras);
                    return boilingFragment;
                case 1:
                    getIntent().getData();
                    FireFragment fireFragment = FireFragment.newInstance(position + 1);
                    extras.putInt("ID", id_food);
                    Log.d(LOG_TAG, "id food = " + id_food);
                    fireFragment.setArguments(extras);
                    return fireFragment;

            }
            return null;
        }

        public CharSequence title_Boiling_Fire(int position) {
            switch (position) {
                case 0:
                    return getString(R.string.tab_boil).toUpperCase(locale);
                case 1:
                    return getString(R.string.tab_fire).toUpperCase(locale);
            }
            return null;

        }

        public Fragment case_Boiling_Steam(int position) {
            switch (position) {
                case 0:
                    getIntent().getData();
                    BoilingFragment boilingFragment = BoilingFragment.newInstance(position + 1);
                    extras.putInt("ID", id_food);
                    Log.d(LOG_TAG, "id food = " + id_food);
                    boilingFragment.setArguments(extras);
                    return boilingFragment;
                case 1:
                    getIntent().getData();
                    SteamFragment steamFragment = SteamFragment.newInstance(position + 1);
                    extras.putInt("ID", id_food);
                    Log.d(LOG_TAG, "id food = " + id_food);
                    steamFragment.setArguments(extras);
                    return steamFragment;
            }
            return null;
        }

        public CharSequence title_Boiling_Steam(int position) {
            switch (position) {
                case 0:
                    return getString(R.string.tab_boil).toUpperCase(locale);
                case 1:
                    return getString(R.string.tab_steam).toUpperCase(locale);
            }
            return null;

        }

        public Fragment case_Boiling_Oven(int position) {
            switch (position) {
                case 0:
                    getIntent().getData();
                    BoilingFragment boilingFragment = BoilingFragment.newInstance(position + 1);
                    extras.putInt("ID", id_food);
                    Log.d(LOG_TAG, "id food = " + id_food);
                    boilingFragment.setArguments(extras);
                    return boilingFragment;
                case 1:
                    getIntent().getData();
                    OvenFragment ovenFragment = OvenFragment.newInstance(position + 1);
                    extras.putInt("ID", id_food);
                    Log.d(LOG_TAG, "id food = " + id_food);
                    ovenFragment.setArguments(extras);
                    return ovenFragment;
            }
            return null;
        }

        public CharSequence title_Boiling_Oven(int position) {
            switch (position) {
                case 0:
                    return getString(R.string.tab_boil).toUpperCase(locale);
                case 1:
                    return getString(R.string.tab_oven).toUpperCase(locale);
            }
            return null;

        }

        public Fragment case_Boiling_Fire_Steam(int position) {
            switch (position) {
                case 0:
                    getIntent().getData();
                    BoilingFragment boilingFragment = BoilingFragment.newInstance(position + 1);
                    extras.putInt("ID", id_food);
                    Log.d(LOG_TAG, "id food = " + id_food);
                    boilingFragment.setArguments(extras);
                    return boilingFragment;
                case 1:
                    getIntent().getData();
                    FireFragment fireFragment = FireFragment.newInstance(position + 1);
                    extras.putInt("ID", id_food);
                    Log.d(LOG_TAG, "id food = " + id_food);
                    fireFragment.setArguments(extras);
                    return fireFragment;
                case 2:
                    getIntent().getData();
                    SteamFragment steamFragment = SteamFragment.newInstance(position + 1);
                    extras.putInt("ID", id_food);
                    Log.d(LOG_TAG, "id food = " + id_food);
                    steamFragment.setArguments(extras);
                    return steamFragment;
            }
            return null;
        }

        public CharSequence title_Boiling_Fire_Steam(int position) {
            switch (position) {
                case 0:
                    return getString(R.string.tab_boil).toUpperCase(locale);
                case 1:
                    return getString(R.string.tab_fire).toUpperCase(locale);
                case 2:
                    return getString(R.string.tab_steam).toUpperCase(locale);
            }
            return null;

        }

        public Fragment case_Boiling_Fire_Oven(int position) {
            switch (position) {
                case 0:
                    getIntent().getData();
                    BoilingFragment boilingFragment = BoilingFragment.newInstance(position + 1);
                    extras.putInt("ID", id_food);
                    Log.d(LOG_TAG, "id food = " + id_food);
                    boilingFragment.setArguments(extras);
                    return boilingFragment;
                case 1:
                    getIntent().getData();
                    FireFragment fireFragment = FireFragment.newInstance(position + 1);
                    extras.putInt("ID", id_food);
                    Log.d(LOG_TAG, "id food = " + id_food);
                    fireFragment.setArguments(extras);
                    return fireFragment;
                case 2:
                    getIntent().getData();
                    OvenFragment ovenFragment = OvenFragment.newInstance(position + 1);
                    extras.putInt("ID", id_food);
                    Log.d(LOG_TAG, "id food = " + id_food);
                    ovenFragment.setArguments(extras);
                    return ovenFragment;
            }
            return null;
        }

        public CharSequence title_Boiling_Fire_Oven(int position) {
            switch (position) {
                case 0:
                    return getString(R.string.tab_boil).toUpperCase(locale);
                case 1:
                    return getString(R.string.tab_fire).toUpperCase(locale);
                case 2:
                    return getString(R.string.tab_oven).toUpperCase(locale);
            }
            return null;

        }


        public Fragment case_Boiling_Steam_Oven(int position) {
            switch (position) {
                case 0:
                    getIntent().getData();
                    BoilingFragment boilingFragment = BoilingFragment.newInstance(position + 1);
                    extras.putInt("ID", id_food);
                    Log.d(LOG_TAG, "id food = " + id_food);
                    boilingFragment.setArguments(extras);
                    return boilingFragment;
                case 1:
                    getIntent().getData();
                    SteamFragment steamFragment = SteamFragment.newInstance(position + 1);
                    extras.putInt("ID", id_food);
                    Log.d(LOG_TAG, "id food = " + id_food);
                    steamFragment.setArguments(extras);
                    return steamFragment;
                case 2:
                    getIntent().getData();
                    OvenFragment ovenFragment = OvenFragment.newInstance(position + 1);
                    extras.putInt("ID", id_food);
                    Log.d(LOG_TAG, "id food = " + id_food);
                    ovenFragment.setArguments(extras);
                    return ovenFragment;
            }
            return null;
        }

        public CharSequence title_Boiling_Steam_Oven(int position) {
            switch (position) {
                case 0:
                    return getString(R.string.tab_boil).toUpperCase(locale);
                case 1:
                    return getString(R.string.tab_steam).toUpperCase(locale);
                case 2:
                    return getString(R.string.tab_oven).toUpperCase(locale);
            }
            return null;

        }

        public Fragment case_Boiling_Fire_Steam_Oven(int position) {
            switch (position) {
                case 0:
                    getIntent().getData();
                    BoilingFragment boilingFragment = BoilingFragment.newInstance(position + 1);
                    extras.putInt("ID", id_food);
                    Log.d(LOG_TAG, "id food = " + id_food);
                    boilingFragment.setArguments(extras);
                    return boilingFragment;
                case 1:
                    getIntent().getData();
                    FireFragment fireFragment = FireFragment.newInstance(position + 1);
                    extras.putInt("ID", id_food);
                    Log.d(LOG_TAG, "id food = " + id_food);
                    fireFragment.setArguments(extras);
                    return fireFragment;
                case 2:
                    getIntent().getData();
                    SteamFragment steamFragment = SteamFragment.newInstance(position + 1);
                    extras.putInt("ID", id_food);
                    Log.d(LOG_TAG, "id food = " + id_food);
                    steamFragment.setArguments(extras);
                    return steamFragment;
                case 3:
                    getIntent().getData();
                    OvenFragment ovenFragment = OvenFragment.newInstance(position + 1);
                    extras.putInt("ID", id_food);
                    Log.d(LOG_TAG, "id food = " + id_food);
                    ovenFragment.setArguments(extras);
                    return ovenFragment;
            }
            return null;
        }

        public CharSequence title_Boiling_Fire_Steam_Oven(int position) {
            switch (position) {
                case 0:
                    return getString(R.string.tab_boil).toUpperCase(locale);
                case 1:
                    return getString(R.string.tab_fire).toUpperCase(locale);
                case 2:
                    return getString(R.string.tab_steam).toUpperCase(locale);
                case 3:
                    return getString(R.string.tab_oven).toUpperCase(locale);
            }
            return null;

        }

        @Override
        public Fragment getItem(int position) {
            switch (checkTheCase()) {
                case BOILING:
                    return case_Boiling(position);
                case FIRE:
                    return case_Boiling_Fire(position);
                case STEAM:
                    return case_Boiling_Steam(position);
                case OVEN:
                    return case_Boiling_Oven(position);
                case FIRE_STEAM:
                    return case_Boiling_Fire_Steam(position);
                case FIRE_OVEN:
                    return case_Boiling_Fire_Oven(position);
                case STEAM_OVEN:
                    return case_Boiling_Steam_Oven(position);
                case FIRE_STEAM_OVEN:
                    return case_Boiling_Fire_Steam_Oven(position);
                default:
                    Log.v("getItem", "sono in default case :/");
                    return new Fragment();
            }
        }

        @Override
        public int getCount() {

            switch (checkTheCase()) {
                case BOILING:
                    return 1;
                case FIRE:
                    return 2;
                case STEAM:
                    return 2;
                case OVEN:
                    return 2;
                case FIRE_STEAM:
                    return 3;
                case FIRE_OVEN:
                    return 3;
                case STEAM_OVEN:
                    return 3;
                case FIRE_STEAM_OVEN:
                    return 4;
                default:
                    return 1;
            }

        }


        @Override
        public CharSequence getPageTitle(int position) {
            switch (checkTheCase()) {
                case BOILING:
                    return title_Boiling(position);
                case FIRE:
                    return title_Boiling_Fire(position);
                case STEAM:
                    return title_Boiling_Steam(position);
                case OVEN:
                    return title_Boiling_Oven(position);
                case FIRE_STEAM:
                    return title_Boiling_Fire_Steam(position);
                case FIRE_OVEN:
                    return title_Boiling_Fire_Oven(position);
                case STEAM_OVEN:
                    return title_Boiling_Steam_Oven(position);
                case FIRE_STEAM_OVEN:
                    return title_Boiling_Fire_Steam_Oven(position);
            }
            return null;
        }

    }

}


