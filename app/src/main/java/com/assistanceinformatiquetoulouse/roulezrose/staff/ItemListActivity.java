package com.assistanceinformatiquetoulouse.roulezrose.staff;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.design.widget.FloatingActionButton;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import javax.net.ssl.HttpsURLConnection;

import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * An activity representing a list of Items. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a {@link ItemDetailActivity} representing
 * item contenu. On tablets, the activity presents the list of items and
 * item contenu side-by-side using two vertical panes.
 */
public class ItemListActivity extends AppCompatActivity {
    // Attributs privés
    private final static int REQUEST_CODE_PARAMETRES = 10;
    private final static int REQUEST_CODE_PRESENCE = 20;
    private SharedPreferences pSharedPreferences;
    private boolean mTwoPane;
    private EditText pEditTextLogin;
    private EditText pEditTextPassword;
    private CheckBox pCheckBoxMemoriser;
    private View pRecyclerView;
    private SwipeRefreshLayout pSwipeRefreshLayout;
    private TextView pTextViewDate;
    private FloatingActionButton pFloatingActionButtonProchaineRandonnee;
    private FloatingActionButton pFloatingActionButtonPrecedenteRandonnee;
    private FloatingActionButton pFloatingActionPresent;
    private SimpleItemRecyclerViewAdapter pSimpleItemRecyclerViewAdapter[];
    private AlertDialog pAlertDialog;
    private Toast pToastConnexionEnCours;
    private int pNbRandonnees; // Nombre de randonnées reçues
    private int pNumRandonnee; // Numéro de la randonnée affichée
    private int pId[];   // ID des randonnées (rando_id)
    private int pType[];  // Type des randonnées (rando_type)
    private int pStaffPresent[]; // nombre de staffeurs présents à chaque randonnée
    private int pStaffIndecis[]; // Nombre de staffeurs indécis à chaque randonnée
    private String pDateString[];    // date des randonnées
    private ArrayList<Staffeur> pListeStaffeur[];    // liste des staffeurs de chaque randonnée
    private ConfigurationPoste pConfigurationPoste;  // Information de la table phpbb3_postes
    private ConfigurationRandonnee pConfigurationRandonnee;  // Information de la table phpbb3_rando_types
    private long pTempsStatistique;  // Information de la table phpbb3_config (mois pour remise à 0 des statistiques

    // Méthode ecrireDateRandonnee
    private void ecrireDateRandonnee(String date, int type) {
        pTextViewDate.setText(date);
        pTextViewDate.setBackgroundColor(pConfigurationRandonnee.lireCouleur(type));
        // Changement de la couleur du texte si randonnée noire
        if (type == 5) {
            pTextViewDate.setTextColor(getColor(R.color.colorWhite));
        }
        else {
            pTextViewDate.setTextColor(getColor(R.color.colorBlack));
        }
    }

