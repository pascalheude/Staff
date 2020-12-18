package com.assistanceinformatiquetoulouse.roulezrose.staff;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.design.widget.CollapsingToolbarLayout;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import javax.net.ssl.HttpsURLConnection;

// Class ItemDetailFragment
public class ItemDetailFragment extends Fragment {
    // Attibuts publics
    // Attributs privés
    protected ProgressDialog pProgressDialog;
    private int pRandonneeId;
    private int pStaffeurId;
    private int pBoutonUpdateActifMem;
    private Staffeur pStaffeur;
    private TextView pTextViewPresence;
    private TextView pTextViewConducteur;
    private TextView pTextViewJaune;
    private TextView pTextViewEclaireur;
    private TextView pTextViewMeneur;
    private TextView pTextViewLanterne;
    private TextView pTextViewBinome;
    private ArrayList<String> pListe;
    private ArrayAdapter<String> pAdapter;
    private Spinner pSpinnerPoste;
    private Button pButtonUpdate;
    private Context pContext;

    // Méthode ecrirePresences
    private String ecrirePresences(URL url) throws IOException {
        String lStatus = "";
        InputStream lInputStream = null;
        try {
            if (url.getProtocol().contains("https")) {
                // Creer une communication https pour communiquer avec l'URL
                HttpsURLConnection lHttpsURLConnection = (HttpsURLConnection) url.openConnection();
                // Connexion à l'URL
                lHttpsURLConnection.connect();
                // Lire le flux depuis la connexion
                lInputStream = lHttpsURLConnection.getInputStream();
            }
            else
            {
                // Creer une communication http pour communiquer avec l'URL
                HttpURLConnection lHttpURLConnection = (HttpURLConnection) url.openConnection();
                // Connexion à l'URL
                lHttpURLConnection.connect();
                // Lire le flux depuis la connexion
                lInputStream = lHttpURLConnection.getInputStream();
            }
            BufferedReader lBufferedReader = new BufferedReader(new InputStreamReader(lInputStream));
            StringBuffer lStringBuffer  = new StringBuffer();
            String lLigne = "";
            while((lLigne = lBufferedReader.readLine()) != null) {
                lStringBuffer.append(lLigne);
                lStringBuffer.append("\n");
            }
            lStatus = lStringBuffer.toString();
            lBufferedReader.close();
        }
        catch (IOException e) {
        }
        finally {
            if (lInputStream != null)
            {
                lInputStream.close();
            }
            else
            {
            }
        }
        return(lStatus);
    }

    // Méthode activerBouton
    private void activerBouton(int position)
    {
        if (position == pBoutonUpdateActifMem)
        {
            pButtonUpdate.setEnabled(false);
        }
        else
        {
            pButtonUpdate.setEnabled(true);
        }
    }

    // Constructeur
    public ItemDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Activity lActivity = this.getActivity();
        Intent lIntent = lActivity.getIntent();

