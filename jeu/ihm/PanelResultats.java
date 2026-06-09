package jeu.ihm;

import jeu.ControleurJeu;
import jeu.metier.Joueur;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class PanelResultats extends JPanel implements ActionListener
{
	private FrameResultats frmResultats;
	private ControleurJeu  ctrl;
	private Image          imgBackground;

	private JButton        btnQuitter;

	public PanelResultats(FrameResultats frmResultats, ControleurJeu ctrl)
	{
		this.frmResultats = frmResultats;
		this.ctrl         = ctrl;

		String img = this.ctrl.getImageFond2();
		if (img != null) this.imgBackground = new ImageIcon(img).getImage();

		this.setLayout(new BorderLayout(10, 10));
		this.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
		this.setOpaque(false);
		this.setPreferredSize(new Dimension(500, 450));

		/*-------------------------*/
		/* Création des composants */
		/*-------------------------*/
		Font gras18 = new Font("Arial", Font.BOLD, 18);
		Font gras14 = new Font("Arial", Font.BOLD, 14);
		Font gras12 = new Font("Arial", Font.BOLD, 12);

		JLabel lblTitre = new JLabel("Fin de partie !", SwingConstants.CENTER);
		lblTitre.setFont(gras18);
		lblTitre.setForeground(Color.WHITE);

		// Trouver le vainqueur
		Joueur[] joueurs  = this.ctrl.getJoueurs();
		Joueur   vainqueur = joueurs[0];
		for (int i = 1; i < joueurs.length; i++)
			if (joueurs[i].getScoreTotal() > vainqueur.getScoreTotal()) vainqueur = joueurs[i];

		JLabel lblVainqueur = new JLabel("Bravo Joueur " + vainqueur.getNumero() + " !", SwingConstants.CENTER);
		lblVainqueur.setFont(gras14);
		lblVainqueur.setForeground(this.ctrl.getCouleurJoueur(vainqueur.getNumero()));

		// Tableau des scores : en-tête + 1 ligne par joueur
		int nbManches = this.ctrl.getNbManches();
		int nbJoueurs = joueurs.length;

		// Colonnes : Joueur | Manche 1 | ... | Manche N | Total
		String[] colonnes = new String[nbManches + 2];
		colonnes[0] = "Joueur";
		for (int m = 1; m <= nbManches; m++) colonnes[m] = "Manche " + m;
		colonnes[nbManches + 1] = "Total";

		Object[][] donnees = new Object[nbJoueurs][nbManches + 2];
		for (int i = 0; i < nbJoueurs; i++)
		{
			donnees[i][0] = "Joueur " + joueurs[i].getNumero();
			for (int m = 0; m < nbManches; m++)
				donnees[i][m + 1] = joueurs[i].getScoreManche(m) + " pt";
			donnees[i][nbManches + 1] = joueurs[i].getScoreTotal() + " pt";
		}

		JTable tableScores = new JTable(donnees, colonnes);
		tableScores.setFont(gras12);
		tableScores.setRowHeight(28);
		tableScores.getTableHeader().setFont(gras12);
		tableScores.setEnabled(false); // non éditable

		JScrollPane scrollTable = new JScrollPane(tableScores);
		scrollTable.setOpaque(false);

		this.btnQuitter = new JButton("Quitter");
		this.btnQuitter.setFont(gras14);

		/*-------------------------------*/
		/* Positionnement des composants */
		/*-------------------------------*/
		JPanel panelHaut = new JPanel(new GridLayout(2, 1, 5, 5));
		panelHaut.setOpaque(false);
		panelHaut.add(lblTitre);
		panelHaut.add(lblVainqueur);

		JPanel panelBas = new JPanel(new FlowLayout(FlowLayout.CENTER));
		panelBas.setOpaque(false);
		panelBas.add(this.btnQuitter);

		this.add(panelHaut,    BorderLayout.NORTH);
		this.add(scrollTable,  BorderLayout.CENTER);
		this.add(panelBas,     BorderLayout.SOUTH);

		/*---------------------------*/
		/* Activation des composants */
		/*---------------------------*/
		this.btnQuitter.addActionListener(this);
	}

	public void actionPerformed(ActionEvent e)
	{
		if (e.getSource() == this.btnQuitter)
			System.exit(0);
	}

	protected void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		if (this.imgBackground != null)
			g.drawImage(this.imgBackground, 0, 0, getWidth(), getHeight(), this);
		g.setColor(new Color(0, 0, 0, 150));
		g.fillRect(0, 0, getWidth(), getHeight());
	}
}
