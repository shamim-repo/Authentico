package com.msbshamim60.authentico.ui.generate;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class GenerateHashPagerAdapter extends FragmentStateAdapter {
    static Fragment fragments[]={new HashFormImageFragment(), new HashFromDocumentFragment()};
    public GenerateHashPagerAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
        super(fragmentManager, lifecycle);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        return fragments[position];
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}
