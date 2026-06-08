# Les Cartographes du Métro — V4

Version **fusion** (le meilleur de la V3 et de l'appli de Quentin), **orientée qualité + objet** :
- métier **riche et objet** (toute la logique du jeu),
- **IHM mince** (elle affiche et transmet les clics, elle ne décide rien),
- contrôleur = simple **façade** qui appelle le métier.

## Structure
```
V4/
├── plateau/
│   ├── Controleur.java          ← façade (appelle le métier) + main
│   ├── metier/
│   │   ├── Case.java            ← une case (arrondissement + station + départ)
│   │   ├── Configuration.java   ← config de la partie (nb joueurs/stations) + vérifications
│   │   ├── Plateau.java         ← la grille de Case + le réseau d'arêtes + LA LOGIQUE
│   │   └── Sauvegarde.java      ← lecture/écriture fichier (séparation des responsabilités)
│   ├── ihm/
│   │   ├── Images.java          ← retrouve le chemin d'une image
│   │   ├── Couleurs.java        ← palette + noms de stations (données d'affichage)
│   │   ├── FrameAccueil/Configuration/Creation/Jeu.java
│   │   └── PanelAccueil/Configuration/Creation/Jeu.java
│   ├── images/                  ← images des stations (1..6) + fonds
│   └── sauvegarde/              ← plateaux enregistrés (.txt)
├── compile.list
├── README.md
├── WIKI-metier.md               ← explication de chaque méthode du métier
└── NOTES-niveau.md              ← méthodes éventuellement « hors niveau » + alternatives
```

## Architecture (MVC) et la règle d'or
- **metier** : les données + **toutes les règles** (placement, arêtes, validations). Ne connaît ni l'écran ni le contrôleur.
- **controleur** : le pont. **Ne contient aucune logique**, il appelle le métier.
- **ihm** : les fenêtres Swing. N'appellent que le contrôleur.

> Règle d'or : si une méthode de l'IHM contient un calcul ou une règle, elle est mal placée → ça doit descendre dans le métier.

## Compiler et lancer
```bash
cd V4
javac -d . @compile.list
java plateau.Controleur
```

## Parcours
`Accueil → Configuration → Création → Jeu`
1. **Configuration** : taille, nombre de joueurs et de stations (le **métier** vérifie les valeurs).
2. **Création** : on peint les arrondissements, puis on enregistre (`.txt`).
3. **Jeu** : on place les **stations** et les **départs** ; les **arêtes** se génèrent toutes seules (chaque station se relie à la plus proche dans 8 directions).

## Ce que la fusion apporte (vs V3)
- métier découpé en **objets** (Case, Configuration, Plateau, Sauvegarde) — qualité + objet ;
- **stations** (au lieu de « métro ») et **arêtes automatiques** (idée de Quentin), calculées dans le métier ;
- **logique remontée de l'IHM vers le métier** : vérification de config, « toutes les cases remplies », « un seul départ par joueur », génération des arêtes, listage des sauvegardes.

## Format de sauvegarde (`sauvegarde/*.txt`)
```
largeur;hauteur;nbJoueurs;nbStations     ← 1re ligne
numCase;arrondissement;station;depart     ← 1 ligne par case
```
Les arêtes ne sont pas enregistrées : elles se recalculent au chargement (données dérivées).
