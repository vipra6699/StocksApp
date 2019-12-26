package com.example.stocksapp;

import android.content.Context;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class AutosuggestAdaptor extends ArrayAdapter<String> implements Filterable {
    private List<String> mlistData;
    private List<String> stockSymbollistData;

    public AutosuggestAdaptor(@NonNull Context context, int resource) {
        super(context, resource);
        mlistData = new ArrayList<>();
        stockSymbollistData = new ArrayList<>();
    }

    public void setData(List<String> stock_name,List<String> stock_symbol) {
        mlistData.clear();
        mlistData.addAll(stock_name);
        stockSymbollistData.clear();
        stockSymbollistData.addAll(stock_symbol);
    }

    @Override
    public int getCount() {
        return mlistData.size();
    }

    @Nullable
    @Override
    public String getItem(int position) {
        return mlistData.get(position);
    }

    /**
     * Used to Return the full object directly from adapter.
     *
     * @param position
     * @return
     */
    public String getObject(int position) {
        return mlistData.get(position);
    }
    public String getDisplayObject(int position) {
        return stockSymbollistData.get(position);
    }

    @NonNull
    @Override
    public Filter getFilter() {
        Filter dataFilter = new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults filterResults = new FilterResults();
                if (constraint != null) {
                    filterResults.values = mlistData;
                    filterResults.count = mlistData.size();
                }
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                if (results != null && (results.count > 0)) {
                    notifyDataSetChanged();
                } else {
                    notifyDataSetInvalidated();
                }
            }
        };
        return dataFilter;
    }
}