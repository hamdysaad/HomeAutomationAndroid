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
import com.gp.smarthome.hamdy.smarthomegp.Controllers.AssignGroupDeviceDialogController;
import com.gp.smarthome.hamdy.smarthomegp.Models.DeviceModel;
import com.gp.smarthome.hamdy.smarthomegp.Models.GroupModel;
import com.gp.smarthome.hamdy.smarthomegp.R;

import java.util.ArrayList;

import Vendor.Errors.Error;

/**
 * Created by hamdy on 28/04/17.
 */

public class AssignGroupDeviceDialog extends DialogFragment{


    public static final String TAG = "AssignGroupDeviceDialog";
    private static String KEY_DEVICE = "bundle_group";
    public View view;

    public Button assignBtn;
    public RelativeLayout containerLL;

    private OnFragmentInteractionListener mListener;
    public Spinner devicesSP;
    public TextView devicesErrValidationTV;
    public RecyclerView devicesRV;
    private ProgressBar mProgressBar;
    public GroupModel model;
    public TextView messageTV;


    public static AssignGroupDeviceDialog newInstance(GroupModel model){


        AssignGroupDeviceDialog dialog = new AssignGroupDeviceDialog();

        Bundle bundle = new Bundle();
        bundle.putParcelable(KEY_DEVICE, new AddGroupDialog.GroupParcel(model));
        dialog.setArguments(bundle);

        return dialog;

    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle bundle = getArguments();

        if(bundle != null){

            model = ((AddGroupDialog.GroupParcel) bundle.getParcelable(KEY_DEVICE)).groupModel;
        }

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {


         view = inflater.inflate(R.layout.assign_group_device_dialog , container , false);

        getDialog().setTitle("ASSIGN DEVICE TO GROUP");

        getDialog().getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT , ViewGroup.LayoutParams.WRAP_CONTENT);

        initViews();

        AssignGroupDeviceDialogController controller = new AssignGroupDeviceDialogController(this);

        controller.actionListener();


        return view;
    }

    public void initViews() {


        devicesSP =  (Spinner)view.findViewById(R.id.assignGroupDeviceDialog_spinnerDevices_sp);
        devicesErrValidationTV =  (TextView)view.findViewById(R.id.assignGroupDeviceDialog_devicesErrorValidation);
        devicesRV =  (RecyclerView)view.findViewById(R.id.recyclarView);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        devicesRV.setLayoutManager(mLayoutManager);
        mProgressBar =  (ProgressBar)view.findViewById(R.id.progressBar);
        assignBtn =  (Button)view.findViewById(R.id.assignGroupDeviceDialog_assign_btn);
        messageTV=  (TextView)view.findViewById(R.id.messageTV);

        containerLL = (RelativeLayout)view.findViewById(R.id.container_rl);

    }

    public void progress(boolean b) {
        mProgressBar.setVisibility(b ?  View.VISIBLE : View.GONE);
    }

    public static void showDialog(Activity activity, GroupModel model){

        AssignGroupDeviceDialog addDeviceDialog = AssignGroupDeviceDialog.newInstance(model);

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
}
