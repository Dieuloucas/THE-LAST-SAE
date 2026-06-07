# Wiki du métier — `plateau.metier.Plateau`

Ce document explique **chaque méthode** de la couche métier : ce qu'elle fait, comment,
avec quoi, pourquoi on l'utilise, et le rôle de **chaque morceau de code**.
But : pouvoir tout expliquer à l'oral.

> Rappel rôle des couches : le **métier** contient les données + les règles, il ne connaît
> ni l'écran ni le contrôleur. Le **contrôleur** ne fait qu'**appeler** le métier. L'**IHM**
> n'appelle que le contrôleur.

---

## 1. Les attributs (les données du plateau)

```java
private int[] tabCases;     // arrondissement de chaque case (0 = aucun, 1..20)
private int[] tabMetros;    // type de metro de chaque case (0 = aucun, 1..6)
private int[] tabDeparts;   // depart de joueur de chaque case (0 = aucun, 1..nbJoueurs)
private int   largeur;
private int   hauteur;
private int   nbJoueurs;
private int   nbMetro;
```

- **Pourquoi 3 tableaux d'`int` ?** Le plateau est une grille. Chaque **case** porte 3 informations
  (son arrondissement, son métro, son départ). On utilise **3 tableaux parallèles** : la case `i`
  a son arrondissement dans `tabCases[i]`, son métro dans `tabMetros[i]`, son départ dans `tabDeparts[i]`.
- **Pourquoi des `int` et pas d'objets ?** C'est volontairement simple : un nombre suffit à coder
  chaque information (0 = rien). Facile à stocker dans un fichier et à expliquer.
- **`largeur` / `hauteur`** : les dimensions de la grille. **`nbJoueurs` / `nbMetro`** : la config de la partie.

**Le numéro de case.** Une case est repérée par un **seul** nombre `numCase` (de 0 à n-1),
au lieu de (ligne, colonne). La grille est rangée **ligne par ligne** :
```
numCase = ligne * largeur + colonne
```

---

## 2. Le constructeur

```java
public Plateau(int largeur, int hauteur)
{
    this.largeur    = largeur;
    this.hauteur    = hauteur;
    this.tabCases   = new int[largeur * hauteur];
    this.tabMetros  = new int[largeur * hauteur];
    this.tabDeparts = new int[largeur * hauteur];
}
```
- **Ce qu'il fait** : crée un plateau vide aux dimensions demandées.
- **Pourquoi `new int[largeur * hauteur]`** : il faut **une case par cellule** de la grille,
  soit `largeur × hauteur` cases. En Java, un `new int[n]` est **rempli de 0** automatiquement →
  toutes les cases démarrent à « aucun arrondissement / aucun métro / aucun départ ». Pratique.

---

## 3. Les accès simples

```java
public int getLargeur() { return this.largeur; }
public int getHauteur() { return this.hauteur; }
public int getNbCases() { return this.tabCases.length; }
```
- **Ce qu'ils font** : renvoient les dimensions et le nombre total de cases.
- **Pourquoi `tabCases.length`** : c'est exactement `largeur × hauteur`, mais on le lit directement
  sur le tableau → pas de risque d'erreur de calcul.
- **Pourquoi des getters** : les attributs sont `private` (encapsulation) ; l'extérieur lit via ces méthodes.

---

## 4. La configuration de la partie

```java
public void setConfig(int nbJoueurs, int nbMetro) { this.nbJoueurs = nbJoueurs; this.nbMetro = nbMetro; }
public int  getNbJoueurs() { return this.nbJoueurs; }
public int  getNbMetro()   { return this.nbMetro; }
```
- **Ce que ça fait** : enregistre / relit le nombre de joueurs et de types de métro.
- **Pourquoi dans le métier** : pour que cette config soit **sauvegardée avec le plateau**
  (voir `enregistrerPlateau`). Avant, elle était dans le contrôleur et se perdait au rechargement.

---

## 5. Numéro de case ↔ coordonnées

