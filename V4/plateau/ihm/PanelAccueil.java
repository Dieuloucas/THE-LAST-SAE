package plateau.ihm;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;

import plateau.Controleur;

public class PanelAccueil extends JPanel implements ActionListener
{
	private FrameAccueil frmAccueil;
	private Controleur   ctrl;

	private JButton btnConfiguration;
	private JButton btnRegles;
	private Image   imgFond;

	public PanelAccueil(FrameAccueil frmAccueil, Controleur ctrl)
	{
		this.frmAccueil = frmAccueil;
		this.ctrl       = ctrl;

		this.imgFond = new ImageIcon(Images.chemin("fond.png")).getImage();
		this.setPreferredSize(new Dimension(800, 600));
		this.setLayout(new GridBagLayout());

		this.btnConfiguration = new JButton("Configuration du plateau");
		this.btnRegles        = new JButton("Regles");

		JPanel boutons = new JPanel(new GridLayout(2, 1, 10, 10));
		boutons.setOpaque(false);
		boutons.add(this.btnConfiguration);
		boutons.add(this.btnRegles);
		this.add(boutons);

		this.btnConfiguration.addActionListener(this);
		this.btnRegles.addActionListener(this);
	}

	public void actionPerformed(ActionEvent e)
	{
		if (e.getSource() == this.btnConfiguration)
		{
			new FrameConfiguration(this.ctrl);
			this.frmAccueil.dispose();
		}
		else if (e.getSource() == this.btnRegles)
		{
			JOptionPane.showMessageDialog(this,
				"Reliez les stations de meme type pour conquerir les arrondissements.",
				"Regles", JOptionPane.INFORMATION_MESSAGE);
		}
	}

	protected void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		if (this.imgFond != null)
			g.drawImage(this.imgFond, 0, 0, getWidth(), getHeight(), this);
	}
}
