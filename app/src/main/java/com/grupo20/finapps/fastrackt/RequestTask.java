package com.grupo20.finapps.fastrackt;

/**
 * Created by xavi on 06/11/15.
 */
import android.os.AsyncTask;
import android.util.Log;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONException;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * Created by albert on 06/11/15.
 */
public class RequestTask extends AsyncTask<String, String, String> {

    @Override
    protected String doInBackground(String... uri) {
        HttpClient httpclient = new DefaultHttpClient();
        HttpResponse response;
        String responseString = null;
        try {
            response = httpclient.execute(new HttpGet("http://192.168.10.45:6969/offices"));
            StatusLine statusLine = response.getStatusLine();
            if(statusLine.getStatusCode() == HttpStatus.SC_OK){
                ByteArrayOutputStream out = new ByteArrayOutputStream();
                response.getEntity().writeTo(out);
                responseString = out
                        .toString();
                out.close();
            } else{
                //Closes the connection.
                response.getEntity().getContent().close();
                throw new IOException(statusLine.getReasonPhrase());
            }
        } catch (ClientProtocolException e) {
            Log.i("RequestTask", "Client protocol exception");
        } catch (IOException e) {
            Log.i("RequestTask", "IOException");
        }
        return responseString;
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);

        if (result == null) Log.d("GET RESULTS", "is null");

        //Log.d("Get results", result);

        try {
            JSONObject jsonObj = new JSONObject(result);
            JSONArray results = jsonObj.getJSONArray("results");
            JSONObject first_office = results.getJSONObject(0);
            Log.d("get results", (String) first_office.get("name"));

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}