```java
public int getLigne(int numCase)              { return numCase / this.largeur; }
public int getColonne(int numCase)            { return numCase % this.largeur; }
public int getNumCase(int ligne, int colonne) { return ligne * this.largeur + colonne; }
```
- **À quoi ça sert** : passer du numéro unique `numCase` à (ligne, colonne) et inversement.
- **Pourquoi `/ largeur` et `% largeur`** :
  - la **division entière** `numCase / largeur` donne le **numéro de ligne** (combien de lignes complètes avant) ;
  - le **modulo** `numCase % largeur` donne la **colonne** (le reste dans la ligne).
  - Exemple grille de largeur 7 : case 10 → ligne `10/7 = 1`, colonne `10%7 = 3`.
- **`getNumCase`** fait l'inverse : `ligne * largeur + colonne`.

---

## 6. Lire / écrire une case (arrondissement, métro, départ)

Les 6 méthodes suivent **le même schéma**. Exemple avec l'arrondissement :
```java
public int getArrondissement(int numCase)
{
    if (numCase >= 0 && numCase < this.tabCases.length)   // garde-fou
        return this.tabCases[numCase];
    return 0;                                             // hors grille -> "aucun"
}

public void affecterArrondissement(int numCase, int arrondissement)
{
    if (numCase >= 0 && numCase < this.tabCases.length)
        this.tabCases[numCase] = arrondissement;
}
```
(et pareil pour `getMetro`/`affecterMetro` sur `tabMetros`, `getDepart`/`affecterDepart` sur `tabDeparts`.)

- **Ce que ça fait** : lit ou modifie la valeur d'**une** case.
- **Pourquoi le `if (numCase >= 0 && numCase < length)`** : c'est un **garde-fou**. Si on demande
  une case qui n'existe pas (index négatif ou trop grand), accéder au tableau planterait
  (`ArrayIndexOutOfBoundsException`). On vérifie d'abord → en lecture on renvoie `0` (aucun),
  en écriture on ne fait rien. Le programme ne plante jamais.
- **Avec quoi** : un simple tableau et un index.

---

## 7. Compter / lister les cases d'un arrondissement

```java
public int compterArrondissement(int arrondissement)
{
    int n = 0;
    for (int c : this.tabCases) if (c == arrondissement) n++;
    return n;
}
```
- **Ce que ça fait** : compte combien de cases appartiennent à un arrondissement donné.
- **Comment** : un `for` qui parcourt **toutes** les cases ; à chaque case égale à l'arrondissement
  cherché, on incrémente le compteur `n`. C'est un **comptage classique**.

```java
public int[] getCasesArrondissement(int arrondissement)
{
    int[] res = new int[compterArrondissement(arrondissement)];   // taille = nb de cases trouvees
    int k = 0;
    for (int i = 0; i < this.tabCases.length; i++)
        if (this.tabCases[i] == arrondissement) res[k++] = i;
    return res;
}
```
- **Ce que ça fait** : renvoie la **liste des numéros de cases** d'un arrondissement.
- **Pourquoi appeler `compterArrondissement` d'abord** : pour connaître la **taille exacte**
  du tableau `res` à créer (un tableau a une taille fixe en Java).
- **Le `res[k++] = i`** : on range le numéro `i` dans `res`, puis `k` avance d'une case (`k++`).

---

## 8. Les cases voisines

