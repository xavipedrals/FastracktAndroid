package com.grupo20.finapps.fastrackt;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageView;
import butterknife.Bind;
import butterknife.OnClick;

import java.util.Map;

public class AfterMain extends AppCompatActivity {

    SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        prefs = getSharedPreferences("user", Context.MODE_PRIVATE);
        redirectActivity();
    }

    private void redirectActivity() {
        if (prefs.getString("lang", "").equals("")) {
            startActivity(new Intent(AfterMain.this, LanguageSelectionActivity.class));
        }
        else {
            startActivity(new Intent(AfterMain.this, LoginActivity.class));
        }
    }
}