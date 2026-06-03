package ihm;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;

import controleur.Controleur;

import metier.Sommet;
import metier.Plateau;

import java.util.List;
import javax.swing.*;

public class Tronçons extends JPanel
{
	private Controleur ctrl;
	private Color      couleur;
	private Graphics2D trait;
	private Sommet     Debut;
	private Sommet     Fin;


	public Tronçons(Controleur ctrl)
	{
		this(ctrl,Color.BLACK);
		
	}
	public Tronçons(Controleur ctrl,Color couleur)
	{
		this.ctrl    = ctrl;
		this.couleur = couleur;
		this.Debut   = null;
		this.Fin     = null;
	}

	public void paintComponent(Graphics g)			
	{
		List<Sommet> sommetlist = this.ctrl.getPlateau().getSommets();
		super.paintComponent(g);

		trait = (Graphics2D) g;
		
		for (Sommet s : sommetlist)
		{
			Debut = s;
			for (Voisins v : this.ctrl.getPlateau().getVoisins(s))
			{
				Fin   = v;
				try 
				{
					trait.drawLine( this.Debut.getX(), this.Debut.getY(), this.Fin.getX(), this.Fin.getY());
				} catch (Exception e){}
			}
		}	
	}


	public Color  getCouleur(){return this.couleur;}


	public void   setCouleur (Color    Couleur){this.couleur = Couleur;}
	public void   setDebut   (Sommet   Debut  ){this.Debut   = Debut  ;}
	public void   setFin     (Sommet   Fin    ){this.Fin     = Fin  ;}

}