    // Méthode textAsBitmap
    private static Bitmap textAsBitmap(String text, float textSize, int textColor) {
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setTextSize(textSize);
        paint.setColor(textColor);
        paint.setTextAlign(Paint.Align.LEFT);
        float baseline = -paint.ascent(); // ascent() is negative
        int width = (int) (paint.measureText(text) + 0.0f); // round
        int height = (int) (baseline + paint.descent() + 0.0f);
        Bitmap image = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(image);
        canvas.drawText(text, 0, baseline, paint);
        return(image);
    }

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_list);
        pRecyclerView = (View) findViewById(R.id.item_list);
        pSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeRefreshLayout);
        pSwipeRefreshLayout.setSize(SwipeRefreshLayout.LARGE);
        pTextViewDate = (TextView) findViewById(R.id.dateRandonnee);
        pFloatingActionButtonProchaineRandonnee = (FloatingActionButton) findViewById(R.id.fabProchaineRandonnee);
        pFloatingActionButtonPrecedenteRandonnee = (FloatingActionButton) findViewById(R.id.fabPrecedenteRandonnee);
        pFloatingActionPresent = (FloatingActionButton) findViewById(R.id.fabPresent);
        pSharedPreferences = getBaseContext().getSharedPreferences(getString(R.string.app_name), MODE_PRIVATE);
        if (pSharedPreferences.contains(getString(R.string.nb_randonnees))) {
            pNbRandonnees = pSharedPreferences.getInt(getString(R.string.nb_randonnees), 4);
        }
        else {
            pNbRandonnees = 4;
        }
        pNumRandonnee = 1;
        pConfigurationPoste = new ConfigurationPoste();
        pConfigurationRandonnee = new ConfigurationRandonnee();
        LayoutInflater lLayoutInflater = getLayoutInflater();
        View connexionView = lLayoutInflater.inflate(R.layout.layout_dialog_login, null);
        pEditTextLogin = (EditText) connexionView.findViewById(R.id.editTextNomUtilisateur);
        pEditTextPassword = (EditText) connexionView.findViewById(R.id.editTextMotDePasse);
        pCheckBoxMemoriser = (CheckBox) connexionView.findViewById(R.id.checkBoxMemoriser);
        if (pSharedPreferences.contains(getString(R.string.memorized_data))) {
            pEditTextLogin.setText(pSharedPreferences.getString(getString(R.string.login), ""));
            pEditTextPassword.setText(pSharedPreferences.getString(getString(R.string.password), ""));
            pCheckBoxMemoriser.setChecked(true);
        }
        else {
            pEditTextLogin.setText("");
            pEditTextPassword.setText("");
            pCheckBoxMemoriser.setChecked(false);
        }
        pToastConnexionEnCours = Toast.makeText(getBaseContext(), "Connexion en cours", Toast.LENGTH_SHORT);
        AlertDialog.Builder lConnexionDialog = new AlertDialog.Builder(this);
        lConnexionDialog.setTitle("");
        lConnexionDialog.setView(connexionView);
        lConnexionDialog.setCancelable(false);
        lConnexionDialog.setPositiveButton("Connexion", null);
        pAlertDialog = lConnexionDialog.create();
        pAlertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                Button lButton = ((AlertDialog) pAlertDialog).getButton(AlertDialog.BUTTON_POSITIVE);
                lButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        pToastConnexionEnCours.show();
                        // Connexion à la base de données du staff
                        ConnexionClass lConnexionClass = new ConnexionClass();
                        lConnexionClass.execute(getString(R.string.login_URL));
                    }
                });
            }
        });
        pAlertDialog.show();
        assert pRecyclerView != null;

        pSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                for (int i = 0;i < pListeStaffeur.length; i++) {
                    pListeStaffeur[i].clear();
                }
                pSimpleItemRecyclerViewAdapter[pNumRandonnee - 1].notifyDataSetChanged();
                // Lire la base de données du staff
                DownloadTask downloadTask = new DownloadTask();
                downloadTask.execute(String.format(getString(R.string.in_URL), pNbRandonnees, pTempsStatistique));
        }});

        pFloatingActionButtonProchaineRandonnee.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (pNumRandonnee < pNbRandonnees) {
                    ecrireDateRandonnee(pDateString[pNumRandonnee], pType[pNumRandonnee]);
                    ((RecyclerView)pRecyclerView).setAdapter(pSimpleItemRecyclerViewAdapter[pNumRandonnee]);
                    pFloatingActionPresent.setImageBitmap(textAsBitmap(String.format("%d-%d", pStaffPresent[pNumRandonnee], pStaffIndecis[pNumRandonnee]), 12, Color.WHITE));
                    pNumRandonnee++;
                    pFloatingActionButtonPrecedenteRandonnee.setEnabled(true);
                    pFloatingActionButtonPrecedenteRandonnee.show();
                    if (pNumRandonnee == pNbRandonnees) {
                        pFloatingActionButtonProchaineRandonnee.setEnabled(false);
                        pFloatingActionButtonProchaineRandonnee.hide();
                    }
                    else {
                    }
                }
                else {
                }
                //Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                //        .setAction("Action", null).show();
            }
        });

        pFloatingActionButtonPrecedenteRandonnee.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (pNumRandonnee > 1) {
                    pNumRandonnee--;
                    ecrireDateRandonnee(pDateString[pNumRandonnee - 1], pType[pNumRandonnee - 1]);
                    ((RecyclerView)pRecyclerView).setAdapter(pSimpleItemRecyclerViewAdapter[pNumRandonnee - 1]);
                    pFloatingActionPresent.setImageBitmap(textAsBitmap(String.format("%d-%d", pStaffPresent[pNumRandonnee - 1], pStaffIndecis[pNumRandonnee - 1]), 12, Color.WHITE));
                    pFloatingActionButtonProchaineRandonnee.setEnabled(true);
                    pFloatingActionButtonProchaineRandonnee.show();
                    if (pNumRandonnee == 1) {
                        pFloatingActionButtonPrecedenteRandonnee.setEnabled(false);
                        pFloatingActionButtonPrecedenteRandonnee.hide();
                    }
                    else {
                    }
                }
                else {
                }
            }
        });

        pTextViewDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(v.getContext())
                        .setTitle("Annulation de randonnée")
                        .setMessage("Annuler ?")
                        .setPositiveButton("OUI", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                new Thread(new Runnable() {
                                    @Override
                                    public void run() {
                                        try {
                                            URL lURL = new URL(String.format(getString(R.string.out_URL), pId[pNumRandonnee - 1], 0, 0, 0));
                                            if (ecrirePresences(lURL).contains("OK")) {
                                                for (int i = 0;i < pListeStaffeur.length; i++) {
                                                    pListeStaffeur[i].clear();
                                                }
                                                pSimpleItemRecyclerViewAdapter[pNumRandonnee].notifyDataSetChanged();
                                                // Lire la base de données du staff
                                                DownloadTask downloadTask = new DownloadTask();
                                                downloadTask.execute(String.format(getString(R.string.in_URL), pNbRandonnees, pTempsStatistique));
                                            }
                                            else {
                                                runOnUiThread(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        Toast.makeText(getApplicationContext(), "Problème mise à jour présence", Toast.LENGTH_LONG).show();
                                                    }
                                                });
                                            }
                                        }
                                        catch(MalformedURLException e) {
                                        }
                                        catch (IOException e) {
                                        }
                                    }
                                }).start();
                            }
                        })
                        .setNegativeButton("NON", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                            }
                        }).show();
            }
        });
        if (findViewById(R.id.item_detail_container) != null) {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-w900dp).
            // If this view is present, then the
            // activity should be in two-pane mode.
            mTwoPane = true;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu, menu);
        return(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        switch(item.getItemId()) {
            case R.id.action_about:
                AlertDialog.Builder lAlertDialog = new AlertDialog.Builder(this);
                lAlertDialog.setTitle("Staff\nVersion " + getString(R.string.version));
                lAlertDialog.setMessage("Compatible login version " + getString(R.string.version_login) +
                        " index version " + getString(R.string.version_index) +
                        " et update version " + getString(R.string.version_update) +
                        "\nGestion de la présence des staffeurs\n© AIT 2019 (pascalh)\n\nassistanceinformatiquetoulouse@gmail.com");
                lAlertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                    }});
                lAlertDialog.setIcon(R.mipmap.ic_staff);
                lAlertDialog.create().show();
                break;
            case R.id.action_parametres:
                Intent lIntent = new Intent(this, ParametresActivity.class);
                lIntent.putExtra("nb_randonnees_in", pNbRandonnees);
                startActivityForResult(lIntent, REQUEST_CODE_PARAMETRES);
                break;
        }
        return(true);
    }

    private void setupRecyclerView(@NonNull RecyclerView recyclerView) {
        // TBR : SimpleItemRecyclerViewAdapter lSimpleItemRecyclerViewAdapter = new SimpleItemRecyclerViewAdapter(NewsContent.aListItem);
        // TBR : recyclerView.setAdapter(lSimpleItemRecyclerViewAdapter);
    }

    public class SimpleItemRecyclerViewAdapter
            extends RecyclerView.Adapter<SimpleItemRecyclerViewAdapter.ViewHolder> {

        private final ArrayList<Staffeur> pListeStaffeur;

        public SimpleItemRecyclerViewAdapter(ArrayList<Staffeur> liste_staffeur) {
            pListeStaffeur = liste_staffeur;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_list_content, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, final int position) {
            int id;
            holder.aStaffeur = pListeStaffeur.get(position);
            holder.aNomView.setText((String) holder.aStaffeur.lireNom());
            holder.aStatView.setText((String) holder.aStaffeur.lireStat());
            if (holder.aStaffeur.estPresent()) {
                id = holder.aStaffeur.lirePoste();
                holder.aPresenceView.setBackgroundColor(pConfigurationPoste.lireCouleur(id));
                if ((id == 4) || (id == 7)) {
                    holder.aPresenceView.setText("Pilote");
                }
                else {
                    holder.aPresenceView.setText(pConfigurationPoste.lirePoste(id));
                }
            }
            else if (holder.aStaffeur.estIndecis()) {
                holder.aPresenceView.setText((String) holder.aStaffeur.lirePresence());
                holder.aPresenceView.setBackgroundColor(getColor(R.color.colorBlack));
            }
            else {
                holder.aPresenceView.setText((String) holder.aStaffeur.lirePresence());
                holder.aPresenceView.setBackgroundColor(0x00000000);
            }

            holder.aView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mTwoPane) {
                        Bundle arguments = new Bundle();
                        // TODO : ajouter les attributs de la classe staffeur
                        arguments.putInt(getString(R.string.randonnee), position);
                        //arguments.putString(ItemDetailFragment.ARG_ITEM_ID, (String) holder.aStaffeur.get("titre"));
                        ItemDetailFragment fragment = new ItemDetailFragment();
                        fragment.setArguments(arguments);
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.item_detail_container, fragment)
                                .commit();
                    }
                    else {
                        Context context = v.getContext();
                        Intent intent = new Intent(context, ItemDetailActivity.class);
                        intent.putExtra(getString(R.string.randonnee), pId[pNumRandonnee - 1]);
                        intent.putExtra(getString(R.string.nom), holder.aStaffeur.lireNom());
                        intent.putExtra(getString(R.string.id), holder.aStaffeur.lireId());
                        intent.putExtra(getString(R.string.presence), holder.aStaffeur.lirePresence());
                        intent.putExtra(getString(R.string.poste), holder.aStaffeur.lirePoste());
                        intent.putExtra(getString(R.string.poste_conducteur), holder.aStaffeur.lireConducteur());
                        intent.putExtra(getString(R.string.poste_jaune), holder.aStaffeur.lireJaune());
                        intent.putExtra(getString(R.string.poste_eclaireur), holder.aStaffeur.lireEclaireur());
                        intent.putExtra(getString(R.string.poste_meneur), holder.aStaffeur.lireMeneur());
                        intent.putExtra(getString(R.string.poste_lanterne), holder.aStaffeur.lireLanterne());
                        intent.putExtra(getString(R.string.poste_binome), holder.aStaffeur.lireBinome());
                        intent.putExtra(getString(R.string.poste_present), holder.aStaffeur.lirePresent());
                        startActivityForResult(intent, REQUEST_CODE_PRESENCE);
                    }
                }
            });
        }

        @Override
        public int getItemCount() {
            return pListeStaffeur.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            public View aView;
            public TextView aNomView;
            public TextView aPresenceView;
            public TextView aStatView;
            public Staffeur aStaffeur;

            public ViewHolder(View view) {
                super(view);
                aView = view;
                aNomView = (TextView) view.findViewById(R.id.nom);
                aPresenceView = (TextView) view.findViewById(R.id.presence);
                aStatView = (TextView) view.findViewById(R.id.stat);
            }

            @Override
            public String toString() {
                return super.toString() + " '" + aPresenceView.getText() + "'";
            }
        }
    }

    // Classe privée ConnexionTask
    private class ConnexionClass extends AsyncTask<String, Integer, Boolean> {
        // Attributs privés
        private String pErreur;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pErreur = "";
        }

        @Override
        protected Boolean doInBackground(String... url) {
            int mois_statistique;
            String lLogin;
            String lPassword;
            String lJSONString;
            JSONObject lGlobalJSONObject;
            JSONObject lJSONObjet;
            JSONArray liste_poste;
            JSONArray liste_randonnee;
            byte[] bytes;
            try {
                bytes = pEditTextLogin.getText().toString().getBytes("UTF-8");
                lLogin = new String(bytes, Charset.forName("UTF-8"));
            }
            catch(UnsupportedEncodingException e) {
                lLogin = null;
            }
            try {
                bytes = pEditTextPassword.getText().toString().getBytes("UTF-8");
                lPassword = new String(bytes, Charset.forName("UTF-8"));
            }
            catch(UnsupportedEncodingException e) {
                lPassword = null;
            }
            HttpPost lHttpPost = new HttpPost(getString(R.string.login_URL));
            HttpClient lHttpClient = new DefaultHttpClient();
            HttpResponse lHttpResponse;
            ArrayList<NameValuePair> lNameValuePairList = new ArrayList<NameValuePair>();
            lNameValuePairList.add(new BasicNameValuePair(getString(R.string.login), lLogin));
            lNameValuePairList.add(new BasicNameValuePair(getString(R.string.password), lPassword));
            lNameValuePairList.add(new BasicNameValuePair("nb", String.valueOf(pNbRandonnees)));
            try {
                lHttpPost.setEntity(new UrlEncodedFormEntity(lNameValuePairList, "UTF-8"));
                lHttpResponse = lHttpClient.execute(lHttpPost);
                if (lHttpResponse.getStatusLine().getStatusCode() == 200) {
                    if (pCheckBoxMemoriser.isChecked()) {
                        SharedPreferences.Editor lEditor = pSharedPreferences.edit();
                        lEditor.putBoolean(getString(R.string.memorized_data), true);
                        lEditor.putString(getString(R.string.login), pEditTextLogin.getText().toString());
                        lEditor.putString(getString(R.string.password), pEditTextPassword.getText().toString());
                        lEditor.apply();
                    } else {
                        SharedPreferences.Editor lEditor = pSharedPreferences.edit();
                        lEditor.remove(getString(R.string.memorized_data));
                        lEditor.remove(getString(R.string.login));
                        lEditor.remove(getString(R.string.password));
                        lEditor.apply();
                    }
                    InputStream lInputStream = lHttpResponse.getEntity().getContent();
                    BufferedReader lBufferedReader = new BufferedReader(new InputStreamReader(lInputStream));
                    StringBuffer lStringBuffer  = new StringBuffer();
                    String lLigne = "";
                    while((lLigne = lBufferedReader.readLine()) != null) {
                        lStringBuffer.append(lLigne);
                        lStringBuffer.append("\n");
                    }
                    lJSONString = lStringBuffer.toString();
                    lBufferedReader.close();
                    try {
                        lGlobalJSONObject = new JSONObject(lJSONString);
                        liste_poste = lGlobalJSONObject.getJSONArray("couleurs_poste");
                        for (int i = 0; i < liste_poste.length(); i++) {
                            lJSONObjet = liste_poste.getJSONObject(i);
                            pConfigurationPoste.ajouterPoste(lJSONObjet.getInt("id"),
                                                lJSONObjet.getString("label"),
                                                lJSONObjet.getString("bg_color"));
                        }
                    }
                    catch (JSONException e) {
                        pConfigurationPoste.effacerPoste();
                        pConfigurationPoste.ajouterPoste(0, "Jaune",
                                getColor(R.color.colorStaffeurJaune));
                        pConfigurationPoste.ajouterPoste(1, "Eclaireur",
                                getColor(R.color.colorStaffeurOrange));
                        pConfigurationPoste.ajouterPoste(2, "Meneur",
                                getColor(R.color.colorStaffeurOrange));
                        pConfigurationPoste.ajouterPoste(3, "Lanterne",
                                getColor(R.color.colorStaffeurOrange));
                        pConfigurationPoste.ajouterPoste(4, "Pilote",
                                getColor(R.color.colorStaffeurConducteur));
                        pConfigurationPoste.ajouterPoste(5, "Electron",
                                getColor(R.color.colorStaffeurOrange));
                        pConfigurationPoste.ajouterPoste(6, "Binôme",
                                getColor(R.color.colorStaffeurBinome));
                        pConfigurationPoste.ajouterPoste(7, "Pilote",
                                getColor(R.color.colorStaffeurConducteur));
                    }
                    try {
                        lGlobalJSONObject = new JSONObject(lJSONString);
                        liste_randonnee = lGlobalJSONObject.getJSONArray("couleurs_randonnée");
                        for (int i = 0; i < liste_randonnee.length(); i++) {
                            lJSONObjet = liste_randonnee.getJSONObject(i);
                            pConfigurationRandonnee.ajouterCouleur(lJSONObjet.getInt("id"),
                                                      lJSONObjet.getString("color"));
                        }
                    }
                    catch (JSONException e) {
                        pConfigurationRandonnee.effacerRandonnee();
                        pConfigurationRandonnee.ajouterCouleur(0,
                                                  getColor(R.color.colorRandonneeVerte));
                        pConfigurationRandonnee.ajouterCouleur(1,
                                                  getColor(R.color.colorRandonneeDoubleBleue));
                        pConfigurationRandonnee.ajouterCouleur(2,
                                                  getColor(R.color.colorRandonneeDoubleOrange));
                        pConfigurationRandonnee.ajouterCouleur(3,
                                                  getColor(R.color.colorRandonneeBleue));
                        pConfigurationRandonnee.ajouterCouleur(4,
                                                  getColor(R.color.colorRandonneeATheme));
                        pConfigurationRandonnee.ajouterCouleur(5,
                                                  getColor(R.color.colorRandoneeNoire));
                    }
                    try {
                        lGlobalJSONObject = new JSONObject(lJSONString);
                        mois_statistique = lGlobalJSONObject.getInt("mois_statistique") - 1;
                    }
                    catch(JSONException e) {
                        mois_statistique = 8; // Septembre
                    }
                    Date lDate = new Date();
                    int annee = lDate.getYear();
                    if (lDate.getMonth() >= 8) {
                        pTempsStatistique = new Date(annee, mois_statistique, 1, 8, 0).getTime();
                    }
                    else {
                        pTempsStatistique = new Date(annee - 1, mois_statistique, 1, 8, 0).getTime() / 1000;
                    }
                    return(true);
                }
                else {
                    InputStream lInputStream = lHttpResponse.getEntity().getContent();
                    BufferedReader lBufferedReader = new BufferedReader(new InputStreamReader(lInputStream));
                    StringBuffer lStringBuffer  = new StringBuffer();
                    String lLigne = "";
                    while((lLigne = lBufferedReader.readLine()) != null) {
                        lStringBuffer.append(lLigne);
                        lStringBuffer.append("\n");
                    }
                    pErreur = lStringBuffer.toString();
                    lBufferedReader.close();
                }
            }
            catch (UnsupportedEncodingException e) {
            }
            catch (IOException e) {
            }
            return(false);
        }

        @Override
        protected void onPostExecute(Boolean result) {
            if (result) {
                pAlertDialog.dismiss();
                pToastConnexionEnCours.cancel();
                Toast.makeText(getBaseContext(), "Connecté", Toast.LENGTH_SHORT).show();
                // Lire la base de données du staff
                DownloadTask downloadTask = new DownloadTask();
                downloadTask.execute(String.format(getString(R.string.in_URL), pNbRandonnees, pTempsStatistique));
            }
            else {
                pEditTextPassword.setError(pErreur);
                pEditTextPassword.requestFocus();
            }
        }
    }

    // Méthode lireDonneesDepuisURL
    private String lireDonneesDepuisURL(String url) throws IOException {
        String lDonnees = "";
        InputStream lInputStream = null;
        URL lURL = new URL(url);
        try {
            if (lURL.getProtocol().contains("https")) {
                // Creer une communication https pour communiquer avec l'URL
                HttpsURLConnection lHttpsURLConnection = (HttpsURLConnection) lURL.openConnection();
                // Connexion à l'URL
                lHttpsURLConnection.connect();
                // Lire le flux depuis la connexion
                lInputStream = lHttpsURLConnection.getInputStream();
            }
            else
            {
                // Creer une communication http pour communiquer avec l'URL
                HttpURLConnection lHttpURLConnection = (HttpURLConnection) lURL.openConnection();
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
            lDonnees = lStringBuffer.toString();
            lBufferedReader.close();
        }
        catch(Exception e) {
            Log.d("Error downloading url", e.toString());
            lDonnees = null;
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
        return(lDonnees);
    }

    // Private class DownloadTask
    private class DownloadTask extends AsyncTask<String, Integer, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... url) {
            String lJSONString = "";
            JSONObject lGlobalJSONObject;
            JSONObject lJSONObjet;
            JSONArray liste_randonnees;
            JSONArray liste_staffeur;
            SimpleDateFormat lSimpleDateFormatIn = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            SimpleDateFormat lSimpleDateFormatOut = new SimpleDateFormat("EEE dd MMM - H:mm");
            Date lDate;
            try
            {
                lJSONString = lireDonneesDepuisURL(url[0]);
                if (lJSONString != null) {
                    lGlobalJSONObject = new JSONObject(lJSONString);
                    pNbRandonnees = lGlobalJSONObject.getInt("Nombre");
                    pSimpleItemRecyclerViewAdapter = new SimpleItemRecyclerViewAdapter[pNbRandonnees];
                    pId = new int[pNbRandonnees];
                    pType = new int[pNbRandonnees];
                    pStaffPresent = new int[pNbRandonnees];
                    pStaffIndecis = new int[pNbRandonnees];
                    pDateString = new String[pNbRandonnees];
                    pListeStaffeur = new ArrayList[pNbRandonnees];
                    liste_randonnees = lGlobalJSONObject.getJSONArray("Randonnees");
                    for (int i = 0;i < liste_randonnees.length(); i++) {
                        lJSONObjet = liste_randonnees.getJSONObject(i);
                        pId[i] = lJSONObjet.getInt("Id");
                        pType[i] = lJSONObjet.getInt("Type");
                        try {
                            lDate = lSimpleDateFormatIn.parse(lJSONObjet.getString("Date"));
                            pDateString[i] = lSimpleDateFormatOut.format(lDate);
                        } catch (ParseException e) {
                            pDateString[i] = "Date inconnue";
                        }
                        pStaffPresent[i] = 0;
                        pStaffIndecis[i] = 0;
                        pListeStaffeur[i] = new ArrayList<>();
                        liste_staffeur = lJSONObjet.getJSONArray("Staffeurs");
                        for (int j = 0; j < liste_staffeur.length(); j++) {
                            Staffeur lStaffeur;
                            lJSONObjet = liste_staffeur.getJSONObject(j);
                            lStaffeur = new Staffeur(lJSONObjet.getString(getString(R.string.nom)),
                                    lJSONObjet.getInt(getString(R.string.id)),
                                    lJSONObjet.getString(getString(R.string.presence)),
                                    lJSONObjet.getInt(getString(R.string.poste)),
                                    lJSONObjet.getInt(getString(R.string.poste_conducteur)),
                                    lJSONObjet.getInt(getString(R.string.poste_jaune)),
                                    lJSONObjet.getInt(getString(R.string.poste_eclaireur)),
                                    lJSONObjet.getInt(getString(R.string.poste_meneur)),
                                    lJSONObjet.getInt(getString(R.string.poste_lanterne)),
                                    lJSONObjet.getInt(getString(R.string.poste_binome)),
                                    lJSONObjet.getInt(getString(R.string.poste_present)));
                            if (lStaffeur.lirePresence().equals(getString(R.string.staff_present))) {
                                pStaffPresent[i]++;
                            }
                            else if (lStaffeur.lirePresence().equals("indécis")) {
                                pStaffIndecis[i]++;
                            }
                            else {
                            }
                            pListeStaffeur[i].add(lStaffeur);
                        }
                        pSimpleItemRecyclerViewAdapter[i] = new SimpleItemRecyclerViewAdapter(pListeStaffeur[i]);
                    }
                }
                else {
                    pNbRandonnees = 1;
                    pDateString = new String[1];
                    pDateString[0] = "Impossible d'accéder au serveur de données";
                }
            }
            catch(IOException e) {
                pNbRandonnees = 1;
                pDateString = new String[1];
                pDateString[0] = e.toString();
                Log.d("Background Task", e.toString());
            }
            catch(JSONException e) {
                pNbRandonnees = 1;
                pDateString = new String[1];
                pDateString[0] = "Erreur JSON";
                e.printStackTrace();
            }
            return(pDateString[0]);
        }

        @Override
        protected void onPostExecute(String result) {
            ecrireDateRandonnee(pDateString[pNumRandonnee - 1], pType[pNumRandonnee - 1]);
            ((RecyclerView) pRecyclerView).setAdapter(pSimpleItemRecyclerViewAdapter[pNumRandonnee - 1]);
            pFloatingActionPresent.setImageBitmap(textAsBitmap(String.format("%d-%d", pStaffPresent[pNumRandonnee - 1], pStaffIndecis[pNumRandonnee - 1]), 12, Color.WHITE));
            if (pNumRandonnee == 1) {
                pFloatingActionButtonPrecedenteRandonnee.setEnabled(false);
                pFloatingActionButtonPrecedenteRandonnee.hide();
                pFloatingActionButtonProchaineRandonnee.setEnabled(true);
                pFloatingActionButtonProchaineRandonnee.show();
            }
            else if (pNumRandonnee == pNbRandonnees) {
                pFloatingActionButtonPrecedenteRandonnee.setEnabled(true);
                pFloatingActionButtonPrecedenteRandonnee.show();
                pFloatingActionButtonProchaineRandonnee.setEnabled(false);
                pFloatingActionButtonProchaineRandonnee.hide();
            }
            else {
                pFloatingActionButtonPrecedenteRandonnee.setEnabled(true);
                pFloatingActionButtonPrecedenteRandonnee.show();
                pFloatingActionButtonProchaineRandonnee.setEnabled(true);
                pFloatingActionButtonProchaineRandonnee.show();
            }
            pSwipeRefreshLayout.setRefreshing(false);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        //super.onActivityResult(requestCode, resultCode, data);
        if ((requestCode == REQUEST_CODE_PARAMETRES) && (resultCode == RESULT_OK)) {
            pNbRandonnees = intent.getIntExtra("nb_randonnees_out", 4);
            SharedPreferences.Editor lEditor = pSharedPreferences.edit();
            lEditor.putInt(getString(R.string.nb_randonnees), pNbRandonnees);
            lEditor.apply();
        }
        else {
        }
        if ((requestCode == REQUEST_CODE_PRESENCE) && (resultCode == RESULT_OK)) {
            for (int i = 0;i < pListeStaffeur.length; i++) {
                pListeStaffeur[i].clear();
            }
            pSimpleItemRecyclerViewAdapter[pNumRandonnee].notifyDataSetChanged();
            // Lire la base de données du staff
            DownloadTask downloadTask = new DownloadTask();
            downloadTask.execute(String.format(getString(R.string.in_URL), pNbRandonnees, pTempsStatistique));
        }
        else {
        }
    }
}