package com.gp.smarthome.hamdy.smarthomegp;

import android.content.Context;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;


import com.gp.smarthome.hamdy.smarthomegp.Activities.LoginActivity;
import com.gp.smarthome.hamdy.smarthomegp.Models.GroupModel;
import com.gp.smarthome.hamdy.smarthomegp.Utilities.Constant;
import com.gp.smarthome.hamdy.smarthomegp.Utilities.SharedPref;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import Vendor.Authentication.Authentication;
import Vendor.WebService.IResponseHandler;
import Vendor.WebService.Request;

/**
 * Created by hamdy on 16/05/17.
 */

public class User {

    public final int TIMEOUT = 10000;

    public String id;
    public String fName;
    public String lName;
    public String email;
    public String userType;


    public boolean isLoggedIn(Context context) {
        return SharedPref.getBoolean(context , Authentication.PREF_AUTH);
    }

    public void logout(Context context) {
        SharedPref.clear(context);
        LoginActivity.start(context);
    }


    public void getAllDevice(Context context , IResponseHandler iResponseHandler) {


        ProgressBar progressBar = new ProgressBar(context);

        progressBar.setVisibility(View.VISIBLE);
        progressBar.setClickable(false);

        String url = userType.equals("admin") ? Constant.DEVICES_ALL_LIST_URL  : Constant.DEVICES_LIST_URL + id;
        Request.get(context, url)
                .setResponseHandler(iResponseHandler)
                .timeout(TIMEOUT)
                .send();

    }

    public void getGroups(Context context , IResponseHandler iResponseHandler) {


        String url = userType.equals("admin") ? Constant.GROUPS_ALL_LIST_URL : Constant.GROUPS_LIST_URL + id;
        Request.get(context, url)
                .setResponseHandler(iResponseHandler)
                .timeout(TIMEOUT)
                .send();

    }


    public void getUserData(Context context) {

        String id = SharedPref.getString(context , "pref_user_id" );
        String fName =  SharedPref.getString(context , "pref_f_user_name");
        String lName =  SharedPref.getString(context , "pref_l_user_name");
        String email = SharedPref.getString(context , "pref_user_email");
        String userType = SharedPref.getString(context , "pref_user_type");

        this.id = id;
        this.fName = fName;
        this.lName = lName;
        this.email = email;
        this.userType = userType;

    }

    public void setUserData(Context context) {

        SharedPref.putString(context , "pref_user_id" , id );
        SharedPref.putString(context , "pref_f_user_name" , fName);
        SharedPref.putString(context , "pref_l_user_name" , lName);
        SharedPref.putString(context , "pref_user_email" , email );
        SharedPref.putString(context , "pref_user_type" , userType );

    }

    public boolean isAdmin() {

        return userType.equals("admin");
    }

    public HashMap<String, String[]> parserUsersList(JSONObject data) throws JSONException {

        HashMap<String , String[]> users = new HashMap<>();

        String names[] = new String[data.length()];
        String ids[] = new String[data.length()];


        JSONArray usersJA = data.getJSONArray("users");

        for (int i = 0 ; i < usersJA.length() ; i++){

            User user = parseUserJO(usersJA.getJSONObject(i));

            ids[i] = user.id;
            names[i] = user.fName + " " + user.lName;
        }


        users.put("ids" , ids);
        users.put("names" , names);


        return users;

    }


    public User parseUserJO(JSONObject jsonObject) throws JSONException {

        User user = new User();

        id = jsonObject.getString("id");
        fName = jsonObject.getString("name");
        lName = jsonObject.getString("L_name");
        userType = jsonObject.getString("user_type");
        email = jsonObject.getString("email");

        return user;

    }

    public ArrayList<User> parseUsersJA(JSONArray usersJA) throws JSONException {

        ArrayList<User> models = new ArrayList<>();

        for (int i =0 ; i < usersJA.length() ; i++){

            JSONObject userJO = usersJA.getJSONObject(i);

            User model = new User();

            model.parserUserJO(userJO);

            models.add(model);
        }

        return models;
    }

    private void parserUserJO(JSONObject userJO) throws JSONException {

        id = userJO.getString("id");
        fName = userJO.getString("name");
        lName = userJO.getString("L_name");
        email = userJO.getString("email");
        userType = userJO.getString("user_type");
    }
}
