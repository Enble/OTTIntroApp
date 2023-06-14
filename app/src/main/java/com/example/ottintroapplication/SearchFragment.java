package com.example.ottintroapplication;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.ottintroapplication.common.cols.CreditCols;
import com.example.ottintroapplication.common.cols.MetadataCols;
import com.example.ottintroapplication.repository.MovieRepository;
import com.example.ottintroapplication.common.SharedViewModel;
import com.example.ottintroapplication.dto.SimpleMovieItem;
import com.google.android.material.tabs.TabLayout;
import com.opencsv.exceptions.CsvException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SearchFragment extends Fragment {

    private List<String[]> data = new ArrayList<>();
    private SearchListViewAdapter adapter;
    private MovieRepository movieRepository;
    private int tmpIdx = 0;
    private String[] credits;
    private SharedViewModel viewModel;

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
                if (etSearchTitle.getText().toString().equals("") ||
                        etSearchTitle.getText().toString() == null) {
                    Toast.makeText(v.getContext(), "영화명을 입력해주세요", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (etSearchActor.getText().toString().equals("") ||
                        etSearchActor.getText().toString() == null) {
                    Toast.makeText(v.getContext(), "배우명을 입력해주세요", Toast.LENGTH_SHORT).show();
                    return;
                }

                // 검색 구현
                adapter.clearItems();
                boolean searched = false;
                for(int i=0; i<data.size(); i++) {
                    String title = data.get(i)[MetadataCols.TITLE.ordinal()];

                    if (title.equals(etSearchTitle.getText().toString())) {
                        String movieId = data.get(i)[MetadataCols.ID.ordinal()];
                        // 배우명 비교
                        String actors;
                        try {
                            credits = movieRepository.findCredits(movieId);
                            actors = credits[CreditCols.CAST.ordinal()];
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        } catch (CsvException e) {
                            throw new RuntimeException(e);
                        }

                        if (actors.contains(etSearchActor.getText().toString())) {
                            String overview = data.get(i)[MetadataCols.OVERVIEW.ordinal()];
                            SimpleMovieItem item = new SimpleMovieItem(null, title, movieId, null, overview, R.drawable.poster_sample);

                            adapter.addItem(item);

                            tmpIdx = i;
                            searched = true;
                        }
                    }
                }

                if(searched == false) {
                    Toast.makeText(v.getContext(), "찾는 영화가 없습니다.", Toast.LENGTH_SHORT).show();
                }

                adapter.notifyDataSetChanged();
            }
        });

        viewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                DetailFragment detail = new DetailFragment();
                String[] metadata = data.get(tmpIdx);

                Bundle bundle = new Bundle();
                bundle.putStringArray("metadata", metadata);
                bundle.putStringArray("credit", credits);

                viewModel.setData(bundle);

                TabLayout tabLayout = container.getRootView().findViewById(R.id.tabs);
                TabLayout.Tab tab = tabLayout.getTabAt(2);
                tab.select();

                getParentFragmentManager().beginTransaction().replace(R.id.frame, detail).commit();
            }
        });

        listView.setAdapter(adapter);

        return v;
    }

}
