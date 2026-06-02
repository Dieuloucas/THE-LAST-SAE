package ihm;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;

import Controleur.Controleur;
import Metier.Arete;

import java.util.List;
import javax.swing.*;

public class Tronçons extends JPanel
{
	private Controleur ctrl;
	private Color      couleur;
	private Graphics2D trait;
	private Stations   Debut;
	private Stations   Fin;


	private Tronçons(Controleur ctrl)
	{
		this(Color.BLACK,ctrl);
		this.ctrl    = ctrl;
	}
	private Tronçons(Color couleur,Controleur ctrl)
	{
		this.ctrl    = ctrl;
		this.couleur = couleur;
		this.Debut   = null;
		this.Fin     = null;
	}
	public Tronçons CreerTronçons()
	{
		Tronçons tr;
		String message; 

		if (this.Debut.getX() == this.Fin.getX() || this.Debut.getY() == this.Fin.getY()) tr = null;
		
		try
		{
			System.out.println("La créations du Panel a bien été effectué");
			return tr;
		}catch(Exception e){message = }
	}
	public Tronçons CreerTronçons(Color couleur)
	{
		Tronçons res = null;
		if (couleur == null) res = null;
		if (this.Debut.getX() == this.Fin.getX() || this.Debut.getY() == this.Fin.getY()) res = null; 
		return res;
	}

	public void paintComponent(Graphics g)			
	{
		List<Arete> aretelist = this.ctrl.getPlateau().getAretes();
		super.paintComponent(g);

		trait = (Graphics2D) g;
		
		for (Arete arr : aretelist)
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
