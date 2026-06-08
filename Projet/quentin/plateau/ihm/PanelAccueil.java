package plateau.ihm;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import plateau.Controleur;

public class PanelAccueil extends JPanel implements ActionListener
{
    private JButton btnConfiguration;
    private JButton btnRegles;

    private FrameAccueil        frmAccueil;
    private FrameConfiguration  frmConfiguration;
    private Controleur          ctrl;

    private Image imgFond;

    public PanelAccueil(FrameAccueil frmAccueil, Controleur ctrl)
    {
        this.frmAccueil = frmAccueil;
        this.ctrl       = ctrl;

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

        this.setLayout(new GridBagLayout());

        JPanel panelBoutons = new JPanel(new GridLayout(2, 1, 10, 10));
        panelBoutons.setOpaque(false);

        panelBoutons.add(this.btnConfiguration);
        panelBoutons.add(this.btnRegles);

        this.add(panelBoutons);

        /*---------------------------*/
        /* Activation des composants */
        /*---------------------------*/

        this.btnConfiguration.addActionListener(this);
        this.btnRegles       .addActionListener(this);
    }

    public void actionPerformed(ActionEvent e)
    {
        if (e.getSource() == this.btnConfiguration)
        {
            this.frmConfiguration = new FrameConfiguration(this.ctrl);
            this.frmAccueil         .setVisible(false);
            this.frmConfiguration   .setVisible(true);
        }
        else if (e.getSource() == this.btnRegles)
        {
            System.out.println("Affichage des règles du jeu...");
        }
    }

    protected void paintComponent(Graphics g)
    {
        super.paintComponent(g);

        if ( imgFond != null )
        {
            g.drawImage(imgFond, 0, 0, this.getWidth(), this.getHeight(), this);
        }
    }
}
