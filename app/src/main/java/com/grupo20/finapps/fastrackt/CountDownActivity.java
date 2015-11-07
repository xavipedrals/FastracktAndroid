package com.grupo20.finapps.fastrackt;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.ButterKnife;

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


    public String paddedText(long v) {
        if (v < 10) return "0" + v;
        else return "" + v;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_count_down);

        ButterKnife.bind(this);

        progress.getProgressDrawable().setColorFilter(Color.parseColor("#1E8DC4"), PorterDuff.Mode.SRC_IN);

        progress.setMax(100);

        new CountDownTimer(5*60*1000, 1000) {//CountDownTimer(edittext1.getText()+edittext2.getText()) also parse it to long

            public void onTick(long millisUntilFinished) {
                int k = (int) millisUntilFinished;
                Log.i("Millis", "" + millisUntilFinished);
                Log.i("Progress", "" + k/(5*60*10));

                progress.setProgress((k/(5*60*10)));
                minutes.setText(paddedText(millisUntilFinished / (60 * 1000)));
                seconds.setText(paddedText(millisUntilFinished / (1000) % 60));
            }

            public void onFinish() {
                Log.i("Countdown", "finished");
            }
        }.start();




        /*Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/
    }

    private void setCounter(int minutes, int seconds) {


    }

}
