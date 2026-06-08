# Wiki du métier V4

Explication de **chaque classe et méthode** du métier : ce qu'elle fait, comment, avec quoi,
pourquoi, et le rôle de chaque morceau de code. À lire pour l'oral.

Le métier = **4 classes** : `Case`, `Configuration`, `Plateau`, `Sauvegarde`.
Une seule est vraiment importante (`Plateau`), les autres l'aident.

---

## `Case` — une case du plateau
Au lieu de 3 tableaux parallèles, **chaque case est un objet** qui retient ses 3 infos.
```java
private int arrondissement;  // 0 = aucun
private int station;         // 0 = aucune
private int depart;          // 0 = aucun
```
- des **getters/setters** pour chaque info (attributs `private` → encapsulation).
- `estVide()` : vrai si la case n'a pas d'arrondissement (sert à vérifier que le plateau est complet).

---

## `Configuration` — la config de la partie
```java
private int nbJoueurs;
private int nbStations;
```
- `setConfig / getNbJoueurs / getNbStations` : lire/écrire la config.
- **`verifier(largeur, hauteur, nbJoueurs, nbStations, nbArrondissements)`** *(static)* : vérifie les
  valeurs saisies à l'écran et renvoie **un message d'erreur, ou `null` si tout est correct**.
  - `static` car on l'utilise **avant** d'avoir un objet (on vérifie d'abord, on crée ensuite).
  - les `MAX_*` sont des **constantes** (`static final`) : 4 joueurs, 6 stations, 20 arrondissements.
  - **Pourquoi dans le métier** : la règle « max 4 joueurs » est une règle du jeu, pas de l'affichage.

---

## `Plateau` — le cœur (la grille + les règles)

### Attributs
```java
private Case[]        cases;    // toutes les cases (case i = cases[i])
private int           largeur, hauteur;
private Configuration config;   // la config de la partie
private boolean[][]   aretes;   // matrice d'adjacence : aretes[i][j] = lien entre i et j
```

### Constructeur
```java
this.cases = new Case[largeur * hauteur];
for (...) this.cases[i] = new Case();   // chaque case est un objet vide
this.aretes = new boolean[taille][taille];
```
- **Pourquoi** : une case par cellule de la grille ; un tableau 2D pour les liens.

### Numéro de case ↔ coordonnées
```java
getLigne(n)   = n / largeur     // division entière -> la ligne
getColonne(n) = n % largeur     // modulo -> la colonne
getNumCase(l, c) = l * largeur + c
```
La grille est rangée **ligne par ligne** ; chaque case a un numéro unique de 0 à n-1.

### Accès à une case (délégation à `Case`)
```java
getArrondissement(n) / affecterArrondissement(n, arr)
getStation(n)        / affecterStation(n, station)
getDepart(n)         / placerDepart(n, joueur)
```
- chaque méthode passe par le **garde-fou** `caseValide(n)` (n compris entre 0 et taille-1) pour ne jamais planter.
- **`affecterStation`** appelle ensuite **`genererAretes()`** : les arêtes dépendent des stations,
  donc le métier les **recalcule tout seul** → l'IHM n'a rien à gérer.
- **`placerDepart(n, joueur)`** applique la règle **« un seul départ par joueur »** : il efface
  d'abord l'ancien départ de ce joueur, puis pose le nouveau (`joueur == 0` = effacer).

### Validations (appelées par l'IHM avant de sauvegarder)
- **`toutesCasesRemplies()`** : vrai si **chaque** case a un arrondissement (parcourt les cases, `estVide()`).
- **`compterDepart(joueur)`** : combien de cases ont le départ de ce joueur.
- **`premierJoueurSansDepartUnique()`** : renvoie le **n° du 1er joueur** qui n'a pas exactement 1 départ
  (ou `0` si tout va bien). C'est la validation « 1 base par joueur ».
- **`recalculerConfig()`** : si on charge un vieux fichier sans config, on déduit nbStations/nbJoueurs
  du plus grand numéro trouvé dans les cases.

### Le réseau d'arêtes
- **`aArete(i, j)`** : y a-t-il un lien entre les cases i et j ? (lit la matrice, avec garde-fou).
- **`genererAretes()`** *(le plus important)* : relie chaque station à la **station la plus proche**
  dans 4 demi-directions (Est, Sud-Est, Sud, Sud-Ouest).
  - on **remet la matrice à zéro** (`new boolean[taille][taille]`).
  - `dLigne`/`dColonne` = les décalages pour avancer dans chaque direction.
  - pour chaque case qui a une station, dans chaque direction, le **`while`** avance case par case
    **jusqu'à** trouver une station (et on s'arrête : `break`) ou sortir de la grille.
  - quand on trouve la station `j`, on met `aretes[i][j] = aretes[j][i] = true` (lien dans les deux sens).
  - **pourquoi 4 demi-directions seulement** : pour ne créer chaque lien **qu'une fois** (sinon A→B et
    B→A créeraient le même lien deux fois).

---

## `Sauvegarde` — lire / écrire un plateau (séparation des responsabilités)
Toutes les méthodes sont `static` (on appelle `Sauvegarde.charger(...)` sans créer d'objet).

### `enregistrer(plateau, nom)`
- crée le dossier `sauvegarde/` si besoin, ouvre un `PrintWriter`.
- écrit la 1re ligne `largeur;hauteur;nbJoueurs;nbStations`, puis **une ligne par case** `i;arr;station;depart`.
- **n'écrit pas les arêtes** : elles se recalculent au chargement (données dérivées).

### `charger(fichier)`
- ouvre un `Scanner`, lit l'entête (dimensions + config), **recrée un `Plateau`**.
- pour chaque ligne suivante : `split(";")`, `Integer.parseInt`, et **remplit la case** directement.
- à la fin : `recalculerConfig()` (au cas où) puis **`genererAretes()`** (on reconstruit le réseau).
- renvoie le `Plateau` (ou `null` si erreur, géré par `try/catch`).

### `listerNoms()`
- liste les fichiers `.txt` du dossier `sauvegarde/` → un tableau de noms (pour le menu de chargement).

---

## Le contrôleur (rappel)
Il ne fait que **déléguer** : `getArrondissement` → `plateau.getArrondissement`, etc.
Seules « décisions » : il tient le **plateau courant** et, au chargement, remplace ce plateau par celui
renvoyé par `Sauvegarde.charger`.

## Phrase à ressortir à l'oral
> « La logique est dans le métier : `Plateau` connaît les règles (placement, validations, génération des
> arêtes), `Sauvegarde` s'occupe du fichier, `Configuration` des règles de config. Le contrôleur ne fait
> qu'appeler, et l'IHM ne fait qu'afficher et transmettre les clics. »
