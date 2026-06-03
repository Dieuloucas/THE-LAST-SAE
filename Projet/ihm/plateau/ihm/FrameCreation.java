package plateau.ihm;

import plateau.Controleur;

import java.awt.FlowLayout;
import java.awt.GridLayout;

import javax.swing.*;

public class FrameCreation extends JFrame
{
    public FrameCreation(Controleur ctrl)
    {
        this.setTitle       ("Création Plateau");
        this.setLocation    ( 100, 100 );


        this.setLayout ( new FlowLayout() );

        this.add ( new PanelCreation() );

        this.setDefaultCloseOperation ( JFrame.EXIT_ON_CLOSE );

        this.setVisible ( true );
		this.pack();
    }

}