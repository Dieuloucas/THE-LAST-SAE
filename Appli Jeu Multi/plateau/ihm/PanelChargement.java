package plateau.ihm;

import plateau.Controleur;

import java.awt.*;
import java.awt.event.*;
import java.io.File;
import javax.swing.*;

// Écran de sélection d'un plateau : liste les sauvegardes (.txt) et choix du mode de jeu
public class PanelChargement extends JPanel implements ActionListener
{
	private FrameChargement     frmChargement;
	private Controleur          ctrl;
	private Image               imgBackground;
	private File[]              sauvegardes;
	private JComboBox<String>   comboFichiers;

	private JRadioButton        rbLocal;
	private JRadioButton        rbMulti;
	private JTextField          txtManches;
	private JButton             btnCharger;
	private JButton             btnRetour;
	private JLabel              lblStatut;

	private JPanel              panelOptionsReseau;
	private JRadioButton        rbHote;
	private JRadioButton        rbInvite;
	private JTextField          txtIP;
	private JTextField          txtPort;

	public PanelChargement(FrameChargement frmChargement, Controleur ctrl)
	{
		this.frmChargement = frmChargement;
		this.ctrl          = ctrl;
		
		// Gestion du fond
		String img = this.ctrl.getImageFond2();
		if (img != null) { this.imgBackground = new ImageIcon(img).getImage(); }

		this.ctrl.setPanelChargement(this);
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
		
		// Menu déroulant rempli avec les fichiers .txt trouvés
		this.comboFichiers = new JComboBox<String>();
		this.comboFichiers.setFont(labelFont);

		this.sauvegardes = this.ctrl.getSauvegardes();
		if (this.sauvegardes.length == 0)
		{
			this.comboFichiers.addItem("Aucun fichier trouvé");
		}
		else
		{
			for (int i = 0; i < this.sauvegardes.length; i++)
			{
				this.comboFichiers.addItem(this.sauvegardes[i].getName());
			}
		}

		// Choix du mode de jeu (sous le menu déroulant, sur la même fenêtre)
		JLabel lblMode = new JLabel("Mode de jeu :");
		lblMode.setForeground(Color.WHITE);
		lblMode.setFont(new Font("Arial", Font.BOLD, 16));

		this.rbLocal = new JRadioButton("Local", true);
		this.rbMulti = new JRadioButton("Multijoueur");

		this.rbLocal.setOpaque(false);
		this.rbMulti.setOpaque(false);
		this.rbLocal.setForeground(Color.WHITE);
		this.rbMulti.setForeground(Color.WHITE);
		this.rbLocal.setFont(labelFont);
		this.rbMulti.setFont(labelFont);
		
		ButtonGroup groupeMode = new ButtonGroup();
		groupeMode.add(this.rbLocal);
		groupeMode.add(this.rbMulti);

		// --- CONSTRUIRE LE BLOC RÉSEAU EN DUR (CACHÉ AU DÉBUT) ---
		this.panelOptionsReseau = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
		this.panelOptionsReseau.setOpaque(false);
		this.panelOptionsReseau.setVisible(false);

		this.rbHote = new JRadioButton("Créer (Hôte)", true);
		this.rbInvite = new JRadioButton("Rejoindre", false);
		this.rbHote.setOpaque(false);
		this.rbInvite.setOpaque(false);
		this.rbHote.setForeground(Color.WHITE);
		this.rbInvite.setForeground(Color.WHITE);
		this.rbHote.setFont(labelFont);
		this.rbInvite.setFont(labelFont);

		ButtonGroup groupeRole = new ButtonGroup();
		groupeRole.add(this.rbHote);
		groupeRole.add(this.rbInvite);

		this.txtIP = new JTextField("127.0.0.1", 10);
		this.txtPort = new JTextField("8080", 4);
		this.txtIP.setEnabled(false); // Désactivé si Hôte

		this.panelOptionsReseau.add(this.rbHote);
		this.panelOptionsReseau.add(this.rbInvite);
		this.panelOptionsReseau.add(new JLabel(" IP: "));
		this.panelOptionsReseau.add(this.txtIP);
		this.panelOptionsReseau.add(new JLabel(" Port: "));
		this.panelOptionsReseau.add(this.txtPort);

		// Nombre de manches (zone de texte)
		JLabel lblManches = new JLabel("Nombre de manches :");
		lblManches.setForeground(Color.WHITE);
		lblManches.setFont(labelFont);

		this.txtManches = new JTextField("1", 3);
		this.txtManches.setFont(labelFont);

		this.lblStatut = new JLabel(" ");
		this.lblStatut.setForeground(new Color(255, 100, 100));
		this.lblStatut.setFont(labelFont);
		
		this.btnCharger = new JButton("Charger");
		this.btnRetour  = new JButton("Retour");
		this.btnCharger.setFont(labelFont);
		this.btnRetour .setFont(labelFont);
		this.btnCharger.setEnabled(this.sauvegardes.length > 0);
		
		/*-------------------------------*/
		/* Positionnement des composants */
		/*-------------------------------*/
		JPanel panelModes = new JPanel(new GridLayout(1, 2, 10, 0));
		panelModes.setOpaque(false);
		panelModes.add(this.rbLocal);
		panelModes.add(this.rbMulti);

		JPanel panelManches = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
		panelManches.setOpaque(false);
		panelManches.add(lblManches);
		panelManches.add(this.txtManches);

		JPanel panelBoutons = new JPanel(new GridLayout(1, 2, 10, 0));
		panelBoutons.setOpaque(false);
		panelBoutons.add(this.btnRetour);
		panelBoutons.add(this.btnCharger);

		JPanel panelFormulaire = new JPanel(new GridLayout(8, 1, 10, 10)); 
		panelFormulaire.setOpaque(false);
		panelFormulaire.add(lblTitre);
		panelFormulaire.add(this.comboFichiers);
		panelFormulaire.add(lblMode);
		panelFormulaire.add(panelModes);
		panelFormulaire.add(this.panelOptionsReseau); 
		panelFormulaire.add(panelManches);
		panelFormulaire.add(this.lblStatut);
		panelFormulaire.add(panelBoutons);

		this.add(panelFormulaire, BorderLayout.CENTER);
		
		/*---------------------------*/
		/* Activation des composants */
		/*---------------------------*/
		this.btnCharger.addActionListener(this);
		this.btnRetour .addActionListener(this);

		this.rbLocal.addActionListener(e -> this.panelOptionsReseau.setVisible(false));
		this.rbMulti.addActionListener(e -> {
			this.panelOptionsReseau.setVisible(true);
			try
			{
				this.lblStatut.setForeground(Color.GREEN);
				this.lblStatut.setText("Votre IP locale : " + java.net.InetAddress.getLocalHost().getHostAddress());
			}
			catch(Exception ex) {}
		});

		this.rbHote.addActionListener(e -> {
			this.txtIP.setEnabled(false);
			this.comboFichiers.setEnabled(true); 
			this.txtManches.setEnabled(true);    
		});

		this.rbInvite.addActionListener(e -> {
			this.txtIP.setEnabled(true);
			this.comboFichiers.setEnabled(false); 
			this.txtManches.setEnabled(false);    
		});
	} // FIN DU CONSTRUCTEUR (C'est cette accolade qui manquait !)

