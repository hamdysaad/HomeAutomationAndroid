package com.gp.smarthome.hamdy.smarthomegp.Adapters;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.gp.smarthome.hamdy.smarthomegp.Activities.MainActivity;
import com.gp.smarthome.hamdy.smarthomegp.Controllers.HomeFragmentController;
import com.gp.smarthome.hamdy.smarthomegp.Fragments.DeviceDetailsDialog;
import com.gp.smarthome.hamdy.smarthomegp.Fragments.HomeFragment;
import com.gp.smarthome.hamdy.smarthomegp.Models.DeviceModel;
import com.gp.smarthome.hamdy.smarthomegp.R;
import com.gp.smarthome.hamdy.smarthomegp.SmartHomeApp;
import com.gp.smarthome.hamdy.smarthomegp.User;

import java.util.ArrayList;

/**
 * Created by hamdy on 03/06/17.
 */

public class DevicesGridViewAdapter extends BaseAdapter {


    private  final String TAG = "DevicesGridViewAdapter";
    private final HomeFragmentController mHomeFragmentController;
    public  ArrayList<DeviceModel> data;
    private final View view;
    private Context mContext;

    public DevicesGridViewAdapter(HomeFragmentController controller, ArrayList<DeviceModel> data , View parent) {

        mHomeFragmentController = controller;
        mContext = controller.mHomeFragment.getActivity();
        this.data =  data;
        this.view = parent;
    }

    public int getCount() {
        int size  = data.size();

        return  size;
    }

    public DeviceModel getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }


    // create a new ImageView for each item referenced by the Adapter
    public View getView(final int position, View convertView, ViewGroup parent) {

        View view;
        if (convertView == null) {
            // if it's not recycled, initialize some attributes
            view = LayoutInflater.from(mContext).inflate(R.layout.device_grid_item , parent , false);

        } else {
            view = convertView;
        }



       final DeviceModel item = getItem(position);

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DevicesGridViewAdapter.this.onClick(v , item);
            }
        });


        ViewHolder holder = new ViewHolder(view);


        holder.popupIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mHomeFragmentController.onPopupIV(v , item);
            }
        });

        holder.setData(item);


        return view;
    }

    private void onClick(View v, DeviceModel item) {

        Log.e(TAG, "onClick: " + item.id );
        DeviceDetailsDialog dialog = DeviceDetailsDialog.newInstance(item);

        dialog.show(((MainActivity)mContext).getFragmentManager() , "DeviceDetailsDialog");
    }

    private class ViewHolder{

//        private final LinearLayout editDeleteLL;
        public final ImageView popupIV;
        public ImageView deleteIV;
        public  ImageView editIV;
        public ImageView deviceImageIV;
        public TextView deviceNameTV;

        public ViewHolder(View view) {

            deviceImageIV = (ImageView) view.findViewById(R.id.deviceGrideItem_deviceImage_iv);
            deviceNameTV = (TextView) view.findViewById(R.id.deviceGrideItem_deviceName_tv);
//            editDevicePUMI = (ImageView) view.findViewById(R.id.deviceGrideItem_edit_iv);
            popupIV = (ImageView) view.findViewById(R.id.deviceGrideItem_adminPopupMenu_iv);


//            deleteDevicePUMI = (ImageView) view.findViewById(R.id.deviceGrideItem_delete_iv);
//            editDeleteLL = (LinearLayout) view.findViewById(R.id.deviceGrideItem_ll);


            User user = ((SmartHomeApp) ((Activity) mContext).getApplication()).user;
            if(user.isAdmin()){
                popupIV.setVisibility(View.VISIBLE);
            }
            else
                popupIV.setVisibility(View.GONE);

        }

        public void setData(DeviceModel item) {

            deviceNameTV.setText(item.name);

            deviceImageIV.setImageBitmap(getImageDrawable(item));


        }
    }

    private Bitmap getImageDrawable(DeviceModel model) {

       return model.getImageDrawable(mContext);
    }

}
