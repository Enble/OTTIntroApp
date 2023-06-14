package com.example.ottintroapplication;

import android.app.AlertDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ottintroapplication.dto.ReviewItem;
import com.example.ottintroapplication.repository.ReviewRepository;
import com.example.ottintroapplication.common.cols.CreditCols;
import com.example.ottintroapplication.common.cols.MetadataCols;
import com.example.ottintroapplication.common.SharedViewModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class DetailFragment extends Fragment {

    private SharedViewModel viewModel;
    private String url;
    private RecyclerView recyclerView;
    private ReviewRepository reviewRepository;
    private DetailRecyclerViewAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.detail_fragment, container, false);

        recyclerView = v.findViewById(R.id.rvDetail);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        viewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);
        Bundle result = viewModel.getData().getValue();

        // search에서 넘기지 않았으면 main activity에서 넘긴 bundle 사용
        if(result == null) {
            result = getArguments();
        }

        // 넘어온 metadata를 ui에 적용
        String[] metadata = result.getStringArray("metadata");

        TextView title = v.findViewById(R.id.tvDetailTitle);
        TextView score = v.findViewById(R.id.tvDetailScore);
        TextView year = v.findViewById(R.id.tvDetailYear);
        TextView runtime = v.findViewById(R.id.tvDetailRuntime);
        TextView overview = v.findViewById(R.id.tvDetailOverview);

        title.setText(metadata[MetadataCols.TITLE.ordinal()]);

        score.setText(metadata[MetadataCols.VOTE_AVERAGE.ordinal()]);

        String yearStr = metadata[MetadataCols.RELEASE_DATE.ordinal()];
        year.setText(yearStr.substring(0, 4));

        String runtimeStr = metadata[MetadataCols.RUNTIME.ordinal()];
        runtime.setText(runtimeStr.substring(0, runtimeStr.length() - 2) + " 분");

        overview.setText(metadata[MetadataCols.OVERVIEW.ordinal()]);

        TextView ageRating = v.findViewById(R.id.tvDetailRating);
        String strAge = metadata[MetadataCols.ADULT.ordinal()];
        if (strAge.equals("true")) {
            ageRating.setText("19세 관람가");
        } else {
            ageRating.setText("전체 관람가");
        }

        // 넘어온 credit을 ui에 적용
        String[] credit = result.getStringArray("credit");
        String crewStr = credit[CreditCols.CREW.ordinal()];
        String castStr = credit[CreditCols.CAST.ordinal()];

        TextView director = v.findViewById(R.id.tvDetailDirector);
        TextView cast = v.findViewById(R.id.tvDetailCast);

        // json parser 사용
        try {
            JSONArray jsonArrayCrew = new JSONArray(crewStr);
            String directorName = "";
            for(int i=0; i<jsonArrayCrew.length(); i++) {
                JSONObject jsonObject = jsonArrayCrew.getJSONObject(i);
                if(jsonObject.getString("job").equals("Director")) {
                    directorName = jsonObject.getString("name");
                }
            }

            director.setText(directorName);

            JSONArray jsonArrayCast = new JSONArray(castStr);
            String castNames = "";
            for(int i=0; i<5; i++) {
                JSONObject jsonObject = jsonArrayCast.getJSONObject(i);
                String name = jsonObject.getString("name");
                castNames += name;
                if(i < 4) {
                    castNames += ", ";
                }
            }

            cast.setText(castNames);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }


        // 웹 크롤링으로 받아오는 코드
        String imdbId = metadata[MetadataCols.IMDB_ID.ordinal()];
        String directorAndCast = "https://www.imdb.com/title/" + imdbId;
        url = "https://www.imdb.com/title/" + imdbId + "/reviews?sort=curated";

        final Bundle reviewBundle = new Bundle();
        new Thread() {
            @Override
            public void run() {
                try {
                    Document doc = Jsoup.connect(url).get();

                    Elements star = doc.select(".lister-item-content .rating-other-user-rating"); // 10/10 or 5/10
                    ArrayList<String> stars = new ArrayList<>();
                    for (Element element : star) {
                        stars.add(element.text());
                    }
                    reviewBundle.putStringArrayList("star", stars);

                    Elements title = doc.select(".lister-item-content .title");
                    ArrayList<String> titles = new ArrayList<>();
                    for (Element element : title) {
                        titles.add(element.text());
                    }
                    reviewBundle.putStringArrayList("title", titles);

                    Elements username = doc.select(".lister-item-content .display-name-link");
                    ArrayList<String> usernames = new ArrayList<>();
                    for (Element element : username) {
                        usernames.add(element.text());
                    }
                    reviewBundle.putStringArrayList("username", usernames);

                    Elements date = doc.select(".lister-item-content .review-date");
                    ArrayList<String> dates = new ArrayList<>();
                    for (Element element : date) {
                        dates.add(element.text());
                    }
                    reviewBundle.putStringArrayList("date", dates);

                    Elements content = doc.select(".lister-item-content .show-more__control");
                    ArrayList<String> contents = new ArrayList<>();
                    for (Element element : content) {
                        contents.add(element.text());
                    }
                    reviewBundle.putStringArrayList("content", contents);

                    Message msg = handler.obtainMessage();
                    msg.setData(reviewBundle);
                    handler.sendMessage(msg);

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }.start();

        // 리뷰 작성
        Button btnReview = v.findViewById(R.id.btnDetailReview);
        btnReview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog(view);
            }
        });

        return v;
    }

    private void showDialog(View v) {
        AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
        builder.setTitle("리뷰 작성");

        View dlgView = getLayoutInflater().inflate(R.layout.review_dialog, v.findViewById(R.id.dlgReview));
        AlertDialog alertDialog = builder.setView(dlgView).create();

        EditText username = dlgView.findViewById(R.id.etReviewDlgUsername);
        EditText title = dlgView.findViewById(R.id.etReviewDlgTitle);
        EditText content = dlgView.findViewById(R.id.etReviewDlgContent);
        RatingBar ratingBar = dlgView.findViewById(R.id.rbDlg);

        dlgView.findViewById(R.id.btnReviewDlgPositive).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String usernameStr = username.getText().toString();
                String titleStr = title.getText().toString();
                String contentStr = content.getText().toString();
                String starStr = String.valueOf((int) (ratingBar.getRating() * 2));
                starStr += "/10";

                if(usernameStr.equals("") || usernameStr == null) {
                    Toast.makeText(v.getContext(), "유저 이름을 작성해주세요", Toast.LENGTH_SHORT);
                } else if(titleStr.equals("") || titleStr == null) {
                    Toast.makeText(v.getContext(), "리뷰 제목을 작성해주세요", Toast.LENGTH_SHORT);
                } else if(contentStr.equals("") || contentStr == null) {
                    Toast.makeText(v.getContext(), "리뷰 본문을 작성해주세요", Toast.LENGTH_SHORT);
                } else {
                    long now = System.currentTimeMillis();
                    Date date = new Date(now);
                    SimpleDateFormat sdf = new SimpleDateFormat("dd MMMM yyyy");
                    String dateStr = sdf.format(date);

                    ReviewItem reviewItem = new ReviewItem(starStr, titleStr, usernameStr, dateStr, contentStr);
                    reviewRepository.putReview(reviewItem);

                    adapter.notifyDataSetChanged();

                    alertDialog.dismiss();
                }
            }
        });

        dlgView.findViewById(R.id.btnReviewDlgNegative).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
            }
        });

        alertDialog.show();
    }

    Handler handler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(@NonNull Message msg) {
            Bundle bundle = msg.getData();

            ArrayList<String> star = bundle.getStringArrayList("star");
            ArrayList<String> title = bundle.getStringArrayList("title");
            ArrayList<String> username = bundle.getStringArrayList("username");
            ArrayList<String> date = bundle.getStringArrayList("date");
            ArrayList<String> content = bundle.getStringArrayList("content");

            reviewRepository = new ReviewRepository();
            reviewRepository.initItems(star, title, username, date, content);

            // recycler view List 설정
            adapter = new DetailRecyclerViewAdapter(reviewRepository.getItems());
            recyclerView.setAdapter(adapter);
        }
    };

}
