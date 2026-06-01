package ihm;
public class Tronçons 
{
	private String couleur;
	private String Orientation;

	public Tronçons()
	{
		this("Rouge");
	}
	public Tronçons(String couleur)
	{
		this(couleur,"Droit");

	}
	public Tronçons(String couleur,String Orientation)
	{
		this.couleur = couleur;
		this.Orientation = Orientation;

	}
	public String getCouleur(){return this.couleur;};
}
