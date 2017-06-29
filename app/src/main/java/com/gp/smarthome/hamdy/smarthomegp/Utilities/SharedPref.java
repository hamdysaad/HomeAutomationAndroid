package com.gp.smarthome.hamdy.smarthomegp.Utilities;

import android.content.Context;
import android.content.SharedPreferences;

import com.gp.smarthome.hamdy.smarthomegp.SmartHomeApp;

/**
 * Created by Hamdy on 5/7/2017.
 */

public class SharedPref {



    public static void putBoolean(Context context, String key, boolean value){

        SharedPreferences  pref = context.getSharedPreferences(SmartHomeApp.PREF_KEY , Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putBoolean(key, value);
        editor.apply();

    }

    public static boolean getBoolean(Context context, String key){

        SharedPreferences  pref = context.getSharedPreferences(SmartHomeApp.PREF_KEY , Context.MODE_PRIVATE);

        return pref.getBoolean(key , false);

    }


    public static String getString(Context context, String key){

        SharedPreferences  pref = context.getSharedPreferences(SmartHomeApp.PREF_KEY , Context.MODE_PRIVATE);

        return pref.getString(key , null);

    }
    public static void clear(Context context) {

        SharedPreferences  pref = context.getSharedPreferences(SmartHomeApp.PREF_KEY , Context.MODE_PRIVATE);

        pref.edit().clear().apply();

    }

    public static void putString(Context context ,String key, String value) {
        SharedPreferences  pref = context.getSharedPreferences(SmartHomeApp.PREF_KEY , Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(key, value);
        editor.apply();

    }
}
