package com.grupo20.finapps.fastrackt;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Created by xavi on 07/11/15.
 */
public class DialogImport extends DialogFragment {

    private EditText mEditText;
    private Button okButton, cancelButton;

    public DialogImport() {
        // Empty constructor required for DialogFragment
    }

    public boolean isValidWord(String w) {
        return w.matches("[A-Za-z]*");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.afegir_idioma, container);
        mEditText = (EditText) view.findViewById(R.id.design_import);
        okButton = (Button) view.findViewById(R.id.okButton);
        cancelButton = (Button) view.findViewById(R.id.cancelButton);

        mEditText.requestFocus();
        getDialog().getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);

        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), Deadline.class));
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        getDialog().setTitle("Afegir Idioma");
        getDialog().setCancelable(true);
        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }
}
