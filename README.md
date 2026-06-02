# Règles du Jeu : Réseau Express Parisien (SAE)

Ce document formalise les règles du jeu de plateau et d'édition de graphes appliquées au thème des transports en commun de Paris.

## Correspondance du Thème

| Élément du Graphe | Équivalent Thématique (Paris) | Description |
| --- | --- | --- |
| **Zone** | **Arrondissement de Paris** | Régions administratives découpant le plateau. |
| **Sommet** | **Hub d'Interconnexion / Station** | Points d'arrêt du réseau (comportant une icône de transport précise). |
| **Arête / Lien** | **Tronçon de Voie** | Rails reliant deux stations directes. |
| **Couleur** | **Ligne de Transport** | 4 lignes distinctes : Rouge, Vert, Bleu, Marron.
| **Base** | **Terminus / Grand Hub** | Station de départ spécifique à une couleur de ligne.


---

## 1. Mode Éditeur : Création et Modification du Plateau

L'application permet de concevoir et de modifier librement l'architecture du réseau parisien avant de lancer une partie (Solo ou Réseau).

* **Gestion des plateaux :** Possibilité de créer un nouveau plan, de modifier un plan existant ou de dupliquer un réseau enregistré.


* **Dimensions de la grille :** Le plateau est un quadrillage de 7x7 cases, mais n'est pas forcément carré (il peut être rectangulaire). Chaque case mesure 50 pixels afin de pouvoir accueillir des icônes ou des images en interface graphique.


* **Arrière-plan :** Intégration d'une image de fond (plan ou carte de Paris).


* **Configuration des Arrondissements (Zones) :** Définition des différentes zones du plateau. Plusieurs configurations de découpes peuvent être sauvegardées et conservées.

* **Placement des Stations (Sommets) :** Les stations sont placées manuellement sur la grille et possèdent l'un des 4 symboles de transport définis:


1. 🚇 **Bouche de Métro** 
2. 🚆 **Gare RER**
3. 🚉 **Gare Transilien** 
4. 🚋 **Arrêt de Tramway** 
* **Génération des Tronçons (Arêtes) :** Les liens entre les sommets sont générés de façon automatique selon 8 directions possibles (exemple vu aux échecs dans le cours héritages).





> 
> **Règle de suppression :** Si une station est supprimée par l'utilisateur, tous ses liens et tronçons adjacents sont automatiquement supprimés.
> 
> 

* **Positionnement des Terminus (Bases) :** Chaque ligne de couleur possède sa propre station de départ (Base). Il est possible de placer plusieurs bases de couleurs différentes au sein d'un même arrondissement (socles de couleurs considérés comme des départs). Les joueurs déploient leurs lignes en glisser-déposer (*Drag and drop*) à partir de ces sommets.



---

## 2. Matériel : Le Système de Tickets (Cartes)

Le jeu utilise un système de pioche pour dicter les constructions autorisées. Le nombre total de cartes dépend directement du nombre de symboles via la formule mathématique suivante :

$$\text{Nombre de cartes} = (\text{nombre de symboles} + 1) \times 2$$

Pour 4 symboles (transports), le paquet comprend 10 cartes réparties en deux catégories (cette distinction claire/sombre apporte une variation visuelle au jeu tout en équilibrant les tirages):

*  **5 Cartes Sombres :** 1 carte pour chaque transport (Métro, RER, Transilien, Tramway) + 1 carte JOKER (Vélib').


*  **5 Cartes Claires :** 1 carte pour chaque transport (Métro, RER, Transilien, Tramway) + 1 carte JOKER (Vélib').



Au début de chaque manche, l'ensemble des 10 cartes est mélangé pour constituer une pioche (configurée en mode face fermée ou ouverte, au choix).

---

## 3. Déroulement d'une Partie
 
Le jeu se joue en *n* manches, *n* étant le nombre de couleurs (donc 4 manches). L'interface affiche en permanence un indicateur visuel de la manche (et de la couleur) en cours.

**Fin de manche :** Une manche se termine exactement et automatiquement **après que 5 cartes ont été piochées** et jouées. Les 5 cartes restantes dans la pioche ne sont pas révélées, assurant ainsi une part d'aléatoire à chaque manche.

### Tour de jeu

1. À chaque tour, l'application tire une carte au hasard dans la pioche.


2. Le joueur doit obligatoirement étendre son réseau de la couleur de la manche en cours.
3. Il doit relier une nouvelle station possédant le symbole de transport indiqué sur la carte tirée. Si la carte est un 🚲 **Vélib' (JOKER)**, il peut se connecter à n'importe quel type de station.


4. **Contrainte de connexion (Règle des extrémités) :** Le nouveau tronçon doit impérativement être connecté soit directement au Terminus (Base) de la manche, soit à l'une des **extrémités (feuilles)** du réseau déjà construit (on part des extrémités du chemin).


* *Embranchements autorisés :* Il est possible de créer des embranchements, mais **uniquement** à partir de la Base ou d'une station qui ne possède actuellement qu'un seul lien (une extrémité).
* *Interdiction :* Il est formellement impossible de se greffer au milieu d'une ligne (c'est-à-dire à partir d'une station possédant déjà 2 liens validés ou plus).



---

## 4. Calcul des Points et Score

Le score d'une manche récompense l'expansion géographique de la ligne et sa forte implantation dans un arrondissement clé.

À la fin d'une manche de couleur, les points sont calculés grâce à la formule suivante :

$$\text{Points} = \text{Zones Conquises} \times \text{Max Stations}$$

* **Arrondissements conquis (Zones) :** Le nombre total de zones (arrondissements) conquises traversées par la ligne.


* **Max Stations :** La zone comportant le plus grand nombre de sommets reliés (la zone la plus conquise).



> **Exemples concrets de scores calculés en fin de manche :**
> * **Manche Ligne Verte :** 3 zones traversées $\times$ 3 stations maximum dans la zone centrale = 9 points.
> * **Manche Ligne Rouge :** 4 zones traversées $\times$ 3 stations maximum = 12 points.
> * **Manche Ligne Marron :** 3 zones traversées $\times$ 2 stations maximum = 6 points.
> 
> 

Le score final du joueur est la somme des points obtenus lors des différentes manches.

---

## 5. Mode Dual en Réseau (2 Joueurs)

Une version réseau permet à deux joueurs de s'affronter simultanément sur le même plateau de jeu en mode **"Multi-solo"**. Chacun développe son propre réseau de manière indépendante.

* **Indépendance des réseaux :** Les joueurs ne se bloquent pas. Ils peuvent utiliser les mêmes tronçons ou les mêmes stations pour leurs réseaux respectifs.
* **Équité du tirage :** Pour garantir une compétition juste, les joueurs partagent strictement le même tirage de cartes tout au long de la manche.

* **Asymétrie des départs :** À chaque manche, les joueurs ne partent pas avec les mêmes bases (leurs départs respectifs sont placés à des endroits distincts).

* **Conditions de victoire :**
1. Le vainqueur est le joueur ayant obtenu le plus grand score final cumulé.


2. En cas d'égalité : Le vainqueur est celui qui a obtenu le meilleur score sur une seule manche.


3. En cas de nouvelle égalité : Les joueurs se partagent la victoire.
