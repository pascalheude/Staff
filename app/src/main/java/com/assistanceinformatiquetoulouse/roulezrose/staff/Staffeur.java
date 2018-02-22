package com.assistanceinformatiquetoulouse.roulezrose.staff;

// Class Staffeur
public class Staffeur {
    // Attributs privés
    private String pNom;
    private String pPresence;
    private String pConducteur;

    // Constructeur
    public Staffeur()
    {
        this.pNom = "";
        this.pPresence = "";
        this.pConducteur = "";
    }

    // Constructeur
    public Staffeur(String nom, String presence, String conducteur)
    {
        this.pNom = nom;
        this.pPresence = presence;
        this.pConducteur = conducteur;
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
    public String lireConducteur()
    {
        return(this.pConducteur);
    }

}
