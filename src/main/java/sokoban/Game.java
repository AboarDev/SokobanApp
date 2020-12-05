package sokoban;

import java.util.*;
import java.util.stream.*;

public class Game {
	protected Deque<Level> gameLevels;
	public Level theLevel;
	public Game () {
		gameLevels = new LinkedList<>();
	}
	public void addLevel (String name, int width, int height,String grid) {
		gameLevels.offerFirst(new Level(name,width,height,grid));
		theLevel = gameLevels.peekFirst();
	}
	public Move move (Direction theDirection){
		return this.move(theDirection,false,false);
	}
	public Move move (Direction theDirection,boolean reverse){
		return this.move(theDirection,reverse,false);
	}
	public Move move (Direction theDirection,boolean reverse,boolean reversePush) {
		Move theMove;
		int inpX = 0;
		int inpY = 0;
		switch (theDirection) {
			case UP:
				inpX = 0;
				inpY = -1;
				break;
			case RIGHT:
				inpX = 1;
				inpY = 0;
				break;
			case LEFT:
				inpX = -1;
				inpY = 0;
				break;
			case DOWN:
				inpX = 0;
				inpY = 1;
				break;
		}
		boolean canMove = false;
		boolean pushed = false;
		if (reverse){
			inpX = inpX * -1;
			inpY = inpY * -1;
		}
		final Destination origObj = theLevel.getDestination(theLevel.thePlayer.x, theLevel.thePlayer.y);
		final Destination destObj = theLevel.getDestination(theLevel.thePlayer.x + inpX, theLevel.thePlayer.y + inpY);
		Destination crateDest = null;
		if (destObj != null) {
			if (destObj.onObject != null && !reverse) {
				crateDest = theLevel.getDestination(theLevel.thePlayer.x + (inpX*2), theLevel.thePlayer.y + (inpY*2));
				if (crateDest != null) {
					if (crateDest.onObject == null) {
						theLevel.moveMoveable(destObj,crateDest);
						canMove = true;
						pushed = true;
					}
				}
			} else if (reverse && reversePush){
				crateDest = theLevel.getDestination(theLevel.thePlayer.x - inpX, theLevel.thePlayer.y - inpY);
				if (crateDest != null && crateDest.onObject != null){
					pushed = true;
				}
				canMove = true;
			}
			else {
				canMove = true;
			}
			if (canMove == true) {
				theLevel.moveMoveable((Destination)origObj,destObj);
				if (pushed && reverse){
					theLevel.moveMoveable(crateDest,origObj);
				}
			}
		}
		theLevel.countMove(pushed);
		if (pushed && crateDest != null){
			theMove = new Move(theLevel.thePlayer, (Crate) crateDest.onObject, theDirection,inpX,inpY);
		}else if(canMove == true) {
			theMove = new Move(theLevel.thePlayer,theDirection,inpX,inpY);
		}else {
			theMove = new Move();
		}
		if (!reverse){
			theLevel.moves.add(theMove);
		}
		return theMove;
	}
	public Move getMove () {
		Deque moves = (Deque) theLevel.moves;
		Move theMove = (Move) moves.removeLast();
		theLevel.moves = (List) moves;
		return theMove;
	}
	public String getCurrentLevelName () {
		String out = "";
		if (gameLevels.size() < 1) {
			out = "no levels";
		}
		else {
			out = theLevel.getName();
		}
		return out;
	}
	public int getLevelCount() {
		return gameLevels.size();
	}
	public int getMoveCount() {
		return theLevel.getMoveCount();
	}
	public int getTargetCount(){
		return theLevel.targetCount;
	}
	public int getCompletedCount(){
		return theLevel.getCompletedCount();
	}
	public List<String> getLevelNames() {
		List<String> joined = gameLevels.stream()
			    .map(Object::toString)
			    .collect(Collectors.toList());
		return joined;
	}
	public String toString () {
		Level out = gameLevels.peekFirst();
		if (out == null) {
			return "no levels";
		}else {
			return out.toString();
		}
	}
	public Level findLevel(String theName){
		Level theLevel = null;
		for (Level l:gameLevels){
			if (l.name.equals(theName)){
				theLevel = l;
			}
		}
		return theLevel;
	}
	public void removeLevel(String theName){
		theLevel = findLevel(theName);
		if (theLevel != null){
			gameLevels.remove(theLevel);
		}
	}
}
