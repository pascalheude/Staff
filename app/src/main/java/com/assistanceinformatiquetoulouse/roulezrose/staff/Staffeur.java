package com.assistanceinformatiquetoulouse.roulezrose.staff;

// Class Staffeur
public class Staffeur {
    // Attributs privés
    private String pNom;
    private String pPresence;
    private String pStat;
    private int pConducteur;
    private int pPresent;

    // Constructeur
    public Staffeur()
    {
        this.pNom = "";
        this.pPresence = "";
        this.pStat = "";
        this.pConducteur = 0;
        this.pPresent = 0;
    }

    // Constructeur
    public Staffeur(String nom, String presence, int conducteur, int present)
    {
        float pourcentage;
        this.pNom = nom;
        this.pPresence = presence;
        this.pConducteur = conducteur;
        this.pPresent = present;
        if ((conducteur != 0) && (present != 0)) {
            pourcentage = (float) 100.0 * (float) conducteur / (float) present;
            this.pStat = String.valueOf(conducteur) +
                         " / " +
                         String.valueOf(present) +
                         " (" + String.valueOf((int) pourcentage) +
                         "%)";
        }
        else {
            this.pStat = String.valueOf(conducteur) +
                         " / " +
                         String.valueOf(present);
        }
    }

    // Méthode lireNom
    public String lireNom()
    {
        return(this.pNom);
    }

    // Méthode lirePresence
    public String lirePresence()
    {
        return(this.pPresence);
    }

    // Méthode lireConducteur
    public int lireConducteur() {
        return(this.pConducteur);
    }

    // Méthode lireStat
    public String lireStat()
    {
        return(this.pStat);
    }

}
