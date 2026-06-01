package Controleur;

import ihm.FrameTransport;

public class Controleur
{
	private FrameTransport frame;

	public Controleur()
	{
		this.frame = new FrameTransport(this);
	}
	public static void main(String[] args) 
	{
		new Controleur();	
	}
}