package com.amine.horaires;

import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.Window;
import com.amine.horaires.bdd.FavorisDao;
import com.amine.horaires.models.Shop;
import com.amine.horaires.util.Configuration;

class OptionsActivity extends AppCompatActivity {
    MenuItem favMenu = null;
    boolean isFav = false;
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_activity_actions, menu);
        return super.onCreateOptionsMenu(menu);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_mail:
                Intent intent = new Intent(Intent.ACTION_SENDTO); // it's not ACTION_SEND
                intent.setType("text/plain");
                intent.putExtra(Intent.EXTRA_SUBJECT, "Les Horaires");
                intent.setData(Uri.parse("mailto:amine.bou.dev@gmail.com")); // or just "mailto:" for blank
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK); // this will make such that when user returns to your app, your app is displayed, instead of the email app.
                startActivity(intent);
                return true;
            case R.id.action_about:
                final Dialog dialog = new Dialog(this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setCancelable(true);

                // Content of the dialog
                dialog.setContentView(R.layout.activity_more);
                dialog.show();
                return true;
            case R.id.action_reload:
                MainActivity.razOrFavs(OptionsActivity.this);
                return true;
            case R.id.action_fav:
                FavorisDao dao = FavorisDao.getInstance(OptionsActivity.this);
                if (isFav) {
                    // REMOVE
                    dao.open();
                    dao.deleteFavori(Configuration.currentShop.getId());
                    dao.close();
                    updateFavStatus(false);
                } else {
                    dao.open();
                    dao.insertFavori(Configuration.currentShop);
                    dao.close();
                    updateFavStatus(true);
                }
                return true;
            case android.R.id.home:
                this.finish();
                return (true);
            default:
                return false;
        }
    }

    private void updateFavStatus(boolean state) {
        if (state) {
            favMenu.setIcon(R.mipmap.ic_turned_in_black_24dp);
        } else {
            favMenu.setIcon(R.mipmap.ic_turned_in_not_black_24dp);
        }
    }

    void updateFavStatus() {
        FavorisDao dao = FavorisDao.getInstance(OptionsActivity.this);
        dao.open();
        Shop sh = dao.getFavori(Configuration.currentShop.getId());
        dao.close();
        if (sh == null) {
            updateFavStatus(false);
        } else {
            updateFavStatus(true);
        }
    }
}
