package Searching.AStarSearchAlgorithm.Example;
import Searching.AStarSearchAlgorithm.Mover;

public class UnitMover implements Mover {
    private int type;

    public UnitMover(int type) {
        this.type = type;
    }

    public int getType() {
        return type;
    }
}
