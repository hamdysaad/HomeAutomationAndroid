package com.gp.smarthome.hamdy.smarthomegp.Controllers;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.gp.smarthome.hamdy.smarthomegp.Activities.MainActivity;
import com.gp.smarthome.hamdy.smarthomegp.Adapters.DeviceGroupsRecycViewAdapter;
import com.gp.smarthome.hamdy.smarthomegp.Admin;
import com.gp.smarthome.hamdy.smarthomegp.Fragments.AssignDeviceToGroupDialog;
import com.gp.smarthome.hamdy.smarthomegp.Models.DeviceGroupModel;
import com.gp.smarthome.hamdy.smarthomegp.Models.DeviceModel;
import com.gp.smarthome.hamdy.smarthomegp.Models.DeviceUserModel;
import com.gp.smarthome.hamdy.smarthomegp.Models.GroupModel;
import com.gp.smarthome.hamdy.smarthomegp.R;
import com.gp.smarthome.hamdy.smarthomegp.SmartHomeApp;
import com.gp.smarthome.hamdy.smarthomegp.User;
import com.gp.smarthome.hamdy.smarthomegp.Utilities.AlertHelper;
import com.gp.smarthome.hamdy.smarthomegp.Utilities.ResponseParserUtils;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;

import Vendor.Errors.Error;
import Vendor.Errors.MyTextView;
import Vendor.Validation.IValidationHandler;
import Vendor.Validation.IValidationRule;
import Vendor.Validation.Required;
import Vendor.Validation.Validation;
import Vendor.WebService.IResponseHandler;

/**
 * Created by Hamdy on 4/18/2017.
 */

public class AssignDeviceToGroupDialogController implements IValidationHandler{

    private  final String TAG = "AssignDGDialogCTRL";
    public final AssignDeviceToGroupDialog mAssignDeviceToGroupDialog;
    private final Admin admin;
    private final MainActivity mMainActivity;
    private final DeviceGroupsRecycViewAdapter deviceGroupsAdapter;
    private final DeviceModel deviceModel;
    private ArrayList<GroupModel> groups = new ArrayList<>();
    private Context context;
    private HashMap<String, String[]> groupsHashMap;
    private ArrayAdapter<String> groupsSpinnerAdapter;
    private String selectedGroupId;

