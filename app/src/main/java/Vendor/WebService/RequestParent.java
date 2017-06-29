package Vendor.WebService;

import android.content.Context;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by hamdy on 29/03/17.
 */

abstract class RequestParent<MethodType> {

    private MethodType methodType;
    private Context context;
    private String url;
    private Map<String, String> params;
    private IResponseHandler responseHandler;
    private int timeout = 3000;


    public RequestParent(Context context , String url) {

        this.url = url;
        this.context = context;
        this.params = new HashMap<>();


    }


    public void setMethodType(MethodType methodType) {
        this.methodType = methodType;
    }

    protected Context getContext() {
        return context;
    }


    protected String getUrl() {
        return url;
    }


    protected Map<String, String> getMapParams() {
        return params;
    }

    protected IResponseHandler getResponseHandler() {
        return responseHandler;
    }

    public MethodType setResponseHandler(IResponseHandler responseHandler) {
        this.responseHandler = responseHandler;
        return methodType;

    }

    protected int getTimeout() {
        return timeout;
    }

    public MethodType timeout(int timeout) {
        this.timeout = timeout;
        return methodType;
    }

    public MethodType addParam(String key, String value) {
        params.put(key, value);

        return  methodType;

    }

    public abstract void send();
}
