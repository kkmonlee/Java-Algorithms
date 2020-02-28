package DataStructures;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("unchecked")
public class FenwickTree<D extends FenwickTree.Data> {

    private Object[] array;

    public FenwickTree(List<D> data) {
        int n = 0;
        for (Data d : data)
            if (d.index > n)
                n = d.index;
        n = next(n + 1);
        array = new Object[n];

        for (D d : data)
            update(d.index, d);
    }

    /**
     * Stabbing query
     * @param index index for query
     * @return data at index
     */
    public D query(int index) {
        return query(index, index);
    }

    /**
     * Range query
     * @param start start of range inclusive
     * @param end end of range inclusive
     * @return data for range
     */
    public D query(int start, int end) {
        final D e = lookup(end);
        final D s = lookup(start - 1);
        final D c = (D) e.copy();
        if (s != null)
            c.separate(s);
        return c;
    }

    private D lookup(int index) {
        index++;
        index = Math.min(array.length - 1, index);
        if (index <= 0)
            return null;

        D res = null;
        while (index > 0) {
            if (res == null) {
                final D data = (D) array[index];
                if (data != null)
                    res = (D) data.copy();
            } else {
                res.combined((D) array[index]);
            }
            index = prev(index);
        }
        return res;
    }

    private void update(int index, D value) {
        index++;
        while (index < array.length) {
            D data = (D) array[index];
            if (data == null) {
                data = (D) value.copy();
                data.index = index;
                array[index] = data;
            } else {
                data.combined(value);
            }
            index = next(index);
        }
    }

    private static final int prev(int x) {
        return x & (x - 1);
    }

