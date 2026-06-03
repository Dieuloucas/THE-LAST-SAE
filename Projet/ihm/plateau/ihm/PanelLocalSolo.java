package plateau.ihm;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import plateau.Controleur;

public class PanelLocalSolo extends JPanel implements MouseListener
{
    private JPanel panelGrille;
    private JPanel[] tabCases = new JPanel[49];

    public PanelLocalSolo()
    {
        this.setLayout(new BorderLayout());

        this.panelGrille = new JPanel(new GridLayout(7, 7, 0, 0));

        /*-------------------------*/
        /* Création des composants */
        /*-------------------------*/

        for (int i = 0; i < 49; i++)
        {
            tabCases[i] = new JPanel();
            tabCases[i].setBackground(Color.LIGHT_GRAY);
            tabCases[i].setBorder(BorderFactory.createLineBorder(Color.BLACK));
			tabCases[i].setPreferredSize(new Dimension(80, 80));

            tabCases[i].addMouseListener(this);

            panelGrille.add(tabCases[i]);
        }

        /*-------------------------------*/
        /* Positionnement des composants */
        /*-------------------------------*/

        this.add(panelGrille, BorderLayout.CENTER);

        /*---------------------------*/
        /* Activation des composants */
        /*---------------------------*/
    }

    @Override
    public void mouseClicked(MouseEvent e)
    {
        for (int i = 0; i < 49; i++)
        {
            if (e.getSource() == tabCases[i])
            {
                int ligne = i / 7;
                int colonne = i % 7;

                System.out.println(i + " -> (" + ligne + "," + colonne + ")");

                tabCases[i].setBackground(Color.YELLOW);
            }
        }
    }

    @Override public void mousePressed(MouseEvent e) {}
    @Override public void mouseReleased(MouseEvent e) {}
    @Override public void mouseEntered(MouseEvent e) {}
    @Override public void mouseExited(MouseEvent e) {}
}