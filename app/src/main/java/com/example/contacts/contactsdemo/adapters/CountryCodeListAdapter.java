package com.example.contacts.contactsdemo.adapters;

import com.example.contacts.contactsdemo.ContactDemo;

/**
 * Created by muffin on 29/06/17.
 */


import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckedTextView;
import android.widget.TextView;

import com.google.i18n.phonenumbers.PhoneNumberUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;


public class CountryCodeListAdapter extends BaseAdapter {

    private final LayoutInflater mInflater;
    private final Context mContext;
    private final List<CountryCode> mData;
    private final int mViewId;
    private final int mDropdownViewId;
    private int mSelected;

    public static final class CountryCode implements Comparable<String> {
        public String regionCode;
        public int countryCode;
        public String regionName;

        @Override
        public int compareTo(String another) {
            return regionCode != null && another != null ? regionCode.compareTo(another) : 1;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o)
                return true;

            if (o != null && o instanceof CountryCode) {
                CountryCode other = (CountryCode) o;

                return regionCode != null &&
                        regionCode.equals(other.regionCode);
            }

            return false;
        }

        @Override
        public String toString() {
            return regionCode;
        }
    }

    public CountryCodeListAdapter(Context context, int viewId, int dropdownViewId) {
        this(context, new ArrayList<CountryCode>(), viewId, dropdownViewId);
    }

    public CountryCodeListAdapter(Context context, List<CountryCode> data, int viewId, int dropdownViewId) {
        mContext = context;
        mInflater = LayoutInflater.from(context);
        mData = data;
        mViewId = viewId;
        mDropdownViewId = dropdownViewId;
    }

    public void add(CountryCode entry) {
        mData.add(entry);
    }

    public void add(String regionCode) {
        CountryCode cc = new CountryCode();
        cc.regionCode = regionCode;
        cc.countryCode = PhoneNumberUtil.getInstance().getCountryCodeForRegion(regionCode);
        cc.regionName = getRegionDisplayName(regionCode, Locale.getDefault());
        mData.add(cc);
    }

    public void clear() {
        mData.clear();
    }

    public void sort(Comparator<? super CountryCode> comparator) {
        Collections.sort(mData, comparator);
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public CountryCode getItem(int position) {
        return mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        CountryCode e = mData.get(position);
        return (e != null) ? e.countryCode : -1;
    }

    public int getPositionForId(CountryCode cc) {
        return cc != null ? mData.indexOf(cc) : -1;
    }

    public void setSelected(int position) {
        mSelected = position;
    }

    public CountryCode getSelected() {
        return  mData.get(mSelected);
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        CheckedTextView textView;
        View view;
        if (convertView == null) {
            view = mInflater.inflate(mDropdownViewId, parent, false);
            textView = (CheckedTextView) view.findViewById(android.R.id.text1);
            view.setTag(textView);
        }
        else {
            view = convertView;
            textView = (CheckedTextView) view.getTag();
        }

        CountryCode e = mData.get(position);

        StringBuilder text = new StringBuilder(5)
                .append(e.regionName)
                .append(" (+")
                .append(e.countryCode)
                .append(')');

        textView.setText(text);
        textView.setChecked((mSelected == position));

        return view;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TextView textView;
        View view;
        if (convertView == null) {
            view = mInflater.inflate(mViewId, parent, false);
            textView = (TextView) view.findViewById(android.R.id.text1);
            view.setTag(textView);
        }
        else {
            view = convertView;
            textView = (TextView) view.getTag();
        }

        CountryCode e = mData.get(position);

        StringBuilder text = new StringBuilder(3)
                .append('+')
                .append(e.countryCode)
                .append(" (")
                .append(e.regionName)
                .append(')');

        textView.setText(text);

        textView.setTextColor(mContext.getResources().getColor(android.R.color.black));
        return view;
    }

    /** Returns the localized region name for the given region code. */
    public String getRegionDisplayName(String regionCode, Locale language) {
        return (regionCode == null || regionCode.equals("ZZ") ||
                regionCode.equals(PhoneNumberUtil.REGION_CODE_FOR_NON_GEO_ENTITY))
                ? "" : new Locale("", regionCode).getDisplayCountry(language);
    }
}
