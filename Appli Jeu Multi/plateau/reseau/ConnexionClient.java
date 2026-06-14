package plateau.reseau;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ConnexionClient extends Thread
{
	private Socket socket;
	private ServeurReseau serveur;
	private int idJoueur;
	private BufferedReader lecteur;
	private PrintWriter ecrivain;

	public ConnexionClient(Socket socket, ServeurReseau serveur, int idJoueur)
	{
		this.socket = socket;
		this.serveur = serveur;
		this.idJoueur = idJoueur;
		try
		{
			this.lecteur = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			this.ecrivain = new PrintWriter(socket.getOutputStream(), true);
		}
		catch (IOException e)
		{
			System.err.println("Erreur flux client : " + e.getMessage());
		}
	}

	@Override
	public void run()
	{
		try
		{
			String requete;
			while ((requete = this.lecteur.readLine()) != null)
			{
				this.serveur.gererAction(requete, this.idJoueur);
			}
		}
		catch (IOException e)
		{
			System.err.println("Connexion perdue avec Joueur " + this.idJoueur);
		}
		finally
		{
			this.serveur.deconnecter(this);
			try
			{
				this.socket.close();
			}
			catch (IOException e) {}
		}
	}

	public void envoyer(String message)
	{
		if (this.ecrivain != null)
		{
			this.ecrivain.println(message);
		}
	}

	public int getIdJoueur()
	{
		return this.idJoueur;
	}
}
