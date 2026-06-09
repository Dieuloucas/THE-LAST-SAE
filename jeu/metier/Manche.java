package jeu.metier;

public class Manche
{
	private int   numero;
	private Pioche pioche;
	private int   indexJoueurCourant;
	private int   nbJoueurs;
	private Carte carteCourante;

	public Manche(int numero, int nbStations, int nbJoueurs)
	{
		this.numero             = numero;
		this.nbJoueurs          = nbJoueurs;
		this.indexJoueurCourant = 0;
		this.carteCourante      = null;
		this.pioche             = new Pioche(nbStations);
		this.pioche.melanger();
	}

	public void demarrer()
	{
		this.carteCourante = this.pioche.piocher();
	}

	public void passerAuJoueurSuivant()
	{
		this.indexJoueurCourant = (this.indexJoueurCourant + 1) % this.nbJoueurs;
		this.carteCourante      = this.pioche.piocher();
	}

	public boolean estTerminee()         { return this.pioche.estTerminee(); }
	public int     getNumero()           { return this.numero; }
	public Carte   getCarteCourante()    { return this.carteCourante; }
	public int     getIndexJoueurCourant(){ return this.indexJoueurCourant; }
	public Pioche  getPioche()           { return this.pioche; }
}
