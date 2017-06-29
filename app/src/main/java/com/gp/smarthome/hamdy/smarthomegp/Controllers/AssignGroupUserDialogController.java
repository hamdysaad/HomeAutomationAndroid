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
import com.gp.smarthome.hamdy.smarthomegp.Adapters.GroupUsersRecycViewAdapter;
import com.gp.smarthome.hamdy.smarthomegp.Admin;
import com.gp.smarthome.hamdy.smarthomegp.Fragments.AssignGroupUserDialog;
import com.gp.smarthome.hamdy.smarthomegp.Models.GroupModel;
import com.gp.smarthome.hamdy.smarthomegp.Models.GroupUserModel;
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

public class AssignGroupUserDialogController implements IValidationHandler{

    private  final String TAG = "AssignDUDialogCTRL";
    public final AssignGroupUserDialog mAssignGroupUserDialog;
    private final Admin admin;
    private final MainActivity mMainActivity;
    private final GroupUsersRecycViewAdapter groupUsersAdapter;
    private final GroupModel model;
    private ArrayList<User> users = new ArrayList<>();
    private Context context;
    private HashMap<String, String[]> usersHashMap;
    private ArrayAdapter<String> usersSpinnerAdapter;
    private String selectedUserId;

    public AssignGroupUserDialogController(AssignGroupUserDialog assignGroupUserDialog) {
        context = assignGroupUserDialog.getActivity();
        this.mAssignGroupUserDialog = assignGroupUserDialog;


        context = mAssignGroupUserDialog.getActivity();

        model = mAssignGroupUserDialog.model;

        mMainActivity = (MainActivity) mAssignGroupUserDialog.getActivity();

        admin = (Admin)((SmartHomeApp)((Activity)context).getApplication()).user;




        mAssignGroupUserDialog.usersSP.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedUserId = usersHashMap.get("ids")[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                selectedUserId = "";
            }
        });

        groupUsersAdapter = new GroupUsersRecycViewAdapter(this , users, mAssignGroupUserDialog.view);
        
        mAssignGroupUserDialog.usersRV.setAdapter(groupUsersAdapter);


        getUsersFromAPI();
        getGroupUsersFromAPI();


    }

    private void getUsersFromAPI() {

        mAssignGroupUserDialog.progress(true);


        //users for spinner
        model.getUsersNotForGroup(context, new IResponseHandler() {
            @Override
            public void onResponseSuccess(String response) {
                onGetUsersNotForGroupResponseSuccess(response);
            }

            @Override
            public void onResponseFailed(VolleyError error) {

                onGetUsersNotForGroupResponseFailed(error);
            }
        });
    }


    private HashMap<String, String[]> getUsersMap(ArrayList<User> users) {

        usersHashMap = new HashMap<>();


        String[] ids = new String[users.size() + 1];
        String[] names = new String[users.size() + 1];

        for (int i = 0 ; i < users.size() ; i++){
            ids[i+ 1] = users.get(i).id ;
            names[i+1] =  users.get(i).fName + " " +  users.get(i).lName;
        }


        usersHashMap.put("ids" , ids);
        usersHashMap.put("names" , names);

        return usersHashMap;
    }

    private void onGetUsersNotForGroupResponseSuccess(String response) {
        Log.e(TAG , "onGetUsersNotForGroupResponseSuccess : " + response);

        try {
            ResponseParserUtils parser = new ResponseParserUtils(response);

            if(parser.result){

                User userModel = new User();
                ArrayList<User> users = userModel.parseUsersJA(parser.data.getJSONArray("list"));

                usersHashMap =  getUsersMap(users);

                usersHashMap.get("names")[0] = "Choose User";
                usersHashMap.get("ids")[0] = "";

                usersSpinnerAdapter = new ArrayAdapter<String>(context ,android.R.layout.simple_spinner_item , usersHashMap.get("names"));
                mAssignGroupUserDialog.usersSP.setAdapter(usersSpinnerAdapter);

            }else
                mMainActivity.displayToastError();

        } catch (JSONException e) {
            e.printStackTrace();
            Log.e(TAG , e.toString());
            mMainActivity.displayToastError();
        }

        mAssignGroupUserDialog.progress(false);

    }

    private void onGetUsersNotForGroupResponseFailed(VolleyError error) {
        Log.e(TAG , error.toString());
        Toast.makeText(mMainActivity, context.getString(R.string.error_occured), Toast.LENGTH_SHORT).show();
        mAssignGroupUserDialog.progress(false);


    }




    public void actionListener() {

        mAssignGroupUserDialog.assignBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                assignBtn();
            }
        });

    }

    private void assignBtn() {

        mAssignGroupUserDialog.progress(true);

        Validation validation = new Validation();

        //desc
        validation.addValidationField(
                selectedUserId,
                new MyTextView(mAssignGroupUserDialog.devicesErrValidationTV),
                new IValidationRule[]{
                        new Required()
                } );

        validation.validate(context , this);


    }

    @Override
    public void onValidationSuccessfull() {

        GroupUserModel model = new GroupUserModel();

        model.groupId = this.model.id;
        model.userId = selectedUserId;

        model.assignGroupToUser(context , new IResponseHandler(){

            @Override
            public void onResponseSuccess(String response) {

                onAssignGroupUserResponseSuccess(response);
            }

            @Override
            public void onResponseFailed(VolleyError error) {
                onGroupUserResponseFailed(error);

            }
        });


    }

    private void onAssignGroupUserResponseSuccess(String response) {
        Log.e(TAG , "onAssignGroupUserResponseSuccess : " + response);

        try {
            ResponseParserUtils parser  = new ResponseParserUtils(response);

            if(parser.result){
                refresh();
            }
            else
                Toast.makeText(mMainActivity, context.getString(R.string.assign_device_failed), Toast.LENGTH_SHORT).show();


        } catch (JSONException e) {
            e.printStackTrace();
            Log.e(TAG , "onAssignGroupUserResponseSuccess : " + e.toString());
            Toast.makeText(mMainActivity, context.getString(R.string.error_occured), Toast.LENGTH_SHORT).show();
        }


        mAssignGroupUserDialog.progress(false);


    }

    private void onGroupUserResponseFailed(VolleyError error) {
        Log.e(TAG , "onGroupUserResponseFailed : " + error.toString());
        Toast.makeText(mMainActivity, context.getString(R.string.error_occured), Toast.LENGTH_SHORT).show();
        mAssignGroupUserDialog.progress(false);

    }



    @Override
    public void onValidationFailed(ArrayList<Error> errors) {

        mAssignGroupUserDialog.displayValidationErrors(errors);

        mAssignGroupUserDialog.progress(false);
    }


    private void getGroupUsersFromAPI() {

        mAssignGroupUserDialog.progress(true);

        Log.e(TAG , "getGroupUsersFromAPI");

        model.getGroupUsers(context , new IResponseHandler(){

            @Override
            public void onResponseSuccess(String response) {

                onGetGroupUsersRespondeSuccess(response);
            }

            @Override
            public void onResponseFailed(VolleyError error) {
                Log.e(TAG , error.toString());
                AlertHelper.showLongSnackBar(mAssignGroupUserDialog.view , mAssignGroupUserDialog.getString(R.string.error_occured));

            }
        });
    }

    private void onGetGroupUsersRespondeSuccess(String response) {

        Log.e(TAG , "onGetGroupUsersRespondeSuccess : " + response);

        try{
            ResponseParserUtils parser = new ResponseParserUtils(response);

            if(parser.result){

                User model = new User();

                groupUsersAdapter.data = model.parseUsersJA(parser.data.getJSONArray("list"));

                if(groupUsersAdapter.data.size() > 0){
                    mAssignGroupUserDialog.showMessage(false);
                }else {
                    mAssignGroupUserDialog.showMessage(true);
//                    AlertHelper.showLongSnackBar(mAssignGroupUserDialog.view , "No Users Found");

                }


                groupUsersAdapter.notifyDataSetChanged();
            }
        } catch (JSONException e) {
            e.printStackTrace();
            Log.e(TAG , e.toString());
            Toast.makeText(mMainActivity, context.getString(R.string.error_occured), Toast.LENGTH_SHORT).show();
        }


    }

    public void deleteGroupUser(final User item) {
        AlertDialog.Builder builder1 = new AlertDialog.Builder(context);
        builder1.setMessage("Are you sure to delete item?");
        builder1.setCancelable(true);

        builder1.setPositiveButton(
                "Yes",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        mAssignGroupUserDialog.progress(true);


                        GroupUserModel model = new GroupUserModel();


                        model.userId = item.id;
                        model.groupId = AssignGroupUserDialogController.this.model.id;

                        model.delete(context, new IResponseHandler() {
                            @Override
                            public void onResponseSuccess(String response) {
                                onDeleteGroupUserResponseSuccess(response);
                            }

                            @Override
                            public void onResponseFailed(VolleyError error) {
                                onDeleteGroupUserResponseFailed(error);

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

    private void onDeleteGroupUserResponseFailed(VolleyError error) {
        Log.e(TAG , "onDeleteResponseFailed : " + error.toString() );
        Toast.makeText(context, context.getString(R.string.delete_failed), Toast.LENGTH_SHORT).show();
        mAssignGroupUserDialog.progress(false);
    }

    private void onDeleteGroupUserResponseSuccess(String response) {
        Log.e(TAG , "onDeleteGroupUserResponseSuccess : " + response );

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

        mAssignGroupUserDialog.progress(false);

    }

    private void refresh() {

        getGroupUsersFromAPI();
        getUsersFromAPI();
    }
}
