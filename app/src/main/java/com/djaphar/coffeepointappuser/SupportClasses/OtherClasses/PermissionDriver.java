package com.djaphar.coffeepointappuser.SupportClasses.OtherClasses;

import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;

import androidx.appcompat.app.AppCompatActivity;

public class PermissionDriver {

    private static final int PERMISSION_REQUEST_CODE = 123;

    public static boolean hasPerms(String[] perms, Context context) {
        int res;

        for (String perm : perms) {
            res = context.checkCallingOrSelfPermission(perm);
            if (res != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }

        return true;
    }

    public static void requestPerms(AppCompatActivity appCompatActivity, String[] perms) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            appCompatActivity.requestPermissions(perms, PERMISSION_REQUEST_CODE);
        }
    }
}
