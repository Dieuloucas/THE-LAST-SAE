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

public class PanelResultats extends JPanel implements ActionListener
{
    private Controleur 	ctrl;
    private JButton 	btnQuitter;

    public PanelResultats(Controleur ctrl)
    {
        this.ctrl = ctrl;

        setLayout(new BorderLayout());
        setBackground(Color.WHITE);

        Joueur[] joueurs 	= ctrl.getJoueurs();
        int nbManches 		= ctrl.getNbManches();

        // Titre
        JLabel titre 		= new JLabel("Résultats", SwingConstants.CENTER);
        titre.setFont(new Font("Arial", Font.BOLD, 20));
        add(titre, BorderLayout.NORTH);

        // Tableau des scores
        JPanel grille 		= new JPanel(new GridLayout(joueurs.length + 1, nbManches + 2));

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

        add(grille, BorderLayout.CENTER);

        // Gagnant(s)
        ArrayList<Integer> gagnants = ctrl.getGagnants();

        String texte = "Gagnant : ";
        for (int i = 0; i < gagnants.size(); i++)
        {
            if (i > 0) texte += ", ";
            texte += "Joueur " + gagnants.get(i);
        }

        JLabel lblGagnant = new JLabel(texte, SwingConstants.CENTER);

        // Bouton quitter
        btnQuitter = new JButton("Quitter");
        btnQuitter.addActionListener(this);

        JPanel bas = new JPanel(new BorderLayout());
        bas.add(lblGagnant, BorderLayout.CENTER);
        bas.add(btnQuitter, BorderLayout.SOUTH);

        add(bas, BorderLayout.SOUTH);
    }

    public void actionPerformed(ActionEvent e)
    {
        if (e.getSource() == btnQuitter)
            System.exit(0);
    }
}
