package jeu.ihm;

import jeu.ControleurJeu;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class PanelAccueilJeu extends JPanel implements ActionListener
{
	private FrameAccueilJeu  frmAccueil;
	private FrameChargement  frmChargement;
	private ControleurJeu    ctrl;
	private Image            imgFond;

	private JButton          btnJouer;
	private JButton          btnRegles;

	public PanelAccueilJeu(FrameAccueilJeu frmAccueil, ControleurJeu ctrl)
	{
		this.frmAccueil = frmAccueil;
		this.ctrl       = ctrl;
		this.imgFond    = new ImageIcon(this.ctrl.getImageFond()).getImage();

		this.setPreferredSize(new Dimension(800, 600));

		/*-------------------------*/
		/* Création des composants */
		/*-------------------------*/
		this.btnJouer  = new JButton("Jouer");
		this.btnRegles = new JButton("Règles");

		Font fontBtn = new Font("Arial", Font.BOLD, 16);
		this.btnJouer .setFont(fontBtn);
		this.btnRegles.setFont(fontBtn);

		/*-------------------------------*/
		/* Positionnement des composants */
		/*-------------------------------*/
		this.setLayout(new GridBagLayout());

		JPanel panelBoutons = new JPanel(new GridLayout(2, 1, 10, 10));
		panelBoutons.setOpaque(false);
		panelBoutons.add(this.btnJouer);
		panelBoutons.add(this.btnRegles);

		this.add(panelBoutons);

		/*---------------------------*/
		/* Activation des composants */
		/*---------------------------*/
		this.btnJouer .addActionListener(this);
		this.btnRegles.addActionListener(this);
	}

	public void actionPerformed(ActionEvent e)
	{
		if (e.getSource() == this.btnJouer)
		{
			this.frmChargement = new FrameChargement(this.ctrl);
			this.frmAccueil.setVisible(false);
		}
		else if (e.getSource() == this.btnRegles)
		{
			try
			{
				String[] chemins = {
					"../CONCEPTION/RENDU FINAL/Règle les Cartographes du Métro.pdf",
					"Règle les Cartographes du Métro.pdf"
				};
				java.io.File pdf = null;
				for (String c : chemins)
				{
					java.io.File f = new java.io.File(c);
					if (f.exists()) { pdf = f; break; }
				}
				if (pdf != null && Desktop.isDesktopSupported())
					Desktop.getDesktop().open(pdf);
				else
					System.out.println("Fichier règles introuvable.");
			}
			catch (Exception ex)
			{
				System.out.println("Erreur ouverture règles : " + ex.getMessage());
			}
		}
	}

	protected void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		if (this.imgFond != null)
			g.drawImage(this.imgFond, 0, 0, getWidth(), getHeight(), this);
	}
}