```java
public int[] getVoisins(int numCase)
{
    int ligne = getLigne(numCase), colonne = getColonne(numCase);
    int[] tmp = new int[8];     // au maximum 8 voisins
    int n = 0;
    for (int dl = -1; dl <= 1; dl++)
        for (int dc = -1; dc <= 1; dc++)
        {
            if (dl == 0 && dc == 0) continue;            // (0,0) = la case elle-meme
            int l = ligne + dl, c = colonne + dc;
            if (l >= 0 && l < this.hauteur && c >= 0 && c < this.largeur)  // dans la grille ?
                tmp[n++] = getNumCase(l, c);
        }
    int[] res = new int[n];     // on recopie a la bonne taille
    for (int i = 0; i < n; i++) res[i] = tmp[i];
    return res;
}
```
- **Ce que ça fait** : renvoie les cases **collées** à une case (les 8 directions : N, S, E, O et diagonales).
- **Comment / pourquoi chaque morceau** :
  - on convertit `numCase` en (ligne, colonne) pour pouvoir se déplacer.
  - les deux boucles `dl` et `dc` (de -1 à +1) testent **toutes** les combinaisons de décalage autour
    de la case (les 9 cases du carré 3×3).
  - `if (dl == 0 && dc == 0) continue;` **saute** le centre (la case elle-même n'est pas sa voisine).
  - `if (l >= 0 && l < hauteur && c >= 0 && c < largeur)` **vérifie qu'on reste dans la grille**
    (sur un bord, certaines directions sortent du plateau).
  - on stocke d'abord dans `tmp` (taille 8 max), puis on **recopie dans `res` à la taille réelle `n`**
    (car sur un bord il y a moins de 8 voisins, et on ne veut pas de zéros parasites).

---

## 9. Validité du plateau

```java
public boolean estValide()
{
    int nbMetros = 0, nbDeparts = 0;
    for (int m : this.tabMetros)  if (m != 0) nbMetros++;
    for (int d : this.tabDeparts) if (d != 0) nbDeparts++;
    return nbMetros > 0 && nbDeparts >= this.nbJoueurs;
}
```
- **Ce que ça fait** : dit si le plateau est **jouable**.
- **La règle** : il faut au moins **un métro posé** et **au moins autant de départs que de joueurs**.
- **Comment** : deux comptages (cases non nulles dans `tabMetros` et `tabDeparts`), puis un test booléen.

---

## 10. Enregistrer le plateau dans un fichier

```java
public boolean enregistrerPlateau(String nomFichier)
{
    try
    {
        File dossier = new File("sauvegarde");
        if (!dossier.exists()) dossier.mkdirs();        // cree le dossier si besoin

        File file = new File(dossier, nomFichier.endsWith(".txt") ? nomFichier : nomFichier + ".txt");
        PrintWriter pw = new PrintWriter(new FileOutputStream(file));

        pw.println(this.largeur + ";" + this.hauteur + ";" + this.nbJoueurs + ";" + this.nbMetro);
        for (int i = 0; i < this.tabCases.length; i++)
            pw.println(i + ";" + this.tabCases[i] + ";" + this.tabMetros[i] + ";" + this.tabDeparts[i]);

        pw.close();
    }
    catch (Exception e) { System.out.println("Erreur d'enregistrement : " + e.getMessage()); return false; }

    System.out.println("Plateau enregistre.");
    return true;
}
```
- **Ce que ça fait** : écrit tout le plateau dans un fichier texte du dossier `sauvegarde/`.
- **Le format du fichier** :
  ```
  largeur;hauteur;nbJoueurs;nbMetro      <- 1re ligne (la config)
  numCase;arrondissement;metro;depart    <- 1 ligne par case
  ...
  ```
- **Pourquoi chaque morceau** :
  - `dossier.mkdirs()` : crée le dossier `sauvegarde/` s'il n'existe pas encore (sinon l'écriture échoue).
  - l'opérateur `? :` (ternaire) ajoute `.txt` seulement si le nom ne l'a pas déjà.
  - `PrintWriter` : l'outil pour **écrire du texte** ligne par ligne (`println`).
  - on écrit d'abord la config, puis une boucle qui écrit **chaque case** avec ses 3 valeurs, séparées par `;`.
  - `pw.close()` : **referme** le fichier (sinon le texte peut ne pas être réellement écrit sur le disque).
  - `try/catch` : si un problème survient (disque plein, droit refusé…), on **n'arrête pas le programme** ;
    on affiche un message et on renvoie `false`.
- **Pourquoi `boolean`** : l'appelant sait si la sauvegarde a réussi (`true`) ou échoué (`false`).

---

## 11. Charger un plateau depuis un fichier

```java
public boolean chargerPlateau(File file)
{
    try
    {
        Scanner scanner = new Scanner(file);
        if (!scanner.hasNextLine()) { scanner.close(); return false; }   // fichier vide

        String[] dim = scanner.nextLine().split(";");      // 1re ligne -> config
        this.largeur = Integer.parseInt(dim[0]);
        this.hauteur = Integer.parseInt(dim[1]);
        if (dim.length >= 4)                                // config presente
        {
            this.nbJoueurs = Integer.parseInt(dim[2]);
            this.nbMetro   = Integer.parseInt(dim[3]);
        }

        int size = this.largeur * this.hauteur;
        this.tabCases   = new int[size];                   // on recree les tableaux a la bonne taille
        this.tabMetros  = new int[size];
        this.tabDeparts = new int[size];

        while (scanner.hasNextLine())                      // les lignes suivantes -> les cases
        {
            String line = scanner.nextLine();
            if (line.trim().isEmpty()) continue;           // on ignore les lignes vides
            String[] parts = line.split(";");
            int index = Integer.parseInt(parts[0]);
            if (index >= 0 && index < size)
            {
                this.tabCases[index] = Integer.parseInt(parts[1]);
                if (parts.length >= 3) this.tabMetros[index]  = Integer.parseInt(parts[2]);
                if (parts.length >= 4) this.tabDeparts[index] = Integer.parseInt(parts[3]);
            }
        }
        scanner.close();
        return true;
    }
    catch (Exception e) { System.out.println("Erreur de lecture : " + e.getMessage()); return false; }
}
```
- **Ce que ça fait** : relit un fichier sauvegardé et **reconstruit** le plateau en mémoire.
- **Pourquoi chaque morceau** :
  - `Scanner` : l'outil pour **lire un fichier texte** ligne par ligne (`nextLine`, `hasNextLine`).
  - `split(";")` : **découpe** une ligne en morceaux selon le `;` → un tableau de `String`.
  - `Integer.parseInt(...)` : convertit un **texte** (« 7 ») en **nombre** (7), car un fichier ne contient que du texte.
  - on lit d'abord la **config** (1re ligne), puis on **recrée les tableaux** à la bonne taille
    (`new int[size]`), puis on **remplit** case par case dans la boucle `while`.
  - `if (parts.length >= 3 / >= 4)` : **compatibilité** — un ancien fichier sans métro/départ se charge quand même.
  - `if (index >= 0 && index < size)` : garde-fou (on ignore une ligne avec un numéro de case invalide).
  - `try/catch` : si le fichier est introuvable/mal formé, on renvoie `false` au lieu de planter.

---

## 12. Le contrôleur (le pont)

Le contrôleur **ne contient aucune logique** : chaque méthode **appelle** simplement le métier.
Exemples :
```java
public int  getArrondissement(int numCase)               { return this.metier.getArrondissement(numCase); }
public void affecterArrondissement(int numCase, int arr) { this.metier.affecterArrondissement(numCase, arr); }
public boolean enregistrerPlateau(String nom)            { return this.metier.enregistrerPlateau(nom); }
public boolean chargerPlateau(java.io.File file)         { return this.metier.chargerPlateau(file); }
```
Seule exception un peu plus longue : `initialiserPlateau` recrée un plateau **en gardant la config** déjà saisie :
```java
public void initialiserPlateau(int largeur, int hauteur)
{
    int j = this.metier.getNbJoueurs();   // on memorise la config
    int m = this.metier.getNbMetro();
    this.metier = new Plateau(largeur, hauteur);   // nouveau plateau a la bonne taille
    this.metier.setConfig(j, m);                   // on lui redonne la config
}
```
- **Pourquoi** : quand l'utilisateur valide la configuration, on crée le vrai plateau à la bonne taille,
  mais on ne veut pas perdre `nbJoueurs`/`nbMetro` → on les remet juste après.

---

## Notions Java à savoir réciter (questions d'oral fréquentes)
- **Tableau** `int[] t = new int[n]` : `n` cases, indexées de `0` à `n-1`, initialisées à `0`.
- **`tabCases.length`** : la taille d'un tableau.
- **Division entière `/` et modulo `%`** : pour passer de `numCase` à (ligne, colonne).
- **`String.split(";")`** : découpe une chaîne → tableau de chaînes.
- **`Integer.parseInt("7")`** : texte → entier.
- **`Scanner`** : lire un fichier ; **`PrintWriter`** : écrire un fichier.
- **`try { } catch (Exception e) { }`** : gérer une erreur sans planter.
- **Encapsulation** : attributs `private`, accès par getters/setters.
