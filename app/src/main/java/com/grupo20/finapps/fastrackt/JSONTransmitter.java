package com.grupo20.finapps.fastrackt;

import android.os.AsyncTask;
import android.util.Log;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * Created by xavi on 07/11/15.
 */
public class JSONTransmitter extends AsyncTask<JSONObject, JSONObject, JSONObject> {

    private OnJsonTransmitionCompleted mCallback;

    public JSONTransmitter(OnJsonTransmitionCompleted callback) {
        this.mCallback = callback;
    }

    @Override
    protected JSONObject doInBackground(JSONObject... data) {
        //.....
        HttpClient httpclient = new DefaultHttpClient();
        HttpResponse response;
        String responseString = null;
        try {
            response = httpclient.execute(new HttpGet("http://192.168.10.45:6969/offices"));
            StatusLine statusLine = response.getStatusLine();
            if (statusLine.getStatusCode() == HttpStatus.SC_OK) {
                ByteArrayOutputStream out = new ByteArrayOutputStream();
                response.getEntity().writeTo(out);
                responseString = out
                        .toString();
                out.close();
            } else {
                //Closes the connection.
                response.getEntity().getContent().close();
                throw new IOException(statusLine.getReasonPhrase());
            }
        } catch (ClientProtocolException e) {
            Log.i("RequestTask", "Client protocol exception");
        } catch (IOException e) {
            Log.i("RequestTask", "IOException");
        }

        JSONObject json = null;
        try {
            json = new JSONObject(responseString);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return json;
    }

    @Override
    protected void onPostExecute(JSONObject jsonObject) {
        super.onPostExecute(jsonObject);
        this.mCallback.onTransmitionCompleted(jsonObject);
    }

    public interface OnJsonTransmitionCompleted {
        void onTransmitionCompleted(JSONObject jsonObject);
    }
}
