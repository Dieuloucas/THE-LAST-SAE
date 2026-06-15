package plateau.ihm;

import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.*;
import javax.swing.*;
import plateau.Controleur;

// Panneau de l'écran d'accueil contenant les boutons principaux et une image de fond
public class PanelAccueil extends JPanel implements ActionListener
{
    // Attributs : Composants graphiques, références des fenêtres et image de fond
    
    private JButton             btnConfiguration;
    private JButton             btnRegles;

    private FrameAccueil        frmAccueil;
    private FrameConfiguration  frmConfiguration;
    private Controleur          ctrl;

    private Image               imgFond;

    // Constructeur : Initialise le fond, crée les boutons et les centre à l'écran
    public PanelAccueil(FrameAccueil frmAccueil, Controleur ctrl)
    {
        this.frmAccueil = frmAccueil;
        this.ctrl       = ctrl;

        // Chargement de l'image de fond et définition de la taille (800x600)
        this.imgFond = new ImageIcon( this.ctrl.getImageFond() ).getImage();
        this.setPreferredSize(new Dimension(800, 600));

        /*-------------------------*/
        /* Création des composants */
        /*-------------------------*/
        this.btnConfiguration = new JButton("Configuration du plateau");
        this.btnRegles        = new JButton("Règles");

        /*-------------------------------*/
        /* Positionnement des composants */
        /*-------------------------------*/
        this.setLayout(new GridBagLayout()); // GridBagLayout utilisé pour centrer automatiquement le contenu

        // Regroupement des boutons dans une grille verticale transparente (2 lignes, 1 colonne)
        JPanel panelBoutons = new JPanel(new GridLayout(2, 1, 10, 10));
        panelBoutons.setOpaque(false);

        panelBoutons.add(this.btnConfiguration);
        panelBoutons.add(this.btnRegles);

        this.add(panelBoutons); // Ajout du bloc de boutons au centre du panneau principal

        /*---------------------------*/
        /* Activation des composants */
        /*---------------------------*/
        this.btnConfiguration.addActionListener(this);
        this.btnRegles       .addActionListener(this);
    }

    // Gestion des actions au clic sur les boutons
    public void actionPerformed(ActionEvent e)
    {
        // 1. Action : Clic sur "Configuration du plateau" -> Ouvre l'écran de config et cache l'accueil
        if (e.getSource() == this.btnConfiguration)
        {
            this.frmConfiguration = new FrameConfiguration(this.ctrl);
            this.frmAccueil         .setVisible(false);
            this.frmConfiguration   .setVisible(true);
        }
        // 2. Action : Clic sur "Règles" -> Cherche le fichier PDF local et l'ouvre avec le lecteur par défaut
        else if (e.getSource() == this.btnRegles)
        {
            try
            {
                String[] cheminRegles = {"../Règle les Cartographes du Métro.pdf"};

                java.io.File fichierPdf = null;
                for (String ch : cheminRegles)
                {
                    java.io.File testFichier = new java.io.File(ch);
                    if (testFichier.exists())
                    {
                        fichierPdf = testFichier;
                        break;
                    }
                }

                if (fichierPdf != null && Desktop.isDesktopSupported())
                {
                    Desktop.getDesktop().open(fichierPdf);
                }
                else
                {
                    System.out.println("Règles du jeu introuvable.");
                }
            }
            catch (Exception ex)
            {
                System.out.println("Erreur lors de l'ouverture des règles : " + ex.getMessage());
            }
        }
    }

    // Dessin du composant : Permet d'étirer l'image de fond sur toute la surface de l'écran
    protected void paintComponent(Graphics g)
    {
        super.paintComponent(g);

        if ( imgFond != null )
        {
            g.drawImage(imgFond, 0, 0, this.getWidth(), this.getHeight(), this);
        }
    }
}