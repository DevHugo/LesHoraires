package com.amine.horaires.updateshop;

import android.os.Bundle;
import com.amine.horaires.OptionsActivity;
import com.amine.horaires.R;
import com.amine.horaires.models.Shop;
import com.amine.horaires.shopdetail.DetailShopFragment;

public class UpdateShopActivity extends OptionsActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_shop);

        Shop s = getIntent().getExtras().getParcelable("shop");

        if (findViewById(R.id.fragmentContainer) != null) {

            // However, if we're being restored from a previous state,
            // then we don't need to do anything and should return or else
            // we could end up with overlapping fragments.
            if (savedInstanceState != null) {
                return;
            }

            // Create a new Fragment to be placed in the activity layout
            UpdateShopFragment updateShopFragment = new UpdateShopFragment();

            Bundle args = new Bundle();
            args.putParcelable("shop", s);

            updateShopFragment.setArguments(args);

            // Add the fragment to the 'fragmentContainer' FrameLayout
            getSupportFragmentManager().beginTransaction().add(R.id.fragmentContainer, updateShopFragment).commit();
        }
    }
}
