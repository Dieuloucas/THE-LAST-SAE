package metier;

public enum Direction
{
	N(-1, 0), NE(-1, 1), E(0, 1), SE(1, 1),
	S(1, 0),  SO(1, -1), W(0, -1), NO(-1, -1);

	public final int dl;
	public final int dc;

	Direction(int dl, int dc)
	{
		this.dl = dl;
		this.dc = dc;
	}
}
