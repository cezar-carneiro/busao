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
import android.view.View;
import android.widget.TextView;

import com.busao.gyn.R;
import com.busao.gyn.components.RecyclerViewEmptySupport;
import com.busao.gyn.data.BusaoDatabase;
import com.busao.gyn.data.IBusStopDataSource;
import com.busao.gyn.data.stop.BusStop;
import com.busao.gyn.data.stop.BusStopDataSource;
import com.busao.gyn.data.stop.BusStopWithLines;

import java.util.List;

public class SearchActivity extends AppCompatActivity implements SearchView.OnQueryTextListener, MenuItemCompat.OnActionExpandListener {

    private IBusStopDataSource mDataSource;
    private TextView mSearchNoResultsInfoFoundTextView;
    private SearchResultsRecyclerViewAdapter mSearchResultsRecyclerViewAdapter;

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

        mDataSource = new BusStopDataSource(BusaoDatabase.getInstance(this).busStopDao());
        mSearchNoResultsInfoFoundTextView = (TextView) findViewById(R.id.searchNoResultsInfoFoundTextView);

        RecyclerViewEmptySupport searchResultsRecyclerView = (RecyclerViewEmptySupport) findViewById(R.id.searchResultsRecyclerView);
        searchResultsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        searchResultsRecyclerView.setmEmptyView(mSearchNoResultsInfoFoundTextView);

        mSearchResultsRecyclerViewAdapter = new SearchResultsRecyclerViewAdapter(mDataSource);
        searchResultsRecyclerView.setAdapter(mSearchResultsRecyclerViewAdapter);

        handleIntent(getIntent());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
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
        List<BusStopWithLines> mStops = mDataSource.searchByText(query);
        if(mStops == null || mStops.size() == 0) {
            mSearchNoResultsInfoFoundTextView.setText("Nenhum resultado encontrado!");
            mSearchResultsRecyclerViewAdapter.clear();
            return;
        }
        mSearchResultsRecyclerViewAdapter.setQuery(query);
        mSearchResultsRecyclerViewAdapter.refresh(mStops);
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
