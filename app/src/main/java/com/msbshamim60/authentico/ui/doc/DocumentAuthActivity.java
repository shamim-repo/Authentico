package com.msbshamim60.authentico.ui.doc;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Bundle;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.msbshamim60.authentico.R;
import com.msbshamim60.authentico.ui.image.ImageAuthPageAdapter;

public class DocumentAuthActivity extends AppCompatActivity {
    static String titlesDoc[]={"Document","Hash"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_document_auth);

        ViewPager2 viewPager=findViewById(R.id.pager_doc);
        DocAuthPageAdapter pageAdapter=new DocAuthPageAdapter(getSupportFragmentManager(),getLifecycle());
        viewPager.setAdapter(pageAdapter);

        TabLayout tabLayout = findViewById(R.id.tab_layout_doc);
        new TabLayoutMediator(tabLayout, viewPager,
                (tab, position) -> tab.setText(titlesDoc[position])
        ).attach();
    }
}