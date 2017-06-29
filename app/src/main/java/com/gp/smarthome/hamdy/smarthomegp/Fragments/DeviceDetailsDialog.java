package com.gp.smarthome.hamdy.smarthomegp.Fragments;


import android.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Switch;
import android.widget.TextView;

import com.gp.smarthome.hamdy.smarthomegp.Controllers.DeviceDetailsDialogController;
import com.gp.smarthome.hamdy.smarthomegp.Models.DeviceModel;
import com.gp.smarthome.hamdy.smarthomegp.R;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

/**
 * Created by hamdy on 28/04/17.
 */

public class DeviceDetailsDialog extends DialogFragment{


    public View view;
    public DeviceModel model;
    public Switch switchSW;
    public TextView deviceNameTV;
    public TextView deviceDescTV;
    public TextView okTV;


    public static DeviceDetailsDialog newInstance(DeviceModel model){


        DeviceDetailsDialog dialog = new DeviceDetailsDialog();

        Bundle bundle = new Bundle();

        if(model!= null) {
            bundle.putString("id", model.id);
            bundle.putString("name", model.name);
            bundle.putString("description", model.description);
            bundle.putString("category", model.category);
            bundle.putString("state", model.currentState);
        }

        dialog.setArguments(bundle);

        return dialog;

    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle args = getArguments();

        if(args != null ){

            model = new DeviceModel();
            model.id = args.getString("id");
            model.name = args.getString("name");
            model.description = args.getString("description");
            model.currentState = args.getString("state");
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {


         view = inflater.inflate(R.layout.device_details_dialog , container , false);

        getDialog().setTitle("Device Details");

        getDialog().getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT , ViewGroup.LayoutParams.WRAP_CONTENT);

        initViews();

        DeviceDetailsDialogController controller = new DeviceDetailsDialogController(this);

        controller.actionListener();


        return view;
    }

    public void initViews() {


        switchSW=  (Switch)view.findViewById(R.id.deviceDetailsDialog_switch_sw);
        deviceNameTV =  (TextView)view.findViewById(R.id.deviceDetailsDialog_deviceName_tv);
        deviceDescTV =  (TextView)view.findViewById(R.id.deviceDetailsDialog_deviceDescription_tv);
        okTV =  (TextView)view.findViewById(R.id.deviceDetailsDialog_ok_tv);

        GraphView graph = (GraphView) view.findViewById(R.id.deviceDetailsDialog_consumption_gv);
        LineGraphSeries<DataPoint> series = new LineGraphSeries<>(new DataPoint[] {
                new DataPoint(0, 1),
                new DataPoint(1, 5),
                new DataPoint(2, 3),
                new DataPoint(3, 2),
                new DataPoint(4, 6)
        });
        graph.addSeries(series);

        graph.getGridLabelRenderer().setHorizontalAxisTitle("Month");
        graph.getGridLabelRenderer().setVerticalAxisTitle("Consumption");

        setSwitch();
    }

    private void setSwitch() {

        switchSW.setChecked(model.currentState.equals("1"));
    }


    public void setData() {

        setSwitch();
        deviceNameTV.setText(model.name);
        deviceDescTV.setText(model.description);

    }
}
