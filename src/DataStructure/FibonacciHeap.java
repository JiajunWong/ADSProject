package DataStructure;

import java.util.*;

public class FibonacciHeap<T>
{
    private FibonacciHeapNode<T> minNode;
    private int numNodes;
    private HashMap<FibonacciHeapNode<T>, T> hashMap;

    public FibonacciHeap()
    {
        hashMap = new HashMap<FibonacciHeapNode<T>, T>();
    }

    public boolean isEmpty()
    {
        return minNode == null;
    }

    public void decreaseKey(FibonacciHeapNode<T> node, int key)
    {
        if (key > node.weight)
        {
            throw new IllegalArgumentException();
        }
        node.weight = key;
        FibonacciHeapNode<T> parent = node.parent;
        if ((parent != null) && (node.weight < parent.weight))
        {
            cut(node, parent);
            cascadingCut(parent);
        }

        if (node.weight < minNode.weight)
        {
            minNode = node;
        }
    }

    public void insert(FibonacciHeapNode<T> node, int key)
    {
        node.weight = key;
        if (minNode != null)
        {
            node.left = minNode;
            node.right = minNode.right;
            minNode.right = node;
            node.right.left = node;

            if (key < minNode.weight)
            {
                minNode = node;
            }
        }
        else
        {
            minNode = node;
        }
        hashMap.put(node, node.getIndex());
        numNodes++;
    }

    public boolean contains(FibonacciHeapNode<T> heapNode)
    {
        if (hashMap.containsKey(heapNode))
        {
            return true;
        }
        return false;
    }

    private void cascadingCut(FibonacciHeapNode<T> y)
    {
        FibonacciHeapNode<T> z = y.parent;

        if (z != null)
        {

            if (!y.childCut)
            {
                y.childCut = true;
            }
            else
            {
                cut(y, z);
                cascadingCut(z);
            }
        }
    }

    private void pairCombine()
    {
        final double oneOverLogPhi = 1.0 / Math.log((1.0 + Math.sqrt(5.0)) / 2.0);
        int arraySize = ((int) Math.floor(Math.log(numNodes) * oneOverLogPhi)) + 1;

        List<FibonacciHeapNode<T>> array = new ArrayList<FibonacciHeapNode<T>>(arraySize);

        for (int i = 0; i < arraySize; i++)
        {
            array.add(null);
        }

        int numRoots = 0;
        FibonacciHeapNode<T> x = minNode;

        if (x != null)
        {
            numRoots++;
            x = x.right;

            while (x != minNode)
            {
                numRoots++;
                x = x.right;
            }
        }

        while (numRoots > 0)
        {
            int d = x.degree;
            FibonacciHeapNode<T> next = x.right;

            for (;;)
            {
                FibonacciHeapNode<T> y = array.get(d);
                if (y == null)
                {
                    break;
                }
                if (x.weight > y.weight)
                {
                    FibonacciHeapNode<T> temp = y;
                    y = x;
                    x = temp;
                }

                link(y, x);

                array.set(d, null);
                d++;
            }
            array.set(d, x);
            x = next;
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

                if (y.weight < minNode.weight)
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
        x.left.right = x.right;
        x.right.left = x.left;
        y.degree--;

        if (y.child == x)
        {
            y.child = x.right;
        }

        if (y.degree == 0)
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

    public FibonacciHeapNode<T> extractMin()
    {
        FibonacciHeapNode<T> min = minNode;
        if (min != null)
        {
            int numKids = min.degree;
            FibonacciHeapNode<T> x = min.child;
            FibonacciHeapNode<T> tempRight;

            while (numKids > 0)
            {
                tempRight = x.right;
                x.left.right = x.right;
                x.right.left = x.left;
                x.left = minNode;
                x.right = minNode.right;
                minNode.right = x;
                x.right.left = x;

                x.parent = null;
                x = tempRight;
                numKids--;
            }
            min.left.right = min.right;
            min.right.left = min.left;

            if (min == min.right)
            {
                minNode = null;
            }
            else
            {
                minNode = min.right;
                pairCombine();
            }
            numNodes--;
        }
        hashMap.remove(min);
        return min;
    }

}
