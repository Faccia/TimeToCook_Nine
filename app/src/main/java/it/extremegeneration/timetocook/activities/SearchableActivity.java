package it.extremegeneration.timetocook.activities;


import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.widget.Toast;

import it.extremegeneration.timetocook.R;
import it.extremegeneration.timetocook.fragments.SearchableFragment;

public class SearchableActivity extends AppCompatActivity {

    public static final String QUERY_FOR_BUNDLE = "query";
    private Toolbar mToolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        Log.v("Activity", "onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_searchable);

        handleIntent(getIntent(), false);

        // Toolbar stuff
        mToolbar = (Toolbar) findViewById(R.id.activity_searchable_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        Log.v("Activity", "onCreateOptionMenu");
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.option_menu, menu);

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.search_item).getActionView();

        //Specify the searchable activity
        //1) this assume the searchable activity is this one
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));

        searchView.setSubmitButtonEnabled(true); //Submit research button
        searchView.setQueryRefinementEnabled(true); //Move the suggestion in the search bar


        searchView.setIconifiedByDefault(true);  // Do not iconify the widget; expand it by default

        return true;
    }

    @Override
    protected void onNewIntent(Intent intent) {
        Log.v("(onNewIntent", "im in NewIntent -Activity");
        // Because this activity has set launchMode="singleTop", the system calls this method
        // to deliver the intent if this activity is currently the foreground activity when
        // invoked again (when the user executes a search from this activity, we don't create
        // a new instance of this activity, so the system delivers the search intent here)
        handleIntent(intent, true);
    }


    private void handleIntent(Intent intent, boolean onNewIntent) {
        //Get the intent, verify the action and get the query
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            //DO MY STUFF WITH THE QUERY
            Bundle arguments = new Bundle();
            arguments.putString(QUERY_FOR_BUNDLE, query);

            SearchableFragment searchableFragment = new SearchableFragment();
            searchableFragment.setArguments(arguments);

            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.activity_searchable_container, searchableFragment)
                    .commit();

            Toast.makeText(getApplicationContext(), "(onCreate)Searching by: " + query, Toast.LENGTH_SHORT).show();

        } else if (Intent.ACTION_VIEW.equals(intent.getAction())) {
            Uri uri = intent.getData();
            int id = Integer.parseInt(uri.getLastPathSegment());

            Intent intentDetailActivity = new Intent (getApplicationContext(), DetailActivity.class);
            intentDetailActivity.putExtra("ID", id);

            Toast.makeText(getApplicationContext(), "(onCreate) Suggestion ID: " + id, Toast.LENGTH_SHORT).show();
            Toast.makeText(getApplicationContext(), "(onCreate) Suggestion URI: " + uri, Toast.LENGTH_LONG).show();

            startActivity(intentDetailActivity);
            finish();

        }

    }


}
