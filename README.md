# Règles du Jeu : Réseau Express Parisien (SAE)

Ce document formalise les règles du jeu de plateau et d'édition de graphes appliquées au thème des transports en commun de Paris, utilisables pour votre présentation.

## Correspondance du Thème

| Élément du Graphe | Équivalent Thématique (Paris) | Description |
| --- | --- | --- |
| **Zone** | **Arrondissement de Paris** | Régions administratives découpant le plateau. |
| **Sommet** | **Station de Métro / RER** | Points d'arrêt du réseau (comportant un symbole précis). |
| **Arête / Lien** | **Tronçon de Voie** | Rails reliant deux stations directes. |
| **Couleur** | **Ligne de Transport** | 4 lignes distinctes : Rouge, Vert, Bleu, Marron. |
| **Base** | **Terminus / Grand Hub** | Station de départ spécifique à une couleur de ligne. |

---

## 1. Mode Éditeur : Création et Modification du Plateau

L'application permet de concevoir et de modifier librement l'architecture du réseau parisien avant de lancer une partie (Solo ou Réseau).

* **Gestion des plateaux :** Possibilité de créer un nouveau plan, de modifier un plan existant ou de dupliquer un réseau enregistré.
* **Dimensions de la grille :** Le plateau est un quadrillage de 7x7 cases (ou toute autre dimension rectangulaire). Chaque case mesure 50 pixels afin de pouvoir accueillir des icônes ou images personnalisées en interface graphique.
* **Arrière-plan :** Intégration d'une image de fond (ex: plan ou carte de Paris).
* **Configuration des Arrondissements (Zones) :** Définition des zones du plateau. Plusieurs configurations de découpes peuvent être sauvegardées et conservées.
* **Placement des Stations (Sommets) :** Les stations sont placées manuellement sur la grille et possèdent l'un des 4 symboles définis (représentant les types de quartiers) :
(Zone Résidentielle)
(Hub Intermodal)
(Quartier d'Affaires)
Zone Touristique)


* **Génération des Tronçons (Arêtes) :** Les liens entre les sommets sont générés automatiquement selon 8 directions possibles (similaire aux mouvements du Roi aux échecs, étudiés dans le cours sur l'héritage).
> **Règle de suppression :** Si une station est supprimée par l'utilisateur, tous ses liens et tronçons adjacents sont automatiquement supprimés.


* **Positionnement des Terminus (Bases) :** Chaque ligne de couleur possède sa propre station de départ (Base). Il est possible de placer plusieurs bases de couleurs différentes au sein d'un même arrondissement (socles de départ). Les joueurs déploient leurs lignes en glisser-déposer (*Drag and Drop*) à partir de ces sommets.

---

## 2. Matériel : Le Système de Tickets (Cartes)

Le jeu utilise un système de pioche pour dicter les constructions autorisées. Le nombre total de cartes dépend directement du nombre de symboles via la formule suivante :

$$\text{Nombre de cartes} = (\text{nombre de symboles} + 1) \times 2$$

Pour 4 symboles, le paquet comprend 10 cartes réparties en deux catégories :

* **5 Cartes Sombres :** 1 carte pour chaque symbole (Cercle, Croix, Triangle, Carré) + 1 carte JOKER.
* **5 Cartes Claires :** 1 carte pour chaque symbole (Cercle, Croix, Triangle, Carré) + 1 carte JOKER.

Au début de chaque manche, l'ensemble des 10 cartes est mélangé pour constituer une pioche (configurée en mode face cachée ou face ouverte).

---

## 3. Déroulement d'une Partie

Une partie complète se joue en 4 manches, correspondant chacune à l'extension d'une ligne de couleur spécifique. L'interface affiche en permanence un indicateur visuel de la manche en cours.

### Tour de jeu

1. À chaque tour, l'application tire une carte au hasard dans la pioche.
2. Le joueur doit obligatoirement étendre son réseau de la couleur de la manche en cours.
3. Il doit relier une nouvelle station possédant le symbole indiqué sur la carte tirée. Si la carte est un JOKER, il peut se connecter à n'importe quel symbole.
4. **Contrainte de connexion :** Le nouveau tronçon doit impérativement être connecté soit directement au Terminus (Base) de la manche, soit à l'une des extrémités des chemins déjà validés de cette même couleur (on part des extrémités du chemin).

---

## 4. Calcul des Points et Score

Le score d'une manche récompense l'expansion géographique de la ligne et sa forte implantation dans un arrondissement clé.

À la fin d'une manche de couleur, les points sont calculés grâce à la formule mathématique suivante :

$$\text{Points} = \text{Zones Conquises} \times \text{Max Stations}$$

* **Arrondissements conquis (Zones) :** Nombre total d'arrondissements différents traversés par la ligne de la manche.
* **Max Stations :** Le plus grand nombre de stations reliées par cette ligne au sein d'un seul et même arrondissement (la zone la plus conquise).

> **Exemple concret de score (basé sur vos notes) :**
> * **Manche Ligne Verte :** 3 arrondissements traversés $\times$ 3 stations maximum dans l'arrondissement central = 9 points.
> * **Manche Ligne Rouge :** 4 arrondissements traversés $\times$ 3 stations maximum = 12 points.
> * **Manche Ligne Marron :** 3 arrondissements traversés $\times$ 2 stations maximum = 6 points.
> 
> 

Le score final du joueur est la somme des points obtenus lors des 4 manches.

---

## 5. Mode Dual en Réseau (2 Joueurs)

Une version réseau permet à deux joueurs de s'affronter simultanément sur le même plateau de jeu.

* **Équité du tirage :** Pour garantir une compétition juste, les deux joueurs partagent strictement le même tirage de cartes tout au long de la partie.
* **Asymétrie des départs :** À chaque manche, les joueurs ne débutent pas depuis les mêmes arrondissements (leurs bases de départ respectives sont placées à des endroits distincts du graphe).
* **Conditions de victoire :**
1. Le joueur obtenant le plus grand score total cumulé à la fin des manches remporte la partie.
2. **En cas d'égalité :** Le joueur ayant réalisé le meilleur score sur une seule et unique manche est déclaré vainqueur.
3. **En cas de nouvelle égalité :** Les joueurs partagent équitablement la victoire.
