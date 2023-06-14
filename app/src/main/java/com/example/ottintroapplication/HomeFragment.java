package com.example.ottintroapplication;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.ottintroapplication.common.cols.MetadataCols;
import com.example.ottintroapplication.dto.SimpleMovieItem;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class HomeFragment extends Fragment {
    private List<String[]> data = new ArrayList<>();
    HomeGridViewAdapter adapter;
    private final String[] sortCriterias = {"VOTE_AVERAGE", "VOTE_COUNT"};
    private final String[] criteriaName = new String[]{"평점", "투표수"};
    private String sortCriteria = sortCriterias[0];

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.home_fragment, container, false);

        Button btnFilter = v.findViewById(R.id.btnHomeFilter);
        if(sortCriteria == sortCriterias[0])
            btnFilter.setText("정렬 기준 : " + criteriaName[0]);
        else if(sortCriteria == sortCriterias[1])
            btnFilter.setText("정렬 기준 : " + criteriaName[1]);
        btnFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(v);
            }
        });

        final GridView gridView = v.findViewById(R.id.gvHome);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                View dialog = View.inflate(getContext(), R.layout.big_poster_dialog, null);
                ImageView ivBigPoster = dialog.findViewById(R.id.ivBigPoster);
                ivBigPoster.setScaleType(ImageView.ScaleType.FIT_CENTER);
                ivBigPoster.setImageResource(R.drawable.poster_sample);

                new AlertDialog.Builder(parent.getContext())
                        .setNegativeButton("닫기", null)
                        .setView(dialog)
                        .show();
            }
        });

        adapter = new HomeGridViewAdapter(v.getContext());

        Bundle bundle = getArguments();
        for(int i=0; i<100; i++) {
            String[] strings = bundle.getStringArray("item" + i);
            data.add(strings);
        }

        List<String[]> list = new ArrayList<>();
        if(sortCriteria == sortCriterias[0]) {
            list = sortByAverage();
        }
        else if(sortCriteria == sortCriterias[1]) {
            list = sortByCount();
        }

        for(int i=0; i<20; i++) {
            String[] strings = list.get(i);
            String title = strings[MetadataCols.TITLE.ordinal()];
            String id = strings[MetadataCols.ID.ordinal()];
            String tmpRuntime = strings[MetadataCols.RUNTIME.ordinal()];
            String runtime = tmpRuntime.substring(0, tmpRuntime.length() - 2) + " 분";
            String overview = strings[MetadataCols.OVERVIEW.ordinal()];
            String rank = String.valueOf(i + 1);

            adapter.addItem(new SimpleMovieItem(rank, title, id, runtime, overview, R.drawable.poster_sample));
        }

        gridView.setAdapter(adapter);

        return v;
    }

    private List<String[]> sortByCount() {
        return data.stream()
                .limit(20)
                .collect(Collectors.toList());
    }

    private List<String[]> sortByAverage() {
        final int voteAvg = MetadataCols.VOTE_AVERAGE.ordinal();

        return data.stream()
                .sorted((o1, o2) -> {
                    if (Double.parseDouble(o1[voteAvg]) < Double.parseDouble(o2[voteAvg])) return 1;
                    else if (Double.parseDouble(o1[voteAvg]) > Double.parseDouble(o2[voteAvg]))
                        return -1;
                    else return 0;
                })
                .limit(20)
                .collect(Collectors.toList());
    }

    private void showDialog(View v) {
        final String[] items = criteriaName;
        int checkedItem = 0;

        if(sortCriteria == sortCriterias[0]) checkedItem = 0;
        else if(sortCriteria == sortCriterias[1]) checkedItem = 1;

        new AlertDialog.Builder(v.getContext())
                .setTitle("정렬 기준")
                .setSingleChoiceItems(items, checkedItem, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(which == 0) {
                            sortCriteria = sortCriterias[0];
                        } else if(which == 1) {
                            sortCriteria = sortCriterias[1];
                        }
                    }
                })
                .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        List<String[]> list = new ArrayList<>();
                        if(sortCriteria == sortCriterias[0]) {
                            list = sortByAverage();
                        }
                        else if(sortCriteria == sortCriterias[1]) {
                            list = sortByCount();
                        }

                        adapter.clearItems();
                        for(int i=0; i<20; i++) {
                            String[] strings = list.get(i);
                            String title = strings[MetadataCols.TITLE.ordinal()];
                            String id = strings[MetadataCols.ID.ordinal()];
                            String tmpRuntime = strings[MetadataCols.RUNTIME.ordinal()];
                            String runtime = tmpRuntime.substring(0, tmpRuntime.length() - 2) + " 분";
                            String overview = strings[MetadataCols.OVERVIEW.ordinal()];
                            String rank = String.valueOf(i + 1);

                            adapter.addItem(new SimpleMovieItem(rank, title, id, runtime, overview, R.drawable.poster_sample));
                        }
                        adapter.notifyDataSetChanged();

                        Button btn = v.findViewById(R.id.btnHomeFilter);
                        if(sortCriteria == sortCriterias[0])
                            btn.setText("정렬 기준 : " + criteriaName[0]);
                        else if(sortCriteria == sortCriterias[1])
                            btn.setText("정렬 기준 : " + criteriaName[1]);
                    }
                })
                .show();
    }

}
