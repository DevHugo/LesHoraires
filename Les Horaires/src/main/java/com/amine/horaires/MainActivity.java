package com.amine.horaires;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.telephony.TelephonyManager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.TextView;
import com.amine.horaires.bdd.FavorisDao;
import com.amine.horaires.models.Shop;
import com.amine.horaires.util.Configuration;
import com.amine.horaires.util.Parseur;
import com.melnykov.fab.FloatingActionButton;
import de.cketti.library.changelog.ChangeLog;

import java.util.ArrayList;

public class MainActivity extends OptionsActivity {

    private static RecyclerView mRecyclerView;
    private static RecyclerView.Adapter mAdapter;
    private static ArrayList<Shop> myDataset;
    private static TextView emptyView;

    static void razOrFavs(Context c) {
        FavorisDao dao = FavorisDao.getInstance(c);
        dao.open();
        myDataset = dao.getAllFavoris();
        dao.close();

        if (myDataset.isEmpty()) {
            mRecyclerView.setVisibility(View.GONE);
            emptyView.setVisibility(View.VISIBLE);
        } else {
            mAdapter = new SearchAdapter(myDataset, c);
            mRecyclerView.setAdapter(mAdapter);
            mRecyclerView.setVisibility(View.VISIBLE);
            emptyView.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        checkLocale();
        ChangeLog cl = new ChangeLog(this);
        if (cl.isFirstRun()) {
            cl.getLogDialog().show();
        }

        mRecyclerView = (RecyclerView) findViewById(R.id.favs_list);
        emptyView = (TextView) findViewById(R.id.empty_list);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.attachToRecyclerView(mRecyclerView);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Intent i = new Intent(getBaseContext(), ShopSearch.class);
                startActivityForResult(i, 0);
            }
        });

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        // specify an adapter (see also next example)
        myDataset = new ArrayList<Shop>();

        razOrFavs(getBaseContext());
    }

    private void checkLocale() {
        TelephonyManager tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        Configuration.pays = tm.getSimCountryIso().toUpperCase();
        if (Configuration.pays == null || Configuration.pays.equals("") || Configuration.pays.isEmpty())
            Configuration.pays = MainActivity.this.getResources().getConfiguration().locale.getCountry();

        SharedPreferences settings = getSharedPreferences("UserInfo", 0);
        if (settings.getString("Pays", "") == null || !settings.getString("Pays", "").equals(Configuration.pays)) {
            settings = getSharedPreferences("UserInfo", 0);
            SharedPreferences.Editor editor = settings.edit();
            editor.putString("Pays", Configuration.pays);
            editor.apply();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case (0): {
                if (resultCode == Activity.RESULT_OK) {
                    myDataset = new ArrayList<Shop>();
                    String result = data.getStringExtra("result");
                    myDataset = new Parseur().parserShops(result);
                    if (myDataset.isEmpty()) {
                        mRecyclerView.setVisibility(View.GONE);
                        emptyView.setVisibility(View.VISIBLE);
                    } else {
                        mAdapter = new SearchAdapter(myDataset, getBaseContext());
                        mRecyclerView.setAdapter(mAdapter);
                        mRecyclerView.setVisibility(View.VISIBLE);
                        emptyView.setVisibility(View.GONE);
                    }
                }
                break;
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_activity_actions_main, menu);
        return super.onCreateOptionsMenu(menu);
    }
}
