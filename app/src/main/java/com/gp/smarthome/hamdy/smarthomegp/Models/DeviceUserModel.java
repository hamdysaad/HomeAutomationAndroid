package com.gp.smarthome.hamdy.smarthomegp.Models;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.gp.smarthome.hamdy.smarthomegp.R;
import com.gp.smarthome.hamdy.smarthomegp.Utilities.Constant;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import Vendor.WebService.IResponseHandler;
import Vendor.WebService.Request;

/**
 * Created by hamdy on 03/06/17.
 */

public class DeviceUserModel {


   private String KEY_USER_ID = "user_id";
   private String KEY_DEVICE_ID = "device_id";


   public String userId;
   public String deviceId;



   public void assignDeviceToUser(Context context, IResponseHandler iResponseHandler) {

      String url = Constant.ASSIGN_DEVICE_URL;
      Request.post(context , url)
              .addParam(KEY_USER_ID , userId)
              .addParam(KEY_DEVICE_ID , deviceId)
              .timeout(10000)
              .setResponseHandler(iResponseHandler)
              .send();
   }

   public void delete(Context context, IResponseHandler iResponseHandler) {

      String url = Constant.DELETE_DEVICE_USER_URL;
      Request.post(context , url)
              .addParam(KEY_USER_ID , userId)
              .addParam(KEY_DEVICE_ID , deviceId)
              .timeout(10000)
              .setResponseHandler(iResponseHandler)
              .send();
   }
}
