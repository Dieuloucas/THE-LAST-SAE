package plateau.ihm;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.*;
import javax.swing.*;
import plateau.Controleur;
import plateau.metier.Joueur;

// Écran de fin de partie : score de chaque joueur + vainqueur.
// Même DA que les autres écrans : image de fond + voile sombre + texte blanc.
public class PanelResultats extends JPanel implements ActionListener
{
	private FrameResultats frame;
	private Controleur     ctrl;
	private Image          imgFond;

	private JButton        btnQuitter;

	public PanelResultats(FrameResultats frame, Controleur ctrl)
	{
		this.frame = frame;
		this.ctrl  = ctrl;

		String img = this.ctrl.getImageFond2();
		if (img != null) this.imgFond = new ImageIcon(img).getImage();

		this.setLayout(new BorderLayout(10, 10));
		this.setBorder(BorderFactory.createEmptyBorder(35, 35, 35, 35));
		this.setOpaque(false);
		this.setPreferredSize(new Dimension(460, 400));

		Font gras22 = new Font("Arial", Font.BOLD, 22);
		Font gras14 = new Font("Arial", Font.BOLD, 14);
		Font gras12 = new Font("Arial", Font.BOLD, 12);

		Joueur[] joueurs = this.ctrl.getJoueurs();

		/*-------------------------*/
		/* Titre + vainqueur       */
		/*-------------------------*/
		JLabel lblTitre = new JLabel("Partie terminée !", SwingConstants.CENTER);
		lblTitre.setFont(gras22);
		lblTitre.setForeground(Color.WHITE);

		// Déterminer le meilleur score et compter les ex-aequo
		int meilleurScore = -1;
		for (int i = 0; i < joueurs.length; i++)
		{
			if (joueurs[i].getScore() > meilleurScore) meilleurScore = joueurs[i].getScore();
		}
		int nbExAequo  = 0;
		int numVainqueur = 0;
		for (int i = 0; i < joueurs.length; i++)
		{
			if (joueurs[i].getScore() == meilleurScore)
			{
				nbExAequo++;
				numVainqueur = joueurs[i].getNumero();
			}
		}

		JLabel lblVainqueur;
		if (nbExAequo > 1)
		{
			lblVainqueur = new JLabel("Égalité ! (" + meilleurScore + " pts)", SwingConstants.CENTER);
			lblVainqueur.setForeground(Color.WHITE);
		}
		else
		{
			lblVainqueur = new JLabel("Vainqueur : Joueur " + numVainqueur + " (" + meilleurScore + " pts)", SwingConstants.CENTER);
			lblVainqueur.setForeground(this.ctrl.getCouleurJoueur(numVainqueur));
		}
		lblVainqueur.setFont(gras14);

		JPanel panelHaut = new JPanel(new GridLayout(2, 1, 5, 8));

		panelHaut.setOpaque(false);

		panelHaut.add(lblTitre);
		panelHaut.add(lblVainqueur);

		/*-------------------------*/
		/* Tableau des scores      */
		/*-------------------------*/

		String[]   colonnes = { "Joueur", "Score" };
		Object[][] donnees  = new Object[joueurs.length][2];

		for (int i = 0; i < joueurs.length; i++)
		{
			donnees[i][0] = "Joueur " + joueurs[i].getNumero();
			donnees[i][1] = joueurs[i].getScore() + " pts";
		}
		JTable tableau = new JTable(donnees, colonnes);

		tableau.setFont(gras12);
		tableau.setRowHeight(30);

		tableau.getTableHeader().setFont(gras12);

		tableau.setEnabled(false);

		JScrollPane scroll = new JScrollPane(tableau);

		scroll.setOpaque(false);

		scroll.getViewport().setOpaque(false);

		/*-------------------------*/
		/* Bouton Quitter          */
		/*-------------------------*/

		this.btnQuitter = new JButton("Quitter");
		this.btnQuitter.setFont(gras14);

		JPanel panelBas = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 0));

		panelBas.setOpaque(false);
		panelBas.add(this.btnQuitter);

		/*-------------------------*/
		/* Assemblage              */
		/*-------------------------*/
		
		this.add(panelHaut, BorderLayout.NORTH);
		this.add(scroll,    BorderLayout.CENTER);
		this.add(panelBas,  BorderLayout.SOUTH);

		this.btnQuitter.addActionListener(this);
	}

	public void actionPerformed(ActionEvent e)
	{
		if (e.getSource() == this.btnQuitter)
			System.exit(0);
	}

        @Override
	protected void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		if (this.imgFond != null)
			g.drawImage(this.imgFond, 0, 0, getWidth(), getHeight(), this);
		g.setColor(new Color(0, 0, 0, 150));
		g.fillRect(0, 0, getWidth(), getHeight());
	}
}
