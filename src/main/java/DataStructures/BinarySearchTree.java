package DataStructures;

import DataStructures.Interfaces.ITree;

import java.lang.reflect.Array;
import java.util.*;
import java.util.Queue;
import java.util.Set;

/**
 * Created by aa on 15/04/17.
 */
@SuppressWarnings("unchecked")
public class BinarySearchTree<T extends Comparable<T>> implements ITree<T> {

    private int modifications = 0;

    protected static final Random RANDOM = new Random();

    protected Node<T> root = null;
    protected int size = 0;
    protected INodeCreator<T> creator = null;

    public enum DepthFirstSearchOrder {
        inOrder, preOrder, postOrder
    }

    public BinarySearchTree() {
        this.creator = new INodeCreator<T>() {
            @Override
            public Node<T> createNewNode(Node<T> parent, T id) {
                return (new Node<T>(parent, id));
            }
        };
    }

    public BinarySearchTree(INodeCreator<T> creator) {
        this.creator = creator;
    }

    @Override
    public boolean add(T value) {
        Node<T> nodeAdded = this.addValue(value);
        return (nodeAdded != null);
    }

    protected Node<T> addValue(T value) {
        Node<T> newNode = this.creator.createNewNode(null, value);

        if (root == null) {
            root = newNode;
            size++;
            return newNode;
        }

        Node<T> node = root;
        while (node != null) {
            if (newNode.id.compareTo(node.id) <= 0) {
                if (node.lesser == null) {
                    node.lesser = newNode;
                    newNode.parent = node;
                    size++;
                    return newNode;
                }
                node = node.lesser;
            } else {
                if (node.greater == null) {
                    node.greater = newNode;
                    newNode.parent = node;
                    size++;
                    return newNode;
                }
                node = node.greater;
            }
        }

        return newNode;
    }

    @Override
    public boolean contains(T value) {
        Node<T> node = getNode(value);
        return (node != null);
    }

    protected Node<T> getNode(T value) {
        Node<T> node = root;
        while (node != null && node.id != null) {
            if (value.compareTo(node.id) < 0) {
                node = node.lesser;
            } else if (value.compareTo(node.id) > 0) {
                node = node.greater;
            } else if (value.compareTo(node.id) == 0) {
                return node;
            }
        }
        return null;
    }

    protected void rotateLeft(Node<T> node) {
        Node<T> parent = node.parent;
        Node<T> greater = node.greater;
        Node<T> lesser = node.lesser;

        greater.lesser = node;
        node.parent = greater;

        node.greater = lesser;

        if (lesser != null)
            lesser.parent = node;

        if (parent != null) {
            if (node == parent.lesser) {
                parent.lesser = greater;
            } else if (node == parent.greater) {
                parent.greater = greater;
            } else {
                throw new RuntimeException("Not related to parent. " + node.toString());
            }
            greater.parent = parent;
        } else {
            root = parent;
            root.parent = null;
        }
    }

    protected void rotateRight(Node<T> node) {
        Node<T> parent = node.parent;
        Node<T> lesser = node.lesser;
        Node<T> greater = lesser.greater;

        lesser.greater = node;
        node.parent = lesser;

        node.lesser = greater;

        if (greater != null)
            greater.parent = node;

        if (parent != null) {
            if (node == parent.lesser) {
                parent.lesser = lesser;
            } else if (node == parent.greater) {
                parent.greater = lesser;
            } else {
                throw new RuntimeException("Not related to parent. " + node.toString());
            }
            lesser.parent = parent;
        } else {
            root = lesser;
            root.parent = null;
        }
    }

    protected Node<T> getGreatest(Node<T> startingNode) {
        if (startingNode == null)
            return null;

        Node<T> greater = startingNode.greater;
        while (greater != null && greater.id != null) {
            Node<T> node = greater.greater;
            if (node != null && node.id != null)
                greater = node;
            else
                break;
        }
        return greater;
    }

