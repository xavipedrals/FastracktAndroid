package com.grupo20.finapps.fastrackt;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginActivity extends AppCompatActivity {

    private SharedPreferences prefs;

    @Bind(R.id.ownerName)
    EditText ownerName;

    @Bind(R.id.cardPin)
    EditText cardPin;

    @Bind(R.id.cardNumber)
    EditText cardNumber;

    @Bind(R.id.doneIdentify)
    ImageView doneIdentify;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        prefs = getSharedPreferences("user", Context.MODE_PRIVATE);

        ButterKnife.bind(this);

        doneIdentify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getIdentifyInfo();
            }
        });
    }

    public void getIdentifyInfo() {
        String owner = ownerName.getText().toString();
        String pin = cardPin.getText().toString();
        String number = cardNumber.getText().toString();

        if (isValid("owner", owner) && isValid("pin", pin) && isValid("number", number)) {
            SharedPreferences.Editor editor = prefs.edit();

            editor.putString("ownerName", owner);
            editor.putString("pin", pin);
            editor.putString("number", number);
            editor.apply();
        }
        else {
            // Mostrar error
        }
    }

    private boolean isValid(String input, String valor) {
        if (valor == null) return false;

        switch(input) {
            case "ownerName":

            break;
            case "pin":

            break;
            case "number":

            break;
        }

        return true;
    }
}
