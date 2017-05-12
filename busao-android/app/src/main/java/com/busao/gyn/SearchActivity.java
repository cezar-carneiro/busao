package com.busao.gyn;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.busao.gyn.stops.BusStop;

import java.util.List;

public class SearchActivity extends AppCompatActivity implements SearchView.OnQueryTextListener,
        SearchView.OnCloseListener {

    private String mQuery;
    private List<BusStop> mStops;
    private ViewFlipper searchResultsViewFlipper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        searchResultsViewFlipper = (ViewFlipper) findViewById(R.id.searchResultsViewFlipper);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SearchActivity.this.finish();
            }
        });

        handleIntent(getIntent());
        switchViews();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        setIntent(intent);
        handleIntent(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.search_activity_menu, menu);
        MenuItem menuItem = menu.findItem(R.id.search_activity_search_item);
        SearchView search = (SearchView) menuItem.getActionView();
        menuItem.expandActionView();
        // Associate searchable configuration with the SearchView
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        search.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        search.setOnQueryTextListener(this);
        search.setOnCloseListener(this);
        search.setIconifiedByDefault(false);
        search.setIconified(false);
        search.requestFocus();
        return true;
    }

    private void handleIntent(Intent intent) {
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            doSearch(query);
        }
    }

    @Override
    public boolean onClose() {
        finish();
        return false;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        doSearch(query);
        return true;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return false;
    }

    private void doSearch(String query){
        this.mQuery = query;

        switchViews();
    }

    private void switchViews() {
        TextView noResultsTextView = (TextView) findViewById(R.id.searchNoResultsInfoFoundTextView);
        View searchResultsLayout = findViewById(R.id.searchResultsLayout);
        if (mStops == null) {
            noResultsTextView.setText(R.string.search_initial_info);
            searchResultsViewFlipper.setDisplayedChild(searchResultsViewFlipper.indexOfChild(noResultsTextView));
        } else if(mStops.size() == 0){
            noResultsTextView.setText(R.string.search_no_results_found);
            searchResultsViewFlipper.setDisplayedChild(searchResultsViewFlipper.indexOfChild(noResultsTextView));
        } else {
            searchResultsViewFlipper.setDisplayedChild(searchResultsViewFlipper.indexOfChild(searchResultsLayout));
        }
    }

}
