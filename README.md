# Règles du Jeu : Les Cartographes du Métro (SAE)

Ce document formalise les règles du jeu de plateau et d'édition de graphes. Jouable de 1 à 4 joueurs , vous devez parcourir le réseau du métro et relier les quartiers emblématiques de la capitale avant vos adversaires.

## But du Jeu

L'objectif est d'obtenir le plus de points. Les joueurs développent leur réseau stratégiquement en :

1. Capturant une ligne entre 2 métros.


2. Capturant le plus d'arrondissements de Paris possible.


3. Réalisant le trajet continu le plus long.



## Correspondance du Thème

| Élément du Graphe | Équivalent Thématique (Paris) | Description |
| --- | --- | --- |
| **Zone** | **Arrondissement de Paris** | Régions administratives découpant le plateau. |
| **Sommet** | **Station de Métro** | Points d'arrêt du réseau (comportant une apparence visuelle précise). |
| **Arête / Lien** | **Tronçon de Voie** | Rails reliant deux stations directes. |
| **Couleur** | **Ligne de Transport** | 4 lignes distinctes : Verte, Rouge, Bleue, Noire.

 |
| **Base** | **Station de départ** | Station de départ spécifique à une couleur de ligne.

 |

---

## 1. Mode Éditeur : Création et Modification du Plateau

L'application permet de concevoir et de modifier librement l'architecture du réseau parisien avant de lancer une partie.

* **Gestion des plateaux :** Possibilité de créer un nouveau plan, de modifier un plan existant ou de dupliquer un réseau enregistré.
* 
**Dimensions de la grille :** Le plateau est un quadrillage de 7x7 cases, mais n'est pas forcément carré (il peut être rectangulaire). Chaque case mesure 50 pixels afin de pouvoir accueillir des icônes ou des images en interface graphique.


* **Arrière-plan :** Intégration d'une image de fond (plan ou carte de Paris).
* **Configuration des Arrondissements (Zones) :** Définition des différentes zones du plateau. Plusieurs configurations de découpes peuvent être sauvegardées et conservées.
* **Placement des Stations (Sommets) :** Les stations sont placées manuellement sur la grille et possèdent l'un des **6 types de métro** définis (ils se distinguent par leur apparence visuelle).
* **Génération des Tronçons (Arêtes) :** Les liens entre les sommets sont générés de façon automatique selon 8 directions possibles (exemple vu aux échecs dans le cours héritages).

> **Règle de suppression :** Si une station est supprimée par l'utilisateur, tous ses liens et tronçons adjacents sont automatiquement supprimés.

* 
**Positionnement des Stations de départ (Bases) :** Chaque ligne de couleur possède sa propre station de départ. Il est possible de placer plusieurs bases de couleurs différentes au sein d'un même arrondissement (socles de couleurs considérés comme des départs). Les joueurs déploient leurs lignes en glisser-déposer (*Drag and drop*) à partir de ces sommets.



---

## 2. Matériel : Le Système de Tickets (Cartes)

Le jeu utilise un système de pioche pour dicter les constructions autorisées. Le nombre total de cartes dépend directement du nombre de symboles via la formule mathématique suivante :

$$\text{Nombre de cartes} = (\text{nombre de types de métro} + 1) \times 2$$

Pour 6 types de métro, le paquet comprend **14 cartes** réparties en deux catégories (cette distinction claire/sombre apporte une variation visuelle au jeu tout en dictant le temps de la manche):

* 
**7 Cartes Sombres :** 1 carte pour chaque type de métro (1 à 6) + 1 carte JOKER (Père Noël).


* 
**7 Cartes Claires :** 1 carte pour chaque type de métro (1 à 6) + 1 carte JOKER (Père Noël).



Au début de chaque manche, l'ensemble des 14 cartes est mélangé pour constituer une pioche (configurée en mode face fermée ou ouverte, au choix).

---

## 3. Déroulement d'une Partie

La partie se déroule en un nombre de manches égal au nombre de lignes de métro défini au début de la partie (1 joueur = 1 ligne). Chaque joueur démarre sa manche avec son réseau relié à sa station de départ.

**Fin de manche :** Seules les cartes foncées comptent dans le déroulement du jeu. La manche s'arrête **immédiatement lorsque toutes les cartes foncées ont été tirées** de la pioche. Les cartes claires ajoutent de l'incertitude sur la durée de la manche.

### Tour de jeu

1. À chaque tour, une carte est tirée au hasard dans la pioche.


2. Le joueur doit obligatoirement étendre son réseau de la couleur de la manche en cours.
3. Il doit relier une nouvelle station correspondant au type de métro indiqué sur la carte tirée. Si la carte est un 🎅 **Père Noël (JOKER)**, il peut se connecter à n'importe quel type de métro.


4. 
**Contrainte de connexion (Règle des extrémités) :** Le nouveau tronçon doit impérativement être connecté soit directement à la station de départ de la manche, soit à l'une des **extrémités (feuilles)** du réseau déjà construit.


* *Embranchements autorisés :* Il est possible de créer des embranchements, mais **uniquement** à partir de la station de départ ou d'une station qui ne possède actuellement qu'un seul lien (une extrémité).
* *Interdiction :* Il est formellement impossible de se greffer au milieu d'une ligne (c'est-à-dire à partir d'une station possédant déjà 2 liens validés ou plus).



---

## 4. Calcul des Points et Score

Le score d'une manche récompense l'expansion géographique de la ligne et sa forte implantation dans un arrondissement clé.

À la fin d'une manche de couleur, les points sont calculés grâce à la formule suivante:

$$\text{Points} = \text{Zones Conquises} \times \text{Max Stations}$$

* 
**Arrondissements conquis (Zones) :** Le nombre total d'arrondissements de Paris traversés par la ligne.


* 
**Max Stations :** Le nombre de stations détenues dans l'arrondissement le plus représenté par le joueur.



> **Exemples concrets de scores calculés en fin de manche :**
> * **Manche Ligne Verte :** 3 arrondissements traversés $\times$ 3 stations maximum dans l'arrondissement central = 9 points.
> * **Manche Ligne Rouge :** 4 arrondissements traversés $\times$ 3 stations maximum = 12 points.
> * **Manche Ligne Noire :** 3 arrondissements traversés $\times$ 2 stations maximum = 6 points.
> 
> 

Le score final du joueur est la somme des points obtenus lors des différentes manches.

---

## 5. Mode Multijoueur (Jusqu'à 4 Joueurs)

Une version réseau permet à plusieurs joueurs (jusqu'à 4) de s'affronter simultanément sur le même plateau de jeu en mode **"Multi-solo"**. Chacun développe son réseau de manière indépendante.

* **Indépendance des réseaux :** Les joueurs ne se bloquent pas. Ils peuvent utiliser les mêmes tronçons ou les mêmes stations pour leurs réseaux respectifs.
* **Équité du tirage :** Pour garantir une compétition juste, les joueurs partagent strictement le même tirage de cartes tout au long de la manche.
* **Asymétrie des départs :** À chaque manche, les joueurs ne partent pas avec les mêmes stations de départ (leurs départs respectifs sont placés à des endroits distincts).
* **Conditions de victoire :**
1. Le vainqueur est le joueur ayant obtenu le plus grand total de points cumulés.


2. En cas d'égalité : Le vainqueur est celui qui a réalisé la meilleure performance sur une seule manche.


3. En cas de nouvelle égalité : Les joueurs se partagent la victoire.
