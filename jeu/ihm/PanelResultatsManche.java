package jeu.ihm;

import jeu.ControleurJeu;
import jeu.metier.Joueur;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

// Écran affiché à la fin de chaque manche (sauf la dernière).
// Montre les scores de la manche + bouton "Manche suivante".
// Même DA que les autres écrans : fond image + overlay sombre + texte blanc.
public class PanelResultatsManche extends JPanel implements ActionListener
{
	private FrameResultatsManche frame;
	private ControleurJeu        ctrl;
	private Image                imgBackground;

	private JButton              btnSuivante;
	private JButton              btnQuitter;

	public PanelResultatsManche(FrameResultatsManche frame, ControleurJeu ctrl)
	{
		this.frame = frame;
		this.ctrl  = ctrl;

		String img = this.ctrl.getImageFond2();
		if (img != null) this.imgBackground = new ImageIcon(img).getImage();

		this.setLayout(new BorderLayout(10, 10));
		this.setBorder(BorderFactory.createEmptyBorder(35, 35, 35, 35));
		this.setOpaque(false);
		this.setPreferredSize(new Dimension(480, 420));

		/*-------------------------*/
		/* Création des composants */
		/*-------------------------*/
		Font gras20 = new Font("Arial", Font.BOLD, 20);
		Font gras14 = new Font("Arial", Font.BOLD, 14);
		Font gras12 = new Font("Arial", Font.BOLD, 12);

		int manche    = this.ctrl.getNumeroManche();
		int nbManches = this.ctrl.getNbManches();

		JLabel lblTitre = new JLabel("Manche " + manche + " / " + nbManches + " terminée !", SwingConstants.CENTER);
		lblTitre.setFont(gras20);
		lblTitre.setForeground(Color.WHITE);

		// Meilleur score de cette manche
		Joueur[] joueurs     = this.ctrl.getJoueurs();
		Joueur   meilleur    = joueurs[0];
		for (int i = 1; i < joueurs.length; i++)
		{
			if (joueurs[i].getScoreManche(manche - 1) > meilleur.getScoreManche(manche - 1))
				meilleur = joueurs[i];
		}
		JLabel lblMeilleur = new JLabel("Meilleur score : Joueur " + meilleur.getNumero(), SwingConstants.CENTER);
		lblMeilleur.setFont(gras14);
		lblMeilleur.setForeground(this.ctrl.getCouleurJoueur(meilleur.getNumero()));

		// Tableau : Joueur | Score manche | Total
		String[] colonnes = { "Joueur", "Score manche " + manche, "Total" };
		Object[][] donnees = new Object[joueurs.length][3];
		for (int i = 0; i < joueurs.length; i++)
		{
			donnees[i][0] = "Joueur " + joueurs[i].getNumero();
			donnees[i][1] = joueurs[i].getScoreManche(manche - 1) + " pt";
			donnees[i][2] = joueurs[i].getScoreTotal() + " pt";
		}
		JTable tableau = new JTable(donnees, colonnes);
		tableau.setFont(gras12);
		tableau.setRowHeight(28);
		tableau.getTableHeader().setFont(gras12);
		tableau.setEnabled(false);
		JScrollPane scroll = new JScrollPane(tableau);
		scroll.setOpaque(false);
		scroll.getViewport().setOpaque(false);

		// Boutons
		this.btnSuivante = new JButton("Manche suivante →");
		this.btnQuitter  = new JButton("Quitter");
		this.btnSuivante.setFont(gras14);
		this.btnQuitter .setFont(gras14);

		/*-------------------------------*/
		/* Positionnement des composants */
		/*-------------------------------*/
		JPanel panelHaut = new JPanel(new GridLayout(2, 1, 5, 8));
		panelHaut.setOpaque(false);
		panelHaut.add(lblTitre);
		panelHaut.add(lblMeilleur);

		JPanel panelBas = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 0));
		panelBas.setOpaque(false);
		panelBas.add(this.btnQuitter);
		panelBas.add(this.btnSuivante);

		this.add(panelHaut, BorderLayout.NORTH);
		this.add(scroll,    BorderLayout.CENTER);
		this.add(panelBas,  BorderLayout.SOUTH);

		/*---------------------------*/
		/* Activation des composants */
		/*---------------------------*/
		this.btnSuivante.addActionListener(this);
		this.btnQuitter .addActionListener(this);
	}

	public void actionPerformed(ActionEvent e)
	{
		if (e.getSource() == this.btnSuivante)
		{
			this.frame.dispose();
			this.ctrl.reprendreManche();
		}
		else if (e.getSource() == this.btnQuitter)
		{
			System.exit(0);
		}
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
