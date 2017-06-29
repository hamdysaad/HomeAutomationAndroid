package Vendor.WebService;

import android.content.Context;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import java.util.Collections;
import java.util.Map;

/**
 * Created by hamdy on 28/03/17.
 */

public class RequestPost extends RequestParent<RequestPost> {


    private final int POST_METHOD = com.android.volley.Request.Method.POST;
    String TAG = "RequestPost";

    public RequestPost(Context context, String url) {
        super(context, url);
        super.setMethodType(this);
    }


    @Override
    public void send() {

        Log.e(TAG , "url : " + getUrl());

        StringRequestPost stringRequestPost = new StringRequestPost(POST_METHOD, getUrl(),
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

        stringRequestPost.setRetryPolicy(new DefaultRetryPolicy(getTimeout(),DefaultRetryPolicy.DEFAULT_MAX_RETRIES,DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        VolleySingleton.getInstance(getContext()).addToRequestQueue(stringRequestPost);

    }

    private class StringRequestPost extends StringRequest {

        public StringRequestPost(int method, String url, Response.Listener<String> listener, Response.ErrorListener errorListener) {
            super(method, url, listener, errorListener);
        }


        @Override
        protected Map<String, String> getParams() throws AuthFailureError {

            Log.e(TAG , "params : " + getMapParams().toString());

            return getMapParams();
        }

        @Override
        protected Response<String> parseNetworkResponse(NetworkResponse response) {

            Log.e(TAG , response.toString());
            if (response.headers == null)
            {
                // cant just set a new empty map because the member is final.
                response = new NetworkResponse(
                        response.statusCode,
                        response.data,
                        Collections.<String, String>emptyMap(), // this is the important line, set an empty but non-null map.
                        response.notModified,
                        response.networkTimeMs);


            }

            return super.parseNetworkResponse(response);

        }
    }

}
