package sokoban;

public class Target extends Destination {
	public Target(int y, int x) {
		super(y, x);
		objChar = '+';
		// TODO Auto-generated constructor stub
	}

	public void addWorker (Worker inp) {
		combinedChar = 'W';
		onObject = inp;
	}
	
	public void addCrate (Crate inp) {
		combinedChar = 'X';
		onObject = inp;
	}

}
