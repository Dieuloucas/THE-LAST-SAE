package plateau.reseau;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.List;
import javax.swing.SwingWorker;
import plateau.Controleur;

public class EcouteurServeur extends SwingWorker<Void, String>
{
	private Socket socket;
	private BufferedReader lecteur;
	private Controleur ctrl;

	public EcouteurServeur(Socket socket, Controleur ctrl)
	{
		this.socket = socket;
		this.ctrl = ctrl;
		try
		{
			this.lecteur = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		}
		catch (Exception e)
		{
			System.err.println("Erreur lecteur : " + e.getMessage());
		}
	}

	@Override
	protected Void doInBackground() throws Exception
	{
		String message;
		while ((message = this.lecteur.readLine()) != null)
		{
			publish(message);
		}
		return null;
	}

	@Override
	protected void process(List<String> messages)
	{
		for (String msg : messages)
		{
			this.ctrl.traiterActionReseau(msg);
		}
	}
}
