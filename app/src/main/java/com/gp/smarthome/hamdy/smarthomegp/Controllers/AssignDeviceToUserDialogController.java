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
import com.gp.smarthome.hamdy.smarthomegp.Adapters.DeviceUsersRecycViewAdapter;
import com.gp.smarthome.hamdy.smarthomegp.Admin;
import com.gp.smarthome.hamdy.smarthomegp.Fragments.AssignDeviceToUserDialog;
import com.gp.smarthome.hamdy.smarthomegp.Models.DeviceModel;
import com.gp.smarthome.hamdy.smarthomegp.Models.DeviceUserModel;
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

public class AssignDeviceToUserDialogController implements IValidationHandler{

    private  final String TAG = "AssignDUDialogCTRL";
    public final AssignDeviceToUserDialog mAssignDeviceToUserDialog;
    private final Admin admin;
    private final MainActivity mMainActivity;
    private final DeviceUsersRecycViewAdapter deviceUsersAdapter;
    private final DeviceModel deviceModel;
    private ArrayList<User> users = new ArrayList<>();
    private Context context;
    private HashMap<String, String[]> usersHashMap;
    private ArrayAdapter<String> usersSpinnerAdapter;
    private String selectedUserId;

    public AssignDeviceToUserDialogController(AssignDeviceToUserDialog assignDeviceToUserDialog) {
        context = assignDeviceToUserDialog.getActivity();
        this.mAssignDeviceToUserDialog = assignDeviceToUserDialog;


        context = assignDeviceToUserDialog.getActivity();

        deviceModel = mAssignDeviceToUserDialog.deviceModel;

        mMainActivity = (MainActivity)assignDeviceToUserDialog.getActivity();

        admin = (Admin)((SmartHomeApp)((Activity)context).getApplication()).user;




        mAssignDeviceToUserDialog.usersSP.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedUserId = usersHashMap.get("ids")[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                selectedUserId = "";
            }
        });

        deviceUsersAdapter = new DeviceUsersRecycViewAdapter(this , users , mAssignDeviceToUserDialog.view);
        mAssignDeviceToUserDialog.usersRV.setAdapter(deviceUsersAdapter);


        getUsersFromAPI();
        getDeviceUsersFromAPI();


    }

    private void getUsersFromAPI() {

        mAssignDeviceToUserDialog.progress(true);


        //users for spinner
        deviceModel.getUsersNotForDevice(context, new IResponseHandler() {
            @Override
            public void onResponseSuccess(String response) {
                onUsersResponseSuccess(response);
            }

            @Override
            public void onResponseFailed(VolleyError error) {

                onUsersResponseFailed(error);
            }
        });
    }


    private HashMap<String, String[]> getUsersMap(ArrayList<User> users) {

        usersHashMap = new HashMap<>();


        String[] ids = new String[users.size() + 1];
        String[] names = new String[users.size() + 1];

        for (int i = 0 ; i < users.size() ; i++){
            ids[i+ 1] = users.get(i).id ;
            names[i+1] =  users.get(i).fName + " " + users.get(i).lName;
        }


        usersHashMap.put("ids" , ids);
        usersHashMap.put("names" , names);

        return usersHashMap;
    }

    private void onUsersResponseSuccess(String response) {
        Log.e(TAG , "onUsersResponseSuccess : " + response);

        try {
            ResponseParserUtils parser = new ResponseParserUtils(response);

            if(parser.result){

                ArrayList<User> users = admin.parseUsersJA(parser.data.getJSONArray("list"));

                usersHashMap =  getUsersMap(users);

                usersHashMap.get("names")[0] = mMainActivity.getString(R.string.select_user_to_assign_device);
                usersHashMap.get("ids")[0] = "";

                usersSpinnerAdapter = new ArrayAdapter<String>(context ,android.R.layout.simple_spinner_item , usersHashMap.get("names"));
                mAssignDeviceToUserDialog.usersSP.setAdapter(usersSpinnerAdapter);

            }else
                mMainActivity.displayToastError();

        } catch (JSONException e) {
            e.printStackTrace();
            Log.e(TAG , e.toString());
            mMainActivity.displayToastError();
        }

        mAssignDeviceToUserDialog.progress(false);

    }

    private void onUsersResponseFailed(VolleyError error) {
        Log.e(TAG , error.toString());
        Toast.makeText(mMainActivity, context.getString(R.string.error_occured), Toast.LENGTH_SHORT).show();
        mAssignDeviceToUserDialog.progress(false);


    }




    public void actionListener() {

        mAssignDeviceToUserDialog.assignBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                assignBtn();
            }
        });

    }

    private void assignBtn() {

        mAssignDeviceToUserDialog.progress(true);

        Validation validation = new Validation();

        //desc
        validation.addValidationField(
                selectedUserId ,
                new MyTextView(mAssignDeviceToUserDialog.userErrValidationTV),
                new IValidationRule[]{
                        new Required()
                } );

        validation.validate(context , this);


    }

    @Override
    public void onValidationSuccessfull() {

        DeviceUserModel model = new DeviceUserModel();

        model.userId = selectedUserId;
        model.deviceId = deviceModel.id;

        model.assignDeviceToUser(context , new IResponseHandler(){

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


        mAssignDeviceToUserDialog.progress(false);


    }

    private void onDeviceUserResponseFailed(VolleyError error) {
        Log.e(TAG , "onDeviceUserResponseFailed : " + error.toString());
        Toast.makeText(mMainActivity, context.getString(R.string.error_occured), Toast.LENGTH_SHORT).show();
        mAssignDeviceToUserDialog.progress(false);

    }



    @Override
    public void onValidationFailed(ArrayList<Error> errors) {

        mAssignDeviceToUserDialog.displayValidationErrors(errors);

        mAssignDeviceToUserDialog.progress(false);
    }


    private void getDeviceUsersFromAPI() {

        mAssignDeviceToUserDialog.progress(true);

        Log.e(TAG , "getDeviceUsersFromAPI");

        deviceModel.getDeviceUsers(context , new IResponseHandler(){

            @Override
            public void onResponseSuccess(String response) {

                onDeviceUsersRespondeSuccess(response);
            }

            @Override
            public void onResponseFailed(VolleyError error) {
                Log.e(TAG , error.toString());
                AlertHelper.showLongSnackBar(mAssignDeviceToUserDialog.view , mAssignDeviceToUserDialog.getString(R.string.error_occured));

            }
        });
    }

    private void onDeviceUsersRespondeSuccess(String response) {

        Log.e(TAG , "onDeviceUsersRespondeSuccess : " + response);

        try{
            ResponseParserUtils parser = new ResponseParserUtils(response);

            if(parser.result){

                User model = new User();

                deviceUsersAdapter.data = model.parseUsersJA(parser.data.getJSONArray("list"));

                if(deviceUsersAdapter.data.size() > 0){
                    mAssignDeviceToUserDialog.showMessage(false);
                }else {
                    mAssignDeviceToUserDialog.showMessage(true);
//                    AlertHelper.showLongSnackBar(mAssignGroupUserDialog.view , "No Users Found");

                }


                deviceUsersAdapter.notifyDataSetChanged();
            }
        } catch (JSONException e) {
            e.printStackTrace();
            Log.e(TAG , e.toString());
            Toast.makeText(mMainActivity, context.getString(R.string.error_occured), Toast.LENGTH_SHORT).show();
        }


    }

    public void deleteDeviceUser(final User item) {
        AlertDialog.Builder builder1 = new AlertDialog.Builder(context);
        builder1.setMessage("Are you sure to delete item?");
        builder1.setCancelable(true);

        builder1.setPositiveButton(
                "Yes",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        mAssignDeviceToUserDialog.progress(true);


                        DeviceUserModel model = new DeviceUserModel();


                        model.userId = item.id;
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
        mAssignDeviceToUserDialog.progress(false);
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

        mAssignDeviceToUserDialog.progress(false);

    }

    private void refresh() {

        getDeviceUsersFromAPI();
        getUsersFromAPI();
    }
}
