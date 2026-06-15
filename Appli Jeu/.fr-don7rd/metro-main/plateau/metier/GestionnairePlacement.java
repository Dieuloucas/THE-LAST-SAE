package plateau.metier;

import plateau.Controleur;

public class GestionnairePlacement
{
	public static void gererClic(int index, String modeSelection, Object selectionItem, Controleur ctrl)
	{
		if (selectionItem == null) return;
		String selected = selectionItem.toString();

		if (modeSelection.equals("STATION"))
		{
			if (selected.equals("Aucun"))
			{
				ctrl.affecterStation(index, 0);
			}
			else
			{
				int 		stationNum = 0;
				String[] 	nomsStations = UtilitaireJeu.getNomsStations();
				
				for (int i = 0; i < nomsStations.length; i++)
				{
					if (selected.equals(nomsStations[i])) stationNum = i + 1;
				}
				if (stationNum == 0 && selected.startsWith("Station "))
				{
					try { stationNum = Integer.parseInt(selected.substring(8)); } catch (Exception ex) {}
				}
				ctrl.affecterStation(index, stationNum);
			}
		}
		else if (modeSelection.equals("DEPART"))
		{
			if (selected.equals("Aucun"))
			{
				ctrl.affecterDepart(index, 0);
			}
			else if (selected.startsWith("Joueur "))
			{
				int numJoueur 	= Integer.parseInt(selected.substring(7));
				int taille 		= ctrl.getLargeur() * ctrl.getHauteur();
				for (int i = 0; i < taille; i++)
				{
					if (ctrl.getDepart(i) == numJoueur) ctrl.affecterDepart(i, 0);
				}
				ctrl.affecterDepart(index, numJoueur);
			}
		}
	}
}
