package com.gp.smarthome.hamdy.smarthomegp.Fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.gp.smarthome.hamdy.smarthomegp.Activities.MainActivity;
import com.gp.smarthome.hamdy.smarthomegp.Controllers.GroupFragmentController;
import com.gp.smarthome.hamdy.smarthomegp.R;
import com.gp.smarthome.hamdy.smarthomegp.Utilities.AlertHelper;
import com.wang.avi.AVLoadingIndicatorView;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link GroupsFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link GroupsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class GroupsFragment extends Fragment implements IShowAddBehavoirDialog {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;
    public View view;
    public RecyclerView mRecyclerView;
    private String TAG = "GroupsFragment";
    private AVLoadingIndicatorView mAVLoadingIndicatorView;
    private TextView messageTV;
    public GroupFragmentController controller;

    public GroupsFragment() {
        // Required empty public constructor
    }

    public static GroupsFragment newInstance() {
        GroupsFragment fragment = new GroupsFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
         view =  inflater.inflate(R.layout.fragment_groups, container, false);

        mRecyclerView = (RecyclerView) view.findViewById(R.id.my_recycler_view);
        mRecyclerView.setHasFixedSize(true);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);


        messageTV = (TextView) view.findViewById(R.id.messageTV);
        mAVLoadingIndicatorView = (AVLoadingIndicatorView) view.findViewById(R.id.loading_avl);



         controller = new GroupFragmentController(this);


        return view;
    }



    private void showAddGroupFormeDialog() {

        Log.e(TAG , "showAddGroupFormeDialog");
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
            ((MainActivity)mListener).setiShowAddBehavoirDialog(this);
            mListener.setGroupsfragment(this);
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        Log.e(TAG , "onResume");

        ((MainActivity)mListener).setiShowAddBehavoirDialog(this);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void showAddBehavoirDialog() {
        Log.e(TAG , "showAddGroupBehavoirDialog");

        AddGroupDialog.showDialog(getActivity() , null);
    }

    public void displayMessage(String message) {
        AlertHelper.showLongSnackBar(view , message);
        messageTV.setVisibility(View.GONE);
        messageTV.setText(message);
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
        // TODO: Update argument type and name
        void setGroupsfragment(GroupsFragment groupsFragment);
    }

    public void showAnim(boolean show){

        if(show)
            mAVLoadingIndicatorView.show();
        else
            mAVLoadingIndicatorView.hide();
    }
}
