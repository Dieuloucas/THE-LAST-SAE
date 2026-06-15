package plateau.reseau;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import plateau.metier.Plateau;

public class ServeurReseau extends Thread
{
	private int port;
	private ServerSocket serverSocket;
	private ArrayList<ConnexionClient> clients;
	private Plateau plateau;
	private int nbManches;
	private int[][] aretesOccupees; 
	private int nbJoueursAttendus;
	private boolean partieLancee;
	
<<<<<<< HEAD
	private String nomPlateau;

=======
	private String nomPlateau; // AJOUT : Nom du fichier choisi par l'hôte

	// AJOUT : Le constructeur prend désormais le nom du plateau
>>>>>>> 593288d (Amélioration)
	public ServeurReseau(int port, Plateau plateau, int nbManches, String nomPlateau)
	{
		this.port = port;
		this.plateau = plateau;
		this.nbManches = nbManches;
		this.nomPlateau = nomPlateau;
		this.clients = new ArrayList<>();
		this.partieLancee = false;

		int taille = plateau.getLargeur() * plateau.getHauteur();
		this.aretesOccupees = new int[taille][taille];

		int maxDepart = 0;
		for (int i = 0; i < taille; i++)
			if (plateau.getDepart(i) > maxDepart) maxDepart = plateau.getDepart(i);
			
		this.nbJoueursAttendus = maxDepart;
	}

	@Override
	public void run()
	{
		try
		{
			this.serverSocket = new ServerSocket(this.port);
			System.out.println("Serveur démarré sur le port " + this.port);

			while (this.clients.size() < this.nbJoueursAttendus)
			{
				Socket socket = this.serverSocket.accept();
				int idJoueur = this.clients.size() + 1;

				ConnexionClient client = new ConnexionClient(socket, this, idJoueur);
				this.clients.add(client);
				client.start();

				client.envoyer("ACCEPT_ID;" + idJoueur + ";" + this.nbJoueursAttendus);
				
<<<<<<< HEAD
				// Informer tout le monde de l'avancée du Lobby
=======
				// --- AJOUT : Informer tout le monde de l'avancée du Lobby ---
>>>>>>> 593288d (Amélioration)
				int restants = this.nbJoueursAttendus - this.clients.size();
				this.diffuser("LOBBY_MAJ;" + restants + ";" + this.nomPlateau);

				if (this.clients.size() == this.nbJoueursAttendus)
				{
					this.partieLancee = true;
<<<<<<< HEAD
					// On envoie aussi le nom du fichier pour que les invités le chargent
=======
					// AJOUT : On envoie aussi le nom du fichier pour que les invités le chargent
>>>>>>> 593288d (Amélioration)
					this.diffuser("START_GAME;" + this.nbManches + ";" + this.nomPlateau);
				}
			}
		}
		catch (IOException e)
		{
			System.err.println("Erreur serveur : " + e.getMessage());
		}
	}

	public synchronized void diffuser(String message)
	{
		for (ConnexionClient c : this.clients) c.envoyer(message);
	}

	public synchronized void gererAction(String message, int idJoueur)
	{
		String[] segments = message.split(";");
		String commande = segments[0];

		if (commande.equals("TENTATIVE_COUP"))
		{
			int source = Integer.parseInt(segments[1]);
			int destination = Integer.parseInt(segments[2]);

			if (this.aretesOccupees[source][destination] == 0 && this.aretesOccupees[destination][source] == 0)
			{
				this.aretesOccupees[source][destination] = idJoueur;
				this.aretesOccupees[destination][source] = idJoueur;
				this.diffuser("VALIDE_COUP;" + idJoueur + ";" + source + ";" + destination);
			}
			else
			{
				for (ConnexionClient c : this.clients)
					if (c.getIdJoueur() == idJoueur)
					{
						c.envoyer("REFUS_COUP;Ce chemin est déjà occupé !");
						break;
					}
			}
		}
		else if (commande.equals("PASSER")) this.diffuser("JOUEUR_PASSE;" + idJoueur);
		else if (commande.equals("PIOCER_CARTE")) this.diffuser(message);
	}

	public synchronized void deconnecter(ConnexionClient client) { this.clients.remove(client); }
	public int getPort() { return this.port; }
<<<<<<< HEAD
}
=======
}
>>>>>>> 593288d (Amélioration)
