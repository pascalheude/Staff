package com.assistanceinformatiquetoulouse.roulezrose.staff;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;

// Class ParametresActivity
public class ParametresActivity extends AppCompatActivity {
    // Attributs privés
    private int pNbRandonnees;
    private int pValeur;
    private String pStringArray[];
    private Spinner pSpinner;

    // Méthode onCreate
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parametres);
        pSpinner = (Spinner) findViewById(R.id.spinnerNbRandonnees);
        Intent lIntent = getIntent();
        pNbRandonnees = lIntent.getIntExtra("nb_randonnees_in", 4);
        pStringArray = getResources().getStringArray(R.array.nb_randonnees);
        pValeur = Integer.parseInt(pStringArray[0]);
        pSpinner.setSelection(pNbRandonnees - pValeur);
        pSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                pNbRandonnees = pValeur + position;
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    // Méthode onFinish
    @Override
    public void finish() {
        Intent lIntent = new Intent();
        lIntent.putExtra("nb_randonnees_out", pNbRandonnees);
        setResult(RESULT_OK, lIntent);
        super.finish();
    }
}
