package Vendor.WebService;

import com.android.volley.VolleyError;

/**
 * Created by hamdy on 28/03/17.
 */

public interface IResponseHandler {
    public void onResponseSuccess(String response);
    public void onResponseFailed(VolleyError error);
}