	public void actionPerformed(ActionEvent e)
	{
		if (e.getSource() == this.btnCharger)
		{
			// CAS 1 : C'est un invité qui veut juste rejoindre (on ne vérifie pas les fichiers locaux)
			if (this.rbMulti.isSelected() && this.rbInvite.isSelected())
			{
				int port = 8080; 
				try { port = Integer.parseInt(this.txtPort.getText().trim()); } catch (Exception ex) {}

				this.btnCharger.setEnabled(false);
				this.lblStatut.setForeground(Color.GREEN);
				this.lblStatut.setText("Connexion en cours...");
				this.ctrl.lancerClientUniquement(this.txtIP.getText().trim(), port);
			}
			// CAS 2 : C'est l'Hôte ou une partie Locale (on doit valider le plateau et les manches)
			else 
			{
				int idx = this.comboFichiers.getSelectedIndex();
				if (idx >= 0 && idx < this.sauvegardes.length)
				{
					File f = this.sauvegardes[idx];
					if (this.ctrl.chargerPlateau(f))
					{
						if (!this.ctrl.plateauEstJouable())
						{
							this.lblStatut.setForeground(new Color(255, 100, 100));
							this.lblStatut.setText("Plateau non finalisé (pas de départ ou de station).");
							return;
						}

						int nbManches;
						try
						{
							nbManches = Integer.parseInt(this.txtManches.getText().trim());
						}
						catch (NumberFormatException ex)
						{
							this.lblStatut.setForeground(new Color(255, 100, 100));
							this.lblStatut.setText("Nombre de manches invalide.");
							return;
						}
						if (nbManches < 1)
						{
							this.lblStatut.setForeground(new Color(255, 100, 100));
							this.lblStatut.setText("Le nombre de manches doit être au moins 1.");
							return;
						}

						// Sous-cas A : Hôte Réseau
						if (this.rbMulti.isSelected() && this.rbHote.isSelected())
						{
							int port = 8080; 
							try { port = Integer.parseInt(this.txtPort.getText().trim()); } catch (Exception ex) {}

							this.btnCharger.setEnabled(false);
							this.lblStatut.setForeground(Color.GREEN);
							this.lblStatut.setText("Serveur ouvert ! En attente des autres joueurs...");
							this.ctrl.lancerServeurEtClient(port, nbManches, f);
						}
						// Sous-cas B : Partie Locale
						else
						{
							this.ctrl.lancerPartieLocale(nbManches);
							this.frmChargement.setVisible(false);
						}
					}
					else
					{
						this.lblStatut.setForeground(new Color(255, 100, 100));
						this.lblStatut.setText("Erreur : fichier invalide ou corrompu.");
					}
				}
			}
		}
		else if (e.getSource() == this.btnRetour)
		{
			new FrameAccueil(this.ctrl);
			this.frmChargement.setVisible(false);
		}
	}

	protected void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		if (this.imgBackground != null)
		{
			g.drawImage(this.imgBackground, 0, 0, getWidth(), getHeight(), this);
		}
		g.setColor(new Color(0, 0, 0, 130));
		g.fillRect(0, 0, getWidth(), getHeight());
	}

	public void setStatutLobby(String texte)
	{
		this.lblStatut.setForeground(Color.GREEN);
		this.lblStatut.setText(texte);
	}
}