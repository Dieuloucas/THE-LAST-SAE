package plateau.metier;

// Représente un joueur en mode LOCAL : son numéro, sa station de départ et son réseau.
// Comme la pioche et la carte sont COMMUNES, le joueur n'a qu'un indicateur
// "a joué" pour savoir s'il a déjà posé (ou passé) la carte commune courante.
public class Joueur
{
	private int          numero;        // 1..4
	private int          numCaseDepart; // index de sa case de départ sur le plateau
	private int          score;         // score final calculé en fin de partie

	private ReseauJoueur reseau;

	private boolean      aJoue;         // a déjà joué la carte commune courante ?
	

	public Joueur(int numero, int numCaseDepart)
	{
		this.numero        = numero;
		this.numCaseDepart = numCaseDepart;

		this.reseau        = new ReseauJoueur();

		this.aJoue         = false;

		this.score         = 0;

		// La station de départ fait partie du réseau dès le début

		this.reseau.ajouterStation(numCaseDepart);
	}

	public int          getNumero       ()          { return this.numero;        }
	public int          getNumCaseDepart()          { return this.numCaseDepart; }
	public ReseauJoueur getReseau       ()          { return this.reseau;        }
	public int          getScore        ()          { return this.score;         }

	public boolean      aJoue           ()          { return this.aJoue;         }

	public void         setAJoue        (boolean b) { this.aJoue = b;            }
	public void         setScore        (int score) { this.score = score;        }
}
