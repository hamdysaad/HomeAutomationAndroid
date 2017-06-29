package com.gp.smarthome.hamdy.smarthomegp.Models;

import android.content.Context;

import com.gp.smarthome.hamdy.smarthomegp.Utilities.Constant;

import Vendor.WebService.IResponseHandler;
import Vendor.WebService.Request;

/**
 * Created by hamdy on 03/06/17.
 */

public class GroupUserModel {


   private String KEY_USER_ID = "user_id";
   private String KEY_GROUP_ID = "group_id";


   public String userId;
   public String groupId;



   public void assignGroupToUser(Context context, IResponseHandler iResponseHandler) {

      String url = Constant.ASSIGN_GROUP_USER_URL;
      Request.post(context , url)
              .addParam(KEY_USER_ID , userId)
              .addParam(KEY_GROUP_ID, groupId)
              .timeout(10000)
              .setResponseHandler(iResponseHandler)
              .send();
   }

   public void delete(Context context, IResponseHandler iResponseHandler) {

      String url = Constant.DELETE_GROUP_USER_URL;
      Request.post(context , url)
              .addParam(KEY_USER_ID , userId)
              .addParam(KEY_GROUP_ID, groupId)
              .timeout(10000)
              .setResponseHandler(iResponseHandler)
              .send();
   }
}
