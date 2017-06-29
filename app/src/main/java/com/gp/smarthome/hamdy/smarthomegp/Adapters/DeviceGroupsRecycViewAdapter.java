package com.gp.smarthome.hamdy.smarthomegp.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.gp.smarthome.hamdy.smarthomegp.Controllers.AssignDeviceToGroupDialogController;
import com.gp.smarthome.hamdy.smarthomegp.Models.GroupModel;
import com.gp.smarthome.hamdy.smarthomegp.R;
import com.gp.smarthome.hamdy.smarthomegp.User;

import java.util.ArrayList;

/**
 * Created by hamdy on 03/06/17.
 */

public class DeviceGroupsRecycViewAdapter extends RecyclerView.Adapter<DeviceGroupsRecycViewAdapter.ViewHolder> {


    private  final String TAG = "DevicesGridViewAdapter";
    public ArrayList<GroupModel> data;
    private final View parent;
    private final AssignDeviceToGroupDialogController mAssignDeviceToGroupDialogController;
    private Context mContext;
    private View view;

    public DeviceGroupsRecycViewAdapter(AssignDeviceToGroupDialogController controller, ArrayList<GroupModel> data , View parent) {

        mAssignDeviceToGroupDialogController = controller;
        mContext = mAssignDeviceToGroupDialogController.mAssignDeviceToGroupDialog.getActivity();
        this.data =  data;
        this.parent = parent;
    }



    public GroupModel getItem(int position) {
        return data.get(position);
    }

    @Override
    public DeviceGroupsRecycViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

         view = LayoutInflater.from(mContext).inflate(R.layout.device_groups_recy_item , parent , false);


        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

       final GroupModel item = getItem(position);


        holder.deleteDeviceGroupIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAssignDeviceToGroupDialogController.deleteDeviceGroup(item);
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


        private final TextView groupNameTV;
        private final ImageView deleteDeviceGroupIV;
        private final TextView devicesCount;

        public ViewHolder(View view) {

            super(view);

            groupNameTV = (TextView) view.findViewById(R.id.deviceGroupsRecyItem_groupName_tv);
            devicesCount = (TextView) view.findViewById(R.id.deviceGroupsRecyItem_deviceCount_tv);
            deleteDeviceGroupIV = (ImageView) view.findViewById(R.id.delete_deviceGroup);
        }

        public void setData(GroupModel item) {

            groupNameTV.setText(item.name);
            devicesCount.setText("Devices Count : " + item.devices.size());



        }
    }


}
