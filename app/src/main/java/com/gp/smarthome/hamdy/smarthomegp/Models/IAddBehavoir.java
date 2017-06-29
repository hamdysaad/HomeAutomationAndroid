package com.gp.smarthome.hamdy.smarthomegp.Models;

import android.content.Context;

import Vendor.WebService.IResponseHandler;

/**
 * Created by hamdy on 09/06/17.
 */

public interface IAddBehavoir {

    void add(Context context, IResponseHandler iResponseHandler);
    void edit(Context context, IResponseHandler iResponseHandler);
    void delete(Context context, IResponseHandler iResponseHandler);
}