    private static final int next(int x) {
        return 2 * x - prev(x);
    }

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        builder.append(FenwickTreePrinter.getString(this));
        return builder.toString();
    }

    protected static class FenwickTreePrinter {
        public static <D extends FenwickTree.Data> String getString(FenwickTree<D> tree) {
            if (tree.array.length == 0)
                return "Tree has no nodes.";

            final D first = (D) tree.array[1];
            if (first == null)
                return "Tree has no nodes.";

            final StringBuilder builder = new StringBuilder();
            builder.append("└── dummy \n");
            builder.append(getString(tree, 0, tree.array.length, "", true));
            return builder.toString();
        }

        private static <D extends FenwickTree.Data> String getString(FenwickTree<D> tree, int start, int end, String prefix, boolean isTail) {
            if (end > tree.array.length || (end - start == 0))
                return "";

            final StringBuilder builder = new StringBuilder();

            final D value = (D) tree.array[start];
            if (value != null)
                builder.append(prefix + (isTail ? "└── " : "├── ") + value + "\n");

            int next = start + 1;
            final List<Integer> children = new ArrayList<Integer>(2);
            while (next < end) {
                children.add(next);
                next = next(next);
            }
            for (int i = 0; i < children.size() - 1; i++)
                builder.append(getString(tree, children.get(i),                 children.get(i+1), prefix + (isTail ? "    " : "│   "), false));
            if (children.size() >= 1)
                builder.append(getString(tree, children.get(children.size()-1), end,               prefix + (isTail ? "    " : "│   "), true));

            return builder.toString();
        }
    }

    public abstract static class Data implements Comparable<Data> {
        protected int index = Integer.MIN_VALUE;

        protected Data(int index) {
            this.index = index;
        }

        public void clear() {
            index = Integer.MIN_VALUE;
        }

        public abstract Data combined(Data data);

        public abstract Data separate(Data data);

        public abstract Data copy();

        public abstract Data query(long startOfRange, long endOfRange);

        @Override
        public String toString() {
            final StringBuilder builder = new StringBuilder();
            builder.append("[").append(index).append("]");
            return builder.toString();
        }

        @Override
        public int compareTo(Data d) {
            if (this.index < d.index)
                return -1;
            if (d.index < this.index)
                return 1;
            return 0;
        }

        public static final class RangeSumData<N extends Number> extends Data {
            public N sum = null;

            public RangeSumData(int index, N number) {
                super(index);
                this.sum = number;
            }

            @Override
            public void clear() {
                super.clear();
                sum = null;
            }

            @Override
            public Data combined(Data data) {
                RangeSumData<N> q = null;
                if (data instanceof RangeSumData) {
                    q = (RangeSumData<N>) data;
                    this.combined(q);
                }
                return this;
            }

            @Override
            public Data separate(Data data) {
                RangeSumData<N> q = null;
                if (data instanceof RangeSumData) {
                    q = (RangeSumData<N>) data;
                    this.separate(q);
                }
                return this;
            }

            private void combined(RangeSumData<N> data) {
                if (this.sum == null && data.sum == null)
                    return;
                else if (this.sum != null && data.sum == null)
                    return;
                else if (this.sum == null && data.sum != null)
                    this.sum = data.sum;
                else {
                    if (this.sum instanceof BigDecimal || data.sum instanceof BigDecimal) {
                        BigDecimal result = ((BigDecimal)this.sum).add((BigDecimal)data.sum);
                        this.sum = (N)result;
                    } else if (this.sum instanceof BigInteger || data.sum instanceof BigInteger) {
                        BigInteger result = ((BigInteger)this.sum).add((BigInteger)data.sum);
                        this.sum = (N)result;
                    } else if (this.sum instanceof Long || data.sum instanceof Long) {
                        Long result = (this.sum.longValue() + data.sum.longValue());
                        this.sum = (N)result;
                    } else if (this.sum instanceof Double || data.sum instanceof Double) {
                        Double result = (this.sum.doubleValue() + data.sum.doubleValue());
                        this.sum = (N)result;
                    } else if (this.sum instanceof Float || data.sum instanceof Float) {
                        Float result = (this.sum.floatValue() + data.sum.floatValue());
                        this.sum = (N)result;
                    } else {
                        // Integer
                        Integer result = (this.sum.intValue() + data.sum.intValue());
                        this.sum = (N)result;
                    }
                }
            }

            private void separate(RangeSumData<N> data) {
                if (this.sum == null && data.sum == null)
                    return;
                else if (this.sum != null && data.sum == null)
                    return;
                else if (this.sum == null && data.sum != null)
                    this.sum = data.sum;
                else {
                    if (this.sum instanceof BigDecimal || data.sum instanceof BigDecimal) {
                        BigDecimal result = ((BigDecimal)this.sum).subtract((BigDecimal)data.sum);
                        this.sum = (N)result;
                    } else if (this.sum instanceof BigInteger || data.sum instanceof BigInteger) {
                        BigInteger result = ((BigInteger)this.sum).subtract((BigInteger)data.sum);
                        this.sum = (N)result;
                    } else if (this.sum instanceof Long || data.sum instanceof Long) {
                        Long result = (this.sum.longValue() - data.sum.longValue());
                        this.sum = (N)result;
                    } else if (this.sum instanceof Double || data.sum instanceof Double) {
                        Double result = (this.sum.doubleValue() - data.sum.doubleValue());
                        this.sum = (N)result;
                    } else if (this.sum instanceof Float || data.sum instanceof Float) {
                        Float result = (this.sum.floatValue() - data.sum.floatValue());
                        this.sum = (N)result;
                    } else {
                        // Integer
                        Integer result = (this.sum.intValue() - data.sum.intValue());
                        this.sum = (N)result;
                    }
                }
            }

            @Override
            public Data copy() {
                return new RangeSumData<N>(index, sum);
            }

            @Override
            public Data query(long startOfQuery, long endOfQuery) {
                if (endOfQuery < this.index || startOfQuery > this.index)
                    return null;

                return copy();
            }

            @Override
            public int hashCode() {
                return 31 * (int)(this.index + this.sum.hashCode());
            }

            @Override
            public boolean equals(Object obj) {
                if (!(obj instanceof RangeSumData))
                    return false;

                RangeSumData<N> data = (RangeSumData<N>) obj;
                if (this.index == data.index && this.sum.equals(data.sum))
                    return true;

                return false;
            }

            @Override
            public String toString() {
                StringBuilder builder = new StringBuilder();
                builder.append(super.toString()).append(" ");
                builder.append("sum=").append(sum);
                return builder.toString();
            }
        }
    }

}
