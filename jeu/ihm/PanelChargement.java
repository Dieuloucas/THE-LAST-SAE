package jeu.ihm;

import jeu.ControleurJeu;

import java.awt.*;
import java.awt.event.*;
import java.io.File;
import javax.swing.*;

public class PanelChargement extends JPanel implements ActionListener
{
	private FrameChargement  frmChargement;
	private ControleurJeu    ctrl;
	private Image            imgBackground;

	private File[]           sauvegardes;
	private JComboBox<String> comboFichiers;
	private JButton          btnCharger;
	private JButton          btnRetour;
	private JLabel           lblStatut;

	public PanelChargement(FrameChargement frmChargement, ControleurJeu ctrl)
	{
		this.frmChargement = frmChargement;
		this.ctrl          = ctrl;

		String img = this.ctrl.getImageFond2();
		if (img != null) this.imgBackground = new ImageIcon(img).getImage();

		this.setLayout(new BorderLayout());
		this.setBorder(BorderFactory.createEmptyBorder(25, 25, 25, 25));
		this.setOpaque(false);

		/*-------------------------*/
		/* Création des composants */
		/*-------------------------*/
		Font labelFont = new Font("Arial", Font.BOLD, 13);

		JLabel lblTitre = new JLabel("Sélectionnez un plateau de jeu :");
		lblTitre.setForeground(Color.WHITE);
		lblTitre.setFont(new Font("Arial", Font.BOLD, 16));

		this.comboFichiers = new JComboBox<String>();
		this.comboFichiers.setFont(labelFont);

		this.sauvegardes = this.ctrl.getSauvegardes();
		if (this.sauvegardes.length == 0)
			this.comboFichiers.addItem("Aucun fichier trouvé");
		else
			for (int i = 0; i < this.sauvegardes.length; i++)
				this.comboFichiers.addItem(this.sauvegardes[i].getName());

		this.lblStatut = new JLabel(" ");
		this.lblStatut.setForeground(new Color(255, 100, 100));
		this.lblStatut.setFont(labelFont);

		this.btnCharger = new JButton("Charger et jouer");
		this.btnRetour  = new JButton("Retour");
		this.btnCharger.setFont(labelFont);
		this.btnRetour .setFont(labelFont);
		this.btnCharger.setEnabled(this.sauvegardes.length > 0);

		/*-------------------------------*/
		/* Positionnement des composants */
		/*-------------------------------*/
		JPanel panelFormulaire = new JPanel(new GridLayout(4, 1, 10, 10));
		panelFormulaire.setOpaque(false);
		panelFormulaire.add(lblTitre);
		panelFormulaire.add(this.comboFichiers);
		panelFormulaire.add(this.lblStatut);

		JPanel panelBoutons = new JPanel(new GridLayout(1, 2, 10, 0));
		panelBoutons.setOpaque(false);
		panelBoutons.add(this.btnRetour);
		panelBoutons.add(this.btnCharger);
		panelFormulaire.add(panelBoutons);

		this.add(panelFormulaire, BorderLayout.CENTER);

		/*---------------------------*/
		/* Activation des composants */
		/*---------------------------*/
		this.btnCharger.addActionListener(this);
		this.btnRetour .addActionListener(this);
	}

	public void actionPerformed(ActionEvent e)
	{
		if (e.getSource() == this.btnCharger)
		{
			int idx = this.comboFichiers.getSelectedIndex();
			if (idx >= 0 && idx < this.sauvegardes.length)
			{
				if (this.ctrl.chargerPlateau(this.sauvegardes[idx]))
				{
					// Vérifier que le plateau a bien des joueurs et des stations
					if (!this.ctrl.plateauEstJouable())
					{
						this.lblStatut.setText("Plateau en attente de finalisation de conception.");
						return;
					}
					this.ctrl.demarrerPartie();
					// Ouvrir une fenêtre par joueur — tous jouent en simultané
					int nbJoueurs = this.ctrl.getNbJoueurs();
					FrameJoueur[] frames = new FrameJoueur[nbJoueurs];
					for (int i = 0; i < nbJoueurs; i++)
						frames[i] = new FrameJoueur(this.ctrl, i + 1);
					this.ctrl.enregistrerFrames(frames);
					this.frmChargement.setVisible(false);
				}
				else
				{
					this.lblStatut.setText("Erreur : fichier invalide ou corrompu.");
				}
			}
		}
		else if (e.getSource() == this.btnRetour)
		{
			new FrameAccueilJeu(this.ctrl);
			this.frmChargement.setVisible(false);
		}
	}

	protected void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		if (this.imgBackground != null)
			g.drawImage(this.imgBackground, 0, 0, getWidth(), getHeight(), this);
		g.setColor(new Color(0, 0, 0, 130));
		g.fillRect(0, 0, getWidth(), getHeight());
	}
}
