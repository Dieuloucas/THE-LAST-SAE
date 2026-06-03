package plateau.ihm;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.ArrayList;

public class PanelCreation extends JPanel implements MouseListener, ActionListener
{
    private JPanel panelPlateau;
	private JPanel panelArrondissments;
    private JPanel[] tabCases = new JPanel[ Integer.parseInt(PanelConfiguration.txtLargeur.getText()) * Integer.parseInt(PanelConfiguration.txtHauteur.getText()) ];

    private int grillelargeur           = Integer.parseInt(PanelConfiguration.txtLargeur.getText());
    private int grillehauteur           = Integer.parseInt(PanelConfiguration.txtHauteur.getText());
    // private int nombreJoueurs           = Integer.parseInt(PanelConfiguration.txtJoueurs.getText());
    // private int nombreMetros            = Integer.parseInt(PanelConfiguration.txtMetro.getText());
    private int nombreArrondissments    = Integer.parseInt(PanelConfiguration.txtArrondissments.getText());
    private int tailleCases             = Integer.parseInt(PanelConfiguration.txtTailleCases.getText());

    private JButton[] btnArrondissments = new JButton[ nombreArrondissments ];

    private Color couleurSelectionnee = Color.LIGHT_GRAY;

    private String[] tabNomsArrondissments =
    {
        "1er","2ème","3ème","4ème","5ème",
        "6ème","7ème","8ème","9ème","10ème",
        "11ème","12ème","13ème","14ème","15ème",
        "16ème","17ème","18ème","19ème","20ème"
    };

    private Color[] tabCouleurs = 
    {

        new Color(255, 105, 180), // rose vif
        new Color(138, 43, 226),  // violet
        new Color(255, 165, 0),   // orange
        new Color(0, 255, 255),   // cyan
        new Color(176, 196, 222), // bleu acier clair
        new Color(255, 20, 147),  // rose profond
        new Color(186, 85, 211),  // orchidée
        new Color(148, 0, 211),   // violet foncé
        new Color(210, 105, 30),  // chocolat
        new Color(244, 164, 96),  // sable
        new Color(199, 21, 133),  // violet rougeâtre
        new Color(123, 104, 238), // bleu-violet doux
        new Color(255, 140, 0),   // orange foncé
        new Color(0, 206, 209),   // turquoise
        new Color(72, 61, 139),   // bleu nuit
        new Color(220, 20, 60),   // cramoisi
        new Color(169, 169, 169), // gris foncé
        new Color(245, 222, 179), // beige
        new Color(255, 228, 225), // rose très pâle
        new Color(255, 192, 203)  // rose clair
        
    };


    public PanelCreation()
    {
        
        this.setLayout(new BorderLayout());

        this.panelPlateau = new JPanel(new GridLayout(this.grillehauteur, this.grillelargeur, 0, 0));

		this.panelArrondissments = new JPanel(new GridLayout(this.nombreArrondissments, 1, 5, 5));

        /*-------------------------*/
        /* Création des composants */
        /*-------------------------*/

        for (int i = 0; i < this.grillelargeur * this.grillehauteur; i++)
        {
            tabCases[i] = new JPanel();
            tabCases[i].setBackground(Color.LIGHT_GRAY);
            tabCases[i].setBorder(BorderFactory.createLineBorder(Color.BLACK));
			tabCases[i].setPreferredSize(new Dimension(this.tailleCases, this.tailleCases));
        }   

        for (int i = 0; i < this.nombreArrondissments; i++)
        {
            btnArrondissments[i] = new JButton( this.tabNomsArrondissments[i] + " Arrondissement" );
            btnArrondissments[i].setBackground( this.tabCouleurs[i] );
        }



        /*-------------------------------*/
        /* Positionnement des composants */
        /*-------------------------------*/

        this.add(this.panelPlateau, BorderLayout.CENTER);

        this.add(this.panelArrondissments, BorderLayout.WEST);

        for (int i = 0; i < this.grillelargeur * this.grillehauteur; i++)
        {
            this.panelPlateau.add(tabCases[i]);
        }

        for (int i = 0; i < this.nombreArrondissments; i++)
        {
            this.panelArrondissments.add(btnArrondissments[i]);
        }

        /*---------------------------*/
        /* Activation des composants */
        /*---------------------------*/

        for (int i = 0; i < this.grillelargeur * this.grillehauteur; i++)
        {
            tabCases[i].addMouseListener(this);
        }

        for (int i = 0; i < this.nombreArrondissments; i++)
        {
            btnArrondissments[i].addActionListener(this);
        }
    }

    public void mouseClicked(MouseEvent e)
    {
        for (int i = 0; i < tabCases.length; i++)
        {
            if (e.getSource() == tabCases[i])
            {
                tabCases[i].setBackground(couleurSelectionnee);
            }
        }
    }

    public void mousePressed(MouseEvent e)  {}

    public void mouseReleased(MouseEvent e) {}

    public void mouseEntered(MouseEvent e)  {}

    public void mouseExited(MouseEvent e)   {}

    public void actionPerformed(ActionEvent e)
    {
        for (int i = 0; i < nombreArrondissments; i++)
        {
            if (e.getSource() == btnArrondissments[i])
            {
                couleurSelectionnee = btnArrondissments[i].getBackground();
                System.out.println( this.tabNomsArrondissments[i] + " Arrondissement sélectionné");
            }
        }
    }
}