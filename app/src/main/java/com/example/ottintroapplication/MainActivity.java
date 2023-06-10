package com.example.ottintroapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;

import com.example.ottintroapplication.common.MetadataCols;
import com.example.ottintroapplication.common.MovieRepository;
import com.google.android.material.tabs.TabLayout;
import com.opencsv.exceptions.CsvException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private MovieRepository movieRepository;
    private List<String[]> data;

    private Fragment home;
    private Fragment search;
    private Fragment detail;

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

        // HomeFragment에 Bundle로 repository에 있는 값 넘기기
        Bundle bundle = new Bundle();

        int tmpIdx = 0;
        double avg = 0.0;
        for(int i=0; i<100; i++) {
            if(Double.parseDouble(data.get(i)[MetadataCols.VOTE_AVERAGE.ordinal()]) > avg) {
                tmpIdx = i;
            }
            bundle.putStringArray("item" + i, data.get(i));
        }
        home.setArguments(bundle);
        search.setArguments(bundle);

        String[] tmpStringArr = data.get(tmpIdx);

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
                    defaultBundle.putStringArray("item", tmpStringArr);
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