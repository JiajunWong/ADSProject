package DataStructure;

public class FibonacciHeapNode
{
    int index;
    FibonacciHeapNode child;
    FibonacciHeapNode left;
    FibonacciHeapNode parent;
    FibonacciHeapNode right;

    boolean childCut;
    int weight;
    int degree;

    public FibonacciHeapNode(int index)
    {
        right = this;
        left = this;
        this.index = index;
    }

    public final int getWeight()
    {
        return weight;
    }

    public final int getIndex()
    {
        return index;
    }
}
