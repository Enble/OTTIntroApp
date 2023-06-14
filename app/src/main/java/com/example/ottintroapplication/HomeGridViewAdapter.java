package com.example.ottintroapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.ottintroapplication.dto.SimpleMovieItem;

import java.util.ArrayList;

public class HomeGridViewAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<SimpleMovieItem> items = new ArrayList<>();

    public HomeGridViewAdapter(Context context) {
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
        convertView = inflater.inflate(R.layout.home_gridview_list, viewGroup, false);

        TextView tvRank = convertView.findViewById(R.id.tvHomeGvRank);
        TextView tvTitle = convertView.findViewById(R.id.tvHomeGvTitle);
        TextView tvPlaytime = convertView.findViewById(R.id.tvHomeGvPlaytime);
        ImageView iv = convertView.findViewById(R.id.ivHomeGv);

        tvRank.setText(movieItem.getRank());
        tvTitle.setText(movieItem.getTitle());
        tvPlaytime.setText(movieItem.getRunTime());
        iv.setImageResource(movieItem.getResId());

        return convertView;
    }
}