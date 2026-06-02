SAE FULL 
Présentation amphi 
Le but c’est de faire un petit jeu de plateau 
Il y a des sommet et des liens entres les sommets (aretes)
Jeu solo qui devient multi, 
On pourra 
Creer un nouveau plateau 
Modifie un ancien plateau
Créer un copie d’un existant 
A la création d’un plateau il faut définir 
La taille 7x7 pas forcement carré peut être rect 
Le nombre de couleur et leur valeurs 		4 rouge vert bleu marron (valeurs = couleurs)
Le nombre de symbole 					4 cercle croix triangle carré
La taille des cases (carrés du plateau) Comme dans IHM en attendant le deuxième semestre, taille en pixel des cases (pour pouvoir y mettre des images) 50 
A la création du plateau 
Definition des différentes zones du plateau
Peut se faire a l’aide de plusieurs configuration, (qui pourront être conservées)
Ensuite un place les différentes sommets a partir de symbole défini fig 2 (voir intra)
Définition/ modification des sommets de base 
Une base pour chacune des couleurs précédemment définies 
On peut tres bien avoir plusieurs bases dans un même zone  fig 3 socles de couleurs sont considéré comme des départs (on peut déposer les objets en drag and drop)
Les départs doivent être sur des sommets (formes)
Définir les arrêtes 
Générations automatique des liens entre les sommets, 8 direction possibles (exemples qu’on a vu au échecs dans le cours héritages) fig4
SI un sommet est supprimé ses liens sont supprimés fig5
Essayé d’avoir une image de fond 
Application jeu 
Le jeu des constitué de nb cartes  nb=nombre de symbole+1x2 
Exemples pour 4 symboles 
Il y aura 5 cartes foncé 1 pour chaque symbole + 1 JOKER 
Et autant en cartes claires fig 9 
Le jeu se joue en n manche n étant le nombre de couleurs 
Au début de la manche on prend toutes les cartes pour constituer un pioche (fermé ou ouverte comme vous voulez)
On affichera la manche en cours, (indicateurs couleur)
A chaque tour l’application tire une carte au hasard dans la pioche. Le joueur doit aller relier à partir de la base rouge ou d’un sommet relier à la bas rouge par des liens rouges déjà existants, un nouveau sommets 
On part des extrémités du chemin 
Pour compter les points:
Le joueur est passé par 3 notes
La zone est le plus de sommets reliés en vert est 
La zone centrale avec 3 sommets
Nb de point = nb de zone conquérants x la zone avec le plus de sommet 
Fig 10 
Manche Verte 3x3 
Manche Rouge 4x3
Manche 
Marron 3x2 
Ensuite vous développerez une version réseau pour 2 joueurs s
A chaque manche les joueurs ne partent pas avec les meme bases par contre ils ont le meme tirage 
Le vainceur est celui qui a le plus grand score final 
En car d ‘égalité le vainqueur est celui qui a obtenu le meilleur score une mange 
En cas de nouvelles « équalité »; les joueuirs sur partagent la victoire 
EVALUTAION 
Oral 
Ça sera a nous de démontrer que notre application fonctionne 
Theme + nom des sommets et arrête pour 13h 30 
Pour demain soir mardi 2 soir 
Règle du jeu adapté au thème 



