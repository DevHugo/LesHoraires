package com.amine.horaires.shopdetail;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import com.amine.horaires.OptionsActivity;
import com.amine.horaires.R;
import com.amine.horaires.models.Shop;

public class DetailShopActivity extends OptionsActivity implements UpdateActionBar {

    private MenuItem favMenu;
    private DetailShopFragment detailShopFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_shop);

        Shop s = getIntent().getExtras().getParcelable("shop");

        if (findViewById(R.id.fragmentContainer) != null) {

            // However, if we're being restored from a previous state,
            // then we don't need to do anything and should return or else
            // we could end up with overlapping fragments.
            if (savedInstanceState != null) {
                return;
            }

            // Create a new Fragment to be placed in the activity layout
             detailShopFragment = new DetailShopFragment();

                Bundle args = new Bundle();
                args.putParcelable("shop", s);

            detailShopFragment.setArguments(args);

            // Add the fragment to the 'fragmentContainer' FrameLayout
            getSupportFragmentManager().beginTransaction().add(R.id.fragmentContainer, detailShopFragment).commit();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_activity_actions_shop, menu);

        favMenu = menu.findItem(R.id.action_fav);

        updateFavsIcon(detailShopFragment.getFav());

        return super.onCreateOptionsMenu(menu);
    }

    public boolean onOptionsItemSelected(MenuItem menuItem) {
        boolean isActionPerformed = defaultOptionsItemSelected(menuItem);

        if (isActionPerformed) {
            return isActionPerformed;
        }
        else {
            if (menuItem.getItemId() == R.id.action_fav) {
                detailShopFragment.updateFav();
                return true;
            }
        }

        return false;
    }

    @Override
    public void updateFavsIcon(boolean isFav) {

        if (isFav) {
            favMenu.setIcon(R.mipmap.ic_turned_in_black_24dp);
        } else {
            favMenu.setIcon(R.mipmap.ic_turned_in_not_black_24dp);
        }
    }
}
