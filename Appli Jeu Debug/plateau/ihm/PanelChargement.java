package plateau.ihm;

import plateau.Controleur;

import java.awt.Image;
import java.awt.Font;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.FlowLayout;
import java.awt.BorderLayout;
import java.awt.Graphics;

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

    public PanelChargement(FrameChargement frmChargement, Controleur ctrl)
    {
        this.frmChargement = frmChargement;
        this.ctrl          = ctrl;

        // Gestion du fond
        String img = this.ctrl.getImageFond2();
        if (img != null) { this.imgBackground = new ImageIcon(img).getImage(); }

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
        // Bloc des deux boutons radio côte à côte
        JPanel panelModes = new JPanel(new GridLayout(1, 2, 10, 0));
        panelModes.setOpaque(false);
        panelModes.add(this.rbLocal);
        panelModes.add(this.rbMulti);

        // Bloc "Nombre de manches : [ ]" sur une ligne
        JPanel panelManches = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        panelManches.setOpaque(false);
        panelManches.add(lblManches);
        panelManches.add(this.txtManches);

        // Bloc des boutons d'action
        JPanel panelBoutons = new JPanel(new GridLayout(1, 2, 10, 0));
        panelBoutons.setOpaque(false);
        panelBoutons.add(this.btnRetour);
        panelBoutons.add(this.btnCharger);

        // Formulaire vertical : plateau, mode, nb de manches, statut, boutons
        JPanel panelFormulaire = new JPanel(new GridLayout(7, 1, 10, 10));
        panelFormulaire.setOpaque(false);
        panelFormulaire.add(lblTitre);
        panelFormulaire.add(this.comboFichiers);
        panelFormulaire.add(lblMode);
        panelFormulaire.add(panelModes);
        panelFormulaire.add(panelManches);
        panelFormulaire.add(this.lblStatut);
        panelFormulaire.add(panelBoutons);

        this.add(panelFormulaire, BorderLayout.CENTER);

        /*---------------------------*/
        /* Activation des composants */
        /*---------------------------*/
        this.btnCharger.addActionListener(this);
        this.btnRetour .addActionListener(this);
    }

    public void actionPerformed(ActionEvent e)
    {
        // 1. Charger le plateau sélectionné + appliquer le mode choisi
        if (e.getSource() == this.btnCharger)
        {
            int idx = this.comboFichiers.getSelectedIndex();
            if (idx >= 0 && idx < this.sauvegardes.length)
            {
                if (this.ctrl.chargerPlateau(this.sauvegardes[idx]))
                {
                    // Vérifier que le plateau possède bien des joueurs et des stations
                    if (!this.ctrl.plateauEstJouable())
                    {
                        this.lblStatut.setText("Plateau non finalisé (pas de départ ou de station).");
                        return;
                    }

                    // Récupérer le mode coché sur la fenêtre
                    if (this.rbMulti.isSelected())
                    {
                        this.lblStatut.setForeground(new Color(255, 180, 80));
                        this.lblStatut.setText("Mode multijoueur (réseau) à venir.");
                        // TODO : lancer la partie en multijoueur via le réseau (étape ultérieure)
                    }
                    else
                    {
                        // Lire le nombre de manches saisi
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

                        // Lance la partie locale : une fenêtre par joueur, pioche commune
                        this.ctrl.lancerPartieLocale(nbManches);
                        this.frmChargement.setVisible(false);
                    }
                }
                else
                {
                    this.lblStatut.setText("Erreur : fichier invalide ou corrompu.");
                }
            }
        }
        // 2. Revenir à l'accueil
        else if (e.getSource() == this.btnRetour)
        {
            new FrameAccueil(this.ctrl);
            this.frmChargement.setVisible(false);
        }
    }

    // Dessin : Affiche l'image de fond et applique un filtre noir translucide pour la lisibilité
    protected void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        if (this.imgBackground != null)
        {
            g.drawImage(this.imgBackground, 0, 0, getWidth(), getHeight(), this);
        }
        g.setColor(new Color(0, 0, 0, 130)); // 130 = opacité (semi-transparent)
        g.fillRect(0, 0, getWidth(), getHeight());
    }
}
