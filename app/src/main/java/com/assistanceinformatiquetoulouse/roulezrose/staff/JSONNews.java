package com.assistanceinformatiquetoulouse.roulezrose.staff;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

// Class JSONNews
public class JSONNews {
    // Attributs privés
    private String pURL;

    // Constructeur
    public JSONNews(String url) {
        pURL = url;
    }

    // Méthode analyser
	public List<HashMap<String,Object>> analyser(JSONArray array) {
        int i;
        List<HashMap<String, Object>> lListeNews = new ArrayList<HashMap<String,Object>>();
        HashMap<String, Object> lHashMap = null;	

        for(i=0;i < array.length();i++){
            try {
                lHashMap = lireUneNews((JSONObject) array.get(i));
                lListeNews.add(lHashMap);
            }
            catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return(lListeNews);
    }

	// Méthode lireUneNews
	private HashMap<String, Object> lireUneNews(JSONObject object) {
        HashMap<String, Object> lNews = new HashMap<String, Object>();

		try {
            // Récupère les chaines de caractères dans l'objet JSON
            String titre = object.getString("titre");
            String date = object.getString("date");
            String contenu = object.getString("contenu");
            String image = object.getString("image");
            // Ecrit les chaines de caractères dans l'objet News de type HashMap
            lNews.put("titre", titre);
            lNews.put("date", date);
            lNews.put("image_id", R.drawable.blank);
            lNews.put("image", pURL + "/" + image);
            lNews.put("contenu", contenu);
        }
        catch (JSONException e) {			
            e.printStackTrace();
        }		
        return(lNews);
    }
}