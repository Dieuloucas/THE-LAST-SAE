# Wiki — Les Cartographes du Métro

Petit guide pour comprendre l'architecture du projet (couche **métier** + **contrôleur**).
But : que n'importe qui dans l'équipe puisse retrouver « quelle méthode fait quoi » et « qui appelle qui ».

---

## 1. Architecture générale (MVC)

```
ihm/         <-- l'affichage (Swing). N'appelle QUE le contrôleur.
controleur/  <-- la façade. Ne contient PAS de logique : il appelle le métier.
metier/      <-- toute la logique du jeu. Ne connaît ni l'IHM ni le contrôleur.
```

**Règle d'or :** le contrôleur ne fait que des **appels** au métier. Si une méthode du
contrôleur contient un algorithme (boucle, calcul...), c'est qu'elle est mal placée :
l'algo doit descendre dans le métier.

Flux d'une partie :
`creerPlateau → (édition : ajouterSommet / creerZone / poserBase) → demarrerPartie → piocher → getSommetsJouables → connecter → joueurSuivant → tourTermine → mancheSuivante → getClassement`

---

## 2. Les concepts du jeu

| Concept | Classe | En français |
|---|---|---|
| Le plateau | `Plateau` | la carte (une grille lignes × colonnes) |
| Une station | `Sommet` | une case occupée, qui porte un symbole |
| Un lien possible | `Arete` | relie deux stations voisines (8 directions) |
| Un quartier | `Zone` | **un ensemble de cases** (peut contenir du vide) |
| Un point de départ | `Base` | une station de départ pour une couleur |
| Une carte piochée | `Carte` | un symbole + une teinte (ou un joker) |
| La pioche | `Pioche` | les 10 cartes mélangées |
| Une manche | `Manche` | un tour complet pour une couleur |
| Le réseau d'un joueur | `CheminCouleur` | les stations reliées par un joueur pour une couleur |
| Un joueur | `Joueur` | un nom + un réseau par couleur |
| La partie | `Jeu` | orchestre manches, joueurs, connexions, score |
| Le stockage des plateaux | `GestionnairePlateaux` | crée / duplique / sauvegarde les plateaux |

> **Vision « Zone = cases »** (importante) : une zone est une liste de cases `(ligne, colonne)`,
> pas une liste de stations. Une station « est dans » une zone si **sa case** y figure. Un
> quartier peut donc s'étendre sur des cases vides.

---

## 3. Les fichiers métier (`metier/`)

### `Symbole`, `Couleur`, `Teinte` (enums)
- `Symbole { CERCLE, CROIX, TRIANGLE, CARRE }` — le symbole d'une station/carte.
- `Couleur { ROUGE, VERT, BLEU, MARRON }` — une couleur = un réseau = une manche.
- `Teinte { FONCE, CLAIR }` — les cartes foncées font avancer la fin de manche.

### `Sommet` — une station
- `getLigne() / getColonne()` : sa position.
- `getSymbole() / setSymbole(...)` : son symbole.
- Comparaison par identité (`==`), pas d'`equals`.

### `Arete` — un lien entre deux stations
- `getS1() / getS2()` : les deux bouts.
- `getAutre(s)` : renvoie l'autre bout (utile pour parcourir les voisins).
- `relie(a, b)` : vrai si l'arête relie bien `a` et `b` (dans un sens ou l'autre).

### `Zone` — un quartier (ensemble de cases)
- `ajouterCase(ligne, colonne)` : ajoute une case (sans doublon).
- `contientCase(ligne, colonne)` : la case est-elle dans la zone ?
- `getCases()` : la liste des cases.
- `getTaille()` : nombre de cases.

### `Base` — un point de départ
- `getCouleur()` : la couleur qui démarre ici.
- `getSommet()` : la station de départ.

### `Plateau` — la carte (PUBLIC)
Le cœur de l'édition. Contient sommets, arêtes, zones, bases.
- `ajouterSommet(l, c, symbole)` / `supprimerSommet(s)` : pose/retire une station.
- `getSommet(l, c)` : la station à cette position (ou `null` si la case est vide).
- `genererAretes()` : **(re)calcule toutes les arêtes** à partir des positions des stations
  (4 demi-directions Est / Sud-Ouest / Sud / Sud-Est → chaque arête créée une seule fois).
  À rappeler après chaque ajout/suppression de station.
- `getVoisins(s)` : les stations reliées à `s` par une arête.
- `ajouterZone()` / `getZones()` : gère les quartiers.
- `ajouterBase(couleur, s)` / `getBases(couleur)` : gère les départs (filtrés par couleur).
- `dupliquer()` : **copie profonde et indépendante** du plateau (stations, zones par cases,
  bases reliées par position) + régénère les arêtes.
- `getId() / setId(...)`, `getNom() / setNom(...)` : identité gérée par le gestionnaire.

### `GestionnairePlateaux` — stockage + persistance (PUBLIC)
Possède la liste des plateaux et l'id auto-incrémenté.
- `creer(l, c, nom)` : crée un plateau, lui donne un id + un nom, le stocke.
- `dupliquer(original, nom)` : appelle `original.dupliquer()` puis l'enregistre.
- `supprimer(p)` / `getParId(id)` / `getPlateaux()` : gestion du menu.
- `sauvegarder(p, chemin)` : écrit le plateau en texte (zones stockées par leurs cases ;
  les arêtes ne sont pas sauvegardées, elles sont régénérées au chargement).
- `charger(chemin)` : relit un plateau, le ré-enregistre (id + nom) et le renvoie.

