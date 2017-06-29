package com.gp.smarthome.hamdy.smarthomegp.Fragments;

import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.gp.smarthome.hamdy.smarthomegp.Activities.LoginActivity;
import com.gp.smarthome.hamdy.smarthomegp.Activities.MainActivity;
import com.gp.smarthome.hamdy.smarthomegp.Activities.SplashActivity;
import com.gp.smarthome.hamdy.smarthomegp.Adapters.DevicesGridViewAdapter;
import com.gp.smarthome.hamdy.smarthomegp.Admin;
import com.gp.smarthome.hamdy.smarthomegp.Controllers.HomeFragmentController;
import com.gp.smarthome.hamdy.smarthomegp.Models.DeviceModel;
import com.gp.smarthome.hamdy.smarthomegp.R;
import com.gp.smarthome.hamdy.smarthomegp.SmartHomeApp;
import com.gp.smarthome.hamdy.smarthomegp.User;
import com.gp.smarthome.hamdy.smarthomegp.Utilities.AlertHelper;
import com.gp.smarthome.hamdy.smarthomegp.Utilities.ResponseParserUtils;
import com.wang.avi.AVLoadingIndicatorView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import Vendor.WebService.IResponseHandler;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link HomeFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment implements IShowAddBehavoirDialog {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    public static final String TAG = "HomeFragment";
    private static final String EXTRA_DEVICES = "extra-devices";


    private OnFragmentInteractionListener mListener;
    public View view;
    public GridView gridview;
    public ArrayList<DeviceModel> devices;
    private AVLoadingIndicatorView loadingAVL;
    private LinearLayout containerRL;
    private RelativeLayout LoadingContainerRL;
    private TextView messageTV;
    public HomeFragmentController controller;

    public HomeFragment() {
        // Required empty public constructor
    }

    public static HomeFragment newInstance(ArrayList<DeviceModel> devices) {

        HomeFragment fragment = new HomeFragment();

        if(devices !=null){

            Bundle bundle = new Bundle();

            bundle.putParcelable(EXTRA_DEVICES, new MyParcel(devices));

            fragment.setArguments(bundle);

        }
        return fragment;
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        controller = new HomeFragmentController(this);
        Bundle bundle = getArguments();

        if(bundle != null){
            devices = ((MyParcel)bundle.getParcelable(EXTRA_DEVICES)).devices;
            controller.data = devices;
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
         view = inflater.inflate(R.layout.fragment_home, container, false);

        gridview =  (GridView) view.findViewById(R.id.gridview);

        containerRL =  (LinearLayout) view.findViewById(R.id.fragmentHome_container_rl);
        LoadingContainerRL =  (RelativeLayout) view.findViewById(R.id.fragmentHome_loadingContainerRL);
        loadingAVL =  (AVLoadingIndicatorView) view.findViewById(R.id.fragmentHome_loading_avl);
        messageTV =  (TextView) view.findViewById(R.id.message);


        controller.run();

        return view;

    }

    @Override
    public void onResume() {
        super.onResume();
        mListener.setHomefragment(this);

    }


    public void showAnim(boolean show){

        if(show)
            loadingAVL.show();
        else
            loadingAVL.hide();
    }

    private void showLoadingContainer(boolean show) {
        LoadingContainerRL.setVisibility(show ? View.VISIBLE : View.GONE);
    }


    public void displayMessage(String message) {

        if(message != null) {
            AlertHelper.showLongSnackBar(view, message);
            messageTV.setText(message);
        }

        showLoadingContainer( message != null ?  true : false);
        messageTV.setVisibility(message != null ? View.VISIBLE : View.GONE);
        showAnim(false);
    }



    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
            ((MainActivity)mListener).setiShowAddBehavoirDialog(this);
            mListener.setHomefragment(this);
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



    @Override
    public void showAddBehavoirDialog() {
        Log.e(TAG, "showAddDeviceBehavoirDialog" );

        AddDeviceDialog.showDialog(getActivity(), null);
    }


    @Override
    public void onStart() {
        super.onStart();

        controller.onStart();
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and fName
        void setHomefragment(HomeFragment fragment);
    }




    public static class MyParcel implements Parcelable{


        private  ArrayList<DeviceModel> devices;

        protected MyParcel(Parcel in) {
            in.readList(devices ,DeviceModel.class.getClassLoader());
        }

        public  MyParcel(ArrayList<DeviceModel>  models){

            this.devices = models;
        }


        public static final Creator<MyParcel> CREATOR = new Creator<MyParcel>() {
            @Override
            public MyParcel createFromParcel(Parcel in) {
                return new MyParcel(in);
            }

            @Override
            public MyParcel[] newArray(int size) {
                return new MyParcel[size];
            }
        };

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {

            dest.writeList(devices);
        }
    }

}
