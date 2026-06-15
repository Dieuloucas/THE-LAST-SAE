package plateau.ihm;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import plateau.Controleur;

// Formulaire permettant de configurer les paramètres de création d'une nouvelle partie
public class PanelConfiguration extends JPanel implements ActionListener
{
    // Attributs : Références des fenêtres, contrôleur, image de fond et composants d'affichage
    private FrameConfiguration  frmConfiguration;
    private FrameCreation       frmCreation;
    private Controleur          ctrl;
    private Image               imgBackground;

    private JPanel              panelConfiguration;

    // Éléments textuels (Labels)
    private JLabel              lblLargeur;
    private JLabel              lblHauteur;
    private JLabel              lblJoueurs;
    private JLabel              lblStation;
    private JLabel              lblArrondissments;
    private JLabel              lblTailleCases;

    // Champs de saisie (TextFields) statiques et publics (visibles depuis l'extérieur)
    public static JTextField    txtLargeur;
    public static JTextField    txtHauteur; 
    public static JTextField    txtJoueurs;
    public static JTextField    txtStation;
    public static JTextField    txtArrondissments;
    public static JTextField    txtTailleCases;

    private JButton             btnValider;
    private JButton             btnAnnuler;

    // Constructeur : Charge le fond, configure la grille (7x2) et initialise les champs avec des valeurs par défaut
    public PanelConfiguration(FrameConfiguration frmConfiguration, Controleur ctrl)
    {
        this.frmConfiguration   = frmConfiguration;
        this.ctrl               = ctrl;

        // Gestion du fond
        String image = this.ctrl.getImageFond2();
        if (image != null) { this.imgBackground = new ImageIcon(image).getImage(); }

        this.setLayout(new BorderLayout());
        this.setBorder(BorderFactory.createEmptyBorder(25, 25, 25, 25));

        // Création de la grille du formulaire (7 lignes, 2 colonnes)

        this.panelConfiguration = new JPanel(new GridLayout(7, 2, 10, 10));
        
        this.panelConfiguration.setOpaque(false);
        this.setOpaque(false);

        /*-------------------------*/
        /* Création des composants */
        /*-------------------------*/
        Font labelFont = new Font("Arial", Font.BOLD, 13);

        this.lblLargeur = new JLabel("Taille en largeur :");

        this.lblLargeur .setForeground(Color.WHITE);
        this.lblLargeur .setFont(labelFont);

        txtLargeur      = new JTextField(5);

        this.lblHauteur = new JLabel("Taille en hauteur :");

        this.lblHauteur .setForeground(Color.WHITE);
        this.lblHauteur .setFont(labelFont);

        txtHauteur      = new JTextField(5);

        this.lblJoueurs = new JLabel("Nombre de joueurs (max 4) :");

        this.lblJoueurs .setForeground(Color.WHITE);
        this.lblJoueurs .setFont(labelFont);

        txtJoueurs      = new JTextField(5);

        this.lblStation = new JLabel("Nombre de types de station (max 6) :");

        this.lblStation .setForeground(Color.WHITE);
        this.lblStation .setFont(labelFont);

        txtStation      = new JTextField(5);

        this.lblArrondissments = new JLabel("Nombre d'arrondissements (max 20) :");

        this.lblArrondissments  .setForeground(Color.WHITE);
        this.lblArrondissments  .setFont(labelFont);

        txtArrondissments       = new JTextField(5);

        this.lblTailleCases = new JLabel("Taille des cases :");

        this.lblTailleCases .setForeground(Color.WHITE);
        this.lblTailleCases .setFont(labelFont);

        txtTailleCases      = new JTextField(5);

        this.btnValider = new JButton("Valider");
        this.btnAnnuler = new JButton("Annuler");

        // Insertion des composants (Label à gauche, Champ de saisie ajusté à droite)

        this.panelConfiguration.add(this.lblLargeur);
        this.panelConfiguration.add(creerPanelAjuste(txtLargeur));

        this.panelConfiguration.add(this.lblHauteur);
        this.panelConfiguration.add(creerPanelAjuste(txtHauteur));

        this.panelConfiguration.add(this.lblJoueurs);
        this.panelConfiguration.add(creerPanelAjuste(txtJoueurs));

        this.panelConfiguration.add(this.lblStation);
        this.panelConfiguration.add(creerPanelAjuste(txtStation));

        this.panelConfiguration.add(this.lblArrondissments);
        this.panelConfiguration.add(creerPanelAjuste(txtArrondissments));

        this.panelConfiguration.add(this.lblTailleCases);
        this.panelConfiguration.add(creerPanelAjuste(txtTailleCases));

        // Ajout des boutons d'action sur la dernière ligne
        this.panelConfiguration.add(this.btnAnnuler);
        this.panelConfiguration.add(this.btnValider);

        // Remplissage automatique des valeurs par défaut pour les tests
        /*this.txtLargeur.setText("7");
        this.txtHauteur.setText("7");
        this.txtJoueurs.setText("4");                   
        this.txtStation.setText("4");
        this.txtArrondissments.setText("10");
        this.txtTailleCases.setText("80");*/

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

    // Méthode utilitaire pour éviter que les champs de texte s'étirent trop en largeur
    private JPanel creerPanelAjuste(JTextField tf)
    {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        p.setOpaque(false);
        p.add(tf);
        return p;
    }

    // Dessin : Affiche l'image de fond et applique un filtre noir translucide pour lisibilité
    protected void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        if (this.imgBackground != null)
        {
            g.drawImage(this.imgBackground, 0, 0, getWidth(), getHeight(), this);
        }
        g.setColor(new Color(0, 0, 0, 130)); // 130 correspond à l'opacité (semi-transparent)
        g.fillRect(0, 0, getWidth(), getHeight());
    }

