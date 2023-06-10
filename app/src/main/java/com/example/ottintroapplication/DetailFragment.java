package com.example.ottintroapplication;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.ottintroapplication.common.MetadataCols;

public class DetailFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.detail_fragment, container, false);

        Bundle bundle = getArguments();
        String[] metadata = bundle.getStringArray("metadata");
        String[] credit = bundle.getStringArray("credit");

        TextView title = v.findViewById(R.id.tvDetailTitle);
        TextView score = v.findViewById(R.id.tvDetailScore);
        TextView year = v.findViewById(R.id.tvDetailYear);
        TextView runtime = v.findViewById(R.id.tvDetailRuntime);

        title.setText(metadata[MetadataCols.TITLE.ordinal()]);
        score.setText(metadata[MetadataCols.VOTE_AVERAGE.ordinal()]);
        String str = metadata[MetadataCols.RELEASE_DATE.ordinal()].substring(0, 3);
        year.setText(str);
        runtime.setText(metadata[MetadataCols.RUNTIME.ordinal()]);

        TextView rating = v.findViewById(R.id.tvDetailRating);

        return v;
    }

}
