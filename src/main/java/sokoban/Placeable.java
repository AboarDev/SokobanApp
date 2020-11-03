package sokoban;

public abstract class Placeable {
	public int x;
	public int y;
	protected char objChar;
	protected boolean onTarget;
	public Placeable (int y,int x) {
		this.x = x;
		this.y = y;
	}
	public String toString () {
		return String.valueOf(objChar);
	}
}
