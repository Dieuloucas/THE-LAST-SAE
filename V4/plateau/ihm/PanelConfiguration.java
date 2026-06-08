package plateau.ihm;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;

import plateau.Controleur;

public class PanelConfiguration extends JPanel implements ActionListener
{
	private FrameConfiguration frmConfiguration;
	private Controleur         ctrl;
	private Image              imgFond;

	private JTextField txtLargeur;
	private JTextField txtHauteur;
	private JTextField txtJoueurs;
	private JTextField txtStations;
	private JTextField txtArrondissements;
	private JTextField txtTailleCases;

	private JButton btnValider;
	private JButton btnAnnuler;

	public PanelConfiguration(FrameConfiguration frmConfiguration, Controleur ctrl)
	{
		this.frmConfiguration = frmConfiguration;
		this.ctrl             = ctrl;
		this.imgFond          = new ImageIcon(Images.chemin("fond2.png")).getImage();

		this.setLayout(new GridLayout(7, 2, 10, 10));
		this.setBorder(BorderFactory.createEmptyBorder(25, 25, 25, 25));

		this.txtLargeur         = new JTextField("7");
		this.txtHauteur         = new JTextField("7");
		this.txtJoueurs         = new JTextField("4");
		this.txtStations        = new JTextField("4");
		this.txtArrondissements = new JTextField("10");
		this.txtTailleCases     = new JTextField("80");

		this.add(label("Largeur :"));                            this.add(this.txtLargeur);
		this.add(label("Hauteur :"));                            this.add(this.txtHauteur);
		this.add(label("Nombre de joueurs (max 4) :"));          this.add(this.txtJoueurs);
		this.add(label("Nombre de types de station (max 6) :")); this.add(this.txtStations);
		this.add(label("Nombre d'arrondissements (max 20) :"));  this.add(this.txtArrondissements);
		this.add(label("Taille des cases :"));                   this.add(this.txtTailleCases);

		this.btnAnnuler = new JButton("Annuler");
		this.btnValider = new JButton("Valider");
		this.add(this.btnAnnuler);
		this.add(this.btnValider);

		this.btnAnnuler.addActionListener(this);
		this.btnValider.addActionListener(this);
	}

	private JLabel label(String texte)
	{
		JLabel l = new JLabel(texte);
		l.setForeground(Color.WHITE);
		l.setFont(new Font("Arial", Font.BOLD, 13));
		return l;
	}

	public void actionPerformed(ActionEvent e)
	{
		if (e.getSource() == this.btnAnnuler)
		{
			this.txtLargeur.setText("");          this.txtHauteur.setText("");
			this.txtJoueurs.setText("");          this.txtStations.setText("");
			this.txtArrondissements.setText("");  this.txtTailleCases.setText("");
			return;
		}

		if (e.getSource() == this.btnValider)
		{
			int largeur, hauteur, nbJoueurs, nbStations, nbArrondissements;
			try
			{
				largeur           = Integer.parseInt(this.txtLargeur.getText().trim());
				hauteur           = Integer.parseInt(this.txtHauteur.getText().trim());
				nbJoueurs         = Integer.parseInt(this.txtJoueurs.getText().trim());
				nbStations        = Integer.parseInt(this.txtStations.getText().trim());
				nbArrondissements = Integer.parseInt(this.txtArrondissements.getText().trim());
				Integer.parseInt(this.txtTailleCases.getText().trim()); // juste pour valider que c'est un nombre
			}
			catch (NumberFormatException ex)
			{
				JOptionPane.showMessageDialog(this, "Veuillez entrer uniquement des nombres.", "Erreur", JOptionPane.ERROR_MESSAGE);
				return;
			}

			// c'est le METIER qui dit si la config est valide
			String erreur = this.ctrl.verifierConfig(largeur, hauteur, nbJoueurs, nbStations, nbArrondissements);
			if (erreur != null)
			{
				JOptionPane.showMessageDialog(this, erreur, "Configuration invalide", JOptionPane.ERROR_MESSAGE);
				return;
			}

			int tailleCases = Integer.parseInt(this.txtTailleCases.getText().trim());
			this.ctrl.creerPlateau(largeur, hauteur, nbJoueurs, nbStations);
			new FrameCreation(this.ctrl, nbArrondissements, tailleCases);
			this.frmConfiguration.dispose();
		}
	}

	protected void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		if (this.imgFond != null) g.drawImage(this.imgFond, 0, 0, getWidth(), getHeight(), this);
		g.setColor(new Color(0, 0, 0, 130));   // voile sombre pour lisibilite
		g.fillRect(0, 0, getWidth(), getHeight());
	}
}
