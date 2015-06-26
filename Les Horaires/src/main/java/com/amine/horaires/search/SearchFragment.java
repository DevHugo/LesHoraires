package com.amine.horaires.search;


import android.content.Intent;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;
import com.amine.horaires.R;
import com.amine.horaires.models.Shop;
import com.amine.horaires.shopdetail.UpdateActionBar;
import com.amine.horaires.util.MyLocation;
import com.amine.horaires.util.Parseur;
import com.amine.horaires.util.Utils;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.List;
import java.util.Scanner;

public class SearchFragment extends Fragment {

    private EditText name;
    private EditText location;
    private Button searchButton;

    private RelativeLayout loading;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_search, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        // If the user is not connected to a network, display an error
        if (!Utils.checkDeviceConnected (getActivity().getApplicationContext())) {
            Toast.makeText(getActivity().getApplicationContext(), getResources().getString(R.string.fail_connexion), Toast.LENGTH_SHORT).show();
        }
        else {

            // Android component form
            name = (EditText) getView().findViewById(R.id.name);
            location = (EditText) getView().findViewById(R.id.location);
            searchButton = (Button) getView().findViewById(R.id.searchAction);

            // Load icons. Useful to show to the user, that something is loading.
            loading = (RelativeLayout) getView().findViewById(R.id.loading);

            searchButton.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    // If the user complete the form
                    if (name.getText().toString().trim().length() > 0 && location.getText().toString().trim().length() > 0) {

                        // Show to the user that we performs the user
                        loading.setVisibility(View.VISIBLE);

                        SearchTask s = new SearchTask();
                        s.execute(Utils.generateUrlForTextLocation(name.getText().toString(), location.getText().toString()));
                    }
                }
            });

            getView().findViewById(R.id.gpsAction).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (name.getText().toString().trim().length() > 0) {

                        loading.setVisibility(View.VISIBLE);

                        MyLocation myLocation = new MyLocation();
                        boolean gotLocation = myLocation.getLocation(getActivity().getApplicationContext(), new MyLocation.LocationResult() {
                            @Override
                            public void gotLocation(Location location) {
                                if (location == null) {
                                    displayLocalizeError();
                                } else {
                                    URL url = Utils.generateUrlForLatLng(name.getText().toString(), location.getLatitude() + "", location.getLongitude() + "");
                                    SearchTask s = new SearchTask();
                                    s.execute(url);
                                }
                            }
                        });

                        if (!gotLocation) {
                            displayLocalizeError();
                        }

                    } else {
                        Toast.makeText(getActivity().getApplicationContext(), getResources().getString(R.string.fail_fill_form), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    private void displayLocalizeError () {
        Toast.makeText(getActivity().getApplicationContext(), getResources().getString(R.string.fail_localize_user), Toast.LENGTH_SHORT).show();
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
                Log.e("ShopSearch", "The API doesn't respond correctly. Asked url was " + url.toString(), e);
            } catch (ProtocolException e) {
                Log.e("ShopSearch", "The protocol doesn't seems to be HTTP. Url was " + url.toString(), e);
            } catch (IOException e) {
                Log.e("ShopSearch", "The API response is not readable. Url was " + url.toString(), e);
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

            loading.setVisibility(View.GONE);

            List<Shop> shops = new Parseur().parserShops(string);

            ((OnSearchResult) getActivity()).onPostExecute(shops);
        }
    }
}


