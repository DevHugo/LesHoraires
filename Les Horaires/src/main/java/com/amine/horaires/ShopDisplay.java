package com.amine.horaires;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.amine.horaires.bdd.FavorisDao;
import com.amine.horaires.models.Shop;
import com.amine.horaires.util.Configuration;
import com.amine.horaires.util.Parseur;
import com.amine.horaires.util.Utils;
import com.melnykov.fab.FloatingActionButton;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.List;
import java.util.Scanner;

public class ShopDisplay extends OptionsActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.shop_display);
        Shop s = getIntent().getExtras().getParcelable("shop");

        Configuration.currentShop = s;

        FavorisDao dao = FavorisDao.getInstance(ShopDisplay.this);
        dao.open();
        List<Shop> fs = dao.getAllFavoris();
        dao.close();

        while (!isFav && !fs.isEmpty()) {
            isFav = fs.remove(0).getId() == Configuration.currentShop.getId();
        }
        if (isFav) {
            SearchTask st = new SearchTask();
            st.execute(Utils.generateUrlForId(s.getId()));
            s = Configuration.currentShop;
            dao.open();
            dao.updateFavori(s);
            dao.close();
        }
        generateView(s);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);

        final Shop finalS = s;
        fab.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                final Intent i = new Intent(getBaseContext(), ShopUpdate.class);
                i.putExtra("shop", finalS);
                startActivity(i);
            }
        });
    }

    private void generateView(final Shop s) {
        Configuration.currentShop = s;

        ImageView imageResult = (ImageView) findViewById(R.id.imageView);

        ImageTask t = new ImageTask(imageResult);
        t.execute(s.getHoraires());

        TextView shopName = (TextView) findViewById(R.id.shopName);
        TextView shopAddress = (TextView) findViewById(R.id.shopAdress);
        TextView shopOpen = (TextView) findViewById(R.id.shopOpen);
        TextView shopStatus = (TextView) findViewById(R.id.shopStatus);
        ImageView wifi = (ImageView) findViewById(R.id.wifi);
        ImageView parking = (ImageView) findViewById(R.id.parking);
        ImageView access = (ImageView) findViewById(R.id.handi);

        shopName.setText(s.getName());
        shopAddress.setText(s.getAdresse());
        shopOpen.setText("Le magasin est " + (s.isOuvert() ? "ouvert" : "fermé"));
        if (s.isOuvert()) {
            shopStatus.setText(s.getOpenStatus());
        }

        if (!s.isWifi()) {
            wifi.setAlpha((float) 0.1);
        }

        if (!s.isAccesHandicape()) {
            access.setAlpha((float) 0.1);
        }

        if (!s.isParking()) {
            parking.setAlpha((float) 0.1);
        }

        findViewById(R.id.imageView).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(s.getUrl()));
                startActivity(browserIntent);
            }
        });

        findViewById(R.id.callButton).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String numeroAppeler = "tel:" + s.getTel();
                startActivity(new Intent(Intent.ACTION_DIAL, Uri
                        .parse(numeroAppeler)));
            }
        });

        findViewById(R.id.gpsButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new
                        Intent(Intent.ACTION_VIEW,
                        Uri.parse("http://maps.google.com/maps?q="
                                + s.getAdresse() + "@" + s.getLat() + "," + s.getLng() + "")
                );
                startActivity(intent);

            }
        });
    }

    // @todo: use this to update the shop if connected.
    private boolean checkDeviceConnected() {
        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        // Need permission : android.permission.ACCESS_NETWORK_STATE
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        return (networkInfo != null && networkInfo.isConnected());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_activity_actions_shop, menu);
        favMenu = menu.findItem(R.id.action_fav);
        updateFavStatus();
        return super.onCreateOptionsMenu(menu);
    }

    public class ImageTask extends AsyncTask<String, Void, Bitmap> {
        private final ImageView contenu;

        public ImageTask(ImageView contenu) {
            this.contenu = contenu;
        }

        @Override
        protected Bitmap doInBackground(String... params) {
            URL url = null;
            Bitmap b = null;
            HttpURLConnection conn = null;
            InputStream is = null;
            try {
                url = new URL(params[0]);
                conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");
                conn.setDoInput(true);
                conn.connect();

                is = conn.getInputStream();
                b = BitmapFactory.decodeStream(is);

                is.close();
                conn.disconnect();

            } catch (MalformedURLException e) {
                Log.e("ShopDisplay", "The API doesn't respond correctly. Asked url was "+url.toString(), e);
            } catch (ProtocolException e) {
                Log.e("ShopDisplay", "The protocol doesn't seems to be HTTP. Url was " + url.toString(), e);
            } catch (IOException e) {
                Log.e("ShopDisplay", "The image seems to be corrupt. Url was " + url.toString(), e);
            } finally {
                // Makes sure that the InputStream is closed after the app is finished using it.
                if (is != null)
                    try {
                        is.close();
                    } catch (IOException e) {}
                if (conn != null)
                    conn.disconnect();
            }
            return b;
        }

        protected void onPostExecute(Bitmap b) {
            this.contenu.setImageBitmap(b);
        }
    }

    private class SearchTask extends AsyncTask<URL, Void, String> {

        @Override
        protected String doInBackground(URL... params) {
            HttpURLConnection conn = null;
            InputStream is = null;
            String contentAsString = "";
            URL url = null;
            try {
                url = params[0];
                conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(10000 /* milliseconds */);
                conn.setConnectTimeout(15000 /* milliseconds */);
                conn.setRequestMethod("GET");
                conn.setDoInput(true);

                // Starts the query
                conn.connect();
                is = conn.getInputStream();

                // Convert the InputStream into a String

                Scanner reader = new Scanner(is, "ISO-8859-1");
                while (reader.hasNextLine()) {
                    contentAsString = contentAsString + reader.nextLine();
                }
            } catch (MalformedURLException e) {
                Log.e("ShopDisplay", "The API doesn't respond correctly. Asked url was " + url.toString(), e);
            } catch (ProtocolException e) {
                Log.e("ShopDisplay", "The protocol doesn't seems to be HTTP. Url was " + url.toString(), e);
            } catch (IOException e) {
                Log.e("ShopDisplay", "The API response is not readable. Url was " + url.toString(), e);
            } finally {
                // Makes sure that the InputStream is closed after the app is finished using it.
                if (is != null)
                    try {
                        is.close();
                    } catch (IOException e) {}
                if (conn != null)
                    conn.disconnect();
            }
            return contentAsString;
        }

        @Override
        protected void onPostExecute(String string) {
            super.onPostExecute(string);
            Configuration.currentShop = new Parseur().parserShops(string).get(0);
        }
    }
}
