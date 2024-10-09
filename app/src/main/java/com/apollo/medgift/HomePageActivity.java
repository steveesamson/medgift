package com.apollo.medgift;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.widget.ListView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.viewpager2.widget.ViewPager2;

import com.apollo.medgift.adapters.HealthTipsAdapter;
import com.apollo.medgift.adapters.HomeSlideImageAdapter;
import com.apollo.medgift.databinding.ActivityHomepageBinding;
import com.apollo.medgift.models.HomeSlideImageItem;

import java.util.ArrayList;
import java.util.List;

public class HomePageActivity extends AppCompatActivity {

    ActivityHomepageBinding binding;

    String titleList[] = {"Title - 1", "Title - 2"};
    String contentList[] = {"Cont - 1 title title title title title title", "Cont - 2 title title title title title title"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);

        binding = ActivityHomepageBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Toolbar homeAppBar = binding.homeAppBar.getRoot();

        setSupportActionBar(homeAppBar);

        // Set Appbar Title
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("MedGift");
        }

        // Image Slider
        ViewPager2 viewPager2 = binding.imageSlider;

        //https://www.youtube.com/watch?v=U7bqZkmVps8
        List<HomeSlideImageItem> homeSliderItem = new ArrayList<>();
        homeSliderItem.add(new HomeSlideImageItem(R.drawable.banner_1));
        homeSliderItem.add(new HomeSlideImageItem(R.drawable.banner_2));
        homeSliderItem.add(new HomeSlideImageItem(R.drawable.banner_3));

        viewPager2.setAdapter(new HomeSlideImageAdapter(homeSliderItem, viewPager2));

        // Health Tips List
        ListView listView = binding.healthTipsListView;
        HealthTipsAdapter healthTipsAdapter = new HealthTipsAdapter(getApplicationContext(), titleList, contentList);
        listView.setAdapter(healthTipsAdapter);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.homemenu, menu);
        return true;
    }
}