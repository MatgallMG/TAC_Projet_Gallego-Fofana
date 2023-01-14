package com.example.tac_projet_gallego_fofana;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class MainActivity extends AppCompatActivity {
    private ViewPager2 viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //Toast.makeText(this, "on create main", Toast.LENGTH_SHORT).show();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setupViewPagerAndTabs();
    }

    @Override
    protected void onResume() {
        super.onResume();
        //Toast.makeText(this, "on resume main", Toast.LENGTH_SHORT).show();
    }

    private void setupViewPagerAndTabs() {
        viewPager = findViewById(R.id.tab_viewpager);
        TabLayout tabLayout = findViewById(R.id.menu_layout);

        final MovieFragment fragmentMovie = MovieFragment.newInstance();
        final FavorisFragment fragmentFavM = FavorisFragment.newInstance();

        viewPager.setAdapter(new FragmentStateAdapter(this) {

            @Override
            public int getItemCount() {
                return 2;
            }

            @NonNull
            @Override
            public Fragment createFragment(int position) {
                if (position == 0) {
                    return fragmentMovie;
                }
                return fragmentFavM;
            }
        });

        TabLayoutMediator tabLayoutMediator = new TabLayoutMediator(tabLayout, viewPager, (tab, position) -> tab.setText(position == 0 ? MovieFragment.TAB_NAME : FavorisFragment.TAB_NAME));
        tabLayoutMediator.attach();

        Button buttonChangeViewVertical = findViewById(R.id.changeViewButton1);
        Button buttonChangeViewGrid = findViewById(R.id.changeViewButton2);

        buttonChangeViewVertical.setOnClickListener(view -> {
            if (viewPager.getCurrentItem() == 0)
                fragmentMovie.displayLinear();
            else
                fragmentFavM.displayLinear();
        });

        buttonChangeViewGrid.setOnClickListener(view -> {
            if (viewPager.getCurrentItem() == 0)
                fragmentMovie.displayGrid();
            else
                fragmentFavM.displayGrid();
        });
    }

}