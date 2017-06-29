package com.gp.smarthome.hamdy.smarthomegp.Models;

import android.content.Context;

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

public class NotificationModel {


   public String id;
   public String text;
   public String timestamp;



   public void getAllNofications(Context context, IResponseHandler iResponseHandler) {

      String url = Constant.GET_ALL_NOTIFICATIONS_URL;
      Request.get(context , url)
              .timeout(10000)
              .setResponseHandler(iResponseHandler)
              .send();
   }

   public void parseNotificationsJA(JSONArray notificationsJA, ArrayList<NotificationModel> data) throws JSONException {

      data.clear();


      for (int i= 0 ; i < notificationsJA.length() ; i++){
         JSONObject notificationsJO = notificationsJA.getJSONObject(i);

         NotificationModel model = new NotificationModel();

         model.parseNotificationJO(notificationsJO);

         data.add(model);
      }


   }

   private void parseNotificationJO(JSONObject notificationsJO) throws JSONException {

      id = notificationsJO.getString("id");
      text = notificationsJO.getString("details");
      timestamp = notificationsJO.getString("created_at");

   }

   public void delete(Context context, IResponseHandler iResponseHandler) {

      String url = Constant.DELETE_NOTIFICATIONS_URL + id;
      Request.get(context , url)
              .timeout(10000)
              .setResponseHandler(iResponseHandler)
              .send();
   }
}
