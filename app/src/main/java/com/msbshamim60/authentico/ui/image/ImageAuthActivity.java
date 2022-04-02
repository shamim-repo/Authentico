package com.msbshamim60.authentico.ui.image;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Bundle;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.msbshamim60.authentico.R;

public class ImageAuthActivity extends AppCompatActivity {
    static String titles[]={"Image","Hash"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_auth);

        ViewPager2 viewPager=findViewById(R.id.pager_Image);
        ImageAuthPageAdapter pageAdapter=new ImageAuthPageAdapter(getSupportFragmentManager(),getLifecycle());
        viewPager.setAdapter(pageAdapter);

        TabLayout tabLayout = findViewById(R.id.tab_layout_Image);
        new TabLayoutMediator(tabLayout, viewPager,
                (tab, position) -> tab.setText(titles[position])
        ).attach();
    }
}