package plateau.ihm;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.*;
import javax.swing.*;
import plateau.Controleur;

public class PanelCreation extends JPanel implements MouseListener, MouseMotionListener, ActionListener
{
	private FrameConfiguration 			frmConfiguration;
	private FrameCreation 				frmCreation;
	private Controleur 					ctrl;

	private JPanel 						panelPlateau;
	private JPanel 						panelArrondissments;
	private JPanel 						panelBoutons;
	private JPanel[] tabCases 			= new JPanel[ Integer.parseInt(PanelConfiguration.txtLargeur.getText()) * Integer.parseInt(PanelConfiguration.txtHauteur.getText()) ];

	private int grillelargeur			= Integer.parseInt(PanelConfiguration.txtLargeur		.getText());
	private int grillehauteur			= Integer.parseInt(PanelConfiguration.txtHauteur		.getText());
	private int nombreArrondissments	= Integer.parseInt(PanelConfiguration.txtArrondissments	.getText());
	private int tailleCases				= Integer.parseInt(PanelConfiguration.txtTailleCases	.getText());

	private JButton[] 					btnArrondissments = new JButton[ nombreArrondissments ];

	private JButton 					btnValider;
	private JButton 					btnRetourConfiguration;
	private JButton 					btnPasserAuJeu;
	private JTextField  				txtNomFichier;

	private int 						arrondissementSelectionne = 0;

	private Color 						couleurSelectionnee = Color.LIGHT_GRAY;

	private String[] 					tabNomsArrondissments = {"1er","2ème","3ème","4ème","5ème","6ème","7ème","8ème","9ème","10ème","11ème","12ème","13ème","14ème","15ème","16ème","17ème","18ème","19ème","20ème"};

	private Color[] tabCouleurs = 
	{
		new Color(255, 105, 180), // rose vif
		new Color(138, 43, 226),  // violet
		new Color(255, 165, 0),   // orange
		new Color(0, 255, 255),   // cyan
		new Color(176, 196, 222), // bleu acier clair
		new Color(255, 20, 147),  // rose profond
		new Color(186, 85, 211),  // orchidée
		new Color(148, 0, 211),   // violet foncé
		new Color(210, 105, 30),  // chocolat
		new Color(244, 164, 96),  // sable
		new Color(199, 21, 133),  // violet rougeâtre
		new Color(123, 104, 238), // bleu-violet doux
		new Color(255, 140, 0),   // orange foncé
		new Color(0, 206, 209),   // turquoise
		new Color(72, 61, 139),   // bleu nuit
		new Color(220, 20, 60),   // cramoisi
		new Color(169, 169, 169), // gris foncé
		new Color(245, 222, 179), // beige
		new Color(255, 228, 225), // rose très pâle
		new Color(255, 192, 203)  // rose clair
	};

