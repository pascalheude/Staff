package com.assistanceinformatiquetoulouse.roulezrose.staff;

import android.graphics.Color;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

// Class ConfigurationPoste
public class ConfigurationPoste {
    // Attributs privés
    HashMap<Integer, Integer> pHashMapCouleurFond;
    HashMap<Integer, String> pHashMapPoste;
    ArrayList<String> pListPoste;

    // Constructeur
    public ConfigurationPoste() {
        pHashMapCouleurFond = new HashMap<>();
        pHashMapPoste = new HashMap<>();
        pListPoste = new ArrayList<String>();
        pListPoste.add(0, "Absent / Indécis");
    }

    // Méthode effacerRandonnee
    public void effacerPoste() {
        pHashMapCouleurFond.clear();
        pHashMapPoste.clear();
        pListPoste.add(0, "Absent / Indécis");
    }

    // Méthode ajouterPoste
    public void ajouterPoste(int id, String label, String couleur) {
        pHashMapCouleurFond.put(id, Color.parseColor(couleur));
        pHashMapPoste.put(id, label);
        pListPoste.add(id + 1, label);
    }
    public void ajouterPoste(int id, String label, int couleur) {
        pHashMapCouleurFond.put(id, couleur);
        pHashMapPoste.put(id, label);
        pListPoste.add(id + 1, label);
    }

    // Méthode lireCouleur
    public int lireCouleur(int id) {
        if (pHashMapCouleurFond.containsKey(id)) {
            return (pHashMapCouleurFond.get(id));
        }
        else {
            return (0);
        }
    }

    // Méthode lirePoste
    public String lirePoste(int id) {
        if (pHashMapPoste.containsKey(id)) {
            return (pHashMapPoste.get(id));
        }
        else {
            return ("Inconnu");
        }
    }

    // Méthode lireListePoste
    public ArrayList<String> lireListePoste() {
        return(pListPoste);
    }
}
