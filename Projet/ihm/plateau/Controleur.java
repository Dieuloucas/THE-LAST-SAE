package plateau;

import plateau.ihm.FrameAcceuil;
import plateau.metier.Acceuil;

public class Controleur
{
	private FrameAcceuil ihm;
	private Acceuil      metier;

	public Controleur()
	{
		this.metier = new Acceuil();
		this.ihm    = new FrameAcceuil ( this );
	}

	public static void main(String[] a)
	{
		new Controleur();
	}
}