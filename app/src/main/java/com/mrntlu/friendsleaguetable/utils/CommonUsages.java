package com.mrntlu.friendsleaguetable.utils;


import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.mrntlu.friendsleaguetable.R;

public class CommonUsages {

    public static Toolbar setToolbar(Toolbar toolbar, String title,FragmentManager fragmentManager){
        toolbar.setTitle(title);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
        toolbar.setNavigationOnClickListener(view1 -> startTransaction(fragmentManager));
        return toolbar;
    }

    private static void startTransaction(FragmentManager fragmentManager){
        if (fragmentManager!=null) fragmentManager.popBackStack();
        else throw new NullPointerException("There was an error!");
    }

    public static void startTransaction(FragmentManager fragmentManager, Fragment fragment){
        if (fragment==null) startTransaction(fragmentManager);
        else {
            FragmentTransaction fragmentTransaction=fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.frameLayout,fragment).addToBackStack(null);
            fragmentTransaction.commit();
        }
    }
}