    public AssignDeviceToGroupDialogController(AssignDeviceToGroupDialog assignDeviceToUserDialog) {
        context = assignDeviceToUserDialog.getActivity();
        this.mAssignDeviceToGroupDialog = assignDeviceToUserDialog;


        context = assignDeviceToUserDialog.getActivity();

        deviceModel = mAssignDeviceToGroupDialog.deviceModel;

        mMainActivity = (MainActivity)assignDeviceToUserDialog.getActivity();

        admin = (Admin)((SmartHomeApp)((Activity)context).getApplication()).user;




        mAssignDeviceToGroupDialog.groupsSP.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedGroupId = groupsHashMap.get("ids")[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                selectedGroupId = "";
            }
        });

        deviceGroupsAdapter = new DeviceGroupsRecycViewAdapter(this , groups, mAssignDeviceToGroupDialog.view);
        
        mAssignDeviceToGroupDialog.groupsRV.setAdapter(deviceGroupsAdapter);


        getGroupsFromAPI();
        getDeviceGroupsFromAPI();


    }

    private void getGroupsFromAPI() {

        mAssignDeviceToGroupDialog.progress(true);


        //groups for spinner
        deviceModel.getGroupsNotForDevice(context, new IResponseHandler() {
            @Override
            public void onResponseSuccess(String response) {
                onGroupsResponseSuccess(response);
            }

            @Override
            public void onResponseFailed(VolleyError error) {

                onGroupsResponseFailed(error);
            }
        });
    }


    private HashMap<String, String[]> getGroupsMap(ArrayList<GroupModel> groups) {

        groupsHashMap = new HashMap<>();


        String[] ids = new String[groups.size() + 1];
        String[] names = new String[groups.size() + 1];

        for (int i = 0 ; i < groups.size() ; i++){
            ids[i+ 1] = groups.get(i).id ;
            names[i+1] =  groups.get(i).name;
        }


        groupsHashMap.put("ids" , ids);
        groupsHashMap.put("names" , names);

        return groupsHashMap;
    }

    private void onGroupsResponseSuccess(String response) {
        Log.e(TAG , "onGroupsResponseSuccess : " + response);

        try {
            ResponseParserUtils parser = new ResponseParserUtils(response);

            if(parser.result){

                GroupModel groupModel = new GroupModel();
                ArrayList<GroupModel> groups = groupModel.parseGroupsJA(parser.data.getJSONArray("list"));

                groupsHashMap =  getGroupsMap(groups);

                groupsHashMap.get("names")[0] = mMainActivity.getString(R.string.select_user_to_assign_device);
                groupsHashMap.get("ids")[0] = "";

                groupsSpinnerAdapter = new ArrayAdapter<String>(context ,android.R.layout.simple_spinner_item , groupsHashMap.get("names"));
                mAssignDeviceToGroupDialog.groupsSP.setAdapter(groupsSpinnerAdapter);

            }else
                mMainActivity.displayToastError();

        } catch (JSONException e) {
            e.printStackTrace();
            Log.e(TAG , e.toString());
            mMainActivity.displayToastError();
        }

        mAssignDeviceToGroupDialog.progress(false);

    }

    private void onGroupsResponseFailed(VolleyError error) {
        Log.e(TAG , error.toString());
        Toast.makeText(mMainActivity, context.getString(R.string.error_occured), Toast.LENGTH_SHORT).show();
        mAssignDeviceToGroupDialog.progress(false);


    }




    public void actionListener() {

        mAssignDeviceToGroupDialog.assignBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                assignBtn();
            }
        });

    }

    private void assignBtn() {

        mAssignDeviceToGroupDialog.progress(true);

        Validation validation = new Validation();

        //desc
        validation.addValidationField(
                selectedGroupId,
                new MyTextView(mAssignDeviceToGroupDialog.groupErrValidationTV),
                new IValidationRule[]{
                        new Required()
                } );

        validation.validate(context , this);


    }

    @Override
    public void onValidationSuccessfull() {

        DeviceGroupModel model = new DeviceGroupModel();

        model.groupId = selectedGroupId;
        model.deviceId = deviceModel.id;

        model.assignDeviceToGroup(context , new IResponseHandler(){

            @Override
            public void onResponseSuccess(String response) {

                onAssignDeviceUserResponseSuccess(response);
            }

            @Override
            public void onResponseFailed(VolleyError error) {
                onDeviceUserResponseFailed(error);

            }
        });


    }

    private void onAssignDeviceUserResponseSuccess(String response) {
        Log.e(TAG , "onAssignDeviceUserResponseSuccess : " + response);

        try {
            ResponseParserUtils parser  = new ResponseParserUtils(response);

            if(parser.result){
                refresh();
            }
            else
                Toast.makeText(mMainActivity, context.getString(R.string.assign_device_failed), Toast.LENGTH_SHORT).show();


        } catch (JSONException e) {
            e.printStackTrace();
            Log.e(TAG , "onAssignDeviceUserResponseSuccess : " + e.toString());
            Toast.makeText(mMainActivity, context.getString(R.string.error_occured), Toast.LENGTH_SHORT).show();
        }


        mAssignDeviceToGroupDialog.progress(false);


    }

    private void onDeviceUserResponseFailed(VolleyError error) {
        Log.e(TAG , "onDeviceUserResponseFailed : " + error.toString());
        Toast.makeText(mMainActivity, context.getString(R.string.error_occured), Toast.LENGTH_SHORT).show();
        mAssignDeviceToGroupDialog.progress(false);

    }



    @Override
    public void onValidationFailed(ArrayList<Error> errors) {

        mAssignDeviceToGroupDialog.displayValidationErrors(errors);

        mAssignDeviceToGroupDialog.progress(false);
    }


    private void getDeviceGroupsFromAPI() {

        mAssignDeviceToGroupDialog.progress(true);

        Log.e(TAG , "getDeviceGroupsFromAPI");

        deviceModel.getDeviceGroups(context , new IResponseHandler(){

            @Override
            public void onResponseSuccess(String response) {

                onDeviceGroupsRespondeSuccess(response);
            }

            @Override
            public void onResponseFailed(VolleyError error) {
                Log.e(TAG , error.toString());
                AlertHelper.showLongSnackBar(mAssignDeviceToGroupDialog.view , mAssignDeviceToGroupDialog.getString(R.string.error_occured));

            }
        });
    }

    private void onDeviceGroupsRespondeSuccess(String response) {

        Log.e(TAG , "onDeviceGroupsRespondeSuccess : " + response);

        try{
            ResponseParserUtils parser = new ResponseParserUtils(response);

            if(parser.result){

                GroupModel model = new GroupModel();

                deviceGroupsAdapter.data = model.parseGroupsJA(parser.data.getJSONArray("list"));

                if(deviceGroupsAdapter.data.size() > 0){
                    mAssignDeviceToGroupDialog.showMessage(false);
                }else {
                    mAssignDeviceToGroupDialog.showMessage(true);
//                    AlertHelper.showLongSnackBar(mAssignGroupUserDialog.view , "No Users Found");

                }


                deviceGroupsAdapter.notifyDataSetChanged();
            }
        } catch (JSONException e) {
            e.printStackTrace();
            Log.e(TAG , e.toString());
            Toast.makeText(mMainActivity, context.getString(R.string.error_occured), Toast.LENGTH_SHORT).show();
        }


    }

    public void deleteDeviceGroup(final GroupModel item) {
        AlertDialog.Builder builder1 = new AlertDialog.Builder(context);
        builder1.setMessage("Are you sure to delete item?");
        builder1.setCancelable(true);

        builder1.setPositiveButton(
                "Yes",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        mAssignDeviceToGroupDialog.progress(true);


                        DeviceGroupModel model = new DeviceGroupModel();


                        model.groupId = item.id;
                        model.deviceId = deviceModel.id;

                        model.delete(context, new IResponseHandler() {
                            @Override
                            public void onResponseSuccess(String response) {
                                onDeleteDeviceUserResponseSuccess(response);
                            }

                            @Override
                            public void onResponseFailed(VolleyError error) {
                                onDeleteDeviceUserResponseFailed(error);

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

    private void onDeleteDeviceUserResponseFailed(VolleyError error) {
        Log.e(TAG , "onDeleteResponseFailed : " + error.toString() );
        Toast.makeText(context, context.getString(R.string.delete_failed), Toast.LENGTH_SHORT).show();
        mAssignDeviceToGroupDialog.progress(false);
    }

    private void onDeleteDeviceUserResponseSuccess(String response) {
        Log.e(TAG , "onDeleteDeviceUserResponseSuccess : " + response );

        try {
            ResponseParserUtils parser = new ResponseParserUtils(response);

            if(parser.result){
                Toast.makeText(context, context.getString(R.string.delete_successfull), Toast.LENGTH_SHORT).show();
                refresh();
            }else
                Toast.makeText(context, context.getString(R.string.delete_failed), Toast.LENGTH_SHORT).show();

        } catch (JSONException e) {
            e.printStackTrace();
            Log.e(TAG , e.toString());
        }

        mAssignDeviceToGroupDialog.progress(false);

    }

    private void refresh() {

        getDeviceGroupsFromAPI();
        getGroupsFromAPI();
    }
}
