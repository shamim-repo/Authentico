package com.msbshamim60.authentico.ui.image;


import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.msbshamim60.authentico.ui.image.hash.ImageHashAuthenticationFragment;
import com.msbshamim60.authentico.ui.image.image.ImageAuthenticationFragment;

public class ImageAuthPageAdapter extends FragmentStateAdapter {
    static Fragment fragments[]={new ImageAuthenticationFragment(), new ImageHashAuthenticationFragment()};

    public ImageAuthPageAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
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