        pRandonneeId = lIntent.getIntExtra("Rando_id", 0);
        pStaffeurId = lIntent.getIntExtra(getString(R.string.id), 0);
        pStaffeur = new Staffeur(lIntent.getStringExtra(getString(R.string.nom)),
                                 pStaffeurId,
                                 lIntent.getStringExtra(getString(R.string.presence)),
                                 lIntent.getIntExtra(getString(R.string.poste), 0),
                                 lIntent.getIntExtra(getString(R.string.poste_conducteur), 0),
                                 lIntent.getIntExtra(getString(R.string.poste_jaune), 0),
                                 lIntent.getIntExtra(getString(R.string.poste_eclaireur), 0),
                                 lIntent.getIntExtra(getString(R.string.poste_meneur), 0),
                                 lIntent.getIntExtra(getString(R.string.poste_lanterne), 0),
                                 lIntent.getIntExtra(getString(R.string.poste_binome), 0),
                                 lIntent.getIntExtra(getString(R.string.poste_present), 0));
        pListe = lIntent.getStringArrayListExtra(getString(R.string.liste_poste));
        CollapsingToolbarLayout appBarLayout = (CollapsingToolbarLayout) lActivity.findViewById(R.id.toolbar_layout);
        if (appBarLayout != null) {
            appBarLayout.setTitle(pStaffeur.lireNom() + " (" + Integer.toString(pStaffeurId)+ ")");
        }
            else {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.item_detail, container, false);
        pProgressDialog = null;
        pBoutonUpdateActifMem = 0;
        pContext = getContext();
        pTextViewConducteur = (TextView) rootView.findViewById(R.id.textViewConducteur);
        pTextViewJaune = (TextView) rootView.findViewById(R.id.textViewJaune);
        pTextViewEclaireur = (TextView) rootView.findViewById(R.id.textViewEclaireur);
        pTextViewMeneur = (TextView) rootView.findViewById(R.id.textViewMeneur);
        pTextViewLanterne = (TextView) rootView.findViewById(R.id.textViewLanterne);
        pTextViewBinome = (TextView) rootView.findViewById(R.id.textViewBinome);
        pSpinnerPoste = (Spinner) rootView.findViewById(R.id.spinnerPoste);
        pAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, pListe);
        pAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        pSpinnerPoste.setAdapter(pAdapter);
        pButtonUpdate = (Button) rootView.findViewById(R.id.buttonUpdate);

        pSpinnerPoste.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                activerBouton(position);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        pButtonUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pProgressDialog = ProgressDialog.show(pContext, "Patience",
                        "Mise à jour de la base de données sur le serveur", true);

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        int valeur;
                        int poste_id;

                        if (pSpinnerPoste.getSelectedItemPosition() != 0) {
                            valeur = 1;
                            poste_id = pSpinnerPoste.getSelectedItemPosition() - 1;
                        }
                        else {
                            valeur = 0;
                            poste_id = 0;
                        }
                        try {
                            URL lURL = new URL(String.format(getString(R.string.out_URL), pRandonneeId, pStaffeurId, valeur, poste_id));
                            pProgressDialog.setMessage(String.format("staff_update.php?rando_id=%d&user_id=%d&valeur=%d&poste_id=%d", pRandonneeId, pStaffeurId, valeur, poste_id));
                            if (ecrirePresences(lURL).contains("OK"))
                            {
                                getActivity().finish();
                            }
                            else
                            {
                                pProgressDialog.dismiss();
                                getActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(pContext, "Problème mise à jour présence", Toast.LENGTH_LONG).show();
                                    }
                                });
                            }
                        }
                        catch (MalformedURLException e) {
                        }
                        catch (IOException e) {
                        }
                    }
                }).start();
            }
        });
        // Show the dummy date as text in a TextView.
        if (pStaffeur != null)
        {
            //((WebView) rootView.findViewById(R.id.item_detail)).loadData(pStaffeur.lireNom(), "text/html", null);
            if (pStaffeur.lirePresence().equals(getString(R.string.staff_present)))
            {
                pBoutonUpdateActifMem = pStaffeur.lirePoste() + 1;
            }
            else
            {
                pBoutonUpdateActifMem = 0;
            }
            pSpinnerPoste.setSelection(pBoutonUpdateActifMem);
            pTextViewConducteur.setText(String.valueOf(pStaffeur.lireConducteur()));
            pTextViewJaune.setText(String.valueOf(pStaffeur.lireJaune()));
            pTextViewEclaireur.setText(String.valueOf(pStaffeur.lireEclaireur()));
            pTextViewMeneur.setText(String.valueOf(pStaffeur.lireMeneur()));
            pTextViewLanterne.setText(String.valueOf(pStaffeur.lireLanterne()));
            pTextViewBinome.setText(String.valueOf(pStaffeur.lireBinome()));
        }
        else
        {
        }
        return(rootView);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (pProgressDialog != null) {
            pProgressDialog.dismiss();
            pProgressDialog = null;
        }
        else {
        }
    }
}