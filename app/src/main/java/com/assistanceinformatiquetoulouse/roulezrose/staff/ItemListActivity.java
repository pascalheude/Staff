package com.assistanceinformatiquetoulouse.roulezrose.staff;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
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
    private boolean mTwoPane;
    private View pRecyclerView;
    private TextView pTextViewDate;
    private FloatingActionButton pFloatingActionButton;
    private SimpleItemRecyclerViewAdapter pSimpleItemRecyclerViewAdapter1;
    private SimpleItemRecyclerViewAdapter pSimpleItemRecyclerViewAdapter2;
    private boolean pPremiereRandonnee;
    private String pURL;
    private int pId1;
    private int pId2;
    private String pDate1String;
    private String pDate2String;
    private ArrayList<Staffeur> pListeStaffeur1;
    private ArrayList<Staffeur> pListeStaffeur2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_list);
        pRecyclerView = findViewById(R.id.item_list);
        pTextViewDate = (TextView) findViewById(R.id.dateRandonnee);
        pFloatingActionButton = (FloatingActionButton) findViewById(R.id.fab);
        pPremiereRandonnee = true;
        pURL = getString(R.string.URL);
        pListeStaffeur1 = new ArrayList<>();
        pListeStaffeur2 = new ArrayList<>();
        assert pRecyclerView != null;
        // Lire la base de données du staff
        DownloadTask downloadTask = new DownloadTask();
        downloadTask.execute(pURL);

        pFloatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (pPremiereRandonnee) {
                    pFloatingActionButton.setImageResource(R.drawable.ic_fleche_gauche_rouge);
                    pTextViewDate.setText(pDate2String);
                    ((RecyclerView)pRecyclerView).setAdapter(pSimpleItemRecyclerViewAdapter2);
                    pPremiereRandonnee = false;
                }
                else {
                    pFloatingActionButton.setImageResource(R.drawable.ic_fleche_droite_rouge);
                    pTextViewDate.setText(pDate1String);
                    ((RecyclerView)pRecyclerView).setAdapter(pSimpleItemRecyclerViewAdapter1);
                    pPremiereRandonnee = true;
                }
                //Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                //        .setAction("Action", null).show();
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
                lAlertDialog.setMessage("Gestion de la présence des staffeurs\n© AIT 2018 (pascalh)\n\nassistanceinformatiquetoulouse@gmail.com");
                lAlertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                    }});
                // TODO : changer l'image et utiliser l'icone de l'application
                lAlertDialog.setIcon(R.drawable.ic_roulezrose);
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
            holder.aPresenceView.setText((String) holder.aStaffeur.lirePresence());
            holder.aStatView.setText((String) holder.aStaffeur.lireStat());

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
                        if (pPremiereRandonnee) {
                            intent.putExtra(getString(R.string.randonnee), pId1);
                        }
                        else
                        {
                            intent.putExtra(getString(R.string.randonnee), pId2);
                        }
                        intent.putExtra(getString(R.string.nom), holder.aStaffeur.lireNom());
                        intent.putExtra(getString(R.string.presence), holder.aStaffeur.lirePresence());
                        intent.putExtra(getString(R.string.conducteur), holder.aStaffeur.lireConducteur());
                        intent.putExtra(getString(R.string.jaune), holder.aStaffeur.lireJaune());
                        intent.putExtra(getString(R.string.eclaireur), holder.aStaffeur.lireEclaireur());
                        intent.putExtra(getString(R.string.meneur), holder.aStaffeur.lireMeneur());
                        intent.putExtra(getString(R.string.lanterne), holder.aStaffeur.lireLanterne());
                        intent.putExtra(getString(R.string.present), holder.aStaffeur.lirePresent());
                        context.startActivity(intent);
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
            JSONArray lListe_staffeur;
            SimpleDateFormat lSimpleDateFormatIn;
            SimpleDateFormat lSimpleDateFormatOut;
            Date lDate;
            try
            {
                lJSONString = lireDonneesDepuisURL(url[0]);
                if (lJSONString != null)
                {
                    lGlobalJSONObject = new JSONObject(lJSONString);
                    try {
                        lSimpleDateFormatIn = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        lDate = lSimpleDateFormatIn.parse(lGlobalJSONObject.getString("Date1"));
                        lSimpleDateFormatOut = new SimpleDateFormat("EEE dd MMM yyyy");
                        pDate1String = lSimpleDateFormatOut.format(lDate);
                        lDate = lSimpleDateFormatIn.parse(lGlobalJSONObject.getString("Date2"));
                        pDate2String = lSimpleDateFormatOut.format(lDate);
                    } catch (Exception e) {
                        pDate1String = "Prochaine randonnée";
                        pDate2String = "Prochaine randonnée";
                    }
                    pId1 = lGlobalJSONObject.getInt("Id1");
                    pId2 = lGlobalJSONObject.getInt("Id2");
                    lListe_staffeur = lGlobalJSONObject.getJSONArray("Staffeurs1");
                    for (int i = 0; i < lListe_staffeur.length(); i++) {
                        Staffeur lStaffeur;
                        lJSONObjet = lListe_staffeur.getJSONObject(i);
                        lStaffeur = new Staffeur(lJSONObjet.getString(getString(R.string.nom)),
                                lJSONObjet.getString(getString(R.string.presence)),
                                lJSONObjet.getInt(getString(R.string.conducteur)),
                                lJSONObjet.getInt(getString(R.string.jaune)),
                                lJSONObjet.getInt(getString(R.string.eclaireur)),
                                lJSONObjet.getInt(getString(R.string.meneur)),
                                lJSONObjet.getInt(getString(R.string.lanterne)),
                                lJSONObjet.getInt(getString(R.string.present)));
                        pListeStaffeur1.add(lStaffeur);
                    }
                    lListe_staffeur = lGlobalJSONObject.getJSONArray("Staffeurs2");
                    for (int i = 0; i < lListe_staffeur.length(); i++) {
                        Staffeur lStaffeur;
                        lJSONObjet = lListe_staffeur.getJSONObject(i);
                        lStaffeur = new Staffeur(lJSONObjet.getString(getString(R.string.nom)),
                                lJSONObjet.getString(getString(R.string.presence)),
                                lJSONObjet.getInt(getString(R.string.conducteur)),
                                lJSONObjet.getInt(getString(R.string.jaune)),
                                lJSONObjet.getInt(getString(R.string.eclaireur)),
                                lJSONObjet.getInt(getString(R.string.meneur)),
                                lJSONObjet.getInt(getString(R.string.lanterne)),
                                lJSONObjet.getInt(getString(R.string.present)));
                        pListeStaffeur2.add(lStaffeur);
                    }
                }
                else
                {
                    pDate1String = "Impossible d'accéder au serveur de données";
                    pDate2String = "Impossible d'accéder au serveur de données";
                }
            }
            catch(IOException e)
            {
                pDate1String = e.toString();
                pDate2String = e.toString();
                Log.d("Background Task", e.toString());
            }
            catch(JSONException e)
            {
                pDate1String = "Erreur JSON";
                pDate2String = "Erreur JSON";
                e.printStackTrace();
            }
            return(pDate1String);
        }

        @Override
        protected void onPostExecute(String result) {
            pTextViewDate.setText(result);
            // TODO : n'afficher que si la chaine result est différente de null
            pSimpleItemRecyclerViewAdapter1 = new SimpleItemRecyclerViewAdapter(pListeStaffeur1);
            pSimpleItemRecyclerViewAdapter2 = new SimpleItemRecyclerViewAdapter(pListeStaffeur2);
            ((RecyclerView)pRecyclerView).setAdapter(pSimpleItemRecyclerViewAdapter1);
        }
    }
}
