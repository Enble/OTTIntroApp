package com.example.ottintroapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;

import com.example.ottintroapplication.common.cols.MetadataCols;
import com.example.ottintroapplication.repository.MovieRepository;
import com.google.android.material.tabs.TabLayout;
import com.opencsv.exceptions.CsvException;

import java.io.IOException;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private MovieRepository movieRepository;
    private List<String[]> data;

    private HomeFragment home;
    private SearchFragment search;
    private DetailFragment detail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        movieRepository = new MovieRepository(this.getAssets());
        try {
            movieRepository.setMetadata();
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (CsvException e) {
            throw new RuntimeException(e);
        }
        data = movieRepository.getMetadata();

        home = new HomeFragment();
        search = new SearchFragment();
        detail = new DetailFragment();

        // Home과 Search에 Bundle로 metadata 넘기기
        Bundle bundle = new Bundle();

        // detail로 넘어갈 metadata 정하기
        int metadataIdx = 0;
        double avg = 0.0;
        for(int i=0; i<data.size(); i++) {
            double tmp = Double.parseDouble(data.get(i)[MetadataCols.VOTE_AVERAGE.ordinal()]);
            if(tmp > avg) {
                metadataIdx = i;
                avg = tmp;
            }
            bundle.putStringArray("item" + i, data.get(i));
        }
        home.setArguments(bundle);
        search.setArguments(bundle);

        String[] metadataStrArr = data.get(metadataIdx);

        // detail로 넘어갈 credit 정하기
        String movieId = metadataStrArr[MetadataCols.ID.ordinal()];
        String[] creditStrArr;
        try {
            creditStrArr = movieRepository.findCredits(movieId);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (CsvException e) {
            throw new RuntimeException(e);
        }

        // 기본 프래그먼트 : Home
        getSupportFragmentManager().beginTransaction().add(R.id.frame, home).commit();

        TabLayout tabs = findViewById(R.id.tabs);
        tabs.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int pos = tab.getPosition();

                Fragment selected = null;
                if(pos == 0) {
                    selected = home;
                } else if(pos == 1) {
                    selected = search;
                } else {
                    Bundle defaultBundle = new Bundle();
                    defaultBundle.putStringArray("metadata", metadataStrArr);
                    defaultBundle.putStringArray("credit", creditStrArr);
                    detail.setArguments(defaultBundle);

                    selected = detail;
                }

                getSupportFragmentManager().beginTransaction().replace(R.id.frame, selected).commit();
            }
            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }
            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }
}