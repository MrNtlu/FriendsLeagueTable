package com.mrntlu.friendsleaguetable.utils;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.view.Window;
import android.view.WindowManager;

import com.mrntlu.friendsleaguetable.R;

public class StatusBarGradient {

    public static void setStatusBarGradiant(Activity activity) {
        Window window = activity.getWindow();
        Drawable background = activity.getDrawable(R.drawable.gradient_background);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(activity.getColor(android.R.color.transparent));
        window.setNavigationBarColor(activity.getColor(android.R.color.transparent));
        window.setBackgroundDrawable(background);

    }
}
