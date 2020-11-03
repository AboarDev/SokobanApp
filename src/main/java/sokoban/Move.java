package sokoban;

import java.util.Calendar;

public class Move {
    public Boolean hasMove;
    public Boolean includesCrate;
    public Worker worker;
    public Crate crate;
    public int moveX;
    public int moveY;
    public Direction direction;
    public Calendar time;
    public Long elapsed = (long) 0;

    public Move(){
        time = Calendar.getInstance();
        includesCrate = false;
        hasMove = false;
    }

    public Move(Worker theWorker, Direction theDirection,int x,int y) {
        time = Calendar.getInstance();
        moveX = x;
        moveY = y;
        includesCrate = false;
        hasMove = true;
        worker = theWorker;
        direction = theDirection;
    }
    public Move(Worker theWorker, Crate theCrate, Direction theDirection,int x,int y) {
        time = Calendar.getInstance();
        moveX = x;
        moveY = y;
        includesCrate = true;
        hasMove = true;
        worker = theWorker;
        crate = theCrate;
        direction = theDirection;
    }
}
