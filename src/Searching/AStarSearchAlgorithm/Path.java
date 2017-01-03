package Searching.AStarSearchAlgorithm;

import java.util.ArrayList;
import java.util.Objects;

/**
 * A path determined by some path finding algorithm. A series of
 * steps from the starting location to the target location.
 * This includes a step for the initial location.
 *
 * @author kkmonlee
 */
public class Path {

    private ArrayList steps = new ArrayList();

    public Path() {

    }

    public int getLength() {
        return steps.size();
    }

    public Step getStep(int index) {
        return (Step) steps.get(index);
    }

    public int getX(int index) {
        return getStep(index).x;
    }

    public int getY(int index) {
        return getStep(index).y;
    }

    public void appendStep(int x, int y) {
        steps.add(new Step(x,y));
    }

    public void prependStep(int x, int y) {
        steps.add(0, new Step(x, y));
    }

    public boolean contains(int x, int y) {
        return steps.contains(new Step(x,y));
    }

    public class Step {
        private int x;
        private int y;

        public Step(int x, int y) {
            this.x = x;
            this.y = y;
        }

        public int getX() {
            return x;
        }

        public int getY() {
            return y;
        }

        public int hashCode() {
            return x * y;
        }

        public boolean equals(Object object) {
            if (object instanceof Step) {
                Step o = (Step) object;
                return (o.x == x) && (o.y == y);
            }

            return false;
        }

    }


}
