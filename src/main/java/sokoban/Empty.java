package sokoban;

public class Empty extends Destination {
	public Empty(int y, int x) {
		super(y, x);
		// TODO Auto-generated constructor stub
		objChar = '.';
	}

	public void addWorker (Worker inp) {
		combinedChar = 'w';
		onObject = inp;
	}
	
	public void addCrate (Crate inp) {
		combinedChar = 'x';
		onObject = inp;
	}

}
