package com.gp.smarthome.hamdy.smarthomegp;

import android.content.Context;
import android.view.View;
import android.widget.ProgressBar;

import com.gp.smarthome.hamdy.smarthomegp.Controllers.AddDeviceDialogController;
import com.gp.smarthome.hamdy.smarthomegp.Models.IAddBehavoir;
import com.gp.smarthome.hamdy.smarthomegp.Utilities.Constant;

import Vendor.WebService.IResponseHandler;
import Vendor.WebService.Request;

/**
 * Created by hamdy on 09/06/17.
 */

public class Admin extends User {

    private IAddBehavoir iAddBehavoir;


    public void getAllUsers(Context context , IResponseHandler iResponseHandler) {


        String url = Constant.USERS_ALL_LIST_URL;
        Request.get(context, url)
                .setResponseHandler(iResponseHandler)
                .timeout(TIMEOUT)
                .send();

    }


    public void add(Context context, IResponseHandler iResponseHandler){
        iAddBehavoir.add(context , iResponseHandler );
    }

    public IAddBehavoir getiAddBehavoir() {
        return iAddBehavoir;
    }

    public void setiAddBehavoir(IAddBehavoir iAddBehavoir) {
        this.iAddBehavoir = iAddBehavoir;
    }

    public void edit(Context context, IResponseHandler iResponseHandler ) {
        iAddBehavoir.edit(context , iResponseHandler );
    }

    public void delete(Context context, IResponseHandler iResponseHandler ) {
        iAddBehavoir.delete(context , iResponseHandler );
    }
}
