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
import com.gp.smarthome.hamdy.smarthomegp.Adapters.GroupDevicesRecycViewAdapter;
import com.gp.smarthome.hamdy.smarthomegp.Admin;
import com.gp.smarthome.hamdy.smarthomegp.Fragments.AssignGroupDeviceDialog;
import com.gp.smarthome.hamdy.smarthomegp.Models.DeviceGroupModel;
import com.gp.smarthome.hamdy.smarthomegp.Models.DeviceModel;
import com.gp.smarthome.hamdy.smarthomegp.Models.GroupModel;
import com.gp.smarthome.hamdy.smarthomegp.R;
import com.gp.smarthome.hamdy.smarthomegp.SmartHomeApp;
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

public class AssignGroupDeviceDialogController implements IValidationHandler{

    private  final String TAG = "AssignDGDialogCTRL";
    public final AssignGroupDeviceDialog mAssignGroupDeviceDialog;
    private final Admin admin;
    private final MainActivity mMainActivity;
    private final GroupDevicesRecycViewAdapter groupDevicesAdapter;
    private final GroupModel model;
    private ArrayList<DeviceModel> devices = new ArrayList<>();
    private Context context;
    private HashMap<String, String[]> devicesHashMap;
    private ArrayAdapter<String> groupsSpinnerAdapter;
    private String selectedDeviceId;

