package com.gp.smarthome.hamdy.smarthomegp;

import android.app.Application;
import android.util.Log;

import com.gp.smarthome.hamdy.smarthomegp.Utilities.SharedPref;


/**
 * Created by hamdy on 16/05/17.
 */

public class SmartHomeApp extends Application {


    public static final String PREF_KEY = "pref_thing_app";
    private static final String TAG = "ThingsApp";
    public User user;

    @Override
    public void onCreate() {
        super.onCreate();

        String userType = SharedPref.getString(getApplicationContext() , "pref_user_type");




        if(userType != null && userType.equals("admin"))
            user = new Admin();
        else
            user = new User();


        user.getUserData(getApplicationContext());


//        user.logout(getApplicationContext());

//        String id = SharedPref.getString(getApplicationContext() , "pref_user_id" );
//        String name =  SharedPref.getString(getApplicationContext() , "pref_user_name");
//        String email = SharedPref.getString(getApplicationContext() , "pref_user_email");
//        String userType = SharedPref.getString(getApplicationContext() , "pref_user_type");
//
//        Log.e(TAG , "id = " + id);
//
//        user = userType.equals("admin") ? new Admin() : new User();
//
//        user.id = id;
//        user.fName = name;
//        user.email = email;
//        user.userType = userType;


    }
}



