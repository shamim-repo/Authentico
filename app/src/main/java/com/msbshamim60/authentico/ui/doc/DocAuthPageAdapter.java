package com.msbshamim60.authentico.ui.doc;


import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.msbshamim60.authentico.ui.doc.document.DocumentAuthenticationFragment;
import com.msbshamim60.authentico.ui.doc.hash.DocHashAuthenticationFragment;
import com.msbshamim60.authentico.ui.image.hash.ImageHashAuthenticationFragment;

public class DocAuthPageAdapter extends FragmentStateAdapter {
    static Fragment fragments[]={new DocumentAuthenticationFragment(), new DocHashAuthenticationFragment()};

    public DocAuthPageAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
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
