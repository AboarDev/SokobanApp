package sokoban;

public abstract class Destination extends Placeable {
	protected char combinedChar;
	public Placeable onObject;
	public Destination(int y, int x) {
		super(y, x);
		onObject = null;
		combinedChar = ' ';
		// TODO Auto-generated constructor stub
	}
	public abstract void addWorker (Worker inp);
	public abstract void addCrate (Crate inp);
	public Placeable removePlaceable () {
		Placeable out = onObject;
		onObject = null;
		combinedChar = ' ';
		return out;
	}
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		if (onObject != null) {
			return String.valueOf(combinedChar);
		} else {
			return super.toString();
		}
	}

}
