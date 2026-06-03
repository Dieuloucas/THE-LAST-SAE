package plateau.ihm;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import plateau.Controleur;

public class PanelAcceuil extends JPanel implements ActionListener
{
    private JButton btnJouerLocalSolo;
    private JButton btnJouerLocalPlusieurs;
    private JButton btnJouerReseau;
    private JButton btnDemo;
    private JButton btnQuitter;

    private FrameAcceuil 	frmAcceuil;
    private FrameConfiguration 	frmConfiguration;
    private Controleur 		ctrl;

    public PanelAcceuil(FrameAcceuil frmAcceuil, Controleur ctrl)
    {
        this.frmAcceuil = frmAcceuil;
        this.ctrl 		= ctrl;

        /*-------------------------*/
        /* Création des composants */
        /*-------------------------*/

        this.btnJouerLocalSolo 		= new JButton("Jouer en local (solo)");
        this.btnJouerLocalPlusieurs = new JButton("Jouer en local (plusieurs)");
        this.btnJouerReseau 		= new JButton("Jouer en réseau");
        this.btnDemo 				= new JButton("Démonstration");
        this.btnQuitter = new JButton("Quitter");

        /*-------------------------------*/
        /* Positionnement des composants */
        /*-------------------------------*/

        this.setLayout(new java.awt.GridLayout(5, 1));

        this.add(this.btnJouerLocalSolo);
        this.add(this.btnJouerLocalPlusieurs);
        this.add(this.btnJouerReseau);
        this.add(this.btnDemo);
        this.add(this.btnQuitter);

        /*---------------------------*/
        /* Activation des composants */
        /*---------------------------*/

        this.btnJouerLocalSolo 		.addActionListener(this);
        this.btnJouerLocalPlusieurs	.addActionListener(this);
        this.btnJouerReseau 		.addActionListener(this);
        this.btnDemo 				.addActionListener(this);
        this.btnQuitter 			.addActionListener(this);
    }

    public void actionPerformed(ActionEvent e)
    {
        if (e.getSource() == this.btnJouerLocalSolo)
        {
            if (this.frmConfiguration == null)
            {
                this.frmConfiguration = new FrameConfiguration(this.ctrl);
            }

            this.frmAcceuil.setVisible(false);
            this.frmConfiguration.setVisible(true);
        }

        else if (e.getSource() == this.btnJouerLocalPlusieurs)
        {
            System.out.println("Jouer en local (plusieurs)");
        }

        else if (e.getSource() == this.btnJouerReseau)
        {
            System.out.println("Jouer en réseau");
        }

        else if (e.getSource() == this.btnDemo)
        {
            System.out.println("Démonstration");
        }

        else if (e.getSource() == this.btnQuitter)
        {
            System.exit(0);
        }
    }
}