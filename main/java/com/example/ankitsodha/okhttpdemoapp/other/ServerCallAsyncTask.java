package demo.jaliyaninfotech.com.hospital;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.widget.Toast;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import demo.jaliyaninfotech.com.hospital.hospital.HospitalProfileFragment;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by Ankit Sodha on 21-Oct-16.
 */

public class ServerCallAsyncTask extends AsyncTask<String,Void,String> {

    Context context;
    String url = "";
    JSONObject jsonObject;
    OnAsyncJSONResponse caller;
    HashMap<String, String> hashMap=null;
    ProgressDialog pDialog = null;
    int flag;

    public ServerCallAsyncTask(Fragment hospitalProfileFragment,
                               FragmentActivity activity, String url,
                               HashMap<String, String> hashMap,
                               int flag) {
        caller = (OnAsyncJSONResponse) hospitalProfileFragment;
        context = activity;
        this.url = url;
        this.hashMap=hashMap;
        this.flag=flag;
    }

    public interface OnAsyncJSONResponse {
        void asyncGetSMSResponse(String response,int flag);
    }

    public ServerCallAsyncTask(Activity activity, String url, HashMap<String, String> hashMap) {
        caller = (OnAsyncJSONResponse) activity;
        context = activity;
        this.url = url;
        this.hashMap=hashMap;
    }


    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        pDialog=new ProgressDialog(context);
        pDialog.setMessage("Please wait a moment...");
        pDialog.show();
    }

    @Override
    protected String doInBackground(String[] params) {
        String response=getJsonFromUrl();
        return response;
    }

    @Override
    protected void onPostExecute(String response) {
        super.onPostExecute(response);
        if (pDialog != null && pDialog.isShowing()) {
            pDialog.dismiss();
        }
        caller.asyncGetSMSResponse(response,flag);
    }

    public String getJsonFromUrl()
    {
        try {

            OkHttpClient client = new OkHttpClient();
            client.connectTimeoutMillis(); // connect timeout
            client.readTimeoutMillis();
            //client.readTimeout(30, TimeUnit.SECONDS);

            Request.Builder builder=new Request.Builder().url(url);
            if(hashMap!=null) {
                FormBody.Builder formBodyBuilder = new FormBody.Builder();
                // Add Params to Builder
                for ( Map.Entry<String, String> entry : hashMap.entrySet() ) {
                    formBodyBuilder.add( entry.getKey(), entry.getValue() );
                }
                RequestBody formBody = formBodyBuilder.build();
                builder.post(formBody);
            }
            Request request = builder.build();
            Response response = client.newCall(request).execute();
            return response.body().string();
        }
        catch (Exception ex)
        {
            return ex.toString();
        }
    }
    public static boolean isNetworkConnected(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null;
    }
    public static void showToast(Context context,String msg)
    {
        if(msg==null)
            msg="No Internet Connection!!!";
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }
}
