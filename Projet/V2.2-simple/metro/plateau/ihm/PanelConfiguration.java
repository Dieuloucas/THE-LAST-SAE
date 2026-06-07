package plateau.ihm;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import plateau.Controleur;

public class PanelConfiguration extends JPanel implements ActionListener
{
	private FrameConfiguration frmConfiguration;
	private Controleur         ctrl;

	private JTextField txtLargeur;
	private JTextField txtHauteur;
	private JTextField txtJoueurs;
	private JTextField txtMetro;
	private JTextField txtArrondissements;
	private JTextField txtTailleCases;

	private JButton btnValider;
	private JButton btnAnnuler;

	public PanelConfiguration(FrameConfiguration frmConfiguration, Controleur ctrl)
	{
		this.frmConfiguration = frmConfiguration;
		this.ctrl             = ctrl;

		this.setLayout(new GridLayout(7, 2, 10, 10));

		// valeurs par defaut pour aller plus vite
		this.txtLargeur         = new JTextField("7");
		this.txtHauteur         = new JTextField("7");
		this.txtJoueurs         = new JTextField("4");
		this.txtMetro           = new JTextField("4");
		this.txtArrondissements = new JTextField("10");
		this.txtTailleCases     = new JTextField("80");

		this.add(new JLabel("Largeur :"));                            this.add(this.txtLargeur);
		this.add(new JLabel("Hauteur :"));                            this.add(this.txtHauteur);
		this.add(new JLabel("Nombre de joueurs (max 4) :"));          this.add(this.txtJoueurs);
		this.add(new JLabel("Nombre de metros (max 6) :"));           this.add(this.txtMetro);
		this.add(new JLabel("Nombre d'arrondissements (max 20) :"));  this.add(this.txtArrondissements);
		this.add(new JLabel("Taille des cases :"));                   this.add(this.txtTailleCases);

		this.btnAnnuler = new JButton("Annuler");
		this.btnValider = new JButton("Valider");
		this.add(this.btnAnnuler);
		this.add(this.btnValider);

		this.btnAnnuler.addActionListener(this);
		this.btnValider.addActionListener(this);
	}

	public void actionPerformed(ActionEvent e)
	{
		if (e.getSource() == this.btnAnnuler)
		{
			this.txtLargeur.setText("");          this.txtHauteur.setText("");
			this.txtJoueurs.setText("");          this.txtMetro.setText("");
			this.txtArrondissements.setText("");  this.txtTailleCases.setText("");
			return;
		}

		if (e.getSource() == this.btnValider)
		{
			int largeur, hauteur, nbJoueurs, nbMetro, nbArrondissements, tailleCases;

			// on convertit les champs en nombres
			try
			{
				largeur           = Integer.parseInt(this.txtLargeur.getText().trim());
				hauteur           = Integer.parseInt(this.txtHauteur.getText().trim());
				nbJoueurs         = Integer.parseInt(this.txtJoueurs.getText().trim());
				nbMetro           = Integer.parseInt(this.txtMetro.getText().trim());
				nbArrondissements = Integer.parseInt(this.txtArrondissements.getText().trim());
				tailleCases       = Integer.parseInt(this.txtTailleCases.getText().trim());
			}
			catch (NumberFormatException ex)
			{
				System.out.println("Erreur : veuillez entrer uniquement des nombres.");
				return;
			}

			// verifications (qui bloquent vraiment)
			if (largeur <= 0 || hauteur <= 0 || nbJoueurs <= 0 || nbMetro <= 0 || nbArrondissements <= 0 || tailleCases <= 0)
			{
				System.out.println("Erreur : toutes les valeurs doivent etre positives.");
				return;
			}
			if (nbJoueurs > 4)          { System.out.println("Erreur : 4 joueurs maximum.");          return; }
			if (nbMetro > 6)            { System.out.println("Erreur : 6 metros maximum.");           return; }
			if (nbArrondissements > 20) { System.out.println("Erreur : 20 arrondissements maximum."); return; }

			// on cree le plateau et on passe a l'ecran de creation
			this.ctrl.initialiserPlateau(largeur, hauteur);
			this.ctrl.setConfigJeu(nbJoueurs, nbMetro);

			new FrameCreation(this.ctrl, nbArrondissements, tailleCases);
			this.frmConfiguration.dispose();
		}
	}
}
