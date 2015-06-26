package com.amine.horaires.search;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import com.amine.horaires.listshop.ListShopsFragment;
import com.amine.horaires.OptionsActivity;
import com.amine.horaires.R;
import com.amine.horaires.models.Shop;
import com.melnykov.fab.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends OptionsActivity implements OnSearchResult {

    private FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        if (findViewById(R.id.fragmentContainer) != null) {

            // However, if we're being restored from a previous state,
            // then we don't need to do anything and should return or else
            // we could end up with overlapping fragments.
            if (savedInstanceState != null) {
                return;
            }

            // Create a new Fragment to be placed in the activity layout
            SearchFragment firstFragment = new SearchFragment();

            // Add the fragment to the 'fragmentContainer' FrameLayout
            getSupportFragmentManager().beginTransaction().add(R.id.fragmentContainer, firstFragment).commit();
        }

        // Configure SearchButton
        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                search();
            }
        });
        fab.setVisibility(View.INVISIBLE);
    }

    /**
     * Used when the fragment want to launch the Search.
     */
    private void search() {
        Intent intent = new Intent(this, SearchActivity.class);
        startActivity(intent);
    }

    /**
     * The search have found some result, need to update the fragments
     */
    @Override
    public void onPostExecute(List<Shop> result) {
        ListShopsFragment listSearchResultFragment = new ListShopsFragment();

        Bundle args = new Bundle();
        args.putParcelableArrayList("list", (ArrayList) result);
        args.putString("errorEmptyList", getErrorMessageResultListEmpty());

        listSearchResultFragment.setArguments(args);

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        transaction.replace(R.id.fragmentContainer, listSearchResultFragment);
        transaction.addToBackStack(null);

        // Commit the transaction
        transaction.commit();

        fab.setVisibility(View.VISIBLE);
    }

    public boolean onOptionsItemSelected(MenuItem menuItem) {
        return defaultOptionsItemSelected (menuItem);
    }

    public String getErrorMessageResultListEmpty() {
        return getString(R.string.resultatRechercheVide);
    }
}
