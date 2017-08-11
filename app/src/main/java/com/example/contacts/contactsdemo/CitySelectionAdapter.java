package com.example.contacts.contactsdemo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by muffin on 11/08/17.
 */

public class CitySelectionAdapter extends BaseAdapter implements Filterable{
    private Context mContext;
    private List<City> resultList = new ArrayList<City>();

    public CitySelectionAdapter(Context mContext) {
        this.mContext = mContext;
    }


    @Override
    public int getCount() {
        return resultList.size();
    }

    @Override
    public City getItem(int position) {
        return resultList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) mContext
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.city_list_item, parent, false);
        }
        ((TextView) convertView.findViewById(R.id.text1)).setText(getItem(position).getCityName());
        ((TextView) convertView.findViewById(R.id.text2)).setText(getItem(position).getCityCountry());
        return convertView;
    }

    public void setCityData(List<City> resultList) {
        this.resultList = resultList;
        notifyDataSetChanged();
    }

    @Override
    public Filter getFilter() {
        Filter filter = new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults filterResults = new FilterResults();
                if (constraint != null) {
                    // Assign the data to the FilterResults
                    filterResults.values = resultList;
                    filterResults.count = resultList.size();
                }
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
//                if (results != null && results.count > 0) {
//                    notifyDataSetChanged();
//                } else {
//                    notifyDataSetInvalidated();
//                }
            }};
        return filter;
    }

}