    protected Node<T> getLeast(Node<T> startingNode) {
        if (startingNode == null)
            return null;

        Node<T> lesser = startingNode.lesser;
        while (lesser != null && lesser.id != null) {
            Node<T> node = lesser.lesser;
            if (node != null && node.id != null)
                lesser = node;
            else
                break;
        }
        return lesser;
    }

    @Override
    public T remove(T value) {
        Node<T> nodeToRemove = this.removeValue(value);
        return ((nodeToRemove != null) ? nodeToRemove.id : null);
    }

    protected Node<T> removeValue(T value) {
        Node<T> nodeToRemove = this.getNode(value);
        if (nodeToRemove != null)
            nodeToRemove = removeNode(nodeToRemove);
        return nodeToRemove;
    }

    protected Node<T> removeNode(Node<T> nodeToRemove) {
        if (nodeToRemove != null) {
            Node<T> replacementNode = this.getReplacementNode(nodeToRemove);
            replaceNodeWithNode(nodeToRemove, replacementNode);
        }
        return nodeToRemove;
    }

    protected Node<T> getReplacementNode(Node<T> nodeToRemoved) {
        Node<T> replacement = null;
        if (nodeToRemoved.greater != null && nodeToRemoved.lesser != null) {
            // Two children.
            // Add some randomness to deletions, so we don't always use the
            // greatest/least on deletion
            if (modifications % 2 != 0) {
                replacement = this.getGreatest(nodeToRemoved.lesser);
                if (replacement == null)
                    replacement = nodeToRemoved.lesser;
            } else {
                replacement = this.getLeast(nodeToRemoved.greater);
                if (replacement == null)
                    replacement = nodeToRemoved.greater;
            }
            modifications++;
        } else if (nodeToRemoved.lesser != null && nodeToRemoved.greater == null) {
            // Using the less subtree
            replacement = nodeToRemoved.lesser;
        } else if (nodeToRemoved.greater != null && nodeToRemoved.lesser == null) {
            // Using the greater subtree (there is no lesser subtree, no refactoring)
            replacement = nodeToRemoved.greater;
        }
        return replacement;
    }

    protected void replaceNodeWithNode(Node<T> nodeToRemove, Node<T> replacementNode) {
        if (replacementNode != null) {
            // Save for later
            Node<T> replacementNodeLesser = replacementNode.lesser;
            Node<T> replacementNodeGreater = replacementNode.greater;

            // Replace replacementNode's branches with nodeToRemove's branches
            Node<T> nodeToRemoveLesser = nodeToRemove.lesser;
            if (nodeToRemoveLesser != null && nodeToRemoveLesser != replacementNode) {
                replacementNode.lesser = nodeToRemoveLesser;
                nodeToRemoveLesser.parent = replacementNode;
            }
            Node<T> nodeToRemoveGreater = nodeToRemove.greater;
            if (nodeToRemoveGreater != null && nodeToRemoveGreater != replacementNode) {
                replacementNode.greater = nodeToRemoveGreater;
                nodeToRemoveGreater.parent = replacementNode;
            }

            // Remove link from replacementNode's parent to replacement
            Node<T> replacementParent = replacementNode.parent;
            if (replacementParent != null && replacementParent != nodeToRemove) {
                Node<T> replacementParentLesser = replacementParent.lesser;
                Node<T> replacementParentGreater = replacementParent.greater;
                if (replacementParentLesser != null && replacementParentLesser == replacementNode) {
                    replacementParent.lesser = replacementNodeGreater;
                    if (replacementNodeGreater != null)
                        replacementNodeGreater.parent = replacementParent;
                } else if (replacementParentGreater != null && replacementParentGreater == replacementNode) {
                    replacementParent.greater = replacementNodeLesser;
                    if (replacementNodeLesser != null)
                        replacementNodeLesser.parent = replacementParent;
                }
            }
        }

        Node<T> parent = nodeToRemove.parent;
        if (parent == null) {
            root = replacementNode;
            if (root != null)
                root.parent = null;
        } else if (parent.lesser != null && (parent.lesser.id.compareTo(nodeToRemove.id) == 0)) {
            parent.lesser = replacementNode;
            if (replacementNode != null)
                replacementNode.parent = parent;
        } else if (parent.greater != null && (parent.greater.id.compareTo(nodeToRemove.id) == 0)) {
            parent.greater = replacementNode;
            if (replacementNode != null)
                replacementNode.parent = parent;
        }
        size--;
    }

