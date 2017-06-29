package com.gp.smarthome.hamdy.smarthomegp.Controllers;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.PopupMenu;
import android.util.Log;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.gp.smarthome.hamdy.smarthomegp.Adapters.GroupsRecycViewAdapter;
import com.gp.smarthome.hamdy.smarthomegp.Admin;
import com.gp.smarthome.hamdy.smarthomegp.Fragments.AddDeviceDialog;
import com.gp.smarthome.hamdy.smarthomegp.Fragments.AddGroupDialog;
import com.gp.smarthome.hamdy.smarthomegp.Fragments.AssignDeviceToGroupDialog;
import com.gp.smarthome.hamdy.smarthomegp.Fragments.AssignGroupDeviceDialog;
import com.gp.smarthome.hamdy.smarthomegp.Fragments.AssignGroupUserDialog;
import com.gp.smarthome.hamdy.smarthomegp.Fragments.GroupsFragment;
import com.gp.smarthome.hamdy.smarthomegp.Fragments.HomeFragment;
import com.gp.smarthome.hamdy.smarthomegp.Models.DeviceModel;
import com.gp.smarthome.hamdy.smarthomegp.Models.GroupModel;
import com.gp.smarthome.hamdy.smarthomegp.R;
import com.gp.smarthome.hamdy.smarthomegp.SmartHomeApp;
import com.gp.smarthome.hamdy.smarthomegp.User;
import com.gp.smarthome.hamdy.smarthomegp.Utilities.ResponseParserUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import Vendor.WebService.IResponseHandler;

/**
 * Created by hamdy on 09/06/17.
 */

public class GroupFragmentController implements IResponseHandler {


    private  final String TAG = "groupFragmentCTRL";
    public final GroupsFragment mGroupsFragment;
    private final User user;
    private final Context context;
    private  ArrayList<GroupModel> data = new ArrayList<>();
    public GroupsRecycViewAdapter adapter;

    public GroupFragmentController(GroupsFragment groupsFragment) {



        mGroupsFragment = groupsFragment;
        context = mGroupsFragment.getActivity();

        user = ( (SmartHomeApp)mGroupsFragment.getActivity().getApplication()).user;

        adapter = new GroupsRecycViewAdapter(this, data , mGroupsFragment.view);


        refreshAdapter();

    }



    @Override
    public void onResponseSuccess(String response) {

        Log.e(TAG , response);

        try {

            ResponseParserUtils parser = new ResponseParserUtils(response);
            if(parser.result){

                GroupModel model = new GroupModel();
                ArrayList<GroupModel> data = model.parserResponse(parser.data);

                mGroupsFragment.mRecyclerView.setAdapter(adapter);

                adapter.data = data;

                adapter.notifyDataSetChanged();

            }


        } catch (JSONException e) {
            e.printStackTrace();
            Log.e(TAG , e.toString());
        }




        mGroupsFragment.showAnim(false);


    }

    @Override
    public void onResponseFailed(VolleyError error) {

        Log.e(TAG , error.toString());

        mGroupsFragment.displayMessage("An Error occured");

        mGroupsFragment.showAnim(false);


    }

    public void onItemClick(View v, GroupModel item) {

        Log.e(TAG , "onItemClick : " + item.id);

        HomeFragment fragment = HomeFragment.newInstance(item.devices);

        FragmentTransaction bt = mGroupsFragment.getActivity().getSupportFragmentManager().beginTransaction();

        bt.replace(R.id.content_main , fragment );

       bt.addToBackStack("HomeFragment");

        bt.commit();



    }




    public void onPopupClicked(View view, final GroupModel model) {

        Log.e(TAG , "onPopupIV : " + model.id);

        PopupMenu popup = new PopupMenu(context,view );
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.admin_group_menu, popup.getMenu());
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                onDeviceGridItemPopClick(item , model);
                return true;
            }
        });
        popup.show();
    }

    private void onDeviceGridItemPopClick(MenuItem item, GroupModel model) {

        switch (item.getItemId()){
            case R.id.edit:
                editGroupPUMI(model);
                break;

            case R.id.delete:
                deleteGroupPUMI(model);
                break;

            case R.id.assign_to_user:
                assignUserItemMenuw(model);
                break;

            case R.id.assign_device:
                assignDeviceItemMenu(model);
                break;
        }
    }

    private void editGroupPUMI(GroupModel model) {
        Log.e(TAG , "onItemEditClick : " + model.id);

        AddGroupDialog.showDialog(mGroupsFragment.getActivity(), model);


    }

    private void deleteGroupPUMI(final GroupModel model) {
        Log.e(TAG , "onItemDeleteClick : " + model.id);

        AlertDialog.Builder builder1 = new AlertDialog.Builder(context);
        builder1.setMessage("Are you sure to delete item?");
        builder1.setCancelable(true);

        builder1.setPositiveButton(
                "Yes",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Admin admin = (Admin) user;

                        admin.setiAddBehavoir(model);

                        admin.delete(context, new IResponseHandler() {
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
        user.getGroups(context, this);
    }

    private void onDeleteResponseFailed(VolleyError error) {
        Log.e(TAG , "onDeleteResponseFailed : " + error.toString() );

        Toast.makeText(context, context.getString(R.string.delete_failed), Toast.LENGTH_SHORT).show();
    }

    private void assignUserItemMenuw(GroupModel model) {
        Log.e(TAG , "assignUserItemMenuw : " + model.id);

        AssignGroupUserDialog.showDialog(mGroupsFragment.getActivity(), model);


    }

    private void assignDeviceItemMenu(GroupModel model) {
        Log.e(TAG , "assignDeviceItemMenu : " + model.id);

        AssignGroupDeviceDialog.showDialog(mGroupsFragment.getActivity(), model);


    }

}
