package DataStructures;

import java.util.ArrayList;
import java.util.List;

/**
 * Set is an abstract data structure used for storing elements.
 */
public class Set<ENTITY> {

    private List<ENTITY> list;

    /**
     * Constructor
     * @param initialCapacity initial capacity of the underlying ArrayList
     */
    public Set(int initialCapacity) {
        list = new ArrayList<ENTITY>(initialCapacity);
    }

    /**
     * Inserts entity into the set
     * @param e entity
     * @return true if the insertion was successful
     *         false if the set already contained e
     */
    public boolean put(ENTITY e) {
        if (list.contains(e)) return false;
        list.add(e);
        return true;
    }

    /**
     * Return (and remove) an element from the set
     * @return null if set is empty
     */
    public ENTITY pick() {
        if (list.size() == 0) return null;
        return list.remove(list.size() - 1);
    }

    /**
     * Remove given entity from the set
     * @param e entity
     * @return true  if the set contained the entity
     *         false otherwise
     */
    public boolean remove(ENTITY e) {
        return list.remove(e);
    }

    /**
     * Query if the set contains given entity
     * @param e entity
     * @return true if set contains the entity
     *         false otherwise
     */
    public boolean contains(ENTITY e) {
        return list.contains(e);
    }

    /**
     * Size of the set
     * @return number of stored entities
     */
    public int size() {
        return list.size();
    }

}
