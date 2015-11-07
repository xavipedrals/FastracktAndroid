package com.grupo20.finapps.fastrackt;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Looper;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.protocol.HTTP;

import java.io.InputStream;

/**
 * Created by xavi on 07/11/15.
 */
public class DialogImport extends DialogFragment {

    private static final String SERVER_URL_CASH = "http://192.168.10.45:6969/withdrawCash";

    private EditText mEditText;
    private ImageView okImg, cancelImg;
    private TextView lastBankTV;
    private SharedPreferences prefs;
    private String importe;

    private Toast toast = null;

    public DialogImport() {
        // Empty constructor required for DialogFragment
    }

    public boolean isValidWord(String w) {
        return w.matches("[A-Za-z]*");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_import, container);
        mEditText = (EditText) view.findViewById(R.id.importEditText);
        okImg = (ImageView) view.findViewById(R.id.imageViewBOk);
        cancelImg = (ImageView) view.findViewById(R.id.imageViewBCancel);
        lastBankTV = (TextView) view.findViewById(R.id.textViewLastBank);

        prefs = getActivity().getSharedPreferences("user", Context.MODE_PRIVATE);
        String banktName = prefs.getString("lastBankClicked", "Bank");
        lastBankTV.setText(banktName);

        mEditText.requestFocus();
        getDialog().getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);

        okImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                importe = mEditText.getText().toString();
                sendDataToServer(importe);
                //startActivity(new Intent(getContext(), Deadline.class));
            }
        });

        cancelImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        getDialog().setTitle("Type an Import");
        getDialog().setCancelable(true);
        return view;
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void onDataCorrect() {
        mostraToast("Correcte");

        //startActivity(new Intent(LoginActivity.this, MapsActivity.class));
    }


    private void mostraToast(String txt) {
        if (toast != null) toast.cancel();
        toast = Toast.makeText(getActivity().getApplicationContext(),
                txt,
                Toast.LENGTH_SHORT);
        toast.show();
    }

    protected void sendJson(final String json) {
        Thread t = new Thread() {

            public void run() {
                Looper.prepare(); //For Preparing Message Pool for the child Thread
                HttpClient client = new DefaultHttpClient();
                HttpConnectionParams.setConnectionTimeout(client.getParams(), 10000); //Timeout Limit
                HttpResponse response;

                try {
                    HttpPost post = new HttpPost(SERVER_URL_CASH);
                    StringEntity se = new StringEntity(json);
                    se.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
                    post.setEntity(se);
                    response = client.execute(post);

                    /*Checking response */
                    if(response!=null){
                        InputStream in = response.getEntity().getContent(); //Get the data in the entity
                        String responseString = IOUtils.toString(in, "UTF-8");
                        Log.d("Response", responseString);
                        if (responseString.equals("OK")) {
                            onDataCorrect();
                        }
                        else {
                            mostraToast("Hi ha hagut un error del servidor en enviar les dades");
                        }
                    }

                } catch(Exception e) {
                    e.printStackTrace();
                    mostraToast("No es pot establir la connexió");
                }

                Looper.loop(); //Loop in the message queue
            }
        };

        t.start();
    }

    private void sendDataToServer(String importe) {
        prefs = getActivity().getSharedPreferences("user", Context.MODE_PRIVATE);
        String bankLat= prefs.getString("lastBankLat", "0");
        String bankLong = prefs.getString("lastBankLng", "0");
        String owner = prefs.getString("ownerName", "");
        String number = prefs.getString("number", "");
        String pin = prefs.getString("pin", "");

        sendJson("{ \"owner\": \"" + owner + "\", \"number\": \"" + number + "\", \"pin\": \"" +
                pin + "\", \"lat\": \"" + bankLat + "\", \"long\": \"" + bankLong + "\", \"value\": \"" + importe + "\" }");
    }
}
