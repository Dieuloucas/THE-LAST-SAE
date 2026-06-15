package plateau.ihm;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

public class PanelErreur extends JPanel
{

	private JLabel ligne1;
	private JLabel ligne2;
	private JLabel ligne3;
	private JLabel espace1;
	private JLabel espace2;

	public PanelErreur(String nomFichier)
	{
		this.setLayout(new GridLayout(5, 1));
		this.setOpaque(false);

		// Instanciation des variables
		this.ligne1  = new JLabel("Fichier de plateau corrompu !", SwingConstants.CENTER);
		this.ligne2  = new JLabel("Le fichier " + nomFichier + " n'a pas pu être chargé.", SwingConstants.CENTER);
		this.ligne3  = new JLabel("Il contient des structures d'arêtes invalides ou des valeurs hors-bornes.", SwingConstants.CENTER);
		this.espace1 = new JLabel(" ");
		this.espace2 = new JLabel(" ");

		// Configuration du style
		this.ligne1.setFont(new Font("Arial", Font.BOLD, 14));
		this.ligne1.setForeground(Color.RED);

		this.ligne2.setFont(new Font("Arial", Font.PLAIN, 13));

		this.ligne3.setFont(new Font("Arial", Font.PLAIN, 13));

		// Ajout au panneau
		this.add(this.ligne1);
		this.add(this.espace1);
		
		this.add(this.ligne2);
		this.add(this.espace2);
		
		this.add(this.ligne3);
	}
}
