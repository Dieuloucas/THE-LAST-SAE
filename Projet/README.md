# Les Cartographes du Métro — projet (branche V3)

Version **simple et propre** du projet, pensée pour être **expliquée à l'oral**.
Une seule appli, pas de variantes.

## Structure
```
Projet/
├── plateau/
│   ├── Controleur.java        ← la facade (ne fait qu'appeler le metier)
│   ├── metier/Plateau.java    ← le metier (les donnees + sauvegarde/chargement)
│   ├── ihm/                   ← les ecrans Swing (Accueil, Configuration, Creation, Jeu)
│   ├── images/                ← images des metros et fonds
│   └── sauvegarde/            ← plateaux enregistres (.txt)
├── compile.list              ← liste des fichiers a compiler
└── WIKI-metier.md            ← explication de CHAQUE methode du metier (a lire pour l'oral)
```

## Architecture (MVC)
- **metier** (`plateau.metier.Plateau`) : les donnees du plateau + les regles. Ne connait ni l'ecran ni le controleur.
- **controleur** (`plateau.Controleur`) : le pont. Ne contient pas de logique, il appelle le metier.
- **ihm** (`plateau.ihm.*`) : les fenetres Swing. N'appellent que le controleur.

## Compiler et lancer
```bash
cd Projet
javac -d . @compile.list
java plateau.Controleur
```

## Comment ca marche (parcours)
`Accueil → Configuration → Creation → Jeu`
1. **Configuration** : on choisit la taille, le nombre de joueurs et de metros.
2. **Creation** : on peint les cases pour dessiner les arrondissements, puis on enregistre (fichier `.txt`).
3. **Jeu** : on place les metros et les departs des joueurs sur le plateau.

## Le fichier de sauvegarde (`sauvegarde/*.txt`)
```
largeur;hauteur;nbJoueurs;nbMetro     <- 1re ligne (la config)
numCase;arrondissement;metro;depart   <- 1 ligne par case
```

## Pour l'oral
Lis **`WIKI-metier.md`** : il explique chaque methode du metier (ce qu'elle fait, comment,
pourquoi, et le role de chaque morceau de code), plus les notions Java a savoir reciter.
