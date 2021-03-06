package com.felkertech.n.boilerplate.Utils;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.afollestad.materialdialogs.MaterialDialog;

/**
 * Created by N on 7/1/2015.
 * Meant for Android Marshmallow and beyond
 */
public class PermissionUtils {
    private static String TAG = PermissionUtils.class.getSimpleName();
    private static final boolean DEBUG = false;

    private final static int MY_PERMISSIONS_RETURN = 1;


    public static boolean isEnabled(Activity activity, String permission) {
        return activity.checkCallingOrSelfPermission(permission)
                == PackageManager.PERMISSION_GRANTED;
    }

    public static boolean isDisabled(Activity mActivity, String permission) {
        return !isEnabled(mActivity, permission);
    }

    public static void requestPermissionIfDisabled(AppCompatActivity activity, String permission) {
        requestPermissionIfDisabled(activity, permission, "");
    }

    public static void requestPermissionIfDisabled(Activity activity, String permission) {
        requestPermissionIfDisabled(activity, permission, "");
    }

    public static void requestPermissionIfDisabled(final AppCompatActivity activity,
            final String permission, String rationale) {
        if (DEBUG) {
            Log.d(TAG, Build.VERSION.CODENAME);
            Log.d(TAG, "Is " + permission + " enabled? " + isEnabled(activity, permission));
        }
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (isDisabled(activity, permission)) {
                if (DEBUG) {
                    Log.d(TAG, "Show rationale? " +
                            activity.shouldShowRequestPermissionRationale(permission) + " " +
                            rationale);
                }
                if (activity.shouldShowRequestPermissionRationale(permission)
                        && !rationale.isEmpty()) {
                    new MaterialDialog.Builder(activity)
                            .title("Request Permission")
                            .content(rationale)
                            .positiveText("OK")
                            .dismissListener(new DialogInterface.OnDismissListener() {
                                @Override
                                public void onDismiss(DialogInterface dialog) {
                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                        activity.requestPermissions(new String[]{permission},
                                                MY_PERMISSIONS_RETURN);
                                    }
                                }
                            })
                            .show();
                } else {
                    if (DEBUG) {
                        Log.d(TAG, "Make a request");
                    }
                    activity.requestPermissions(new String[]{permission},
                            MY_PERMISSIONS_RETURN);
                }
            }
        }
    }

    public static void requestPermissionIfDisabled(final Activity mActivity,
            final String permission, String rationale) {
        if (DEBUG) {
            Log.d(TAG, Build.VERSION.CODENAME);
            Log.d(TAG, "Is " + permission + " enabled? " + isEnabled(mActivity, permission));
        }
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if(isDisabled(mActivity, permission)) {
                if (DEBUG) {
                    Log.d(TAG, "Show rationale? " +
                            mActivity.shouldShowRequestPermissionRationale(permission) + " " +
                            rationale);
                }
                if(mActivity.shouldShowRequestPermissionRationale(permission)
                        && !rationale.isEmpty()) {
                    new MaterialDialog.Builder(mActivity)
                            .title("Request Permission")
                            .content(rationale)
                            .positiveText("OK")
                            .dismissListener(new DialogInterface.OnDismissListener() {
                                @Override
                                public void onDismiss(DialogInterface dialog) {
                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                        mActivity.requestPermissions(new String[]{permission},
                                                MY_PERMISSIONS_RETURN);
                                    }
                                }
                            })
                            .show();
                } else {
                    if (DEBUG) {
                        Log.d(TAG, "Make a request");
                    }
                    mActivity.requestPermissions(new String[]{permission},
                            MY_PERMISSIONS_RETURN);
                }
            }
        }
    }
}