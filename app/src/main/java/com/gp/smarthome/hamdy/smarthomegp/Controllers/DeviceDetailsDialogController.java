package com.gp.smarthome.hamdy.smarthomegp.Controllers;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.gp.smarthome.hamdy.smarthomegp.Fragments.DeviceDetailsDialog;
import com.gp.smarthome.hamdy.smarthomegp.Models.DeviceModel;

import org.json.JSONException;
import org.json.JSONObject;

import Vendor.WebService.IResponseHandler;

/**
 * Created by Hamdy on 4/18/2017.
 */

public class DeviceDetailsDialogController{

    private  final String TAG = "LoginController";
    public final DeviceDetailsDialog mDeviceDetailsDialog;
    private final DeviceModel model;
    private Context context;

    public DeviceDetailsDialogController(DeviceDetailsDialog deviceDetailsDialog) {
        context = deviceDetailsDialog.getActivity();
        this.mDeviceDetailsDialog = deviceDetailsDialog;


        context = deviceDetailsDialog.getActivity();

        model = deviceDetailsDialog.model;

        mDeviceDetailsDialog.setData();

    }


    public void actionListener() {


        mDeviceDetailsDialog.switchSW.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                switchSW(buttonView , isChecked);
            }
        });

        mDeviceDetailsDialog.okTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDeviceDetailsDialog.dismiss();
            }
        });
    }

    private void switchSW(CompoundButton buttonView ,boolean isChecked) {

        Log.e(TAG, "switchSW : " + isChecked);

        model.currentState = isChecked ? "1" : "0" ;

        model.controlDevice(context, new IResponseHandler() {
            @Override
            public void onResponseSuccess(String response) {

                onControlDeviceResponseSuccess(response);

            }

            @Override
            public void onResponseFailed(VolleyError error) {

                onControlDeviceonResponseFailed(error);
            }
        });


        /*
        *
     انت هتبعت post request لل url
    ال parameters هتكون
    device_id
    device_state
        * */


    }

    private void onControlDeviceonResponseFailed(VolleyError error) {
        Log.e(TAG , "onControlDeviceonResponseFailed : " + error.toString());

        Toast.makeText(context, "error occured", Toast.LENGTH_SHORT).show();

    }

    private void onControlDeviceResponseSuccess(String response) {
        Log.e(TAG , "onControlDeviceResponseSuccess : "  + response);

        try {
            JSONObject responseJO = new JSONObject(response);
            boolean result = responseJO.getBoolean("result");

            if(result){
//                Toast.makeText(context, "device state changed", Toast.LENGTH_SHORT).show();
            }
            else
            {
                String errorMessage = responseJO.getString("errormsg");
                Log.e(TAG , "onControlDeviceResponseSuccess : errorMessage " + errorMessage);

            }

        } catch (JSONException e) {
            e.printStackTrace();
            Log.e(TAG , "onControlDeviceResponseSuccess : " + e.toString());
        }


    }
}
