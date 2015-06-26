package com.amine.horaires.updateshop;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;
import com.amine.horaires.R;
import com.amine.horaires.models.Horaires;
import com.amine.horaires.models.Shop;
import com.amine.horaires.util.Utils;
import com.melnykov.fab.FloatingActionButton;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Scanner;

public class UpdateShopFragment extends Fragment {
    private ArrayList<Horaires> h;
    private RelativeLayout loading;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_update_shop, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


        loading = (RelativeLayout) getView().findViewById(R.id.loading);

        final Shop s = getArguments().getParcelable("shop");

        ListView hList = (ListView) getView().findViewById(R.id.horaires_list);
        FloatingActionButton fab = (FloatingActionButton) getView().findViewById(R.id.fab);
        Button save = (Button) getView().findViewById(R.id.button);

        h = new ArrayList<Horaires>();

        initializeHoraires();

        final UpdateAdapter adapter = new UpdateAdapter(getActivity().getApplicationContext(), h, getActivity().getFragmentManager());

        hList.setAdapter(adapter);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Horaires h = adapter.getItem(adapter.getCount() - 1);
                adapter.add(new Horaires(h));
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loading.setVisibility(View.VISIBLE);

                ArrayList<HashMap<String, Integer>> periodes = new ArrayList<HashMap<String, Integer>>();
                Collections.sort(h);

                String periodsString = "";
                int i = 0;
                for (Horaires hor : h) {
                    periodsString += "&periods[0][" + i + "][day]=" + hor.getDay();
                    periodsString += "&periods[0][" + i + "][from_h]=" + hor.getFrom_h();
                    periodsString += "&periods[0][" + i + "][to_h]=" + hor.getTo_h();
                    periodsString += "&periods[0][" + i + "][to_m]=" + hor.getTo_m();
                    i++;
                }

                // Toast.makeText(getApplicationContext(), "Cette fonctionnalité est en attente de validation par Les-horaires.fr", Toast.LENGTH_SHORT).show();
                UpdateTask u = new UpdateTask();
                u.execute(Utils.generateUrlForEdit(s.getId(), periodsString));

            }
        });
    }

    private void initializeHoraires() {
        h.add(new Horaires(1));
    }

    private class UpdateTask extends AsyncTask<URL, Void, String> {

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
                conn.setRequestMethod("POST");
                conn.setDoInput(true);
                conn.setDoOutput(true);
                conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");

                // Starts the query
                conn.connect();
                is = conn.getInputStream();

                // Convert the InputStream into a String

                Scanner reader = new Scanner(is, "ISO-8859-1");
                while (reader.hasNextLine()) {
                    contentAsString = contentAsString + reader.nextLine();
                }
            } catch (MalformedURLException e) {
                Log.e("ShopUpdate", "The API doesn't respond correctly. Asked url was " + url.toString(), e);
            } catch (ProtocolException e) {
                Log.e("ShopUpdate", "The protocol doesn't seems to be HTTP. Url was " + url.toString(), e);
            } catch (IOException e) {
                Log.e("ShopUpdate", "The API response is not readable. Url was " + url.toString(), e);
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
            loading.setVisibility(View.GONE);
            super.onPostExecute(string);
            Toast.makeText(getActivity().getApplicationContext(), getResources().getString(R.string.success_update_shop), Toast.LENGTH_SHORT).show();
        }
    }
}
