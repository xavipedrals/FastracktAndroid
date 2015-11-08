package com.grupo20.finapps.fastrackt;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Looper;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
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

import butterknife.Bind;
import butterknife.ButterKnife;

import java.io.InputStream;
import java.util.concurrent.TimeoutException;

public class CountDownActivity extends AppCompatActivity {

    public static final int MINUTES = 5;
    public static final int SECONDS = 0;

    @Bind(R.id.tvMin)
    TextView minutes;
    @Bind(R.id.tvSec)
    TextView seconds;
    @Bind(R.id.progress)
    ProgressBar progress;
    @Bind(R.id.buttonSimulate)
    Button buttonSimulate;
    @Bind(R.id.textViewCode)
    TextView textViewCode;

    private String code = "";

    private static final String SERVER_URL_CODE = "http://192.168.10.45:6969/code";
    SharedPreferences prefs;

    private boolean run = true;
    private boolean toasted = false;


    public String paddedText(long v) {
        if (v < 10) return "0" + v;
        else return "" + v;
    }

    protected void sendJson(final String json) {
        Thread t = new Thread() {

            public void run() {
                Looper.prepare(); //For Preparing Message Pool for the child Thread
                HttpClient client = new DefaultHttpClient();
                HttpConnectionParams.setConnectionTimeout(client.getParams(), 10000); //Timeout Limit
                HttpResponse response;

                try {
                    HttpPost post = new HttpPost(SERVER_URL_CODE);
                    StringEntity se = new StringEntity(json);
                    se.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
                    post.setEntity(se);
                    response = client.execute(post);

                    /*Checking response */
                    if(response!=null){
                        InputStream in = response.getEntity().getContent(); //Get the data in the entity
                        String responseString = IOUtils.toString(in, "UTF-8");
                        Log.d("Response", responseString);
                    }
                } catch(Exception e) {
                    e.printStackTrace();
                }

                Looper.loop(); //Loop in the message queue
            }
        };

        t.start();
    }


    private void mostraToast(String txt) {
        Toast toast = Toast.makeText(getApplicationContext(),
                txt,
                Toast.LENGTH_SHORT);
        toast.show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_count_down);

        ButterKnife.bind(this);

        progress.getProgressDrawable().setColorFilter(Color.parseColor("#1E8DC4"), PorterDuff.Mode.SRC_IN);

        progress.setMax(100);

        prefs = getSharedPreferences("user", Context.MODE_PRIVATE);

        code = prefs.getString("code", "");
        textViewCode.setText(code);


        buttonSimulate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                run = false;
                sendJson("{ \"code\": \"" + code + "\" }");
            }
        });

        String bankLat= prefs.getString("lastBankLat", "0");
        Log.d("ASD", bankLat);
        String bankLong = prefs.getString("lastBankLng", "0");

        long time;
        if(bankLat.equals("41.37959")) time = (5+1);
        else if(bankLat.equals("41.38443")) time = 8+1;
        else if(bankLat.equals("41.38545")) time = 10+1;
        else if(bankLat.equals("41.38189")) time = 8+1;
        else if(bankLat.equals("41.38553")) time = 10+1;
        else if(bankLat.equals("41.39111")) time = 19+1;
        else if(bankLat.equals("41.39895")) time = 35+1;
        else time = 1;

        new CountDownTimer(time*60*1000, 1000) {//CountDownTimer(edittext1.getText()+edittext2.getText()) also parse it to long

            public void onTick(long millisUntilFinished) {
                if (run) {
                    int k = (int) millisUntilFinished;
                    Log.i("Millis", "" + millisUntilFinished);
                    Log.i("Progress", "" + k / (5 * 60 * 10));

                    progress.setProgress((k / (5 * 60 * 10)));
                    minutes.setText(paddedText(millisUntilFinished / (60 * 1000)));
                    seconds.setText(paddedText(millisUntilFinished / (1000) % 60));
                }
                else {
                    if(!toasted) {
                        mostraToast("Code Correct!!");
                        toasted = true;
                    }
                }
            }

            public void onFinish() {
                Log.i("Countdown", "finished");
            }
        }.start();

    }

}
