package com.example.ottintroapplication;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.ottintroapplication.common.MetadataCols;
import com.example.ottintroapplication.common.MovieRepository;
import com.example.ottintroapplication.common.SimpleMovieItem;
import com.opencsv.exceptions.CsvException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SearchFragment extends Fragment {

    private List<String[]> data = new ArrayList<>();
    private SearchListViewAdapter adapter;
    private MovieRepository movieRepository;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.search_fragment, container, false);

        ListView listView = v.findViewById(R.id.lvSearch);
        adapter = new SearchListViewAdapter(v.getContext());

        Bundle bundle = getArguments();
        for(int i=0; i<100; i++) {
            String[] strings = bundle.getStringArray("item" + i);
            data.add(strings);
        }

        // Activity에서 Asset 받아옴
        movieRepository = new MovieRepository(getActivity().getAssets());
        Button btnSearch = v.findViewById(R.id.btnSearch);

        EditText etSearchTitle = v.findViewById(R.id.etSearchTitle);
        EditText etSearchActor = v.findViewById(R.id.etSearchActor);
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 검색 구현
                adapter.clearItems();
                for(int i=0; i<data.size(); i++) {
                    String title = data.get(i)[MetadataCols.TITLE.ordinal()];

                    // TODO : 검색 안됐을 경우 토스트로 찾는 영화가 없다고 띄우기
                    if(title.equals(etSearchTitle.getText().toString())) {
                        String movieId = data.get(i)[MetadataCols.ID.ordinal()];
                        // 배우명 비교
                        String actors;
                        try {
                            actors = movieRepository.findActors(movieId);
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        } catch (CsvException e) {
                            throw new RuntimeException(e);
                        }

                        if(actors == null) {
                            // TODO : 배우를 못 찾았을 때 처리
                        }

                        if(actors.contains(etSearchActor.getText().toString())) {
                            String overview = data.get(i)[MetadataCols.OVERVIEW.ordinal()];
                            SimpleMovieItem item = new SimpleMovieItem(null, title, movieId, null, overview, R.drawable.poster_sample);

                            adapter.addItem(item);
                        }
                        else {
                            // TODO : 토스트로 찾는 영화가 없다고 띄우기
                        }
                    }
                }
                adapter.notifyDataSetChanged();
            }
        });

        listView.setAdapter(adapter);

        return v;
    }

}
