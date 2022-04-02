package com.msbshamim60.authentico.ui.generate;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Bundle;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.msbshamim60.authentico.R;
import com.msbshamim60.authentico.ui.doc.DocAuthPageAdapter;

public class GenerateSignActivity extends AppCompatActivity {
    private static String titlesGen[]={"Image","Document"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_genarate_sign);

        ViewPager2 viewPager=findViewById(R.id.pager_gen);
        GenerateHashPagerAdapter pageAdapter=new GenerateHashPagerAdapter(getSupportFragmentManager(),getLifecycle());
        viewPager.setAdapter(pageAdapter);

        TabLayout tabLayout = findViewById(R.id.tab_layout_gen);
        new TabLayoutMediator(tabLayout, viewPager,
                (tab, position) -> tab.setText(titlesGen[position])
        ).attach();
    }
}