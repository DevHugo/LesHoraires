package com.amine.horaires;

import android.content.Intent;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;
import com.amine.horaires.util.MyLocation;
import com.amine.horaires.util.Utils;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.Scanner;

public class ShopSearch extends OptionsActivity {
    private MyLocation myLocation;
    private RelativeLayout loading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.shop_search);

        final EditText name = (EditText) findViewById(R.id.name);
        final EditText location = (EditText) findViewById(R.id.location);
        loading = (RelativeLayout) findViewById(R.id.loading);
        Button searchButton = (Button) findViewById(R.id.searchAction);

        // @todo form validation (empty ...)
        searchButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                loading.setVisibility(View.VISIBLE);
                SearchTask s = new SearchTask();
                s.execute(Utils.generateUrlForTextLocation(name.getText().toString(), location.getText().toString()));
            }
        });

        findViewById(R.id.gpsAction).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                loading.setVisibility(View.VISIBLE);

                MyLocation.LocationResult locationResult = new MyLocation.LocationResult() {
                    @Override
                    public void gotLocation(Location location) {
                        if (location == null) {
                            Toast.makeText(getApplicationContext(), "Petit soucis lors de la localisation", Toast.LENGTH_SHORT).show();
                        } else {
                            URL url = Utils.generateUrlForLatLng(name.getText().toString(), location.getLatitude() + "", location.getLongitude() + "");
                            SearchTask s = new SearchTask();
                            s.execute(url);
                        }
                    }
                };
                myLocation = new MyLocation();
                boolean gotLocation = myLocation.getLocation(getApplicationContext(),
                        locationResult);
                if (!gotLocation) {
                    Toast.makeText(getApplicationContext(), "Erreur de localisation.", Toast.LENGTH_SHORT).show();
                }
            }
        });
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
                Log.e("ShopSearch", "The API doesn't respond correctly. Asked url was " + url.toString(), e);
            } catch (ProtocolException e) {
                Log.e("ShopSearch", "The protocol doesn't seems to be HTTP. Url was " + url.toString(), e);
            } catch (IOException e) {
                Log.e("ShopSearch", "The API response is not readable. Url was " + url.toString(), e);
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
            Intent resultIntent = new Intent();
            resultIntent.putExtra("result", string);
            setResult(MainActivity.RESULT_OK, resultIntent);
            finish();
        }
    }
}