    @Override
    public void clear() {
        root = null;
        size = 0;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean validate() {
        if (root == null) return true;
        return validateNode(root);
    }

    protected boolean validateNode(Node<T> node) {
        Node<T> lesser = node.lesser;
        Node<T> greater = node.greater;

        boolean lesserCheck = true;
        if (lesser != null && lesser.id != null) {
            lesserCheck = (lesser.id.compareTo(node.id) <= 0);
            if (lesserCheck)
                lesserCheck = validateNode(lesser);
        }
        if (!lesserCheck)
            return false;

        boolean greaterCheck = true;
        if (greater != null && greater.id != null) {
            greaterCheck = (greater.id.compareTo(node.id) > 0);
            if (greaterCheck)
                greaterCheck = validateNode(greater);
        }
        return greaterCheck;
    }

    public T[] getBFS() {
        return getBFS(this.root, this.size);
    }

    public static <T extends Comparable<T>> T[] getBFS(Node<T> start, int size) {
        final Queue<Node<T>> queue = new ArrayDeque<>();
        final T[] values = (T[]) Array.newInstance(start.id.getClass(), size);
        int count = 0;
        Node<T> node = start;

        while (node != null) {
            values[count++] = node.id;
            if (node.lesser != null)
                queue.add(node.lesser);
            if (node.greater != null)
                queue.add(node.greater);
            if (!queue.isEmpty())
                node = queue.remove();
            else
                node = null;
        }
        return values;
    }

    public T[] getLevelOrder() {
        return getBFS();
    }

    public T[] getDFS(DepthFirstSearchOrder order) {
        return getDFS(order, this.root, this.size);
    }

    public static <T extends Comparable<T>> T[] getDFS(DepthFirstSearchOrder order, Node<T> start, int size) {
        final Set<Node<T>> added = new HashSet<>(2);
        final T[] nodes = (T[])Array.newInstance(start.id.getClass(), size);
        int index = 0;
        Node<T> node = start;
        while (index < size && node != null) {
            Node<T> parent = node.parent;
            Node<T> lesser = (node.lesser != null && !added.contains(node.lesser)) ? node.lesser : null;
            Node<T> greater = (node.greater != null && !added.contains(node.greater)) ? node.greater : null;

            if (parent == null && lesser == null && greater == null) {
                if (!added.contains(node))
                    nodes[index++] = node.id;
                break;
            }

            if (order == DepthFirstSearchOrder.inOrder) {
                if (lesser != null) {
                    node = lesser;
                } else {
                    if (!added.contains(node)) {
                        nodes[index++] = node.id;
                        added.add(node);
                    }
                    if (greater != null) {
                        node = greater;
                    } else if (added.contains(node)) {
                        node = parent;
                    } else {
                        // We should not get here. Stop the loop!
                        node = null;
                    }
                }
            } else if (order == DepthFirstSearchOrder.preOrder) {
                if (!added.contains(node)) {
                    nodes[index++] = node.id;
                    added.add(node);
                }
                if (lesser != null) {
                    node = lesser;
                } else if (greater != null) {
                    node = greater;
                } else if (added.contains(node)) {
                    node = parent;
                } else {
                    // We should not get here. Stop the loop!
                    node = null;
                }
            } else {
                // post-Order
                if (lesser != null) {
                    node = lesser;
                } else {
                    if (greater != null) {
                        node = greater;
                    } else {
                        // lesser==null && greater==null
                        nodes[index++] = node.id;
                        added.add(node);
                        node = parent;
                    }
                }
            }
        }
        return nodes;
    }

    public T[] getSorted() {
        return getDFS(DepthFirstSearchOrder.inOrder);
    }

    @Override
    public Collection<T> toCollection() {
        return (new JavaCompatibleBinarySearchTree<>(this));
    }

    @Override
    public String toString() {
        return TreePrinter.getString(this);
    }


    protected static class Node<T extends Comparable<T>> {
        protected T id = null;
        protected Node<T> parent = null;
        protected Node<T> lesser = null;
        protected Node<T> greater = null;

        protected Node(Node<T> parent, T id) {
            this.parent = parent;
            this.id = id;
        }

        @Override
        public String toString() {
            return "id=" + id + " parent=" + ((parent != null) ? parent.id : "NULL") + " lesser="
                    + ((lesser != null) ? lesser.id : "NULL") + " greater=" + ((greater != null) ? greater.id : "NULL");
        }
    }

    protected static interface INodeCreator<T extends Comparable<T>> {
        public Node<T> createNewNode(Node<T> parent, T id);
    }

    protected static class TreePrinter {
        public static <T extends Comparable<T>> String getString(BinarySearchTree<T> tree) {
            if (tree.root == null)
                return "Tree has no nodes.";
            return getString(tree.root, "", true);
        }

        private static <T extends Comparable<T>> String getString(Node<T> node, String prefix, boolean isTail) {
            StringBuilder builder = new StringBuilder();

            if (node.parent != null) {
                String side = "left";
                if (node.equals(node.parent.greater))
                    side = "right";
                builder.append(prefix + (isTail ? "└── " : "├── ") + "(" + side + ") " + node.id + "\n");
            } else {
                builder.append(prefix + (isTail ? "└── " : "├── ") + node.id + "\n");
            }
            List<Node<T>> children = null;
            if (node.lesser != null || node.greater != null) {
                children = new ArrayList<Node<T>>(2);
                if (node.lesser != null)
                    children.add(node.lesser);
                if (node.greater != null)
                    children.add(node.greater);
            }
            if (children != null) {
                for (int i = 0; i < children.size() - 1; i++) {
                    builder.append(getString(children.get(i), prefix + (isTail ? "    " : "│   "), false));
                }
                if (children.size() >= 1) {
                    builder.append(getString(children.get(children.size() - 1), prefix + (isTail ? "    " : "│   "), true));
                }
            }

            return builder.toString();
        }
    }

    private static class JavaCompatibleBinarySearchTree<T extends Comparable<T>> extends java.util.AbstractCollection<T> {

        protected BinarySearchTree<T> tree = null;

        public JavaCompatibleBinarySearchTree(BinarySearchTree<T> tree) {
            this.tree = tree;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public boolean add(T value) {
            return tree.add(value);
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public boolean remove(Object value) {
            return (tree.remove((T) value) != null);
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public boolean contains(Object value) {
            return tree.contains((T) value);
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public int size() {
            return tree.size();
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public java.util.Iterator<T> iterator() {
            return (new BinarySearchTreeIterator<T>(this.tree));
        }

        private static class BinarySearchTreeIterator<C extends Comparable<C>> implements java.util.Iterator<C> {

            private BinarySearchTree<C> tree = null;
            private BinarySearchTree.Node<C> last = null;
            private Deque<Node<C>> toVisit = new ArrayDeque<Node<C>>();

            protected BinarySearchTreeIterator(BinarySearchTree<C> tree) {
                this.tree = tree;
                if (tree.root != null) toVisit.add(tree.root);
            }

            /**
             * {@inheritDoc}
             */
            @Override
            public boolean hasNext() {
                if (toVisit.size() > 0) return true;
                return false;
            }

            /**
             * {@inheritDoc}
             */
            @Override
            public C next() {
                while (toVisit.size() > 0) {
                    // Go thru the current nodes
                    BinarySearchTree.Node<C> n = toVisit.pop();

                    // Add non-null children
                    if (n.lesser != null) toVisit.add(n.lesser);
                    if (n.greater != null) toVisit.add(n.greater);

                    // Update last node (used in remove method)
                    last = n;
                    return n.id;
                }
                return null;
            }

            /**
             * {@inheritDoc}
             */
            @Override
            public void remove() {
                tree.removeNode(last);
            }
        }
    }

}

