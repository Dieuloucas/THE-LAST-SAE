package metier;

import java.util.*;

public class Zone
{
    private int         id;
    private List<int[]> cases = new ArrayList<>();

    Zone(int id) { this.id = id; }

    public int getId() { return id; }

    public void ajouterCase(int ligne, int colonne)
    {
        if (!contientCase(ligne, colonne))
            cases.add(new int[]{ligne, colonne});
    }

    public boolean contientCase(int ligne, int colonne)
    {
        for (int[] c : cases)
            if (c[0] == ligne && c[1] == colonne)
                return true;
        return false;
    }

    public List<int[]> getCases()  { return cases; }
    public int         getTaille() { return cases.size(); } // taille = nombre de cases
}
