package com.dongmihui.fragment.base;


import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;


import com.dongmihui.R;

/**
 * Created by administrator on 2016-09-22.
 */
public class LoadFragment {
    FragmentManager fragmentManager;
    public LoadFragment(FragmentManager fragmentManager)
    {
        this.fragmentManager = fragmentManager;
    }

    protected void initializeFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.realtabcontent, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }
}
