package com.example.ottintroapplication.repository;

import android.content.res.AssetManager;

import com.example.ottintroapplication.common.cols.CreditCols;
import com.example.ottintroapplication.common.cols.LinkCols;
import com.example.ottintroapplication.common.cols.MetadataCols;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.stream.Collectors;

public class MovieRepository {

    AssetManager assetManager;
    private List<String[]> metadata;

    public MovieRepository(AssetManager assetManager) {
        this.assetManager = assetManager;
    }

    public void setMetadata() throws IOException, CsvException {
        InputStream inputStream = assetManager.open("movies_metadata.csv");
        CSVReader csvReader = new CSVReader(new InputStreamReader(inputStream));

        final int voteCount = MetadataCols.VOTE_COUNT.ordinal();

        metadata = csvReader.readAll()
                .stream()
                .filter(strings -> strings.length >= voteCount)
                .filter(strings -> isNumeric(strings[voteCount]))
                .sorted((o1, o2) -> {
                    if(Integer.parseInt(o1[voteCount]) < Integer.parseInt(o2[voteCount])) return 1;
                    else if(Integer.parseInt(o1[voteCount]) > Integer.parseInt(o2[voteCount])) return -1;
                    else return 0;
                })
                .limit(100)
                .collect(Collectors.toList());
    }

    public List<String[]> getMetadata() {
        return metadata;
    }

    public String[] findCredits(String movieId) throws IOException, CsvException {
        InputStream inputStream = assetManager.open("credits.csv");
        CSVReader csvReader = new CSVReader(new InputStreamReader(inputStream));

        String[] strArr;
        while((strArr = csvReader.readNext()) != null) {
            if(strArr[CreditCols.ID.ordinal()].equals(movieId))
                return strArr;
        }
        return null;
    }

    public String[] findLinks(String movieId) throws IOException, CsvException {
        InputStream inputStream = assetManager.open("links.csv");
        CSVReader csvReader = new CSVReader(new InputStreamReader(inputStream));

        String[] strArr;
        while ((strArr = csvReader.readNext()) != null) {
            if (strArr[LinkCols.MOVIE_ID.ordinal()].equals(movieId))
                return strArr;
        }
        return null;
    }

    public boolean isNumeric(String str) {
        try {
            Integer.parseInt(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}
