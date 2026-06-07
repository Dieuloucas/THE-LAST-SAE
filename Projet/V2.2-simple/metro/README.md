# V2.2 — version simplifiée (pour l'oral)

Copie de l'appli V2.2 (Quentin/Antoine), **simplifiée pour pouvoir expliquer chaque ligne**.
Mêmes fonctionnalités, mais avec uniquement des notions vues en cours.

## Ce qui a été remplacé (et par quoi)

| Avant (V2.2) | Après (cette version) |
|---|---|
| Lambdas `e -> {...}` | logique dans `actionPerformed` (`implements ActionListener`) |
| `JFileChooser` (explorateur) | on tape le nom du fichier dans la **console** (`Scanner`) |
| `JOptionPane` (popups de saisie/message) | `System.out.println` / `Scanner` dans la console |
| `paintComponent` + `Graphics` (images, dessins) | chaque case = `JPanel`/`JLabel` coloré + texte `M3` / `D2` |
| Champs `static` partagés entre panels | valeurs passées par les **constructeurs** |
| `try (...) {}` (try-with-resources) | `try { } catch { }` classique |
| `JMenuBar` / `JMenu` | un simple `JButton "Importer"` |
| Images des métros (`.png`) | texte `M1`..`M6` dans la case |

## Architecture (MVC)
- `plateau.metier.Plateau` : le modèle (3 tableaux d'`int` : arrondissement, métro, départ) + sauvegarde/chargement texte.
- `plateau.Controleur` : la façade (ne fait qu'appeler le métier) + le `main`.
- `plateau.ihm.*` : les écrans Swing (Accueil → Configuration → Création → Jeu).

## Compiler / lancer
```bash
cd Projet/V2.2-simple/metro
javac -d . @compile.list
java plateau.Controleur
```
> Les saisies (nom de plateau, fichier à charger) se font **dans la console**.

## Format de sauvegarde (`sauvegarde/*.txt`)
```
largeur;hauteur;nbJoueurs;nbMetro
numCase;arrondissement;metro;depart
...
```
