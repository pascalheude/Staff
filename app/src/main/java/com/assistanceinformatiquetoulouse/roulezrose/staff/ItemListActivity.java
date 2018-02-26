package com.assistanceinformatiquetoulouse.roulezrose.staff;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

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
                lAlertDialog.setMessage("Affichage de la présence des staffeurs\n© AIT 2018 (pascalh)\n\nassistanceinformatiquetoulouse@gmail.com");
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
        // TBR : private final List<HashMap<String, Object>> pListeStaffeur1;

        public SimpleItemRecyclerViewAdapter(ArrayList<Staffeur> liste_staffeur) {
        // TBR : public SimpleItemRecyclerViewAdapter(List<HashMap<String, Object>> items) {
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
            String lImage;
            holder.aStaffeur = pListeStaffeur.get(position);
            holder.aNomView.setText((String) holder.aStaffeur.lireNom());
            // TBR : holder.aNomView.setText((String) holder.aStaffeur.get("titre"));
            holder.aPresenceView.setText((String) holder.aStaffeur.lirePresence());
            holder.aConducteurView.setText((String) holder.aStaffeur.lireConducteur());
            // TBR : holder.aPresenceView.setText((String) holder.aStaffeur.get("date"));
            // TBR : lImage = (String) holder.aStaffeur.get("image");
            // TBR : Bitmap lBitmap = BitmapFactory.decodeFile(lImage);
            // TBR : holder.mImageView.setImageBitmap(lBitmap);

            holder.aView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mTwoPane) {
                        Bundle arguments = new Bundle();
                        arguments.putInt(ItemDetailFragment.ARG_ITEM_ID, position);
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
                        intent.putExtra(ItemDetailFragment.ARG_ITEM_ID, position);
                        //intent.putExtra(ItemDetailFragment.ARG_ITEM_ID, (String) holder.aStaffeur.get("titre"));
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
            public TextView aConducteurView;
            public ImageView mImageView;
            public Staffeur aStaffeur;
            // TBR : public HashMap<String, Object> aStaffeur;

            public ViewHolder(View view) {
                super(view);
                aView = view;
                aNomView = (TextView) view.findViewById(R.id.nom);
                aPresenceView = (TextView) view.findViewById(R.id.presence);
                aConducteurView = (TextView) view.findViewById(R.id.conducteur);
                // TBR : mImageView = (ImageView) view.findViewById(R.id.imageViewNews);
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
            // Creer une communication http pour communiquer avec l'URL
            HttpURLConnection lHttpURLConnection = (HttpURLConnection) lURL.openConnection();
            // Connexion à l'URL
            lHttpURLConnection.connect();
            // Lire le flux depuis la connexion
            lInputStream = lHttpURLConnection.getInputStream();
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
                    lListe_staffeur = lGlobalJSONObject.getJSONArray("Staffeurs1");
                    for (int i = 0; i < lListe_staffeur.length(); i++) {
                        Staffeur lStaffeur;
                        lJSONObjet = lListe_staffeur.getJSONObject(i);
                        lStaffeur = new Staffeur(lJSONObjet.getString("nom"),
                                lJSONObjet.getString("présence"),
                                lJSONObjet.getString("conducteur"));
                        pListeStaffeur1.add(lStaffeur);
                    }
                    lListe_staffeur = lGlobalJSONObject.getJSONArray("Staffeurs2");
                    for (int i = 0; i < lListe_staffeur.length(); i++) {
                        Staffeur lStaffeur;
                        lJSONObjet = lListe_staffeur.getJSONObject(i);
                        lStaffeur = new Staffeur(lJSONObjet.getString("nom"),
                                lJSONObjet.getString("présence"),
                                lJSONObjet.getString("conducteur"));
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
            // Analyser les données reçues et charger les images
            // TBR : ListViewLoaderTask lListViewLoaderTask = new ListViewLoaderTask();
            // TBR : lListViewLoaderTask.execute(result);
        }
    }

    // Private class ListViewLoaderTask
    // SimpleAdapter private class ListViewLoaderTask  extends AsyncTask<String, Void, SimpleAdapter> {
    private class ListViewLoaderTask  extends AsyncTask<String, Void, List<HashMap<String, Object>>> {
        // Attributs privés
        JSONArray pJSONArray;

        @Override
        // SimpleAdapter protected SimpleAdapter doInBackground(String... chaine) {
        protected List<HashMap<String, Object>> doInBackground(String... chaine) {
            // A list object to store the parsed countries list
            List<HashMap<String, Object>> liste_news = null;
            try {
                pJSONArray = new JSONArray(chaine[0]);
                // Instantiating json parser class
                JSONNews lJSONNews = new JSONNews(pURL);
                // Getting the parsed data as a List construct
                liste_news = lJSONNews.analyser(pJSONArray);
            }
            catch(Exception e) {
                Log.d("Exception", e.toString());
            }
            // SimpleAdapter Keys used in Hashmap
            // SimpleAdapter String[] from = { "titre", "image", "date"};
            // SimpleAdapter Ids of views in listview_layout
            // SimpleAdapter int[] to = { R.titre.textViewNewsTitre, R.titre.imageViewNews, R.titre.textViewNewsDate};
            // SimpleAdapter Instantiating an adapter to store each items
            // SimpleAdapter R.layout.listview_layout defines the layout of each item
            // SimpleAdapter SimpleAdapter adapter = new SimpleAdapter(getBaseContext(), liste_news, R.layout.listview_news, from, to);
            // SimpleAdapter return(adapter);
            return(liste_news);
        }

        @Override
        // SimpleAdapter protected void onPostExecute(SimpleAdapter adapter) {
        protected void onPostExecute(List<HashMap<String, Object>> hashMapList) {
            int i;
            // SimpleAdapter Mise en place de l'adapter pour la liste des news
            // SimpleAdapter pListView.setAdapter(adapter);
            // Chargement des toutes les images par une boucle
            // SimpleAdapter for(i=0;i < adapter.getCount();i++) {
            for(i=0;i < hashMapList.size();i++) {
                // SimpleAdapter HashMap<String, Object> lHashMap = (HashMap<String, Object>) adapter.getItem(i);
                HashMap<String, Object> lHashMap = (HashMap<String, Object>) hashMapList.get(i);
                NewsContent.addNewsItem(lHashMap);
                String lImageURL = (String) lHashMap.get("image");
                ImageLoaderTask imageLoaderTask = new ImageLoaderTask();
                lHashMap.put("image", lImageURL);
                lHashMap.put("position", i);
                // Charger l'image et mettre à jour la liste des news
                imageLoaderTask.execute(lHashMap);
            }
        }
    }

    // Private class ImageLoaderTask
    private class ImageLoaderTask extends AsyncTask<HashMap<String, Object>, Void, HashMap<String, Object>>{
        @Override
        protected HashMap<String, Object> doInBackground(HashMap<String, Object>... hashMap) {
            InputStream lInputStream = null;
            String lImageURL = (String) hashMap[0].get("image");
            int position = (Integer) hashMap[0].get("position");
            URL lURL;
            try {
                lURL = new URL(lImageURL);

                // Création d'une connexion http avec l'URL
                HttpURLConnection urlConnection = (HttpURLConnection) lURL.openConnection();
                // Connexion à l'URL
                urlConnection.connect();
                // Téléchargement des données contenues (image) dans l'URL
                lInputStream = urlConnection.getInputStream();

                // Copie de l'image dans un répertoire local
                File cacheDirectory = getBaseContext().getCacheDir();
                File lFile = new File(cacheDirectory.getPath() + "/image_" + position + ".png");
                FileOutputStream lFileOutputStream = new FileOutputStream(lFile);
                Bitmap lBitmap = BitmapFactory.decodeStream(lInputStream);
                lBitmap.compress(Bitmap.CompressFormat.PNG, 100, lFileOutputStream);
                lFileOutputStream.flush();
                lFileOutputStream.close();

                // Création d'un HashMap pour mémoriser le chemin d'accès à l'image et sa position dans la ListView
                HashMap<String, Object> lHashMap = new HashMap<String, Object>();
                lHashMap.put("image", lFile.getPath());
                lHashMap.put("position", position);

                // Renvoie le HashMap contenant le chemin d'accès à l'image et la position
                return(lHashMap);
            }
            catch (Exception e) {
                e.printStackTrace();
            }
            return(null);
        }

        @Override
        protected void onPostExecute(HashMap<String, Object> result) {
            // Lit le chemin d'accès à l'image téléchargée
            String lChemin = (String) result.get("image");
            // Lit la position de l'image téléchargée dans la ListView
            int position = (Integer) result.get("position");
            // SimpleAdapter Récupère le HashMap associé dans l'Apadater de la ListVieww
            // SimpleAdapter SimpleAdapter lSimpleAdapter = (SimpleAdapter) pListView.getAdapter();
            // SimpleAdapter HashMap<String, Object> lHashMap = (HashMap<String, Object>) lSimpleAdapter.getItem(position);
            // Ré-écrit le chemin d'accès à l'image téléchargée dans le HashMap
            HashMap<String, Object> lHashMap = result;
            lHashMap.put("image", lChemin);
            NewsContent.updateImage(position, lChemin);
            // Mise à jour de la ListView
            // SimpleAdapter lSimpleAdapter.notifyDataSetChanged();
            setupRecyclerView((RecyclerView) pRecyclerView);
        }
    }
}
