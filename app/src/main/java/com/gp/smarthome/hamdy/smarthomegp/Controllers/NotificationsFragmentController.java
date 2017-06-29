package com.gp.smarthome.hamdy.smarthomegp.Controllers;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.PopupMenu;
import android.util.Log;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.gp.smarthome.hamdy.smarthomegp.Adapters.NotificationsRecycViewAdapter;
import com.gp.smarthome.hamdy.smarthomegp.Admin;
import com.gp.smarthome.hamdy.smarthomegp.Fragments.AddGroupDialog;
import com.gp.smarthome.hamdy.smarthomegp.Fragments.AssignGroupDeviceDialog;
import com.gp.smarthome.hamdy.smarthomegp.Fragments.AssignGroupUserDialog;
import com.gp.smarthome.hamdy.smarthomegp.Fragments.HomeFragment;
import com.gp.smarthome.hamdy.smarthomegp.Fragments.NotificationsFragment;
import com.gp.smarthome.hamdy.smarthomegp.Models.GroupModel;
import com.gp.smarthome.hamdy.smarthomegp.Models.NotificationModel;
import com.gp.smarthome.hamdy.smarthomegp.R;
import com.gp.smarthome.hamdy.smarthomegp.SmartHomeApp;
import com.gp.smarthome.hamdy.smarthomegp.User;
import com.gp.smarthome.hamdy.smarthomegp.Utilities.ResponseParserUtils;

import org.json.JSONException;

import java.util.ArrayList;

import Vendor.WebService.IResponseHandler;

/**
 * Created by hamdy on 09/06/17.
 */

public class NotificationsFragmentController implements IResponseHandler {


    private  final String TAG = "notifFragmentCTRL";
    public final NotificationsFragment mNotificationsFragment;
    private final User user;
    private final Context context;
    private final NotificationModel model;
    private  ArrayList<NotificationModel> data = new ArrayList<>();
    public NotificationsRecycViewAdapter adapter;

    public NotificationsFragmentController(NotificationsFragment notificationsFragment) {

        mNotificationsFragment = notificationsFragment;
        context = mNotificationsFragment.getActivity();

        user = ( (SmartHomeApp) mNotificationsFragment.getActivity().getApplication()).user;

        model = new NotificationModel();

        adapter = new NotificationsRecycViewAdapter(this, data , mNotificationsFragment.view);

        mNotificationsFragment.mRecyclerView.setAdapter(adapter);

        refreshAdapter();

    }



    @Override
    public void onResponseSuccess(String response) {

        Log.e(TAG , response);

        try {

            ResponseParserUtils parser = new ResponseParserUtils(response);
            if(parser.result){


                model.parseNotificationsJA(parser.data.getJSONArray("notifications") , data);

                adapter.notifyDataSetChanged();

            }


        } catch (JSONException e) {
            e.printStackTrace();
            Log.e(TAG , e.toString());
        }




        mNotificationsFragment.showAnim(false);


    }

    @Override
    public void onResponseFailed(VolleyError error) {

        Log.e(TAG , error.toString());

        mNotificationsFragment.displayMessage("An Error occured");

        mNotificationsFragment.showAnim(false);


    }



    public void onDeleteClicked(final NotificationModel model) {
        Log.e(TAG , "onItemDeleteClick : " + model.id);

        AlertDialog.Builder builder1 = new AlertDialog.Builder(context);
        builder1.setMessage("Are you sure to delete item?");
        builder1.setCancelable(true);

        builder1.setPositiveButton(
                "Yes",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        model.delete(context, new IResponseHandler() {
                            @Override
                            public void onResponseSuccess(String response) {
                                onDeleteResponseSuccess(response);
                            }

                            @Override
                            public void onResponseFailed(VolleyError error) {
                                onDeleteResponseFailed(error);

                            }
                        });
                    }
                });

        builder1.setNegativeButton(
                "No",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        AlertDialog alert11 = builder1.create();
        alert11.show();
    }

    private void onDeleteResponseSuccess(String response) {
        Log.e(TAG , "onDeleteResponseSuccess : " + response );

        try {
            ResponseParserUtils parser = new ResponseParserUtils(response);

            if(parser.result){
                Toast.makeText(context, context.getString(R.string.delete_successfull), Toast.LENGTH_SHORT).show();
                refreshAdapter();
            }else
                Toast.makeText(context, context.getString(R.string.delete_failed), Toast.LENGTH_SHORT).show();

        } catch (JSONException e) {
            e.printStackTrace();
            Log.e(TAG , e.toString());
        }
    }

    void refreshAdapter() {
        model.getAllNofications(context, this);
    }

    private void onDeleteResponseFailed(VolleyError error) {
        Log.e(TAG , "onDeleteResponseFailed : " + error.toString() );

        Toast.makeText(context, context.getString(R.string.delete_failed), Toast.LENGTH_SHORT).show();
    }


}
