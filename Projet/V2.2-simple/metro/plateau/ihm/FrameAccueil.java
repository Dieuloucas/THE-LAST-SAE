package plateau.ihm;

import plateau.Controleur;

import javax.swing.*;

public class FrameAccueil extends JFrame
{
    public FrameAccueil(Controleur ctrl)
    {
        this.setTitle       ("Accueil Jeu");

        this.add( new PanelAccueil(this, ctrl) );

        this.setDefaultCloseOperation ( JFrame.EXIT_ON_CLOSE );

        this.pack();
        this.setLocationRelativeTo(null); // Centre la fenêtre sur l'écran
        this.setVisible ( true );
    }
}
