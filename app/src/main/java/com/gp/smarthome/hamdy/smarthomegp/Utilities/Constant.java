package com.gp.smarthome.hamdy.smarthomegp.Utilities;

/**
 * Created by hamdy on 16/05/17.
 */

public class Constant {


    public static final String IPADDRESS = "192.168.43.169";
    public static final String BASE_URL = "http://" + IPADDRESS + "/";
//    public static final String BASE_FOLDER_URL = BASE_URL + "projects/gp/homeAutomation/public/api/";
    public static final String BASE_FOLDER_URL = "https://web-cotroller.eu-gb.mybluemix.net/api/";
    public static final String LOGIN_URL = BASE_FOLDER_URL + "login";

    public static final String DEVICES_LIST_URL = BASE_FOLDER_URL + "getDevices/";;
    public static final String GROUPS_LIST_URL = BASE_FOLDER_URL + "getGroups/";
    public static final String ADD_DEVICE_URL =  BASE_FOLDER_URL + "addDevice";
    public static final String ADD_GROUP_URL =  BASE_FOLDER_URL + "addGroup";
    public static final String DEVICES_ALL_LIST_URL = BASE_FOLDER_URL + "getAllDevices";
    public static final String GROUPS_ALL_LIST_URL = BASE_FOLDER_URL + "getAllGroups";
    public static final String EDIT_DEVICE_URL = BASE_FOLDER_URL + "editDevice";
    public static final String DELETE_DEVICE_URL = BASE_FOLDER_URL + "deleteDevice/";

    public static final String EDIT_GROUP_URL = BASE_FOLDER_URL + "editGroup";
    public static final String DELETE_GROUP_URL = BASE_FOLDER_URL + "deleteGroup/";
    public static final String USERS_ALL_LIST_URL = BASE_FOLDER_URL + "getUsers";
    public static final String ASSIGN_DEVICE_URL = BASE_FOLDER_URL + "assignDeviceToUser";

    public static final String GET_DEVICE_USERS = BASE_FOLDER_URL + "getDeviceUsers/";
    public static final String GET_DEVICE_GROUPS = BASE_FOLDER_URL + "getDeviceGroups/" ;
    public static final String GET_USERS_NOT_FOR_DEVICE = BASE_FOLDER_URL + "getUsersNotForDevice/";
    public static final String GET_GROUPS_NOT_FOR_DEVICE = BASE_FOLDER_URL + "getGroupsNotForDevice/";

    public static final String DELETE_DEVICE_USER_URL = BASE_FOLDER_URL + "deleteDeviceUser";
    public static final String ASSIGN_DEVICE_TO_GROUP_URL = BASE_FOLDER_URL + "assignDeviceToGroup";
    public static final String DELETE_DEVICE_GROUP_URL = BASE_FOLDER_URL + "deleteDeviceGroup" ;
    public static final String GET_GROUP_DEVICES = BASE_FOLDER_URL + "getGroupDevices/";
    public static final String GET_DEVICES_NOT_FOR_GROUP = BASE_FOLDER_URL + "getDevicesNotForGroup/";
    public static final String GET_GROUP_USERS = BASE_FOLDER_URL + "getGroupUsers/";
    public static final String GET_USERS_NOT_FOR_GROUP = BASE_FOLDER_URL + "getUsersNotForGroup/";
    public static final String ASSIGN_GROUP_USER_URL = BASE_FOLDER_URL + "assignUserToGroup";
    public static final String DELETE_GROUP_USER_URL = BASE_FOLDER_URL + "deleteGroupUser";
    public static final String GET_ALL_NOTIFICATIONS_URL = BASE_FOLDER_URL + "getNotifications" ;
    public static final String DELETE_NOTIFICATIONS_URL = BASE_FOLDER_URL + "deleteNotification/";
    public static final String CONTROL_DEVICE_URL = "https://flow-app.eu-gb.mybluemix.net/device_control";
}
