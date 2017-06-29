package com.gp.smarthome.hamdy.smarthomegp.Controllers;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.gp.smarthome.hamdy.smarthomegp.Activities.MainActivity;
import com.gp.smarthome.hamdy.smarthomegp.Admin;
import com.gp.smarthome.hamdy.smarthomegp.Fragments.AddDeviceDialog;
import com.gp.smarthome.hamdy.smarthomegp.Models.DeviceModel;
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
import Vendor.Errors.MyEditText;
import Vendor.Errors.MyTextView;
import Vendor.Errors.TextInput;
import Vendor.Validation.IValidationHandler;
import Vendor.Validation.IValidationRule;
import Vendor.Validation.Required;
import Vendor.Validation.Validation;
import Vendor.WebService.IResponseHandler;

/**
 * Created by Hamdy on 4/18/2017.
 */

public class AddDeviceDialogController implements IValidationHandler, IResponseHandler {

    private  final String TAG = "AddDeviceDialogCTRL";
    public final AddDeviceDialog addDeviceDialog;
    private final Admin admin;
    private final MainActivity mMainActivity;
    private  DeviceModel model;
    private Context context;
    private String name;
    private String desc;
    private String category;
    private int status;


    public AddDeviceDialogController(AddDeviceDialog deviceDetailsDialog) {
        context = deviceDetailsDialog.getActivity();
        this.addDeviceDialog = deviceDetailsDialog;


        context = deviceDetailsDialog.getActivity();

        mMainActivity = (MainActivity)deviceDetailsDialog.getActivity();


        admin = (Admin)((SmartHomeApp)((Activity)context).getApplication()).user;

        model = addDeviceDialog.model;

        if(model != null)
            addDeviceDialog.setData();

    }


    public void actionListener() {

        addDeviceDialog.addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addBtn();
            }
        });

    }

    private void addBtn() {

        addDeviceDialog.progress(true);

         name = addDeviceDialog.nameET.getText().toString();
         desc = addDeviceDialog.descET.getText().toString();

//        if(addDeviceDialog.categorySP.getSelectedItemPosition() != 0)
         category = addDeviceDialog.categorySP.getSelectedItemPosition() != 0 ?
                 context.getResources().getStringArray(R.array.selectCategoryValues)[addDeviceDialog.categorySP.getSelectedItemPosition()-1]
                 : "";

         status = addDeviceDialog.statusRG.getCheckedRadioButtonId() == R.id.addDeviceDialog_statusOn_rb ? 1 : 0;



        Validation validation = new Validation();

        //name
        validation.addValidationField(
                name ,
                new TextInput( addDeviceDialog.nameTIL) ,
                new IValidationRule[]{
                        new Required(),
                });

        //desc
        validation.addValidationField(
                desc ,
                new TextInput(addDeviceDialog.descTIL ),
                new IValidationRule[]{
                        new Required()
                } );


        validation.validate(context , this);


    }

    @Override
    public void onValidationSuccessfull() {

        DeviceModel model = new DeviceModel();

        model.adminId = admin.id;
        model.name = name;
        model.description = desc;
        model.category = category;
        model.currentState = status + "";

        admin.setiAddBehavoir(model);

        if(addDeviceDialog.edit) {
            model.id = addDeviceDialog.model.id;
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

        addDeviceDialog.dismiss();
    }

    private void onEditResponseSuccess(String response) {

        Log.e(TAG , "onEditResponseSuccess : "  + response);

        try {
            ResponseParserUtils parser = new ResponseParserUtils(response);

            if(parser.result){

                Toast.makeText(mMainActivity, context.getString(R.string.edit_successfully), Toast.LENGTH_SHORT).show();

                mMainActivity.mHomeFragment.controller.refreshAdapter();
            }
            else
                Toast.makeText(mMainActivity, context.getString(R.string.edit_failed), Toast.LENGTH_SHORT).show();

        } catch (JSONException e) {
            e.printStackTrace();
            Log.e(TAG , e.toString());
        }

        addDeviceDialog.dismiss();
    }

    @Override
    public void onValidationFailed(ArrayList<Error> errors) {

        addDeviceDialog.displayValidationErrors(errors);

        addDeviceDialog.progress(false);

    }

    @Override
    public void onResponseSuccess(String response) {
        Log.e(TAG , response);

        try {
            ResponseParserUtils parser = new ResponseParserUtils(response);

            if(parser.result){
                //refresh devices adapter
                Log.e(TAG , "refresh adapter");

                Toast.makeText(mMainActivity, addDeviceDialog.getString(R.string.device_added_successfully), Toast.LENGTH_SHORT).show();

                mMainActivity.mHomeFragment.controller.refreshAdapter();
                addDeviceDialog.dismiss();
            }
            else
                AlertHelper.showLongSnackBar(addDeviceDialog.view , addDeviceDialog.getString(R.string.error_occured));

        } catch (JSONException e) {
            e.printStackTrace();
            Log.e(TAG , e.toString());
        }

        addDeviceDialog.progress(false);

    }

    @Override
    public void onResponseFailed(VolleyError error) {
        Log.e(TAG , error.toString());
        AlertHelper.showLongSnackBar(addDeviceDialog.view , addDeviceDialog.getString(R.string.error_occured));
        addDeviceDialog.progress(false);


    }
}
