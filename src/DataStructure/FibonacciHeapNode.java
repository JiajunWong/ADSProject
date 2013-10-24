package DataStructure;

public class FibonacciHeapNode<T>
{
    T index;
    FibonacciHeapNode<T> child;
    FibonacciHeapNode<T> left;
    FibonacciHeapNode<T> parent;
    FibonacciHeapNode<T> right;

    boolean childCut;
    int weight;
    int degree;

    public FibonacciHeapNode(T index)
    {
        right = this;
        left = this;
        this.index = index;
    }

    public final int getWeight()
    {
        return weight;
    }

    public final T getIndex()
    {
        return index;
    }
}
