package com.prototype.prototype.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;


public class Utility {
    public static void displayAlertDialogMessage(Activity activity, String message, DialogInterface.OnClickListener listener){
        new AlertDialog.Builder(activity)
                .setMessage(message)
                .setPositiveButton("OK", listener)
                .setNegativeButton("Cancel", null)
                .show();
    }
}
