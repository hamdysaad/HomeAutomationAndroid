package com.gp.smarthome.hamdy.smarthomegp.Controllers;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.PopupMenu;
import android.util.Log;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.gp.smarthome.hamdy.smarthomegp.Adapters.DevicesGridViewAdapter;
import com.gp.smarthome.hamdy.smarthomegp.Admin;
import com.gp.smarthome.hamdy.smarthomegp.Fragments.AddDeviceDialog;
import com.gp.smarthome.hamdy.smarthomegp.Fragments.AssignDeviceToGroupDialog;
import com.gp.smarthome.hamdy.smarthomegp.Fragments.AssignDeviceToUserDialog;
import com.gp.smarthome.hamdy.smarthomegp.Fragments.HomeFragment;
import com.gp.smarthome.hamdy.smarthomegp.Models.DeviceModel;
import com.gp.smarthome.hamdy.smarthomegp.R;
import com.gp.smarthome.hamdy.smarthomegp.SmartHomeApp;
import com.gp.smarthome.hamdy.smarthomegp.User;
import com.gp.smarthome.hamdy.smarthomegp.Utilities.ResponseParserUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import Vendor.WebService.IResponseHandler;

/**
 * Created by hamdy on 09/06/17.
 */

public class HomeFragmentController {
    private final String TAG = "HomeFragmentController";
    public final HomeFragment mHomeFragment;
    private final Context context;
    private final User user;
    private DevicesGridViewAdapter adapter;
    public ArrayList<DeviceModel> data = new ArrayList<>();

    public HomeFragmentController(HomeFragment fragment) {

        mHomeFragment = fragment;
        context = fragment.getActivity();

        user = ((SmartHomeApp)fragment.getActivity().getApplication()).user;


    }

    public void run() {

        adapter = new DevicesGridViewAdapter(this , data , mHomeFragment.view);

        mHomeFragment.gridview.setAdapter(adapter);


    }

    private void onGetAllDevicesResponseSuccess(String response) {
        Log.e(TAG , response);

        try{
            data = getData(response);
            adapter.data = data;
            adapter.notifyDataSetChanged();
            if(data.size() == 0 )
                mHomeFragment.displayMessage("No Devices Found");
            else
                mHomeFragment.displayMessage(null);

        } catch (JSONException e) {
            e.printStackTrace();
            Log.e(TAG , "onGetAllDevicesResponseSuccess : " + e.toString());
        }

        mHomeFragment.showAnim(false);

    }

    private ArrayList<DeviceModel> getData(String response) throws JSONException {

        data = new ArrayList<>();


            JSONObject object = new JSONObject(response);
            boolean result = object.getBoolean("result");

            if(result){

                JSONArray listJA = object.getJSONObject("data").getJSONArray("list");

                DeviceModel device = new DeviceModel();

                data = device.parseDevicesJA(listJA);


            }else{

                Toast.makeText(context, context.getString(R.string.error_occured), Toast.LENGTH_SHORT).show();
            }


        return data;
    }

    private void onGetAllDevicesResponseFailed(VolleyError error) {
        Log.e(TAG , error.toString());

        displayMessage( context.getString(R.string.error_occured));

        mHomeFragment.showAnim(false);
    }

    public void displayMessage(String message) {

        mHomeFragment.displayMessage(message);
    }


    public void editDevicePUMI(DeviceModel item) {

        AddDeviceDialog.showDialog(mHomeFragment.getActivity(), item);
    }

    private void assignToUserItemMenuw(DeviceModel model) {
        AssignDeviceToUserDialog.showDialog(mHomeFragment.getActivity(), model);
    }

    private void assignToGroupItemMenu(DeviceModel model) {
        AssignDeviceToGroupDialog.showDialog(mHomeFragment.getActivity(), model);

    }

    public void deleteDevicePUMI(final DeviceModel model) {


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


    private void onDeleteResponseFailed(VolleyError error) {
        Log.e(TAG , "onDeleteResponseFailed : " + error.toString() );

        Toast.makeText(context, context.getString(R.string.delete_failed), Toast.LENGTH_SHORT).show();

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

    public void refreshAdapter(){
        user.getAllDevice(context, new IResponseHandler() {
            @Override
            public void onResponseSuccess(String response) {
                onGetAllDevicesResponseSuccess(response);
            }

            @Override
            public void onResponseFailed(VolleyError error) {
                onGetAllDevicesResponseFailed(error);
            }
        });
    }

    public void onPopupIV(View view , final DeviceModel model) {

        Log.e(TAG , "onPopupIV : " + model.id);

        PopupMenu popup = new PopupMenu(context,view );
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.admin_device_menu, popup.getMenu());
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                onDeviceGridItemPopClick(item , model);
                return true;
            }
        });
        popup.show();
    }

    private void onDeviceGridItemPopClick(MenuItem item, DeviceModel model) {

        switch (item.getItemId()){
            case R.id.edit:
                editDevicePUMI(model);
                break;

            case R.id.delete:
                deleteDevicePUMI(model);
                break;

            case R.id.assign_to_user:
                assignToUserItemMenuw(model);
                break;

            case R.id.assign_to_group:
                assignToGroupItemMenu(model);
                break;
        }
    }


    public void onStart() {

        if(mHomeFragment.devices == null)
            refreshAdapter();
        else{

            if(data.size() == 0 )
                mHomeFragment.displayMessage("No Devices Found");
            else
                mHomeFragment.displayMessage(null);
        }

        adapter.notifyDataSetChanged();
    }
}
