package com.gp.smarthome.hamdy.smarthomegp.Fragments;


import android.app.Activity;
import android.app.DialogFragment;
import android.content.Context;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.gp.smarthome.hamdy.smarthomegp.Controllers.AssignDeviceToGroupDialogController;
import com.gp.smarthome.hamdy.smarthomegp.Models.DeviceModel;
import com.gp.smarthome.hamdy.smarthomegp.R;

import java.util.ArrayList;

import Vendor.Errors.Error;

/**
 * Created by hamdy on 28/04/17.
 */

public class AssignDeviceToGroupDialog extends DialogFragment{


    public static final String TAG = "AddDeviceDialog";
    private static String KEY_DEVICE = "bundle_device";
    public View view;

    public Button assignBtn;
    public RelativeLayout containerLL;

    private OnFragmentInteractionListener mListener;
    public Spinner groupsSP;
    public TextView groupErrValidationTV;
    public RecyclerView groupsRV;
    private ProgressBar mProgressBar;
    public DeviceModel deviceModel;
    public TextView messageTV;


    public static AssignDeviceToGroupDialog newInstance(DeviceModel model){


        AssignDeviceToGroupDialog dialog = new AssignDeviceToGroupDialog();

        Bundle bundle = new Bundle();
        bundle.putParcelable(KEY_DEVICE, new DeviceParcelable(model));
        dialog.setArguments(bundle);

        return dialog;

    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle bundle = getArguments();

        if(bundle != null){

            deviceModel = ((DeviceParcelable) bundle.getParcelable(KEY_DEVICE)).device;
        }

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {


         view = inflater.inflate(R.layout.assign_device_to_group_dialog , container , false);

        getDialog().setTitle("ASSIGN DEVICE TO GROUP");

        getDialog().getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT , ViewGroup.LayoutParams.WRAP_CONTENT);

        initViews();

        AssignDeviceToGroupDialogController controller = new AssignDeviceToGroupDialogController(this);

        controller.actionListener();


        return view;
    }

    public void initViews() {


        groupsSP =  (Spinner)view.findViewById(R.id.assignDeviceToGroupDialog_spinnerUsers_sp);
        groupErrValidationTV =  (TextView)view.findViewById(R.id.groupsErrorValidation);
        groupsRV =  (RecyclerView)view.findViewById(R.id.recyclarView);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        groupsRV.setLayoutManager(mLayoutManager);
        mProgressBar =  (ProgressBar)view.findViewById(R.id.progressBar);
        assignBtn =  (Button)view.findViewById(R.id.assignDeviceToGroupDialog_assign_btn);
        messageTV=  (TextView)view.findViewById(R.id.messageTV);

        containerLL = (RelativeLayout)view.findViewById(R.id.container_rl);

    }

    public void progress(boolean b) {
        mProgressBar.setVisibility(b ?  View.VISIBLE : View.GONE);
    }

    public static void showDialog(Activity activity, DeviceModel model){

        AssignDeviceToGroupDialog addDeviceDialog = AssignDeviceToGroupDialog.newInstance(model);

        addDeviceDialog.show(activity.getFragmentManager() , TAG);

    }

    public void displayValidationErrors(ArrayList<Error> errors) {

        for(Error error : errors)
            error.displayError();

        progress(false);
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof HomeFragment.OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public void showMessage(boolean b) {
        messageTV.setVisibility(b ?  View.VISIBLE : View.GONE);

    }


    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and fName
    }



    public static class DeviceParcelable implements Parcelable {


        private  DeviceModel device;

        protected DeviceParcelable(Parcel in) {
            in.writeString(device.id);
            in.writeString(device.name);
            in.writeString(device.description);
            in.writeString(device.category);
            in.writeString(device.currentState);
            in.readList(device.groups,DeviceModel.class.getClassLoader());
            in.readList(device.users,DeviceModel.class.getClassLoader());
        }

        public DeviceParcelable(DeviceModel  model){

            this.device = model;
        }


        public static final Creator<DeviceParcelable> CREATOR = new Creator<DeviceParcelable>() {
            @Override
            public DeviceParcelable createFromParcel(Parcel in) {
                return new DeviceParcelable(in);
            }

            @Override
            public DeviceParcelable[] newArray(int size) {
                return new DeviceParcelable[size];
            }
        };

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {


            dest.writeString(device.id);
            dest.writeString(device.name);
            dest.writeString(device.description);
            dest.writeString(device.category);
            dest.writeString(device.currentState);
            dest.writeList(device.users);
            dest.writeList(device.groups);
        }
    }
}
