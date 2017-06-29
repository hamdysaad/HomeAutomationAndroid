package com.gp.smarthome.hamdy.smarthomegp.Models;

import android.content.Context;

import com.gp.smarthome.hamdy.smarthomegp.User;
import com.gp.smarthome.hamdy.smarthomegp.Utilities.Constant;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import Vendor.WebService.IResponseHandler;
import Vendor.WebService.Request;

/**
 * Created by hamdy on 09/06/17.
 */

public class GroupModel implements IAddBehavoir  {

    public String id;
    public String name;
    public String description;
    public String created_at;
    public ArrayList<DeviceModel> devices;
    public ArrayList<User> users;


    private String KEY_ID = "id";
    private String KEy_NAME = "name";
    private String KEY_DESC = "desc";


    public ArrayList<GroupModel> parserResponse(JSONObject data) throws JSONException {

        ArrayList<GroupModel> models = new ArrayList<>();

        JSONArray list = data.getJSONArray("list");

        for(int i = 0 ; i < list.length() ; i++){

            GroupModel model = new GroupModel();

            JSONObject object = list.getJSONObject(i);

            JSONObject groupJO = object.getJSONObject("group");
            JSONArray devicesJA = object.getJSONArray("devices");
            JSONArray usersJA = object.getJSONArray("users");

            model.id = groupJO.getString("id");
            model.name = groupJO.getString("name");
            model.description = groupJO.getString("description");
            model.created_at = groupJO.getString("created_at");

            DeviceModel device = new DeviceModel();
            model.devices = device.parseDevicesJA(devicesJA);

            User user = new User();
            model.users = user.parseUsersJA(usersJA);

            models.add(model);


        }


        return models;

    }

    @Override
    public void add(Context context, IResponseHandler iResponseHandler) {

        String url = Constant.ADD_GROUP_URL;

        Request.post(context , url)
                .addParam(KEy_NAME, name)
                .addParam(KEY_DESC , description)
                .setResponseHandler(iResponseHandler)
                .timeout(10000)
                .send();
    }

    @Override
    public void edit(Context context, IResponseHandler iResponseHandler) {

        String url = Constant.EDIT_GROUP_URL;

        Request.post(context , url)
                .addParam(KEY_ID , id)
                .addParam(KEy_NAME, name)
                .addParam(KEY_DESC , description)
                .setResponseHandler(iResponseHandler)
                .timeout(10000)
                .send();
    }

    @Override
    public void delete(Context context, IResponseHandler iResponseHandler) {

        String url = Constant.DELETE_GROUP_URL + id;
        Request.get(context , url)
                .timeout(10000)
                .setResponseHandler(iResponseHandler)
                .send();
    }


    public ArrayList<GroupModel> parseGroupsJA(JSONArray groupsJA) throws JSONException {

        ArrayList<GroupModel> models = new ArrayList<>();

        for (int i =0 ; i < groupsJA.length() ; i++){

            GroupModel model = new GroupModel();

            if(groupsJA.getJSONObject(i).has("group")) {
                JSONObject groupJO = groupsJA.getJSONObject(i).getJSONObject("group");
                model.parseGroupJO(groupJO);

            }
            else{
                model.parseGroupJO(groupsJA.getJSONObject(i));
            }

            if(groupsJA.getJSONObject(i).has("devices")) {
                JSONArray devicesJA = groupsJA.getJSONObject(i).getJSONArray("devices");
                DeviceModel deviceModel = new DeviceModel();
                model.devices = deviceModel.parseDevicesJA(devicesJA);
            }

            if(groupsJA.getJSONObject(i).has("users")) {
                JSONArray usersJA = groupsJA.getJSONObject(i).getJSONArray("users");
                User user = new User();
                model.users = user.parseUsersJA(usersJA);
            }


            models.add(model);
        }

        return models;
    }

    private void parseGroupJO(JSONObject groupJO) throws JSONException {

        id = groupJO.getString("id");
        name = groupJO.getString("name");
        description = groupJO.getString("description");
        created_at = groupJO.getString("created_at");
    }


    public void getGroupDevices(Context context, IResponseHandler iResponseHandler) {

        String url = Constant.GET_GROUP_DEVICES + id;
        Request.get(context , url)
                .setResponseHandler(iResponseHandler)
                .timeout(10000)
                .send();
    }

    public void getDevicesNotForGroup(Context context, IResponseHandler iResponseHandler) {

        String url = Constant.GET_DEVICES_NOT_FOR_GROUP + id ;

        Request.get(context , url)
                .setResponseHandler(iResponseHandler)
                .timeout(10000)
                .send();
    }


    public void getGroupUsers(Context context, IResponseHandler iResponseHandler) {

        String url = Constant.GET_GROUP_USERS + id;
        Request.get(context , url)
                .setResponseHandler(iResponseHandler)
                .timeout(10000)
                .send();
    }

    public void getUsersNotForGroup(Context context, IResponseHandler iResponseHandler) {

        String url = Constant.GET_USERS_NOT_FOR_GROUP + id ;

        Request.get(context , url)
                .setResponseHandler(iResponseHandler)
                .timeout(10000)
                .send();

    }
}
