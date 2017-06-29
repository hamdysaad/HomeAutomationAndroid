package com.gp.smarthome.hamdy.smarthomegp.Models;

import android.content.Context;

import com.gp.smarthome.hamdy.smarthomegp.Utilities.Constant;

import Vendor.WebService.IResponseHandler;
import Vendor.WebService.Request;

/**
 * Created by hamdy on 03/06/17.
 */

public class DeviceGroupModel {


   private String KEY_GROUP_ID = "group_id";
   private String KEY_DEVICE_ID = "device_id";


   public String groupId;
   public String deviceId;



   public void assignDeviceToGroup(Context context, IResponseHandler iResponseHandler) {

      String url = Constant.ASSIGN_DEVICE_TO_GROUP_URL;
      Request.post(context , url)
              .addParam(KEY_GROUP_ID , groupId)
              .addParam(KEY_DEVICE_ID , deviceId)
              .timeout(10000)
              .setResponseHandler(iResponseHandler)
              .send();
   }

   public void delete(Context context, IResponseHandler iResponseHandler) {

      String url = Constant.DELETE_DEVICE_GROUP_URL;
      Request.post(context , url)
              .addParam(KEY_GROUP_ID , groupId)
              .addParam(KEY_DEVICE_ID , deviceId)
              .timeout(10000)
              .setResponseHandler(iResponseHandler)
              .send();
   }
}
