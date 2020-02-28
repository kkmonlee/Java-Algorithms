package DataStructures.Interfaces;

import java.util.Collection;

/**
 * Created by aa on 15/04/17.
 */
public interface ITree<T> {

    boolean add(T value);

    T remove(T value);

    public void clear();

    public boolean contains(T value);

    public int size();

    public boolean validate();

    public Collection<T> toCollection();
}
