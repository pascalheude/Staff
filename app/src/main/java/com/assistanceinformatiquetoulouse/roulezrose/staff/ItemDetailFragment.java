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
public class ItemDetailFragment extends Fragment {
    // Attibuts publics
    // Attributs privés
    protected ProgressDialog pProgressDialog;
    private int pRandonneeId;
    private int pStaffeurId;
    private Staffeur pStaffeur;
    private TextView pTextViewPresence;
    private CheckBox pCheckBoxPresent;
    private TextView pTextViewConducteur;
    private TextView pTextViewConducteur1;
    private CheckBox pCheckBoxConducteur;
    private TextView pTextViewJaune;
    private TextView pTextViewJaune1;
    private CheckBox pCheckBoxJaune;
    private TextView pTextViewEclaireur1;
    private TextView pTextViewEclaireur;
    private CheckBox pCheckBoxEclaireur;
    private TextView pTextViewMeneur;
    private TextView pTextViewMeneur1;
    private CheckBox pCheckBoxMeneur;
    private TextView pTextViewLanterne;
    private TextView pTextViewLanterne1;
    private CheckBox pCheckBoxLanterne;
    private TextView pTextViewBinome;
    private TextView pTextViewBinome1;
    private CheckBox pCheckBoxBinome;
    private TextView pTextViewPresent;
    private TextView pTextViewPresent1;
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

    // Méthode inverserBouton
    private void inverserBouton()
    {
        if (pButtonUpdate.isEnabled())
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
                                     getArguments().getInt(getString(R.string.conducteur)),
                                     getArguments().getInt(getString(R.string.jaune)),
                                     getArguments().getInt(getString(R.string.eclaireur)),
                                     getArguments().getInt(getString(R.string.meneur)),
                                     getArguments().getInt(getString(R.string.lanterne)),
                                     getArguments().getInt(getString(R.string.binome)),
                                     getArguments().getInt(getString(R.string.present)));
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
        pContext = getContext();
        pTextViewPresence = (TextView) rootView.findViewById(R.id.textViewPresence);
        pCheckBoxPresent = (CheckBox) rootView.findViewById(R.id.checkBoxPresent);
        pTextViewConducteur = (TextView) rootView.findViewById(R.id.textViewConducteur);
        pTextViewConducteur1 = (TextView) rootView.findViewById(R.id.textViewConducteur1);
        pCheckBoxConducteur = (CheckBox) rootView.findViewById(R.id.checkBoxConducteur);
        pTextViewJaune = (TextView) rootView.findViewById(R.id.textViewJaune);
        pTextViewJaune1 = (TextView) rootView.findViewById(R.id.textViewJaune1);
        pCheckBoxJaune = (CheckBox) rootView.findViewById(R.id.checkBoxJaune);
        pTextViewEclaireur = (TextView) rootView.findViewById(R.id.textViewEclaireur);
        pTextViewEclaireur1 = (TextView) rootView.findViewById(R.id.textViewEclaireur1);
        pCheckBoxEclaireur = (CheckBox) rootView.findViewById(R.id.checkBoxEclaireur);
        pTextViewMeneur = (TextView) rootView.findViewById(R.id.textViewMeneur);
        pTextViewMeneur1 = (TextView) rootView.findViewById(R.id.textViewMeneur1);
        pCheckBoxMeneur = (CheckBox) rootView.findViewById(R.id.checkBoxMeneur);
        pTextViewLanterne = (TextView) rootView.findViewById(R.id.textViewLanterne);
        pTextViewLanterne1 = (TextView) rootView.findViewById(R.id.textViewLanterne1);
        pCheckBoxLanterne = (CheckBox) rootView.findViewById(R.id.checkBoxLanterne);
        pTextViewBinome = (TextView) rootView.findViewById(R.id.textViewBinome);
        pTextViewBinome1 = (TextView) rootView.findViewById(R.id.textViewBinome1);
        pCheckBoxBinome = (CheckBox) rootView.findViewById(R.id.checkBoxBinome);
        pTextViewPresent = (TextView) rootView.findViewById(R.id.textViewPresent);
        pTextViewPresent1 = (TextView) rootView.findViewById(R.id.textViewPresent1);
        pButtonUpdate = (Button) rootView.findViewById(R.id.buttonUpdate);

