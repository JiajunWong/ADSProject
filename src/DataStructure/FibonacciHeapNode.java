package DataStructure;

public class FibonacciHeapNode<T>
{
    public T data;
    public FibonacciHeapNode<T> left;
    public FibonacciHeapNode<T> right;
    public FibonacciHeapNode<T> child;
    public FibonacciHeapNode<T> parent;
    
    public double key;
    public boolean childCut;
    public int degree;
    
    public FibonacciHeapNode(T data){
        right = this;
        left = this;
        this.data = data;
    }

    public FibonacciHeapNode(T data, double key){
        this(data);
        this.key = key;
    }
    
    public FibonacciHeapNode(){
        
    }
    
    public final double getKey(){
        return key;
    }
    
    public final T getData(){
        return data;
    }
}
