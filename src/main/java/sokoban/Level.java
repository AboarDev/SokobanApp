package sokoban;

//import java.util.*;

import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;

public class Level {
	
	public int targetCount;
	protected Placeable[] [] allPlaceables;
	public Worker thePlayer;
	final protected String name;
	protected int pushes = 0;
	public List<Move> moves = new LinkedList<Move>();
	public Long Elapsed = (long) 0;
	public Calendar timeInitialized;
	
	public Level (String inpName, int inpHeight, int inpWidth, String inpGrid) {
		allPlaceables = new Placeable [inpHeight] [inpWidth];
		name = inpName;
		targetCount = 0;
		this.parseLevel(inpHeight, inpWidth, inpGrid);
		timeInitialized = Calendar.getInstance();
	}
	
	public void moveMoveable(Destination origin,Destination destination) {
		final Placeable toMove = origin.removePlaceable();
		toMove.x = destination.x;
		toMove.y = destination.y;
		if (toMove instanceof Worker) {
			destination.addWorker((Worker) toMove);
		} else if (toMove instanceof Crate) {
			destination.addCrate((Crate) toMove);
		}
	}
	
	public void countMove(boolean countPushes) {
		if (countPushes) {
			pushes++;
		}
		//moveCount++;
	}
	
	public int getHeight() {
		int height = allPlaceables.length;
		return height;
	}
	
	public int getWidth() {
		int width = allPlaceables[0].length;
		return width;
	}
	
	public Destination getDestination (int x, int y) {
		Destination out = null;
		if (allPlaceables[y][x] instanceof Destination) {
			out = (Destination) allPlaceables[y][x];
		}
		//System.out.println(out.getClass());
		return out;
	}
	
	public String getName() {
		return name;
	}
	
	public int getCompletedCount() {
		int out = 0;
		for (Placeable[] item:allPlaceables) {
			for (Placeable theItem: item) {
			if (theItem instanceof Target) {
				Target theTarget = (Target) theItem;
					if(theTarget.onObject instanceof Crate) {
						out++;
					}
				}
			}
		}
		return out;
	}
	
	public int getMoveCount() {
		return moves.size();
	}
	
	private void parseLevel (int height, int width, String grid) {
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				int n = y * width;
				this.parseX(grid.charAt(x+n),y,x);
			}
		}
	}
	
	private void parseX(char theChar,int inpY,int inpX) {
		Destination theEmpty;
		Crate theCrate;
		switch (theChar) {
			case '+':
				allPlaceables[inpY][inpX] = new Target(inpY,inpX);
				targetCount++;
				break;
			case '.':
				allPlaceables[inpY][inpX] = (new Empty(inpY,inpX));
				break;
			case '#':
				allPlaceables[inpY][inpX] = (new Wall(inpY,inpX));
				break;
			case 'x':
				theEmpty = new Empty(inpY,inpX);
				theCrate = new Crate(inpY,inpX);
				theEmpty.addCrate(theCrate);
				allPlaceables[inpY][inpX] = (theEmpty);
				//items.add(theCrate);
				break;
			case 'w':
				theEmpty = new Empty(inpY,inpX);
				thePlayer = new Worker(inpY,inpX);
				theEmpty.addWorker(thePlayer);
				allPlaceables[inpY][inpX] = (theEmpty);
				//items.add(thePlayer);
				break;
			case 'W':
				theEmpty = new Target(inpY,inpX);
				thePlayer = new Worker(inpY,inpX);
				theEmpty.addWorker(thePlayer);
				allPlaceables[inpY][inpX] = (theEmpty);
				targetCount++;
				//items.add(thePlayer);
				break;
			case 'X':
				theEmpty = new Target(inpY,inpX);
				theCrate = new Crate(inpY,inpX);
				theEmpty.addCrate(theCrate);
				allPlaceables[inpY][inpX] = (theEmpty);
				targetCount++;
				//items.add(theCrate);
				break;
			default:
				break;
		}
	}

	public String getSchema (){
		StringBuilder out = new StringBuilder();
		for (Placeable[] item : allPlaceables) {
			for (Placeable theItem : item) {
				out.append(theItem.toString());
			}
		}
		System.out.println(out.toString());
		return out.toString();
	}

	public String toString () {
		StringBuilder out = new StringBuilder();
		out.append(name + "\n");
		for (Placeable[] item:allPlaceables) {
			for (Placeable theItem: item) {
			out.append(theItem.toString());
			}
			out.append("\n");
		}
		out.append("move ");
		out.append(getMoveCount());
		out.append("\ncompleted ");
		out.append(getCompletedCount());
		out.append(" of ");
		out.append(targetCount);
		out.append("\n");
		return out.toString();
	}
}
