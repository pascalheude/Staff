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

import java.net.MalformedURLException;
import java.net.URL;

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
    private TextView pTextViewPresent;
    private TextView pTextViewPresent1;
    private Button pButtonUpdate;
    private Context pContext;

    // Méthode ecrirePresences
    private void ecrirePresences(URL url)
    {

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
        pTextViewPresent = (TextView) rootView.findViewById(R.id.textViewPresent);
        pTextViewPresent1 = (TextView) rootView.findViewById(R.id.textViewPresent1);
        pButtonUpdate = (Button) rootView.findViewById(R.id.buttonUpdate);

        pCheckBoxConducteur.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (pCheckBoxConducteur.isChecked())
                {
                    pTextViewConducteur1.setText("+1");
                    pTextViewPresent1.setText("+1");
                    pCheckBoxPresent.setChecked(true);
                    pTextViewJaune1.setText("");
                    pCheckBoxJaune.setChecked(false);
                    pTextViewEclaireur1.setText("");
                    pCheckBoxEclaireur.setChecked(false);
                    pTextViewMeneur1.setText("");
                    pCheckBoxMeneur.setChecked(false);
                    pTextViewLanterne1.setText("");
                    pCheckBoxLanterne.setChecked(false);
                    pButtonUpdate.setEnabled(true);
                }
                else
                {
                    pTextViewConducteur1.setText("");
                    pTextViewPresent1.setText("");
                    pCheckBoxPresent.setChecked(false);
                    pButtonUpdate.setEnabled(false);
                }
            }
        });
        pCheckBoxJaune.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (pCheckBoxJaune.isChecked())
                {
                    pTextViewJaune1.setText("+1");
                    pTextViewPresent1.setText("+1");
                    pCheckBoxPresent.setChecked(true);
                    pTextViewConducteur1.setText("");
                    pCheckBoxConducteur.setChecked(false);
                    pTextViewEclaireur1.setText("");
                    pCheckBoxEclaireur.setChecked(false);
                    pTextViewMeneur1.setText("");
                    pCheckBoxMeneur.setChecked(false);
                    pTextViewLanterne1.setText("");
                    pCheckBoxLanterne.setChecked(false);
                    pButtonUpdate.setEnabled(true);
                }
                else
                {
                    pTextViewJaune1.setText("");
                    pTextViewPresent1.setText("");
                    pCheckBoxPresent.setChecked(false);
                    pButtonUpdate.setEnabled(false);
                }
            }
        });
        pCheckBoxEclaireur.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (pCheckBoxEclaireur.isChecked())
                {
                    pTextViewEclaireur1.setText("+1");
                    pTextViewPresent1.setText("+1");
                    pCheckBoxPresent.setChecked(true);
                    pTextViewConducteur1.setText("");
                    pCheckBoxConducteur.setChecked(false);
                    pTextViewJaune1.setText("");
                    pCheckBoxJaune.setChecked(false);
                    pTextViewMeneur1.setText("");
                    pCheckBoxMeneur.setChecked(false);
                    pTextViewLanterne1.setText("");
                    pCheckBoxLanterne.setChecked(false);
                    pButtonUpdate.setEnabled(true);
                }
                else
                {
                    pTextViewEclaireur1.setText("");
                    pTextViewPresent1.setText("");
                    pCheckBoxPresent.setChecked(false);
                    pButtonUpdate.setEnabled(false);
                }
            }
        });
        pCheckBoxMeneur.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (pCheckBoxMeneur.isChecked())
                {
                    pTextViewMeneur1.setText("+1");
                    pTextViewPresent1.setText("+1");
                    pCheckBoxPresent.setChecked(true);
                    pTextViewConducteur1.setText("");
                    pCheckBoxConducteur.setChecked(false);
                    pTextViewJaune1.setText("");
                    pCheckBoxJaune.setChecked(false);
                    pTextViewEclaireur1.setText("");
                    pCheckBoxEclaireur.setChecked(false);
                    pTextViewLanterne1.setText("");
                    pCheckBoxLanterne.setChecked(false);
                    pButtonUpdate.setEnabled(true);
                }
                else
                {
                    pTextViewMeneur1.setText("");
                    pTextViewPresent1.setText("");
                    pCheckBoxPresent.setChecked(false);
                    pButtonUpdate.setEnabled(false);
                }
            }
        });
        pCheckBoxLanterne.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (pCheckBoxLanterne.isChecked())
                {
                    pTextViewLanterne1.setText("+1");
                    pTextViewPresent1.setText("+1");
                    pCheckBoxPresent.setChecked(true);
                    pTextViewConducteur1.setText("");
                    pCheckBoxConducteur.setChecked(false);
                    pTextViewJaune1.setText("");
                    pCheckBoxJaune.setChecked(false);
                    pTextViewEclaireur1.setText("");
                    pCheckBoxEclaireur.setChecked(false);
                    pTextViewMeneur1.setText("");
                    pCheckBoxMeneur.setChecked(false);
                    pButtonUpdate.setEnabled(true);
                }
                else
                {
                    pTextViewLanterne1.setText("");
                    pTextViewPresent1.setText("");
                    pCheckBoxPresent.setChecked(false);
                    pButtonUpdate.setEnabled(false);
                }
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
                        int poste_id;
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
                        else // if (pTextViewLanterne1.getText().equals("+1"))
                        {
                            poste_id = 3;
                        }
                        try {
                            URL lURL = new URL(String.format(getString(R.string.out_URL), pRandonneeId, pStaffeurId, poste_id));
                            ecrirePresences(lURL);
                            pProgressDialog.dismiss();
                        }
                        catch (MalformedURLException e) {
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
            pTextViewConducteur.setText(String.valueOf(pStaffeur.lireConducteur()));
            pTextViewJaune.setText(String.valueOf(pStaffeur.lireJaune()));
            pTextViewEclaireur.setText(String.valueOf(pStaffeur.lireEclaireur()));
            pTextViewMeneur.setText(String.valueOf(pStaffeur.lireMeneur()));
            pTextViewLanterne.setText(String.valueOf(pStaffeur.lireLanterne()));
            pTextViewPresent.setText(String.valueOf(pStaffeur.lirePresent()));
        }
        else
        {
        }
        return(rootView);
    }
}