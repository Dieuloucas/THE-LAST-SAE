package plateau.ihm;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

// Gestionnaire d'événements (écouteur) pour centraliser les actions des boutons du jeu
public class ListenerJeu implements ActionListener
{
    private PanelJeu panel; // Référence vers le panneau principal pour accéder à ses composants

    public ListenerJeu(PanelJeu panel)
    {
        this.panel = panel;
    }

    // Méthode déclenchée automatiquement à chaque clic sur un bouton connecté
    public void actionPerformed(ActionEvent e)
    {
        // 1. Action : Clic sur le bouton "Importer"
        // Récupère le fichier sélectionné dans la liste déroulante et charge son aperçu s'il existe
        if (e.getSource() == panel.getBtnImporter())
        {
            Object selectedItem = panel.getComboFichiersImport().getSelectedItem();
            if (selectedItem != null)
            {
                File file = new File("sauvegarde", selectedItem.toString());
                if (file.exists()) panel.chargerApercu(file);
            }
        }
        
        // 2. Action : Clic sur le bouton "Créer Nouveau"
        // Ouvre la fenêtre de configuration et ferme (détruit) la fenêtre actuelle du jeu
        if (e.getSource() == panel.getBtnCreerNouveau())
        {
            new FrameConfiguration(panel.getCtrl());
            panel.getFrame().setVisible(false);
        }
        
        // 3. Action : Clic sur le bouton "Sauvegarder"
        // Si un fichier est chargé, valide les points de départ via le contrôleur, enregistre et ferme la fenêtre
        if (e.getSource() == panel.getBtnSauvegarder() && panel.getFichierCharge() != null)
        {
            if (panel.getCtrl().validerDepartsPlateau())
            {
                if (panel.getCtrl().enregistrerPlateau(panel.getFichierCharge().getName()))
                {
                    panel.getFrame().setVisible(false);
                }
            }
        }
    }
}
