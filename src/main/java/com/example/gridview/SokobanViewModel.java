package com.example.gridview;

import androidx.lifecycle.ViewModel;

import java.util.List;
import sokoban.*;

public class SokobanViewModel extends ViewModel {
    private Game game;
    Boolean canMove;
    List<List<String>> levelData;
    Boolean dataAdded;
    public int levelIndex;
    Boolean paused;
    int seconds;
    int minutes;
    public SokobanViewModel() {
        super();
        canMove = true;
        dataAdded = false;
        paused = false;
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
            return true;
        } else {
            return false;
        }
    }

    public void addLevel (List<String> level){
        paused = false;
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

    public void pause () {
        paused = true;
    }

    public void resume () {
        if (paused) {
            paused = false;
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
