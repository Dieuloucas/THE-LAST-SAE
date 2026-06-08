package plateau.ihm;

import java.awt.Color;

// Donnees d'affichage partagees par les ecrans (evite de dupliquer la palette).
public class Couleurs
{
	// une couleur par arrondissement possible (20)
	public static final Color[] PALETTE =
	{
		new Color(255,105,180), new Color(138, 43,226), new Color(255,165,  0), new Color(  0,255,255),
		new Color(176,196,222), new Color(255, 20,147), new Color(186, 85,211), new Color(148,  0,211),
		new Color(210,105, 30), new Color(244,164, 96), new Color(199, 21,133), new Color(123,104,238),
		new Color(255,140,  0), new Color(  0,206,209), new Color( 72, 61,139), new Color(220, 20, 60),
		new Color(169,169,169), new Color(245,222,179), new Color(255,228,225), new Color(255,192,203)
	};

	// noms affiches pour les types de station (1..6)
	public static final String[] NOMS_STATIONS =
	{
		"Tour Eiffel", "Moulin Rouge", "Louvre", "Restaurant", "Gare", "Aeroport"
	};
}