	public PanelCreation(FrameCreation frmCreation, Controleur ctrl)
	{
		this.frmCreation 			= frmCreation;
		this.ctrl 					= ctrl;
		
		this.setLayout(new BorderLayout());

		this.panelPlateau 			= new JPanel(new GridLayout(this.grillehauteur, this.grillelargeur, 0, 0));
		this.panelArrondissments 	= new JPanel(new GridLayout(this.nombreArrondissments, 1, 5, 5));
		this.panelBoutons 			= new JPanel(new GridLayout(2, 1, 5, 5));

		/*-------------------------*/
		/* Création des composants */
		/*-------------------------*/

		for (int i = 0; i < this.grillelargeur * this.grillehauteur; i++)
		{
			tabCases[i] 			= new JPanel();
			tabCases[i]				.setBackground(Color.LIGHT_GRAY);
			tabCases[i]				.setBorder(BorderFactory.createLineBorder(Color.BLACK));
			tabCases[i]				.setPreferredSize(new Dimension(this.tailleCases, this.tailleCases));
		}   

		for (int i = 0; i < this.nombreArrondissments; i++)
		{
			btnArrondissments[i] 	= new JButton( this.tabNomsArrondissments[i] + " Arrondissement" );
			btnArrondissments[i]	.setBackground( this.tabCouleurs[i] );
		}

		this.btnValider 			= new JButton("Valider");
		this.btnRetourConfiguration = new JButton("Retour à la configuration");
		this.btnPasserAuJeu 		= new JButton("Importation Uniquement");

		// Label et Champ de texte pour le nom du fichier
		JPanel panelNom 			= new JPanel(new FlowLayout(FlowLayout.CENTER));
		JLabel lblNom 				= new JLabel("Nom du plateau :");
		this.txtNomFichier 			= new JTextField(15);

		panelNom.add(lblNom);
		panelNom.add(this.txtNomFichier);

		JPanel panelBtns 			= new JPanel(new GridLayout(1, 3, 5, 5));
		panelBtns					.add(this.btnValider);
		panelBtns					.add(this.btnRetourConfiguration);
		panelBtns					.add(this.btnPasserAuJeu);

		this.panelBoutons			.add(panelNom);
		this.panelBoutons			.add(panelBtns);

		/*-------------------------------*/
		/* Positionnement des composants */
		/*-------------------------------*/

		this.add(this.panelPlateau, 		BorderLayout.CENTER);
		this.add(this.panelArrondissments, 	BorderLayout.WEST);

		for (int i = 0; i < this.grillelargeur * this.grillehauteur; i++)
		{
			this.panelPlateau		.add(tabCases[i]);
		}

		for (int i = 0; i < this.nombreArrondissments; i++)
		{
			this.panelArrondissments.add(btnArrondissments[i]);
		}

		this.add(this.panelBoutons, BorderLayout.SOUTH);

		/*---------------------------*/
		/* Activation des composants */
		/*---------------------------*/

		this.panelPlateau			.addMouseListener(this);
		this.panelPlateau			.addMouseMotionListener(this);

		for (int i = 0; i < this.nombreArrondissments; i++)
		{
			btnArrondissments[i]	.addActionListener(this);
		}

		this.btnValider				.addActionListener(this);
		this.btnRetourConfiguration	.addActionListener(this);
		this.btnPasserAuJeu			.addActionListener(this);
	}

	private void colorierCaseSousSouris(MouseEvent e)
	{
		Component comp = this.panelPlateau.getComponentAt(e.getPoint());
		if (comp instanceof JPanel && comp != this.panelPlateau)
		{
			JPanel casePanel = (JPanel) comp;
			for (int i = 0; i < this.tabCases.length; i++)
			{
				if (this.tabCases[i] == casePanel)
				{
					casePanel.setBackground(this.couleurSelectionnee);
					this.ctrl.affecterArrondissement(i, this.arrondissementSelectionne);
					break;
				}
			}
		}
	}

	public void mousePressed(MouseEvent e)
	{
		colorierCaseSousSouris(e);
	}

	public void mouseDragged(MouseEvent e)
	{
		colorierCaseSousSouris(e);
	}

	public void mouseMoved(MouseEvent e) 	{}
	public void mouseClicked(MouseEvent e) 	{}
	public void mouseReleased(MouseEvent e) {}
	public void mouseEntered(MouseEvent e) 	{}
	public void mouseExited(MouseEvent e) 	{}

	public void actionPerformed(ActionEvent e)
	{
		for (int i = 0; i < nombreArrondissments; i++)
		{
			if (e.getSource() == btnArrondissments[i])
			{
				couleurSelectionnee = btnArrondissments[i].getBackground();
				this.arrondissementSelectionne = i + 1;
			}
		}

		if (e.getSource() == this.btnValider)
		{
			for (int i = 0; i < this.tabCases.length; i++)
			{
				if (this.tabCases[i].getBackground().equals(Color.LIGHT_GRAY))
				{
					System.out.println("Veuillez remplir toutes les cases du plateau avant de valider.");
					return;
				}
			}

			String nomPlateau = this.txtNomFichier.getText();
			if (nomPlateau == null || nomPlateau.trim().isEmpty())
			{
				System.out.println("Le nom de fichier ne peut pas être vide.");
				return;
			}

			if (this.ctrl.enregistrerPlateau(nomPlateau.trim()))
			{
				System.out.println("Plateau \"" + nomPlateau.trim() + "\" sauvegardé avec succès dans le dossier sauvegarde/ !");
				new FrameJeu(this.ctrl);
				this.frmCreation.setVisible(false);
			}
		}

		if (e.getSource() == this.btnRetourConfiguration)
		{
			if (this.frmConfiguration == null)
			{
				this.frmConfiguration = new FrameConfiguration(this.ctrl);
			}

			this.frmConfiguration.setVisible(true);
			this.frmCreation.setVisible(false);
		}

		if (e.getSource() == this.btnPasserAuJeu)
		{
			new FrameJeu(this.ctrl);
			this.frmCreation.setVisible(false);
		}
	}
}