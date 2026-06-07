package plateau.ihm;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JPanel;

import plateau.Controleur;

public class PanelAccueil extends JPanel implements ActionListener
{
	private FrameAccueil frmAccueil;
	private Controleur   ctrl;

	private JButton btnConfiguration;
	private JButton btnRegles;

	public PanelAccueil(FrameAccueil frmAccueil, Controleur ctrl)
	{
		this.frmAccueil = frmAccueil;
		this.ctrl       = ctrl;

		this.setLayout(new GridLayout(2, 1, 10, 10));

		this.btnConfiguration = new JButton("Configuration du plateau");
		this.btnRegles        = new JButton("Regles");

		this.add(this.btnConfiguration);
		this.add(this.btnRegles);

		this.btnConfiguration.addActionListener(this);
		this.btnRegles       .addActionListener(this);
	}

	public void actionPerformed(ActionEvent e)
	{
		if (e.getSource() == this.btnConfiguration)
		{
			new FrameConfiguration(this.ctrl);
			this.frmAccueil.dispose();
		}
		else if (e.getSource() == this.btnRegles)
		{
			// affichage simple dans la console (pas de fenetre popup)
			System.out.println("Regles : reliez les stations de meme symbole pour conquerir des arrondissements.");
		}
	}
}