### `Carte` — une carte de pioche
- Construite par usine : `Carte.de(symbole, teinte)` ou `Carte.joker(teinte)`.
- `getSymbole()` (`null` si joker), `getTeinte()`, `estJoker()`, `estFonce()`.

### `Pioche` — les 10 cartes
- Construit 1 carte par (symbole × teinte) + 1 joker par teinte = 10 cartes, puis mélange.
- `piocher()` : retire et renvoie la dernière carte (pas de remise) ; `null` si vide.
- `estVide()`, `getNbTotal()`, `reinitialiser()`.

### `Manche` — un tour pour une couleur
- `piocher()` : pioche et compte les cartes foncées.
- `tourTermine()` : vrai quand assez de cartes foncées sont sorties
  (`seuil = (nbTotal - 2) / 2`) **ou** que la pioche est vide.

### `CheminCouleur` — le réseau d'un joueur pour une couleur
Démarre sur la station de la base. Maintient 3 listes recalculées à chaque ajout :
- `relies` : les stations du réseau.
- `liens` : les arêtes internes (les deux bouts sont reliés).
- `aretesDisponibles` : les arêtes de **frontière** (un seul bout relié) → les coups possibles.

Méthodes clés :
- `ajouter(s)` : ajoute une station si elle est adjacente et pas déjà présente.
- `estAdjacent(s)` : `s` touche-t-elle au moins une station du réseau ?
- `recalculer()` : reconstruit `liens` et `aretesDisponibles`.
- `getZonesConquises()` : les zones touchées par au moins une station du réseau.
- `calculerScore()` : **nb de zones conquises × (max de stations du réseau dans une même zone)**.

### `Joueur`
- `initialiserChemin(couleur, base, plateau)` : crée un `CheminCouleur` par couleur.
- `getChemin(couleur)` : le réseau pour cette couleur.
- `getScoreTotal()` : somme des scores de tous ses réseaux.

### `Jeu` — l'orchestrateur (PUBLIC)
- Constructeur : détermine l'ordre des manches (les couleurs qui ont une base) et initialise
  un réseau par joueur et par couleur.
- `demarrer()` / `mancheSuivante()` : enchaîne les manches (`etat` passe à `TERMINE` à la fin).
- `piocher()` : délègue à la manche en cours.
- `peutConnecter(joueur, s)` : la station est-elle reliable (en cours, voisine, pas déjà prise) ?
- `getSommetsJouables(joueur, carte)` : **la règle du tour** — les bouts libres des arêtes de
  frontière, filtrés par le symbole de la carte (joker = tous). *C'est ici que vit la règle de
  carte, pas dans le contrôleur.*
- `connecter(joueur, s)` : ajoute la station au réseau si le coup est légal.
- `supprimerSommet(s)` : retire une station du plateau ET des réseaux, puis recalcule.
- `getClassement()` : joueurs triés par score décroissant.

---

## 4. Le contrôleur (`controleur/Controleur.java`)

Façade entre l'IHM et le métier. **Aucune logique** : que des appels. Deux modes : `EDITION` / `JEU`.

### Édition
| Méthode | Fait quoi | Appelle |
|---|---|---|
| `creerPlateau(l, c, nom)` | nouveau plateau courant | `gestionnaire.creer` |
| `ajouterSommet(l, c, sym)` | pose une station + régénère les arêtes | `plateau.ajouterSommet` + `genererAretes` |
| `supprimerSommet(s)` | retire une station + régénère | `plateau.supprimerSommet` |
| `creerZone()` | nouveau quartier | `plateau.ajouterZone` |
| `ajouterCaseAZone(z, l, c)` | ajoute une case au quartier | `zone.ajouterCase` |
| `poserBase(couleur, s)` | pose un départ | `plateau.ajouterBase` |
| `dupliquerPlateau(nom)` | copie du plateau courant | `gestionnaire.dupliquer` |
| `sauvegarderPlateau(chemin)` | sauvegarde fichier | `gestionnaire.sauvegarder` |
| `chargerPlateau(chemin)` | charge fichier | `gestionnaire.charger` |

### Jeu
| Méthode | Fait quoi | Appelle |
|---|---|---|
| `demarrerPartie(noms)` | crée les joueurs + lance | `new Jeu(...)` + `jeu.demarrer` |
| `piocher()` | tire la carte du tour | `jeu.piocher` |
| `getSommetsJouables()` | coups possibles du joueur courant | `jeu.getSommetsJouables` |
| `connecter(s)` | joue une station (si légal) | `jeu.getSommetsJouables` + `jeu.connecter` |
| `joueurSuivant()` | joueur suivant (même tirage) | — (curseur local) |
| `tourTermine()` | la manche est-elle finie ? | `jeu.tourActuelTermine` |
| `mancheSuivante()` | manche suivante | `jeu.mancheSuivante` |
| `getClassement()` | classement final | `jeu.getClassement` |

Le contrôleur garde juste l'**état du tour** (`joueurActuel`, `carteActuelle`, `indiceJoueur`)
parce que c'est de la navigation d'IHM, pas une règle de jeu.

---

## 5. Conventions du code

- **Pas de `HashMap`/`HashSet`** côté pédagogie métier sauf besoin réel ; on privilégie `List`/`ArrayList`.
- Comparaisons d'objets par **identité (`==`)**, pas d'`equals`/`hashCode`.
- **Une classe publique de premier niveau par fichier** (nom du fichier = nom de la classe).
- Après toute modif des stations, **rappeler `genererAretes()`** (sinon les voisins sont faux).
- Les **arêtes ne se sauvegardent pas** : elles se recalculent.
