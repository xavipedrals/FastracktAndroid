package com.grupo20.finapps.fastrackt;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Looper;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
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

public class LoginActivity extends AppCompatActivity {

    private static final String SERVER_URL = "http://192.168.10.45:6969/checkCredentials";

    private SharedPreferences prefs;
    private Toast toast = null;

    @Bind(R.id.ownerName)
    EditText ownerName;

    @Bind(R.id.cardPin)
    EditText cardPin;

    @Bind(R.id.cardNumber)
    EditText cardNumber;

    @Bind(R.id.doneIdentify)
    ImageView doneIdentify;

    String owner;
    String number;
    String pin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        prefs = getSharedPreferences("user", Context.MODE_PRIVATE);

        ButterKnife.bind(this);


        String currentName = prefs.getString("ownerName", "");
        String currentNumber = prefs.getString("number", "");
        ownerName.setText(currentName);
        cardNumber.setText(currentNumber);

        doneIdentify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setIdentifyInfo();
            }
        });
    }

    public void setIdentifyInfo() {
        owner = ownerName.getText().toString();
        pin = cardPin.getText().toString();
        number = cardNumber.getText().toString();

        sendDataToServer(owner, number, pin);
    }

    private void onDataCorrect() {
        SharedPreferences.Editor editor = prefs.edit();

        editor.putString("ownerName", owner);
        editor.putString("number", number);
        editor.putString("pin", pin);
        editor.apply();

        startActivity(new Intent(LoginActivity.this, MapsActivity.class));
    }

    private void mostraToast(String txt) {
        if (toast != null) toast.cancel();
        toast = Toast.makeText(getApplicationContext(),
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
                    HttpPost post = new HttpPost(SERVER_URL);
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
                    } else {
                        mostraToast("El servidor no respon");
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

    private void sendDataToServer(String owner, String number, String pin) {
        Log.d("Hola Xavi", owner + " " + number + " " + pin);
        String strJSON = "{ \"owner\": \"" + owner + "\", \"number\": \"" + number + "\", \"pin\": \"" + pin + "\" }";
        Log.d("JSOOOOON", strJSON);
        sendJson("{ \"owner\": \"" + owner + "\", \"number\": \"" + number + "\", \"pin\": \"" + pin + "\" }");
    }
}
