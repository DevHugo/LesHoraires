package com.amine.horaires.shopdetail;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.amine.horaires.R;
import com.amine.horaires.bdd.FavorisDao;
import com.amine.horaires.listfavorites.ListFavoritesSingleton;
import com.amine.horaires.models.Shop;
import com.amine.horaires.updateshop.UpdateShopActivity;
import com.amine.horaires.util.Parseur;
import com.amine.horaires.util.Utils;
import com.melnykov.fab.FloatingActionButton;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Callback;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.List;
import java.util.Scanner;

public class DetailShopFragment extends Fragment implements UpdateFav {

    private boolean isFav = false;
    private Shop s;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_detail_shop, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        s = getArguments().getParcelable("shop");
        FavorisDao dao = FavorisDao.getInstance(getActivity().getApplicationContext());
        dao.openReadable();
        List<Shop> fs = dao.getAllFavoris();

        while (!isFav && !fs.isEmpty()) {
            isFav = fs.remove(0).getId() == s.getId();
        }

        if (isFav) {
            SearchTask st = new SearchTask();
            st.execute(Utils.generateUrlForId(s.getId()));

            dao.open();
            dao.updateFavori(s);
            dao.close();
        }

        generateView(s);
        FloatingActionButton fab = (FloatingActionButton) getView().findViewById(R.id.fab);

        final Shop finalS = s;
        fab.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                final Intent i = new Intent(getActivity().getApplicationContext(), UpdateShopActivity.class);
                i.putExtra("shop", finalS);
                startActivity(i);
            }
        });
    }

    private void generateView(final Shop s) {

        ImageView imageResult = (ImageView) getView().findViewById(R.id.imageView);

        Picasso.with(imageResult.getContext())
                .load(s.getHoraires())
                .fit()
                .into(imageResult, new Callback() {
                    @Override
                    public void onSuccess() {
                        getActivity().supportStartPostponedEnterTransition();
                    }

                    @Override
                    public void onError() {}
                });
        TextView shopName = (TextView) getView().findViewById(R.id.shopName);
        TextView shopAddress = (TextView) getView().findViewById(R.id.shopAdress);
        TextView shopOpen = (TextView) getView().findViewById(R.id.shopOpen);
        TextView shopStatus = (TextView) getView().findViewById(R.id.shopStatus);
        ImageView wifi = (ImageView) getView().findViewById(R.id.wifi);
        ImageView parking = (ImageView) getView().findViewById(R.id.parking);
        ImageView access = (ImageView) getView().findViewById(R.id.handi);

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

        getView().findViewById(R.id.imageView).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(s.getUrl()));
                startActivity(browserIntent);
            }
        });

        getView().findViewById(R.id.callButton).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String numeroAppeler = "tel:" + s.getTel();
                startActivity(new Intent(Intent.ACTION_DIAL, Uri
                        .parse(numeroAppeler)));
            }
        });

        getView().findViewById(R.id.gpsButton).setOnClickListener(new View.OnClickListener() {
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

    @Override
    public void updateFav() {
        FavorisDao dao = FavorisDao.getInstance(getActivity().getApplicationContext());

        if (isFav) {
            dao.open();
            dao.deleteFavori(s.getId());
            dao.close();

            // Notify the favorites list that he remove a shop
            ListFavoritesSingleton.getInstance().getFavoritesShops().remove(s);

        } else {
            dao.open();
            dao.insertFavori(s);
            dao.close();

            // Notify the favorites list that he added a shop
            ListFavoritesSingleton.getInstance().getFavoritesShops().add(s);
        }

        isFav = !isFav;
        ((UpdateActionBar) getActivity()).updateFavsIcon(isFav);
    }

    public boolean getFav() {
        return isFav;
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
                Log.e("ShopDisplay", "The API doesn't respond correctly. Asked url was " + url.toString(), e);
            } catch (ProtocolException e) {
                Log.e("ShopDisplay", "The protocol doesn't seems to be HTTP. Url was " + url.toString(), e);
            } catch (IOException e) {
                Log.e("ShopDisplay", "The API response is not readable. Url was " + url.toString(), e);
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
            s = new Parseur().parserShops(string).get(0);
        }
    }

}
