package plateau.ihm;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.*;
import java.util.ArrayList;
import javax.swing.*;
import plateau.Controleur;
import plateau.metier.Joueur;

// Tableau des scores (par manche + total) et annonce du/des gagnant(s).
public class PanelResultats extends JPanel implements ActionListener
{
	private Controleur ctrl;
	private JButton    btnQuitter;

	public PanelResultats(Controleur ctrl)
	{
		this.ctrl = ctrl;

		this.setBackground(Color.WHITE);
		this.setLayout(new BorderLayout(10, 10));
		this.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

		Joueur[] js        = ctrl.getJoueurs();
		int      nbManches = ctrl.getNbManches();

		JLabel lblTitre = new JLabel("Résultats", SwingConstants.CENTER);
		lblTitre.setFont(new Font("Arial", Font.BOLD, 20));
		this.add(lblTitre, BorderLayout.NORTH);

		/*-------------------------------------*/
		/* Tableau des scores                  */
		/*-------------------------------------*/
		Font gras   = new Font("Arial", Font.BOLD, 13);
		Font normal = new Font("Arial", Font.PLAIN, 13);

		JPanel grille = new JPanel(new GridLayout(js.length + 1, nbManches + 2, 8, 6));
		grille.setBackground(Color.WHITE);

		// Ligne d'entête : Joueur | M1 | M2 | ... | Total
		grille.add(entete("Joueur", gras));
		for (int m = 1; m <= nbManches; m++) grille.add(entete("M" + m, gras));
		grille.add(entete("Total", gras));

		// Une ligne par joueur
		for (int i = 0; i < js.length; i++)
		{
			grille.add(cellule("Joueur " + (i + 1), gras));
			for (int m = 0; m < nbManches; m++)
				grille.add(cellule("" + js[i].getScoreManche(m), normal));
			grille.add(cellule("" + js[i].getScoreTotal(), gras));
		}

		this.add(grille, BorderLayout.CENTER);

		/*-------------------------------------*/
		/* Gagnant(s) + bouton Quitter         */
		/*-------------------------------------*/
		ArrayList<Integer> gagnants = ctrl.getGagnants();
		String texte;
		if (gagnants.size() == 1)
		{
			texte = "Gagnant : Joueur " + gagnants.get(0);
		}
		else
		{
			StringBuilder sb = new StringBuilder("Égalité, victoire partagée : ");
			for (int k = 0; k < gagnants.size(); k++)
			{
				if (k > 0) sb.append(", ");
				sb.append("Joueur " + gagnants.get(k));
			}
			texte = sb.toString();
		}

		JLabel lblGagnant = new JLabel(texte, SwingConstants.CENTER);
		lblGagnant.setFont(new Font("Arial", Font.BOLD, 16));

		this.btnQuitter = new JButton("Quitter");
		this.btnQuitter.addActionListener(this);

		JPanel pBouton = new JPanel();
		pBouton.setBackground(Color.WHITE);
		pBouton.add(this.btnQuitter);

		JPanel bas = new JPanel(new BorderLayout(10, 10));
		bas.setBackground(Color.WHITE);
		bas.add(lblGagnant, BorderLayout.CENTER);
		bas.add(pBouton, BorderLayout.SOUTH);

		this.add(bas, BorderLayout.SOUTH);
	}

	private JLabel entete(String t, Font f)
	{
		JLabel l = new JLabel(t, SwingConstants.CENTER);
		l.setFont(f);
		l.setOpaque(true);
		l.setBackground(new Color(230, 230, 230));
		return l;
	}

	private JLabel cellule(String t, Font f)
	{
		JLabel l = new JLabel(t, SwingConstants.CENTER);
		l.setFont(f);
		return l;
	}

	public void actionPerformed(ActionEvent e)
	{
		if (e.getSource() == this.btnQuitter) System.exit(0);
	}
}
