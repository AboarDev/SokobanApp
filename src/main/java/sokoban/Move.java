package sokoban;

public class Move {
    public Boolean hasMove;
    public Boolean includesCrate;
    public Worker worker;
    public Crate crate;
    public int moveX;
    public int moveY;
    public Direction direction;

    public Move(){
        includesCrate = false;
        hasMove = false;
    }

    public Move(Worker theWorker, Direction theDirection,int x,int y) {
        moveX = x;
        moveY = y;
        includesCrate = false;
        hasMove = true;
        worker = theWorker;
        direction = theDirection;
    }
    public Move(Worker theWorker, Crate theCrate, Direction theDirection,int x,int y) {
        moveX = x;
        moveY = y;
        includesCrate = true;
        hasMove = true;
        worker = theWorker;
        crate = theCrate;
        direction = theDirection;
    }
}
