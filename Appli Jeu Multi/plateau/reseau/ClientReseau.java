package plateau.reseau;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import plateau.Controleur;

public class ClientReseau
{
	private String ip;
	private int port;
	private Socket socket;
	private PrintWriter ecrivain;
	private Controleur ctrl;
	private EcouteurServeur ecouteur;

	public ClientReseau(String ip, int port, Controleur ctrl)
	{
		this.ip = ip;
		this.port = port;
		this.ctrl = ctrl;
	}

	public boolean connecter()
	{
		try
		{
			this.socket = new Socket(this.ip, this.port);
			this.ecrivain = new PrintWriter(this.socket.getOutputStream(), true);

			this.ecouteur = new EcouteurServeur(this.socket, this.ctrl);
			this.ecouteur.execute();

			return true;
		}
		catch (IOException e)
		{
			return false;
		}
	}

	public void envoyerAction(String action)
	{
		if (this.ecrivain != null)
		{
			this.ecrivain.println(action);
		}
	}

	public void deconnecter()
	{
		try
		{
			if (this.ecouteur != null) this.ecouteur.cancel(true);
			if (this.socket != null) this.socket.close();
		}
		catch (IOException e) {}
	}
}
