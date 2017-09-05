package com.busao.gyn.search;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.busao.gyn.DefaultRecyclerViewAdapter;
import com.busao.gyn.R;
import com.busao.gyn.components.RecyclerViewEmptySupport;
import com.busao.gyn.data.BusaoDatabase;
import com.busao.gyn.data.IBusLineDataSource;
import com.busao.gyn.data.IBusStopDataSource;
import com.busao.gyn.data.IDataSource;
import com.busao.gyn.data.line.BusLineDataSource;
import com.busao.gyn.data.stop.BusStopDataSource;

import java.util.List;

public class SearchActivity extends AppCompatActivity implements SearchView.OnQueryTextListener, MenuItemCompat.OnActionExpandListener {

    private IDataSource mDataSource;
    private TextView mSearchNoResultsInfoFoundTextView;
    private DefaultRecyclerViewAdapter mResultsAdapter;

    public static final String TYPE_KEY = "SEARCH_TYPE";

    public enum SearchType{
        LINE,
        STOP
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mSearchNoResultsInfoFoundTextView = (TextView) findViewById(R.id.searchNoResultsInfoFoundTextView);
        RecyclerViewEmptySupport searchResultsRecyclerView = (RecyclerViewEmptySupport) findViewById(R.id.searchResultsRecyclerView);
        searchResultsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        searchResultsRecyclerView.setEmptyView(mSearchNoResultsInfoFoundTextView);

        SearchType type = (SearchType) getIntent().getSerializableExtra(TYPE_KEY);
        switch (type){
            case LINE:
                mDataSource = new BusLineDataSource(BusaoDatabase.get(SearchActivity.this).busLineDao());
                mResultsAdapter = new LinesSearchResultsRecyclerViewAdapter((IBusLineDataSource) mDataSource);
                break;
            case STOP:
                mDataSource = new BusStopDataSource(BusaoDatabase.get(this).busStopDao());
                mResultsAdapter = new StopsSearchResultsRecyclerViewAdapter((IBusStopDataSource) mDataSource);
                break;
        }

        searchResultsRecyclerView.setAdapter(mResultsAdapter);

        handleIntent(getIntent());
    }

    @Override
    protected void onNewIntent(Intent intent) {
        setIntent(intent);
        handleIntent(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search_activity_menu, menu);
        MenuItem menuItem = menu.findItem(R.id.search_activity_search_item);
        SearchView search = (SearchView) menuItem.getActionView();
        menuItem.expandActionView();
        MenuItemCompat.setOnActionExpandListener(menuItem, this);

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        search.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        search.setOnQueryTextListener(this);
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
    public boolean onQueryTextSubmit(String query) {
        doSearch(query);
        return true;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return false;
    }

    private void doSearch(String query) {
        List results = mDataSource.searchByText(query);
        if(results == null || results.size() == 0) {
            mSearchNoResultsInfoFoundTextView.setText("Nenhum resultado encontrado!");
            mResultsAdapter.clear();
            return;
        }
        mResultsAdapter.setQuery(query);
        mResultsAdapter.refresh(results);
    }

    @Override
    public boolean onMenuItemActionCollapse(MenuItem item) {
        finish();
        return true;
    }

    @Override
    public boolean onMenuItemActionExpand(MenuItem item) {
        return true;
    }

}
