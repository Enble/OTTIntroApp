package com.example.ottintroapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.ottintroapplication.common.SimpleMovieItem;

import java.util.ArrayList;

public class SearchListViewAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<SimpleMovieItem> items = new ArrayList<>();

    public SearchListViewAdapter(Context context) {
        this.context = context;
    }

    @Override
    public int getCount() {
        return items.size();
    }

    public void addItem(SimpleMovieItem simpleMovieItem) {
        items.add(simpleMovieItem);
    }

    @Override
    public Object getItem(int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public void clearItems() {
        items.clear();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        final SimpleMovieItem movieItem = items.get(position);

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = inflater.inflate(R.layout.search_listview_list, viewGroup, false);

        TextView tvTitle = convertView.findViewById(R.id.tvLvTitle);
        TextView tvId = convertView.findViewById(R.id.tvLvId);
        TextView tvOverview = convertView.findViewById(R.id.tvLvOverview);
        ImageView iv = convertView.findViewById(R.id.ivSearch);

        tvTitle.setText(movieItem.getTitle());
        tvId.setText(movieItem.getMovieId());
        tvOverview.setText(movieItem.getOverview());
        iv.setImageResource(movieItem.getResId());

        return convertView;
    }
}