    public AssignGroupDeviceDialogController(AssignGroupDeviceDialog assignGroupDeviceDialog) {
        context = assignGroupDeviceDialog.getActivity();
        this.mAssignGroupDeviceDialog = assignGroupDeviceDialog;


        context = mAssignGroupDeviceDialog.getActivity();

        model = mAssignGroupDeviceDialog.model;

        mMainActivity = (MainActivity)mAssignGroupDeviceDialog.getActivity();

        admin = (Admin)((SmartHomeApp)((Activity)context).getApplication()).user;




        mAssignGroupDeviceDialog.devicesSP.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedDeviceId = devicesHashMap.get("ids")[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                selectedDeviceId = "";
            }
        });

        groupDevicesAdapter = new GroupDevicesRecycViewAdapter(this , devices, mAssignGroupDeviceDialog.view);
        
        mAssignGroupDeviceDialog.devicesRV.setAdapter(groupDevicesAdapter);


        getDevicesFromAPI();
        getGroupDevicesFromAPI();


    }

    private void getDevicesFromAPI() {

        mAssignGroupDeviceDialog.progress(true);


        //devices for spinner
        model.getDevicesNotForGroup(context, new IResponseHandler() {
            @Override
            public void onResponseSuccess(String response) {
                onGetDevicesNotForGroupResponseSuccess(response);
            }

            @Override
            public void onResponseFailed(VolleyError error) {

                ongetDevicesNotForGroupResponseFailed(error);
            }
        });
    }


    private HashMap<String, String[]> getGroupsMap(ArrayList<GroupModel> groups) {

        devicesHashMap = new HashMap<>();


        String[] ids = new String[groups.size() + 1];
        String[] names = new String[groups.size() + 1];

        for (int i = 0 ; i < groups.size() ; i++){
            ids[i+ 1] = groups.get(i).id ;
            names[i+1] =  groups.get(i).name;
        }


        devicesHashMap.put("ids" , ids);
        devicesHashMap.put("names" , names);

        return devicesHashMap;
    }

    private void onGetDevicesNotForGroupResponseSuccess(String response) {
        Log.e(TAG , "onGetDevicesNotForGroupResponseSuccess : " + response);

        try {
            ResponseParserUtils parser = new ResponseParserUtils(response);

            if(parser.result){

                GroupModel groupModel = new GroupModel();
                ArrayList<GroupModel> groups = groupModel.parseGroupsJA(parser.data.getJSONArray("list"));

                devicesHashMap =  getGroupsMap(groups);

                devicesHashMap.get("names")[0] = "Choose device";
                devicesHashMap.get("ids")[0] = "";

                groupsSpinnerAdapter = new ArrayAdapter<String>(context ,android.R.layout.simple_spinner_item , devicesHashMap.get("names"));
                mAssignGroupDeviceDialog.devicesSP.setAdapter(groupsSpinnerAdapter);

            }else
                mMainActivity.displayToastError();

        } catch (JSONException e) {
            e.printStackTrace();
            Log.e(TAG , e.toString());
            mMainActivity.displayToastError();
        }

        mAssignGroupDeviceDialog.progress(false);

    }

    private void ongetDevicesNotForGroupResponseFailed(VolleyError error) {
        Log.e(TAG , error.toString());
        Toast.makeText(mMainActivity, context.getString(R.string.error_occured), Toast.LENGTH_SHORT).show();
        mAssignGroupDeviceDialog.progress(false);


    }




    public void actionListener() {

        mAssignGroupDeviceDialog.assignBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                assignBtn();
            }
        });

    }

    private void assignBtn() {

        mAssignGroupDeviceDialog.progress(true);

        Validation validation = new Validation();

        //desc
        validation.addValidationField(
                selectedDeviceId,
                new MyTextView(mAssignGroupDeviceDialog.devicesErrValidationTV),
                new IValidationRule[]{
                        new Required()
                } );

        validation.validate(context , this);


    }

    @Override
    public void onValidationSuccessfull() {

        DeviceGroupModel model = new DeviceGroupModel();

        model.groupId = this.model.id;
        model.deviceId = selectedDeviceId;

        model.assignDeviceToGroup(context , new IResponseHandler(){

            @Override
            public void onResponseSuccess(String response) {

                onAssignGroupDeviceResponseSuccess(response);
            }

            @Override
            public void onResponseFailed(VolleyError error) {
                onGroupDeviceResponseFailed(error);

            }
        });


    }

    private void onAssignGroupDeviceResponseSuccess(String response) {
        Log.e(TAG , "onAssignGroupDeviceResponseSuccess : " + response);

        try {
            ResponseParserUtils parser  = new ResponseParserUtils(response);

            if(parser.result){
                refresh();
            }
            else
                Toast.makeText(mMainActivity, context.getString(R.string.assign_device_failed), Toast.LENGTH_SHORT).show();


        } catch (JSONException e) {
            e.printStackTrace();
            Log.e(TAG , "onAssignGroupDeviceResponseSuccess : " + e.toString());
            Toast.makeText(mMainActivity, context.getString(R.string.error_occured), Toast.LENGTH_SHORT).show();
        }


        mAssignGroupDeviceDialog.progress(false);


    }

    private void onGroupDeviceResponseFailed(VolleyError error) {
        Log.e(TAG , "onGroupDeviceResponseFailed : " + error.toString());
        Toast.makeText(mMainActivity, context.getString(R.string.error_occured), Toast.LENGTH_SHORT).show();
        mAssignGroupDeviceDialog.progress(false);

    }



    @Override
    public void onValidationFailed(ArrayList<Error> errors) {

        mAssignGroupDeviceDialog.displayValidationErrors(errors);

        mAssignGroupDeviceDialog.progress(false);
    }


    private void getGroupDevicesFromAPI() {

        mAssignGroupDeviceDialog.progress(true);

        Log.e(TAG , "getGroupDevicesFromAPI");

        model.getGroupDevices(context , new IResponseHandler(){

            @Override
            public void onResponseSuccess(String response) {

                onGetGroupDevicesRespondeSuccess(response);
            }

            @Override
            public void onResponseFailed(VolleyError error) {
                Log.e(TAG , error.toString());
                AlertHelper.showLongSnackBar(mAssignGroupDeviceDialog.view , mAssignGroupDeviceDialog.getString(R.string.error_occured));

            }
        });
    }

    private void onGetGroupDevicesRespondeSuccess(String response) {

        Log.e(TAG , "onGetGroupDevicesRespondeSuccess : " + response);

        try{
            ResponseParserUtils parser = new ResponseParserUtils(response);

            if(parser.result){

                DeviceModel model = new DeviceModel();

                groupDevicesAdapter.data = model.parseDevicesJA(parser.data.getJSONArray("list"));

                if(groupDevicesAdapter.data.size() > 0){
                    mAssignGroupDeviceDialog.showMessage(false);
                }else {
                    mAssignGroupDeviceDialog.showMessage(true);
//                    AlertHelper.showLongSnackBar(mAssignGroupUserDialog.view , "No Users Found");

                }


                groupDevicesAdapter.notifyDataSetChanged();
            }
        } catch (JSONException e) {
            e.printStackTrace();
            Log.e(TAG , e.toString());
            Toast.makeText(mMainActivity, context.getString(R.string.error_occured), Toast.LENGTH_SHORT).show();
        }


    }

    public void deleteGroupDevice(final DeviceModel item) {
        AlertDialog.Builder builder1 = new AlertDialog.Builder(context);
        builder1.setMessage("Are you sure to delete item?");
        builder1.setCancelable(true);

        builder1.setPositiveButton(
                "Yes",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        mAssignGroupDeviceDialog.progress(true);


                        DeviceGroupModel model = new DeviceGroupModel();


                        model.deviceId = item.id;
                        model.groupId = AssignGroupDeviceDialogController.this.model.id;

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
        mAssignGroupDeviceDialog.progress(false);
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

        mAssignGroupDeviceDialog.progress(false);

    }

    private void refresh() {

        getGroupDevicesFromAPI();
        getDevicesFromAPI();
    }
}
