package DataStructure;

import java.util.ArrayList;
import java.util.List;

public class FibonacciHeap<T>
{
    private static final double oneOverLogPhi = 1.0 / Math.log((1.0 + Math.sqrt(5.0)) / 2.0);
    private FibonacciHeapNode<T> minNode;
    private int numNodes; //num of nodes in the heap

    public FibonacciHeap(T data, double key)
    {
        minNode = new FibonacciHeapNode<T>(data, key);
    }

    public FibonacciHeap()
    {
        minNode = null;
    }

    public boolean isEmpty()
    {
        return minNode == null;
    }

    public void clear()
    {
        minNode = null;
        numNodes = 0;
    }

    public void decreaseKey(FibonacciHeapNode<T> node, double x)
    {
        if (x > node.getKey())
        {
            throw new IllegalArgumentException();
        }
        node.key = x;
        FibonacciHeapNode<T> parent = node.parent;
        if ((parent != null) && (node.key < parent.key))
        {
            cut(node, parent);
            cascadingCut(parent);
        }
        if (node.key < minNode.key)
        {
            minNode = node;
        }
    }

    public void delete(FibonacciHeapNode<T> node)
    {
        decreaseKey(node, Double.NEGATIVE_INFINITY);
        removeMin();
    }

    public void insert(FibonacciHeapNode<T> node, double key)
    {
        node.key = key;
        if (minNode != null)
        {
            node.left = minNode;
            node.right = minNode.right;
            minNode.right = node;
            minNode.right.left = node;

            if (minNode.key > key)
            {
                minNode = node;
            }
        }
        else
        {
            minNode = node;
        }
        numNodes++;
    }

    public FibonacciHeapNode<T> min()
    {
        return minNode;
    }

    public FibonacciHeapNode<T> removeMin()
    {
        FibonacciHeapNode<T> min = minNode;
        if (min != null)
        {
            int numKids = min.degree;
            FibonacciHeapNode<T> child = min.child;
            FibonacciHeapNode<T> temRight;

            while (numKids > 0)
            {
                temRight = child.right;
                child.left.right = child.right;
                child.right.left = child.left;

                child.left = minNode;
                child.right = minNode.right;
                minNode.right = child;
                minNode.right.left = child;

                child.parent = null;
                child = temRight;
                numKids--;
            }
            min.left.right = min.right;
            min.right.left = min.left;
            if (min.right == min)
            {
                minNode = null;
            }
            else
            {
                minNode = min.right;
                consolidate();
            }
            numNodes--;
        }
        return min;
    }

    public int size()
    {
        return numNodes;
    }

    public static <T> FibonacciHeap<T> union(FibonacciHeap<T> heap1, FibonacciHeap<T> heap2)
    {
        FibonacciHeap<T> result = new FibonacciHeap<T>();
        if (heap1 != null && heap2 != null)
        {
            result.minNode = heap1.minNode;
            if (result.minNode != null)
            {
                if (heap2.minNode != null)
                {
                    result.minNode.right.left = heap2.minNode.left;
                    heap2.minNode.left.right = result.minNode.right;
                    result.minNode.right = heap2.minNode;
                    heap2.minNode.left = result.minNode;

                    if (heap2.minNode.key < result.minNode.key)
                    {
                        result.minNode = heap2.minNode;
                    }
                }
                else
                {
                    result.minNode = heap2.minNode;
                }
                result.numNodes = heap1.numNodes + heap2.numNodes;
            }
        }

        return result;
    }

    private void cascadingCut(FibonacciHeapNode<T> node)
    {
        FibonacciHeapNode<T> parent = node.parent;
        if (parent != null)
        {
            if (!node.childCut)
            {
                node.childCut = true;
            }
            else
            {
                cut(node, parent);
                cascadingCut(parent);
            }
        }
    }

    private void consolidate()
    {
        int arraySize = ((int) Math.floor(Math.log(numNodes) * oneOverLogPhi)) + 1;
        List<FibonacciHeapNode<T>> array = new ArrayList<FibonacciHeapNode<T>>(arraySize);
        for (int i = 0; i < array.size(); i++)
        {
            array.add(null);
        }
        int numRoots = 0;
        FibonacciHeapNode<T> node = minNode;

        if (node != null)
        {
            numRoots++;
            node = node.right;
            while (node != minNode)
            {
                numRoots++;
                node = node.right;
            }
        }

        while (numRoots > 0)
        {
            int d = node.degree;
            FibonacciHeapNode<T> next = node.right;
            for (;;)
            {
                FibonacciHeapNode<T> y = array.get(d);
                if (y == null)
                {
                    break;
                }

                if (node.key > y.key)
                {
                    FibonacciHeapNode<T> tem = y;
                    y = node;
                    node = tem;
                }

                link(y, node);

                array.set(d, null);
                d++;
            }
            array.set(d, node);
            node = next;
            numRoots--;
        }
        minNode = null;
        for (int i = 0; i < arraySize; i++)
        {
            FibonacciHeapNode<T> y = array.get(i);
            if (y == null)
            {
                continue;
            }
            if (minNode != null)
            {
                y.left.right = y.right;
                y.right.left = y.left;

                y.left = minNode;
                y.right = minNode.right;
                minNode.right = y;
                y.right.left = y;

                if (y.key < minNode.key)
                {
                    minNode = y;
                }
            }
            else
            {
                minNode = y;
            }
        }
    }

    private void cut(FibonacciHeapNode<T> x, FibonacciHeapNode<T> y)
    {
        x.right.left = x.left;
        x.left.right = x.right;
        y.degree--;
        if (y.child == x)
        {
            y.child = null;
        }
        x.left = minNode;
        x.right = minNode.right;
        minNode.right = x;
        x.right.left = x;

        x.parent = null;
        x.childCut = false;
    }

    private void link(FibonacciHeapNode<T> y, FibonacciHeapNode<T> x)
    {
        y.left.right = y.right;
        y.right.left = y.left;

        y.parent = x;
        if (x.child == null)
        {
            x.child = y;
            y.right = y;
            y.left = y;
        }
        else
        {
            y.left = x.child;
            y.right = x.child.right;
            x.child.right = y;
            y.right.left = y;
        }

        x.degree++;
        y.childCut = false;
    }

}
