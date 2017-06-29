package com.gp.smarthome.hamdy.smarthomegp.Fragments;


import android.app.Activity;
import android.app.DialogFragment;
import android.content.Context;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.gp.smarthome.hamdy.smarthomegp.Activities.MainActivity;
import com.gp.smarthome.hamdy.smarthomegp.Controllers.AddDeviceDialogController;
import com.gp.smarthome.hamdy.smarthomegp.Models.DeviceModel;
import com.gp.smarthome.hamdy.smarthomegp.R;

import java.util.ArrayList;

import Vendor.Errors.Error;

/**
 * Created by hamdy on 28/04/17.
 */

public class AddDeviceDialog extends DialogFragment{


    public static final String TAG = "AddDeviceDialog";
    private static String KEY_DEVICE = "bundle_device";
    public View view;
    public DeviceModel model;
    public EditText nameET;
    public EditText descET;
    public Spinner categorySP;
    public RadioGroup statusRG;
    public RadioButton statusOnRB;
    public RadioButton statusOffRB;
    public TextInputLayout nameTIL;
    public TextInputLayout descTIL;
    public FrameLayout mProgressContainer;
    public Button addBtn;
    public RelativeLayout containerLL;

    private OnFragmentInteractionListener mListener;
    public boolean edit = false;
    private ProgressBar mProgressBar;


    public static AddDeviceDialog newInstance(DeviceModel item){


        AddDeviceDialog dialog = new AddDeviceDialog();


        if(item != null) {
            Bundle bundle = new Bundle();
            bundle.putParcelable(KEY_DEVICE, new DeviceParcel(item));
            dialog.setArguments(bundle);
        }
        return dialog;

    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle bundle = getArguments();

        if(bundle != null){

            model = ((DeviceParcel) bundle.getParcelable(KEY_DEVICE)).deviceModel;
        }

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {


         view = inflater.inflate(R.layout.add_device_dialog , container , false);

        getDialog().setTitle(model != null ? "EDIT DEVICE" : "ADD DEVICE");

        getDialog().getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT , ViewGroup.LayoutParams.WRAP_CONTENT);

        initViews();

        AddDeviceDialogController controller = new AddDeviceDialogController(this);

        controller.actionListener();


        return view;
    }

    public void initViews() {


        nameET =  (EditText)view.findViewById(R.id.addDeviceDialog_name_et);
        descET =  (EditText)view.findViewById(R.id.addDeviceDialog_desc_et);
        categorySP =  (Spinner)view.findViewById(R.id.addDeviceDialog_spinnerCategory_sp);
        statusRG =  (RadioGroup)view.findViewById(R.id.addDeviceDialog_status_rg);
        statusOnRB =  (RadioButton)view.findViewById(R.id.addDeviceDialog_statusOn_rb);
        statusOffRB =  (RadioButton)view.findViewById(R.id.addDeviceDialog_statusOff_rb);

        mProgressBar =  (ProgressBar)view.findViewById(R.id.progressBar);

        nameTIL =  (TextInputLayout)view.findViewById(R.id.addDeviceDialog_name_til);
        descTIL =  (TextInputLayout)view.findViewById(R.id.addDeviceDialog_desc_til);

        addBtn =  (Button)view.findViewById(R.id.addDeviceDialog_add_btn);


        mProgressContainer = (FrameLayout)view.findViewById(R.id.addDeviceDialog_progressContainer_fl);
        containerLL = (RelativeLayout)view.findViewById(R.id.addDeviceDialog_container_rl);

    }

    public void progress(boolean b) {
        mProgressBar.setVisibility(b ?  View.VISIBLE : View.GONE);
    }

    public static void showDialog(Activity activity, DeviceModel item){

        AddDeviceDialog addDeviceDialog = AddDeviceDialog.newInstance(item);

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
            mListener.setAddDeviceDialog(this);
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

    public void setData() {

        edit = true;
        nameET.setText(model.name);
        descET.setText(model.description);
        categorySP.setSelection(2);
        statusRG.check(model.currentState.equals("1")? R.id.addDeviceDialog_statusOn_rb : R.id.addDeviceDialog_statusOff_rb);

        addBtn.setText(getString(R.string.edit));
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and fName
        void setAddDeviceDialog(AddDeviceDialog fragment);
    }


    public static class DeviceParcel implements Parcelable {


        private  DeviceModel deviceModel;

        protected DeviceParcel(Parcel in) {

            deviceModel.name = in.readString();
            deviceModel.description=  in.readString();
            deviceModel.category = in.readString();
            deviceModel.currentState = in.readString();
            deviceModel.createdAt = in.readString();
            deviceModel.updatedAt = in.readString();
        }

        public  DeviceParcel(DeviceModel model){

            this.deviceModel = model;
        }


        public static final Creator<HomeFragment.MyParcel> CREATOR = new Creator<HomeFragment.MyParcel>() {
            @Override
            public HomeFragment.MyParcel createFromParcel(Parcel in) {
                return new HomeFragment.MyParcel(in);
            }

            @Override
            public HomeFragment.MyParcel[] newArray(int size) {
                return new HomeFragment.MyParcel[size];
            }
        };

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {

            dest.writeString(deviceModel.name);
            dest.writeString(deviceModel.description);
            dest.writeString(deviceModel.category);
            dest.writeString(deviceModel.currentState);
            dest.writeString(deviceModel.createdAt);
            dest.writeString(deviceModel.updatedAt);
        }
    }

}
