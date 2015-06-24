package com.amine.horaires.listshop;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.amine.horaires.R;
import com.amine.horaires.models.Shop;

import java.util.List;

public class ListShopsAdapter extends RecyclerView.Adapter<ListShopsAdapter.ShopViewHolder> {

    private List<Shop> shops;

    ListShopsAdapter(List<Shop> shop){
        this.shops = shop;
    }

    public class ShopViewHolder extends RecyclerView.ViewHolder {

        private CardView cv;
        private TextView shopName;
        private TextView shopAddress;

        ShopViewHolder(View itemView) {
            super(itemView);
            cv = (CardView)itemView.findViewById(R.id.card_view);
            shopName = (TextView)itemView.findViewById(R.id.shop_name);
            shopAddress = (TextView)itemView.findViewById(R.id.shop_adress);
        }

        public TextView getShopName() {
            return shopName;
        }

        public TextView getShopAddress() {
            return shopAddress;
        }
    }

    @Override
    public ShopViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_shop_item, parent, false);
        ShopViewHolder pvh = new ShopViewHolder(v);
        return pvh;
    }

    @Override
    public void onBindViewHolder(ShopViewHolder holder, int position) {
        holder.getShopName().setText(shops.get(position).getName());
        holder.getShopAddress().setText(shops.get(position).getAdresse());
    }

    @Override
    public int getItemCount() {
        return shops.size();
    }
}
