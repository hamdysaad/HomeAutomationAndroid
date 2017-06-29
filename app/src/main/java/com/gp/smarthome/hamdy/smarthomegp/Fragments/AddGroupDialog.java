package com.gp.smarthome.hamdy.smarthomegp.Fragments;


import android.app.Activity;
import android.app.DialogFragment;
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

import com.gp.smarthome.hamdy.smarthomegp.Controllers.AddGroupDialogController;
import com.gp.smarthome.hamdy.smarthomegp.Models.DeviceModel;
import com.gp.smarthome.hamdy.smarthomegp.Models.GroupModel;
import com.gp.smarthome.hamdy.smarthomegp.R;

import java.util.ArrayList;

import Vendor.Errors.Error;

/**
 * Created by hamdy on 28/04/17.
 */

public class AddGroupDialog extends DialogFragment{


    public static final String TAG = "AddGroupDialog";
    private static final String KEY_GROUP = "key_group";
    public View view;
    public GroupModel model;
    public EditText nameET;
    public EditText descET;
    public TextInputLayout nameTIL;
    public TextInputLayout descTIL;
    public FrameLayout mProgressContainer;
    public Button addBtn;
    public boolean edit = false;


    public static AddGroupDialog newInstance(GroupModel item){



        AddGroupDialog dialog = new AddGroupDialog();


        if(item != null) {
            Bundle bundle = new Bundle();
            bundle.putParcelable(KEY_GROUP, new GroupParcel(item));
            dialog.setArguments(bundle);
        }
        return dialog;

    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle bundle = getArguments();

        if(bundle != null){

            model = ((GroupParcel) bundle.getParcelable(KEY_GROUP)).groupModel;
        }

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {


         view = inflater.inflate(R.layout.add_group_dialog , container , false);

        getDialog().setTitle("ADD Group");

        getDialog().getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT , ViewGroup.LayoutParams.WRAP_CONTENT);

        initViews();

        AddGroupDialogController controller = new AddGroupDialogController(this);

        controller.actionListener();


        return view;
    }

    public void initViews() {


        nameET =  (EditText)view.findViewById(R.id.addGroupDialog_name_et);
        descET =  (EditText)view.findViewById(R.id.addGroupDialog_desc_et);



        nameTIL =  (TextInputLayout)view.findViewById(R.id.addGroupDialog_name_til);
        descTIL =  (TextInputLayout)view.findViewById(R.id.addGroupDialog_desc_til);


        addBtn =  (Button)view.findViewById(R.id.addGroupDialog_add_btn);



        mProgressContainer = (FrameLayout)view.findViewById(R.id.addGroupDialog_progressContainer_fl);

    }

    public void progress(boolean b) {
        mProgressContainer.setVisibility(b ?  View.VISIBLE : View.GONE);
    }

    public static void showDialog(Activity activity , GroupModel model){

        AddGroupDialog addGroupDialog = AddGroupDialog.newInstance(model);

        addGroupDialog.show(activity.getFragmentManager() , TAG);

    }

    public void setData() {

        edit = true;
        nameET.setText(model.name);
        descET.setText(model.description);
        addBtn.setText(getString(R.string.edit));
    }

    public void displayValidationErrors(ArrayList<Error> errors) {

        for(Error error : errors)
            error.displayError();

        progress(false);
    }


    public static class GroupParcel implements Parcelable {


        public GroupModel groupModel;

        protected GroupParcel(Parcel in) {
            groupModel.id = in.readString();
            groupModel.name = in.readString();
            groupModel.description=  in.readString();
            in.readList(groupModel.devices , GroupModel.class.getClassLoader());
            in.readList(groupModel.users , GroupModel.class.getClassLoader());
        }

        public  GroupParcel(GroupModel model){

            this.groupModel = model;
        }


        public static final Creator<GroupParcel> CREATOR = new Creator<GroupParcel>() {
            @Override
            public GroupParcel createFromParcel(Parcel in) {
                return new GroupParcel(in);
            }

            @Override
            public GroupParcel[] newArray(int size) {
                return new GroupParcel[size];
            }
        };

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {

            dest.writeString(groupModel.id);
            dest.writeString(groupModel.name);
            dest.writeString(groupModel.description);
            dest.writeList(groupModel.devices);
            dest.writeList(groupModel.users);

        }
    }

}
