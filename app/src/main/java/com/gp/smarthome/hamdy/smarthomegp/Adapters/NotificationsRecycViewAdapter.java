package com.gp.smarthome.hamdy.smarthomegp.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.gp.smarthome.hamdy.smarthomegp.Controllers.NotificationsFragmentController;
import com.gp.smarthome.hamdy.smarthomegp.Models.GroupModel;
import com.gp.smarthome.hamdy.smarthomegp.Models.NotificationModel;
import com.gp.smarthome.hamdy.smarthomegp.R;

import java.util.ArrayList;

/**
 * Created by hamdy on 03/06/17.
 */

public class NotificationsRecycViewAdapter extends RecyclerView.Adapter<NotificationsRecycViewAdapter.ViewHolder> {


    private  final String TAG = "DevicesGridViewAdapter";
    public ArrayList<NotificationModel> data;
    private final View parent;
    private final NotificationsFragmentController mNotificationsFragmentController;
    private Context mContext;
    private View view;

    public NotificationsRecycViewAdapter(NotificationsFragmentController  notificationsFragmentController, ArrayList<NotificationModel> data , View parent) {

        mNotificationsFragmentController = notificationsFragmentController;
        mContext = mNotificationsFragmentController.mNotificationsFragment.getActivity();
        this.data =  data;
        this.parent = parent;
    }

    public int getCount() {
        int size  = data.size();

        return  size;
    }

    public NotificationModel getItem(int position) {
        return data.get(position);
    }

    @Override
    public NotificationsRecycViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

         view = LayoutInflater.from(mContext).inflate(R.layout.notification_recy_item , parent , false);


        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

       final NotificationModel item = getItem(position);



        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mNotificationsFragmentController.onDeleteClicked(item);
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

        private final ImageView deleteIV;
        public TextView notificationText;
        public TextView notificationTime;

        public ViewHolder(View view) {

            super(view);

            deleteIV = (ImageView) view.findViewById(R.id.delete_iv);

            notificationText = (TextView) view.findViewById(R.id.noficationRecyItem_notifText_tv);
            notificationTime = (TextView) view.findViewById(R.id.noficationRecyItem_notifTime_tv);
        }

        public void setData(NotificationModel item) {

            notificationText.setText(item.text);
            notificationTime.setText(item.timestamp);

        }
    }


}
