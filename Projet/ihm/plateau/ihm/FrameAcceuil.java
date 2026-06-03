package plateau.ihm;

import plateau.Controleur;

import java.awt.FlowLayout;
import java.awt.GridLayout;

import javax.swing.*;

public class FrameAcceuil extends JFrame
{
    public FrameAcceuil(Controleur ctrl)
    {
        this.setTitle       ("Acceuil Jeu");
        this.setLocation    ( 900, 400 );


        this.setLayout ( new FlowLayout() );

        this.add( new PanelAcceuil(this, ctrl) );

        this.setDefaultCloseOperation ( JFrame.EXIT_ON_CLOSE );

        this.setVisible ( true );
		this.pack();
    }

}