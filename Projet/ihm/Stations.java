package ihm;

public class Stations 
{
	private Stations[] stationsAdjacente;
	private int x;
	private int y;

	public Stations(int x, int y)
	{
		this.stationsAdjacente = new Stations[8]; //8 étant le nombre de coté adjacent possible
		this.x = x;
		this.y = y;
	}
	public int getX(){return this.x;}
	public int getY(){return this.y;}
	public Stations[] getAdjacent(){return this.stationsAdjacente;}
}
