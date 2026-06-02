package Metier;

public class Sommet
{
	private static int  nbArrondissement;
	private        int  numéroArrondissement; 
	private        Zone zone;
	private Sommet()
	{
		this.numéroArrondissement = ++Sommet.nbArrondissement;
	}
	public Sommet CreerArrondissements()
	{
		return null;
	}
	public void setZone(Zone zone)
	{
		this.zone = zone;
	}
	public Zone getZone(){return zone;}
}
