package com.assistanceinformatiquetoulouse.roulezrose.staff;

// Class Staffeur
public class Staffeur {
    // Attributs privés
    private String pNom;
    private int pId;
    private String pPresence;
    private int pPoste;
    private String pStat;
    private int pConducteur;
    private int pJaune;
    private int pEclaireur;
    private int pMeneur;
    private int pLanterne;
    private int pBinome;
    private int pPresent;

    // Constructeur
    public Staffeur()
    {
        this.pNom = "";
        this.pId = 0;
        this.pPresence = "";
        this.pPoste = 0;
        this.pStat = "";
        this.pConducteur = 0;
        this.pJaune = 0;
        this.pEclaireur = 0;
        this.pMeneur = 0;
        this.pLanterne = 0;
        this.pBinome = 0;
        this.pPresent = 0;
    }

    // Constructeur
    public Staffeur(String nom,
                    int id,
                    String presence,
                    int poste,
                    int conducteur,
                    int jaune,
                    int eclaireur,
                    int meneur,
                    int lanterne,
                    int binome,
                    int present)
    {
        float pourcentage;
        this.pNom = nom;
        this.pId = id;
        this.pPresence = presence;
        this.pPoste = poste;
        this.pConducteur = conducteur;
        this.pJaune = jaune;
        this.pEclaireur = eclaireur;
        this.pMeneur = meneur;
        this.pLanterne = lanterne;
        this.pBinome = binome;
        present = conducteur + jaune + eclaireur + meneur + lanterne + binome;
        this.pPresent = present;
        if (present != 0) {
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

    // Methode lireId
    public int lireId() {
        return(this.pId);
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

    // Méthode lireJaune
    public int lireJaune() {
        return(this.pJaune);
    }

    // Méthode lireEclaireur
    public int lireEclaireur() {
        return(this.pEclaireur);
    }

    // Méthode lireMeneur
    public int lireMeneur() {
        return(this.pMeneur);
    }

    // Méthode lireLanterne
    public int lireLanterne() {
        return(this.pLanterne);
    }

    // Méthode lireBinome
    public int lireBinome() {
        return(this.pBinome);
    }

    // Méthode lirePrésent
    public int lirePresent () {
        return(this.pPresent);
    }

    // Méthode isPresent
    public boolean isPresent() {
        if (this.pPresence.contains("présent")) {
            return(true);
        }
        else {
            return(false);
        }
    }

    // Méthode lirePoste
    public int lirePoste() {
        return(this.pPoste);
    }
}
