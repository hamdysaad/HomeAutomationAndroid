package com.gp.smarthome.hamdy.smarthomegp.Models;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.gp.smarthome.hamdy.smarthomegp.Controllers.DeviceDetailsDialogController;
import com.gp.smarthome.hamdy.smarthomegp.R;
import com.gp.smarthome.hamdy.smarthomegp.User;
import com.gp.smarthome.hamdy.smarthomegp.Utilities.Constant;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import Vendor.WebService.IResponseHandler;
import Vendor.WebService.Request;
import Vendor.WebService.RequestPost;

/**
 * Created by hamdy on 03/06/17.
 */

public class DeviceModel implements IAddBehavoir {
   //`id`, `fName`, `adminId`, `description`, `current_state`, `created_at`, `updated_at`

   public String id;
   public String name;
   public String category;
   public String imageUrl;
   public ArrayList<User> users;
   public ArrayList<GroupModel> groups;
   public String adminId;
   public String description;
   public String currentState;
   public String createdAt;
   public String updatedAt;


   private String KEY_ADMIN_ID = "admin_id";
   private String KEY_NAME = "name";
   private String KEY_desc = "desc";
   private String KEY_category = "category";
   private String KEY_status = "status";
   private String KEY_ID = "id";

   private void parseDeviceJO(JSONObject o) {


      try {
         id = o.getString("id");
         name = o.getString("name");
         category = o.getString("category");
         description = o.getString("description");
         currentState = o.getString("current_state");
         createdAt = o.getString("created_at");
         updatedAt = o.getString("updated_at");
      } catch (JSONException e) {
         e.printStackTrace();
      }
   }

   public Bitmap getImageDrawable(Context context) {


      Bitmap bitmap;

      switch (category){

         case "fridge" :
             bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.fridge);
            break;
         case "bulb" :
            bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.bulb);
            break;
         case "fan" :
            bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.fan);
            break;
         case "tv" :
            bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.television);
            break;
         default:
            bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.devices);
            break;
      }
      return bitmap;


   }

   @Override
   public void add(Context context, IResponseHandler iResponseHandler) {


      String url = Constant.ADD_DEVICE_URL;
       RequestPost request = Request.post(context, url)
              .addParam(KEY_ADMIN_ID, adminId)
              .addParam(KEY_NAME, name)
              .addParam(KEY_desc, description)
              .addParam(KEY_status, currentState)
              .timeout(10000)
              .setResponseHandler(iResponseHandler);


      if(!category.equals(""))
           request.addParam(KEY_category, category);


      request.send();



   }

   @Override
   public void edit(Context context, IResponseHandler iResponseHandler) {

      String url = Constant.EDIT_DEVICE_URL;
      Request.post(context , url)
              .addParam(KEY_ID , id)
              .addParam(KEY_ADMIN_ID , adminId)
              .addParam(KEY_NAME , name)
              .addParam(KEY_desc , description)
              .addParam(KEY_category , category)
              .addParam(KEY_status , currentState)
              .timeout(10000)
              .setResponseHandler(iResponseHandler)
              .send();
   }

   @Override
   public void delete(Context context, IResponseHandler iResponseHandler) {

      String url = Constant.DELETE_DEVICE_URL + id;
      Request.get(context , url)
              .timeout(10000)
              .setResponseHandler(iResponseHandler)
              .send();

   }

   public void getDeviceUsers(Context context, IResponseHandler iResponseHandler) {

      String url = Constant.GET_DEVICE_USERS + id;

      Request.get(context , url)
              .setResponseHandler(iResponseHandler)
              .timeout(10000)
              .send();
   }

   public void getUsersNotForDevice(Context context, IResponseHandler iResponseHandler) {

      String url = Constant.GET_USERS_NOT_FOR_DEVICE + id;

      Request.get(context , url)
              .setResponseHandler(iResponseHandler)
              .timeout(10000)
              .send();
   }

   public ArrayList<DeviceModel> parseDevicesJA(JSONArray listJA) throws JSONException {

      ArrayList<DeviceModel> models = new ArrayList<>();

      for (int i = 0 ; i < listJA.length() ; i++) {

         JSONObject listJO = listJA.getJSONObject(i);

         DeviceModel deviceModel = new DeviceModel();

         if(listJO.has("device")) {
            JSONObject deviceJO = listJO.getJSONObject("device");
            deviceModel.parseDeviceJO(deviceJO);

         }else
            deviceModel.parseDeviceJO(listJA.getJSONObject(i));


         if(listJO.has("groups")) {
            GroupModel groupModel = new GroupModel();
            JSONArray groupsJA = listJO.getJSONArray("groups");
            deviceModel.groups = groupModel.parseGroupsJA(groupsJA);


         }

         if(listJO.has("users")) {
            User user = new User();
            JSONArray usersJA = listJO.getJSONArray("users");
            deviceModel.users = user.parseUsersJA(usersJA);


         }

         models.add(deviceModel);
      }

      return models;

   }

   public void getDeviceGroups(Context context, IResponseHandler iResponseHandler) {

      String url = Constant.GET_DEVICE_GROUPS + id;
      Request.get(context , url)
              .setResponseHandler(iResponseHandler)
              .timeout(10000)
              .send();
   }

   public void getGroupsNotForDevice(Context context, IResponseHandler iResponseHandler) {

      String url = Constant.GET_GROUPS_NOT_FOR_DEVICE + id ;

      Request.get(context , url)
              .setResponseHandler(iResponseHandler)
              .timeout(10000)
              .send();
   }

    public void controlDevice(Context context, IResponseHandler iResponseHandler) {


        //https://flow-app.eu-gb.mybluemix.net//device_control

        String url = Constant.CONTROL_DEVICE_URL;
        Request.post(context , url)
                .addParam("device_id" , id)
                .addParam("device_state", currentState)
                .timeout(10000)
                .setResponseHandler(iResponseHandler)
                .send();


    }
}
