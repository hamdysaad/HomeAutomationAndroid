package com.gp.smarthome.hamdy.smarthomegp.Adapters;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.gp.smarthome.hamdy.smarthomegp.Controllers.AssignDeviceToUserDialogController;
import com.gp.smarthome.hamdy.smarthomegp.Controllers.GroupFragmentController;
import com.gp.smarthome.hamdy.smarthomegp.Models.GroupModel;
import com.gp.smarthome.hamdy.smarthomegp.R;
import com.gp.smarthome.hamdy.smarthomegp.SmartHomeApp;
import com.gp.smarthome.hamdy.smarthomegp.User;
import com.gp.smarthome.hamdy.smarthomegp.Utilities.AlertHelper;

import java.util.ArrayList;

/**
 * Created by hamdy on 03/06/17.
 */

public class DeviceUsersRecycViewAdapter extends RecyclerView.Adapter<DeviceUsersRecycViewAdapter.ViewHolder> {


    private  final String TAG = "DevicesGridViewAdapter";
    public ArrayList<User> data;
    private final View parent;
    private final AssignDeviceToUserDialogController mAssignDeviceToUserDialogController;
    private Context mContext;
    private View view;

    public DeviceUsersRecycViewAdapter(AssignDeviceToUserDialogController controller, ArrayList<User> data , View parent) {

        mAssignDeviceToUserDialogController = controller;
        mContext = mAssignDeviceToUserDialogController.mAssignDeviceToUserDialog.getActivity();
        this.data =  data;
        this.parent = parent;
    }



    public User getItem(int position) {
        return data.get(position);
    }

    @Override
    public DeviceUsersRecycViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

         view = LayoutInflater.from(mContext).inflate(R.layout.device_users_recy_item , parent , false);


        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

       final User item = getItem(position);


        holder.deleteDeviceUserIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAssignDeviceToUserDialogController.deleteDeviceUser(item);
            }
        });

        holder.setData(item);

    }


    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public int getItemCount() {
        int size  = data.size();
        return  size;
    }


    public class ViewHolder extends RecyclerView.ViewHolder {


        private final TextView userNameTV;
        private final ImageView deleteDeviceUserIV;

        public ViewHolder(View view) {

            super(view);

            userNameTV = (TextView) view.findViewById(R.id.deviceUsersRecyItem_userName_tv);
            deleteDeviceUserIV = (ImageView) view.findViewById(R.id.delete_deviceUser);
        }

        public void setData(User item) {

            userNameTV.setText(item.fName +  " " +item.lName);



        }
    }


}
