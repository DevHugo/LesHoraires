package com.amine.horaires.listshop;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.amine.horaires.R;
import com.amine.horaires.models.Shop;
import com.amine.horaires.shopdetail.DetailShopActivity;
import com.amine.horaires.util.RecyclerItemClickListener;
import com.melnykov.fab.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class ListShopsFragment extends Fragment {
    private List<Shop> shops;
    private ListShopsAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_list_shops, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        shops = getArguments().getParcelableArrayList("list");

        // Get list android component
        RecyclerView favsRecyclerView = (RecyclerView) getView().findViewById(R.id.list);

        if (shops.isEmpty()) {
            favsRecyclerView.setVisibility(View.GONE);

            TextView emptyView = (TextView) getView().findViewById(R.id.empty_list);
            emptyView.setVisibility(View.VISIBLE);
            emptyView.setText(getArguments().getString("errorEmptyList"));
        }
        else {
            // Create an Adapter: provides access to the data items and is also responsible for making a View for each item
            adapter = new ListShopsAdapter(shops);
            favsRecyclerView.setAdapter(adapter);

            // Create a LayoutManager: Layout Manager is responsible for measuring and positioning item views within a RecyclerView
            LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
            favsRecyclerView.setLayoutManager(layoutManager);

            // Improve performances, the list have a fixed size
            favsRecyclerView.setHasFixedSize(true);

            // Open the shop on click
            favsRecyclerView.addOnItemTouchListener(
                    new RecyclerItemClickListener(getActivity().getApplicationContext(),
                            new RecyclerItemClickListener.OnItemClickListener() {
                                @Override
                                public void onItemClick(View view, final int position) {
                                    final Intent i = new Intent(view.getContext(), DetailShopActivity.class);
                                    final Handler handler = new Handler();
                                    Runnable t = new Runnable() {
                                        public void run() {
                                            i.putExtra("shop", shops.get(position));
                                            getActivity().startActivity(i);
                                        }
                                    };

                                    handler.post(t);
                                }
                            }
                    )
            );
        }
    }

    public void updateList (List<Shop> listOfShops) {
        adapter.setShops(listOfShops);
        adapter.notifyDataSetChanged();
    }



}
