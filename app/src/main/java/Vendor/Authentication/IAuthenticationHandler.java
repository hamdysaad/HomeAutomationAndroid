package Vendor.Authentication;

import com.android.volley.VolleyError;

import org.json.JSONObject;

/**
 * Created by Hamdy on 4/23/2017.
 */

public interface IAuthenticationHandler {

    void onAuthSuccessfull(JSONObject user);
    void onAuthFailed(int message);
    void onAuthResponseFailed(VolleyError error);


}
