package DataStructure;

import java.util.*;

public class FibonacciHeap
{
    private FibonacciHeapNode minNode;
    private HashMap<FibonacciHeapNode, Integer> hashMap; //use for check element whether in the structure

    public FibonacciHeap()
    {
        hashMap = new HashMap<FibonacciHeapNode, Integer>();
    }

    public boolean isEmpty()
    {
        return minNode == null;
    }

    public void insert(FibonacciHeapNode node, int key)
    {
        node.weight = key;
        if (minNode != null)
        {
            node.left = minNode;
            node.right = minNode.right;
            minNode.right = node;
            node.right.left = node;

            //change pointer
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
    }

    public void decreaseKey(FibonacciHeapNode node, int key)
    {
        if (key > node.weight)
        {
            throw new IllegalArgumentException();
        }
        node.weight = key;
        FibonacciHeapNode parent = node.parent;
        if ((parent != null) && (node.weight < parent.weight))
        {
            cut(node, parent);
            cascadingCut(parent);
        }

        //change the point to the min
        if (node.weight < minNode.weight)
        {
            minNode = node;
        }
    }

    /*
            //using pairwise combine..
            public FibonacciHeapNode extractMin()
            {
                if (minNode == null)
                {
                    throw new NoSuchElementException();
                }
                FibonacciHeapNode min = minNode;

                //1.only one root node
                if (minNode.right == minNode)
                {
                    if (minNode.child != null)
                    {
                        //update number of root node in the heap
                        numNodes = minNode.degree;
                        FibonacciHeapNode tem = min;
                        //change root pointer
                        //note that now we have a sibling linked list in the heap.. we should pairwise combine it.
                        minNode = minNode.child;
                        minNode.parent = null;

                        isolateNode(tem);
                        pairwiseCombine();
                        updateMinNode();
                    }
                    else
                    {//no child and only one node in the heap
                        minNode = null;
                        numNodes = 0;
                    }
                    hashMap.remove(min);
                    return min;
                }
                else
                {
                    //2. more than one node in the root linked list...
                    //TODO assert: check the maintaince of the FibonacciHeap..
                    numNodes = minNode.degree + numNodes - 1;

                    FibonacciHeapNode child = minNode.child;
                    FibonacciHeapNode temMin = minNode;

                    minNode.left.right = minNode.right;
                    minNode.right.left = minNode.left;
                    minNode = minNode.right;
                    isolateNode(temMin);
                    temMin = null;
                    if (child != null)
                    {
                        child.parent = null;
                        for (FibonacciHeapNode node = child.right; node != child; node = node.right)
                            node.parent = null;
                        FibonacciHeapNode tem = minNode.right;
                        child.right.left = minNode;
                        minNode.right = child.right;
                        child.right = tem;
                        tem.left = child;
                    }
                    pairwiseCombine();
                    updateMinNode();
                    hashMap.remove(min);
                    return min;
                }
            }
    */

    public FibonacciHeapNode extractMin()
    {
        if (minNode == null)
        {
            return null;
        }
        FibonacciHeapNode oldMinNode = minNode;
        if (minNode.right == minNode)
        {
            minNode = null;
        }
        else
        {
            minNode.right.left = minNode.left;
            minNode.left.right = minNode.right;
            minNode = minNode.right;
        }

        FibonacciHeapNode child = oldMinNode.child;
        if (child != null)
        {
            child.parent = null;
            FibonacciHeapNode sibling = child.right;
            while (sibling != child)
            {
                sibling.parent = null;
                sibling = sibling.right;
            }
            oldMinNode.child = null;
        }

        minNode = addToRoot(minNode, child);
        if (minNode != null)
        {
            pairwiseCombine();
        }
        return oldMinNode == null ? null : oldMinNode;
    }

    private FibonacciHeapNode addToRoot(FibonacciHeapNode node1, FibonacciHeapNode node2)
    {
        if (node1 == null)
        {
            return node2;
        }
        if (node2 == null)
            return node1;
        FibonacciHeapNode temRightNode = node1.right;
        node1.right.left = node2;
        node2.right.left = node1;
        node1.right = node2.right;
        node2.right = temRightNode;
        return node1.getWeight() <= node2.getWeight() ? node1 : node2;
    }

    private void pairwiseCombine()
    {
        HashMap<Integer, FibonacciHeapNode> rootNodes = new HashMap<Integer, FibonacciHeapNode>();
        Queue<FibonacciHeapNode> queue = new LinkedList<FibonacciHeapNode>();
        FibonacciHeapNode it = minNode;
        do
        {
            FibonacciHeapNode next = it.right;
            it.left = it;
            it.right = it;
            queue.add(it);
            it = next;
        }
        while (it != minNode);
        while (!queue.isEmpty())
        {
            FibonacciHeapNode rootNode = queue.poll();
            FibonacciHeapNode fromHashMap = rootNodes.remove(rootNode.degree);
            if (fromHashMap == null)
            {
                rootNodes.put(rootNode.degree, rootNode);
            }
            else
            {
                if (fromHashMap.getWeight() > rootNode.getWeight())
                {
                    FibonacciHeapNode newNode = meld(rootNode, fromHashMap);
                    queue.add(newNode);
                }
                else
                {
                    FibonacciHeapNode newNode = meld(fromHashMap, rootNode);
                    queue.add(newNode);
                }
            }
        }
        reconnectHeap(rootNodes);
    }

    private void reconnectHeap(HashMap<Integer, FibonacciHeapNode> rootNodes)
    {
        Iterator<FibonacciHeapNode> iterator = rootNodes.values().iterator();
        FibonacciHeapNode first;
        if (iterator.hasNext())
        {
            first = iterator.next();
            FibonacciHeapNode last = first;
            FibonacciHeapNode min = first;
            while (iterator.hasNext())
            {
                last.right = iterator.next();
                last.right.left = last;
                last = last.right;
                if (last.getWeight() < min.getWeight())
                {
                    min = last;
                }
            }
            last.right = first;
            first.left = last;
            minNode = min;
        }
    }

    public boolean contains(FibonacciHeapNode heapNode)
    {
        if (hashMap.containsKey(heapNode))
        {
            return true;
        }
        return false;
    }

    private void cascadingCut(FibonacciHeapNode y)
    {
        FibonacciHeapNode z = y.parent;

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

    private void cut(FibonacciHeapNode x, FibonacciHeapNode y)
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

    //meld two heap with same degree
    //TODO check this method..
    private FibonacciHeapNode meld(FibonacciHeapNode parent, FibonacciHeapNode child)
    {
        if (parent == null)
        {
            return child;
        }
        if (child == null)
        {
            return parent;
        }
        child.parent = parent;
        parent.child = addToRoot(parent.child, child);
        parent.degree++;
        return parent;
    }
    /*
        private void updateMinNode()
        {
            FibonacciHeapNode runner = minNode.right;
            FibonacciHeapNode temp = minNode;
            while (runner != minNode)
            {
                if (runner.getWeight() < minNode.getWeight())
                {
                    temp = runner;
                    runner = runner.right;
                }
            }
            minNode = temp;
        }

        private void isolateNode(FibonacciHeapNode node)
        {
            node.child = null;
            node.parent = null;
            node.left = node;
            node.right = node;
        }
        */
}
