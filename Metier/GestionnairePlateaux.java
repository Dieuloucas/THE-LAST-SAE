package Metier;

import java.util.*;

public class GestionnairePlateaux
{
    private List<Plateau> plateaux   = new ArrayList<>();
    private int           prochainId = 0;        // l'auto-incrément vit ICI

    public Plateau creer(int lignes, int colonnes, String nom)
    {
        Plateau p = new Plateau(lignes, colonnes);
        p.setId(prochainId++);                   // on attribue puis on incrémente
        p.setNom(nom);
        plateaux.add(p);
        return p;
    }

    public Plateau dupliquer(Plateau original, String nouveauNom)
    {
        Plateau copie = original.dupliquer();    // la copie profonde
        copie.setId(prochainId++);
        copie.setNom(nouveauNom);
        plateaux.add(copie);
        return copie;
    }

    public void supprimer(Plateau p) { plateaux.remove(p); }

    public Plateau getParId(int id)              // pour retrouver un plateau dans le menu
    {
        for (Plateau p : plateaux)
            if (p.getId() == id)
                return p;
        return null;
    }

    public List<Plateau> getPlateaux() { return plateaux; }   // pour lister dans le menu
}
