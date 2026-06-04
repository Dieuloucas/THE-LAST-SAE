package plateau.ihm;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import plateau.Controleur;

public class PanelConfiguration extends JPanel implements ActionListener
{
	private FrameConfiguration frmConfiguration;
	private FrameCreation frmCreation;
	private Controleur ctrl;
	private Image imgBackground;

	private JPanel panelConfiguration;

	private JLabel lblLargeur;
	private JLabel lblHauteur;
	private JLabel lblJoueurs;
	private JLabel lblMetro;
	private JLabel lblArrondissments;
	private JLabel lblTailleCases;

	public static JTextField txtLargeur;
	public static JTextField txtHauteur;
	public static JTextField txtJoueurs;
	public static JTextField txtMetro;
	public static JTextField txtArrondissments;
	public static JTextField txtTailleCases;

	private JButton btnValider;
	private JButton btnAnnuler;

	public PanelConfiguration(FrameConfiguration frmConfiguration, Controleur ctrl)
	{
		this.frmConfiguration = frmConfiguration;
		this.ctrl = ctrl;

		// Charger l'image de fond fond2.png
		String bgPath = this.ctrl.getImageFond2();
		if (bgPath != null)
		{
			this.imgBackground = new ImageIcon(bgPath).getImage();
		}

		this.setLayout(new BorderLayout());
		this.setBorder(BorderFactory.createEmptyBorder(25, 25, 25, 25));

		this.panelConfiguration = new JPanel(new GridLayout(7, 2, 10, 10));
		this.panelConfiguration.setOpaque(false);
		this.setOpaque(false);

		/*-------------------------*/
		/* Création des composants */
		/*-------------------------*/

		Font labelFont = new Font("Arial", Font.BOLD, 13);


		this.lblLargeur = new JLabel("Taille en largeur :");

		this.lblLargeur.setForeground(Color.WHITE);
		this.lblLargeur.setFont(labelFont);
	
		txtLargeur = new JTextField();


		this.lblHauteur = new JLabel("Taille en hauteur :");

		this.lblHauteur.setForeground(Color.WHITE);
		this.lblHauteur.setFont(labelFont);

		txtHauteur = new JTextField();


		this.lblJoueurs = new JLabel("Nombre de joueurs (max 4) :");

		this.lblJoueurs.setForeground(Color.WHITE);
		this.lblJoueurs.setFont(labelFont);

		txtJoueurs = new JTextField();


		this.lblMetro = new JLabel("Nombre de types de métro (max 6) :");

		this.lblMetro.setForeground(Color.WHITE);
		this.lblMetro.setFont(labelFont);

		txtMetro = new JTextField();

		this.lblArrondissments = new JLabel("Nombre d'arrondissements (max 20) :");

		this.lblArrondissments.setForeground(Color.WHITE);
		this.lblArrondissments.setFont(labelFont);

		txtArrondissments = new JTextField();

		this.lblTailleCases = new JLabel("Taille des cases :");

		this.lblTailleCases.setForeground(Color.WHITE);
		this.lblTailleCases.setFont(labelFont);
		
		txtTailleCases = new JTextField();

		this.btnValider = new JButton("Valider");
		this.btnAnnuler = new JButton("Annuler");

		this.panelConfiguration.add(this.lblLargeur);
		this.panelConfiguration.add(txtLargeur);

		this.panelConfiguration.add(this.lblHauteur);
		this.panelConfiguration.add(txtHauteur);

		this.panelConfiguration.add(this.lblJoueurs);
		this.panelConfiguration.add(txtJoueurs);

		this.panelConfiguration.add(this.lblMetro);
		this.panelConfiguration.add(txtMetro);

		this.panelConfiguration.add(this.lblArrondissments);
		this.panelConfiguration.add(txtArrondissments);

		this.panelConfiguration.add(this.lblTailleCases);
		this.panelConfiguration.add(txtTailleCases);

		this.panelConfiguration.add(this.btnAnnuler);
		this.panelConfiguration.add(this.btnValider);


		this.txtLargeur       .setText("7" );
		this.txtHauteur       .setText("7" );

		this.txtJoueurs       .setText("4" ); // Pour faciliter les tests
		this.txtMetro         .setText("4" );
		this.txtArrondissments.setText("10");

		this.txtTailleCases   .setText("80");

		/*-------------------------------*/
		/* Positionnement des composants */
		/*-------------------------------*/
		this.add(this.panelConfiguration, BorderLayout.CENTER);

		/*---------------------------*/
		/* Activation des composants */
		/*---------------------------*/
		this.btnAnnuler.addActionListener(this);
		this.btnValider.addActionListener(this);
	}

	@Override
	protected void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		if (this.imgBackground != null)
		{
			g.drawImage(this.imgBackground, 0, 0, getWidth(), getHeight(), this);
		}
		// Un voile sombre semi-transparent pour garantir la lisibilité du texte
		g.setColor(new Color(0, 0, 0, 130));
		g.fillRect(0, 0, getWidth(), getHeight());
	}

	public void actionPerformed(ActionEvent e)
	{
		if (e.getSource() == this.btnValider)
		{
			if (txtLargeur         .getText().isEmpty()
				|| txtHauteur       .getText().isEmpty()
				|| txtJoueurs       .getText().isEmpty()
				|| txtMetro         .getText().isEmpty()
				|| txtArrondissments.getText().isEmpty()
				|| txtTailleCases   .getText().isEmpty())
			{
				System.out.println("Veuillez remplir tous les champs.");
				return;
			}

			try
			{
				int largeur           = Integer.parseInt(txtLargeur       .getText());
				int hauteur           = Integer.parseInt(txtHauteur       .getText());

				int nbJoueurs         = Integer.parseInt(txtJoueurs       .getText());
				int nbMetro           = Integer.parseInt(txtMetro         .getText());
				int nbArrondissements = Integer.parseInt(txtArrondissments.getText());

				int tailleCases       = Integer.parseInt(txtTailleCases   .getText());

				if (nbMetro > 6)
				{
					System.out.println("Le nombre de types de métro ne peut pas dépasser 6.");
				}

				if (nbJoueurs > 4)
				{
					System.out.println("Le nombre de joueurs ne peut pas dépasser 4.");
				}
				if (nbArrondissements > 20)
				{
					System.out.println("Le nombre d'arrondissements ne peut pas dépasser 20.");
				}

				if (largeur <= 0 || hauteur <= 0 || nbJoueurs <= 0 || nbMetro <= 0 || nbArrondissements <= 0)
				{
					System.out.println("Veuillez entrer des valeurs positives.");
				}

				if (largeur < nbJoueurs)
				{
					System.out.println("La largeur doit être cohérente avec le nombre de joueurs.");
				}

				if (hauteur < nbJoueurs)
				{
					System.out.println("La hauteur doit être cohérente avec le nombre de joueurs.");
				}

				this.ctrl.initialiserPlateau(largeur, hauteur);
				
				this.ctrl.setConfigJeu(nbJoueurs, nbMetro);

				this.frmCreation = new FrameCreation(this.ctrl);

				this.frmConfiguration.setVisible(false);
				this.frmCreation     .setVisible(true);
			}
			catch (NumberFormatException ex)
			{
				System.out.println("Veuillez entrer uniquement des nombres.");
			}
		}

		if (e.getSource() == this.btnAnnuler)
		{
			txtLargeur        .setText("");
			txtHauteur        .setText("");
			txtJoueurs        .setText("");
			txtMetro          .setText("");
			txtArrondissments .setText("");
			txtTailleCases    .setText("");
		}
	}
}