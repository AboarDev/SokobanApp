package com.example.gridview;

import androidx.lifecycle.ViewModel;

import java.util.List;
import sokoban.*;
import java.util.Calendar;

public class SokobanViewModel extends ViewModel {
    private Game theGame;
    Boolean canMove;
    List<List<String>> levelData;
    Boolean dataAdded;
    public int levelIndex;
    Long totalElapsed;
    Long totalPaused;
    Calendar timePaused;
    Boolean paused;
    Boolean rotate;
    public SokobanViewModel() {
        super();
        canMove = true;
        dataAdded = false;
        paused = false;
        rotate = false;
        totalPaused = (long) 0;
        totalElapsed = (long) 0;
        levelIndex = 0;
        theGame = new Game();
    }

    public void closeLevel () {
        theGame.removeLevel(theGame.getCurrentLevelName());
    }

    public String[] getLevels () {
        String[] out = new String[levelData.size()];
        int count = 0;
        for (List<String> x: levelData) {
            out[count] = x.get(3);
            count++;
        }
        return out;
    }

    public Move undo(){
        Move theMove = null;
        int moveCount = theGame.getMoveCount();
        if (moveCount > 0){
            theMove = theGame.getMove();
            if (theMove.hasMove){
                theGame.move(theMove.direction,true,theMove.includesCrate);
                System.out.println(theGame.toString());
            }
        }
        return theMove;
    }

    public boolean getLevelByString(String inp){
        Level foundLevel = theGame.findLevel(inp);
        if (foundLevel != null){
            paused = false;
            theGame.theLevel = foundLevel;
            totalElapsed = theGame.getElapsed();
            return true;
        } else {
            return false;
        }
    }

    public void addLevel (List<String> level){
        paused = false;
        totalPaused = (long) 0;
        totalElapsed = (long) 0;
        //totalElapsed = theGame.getElapsed();
        String name = level.get(3);
        theGame.addLevel(name,Integer.parseInt(level.get(2)),Integer.parseInt(level.get(1)),level.get(0));
    }
    public int getLevelWidth () {
        if (rotate){
            return theGame.theLevel.getHeight();
        }else {
            return theGame.theLevel.getWidth();
        }
    }
    public int getLevelHeight () {
        if (rotate){
            return theGame.theLevel.getWidth();
        } else {
            return theGame.theLevel.getHeight();
        }
    }
    public String getLevelSchema(){
        return theGame.theLevel.getSchema(rotate);
    }
    public Move move (Direction theDirection){
        Move theMove = null;
        if (canMove) {
            canMove = true;
            theMove = theGame.move(theDirection,false);
            System.out.println(theGame.toString());
        }
        return theMove;
    }
    public int moves (){
        return theGame.getMoveCount();
    }
    public int completed (){
        return theGame.getCompletedCount();
    }
    public int total (){
        return theGame.getTargetCount();
    }
    public String getElapsed (Move LastMove) {
        Calendar now = Calendar.getInstance();
        totalElapsed += theGame.getElapsed();
        if (totalPaused > 0){
            totalElapsed -= totalPaused;
        }
        if (LastMove != null && LastMove.elapsed != null) {
            LastMove.elapsed = totalElapsed;
        }
        String out = "";
        Long elapsed = totalElapsed.longValue();
        Long milliseconds =  totalElapsed % 1000;
        elapsed = (elapsed - milliseconds) /1000;
        Long seconds = elapsed % 60;
        elapsed = (elapsed - seconds) / 60;
        Long minutes = elapsed % 60;

        out += minutes.intValue();
        out += " : ";
        out += seconds.intValue();
        out += ".";
        out += milliseconds / 100;
        totalPaused = (long) 0;
        return out;
    }
    public void pause () {
        paused = true;
        timePaused = Calendar.getInstance();
    }
    public void resume () {
        if (paused) {
            paused = false;
            Calendar now = Calendar.getInstance();
            Long i = now.getTimeInMillis() - timePaused.getTimeInMillis();
            totalPaused += i;
            System.out.println(totalPaused);
        }
    }
}