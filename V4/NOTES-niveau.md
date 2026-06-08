# Notes — méthodes / notions qui peuvent être « hors niveau »

Liste honnête des morceaux de V4 qui **dépassent peut-être le programme de 1re année**, ou
qu'on pourrait te demander d'expliquer à l'oral. Pour chacun : où c'est, pourquoi c'est là,
et une **alternative plus simple** si tu préfères le remplacer.

> Le **métier** (Case, Configuration, Plateau, Sauvegarde) est volontairement **simple et explicable**.
> Le « hors niveau » est surtout dans l'**IHM** (le dessin), ce qui est normal : dessiner, c'est de l'affichage.

---

## 🟠 Dans l'IHM (affichage) — surtout du dessin

### 1. `paintComponent(Graphics g)` et `paint(Graphics g)` — `PanelJeu` (CasePanel, GrillePanel)
- **Ce que c'est** : on **redéfinit** la méthode que Swing appelle pour dessiner un composant, pour dessiner nous-mêmes (couleurs, images, lignes).
- **Pourquoi** : afficher la couleur d'arrondissement, l'image de station, et **les arêtes** (lignes).
- **Plus simple si besoin** : remplacer une case dessinée par un `JPanel` coloré + un `JLabel` ("S3", "D2") ; mais on perd les images et les lignes d'arêtes.

### 2. `Graphics2D`, `BasicStroke`, `RenderingHints`, `drawLine` — `GrillePanel.paint`
- **Ce que c'est** : la version « avancée » du dessin (épaisseur de trait, anti-aliasing) pour tracer les **arêtes** entre stations.
- **Plus simple** : ne pas dessiner les arêtes du tout (juste les cases) — la logique d'arêtes reste dans le métier, on ne l'affiche simplement pas.

### 3. `ImageIcon` / `Image` / `drawImage` — `Images`, `PanelJeu`, `PanelAccueil`
- **Ce que c'est** : charger un fichier `.png` et le dessiner.
- **Plus simple** : afficher du texte à la place des images (ex. "S3").

### 4. Classes internes `private class CasePanel` / `GrillePanel` — `PanelJeu`
- **Ce que c'est** : des classes **définies à l'intérieur** d'une autre, qui peuvent utiliser ses attributs (`ctrl`).
- **Pourquoi** : pratique pour des petits composants liés au panel.
- **Plus simple** : en faire des classes séparées (fichiers à part) en leur passant `ctrl` dans le constructeur.

### 5. Layouts `BoxLayout` / `GridBagLayout` — `PanelJeu`, `PanelAccueil`
- **Ce que c'est** : des gestionnaires de placement plus fins que `GridLayout`/`BorderLayout`.
- **Plus simple** : tout faire en `GridLayout` / `BorderLayout` / `FlowLayout` (vus en cours), c'est moins joli mais suffisant.

---

## 🟡 Dans le métier — 2 points à bien préparer

### 6. La matrice d'adjacence `boolean[][] aretes` — `Plateau`
- **Ce que c'est** : un **tableau à 2 dimensions**. `aretes[i][j] = true` veut dire « la case i et la case j sont reliées ».
- **Pourquoi** : c'est la façon classique de représenter un **graphe** (qui est relié à qui).
- **À savoir dire** : "c'est un tableau de booléens à double entrée ; la ligne i, colonne j indique s'il y a un lien."

### 7. L'algorithme `genererAretes()` — `Plateau` (le plus « malin »)
- **Ce qu'il fait** : pour chaque station, regarde dans **4 demi-directions** (Est, Sud-Est, Sud, Sud-Ouest) et relie à la **station la plus proche** dans cette direction.
- **Les morceaux à expliquer** :
  - les tableaux `dLigne`/`dColonne` = les **décalages** pour avancer dans une direction ;
  - le `while` qui **avance case par case** jusqu'à trouver une station (ou sortir de la grille) ;
  - **pourquoi seulement 4 demi-directions** : pour ne créer chaque lien **qu'une seule fois** (sinon A–B et B–A seraient comptés deux fois).
- **Plus simple si besoin** : relier seulement les **cases voisines immédiates** (les 8 cases collées) au lieu de chercher la plus proche au loin → un simple test des 8 voisins, sans `while`.

---

## 🟢 Notions « limites » mais OK à expliquer
- **`String.split(";")`** : découpe une ligne en morceaux → `Sauvegarde`.
- **`Integer.parseInt("7")`** : texte → entier → `Sauvegarde`, `PanelConfiguration`.
- **`Scanner` / `PrintWriter`** : lire / écrire un fichier → `Sauvegarde`.
- **`try { } catch (Exception e) { }`** : gérer une erreur sans planter → `Sauvegarde`.
- **`static`** : `Configuration.verifier(...)` et `Sauvegarde.*` sont `static` (on les appelle sans créer d'objet) ; `MAX_JOUEURS` etc. sont des **constantes** `static final`.
- **`List<String>` + `toArray(new String[0])`** : `Sauvegarde.listerNoms` (liste → tableau). Si jamais on te le demande : "je remplis une liste puis je la convertis en tableau".
- **`JComboBox` + `getSelectedIndex()`** : l'indice choisi dans une liste déroulante (0 = "Aucun") → `PanelJeu`.

---

## Conseil
Si un point te stresse pour l'oral, dis-le-moi : je te le **remplace par l'alternative simple**
indiquée ci-dessus. Mieux vaut du code que tu sais défendre à 100 %.
