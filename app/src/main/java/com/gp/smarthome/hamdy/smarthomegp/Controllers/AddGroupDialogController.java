package com.gp.smarthome.hamdy.smarthomegp.Controllers;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.gp.smarthome.hamdy.smarthomegp.Activities.MainActivity;
import com.gp.smarthome.hamdy.smarthomegp.Admin;
import com.gp.smarthome.hamdy.smarthomegp.Fragments.AddGroupDialog;
import com.gp.smarthome.hamdy.smarthomegp.Models.DeviceModel;
import com.gp.smarthome.hamdy.smarthomegp.Models.GroupModel;
import com.gp.smarthome.hamdy.smarthomegp.R;
import com.gp.smarthome.hamdy.smarthomegp.SmartHomeApp;
import com.gp.smarthome.hamdy.smarthomegp.Utilities.AlertHelper;
import com.gp.smarthome.hamdy.smarthomegp.Utilities.ResponseParserUtils;

import org.json.JSONException;

import java.util.ArrayList;

import Vendor.Errors.Error;
import Vendor.Errors.TextInput;
import Vendor.Validation.IValidationHandler;
import Vendor.Validation.IValidationRule;
import Vendor.Validation.Required;
import Vendor.Validation.Validation;
import Vendor.WebService.IResponseHandler;

/**
 * Created by Hamdy on 4/18/2017.
 */

public class AddGroupDialogController implements IValidationHandler, IResponseHandler {

    private  final String TAG = "AddGroupDialogCTRL";
    public final AddGroupDialog addGroupDialog;
    private final Admin admin;
    private final MainActivity mMainActivity;
    private final AddGroupDialog mAddGroupDialog;
    private GroupModel model;
    private Context context;
    private String name;
    private String desc;

    public AddGroupDialogController(AddGroupDialog addGroupDialog) {
        context = addGroupDialog.getActivity();
        this.addGroupDialog = addGroupDialog;


        mAddGroupDialog = addGroupDialog;
        context = addGroupDialog.getActivity();

        mMainActivity = (MainActivity)addGroupDialog.getActivity();


        admin = (Admin)((SmartHomeApp)((Activity)context).getApplication()).user;

        model = mAddGroupDialog.model;

        if(model != null)
            mAddGroupDialog.setData();

    }


    public void actionListener() {

        mAddGroupDialog.addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addBtn();
            }
        });

    }

    private void addBtn() {

        mAddGroupDialog.progress(true);

        name = mAddGroupDialog.nameET.getText().toString();
        desc = mAddGroupDialog.descET.getText().toString();

        Validation validation = new Validation();

        //name
        validation.addValidationField(
                name ,
                new TextInput( mAddGroupDialog.nameTIL) ,
                new IValidationRule[]{
                        new Required(),
                });

        //desc
        validation.addValidationField(
                desc ,
                new TextInput(mAddGroupDialog.descTIL ),
                new IValidationRule[]{
                        new Required()
                } );


        validation.validate(context , this);


    }

    @Override
    public void onValidationSuccessfull() {

        GroupModel model = new GroupModel();

        model.name = name;
        model.description = desc;

        admin.setiAddBehavoir(model);

        if(mAddGroupDialog.edit) {
            model.id = mAddGroupDialog.model.id;
            admin.edit(context, new IResponseHandler() {
                @Override
                public void onResponseSuccess(String response) {
                    onEditResponseSuccess(response);
                }

                @Override
                public void onResponseFailed(VolleyError error) {
                    onEditResponseFailed(error);

                }
            });
        }
        else
            admin.add(context , this);




    }

    private void onEditResponseFailed(VolleyError error) {

        Log.e(TAG , "onEditResponseFailed : "  + error.toString());

        Toast.makeText(mMainActivity, context.getString(R.string.edit_failed), Toast.LENGTH_SHORT).show();

        mAddGroupDialog.dismiss();
    }

    private void onEditResponseSuccess(String response) {

        Log.e(TAG , "onEditResponseSuccess : "  + response);

        try {
            ResponseParserUtils parser = new ResponseParserUtils(response);

            if(parser.result){

                Toast.makeText(mMainActivity, context.getString(R.string.edit_successfully), Toast.LENGTH_SHORT).show();

                mMainActivity.mGroupsFragment.controller.refreshAdapter();
            }
            else
                Toast.makeText(mMainActivity, context.getString(R.string.edit_failed), Toast.LENGTH_SHORT).show();

        } catch (JSONException e) {
            e.printStackTrace();
            Log.e(TAG , e.toString());
        }

        mAddGroupDialog.dismiss();
    }

    @Override
    public void onValidationFailed(ArrayList<Error> errors) {

        mAddGroupDialog.displayValidationErrors(errors);

        mAddGroupDialog.progress(false);

    }

    @Override
    public void onResponseSuccess(String response) {
        Log.e(TAG , response);

        try {
            ResponseParserUtils parser = new ResponseParserUtils(response);

            if(parser.result){
                //refresh devices adapter
                Log.e(TAG , "refresh adapter");

                Toast.makeText(mMainActivity, mAddGroupDialog.getString(R.string.group_added_successfully), Toast.LENGTH_SHORT).show();

                mMainActivity.mGroupsFragment.controller.refreshAdapter();
                mAddGroupDialog.dismiss();
            }
            else
                AlertHelper.showLongSnackBar(mAddGroupDialog.view , mAddGroupDialog.getString(R.string.error_occured));

        } catch (JSONException e) {
            e.printStackTrace();
            Log.e(TAG , e.toString());
        }

        mAddGroupDialog.progress(false);

    }

    @Override
    public void onResponseFailed(VolleyError error) {
        Log.e(TAG , error.toString());
        AlertHelper.showLongSnackBar(mAddGroupDialog.view , mAddGroupDialog.getString(R.string.error_occured));
        mAddGroupDialog.progress(false);


    }

}
