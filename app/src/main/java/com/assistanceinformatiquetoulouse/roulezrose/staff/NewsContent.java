package com.assistanceinformatiquetoulouse.roulezrose.staff;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

// Class NewsContent
public class NewsContent {
    // Attributs privés
    public static List<HashMap<String, Object>> aListItem = new ArrayList<HashMap<String, Object>>();

    // Méthode addNewsItem
    public static void addNewsItem(HashMap<String, Object> hashMap) {
        // Ajoute une news sous forme d'objet HashMap dans la list des news
        aListItem.add(hashMap);
    }

    // Méthode updateImage
    public static void updateImage(int position, String image) {
        // Change la localisation (nouveau chemin d'accès) de l'image une fois que celle ci a été téléchargée
        aListItem.get(position).put("image", image);
    }

    // Sous class NewsItem
    public static class NewsItem {
// TODO : passer les attributs en privés
        // Attributs publics
        public String pTitre;
        public String pDate;
        public String pContenu;
        public String pImage;

        // Constructeur
        public NewsItem(String titre, String date, String contenu) {
            this.pTitre = titre;
            this.pDate = date;
            this.pContenu = contenu;
            // La localisation de l'image sera écrite lorsqu'elle aura été téléchargée
            this.pImage = "";
        }
        // Constructeur
        public NewsItem(String titre, String date, String contenu, String image) {
            this.pTitre = titre;
            this.pDate = date;
            this.pContenu = contenu;
            this.pImage = image;
        }
    }
}
