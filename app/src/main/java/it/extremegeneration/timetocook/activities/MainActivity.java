package it.extremegeneration.timetocook.activities;

import android.app.SearchManager;
import android.content.ComponentName;
import android.content.Context;
import android.net.Uri;
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
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import java.util.Locale;

import it.extremegeneration.timetocook.R;
import it.extremegeneration.timetocook.fragments.CategoriesFragment;
import it.extremegeneration.timetocook.fragments.FavFragment;
import it.extremegeneration.timetocook.fragments.MainFragment;
import it.extremegeneration.timetocook.fragments.SearchFragment;

public class MainActivity extends AppCompatActivity implements MainFragment.Callback {

    private SectionPagerAdapter mSectionPagerAdapter;
    private ViewPager mViewPager;
    private TabLayout mTabLayout;

    private Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Toolbar stuff
        mToolbar = (Toolbar) findViewById(R.id.activity_main_toolbar);
        setSupportActionBar(mToolbar);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mToolbar.setElevation(0);
        }
        getSupportActionBar().setDisplayShowTitleEnabled(false);


        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionPagerAdapter = new SectionPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.activity_main_viewPager);
        mViewPager.setAdapter(mSectionPagerAdapter);
        mViewPager.setOffscreenPageLimit(3);
        mTabLayout = (TabLayout) findViewById(R.id.activity_main_tabLayout);
        mTabLayout.setupWithViewPager(mViewPager);
        mTabLayout.setTabMode(TabLayout.MODE_FIXED);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.option_menu, menu);

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.search_item).getActionView();

        //Specify the searchable activity
        //1) this assume the searchable activity is this one
        //searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));

        searchView.setSearchableInfo(searchManager.getSearchableInfo
                (new ComponentName(this, SearchableActivity.class)));

        searchView.setIconifiedByDefault(false);  // Do not iconify the widget; expand it by default

        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.search_item:
                onSearchRequested();
                return true;

            default:
                return false;
        }
    }

    @Override
    public void onItemSelected(Uri foodUri) {

    }

    public class SectionPagerAdapter extends FragmentPagerAdapter {

        public SectionPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {

            switch (position) {
                case 0:
                    return SearchFragment.newInstance(position + 1);
                case 1:
                    return CategoriesFragment.newInstance(position + 1);
                case 2:
                    return FavFragment.newInstance(position + 1);
            }
            return null;
        }

        @Override
        public int getCount() {
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            Locale locale = Locale.getDefault();
            switch (position) {
                case 0:
                    return getString(R.string.tab_home).toUpperCase(locale);
                case 1:
                    return getString(R.string.tab_categories).toUpperCase(locale);
                case 2:
                    return getString(R.string.tab_fav).toUpperCase(locale);
            }

            return null;
        }
    }
}
