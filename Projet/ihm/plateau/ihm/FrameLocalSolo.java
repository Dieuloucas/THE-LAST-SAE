package plateau.ihm;

import plateau.Controleur;

import java.awt.FlowLayout;
import java.awt.GridLayout;

import javax.swing.*;

public class FrameLocalSolo extends JFrame
{
    public FrameLocalSolo(Controleur ctrl)
    {
        this.setTitle       ("Jeu Local en Solo");
        this.setLocation    ( 800, 400 );


        this.setLayout ( new FlowLayout() );

        this.add ( new PanelLocalSolo() );

        this.setDefaultCloseOperation ( JFrame.EXIT_ON_CLOSE );

        this.setVisible ( true );
		this.pack();
    }

}