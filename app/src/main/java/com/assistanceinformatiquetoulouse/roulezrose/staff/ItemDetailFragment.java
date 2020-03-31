package com.assistanceinformatiquetoulouse.roulezrose.staff;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.support.design.widget.CollapsingToolbarLayout;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

import static java.lang.Thread.sleep;

// Class ItemDetailFragment
// TODO Remplacer les checkbox poste (conducteur, jaune, ..., binome) par un spinner
public class ItemDetailFragment extends Fragment {
    // Attibuts publics
    // Attributs privés
    protected ProgressDialog pProgressDialog;
    private int pRandonneeId;
    private int pStaffeurId;
    private int pBoutonUpdateActif;
    private int pBoutonUpdateActifMem;
    private Staffeur pStaffeur;
    private TextView pTextViewPresence;
    private CheckBox pCheckBoxPresent;
    private TextView pTextViewConducteur;
    private CheckBox pCheckBoxConducteur;
    private TextView pTextViewJaune;
    private CheckBox pCheckBoxJaune;
    private TextView pTextViewEclaireur;
    private CheckBox pCheckBoxEclaireur;
    private TextView pTextViewMeneur;
    private CheckBox pCheckBoxMeneur;
    private TextView pTextViewLanterne;
    private CheckBox pCheckBoxLanterne;
    private TextView pTextViewBinome;
    private CheckBox pCheckBoxBinome;
    private TextView pTextViewPresent;
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
    private void activerBouton()
    {
        if (pBoutonUpdateActif == pBoutonUpdateActifMem)
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

        if (getArguments().containsKey(getString(R.string.randonnee)))
        {
            pRandonneeId = getArguments().getInt("Rando_id");
            pStaffeurId = getArguments().getInt(getString(R.string.id));
            pStaffeur = new Staffeur(getArguments().getString(getString(R.string.nom)),
                                     pStaffeurId,
                                     getArguments().getString(getString(R.string.presence)),
                                     getArguments().getInt(getString(R.string.poste)),
                                     getArguments().getInt(getString(R.string.poste_conducteur)),
                                     getArguments().getInt(getString(R.string.poste_jaune)),
                                     getArguments().getInt(getString(R.string.poste_eclaireur)),
                                     getArguments().getInt(getString(R.string.poste_meneur)),
                                     getArguments().getInt(getString(R.string.poste_lanterne)),
                                     getArguments().getInt(getString(R.string.poste_binome)),
                                     getArguments().getInt(getString(R.string.poste_present)));
            // Load the dummy date specified by the fragment
            // arguments. In a real-world scenario, use a Loader
            // to load date from a date provider.

            Activity activity = this.getActivity();
            CollapsingToolbarLayout appBarLayout = (CollapsingToolbarLayout) activity.findViewById(R.id.toolbar_layout);
            if (appBarLayout != null)
            {
                appBarLayout.setTitle(pStaffeur.lireNom() + " (" + Integer.toString(pStaffeurId)+ ")");
            }
            else
            {
            }
        }
        else
        {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.item_detail, container, false);
        pProgressDialog = null;
        pBoutonUpdateActif = 0;
        pBoutonUpdateActifMem = 0;
        pContext = getContext();
        pTextViewPresence = (TextView) rootView.findViewById(R.id.textViewPresence);
        pCheckBoxPresent = (CheckBox) rootView.findViewById(R.id.checkBoxPresent);
        pTextViewConducteur = (TextView) rootView.findViewById(R.id.textViewConducteur);
        pCheckBoxConducteur = (CheckBox) rootView.findViewById(R.id.checkBoxConducteur);
        pTextViewJaune = (TextView) rootView.findViewById(R.id.textViewJaune);
        pCheckBoxJaune = (CheckBox) rootView.findViewById(R.id.checkBoxJaune);
        pTextViewEclaireur = (TextView) rootView.findViewById(R.id.textViewEclaireur);
        pCheckBoxEclaireur = (CheckBox) rootView.findViewById(R.id.checkBoxEclaireur);
        pTextViewMeneur = (TextView) rootView.findViewById(R.id.textViewMeneur);
        pCheckBoxMeneur = (CheckBox) rootView.findViewById(R.id.checkBoxMeneur);
        pTextViewLanterne = (TextView) rootView.findViewById(R.id.textViewLanterne);
        pCheckBoxLanterne = (CheckBox) rootView.findViewById(R.id.checkBoxLanterne);
        pTextViewBinome = (TextView) rootView.findViewById(R.id.textViewBinome);
        pCheckBoxBinome = (CheckBox) rootView.findViewById(R.id.checkBoxBinome);
        pTextViewPresent = (TextView) rootView.findViewById(R.id.textViewPresent);
        pButtonUpdate = (Button) rootView.findViewById(R.id.buttonUpdate);

        pCheckBoxPresent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (pCheckBoxPresent.isChecked()) {
                    pBoutonUpdateActif = 0x1;
                }
                else {
                    pBoutonUpdateActif = 0;
                    pCheckBoxConducteur.setChecked(false);
                    pCheckBoxJaune.setChecked(false);
                    pCheckBoxEclaireur.setChecked(false);
                    pCheckBoxMeneur.setChecked(false);
                    pCheckBoxLanterne.setChecked(false);
                    pCheckBoxBinome.setChecked(false);
                }
                activerBouton();
            }
        });
        pCheckBoxConducteur.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pBoutonUpdateActif &= 0x01;
                if (pCheckBoxConducteur.isChecked())
                {
                    pBoutonUpdateActif |= 0x2;
                    pCheckBoxJaune.setChecked(false);
                    pCheckBoxEclaireur.setChecked(false);
                    pCheckBoxMeneur.setChecked(false);
                    pCheckBoxLanterne.setChecked(false);
                    pCheckBoxBinome.setChecked(false);
                }
                else
                {
                    pBoutonUpdateActif &= 0x03;
                }
                activerBouton();
            }
        });
        pCheckBoxJaune.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pBoutonUpdateActif &= 0x01;
                if (pCheckBoxJaune.isChecked())
                {
                    pBoutonUpdateActif |= 0x4;
                    pCheckBoxConducteur.setChecked(false);
                    pCheckBoxEclaireur.setChecked(false);
                    pCheckBoxMeneur.setChecked(false);
                    pCheckBoxLanterne.setChecked(false);
                    pCheckBoxBinome.setChecked(false);
                }
                else
                {
                    pBoutonUpdateActif &= 0x05;
                }
                activerBouton();
            }
        });
        pCheckBoxEclaireur.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pBoutonUpdateActif &= 0x01;
                if (pCheckBoxEclaireur.isChecked())
                {
                    pBoutonUpdateActif |= 0x8;
                    pCheckBoxConducteur.setChecked(false);
                    pCheckBoxJaune.setChecked(false);
                    pCheckBoxMeneur.setChecked(false);
                    pCheckBoxLanterne.setChecked(false);
                    pCheckBoxBinome.setChecked(false);
                }
                else
                {
                    pBoutonUpdateActif &= 0x09;
                }
                activerBouton();
            }
        });
        pCheckBoxMeneur.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pBoutonUpdateActif &= 0x01;
                if (pCheckBoxMeneur.isChecked())
                {
                    pBoutonUpdateActif |= 0x10;
                    pCheckBoxConducteur.setChecked(false);
                    pCheckBoxJaune.setChecked(false);
                    pCheckBoxEclaireur.setChecked(false);
                    pCheckBoxLanterne.setChecked(false);
                    pCheckBoxBinome.setChecked(false);
                }
                else
                {
                    pBoutonUpdateActif &= 0x11;
                }
                activerBouton();
            }
        });
        pCheckBoxLanterne.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pBoutonUpdateActif &= 0x01;
                if (pCheckBoxLanterne.isChecked())
                {
                    pBoutonUpdateActif |= 0x20;
                    pCheckBoxConducteur.setChecked(false);
                    pCheckBoxJaune.setChecked(false);
                    pCheckBoxEclaireur.setChecked(false);
                    pCheckBoxMeneur.setChecked(false);
                    pCheckBoxBinome.setChecked(false);
                }
                else
                {
                    pBoutonUpdateActif &= 0x21;
                }
                activerBouton();
            }
        });
        pCheckBoxBinome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pBoutonUpdateActif &= 0x01;
                if (pCheckBoxBinome.isChecked())
                {
                    pBoutonUpdateActif |= 0x40;
                    pCheckBoxConducteur.setChecked(false);
                    pCheckBoxJaune.setChecked(false);
                    pCheckBoxEclaireur.setChecked(false);
                    pCheckBoxMeneur.setChecked(false);
                    pCheckBoxLanterne.setChecked(false);
                }
                else
                {
                    pBoutonUpdateActif &= 0x41;
                }
                activerBouton();
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

                        if (pCheckBoxPresent.isChecked()) {
                            valeur = 1;
                            if (pCheckBoxConducteur.isChecked()) //pTextViewConducteur1.getText().equals("+1"))
                            {
                                poste_id = 4;
                            } else if (pCheckBoxJaune.isChecked()) //pTextViewJaune1.getText().equals("+1"))
                            {
                                poste_id = 0;
                            } else if (pCheckBoxEclaireur.isChecked()) //pTextViewEclaireur1.getText().equals("+1"))
                            {
                                poste_id = 1;
                            } else if (pCheckBoxMeneur.isChecked()) //pTextViewMeneur1.getText().equals("+1"))
                            {
                                poste_id = 2;
                            } else if (pCheckBoxLanterne.isChecked()) //pTextViewLanterne1.getText().equals("+1"))
                            {
                                poste_id = 3;
                            } else // if (pCheckBoxBinome.isChecked()) // if (pTextViewBinome1.getText().equals("+1"))
                            {
                                poste_id = 6;
                            }
                        }
                        else
                        {
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
            pTextViewPresence.setText(pStaffeur.lirePresence());
            if (pStaffeur.lirePresence().equals(getString(R.string.staff_present)))
            {
                pBoutonUpdateActif = 0x1;
                pCheckBoxPresent.setChecked(true);
                switch(pStaffeur.lirePoste()) {
                    case 0 : // jaune
                        pCheckBoxJaune.setChecked(true);
                        pBoutonUpdateActif |= 0x4;
                        break;
                    case 1 : // éclaireur
                        pCheckBoxEclaireur.setChecked(true);
                        pBoutonUpdateActif |= 0x8;
                        break;
                    case 2 : // meneur
                        pCheckBoxMeneur.setChecked(true);
                        pBoutonUpdateActif |= 0x10;
                        break;
                    case 3 : // lanterne
                        pCheckBoxLanterne.setChecked(true);
                        pBoutonUpdateActif |= 0x20;
                        break;
                    case 4 : // conducteur
                        pCheckBoxConducteur.setChecked(true);
                        pBoutonUpdateActif |= 0x2;
                        break;
                    case 5 : // électron
                        break;
                    case 6 : // binôme
                        pCheckBoxBinome.setChecked(true);
                        pBoutonUpdateActif |= 0x40;
                        break;
                    default : // autre (impossible)
                        break;
                }
                pBoutonUpdateActifMem = pBoutonUpdateActif;
            }
            else
            {
                pCheckBoxPresent.setChecked(false);
            }
            pTextViewConducteur.setText(String.valueOf(pStaffeur.lireConducteur()));
            pTextViewJaune.setText(String.valueOf(pStaffeur.lireJaune()));
            pTextViewEclaireur.setText(String.valueOf(pStaffeur.lireEclaireur()));
            pTextViewMeneur.setText(String.valueOf(pStaffeur.lireMeneur()));
            pTextViewLanterne.setText(String.valueOf(pStaffeur.lireLanterne()));
            pTextViewBinome.setText(String.valueOf(pStaffeur.lireBinome()));
            pTextViewPresent.setText(String.valueOf(pStaffeur.lirePresent()));
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