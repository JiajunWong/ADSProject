package DataStructure;

public class FibonacciHeapNode<T>
{
    T data;
    FibonacciHeapNode<T> child;
    FibonacciHeapNode<T> left;
    FibonacciHeapNode<T> parent;
    FibonacciHeapNode<T> right;

    boolean childCut;
    int key;
    int degree;

    public FibonacciHeapNode(T data)
    {
        right = this;
        left = this;
        this.data = data;
    }

    public final int getKey()
    {
        return key;
    }

    public final T getData()
    {
        return data;
    }
}
