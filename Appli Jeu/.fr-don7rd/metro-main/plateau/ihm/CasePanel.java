package plateau.ihm;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.*;
import javax.swing.*;
import plateau.Controleur;
import plateau.metier.UtilitaireJeu;

public class CasePanel extends JPanel
{
    // Attributs : Stockent les données de la case et les références pour communiquer
    private int         index;
    private Controleur  ctrl;
    private PanelJeu    panelJeu;
    private Color[]     tabCouleurs;

    // Constructeur : Initialise la case et configure l'écouteur de clic
    public CasePanel(int index, Controleur ctrl, PanelJeu panelJeu)
    {
        this.index          = index;
        this.ctrl           = ctrl;
        this.panelJeu       = panelJeu;
        this.tabCouleurs    = UtilitaireJeu.getCouleurs();

        // Aspect visuel de base (taille et bordure)
        this.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        this.setPreferredSize(new Dimension(50, 50));
        
        // Gestion du clic : prévient le PanelJeu qu'on a cliqué sur cette case
        this.addMouseListener(new MouseAdapter()
        {
            public void mousePressed(MouseEvent e)
            {
                CasePanel.this.panelJeu.caseCliquee(CasePanel.this.index);
            }
        });
    }

    // Dessin de la case : Gère l'affichage visuel (Fond, Station, Départ)
    protected void paintComponent(Graphics g)
    {
        super.paintComponent(g);

        int w = getWidth();
        int h = getHeight();
        
        // 1. Coloration du fond selon l'arrondissement renvoyé par le contrôleur
        int arr = ctrl.getArrondissement(index);
        if (arr == 0)                          { g.setColor(Color.LIGHT_GRAY); }
        else if (arr > 0 && arr <= tabCouleurs.length) { g.setColor(tabCouleurs[arr - 1]); }
        else                                   { g.setColor(Color.WHITE); }
        g.fillRect(0, 0, w, h);

        // 2. Superposition de l'image de la station si elle existe
        int station = ctrl.getStation(index);
        if (station > 0)
        {
            Image img = panelJeu.getImageStation(station);
            if (img != null) { g.drawImage(img, 2, 2, w - 4, h - 4, this); }
        }

        // 3. Affichage du texte de départ (ex: "D1") si la case est un point de départ
		
        int depart = ctrl.getDepart(index);
        if (depart > 0)
        {
            g.setColor(Color.BLACK);
            g.setFont(new Font("Arial", Font.BOLD, 14));
            g.drawString("D" + depart, 5, 18);
        }
    }
}