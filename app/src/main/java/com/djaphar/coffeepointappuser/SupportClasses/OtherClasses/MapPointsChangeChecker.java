package com.djaphar.coffeepointappuser.SupportClasses.OtherClasses;

import android.os.Handler;

import com.djaphar.coffeepointappuser.Activities.MainActivity;

public class MapPointsChangeChecker {

    private Handler handler;
    private MainActivity mainActivity;

    public MapPointsChangeChecker(Handler handler, MainActivity mainActivity) {
        this.handler = handler;
        this.mainActivity = mainActivity;
    }

    public void startMapPointsChangeCheck() {
        asyncMapPointsChangeChecker.run();
    }

    public void stopMapPointsChangeCheck() {
        handler.removeCallbacksAndMessages(null);
    }

    private Runnable asyncMapPointsChangeChecker = () -> {
        mainActivity.requestPointsInBox();
        handler.postDelayed(this::startMapPointsChangeCheck, 60000);
    };
}
