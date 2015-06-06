package com.amine.horaires;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.amine.horaires.models.Shop;

import java.util.ArrayList;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.ViewHolder> {
    private final ArrayList<Shop> mDataset;
    private final Context c;

    // Provide a suitable constructor (depends on the kind of dataset)
    public SearchAdapter(ArrayList<Shop> myDataset, Context c) {
        mDataset = myDataset;
        this.c = c;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public SearchAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                   int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fav_list_item, parent, false);
        // set the view's size, margins, paddings and layout parameters

        return new ViewHolder((RelativeLayout) v, c, mDataset);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        holder.shopAddr.setText(mDataset.get(position).getAdresse());
        holder.shopName.setText(mDataset.get(position).getName());
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        // each data item is just a string in this case
        public final TextView shopName;
        public final TextView shopAddr;
        private final Context c;
        private final ArrayList<Shop> mDataset;

        public ViewHolder(RelativeLayout v, Context c, ArrayList<Shop> mDataset) {
            super(v);
            v.setOnClickListener(this);
            shopName = (TextView) v.findViewById(R.id.shop_name);
            shopAddr = (TextView) v.findViewById(R.id.shop_adress);
            this.c = c;
            this.mDataset = mDataset;
        }

        @Override
        public void onClick(View view) {
            final Intent i = new Intent(view.getContext(), ShopDisplay.class);
            final Handler handler = new Handler();
            Runnable t = new Runnable() {
                public void run() {
                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    i.putExtra("shop", mDataset.get(getLayoutPosition()));
                    c.startActivity(i);
                }
            };

            handler.post(t);
        }

    }
}