        pCheckBoxPresent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                inverserBouton();
            }
        });
        pCheckBoxConducteur.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (pCheckBoxConducteur.isChecked())
                {
                    pTextViewConducteur1.setText("+1");
                    pTextViewPresent1.setText("+1");
                    pTextViewJaune1.setText("");
                    pCheckBoxJaune.setChecked(false);
                    pTextViewEclaireur1.setText("");
                    pCheckBoxEclaireur.setChecked(false);
                    pTextViewMeneur1.setText("");
                    pCheckBoxMeneur.setChecked(false);
                    pTextViewLanterne1.setText("");
                    pCheckBoxLanterne.setChecked(false);
                    pTextViewBinome1.setText("");
                    pCheckBoxBinome.setChecked(false);
                }
                else
                {
                    pTextViewConducteur1.setText("");
                    pTextViewPresent1.setText("");
                }
                inverserBouton();
            }
        });
        pCheckBoxJaune.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (pCheckBoxJaune.isChecked())
                {
                    pTextViewJaune1.setText("+1");
                    pTextViewPresent1.setText("+1");
                    pTextViewConducteur1.setText("");
                    pCheckBoxConducteur.setChecked(false);
                    pTextViewEclaireur1.setText("");
                    pCheckBoxEclaireur.setChecked(false);
                    pTextViewMeneur1.setText("");
                    pCheckBoxMeneur.setChecked(false);
                    pTextViewLanterne1.setText("");
                    pCheckBoxLanterne.setChecked(false);
                    pTextViewBinome1.setText("");
                    pCheckBoxBinome.setChecked(false);
                }
                else
                {
                    pTextViewJaune1.setText("");
                    pTextViewPresent1.setText("");
                }
                inverserBouton();
            }
        });
        pCheckBoxEclaireur.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (pCheckBoxEclaireur.isChecked())
                {
                    pTextViewEclaireur1.setText("+1");
                    pTextViewPresent1.setText("+1");
                    pTextViewConducteur1.setText("");
                    pCheckBoxConducteur.setChecked(false);
                    pTextViewJaune1.setText("");
                    pCheckBoxJaune.setChecked(false);
                    pTextViewMeneur1.setText("");
                    pCheckBoxMeneur.setChecked(false);
                    pTextViewLanterne1.setText("");
                    pCheckBoxLanterne.setChecked(false);
                    pTextViewBinome1.setText("");
                    pCheckBoxBinome.setChecked(false);
                }
                else
                {
                    pTextViewEclaireur1.setText("");
                    pTextViewPresent1.setText("");
                }
                inverserBouton();
            }
        });
        pCheckBoxMeneur.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (pCheckBoxMeneur.isChecked())
                {
                    pTextViewMeneur1.setText("+1");
                    pTextViewPresent1.setText("+1");
                    pTextViewConducteur1.setText("");
                    pCheckBoxConducteur.setChecked(false);
                    pTextViewJaune1.setText("");
                    pCheckBoxJaune.setChecked(false);
                    pTextViewEclaireur1.setText("");
                    pCheckBoxEclaireur.setChecked(false);
                    pTextViewLanterne1.setText("");
                    pCheckBoxLanterne.setChecked(false);
                    pTextViewBinome1.setText("");
                    pCheckBoxBinome.setChecked(false);
                }
                else
                {
                    pTextViewMeneur1.setText("");
                    pTextViewPresent1.setText("");
                }
                inverserBouton();
            }
        });
        pCheckBoxLanterne.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (pCheckBoxLanterne.isChecked())
                {
                    pTextViewLanterne1.setText("+1");
                    pTextViewPresent1.setText("+1");
                    pTextViewConducteur1.setText("");
                    pCheckBoxConducteur.setChecked(false);
                    pTextViewJaune1.setText("");
                    pCheckBoxJaune.setChecked(false);
                    pTextViewEclaireur1.setText("");
                    pCheckBoxEclaireur.setChecked(false);
                    pTextViewMeneur1.setText("");
                    pCheckBoxMeneur.setChecked(false);
                    pTextViewBinome1.setText("");
                    pCheckBoxBinome.setChecked(false);
                }
                else
                {
                    pTextViewLanterne1.setText("");
                    pTextViewPresent1.setText("");
                }
                inverserBouton();
            }
        });
        pCheckBoxBinome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (pCheckBoxBinome.isChecked())
                {
                    pTextViewBinome1.setText("+1");
                    pTextViewPresent1.setText("+1");
                    pTextViewConducteur1.setText("");
                    pCheckBoxConducteur.setChecked(false);
                    pTextViewJaune1.setText("");
                    pCheckBoxJaune.setChecked(false);
                    pTextViewEclaireur1.setText("");
                    pCheckBoxEclaireur.setChecked(false);
                    pTextViewMeneur1.setText("");
                    pCheckBoxMeneur.setChecked(false);
                    pTextViewLanterne1.setText("");
                    pCheckBoxLanterne.setChecked(false);
                }
                else
                {
                    pTextViewBinome1.setText("");
                    pTextViewPresent1.setText("");
                }
                inverserBouton();
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

                        if (pCheckBoxPresent.isChecked())
                        {
                            valeur = 1;
                        }
                        else
                        {
                            valeur = 0;
                        }
                        if (pTextViewConducteur1.getText().equals("+1"))
                        {
                            poste_id = 4;
                        }
                        else if (pTextViewJaune1.getText().equals("+1"))
                        {
                            poste_id = 0;
                        }
                        else if (pTextViewEclaireur1.getText().equals("+1"))
                        {
                            poste_id = 1;
                        }
                        else if (pTextViewMeneur1.getText().equals("+1"))
                        {
                            poste_id = 2;
                        }
                        else if (pTextViewLanterne1.getText().equals("+1"))
                        {
                            poste_id = 3;
                        }
                        else // if (pTextViewBinome1.getText().equals("+1"))
                        {
                            poste_id = 6;
                        }
                        try {
                            URL lURL = new URL(String.format(getString(R.string.out_URL), pRandonneeId, pStaffeurId, poste_id));
                            pProgressDialog.setMessage(String.format("update.php?rando_id=%d&user_id=%d&valeur=%d&poste_id=%d", pRandonneeId, pStaffeurId, valeur, poste_id));
                            if (ecrirePresences(lURL).equals("OK"))
                            {
                                sleep(1000);
                                pProgressDialog.setMessage("Présence mise à jour");
                                sleep(1000);
                            }
                            else
                            {
                                sleep(1000);
                                pProgressDialog.setMessage("Problème mise à jour présence");
                                sleep(1000);
                            }
                            pProgressDialog.dismiss();
                        }
                        catch (InterruptedException e) {
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
                pCheckBoxPresent.setChecked(true);
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
}