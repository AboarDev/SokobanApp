package com.example.gridview;

import androidx.lifecycle.ViewModel;

import java.util.List;
import sokoban.*;
import java.util.Calendar;

public class SokobanViewModel extends ViewModel {
    private Game game;
    Boolean canMove;
    List<List<String>> levelData;
    Boolean dataAdded;
    public int levelIndex;
    Long totalElapsed;
    Long totalPaused;
    Calendar timePaused;
    Boolean paused;
    int seconds;
    int minutes;
    public SokobanViewModel() {
        super();
        canMove = true;
        dataAdded = false;
        paused = false;
        totalPaused = (long) 0;
        totalElapsed = (long) 0;
        levelIndex = 0;
        game = new Game();
        seconds = -1;
        minutes = 0;
    }

    public void closeLevel () {
        game.removeLevel(game.getCurrentLevelName());
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
        int moveCount = game.getMoveCount();
        if (moveCount > 0){
            theMove = game.getMove();
            if (theMove.hasMove){
                game.move(theMove.direction,true,theMove.includesCrate);
                System.out.println(game.toString());
            }
        }
        return theMove;
    }

    public boolean getLevelByString(String inp){
        Level foundLevel = game.findLevel(inp);
        if (foundLevel != null){
            paused = false;
            game.theLevel = foundLevel;
            totalElapsed = game.getElapsed();
            return true;
        } else {
            return false;
        }
    }

    public void addLevel (List<String> level){
        paused = false;
        totalPaused = (long) 0;
        totalElapsed = (long) 0;
        String name = level.get(3);
        game.addLevel(name,Integer.parseInt(level.get(2)),Integer.parseInt(level.get(1)),level.get(0));
    }
    public int getLevelWidth () {
            return game.theLevel.getWidth();
    }
    public int getLevelHeight () {
            return game.theLevel.getHeight();
    }
    public String getLevelSchema(){
        return game.theLevel.getSchema();
    }
    public Move move (Direction theDirection){
        Move theMove = null;
        if (canMove) {
            canMove = true;
            theMove = game.move(theDirection,false);
            System.out.println(game.toString());
        }
        return theMove;
    }
    public int moves (){
        return game.getMoveCount();
    }
    public int completed (){
        return game.getCompletedCount();
    }
    public int total (){
        return game.getTargetCount();
    }
    public String getElapsed (Move LastMove) {
        totalElapsed += game.getElapsed();
        if (totalPaused > 0){
            totalElapsed -= totalPaused;
        }
        if (LastMove != null && LastMove.elapsed != null) {
            LastMove.elapsed = totalElapsed;
        }
        String out = "";
        long elapsed = totalElapsed;
        long milliseconds =  totalElapsed % 1000;
        elapsed = (elapsed - milliseconds) /1000;
        long seconds = elapsed % 60;
        elapsed = (elapsed - seconds) / 60;
        long minutes = elapsed % 60;

        out += (int) minutes;
        out += " : ";
        out += (int) seconds;
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
            long i = now.getTimeInMillis() - timePaused.getTimeInMillis();
            totalPaused += i;
            System.out.println(totalPaused);
        }
    }

    public String timer () {
        if (seconds < 60){
            seconds ++;
        }else {
            seconds = 0;
            minutes++;
        }
        return minutes + ":" + seconds;
    }
}