    private boolean estVide()
    {
        return  txtLargeur.getText().isEmpty()          || 
                txtHauteur.getText().isEmpty()          || 
                txtJoueurs.getText().isEmpty()          || 
                txtStation.getText().isEmpty()          || 
                txtArrondissments.getText().isEmpty()   || 
                txtTailleCases.getText().isEmpty();
    }
    private void viderChamps()
    {
        txtLargeur          .setText("");
        txtHauteur          .setText("");
        txtJoueurs          .setText("");
        txtStation          .setText("");
        txtArrondissments   .setText("");
        txtTailleCases      .setText("");
    }
    // Traitement des actions au clic
    public void actionPerformed(ActionEvent e)
    {
        // Action 1 : Validation du formulaire
        if (e.getSource() == this.btnValider)
        {
            // Vérification de présence des données
            if (this.estVide())
            {
                System.out.println("Veuillez remplir tous les champs.");
                return; // Bloque l'exécution ici
            }

            try
            {
                // Conversion des textes en entiers

                int largeur             = Integer.parseInt(txtLargeur       .getText());
                int hauteur             = Integer.parseInt(txtHauteur       .getText());
                int nbJoueurs           = Integer.parseInt(txtJoueurs       .getText());
                int nbStations          = Integer.parseInt(txtStation       .getText());
                int nbArrondissements   = Integer.parseInt(txtArrondissments.getText());
                int tailleCases         = Integer.parseInt(txtTailleCases   .getText());

                // Batterie de tests de cohérence des données saisies (limites max et min)
                if (nbStations > 6) 
                { 
                    System.out.println("Le nombre de types de station ne peut pas dépasser 6."); 
                    return; 
                }
                if (nbJoueurs > 4) 
                { 
                    System.out.println("Le nombre de joueurs ne peut pas dépasser 4."); 
                    return; 
                }
                if (nbArrondissements > 20) 
                { 
                    System.out.println("Le nombre d'arrondissements ne peut pas dépasser 20."); 
                    return; 
                }
                if (largeur <= 0 || hauteur <= 0 || nbJoueurs <= 0 || nbStations <= 0 || nbArrondissements <= 0) 
                { 
                    System.out.println("Veuillez entrer des valeurs positives."); 
                    return; 
                }
                if (largeur < nbJoueurs) 
                { 
                    System.out.println("La largeur doit être cohérente avec le nombre de joueurs."); 
                    return; 
                }
                if (hauteur < nbJoueurs) 
                { 
                    System.out.println("La hauteur doit être cohérente avec le nombre de joueurs."); 
                    return; 
                }

                // Initialisation du modèle via le contrôleur (uniquement si aucun 'return' n'a été déclenché)

                this.ctrl.initialiserPlateau(largeur, hauteur);
                this.ctrl.setConfigJeu(nbJoueurs, nbStations);

                this.frmCreation        = new FrameCreation(this.ctrl);

                this.frmConfiguration   .setVisible(false);
                this.frmCreation        .setVisible(true);
            }
            catch (NumberFormatException ex)
            {
                System.out.println("Veuillez entrer uniquement des nombres.");
            }
        }

        // Action 2 : Annulation (Vide instantanément tous les champs de texte)
        if (e.getSource() == this.btnAnnuler)
        {
            this.viderChamps();
        }
    }
}