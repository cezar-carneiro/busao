package com.busao.gyn;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

public class SearchActivity extends AppCompatActivity implements SearchView.OnQueryTextListener,
        SearchView.OnCloseListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SearchActivity.this.finish();
            }
        });

        handleIntent(getIntent());
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
        //TODO: we'll do some searching here
    }

}
