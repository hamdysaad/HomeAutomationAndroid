package Vendor.WebService;

import android.content.Context;

/**
 * Created by hamdy on 28/03/17.
 */

public class Request {



    public static RequestPost post(Context context , String url){

        return  new RequestPost(context , url);

    }

    public static RequestGet get(Context context , String url){

        return  new RequestGet(context , url);
    }

}
