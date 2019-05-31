package loop.tvapp.framework;

import android.content.Context;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

import loop.tvapp.utilities.Contants;


/**
 * Created by lalit on 7/25/2017.
 */
public class ServiceCaller {
    Context context;

    public ServiceCaller(Context context) {
        this.context = context;
    }

    //    call All login data
    public void callLoginService(final String username, final String password, final IAsyncWorkCompletedCallback workCompletedCallback) {
//        final String url = "http://dnexusapi.veteransoftwares.com/api/values";
        final String url = "http://dnexusapi.veteransoftwares.com/api/values?tvcode="+username+"&pass="+password;
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                workCompletedCallback.onDone(response, true);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
//                error.printStackTrace();
                workCompletedCallback.onDone(error.getMessage(), false);
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
//                params.put("tvcode", username);
//                params.put("pass", password);
                return params;
            }
        };

//        RequestQueue requestQueue = Volley.newRequestQueue(context);
//        requestQueue.add(stringRequest);

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(20000, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppController.getInstance().addToRequestQueue(stringRequest);//, tag_json_obj);
    }

//    call video player data
    public void callVideoPlayer(int code,final IAsyncWorkCompletedCallback workCompletedCallback){
        final String url="http://dnexusapi.veteransoftwares.com/api/alldata/?tvcode="+code;
        StringRequest stringRequest=new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                workCompletedCallback.onDone(response, true);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                workCompletedCallback.onDone(error.getMessage(), false);
            }
        });

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(20000, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppController.getInstance().addToRequestQueue(stringRequest);//, tag_json_obj);

    }



}
