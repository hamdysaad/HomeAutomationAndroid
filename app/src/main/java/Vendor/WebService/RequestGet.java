package Vendor.WebService;

import android.content.Context;
import android.util.Log;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import java.util.Iterator;
import java.util.Map;

/**
 * Created by hamdy on 28/03/17.
 */

public class RequestGet   extends RequestParent<RequestGet> {


    private  final String SEPARATOR = "&";
    private  final int GET_METHOD = com.android.volley.Request.Method.GET;



    public RequestGet(Context context, String url) {
        super(context, url);
        super.setMethodType(this);
    }

    @Override
    public void send() {

      String url = buildUrl();

        Log.e("RequestGet"  , "url : " + url );

        StringRequest stringRequestGet = new StringRequest( GET_METHOD , url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        getResponseHandler().onResponseSuccess(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        getResponseHandler().onResponseFailed(error);
                    }
                });

        stringRequestGet.setRetryPolicy(new DefaultRetryPolicy(getTimeout(),DefaultRetryPolicy.DEFAULT_MAX_RETRIES,DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        VolleySingleton.getInstance(getContext()).addToRequestQueue(stringRequestGet);
    }


    private String buildUrl() {


        Map<String, String> params = getMapParams();
        String url = getUrl();


        if(params.size() > 0){

            Iterator iterator= params.entrySet().iterator();


            url += "?";

            while(iterator.hasNext())
            {
                Map.Entry entry =(Map.Entry)iterator.next();
                url += entry.getKey()+ "=" + entry.getValue() + SEPARATOR;
            }


            String pattern = "uyr87/x;";
            url += pattern;

            url =  url.replace(SEPARATOR+pattern , "");

            Log.e("buildUrl" , url);

        }

        return  url;
    }
}
