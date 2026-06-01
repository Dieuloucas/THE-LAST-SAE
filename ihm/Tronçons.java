package ihm;

import java.awt.Color;

public class Tronçons 
{
	private Color couleur;
	private String Orientation;

	public Tronçons()
	{
		this(Color.RED);
	}
	public Tronçons(Color couleur)
	{
		this(couleur,"Droit");

	}
	public Tronçons(Color couleur,String Orientation)
	{
		this.couleur = couleur;
		this.Orientation = Orientation;

	}
	public Color getCouleur(){return this.couleur;};
	public void setCouleur(Color couleur){this.couleur = couleur;}
}
