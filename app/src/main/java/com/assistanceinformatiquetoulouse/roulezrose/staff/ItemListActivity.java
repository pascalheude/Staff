package com.assistanceinformatiquetoulouse.roulezrose.staff;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import javax.net.ssl.HttpsURLConnection;

import java.net.HttpURLConnection;
import java.net.URL;
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
    private final static int REQUEST_CODE = 10;
    private boolean mTwoPane;
    private View pRecyclerView;
    private SwipeRefreshLayout pSwipeRefreshLayout;
    private TextView pTextViewDate;
    private FloatingActionButton pFloatingActionButtonProchaineRandonnee;
    private FloatingActionButton pFloatingActionButtonPrecedenteRandonnee;
    private FloatingActionButton pFloatingActionPresent;
    private SimpleItemRecyclerViewAdapter pSimpleItemRecyclerViewAdapter[];
    private int pNbRandonnee; // Nombre de randonnées reçues
    private int pNumRandonnee; // Numéro de la randonnée affichée
    private String pURL;    // URL pour lire le fichier json à partir de la base de données
    private int pId[];   // ID des randonnées (rando_id)
    private int pStaffPresent[]; // nombre de staffeurs présents à chaque randonnée
    private String pDateString[];    // date des randonnées
    private ArrayList<Staffeur> pListeStaffeur[];    // liste des staffeurs de chaque randonnée

    public static Bitmap textAsBitmap(String text, float textSize, int textColor) {
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_list);
        pRecyclerView = findViewById(R.id.item_list);
        pSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeRefreshLayout);
        pSwipeRefreshLayout.setSize(SwipeRefreshLayout.LARGE);
        pTextViewDate = (TextView) findViewById(R.id.dateRandonnee);
        pFloatingActionButtonProchaineRandonnee = (FloatingActionButton) findViewById(R.id.fabProchaineRandonnee);
        pFloatingActionButtonPrecedenteRandonnee = (FloatingActionButton) findViewById(R.id.fabPrecedenteRandonnee);
        pFloatingActionPresent = (FloatingActionButton) findViewById(R.id.fabPresent);
        pNumRandonnee = 1;
        pURL = getString(R.string.in_URL);
        assert pRecyclerView != null;
        // Lire la base de données du staff
        DownloadTask downloadTask = new DownloadTask();
        downloadTask.execute(pURL);

        pSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                for (int i = 0;i < pListeStaffeur.length; i++) {
                    pListeStaffeur[i].clear();
                }
                // Lire la base de données du staff
                DownloadTask downloadTask = new DownloadTask();
                downloadTask.execute(pURL);
        }});

        pFloatingActionButtonProchaineRandonnee.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (pNumRandonnee < pNbRandonnee) {
                    pTextViewDate.setText(pDateString[pNumRandonnee]);
                    ((RecyclerView)pRecyclerView).setAdapter(pSimpleItemRecyclerViewAdapter[pNumRandonnee]);
                    pFloatingActionPresent.setImageBitmap(textAsBitmap(String.valueOf(pStaffPresent[pNumRandonnee]), 16, Color.WHITE));
                    pNumRandonnee++;
                    pFloatingActionButtonPrecedenteRandonnee.setEnabled(true);
                    if (pNumRandonnee == pNbRandonnee) {
                        pFloatingActionButtonProchaineRandonnee.setEnabled(false);
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
                    pTextViewDate.setText(pDateString[pNumRandonnee - 1]);
                    ((RecyclerView)pRecyclerView).setAdapter(pSimpleItemRecyclerViewAdapter[pNumRandonnee - 1]);
                    pFloatingActionPresent.setImageBitmap(textAsBitmap(String.valueOf(pStaffPresent[pNumRandonnee - 1]), 16, Color.WHITE));
                    pFloatingActionButtonProchaineRandonnee.setEnabled(true);
                    if (pNumRandonnee == 1) {
                        pFloatingActionButtonPrecedenteRandonnee.setEnabled(false);
                    }
                }
                else {
                }
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
                lAlertDialog.setTitle("Staff\nVersion " + this.getString(R.string.version));
                lAlertDialog.setMessage("Compatible index version " + this.getString(R.string.index) + " et update version " + this.getString(R.string.update) + "\nGestion de la présence des staffeurs\n© AIT 2019 (pascalh)\n\nassistanceinformatiquetoulouse@gmail.com");
                lAlertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                    }});
                lAlertDialog.setIcon(R.mipmap.ic_staff);
                lAlertDialog.create().show();
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
            holder.aStaffeur = pListeStaffeur.get(position);
            holder.aNomView.setText((String) holder.aStaffeur.lireNom());
            holder.aStatView.setText((String) holder.aStaffeur.lireStat());
            if (holder.aStaffeur.isPresent()) {
                switch(holder.aStaffeur.lirePoste()) {
                    case 0 : // jaune
                        holder.aPresenceView.setText("Jaune");
                        holder.aPresenceView.setBackgroundColor(getColor(R.color.colorStaffeurJaune));
                        break;
                    case 1 : // éclaireur
                        holder.aPresenceView.setText("Eclaireur");
                        holder.aPresenceView.setBackgroundColor(getColor(R.color.colorStaffeurOrange));
                        break;
                    case 2 : // meneur
                        holder.aPresenceView.setText("Meneur");
                        holder.aPresenceView.setBackgroundColor(getColor(R.color.colorStaffeurOrange));
                        break;
                    case 3 : // lanterne
                        holder.aPresenceView.setText("Lanterne");
                        holder.aPresenceView.setBackgroundColor(getColor(R.color.colorStaffeurOrange));
                        break;
                    case 4 : // conducteur
                        holder.aPresenceView.setText("Pilote");
                        holder.aPresenceView.setBackgroundColor(getColor(R.color.colorStaffeurConducteur));
                        break;
                    case 5 : // électron
                        holder.aPresenceView.setText("Electron");
                        holder.aPresenceView.setBackgroundColor(getColor(R.color.colorStaffeurOrange));
                        break;
                    case 6 : // binôme
                        holder.aPresenceView.setText("Binôme");
                        holder.aPresenceView.setBackgroundColor(getColor(R.color.colorStaffeurBinome));
                        break;
                    default : // autre (impossible)
                        holder.aPresenceView.setBackgroundColor(0x00000000);
                }
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
                        intent.putExtra(getString(R.string.randonnee), pId[pNumRandonnee]);
                        intent.putExtra(getString(R.string.nom), holder.aStaffeur.lireNom());
                        intent.putExtra(getString(R.string.id), holder.aStaffeur.lireId());
                        intent.putExtra(getString(R.string.presence), holder.aStaffeur.lirePresence());
                        intent.putExtra(getString(R.string.poste), holder.aStaffeur.lirePoste());
                        intent.putExtra(getString(R.string.conducteur), holder.aStaffeur.lireConducteur());
                        intent.putExtra(getString(R.string.jaune), holder.aStaffeur.lireJaune());
                        intent.putExtra(getString(R.string.eclaireur), holder.aStaffeur.lireEclaireur());
                        intent.putExtra(getString(R.string.meneur), holder.aStaffeur.lireMeneur());
                        intent.putExtra(getString(R.string.lanterne), holder.aStaffeur.lireLanterne());
                        intent.putExtra(getString(R.string.binome), holder.aStaffeur.lireBinome());
                        intent.putExtra(getString(R.string.present), holder.aStaffeur.lirePresent());
                        startActivityForResult(intent, REQUEST_CODE);
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
                if (lJSONString != null)
                {
                    lGlobalJSONObject = new JSONObject(lJSONString);
                    pNbRandonnee = lGlobalJSONObject.getInt("Nombre");
                    pSimpleItemRecyclerViewAdapter = new SimpleItemRecyclerViewAdapter[pNbRandonnee];
                    pId = new int[pNbRandonnee];
                    pStaffPresent = new int[pNbRandonnee];
                    pDateString = new String[pNbRandonnee];
                    pListeStaffeur = new ArrayList[pNbRandonnee];
                    liste_randonnees = lGlobalJSONObject.getJSONArray("Randonnees");
                    for (int i = 0;i < liste_randonnees.length(); i++) {
                        lJSONObjet = liste_randonnees.getJSONObject(i);
                        pId[i] = lJSONObjet.getInt("Id");
                        try {
                            lDate = lSimpleDateFormatIn.parse(lJSONObjet.getString("Date"));
                            pDateString[i] = lSimpleDateFormatOut.format(lDate);
                        } catch (ParseException e) {
                            pDateString[i] = "Date inconnue";
                        }
                        pStaffPresent[i] = 0;
                        pListeStaffeur[i] = new ArrayList<>();
                        liste_staffeur = lJSONObjet.getJSONArray("Staffeurs");
                        for (int j = 0; j < liste_staffeur.length(); j++) {
                            Staffeur lStaffeur;
                            lJSONObjet = liste_staffeur.getJSONObject(j);
                            lStaffeur = new Staffeur(lJSONObjet.getString(getString(R.string.nom)),
                                    lJSONObjet.getInt(getString(R.string.id)),
                                    lJSONObjet.getString(getString(R.string.presence)),
                                    lJSONObjet.getInt(getString(R.string.poste)),
                                    lJSONObjet.getInt(getString(R.string.conducteur)),
                                    lJSONObjet.getInt(getString(R.string.jaune)),
                                    lJSONObjet.getInt(getString(R.string.eclaireur)),
                                    lJSONObjet.getInt(getString(R.string.meneur)),
                                    lJSONObjet.getInt(getString(R.string.lanterne)),
                                    lJSONObjet.getInt((getString(R.string.binome))),
                                    lJSONObjet.getInt(getString(R.string.present)));
                            if (lStaffeur.lirePresence().equals(getString(R.string.staff_present)))
                            {
                                pStaffPresent[i]++;
                            }
                            else
                            {
                            }
                            pListeStaffeur[i].add(lStaffeur);
                        }
                        pSimpleItemRecyclerViewAdapter[i] = new SimpleItemRecyclerViewAdapter(pListeStaffeur[i]);
                    }
                }
                else
                {
                    pNbRandonnee = 1;
                    pDateString = new String[1];
                    pDateString[0] = "Impossible d'accéder au serveur de données";
                }
            }
            catch(IOException e)
            {
                pNbRandonnee = 1;
                pDateString = new String[1];
                pDateString[0] = e.toString();
                Log.d("Background Task", e.toString());
            }
            catch(JSONException e)
            {
                pNbRandonnee = 1;
                pDateString = new String[1];
                pDateString[0] = "Erreur JSON";
                e.printStackTrace();
            }
            return(pDateString[0]);
        }

        @Override
        protected void onPostExecute(String result) {
            pTextViewDate.setText(pDateString[pNumRandonnee - 1]);
            ((RecyclerView) pRecyclerView).setAdapter(pSimpleItemRecyclerViewAdapter[pNumRandonnee - 1]);
            pFloatingActionPresent.setImageBitmap(textAsBitmap(String.valueOf(pStaffPresent[pNumRandonnee - 1]), 16, Color.WHITE));
            if (pNumRandonnee == 1) {
                pFloatingActionButtonPrecedenteRandonnee.setEnabled(false);
                pFloatingActionButtonProchaineRandonnee.setEnabled(true);
            }
            else if (pNumRandonnee == pNbRandonnee) {
                pFloatingActionButtonPrecedenteRandonnee.setEnabled(true);
                pFloatingActionButtonProchaineRandonnee.setEnabled(false);
            }
            else {
                pFloatingActionButtonPrecedenteRandonnee.setEnabled(true);
                pFloatingActionButtonProchaineRandonnee.setEnabled(true);
            }
            pSwipeRefreshLayout.setRefreshing(false);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            for (int i = 0;i < pListeStaffeur.length; i++) {
                pListeStaffeur[i].clear();
            }
            // Lire la base de données du staff
            DownloadTask downloadTask = new DownloadTask();
            downloadTask.execute(pURL);
        }
        else {
        }
    }
}