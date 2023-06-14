package com.example.ottintroapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ottintroapplication.dto.ReviewItem;

import java.util.ArrayList;

public class DetailRecyclerViewAdapter extends RecyclerView.Adapter<DetailRecyclerViewAdapter.ViewHolder> {

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView star;
        TextView title;
        TextView username;
        TextView date;
        TextView content;
        public ViewHolder(View itemView) {
            super(itemView);

            star = itemView.findViewById(R.id.tvDetailRvStar);
            title = itemView.findViewById(R.id.tvDetailRvTitle);
            username = itemView.findViewById(R.id.tvDetailRvUsername);
            date = itemView.findViewById(R.id.tvDetailRvDate);
            content = itemView.findViewById(R.id.tvDetailRvContent);
        }

    }

    private ArrayList<ReviewItem> items;
    public DetailRecyclerViewAdapter(ArrayList<ReviewItem> items) {
        this.items = items;
    }

    @NonNull
    @Override
    public DetailRecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View view = inflater.inflate(R.layout.detail_recyclerview_list, parent, false);
        DetailRecyclerViewAdapter.ViewHolder vh = new DetailRecyclerViewAdapter.ViewHolder(view);

        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull DetailRecyclerViewAdapter.ViewHolder holder, int position) {
        ReviewItem reviewItem = items.get(position);

        holder.star.setText(reviewItem.getStar());
        holder.title.setText(reviewItem.getTitle());
        holder.username.setText(reviewItem.getUsername());
        holder.date.setText(reviewItem.getDate());
        holder.content.setText(reviewItem.getContent());
    }

    @Override
    public int getItemCount() {
        return items.size();
    }
}
