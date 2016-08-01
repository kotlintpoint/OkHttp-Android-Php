package com.example.ankitsodha.okhttpdemoapp.other;

import android.content.Context;
import android.net.ConnectivityManager;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by Ankit Sodha on 29-Jul-16.
 */
public class ServerCall {

    public static String getJsonFromUrl(String url, HashMap<String, String> hashMap)
    {
        try {

            OkHttpClient client = new OkHttpClient();
            client.connectTimeoutMillis(); // connect timeout
            client.readTimeoutMillis();
            //client.readTimeout(30, TimeUnit.SECONDS);

            Request.Builder builder=new Request.Builder().url(url);
            if(hashMap!=null) {
				/*FormEncodingBuilder formEncodingBuilder = new FormEncodingBuilder();
				Set<String> keyset=hashMap.keySet();
				for (String key:keyset) {
					formEncodingBuilder.add(key, hashMap.get(key));
				}
				RequestBody formBody = formEncodingBuilder.build();*/
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
}
