package plateau.ihm;

import plateau.Controleur;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

// Longue bande verticale centrale : carte commune (pioche) + statut de chaque joueur + boutons.
// Design simple : fond clair, texte noir lisible.
public class PanelInfos extends JPanel implements ActionListener
{
	private Controleur ctrl;
	private int        nbJoueurs;

	private PanelCarte panelCarte;
	private JLabel     lblManche;
	private JLabel     lblPioche;
	private JLabel[]   lblStatutJoueur;
	private JButton[]  btnPasserJoueur;
	private JButton    btnQuitter;

	public PanelInfos(Controleur ctrl)
	{
		this.ctrl      = ctrl;
		this.nbJoueurs = ctrl.getNbJoueurs();

		this.setPreferredSize(new Dimension(220, 660));
		this.setBackground(Color.WHITE);
		this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		this.setBorder(BorderFactory.createEmptyBorder(14, 14, 14, 14));

		Font gras16 = new Font("Arial", Font.BOLD, 16);
		Font gras12 = new Font("Arial", Font.BOLD, 12);
		Font normal12 = new Font("Arial", Font.PLAIN, 12);

		/*-------------------------*/
		/* Titre + carte + pioche  */
		/*-------------------------*/
		JLabel lblTitre = new JLabel("Infos de jeu");
		lblTitre.setFont(gras16);
		lblTitre.setAlignmentX(Component.CENTER_ALIGNMENT);

		this.lblManche = new JLabel("Manche — / —");
		this.lblManche.setFont(gras12);
		this.lblManche.setAlignmentX(Component.CENTER_ALIGNMENT);

		JLabel lblCarteTitre = new JLabel("Carte commune");
		lblCarteTitre.setFont(gras12);
		lblCarteTitre.setAlignmentX(Component.CENTER_ALIGNMENT);

		this.panelCarte = new PanelCarte(ctrl);
		this.panelCarte.setAlignmentX(Component.CENTER_ALIGNMENT);
		this.panelCarte.setMaximumSize(new Dimension(120, 160));

		this.lblPioche = new JLabel("— foncée(s) restante(s)");
		this.lblPioche.setFont(normal12);
		this.lblPioche.setAlignmentX(Component.CENTER_ALIGNMENT);

		this.add(lblTitre);
		this.add(Box.createVerticalStrut(6));
		this.add(this.lblManche);
		this.add(Box.createVerticalStrut(12));
		this.add(lblCarteTitre);
		this.add(Box.createVerticalStrut(4));
		this.add(this.panelCarte);
		this.add(Box.createVerticalStrut(6));
		this.add(this.lblPioche);
		this.add(Box.createVerticalStrut(14));
		this.add(new JSeparator());
		this.add(Box.createVerticalStrut(10));

		/*-------------------------*/
		/* Statut des joueurs      */
		/*-------------------------*/
		JLabel lblJoueurs = new JLabel("Joueurs");
		lblJoueurs.setFont(gras12);
		lblJoueurs.setAlignmentX(Component.CENTER_ALIGNMENT);
		this.add(lblJoueurs);
		this.add(Box.createVerticalStrut(8));

		this.lblStatutJoueur = new JLabel[this.nbJoueurs];
		this.btnPasserJoueur = new JButton[this.nbJoueurs];

		for (int j = 1; j <= this.nbJoueurs; j++)
		{
			JLabel statut = new JLabel("Joueur " + j + " : à jouer");
			statut.setFont(normal12);
			statut.setAlignmentX(Component.CENTER_ALIGNMENT);

			JButton pass = new JButton("Passer (Joueur " + j + ")");
			pass.setFont(normal12);
			pass.setAlignmentX(Component.CENTER_ALIGNMENT);
			pass.setMaximumSize(new Dimension(180, 26));
			pass.setActionCommand("PASSER_" + j);
			pass.addActionListener(this);

			this.lblStatutJoueur[j - 1] = statut;
			this.btnPasserJoueur[j - 1] = pass;

			this.add(statut);
			this.add(Box.createVerticalStrut(3));
			this.add(pass);
			this.add(Box.createVerticalStrut(10));
		}

		/*-------------------------*/
		/* Quitter                 */
		/*-------------------------*/
		this.add(Box.createVerticalGlue());
		this.btnQuitter = new JButton("Quitter");
		this.btnQuitter.setFont(normal12);
		this.btnQuitter.setAlignmentX(Component.CENTER_ALIGNMENT);
		this.btnQuitter.setMaximumSize(new Dimension(180, 28));
		this.btnQuitter.addActionListener(this);
		this.add(this.btnQuitter);

		rafraichir();
	}

	public void actionPerformed(ActionEvent e)
	{
		if (e.getSource() == this.btnQuitter)
		{
			System.exit(0);
			return;
		}

		String cmd = e.getActionCommand();
		if (cmd != null && cmd.startsWith("PASSER_"))
		{
			int j = Integer.parseInt(cmd.substring(7));
			if (!this.ctrl.aJoue(j) && !this.ctrl.isPartieTerminee())
			{
				this.ctrl.passerTour(j);
				this.ctrl.rafraichirTout();
			}
		}
	}

	public void rafraichir()
	{
		this.lblManche.setText("Manche " + this.ctrl.getNumeroManche() + " / " + this.ctrl.getNbManches());
		this.panelCarte.repaint();
		this.lblPioche.setText(this.ctrl.getNbFonceesRestantes() + " foncée(s) restante(s)");

		for (int j = 1; j <= this.nbJoueurs; j++)
		{
			if (this.ctrl.isPartieTerminee())
			{
				this.lblStatutJoueur[j - 1].setText("Joueur " + j + " : terminé");
				this.btnPasserJoueur[j - 1].setEnabled(false);
			}
			else if (this.ctrl.aJoue(j))
			{
				this.lblStatutJoueur[j - 1].setText("Joueur " + j + " : A déjà joué");
				this.btnPasserJoueur[j - 1].setEnabled(false);
			}
			else if (this.ctrl.estSonTour(j))
			{
				// C'est à ce joueur de jouer : lui seul peut passer son tour
				this.lblStatutJoueur[j - 1].setText("Joueur " + j + " : Jouer");
				this.btnPasserJoueur[j - 1].setEnabled(true);
			}
			else
			{
				this.lblStatutJoueur[j - 1].setText("Joueur " + j + " : En attente");
				this.btnPasserJoueur[j - 1].setEnabled(false);
			}
		}
	}
}
