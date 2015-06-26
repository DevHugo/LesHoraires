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

public class OptionsActivity extends AppCompatActivity {

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_activity_actions, menu);
        return super.onCreateOptionsMenu(menu);
    }

    /*
     * Default operation, when the user click on the menu.
     */
    public boolean defaultOptionsItemSelected(MenuItem item) {
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
                //MainActivity.razOrFavs(OptionsActivity.this);
                return true;
            case android.R.id.home:
                this.finish();
                return (true);
            default:
                return false;
        }
    }

}
