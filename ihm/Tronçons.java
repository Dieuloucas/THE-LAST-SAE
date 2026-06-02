package ihm;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import javax.swing.*;

public class Tronçons extends JPanel
{
	private Color      couleur;
	private Graphics2D trait;
	private Stations   Debut;
	private Stations   Fin;

	private Tronçons()
	{
		this(Color.BLACK);
	}
	private Tronçons(Color couleur)
	{
		this.couleur = couleur;
		this.Debut   = null;
		this.Fin     = null;
	}
	public Tronçons CreerTronçons()
	{
		Tronçons res = null;
		if (this.Debut.getX() == this.Fin.getX() || this.Debut.getY() == this.Fin.getY()) res = null;
		
		if (res == null)
			System.out.println("Le Tronçons n'a pas pus être crée car plusieurs tronçons se croisent");
		return res;
	}
	public Tronçons CreerTronçons(Color couleur)
	{
		Tronçons res = null;
		if (couleur == null) res = null;
		if (this.Debut.getX() == this.Fin.getX() || this.Debut.getY() == this.Fin.getY()) res = null;
		if res 
		return res;
	}

	public void paintComponent(Graphics g)			
	{
		super.paintComponent(g);

		trait = (Graphics2D) g;
		
		
		try 
		{

			trait.drawLine( this.Debut.getX(), this.Debut.getY(), this.Fin.getX(), this.Fin.getY());

		} catch (Exception e) {}

		
	}


	public Color  getCouleur(){return this.couleur;}


	public void   setCouleur (Color    Couleur){this.couleur = Couleur;}
	public void   setDebut   (Stations Debut  ){this.Debut   = Debut  ;}
	public void   setFin     (Stations Debut  ){this.Debut   = Debut  ;}

}
