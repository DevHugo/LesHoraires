package com.amine.horaires.listfavorites;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import com.amine.horaires.OptionsActivity;
import com.amine.horaires.R;
import com.amine.horaires.bdd.FavorisDao;
import com.amine.horaires.listshop.ListShopsFragment;
import com.amine.horaires.models.Shop;
import com.amine.horaires.search.SearchActivity;
import com.amine.horaires.util.Configuration;
import com.melnykov.fab.FloatingActionButton;
import de.cketti.library.changelog.ChangeLog;

import java.util.ArrayList;

public class ListFavoritesActivity extends OptionsActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (findViewById(R.id.fragmentContainer) != null) {

            // However, if we're being restored from a previous state,
            // then we don't need to do anything and should return or else
            // we could end up with overlapping fragments.
            if (savedInstanceState != null) {
                return;
            }

            // Create a new Fragment to be placed in the activity layout
            ListShopsFragment firstFragment = new ListShopsFragment();

            // Get all favorites shops
            FavorisDao dao = FavorisDao.getInstance(getApplicationContext());
            dao.openReadable();
            ArrayList<Shop> shops = dao.getAllFavoris();

            Bundle args = new Bundle();
            args.putParcelableArrayList("list", shops);
            args.putString("errorEmptyList", getErrorMessageListFavoritesEmpty());

            firstFragment.setArguments(args);

            // Add the fragment to the 'fragment_container' FrameLayout
            getSupportFragmentManager().beginTransaction().add(R.id.fragmentContainer, firstFragment).commit();
        }

        // Save user country
        saveUserCountry();

        // Display changelog
        ChangeLog cl = new ChangeLog(this);
        if (cl.isFirstRun()) {
            cl.getLogDialog().show();
        }

        // Configure SearchButton
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                search();
            }
        });
    }

    /**
     * Used when the fragment want to launch the Search.
     */
    private void search() {
        Intent intent = new Intent(this, SearchActivity.class);
        startActivity(intent);
    }

    public String getErrorMessageListFavoritesEmpty() {
        return getString(R.string.listvide);
    }

    /**
     * We save the user country to display the best api url.
     */
    private void saveUserCountry() {
        TelephonyManager tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        Configuration.pays = tm.getSimCountryIso().toUpperCase();
        if (Configuration.pays == null || Configuration.pays.equals("") || Configuration.pays.isEmpty())
            Configuration.pays = ListFavoritesActivity.this.getResources().getConfiguration().locale.getCountry();

        SharedPreferences settings = getSharedPreferences("UserInfo", 0);
        if (settings.getString("Pays", "") == null || !settings.getString("Pays", "").equals(Configuration.pays)) {
            settings = getSharedPreferences("UserInfo", 0);
            SharedPreferences.Editor editor = settings.edit();
            editor.putString("Pays", Configuration.pays);
            editor.apply();
        }
    }

    public boolean onOptionsItemSelected(MenuItem menuItem) {
        return defaultOptionsItemSelected (menuItem);
    }
}
