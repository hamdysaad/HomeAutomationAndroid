package com.gp.smarthome.hamdy.smarthomegp.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.gp.smarthome.hamdy.smarthomegp.Controllers.AssignGroupDeviceDialogController;
import com.gp.smarthome.hamdy.smarthomegp.Models.DeviceModel;
import com.gp.smarthome.hamdy.smarthomegp.R;

import java.util.ArrayList;

/**
 * Created by hamdy on 03/06/17.
 */

public class GroupDevicesRecycViewAdapter extends RecyclerView.Adapter<GroupDevicesRecycViewAdapter.ViewHolder> {


    private  final String TAG = "DevicesGridViewAdapter";
    public ArrayList<DeviceModel> data;
    private final View parent;
    private final AssignGroupDeviceDialogController mAssignGroupDeviceDialogController;
    private Context mContext;
    private View view;

    public GroupDevicesRecycViewAdapter(AssignGroupDeviceDialogController controller, ArrayList<DeviceModel> data , View parent) {

        mAssignGroupDeviceDialogController = controller;
        mContext = mAssignGroupDeviceDialogController.mAssignGroupDeviceDialog.getActivity();
        this.data =  data;
        this.parent = parent;
    }



    public DeviceModel getItem(int position) {
        return data.get(position);
    }

    @Override
    public GroupDevicesRecycViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

         view = LayoutInflater.from(mContext).inflate(R.layout.group_device_recy_item , parent , false);


        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

       final DeviceModel item = getItem(position);


        holder.deleteGroupDeviceIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAssignGroupDeviceDialogController.deleteGroupDevice(item);
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


        private final TextView nameTV;
        private final ImageView deleteGroupDeviceIV;
        private final ImageView deviceImageIV;

        public ViewHolder(View view) {

            super(view);

            nameTV = (TextView) view.findViewById(R.id.GroupDeviceRecyItem_name_tv);
            deleteGroupDeviceIV = (ImageView) view.findViewById(R.id.delete_GroupDevice);
            deviceImageIV = (ImageView) view.findViewById(R.id.deviceImage);
        }

        public void setData(DeviceModel item) {

            nameTV.setText(item.name);
            deviceImageIV.setImageBitmap(item.getImageDrawable(mContext));



        }
    }


}
