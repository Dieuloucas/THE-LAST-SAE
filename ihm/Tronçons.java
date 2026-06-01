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

	public Tronçons()
	{
		this(Color.BLACK);
	}
	public Tronçons(Color couleur)
	{
		this.couleur = couleur;
		this.Debut   = null;
		this.Fin     = null;
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
