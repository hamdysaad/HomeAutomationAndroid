package com.gp.smarthome.hamdy.smarthomegp.Adapters;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

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

public class GroupsRecycViewAdapter extends RecyclerView.Adapter<GroupsRecycViewAdapter.ViewHolder> {


    private  final String TAG = "DevicesGridViewAdapter";
    public ArrayList<GroupModel> data;
    private final View parent;
    private final GroupFragmentController mGroupFragmentController;
    private Context mContext;
    private View view;

    public GroupsRecycViewAdapter(GroupFragmentController groupFragmentController, ArrayList<GroupModel> data , View parent) {

        mGroupFragmentController = groupFragmentController;
        mContext = mGroupFragmentController.mGroupsFragment.getActivity();
        this.data =  data;
        this.parent = parent;
    }

    public int getCount() {
        int size  = data.size();

        if(size == 0 )
            AlertHelper.showLongSnackBar(parent, "No Groups Found");

        return  size;
    }

    public GroupModel getItem(int position) {
        return data.get(position);
    }

    @Override
    public GroupsRecycViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

         view = LayoutInflater.from(mContext).inflate(R.layout.group_recy_item , parent , false);


        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

       final GroupModel item = getItem(position);



        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mGroupFragmentController.onItemClick(v , item);
            }
        });

        holder.popupIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mGroupFragmentController.onPopupClicked(v , item);
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
        return data.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {


        public final TextView groupDescTV;
        public final TextView deviceCountTV;
        private final ImageView popupIV;
        public TextView groupNameTV;

        public ViewHolder(View view) {

            super(view);

            popupIV = (ImageView) view.findViewById(R.id.groupRecyItem_adminPopupMenu_iv);

            groupNameTV = (TextView) view.findViewById(R.id.groupRecyItem_groupName_tv);
            groupDescTV = (TextView) view.findViewById(R.id.groupRecyItem_groupDesc_tv);
            deviceCountTV = (TextView) view.findViewById(R.id.groupRecyItem_deviceCount_tv);

            User user = ((SmartHomeApp) ((Activity) mContext).getApplication()).user;
            if(user.userType.equals("admin")){
                popupIV.setVisibility(View.VISIBLE);
            }



        }

        public void setData(GroupModel item) {

            groupNameTV.setText(item.name);
            groupDescTV.setText(item.description);
            deviceCountTV.setText(item.devices.size() + "");



        }
    }


}
