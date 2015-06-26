package com.amine.horaires.search;


import com.amine.horaires.models.Shop;

import java.util.List;

public interface OnSearchResult  {
    void onPostExecute(List<Shop> result);
}
