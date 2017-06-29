package com.gp.smarthome.hamdy.smarthomegp.Utilities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.Toast;

/**
 * Created by MOHAMED on 02/04/2017.
 */

public class AlertHelper {

    Context context;
    private ProgressDialog progressDialog;


    public AlertHelper(Context context) {
        this.context = context;
    }


    public void showLongToast(String message) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show();
    }

    public void showShortToast(String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    public static void  showLongSnackBar(View view, String message) {

        Snackbar.make(view, message, Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();
    }

    public static void showShortSnackBar(View view, String message) {

        Snackbar.make(view, message, Snackbar.LENGTH_SHORT)
                .setAction("Action", null).show();
    }

    public void showProgressDialog(String title, String message, boolean cancelable) {


        progressDialog = new ProgressDialog(context);
        progressDialog.setTitle(title);
        progressDialog.setMessage(message);
        progressDialog.setCancelable(cancelable);
        progressDialog.show();

    }

    public void dismissProgressDialog() {
        progressDialog.dismiss();
    }


    public void showShowAlertDialog(String title, String message, DialogInterface.OnClickListener positiveButton, String positiveText,
                                    DialogInterface.OnClickListener negativeButton, String negativeText) {


        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(message);
        builder.setCancelable(true);
        builder.setTitle(title);

        builder.setPositiveButton(
                positiveText,
                positiveButton);

        builder.setNegativeButton(
                negativeText, negativeButton);

        AlertDialog alert = builder.create();
        alert.show();

    }


}
