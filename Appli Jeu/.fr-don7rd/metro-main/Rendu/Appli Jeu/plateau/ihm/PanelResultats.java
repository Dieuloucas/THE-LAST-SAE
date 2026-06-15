package plateau.ihm;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.*;
import java.util.ArrayList;
import javax.swing.*;
import plateau.Controleur;
import plateau.metier.Joueur;

// Écran final : tableau des scores manche par manche, total, et désignation du ou des gagnant(s).
public class PanelResultats extends JPanel implements ActionListener
{
    private Controleur 	ctrl;
    private JButton 	btnQuitter;

    public PanelResultats(Controleur ctrl)
    {
        this.ctrl = ctrl;

        Joueur[] joueurs 	= ctrl.getJoueurs();
        int nbManches 		= ctrl.getNbManches();

        /*-------------------------*/
        /* Création des composants */
        /*-------------------------*/
        // Titre en haut de l'écran
        JLabel titre = new JLabel("Résultats", SwingConstants.CENTER);
        titre.setFont(new Font("Arial", Font.BOLD, 20));

        // Tableau des scores : une ligne par joueur, une colonne par manche puis le total
        JPanel grille = new JPanel(new GridLayout(joueurs.length + 1, nbManches + 2));

        grille.add(new JLabel("Joueur", SwingConstants.CENTER));
        for (int m = 1; m <= nbManches; m++)
            grille.add(new JLabel("M" + m, SwingConstants.CENTER));
        grille.add(new JLabel("Total", SwingConstants.CENTER));

        for (int i = 0; i < joueurs.length; i++)
        {
            grille.add(new JLabel("Joueur " + (i + 1), SwingConstants.CENTER));

            for (int m = 0; m < nbManches; m++)
                grille.add(new JLabel("" + joueurs[i].getScoreManche(m), SwingConstants.CENTER));

            grille.add(new JLabel("" + joueurs[i].getScoreTotal(), SwingConstants.CENTER));
        }

        // Libellé du ou des gagnant(s)
        String texte = "Gagnant : ";
        ArrayList<Integer> gagnants = ctrl.getGagnants();
        for (int i = 0; i < gagnants.size(); i++)
        {
            if (i > 0) texte += ", ";
            texte += "Joueur " + gagnants.get(i);
        }
        JLabel lblGagnant = new JLabel(texte, SwingConstants.CENTER);

        // Bouton de fermeture
        btnQuitter = new JButton("Quitter");

        /*-------------------------------*/
        /* Positionnement des composants */
        /*-------------------------------*/
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);

        add(titre, BorderLayout.NORTH);
        add(grille, BorderLayout.CENTER);

        // Gagnant(s) et bouton "Quitter" regroupés en bas de l'écran
        JPanel bas = new JPanel(new BorderLayout());
        bas.add(lblGagnant, BorderLayout.CENTER);
        bas.add(btnQuitter, BorderLayout.SOUTH);
        add(bas, BorderLayout.SOUTH);

        /*---------------------------*/
        /* Activation des composants */
        /*---------------------------*/
        btnQuitter.addActionListener(this);
    }

    public void actionPerformed(ActionEvent e)
    {
        if (e.getSource() == btnQuitter)
            System.exit(0);
    }
}
