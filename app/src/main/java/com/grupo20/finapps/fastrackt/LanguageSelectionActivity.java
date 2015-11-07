package com.grupo20.finapps.fastrackt;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import butterknife.OnClick;

public class LanguageSelectionActivity extends AppCompatActivity {

    SharedPreferences prefs;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_language_selection);
    }

    @OnClick(R.id.imageViewCatalan)
    public void selectedCatalan() {
        setLanguage("ca");
    }

    @OnClick(R.id.imageViewSpanish)
    public void selectedSpanish() {
        setLanguage("es");
    }

    @OnClick(R.id.imageViewEnglish)
    public void selectedEnglish() {
        setLanguage("en");
    }


    public void setLanguage(String language) {
        SharedPreferences.Editor editor = prefs.edit();

        editor.putString("lang", language).apply();

        startActivity(new Intent(LanguageSelectionActivity.this, LoginActivity.class));
    }

}
